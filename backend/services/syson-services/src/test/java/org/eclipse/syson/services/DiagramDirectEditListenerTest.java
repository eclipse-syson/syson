/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.services.data.ItemAndAttributesModelTest;
import org.eclipse.syson.services.data.SmallFlashlightExample;
import org.eclipse.syson.services.grammars.DirectEditLexer;
import org.eclipse.syson.services.grammars.DirectEditParser;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChainExpression;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.LiteralRational;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.util.NamedProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DiagramDirectEditListener}.
 *
 * @author gdaniel
 */
public class DiagramDirectEditListenerTest {

    private static final String A_FEATURE_NAME = "aFeatureName";

    private static final String GREATER_OR_EQUAL = ">=";

    private static final String CONSTRAINT_SHOULD_HAVE_ONE_OPERATOR_MESSAGE = "The constraint should have 1 owned OperatorExpression";

    @DisplayName("GIVEN a ConstraintUsage, WHEN it is edited with '1 >= 2', THEN its expression is set")
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

    @DisplayName("GIVEN a Feature with a FeatureValue, WHEN it is edited with ':=' symbol, THEN its FeatureValue should have its isInital property modified")
    @Test
    public void testDirectEditFeatureWithInitialValue() {
        Feature feature = SysmlFactory.eINSTANCE.createFeature();
        this.doDirectEditOnCompartmentListItem(feature, "aFeatureName := 2");
        assertEquals(A_FEATURE_NAME, feature.getName());
        this.checkHasInitialValue(feature, true);

        FeatureValue featureValue = (FeatureValue) feature.getOwnedRelationship().get(0);

        this.hasIntegerValue(featureValue, 2);

        this.doDirectEditOnCompartmentListItem(feature, "aFeatureName = 2");

        assertEquals(A_FEATURE_NAME, feature.getName());
        this.checkHasInitialValue(feature, false);

        featureValue = (FeatureValue) feature.getOwnedRelationship().get(0);

        this.hasIntegerValue(featureValue, 2);

        // Partial direct edit
        this.doDirectEditOnCompartmentListItem(feature, ":= 3");
        assertEquals(A_FEATURE_NAME, feature.getName());
        this.checkHasInitialValue(feature, true);
        this.hasIntegerValue(featureValue, 3);
    }

    @DisplayName("GIVEN a Feature with a FeatureValue, WHEN it is edited with 'default' symbol, THEN its FeatureValue should have its isDefault property modified")
    @Test
    public void testDirectEditFeatureWithDefaultValue() {
        Feature feature = SysmlFactory.eINSTANCE.createFeature();
        this.doDirectEditOnCompartmentListItem(feature, "aFeatureName default = 2");
        assertEquals(A_FEATURE_NAME, feature.getName());
        this.checkHasDefaultValue(feature, true);

        FeatureValue featureValue = (FeatureValue) feature.getOwnedRelationship().get(0);

        this.hasIntegerValue(featureValue, 2);

        this.doDirectEditOnCompartmentListItem(feature, "aFeatureName = 2");

        assertEquals(A_FEATURE_NAME, feature.getName());
        this.checkHasDefaultValue(feature, false);

        featureValue = (FeatureValue) feature.getOwnedRelationship().get(0);

        this.hasIntegerValue(featureValue, 2);
    }

    @DisplayName("GIVEN an Element, WHEN editing its name with the symbol 'default', THEN it should be allowed if not suffixed with a ' ' ")
    @Test
    public void testDirectEditElementWithNameAndDefaultSymbole() {
        Feature feature = SysmlFactory.eINSTANCE.createFeature();
        this.doDirectEditOnCompartmentListItem(feature, "defaultAttr1");
        assertEquals("defaultAttr1", feature.getName());

        this.doDirectEditOnCompartmentListItem(feature, "default Attr1");
        assertEquals("default Attr1", feature.getName());

        // Prevent the use of ' default' keyword
        this.doDirectEditOnCompartmentListItem(feature, "Attr1 default");
        assertEquals("Attr1", feature.getName());

        this.doDirectEditOnCompartmentListItem(feature, "Attr1 default := 3");
        assertEquals("Attr1", feature.getName());
        this.checkHasInitialValue(feature, true);
        FeatureValue featureValue = (FeatureValue) feature.getOwnedRelationship().get(0);
        this.hasIntegerValue(featureValue, 3);
    }

