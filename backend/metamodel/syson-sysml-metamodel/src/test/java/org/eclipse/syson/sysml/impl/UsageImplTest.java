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
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.VariantMembership;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link UsageImpl}.
 *
 * @author Arthur Daussy
 */
public class UsageImplTest {

    private final ModelBuilder builder = new ModelBuilder();

    @Test
    public void getNamesFromReferenceSubsetting() {

        Usage usage = this.builder.create(Usage.class);

        assertNull(usage.getName());
        assertNull(usage.getShortName());

        Feature feature = this.builder.createWithName(Feature.class, "f1");
        feature.setDeclaredShortName("f");

        this.builder.addReferenceSubsetting(usage, feature);

        assertEquals("f1", usage.getName());
        assertEquals("f", usage.getShortName());

        // define a name and short name

        usage.setDeclaredName("u1");
        usage.setDeclaredShortName("u");

        assertEquals("u1", usage.getName());
        assertEquals("u", usage.getShortName());
    }

    @Test
    public void getNamesFromRedefinitionInVariant() {

        VariantMembership variantMembership = this.builder.create(VariantMembership.class);
        Usage usage = this.builder.create(Usage.class);

        variantMembership.getOwnedRelatedElement().add(usage);

        assertNull(usage.getName());
        assertNull(usage.getShortName());

        Feature feature = this.builder.createWithName(Feature.class, "f1");
        feature.setDeclaredShortName("f");

        this.builder.addRedefinition(usage, feature);

        assertEquals("f1", usage.getName());
        assertEquals("f", usage.getShortName());

        // define a name and short name

        usage.setDeclaredName("u1");
        usage.setDeclaredShortName("u");

        assertEquals("u1", usage.getName());
        assertEquals("u", usage.getShortName());
    }

}
