/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.api.Assertions;
import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.Test;

/**
 * Name generation-related tests.
 *
 * @author gdaniel
 */
public class DescriptionNameGeneratorTest {

    @Test
    public void testGetCreationToolNameForUsageWithMatchingDefinition() {
        IDescriptionNameGenerator nameGenerator = new DescriptionNameGenerator("");
        String creationToolName = nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getAttributeUsage());
        assertEquals("New Attribute", creationToolName);
    }

    @Test
    public void testGetCreationToolNameForUsageWithoutMatchingDefinition() {
        IDescriptionNameGenerator nameGenerator = new DescriptionNameGenerator("");
        String creationToolName = nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getConnectorAsUsage());
        assertEquals("New Connector As Usage", creationToolName);
    }

    @Test
    public void testGetCreationToolNameForDefinition() {
        IDescriptionNameGenerator nameGenerator = new DescriptionNameGenerator("");
        String creationToolName = nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getAttributeDefinition());
        assertEquals("New Attribute Definition", creationToolName);
    }

    @Test
    public void testIsCompartmentName() {
        IDescriptionNameGenerator nameGenerator = new DescriptionNameGenerator("fakeDiag");
        this.assertIsCompartmentName(false, nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getAttributeDefinition()), nameGenerator);
        this.assertIsCompartmentName(false, nameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getConnectionUsage()), nameGenerator);
        this.assertIsCompartmentName(false, nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), nameGenerator);
        this.assertIsCompartmentName(false, nameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage()), nameGenerator);
        this.assertIsCompartmentName(false, nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getPortUsage()), nameGenerator);
        this.assertIsCompartmentName(false, nameGenerator.getInheritedBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage()), nameGenerator);


        this.assertIsCompartmentName(true, nameGenerator.getCompartmentName("concept1", "suffix1"), nameGenerator);
        this.assertIsCompartmentName(false, nameGenerator.getCompartmentItemName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort()),
                nameGenerator);
        this.assertIsCompartmentItemName(true, nameGenerator.getCompartmentItemName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort()),
                nameGenerator);
        this.assertIsCompartmentName(true, nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort()), nameGenerator);
        this.assertIsCompartmentName(false, nameGenerator.getInheritedCompartmentItemName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort()), nameGenerator);
        this.assertIsCompartmentItemName(true, nameGenerator.getInheritedCompartmentItemName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort()), nameGenerator);

    }

    private void assertIsCompartmentName(boolean expect, String name, IDescriptionNameGenerator nameGenerator) {
        Assertions.assertThat(
                nameGenerator.isCompartmentNodeDescriptionName(name)).isEqualTo(expect);
    }

    private void assertIsCompartmentItemName(boolean expect, String name, IDescriptionNameGenerator nameGenerator) {
        Assertions.assertThat(
                nameGenerator.isCompartmentItemNodeDescriptionName(name)).isEqualTo(expect);
    }

}
