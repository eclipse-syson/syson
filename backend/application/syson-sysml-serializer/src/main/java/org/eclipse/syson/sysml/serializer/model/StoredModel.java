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
package org.eclipse.syson.sysml.serializer.model;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.serializer.names.NameEscaper;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.adapter.ElementAdapter;
import org.omg.sysml.lang.sysml.ConjugatedPortDefinition;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.PortDefinition;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.ReferenceSubsetting;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.Type;
import org.omg.sysml.lang.sysml.Usage;
import org.omg.sysml.lang.sysml.VariantMembership;

/**
 * Reads a SysML model from its stored features and its containment only, so that serialization never changes it.
 * <p>
 * The derived features of the metamodel (names, qualified names, relationship targets, owning namespaces, element ids,
 * name resolution) are computed by the SysML logic. That computation resolves proxies, which replaces them in place,
 * notifies and can load resources, and it attaches the logic's own adapters and caches to the elements it visits.
 * Every model read of the serializer that would need a derived feature goes through this class instead. Its answers
 * follow the SysML definitions restricted to what the model states explicitly: a name implied by a relationship that
 * the logic would compute, such as an implicit redefinition, is not seen, and a proxy is never looked through.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class StoredModel {

    private static final EReference RELATIONSHIP_TARGET = SysMLPackage.eINSTANCE.getRelationship_Target();

    /** The private field of {@link ElementAdapter} that holds an assigned element id, or {@code null}. */
    private static final Field ELEMENT_ID_FIELD = elementIdField();

    private StoredModel() {
        // Utility class.
    }

    /**
     * Returns the first stored value of a reference, which may be a proxy, without resolving it.
     *
     * @param object
     *            the object holding the reference
     * @param reference
     *            the reference to read
     * @return the first referenced element, or {@code null}
     */
    public static Element value(final EObject object, final EReference reference) {
        return values(object, reference).stream().findFirst().orElse(null);
    }

    /**
     * Returns the stored values of a reference, proxies included, without resolving them.
     *
     * @param object
     *            the object holding the reference
     * @param reference
     *            the reference to read
     * @return the referenced elements in list order
     */
    public static List<Element> values(final EObject object, final EReference reference) {
        return SafeEmfAccess.unresolvedValues(object, reference).stream()
                .filter(Element.class::isInstance)
                .map(Element.class::cast)
                .toList();
    }

    /**
     * Returns the targets of a relationship: its owned member for an owning membership, otherwise the values of the
     * stored references that redefine or subset {@code Relationship::target}, such as the type of a feature typing or
     * the imported namespace of an import. Proxies are returned unresolved.
     *
     * @param relationship
     *            the relationship, or {@code null}
     * @return the targets, without duplicates
     */
    public static List<Element> targets(final Relationship relationship) {
        List<Element> targets = List.of();
        if (relationship instanceof OwningMembership) {
            targets = relationship.getOwnedRelatedElement().stream().limit(1).toList();
        } else if (relationship != null) {
            targets = values(relationship, RELATIONSHIP_TARGET);
        }
        return targets;
    }

    /**
     * Returns the member of a membership: the owned member of an owning membership, otherwise the stored member, which
     * may be a proxy.
     *
     * @param membership
     *            the membership, or {@code null}
     * @return the member, or {@code null}
     */
    public static Element memberElement(final Membership membership) {
        Element member = null;
        if (membership instanceof OwningMembership) {
            member = membership.getOwnedRelatedElement().stream().findFirst().orElse(null);
        } else if (membership != null) {
            member = value(membership, SysMLPackage.eINSTANCE.getMembership_MemberElement());
        }
        return member;
    }

    /**
     * Returns the name under which a membership makes its member visible: the name of an owned member, or the alias
     * name of another membership.
     *
     * @param membership
     *            the membership, or {@code null}
     * @return the member name, or {@code null}
     */
    public static String memberName(final Membership membership) {
        String name = null;
        if (membership instanceof OwningMembership) {
            name = name(memberElement(membership));
        } else if (membership != null) {
            name = membership.getMemberName();
        }
        return name;
    }

    /**
     * Returns the short name under which a membership makes its member visible.
     *
     * @param membership
     *            the membership, or {@code null}
     * @return the member short name, or {@code null}
     */
    public static String memberShortName(final Membership membership) {
        String shortName = null;
        if (membership instanceof OwningMembership) {
            shortName = shortName(memberElement(membership));
        } else if (membership != null) {
            shortName = membership.getMemberShortName();
        }
        return shortName;
    }

    /**
     * Returns the owner of an element: the element owning its owning relationship, or, for a relationship, the element
     * owning it.
     *
     * @param element
     *            the element, or {@code null}
     * @return the owner, or {@code null} for a root or a {@code null} element
     */
    public static Element owner(final Element element) {
        if (element == null) {
            return null;
        }
        EObject container = element.eContainer();
        if (!(element instanceof Relationship) && container instanceof final Relationship owningRelationship) {
            container = owningRelationship.eContainer();
        }
        Element owner = null;
        if (container instanceof final Element containerElement) {
            owner = containerElement;
        }
        return owner;
    }

    /**
     * Returns the port definition that a resolved conjugated port definition conjugates: its owner, since the
     * conjugated definition is owned by the original one and has no name of its own.
     *
     * @param element
     *            the element, or {@code null}
     * @return the original port definition, or {@code null} for any other element
     */
    public static Element originalPortDefinition(final Element element) {
        Element original = null;
        if (element instanceof ConjugatedPortDefinition && !element.eIsProxy() && owner(element) instanceof final PortDefinition owner) {
            original = owner;
        }
        return original;
    }

    /**
     * Returns the namespace in which an element is a member through its owning membership.
     *
     * @param element
     *            the element, or {@code null}
     * @return the owning namespace, or {@code null} when the element is {@code null} or not owned through a membership
     */
    public static Namespace owningNamespace(final Element element) {
        Namespace namespace = null;
        if (element != null && element.eContainer() instanceof final OwningMembership membership && membership.eContainer() instanceof final Namespace owner) {
            namespace = owner;
        }
        return namespace;
    }

    /**
     * Returns the type that owns a feature through its owning relationship.
     *
     * @param feature
     *            the feature, or {@code null}
     * @return the owning type, or {@code null}
     */
    public static Type owningType(final Feature feature) {
        Type type = null;
        if (feature != null && feature.eContainer() instanceof final Relationship relationship && relationship.eContainer() instanceof final Type owner) {
            type = owner;
        }
        return type;
    }

    /**
     * Returns the effective name of an element: its declared name, or, for a feature without declared name or short
     * name, the effective name of its naming feature.
     *
     * @param element
     *            the element, or {@code null}
     * @return the name, or {@code null}
     */
    public static String name(final Element element) {
        return effectiveName(element, true, Collections.newSetFromMap(new IdentityHashMap<>()));
    }

    /**
     * Returns the effective short name of an element, following the same naming feature as {@link #name(Element)}.
     *
     * @param element
     *            the element, or {@code null}
     * @return the short name, or {@code null}
     */
    public static String shortName(final Element element) {
        return effectiveName(element, false, Collections.newSetFromMap(new IdentityHashMap<>()));
    }

    /**
     * Returns the name of an element as it is written in a qualified name: its escaped name, else its escaped short
     * name.
     *
     * @param element
     *            the element
     * @return the escaped name, or {@code null} when the element has neither
     */
    public static String escapedName(final Element element) {
        String escapedName = null;
        String name = name(element);
        if (name == null) {
            name = shortName(element);
        }
        if (name != null) {
            escapedName = new NameEscaper().escapeDeclaredName(name);
        }
        return escapedName;
    }

    /**
     * Returns the qualified name of an element from its owning namespaces: {@code null} when the element or one of its
     * owning namespaces is unnamed, or when it is not owned through memberships up to a root namespace.
     *
     * @param element
     *            the element
     * @return the qualified name, or {@code null}
     */
    public static String qualifiedName(final Element element) {
        final Namespace owningNamespace = owningNamespace(element);
        final String escapedName = escapedName(element);
        if (owningNamespace == null || escapedName == null) {
            return null;
        }
        final String qualification = qualification(owningNamespace);
        String qualifiedName = null;
        if (qualification != null && qualification.isEmpty()) {
            qualifiedName = escapedName;
        } else if (qualification != null) {
            qualifiedName = qualification + SysMLTokens.NAMESPACE_SEPARATOR + escapedName;
        }
        return qualifiedName;
    }

    /** Returns the qualified name of a namespace as a qualification: empty for a root namespace, which has no name. */
    private static String qualification(final Namespace namespace) {
        String qualification = "";
        if (owner(namespace) != null) {
            qualification = qualifiedName(namespace);
        }
        return qualification;
    }

    /**
     * Returns the element id already assigned to an element, without creating one. The id is held by the SysML logic
     * adapter of the element, which assigns a new id when it is first read; an element without that adapter or without
     * an assigned id therefore has no id here.
     *
     * @param element
     *            the element
     * @return the assigned element id, or {@code null}
     */
    @SuppressWarnings("checkstyle:IllegalCatch")
    public static String existingElementId(final Element element) {
        String elementId = null;
        if (ELEMENT_ID_FIELD != null) {
            for (final Adapter adapter : element.eAdapters()) {
                if (adapter instanceof final ElementAdapter elementAdapter) {
                    try {
                        elementId = (String) ELEMENT_ID_FIELD.get(elementAdapter);
                    } catch (final IllegalAccessException | RuntimeException exception) {
                        // The logic changed its internals; reading the id would assign one, so there is none.
                        elementId = null;
                    }
                }
            }
        }
        return elementId;
    }

    /**
     * Computes an effective name with a guard against naming cycles. The naming feature of a variant usage is its
     * referenced feature; that of another feature is the feature its first owned redefinition redefines. A proxy has
     * no name.
     */
    private static String effectiveName(final Element element, final boolean declaredName, final Set<Element> visited) {
        if (element == null || element.eIsProxy() || !visited.add(element)) {
            return null;
        }
        String name = element.getDeclaredShortName();
        if (declaredName) {
            name = element.getDeclaredName();
        }
        if (element instanceof final Feature feature && element.getDeclaredName() == null && element.getDeclaredShortName() == null) {
            name = effectiveName(namingFeature(feature), declaredName, visited);
        }
        return name;
    }

    /**
     * Returns the feature an unnamed feature is named after, from its explicit relationships only: the redefined
     * feature of its first owned redefinition, or, for a variant usage, the referenced feature of its owned reference
     * subsetting. As in the SysML logic, a relationship is owned by the feature only when the feature is its stored
     * specific end (the redefining or referencing feature).
     *
     * @param feature
     *            the feature
     * @return the naming feature, possibly a proxy, or {@code null}
     */
    public static Feature namingFeature(final Feature feature) {
        final boolean variant = feature instanceof Usage && feature.eContainer() instanceof VariantMembership;
        return feature.getOwnedRelationship().stream()
                .filter(relationship -> !relationship.isImplied())
                .map(relationship -> namingTarget(feature, relationship, variant))
                .filter(target -> target != null)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the feature that one owned relationship names the feature after: the referenced feature of a variant's
     * reference subsetting, or the redefined feature of another feature's redefinition, when the feature is its specific
     * end.
     */
    private static Feature namingTarget(final Feature feature, final Relationship relationship, final boolean variant) {
        final SysMLPackage sysml = SysMLPackage.eINSTANCE;
        Feature target = null;
        if (variant && relationship instanceof ReferenceSubsetting
                && value(relationship, sysml.getReferenceSubsetting_ReferencingFeature()) == feature) {
            target = featureValue(relationship, sysml.getReferenceSubsetting_ReferencedFeature());
        } else if (!variant && relationship instanceof Redefinition
                && value(relationship, sysml.getRedefinition_RedefiningFeature()) == feature) {
            target = featureValue(relationship, sysml.getRedefinition_RedefinedFeature());
        }
        return target;
    }

    private static Feature featureValue(final Relationship relationship, final EReference reference) {
        Feature feature = null;
        if (value(relationship, reference) instanceof final Feature target) {
            feature = target;
        }
        return feature;
    }

    @SuppressWarnings("checkstyle:IllegalCatch")
    private static Field elementIdField() {
        try {
            final Field field = ElementAdapter.class.getDeclaredField("elementId");
            field.setAccessible(true);
            return field;
        } catch (final NoSuchFieldException | RuntimeException exception) {
            // Without access to the stored id, no element id can be read without assigning one.
            return null;
        }
    }
}
