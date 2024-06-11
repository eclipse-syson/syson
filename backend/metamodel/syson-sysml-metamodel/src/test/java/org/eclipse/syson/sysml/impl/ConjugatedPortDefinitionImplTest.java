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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link ConjugatedPortDefinition}.
 *
 * @author Arthur Daussy
 */
public class ConjugatedPortDefinitionImplTest {

    private ModelBuilder builder;

    @BeforeEach
    public void setUp() {
        this.builder = new ModelBuilder();
    }

    @Test
    public void getNames() {
        PortDefinition port = this.builder.createWithName(PortDefinition.class, "p1");
        ConjugatedPortDefinition conjugatedPort = this.builder.createInWithName(ConjugatedPortDefinition.class, port, "unusedName");
        assertEquals(LabelConstants.CONJUGATED + "p1", conjugatedPort.getName());

    }
}
