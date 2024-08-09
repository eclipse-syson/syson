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
package org.eclipse.syson.diagram.common.view.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.ResultExpressionMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * View Label services tests.
 *
 * @author jmallet
 */
public class ViewLabelServiceTest {

    private static final String ATTRIBUTE_USAGE_NAME = "myAttributeUsage";

    private ViewLabelService viewLabelService;

    @BeforeEach
    void beforeEach() {
        this.viewLabelService = new ViewLabelService(new TestFeedbackMessageService(), new ShowDiagramsIconsService());
    }

    // @DisplayName("Chek Attribute Usage item label with default properties")
    // @Test
    // void testItemCompartmentLabelWithDefaultProperties() {
    // AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
    // attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);
    // assertEquals(ATTRIBUTE_USAGE_NAME, this.viewLabelService.getCompartmentItemLabel(attributeUsage));
    // }
    //
    // @DisplayName("Chek Attribute Usage item label with no name")
    // @Test
    // void testItemCompartmentLabelWithoutName() {
    // AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
    // assertEquals("", this.viewLabelService.getCompartmentItemLabel(attributeUsage));
    // }
    //
    // @DisplayName("Chek Attribute Usage item label with prefix")
    // @Test
    // void testItemCompartmentLabelWithPrefix() {
    // AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
    // attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);
    //
    // attributeUsage.setIsAbstract(true);
    // assertEquals(LabelConstants.ABSTRACT + LabelConstants.SPACE + ATTRIBUTE_USAGE_NAME,
    // this.viewLabelService.getCompartmentItemLabel(attributeUsage));
    // }
    //
    // @DisplayName("Chek Attribute Usage item label with multiplicity")
    // @Test
    // void testItemCompartmentLabelWithMultiplicity() {
    // AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
    // attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);
    //
    // attributeUsage.setIsOrdered(true);
    // assertEquals(ATTRIBUTE_USAGE_NAME + LabelConstants.SPACE + LabelConstants.ORDERED,
    // this.viewLabelService.getCompartmentItemLabel(attributeUsage));
    // }
    //
    // @DisplayName("Chek Attribute Usage item label with prefix and multiplicity")
    // @Test
    // void testItemCompartmentLabelWithPrefixAndMultiplicity() {
    // AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
    // attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);
    //
    // attributeUsage.setIsOrdered(true);
    // attributeUsage.setIsAbstract(true);
    // assertEquals(LabelConstants.ABSTRACT + LabelConstants.SPACE + ATTRIBUTE_USAGE_NAME + LabelConstants.SPACE +
    // LabelConstants.ORDERED,
    // this.viewLabelService.getCompartmentItemLabel(attributeUsage));
    // }

    @DisplayName("Given a ConstraintUsage with no expression, when its label is computed, then the label contains the name of the constraint")
    @Test
    public void testGetCompartmentItemLabelOfConstraintWithNoExpression() {
        ConstraintUsage constraintUsage = SysmlFactory.eINSTANCE.createConstraintUsage();
        constraintUsage.setDeclaredName("myConstraint");
        assertThat(this.viewLabelService.getCompartmentItemLabel(constraintUsage)).isEqualTo("myConstraint");
    }

    @DisplayName("Given a ConstraintUsage with a boolean expression, when its label is computed, then the label represents the expression")
    @Test
    public void testGetCompartmentItemLabelOfConstraintWithBooleanExpression() {
        ConstraintUsage constraintUsage = SysmlFactory.eINSTANCE.createConstraintUsage();
        constraintUsage.setDeclaredName("myConstraint");
        ResultExpressionMembership resultExpressionMembership = SysmlFactory.eINSTANCE.createResultExpressionMembership();
        constraintUsage.getOwnedRelationship().add(resultExpressionMembership);
        OperatorExpression operatorExpression = SysmlFactory.eINSTANCE.createOperatorExpression();
        resultExpressionMembership.getOwnedRelatedElement().add(operatorExpression);
        operatorExpression.setOperator(">=");

        ParameterMembership p1 = SysmlFactory.eINSTANCE.createParameterMembership();
        operatorExpression.getOwnedRelationship().add(p1);
        Feature x = SysmlFactory.eINSTANCE.createFeature();
        p1.getOwnedRelatedElement().add(x);
        x.setDeclaredName("x");
        x.setDirection(FeatureDirectionKind.IN);
        FeatureValue xValue = SysmlFactory.eINSTANCE.createFeatureValue();
        x.getOwnedRelationship().add(xValue);
        LiteralInteger literalInteger1 = SysmlFactory.eINSTANCE.createLiteralInteger();
        literalInteger1.setValue(1);
        xValue.getOwnedRelatedElement().add(literalInteger1);

        ParameterMembership p2 = SysmlFactory.eINSTANCE.createParameterMembership();
        operatorExpression.getOwnedRelationship().add(p2);
        Feature y = SysmlFactory.eINSTANCE.createFeature();
        p2.getOwnedRelatedElement().add(y);
        y.setDeclaredName("y");
        y.setDirection(FeatureDirectionKind.IN);
        FeatureValue yValue = SysmlFactory.eINSTANCE.createFeatureValue();
        y.getOwnedRelationship().add(yValue);
        LiteralInteger literalInteger2 = SysmlFactory.eINSTANCE.createLiteralInteger();
        literalInteger2.setValue(2);
        yValue.getOwnedRelatedElement().add(literalInteger2);

        assertThat(this.viewLabelService.getCompartmentItemLabel(constraintUsage)).isEqualTo("1 >= 2");
    }

    @DisplayName("Given a ConstraintUsage with an expression containing a feature reference, when its label is computed, then the label represents the expression")
    @Test
    public void testGetCompartmentItemLabelOfConstraintWithFeatureReferenceExpression() {

    }

    @DisplayName("Given a ConstraintUsage with an expression containing a single feature chaining, when its label is computed, then the label represents the expression")
    @Test
    public void testGetCompartmentItemLabelOfConstraintWithSingleFeatureChainingExpression() {

    }

    @DisplayName("Given a ConstraintUsage with an expression containing multiple feature chainings, when its label is computed, then the label represents the expression")
    @Test
    public void testGetCompartmentItemLabelOfConstraintWithMultipleFeatureChainingExpression() {

    }

    /**
     * A test-level implementation of {@link IFeedbackMessageService}.
     *
     * @author jmallet
     */
    public class TestFeedbackMessageService implements IFeedbackMessageService {

        private final List<Message> feedbackMessages = Collections.synchronizedList(new ArrayList<>());

        @Override
        public void addFeedbackMessage(Message message) {
            this.feedbackMessages.add(message);
        }

        @Override
        public List<Message> getFeedbackMessages() {
            return this.feedbackMessages;
        }
    }
}