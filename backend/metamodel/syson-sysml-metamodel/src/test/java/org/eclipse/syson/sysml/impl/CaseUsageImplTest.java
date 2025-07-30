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

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.CaseUsage;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * Test class for {@link CaseUsageImpl}.
 *
 * @author arichard
 */
public class CaseUsageImplTest {

    private ModelBuilder builder;

    @BeforeEach
    public void setUp() {
        this.builder = new ModelBuilder();
    }

    @Test
    public void actorParameter() {
        CaseUsage caseUsage = this.builder.createWithName(CaseUsage.class, "caseUsage1");
        ActorMembership actorMembership = this.builder.createIn(ActorMembership.class, caseUsage);
        PartUsage partUsage = this.builder.createIn(PartUsage.class, actorMembership);

        assertFalse(caseUsage.getActorParameter().isEmpty());
        assertSame(1, caseUsage.getActorParameter().size());
        assertEquals(partUsage, caseUsage.getActorParameter().get(0));
    }
}
