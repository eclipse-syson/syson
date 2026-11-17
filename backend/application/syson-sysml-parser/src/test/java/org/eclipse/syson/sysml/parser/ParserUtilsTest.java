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
package org.eclipse.syson.sysml.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests {@link ParserUtils}.
 *
 * @author arichard
 */
public class ParserUtilsTest {

    /**
     * Verifies that the delimiter-separating space is removed from inline comments.
     */
    @Test
    public void removesInlineCommentDelimiterSeparatingSpace() {
        assertEquals("text ", ParserUtils.processCommentBody("/* text */"));
    }

    /**
     * Verifies that whitespace before the delimiter-separating space is preserved.
     */
    @Test
    public void preservesInlineWhitespaceBeforeDelimiterSeparatingSpace() {
        assertEquals("text \t  ", ParserUtils.processCommentBody("/* text \t  */"));
    }

    /**
     * Verifies that a trailing tab is preserved when no delimiter-separating space is present.
     */
    @Test
    public void preservesInlineTrailingTab() {
        assertEquals("text\t", ParserUtils.processCommentBody("/* text\t*/"));
    }

    /**
     * Verifies that multiline comments retain trailing whitespace on their final line.
     */
    @Test
    public void preservesMultilineCommentTrailingWhitespace() {
        assertEquals("first\nsecond ", ParserUtils.processCommentBody("/* first\n * second */"));
    }

    /**
     * Verifies that multiline comments with Windows line endings retain their final-line whitespace.
     */
    @Test
    public void preservesCrLfMultilineCommentTrailingWhitespace() {
        assertEquals("first\nsecond ", ParserUtils.processCommentBody("/* first\r\n * second */"));
    }

    /**
     * Verifies that a missing comment body remains missing.
     */
    @Test
    public void preservesNullCommentBody() {
        assertNull(ParserUtils.processCommentBody(null));
    }

}
