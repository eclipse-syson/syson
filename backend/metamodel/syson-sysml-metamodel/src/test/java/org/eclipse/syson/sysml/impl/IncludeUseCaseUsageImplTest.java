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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.IncludeUseCaseUsage;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.junit.jupiter.api.Test;

/**
 * {@link IncludeUseCaseUsageImpl} related tests.
 *
 * @author arichard
 */
public class IncludeUseCaseUsageImplTest {

    @Test
    public void testUseCaseIncluded() {
        IncludeUseCaseUsage includeUseCaseUsage = SysmlFactory.eINSTANCE.createIncludeUseCaseUsage();
        UseCaseUsage useCaseIncluded = includeUseCaseUsage.getUseCaseIncluded();
        assertNull(useCaseIncluded);

        ReferenceSubsetting referencesubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        includeUseCaseUsage.getOwnedRelationship().add(referencesubsetting);
        UseCaseUsage useCaseUsage = SysmlFactory.eINSTANCE.createUseCaseUsage();
        referencesubsetting.setReferencedFeature(useCaseUsage);

        useCaseIncluded = includeUseCaseUsage.getUseCaseIncluded();
        assertEquals(useCaseUsage, useCaseIncluded);
    }

    @Test
    public void testPerformedAction() {
        IncludeUseCaseUsage includeUseCaseUsage = SysmlFactory.eINSTANCE.createIncludeUseCaseUsage();
        ActionUsage performedAction = includeUseCaseUsage.getPerformedAction();
        assertNull(performedAction);

        ReferenceSubsetting referencesubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        includeUseCaseUsage.getOwnedRelationship().add(referencesubsetting);
        UseCaseUsage useCaseUsage = SysmlFactory.eINSTANCE.createUseCaseUsage();
        referencesubsetting.setReferencedFeature(useCaseUsage);

        performedAction = includeUseCaseUsage.getPerformedAction();
        assertEquals(useCaseUsage, performedAction);
    }

    @Test
    public void testEventOccurrence() {
        IncludeUseCaseUsage includeUseCaseUsage = SysmlFactory.eINSTANCE.createIncludeUseCaseUsage();
        OccurrenceUsage eventOccurrence = includeUseCaseUsage.getEventOccurrence();
        assertNull(eventOccurrence);

        ReferenceSubsetting referencesubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        includeUseCaseUsage.getOwnedRelationship().add(referencesubsetting);
        UseCaseUsage useCaseUsage = SysmlFactory.eINSTANCE.createUseCaseUsage();
        referencesubsetting.setReferencedFeature(useCaseUsage);

        eventOccurrence = includeUseCaseUsage.getEventOccurrence();
        assertEquals(useCaseUsage, eventOccurrence);
    }
}