    @DisplayName("GIVEN a ConstraintUsage, WHEN it is edited with 'myAttribute >= 1', THEN its expression is set")
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

    @DisplayName("GIVEN a ConstraintUsage, WHEN it is edited with 'myAttribute >= 1 [nm]', THEN its expression is correctly built")
    @Test
    public void testDirectEditConstraintUsageWithBracketExpression() {

        SmallFlashlightExample smallFlashlightExample = new SmallFlashlightExample();
        ConstraintUsage constraint = smallFlashlightExample.getFlashlightConstraint();

        this.doDirectEditOnConstraint(constraint, "actualWeight <= 0.25 [nm]");
        assertThat(constraint.getOwnedMember()).as(CONSTRAINT_SHOULD_HAVE_ONE_OPERATOR_MESSAGE)
                .hasSize(1)
                .allMatch(OperatorExpression.class::isInstance);
        OperatorExpression operatorExpression = (OperatorExpression) constraint.getOwnedMember().get(0);
        assertThat(operatorExpression.getOperator()).isEqualTo("<=");
        assertThat(operatorExpression.getArgument()).hasSize(2);

        Expression firstParam = operatorExpression.getArgument().get(0);
        assertThat(firstParam).isInstanceOf(FeatureReferenceExpression.class);
        FeatureReferenceExpression featureReferenceExpression = (FeatureReferenceExpression) operatorExpression.getArgument().get(0);
        assertThat(featureReferenceExpression.getReferent()).isInstanceOf(AttributeUsage.class);
        assertThat(featureReferenceExpression.getReferent().getName()).isEqualTo("actualWeight");

        Expression secondParam = operatorExpression.getArgument().get(1);
        assertThat(secondParam).isInstanceOf(OperatorExpression.class);
        OperatorExpression bracketExpression = (OperatorExpression) secondParam;
        assertThat(bracketExpression.getOperator()).isEqualTo("[");

        assertThat(bracketExpression.getParameter().get(0).getValuation().getValue())
                .isInstanceOf(LiteralRational.class)
                .extracting(l -> ((LiteralRational) l).getValue())
                .isEqualTo(0.25);

        assertThat(bracketExpression.getParameter().get(1).getValuation().getValue())
                .isInstanceOf(FeatureReferenceExpression.class)
                .extracting(l -> ((FeatureReferenceExpression) l).getReferent())
                .isEqualTo(smallFlashlightExample.getNumAttributeUsage());
    }

    @DisplayName("GIVEN a ConstraintUsage and an AttributeUsage in a Namespace, WHEN the ConstraintUsage is edited with 'externalAttribute >= 1', THEN its expression is set")
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

    @DisplayName("GIVEN a ConstraintUsage, WHEN it is edited with 'subject >= 1', THEN its expression is set")
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

    @DisplayName("GIVEN a ConstraintUsage, WHEN it is edited with 'feature1.feature2 >= 1', THEN its expression is set")
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

    @DisplayName("GIVEN a ConstraintUsage, WHEN it is edited with 'feature1.feature2.feature3 >= 1', THEN its expression is set")
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

    @DisplayName("GIVEN a PartUsage as graphical node, WHEN it is edited with '[4]', THEN its multiplicity is set")
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

    @DisplayName("GIVEN a PartUsage as graphical node, WHEN it is edited with '[]', THEN its multiplicity is deleted")
    @Test
    public void testDirectEditPartUsageNodeWithMultiplicityDeleted() {
        PartUsage partUsage = this.createFlashlight();
        this.doDirectEditOnNode(partUsage, "[]");
        var optMultiplicityRange = partUsage.getOwnedRelationship().stream()
                .filter(OwningMembership.class::isInstance)
                .map(OwningMembership.class::cast)
                .flatMap(m -> m.getOwnedRelatedElement().stream())
                .filter(MultiplicityRange.class::isInstance)
                .map(MultiplicityRange.class::cast)
                .findFirst();
        assertTrue(optMultiplicityRange.isEmpty());
    }

