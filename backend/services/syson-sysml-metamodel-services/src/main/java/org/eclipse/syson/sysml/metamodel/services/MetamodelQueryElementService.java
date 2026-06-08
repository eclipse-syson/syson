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
import org.eclipse.syson.sysml.BooleanExpression;
import org.eclipse.syson.sysml.ConcernUsage;
import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.FramedConcernMembership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.ResultExpressionMembership;
import org.eclipse.syson.sysml.StakeholderMembership;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.metamodel.services.textual.SysMLElementSerializer;
import org.eclipse.syson.sysml.metamodel.services.textual.SysMLSerializingOptions;
import org.eclipse.syson.sysml.metamodel.services.textual.utils.FileNameDeresolver;

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
     * Check if a given {@link Element element} is an {@link Expression expression} definition. We can not simply rely
     * on whether the element is an instance of {@link Expression expression} as many types in SysMLv2 inherit from this
     * type without being themselves an actual expressions.
     *
     * @param element
     *            the element to test.
     * @return true if the element is an actual expression definition.
     */
    public boolean isExpressionDefinition(Element element) {
        return element instanceof Expression && !(element instanceof Usage) && !(element instanceof BooleanExpression);
    }

    /**
     * Check is a given {@link Element element} is a top-level {@link Expression expression}. In most cases, end-users
     * are only interested with these and not the internal elements which are technically also expressions but represent
     * parts of the overall expression.
     *
     * @param element
     *            the element to test.
     * @return true if the element is a top-level expression definition.
     */
    public boolean isTopLevelExpression(Element element) {
        boolean result = false;
        if (this.isExpressionDefinition(element)) {
            result = true;
            var ancestor = element.getOwner();
            while (ancestor != null) {
                if (this.isExpressionDefinition(ancestor)) {
                    result = false;
                    break;
                }
                ancestor = ancestor.getOwner();
            }
        }
        return result;
    }

    /**
     * Check is a given {@code element} has a single/non-ambiguous existing {@link Expression} definition associated.
     *
     * @param element
     *            the element to test
     * @return true if the element has a single existing associated expression definition.
     */
    public boolean hasSingleExpressionDefinition(Element element) {
        return this.findSingleExpressionDefinition(element).isPresent();
    }

    /**
     * Check whether a given {@link Element element} can contain an {@link Expression}.
     *
     * @param element
     *            the element to test.
     * @return true if the element can contain an expression.
     */
    public boolean canContainExpressionDefinition(Element element) {
        return !this.getCompatibleExpressionOwnerships(element).isEmpty();
    }

    /**
     * Given an {@link Element element}, returns the list of all {@link OwningMembership owning memberships} through
     * which it can contain an {@link Expression expression}.
     *
     * @param element
     *            a SysML {@link Element element}.
     * @return the owning memberships through which the element may contain an expression.
     */
    public List<OwningMembership> getCompatibleExpressionOwnerships(Element element) {
        var result = new ArrayList<OwningMembership>();

        // KerML 8.3.4.10.2 FeatureValue: "A FeatureValue is a Membership that identifies a particular member
        // Expression that provides the value of the Feature that owns the FeatureValue."
        if (element instanceof Feature) {
            result.add(SysmlFactory.eINSTANCE.createFeatureValue());
        }

        return result;
    }

    /**
     * Check is a given {@code element} has a single/non-ambiguous existing {@link Expression} definition associated.
     *
     * @param element
     *            the element to test
     * @return true if the element has a single existing associated expression definition.
     */
    public Optional<Expression> findSingleExpressionDefinition(Element element) {
        Optional<Expression> result = Optional.empty();
        if (this.isExpressionDefinition(element)) {
            result = Optional.of((Expression) element);
        } else {
            var ownedExpressions = element.getOwnedElement().stream().filter(child -> this.isExpressionDefinition(child)).toList();
            if (ownedExpressions.size() == 1) {
                result = Optional.of((Expression) ownedExpressions.get(0));
            }
        }
        return result;
    }

    /**
     * Returns the textual representation of an {@link Expression expression}.
     *
     * @param expression
     *            an expression.
     * @return its textual representation/serialization.
     */
    public String getExpressionTextualRepresentation(Expression expression) {
        String result = "";
        if (expression != null) {
            SysMLSerializingOptions options = new SysMLSerializingOptions.Builder()
                    .lineSeparator("\n")
                    .nameDeresolver(new FileNameDeresolver())
                    .indentation("\t")
                    .needEscapeCharacter(false)
                    .build();
            String textualFormat = new SysMLElementSerializer(options, s -> {
                // Do nothing for now
            }).doSwitch(expression);
            if (textualFormat != null) {
                result = textualFormat;
            }
        }
        return result;
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

    /**
     * Returns the requirement constraint target of {@link RequirementConstraintMembership}.
     * <p>
     *     It returns a constraint when the requirement constraint membership owned constraint is subsetted by another constraint.
     * </p>
     *
     * @param requirementConstraintMembership
     *               The requirement constraint membership
     * @return the requirement constraint target of {@link RequirementConstraintMembership}
     */
    public ConstraintUsage getRequirementConstraintTarget(RequirementConstraintMembership requirementConstraintMembership) {
        if (requirementConstraintMembership.getOwnedConstraint() != requirementConstraintMembership.getReferencedConstraint()) {
            return requirementConstraintMembership.getReferencedConstraint();
        }
        return null;
    }

    /**
     * Get the {@link ResultExpressionMembership} contained inside a given {@link Namespace}.
     *
     * @param namespace
     *            a given {@link Namespace}.
     * @return a {@link ResultExpressionMembership}, or <code>null</code> if not found.
     */
    public ResultExpressionMembership getResultExpressionMembership(Namespace namespace) {
        return namespace.getOwnedMembership().stream()
                .filter(ResultExpressionMembership.class::isInstance)
                .map(ResultExpressionMembership.class::cast)
                .findFirst()
                .orElse(null);
    }
}
