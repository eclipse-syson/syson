/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.syson.sysml.linker;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.omg.sysml.lang.sysml.ConjugatedPortDefinition;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureChaining;
import org.omg.sysml.lang.sysml.Import;
import org.omg.sysml.lang.sysml.InvocationExpression;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.MembershipImport;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.NamespaceImport;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.RequirementUsage;
import org.omg.sysml.lang.sysml.Specialization;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.Type;
import org.omg.sysml.lang.sysml.VerificationCaseDefinition;
import org.omg.sysml.lang.sysml.VisibilityKind;
import org.omg.sysml.util.TypeUtil;

/**
 * SysMLv2 linker.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SysMLLinker {

    /**
     * A {@link Set} with the {@link VisibilityKind#PUBLIC}.
     */
    public static final Set<VisibilityKind> IMPORT = Collections.singleton(VisibilityKind.PUBLIC);

    /**
     * A {@link Set} with {@link VisibilityKind#PROTECTED} and {@link VisibilityKind#PUBLIC}.
     */
    public static final Set<VisibilityKind> SPECIALIZATION = Collections.unmodifiableSet(new LinkedHashSet<>(List.of(VisibilityKind.PROTECTED, VisibilityKind.PUBLIC)));

    /**
     * A {@link Set} with all {@link VisibilityKind}.
     */
    public static final Set<VisibilityKind> LOCAL = Collections.unmodifiableSet(new LinkedHashSet<>(VisibilityKind.VALUES));

    private static int statisticsNbPasses;

    /**
     * The global context {@link Notifier}.
     */
    private final Notifier globalContext;

    /**
     * The {@link Map} of links to resolve.
     */
    private Map<EObject, Map<EReference, Set<QualifiedName>>> toResolveMap;

    /**
     * The {@link Map} of unresolved links
     */
    private Map<EObject, Map<EReference, Set<QualifiedName>>> notResolvedMap = new LinkedHashMap<>();

    /**
     * The {@link MemberNameProvider}.
     */
    private final MemberNameProvider nameProvider;

    /**
     * The {@link MemberNameProvider}.
     */
    private final ContextNamespaceProvider contextProvider = new ContextNamespaceProvider(this);

    /**
     * The {@link SysMLProxySafeNavigator}.
     */
    private final SysMLProxySafeNavigator sysMLEMFNavigator = new SysMLProxySafeNavigator();

    /**
     * The {@link SysMLMetamodelHelper}.
     */
    private final SysMLMetamodelHelper sysMLMetamodelHelper = new SysMLMetamodelHelper();

    /**
     * The {@link DiagnosticBuilder}.
     */
    private final DiagnosticBuilder diagnosticBuilder = new DiagnosticBuilder();

    /**
     * The {@link RedefinitionResolver}.
     */
    private final RedefinitionResolver redefinitionResolver = new RedefinitionResolver(this, this.diagnosticBuilder, this.contextProvider);

    /**
     * Constructor.
     *
     * @param globalContext
     *            the global context
     * @param toResolveMap
     *            the {@link Map} of unresolved {@link QualifiedName}
     */
    public SysMLLinker(Notifier globalContext, Map<EObject, Map<EReference, Set<QualifiedName>>> toResolveMap) {
        this.globalContext = globalContext;
        this.toResolveMap = toResolveMap;
        this.nameProvider = new MemberNameProvider(this, this.toResolveMap);
    }

    /**
     * Gets the number of linking passes performed by the latest resolution.
     *
     * @return the number of linking passes
     */
    public static int getStatisticsNbPasses() {
        return statisticsNbPasses;
    }

    /**
     * Gets the {@link Set} of values for the given {@link EReference} for the given {@link EObject}.
     *
     * @param eObj
     *            the {@link EObject}
     * @param reference
     *            the {@link EReference}
     * @return the {@link Set} of values for the given {@link EReference} for the given {@link EObject}
     */
    public Set<Element> get(EObject eObj, EReference reference) {
        if (reference.isContainment()) {
            return this.eGet(eObj, reference);
        }

        Set<Element> res;
        Set<QualifiedName> toResolve = this.consumeQualifiedNamesToResolve(eObj, reference);
        if (toResolve == null) {
            res = this.eGet(eObj, reference);
        } else {
            Set<Element> resolvedValues = new LinkedHashSet<>();
            Set<QualifiedName> notResolved = new LinkedHashSet<>();
            for (QualifiedName qualifiedName : toResolve) {
                this.diagnosticBuilder.startDiagnosticContext();
                try {
                    QualifiedName longQualifiedName = qualifiedName.getLongNotation();
                    boolean global = longQualifiedName.isGlobal();
                    boolean contextFound = global;
                    Optional<Membership> resolved = Optional.empty();
                    if (global) {
                        // TODO resolve global for Redefinition.redefinedFeature: do we need to check if the resolved
                        // Membership is in a Namespace specialized by the context Namespace? The pilot performs no
                        // specific check in this case.
                        resolved = this.resolveGlobal(longQualifiedName);
                    } else {
                        Namespace context = this.contextProvider.getContextNamespace(eObj, reference);
                        contextFound = context != null;
                        if (contextFound) {
                            Set<VisibilityKind> visibilities = this.getResolveVisibilities(eObj, reference);
                            boolean descendantsOnly = this.isFollowingFeatureChaining(eObj, reference);
                            if (reference == SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature()) {
                                resolved = this.redefinitionResolver.resolveRedefinedReference((Redefinition) eObj, context, longQualifiedName, visibilities);
                            } else {
                                resolved = this.resolve(context, longQualifiedName, visibilities, Collections.emptySet(), descendantsOnly, new HashSet<>());
                            }
                        } else {
                            this.diagnosticBuilder.addMissingContext(eObj, reference, longQualifiedName);
                        }
                    }

                    if (resolved.isPresent()) {
                        Membership resolvedMembership = resolved.get();
                        this.addResolvedValue(reference, resolvedValues, resolvedMembership);
                    } else {
                        notResolved.add(longQualifiedName);
                        if (contextFound) {
                            this.diagnosticBuilder.addUnresolvedReference(eObj, reference, longQualifiedName);
                        }
                    }
                } finally {
                    this.diagnosticBuilder.endDiagnosticContext();
                }
            }

            res = this.setValues(eObj, reference, resolvedValues);
            if (!notResolved.isEmpty()) {
                this.notResolvedMap.computeIfAbsent(eObj, e -> new LinkedHashMap<>()).put(reference, notResolved);
            }
        }

        return res;
    }

    /**
     * Add the given resolved {@link Membership} to the {@link Set} of resolved values
     *
     * @param reference
     *            the {@link EReference} that has been resolved
     * @param resolvedValues
     *            the {@link Set} of resolved values to add the resolved {@link Membership} to
     * @param resolvedMembership
     *            the resolved {@link Membership}
     */
    private void addResolvedValue(EReference reference, Set<Element> resolvedValues, Membership resolvedMembership) {
        if (reference == SysMLPackage.eINSTANCE.getMembershipImport_ImportedMembership()) {
            resolvedValues.add(resolvedMembership);
        } else {
            resolvedValues.add(this.getMemberElement(resolvedMembership));
        }
    }

    /**
     * Sets the given resolved values the the given {@link EReference} of the given {@link EObject}.
     *
     * @param eObj
     *            the {@link EObject}
     * @param reference
     *            the {@link EReference}
     * @param resolvedValues
     *            the {@link Set} of values
     * @return the {@link Set} of set {@link Element}
     */
    private Set<Element> setValues(EObject eObj, EReference reference, Set<Element> resolvedValues) {
        Set<Element> res;
        res = new LinkedHashSet<>();
        if (!resolvedValues.isEmpty()) {
            EClass type = (EClass) reference.getEType();
            if (reference.isMany()) {
                Collection<Element> values = (Collection<Element>) eObj.eGet(reference);
                for (Element value : resolvedValues) {
                    if (type.isInstance(value)) {
                        values.add(value);
                        res.add(value);
                    } else {
                        // the resolved value is not assignable to the reference
                        this.diagnosticBuilder.addUnassignableValue(eObj, reference, value);
                    }
                }
            } else {
                Element value = resolvedValues.iterator().next();
                boolean hasBeenSet = false;
                if (type.isInstance(value)) {
                    // In some rare case, the MM do not allow values event if they match the expected MM type.
                    // In those cases it throws IllegalArgumentException such as in
                    // org.omg.sysml.lang.sysml.impl.FeatureMembershipImpl.setOwnedMemberElement
                    try {
                        eObj.eSet(reference, value);
                        res.add(value);
                        hasBeenSet = true;
                    } catch (IllegalArgumentException e) {
                        // Do nothing but hasBeenSet stays false
                    }

                }
                if (!hasBeenSet) {
                    // the resolved value is not assignable to the reference
                    this.diagnosticBuilder.addUnassignableValue(eObj, reference, value);
                }
            }
        }
        return res;
    }

    /**
     * Gets the {@link Set} of {@link VisibilityKind} needed to resolve the given {@link EReference} from the given
     * {@link EObject}.
     *
     * @param eObj
     *            the {@link EObject}
     * @param reference
     *            the {@link EReference}
     * @return the {@link Set} of {@link VisibilityKind} needed to resolve the given {@link EReference} from the given
     *         {@link EObject}
     */
    private Set<VisibilityKind> getResolveVisibilities(EObject eObj, EReference reference) {
        Set<VisibilityKind> res;

        if (this.isFollowingFeatureChaining(eObj, reference)) {
            res = IMPORT;
        } else {
            res = LOCAL;
        }

        return res;
    }

    /**
     * Tells if the given reference belongs to a {@link FeatureChaining} that follows another chaining relationship.
     * <p>
     * The parser stores unresolved chain segments on {@link FeatureChaining#getChainingFeature()}, not on the derived
     * {@link Feature#getChainingFeature()} reference. SysML 7.6.6 restricts these following segments to the public
     * members of the previous feature. Their lookup must consequently stay within that context instead of continuing in
     * an enclosing namespace.
     * </p>
     *
     * @param eObj
     *            the object carrying the reference
     * @param reference
     *            the reference to resolve
     * @return {@code true} if the reference belongs to a following feature-chain segment, <code>false</code> otherwise
     */
    private boolean isFollowingFeatureChaining(EObject eObj, EReference reference) {
        boolean res = false;

        // A textual feature chain is represented by FeatureChaining relationships. The derived
        // Feature::chainingFeature reference is not the reference carried by parser dangling links.
        if (reference == SysMLPackage.eINSTANCE.getFeatureChaining_ChainingFeature()) {
            EObject container = eObj.eContainer();
            if (container != null) {
                EStructuralFeature containingFeature = eObj.eContainingFeature();
                if (containingFeature.isMany()) {
                    Collection<?> values = (Collection<?>) container.eGet(containingFeature);
                    List<FeatureChaining> featureChainings = values.stream()
                            .filter(FeatureChaining.class::isInstance)
                            .map(FeatureChaining.class::cast).collect(Collectors.toList());
                    int index = featureChainings.indexOf(eObj);
                    if (index > 0) {
                        res = true;
                    }
                }
            }
        }

        return res;
    }

    /**
     * Gets the {@link Set} of values for the given {@link EReference} for the given {@link EObject}.
     *
     * @param eObj
     *            the {@link EObject}
     * @param reference
     *            the {@link EReference}
     * @return the {@link Set} of values for the given {@link EReference} for the given {@link EObject}
     */
    private Set<Element> eGet(EObject eObj, EReference reference) {
        Set<Element> res;

        if (reference.isMany()) {
            res = new LinkedHashSet<>((Collection<Element>) eObj.eGet(reference));
        } else {
            Element value = (Element) eObj.eGet(reference);
            if (value != null) {
                res = Collections.singleton(value);
            } else {
                res = Collections.emptySet();
            }
        }

        return res;
    }

    /**
     * Resolves globally (from root {@link Namespace}, parent {@link Resource}, parent {@link ResourceSet}) the given
     * {@link QualifiedName}.
     *
     * @param qualifiedName
     *            the {@link QualifiedName}
     * @return the {@link Optional} resolved {@link Membership}
     */
    private Optional<Membership> resolveGlobal(QualifiedName qualifiedName) {
        Optional<Membership> res;

        if (this.globalContext instanceof ResourceSet) {
            res = this.resolveGlobal((ResourceSet) this.globalContext, qualifiedName);
        } else if (this.globalContext instanceof Resource) {
            res = this.resolveGlobal((Resource) this.globalContext, qualifiedName);
        } else if (this.globalContext instanceof Namespace) {
            res = this.resolveGlobal((Namespace) this.globalContext, qualifiedName);
        } else {
            this.diagnosticBuilder.addUnknownGlobalContextKind(this.globalContext);
            res = Optional.empty();
        }

        return res;
    }

    /**
     * Consumes the {@link Set} of {@link QualifiedName} to resolve for the given {@link EObject} and
     * {@link EReference}.
     *
     * @param eObj
     *            the {@link EObject}
     * @param reference
     *            the {@link EReference}
     * @return the {@link Set} of {@link QualifiedName} to resolve for the given {@link EObject} and {@link EReference}
     */
    private Set<QualifiedName> consumeQualifiedNamesToResolve(EObject eObj, EReference reference) {
        Set<QualifiedName> res;

        Map<EReference, Set<QualifiedName>> map = this.toResolveMap.get(eObj);
        if (map != null) {
            res = map.remove(reference);
            if (res != null && map.isEmpty()) {
                this.toResolveMap.remove(eObj);
            }
        } else {
            res = null;
        }

        return res;
    }

    /**
     * Gets unresolved link {@link Map}.
     *
     * @return unresolved link {@link Map}
     */
    public Map<EObject, Map<EReference, Set<QualifiedName>>> getNotResolved() {
        return this.notResolvedMap;
    }

    /**
     * Gets the resolution {@link Diagnostic}.
     *
     * @return the resolution {@link Diagnostic}
     */
    public Diagnostic getDiagnostic() {
        return this.diagnosticBuilder.buildDiagnostic();
    }

    /**
     * Resolves the given {@link QualifiedName} globally from the given root {@link ResourceSet}
     *
     * @param resourceSet
     *            the root {@link ResourceSet} to resolve from
     * @param qualifiedName
     *            the {@link QualifiedName} to resolve
     * @return the {@link Optional} resolved {@link Membership}
     */
    private Optional<Membership> resolveGlobal(ResourceSet resourceSet, QualifiedName qualifiedName) {
        return resourceSet.getResources().stream()
                .map(r -> this.resolveGlobal(r, qualifiedName))
                .filter(o -> o.isPresent())
                .map(o -> o.get()).findFirst();
    }

    /**
     * Resolves the given {@link QualifiedName} globally from the given root {@link Resource}
     *
     * @param resource
     *            the root {@link Resource} to resolve from
     * @param qualifiedName
     *            the {@link QualifiedName} to resolve
     * @return the {@link Optional} resolved {@link Membership}
     */
    private Optional<Membership> resolveGlobal(Resource resource, QualifiedName qualifiedName) {
        return resource.getContents().stream()
                .filter(Namespace.class::isInstance)
                .map(Namespace.class::cast)
                .map(ns -> this.resolveGlobal(ns, qualifiedName))
                .filter(o -> o.isPresent())
                .map(o -> o.get()).findFirst();
    }

    /**
     * Resolves the given {@link QualifiedName} globally from the given root {@link Namespace}
     *
     * @param root
     *            the root {@link Namespace} to resolve from
     * @param qualifiedName
     *            the {@link QualifiedName} to resolve
     * @return the {@link Optional} resolved {@link Membership}
     */
    private Optional<Membership> resolveGlobal(Namespace root, QualifiedName qualifiedName) {
        return this.resolve(root, qualifiedName, IMPORT, Collections.emptySet(), true, new HashSet<>());
    }

    /**
     * Resolves the {@link Membership} for the given {@link Namespace} and {@link QualifiedName}.
     *
     * @param namespace
     *            the context {@link Namespace}
     * @param qualifiedName
     *            the {@link QualifiedName} to resolve
     * @param visibilities
     *            the {@link Set} of allowed {@link VisibilityKind}
     * @param redefined
     *            the {@link Set} or {@link Redefinition redefined} {@link Membership}
     * @param descendent
     *            <code>true</code> if the resolution is restricted to descendent (won't look into parent
     *            {@link Namespace})
     * @return the {@link Optional} resolved {@link Membership}
     */
    public Optional<Membership> resolve(Namespace namespace, QualifiedName qualifiedName, Set<VisibilityKind> visibilities, Set<Membership> redefined, boolean descendent, Set<Namespace> visited) {
        if (visited.contains(namespace)) {
            return Optional.empty();
        }
        visited.add(namespace);

        // TODO optimize with a cache of Optional<Membership> per visibility

        Set<Membership> newRedefined = this.redefinitionResolver.getRedefinedMemberships(namespace, redefined);

        // start by local lookup
        Optional<Membership> res = this.resolveLocally(namespace, qualifiedName, visibilities, newRedefined, visited);

        if (res.isEmpty()) {
            this.diagnosticBuilder.addNotResolvedLocally(namespace, qualifiedName);
            res = this.resolveSpecializations(namespace, qualifiedName, newRedefined, visited);
            if (res.isEmpty()) {
                this.diagnosticBuilder.addNotResolvedViaSpecializations(namespace, qualifiedName);
                res = this.resolveImports(namespace, qualifiedName, visibilities, visited);
                if (res.isEmpty()) {
                    this.diagnosticBuilder.addNotResolvedViaImports(namespace, qualifiedName);
                    if (res.isEmpty() && !descendent) {
                        res = this.resolveInParentOrGlobal(namespace, qualifiedName, visibilities, descendent);
                    }
                }
            }
        }

        // Special case for VerificationCaseDefinition where the content of its RequirementUsage can be referenced
        // directly
        // SysMLv2 8.3.24.4
        // The RequirementUsages verified by this VerificationCaseUsage, which are the verifiedRequirements
        // of all RequirementVerificationMemberships of the objectiveRequirement.
        if (res.isEmpty() && namespace instanceof VerificationCaseDefinition verificationCaseDefinition) {
            res = this.resolveRequirementUsage(qualifiedName, visibilities, newRedefined, verificationCaseDefinition);
        }

        return res;
    }

    /**
     * Looks up a name without assigning a reference, establishing the diagnostic context required by resolution.
     *
     * @param namespace
     *            the namespace from which to start normal name resolution
     * @param qualifiedName
     *            the name to look up
     * @return the matching membership, or an empty optional when the name cannot be resolved
     */
    public Optional<Membership> resolve(Namespace namespace, QualifiedName qualifiedName) {
        this.diagnosticBuilder.startDiagnosticContext();
        try {
            if (qualifiedName.isGlobal()) {
                return this.resolveGlobal(qualifiedName.getLongNotation());
            }
            return this.resolve(namespace, qualifiedName.getLongNotation(), LOCAL, Collections.emptySet(), false, new HashSet<>());
        } finally {
            this.diagnosticBuilder.endDiagnosticContext();
        }
    }

    /**
     * Resolves in the owned {@link RequirementUsage} of the given {@link VerificationCaseDefinition}.
     *
     * @param qualifiedName
     *            the {@link QualifiedName} to resolve
     * @param visibilities
     *            the {@link Set} of allowed {@link VisibilityKind}
     * @param redefined
     *            the set of redefined {@link Membership} we can't resolve against
     * @param verificationCaseDefinition
     *            the {@link VerificationCaseDefinition}
     * @return the {@link Optional} resolved {@link Membership}
     */
    private Optional<Membership> resolveRequirementUsage(QualifiedName qualifiedName, Set<VisibilityKind> visibilities, Set<Membership> redefined,
            VerificationCaseDefinition verificationCaseDefinition) {
        Optional<Membership> res = Optional.empty();
        for (RequirementUsage requirementUsage : verificationCaseDefinition.getOwnedRequirement()) {
            res = this.resolve(requirementUsage, qualifiedName, visibilities, redefined, true, new HashSet<>());
            if (res.isPresent()) {
                break;
            }
        }
        return res;
    }

    /**
     * Resolves in the parent {@link Namespace} of the given {@link Namespace} if any,
     * {@link #resolveGlobal(Resource, QualifiedName) resolve globally} otherwise.
     *
     * @param namespace
     *            the context {@link Namespace}
     * @param qualifiedName
     *            the {@link QualifiedName} to resolve
     * @param visibilities
     *            the {@link Set} of allowed {@link VisibilityKind}
     * @param descendent
     *            <code>true</code> if the resolution is restricted to descendent (won't look into parent
     *            {@link Namespace})
     * @return the {@link Optional} resolved {@link Membership}
     */
    public Optional<Membership> resolveInParentOrGlobal(Namespace namespace, QualifiedName qualifiedName, Set<VisibilityKind> visibilities,
            boolean descendent) {
        Optional<Membership> result;
        Namespace parentNamespace = this.sysMLEMFNavigator.getContaining(Namespace.class, namespace.eContainer());
        if (parentNamespace != null) {
            // Restart from the containing Namespace.
            result = this.resolve(parentNamespace, qualifiedName, visibilities, new HashSet<>(), descendent, new HashSet<>());
        } else if (namespace.eResource() != null) {
            Resource rootResource = namespace.eResource();
            ResourceSet rootResourceSet = rootResource.getResourceSet();
            if (rootResourceSet == null) {
                result = this.resolveGlobal(rootResource, qualifiedName);
            } else {
                result = this.resolveGlobal(rootResourceSet, qualifiedName);
            }
        } else {
            this.diagnosticBuilder.addNoOtherGlobalNamespaces(namespace, qualifiedName, visibilities, new HashSet<>());
            result = Optional.empty();
        }
        return result;
    }

    /**
     * Resolves the given {@link QualifiedName} from imports of the given {@link Namespace}.
     *
     * @param namespace
     *            the {@link Namespace} to resolve from
     * @param qualifiedName
     *            the {@link QualifiedName} to resolve
     * @param visibilities
     *            the {@link Set} of allowed {@link VisibilityKind}
     * @return the {@link Optional} resolved {@link Membership}
     */
    private Optional<Membership> resolveImports(Namespace namespace, QualifiedName qualifiedName, Set<VisibilityKind> visibilities, Set<Namespace> visited) {
        Optional<Membership> res = Optional.empty();

        // TODO optimize with Import cache
        List<Import> imports = namespace.getOwnedRelationship().stream()
                .filter(Import.class::isInstance)
                .map(Import.class::cast)
                .filter(i -> visibilities.contains(i.getVisibility()))
                .collect(Collectors.toList());
        resolved: for (Import imp : imports) {
            EReference importFeature = this.sysMLMetamodelHelper.getImportFeature(imp);
            Set<Element> importedElements = this.get(imp, importFeature);
            boolean isRecursive = imp.isRecursive();
            boolean isImportAll = imp.isImportAll();
            for (Element importedElement : importedElements) {
                if (importedElement instanceof Membership) {
                    res = this.resolveMembershipImport(namespace, importedElement, qualifiedName, isRecursive, isImportAll, visited);
                    if (res.isPresent()) {
                        break resolved;
                    }
                } else if (importedElement instanceof Namespace) {
                    res = this.resolveNamespaceImport((Namespace) importedElement, qualifiedName, isRecursive, isImportAll, visited, new HashSet<>());
                    if (res.isPresent()) {
                        break resolved;
                    }
                } else {
                    // the imported element has not been resolved to a Membership nor a Namespace so we can't continue
                    // this resolution
                    this.diagnosticBuilder.addImportedElementNotResolved(namespace, importedElement, qualifiedName, visibilities);
                }
            }
        }

        return res;
    }

    /**
     * Resolves the {@link MembershipImport} of the given {@link Element}. Recursive imports use the visibility rules of
     * KerML 1.0, section 8.3.2.4.4, {@code MembershipImport::importedMemberships(excluded)}.
     *
     * @param namespace
     *            the {@link Namespace} to resolve from
     * @param importedElement
     *            the imported {@link Element}
     * @param qualifiedName
     *            the {@link QualifiedName} to resolve
     * @param isRecursive
     *            {@link MembershipImport#isRecursive()}
     * @param isImportAll
     *            whether to include memberships regardless of visibility during recursive import
     * @return the {@link Optional} resolved {@link Membership}
     */
    private Optional<Membership> resolveMembershipImport(Namespace namespace, Element importedElement, QualifiedName qualifiedName, boolean isRecursive, boolean isImportAll, Set<Namespace> visited) {
        Optional<Membership> res = Optional.empty();

        Membership importedMembership = (Membership) importedElement;
        Set<String> memberNames = this.getMemberNames(importedMembership);
        String firstSegment = qualifiedName.getFirstSegment();
        if (memberNames.contains(firstSegment)) {
            if (qualifiedName.isLocal()) {
                res = Optional.of(importedMembership);
            } else {
                QualifiedName nextQualifiedName = qualifiedName.removeFirstSegment();
                Element element = this.getMemberElement(importedMembership);
                if (element instanceof Namespace) {
                    res = this.resolve((Namespace) element, nextQualifiedName, IMPORT, Collections.emptySet(), true, visited);
                } else {
                    // no Namespace to resolve remaining segments
                    this.diagnosticBuilder.addImportedElementNotNamespace(namespace, importedMembership, qualifiedName);
                }
            }
        } else if (isRecursive) {
            Element element = this.getMemberElement(importedMembership);
            if (element instanceof Namespace) {
                res = this.resolveNamespaceImport((Namespace) element, qualifiedName, true, isImportAll, visited, new HashSet<>());
            } else {
                // recursive import of something that is not a Namespace
                this.diagnosticBuilder.addRecursiveImportNotNamespace(namespace, importedMembership, qualifiedName);
            }
        }
        return res;
    }

    /**
     * Gets the {@link Set} of member names for the given {@link Element} (member name, member short name).
     *
     * @param membership
     *            the tested membership
     * @return the {@link Set} of member names for the given {@link Element} (member name, member short name)
     */
    private Set<String> getMemberNames(Membership membership) {
        return this.nameProvider.getMemberNames(membership);
    }

    /**
     * Resolves a {@link NamespaceImport} of the given {@link QualifiedName} in the given imported {@link Namespace}.
     * KerML 1.0, section 8.3.2.4.6, {@code NamespaceImport::importedMemberships(excluded)}, delegates to
     * {@code visibleMemberships(excluded, isRecursive, isImportAll)}. Per sections 8.3.2.4.2 and 8.3.2.4.5,
     * {@code isImportAll} includes memberships regardless of visibility, including privately imported memberships.
     *
     * @param namespace
     *            the imported {@link Namespace}
     * @param qualifiedName
     *            the {@link QualifiedName} to resolve
     * @param recursive
     *            {@link NamespaceImport#isRecursive()}
     * @param isImportAll
     *            whether to include memberships regardless of visibility
     * @return the {@link Optional} resolved {@link Membership}
     */
    private Optional<Membership> resolveNamespaceImport(Namespace namespace, QualifiedName qualifiedName, boolean recursive, boolean isImportAll, Set<Namespace> visited,
            Set<Namespace> visitedImported) {
        Set<VisibilityKind> visibilities;
        if (isImportAll) {
            visibilities = LOCAL;
        } else {
            visibilities = IMPORT;
        }
        Optional<Membership> res = this.resolve(namespace, qualifiedName, visibilities, Collections.emptySet(), true, visited);

        if (recursive && res.isEmpty()) {
            if (!visitedImported.add(namespace)) {
                return Optional.empty();
            }

            List<Membership> memberships = this.getMemberships(namespace, visibilities).collect(Collectors.toList());
            for (Membership membership : memberships) {
                Element element = this.getMemberElement(membership);
                if (element instanceof Namespace) {
                    res = this.resolveNamespaceImport((Namespace) element, qualifiedName, recursive, isImportAll, visited, visitedImported);
                    if (res.isPresent()) {
                        break;
                    }
                }
            }
        }

        return res;
    }

    /**
     * Resolves via the {@link Specialization} of the given {@link Namespace}.
     *
     * @param namespace
     *            the context {@link Namespace}
     * @param qualifiedName
     *            the {@link QualifiedName} to resolve
     * @param redefined
     *            the {@link Set} of redefined {@link Membership} we can't resolve against
     * @param visited
     *            the {@link Set} of already visited {@link Namespace}
     * @return the {@link Optional} resolved {@link Membership}
     */
    public Optional<Membership> resolveSpecializations(Namespace namespace, QualifiedName qualifiedName, Set<Membership> redefined, Set<Namespace> visited) {
        Optional<Membership> res = Optional.empty();
        // TODO optimize with Specialization cache
        List<Specialization> specializations = namespace.getOwnedRelationship().stream()
                .filter(Specialization.class::isInstance)
                .map(Specialization.class::cast)
                .collect(Collectors.toList());
        Set<Membership> candidates = new LinkedHashSet<>();
        for (Specialization specialization : specializations) {
            EReference specializationFeature = this.sysMLMetamodelHelper.getSpecializationFeature(specialization);
            Set<Element> specializedElements = this.get(specialization, specializationFeature);
            for (Element specializedElement : specializedElements) {
                if (specializedElement instanceof Namespace) {
                    Namespace specializedNamespace = (Namespace) specializedElement;
                    if (specializedElement instanceof ConjugatedPortDefinition) {
                        // getOriginalPortDefinition()
                        specializedNamespace = (Namespace) specializedElement.getOwningMembership().getOwningRelatedElement();
                    }
                    Set<VisibilityKind> visibilities;
                    if (qualifiedName.isLocal()) {
                        visibilities = SPECIALIZATION;
                    } else {
                        visibilities = Collections.singleton(VisibilityKind.PUBLIC);
                    }
                    Optional<Membership> specializationResolved = this.resolve(specializedNamespace, qualifiedName, visibilities, redefined, true, visited);
                    specializationResolved.ifPresent(candidates::add);
                } else {
                    // specializing something that is not a Namespace
                    this.diagnosticBuilder.addSpecializedElementNotNamespace(namespace, specializedElement, qualifiedName);
                }
            }
        }

        // KerML 8.3.3.1.10 inheritedMemberships/removeRedefinedFeatures removes redefined
        // features across all general types. A later specialization can redefine an earlier match.
        res = this.redefinitionResolver.removeRedefinedMemberships(candidates).stream().findFirst();

        // Try the terminal feature of a chain before consulting implicit general types.
        if (res.isEmpty() && namespace instanceof Feature feature) {
            res = this.resolveThroughFeatureChain(qualifiedName, redefined, visited, feature);
        }
        if (res.isEmpty() && namespace instanceof Feature feature) {
            Namespace resultContext = this.contextProvider.getResultContext(feature);
            if (resultContext != null && resultContext != feature) {
                res = this.resolve(resultContext, qualifiedName, SPECIALIZATION, redefined, true, visited);
            }
        }
        if (res.isEmpty() && namespace instanceof Type type) {
            res = this.resolveSpecializationsViaImplicit(qualifiedName, redefined, visited, type);
        }

        return res;
    }

    /**
     * Resolves the given name through the terminal feature of the context type's feature chain. KerML 1.0, section
     * 8.3.3.3.4, {@code deriveFeatureFeatureTarget} identifies the last chaining feature as the {@code featureTarget}.
     * {@code Feature::supertypes(excludeImplied)} includes that target when it differs from the feature itself,
     * allowing inherited members to be resolved through it. The chain target is obtained through the linker so pending
     * references can be resolved without relying on implicit general-type information.
     *
     * @param qualifiedName
     *            the name to resolve
     * @param redefined
     *            the redefined memberships excluded from resolution
     * @param visited
     *            the namespaces already visited during resolution
     * @param feature
     *            the context {@link Feature}
     * @return the resolved membership, or an empty optional when no distinct chain target or matching member is found
     */
    private Optional<Membership> resolveThroughFeatureChain(QualifiedName qualifiedName, Set<Membership> redefined, Set<Namespace> visited, Feature feature) {
        Optional<Membership> res = Optional.empty();
        Namespace chainedContext = this.contextProvider.getReferencedFeatureContext(feature);
        if (chainedContext != null && chainedContext != feature) {
            res = this.resolve(chainedContext, qualifiedName, SPECIALIZATION, redefined, true, visited);
        }
        return res;
    }

    /**
     * Resolves {@link Specialization} via implicit.
     *
     * @param qualifiedName
     *            the {@link QualifiedName}
     * @param redefined
     *            the {@link Set} of redefined {@link Membership} we can't resolve against
     * @param visited
     *            the {@link Set} of visited {@link Namespace}
     * @param type
     *            the context {@link Type}
     * @return the {@link Optional} resolved {@link Membership}
     */
    private Optional<Membership> resolveSpecializationsViaImplicit(QualifiedName qualifiedName, Set<Membership> redefined, Set<Namespace> visited, Type type) {
        Set<Membership> candidates = new LinkedHashSet<>();
        for (Type impliciteType : TypeUtil.getImplicitGeneralTypesFor(type)) {
            Optional<Membership> target = this.resolve(impliciteType, qualifiedName, SPECIALIZATION, redefined, true, visited);
            target.ifPresent(candidates::add);
        }
        // KerML 8.3.3.1.10 inheritedMemberships/removeRedefinedFeatures also applies to implied specializations.
        // For example, a snapshot inherits the localClock redefinition from Occurrence::suboccurrences
        // as well as the original feature from Occurrence (SysML 8.3.9.4).
        return this.redefinitionResolver.removeRedefinedMemberships(candidates).stream().findFirst();
    }

    /**
     * Resolves locally to the given context {@link Namespace}
     *
     * @param namespace
     *            the context {@link Namespace}
     * @param qualifiedName
     *            the {@link QualifiedName} to resolve
     * @param visibilities
     *            the {@link Set} of accepted {@link VisibilityKind}
     * @param redefined
     *            the {@link Set} of redefined Membership we can't resolve against
     * @param visited
     *            the {@link Set} of visited {@link Namespace}
     * @return the {@link Optional} resolved {@link Membership}
     */
    private Optional<Membership> resolveLocally(Namespace namespace, QualifiedName qualifiedName, Set<VisibilityKind> visibilities, Set<Membership> redefined, Set<Namespace> visited) {
        Optional<Membership> res = Optional.empty();

        Membership localMembership = this.getMemberships(namespace, visibilities, qualifiedName.getFirstSegment()).findFirst().orElse(null);
        if (localMembership != null) {
            if (redefined.contains(localMembership)) {
                this.diagnosticBuilder.addMembershipRedefined(namespace, localMembership, qualifiedName);
            } else {
                res = this.resolveLocalMembershipRemainingSegments(namespace, qualifiedName, visibilities, visited, localMembership);
            }
        }

        return res;
    }

    /**
     * Resolves the remaining segments of the given {@link QualifiedName} from the given locally resolved
     * {@link Membership}.
     *
     * @param namespace
     *            the context {@link Namespace}
     * @param qualifiedName
     *            the {@link QualifiedName}
     * @param visibilities
     *            the {@link Set} of accepted {@link VisibilityKind}
     * @param visited
     *            the {@link Set} of visited {@link Namespace}
     * @param localMembership
     *            the locally resolved {@link Membership}
     * @return the {@link Optional} resolved {@link Membership}
     */
    private Optional<Membership> resolveLocalMembershipRemainingSegments(Namespace namespace, QualifiedName qualifiedName, Set<VisibilityKind> visibilities, Set<Namespace> visited,
            Membership localMembership) {
        Optional<Membership> result;
        if (qualifiedName.isLocal()) {
            result = Optional.of(localMembership);
        } else {
            Element localResolved = this.getMemberElement(localMembership);
            if (localResolved instanceof Namespace localNamespace) {
                QualifiedName nextQualifiedName = qualifiedName.removeFirstSegment();
                // Visibilities should probably be public only.
                result = this.resolve(localNamespace, nextQualifiedName, visibilities, Collections.emptySet(), true, visited);
            } else {
                this.diagnosticBuilder.addLocalElementNotNamespace(namespace, localResolved, qualifiedName);
                result = Optional.empty();
            }
        }
        return result;
    }

    /**
     * Gets the {@link Stream} of {@link Membership} from the given {@link Namespace} that have a compatible visibility.
     *
     * @param namespace
     *            the {@link Namespace}
     * @param visibilities
     *            the {@link Set} of accepted {@link VisibilityKind}
     * @return the {@link Stream} of {@link Membership} from the given {@link Namespace} that have a compatible
     *         visibility
     */
    private Stream<Membership> getMemberships(Namespace namespace, Set<VisibilityKind> visibilities) {
        // ownedMembership is a DerivedEObjectEList backed by ownedRelationship. Name resolution in
        // the filters below can recursively link and extend that containment list, therefore a live
        // stream would fail fast with ConcurrentModificationException. Unlike the pilot scope, do
        // not materialize additional members here: this linker must preserve the parsed AST unless
        // a semantic computation already requires such a lazy transformation.
        return List.copyOf(namespace.getOwnedMembership()).stream()
                .filter(m -> visibilities.contains(m.getVisibility()));
    }

    /**
     * Gets the {@link Stream} of {@link Membership} from the given {@link Namespace} that have a compatible visibility
     * and name.
     *
     * @param namespace
     *            the {@link Namespace}
     * @param visibilities
     *            the {@link Set} of accepted {@link VisibilityKind}
     * @param name
     *            the name
     * @return the {@link Stream} of {@link Membership} from the given {@link Namespace} that have a compatible
     *         visibility and name
     */
    private Stream<Membership> getMemberships(Namespace namespace, Set<VisibilityKind> visibilities, String name) {
        return this.getMemberships(namespace, visibilities)
                .filter(m -> this.getMemberNames(m).contains(name));
    }

    /**
     * Resolves all links.
     */
    public void resolveAll() {
        boolean newResolved;
        do {
            statisticsNbPasses++;
            // this.clearCachesOf(this.globalContext);
            this.notResolvedMap = new LinkedHashMap<>();
            newResolved = this.resolveAllRedefinition();

            // at this point we can't resolve more redefines
            // lets look at features from the containing namespace see KerML paragraph 8.3.3.3.4
            this.resolveAllRedefinitionViaParentPackages();

            // resolve all remaining links
            while (!this.toResolveMap.isEmpty()) {
                Entry<EObject, Map<EReference, Set<QualifiedName>>> elementEntry = this.toResolveMap.entrySet().iterator().next();
                Entry<EReference, Set<QualifiedName>> referenceEntry = elementEntry.getValue().entrySet().iterator().next();
                if (!this.get(elementEntry.getKey(), referenceEntry.getKey()).isEmpty()) {
                    newResolved = true;
                }
            }

            this.toResolveMap = this.notResolvedMap;
        } while (newResolved);
        if (this.toResolveMap.isEmpty()) {
            // some errors might remain if a link is not resolved at some point in the pass and then resolved in the
            // same pass
            this.diagnosticBuilder.clear();
        }
    }

    /**
     * Resolves all {@link Redefinition} via {@link Feature} in parent {@link org.omg.sysml.lang.sysml.Package Package}
     * (they are attached to Anything).
     */
    private void resolveAllRedefinitionViaParentPackages() {
        for (Entry<EObject, Map<EReference, Set<QualifiedName>>> entry : new LinkedHashSet<>(this.toResolveMap.entrySet())) {
            EObject owner = entry.getKey();
            if (owner instanceof Redefinition && entry.getValue().containsKey(SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature())
                    && !(this.sysMLEMFNavigator.getRedefinitionOwningType((Redefinition) owner) instanceof InvocationExpression)
                    && ((Redefinition) owner).getOwningFeature().getOwningNamespace() != null) {
                QualifiedName qualifiedName = this.consumeQualifiedNamesToResolve(owner, SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature()).iterator().next();
                Optional<Membership> membership = this.resolveAgainstFeatureInContainingNamespace(((Redefinition) owner).getOwningFeature().getOwningNamespace().getOwningNamespace(),
                        qualifiedName);
                if (membership.isPresent()) {
                    this.setValues(owner, SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature(), Collections.singleton(this.getMemberElement(membership.get())));
                } else {
                    this.notResolvedMap.computeIfAbsent(owner, e -> new LinkedHashMap<>()).put(SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature(), Collections.singleton(qualifiedName));
                    this.diagnosticBuilder.addUnresolvedRedefinitionViaContainingFeature(owner, SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature(), qualifiedName);
                }
            }
        }
    }

    /**
     * Resolves all {@link Redefinition}.
     *
     * @return <code>true</code> if at least a {@link Redefinition} has been resolved, <code>false</code> otherwise
     */
    private boolean resolveAllRedefinition() {
        boolean newResolved;
        do {
            this.diagnosticBuilder.clear();
            newResolved = false;
            if (new SemanticMetadataResolver(this.toResolveMap, this::get).resolveDependencies()) {
                newResolved = true;
            }
            // start with the redefines resolution
            for (Entry<EObject, Map<EReference, Set<QualifiedName>>> entry : new LinkedHashSet<>(this.toResolveMap.entrySet())) {
                if (entry.getKey() instanceof Redefinition && entry.getValue().containsKey(SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature())) {
                    if (!this.get(entry.getKey(), SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature()).isEmpty()) {
                        newResolved = true;
                    }
                }
            }
            this.toResolveMap.putAll(this.notResolvedMap);
            this.notResolvedMap = new LinkedHashMap<>();
        } while (newResolved);
        return newResolved;
    }

    /**
     * Resolves {@link Feature} that are in a parent {@link Namespace}. They are attached to the Base::Anything.
     *
     * @param namespace
     *            the owning {@link Namespace}
     * @param qualifiedName
     *            the {@link QualifiedName} to resolve
     */
    private Optional<Membership> resolveAgainstFeatureInContainingNamespace(Namespace namespace, QualifiedName qualifiedName) {
        Optional<Membership> res;

        if (namespace != null) {
            Optional<Membership> localMembership = this.getMemberships(namespace, LOCAL, qualifiedName.getFirstSegment())
                    .filter(m -> this.hasFeature(m)).findFirst();
            if (localMembership.isPresent()) {
                if (qualifiedName.isLocal()) {
                    res = localMembership;
                } else {
                    Feature feature = (Feature) this.getMemberElement(localMembership.get());
                    this.diagnosticBuilder.startDiagnosticContext();
                    try {
                        res = this.resolve(feature, qualifiedName.removeFirstSegment(), IMPORT, new HashSet<>(), true, new HashSet<>());
                    } finally {
                        this.diagnosticBuilder.endDiagnosticContext();
                    }
                }
            } else {
                this.diagnosticBuilder.startDiagnosticContext();
                try {
                    res = this.resolveImports(namespace, qualifiedName, LOCAL, new HashSet<>());
                } finally {
                    this.diagnosticBuilder.endDiagnosticContext();
                }
                if (res.isEmpty()) {
                    // continue on the owning Namespace
                    res = this.resolveAgainstFeatureInContainingNamespace(namespace.getOwningNamespace(), qualifiedName);
                }
            }
        } else {
            // namepsace is null the search ends
            res = Optional.empty();
        }

        return res;
    }

    /**
     * Tells if the given {@link Membership} reference a {@link Feature}.
     *
     * @param membership
     *            the {@link Membership}
     * @return <code>true</code> if the given {@link Membership} reference a {@link Feature}, <code>false</code>
     *         otherwise
     */
    private boolean hasFeature(Membership membership) {
        return this.get(membership, SysMLPackage.eINSTANCE.getMembership_MemberElement()).stream()
                .filter(Feature.class::isInstance)
                .findFirst()
                .isPresent();
    }

    /**
     * Gets the member {@link Element} of the given {@link Membership}.
     *
     * @param membership
     *            the {@link Membership}
     * @return the member {@link Element} of the given {@link Membership}
     */
    public Element getMemberElement(Membership membership) {
        Element current = membership;
        while (current instanceof Membership) {
            Set<Element> memberElements = this.get(current, SysMLPackage.eINSTANCE.getMembership_MemberElement());
            if (!memberElements.isEmpty()) {
                current = memberElements.iterator().next();
            } else {
                current = null;
                break;
            }
        }

        return current;
    }

}
