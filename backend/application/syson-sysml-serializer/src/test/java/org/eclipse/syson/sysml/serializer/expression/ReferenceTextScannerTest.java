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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Splits reference spellings and finds reference tokens with the same quoted-name, string and comment boundaries as
 * the lexer.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class ReferenceTextScannerTest {

    /** The quoted name of a qualified reference containing an escaped backslash. */
    private static final String ESCAPED_BACKSLASH_NAME = "'a\\\\'";

    /** A quote after an even number of backslashes closes the name: the next segment must stay separate. */
    @Test
    void closesQuotedNamesAfterEscapedBackslashes() {
        final String path = ESCAPED_BACKSLASH_NAME + "::b";

        assertEquals(List.of(ESCAPED_BACKSLASH_NAME, "b"), ReferenceTextScanner.referenceSegments(path));
        assertEquals("b", ReferenceTextScanner.lastReferenceSegment(path));
        assertEquals(ESCAPED_BACKSLASH_NAME, ReferenceTextScanner.lastReferenceSegment("b::" + ESCAPED_BACKSLASH_NAME));
        assertEquals(2, ExpressionReferenceCollector.atomicReferences(path, null).size());
    }

    /** A quote after an odd number of backslashes is escaped and stays inside the name. */
    @Test
    void keepsEscapedQuotesInsideQuotedNames() {
        final String name = "'it\\'s::not'";

        assertEquals(List.of(name, "x"), ReferenceTextScanner.referenceSegments(name + ".x"));
        assertEquals("x", ReferenceTextScanner.lastReferenceSegment(name + ".x"));
    }

    /** An apostrophe inside a string literal or a comment must not hide the reference that follows. */
    @ParameterizedTest
    @ValueSource(strings = { "\"it's\" + target", "//* it's */ target", "/* it's */ target", "// it's\ntarget" })
    void ignoresApostrophesInStringsAndComments(final String text) {
        assertTrue(ReferenceTextScanner.referenceTokens(text).contains("target"), () -> String.valueOf(ReferenceTextScanner.referenceTokens(text)));
    }

    /** A path mixing qualification and navigation must expose its atomic segments and its prefixes. */
    @Test
    void extractsSegmentsOfMixedPaths() {
        final Set<String> tokens = ReferenceTextScanner.referenceTokens("P::a.b");

        assertTrue(tokens.containsAll(Set.of("P::a.b", "P::a", "P", "a", "b")), tokens::toString);
    }
}
