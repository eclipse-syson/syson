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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.EndFeatureMembership;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.FeatureValue;
import org.omg.sysml.lang.sysml.FlowEnd;
import org.omg.sysml.lang.sysml.FlowUsage;
import org.omg.sysml.lang.sysml.Import;
import org.omg.sysml.lang.sysml.InterfaceUsage;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Multiplicity;
import org.omg.sysml.lang.sysml.Package;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.Subclassification;
import org.omg.sysml.lang.sysml.Subsetting;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Containment navigation over the explicit syntax of a SysML model: owned relationships, owned related elements,
 * parameters, body members, connector ends, feature values, operands and containers.
 * <p>
 * Look here when the serializer emits or skips the wrong <em>owned</em> element, visits members in the wrong order, or
 * picks the wrong nested expression. Implied relationships are excluded everywhere: the parser never writes them, so
 * they cannot contribute syntax. Reference names and proxy resolution belong to {@link ReferenceTargets}.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ModelNavigation {

    /**
     * Returns owned relationships that can contribute to serialized syntax.
     *
     * @param element
     *            the relationship owner, possibly {@code null}
     * @return an immutable snapshot of explicit owned relationships, in model order
     */
    public List<Relationship> ownedRelationships(final Element element) {
        if (element == null) {
            return List.of();
        }
        final List<Relationship> relationships = new ArrayList<>();
        for (final Relationship relationship : element.getOwnedRelationship()) {
            if (!relationship.isImplied()) {
                relationships.add(relationship);
            }
        }
        return Collections.unmodifiableList(relationships);
    }

    /** Returns the live EMF list for a relationship, or an immutable empty list for null. */
    public List<Element> ownedRelatedElements(final Relationship relationship) {
        if (relationship == null) {
            return List.of();
        }
        return relationship.getOwnedRelatedElement();
    }

    /**
     * Returns the elements owned through explicit parameter memberships, such as action, calculation and message
     * parameters, in declaration order.
     */
    public List<Element> parameterElements(final Element element) {
        return this.elementsOwnedThrough(element, SysMLPackage.eINSTANCE.getParameterMembership());
    }

    /** Returns the {@link #parameterElements(Element) parameters} that are features, e.g. invocation arguments. */
    public List<Feature> parameterFeatures(final Element element) {
        return this.parameterElements(element).stream()
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .toList();
    }

    /**
     * Returns the elements owned through exact {@code FeatureMembership} relationships, excluding specializations such
     * as parameter or end memberships.
     */
    public List<Element> featureMembershipElements(final Element element) {
        return this.elementsOwnedThrough(element, SysMLPackage.eINSTANCE.getFeatureMembership());
    }

    /**
     * Returns the explicit relationships rendered inside an element body. Declaration-part relationships (typing,
     * specialization, feature values and multiplicity) are excluded because the declaration header emits them.
     */
    public List<Relationship> bodyRelationships(final Element element) {
        return this.ownedRelationships(element).stream()
                .filter(this::isBodyRelationship)
                .toList();
    }

    /**
     * Tells whether a relationship belongs to the element body rather than to the declaration header (typing,
     * subclassification, subsetting, feature value or owned multiplicity).
     */
    public boolean isBodyRelationship(final Relationship relationship) {
        if (relationship == null) {
            return false;
        }
        final boolean specializationPart = relationship instanceof FeatureTyping
                || relationship instanceof Subclassification
                || relationship instanceof Subsetting;
        return !specializationPart && !(relationship instanceof FeatureValue) && !this.ownsMultiplicity(relationship);
    }

    /**
     * Returns the exact {@code ReferenceUsage} ends of a connector-like body, such as {@code connect a to b}. Only
     * references owned through an end feature membership are ends; a {@code ref extra;} of the body is not.
     */
    public List<Element> referenceEnds(final Element connector) {
        return this.bodyRelationships(connector).stream()
                .filter(EndFeatureMembership.class::isInstance)
                .flatMap(relationship -> this.ownedRelatedElements(relationship).stream())
                .filter(element -> this.isMetaclass(element, SysMLPackage.eINSTANCE.getReferenceUsage()))
                .toList();
    }

    /** Returns the {@link FlowEnd} ends of a flow usage, used for {@code flow from a.x to b.y}. */
    public List<Element> flowEnds(final FlowUsage flowUsage) {
        return this.bodyElements(flowUsage)
                .filter(FlowEnd.class::isInstance)
                .toList();
    }

    /** Returns the event occurrence parameters of a message, used for {@code message from a to b}. */
    public List<Element> messageEnds(final FlowUsage flowUsage) {
        return this.parameterElements(flowUsage).stream()
                .filter(element -> this.isMetaclass(element, SysMLPackage.eINSTANCE.getEventOccurrenceUsage()))
                .toList();
    }

    /**
     * Returns the exact {@code PortUsage} ends of an interface usage, used for {@code interface connect a to b}. Only
     * ports owned through an end feature membership are ends; a {@code port q;} of the body is not.
     */
    public List<Element> interfaceEnds(final InterfaceUsage interfaceUsage) {
        return this.bodyRelationships(interfaceUsage).stream()
                .filter(this::isInterfaceEndMembership)
                .flatMap(relationship -> this.ownedRelatedElements(relationship).stream())
                .filter(this::isExactPortUsage)
                .toList();
    }

    /** Returns interface body relationships other than the port ends already emitted by the connect clause. */
    public List<Relationship> interfaceBodyRelationships(final InterfaceUsage interfaceUsage) {
        return this.bodyRelationships(interfaceUsage).stream()
                .filter(relationship -> !this.isInterfaceEndMembership(relationship))
                .toList();
    }

    /** Tells whether the relationship is an end feature membership owning a port, i.e. an interface end. */
    private boolean isInterfaceEndMembership(final Relationship relationship) {
        return relationship instanceof EndFeatureMembership
                && this.ownedRelatedElements(relationship).stream().anyMatch(this::isExactPortUsage);
    }

    /** Returns the explicit feature values ({@code = expr}, {@code := expr}, {@code default}) owned by an element. */
    public List<FeatureValue> featureValues(final Element element) {
        return this.ownedRelationships(element).stream()
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .toList();
    }

    /**
     * Returns the expression a feature value owns, or {@code null}; the derived {@code value} getter is not used, since
     * the SysML logic computes it.
     */
    public Expression featureValueExpression(final FeatureValue featureValue) {
        return this.ownedRelatedElements(featureValue).stream()
                .filter(Expression.class::isInstance)
                .map(Expression.class::cast)
                .findFirst()
                .orElse(null);
    }

    /** Returns the result expression of a calculation-like body (its {@code ResultExpressionMembership}), or null. */
    public Expression resultExpression(final Element element) {
        return this.ownedRelationships(element).stream()
                .filter(relationship -> this.isMetaclass(relationship, SysMLPackage.eINSTANCE.getResultExpressionMembership()))
                .flatMap(relationship -> this.ownedRelatedElements(relationship).stream())
                .filter(Expression.class::isInstance)
                .map(Expression.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the argument expressions of an operator or invocation expression. Arguments are the feature values of
     * owned member features or, when such a feature has no value, of its own parameters.
     */
    public List<Expression> operands(final Element expression) {
        return this.ownedMembershipFeatures(expression)
                .flatMap(this::argumentFeatures)
                .flatMap(feature -> this.featureValues(feature).stream())
                .map(this::featureValueExpression)
                .filter(Expression.class::isInstance)
                .toList();
    }

    /**
     * Returns the first expression owned through an explicit relationship of the element, such as the expression
     * wrapped by a feature reference or an assignment parameter; deeper descendants are not searched.
     */
    public Expression firstOwnedExpression(final Element element) {
        return this.firstOwnedRelatedElement(element, Expression.class);
    }

    /**
     * Returns the import wrapped by a filtered import ({@code import a::*[@M]}), whose parser representation nests the
     * real import in an owned package; returns the import itself when it is not filtered.
     */
    public Import nestedImport(final Import importRelationship) {
        return this.filterPackageRelationships(importRelationship)
                .filter(Import.class::isInstance)
                .map(Import.class::cast)
                .findFirst()
                .orElse(importRelationship);
    }

    /** Returns the filter conditions of a filtered import ({@code import a::*[cond]}), in source order. */
    public List<Expression> importFilterExpressions(final Import importRelationship) {
        return this.filterPackageRelationships(importRelationship)
                .filter(relationship -> this.isMetaclass(relationship, SysMLPackage.eINSTANCE.getElementFilterMembership()))
                .flatMap(relationship -> this.ownedRelatedElements(relationship).stream())
                .filter(Expression.class::isInstance)
                .map(Expression.class::cast)
                .toList();
    }

    /**
     * Tells whether a state subaction is the empty placeholder ({@code entry;}, grammar {@code EmptyActionUsage}): an
     * exact {@code ActionUsage} without name, short name or owned relationships. A {@code PerformActionUsage}, such as
     * {@code entry action <e>;} or {@code entry action;}, is never the placeholder.
     */
    public boolean isEmptyStateEntryAction(final Element action) {
        return this.isMetaclass(action, SysMLPackage.eINSTANCE.getActionUsage())
                && isBlank(action.getDeclaredName())
                && isBlank(action.getDeclaredShortName())
                && this.ownedRelationships(action).isEmpty();
    }

    private static boolean isBlank(final String name) {
        return name == null || name.isBlank();
    }

    /** Returns the nearest containing SysML element, skipping non-element EMF containers. */
    public Element containingElement(final EObject object) {
        return this.containers(object)
                .filter(Element.class::isInstance)
                .map(Element.class::cast)
                .findFirst()
                .orElse(null);
    }

    /** Tells whether any container of an element has exactly the given metaclass, e.g. to detect nesting in a flow. */
    public boolean isOwnedByMetaclass(final Element element, final EClass metaclass) {
        return this.containers(element).anyMatch(container -> container instanceof Element && this.isMetaclass(container, metaclass));
    }

    /** Tells whether any container of an element has exactly one of the two metaclasses, walking the containers once. */
    public boolean isOwnedByMetaclass(final Element element, final EClass metaclass, final EClass otherMetaclass) {
        return this.containers(element).anyMatch(container -> container instanceof Element
                && (container.eClass() == metaclass || container.eClass() == otherMetaclass));
    }

    /**
     * Tests the exact EMF metaclass. Unlike {@code instanceof}, this deliberately excludes instances of derived
     * metaclasses.
     */
    public boolean isMetaclass(final EObject object, final EClass clazz) {
        return object != null && object.eClass() == clazz;
    }

    /**
     * Returns the first element of the given type owned through an explicit relationship of the element, without
     * descending further.
     */
    <T extends Element> T firstOwnedRelatedElement(final Element element, final Class<T> type) {
        return this.ownedRelationships(element).stream()
                .flatMap(relationship -> this.ownedRelatedElements(relationship).stream())
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the first non-blank text produced for an owned descendant, depth first: a descendant's own text wins over
     * its children's.
     */
    String firstNestedText(final Element element, final Function<Element, String> textProvider) {
        for (final Relationship relationship : this.ownedRelationships(element)) {
            for (final Element related : this.ownedRelatedElements(relationship)) {
                String text = textProvider.apply(related);
                if (text.isBlank()) {
                    text = this.firstNestedText(related, textProvider);
                }
                if (!text.isBlank()) {
                    return text;
                }
            }
        }
        return "";
    }

    /** Returns the features owned through the explicit memberships of an element, i.e. expression argument slots. */
    Stream<Feature> ownedMembershipFeatures(final Element element) {
        return this.ownedRelationships(element).stream()
                .filter(Membership.class::isInstance)
                .flatMap(membership -> this.ownedRelatedElements(membership).stream())
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast);
    }

    /** An argument slot without a value of its own carries the arguments on its parameters (invocation arguments). */
    private Stream<Feature> argumentFeatures(final Feature feature) {
        Stream<Feature> features = Stream.of(feature);
        if (this.featureValues(feature).isEmpty()) {
            final List<Feature> parameters = this.parameterFeatures(feature);
            if (!parameters.isEmpty()) {
                features = parameters.stream();
            }
        }
        return features;
    }

    private boolean ownsMultiplicity(final Relationship relationship) {
        // Called for every relationship of every body: a plain loop avoids building a stream per relationship.
        for (final Element related : relationship.getOwnedRelatedElement()) {
            if (related instanceof Multiplicity) {
                return true;
            }
        }
        return false;
    }

    private Stream<Element> bodyElements(final Element element) {
        return this.bodyRelationships(element).stream()
                .flatMap(relationship -> this.ownedRelatedElements(relationship).stream());
    }

    private boolean isExactPortUsage(final Element element) {
        return this.isMetaclass(element, SysMLPackage.eINSTANCE.getPortUsage());
    }

    /** Returns the body relationships of the package that the parser wraps around a filtered import. */
    private Stream<Relationship> filterPackageRelationships(final Import importRelationship) {
        return this.ownedRelatedElements(importRelationship).stream()
                .filter(Package.class::isInstance)
                .flatMap(filterPackage -> this.bodyRelationships(filterPackage).stream());
    }

    private Stream<EObject> containers(final EObject object) {
        EObject firstContainer = null;
        if (object != null) {
            firstContainer = object.eContainer();
        }
        return Stream.iterate(firstContainer, Objects::nonNull, EObject::eContainer);
    }

    private List<Element> elementsOwnedThrough(final Element element, final EClass relationshipClass) {
        return this.ownedRelationships(element).stream()
                .filter(relationship -> this.isMetaclass(relationship, relationshipClass))
                .flatMap(relationship -> this.ownedRelatedElements(relationship).stream())
                .toList();
    }
}
