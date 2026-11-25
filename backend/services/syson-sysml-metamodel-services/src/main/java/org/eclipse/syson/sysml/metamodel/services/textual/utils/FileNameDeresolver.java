/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.sysml.metamodel.services.textual.utils;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.metamodel.helper.DeresolvingNamespaceProvider;
import org.eclipse.syson.sysml.metamodel.helper.EMFUtils;
import org.eclipse.syson.sysml.metamodel.helper.MembershipComputer;
import org.eclipse.syson.sysml.metamodel.helper.NameHelper;

/**
 * Object in charge of converting an Element to a resolvable qualified name depending on its context. This class tries
 * its best to find the shortest resolvable name. Be aware that this element keeps a cache of the computation of
 * {@link Namespace#visibleMemberships(EList, boolean, boolean)}. It should be used on a static model, discard this
 * object if the model changes.
 *
 * @author Arthur Daussy
 */
public class FileNameDeresolver implements INameDeresolver {

    /**
     * Keep a cache of the visible memberships of a given namespace and the visibility
     * mode used to compute them.
     */
    private final Map<Pair<Namespace, Boolean>, VisibleMemberships> visibleMembershipsCache = new HashMap<>();

    /**
     * Shares paths between scopes with the same private visibility and no conflicting path identifiers. A scope
     * that shadows any possible first segment gets its own cache entry.
     */
    private final Map<DeresolutionKey, String> deresolvedNamesCache = new HashMap<>();

    /**
     * Keeps a cache of the qualified name of each target element.
     */
    private final Map<Element, String> qualifiedNameCache = new IdentityHashMap<>();

    /** Target and namespace identities that can contribute a path to each element in this static model. */
    private final Map<Element, Set<Element>> pathElementsCache = new IdentityHashMap<>();

    /** First-segment checks depend on scope and identifier, not on the remaining qualified path. */
    private final Map<ShadowingKey, Boolean> shadowingCache = new HashMap<>();

    private final DeresolvingNamespaceProvider deresolvingNamespaceProvider = new DeresolvingNamespaceProvider();

    /**
     * Returns a resolvable name, preserving the lexical path for import and expose declarations so they cannot
     * shorten their own targets through the visibility they introduce.
     *
     * @param element the referenced element
     * @param context the referencing element
     * @return the referenced name, or {@code null} when the element is null
     */
    @Override
    public String getDeresolvedName(Element element, Element context) {

        if (element == null) {
            return null;
        }

        List<Namespace> deresolvingNamespaces = this.deresolvingNamespaceProvider.getDeresolvingNamespaces(context);

        final String qualifiedName;
        if (context instanceof Import declaration) {
            qualifiedName = this.getImportQualifiedName(element, declaration);
        } else if (deresolvingNamespaces.isEmpty()) {
            qualifiedName = this.getQualifiedName(element);
        } else {

            List<String> qualifiedNames = new ArrayList<>();
            for (Namespace deresolvingNamespace : deresolvingNamespaces) {

                // Check if resolving the direct identifier works (declaredShortName or declaredName)
                String directIdentifier = this.getResolvableDirectIdentifier(element, deresolvingNamespace, deresolvingNamespace);
                if (directIdentifier != null) {
                    qualifiedNames.add(directIdentifier);
                } else {
                    // If not, use a more complex computation
                    // A namespace can be reached through its owning membership or a differently named alias.
                    Set<Element> pathElements = this.getPathElements(element);
                    String computedQualifiedName = this.deresolve(element, deresolvingNamespace, deresolvingNamespace, pathElements);
                    if (computedQualifiedName != null && !computedQualifiedName.isBlank()) {
                        qualifiedNames.add(computedQualifiedName);
                    }
                }
            }
            qualifiedName = qualifiedNames.stream().sorted(Comparator.comparing(String::length)).findFirst().orElse(this.getQualifiedName(element));
        }

        return qualifiedName;

    }

