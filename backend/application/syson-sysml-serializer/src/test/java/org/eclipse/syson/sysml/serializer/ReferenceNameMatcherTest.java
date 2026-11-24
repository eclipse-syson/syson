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
package org.eclipse.syson.sysml.serializer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.syson.sysml.serializer.references.ReferenceNameMatcher;
import org.junit.jupiter.api.Test;

/**
 * Tests quote-insensitive comparison of rendered reference names.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class ReferenceNameMatcherTest {

    /**
     * Names differing only by SysML identifier quotes must compare as equivalent; different qualifications must remain
     * distinct.
     */
    @Test
    void equivalentIgnoresSysMLNameQuotes() {
        assertTrue(ReferenceNameMatcher.equivalent("'target'", "target"));
        assertTrue(ReferenceNameMatcher.equivalent("'namespace'::'target'",
                "namespace::target"));
        assertFalse(ReferenceNameMatcher.equivalent("first::target",
                "second::target"));
    }

    /**
     * A quoted name containing {@code .} or {@code ::} is one name and must not compare equal to the feature chain or
     * qualified name spelled with the same characters; escaped quotes must compare by their decoded content.
     */
    @Test
    void equivalentKeepsQuotedNameBoundaries() {
        assertFalse(ReferenceNameMatcher.equivalent("'a.b'", "a.b"));
        assertFalse(ReferenceNameMatcher.equivalent("'a::b'", "a::b"));
        assertTrue(ReferenceNameMatcher.equivalent("'a'.'b'", "a.b"));
        assertTrue(ReferenceNameMatcher.equivalent("'a.b'", "'a.b'"));
        assertTrue(ReferenceNameMatcher.equivalent("'it\\'s'", "'it\\'s'"));
        assertFalse(ReferenceNameMatcher.equivalent("'it\\'s'", "its"));
    }

    /**
     * Every escape sequence of the grammar is compared by its decoded character: {@code '\n'} is a line break and
     * differs from {@code '\\n'}, a backslash followed by {@code n}, whereas {@code '\"'} is just a double quote.
     */
    @Test
    void equivalentDecodesEveryEscapeSequence() {
        assertFalse(ReferenceNameMatcher.equivalent("'a\\nb'", "'a\\\\nb'"));
        assertTrue(ReferenceNameMatcher.equivalent("'a\\nb'", "'a\nb'"));
        assertTrue(ReferenceNameMatcher.equivalent("'a\\tb'", "'a\tb'"));
        assertTrue(ReferenceNameMatcher.equivalent("'a\\\"b'", "'a\"b'"));
        assertFalse(ReferenceNameMatcher.equivalent("'a\\rb'", "'a\\nb'"));
    }
}
