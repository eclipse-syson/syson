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
package org.eclipse.syson.sysml.serializer.names;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.junit.jupiter.api.Test;

/**
 * Tests reference-name escaping.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class NameEscaperTest {

    private final NameEscaper escaper = new NameEscaper();

    /**
     * Separators and escaped characters already present inside quoted reference segments must remain unchanged.
     */
    @Test
    void preservesSeparatorsAndEscapesInsideQuotedReferenceSegments() {
        for (final String name : new String[] { "P::'A::B'", "'P::Q'::A", "P::'a.b'.field",
            "'P\\\\'::A", "P::'A\\'::B'" }) {
            assertEquals(name, this.escaper.escapeReferenceName(name));
        }
    }

    /**
     * A reference that starts and ends with a quote but has unquoted segments in between is not one unrestricted name:
     * its unquoted reserved or invalid segments must still be quoted.
     */
    @Test
    void escapesUnquotedSegmentsBetweenQuotedOnes() {
        assertEquals("'a'::'part'::'c'", this.escaper.escapeReferenceName("'a'::part::'c'"));
        assertEquals("'a'.'part'.'c'", this.escaper.escapeReferenceName("'a'.part.'c'"));
        assertEquals("'a'::b::'c'", this.escaper.escapeReferenceName("'a'::b::'c'"));
        assertEquals("'it\\'s'", this.escaper.escapeReferenceName("'it\\'s'"));
    }

    /**
     * Reserved words and invalid name segments must be quoted; valid names, already quoted segments, and the
     * conjugation, wildcard, and root-qualification markers must be preserved.
     */
    @Test
    void escapesCompoundReferenceNames() {
        assertEquals("Collections::Array.dimensions",
                this.escaper.escapeReferenceName(
                        "Collections::Array.dimensions"));
        assertEquals("'package'::'part'",
                this.escaper.escapeReferenceName("package::part"));
        assertEquals("'invalid.path-segment'::target",
                this.escaper.escapeReferenceName(
                        "invalid.path-segment::target"));
        assertEquals("'invalid-segment.next.more'::target",
                this.escaper.escapeReferenceName(
                        "invalid-segment.next.more::target"));
        assertEquals("namespace::'invalid-segment'.target",
                this.escaper.escapeReferenceName(
                        "namespace::invalid-segment.target"));
        assertEquals("'quoted.path'",
                this.escaper.escapeReferenceName("'quoted.path'"));
        assertEquals(SysMLTokens.CONJUGATION_OPERATOR + "namespace::*",
                this.escaper.escapeReferenceName(SysMLTokens.CONJUGATION_OPERATOR + "namespace::*"));
        assertEquals("$::namespace::member",
                this.escaper.escapeReferenceName("$::namespace::member"));
    }

    /** The control characters that the grammar escapes are written as their escape sequences, as the parser reads them. */
    @Test
    void escapesControlCharactersOfUnrestrictedNames() {
        assertEquals("'a\\b\\t\\n\\f\\r'", this.escaper.escapeUnrestrictedName("a\b\t\n\f\r"));
        assertEquals("'\\\\n\\''", this.escaper.escapeUnrestrictedName("\\n'"));
    }
}