    /**
     * Names an import or expose target without using the visibility that the importing namespace's imports introduce,
     * since an import cannot make its own target visible. The lexical path relative to the common containing namespace
     * is kept when each of its segments is visible from the importing namespace. Otherwise, as for a target exposed only
     * through a public alias of a private package, the target is named through visible memberships, ownership and
     * aliases alike.
     *
     * @param element the imported or exposed element
     * @param context the import or expose declaration
     * @return the lexical name relative to the common namespace when it is visible, else a visible path, else the
     *         lexical name
     */
    private String getImportQualifiedName(Element element, Import context) {
        Membership membership = null;
        if (element instanceof Membership externalMembership) {
            membership = externalMembership;
        } else if (context instanceof MembershipImport declaration) {
            membership = declaration.getImportedMembership();
        }
        if (membership != null && membership.eClass() == SysmlPackage.eINSTANCE.getMembership()) {
            // An import introduces this specific alias, even if several memberships designate the same element.
            String alias = Objects.toString(membership.getMemberName(), membership.getMemberShortName());
            if (alias != null && !alias.isBlank()) {
                String prefix = this.getImportPath(this.getOwningNamespaceFromContainment(membership), context);
                return Stream.of(prefix, new NameHelper().toPrintableName(alias, true))
                        .filter(segment -> !segment.isBlank()).collect(joining("::"));
            }
        }
        return this.getImportPath(element, context);
    }

    /** Finds an accessible path to an import target before the import itself takes effect. */
    private String getImportPath(Element element, Element context) {
        if (element == null) {
            return "";
        }
        Namespace commonAncestor = EMFUtils.getLeastCommonContainer(Namespace.class, element, context);
        Namespace importingNamespace = this.getOwningNamespaceFromContainment(context);
        String qualifiedName = this.getLexicalImportName(element, commonAncestor);
        if (importingNamespace != null && !this.isLexicalPathVisible(element, commonAncestor, importingNamespace)) {
            String visiblePath = this.getImportVisiblePath(element, importingNamespace);
            if (visiblePath != null && !visiblePath.isBlank()) {
                qualifiedName = visiblePath;
            }
        }
        return qualifiedName;
    }

    /** Escapes each containment segment below the common namespace, the whole containment path when there is none. */
    private String getLexicalImportName(Element element, Namespace commonAncestor) {
        List<String> names = new ArrayList<>();
        Element current = element;
        while (current != null && current != commonAncestor) {
            String name = this.getPrintableElementName(current);
            if (!name.isBlank()) {
                names.add(name);
            }
            current = this.getOwningNamespaceFromContainment(current);
        }
        return String.join("::", names.reversed());
    }

    /**
     * Tells whether each lexical segment below the common namespace is visible from the importing namespace: a segment
     * owned through a non-public membership is visible only from inside its owning namespace.
     */
    private boolean isLexicalPathVisible(Element element, Namespace commonAncestor, Namespace importingNamespace) {
        boolean visible = true;
        Element current = element;
        while (visible && current != null && current != commonAncestor) {
            Namespace owner = this.getOwningNamespaceFromContainment(current);
            if (current.eContainer() instanceof Membership membership && membership.getVisibility() != VisibilityKind.PUBLIC) {
                visible = owner != null && this.isContainerOrIdentity(owner, importingNamespace);
            }
            current = owner;
        }
        return visible;
    }

    /**
     * Searches a visible path for an import target. In the importing namespace only owned memberships are followed,
     * never its imports; outer namespaces are searched as for any other reference.
     */
    private String getImportVisiblePath(Element element, Namespace importingNamespace) {
        Set<Element> pathElements = this.getPathElements(element);
        return importingNamespace.getOwnedMembership().stream()
                .filter(membership -> pathElements.contains(membership.getMemberElement()))
                .map(membership -> this.buildRelativeQualifiedName(element, importingNamespace, membership, importingNamespace, pathElements))
                .filter(name -> name != null && !name.isBlank())
                .min(Comparator.comparing(String::length))
                .orElseGet(() -> this.deresolveFromOuterScope(element, importingNamespace, pathElements));
    }

