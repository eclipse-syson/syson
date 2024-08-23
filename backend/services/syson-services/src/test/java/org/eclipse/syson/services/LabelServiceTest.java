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

    private LabelService labelService;

    @BeforeEach
    void beforeEach() {
        this.labelService = new LabelService(new IFeedbackMessageService.NoOp());
    }

    @DisplayName("Check Usage prefix label with default properties")
    @Test
    void testUsagePrefixLabelWithDefaultProperties() {
        Usage usageWithNoDirection = SysmlFactory.eINSTANCE.createUsage();

        assertEquals(LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsagePrefix(usageWithNoDirection));
    }

    @DisplayName("Check Usage prefix label with abstract property")
    @Test
    void testUsagePrefixLabelWithAbstractProperty() {
        Usage usageWithNoDirection = SysmlFactory.eINSTANCE.createUsage();

        usageWithNoDirection.setIsAbstract(true);
        assertEquals(LabelConstants.ABSTRACT + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsagePrefix(usageWithNoDirection));
    }

    @DisplayName("Check Usage prefix label with derived property")
    @Test
    void testUsagePrefixLabelWithDerivedProperty() {
        Usage usageWithNoDirection = SysmlFactory.eINSTANCE.createUsage();

        usageWithNoDirection.setIsDerived(true);
        assertEquals(LabelConstants.DERIVED + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsagePrefix(usageWithNoDirection));
    }

    @DisplayName("Check Usage prefix label with end property")
    @Test
    void testUsagePrefixLabelWithEndProperty() {
        Usage usageWithNoDirection = SysmlFactory.eINSTANCE.createUsage();

        usageWithNoDirection.setIsEnd(true);
        assertEquals(LabelConstants.END + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsagePrefix(usageWithNoDirection));
    }

    @DisplayName("Check Usage prefix label with readonly property")
    @Test
    void testUsagePrefixLabelWithReadOnlyProperty() {
        Usage usageWithNoDirection = SysmlFactory.eINSTANCE.createUsage();

        usageWithNoDirection.setIsReadOnly(true);
        assertEquals(LabelConstants.READ_ONLY + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsagePrefix(usageWithNoDirection));
    }

    @DisplayName("Check Usage prefix label with readonly property")
    @Test
    void testUsagePrefixLabelWithVariationProperty() {
        Usage usageWithNoDirection = SysmlFactory.eINSTANCE.createUsage();

        usageWithNoDirection.setIsVariation(true);
        assertEquals(LabelConstants.VARIATION + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsagePrefix(usageWithNoDirection));
    }

    @DisplayName("Check Usage prefix label with many custom properties")
    @Test
    void testUsagePrefixLabelWithManyCustomProperties() {
        Usage usageWithNoDirection = SysmlFactory.eINSTANCE.createUsage();
        usageWithNoDirection.setIsAbstract(true);
        usageWithNoDirection.setIsDerived(true);
        usageWithNoDirection.setIsEnd(true);
        usageWithNoDirection.setIsReadOnly(true);
        usageWithNoDirection.setIsVariation(true);

        assertEquals(LabelConstants.VARIATION + LabelConstants.SPACE + LabelConstants.READ_ONLY + LabelConstants.SPACE
                + LabelConstants.DERIVED + LabelConstants.SPACE
                + LabelConstants.END
                + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsagePrefix(usageWithNoDirection));
    }

    @DisplayName("Check Usage prefix label with direction properties")
    @Test
    void testUsagePrefixLabelWithDirectionProperties() {
        Usage usageWithDirection = SysmlFactory.eINSTANCE.createUsage();

        usageWithDirection.setDirection(FeatureDirectionKind.IN);
        assertEquals(LabelConstants.IN + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsagePrefix(usageWithDirection));

        usageWithDirection.setDirection(FeatureDirectionKind.OUT);
        assertEquals(LabelConstants.OUT + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsagePrefix(usageWithDirection));

        usageWithDirection.setDirection(FeatureDirectionKind.INOUT);
        assertEquals(LabelConstants.INOUT + LabelConstants.SPACE + LabelConstants.REF + LabelConstants.SPACE, this.labelService.getUsagePrefix(usageWithDirection));
    }

    @DisplayName("Check AttributeUsage prefix label with default properties")
    @Test
    void testAttributeUsagePrefixLabelWithDefaultProperties() {
        AttributeUsage attributeUsageWithNoDirection = SysmlFactory.eINSTANCE.createAttributeUsage();

        assertEquals("", this.labelService.getUsagePrefix(attributeUsageWithNoDirection));
    }

}
