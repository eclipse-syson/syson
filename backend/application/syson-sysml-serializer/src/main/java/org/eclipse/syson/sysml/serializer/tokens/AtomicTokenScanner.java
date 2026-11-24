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
package org.eclipse.syson.sysml.serializer.tokens;

/**
 * Recognizes the lexical tokens of KerMLExpressions.g4 whose content is opaque to text heuristics: string literals
 * ({@code STRING_VALUE}), unrestricted names ({@code UNRESTRICTED_NAME}) and comments ({@code REGULAR_COMMENT},
 * {@code ML_NOTE}, {@code SL_NOTE}). Parentheses, dots or names inside them are not expression syntax, so scanners
 * step over them as a whole. Expression scanners and the HTML reference search share it, so they agree with the lexer
 * on where such a token ends.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class AtomicTokenScanner {

    private static final char STRING_DELIMITER = '"';

    private static final char NAME_DELIMITER = '\'';

    private static final char ESCAPE = '\\';

    private static final String NOTE_START = SysMLTokens.LINE_COMMENT_PREFIX + SysMLTokens.BLOCK_COMMENT_ASTERISK;

    private AtomicTokenScanner() {
        // Prevent instantiation.
    }

    /**
     * Returns the index just after the string literal, unrestricted name or comment starting at {@code index}, or
     * {@code index} itself when none starts there. An unterminated token extends to the end of the text, as the
     * lexer would not end it earlier either.
     *
     * @param text
     *            the scanned text
     * @param index
     *            a position in {@code text}
     * @return the end of the atomic token, or {@code index}
     */
    public static int atomicTokenEnd(final String text, final int index) {
        final char character = text.charAt(index);
        int end = index;
        if (character == STRING_DELIMITER || character == NAME_DELIMITER) {
            end = quotedTokenEnd(text, index, character);
        } else if (text.startsWith(NOTE_START, index)) {
            end = delimitedEnd(text, index + NOTE_START.length(), SysMLTokens.BLOCK_COMMENT_END);
        } else if (text.startsWith(SysMLTokens.BLOCK_COMMENT_START, index)) {
            end = delimitedEnd(text, index + SysMLTokens.BLOCK_COMMENT_START.length(), SysMLTokens.BLOCK_COMMENT_END);
        } else if (text.startsWith(SysMLTokens.LINE_COMMENT_PREFIX, index)) {
            end = lineCommentEnd(text, index + SysMLTokens.LINE_COMMENT_PREFIX.length());
        }
        return end;
    }

    /** Returns the index after the closing delimiter matching the one at {@code start}, skipping escapes. */
    private static int quotedTokenEnd(final String text, final int start, final char delimiter) {
        int index = start + 1;
        while (index < text.length() && text.charAt(index) != delimiter) {
            if (text.charAt(index) == ESCAPE) {
                index++;
            }
            index++;
        }
        return Math.min(index + 1, text.length());
    }

    /** Skips a line comment through its CR, LF or CRLF ending, or to the end of the text. */
    private static int lineCommentEnd(final String text, final int from) {
        int index = from;
        while (index < text.length() && !text.startsWith(SysMLTokens.CARRIAGE_RETURN, index)
                && !text.startsWith(SysMLTokens.LINE_FEED, index)) {
            index++;
        }
        if (text.startsWith(SysMLTokens.CARRIAGE_RETURN + SysMLTokens.LINE_FEED, index)) {
            index++;
        }
        return Math.min(index + 1, text.length());
    }

    private static int delimitedEnd(final String text, final int from, final String terminator) {
        final int terminatorIndex = text.indexOf(terminator, from);
        if (terminatorIndex < 0) {
            return text.length();
        }
        return terminatorIndex + terminator.length();
    }
}
