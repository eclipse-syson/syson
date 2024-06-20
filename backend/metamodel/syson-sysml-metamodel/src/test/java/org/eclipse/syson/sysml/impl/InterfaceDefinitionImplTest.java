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
package org.eclipse.syson.sysml.impl;

import static org.eclipse.syson.sysml.util.TestUtils.assertContentEquals;

import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link InterfaceDefinitionImpl}.
 *
 * @author Arthur Daussy
 */
public class InterfaceDefinitionImplTest {

    private final ModelBuilder builder = new ModelBuilder();

    @Test
    public void getInterfaceEnd() {
        InterfaceDefinition interfaceDef = this.builder.createWithName(InterfaceDefinition.class, "int1");

        PortUsage portUsage1 = this.builder.createInWithName(PortUsage.class, interfaceDef, "p1");
        portUsage1.setIsEnd(true);

        PortUsage portUsage2 = this.builder.createInWithName(PortUsage.class, interfaceDef, "p2");
        portUsage2.setIsEnd(true);

        assertContentEquals(interfaceDef.getInterfaceEnd(), portUsage1, portUsage2);
    }

}
