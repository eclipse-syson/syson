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

import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MetadataUsageImpl}.
 * 
 * @author Arthur Daussy
 */
public class MetadataUsageImplTest {

    private final SysmlFactory fact = SysmlFactory.eINSTANCE;

    @Test
    @DisplayName("Test the implementation of getMetadataDefinition method")
    public void getMetadataDefinition() {

        MetadataDefinition metaData1 = fact.createMetadataDefinition();
        metaData1.setDeclaredName("m1");

        FeatureTyping featuringType = fact.createFeatureTyping();
        featuringType.setType(metaData1);

        MetadataUsage metaDataUsage = fact.createMetadataUsage();
        OwningMembership owningMember = fact.createOwningMembership();
        owningMember.getOwnedRelatedElement().add(featuringType);
        metaDataUsage.getOwnedRelationship().add(owningMember);

        assertEquals(metaData1, metaDataUsage.getMetadataDefinition());
    }

}
