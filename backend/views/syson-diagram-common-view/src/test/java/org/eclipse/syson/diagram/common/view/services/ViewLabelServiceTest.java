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
package org.eclipse.syson.diagram.common.view.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChainExpression;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.ResultExpressionMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.helper.LabelConstants;
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

    private static final String ATTRIBUTE_USAGE_SHORT_NAME = "shortName";

    private static final String CONSTRAINT_USAGE_NAME = "myConstraint";

    private static final String SHORT_NAME = "1.1";

    private static final String SHORT_NAME_LABEL = LabelConstants.LESSER_THAN + SHORT_NAME + LabelConstants.GREATER_THAN;

    private ViewLabelService viewLabelService;

    @BeforeEach
    void beforeEach() {
        this.viewLabelService = new ViewLabelService(new IFeedbackMessageService.NoOp(), new ShowDiagramsIconsService());
    }

    @DisplayName("Check Attribute Usage item label with name")
    @Test
    void testItemCompartmentLabelWithName() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);
        assertEquals(ATTRIBUTE_USAGE_NAME, this.viewLabelService.getCompartmentItemLabel(attributeUsage));
    }

    @DisplayName("Check Attribute Usage item label with name and short name")
    void testItemCompartmentLabelWithNameAndShortName() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);
        attributeUsage.setDeclaredShortName(ATTRIBUTE_USAGE_SHORT_NAME);
        assertEquals(LabelConstants.LESSER_THAN + ATTRIBUTE_USAGE_SHORT_NAME + LabelConstants.GREATER_THAN + LabelConstants.SPACE + ATTRIBUTE_USAGE_NAME,
                this.viewLabelService.getCompartmentItemLabel(attributeUsage));
    }

    @DisplayName("Given a FeatureValue to an attribute, when it is an initial FeatureValue relationship, then the label should use the symbole ':=' instead of '='")
    @Test
    public void testInitalFeatureValueSymboleLabel() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);

        LiteralInteger literal = SysmlFactory.eINSTANCE.createLiteralInteger();
        literal.setValue(1);
        FeatureValue featureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        featureValue.getOwnedRelatedElement().add(literal);
        attributeUsage.getOwnedRelationship().add(featureValue);

        assertEquals("myAttributeUsage = 1", this.viewLabelService.getCompartmentItemLabel(attributeUsage));

        featureValue.setIsInitial(true);

        assertEquals("myAttributeUsage := 1", this.viewLabelService.getCompartmentItemLabel(attributeUsage));

    }

    @DisplayName("Given a FeatureValue to an attribute, when it is a default FeatureValue relationship, then the label should use the symbole ':=' instead of '='")
    @Test
    public void testDefaultFeatureValueSymboleLabel() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);

        LiteralInteger literal = SysmlFactory.eINSTANCE.createLiteralInteger();
        literal.setValue(1);
        FeatureValue featureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        featureValue.getOwnedRelatedElement().add(literal);
        attributeUsage.getOwnedRelationship().add(featureValue);

        assertEquals("myAttributeUsage = 1", this.viewLabelService.getCompartmentItemLabel(attributeUsage));

        featureValue.setIsDefault(true);

        assertEquals("myAttributeUsage default = 1", this.viewLabelService.getCompartmentItemLabel(attributeUsage));

        featureValue.setIsInitial(true);

        assertEquals("myAttributeUsage default := 1", this.viewLabelService.getCompartmentItemLabel(attributeUsage));

    }

    @DisplayName("Check Attribute Usage item label with no name")
    @Test
    void testItemCompartmentLabelWithoutName() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        assertEquals("", this.viewLabelService.getCompartmentItemLabel(attributeUsage));
    }

    @DisplayName("Check Attribute Usage item label with prefix")
    @Test
    void testItemCompartmentLabelWithPrefix() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);

        attributeUsage.setIsAbstract(true);
        assertEquals(LabelConstants.ABSTRACT + LabelConstants.SPACE + ATTRIBUTE_USAGE_NAME, this.viewLabelService.getCompartmentItemLabel(attributeUsage));
    }

    @DisplayName("Check Attribute Usage item label with multiplicity")
    @Test
    void testItemCompartmentLabelWithMultiplicity() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);

        attributeUsage.setIsOrdered(true);
        assertEquals(ATTRIBUTE_USAGE_NAME + LabelConstants.SPACE + LabelConstants.ORDERED, this.viewLabelService.getCompartmentItemLabel(attributeUsage));
    }

    @DisplayName("Check Attribute Usage item label with prefix and multiplicity")
    @Test
    void testItemCompartmentLabelWithPrefixAndMultiplicity() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);

        attributeUsage.setIsOrdered(true);
        attributeUsage.setIsAbstract(true);
        assertEquals(LabelConstants.ABSTRACT + LabelConstants.SPACE + ATTRIBUTE_USAGE_NAME + LabelConstants.SPACE + LabelConstants.ORDERED,
                this.viewLabelService.getCompartmentItemLabel(attributeUsage));
    }

    @DisplayName("Given a ConstraintUsage with no expression, when its label is computed, then the label contains the name of the constraint")
    @Test
    public void testGetCompartmentItemLabelOfConstraintWithNoExpression() {
        ConstraintUsage constraintUsage = SysmlFactory.eINSTANCE.createConstraintUsage();
        constraintUsage.setDeclaredName(CONSTRAINT_USAGE_NAME);
        // Constraints have a special label when they are inside a RequirementConstraintMembership
        RequirementConstraintMembership requirementConstraintMembership = SysmlFactory.eINSTANCE.createRequirementConstraintMembership();
        requirementConstraintMembership.getOwnedRelatedElement().add(constraintUsage);
        assertThat(this.viewLabelService.getCompartmentItemLabel(constraintUsage)).isEqualTo(CONSTRAINT_USAGE_NAME);
    }

    @DisplayName("Given a ConstraintUsage with a boolean expression, when its label is computed, then the label represents the expression")
    @Test
    public void testGetCompartmentItemLabelOfConstraintWithBooleanExpression() {
        ConstraintUsage constraintUsage = SysmlFactory.eINSTANCE.createConstraintUsage();
        constraintUsage.setDeclaredName(CONSTRAINT_USAGE_NAME);
        // Constraints have a special label when they are inside a RequirementConstraintMembership
        RequirementConstraintMembership requirementConstraintMembership = SysmlFactory.eINSTANCE.createRequirementConstraintMembership();
        requirementConstraintMembership.getOwnedRelatedElement().add(constraintUsage);

        OperatorExpression operatorExpression = this.createOperatorExpression(constraintUsage, ">=");
        this.createIntegerLiteralForXParameter(operatorExpression);
        FeatureValue yValue = this.createFeatureValueForYParameter(operatorExpression);
        LiteralInteger literalInteger = SysmlFactory.eINSTANCE.createLiteralInteger();
        literalInteger.setValue(2);
        yValue.getOwnedRelatedElement().add(literalInteger);

        assertThat(this.viewLabelService.getCompartmentItemLabel(constraintUsage)).isEqualTo("1 >= 2");
    }

    @DisplayName("Given a ConstraintUsage with an expression containing a subject reference, when its label is computed, then the label represents the expression")
    @Test
    public void testGetCompartmentItemLabelOfConstraintWithSubjectReferenceExpression() {
        ConstraintUsage constraintUsage = SysmlFactory.eINSTANCE.createConstraintUsage();
        constraintUsage.setDeclaredName(CONSTRAINT_USAGE_NAME);
        // Constraints have a special label when they are inside a RequirementConstraintMembership
        RequirementConstraintMembership requirementConstraintMembership = SysmlFactory.eINSTANCE.createRequirementConstraintMembership();
        requirementConstraintMembership.getOwnedRelatedElement().add(constraintUsage);
        ReferenceUsage subjectReference = SysmlFactory.eINSTANCE.createReferenceUsage();
        subjectReference.setDeclaredName("mySubject");

        OperatorExpression operatorExpression = this.createOperatorExpression(constraintUsage, ">=");
        this.createIntegerLiteralForXParameter(operatorExpression);
        FeatureValue yValue = this.createFeatureValueForYParameter(operatorExpression);
        FeatureReferenceExpression yFeatureReference = SysmlFactory.eINSTANCE.createFeatureReferenceExpression();
        yValue.getOwnedRelatedElement().add(yFeatureReference);
        Membership yFeatureReferenceMembership = SysmlFactory.eINSTANCE.createMembership();
        yFeatureReference.getOwnedRelationship().add(yFeatureReferenceMembership);
        yFeatureReferenceMembership.setMemberElement(subjectReference);

        assertThat(this.viewLabelService.getCompartmentItemLabel(constraintUsage)).isEqualTo("1 >= mySubject");

    }

    @DisplayName("Given a ConstraintUsage with an expression containing an attribute reference, when its label is computed, then the label represents the expression")
    @Test
    public void testGetCompartmentItemLabelOfConstraintWithAttributeReferenceExpression() {
        ConstraintUsage constraintUsage = SysmlFactory.eINSTANCE.createConstraintUsage();
        constraintUsage.setDeclaredName(CONSTRAINT_USAGE_NAME);
        // Constraints have a special label when they are inside a RequirementConstraintMembership
        RequirementConstraintMembership requirementConstraintMembership = SysmlFactory.eINSTANCE.createRequirementConstraintMembership();
        requirementConstraintMembership.getOwnedRelatedElement().add(constraintUsage);

        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName("myAttribute");

        OperatorExpression operatorExpression = this.createOperatorExpression(constraintUsage, ">=");
        this.createIntegerLiteralForXParameter(operatorExpression);
        FeatureValue yValue = this.createFeatureValueForYParameter(operatorExpression);
        FeatureReferenceExpression yFeatureReference = SysmlFactory.eINSTANCE.createFeatureReferenceExpression();
        yValue.getOwnedRelatedElement().add(yFeatureReference);
        Membership yFeatureReferenceMembership = SysmlFactory.eINSTANCE.createMembership();
        yFeatureReference.getOwnedRelationship().add(yFeatureReferenceMembership);
        yFeatureReferenceMembership.setMemberElement(attributeUsage);

        assertThat(this.viewLabelService.getCompartmentItemLabel(constraintUsage)).isEqualTo("1 >= myAttribute");
    }

    @DisplayName("Given a ConstraintUsage with an expression containing a single feature chaining, when its label is computed, then the label represents the expression")
    @Test
    public void testGetCompartmentItemLabelOfConstraintWithSingleFeatureChainingExpression() {
        ConstraintUsage constraintUsage = SysmlFactory.eINSTANCE.createConstraintUsage();
        constraintUsage.setDeclaredName(CONSTRAINT_USAGE_NAME);
        // Constraints have a special label when they are inside a RequirementConstraintMembership
        RequirementConstraintMembership requirementConstraintMembership = SysmlFactory.eINSTANCE.createRequirementConstraintMembership();
        requirementConstraintMembership.getOwnedRelatedElement().add(constraintUsage);

        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName("myAttribute");
        AttributeUsage subAttributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        subAttributeUsage.setDeclaredName("mySubAttribute");

        OperatorExpression operatorExpression = this.createOperatorExpression(constraintUsage, ">=");
        this.createIntegerLiteralForXParameter(operatorExpression);
        FeatureValue yValue = this.createFeatureValueForYParameter(operatorExpression);
        FeatureChainExpression featureChainExpression = this.createFeatureChainExpression(yValue, attributeUsage);
        Membership featureChainMembership = SysmlFactory.eINSTANCE.createMembership();
        featureChainExpression.getOwnedRelationship().add(featureChainMembership);
        featureChainMembership.setMemberElement(subAttributeUsage);

        assertThat(this.viewLabelService.getCompartmentItemLabel(constraintUsage)).isEqualTo("1 >= myAttribute.mySubAttribute");
    }

    @DisplayName("Given a ConstraintUsage with an expression containing multiple feature chainings, when its label is computed, then the label represents the expression")
    @Test
    public void testGetCompartmentItemLabelOfConstraintWithMultipleFeatureChainingExpression() {
        ConstraintUsage constraintUsage = SysmlFactory.eINSTANCE.createConstraintUsage();
        constraintUsage.setDeclaredName(CONSTRAINT_USAGE_NAME);
        // Constraints have a special label when they are inside a RequirementConstraintMembership
        RequirementConstraintMembership requirementConstraintMembership = SysmlFactory.eINSTANCE.createRequirementConstraintMembership();
        requirementConstraintMembership.getOwnedRelatedElement().add(constraintUsage);

        AttributeUsage xAttributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        xAttributeUsage.setDeclaredName("x");
        AttributeUsage yAttributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        yAttributeUsage.setDeclaredName("y");
        AttributeUsage zAttributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        zAttributeUsage.setDeclaredName("z");

        OperatorExpression operatorExpression = this.createOperatorExpression(constraintUsage, ">=");
        this.createIntegerLiteralForXParameter(operatorExpression);
        FeatureValue yValue = this.createFeatureValueForYParameter(operatorExpression);
        FeatureChainExpression featureChainExpression = this.createFeatureChainExpression(yValue, xAttributeUsage);
        OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        featureChainExpression.getOwnedRelationship().add(owningMembership);
        Feature feature = SysmlFactory.eINSTANCE.createFeature();
        owningMembership.getOwnedRelatedElement().add(feature);
        FeatureChaining featureChaining1 = SysmlFactory.eINSTANCE.createFeatureChaining();
        featureChaining1.setChainingFeature(yAttributeUsage);
        FeatureChaining featureChaining2 = SysmlFactory.eINSTANCE.createFeatureChaining();
        featureChaining2.setChainingFeature(zAttributeUsage);
        feature.getOwnedRelationship().addAll(List.of(featureChaining1, featureChaining2));

        assertThat(this.viewLabelService.getCompartmentItemLabel(constraintUsage)).isEqualTo("1 >= x.y.z");
    }

    @DisplayName("Given a Dependency with a name and short name, when its edge label is computed, then the label contains the name and short name")
    @Test
    public void testGetDependencyLabelOfDependencyWithNameAndShortName() {
        Dependency dependency = SysmlFactory.eINSTANCE.createDependency();
        dependency.setDeclaredShortName(SHORT_NAME);
        dependency.setDeclaredName("dependency");

        assertThat(this.viewLabelService.getDependencyLabel(dependency)).isEqualTo(SHORT_NAME_LABEL + " dependency");
    }

    @DisplayName("Given an Interface with a name and short name, when its edge label is computed, then the label contains the name and short name")
    @Test
    public void testGetEdgeLabelOfInterfaceWithNameAndShortName() {
        InterfaceUsage interfaceUsage = SysmlFactory.eINSTANCE.createInterfaceUsage();
        interfaceUsage.setDeclaredShortName(SHORT_NAME);
        interfaceUsage.setDeclaredName("interface");

        assertThat(this.viewLabelService.getEdgeLabel(interfaceUsage)).isEqualTo(SHORT_NAME_LABEL + " interface");
    }

    /**
     * Creates an {@link OperatorExpression} in the given {@code constraintUsage}.
     * <p>
     * This method creates all the intermediate elements required to add an {@link OperatorExpression} in the given
     * {@code ConstraintUsage}.
     * </p>
     *
     * @param constraintUsage
     *            the constraint containing the operator to create
     * @param operator
     *            the operator literal
     * @return the created operator
     */
    private OperatorExpression createOperatorExpression(ConstraintUsage constraintUsage, String operator) {
        ResultExpressionMembership resultExpressionMembership = SysmlFactory.eINSTANCE.createResultExpressionMembership();
        constraintUsage.getOwnedRelationship().add(resultExpressionMembership);
        OperatorExpression operatorExpression = SysmlFactory.eINSTANCE.createOperatorExpression();
        resultExpressionMembership.getOwnedRelatedElement().add(operatorExpression);
        operatorExpression.setOperator(operator);
        return operatorExpression;
    }

    /**
     * Creates a {@link LiteralInteger} as the {@code x} parameter of the provided {@code operatorExpression}.
     * <p>
     * This method creates all the intermediate elements required to add a {@link LiteralInteger} in the given
     * {@code operatorExpression}.
     * </p>
     *
     * @param operatorExpression
     *            the operator containing the literal to create
     */
    private void createIntegerLiteralForXParameter(OperatorExpression operatorExpression) {
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
    }

    /**
     * Creates a {@link FeatureValue} as the {@code y} parameter of the provided {@code operatorExpression}.
     * <p>
     * This method creates all the intermediate elements required to add a {@link FeatureValue} in the given
     * {@code operatorExpression}.
     * </p>
     *
     * @param operatorExpression
     *            the operator containing the feature value to create
     * @return the created feature value
     */
    private FeatureValue createFeatureValueForYParameter(OperatorExpression operatorExpression) {
        ParameterMembership p2 = SysmlFactory.eINSTANCE.createParameterMembership();
        operatorExpression.getOwnedRelationship().add(p2);
        Feature y = SysmlFactory.eINSTANCE.createFeature();
        p2.getOwnedRelatedElement().add(y);
        y.setDeclaredName("y");
        y.setDirection(FeatureDirectionKind.IN);
        FeatureValue yValue = SysmlFactory.eINSTANCE.createFeatureValue();
        y.getOwnedRelationship().add(yValue);
        return yValue;
    }

    /**
     * Creates a {@link FeatureChainExpression} in the given {@code feautreValue} with {@code usage} as its source.
     * <p>
     * This method creates all the intermediate elements required to add a {@link FeatureChainExpression} in the given
     * {@code featureValue}.
     * </p>
     *
     * @param featureValue
     *            the feature value containing the feature chain expression to create
     * @param usage
     *            the source usage of the feature chain expression to create
     * @return the created feature chain expression
     */
    private FeatureChainExpression createFeatureChainExpression(FeatureValue featureValue, Usage usage) {
        FeatureChainExpression featureChainExpression = SysmlFactory.eINSTANCE.createFeatureChainExpression();
        featureValue.getOwnedRelatedElement().add(featureChainExpression);
        ParameterMembership parameterMembership = SysmlFactory.eINSTANCE.createParameterMembership();
        featureChainExpression.getOwnedRelationship().add(parameterMembership);
        Feature source = SysmlFactory.eINSTANCE.createFeature();
        parameterMembership.getOwnedRelatedElement().add(source);
        source.setDeclaredName("source");
        source.setDirection(FeatureDirectionKind.IN);
        FeatureValue sourceValue = SysmlFactory.eINSTANCE.createFeatureValue();
        source.getOwnedRelationship().add(sourceValue);
        FeatureReferenceExpression featureReferenceExpression = SysmlFactory.eINSTANCE.createFeatureReferenceExpression();
        sourceValue.getOwnedRelatedElement().add(featureReferenceExpression);
        Membership xFeatureReferenceMembership = SysmlFactory.eINSTANCE.createMembership();
        featureReferenceExpression.getOwnedRelationship().add(xFeatureReferenceMembership);
        xFeatureReferenceMembership.setMemberElement(usage);
        return featureChainExpression;
    }
}
