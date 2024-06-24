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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.syson.sysml.EventOccurrenceUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.Test;

/**
 * {@link EventOccurrenceUsageImpl} related tests.
 *
 * @author arichard
 */
public class EventOccurrenceUsageImplTest {

    @Test
    public void testIsReference() {
        EventOccurrenceUsage eventOccurrenceUsage = SysmlFactory.eINSTANCE.createEventOccurrenceUsage();
        assertTrue(eventOccurrenceUsage.isIsReference());
    }
}
