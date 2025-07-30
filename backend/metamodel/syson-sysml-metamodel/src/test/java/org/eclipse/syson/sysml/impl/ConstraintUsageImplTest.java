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
package org.eclipse.syson.sysml.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * Test class for {@link ConstraintUsageImpl}.
 *
 * @author Arthur Daussy
 */
public class ConstraintUsageImplTest {

    private ModelBuilder builder;

    @BeforeEach
    public void setUp() {
        this.builder = new ModelBuilder();
    }

    @Test
    public void getNames() {
        ConstraintUsage superConstraint = this.builder.createWithName(ConstraintUsage.class, "SuperConstraint");
        superConstraint.setDeclaredShortName("SC");

        ConstraintUsage constraint = this.builder.create(ConstraintUsage.class);


        RequirementConstraintMembership reqMembership = this.builder.create(RequirementConstraintMembership.class);

        reqMembership.getOwnedRelatedElement().add(constraint);
        this.builder.addReferenceSubsetting(constraint, superConstraint);

        assertEquals("SuperConstraint", constraint.getName());
        assertEquals("SuperConstraint", constraint.effectiveName());
        assertEquals("SC", constraint.getShortName());
        assertEquals("SC", constraint.effectiveShortName());

        // Set declaredName and declaredShortName

        constraint.setDeclaredName("Co1");
        constraint.setDeclaredShortName("C1");

        assertEquals("Co1", constraint.getName());
        assertEquals("Co1", constraint.effectiveName());
        assertEquals("C1", constraint.getShortName());
        assertEquals("C1", constraint.effectiveShortName());
    }

}
