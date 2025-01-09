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

import java.util.List;
import java.util.stream.Stream;

import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.StakeholderMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RequirementUsageImpl}.
 *
 * @author flatombe
 */
public class RequirementUsageImplTest {

    private static final SysmlFactory FACTORY = SysmlPackage.eINSTANCE.getSysmlFactory();

    private static final RequirementUsage REQUIREMENT_USAGE = FACTORY.createRequirementUsage();

    private static final List<PartUsage> STAKEHOLDER_PART_USAGES = Stream.of(FACTORY.createPartUsage(), FACTORY.createPartUsage()).toList();

    private static final List<PartUsage> ACTOR_PART_USAGES = Stream.of(FACTORY.createPartUsage(), FACTORY.createPartUsage()).toList();

    @BeforeAll
    public static void setUpRequirementUsage() {
        // ActorParameter
        ACTOR_PART_USAGES.forEach(actorPartUsage -> {
            final ActorMembership actorMembership = FACTORY.createActorMembership();
            actorMembership.getOwnedRelatedElement().add(actorPartUsage);
            REQUIREMENT_USAGE.getOwnedRelationship().add(actorMembership);
        });

        // StakeholderParameter
        STAKEHOLDER_PART_USAGES.forEach(stakeholderPartUsage -> {
            final StakeholderMembership stakeholderMembership = FACTORY.createStakeholderMembership();
            stakeholderMembership.getOwnedRelatedElement().add(stakeholderPartUsage);
            REQUIREMENT_USAGE.getOwnedRelationship().add(stakeholderMembership);
        });
    }

    @Test
    public void testGetActorParameter() {
        final List<PartUsage> actorParameterValue = REQUIREMENT_USAGE.getActorParameter();
        Assertions.assertNotNull(actorParameterValue);
        Assertions.assertIterableEquals(ACTOR_PART_USAGES, actorParameterValue);
    }

    @Test
    public void testGetStakeholderParameter() {
        final List<PartUsage> stakeholderParameterValue = REQUIREMENT_USAGE.getStakeholderParameter();
        Assertions.assertNotNull(stakeholderParameterValue);
        Assertions.assertIterableEquals(STAKEHOLDER_PART_USAGES, stakeholderParameterValue);
    }
}
