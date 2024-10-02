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
package org.eclipse.syson.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.services.grammars.DirectEditLexer;
import org.eclipse.syson.services.grammars.DirectEditListener;
import org.eclipse.syson.services.grammars.DirectEditParser;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChainExpression;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.VisibilityKind;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DiagramDirectEditListener}.
 *
 * @author gdaniel
 */
public class DiagramDirectEditListenerTest {

    private static final String GREATER_OR_EQUAL = ">=";

    private static final String CONSTRAINT_SHOULD_HAVE_ONE_OPERATOR_MESSAGE = "The constraint should have 1 owned OperatorExpression";

    @DisplayName("Given a ConstraintUsage, when it is edited with '1 >= 2', then its expression is set")
    @Test
    public void testDirectEditConstraintUsageWithBooleanExpression() {
        ConstraintUsage constraint = this.createConstraintUsageWithContext();
        this.doDirectEditOnConstraint(constraint, "1 >= 2");
        assertThat(constraint.getOwnedMember()).as(CONSTRAINT_SHOULD_HAVE_ONE_OPERATOR_MESSAGE)
                .hasSize(1)
                .allMatch(OperatorExpression.class::isInstance);
        OperatorExpression operatorExpression = (OperatorExpression) constraint.getOwnedMember().get(0);
        assertThat(operatorExpression.getOperator()).isEqualTo(GREATER_OR_EQUAL);
        assertThat(operatorExpression.getArgument()).hasSize(2);
        assertThat(operatorExpression.getArgument().get(0)).isInstanceOf(LiteralInteger.class);
        LiteralInteger literalInteger1 = (LiteralInteger) operatorExpression.getArgument().get(0);
        assertThat(literalInteger1.getValue()).isEqualTo(1);
        assertThat(operatorExpression.getArgument().get(1)).isInstanceOf(LiteralInteger.class);
        LiteralInteger literalInteger2 = (LiteralInteger) operatorExpression.getArgument().get(1);
        assertThat(literalInteger2.getValue()).isEqualTo(2);
    }

    @DisplayName("Given a ConstraintUsage, when it is edited with 'myAttribute >= 1', then its expression is set")
    @Test
    public void testDirectEditConstraintUsageWithAttributeReferenceExpression() {
        ConstraintUsage constraint = this.createConstraintUsageWithContext();
        this.doDirectEditOnConstraint(constraint, "myAttribute >= 1");
        assertThat(constraint.getOwnedMember()).as(CONSTRAINT_SHOULD_HAVE_ONE_OPERATOR_MESSAGE)
                .hasSize(1)
                .allMatch(OperatorExpression.class::isInstance);
        OperatorExpression operatorExpression = (OperatorExpression) constraint.getOwnedMember().get(0);
        assertThat(operatorExpression.getOperator()).isEqualTo(GREATER_OR_EQUAL);
        assertThat(operatorExpression.getArgument()).hasSize(2);
        assertThat(operatorExpression.getArgument().get(0)).isInstanceOf(FeatureReferenceExpression.class);
        FeatureReferenceExpression featureReferenceExpression = (FeatureReferenceExpression) operatorExpression.getArgument().get(0);
        assertThat(featureReferenceExpression.getReferent()).isInstanceOf(AttributeUsage.class);
        assertThat(featureReferenceExpression.getReferent().getName()).isEqualTo("myAttribute");
        assertThat(operatorExpression.getArgument().get(1)).isInstanceOf(LiteralInteger.class);
        LiteralInteger literalInteger = (LiteralInteger) operatorExpression.getArgument().get(1);
        assertThat(literalInteger.getValue()).isEqualTo(1);
    }

