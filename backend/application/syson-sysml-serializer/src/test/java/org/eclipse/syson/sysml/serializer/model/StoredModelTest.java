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
package org.eclipse.syson.sysml.serializer.model;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Reads stored model values without failing on absent elements.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class StoredModelTest {

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /** Like their neighbours, the ownership and member-name queries must answer {@code null} for a missing element. */
    @Test
    void answersNullForMissingElements() {
        assertNull(StoredModel.memberName(null));
        assertNull(StoredModel.memberShortName(null));
        assertNull(StoredModel.owner(null));
        assertNull(StoredModel.owningNamespace(null));
        assertNull(StoredModel.owningType(null));
    }
}
