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

/**
 * Text normalization utilities shared by the parser's semantic actions.
 * <p>
 * These operations convert lexical representations into model values. They do not perform name resolution or modify
 * the parsed model.
 * </p>
 *
 * @author ylussaud
 */
public class ParserUtils {

    /**
     * Invalid escape sequence message.
     */
    private static final String INVALID_ESCAPE_SEQUENCE = "Invalid escape sequence : \\";

    /**
     * Gets the string value of the given text.
     *
     * @param text
     *            the parsed text
     * @return the string value of the given text
     */
    public static String getStringValue(String text) {
        return stripSlashes(text.substring(1, text.length() - 1));
    }

    /**
     * Returns the text passed as argument replacing all escaped characters to there value.
     *
     * @param text
     *            the text to convert
     * @return the converted text
     */
    public static String stripSlashes(String text) {
        final int end = text.length();
        final StringBuilder res = new StringBuilder(end);
        int i = 0;
        while (i < end) {
            char c = text.charAt(i);
            if (c != '\\') {
                res.append(c);
            } else if (i + 1 < end) {
                char c1 = text.charAt(++i);
                unescapeCharSequence(res, c1);
            } else {
                throw new IllegalArgumentException(INVALID_ESCAPE_SEQUENCE);
            }
            ++i;
        }
        return res.toString();
    }

    /**
     * Un-escapes the character sequence <code>\c1</code> and fill the given {@link StringBuilder} with the result.
     *
     * @param builder
     *            the {@link StringBuilder} to fill
     * @param c1
     *            the second character of the escape sequence
     */
    private static void unescapeCharSequence(final StringBuilder builder, char c1) {
        switch (c1) {
            case 'b':
                builder.append('\b');
                break;
            case 't':
                builder.append('\t');
                break;
            case 'n':
                builder.append('\n');
                break;
            case 'f':
                builder.append('\f');
                break;
            case 'r':
                builder.append('\r');
                break;
            case '\\':
                builder.append('\\');
                break;
            case '\"':
                builder.append('\"');
                break;
            case '\'':
                builder.append('\'');
                break;
            default:
                throw new IllegalArgumentException(INVALID_ESCAPE_SEQUENCE + c1);
        }
    }

    /**
     * Processes the comment body.
     *
     * @param body
     *            the body {@link String}
     * @return the processed comment body
     */
    public static String processCommentBody(String body) {
        String result = body;
        if (result != null) {
            result = result.replaceFirst("/\\*", "").replaceFirst("^\\s*", "");
            if (result.endsWith("*/")) {
                result = result.substring(0, result.length() - 2);
            }
            String[] lines = result.split("\\r?\\n");
            if (lines.length == 1) {
                result = lines[0];
            } else {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < lines.length; i++) {
                    // Strip initial white space(not including line breaks) -
                    // by splitting no line breaks at the end
                    String s = lines[i].replaceFirst("^\\s*", "");
                    if (s.startsWith("* ")) {
                        s = s.replaceFirst("\\*\\s{1}", "");
                    } else if (s.startsWith("*")) {
                        s = s.replaceFirst("\\*", "");
                    }
                    if (i != 0) {
                        // Add back newline
                        builder.append("\n");
                    }
                    builder.append(s);
                }
                result = builder.toString();
            }
        }
        return result;
    }

}