    @DisplayName("Given a ConstraintUsage and an AttributeUsage in a Namespace, when the ConstraintUsage is edited with 'externalAttribute >= 1', then its expression is set")
    @Test
    public void testDirectEditConstraintUsageWithExternalAttributeReferenceExpression() {
        Package pack = SysmlFactory.eINSTANCE.createPackage();
        ConstraintUsage constraint = this.createConstraintUsageWithContext();
        OwningMembership requirementMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        requirementMembership.getOwnedRelatedElement().add(constraint.getOwner());
        pack.getOwnedRelationship().add(requirementMembership);
        AttributeUsage externalAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        externalAttribute.setDeclaredName("externalAttribute");
        OwningMembership attributeMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        attributeMembership.getOwnedRelatedElement().add(externalAttribute);
        pack.getOwnedRelationship().add(attributeMembership);
        this.doDirectEditOnConstraint(constraint, "externalAttribute >= 1");
        assertThat(constraint.getOwnedMember()).as(CONSTRAINT_SHOULD_HAVE_ONE_OPERATOR_MESSAGE)
                .hasSize(1)
                .allMatch(OperatorExpression.class::isInstance);
        OperatorExpression operatorExpression = (OperatorExpression) constraint.getOwnedMember().get(0);
        assertThat(operatorExpression.getOperator()).isEqualTo(GREATER_OR_EQUAL);
        assertThat(operatorExpression.getArgument()).hasSize(2);
        assertThat(operatorExpression.getArgument().get(0)).isInstanceOf(FeatureReferenceExpression.class);
        FeatureReferenceExpression featureReferenceExpression = (FeatureReferenceExpression) operatorExpression.getArgument().get(0);
        assertThat(featureReferenceExpression.getReferent()).isInstanceOf(AttributeUsage.class);
        assertThat(featureReferenceExpression.getReferent().getName()).isEqualTo("externalAttribute");
        assertThat(operatorExpression.getArgument().get(1)).isInstanceOf(LiteralInteger.class);
        LiteralInteger literalInteger = (LiteralInteger) operatorExpression.getArgument().get(1);
        assertThat(literalInteger.getValue()).isEqualTo(1);
    }

    @DisplayName("Given a ConstraintUsage, when it is edited with 'subject >= 1', then its expression is set")
    @Test
    public void testDirectEditConstraintUsageWithSubjectReferenceExpression() {
        ConstraintUsage constraint = this.createConstraintUsageWithContext();
        this.doDirectEditOnConstraint(constraint, "mySubject >= 1");
        assertThat(constraint.getOwnedMember()).as(CONSTRAINT_SHOULD_HAVE_ONE_OPERATOR_MESSAGE)
                .hasSize(1)
                .allMatch(OperatorExpression.class::isInstance);
        OperatorExpression operatorExpression = (OperatorExpression) constraint.getOwnedMember().get(0);
        assertThat(operatorExpression.getOperator()).isEqualTo(GREATER_OR_EQUAL);
        assertThat(operatorExpression.getArgument()).hasSize(2);
        assertThat(operatorExpression.getArgument().get(0)).isInstanceOf(FeatureReferenceExpression.class);
        FeatureReferenceExpression featureReferenceExpression = (FeatureReferenceExpression) operatorExpression.getArgument().get(0);
        assertThat(featureReferenceExpression.getReferent()).isInstanceOf(ReferenceUsage.class);
        assertThat(featureReferenceExpression.getReferent().getName()).isEqualTo("mySubject");
        assertThat(operatorExpression.getArgument().get(1)).isInstanceOf(LiteralInteger.class);
        LiteralInteger literalInteger = (LiteralInteger) operatorExpression.getArgument().get(1);
        assertThat(literalInteger.getValue()).isEqualTo(1);

    }

    @DisplayName("Given a ConstraintUsage, when it is edited with 'feature1.feature2 >= 1', then its expression is set")
    @Test
    public void testDirectEditConstraintUsageWithSingleFeatureChainingExpression() {
        ConstraintUsage constraint = this.createConstraintUsageWithContext();
        this.doDirectEditOnConstraint(constraint, "mySubject.actualWeight >= 1");
        assertThat(constraint.getOwnedMember()).as(CONSTRAINT_SHOULD_HAVE_ONE_OPERATOR_MESSAGE)
                .hasSize(1)
                .allMatch(OperatorExpression.class::isInstance);
        OperatorExpression operatorExpression = (OperatorExpression) constraint.getOwnedMember().get(0);
        assertThat(operatorExpression.getOperator()).isEqualTo(GREATER_OR_EQUAL);
        assertThat(operatorExpression.getArgument()).hasSize(2);
        assertThat(operatorExpression.getArgument().get(0)).isInstanceOf(FeatureChainExpression.class);
        FeatureChainExpression featureChainExpression = (FeatureChainExpression) operatorExpression.getArgument().get(0);
        assertThat(featureChainExpression.getArgument())
                .as("The feature chain expression should have 1 FeatureReferenceExpression argument")
                .hasSize(1)
                .allMatch(FeatureReferenceExpression.class::isInstance);
        FeatureReferenceExpression featureReferenceExpression = (FeatureReferenceExpression) featureChainExpression.getArgument().get(0);
        assertThat(featureReferenceExpression.getReferent()).isInstanceOf(ReferenceUsage.class);
        assertThat(featureReferenceExpression.getReferent().getName()).isEqualTo("mySubject");
        assertThat(featureChainExpression.getTargetFeature())
                .as("The target feature shouldn't contain chaining features and should be named 'actualWeight'")
                .returns(List.of(), Feature::getChainingFeature)
                .returns("actualWeight", Feature::getDeclaredName);
        assertThat(operatorExpression.getArgument().get(1)).isInstanceOf(LiteralInteger.class);
        LiteralInteger literalInteger = (LiteralInteger) operatorExpression.getArgument().get(1);
        assertThat(literalInteger.getValue()).isEqualTo(1);
    }