    /** Continues an import target search in the enclosing namespaces; empty when the importing namespace is a root. */
    private String deresolveFromOuterScope(Element element, Namespace importingNamespace, Set<Element> pathElements) {
        String name = "";
        Namespace outer = this.getOwningNamespaceFromContainment(importingNamespace);
        if (outer != null) {
            name = this.deresolve(element, importingNamespace, outer, pathElements);
        }
        return name;
    }

    /**
     * Returns the full printable qualified name used as the deresolution fallback.
     *
     * @param e
     *         the element to name
     * @return the qualified name, or an empty string
     */
    private String getQualifiedName(Element e) {
        if (e == null) {
            return "";
        }
        return this.qualifiedNameCache.computeIfAbsent(e, key -> Objects.toString(key.getQualifiedName(), ""));
    }

    /**
     * Finds the owning namespace from the containment tree when the derived owning namespace is unavailable.
     *
     * @param element
     *         the element for which an owning namespace is needed
     * @return the first namespace found in the containment tree, or {@code null}
     */
    private Namespace getOwningNamespaceFromContainment(Element element) {
        EObject container = element.eContainer();
        while (container != null && !(container instanceof Namespace)) {
            container = container.eContainer();
        }
        Namespace namespace = null;
        if (container instanceof Namespace containerNamespace) {
            namespace = containerNamespace;
        }
        return namespace;
    }

    /**
     * Finds the namespaces that can expose the target through ownership or alias memberships. Following incoming
     * memberships also includes facades outside the target's containment tree without enumerating qualified names.
     */
    private Set<Element> getPathElements(Element element) {
        return this.pathElementsCache.computeIfAbsent(element, this::collectPathElements);
    }

    /** Collects lexical owners and incoming aliases from the available cross-reference index. */
    private Set<Element> collectPathElements(Element element) {
        Set<Element> pathElements = new HashSet<>();
        List<Element> pending = new ArrayList<>(List.of(element));
        while (!pending.isEmpty()) {
            Element current = pending.removeLast();
            if (pathElements.add(current)) {
                Namespace owner = this.getOwningNamespaceFromContainment(current);
                if (owner != null) {
                    pending.add(owner);
                }
                EMFUtils.getAdapter(current, ECrossReferenceAdapter.class).stream()
                        .flatMap(index -> index.getInverseReferences(current, SysmlPackage.eINSTANCE.getMembership_MemberElement(), false).stream())
                        .map(setting -> this.getOwningNamespaceFromContainment((Element) setting.getEObject()))
                        .filter(namespace -> namespace != null)
                        .forEach(pending::add);
            }
        }
        return pathElements;
    }

