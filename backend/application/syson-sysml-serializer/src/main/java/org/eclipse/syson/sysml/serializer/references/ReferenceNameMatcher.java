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
package org.eclipse.syson.sysml.serializer.references;

import org.eclipse.syson.sysml.serializer.names.NameEscaper;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;

/**
 * Compares rendered SysML reference names without letting lexical quoting affect model-level decisions, while keeping
 * the boundaries quotes draw: {@code 'a.b'} is one name, {@code a.b} is a feature chain.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ReferenceNameMatcher {

    private static final char QUOTE = SysMLTokens.UNRESTRICTED_NAME_DELIMITER.charAt(0);

    private static final char BACKSLASH = '\\';

    private ReferenceNameMatcher() {
        // Static domain service.
    }

    /**
     * Tells whether two spellings name the same thing once quoting is ignored, so {@code 'a'} and {@code a} match but
     * {@code 'a.b'} and {@code a.b} (one name versus a feature chain) do not.
     *
     * @param left
     *            a rendered name, not {@code null}
     * @param right
     *            another rendered name, not {@code null}
     * @return {@code true} when their {@link #normalized(String)} keys are equal
     */
    public static boolean equivalent(final String left, final String right) {
        return normalized(left).equals(normalized(right));
    }

    /**
     * Builds a comparison key in which every quoted segment is written canonically: unquoted when its decoded content
     * is an identifier, re-quoted with canonical escaping otherwise. Text outside quotes, including the {@code .} and
     * {@code ::} separators, is kept, so quoting a segment never merges it with its neighbours.
     *
     * @param name
     *            a rendered name, not {@code null}
     * @return the comparison key
     */
    public static String normalized(final String name) {
        final StringBuilder key = new StringBuilder(name.length());
        int index = 0;
        while (index < name.length()) {
            final char character = name.charAt(index);
            if (character == QUOTE) {
                final StringBuilder content = new StringBuilder();
                index = decodeUnrestrictedName(name, index + 1, content);
                key.append(canonicalSegment(content.toString()));
            } else {
                key.append(character);
                index++;
            }
        }
        return key.toString();
    }

    /**
     * Reads the content of an {@code UNRESTRICTED_NAME} starting after its opening quote, decoding every escape
     * sequence of the grammar, so that {@code '\n'} (a line break) and {@code '\\n'} (a backslash and {@code n}) differ,
     * and returns the index after the closing quote, or the end of the text when the quote is not closed.
     */
    private static int decodeUnrestrictedName(final String name, final int start, final StringBuilder content) {
        int index = start;
        while (index < name.length() && name.charAt(index) != QUOTE) {
            final char character = name.charAt(index);
            if (character == BACKSLASH && index + 1 < name.length()) {
                content.append(NameEscaper.unescapedCharacter(name.charAt(index + 1)));
                index += 2;
            } else {
                content.append(character);
                index++;
            }
        }
        return Math.min(index + 1, name.length());
    }

    private static String canonicalSegment(final String content) {
        if (NameEscaper.isIdentifier(content)) {
            return content;
        }
        return new NameEscaper().escapeUnrestrictedName(content);
    }

}
