/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.diagram.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.DataType;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChainExpression;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.PartDefinition;
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
 * Label services tests.
 *
 * @author jmallet
 */
public class DiagramQueryLabelServiceTest {

    private static final String ATTRIBUTE_USAGE_NAME = "myAttributeUsage";

    private static final String ATTRIBUTE_USAGE_SHORT_NAME = "shortName";

    private static final String CONSTRAINT_USAGE_NAME = "myConstraint";

    private static final String SHORT_NAME = "1.1";

    private static final String SHORT_NAME_LABEL = LabelConstants.LESSER_THAN + SHORT_NAME + LabelConstants.GREATER_THAN;

    private DiagramQueryLabelService labelService;

    @BeforeEach
    void beforeEach() {
        this.labelService = new DiagramQueryLabelService();
    }

    @DisplayName("Check Attribute label with name and short name")
    @Test
    void testAttributeLabelWithNameAndShortName() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);
        attributeUsage.setDeclaredShortName(ATTRIBUTE_USAGE_SHORT_NAME);
        StringBuilder expectedLabel = new StringBuilder();
        expectedLabel.append(LabelConstants.OPEN_QUOTE)
                .append("attribute")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(LabelConstants.LESSER_THAN)
                .append(ATTRIBUTE_USAGE_SHORT_NAME)
                .append(LabelConstants.GREATER_THAN)
                .append(LabelConstants.SPACE)
                .append(ATTRIBUTE_USAGE_NAME);
        assertEquals(expectedLabel.toString(), this.labelService.getContainerLabel(attributeUsage));
    }

    @DisplayName("Check Usage prefix label with default properties")
    @Test
    void testUsagePrefixLabelWithDefaultProperties() {
        Usage usageWithNoDirection = SysmlFactory.eINSTANCE.createUsage();
        assertEquals(LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsageListItemPrefix(usageWithNoDirection));
    }

    @DisplayName("Check Usage prefix label with abstract property")
    @Test
    void testUsagePrefixLabelWithAbstractProperty() {
        Usage usageWithNoDirection = SysmlFactory.eINSTANCE.createUsage();
        usageWithNoDirection.setIsAbstract(true);
        assertEquals(LabelConstants.ABSTRACT + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsageListItemPrefix(usageWithNoDirection));
    }

    @DisplayName("Check Usage prefix label with derived property")
    @Test
    void testUsagePrefixLabelWithDerivedProperty() {
        Usage usageWithNoDirection = SysmlFactory.eINSTANCE.createUsage();
        usageWithNoDirection.setIsDerived(true);
        assertEquals(LabelConstants.DERIVED + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsageListItemPrefix(usageWithNoDirection));
    }

    @DisplayName("Check Usage prefix label with end property")
    @Test
    void testUsagePrefixLabelWithEndProperty() {
        Usage usageWithNoDirection = SysmlFactory.eINSTANCE.createUsage();
        usageWithNoDirection.setIsEnd(true);
        assertEquals(LabelConstants.END + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsageListItemPrefix(usageWithNoDirection));
    }

    @DisplayName("Check Usage prefix label with constant property")
    @Test
    void testUsagePrefixLabelWithConstantProperty() {
        Usage usageWithNoDirection = SysmlFactory.eINSTANCE.createUsage();
        usageWithNoDirection.setIsConstant(true);
        assertEquals(LabelConstants.CONSTANT + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsageListItemPrefix(usageWithNoDirection));
    }

    @DisplayName("Check Usage prefix label with readonly property")
    @Test
    void testUsagePrefixLabelWithVariationProperty() {
        Usage usageWithNoDirection = SysmlFactory.eINSTANCE.createUsage();
        usageWithNoDirection.setIsVariation(true);
        assertEquals(LabelConstants.VARIATION + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsageListItemPrefix(usageWithNoDirection));
    }

    @DisplayName("Check Usage prefix label with many custom properties")
    @Test
    void testUsagePrefixLabelWithManyCustomProperties() {
        Usage usageWithNoDirection = SysmlFactory.eINSTANCE.createUsage();
        usageWithNoDirection.setIsAbstract(true);
        usageWithNoDirection.setIsDerived(true);
        usageWithNoDirection.setIsEnd(true);
        usageWithNoDirection.setIsConstant(true);
        usageWithNoDirection.setIsVariation(true);

        assertEquals(LabelConstants.VARIATION + LabelConstants.SPACE + LabelConstants.CONSTANT + LabelConstants.SPACE
                + LabelConstants.DERIVED + LabelConstants.SPACE
                + LabelConstants.END
                + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsageListItemPrefix(usageWithNoDirection));
    }

    @DisplayName("Check Usage prefix label with direction properties")
    @Test
    void testUsagePrefixLabelWithDirectionProperties() {
        Usage usageWithDirection = SysmlFactory.eINSTANCE.createUsage();

        usageWithDirection.setDirection(FeatureDirectionKind.IN);
        assertEquals(LabelConstants.IN + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsageListItemPrefix(usageWithDirection));

        usageWithDirection.setDirection(FeatureDirectionKind.OUT);
        assertEquals(LabelConstants.OUT + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsageListItemPrefix(usageWithDirection));

        usageWithDirection.setDirection(FeatureDirectionKind.INOUT);
        assertEquals(LabelConstants.INOUT + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsageListItemPrefix(usageWithDirection));
    }

    @DisplayName("Check AttributeUsage prefix label with default properties")
    @Test
    void testAttributeUsagePrefixLabelWithDefaultProperties() {
        AttributeUsage attributeUsageWithNoDirection = SysmlFactory.eINSTANCE.createAttributeUsage();
        assertEquals("", this.labelService.getUsageListItemPrefix(attributeUsageWithNoDirection));
    }

    @DisplayName("Check Attribute Usage item label with name")
    @Test
    void testItemCompartmentLabelWithName() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);
        assertEquals(ATTRIBUTE_USAGE_NAME, this.labelService.getCompartmentItemLabel(attributeUsage));
    }

    @DisplayName("Check Attribute Usage item label with name and short name")
    void testItemCompartmentLabelWithNameAndShortName() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);
        attributeUsage.setDeclaredShortName(ATTRIBUTE_USAGE_SHORT_NAME);
        assertEquals(LabelConstants.LESSER_THAN + ATTRIBUTE_USAGE_SHORT_NAME + LabelConstants.GREATER_THAN + LabelConstants.SPACE + ATTRIBUTE_USAGE_NAME,
                this.labelService.getCompartmentItemLabel(attributeUsage));
    }

    @DisplayName("GIVEN a FeatureValue to an attribute, WHEN it is an initial FeatureValue relationship, THEN the label should use the symbole ':=' instead of '='")
    @Test
    public void testInitalFeatureValueSymboleLabel() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);

        LiteralInteger literal = SysmlFactory.eINSTANCE.createLiteralInteger();
        literal.setValue(1);
        FeatureValue featureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        featureValue.getOwnedRelatedElement().add(literal);
        attributeUsage.getOwnedRelationship().add(featureValue);

        assertEquals("myAttributeUsage = 1", this.labelService.getCompartmentItemLabel(attributeUsage));

        featureValue.setIsInitial(true);

        assertEquals("myAttributeUsage := 1", this.labelService.getCompartmentItemLabel(attributeUsage));

    }

    @DisplayName("GIVEN a FeatureValue to an attribute, WHEN it is a default FeatureValue relationship, THEN the label should use the symbole ':=' instead of '='")
    @Test
    public void testDefaultFeatureValueSymboleLabel() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);

        LiteralInteger literal = SysmlFactory.eINSTANCE.createLiteralInteger();
        literal.setValue(1);
        FeatureValue featureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        featureValue.getOwnedRelatedElement().add(literal);
        attributeUsage.getOwnedRelationship().add(featureValue);

        assertEquals("myAttributeUsage = 1", this.labelService.getCompartmentItemLabel(attributeUsage));

        featureValue.setIsDefault(true);

        assertEquals("myAttributeUsage default = 1", this.labelService.getCompartmentItemLabel(attributeUsage));

        featureValue.setIsInitial(true);

        assertEquals("myAttributeUsage default := 1", this.labelService.getCompartmentItemLabel(attributeUsage));

    }

    @DisplayName("Check Attribute Usage item label with no name")
    @Test
    void testItemCompartmentLabelWithoutName() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        assertEquals("", this.labelService.getCompartmentItemLabel(attributeUsage));
    }

    @DisplayName("Check Attribute Usage item label with prefix")
    @Test
    void testItemCompartmentLabelWithPrefix() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);

        attributeUsage.setIsAbstract(true);
        assertEquals(LabelConstants.ABSTRACT + LabelConstants.SPACE + ATTRIBUTE_USAGE_NAME, this.labelService.getCompartmentItemLabel(attributeUsage));
    }

    @DisplayName("Check Attribute Usage item label with multiplicity")
    @Test
    void testItemCompartmentLabelWithMultiplicity() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);

        attributeUsage.setIsOrdered(true);
        assertEquals(ATTRIBUTE_USAGE_NAME + LabelConstants.SPACE + LabelConstants.ORDERED, this.labelService.getCompartmentItemLabel(attributeUsage));
    }

    @DisplayName("Check Attribute Usage item label with prefix and multiplicity")
    @Test
    void testItemCompartmentLabelWithPrefixAndMultiplicity() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);

        attributeUsage.setIsOrdered(true);
        attributeUsage.setIsAbstract(true);
        assertEquals(LabelConstants.ABSTRACT + LabelConstants.SPACE + ATTRIBUTE_USAGE_NAME + LabelConstants.SPACE + LabelConstants.ORDERED,
                this.labelService.getCompartmentItemLabel(attributeUsage));
    }

    @DisplayName("GIVEN a ConstraintUsage with no expression, WHEN its label is computed, THEN the label contains the name of the constraint")
    @Test
    public void testGetCompartmentItemLabelOfConstraintWithNoExpression() {
        ConstraintUsage constraintUsage = SysmlFactory.eINSTANCE.createConstraintUsage();
        constraintUsage.setDeclaredName(CONSTRAINT_USAGE_NAME);
        // Constraints have a special label when they are inside a RequirementConstraintMembership
        RequirementConstraintMembership requirementConstraintMembership = SysmlFactory.eINSTANCE.createRequirementConstraintMembership();
        requirementConstraintMembership.getOwnedRelatedElement().add(constraintUsage);
        assertThat(this.labelService.getCompartmentItemLabel(constraintUsage)).isEqualTo(CONSTRAINT_USAGE_NAME);
    }

    @DisplayName("GIVEN a ConstraintUsage with a boolean expression, WHEN its label is computed, THEN the label represents the expression")
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

        assertThat(this.labelService.getCompartmentItemLabel(constraintUsage)).isEqualTo("1 >= 2");
    }

    @DisplayName("GIVEN a ConstraintUsage with an expression containing a subject reference, WHEN its label is computed, THEN the label represents the expression")
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

        assertThat(this.labelService.getCompartmentItemLabel(constraintUsage)).isEqualTo("1 >= mySubject");

    }

    @DisplayName("GIVEN a ConstraintUsage with an expression containing an attribute reference, WHEN its label is computed, THEN the label represents the expression")
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

        assertThat(this.labelService.getCompartmentItemLabel(constraintUsage)).isEqualTo("1 >= myAttribute");
    }

    @DisplayName("GIVEN a ConstraintUsage with an expression containing a single feature chaining, WHEN its label is computed, THEN the label represents the expression")
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

        assertThat(this.labelService.getCompartmentItemLabel(constraintUsage)).isEqualTo("1 >= myAttribute.mySubAttribute");
    }

    @DisplayName("GIVEN a ConstraintUsage with an expression containing multiple feature chainings, WHEN its label is computed, THEN the label represents the expression")
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

        assertThat(this.labelService.getCompartmentItemLabel(constraintUsage)).isEqualTo("1 >= x.y.z");
    }

    @DisplayName("GIVEN a Dependency with a name and short name, WHEN its edge label is computed, THEN the label contains the name and short name")
    @Test
    public void testGetDependencyLabelOfDependencyWithNameAndShortName() {
        Dependency dependency = SysmlFactory.eINSTANCE.createDependency();
        dependency.setDeclaredShortName(SHORT_NAME);
        dependency.setDeclaredName("dependency");

        assertThat(this.labelService.getDependencyLabel(dependency)).isEqualTo(SHORT_NAME_LABEL + " dependency");
    }

    @DisplayName("GIVEN an Interface with a name and short name, WHEN its edge label is computed, THEN the label contains the name and short name")
    @Test
    public void testGetEdgeLabelOfInterfaceWithNameAndShortName() {
        InterfaceUsage interfaceUsage = SysmlFactory.eINSTANCE.createInterfaceUsage();
        interfaceUsage.setDeclaredShortName(SHORT_NAME);
        interfaceUsage.setDeclaredName("interface");

        assertThat(this.labelService.getEdgeLabel(interfaceUsage)).isEqualTo(SHORT_NAME_LABEL + " interface");
    }

    @DisplayName("GIVEN a namespace imported, WHEN the label of an attribute whose type is from the imported namespace, THEN the attribute's type should be shortened in its label")
    @Test
    public void testAttributeTypeShortenedIfNamespaceImported() {
        String customTypeName = "CustomType";

        Package parentPackage = SysmlFactory.eINSTANCE.createPackage();
        parentPackage.setDeclaredName("Parent");

        Package definition = SysmlFactory.eINSTANCE.createPackage();
        definition.setDeclaredName("TypeDefinition");
        this.addOwnedMember(parentPackage, definition);

        DataType customDataType = SysmlFactory.eINSTANCE.createDataType();
        customDataType.setDeclaredName(customTypeName);
        this.addOwnedMember(definition, customDataType);

        Package usage = SysmlFactory.eINSTANCE.createPackage();
        usage.setDeclaredName("TypeUsage");
        this.addOwnedMember(parentPackage, usage);

        NamespaceImport nsImport = SysmlFactory.eINSTANCE.createNamespaceImport();
        nsImport.setImportedNamespace(definition);
        this.addOwnedMember(usage, nsImport);

        PartDefinition partDef = SysmlFactory.eINSTANCE.createPartDefinition();
        partDef.setDeclaredName("PartDef1");
        this.addOwnedMember(usage, partDef);

        AttributeUsage attribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        attribute.setDeclaredName("x1");
        this.addOwnedMember(partDef, attribute);

        FeatureTyping typing = SysmlFactory.eINSTANCE.createFeatureTyping();
        typing.setType(customDataType);
        attribute.getOwnedRelationship().add(typing);

        assertThat(this.labelService.getCompartmentItemLabel(attribute)).isEqualTo("x1 : CustomType");
    }

    private void addOwnedMember(Element parent, Element child) {
        OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        owningMembership.getOwnedRelatedElement().add(child);
        parent.getOwnedRelationship().add(owningMembership);
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
