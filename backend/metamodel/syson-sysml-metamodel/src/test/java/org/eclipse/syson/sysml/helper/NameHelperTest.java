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
package org.eclipse.syson.sysml.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link NameHelper} methods.
 *
 * @author arichard
 */
public class NameHelperTest {

    @DisplayName("Test that names containg simple quotes are printed with these simple quotes escpaed.")
    @Test
    void testEscapeSimpleQuotes() {
        String printableName = NameHelper.toPrintableName("Hello what's up!");
        assertEquals("'Hello what\\'s up!'", printableName);
        printableName = NameHelper.toPrintableName("Hel'lo");
        assertEquals("'Hel\\'lo'", printableName);
    }
}