    /**
     * Deresolve the name of the given element.
     *
     * @param element
     *         the element to deresolve.
     * @param sourceNamespace
     *         the original owning namespace of the element
     * @param deresolvingNamespace
     *         the current namespace used to deresolved the name of the element
     * @param pathElements
     *         the target and the namespaces that can expose it through ownership or aliases
     * @return a name
     */
    private String deresolve(Element element, Namespace sourceNamespace, Namespace deresolvingNamespace, Set<Element> pathElements) {
        final String qualifiedName;
        if (deresolvingNamespace == null) {
            qualifiedName = this.getQualifiedName(element);
        } else {
            DeresolutionKey searchKey = this.deresolutionKey(element, sourceNamespace, deresolvingNamespace, pathElements);
            String cacheValue = this.deresolvedNamesCache.get(searchKey);
            if (cacheValue != null) {
                qualifiedName = cacheValue;
            } else {
                String directIdentifier = this.getResolvableDirectIdentifier(element, deresolvingNamespace, sourceNamespace);
                if (directIdentifier != null) {
                    this.deresolvedNamesCache.put(searchKey, directIdentifier);
                    return directIdentifier;
                }
                // should sourceNamespace be context or be contained in context, then private elements should be
                // accessible while deresolving.
                List<Membership> visibleMemberships = this.getVisibleMembershipsIndex(deresolvingNamespace,
                        this.isContainerOrIdentity(deresolvingNamespace, sourceNamespace)).pathMemberships(pathElements);
                // Path construction shares the visible-membership caches and must remain sequential.
                Optional<String> relativeQualifiedName = visibleMemberships.stream()
                        .map(membership -> this.buildRelativeQualifiedName(element, deresolvingNamespace, membership, sourceNamespace, pathElements))
                        .filter(name -> name != null && !name.isBlank())
                        .sorted(Comparator.comparing(String::length))
                        .findFirst();

                if (relativeQualifiedName.isPresent()) {
                    qualifiedName = relativeQualifiedName.get();
                } else {
                    // Query the parent namespace
                    qualifiedName = this.deresolve(element, sourceNamespace, deresolvingNamespace.getOwningNamespace(), pathElements);
                }

                if (qualifiedName != null) {
                    this.deresolvedNamesCache.put(searchKey, qualifiedName);
                }
            }
        }

        return qualifiedName;
    }

    /**
     * Shares a path only when private access and all possible first-segment bindings are equivalent. The closest
     * containing namespace on a possible target path determines private visibility throughout that path; other
     * scope differences matter only when a first segment is shadowed.
     */
    private DeresolutionKey deresolutionKey(Element element, Namespace sourceNamespace, Namespace namespace, Set<Element> pathElements) {
        Namespace privateScope = sourceNamespace;
        while (privateScope != null && !pathElements.contains(privateScope)) {
            privateScope = this.getOwningNamespaceFromContainment(privateScope);
        }
        Namespace shadowingScope = null;
        if (sourceNamespace != namespace) {
            VisibleMemberships visible = this.getVisibleMembershipsIndex(namespace, this.isContainerOrIdentity(namespace, sourceNamespace));
            for (Membership membership : visible.pathMemberships(pathElements)) {
                for (String identifier : visible.names(membership)) {
                    if (!identifier.isBlank() && !this.hasNoShadowingConflict(membership.getMemberElement(), identifier, sourceNamespace, namespace)) {
                        shadowingScope = sourceNamespace;
                    }
                }
            }
        }
        return new DeresolutionKey(element, namespace, privateScope, shadowingScope);
    }

    private boolean isContainerOrIdentity(Namespace ancestorObjectNamespace, Namespace namespace) {
        if (ancestorObjectNamespace == namespace) {
            return true;
        } else {
            return EcoreUtil.isAncestor(ancestorObjectNamespace, namespace);
        }
    }

    /**
     * Returns the memberships visible from a namespace, together with their lookup by name for this serialization.
     *
     * @param deresolvingNamespace
     *            the namespace whose visible memberships are needed
     * @param includePrivate
     *            whether private memberships are visible, as they are from the namespace itself or its contents
     * @return the visible memberships of the namespace
     */
    private VisibleMemberships getVisibleMembershipsIndex(Namespace deresolvingNamespace, boolean includePrivate) {
        return this.visibleMembershipsCache.computeIfAbsent(Pair.of(deresolvingNamespace, includePrivate), key -> {
            EList<Membership> visible = new MembershipComputer<>(deresolvingNamespace, new UniqueEList<>()).visibleMemberships(false, includePrivate, true);
            return new VisibleMemberships(visible.stream()
                    .filter(m -> m.getMemberElement() != null)
                    .filter(m -> includePrivate || m.getVisibility() != VisibilityKind.PRIVATE)
                    .collect(toCollection(UniqueEList<Membership>::new)));
        });
    }

