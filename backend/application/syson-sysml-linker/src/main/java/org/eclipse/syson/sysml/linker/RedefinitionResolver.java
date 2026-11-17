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

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.omg.sysml.lang.sysml.ConjugatedPortDefinition;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureMembership;
import org.omg.sysml.lang.sysml.FlowEnd;
import org.omg.sysml.lang.sysml.FlowUsage;
import org.omg.sysml.lang.sysml.InvocationExpression;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.OccurrenceUsage;
import org.omg.sysml.lang.sysml.PortConjugation;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.RequirementUsage;
import org.omg.sysml.lang.sysml.Specialization;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.Type;
import org.omg.sysml.lang.sysml.VerificationCaseUsage;
import org.omg.sysml.lang.sysml.VisibilityKind;
import org.omg.sysml.util.TypeUtil;

/**
 * Resolves {@link Redefinition}.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class RedefinitionResolver {

    /**
     * The {@link SysMLLinker}.
     */
    private final SysMLLinker linker;

    /**
     * The {@link MemberNameProvider}.
     */
    private final ContextNamespaceProvider contextProvider;

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
    private final DiagnosticBuilder diagnosticBuilder;

    /**
     * Constructor.
     *
     * @param linker
     *            the {@link SysMLLinker}
     * @param diagnosticBuilder
     *            the {@link DiagnosticBuilder} for error reporting
     * @param contextProvider
     *            the {@link ContextNamespaceProvider}
     */
    public RedefinitionResolver(SysMLLinker linker, DiagnosticBuilder diagnosticBuilder, ContextNamespaceProvider contextProvider) {
        this.linker = linker;
        this.diagnosticBuilder = diagnosticBuilder;
        this.contextProvider = contextProvider;
    }

    /**
     * Resolves the redefined feature of the given {@link Redefinition}.
     *
     * <p>
     * KerML 8.2.3.5.1 requires the basic name-resolution process to be repeated with the general type of every owned
     * specialization of the owning type as the local namespace. A qualified name therefore has two different visibility
     * phases: its first segment is resolved fully from a general type, while each remaining segment is resolved
     * visibly, hence with public visibility.
     * </p>
     *
     * <p>
     * {@link FlowEnd} redefinitions keep their implementation-specific resolution for now. Their context is adjusted by
     * {@link ContextNamespaceProvider} from already linked flow-end information and cannot be replaced safely by the
     * general rule without treating that model-completion case explicitly.
     * </p>
     *
     * @param redefinition
     *            the {@link Redefinition}
     * @param context
     *            the context computed by the {@link ContextNamespaceProvider}
     * @param qualifiedName
     *            the name to resolve
     * @param visibilities
     *            the visibilities used by the existing {@link FlowEnd} resolution
     * @return the {@link Optional} resolved {@link Membership}
     */
    public Optional<Membership> resolveRedefinedReference(Redefinition redefinition, Namespace context, QualifiedName qualifiedName, Set<VisibilityKind> visibilities) {
        Type owningType = this.sysMLEMFNavigator.getRedefinitionOwningType(redefinition);
        Optional<Membership> resolved;
        if (owningType != null && !(owningType instanceof FlowEnd)) {
            if (owningType instanceof RequirementUsage && owningType.getOwningNamespace() instanceof VerificationCaseUsage) {
                resolved = this.resolveOrdinaryRedefinedReference(redefinition, (Type) owningType.getOwningNamespace(), qualifiedName);
            } else {
                Type lastOwningType;
                do {
                    resolved = this.resolveOrdinaryRedefinedReference(redefinition, owningType, qualifiedName);
                    // SysML 7.9.3 that is, an occurrence usage not of a more specialized kind, but declared as a time
                    // slice or snapshot, respectively
                    lastOwningType = owningType;
                    owningType = this.sysMLEMFNavigator.getContaining(Type.class, owningType.eContainer());
                } while (resolved.isEmpty() && lastOwningType instanceof OccurrenceUsage occurrenceUsage && occurrenceUsage.getPortionKind() != null);
            }
        } else {
            resolved = this.resolveFlowEndRedefinedReference(redefinition, context, qualifiedName, visibilities);
        }
        return resolved;
    }

    /**
     * Resolves a redefined feature for an ordinary owning type.
     *
     * <p>
     * Existing simple-name resolution is attempted first only when the owning type itself owns a redefinition. This
     * preserves its selection when several general types expose the same name, as in a nested typed-and-redefining
     * feature. The specification passage does not define a different tie-breaking rule for that situation. The
     * general-type process is otherwise used directly. A simple name is resolved fully, then the selected membership is
     * limited to the public and protected visibilities required for inheritance. A qualified name always uses the
     * general-type process so that only its first segment is resolved fully.
     * </p>
     *
     * @param redefinition
     *            the {@link Redefinition} to resolve
     * @param owningType
     *            the ownining {@link Type}
     * @param qualifiedName
     *            the {@link QualifiedName} to resolve
     * @return the {@link Optional} resolved {@link Membership}
     */
    private Optional<Membership> resolveOrdinaryRedefinedReference(Redefinition redefinition, Type owningType, QualifiedName qualifiedName) {
        Optional<Membership> resolved = Optional.empty();
        boolean owningTypeRedefinesAType = this.linker.get(owningType, SysMLPackage.eINSTANCE.getElement_OwnedRelationship()).stream().anyMatch(Redefinition.class::isInstance);
        if (qualifiedName.isLocal() && owningTypeRedefinesAType) {
            Set<Membership> redefined = this.getRedefinedMemberships(owningType, Collections.emptySet());
            resolved = this.linker.resolveSpecializations(owningType, qualifiedName, redefined, new HashSet<>());
        }
        if (resolved.isEmpty()) {
            Feature redefiningFeature = this.getRedefinitionOwningFeature(redefinition);
            resolved = this.resolveRedefinedReferenceFromGeneralTypes(owningType, qualifiedName, redefiningFeature);
        }
        boolean flowEnd = owningType instanceof FlowUsage && this.getRedefinitionOwningFeature(redefinition).isEnd();
        if (!(owningType instanceof InvocationExpression) && (resolved.isEmpty() || flowEnd)
                && !this.hasPendingExplicitGeneral(owningType)) {
            // SysML 8.3.16.3 requires a FlowUsage with ends to specialize Flows::flows even when
            // explicitly typed. KerML 8.3.3.1.10 removes redefined members across explicit and
            // implied general types, including when the owning feature itself redefines another feature.
            // Keep implicit lookup as a fallback for other types: recursively searching their library
            // hierarchies after every explicit match adds work without addressing the flow-end rule.
            Set<Membership> candidates = new LinkedHashSet<>();
            resolved.ifPresent(candidates::add);
            Set<Membership> redefined = this.getRedefinedMemberships(owningType, Collections.emptySet(), this.getRedefinitionOwningFeature(redefinition));
            for (Type implicitGeneralType : TypeUtil.getImplicitGeneralTypesFor(owningType)) {
                this.resolveRedefinedReferenceFromGeneralType(implicitGeneralType, qualifiedName, redefined).ifPresent(candidates::add);
                if (!flowEnd && !candidates.isEmpty()) {
                    break;
                }
            }
            resolved = this.removeRedefinedMemberships(candidates).stream().findFirst();
        }
        // the resolution from parent Namespace need to be done after all specializations are explored
        if (resolved.isEmpty() && !this.hasPendingExplicitGeneral(owningType)) {
            resolved = this.linker.resolveInParentOrGlobal(owningType, qualifiedName, SysMLLinker.LOCAL, false);
        }
        return resolved;
    }

    /**
     * Tells if the given {@link Type} has unresolved {@link Specialization}.
     *
     * @param owningType
     *            the {@link Type} to check
     * @return <code>true</code> if the given {@link Type} has unresolved {@link Specialization}, <code>false</code>
     *         otherwise
     */
    private boolean hasPendingExplicitGeneral(Type owningType) {
        List<Specialization> specializations = this.linker.get(owningType, SysMLPackage.eINSTANCE.getElement_OwnedRelationship()).stream()
                .filter(Specialization.class::isInstance)
                .map(Specialization.class::cast)
                .toList();
        for (Specialization specialization : specializations) {
            EReference specializationFeature = this.sysMLMetamodelHelper.getSpecializationFeature(specialization);
            Set<Element> generalElements = this.linker.get(specialization, specializationFeature);
            if (generalElements.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the {@link Feature} that owns the given {@link Redefinition} using containment only.
     *
     * @param redefinition
     *            the {@link Redefinition}
     * @return the owning {@link Feature}, or <code>null</code> when it is not contained in a feature
     */
    private Feature getRedefinitionOwningFeature(Redefinition redefinition) {
        return this.sysMLEMFNavigator.getContaining(Feature.class, redefinition.eContainer());
    }

    /**
     * Resolves a redefined feature from the general type of each contained specialization of the owning type.
     *
     * <p>
     * The containment reference is read through {@link SysMLLinker#get(EObject, EReference)} instead of
     * {@link Type#getOwnedSpecialization()}. The latter is derived and can expose implied relationships that are added
     * during model completion, whereas linking must only navigate the parser-produced containment tree and resolve
     * relationship targets through {@link SysMLLinker#get(EObject, EReference)}. Implicit general types are then used
     * as an implementation-specific fallback because the parser does not materialize their specializations. This
     * preserves the resolution of implicit semantic-metadata redefinitions until those relationships are available in
     * the containment tree.
     * </p>
     *
     * <p>
     * Invocation parameter redefinitions require a specific path through this rule. KerML 8.2.5.8.3 maps every named
     * argument to a {@code ParameterRedefinition}, and KerML 7.4.9.4 states that a named argument is matched to an
     * input parameter by name. KerML 8.3.4.8.8 then requires every input parameter of an invocation to redefine exactly
     * one input parameter of its {@code instantiatedType}, and requires the invocation to specialize that type. The
     * semantic equivalence in KerML 8.4.4.9.5 expresses the same relation as a feature {@code a} that redefines
     * {@code F::a}, where {@code F} is the invoked type.
     * </p>
     *
     * <p>
     * Normally, KerML 8.2.3.5.1 resolves a redefinition from the general Types of the owning Type, so the implied
     * specialization of the invocation would make its {@code instantiatedType} the resolution namespace. Our parser
     * deliberately serializes only the invocation Membership and does not materialize that implied specialization.
     * Moreover, {@link TypeUtil#getImplicitGeneralTypesFor(Type)} can be computed before this Membership is linked and
     * then retain only the generic invocation type. Therefore, the invocation branch below obtains the
     * {@code instantiatedType} from the parser-produced Membership and applies the same general-Type resolution
     * directly. This preserves the specified semantics without adding or serializing an implied FeatureTyping.
     * </p>
     *
     * @param owningType
     *            the owning {@link Type}
     * @param qualifiedName
     *            the {@link QualifiedName} to resolve
     * @param redefiningFeature
     *            the redefining {@link Feature}
     * @return the {@link Optional} resolved {@link Membership}
     */
    private Optional<Membership> resolveRedefinedReferenceFromGeneralTypes(Type owningType, QualifiedName qualifiedName, Feature redefiningFeature) {
        Set<Membership> redefined = this.getRedefinedMemberships(owningType, Collections.emptySet(), redefiningFeature);
        if (owningType instanceof InvocationExpression invocationExpression) {
            return this.getInvocationInstantiatedType(invocationExpression)
                    .flatMap(instantiatedType -> this.resolveRedefinedReferenceFromGeneralType(instantiatedType, qualifiedName, redefined));
        }
        List<Specialization> specializations = this.linker.get(owningType, SysMLPackage.eINSTANCE.getElement_OwnedRelationship()).stream()
                .filter(Specialization.class::isInstance)
                .map(Specialization.class::cast)
                .toList();
        Set<Membership> candidates = new LinkedHashSet<>();
        for (Specialization specialization : specializations) {
            EReference specializationFeature = this.sysMLMetamodelHelper.getSpecializationFeature(specialization);
            Set<Element> generalElements = this.linker.get(specialization, specializationFeature);
            for (Element generalElement : generalElements) {
                Type referencedType = this.getReferencedType(generalElement);
                if (referencedType != null) {
                    this.resolveRedefinedReferenceFromGeneralType(referencedType, qualifiedName, redefined).ifPresent(candidates::add);
                }
            }
        }
        // KerML 8.3.3.1.10 inheritedMemberships/removeRedefinedFeatures compares candidates across all
        // explicit supertypes before discarding redefined features.
        return this.removeRedefinedMemberships(candidates).stream().findFirst();
    }

    /**
     * Gets the referenced {@link Type} from the given {@link Element}.
     *
     * @param element
     *            the {@link Element}
     * @return the referenced {@link Type} from the given {@link Element}
     */
    private Type getReferencedType(Element element) {
        Type referencedType;

        if (element instanceof Feature generalFeature) {
            referencedType = this.contextProvider.getReferencedFeatureContext(generalFeature);
        } else if (element instanceof Type generalType) {
            referencedType = generalType;
        } else {
            referencedType = null;
        }

        return referencedType;
    }

    /**
     * Gets the type instantiated by an invocation from its parser-produced membership.
     *
     * <p>
     * KerML 8.3.4.8.8 requires every invocation input parameter to redefine an input parameter of the instantiated
     * type. The parser represents that type with the first owned {@link Membership} that is not a
     * {@link FeatureMembership}. Reading its member element through {@link SysMLLinker#get(EObject, EReference)} also
     * allows the linker to resolve the membership before resolving the parameter redefinitions. This avoids consulting
     * a stale implicit-general-type cache that may have been computed while the membership was still unresolved.
     * </p>
     *
     * @param invocationExpression
     *            the invocation expression
     * @return the instantiated type, if its membership can be resolved
     */
    private Optional<Type> getInvocationInstantiatedType(InvocationExpression invocationExpression) {
        return this.linker.get(invocationExpression, SysMLPackage.eINSTANCE.getElement_OwnedRelationship()).stream()
                .filter(Membership.class::isInstance)
                .map(Membership.class::cast)
                .filter(membership -> !(membership instanceof FeatureMembership))
                .findFirst()
                .flatMap(membership -> this.linker.get(membership, SysMLPackage.eINSTANCE.getMembership_MemberElement()).stream()
                        .filter(Type.class::isInstance)
                        .map(Type.class::cast)
                        .findFirst());
    }

    /**
     * Applies the redefinition name-resolution process with the given general type as local namespace.
     *
     * <p>
     * KerML 8.2.3.5.1 resolves the first segment of a qualified name fully and every remaining segment visibly.
     * Therefore, a private first segment is accepted when it only qualifies the target. For a simple name, that first
     * segment is also the redefined feature itself. It is resolved fully, then the visibility of the selected
     * membership is checked separately for the inheritability required by KerML 7.3.4.5. This separation allows full
     * resolution to traverse a private import without accepting a private target membership.
     * </p>
     */
    private Optional<Membership> resolveRedefinedReferenceFromGeneralType(Type generalType, QualifiedName qualifiedName, Set<Membership> redefined) {
        QualifiedName firstSegment = new QualifiedName(qualifiedName.getFirstSegment());
        QualifiedName remainingSegments = qualifiedName.removeFirstSegment();
        boolean isQualified = remainingSegments != null;
        Optional<Membership> resolved = this.linker.resolve(generalType, firstSegment, SysMLLinker.LOCAL, redefined, true, new HashSet<>());
        if (!isQualified) {
            resolved = resolved.filter(membership -> SysMLLinker.SPECIALIZATION.contains(membership.getVisibility()));
        }
        if (resolved.isEmpty() && !isQualified && generalType instanceof ConjugatedPortDefinition conjugatedPortDefinition) {
            resolved = this.resolveRedefinedReferenceFromOriginalPort(conjugatedPortDefinition, firstSegment, redefined);
        }
        if (resolved.isPresent() && remainingSegments != null) {
            Element qualification = this.linker.getMemberElement(resolved.get());
            if (qualification instanceof Namespace qualificationNamespace) {
                resolved = this.linker.resolve(qualificationNamespace, remainingSegments, SysMLLinker.IMPORT, Collections.emptySet(), true, new HashSet<>());
            } else {
                this.diagnosticBuilder.addLocalElementNotNamespace(generalType, qualification, qualifiedName);
                resolved = Optional.empty();
            }
        }

        return resolved;
    }

    /**
     * Resolves a simple redefinition name from the original definition of a conjugated port.
     *
     * <p>
     * The parser-produced model represents the relation to the original port with a contained {@link PortConjugation};
     * it does not materialize inherited memberships on the {@link ConjugatedPortDefinition}. Both containment and the
     * conjugation target are therefore navigated through {@link SysMLLinker#get(EObject, EReference)}. This fallback is
     * deliberately limited to conjugated ports so that an ordinary general type cannot expose a private member by
     * continuing in its containing namespace.
     * </p>
     *
     * @param conjugatedPortDefinition
     *            the conjugated port used as general type
     * @param qualifiedName
     *            the simple name to resolve
     * @param redefined
     *            memberships already redefined by the owning type
     * @return the resolved membership, if any
     */
    private Optional<Membership> resolveRedefinedReferenceFromOriginalPort(ConjugatedPortDefinition conjugatedPortDefinition, QualifiedName qualifiedName,
            Set<Membership> redefined) {
        Optional<Membership> resolved = Optional.empty();
        Set<Element> relationships = this.linker.get(conjugatedPortDefinition, SysMLPackage.eINSTANCE.getElement_OwnedRelationship());
        for (Element relationship : relationships) {
            if (relationship instanceof PortConjugation portConjugation) {
                Set<Element> originalPorts = this.linker.get(portConjugation, SysMLPackage.eINSTANCE.getPortConjugation_OriginalPortDefinition());
                for (Element originalPort : originalPorts) {
                    if (originalPort instanceof Namespace originalPortNamespace) {
                        resolved = this.linker.resolve(originalPortNamespace, qualifiedName, SysMLLinker.SPECIALIZATION, redefined, true, new HashSet<>());
                        if (resolved.isPresent()) {
                            break;
                        }
                    }
                }
            }
            if (resolved.isPresent()) {
                break;
            }
        }
        return resolved;
    }

    /**
     * Preserves the existing resolution used for {@link FlowEnd} redefinitions.
     *
     * @param redefinition
     *            the {@link Redefinition}
     * @param context
     *            the context computed by the {@link ContextNamespaceProvider}
     * @param qualifiedName
     *            the name to resolve
     * @param visibilities
     *            the visibilities used by the existing {@link FlowEnd} resolution
     * @return the resolved {@link Membership}, if any
     */
    private Optional<Membership> resolveFlowEndRedefinedReference(Redefinition redefinition, Namespace context, QualifiedName qualifiedName, Set<VisibilityKind> visibilities) {
        Set<Membership> redefined = this.getRedefinedMemberships(context, Collections.emptySet());
        Optional<Membership> resolved;
        // the last check tells us we are already on the specialized type and should perform a simple resolution
        if (qualifiedName.isLocal() && redefinition.getOwningFeature().getOwningNamespace() == context) {
            resolved = this.linker.resolveSpecializations(context, qualifiedName, redefined, new HashSet<>());
        } else {
            resolved = this.linker.resolve(context, qualifiedName, visibilities, redefined, false, new HashSet<>());
        }
        return resolved;
    }

    /**
     * Removes inherited candidates whose features are directly or indirectly redefined by another candidate.
     *
     * @param memberships
     *            the candidates from the general types, in lookup order
     * @return the remaining memberships, preserving lookup order for unrelated candidates
     */
    public Set<Membership> removeRedefinedMemberships(Set<Membership> memberships) {
        Set<Element> redefinedFeatures = new HashSet<>();
        for (Membership membership : memberships) {
            Element member = this.linker.getMemberElement(membership);
            if (member instanceof Feature feature) {
                this.collectRedefinedFeatures(feature, redefinedFeatures, new HashSet<>());
            }
        }
        return memberships.stream()
                .filter(membership -> !redefinedFeatures.contains(this.linker.getMemberElement(membership)))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Collects the features directly or indirectly redefined by the given {@link Feature}. Targets are resolved through
     * the linker, and already visited features are skipped to prevent cyclic traversal.
     *
     * @param feature
     *            the feature whose owned redefinitions are traversed
     * @param redefinedFeatures
     *            the set to which resolved redefined features are added
     * @param visited
     *            the set of features already visited during this traversal
     */
    private void collectRedefinedFeatures(Feature feature, Set<Element> redefinedFeatures, Set<Feature> visited) {
        if (visited.add(feature)) {
            for (var relationship : feature.getOwnedRelationship()) {
                if (relationship instanceof Redefinition redefinition) {
                    for (Element redefined : this.linker.get(redefinition, SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature())) {
                        if (redefined instanceof Feature redefinedFeature) {
                            redefinedFeatures.add(redefinedFeature);
                            this.collectRedefinedFeatures(redefinedFeature, redefinedFeatures, visited);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets the {@link Set} of redefined {@link Membership} for the given {@link Namespace}.
     *
     * @param namespace
     *            the {@link Namespace}
     * @param redefined
     *            the already redefined {@link Membership}
     * @return the {@link Set} of redefined {@link Membership} for the given {@link Namespace}
     */
    public Set<Membership> getRedefinedMemberships(Namespace namespace, Set<Membership> redefined) {
        return this.getRedefinedMemberships(namespace, redefined, null);
    }

    /**
     * Gets the redefined memberships, optionally ignoring the redefinitions owned by one feature.
     *
     * <p>
     * The exclusion is used while resolving a {@link Redefinition}: the feature that owns the current relationship must
     * not hide that relationship's own candidate. This is especially important when the same feature owns several
     * redefinitions.
     * </p>
     */
    /**
     * @param context
     *            the context {@link Namespace}
     * @param redefined
     *            the {@link Set} of redefined {@link Membership}
     * @param excludedRedefiningFeature
     *            the excluded redefining {@link Feature} if any, <code>null</code> otherwise
     * @return the {@link Set} of redefined {@link Membership}
     */
    private Set<Membership> getRedefinedMemberships(Namespace context, Set<Membership> redefined, Feature excludedRedefiningFeature) {
        Set<Membership> res;

        if (context instanceof Type) {
            // Resolving a redefinition below may lazily transform another element and add an owned
            // relationship to this Type. Iterate over a snapshot so that such a reentrant model
            // completion cannot invalidate the EMF containment-list iterator.
            Set<Membership> localRedefined = List.copyOf(context.getOwnedRelationship()).stream()
                    .filter(FeatureMembership.class::isInstance)
                    .map(FeatureMembership.class::cast)
                    .map(FeatureMembership::getOwnedMemberFeature)
                    .filter(f -> f != null)
                    .filter(f -> f != excludedRedefiningFeature)
                    .flatMap(f -> f.getOwnedRedefinition().stream())
                    .flatMap(r -> this.linker.get(r, SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature()).stream())
                    .filter(Feature.class::isInstance)
                    .map(Feature.class::cast)
                    .map(f -> f.getOwningMembership())
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            if (!localRedefined.isEmpty()) {
                res = new LinkedHashSet<>(redefined);
                res.addAll(localRedefined);
            } else {
                res = redefined;
            }
        } else {
            res = redefined;
        }

        return res;
    }

}
