/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.sysml.export.utils;

import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.helper.DeresolvingNamespaceProvider;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.helper.MembershipComputer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Object in charge of converting an Element to a resolvable qualified name depending on its context. This class tries
 * its best to find the shortest resolvable name. Be aware that this element keeps a cache of the computation of
 * {@link Namespace#visibleMemberships(EList, boolean, boolean)}. It should be used on a static model, discard this
 * object if the model changes.
 *
 * @author Arthur Daussy
 */
public class NameDeresolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(NameDeresolver.class);

    /**
     * Keep a cache of the visible memberships of a given Namespace identified by its elementId.
     */
    private final Map<String, EList<Membership>> visibleMembershipsCache = new HashMap<>();

    /**
     * Keep a cache the deresolved name of an element from a given namespace. The key of the map is a pair of the
     * elementId of the deresolving namespace and the element itself.
     */
    private final Map<Pair<String, String>, String> deresolvedNamesCache = new HashMap<>();

    /**
     * Keeps a cache of the qualified name of an element identified by its elementId.
     */
    private final Map<String, String> qualifiedNameCache = new HashMap<>();

    private final DeresolvingNamespaceProvider deresolvingNamespaceProvider = new DeresolvingNamespaceProvider();

    public String getDeresolvedName(Element element, Element context) {

        if (element == null) {
            return null;
        }

        List<Namespace> deresolvingNamespaces = this.deresolvingNamespaceProvider.getDeresolvingNamespaces(context);

        final String qualifiedName;
        if (deresolvingNamespaces.isEmpty()) {
            qualifiedName = this.getQualifiedName(element);
        } else {

            List<String> qualifiedNames = new ArrayList<>();
            for (Namespace deresolvingNamespace : deresolvingNamespaces) {

                // An element is either reachable form its containment tree or via a reference Membership#memberElement
                Set<Membership> elementAncestors = new HashSet<>(EMFUtils.getAncestors(Membership.class, element, null));
                EMFUtils.getInverse(element, SysmlPackage.eINSTANCE.getMembership_MemberElement()).stream().map(s -> (Membership) s.getEObject()).forEach(elementAncestors::add);
                String computedQualifiedName = this.deresolve(element, deresolvingNamespace, deresolvingNamespace, elementAncestors);
                if (computedQualifiedName != null && !computedQualifiedName.isBlank()) {
                    qualifiedNames.add(computedQualifiedName);
                }

            }
            qualifiedName = qualifiedNames.stream().sorted(Comparator.comparing(String::length)).findFirst().orElse(this.getQualifiedName(element));
        }

        return qualifiedName;

    }

    private String getQualifiedName(Element e) {
        if (e == null) {
            return "";
        }
        String qn = this.qualifiedNameCache.get(e.getElementId());
        if (qn == null) {
            qn = e.getQualifiedName();
            if (qn == null) {
                qn = "";
            }
            this.qualifiedNameCache.put(e.getElementId(), qn);
        }
        return qn;
    }

    /**
     * Deresolve the name of the given element.
     *
     * @param element
     *            the element to deresolve.
     * @param sourceNamespace
     *            the original owning namespace of the element
     * @param deresolvingNamespace
     *            the current namespace used to deresolved the name of the element
     * @param ancestors
     *            the ancestor memberships of the element from which it can be reached
     * @return a name
     */
    private String deresolve(Element element, Namespace sourceNamespace, Namespace deresolvingNamespace, Set<Membership> ancestors) {
        final String qualifiedName;
        if (deresolvingNamespace == null) {
            qualifiedName = this.getQualifiedName(element);
        } else {
            Pair<String, String> searchKey = Pair.of(deresolvingNamespace.getElementId(), element.getElementId());
            String cacheValue = this.deresolvedNamesCache.get(searchKey);
            if (cacheValue != null) {
                return cacheValue;
            }
            // should sourceNamespace be context or be contained in context, then private elements should be accessible while deresolving.
            EList<Membership> visibleMemberships = this.getVisibleMemberships(deresolvingNamespace, isContainerOrIdentity(deresolvingNamespace, sourceNamespace));
            final Stream<Membership> stream;
            // Some element have a lots of elements to checks. In that case use parallel stream
            if (visibleMemberships.size() > 100) {
                stream = visibleMemberships.parallelStream();
            } else {
                stream = visibleMemberships.stream();
            }
            Optional<Membership> importedContainer = stream.filter(ancestors::contains)
                    // Get the membership the closest to the element to deresolve
                    .sorted(Comparator.comparing(m -> this.getPathLength(element, m))).findFirst();

            if (importedContainer.isPresent()) {
                // We found a visible membership that can reach the element
                // Try to compute its qualified name
                qualifiedName = this.buildRelativeQualifiedName(element, deresolvingNamespace, importedContainer.get(), sourceNamespace);
            } else {
                // Query the parent namespace
                qualifiedName = this.deresolve(element, sourceNamespace, deresolvingNamespace.getOwningNamespace(), ancestors);
            }

            if (qualifiedName != null) {
                this.deresolvedNamesCache.put(searchKey, qualifiedName);
            }
        }

        return qualifiedName;
    }

    private int getPathLength(Element element, Membership ancestor) {
        int length = 0;
        EObject current = element;
        while (current != null && ancestor != current) {
            length++;
            current = current.eContainer();
        }
        return length;
    }

    private boolean isContainerOrIdentity(Namespace ancestorObjectNamespace, Namespace namespace) {
        if (ancestorObjectNamespace == namespace) {
            return true;
        } else {
            return EcoreUtil.isAncestor(ancestorObjectNamespace, namespace);
        }
    }

    private EList<Membership> getVisibleMemberships(Namespace deresolvingNamespace, boolean includePrivate) {
        EList<Membership> memberships = null;
        String namespaceId = deresolvingNamespace.getElementId();
        if (namespaceId != null) {
            memberships = this.visibleMembershipsCache.get(namespaceId);
        }
        if (memberships == null) {
            EList<Membership> visible = new MembershipComputer<>(deresolvingNamespace, new UniqueEList<>()).visibleMemberships(false, includePrivate, true);
            memberships = visible.stream()
                    .filter(m -> m.getMemberElement() != null)
                    .filter(m -> includePrivate || m.getVisibility() != VisibilityKind.PRIVATE)
                    .collect(toCollection(UniqueEList<Membership>::new));
            this.visibleMembershipsCache.put(namespaceId, memberships);
        }
        return memberships;
    }

    private String buildRelativeQualifiedName(Element element, Namespace owningNamespace, Membership visibleMembership, Namespace sourceNamespace) {
        String elementQn = this.getQualifiedName(element);
        if (elementQn.isEmpty()) {
            LOGGER.warn("No qualified name found for {}", element.getElementId());
            return "";
        }

        // Qualified name between visible membership and the element
        String relativeQualifiedName = this.getRelativeQualifiedName(elementQn, element, visibleMembership);

        Membership resolvedElement = sourceNamespace.resolve(relativeQualifiedName);
        // If the name resolve against an element which is not the expected element it means that there is a name
        // conflict.
        // In that case keep we need a more detailed qualified name
        if (resolvedElement != null && !this.match(element, resolvedElement)) {
            // Last try if the element is in the containment tree find the shortest qualified name
            String qualifiedName = this.getQualifiedName(owningNamespace);
            if (!qualifiedName.isBlank() && elementQn.startsWith(qualifiedName)) {
                relativeQualifiedName = owningNamespace.getName() + "::" + elementQn.substring(qualifiedName.length() + 2);
            } else {
                relativeQualifiedName = elementQn;
            }
        }

        return relativeQualifiedName;

    }

    /**
     * Checks if the resolved element is either the resolved element member or if it implicitly matches the naming
     * feature of unnamed element which name computation used the resolved element.
     *
     * @param element
     *            the element to compute the name from
     * @param resolvedElement
     *            the resolution tested name in the local namespace
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

    private String getRelativeQualifiedName(String elementQn, Element element, Membership m) {
        final String qn;
        Element importedElement = m.getMemberElement();
        String importedElementQualifiedName = this.getQualifiedName(importedElement);
        if (importedElement != element && !importedElementQualifiedName.isEmpty()) {
            int partToRemove = importedElementQualifiedName.length() + 2;
            qn = Appender.toPrintableName(importedElement.getName()) + "::" + elementQn.substring(partToRemove);
        } else {
            qn = Appender.toPrintableName(element.getName());
        }
        return qn;
    }

}
