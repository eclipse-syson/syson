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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.FeatureDirectionKind;
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
public class LabelServiceTest extends AbstractServiceTest {

    private static final String ATTRIBUTE_USAGE_NAME = "myAttributeUsage";

    private static final String ATTRIBUTE_USAGE_SHORT_NAME = "shortName";

    private LabelService labelService;

    @BeforeEach
    void beforeEach() {
        this.labelService = new LabelService(new IFeedbackMessageService.NoOp());
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
}
