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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.SysmlFactory;
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

    private ViewLabelService viewLabelService;

    @BeforeEach
    void beforeEach() {
        this.viewLabelService = new ViewLabelService(new IFeedbackMessageService.NoOp(), new ShowDiagramsIconsService());
    }

    @DisplayName("Check Attribute Usage item label with default properties")
    @Test
    void testItemCompartmentLabelWithDefaultProperties() {
        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        attributeUsage.setDeclaredName(ATTRIBUTE_USAGE_NAME);
        assertEquals(ATTRIBUTE_USAGE_NAME, this.viewLabelService.getCompartmentItemLabel(attributeUsage));
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

}