    @DisplayName("Given a ConstraintUsage, when it is edited with 'feature1.feature2.feature3 >= 1', then its expression is set")
    @Test
    public void testDirectEditConstraintUsageWithMultipleFeatureChainingExpression() {
        ConstraintUsage constraint = this.createConstraintUsageWithContext();
        this.doDirectEditOnConstraint(constraint, "mySubject.actualWeight.num >= 1");
        assertThat(constraint.getOwnedMember()).as(CONSTRAINT_SHOULD_HAVE_ONE_OPERATOR_MESSAGE)
                .hasSize(1)
                .allMatch(OperatorExpression.class::isInstance);
        OperatorExpression operatorExpression = (OperatorExpression) constraint.getOwnedMember().get(0);
        assertThat(operatorExpression.getOperator()).isEqualTo(GREATER_OR_EQUAL);
        assertThat(operatorExpression.getArgument()).hasSize(2);
        assertThat(operatorExpression.getArgument().get(0)).isInstanceOf(FeatureChainExpression.class);
        FeatureChainExpression featureChainExpression = (FeatureChainExpression) operatorExpression.getArgument().get(0);
        assertThat(featureChainExpression.getArgument())
                .as("The feature chain expression should have 1 FeatureReferenceExpression argument")
                .hasSize(1)
                .allMatch(FeatureReferenceExpression.class::isInstance);
        FeatureReferenceExpression featureReferenceExpression = (FeatureReferenceExpression) featureChainExpression.getArgument().get(0);
        assertThat(featureReferenceExpression.getReferent()).isInstanceOf(ReferenceUsage.class);
        assertThat(featureReferenceExpression.getReferent().getName()).isEqualTo("mySubject");
        assertThat(featureChainExpression.getTargetFeature()).isNotNull();
        assertThat(featureChainExpression.getTargetFeature().getChainingFeature()).hasSize(2);
        List<Feature> chainingFeatures = featureChainExpression.getTargetFeature().getChainingFeature();
        assertThat(chainingFeatures.get(0)).isInstanceOf(AttributeUsage.class)
                .returns("actualWeight", Feature::getName);
        assertThat(chainingFeatures.get(1)).isInstanceOf(AttributeUsage.class)
                .returns("num", Feature::getName);

        assertThat(operatorExpression.getArgument().get(1)).isInstanceOf(LiteralInteger.class);
        LiteralInteger literalInteger = (LiteralInteger) operatorExpression.getArgument().get(1);
        assertThat(literalInteger.getValue()).isEqualTo(1);
    }

    @DisplayName("Given a PartUsage as graphical node, when it is edited with '[4]', then its multiplicity is set")
    @Test
    public void testDirectEditPartUsageNodeWithMultiplicity() {
        PartUsage partUsage = this.createFlashlight();
        this.doDirectEditOnNode(partUsage, "[4]");
        var optMultiplicityRange = partUsage.getOwnedRelationship().stream()
                .filter(OwningMembership.class::isInstance)
                .map(OwningMembership.class::cast)
                .flatMap(m -> m.getOwnedRelatedElement().stream())
                .filter(MultiplicityRange.class::isInstance)
                .map(MultiplicityRange.class::cast)
                .findFirst();
        assertTrue(optMultiplicityRange.isPresent());
        MultiplicityRange multiplicityRange = optMultiplicityRange.get();
        Expression lowerBound = multiplicityRange.getLowerBound();
        assertTrue(lowerBound instanceof LiteralInteger);
        LiteralInteger lowerBoundLiteral = (LiteralInteger) lowerBound;
        assertEquals(4, lowerBoundLiteral.getValue());
    }

