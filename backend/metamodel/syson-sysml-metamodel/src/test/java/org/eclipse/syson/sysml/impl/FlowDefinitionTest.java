/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.eclipse.syson.sysml.FlowDefinition;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.Test;

/**
 * {@link FlowDefinition} related tests.
 *
 * @author rpage
 */
public class FlowDefinitionTest {

    @Test
    public void testElementIdNotEmpty() {
        FlowDefinition flowDefinition = SysmlFactory.eINSTANCE.createFlowDefinition();
        assertFalse(flowDefinition.getElementId().isEmpty());
    }
}
