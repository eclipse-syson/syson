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
package org.eclipse.syson.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