    /** Names the target through a visible first membership in the referencing scope. */
    private String buildRelativeQualifiedName(Element element, Namespace namespace, Membership membership, Namespace sourceNamespace, Set<Element> pathElements) {
        return this.buildVisiblePath(element, namespace, membership, sourceNamespace,
                pathElements, new HashSet<>(Set.of(namespace)));
    }

    /**
     * Tries to use the {@link Element#getDeclaredShortName()} or the {@link Element#getDeclaredName()} to access the targetElement
     *
     * @param targetElement
     *         the element to target
     * @param contextNamespace
     *         the namespace used to test the resolution of the identifier
     * @return an identifier or null if no direct resolvable identifier found.
     */
    private String getResolvableDirectIdentifier(Element targetElement, Namespace contextNamespace, Namespace sourceNamespace) {
        String identifier = null;
        String shortName = targetElement.getDeclaredShortName();
        if (shortName != null && !shortName.isBlank()) {
            if (this.isDirectlyVisibleIdentifier(targetElement, contextNamespace, sourceNamespace, shortName)) {
                identifier = shortName;
            }
        }

        if (identifier == null) {
            String declaredName = targetElement.getDeclaredName();
            if (declaredName != null && !declaredName.isBlank()) {
                if (this.isDirectlyVisibleIdentifier(targetElement, contextNamespace, sourceNamespace, declaredName)) {
                    identifier = declaredName;
                }
            }
        }

        if (identifier == null && targetElement.getDeclaredName() == null && targetElement.getDeclaredShortName() == null) {
            String effectiveName = targetElement.getName();
            if (effectiveName != null && !effectiveName.isBlank()) {
                if (this.isDirectlyVisibleIdentifier(targetElement, contextNamespace, sourceNamespace, effectiveName)) {
                    identifier = effectiveName;
                }
            }
        }

        if (identifier != null) {
            identifier = new NameHelper().toPrintableName(identifier);
        }
        return identifier;
    }

    /**
     * Checks whether the given identifier matches a membership that is textually visible from the current namespace.
     *
     * <p>
     * The direct-name fast path must consider private imports owned by the current namespace, because they are
     * directly usable inside that lexical scope. At the same time, it must still honor shadowing and distinguish
     * derived-name conflicts when matching the visible memberships.
     * </p>
     *
     * @param targetElement
     *         the element to resolve
     * @param contextNamespace
     *         the namespace in which the identifier is written
     * @param sourceNamespace
     *         the original lexical namespace in which the resulting identifier must also resolve without conflict
     * @param identifier
     *         the candidate direct identifier
     * @return {@code true} if the identifier directly denotes the target element in the given namespace
     */
    private boolean isDirectlyVisibleIdentifier(Element targetElement, Namespace contextNamespace, Namespace sourceNamespace, String identifier) {
        Optional<Membership> directlyVisibleMembership = this.getVisibleMembershipsIndex(contextNamespace, true).firstNamed(identifier)
                .filter(membership -> membership.getMemberElement() == targetElement);
        if (directlyVisibleMembership.isEmpty()) {
            return false;
        }
        return this.hasNoShadowingConflict(targetElement, identifier, sourceNamespace, contextNamespace);
    }

    /**
     * Checks that the candidate identifier is not shadowed by a conflicting visible membership in any lexical scope
     * between the source namespace and the namespace from which the shortcut is evaluated.
     *
     * @param targetElement
     *         the element targeted by the identifier
     * @param identifier
     *         the identifier being checked
     * @param sourceNamespace
     *         the original lexical namespace
     * @param contextNamespace
     *         the namespace from which the shortcut is evaluated
     * @return {@code true} if no intermediate scope introduces a conflicting visible membership
     */
    private boolean hasNoShadowingConflict(Element targetElement, String identifier, Namespace sourceNamespace, Namespace contextNamespace) {
        ShadowingKey key = new ShadowingKey(targetElement, identifier, sourceNamespace, contextNamespace);
        return this.shadowingCache.computeIfAbsent(key,
                ignored -> this.computeHasNoShadowingConflict(targetElement, identifier, sourceNamespace, contextNamespace));
    }

