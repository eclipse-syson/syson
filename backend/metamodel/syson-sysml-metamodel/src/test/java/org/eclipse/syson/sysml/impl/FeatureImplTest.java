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

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link FeatureImpl}.
 *
 * @author Arthur Daussy
 */
public class FeatureImplTest {

    private ModelBuilder builder;

    @BeforeEach
    public void setUp() {
        this.builder = new ModelBuilder();
    }

    @Test
    public void testNames() {
        Feature f1 = this.builder.createWithName(Feature.class, "f1");
        f1.setDeclaredShortName("f");

        assertEquals("f1", f1.getName());
        assertEquals("f1", f1.effectiveName());
        assertEquals("f", f1.getShortName());
        assertEquals("f", f1.effectiveShortName());

        Feature f2 = this.builder.create(Feature.class);
        assertNull(f2.getName());
        assertNull(f2.effectiveName());
        assertNull(f2.getShortName());
        assertNull(f2.effectiveShortName());

        // Test redefinition

        this.builder.addRedefinition(f2, f1);
        assertEquals("f1", f2.getName());
        assertEquals("f1", f2.effectiveName());
        assertEquals("f", f2.getShortName());
        assertEquals("f", f2.effectiveShortName());
    }

}
