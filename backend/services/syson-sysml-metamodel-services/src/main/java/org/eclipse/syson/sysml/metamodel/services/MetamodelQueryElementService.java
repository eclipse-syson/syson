/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.sysml.metamodel.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.ConcernUsage;
import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.FramedConcernMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.StakeholderMembership;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.Usage;

/**
 * Element-related services doing queries. This class should not depend on sirius-web services or other spring services.
 *
 * @author arichard
 */
public class MetamodelQueryElementService {

    /**
     * Return {@code true} if the provided {@code element} is an actor, {@code false} otherwise.
     * <p>
     * An actor (typically of a UseCase or Requirement) is a kind of parameter stored in an {@link ActorMembership}.
     * </p>
     *
     * @param element
     *            the element to check
     * @return {@code true} if the provided {@code element} is an actor, {@code false} otherwise
     */
    public boolean isActor(Element element) {
        return element instanceof PartUsage && element.getOwningMembership() instanceof ActorMembership;
    }

    /**
     * Return {@code true} if the provided {@code element} is a subject, {@code false} otherwise.
     * <p>
     * A subject (typically of a UseCase or Requirement) is a kind of parameter stored in an {@link SubjectMembership}.
     * </p>
     *
     * @param element
     *            the element to check
     * @return {@code true} if the provided {@code element} is a subject, {@code false} otherwise
     */
    public boolean isSubject(Element element) {
        return element instanceof ReferenceUsage && element.getOwningMembership() instanceof SubjectMembership;
    }

    /**
     * Return {@code true} if the provided {@code element} is a stakeholder, {@code false} otherwise.
     * <p>
     * A stakeholder is a kind of parameter stored in an {@link StakeholderMembership}.
     * </p>
     *
     * @param element
     *            the element to check
     * @return {@code true} if the provided {@code element} is an stakeholder, {@code false} otherwise
     */
    public boolean isStakeholder(Element element) {
        return element instanceof PartUsage && element.getOwningMembership() instanceof StakeholderMembership;
    }

    /**
     * Check if a given {@code element} is an {@link Expression} definition. We can not simply rely on whether the
     * element is an instance of {@link Expression} as many types in SysMLv2 inherit from this type without being
     * themselves an actual expressions.
     *
     * @param element
     *            the element to test.
     * @return true if the element is an actual expression definition.
     */
    public boolean isExpressionDefinition(Element element) {
        return element instanceof Expression && !(element instanceof Usage);
    }

    /**
     * Get the source of a {@link Connector}.
     *
     * @param connector
     *            a {@link Connector}
     * @return the source feature
     */
    public Feature getConnectorSource(Connector connector) {
        Feature sourceFeature = connector.getSourceFeature();
        if (sourceFeature != null) {
            return sourceFeature.getFeatureTarget();
        }
        return null;
    }

    /**
     * Get the targets of a {@link Connector}.
     *
     * @param connector
     *            a {@link Connector}
     * @return a list of targets
     */
    public List<Feature> getConnectorTarget(Connector connector) {
        return connector.getTargetFeature().stream()
                .filter(Objects::nonNull)
                .map(Feature::getFeatureTarget)
                .toList();
    }

    /**
     * Get the closest common owner for both given {@link Element}.
     *
     * @param e1
     *            an Element
     * @param e2
     *            another Element
     * @return an optional common owning ancestor with the expected type
     */
    public <T extends Element> Optional<T> getCommonOwnerAncestor(Element e1, Element e2, Class<T> expectedType, Predicate<T> test) {

        // Collect all owners of e1;
        List<T> e1Owners = new ArrayList<>();
        Element currentE1Owner = e1;
        while (currentE1Owner != null) {
            if (expectedType.isInstance(currentE1Owner) && (test == null || test.test(expectedType.cast(currentE1Owner)))) {
                e1Owners.add((T) currentE1Owner);
            }
            currentE1Owner = currentE1Owner.getOwner();
        }

        // Navigate on e2 owners
        Element currentE2Owner = e2;
        while (currentE2Owner != null) {
            if (e1Owners.contains(currentE2Owner)) {
                return Optional.of(expectedType.cast(currentE2Owner));
            }
            currentE2Owner = currentE2Owner.getOwner();
        }

        return Optional.empty();
    }

    /**
     * Gets the value expression of a feature (Value of the FeatureValue owned by this feature).
     *
     * @param feature
     *            a non-null feature
     * @return an optional expression
     */
    public Optional<Expression> getValueExpression(Feature feature) {
        return feature.getOwnedRelationship().stream()
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .findFirst()
                .map(FeatureValue::getValue);
    }

    /**
     * Returns the framed concern target of {@link FramedConcernMembership}.
     * <p>
     *     It returns a concern when the framed concern membership owned concern is subsetted by another concern.
     * </p>
     *
     * @param framedConcernMembership
     *               The framed concern membership
     * @return the framed concern target of {@link FramedConcernMembership}
     */
    public ConcernUsage getFramedConcernTarget(FramedConcernMembership framedConcernMembership) {
        if (framedConcernMembership.getOwnedConcern() != framedConcernMembership.getReferencedConcern()) {
            return framedConcernMembership.getReferencedConcern();
        }
        return null;
    }
}