    /** Checks intermediate lexical scopes for a different first binding. */
    private boolean computeHasNoShadowingConflict(Element targetElement, String identifier, Namespace sourceNamespace, Namespace contextNamespace) {
        boolean hasNoShadowingConflict = false;
        Namespace currentNamespace = sourceNamespace;
        while (currentNamespace != null) {
            Optional<Membership> visibleMembership = this.getVisibleMembershipsIndex(currentNamespace, true).firstNamed(identifier);
            if (visibleMembership.isPresent() && !this.match(targetElement, visibleMembership.get())) {
                hasNoShadowingConflict = false;
                break;
            } else if (currentNamespace == contextNamespace) {
                hasNoShadowingConflict = true;
                break;
            } else {
                currentNamespace = currentNamespace.getOwningNamespace();
            }
        }
        return hasNoShadowingConflict;
    }

    /**
     * Checks if the resolved element is either the resolved element member or if it implicitly matches the naming
     * feature of unnamed element which name computation used the resolved element.
     *
     * @param element
     *         the element to compute the name from
     * @param resolvedElement
     *         the resolution tested name in the local namespace
     * @return <code>true</code> if the elements match
     */
    private boolean match(Element element, Membership resolvedElement) {
        return resolvedElement.getMemberElement() == element || this.implicitMatch(resolvedElement, element);
    }

    private boolean implicitMatch(Membership resolvedMembership, Element context) {
        Element resolvedElement = resolvedMembership.getMemberElement();

        if (resolvedElement.getDeclaredName() == null && resolvedElement.getDeclaredShortName() == null && resolvedElement instanceof Feature feature) {
            return feature.namingFeature() == context;

        }
        return false;
    }

    /**
     * Builds a path through visible memberships, retaining alias names at every namespace segment. Only namespaces
     * that can expose the target are explored; an alias back to a visited namespace cannot contribute a valid suffix.
     */
    private String buildVisiblePath(Element element, Namespace namespace, Membership membership, Namespace sourceNamespace,
            Set<Element> pathElements, Set<Namespace> visited) {
        Element member = membership.getMemberElement();
        boolean includePrivate = this.isContainerOrIdentity(namespace, sourceNamespace);
        boolean firstSegment = visited.size() == 1;
        VisibleMemberships visibleMemberships = this.getVisibleMembershipsIndex(namespace, includePrivate);
        Optional<String> name = visibleMemberships.names(membership).stream()
                .filter(identifier -> !identifier.isBlank())
                .filter(identifier -> visibleMemberships.firstNamed(identifier)
                        .filter(visible -> this.match(member, visible)).isPresent())
                .filter(identifier -> !firstSegment || this.hasNoShadowingConflict(member, identifier, sourceNamespace, namespace))
                .map(identifier -> new NameHelper().toPrintableName(identifier))
                .min(Comparator.comparing(String::length));
        if (name.isEmpty()) {
            return "";
        }
        String path = "";
        if (member == element) {
            path = name.get();
        } else if (member instanceof Namespace memberNamespace && visited.add(memberNamespace)) {
            try {
                String suffix = this.getVisibleMembershipsIndex(memberNamespace, this.isContainerOrIdentity(memberNamespace, sourceNamespace))
                        .pathMemberships(pathElements).stream()
                        .map(candidate -> this.buildVisiblePath(element, memberNamespace, candidate, sourceNamespace, pathElements, visited))
                        .filter(candidate -> !candidate.isBlank())
                        .min(Comparator.comparingInt(String::length))
                        .orElse("");
                if (!suffix.isBlank()) {
                    path = name.get() + "::" + suffix;
                }
            } finally {
                visited.remove(memberNamespace);
            }
        }
        return path;
    }

