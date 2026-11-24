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
package org.eclipse.syson.sysml.serializer.expression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.eclipse.syson.sysml.serializer.tokens.AtomicTokenScanner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Keeps comment boundaries consistent with the lexer when scanning grouping and reference spellings.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class AtomicTokenScannerTest {

    /** Code after any supported line ending must remain visible to grouping and reference replacement. */
    @ParameterizedTest
    @ValueSource(strings = { "\n", "\r\n", "\r" })
    void keepsCodeAfterLineComments(final String lineEnding) {
        final String text = "(a + // comment" + lineEnding + "b)";

        assertEquals(text.indexOf('b'), AtomicTokenScanner.atomicTokenEnd(text, text.indexOf("//")));
        assertEquals(text.length() - 1, ExpressionPathSupport.matchingClosingParenthesis(text, 0));
        assertEquals("(a + // comment" + lineEnding + "renamed)",
                ReferenceTextScanner.replaceFirstReferences(text, List.of(new ReferenceRename("b", "renamed"))));
    }

    /** An unterminated line comment must extend to EOF and protect reference-like text inside it. */
    @Test
    void keepsLineCommentAtEndOfInput() {
        final String text = "a // b";

        assertEquals(text.length(), AtomicTokenScanner.atomicTokenEnd(text, text.indexOf("//")));
        assertEquals(text, ReferenceTextScanner.replaceFirstReferences(text, List.of(new ReferenceRename("b", "renamed"))));
    }
}
