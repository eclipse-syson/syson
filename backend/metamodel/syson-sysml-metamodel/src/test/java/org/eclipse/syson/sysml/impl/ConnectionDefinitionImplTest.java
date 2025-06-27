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
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * {@link ConnectionDefinitionImpl} related tests.
 *
 * @author arichard
 */
public class ConnectionDefinitionImplTest {

    private ConnectionDefinition connectionDefinition;

    private PartDefinition partDef1;

    private PartDefinition partDef2;

    @BeforeEach
    public void setUp() {
        this.connectionDefinition = SysmlFactory.eINSTANCE.createConnectionDefinition();
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
        featureTypingEnd1.setTypedFeature(end1);
        var end2 = SysmlFactory.eINSTANCE.createReferenceUsage();
        end2.setIsEnd(true);
        membershipEnd2.getOwnedRelatedElement().add(end2);
        var featureTypingEnd2 = SysmlFactory.eINSTANCE.createFeatureTyping();
        end2.getOwnedRelationship().add(featureTypingEnd2);
        this.partDef2 = SysmlFactory.eINSTANCE.createPartDefinition();
        featureTypingEnd2.setType(this.partDef2);
        featureTypingEnd2.setTypedFeature(end2);
    }

    @Test
    public void testRelatedType() {
        var relatedTypes = this.connectionDefinition.getRelatedType();
        assertThat(relatedTypes).hasSize(2).contains(this.partDef1, this.partDef2);
    }

    @Test
    public void testSourceType() {
        var sourceType = this.connectionDefinition.getSourceType();
        assertThat(sourceType).isEqualTo(this.partDef1);
    }

    @Test
    public void testTargetType() {
        var targetTypes = this.connectionDefinition.getTargetType();
        assertThat(targetTypes).hasSize(1).contains(this.partDef2);
    }
}