    /**
     * Gets the best available simple name for an element.
     *
     * @param element
     *         the element for which a name is needed
     * @return the derived name, declared name, or declared short name
     */
    private String getElementName(Element element) {
        String name = element.getName();
        if (name == null || name.isBlank()) {
            name = element.getDeclaredName();
        }
        if (name == null || name.isBlank()) {
            name = element.getDeclaredShortName();
        }
        return name;
    }

    /**
     * Gets the best available simple name for an element in printable SysML form.
     *
     * @param element
     *         the element for which a name is needed
     * @return the printable name, or an empty string
     */
    private String getPrintableElementName(Element element) {
        String name = this.getElementName(element);
        if (name == null) {
            name = "";
        }
        return new NameHelper().toPrintableName(name);
    }


    /** Private access and prefix shadowing determine whether two scopes can reuse a qualified name. */
    private record DeresolutionKey(Element target, Namespace namespace, Namespace privateScope, Namespace shadowingScope) {
    }

    /** Shadowing checks can be shared only for the same identifier, expected target and lexical scopes. */
    private record ShadowingKey(Element target, String identifier, Namespace sourceNamespace, Namespace contextNamespace) {
    }

    /**
     * The memberships visible from a namespace, in the order {@link Namespace#visibleMemberships} gives them, with the
     * lookup of the first one that a name designates.
     * <p>
     * The names of a membership are derived: naming an unnamed feature goes through its redefinitions and their
     * implicit specializations, which costs far more than a lookup. The memberships are therefore indexed by name as
     * they are scanned, at most once each, and only as far as a lookup needs, so that each name is computed once per
     * namespace instead of once per reference, and no name is computed beyond the first match.
     * </p>
     */
    private static final class VisibleMemberships {

        private final EList<Membership> memberships;

        /** The first scanned membership for each of its short name and name. */
        private final Map<String, Membership> firstByName = new HashMap<>();

        private final Map<Membership, List<String>> namesByMembership = new IdentityHashMap<>();

        private final Map<Set<Element>, List<Membership>> membershipsByPath = new IdentityHashMap<>();

        /** The number of memberships already indexed, from the start of the list. */
        private int scannedCount;

        /** Starts an index in the same resolution order as the visible memberships. */
        private VisibleMemberships(EList<Membership> memberships) {
            this.memberships = memberships;
        }

        /**
         * Returns the first membership whose short name or name is {@code identifier}, as a scan of the list in order
         * would.
         *
         * @param identifier
         *            the name to look for
         * @return the first membership with that name, if any
         */
        Optional<Membership> firstNamed(String identifier) {
            Membership found = this.firstByName.get(identifier);
            while (found == null && this.scannedCount < this.memberships.size()) {
                Membership membership = this.memberships.get(this.scannedCount);
                this.scannedCount++;
                this.names(membership).forEach(name -> this.index(name, membership));
                found = this.firstByName.get(identifier);
            }
            return Optional.ofNullable(found);
        }

        /** Keeps namespace scans independent of the referencing scope; shadowing is checked after this selection. */
        private List<Membership> pathMemberships(Set<Element> pathElements) {
            return this.membershipsByPath.computeIfAbsent(pathElements, elements -> this.memberships.stream()
                    .filter(membership -> elements.contains(membership.getMemberElement())).toList());
        }

        /** Reads each membership's derived names once, whether first requested by lookup or path construction. */
        private List<String> names(Membership membership) {
            return this.namesByMembership.computeIfAbsent(membership, member -> Stream.of(member.getMemberShortName(), member.getMemberName())
                    .filter(name -> name != null).toList());
        }

        /** Keeps the first membership that binds an identifier. */
        private void index(String name, Membership membership) {
            if (name != null) {
                this.firstByName.putIfAbsent(name, membership);
            }
        }
    }
}