    @DisplayName("GIVEN a PartUsage as graphical node, WHEN it is edited with '<1.1>', THEN its short name is set and its name is unchanged")
    @Test
    public void testDirectEditPartUsageNodeWithShortName() {
        PartUsage partUsage = this.createFlashlight();
        String initialName = partUsage.getName();
        this.doDirectEditOnNode(partUsage, "<1.1>");
        assertThat(partUsage.getShortName()).isEqualTo("1.1");
        assertThat(partUsage.getName()).isEqualTo(initialName);
    }

    @DisplayName("GIVEN a PartUsage with a short name as graphical node, WHEN it is edited with '<>', THEN its short name is deleted and its name is unchanged")
    @Test
    public void testDirectEditPartUsageNodeRemoveShortName() {
        PartUsage partUsage = this.createFlashlight();
        String initialName = partUsage.getName();
        partUsage.setDeclaredShortName("1.1");
        this.doDirectEditOnNode(partUsage, "<>");
        assertThat(partUsage.getShortName()).isEmpty();
        assertThat(partUsage.getName()).isEqualTo(initialName);
    }

    @DisplayName("GIVEN an AttributeUsage, WHEN editing with a qualified name'x1 = RootPackage::p1::p11::x1', THEN the expression is correctly created")
    @Test
    public void testFeatureQualifiedNameInExpression() {
        ItemAndAttributesModelTest model = new ItemAndAttributesModelTest();
        assertThat(model.getX1().getName()).isEqualTo("x1");
        this.doDirectEditOnNode(model.getX1(), "x1 = RootPackage::p1::p1_1::x1");
        assertThat(model.getX1().getValuation())
                .isNotNull()
                .extracting(FeatureValue::getValue)
                .isInstanceOf(FeatureReferenceExpression.class)
                .extracting(exp -> ((FeatureReferenceExpression) exp).getReferent())
                .isEqualTo(model.getA2x1());

    }

    @DisplayName("GIVEN an ItemUsage, WHEN editing with a FeatureChain expression with 2 segments, THEN the expression is correctly created")
    @Test
    public void testFeature2SegmentsFeatureChainPartialEdit() {
        ItemAndAttributesModelTest model = new ItemAndAttributesModelTest();
        this.doDirectEditOnNode(model.getA21(), "= a1.a1_2");
        assertThat(model.getA21().getDirection()).isEqualTo(FeatureDirectionKind.IN);
        assertThat(model.getA21().getName()).isEqualTo("a2_1");
        assertThat(model.getA21().getValuation())
                .isNotNull()
                .extracting(FeatureValue::getValue)
                .isInstanceOf(FeatureChainExpression.class)
                .extracting(exp -> ((FeatureChainExpression) exp).getTargetFeature().getFeatureTarget())
                .isEqualTo(model.getA12());

    }

    @DisplayName("GIVEN an ItemUsage, WHEN editing with a FeatureChain expression with 4 segments, THEN the expression is correctly created")
    @Test
    public void testFeature4SegmentsFeatureChainPatialEdit() {
        ItemAndAttributesModelTest model = new ItemAndAttributesModelTest();
        this.doDirectEditOnNode(model.getA21(), "= a1.a1_3.i2_1.i3_1");
        assertThat(model.getA21().getDirection()).isEqualTo(FeatureDirectionKind.IN);
        assertThat(model.getA21().getName()).isEqualTo("a2_1");
        assertThat(model.getA21().getValuation())
                .isNotNull()
                .extracting(FeatureValue::getValue)
                .isInstanceOf(FeatureChainExpression.class)
                .extracting(exp -> ((FeatureChainExpression) exp).getTargetFeature().getFeatureTarget())
                .isEqualTo(model.getI31());

    }

