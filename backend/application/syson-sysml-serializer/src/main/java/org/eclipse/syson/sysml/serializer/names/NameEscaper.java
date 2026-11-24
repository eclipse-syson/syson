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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.eclipse.syson.sysml.parser.internal.SysMLLexer;
import org.eclipse.syson.sysml.serializer.tokens.ReferenceNameSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;

/**
 * Turns raw model names into the {@code name} grammar rule: an {@code ID} when the name has the identifier shape and
 * is not a keyword, otherwise an {@code UNRESTRICTED_NAME} ({@code 'name'}) with backslashes and single quotes escaped.
 * Reference spellings ({@code A::B.c}, <code>&#126;A</code>, {@code $::A}) are escaped segment by segment.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class NameEscaper {

    private static final String ESCAPED_BACKSLASH = "\\\\";

    private static final String ESCAPED_UNRESTRICTED_NAME_DELIMITER = "\\'";

    private static final Set<String> RESERVED_WORDS = collectLexerReservedWords();

    /**
     * Escapes one raw declared name. {@code null} means absent and gives an empty string; an empty name becomes
     * {@code ''}, since the grammar has no other spelling for it.
     *
     * @param name
     *            the raw name, or {@code null}
     * @return the name as an {@code ID} or an {@code UNRESTRICTED_NAME}
     */
    public String escapeDeclaredName(final String name) {
        if (name == null) {
            return "";
        }
        String escapedName = name;
        if (!isIdentifier(name) || RESERVED_WORDS.contains(name)) {
            escapedName = this.escapeUnrestrictedName(name);
        }
        return escapedName;
    }

    /**
     * Always writes {@code name} as an unrestricted name ({@code 'name'}), escaping backslashes, single quotes and the
     * control characters that the grammar spells {@code \b}, {@code \t}, {@code \n}, {@code \f} and {@code \r}, as the
     * parser decodes them. Callers use it directly when the source spelled a regular name with quotes and that choice
     * must be kept.
     *
     * @param name
     *            the raw name
     * @return the quoted name
     */
    public String escapeUnrestrictedName(final String name) {
        final StringBuilder escaped = new StringBuilder(name.length() + 2).append(SysMLTokens.UNRESTRICTED_NAME_DELIMITER);
        for (final char character : name.toCharArray()) {
            escaped.append(escapedCharacter(character));
        }
        return escaped.append(SysMLTokens.UNRESTRICTED_NAME_DELIMITER).toString();
    }

    /** Returns the spelling of one character inside an unrestricted name. */
    private static String escapedCharacter(final char character) {
        return switch (character) {
            case '\\' -> ESCAPED_BACKSLASH;
            case '\'' -> ESCAPED_UNRESTRICTED_NAME_DELIMITER;
            case '\b' -> "\\b";
            case '\t' -> "\\t";
            case '\n' -> "\\n";
            case '\f' -> "\\f";
            case '\r' -> "\\r";
            default -> String.valueOf(character);
        };
    }

    /**
     * Escapes a reference spelling whose structure ({@code ::}, {@code .}, <code>&#126;</code>) is already SysML
     * syntax. Quoted segments are kept as they are, so escaping an escaped reference changes nothing.
     *
     * @param name
     *            the reference spelling, or {@code null}
     * @return the escaped reference, or an empty string for a blank name
     */
    public String escapeReferenceName(final String name) {
        final String escapedName;
        if (name == null || name.isBlank()) {
            escapedName = "";
        } else if (isQuotedName(name)) {
            escapedName = name;
        } else if (name.startsWith(SysMLTokens.CONJUGATION_OPERATOR) || name.contains(SysMLTokens.NAMESPACE_SEPARATOR)
                || name.contains(SysMLTokens.PATH_SEPARATOR)) {
            escapedName = this.escapeCompoundReferenceName(name);
        } else {
            escapedName = this.escapeDeclaredName(name);
        }
        return escapedName;
    }

    /**
     * Escapes a qualified, dotted or conjugated reference. A leading {@code $} root marker ({@code globalQualification})
     * is kept unquoted, also right after a conjugation prefix ({@code conjugatedQualifiedName}: <code>&#126;$::P</code>).
     * A {@code ::} qualification segment whose dotted parts are not all regular feature-chain steps is one
     * name containing dots, so it is quoted whole; the last segment is always read as a feature chain.
     */
    private String escapeCompoundReferenceName(final String name) {
        final boolean conjugated = name.startsWith(SysMLTokens.CONJUGATION_OPERATOR);
        String prefix = "";
        String qualifiedName = name;
        if (conjugated) {
            prefix = SysMLTokens.CONJUGATION_OPERATOR;
            qualifiedName = name.substring(prefix.length());
        }
        final List<String> segments = ReferenceNameSyntax.splitOutsideQuotes(qualifiedName,
                SysMLTokens.NAMESPACE_SEPARATOR);
        final StringJoiner escaped = new StringJoiner(SysMLTokens.NAMESPACE_SEPARATOR, prefix, "");
        final int lastIndex = segments.size() - 1;
        for (int index = 0; index < lastIndex; index++) {
            final String segment = segments.get(index);
            if (index == 0 && SysMLTokens.ROOT_NAMESPACE_MARKER.equals(segment)) {
                escaped.add(segment);
            } else if (this.isNameContainingDots(segment)) {
                escaped.add(this.escapeDeclaredName(segment));
            } else {
                escaped.add(this.escapeFeatureChain(segment));
            }
        }
        return escaped.add(this.escapeFeatureChain(segments.get(lastIndex))).toString();
    }

    /**
     * Escapes each {@code .}-separated step of a feature chain or import path, keeping wildcards unquoted. A quoted
     * dotted name is one lexical segment, which some queries already return to preserve a source spelling: it is kept
     * intact so that escaping twice is harmless.
     */
    private String escapeFeatureChain(final String name) {
        if (isQuotedName(name)) {
            return name;
        }
        return ReferenceNameSyntax.splitOutsideQuotes(name, SysMLTokens.PATH_SEPARATOR).stream()
                .map(this::escapeFeatureChainStep)
                .collect(Collectors.joining(SysMLTokens.PATH_SEPARATOR));
    }

    private String escapeFeatureChainStep(final String step) {
        if (isWildcard(step) || isQuotedName(step)) {
            return step;
        }
        return this.escapeDeclaredName(step);
    }

    /**
     * Tells whether an unquoted qualification segment contains dots and at least one of its dotted parts is neither a
     * valid identifier nor a wildcard, meaning the dots belong to the name itself.
     */
    private boolean isNameContainingDots(final String segment) {
        if (isQuotedName(segment)) {
            return false;
        }
        final List<String> parts = ReferenceNameSyntax.splitOutsideQuotes(segment, SysMLTokens.PATH_SEPARATOR);
        return parts.size() > 1
                && parts.stream().anyMatch(part -> !isWildcard(part) && !isQuotedName(part) && !isIdentifier(part));
    }

    private static boolean isWildcard(final String step) {
        return SysMLTokens.WILDCARD_SYMBOL.equals(step) || SysMLTokens.RECURSIVE_WILDCARD.equals(step);
    }

    /**
     * Tells whether {@code name} is exactly one {@code UNRESTRICTED_NAME} token: it opens with a quote and its first
     * unescaped closing quote is its last character. {@code 'a'::b::'c'} starts and ends with a quote but is a
     * qualified name whose middle segment still needs escaping.
     */
    private static boolean isQuotedName(final String name) {
        if (name.length() < 2 || !name.startsWith(SysMLTokens.UNRESTRICTED_NAME_DELIMITER)) {
            return false;
        }
        int index = 1;
        while (index < name.length() && name.charAt(index) != '\'') {
            if (name.charAt(index) == '\\') {
                index++;
            }
            index++;
        }
        return index == name.length() - 1;
    }

    /**
     * Decodes the character after a backslash in an {@code UNRESTRICTED_NAME}: {@code b}, {@code t}, {@code n},
     * {@code f} and {@code r} stand for control characters, and any other character, such as a quote or a backslash,
     * stands for itself.
     *
     * @param escape
     *            the character following the backslash
     * @return the decoded character
     */
    public static char unescapedCharacter(final char escape) {
        return switch (escape) {
            case 'b' -> '\b';
            case 't' -> '\t';
            case 'n' -> '\n';
            case 'f' -> '\f';
            case 'r' -> '\r';
            default -> escape;
        };
    }

    /**
     * Tests the shape of the {@code ID} lexer rule ({@code [a-zA-Z_][a-zA-Z_0-9]*}, ASCII only). Keywords have that
     * shape too and must be checked separately.
     *
     * @param name
     *            the candidate name
     * @return {@code true} when the lexer reads {@code name} as a single {@code ID} or keyword token
     */
    public static boolean isIdentifier(final String name) {
        boolean identifier = !name.isEmpty() && isIdentifierStart(name.charAt(0));
        for (int index = 1; index < name.length() && identifier; index++) {
            final char character = name.charAt(index);
            identifier = isIdentifierStart(character) || character >= '0' && character <= '9';
        }
        return identifier;
    }

    private static boolean isIdentifierStart(final char character) {
        final boolean uppercase = character >= 'A' && character <= 'Z';
        final boolean lowercase = character >= 'a' && character <= 'z';
        return uppercase || lowercase || character == '_';
    }

    /**
     * Derives the reserved words from the generated lexer's word literals, so names that collide with any keyword of
     * the grammar get quoted without maintaining a separate keyword list.
     */
    private static Set<String> collectLexerReservedWords() {
        final Set<String> words = new HashSet<>();
        for (int tokenType = 1; tokenType < SysMLLexer.ID; tokenType++) {
            final String literal = SysMLLexer.VOCABULARY.getLiteralName(tokenType);
            if (literal != null && literal.length() > 2) {
                final String text = literal.substring(1, literal.length() - 1);
                if (text.chars().allMatch(character -> Character.isLetterOrDigit(character) || character == '_')) {
                    words.add(text);
                }
            }
        }
        return Set.copyOf(words);
    }
}
