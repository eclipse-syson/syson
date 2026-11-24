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


import org.eclipse.syson.sysml.serializer.tokens.AtomicTokenScanner;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;

/**
 * Stateless operations on rendered expression text: feature-chain path prefixes, leading comments and parenthesis
 * grouping. They inspect text markers only and do not parse SysML.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class ExpressionPathSupport {

    private ExpressionPathSupport() {
        // Prevent instantiation.
    }

    /**
     * Tells whether rendered operand text contains only comments and whitespace, so a sequence can omit an element that
     * holds no expression while keeping an operand preceded by a comment.
     *
     * @param text
     *            the rendered text
     * @return {@code true} if the text contains comments and no expression tokens
     */
    static boolean isCommentOnlyText(final String text) {
        int index = 0;
        boolean hasComment = false;
        while (index < text.length()) {
            if (Character.isWhitespace(text.charAt(index))) {
                index++;
            } else if (text.startsWith(SysMLTokens.LINE_COMMENT_PREFIX, index)
                    || text.startsWith(SysMLTokens.BLOCK_COMMENT_START, index)) {
                hasComment = true;
                index = AtomicTokenScanner.atomicTokenEnd(text, index);
            } else {
                return false;
            }
        }
        return hasComment;
    }

    /**
     * Wraps rendered text in one pair of parentheses.
     *
     * @param text
     *            the rendered text
     * @return {@code (text)}
     */
    static String parenthesized(final String text) {
        return SysMLTokens.OPEN_PARENTHESIS + text + SysMLTokens.CLOSE_PARENTHESIS;
    }

    /**
     * Tells whether one pair of parentheses encloses the whole text, as in {@code (a + b)} but not {@code (a) + (b)},
     * so that callers do not group already grouped text twice.
     *
     * @param text
     *            the rendered text
     * @return {@code true} if the opening parenthesis at index 0 closes at the last index
     */
    static boolean isEnclosedInParentheses(final String text) {
        return text.startsWith(SysMLTokens.OPEN_PARENTHESIS)
                && matchingClosingParenthesis(text, 0) == text.length() - 1;
    }

    /**
     * Returns the index of the parenthesis closing the one at {@code openingIndex}, or -1 if it is unbalanced.
     * Parentheses inside string literals, quoted names and comments are not grouping syntax and are skipped, so
     * {@code ('x)y' + 1)} closes at its last character.
     *
     * @param text
     *            the text to scan
     * @param openingIndex
     *            the index of an opening parenthesis
     * @return the closing index, or -1
     */
    static int matchingClosingParenthesis(final String text,
            final int openingIndex) {
        int nestingDepth = 0;
        int closingIndex = -1;
        int index = openingIndex;
        while (index < text.length() && closingIndex < 0) {
            final int atomicTokenEnd = AtomicTokenScanner.atomicTokenEnd(text, index);
            final char character = text.charAt(index);
            if (atomicTokenEnd > index) {
                index = atomicTokenEnd - 1;
            } else if (character == '(') {
                nestingDepth++;
            } else if (character == ')') {
                nestingDepth--;
                if (nestingDepth == 0) {
                    closingIndex = index;
                }
            }
            index++;
        }
        return closingIndex;
    }
}