    @DisplayName("GIVEN an AttributeUsage, WHEN editing with a qualified name'x1 = RootPackage::a1::a2::x1', THEN the expression is correctly created")
    @Test
    public void testFeatureQualifiedNameInExpressionPartialEdit() {
        ItemAndAttributesModelTest model = new ItemAndAttributesModelTest();
        this.doDirectEditOnNode(model.getX1(), "= RootPackage::p1::p1_1::x1");
        assertThat(model.getX1().getName()).isEqualTo("x1");
        assertThat(model.getX1().getValuation())
                .isNotNull()
                .extracting(FeatureValue::getValue)
                .isInstanceOf(FeatureReferenceExpression.class)
                .extracting(exp -> ((FeatureReferenceExpression) exp).getReferent())
                .isEqualTo(model.getA2x1());

    }

    @DisplayName("GIVEN an AttributeUsage, WHEN editing with a simple operator expression 'x1 = RootPackage::a1::a2::x1 + x2', THEN the expression is correctly created")
    @Test
    public void testFeatureSimpleOperatorExpression() {
        ItemAndAttributesModelTest model = new ItemAndAttributesModelTest();
        this.doDirectEditOnNode(model.getX1(), "= RootPackage::p1::p1_1::x1 + 1");
        assertThat(model.getX1().getName()).isEqualTo("x1");
        assertThat(model.getX1().getValuation())
                .isNotNull()
                .extracting(FeatureValue::getValue)
                .isInstanceOf(OperatorExpression.class)
                .extracting(exp -> (OperatorExpression) exp)
                .matches(epExp -> "+".equals(epExp.getOperator()));

        assertThat(model.getX1().getValuation().getValue().getParameter())
                .hasSize(2)
                .extracting(p -> p.getValuation().getValue())
                .satisfies(params -> {
                    assertThat(params.get(0)).matches(first -> first instanceof FeatureReferenceExpression featureRef && featureRef.getReferent() == model.getA2x1());
                    assertThat(params.get(1)).matches(second -> second instanceof LiteralInteger intLit && intLit.getValue() == 1);
                });

    }

    @DisplayName("GIVEN an AttributeUsage, WHEN editing with a simple operator expression 'x1 = x2 + x3 - 10.5', THEN the expression is correctly created")
    @Test
    public void testFeatureNestedOperatorExpression() {
        ItemAndAttributesModelTest model = new ItemAndAttributesModelTest();
        this.doDirectEditOnNode(model.getX1(), "= x2 + x3 - 10.5");
        assertThat(model.getX1().getName()).isEqualTo("x1");
        assertThat(model.getX1().getValuation())
                .isNotNull()
                .extracting(FeatureValue::getValue)
                .isInstanceOf(OperatorExpression.class)
                .extracting(exp -> (OperatorExpression) exp)
                .matches(epExp -> "-".equals(epExp.getOperator()));

        assertThat(model.getX1().getValuation().getValue().getParameter())
                .hasSize(2)
                .extracting(p -> p.getValuation().getValue())
                .satisfies(params -> {
                    // Check x2 + x3
                    assertThat(params.get(0))
                            .isInstanceOf(OperatorExpression.class)
                            .extracting(exp -> (OperatorExpression) exp)
                            .matches(epExp -> "+".equals(epExp.getOperator()));
                    assertThat(params.get(0).getParameter())
                            .hasSize(2)
                            .extracting(p -> p.getValuation().getValue())
                            .satisfies(param2 -> {
                                assertThat(param2.get(0)).matches(first -> first instanceof FeatureReferenceExpression featureRef && featureRef.getReferent() == model.getX2());
                                assertThat(param2.get(1)).matches(second -> second instanceof FeatureReferenceExpression featureRef && featureRef.getReferent() == model.getX3());
                            });
                    // Check 10.5
                    assertThat(params.get(1)).matches(second -> second instanceof LiteralRational ratLit && ratLit.getValue() == 10.5);
                });

    }