    @DisplayName("Given a PartUsage as graphical node, when it is edited with '<1.1>', then its short name is set and its name is unchanged")
    @Test
    public void testDirectEditPartUsageNodeWithShortName() {
        PartUsage partUsage = this.createFlashlight();
        String initialName = partUsage.getName();
        this.doDirectEditOnNode(partUsage, "<1.1>");
        assertThat(partUsage.getShortName()).isEqualTo("1.1");
        assertThat(partUsage.getName()).isEqualTo(initialName);
    }

    @DisplayName("Given a PartUsage with a short name as graphical node, when it is edited with '<>', then its short name is deleted and its name is unchanged")
    @Test
    public void testDirectEditPartUsageNodeRemoveShortName() {
        PartUsage partUsage = this.createFlashlight();
        String initialName = partUsage.getName();
        partUsage.setDeclaredShortName("1.1");
        this.doDirectEditOnNode(partUsage, "<>");
        assertThat(partUsage.getShortName()).isEmpty();
        assertThat(partUsage.getName()).isEqualTo(initialName);
    }

    @DisplayName("Given a PartUsage as graphical compartment list item, when it is edited with '[4]', then its multiplicity is set")
    @Test
    public void testDirectEditPartUsageListItemWithMultiplicity() {
        PartUsage partUsage = this.createFlashlight();
        this.doDirectEditOnCompartmentListItem(partUsage, "[4]");
        var optMultiplicityRange = partUsage.getOwnedRelationship().stream()
                .filter(OwningMembership.class::isInstance)
                .map(OwningMembership.class::cast)
                .flatMap(m -> m.getOwnedRelatedElement().stream())
                .filter(MultiplicityRange.class::isInstance)
                .map(MultiplicityRange.class::cast)
                .findFirst();
        assertTrue(optMultiplicityRange.isPresent());
        MultiplicityRange multiplicityRange = optMultiplicityRange.get();
        Expression lowerBound = multiplicityRange.getLowerBound();
        assertTrue(lowerBound instanceof LiteralInteger);
        LiteralInteger lowerBoundLiteral = (LiteralInteger) lowerBound;
        assertEquals(4, lowerBoundLiteral.getValue());
    }

    @DisplayName("Given a PartUsage as graphical compartment list item, when it is edited with '<1.1>', then its short name is set and its name is unchanged")
    @Test
    public void testDirectEditPartUsageListItemWithShortName() {
        PartUsage partUsage = this.createFlashlight();
        String initialName = partUsage.getName();
        this.doDirectEditOnCompartmentListItem(partUsage, "<1.1>");
        assertThat(partUsage.getShortName()).isEqualTo("1.1");
        assertThat(partUsage.getName()).isEqualTo(initialName);
    }

    @DisplayName("Given a PartUsage with a short name as graphical compartment list item, when it is edited with '<>', then its short name is deleted and its name is unchanged")
    @Test
    public void testDirectEditPartUsageListItemRemoveShortName() {
        PartUsage partUsage = this.createFlashlight();
        partUsage.setDeclaredShortName("1.1");
        String initialName = partUsage.getName();
        this.doDirectEditOnCompartmentListItem(partUsage, "<>");
        assertThat(partUsage.getShortName()).isEmpty();
        assertThat(partUsage.getName()).isEqualTo(initialName);
    }

