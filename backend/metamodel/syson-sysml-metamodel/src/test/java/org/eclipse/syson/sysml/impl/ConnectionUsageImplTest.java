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

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.syson.sysml.ConnectionDefinition;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * {@link ConnectionUsageImpl} related tests.
 *
 * @author arichard
 */
public class ConnectionUsageImplTest {

    private ConnectionDefinition connectionDefinition;

    private PartDefinition partDef1;

    private PartDefinition partDef2;

    private ConnectionUsage connectionUsage;

    @BeforeEach
    public void setUp() {
        this.connectionDefinition = SysmlFactory.eINSTANCE.createConnectionDefinition();
        this.connectionDefinition.setIsIndividual(true);
        var membershipEnd1 = SysmlFactory.eINSTANCE.createFeatureMembership();
        var membershipEnd2 = SysmlFactory.eINSTANCE.createFeatureMembership();
        this.connectionDefinition.getOwnedRelationship().add(membershipEnd1);
        this.connectionDefinition.getOwnedRelationship().add(membershipEnd2);
        var end1 = SysmlFactory.eINSTANCE.createReferenceUsage();
        end1.setIsEnd(true);
        membershipEnd1.getOwnedRelatedElement().add(end1);
        var featureTypingEnd1 = SysmlFactory.eINSTANCE.createFeatureTyping();
        end1.getOwnedRelationship().add(featureTypingEnd1);
        this.partDef1 = SysmlFactory.eINSTANCE.createPartDefinition();
        featureTypingEnd1.setType(this.partDef1);
        var end2 = SysmlFactory.eINSTANCE.createReferenceUsage();
        end2.setIsEnd(true);
        membershipEnd2.getOwnedRelatedElement().add(end2);
        var featureTypingEnd2 = SysmlFactory.eINSTANCE.createFeatureTyping();
        end2.getOwnedRelationship().add(featureTypingEnd2);
        this.partDef2 = SysmlFactory.eINSTANCE.createPartDefinition();
        featureTypingEnd2.setType(this.partDef2);

        this.connectionUsage = SysmlFactory.eINSTANCE.createConnectionUsage();
        var featureTypingConnectionUsage = SysmlFactory.eINSTANCE.createFeatureTyping();
        featureTypingConnectionUsage.setType(this.connectionDefinition);
        this.connectionUsage.getOwnedRelationship().add(featureTypingConnectionUsage);
        var membershipEnd3 = SysmlFactory.eINSTANCE.createFeatureMembership();
        var membershipEnd4 = SysmlFactory.eINSTANCE.createFeatureMembership();
        this.connectionUsage.getOwnedRelationship().add(membershipEnd3);
        this.connectionUsage.getOwnedRelationship().add(membershipEnd4);
        var end3 = SysmlFactory.eINSTANCE.createReferenceUsage();
        end3.setIsEnd(true);
        membershipEnd3.getOwnedRelatedElement().add(end3);
        var subsettingEnd3 = SysmlFactory.eINSTANCE.createSubsetting();
        subsettingEnd3.setSubsettingFeature(end3);
        subsettingEnd3.setSubsettedFeature(end1);
        end3.getOwnedRelationship().add(subsettingEnd3);
        var end4 = SysmlFactory.eINSTANCE.createReferenceUsage();
        end4.setIsEnd(true);
        membershipEnd4.getOwnedRelatedElement().add(end4);
        var subsettingEnd4 = SysmlFactory.eINSTANCE.createSubsetting();
        subsettingEnd4.setSubsettingFeature(end4);
        subsettingEnd4.setSubsettedFeature(end2);
        end4.getOwnedRelationship().add(subsettingEnd4);
    }

    @Test
    public void testIndividualConnection() {
        var individualDefinition = this.connectionUsage.getIndividualDefinition();
        assertThat(individualDefinition).isEqualTo(this.connectionDefinition);
    }

    @Test
    public void testOccurrenceDefinition() {
        var occurrenceDefinitions = this.connectionUsage.getOccurrenceDefinition();
        assertThat(occurrenceDefinitions).hasSize(1).contains(this.connectionDefinition);
    }

    @Test
    public void testItemDefinition() {
        var itemDefinitions = this.connectionUsage.getItemDefinition();
        assertThat(itemDefinitions).hasSize(1).contains(this.connectionDefinition);
    }

    @Test
    public void testPartDefinition() {
        var partDefinitions = this.connectionUsage.getPartDefinition();
        assertThat(partDefinitions).hasSize(1).contains(this.connectionDefinition);
    }

    @Test
    public void testConnectionDefinition() {
        var connectionDefinitions = this.connectionUsage.getConnectionDefinition();
        assertThat(connectionDefinitions).hasSize(1).contains(this.connectionDefinition);
    }
}