    @DisplayName("GIVEN a PartUsage with complex name (with spaces or reserved keywords), WHEN edited without the escaping char ('), THEN the name is correctly set")
    @Test
    public void testDirectEditPartUsageComplexName() {
        PartUsage partUsage = this.createFlashlight();

        // Using space would required to use ' in a textual format but we want the direct edit to work on this case
        this.doDirectEditOnNode(partUsage, "My new flashlight");
        assertThat(partUsage.getName()).isEqualTo("My new flashlight");

        // 'default' is a keyword
        this.doDirectEditOnNode(partUsage, "default new flashlight");
        assertThat(partUsage.getName()).isEqualTo("default new flashlight");

        // 'first' is a keyword
        this.doDirectEditOnNode(partUsage, "first new flashlight");
        assertThat(partUsage.getName()).isEqualTo("first new flashlight");
    }

    @DisplayName("GIVEN a PartUsage, WHEN editing with 'flashlight : Flashlight', THEN the type of flashlight should be set to Flashlight")
    @Test
    public void testDirectEditTyping() {
        SmallFlashlightExample flashlightExample = new SmallFlashlightExample();

        PartUsage partUsage = flashlightExample.getFlashlightPartUsage();
        this.doDirectEditOnNode(partUsage, "flashlight : Flashlight");
        assertThat(partUsage.getName()).isEqualTo(SmallFlashlightExample.FLASHLIGHT_PART_USAGE_NAME);
        assertThat(partUsage.getType()).hasSize(1).allMatch(type -> type == flashlightExample.getFlashLightDefinition());
    }

    @DisplayName("GIVEN a PartUsage, WHEN editing with 'flashlight :> superFlashlight', THEN flashlight should subset superFlashlight")
    @Test
    public void testDirectEditSubsetting() {
        SmallFlashlightExample flashlightExample = new SmallFlashlightExample();

        PartUsage partUsage = flashlightExample.getFlashlightPartUsage();
        this.doDirectEditOnNode(partUsage, "flashlight :> superFlashlight");
        assertThat(partUsage.getName()).isEqualTo(SmallFlashlightExample.FLASHLIGHT_PART_USAGE_NAME);
        assertThat(partUsage.supertypes(true)).hasSize(1).allMatch(type -> type == flashlightExample.getSuperFlashLightPartUsage());
    }

    @DisplayName("GIVEN a PartUsage, WHEN editing with ': Flashlight', THEN the type of flashlight should be set to Flashlight")
    @Test
    public void testPartialDirectEditTyping() {
        SmallFlashlightExample flashlightExample = new SmallFlashlightExample();

        PartUsage partUsage = flashlightExample.getFlashlightPartUsage();
        this.doDirectEditOnNode(partUsage, ": Flashlight");
        assertThat(partUsage.getName()).isEqualTo(SmallFlashlightExample.FLASHLIGHT_PART_USAGE_NAME);
        assertThat(partUsage.getType()).hasSize(1).allMatch(type -> type == flashlightExample.getFlashLightDefinition());
    }

    @DisplayName("GIVEN a PartUsage, WHEN editing with ':> superFlashlight', THEN flashlight should specialize superFlashlight")
    @Test
    public void testPartialDirectEditSubsetting() {
        SmallFlashlightExample flashlightExample = new SmallFlashlightExample();

        PartUsage partUsage = flashlightExample.getFlashlightPartUsage();
        this.doDirectEditOnNode(partUsage, ":> superFlashlight");
        assertThat(partUsage.getName()).isEqualTo(SmallFlashlightExample.FLASHLIGHT_PART_USAGE_NAME);
        assertThat(partUsage.supertypes(true)).hasSize(1).allMatch(type -> type == flashlightExample.getSuperFlashLightPartUsage());
    }

