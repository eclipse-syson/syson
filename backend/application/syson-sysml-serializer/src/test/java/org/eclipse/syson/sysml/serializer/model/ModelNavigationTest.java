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

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Tests {@link ModelNavigation} on degenerate inputs.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class ModelNavigationTest {

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /** Like the other navigation queries, the predicates answer {@code false} for a missing element or relationship. */
    @Test
    void answersFalseForMissingElements() {
        final ModelNavigation navigation = new ModelNavigation();
        assertFalse(navigation.isBodyRelationship(null));
        assertFalse(navigation.isOwnedByMetaclass(null, SysMLPackage.eINSTANCE.getFlowUsage()));
        assertFalse(navigation.isOwnedByMetaclass(null, SysMLPackage.eINSTANCE.getFlowUsage(),
                SysMLPackage.eINSTANCE.getSuccessionFlowUsage()));
        assertFalse(navigation.isEmptyStateEntryAction(null));
    }
}
