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

import static org.eclipse.syson.sysml.serializer.names.NameEscaper.isIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.names.NameEscaper;
import org.eclipse.syson.sysml.serializer.names.NameRenderer;
import org.eclipse.syson.sysml.serializer.tokens.ReferenceNameSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;

/**
 * Formats declared and reference names for their final SysML text position: {@code NAME} or
 * {@code UNRESTRICTED_NAME} segments, joined by {@code ::} (qualified names) or {@code .} (feature chains). Works on
 * spellings that {@link NameRenderer} has already escaped and repairs what composing them can break.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class SysMLNameFormatter {

    private final NameRenderer nameRenderer;

    private final ParserAdapterQueries adapters;

    /**
     * Creates a formatter that shares the run's name escaping and cache.
     *
     * @param nameRenderer
     *            the run's name renderer
     * @param adapters
     *            parser markers telling whether a declared name was written quoted
     */
    SysMLNameFormatter(final NameRenderer nameRenderer, final ParserAdapterQueries adapters) {
        this.nameRenderer = nameRenderer;
        this.adapters = adapters;
    }

    /**
     * Returns a short name ready to write between {@code <} and {@code >}; any non-identifier is quoted, and an
     * already quoted name is kept, so names escaped by {@link NameRenderer#declaredShortName(Element)} pass through.
     */
    String renderedShortName(final String shortName) {
        if (isIdentifier(shortName)) {
            return shortName;
        }
        return this.quotedName(shortName);
    }

    /** Renders each name with {@link #referenceName(String)} and joins them with {@code , }. */
    String referenceNames(final List<String> names) {
        final StringJoiner renderedNames = new StringJoiner(SysMLTokens.ARGUMENT_SEPARATOR);
        for (final String name : names) {
            renderedNames.add(this.referenceName(name));
        }
        return renderedNames.toString();
    }

    /**
     * Renders a raw reference spelling for a reference position. Beyond the {@link NameRenderer} escaping, a segment
     * that is not an identifier is quoted, and a segment that the escaping quoted although it holds index-expression
     * syntax ({@code #(}) is unquoted again. A segment the given spelling already quoted is an
     * {@code UNRESTRICTED_NAME} and keeps its quotes whatever it contains; repeated segments ({@code 'a'.'a'}) are kept
     * since each one navigates. A name without separators is only unquoted, never quoted.
     */
    String referenceName(final String name) {
        final String rendered = this.nameRenderer.referenceName(name);
        if (!rendered.contains(SysMLTokens.PATH_SEPARATOR) && !rendered.contains(SysMLTokens.NAMESPACE_SEPARATOR)) {
            return withoutAddedExpressionQuotes(rendered, name);
        }
        final StringBuilder formatted = new StringBuilder(rendered.length());
        for (final String token : tokenizedPath(rendered)) {
            formatted.append(this.formattedPathToken(token, name));
        }
        return formatted.toString();
    }

    /**
     * Returns the declared name as written in a declaration. A name the source wrote quoted stays quoted even when it
     * would be a valid identifier; otherwise quotes are added only when required.
     *
     * @param element
     *            the declared element, or {@code null}
     * @return the rendered name, or an empty string when unnamed
     */
    String declaredName(final Element element) {
        String name;
        if (this.adapters.hasTextAdapter(element, ITextAdapter.QUOTED_NAME)) {
            name = this.escapedName(element.getDeclaredName(), true);
        } else {
            name = this.nameRenderer.declaredName(element);
        }
        return name;
    }

    /**
     * Writes a raw model name, such as a declared or alias name: as an unrestricted name when the source quoted it,
     * otherwise quoted only when it is not an identifier. Unlike {@link #quotedName(String)} and
     * {@link #referenceName(String)}, which take rendered spellings, apostrophes around the name belong to it: the name
     * {@code 'x'} is written <code>'\'x\''</code>.
     *
     * @param rawName
     *            the decoded model name, or {@code null}
     * @param quoted
     *            whether the source quoted the name
     * @return the escaped name, or an empty string when there is no name
     */
    String escapedName(final String rawName, final boolean quoted) {
        final NameEscaper escaper = new NameEscaper();
        String name = "";
        if (rawName != null && !rawName.isBlank() && quoted) {
            name = escaper.escapeUnrestrictedName(rawName);
        } else if (rawName != null && !rawName.isBlank()) {
            name = escaper.escapeDeclaredName(rawName);
        }
        return name;
    }

    /**
     * Writes a name as an unrestricted name, escaping backslashes and quotes; a name already delimited by quotes is
     * returned unchanged so the call is idempotent.
     */
    String quotedName(final String name) {
        if (isQuoted(name)) {
            return name;
        }
        return new NameEscaper().escapeUnrestrictedName(name);
    }

    /**
     * Renders each {@code ::} segment of a qualified name independently: identifiers go through reference escaping,
     * other unquoted segments (including ones containing dots) are quoted, and a <code>&#126;</code> prefix is
     * preserved, as is a leading {@code $} root marker ({@code globalQualification}), while a quoted {@code '$'} stays a
     * name. Used when the dots of a segment belong to a name and must not be read as feature navigation.
     */
    String quoteQualifiedNameSegments(final String name) {
        final StringJoiner quoted = new StringJoiner(SysMLTokens.NAMESPACE_SEPARATOR);
        int start = 0;
        int separator = ReferenceNameSyntax.separatorOutsideQuotes(name, SysMLTokens.NAMESPACE_SEPARATOR, start);
        while (separator >= 0) {
            quoted.add(this.quoteQualifiedNameSegment(name.substring(start, separator), start == 0));
            start = separator + SysMLTokens.NAMESPACE_SEPARATOR.length();
            separator = ReferenceNameSyntax.separatorOutsideQuotes(name, SysMLTokens.NAMESPACE_SEPARATOR, start);
        }
        return quoted.add(this.quoteQualifiedNameSegment(name.substring(start), start == 0)).toString();
    }

    /**
     * Quotes only the last {@code ::} segment, leaving the qualification as given. Used when the source quoted the
     * imported or aliased member's name and round-trip must keep that choice.
     */
    String quoteFinalQualifiedNameSegment(final String name) {
        final int separator = ReferenceNameSyntax.lastSeparatorOutsideQuotes(name, SysMLTokens.NAMESPACE_SEPARATOR);
        if (separator < 0) {
            return this.quotedName(name);
        }
        final int finalSegmentStart = separator + SysMLTokens.NAMESPACE_SEPARATOR.length();
        return name.substring(0, finalSegmentStart) + this.quotedName(name.substring(finalSegmentStart));
    }

    private String quoteQualifiedNameSegment(final String segment, final boolean first) {
        String quoted = segment;
        if (segment.startsWith(SysMLTokens.CONJUGATION_OPERATOR) && segment.length() > 1) {
            quoted = SysMLTokens.CONJUGATION_OPERATOR + this.quoteQualifiedNameSegment(segment.substring(1), first);
        } else if (!isQuoted(segment) && !(first && SysMLTokens.ROOT_NAMESPACE_MARKER.equals(segment))) {
            if (isIdentifier(segment)) {
                quoted = this.referenceName(segment);
            } else {
                quoted = this.quotedName(segment);
            }
        }
        return quoted;
    }

    /**
     * Splits a path into name segments and {@code .} / {@code ::} separator tokens; separators inside quoted names and
     * backslash escapes in them are kept within the segment.
     */
    private static List<String> tokenizedPath(final String path) {
        final List<String> tokens = new ArrayList<>();
        final StringBuilder segment = new StringBuilder();
        boolean quoted = false;
        int index = 0;
        while (index < path.length()) {
            final char character = path.charAt(index);
            if (quoted && character == '\\' && index + 1 < path.length()) {
                segment.append(character).append(path.charAt(index + 1));
                index += 2;
            } else if (character == '\'') {
                quoted = !quoted;
                segment.append(character);
                index++;
            } else if (!quoted && character == '.') {
                flushSegment(tokens, segment);
                tokens.add(SysMLTokens.PATH_SEPARATOR);
                index++;
            } else if (isNamespaceSeparatorOutsideQuotes(path, index, quoted)) {
                flushSegment(tokens, segment);
                tokens.add(SysMLTokens.NAMESPACE_SEPARATOR);
                index += SysMLTokens.NAMESPACE_SEPARATOR.length();
            } else {
                segment.append(character);
                index++;
            }
        }
        flushSegment(tokens, segment);
        return tokens;
    }

    private static boolean isNamespaceSeparatorOutsideQuotes(final String path, final int index,
            final boolean quoted) {
        return !quoted && path.startsWith(SysMLTokens.NAMESPACE_SEPARATOR, index);
    }

    private static void flushSegment(final List<String> tokens, final StringBuilder segment) {
        if (!segment.isEmpty()) {
            tokens.add(segment.toString());
            segment.setLength(0);
        }
    }

    /**
     * Unquotes an index-expression segment quoted by the escaping, quotes a segment that is not a valid name, and keeps
     * anything else.
     */
    private String formattedPathToken(final String token, final String source) {
        String formatted = token;
        if (isQuotedExpressionSegment(token)) {
            formatted = withoutAddedExpressionQuotes(token, source);
        } else if (requiresQuotes(token)) {
            formatted = this.quotedName(token);
        }
        return formatted;
    }

    /**
     * Tells whether a path segment must be quoted: not a valid identifier and not already syntax (quoted name,
     * separator, or {@code $} root marker). A conjugation prefix ({@code conjugatedQualifiedName}) is not part of the
     * name, so <code>&#126;x</code>, <code>&#126;'x'</code> and <code>&#126;$</code> are judged on what follows it.
     */
    private static boolean requiresQuotes(final String segment) {
        String name = segment;
        if (segment.startsWith(SysMLTokens.CONJUGATION_OPERATOR) && segment.length() > 1) {
            name = segment.substring(SysMLTokens.CONJUGATION_OPERATOR.length());
        }
        final boolean structuralToken = name.isBlank()
                || SysMLTokens.NAMESPACE_SEPARATOR.equals(name)
                || SysMLTokens.PATH_SEPARATOR.equals(name);
        final boolean syntaxToken = structuralToken
                || name.startsWith(SysMLTokens.UNRESTRICTED_NAME_DELIMITER)
                || SysMLTokens.ROOT_NAMESPACE_MARKER.equals(name);
        return !syntaxToken && !isIdentifier(name);
    }

    /**
     * Removes the quotes the escaping put around a segment holding index-expression syntax ({@code a#(1)} rendered as
     * {@code 'a#(1)'}); a segment already quoted in {@code source} is a name such as {@code 'a#(1)'} and is returned as
     * is, like any other segment.
     */
    private static String withoutAddedExpressionQuotes(final String segment, final String source) {
        if (isQuotedExpressionSegment(segment) && !source.contains(segment)) {
            return segment.substring(1, segment.length() - 1);
        }
        return segment;
    }

    private static boolean isQuotedExpressionSegment(final String segment) {
        return segment.length() > 2 && isQuoted(segment)
                && segment.substring(1, segment.length() - 1).contains(SysMLTokens.INDEX_START);
    }

    private static boolean isQuoted(final String name) {
        return name.length() > 1
                && name.startsWith(SysMLTokens.UNRESTRICTED_NAME_DELIMITER)
                && name.endsWith(SysMLTokens.UNRESTRICTED_NAME_DELIMITER);
    }
}