    @DisplayName("GIVEN a PartUsage, WHEN editing with 'flashlight : Flashlight2', THEN the type of flashlight should be set to a newly created Flashlight2")
    @Test
    public void testDirectEditTypingWithCreation() {
        SmallFlashlightExample flashlightExample = new SmallFlashlightExample();

        PartUsage partUsage = flashlightExample.getFlashlightPartUsage();
        this.doDirectEditOnNode(partUsage, "flashlight : Flashlight2");
        assertThat(partUsage.getName()).isEqualTo(SmallFlashlightExample.FLASHLIGHT_PART_USAGE_NAME);
        assertThat(partUsage.getType()).hasSize(1).allMatch(type -> "Flashlight2".equals(type.getName())
                && type.getOwningNamespace() == flashlightExample.getP1()
                && type instanceof PartDefinition);
    }

    @DisplayName("GIVEN a PartUsage, WHEN editing with 'flashlight :> superFlashlight2', THEN flashlight should subset a newly created part superFlashlight2")
    @Test
    public void testDirectEditSubsettingWithCreation() {
        SmallFlashlightExample flashlightExample = new SmallFlashlightExample();

        PartUsage partUsage = flashlightExample.getFlashlightPartUsage();
        this.doDirectEditOnNode(partUsage, "flashlight :> superFlashlight2");
        assertThat(partUsage.getName()).isEqualTo(SmallFlashlightExample.FLASHLIGHT_PART_USAGE_NAME);
        assertThat(partUsage.supertypes(true)).hasSize(1).allMatch(type -> "superFlashlight2".equals(type.getName())
                && type.getOwningNamespace() == flashlightExample.getP1()
                && type instanceof PartUsage);
    }

    @DisplayName("GIVEN a PartUsage, WHEN editing with ': Flashlight2', THEN the type of flashlight should be set to a newly created Flashlight2")
    @Test
    public void testPartialDirectEditTypingWithCreation() {
        SmallFlashlightExample flashlightExample = new SmallFlashlightExample();

        PartUsage partUsage = flashlightExample.getFlashlightPartUsage();
        this.doDirectEditOnNode(partUsage, ": Flashlight2");
        assertThat(partUsage.getName()).isEqualTo(SmallFlashlightExample.FLASHLIGHT_PART_USAGE_NAME);
        assertThat(partUsage.getType()).hasSize(1).allMatch(type -> "Flashlight2".equals(type.getName())
                && type.getOwningNamespace() == flashlightExample.getP1()
                && type instanceof PartDefinition);
    }

    @DisplayName("GIVEN a PartUsage, WHEN editing with ':> superFlashlight2', THEN flashlight should specialize a newly created part superFlashlight2")
    @Test
    public void testPartialDirectEditSubsettingWithCreation() {
        SmallFlashlightExample flashlightExample = new SmallFlashlightExample();

        PartUsage partUsage = flashlightExample.getFlashlightPartUsage();
        this.doDirectEditOnNode(partUsage, ":> superFlashlight2");
        assertThat(partUsage.getName()).isEqualTo(SmallFlashlightExample.FLASHLIGHT_PART_USAGE_NAME);
        assertThat(partUsage.supertypes(true)).hasSize(1)
                .allMatch(type -> "superFlashlight2".equals(type.getName())
                        && type.getOwningNamespace() == flashlightExample.getP1()
                        && type instanceof PartUsage);
    }

    @DisplayName("GIVEN a PartUsage with a type, WHEN editing with ':', THEN type should be removed")
    @Test
    public void testDirectEditRemoveType() {
        SmallFlashlightExample flashlightExample = new SmallFlashlightExample().addFlashlightType();

        PartUsage partUsage = flashlightExample.getFlashlightPartUsage();
        assertThat(partUsage.getType()).hasSize(1);
        this.doDirectEditOnNode(partUsage, ":");
        assertThat(partUsage.getName()).isEqualTo(SmallFlashlightExample.FLASHLIGHT_PART_USAGE_NAME);
        assertThat(partUsage.getType()).isEmpty();
    }

    @DisplayName("GIVEN a PartUsage with a subsetting, WHEN editing with ':>', THEN subsetting should be removed")
    @Test
    public void testDirectEditRemoveSubsetting() {
        SmallFlashlightExample flashlightExample = new SmallFlashlightExample().addFlashlightSubsetting();

        PartUsage partUsage = flashlightExample.getFlashlightPartUsage();
        assertThat(partUsage.supertypes(true)).hasSize(1);
        this.doDirectEditOnNode(partUsage, ":>");
        assertThat(partUsage.getName()).isEqualTo(SmallFlashlightExample.FLASHLIGHT_PART_USAGE_NAME);
        assertThat(partUsage.supertypes(true)).isEmpty();
    }