    /**
     * Creates a {@link ConstraintUsage} with the required context to test direct edit.
     * <p>
     * This method creates a {@link RequirementUsage} containing the constraint, an {@link AttributeUsage} in the
     * requirement, as well as the {@code subject} of the constraint. The created constraint has a name and no
     * expression.
     * </p>
     *
     * @return the created constraint
     */
    private ConstraintUsage createConstraintUsageWithContext() {
        RequirementUsage requirementUsage = SysmlFactory.eINSTANCE.createRequirementUsage();
        requirementUsage.setDeclaredName("myRequirement");
        // Create the constraint inside the requirement
        RequirementConstraintMembership requirementConstraintMembership = SysmlFactory.eINSTANCE.createRequirementConstraintMembership();
        requirementUsage.getOwnedRelationship().add(requirementConstraintMembership);
        ConstraintUsage constraintUsage = SysmlFactory.eINSTANCE.createConstraintUsage();
        requirementConstraintMembership.getOwnedRelatedElement().add(constraintUsage);
        constraintUsage.setDeclaredName("myConstraint");
        // Create the subject of the requirement
        SubjectMembership subjectMembership = SysmlFactory.eINSTANCE.createSubjectMembership();
        requirementUsage.getOwnedRelationship().add(subjectMembership);
        ReferenceUsage referenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
        subjectMembership.getOwnedRelatedElement().add(referenceUsage);
        referenceUsage.setDeclaredName("mySubject");
        FeatureTyping referenceFeatureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
        referenceUsage.getOwnedRelationship().add(referenceFeatureTyping);
        referenceFeatureTyping.setSpecific(referenceUsage);
        referenceFeatureTyping.setType(this.createFlashlight());
        // Create the attribute of the requirement
        FeatureMembership featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        requirementUsage.getOwnedRelationship().add(featureMembership);
        AttributeUsage requirementAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        featureMembership.getOwnedRelatedElement().add(requirementAttribute);
        requirementAttribute.setDeclaredName("myAttribute");
        return constraintUsage;
    }

    /**
     * Creates a {@link PartUsage} representing a flashlight with an {@code actualWeight : Mass} attribute.
     * <p>
     * The {@code Mass} {@link AttributeDefinition} is created next to the {@link PartUsage}, and is not retrieved from
     * standard libraries. It contains a {@code num} attribute, allowing to test direct edit on multiple feature
     * chaining.
     * </p>
     *
     * @return the part representing the flashlight
     */
    private PartUsage createFlashlight() {
        PartUsage flashlight = SysmlFactory.eINSTANCE.createPartUsage();
        flashlight.setDeclaredName("flashlight");
        FeatureMembership actualWeightMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        flashlight.getOwnedRelationship().add(actualWeightMembership);
        actualWeightMembership.setVisibility(VisibilityKind.PUBLIC);
        AttributeUsage actualWeight = SysmlFactory.eINSTANCE.createAttributeUsage();
        actualWeightMembership.getOwnedRelatedElement().add(actualWeight);
        actualWeight.setDeclaredName("actualWeight");
        AttributeDefinition massDefinition = SysmlFactory.eINSTANCE.createAttributeDefinition();
        massDefinition.setDeclaredName("Mass");
        FeatureMembership numMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        massDefinition.getOwnedRelationship().add(numMembership);
        numMembership.setVisibility(VisibilityKind.PUBLIC);
        AttributeUsage num = SysmlFactory.eINSTANCE.createAttributeUsage();
        numMembership.getOwnedRelatedElement().add(num);
        num.setDeclaredName("num");
        // Set the type of actualWeight to mass
        FeatureTyping actualWeightTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
        actualWeight.getOwnedRelationship().add(actualWeightTyping);
        actualWeightTyping.setSpecific(actualWeight);
        actualWeightTyping.setType(massDefinition);
        return flashlight;
    }

    private void doDirectEditOnNode(Element element, String input) {
        DirectEditLexer lexer = new DirectEditLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DirectEditParser parser = new DirectEditParser(tokens);
        ParseTree tree = parser.nodeExpression();
        this.doDirectEdit(element, tree);
    }

    private void doDirectEditOnCompartmentListItem(Element element, String input) {
        DirectEditLexer lexer = new DirectEditLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DirectEditParser parser = new DirectEditParser(tokens);
        ParseTree tree = parser.listItemExpression();
        this.doDirectEdit(element, tree);
    }

    private void doDirectEditOnConstraint(ConstraintUsage element, String input) {
        DirectEditLexer lexer = new DirectEditLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DirectEditParser parser = new DirectEditParser(tokens);
        ParseTree tree = parser.constraintExpression();
        this.doDirectEdit(element, tree);
    }

    private void doDirectEdit(Element element, ParseTree tree) {
        ParseTreeWalker walker = new ParseTreeWalker();
        DirectEditListener listener = new DiagramDirectEditListener(element, new IFeedbackMessageService.NoOp());
        walker.walk(listener, tree);
    }
}