    @DisplayName("GIVEN a PartUsage with a subsetting, WHEN editing with 'flashlight2', THEN subsetting should not be removed and the name should change")
    @Test
    public void testPartialDirectEditName() {
        SmallFlashlightExample flashlightExample = new SmallFlashlightExample().addFlashlightSubsetting();

        PartUsage partUsage = flashlightExample.getFlashlightPartUsage();
        assertThat(partUsage.supertypes(true)).hasSize(1).allMatch(t -> t == flashlightExample.getSuperFlashLightPartUsage());
        this.doDirectEditOnNode(partUsage, "flashlight2");
        assertThat(partUsage.getName()).isEqualTo("flashlight2");
        assertThat(partUsage.supertypes(true)).hasSize(1).allMatch(t -> t == flashlightExample.getSuperFlashLightPartUsage());
    }

    @DisplayName("GIVEN a PartUsage as graphical compartment list item, WHEN it is edited with '[4]', THEN its multiplicity is set")
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

    @DisplayName("GIVEN a PartUsage as graphical compartment list item, WHEN it is edited with '<1.1>', THEN its short name is set and its name is unchanged")
    @Test
    public void testDirectEditPartUsageListItemWithShortName() {
        PartUsage partUsage = this.createFlashlight();
        String initialName = partUsage.getName();
        this.doDirectEditOnCompartmentListItem(partUsage, "<1.1>");
        assertThat(partUsage.getShortName()).isEqualTo("1.1");
        assertThat(partUsage.getName()).isEqualTo(initialName);
    }

    @DisplayName("GIVEN a PartUsage with a short name as graphical compartment list item, WHEN it is edited with '<>', THEN its short name is deleted and its name is unchanged")
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
        SmallFlashlightExample flashlightExample = new SmallFlashlightExample();

        RequirementUsage requirementUsage = SysmlFactory.eINSTANCE.createRequirementUsage();
        flashlightExample.addToRoot(requirementUsage);
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
        referenceFeatureTyping.setType(flashlightExample.getFlashlightPartUsage());
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
        return new SmallFlashlightExample().getFlashlightPartUsage();
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
        DiagramDirectEditListener listener = new DiagramDirectEditListener(element, new IFeedbackMessageService.NoOp());
        walker.walk(listener, tree);
        List<NamedProxy> unresolvedProxies = listener.resolveProxies();
        if (!unresolvedProxies.isEmpty()) {
            fail("Failing to resolve the follwing proxies : \n" + unresolvedProxies.stream().map(proxy -> proxy.nameToResolve() + " on " + proxy.context().getQualifiedName()).collect(joining("\n")));
        }
    }

    private void hasIntegerValue(FeatureValue featureValue, int value) {
        assertThat(featureValue.getOwnedRelatedElement().stream().filter(Expression.class::isInstance)).as("The feature value should contain a LiteralInteger with value " + value)
                .hasSize(1)
                .allMatch(r -> r instanceof LiteralInteger litInt && litInt.getValue() == value);
    }

    private void checkHasInitialValue(Feature feature, boolean expected) {
        assertThat(feature.getOwnedRelationship().stream().filter(FeatureValue.class::isInstance)).as("The feature should have a FeatureValue with isInitial feature set to " + expected)
                .hasSize(1)
                .allMatch(r -> ((FeatureValue) r).isIsInitial() == expected);
    }

    private void checkHasDefaultValue(Feature feature, boolean expected) {
        assertThat(feature.getOwnedRelationship().stream().filter(FeatureValue.class::isInstance)).as("The feature should have a FeatureValue with isDefault feature set to " + expected)
                .hasSize(1)
                .allMatch(r -> ((FeatureValue) r).isIsDefault() == expected);
    }
}
