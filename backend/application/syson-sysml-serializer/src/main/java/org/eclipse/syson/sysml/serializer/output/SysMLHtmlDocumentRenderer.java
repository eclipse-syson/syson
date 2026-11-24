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
package org.eclipse.syson.sysml.serializer.output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.parser.internal.SysMLLexer;
import org.eclipse.syson.sysml.serializer.HtmlReferenceLinkResolver;
import org.eclipse.syson.sysml.serializer.model.SafeEmfAccess;
import org.eclipse.syson.sysml.serializer.model.StoredModel;
import org.eclipse.syson.sysml.serializer.output.ElementTrace.ElementRange;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Relationship;

/**
 * Converts serialized SysML text and element ranges to standalone HTML.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class SysMLHtmlDocumentRenderer {

    private static final Set<String> PUNCTUATION = Set.of(SysMLTokens.SHORT_NAME_START, SysMLTokens.SHORT_NAME_END,
            SysMLTokens.STATEMENT_TERMINATOR, SysMLTokens.OPEN_BRACE, SysMLTokens.CLOSE_BRACE, SysMLTokens.COMMA,
            SysMLTokens.NAMESPACE_SEPARATOR, SysMLTokens.OPEN_BRACKET, SysMLTokens.CLOSE_BRACKET,
            SysMLTokens.OPEN_PARENTHESIS, SysMLTokens.CLOSE_PARENTHESIS, SysMLTokens.PATH_SEPARATOR);

    private static final Set<String> WORD_LITERALS = Set.of(SysMLTokens.TRUE_LITERAL, SysMLTokens.FALSE_LITERAL,
            SysMLTokens.NULL_LITERAL);

    private static final Set<Integer> LITERAL_TOKEN_TYPES = Set.of(SysMLLexer.DECIMAL_VALUE, SysMLLexer.EXP_VALUE);

    private static final Set<Integer> IDENTIFIER_TOKEN_TYPES = Set.of(SysMLLexer.ID, SysMLLexer.UNRESTRICTED_NAME);

    private static final Set<Integer> COMMENT_TOKEN_TYPES = Set.of(SysMLLexer.REGULAR_COMMENT, SysMLLexer.ML_NOTE,
            SysMLLexer.SL_NOTE);

    /** Tooltip and data value for an element or target without id or type. */
    private static final String NONE = "<none>";

    private static final String DOCUMENT_PREFIX = """
            <!doctype html>
            <html lang="en">
            <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>SysML</title>
            <style>
            :root {
              --sysml-background: #ffffff;
              --sysml-foreground: #24292f;
              --sysml-gutter-background: #f6f8fa;
              --sysml-gutter-foreground: #57606a;
              --sysml-border: #d0d7de;
              --sysml-keyword: #cf222e;
              --sysml-identifier: #0550ae;
              --sysml-string: #0a3069;
              --sysml-literal: #953800;
              --sysml-comment: #6e7781;
              --sysml-operator: #8250df;
              --sysml-punctuation: #57606a;
              --sysml-reference: #0b6e4f;
              --sysml-marker: #1f6feb;
              --sysml-marker-hover: #0969da;
            }
            * { box-sizing: border-box; }
            body {
              margin: 0;
              color: var(--sysml-foreground);
              background: var(--sysml-background);
              font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", monospace;
              font-size: 14px;
              line-height: 1.5;
            }
            .sysml-source { min-width: max-content; padding: 0.75rem 0; }
            .sysml-line {
              display: grid;
              grid-template-columns: 4.5rem minmax(2rem, auto) 1fr;
              min-height: 1.5em;
            }
            .sysml-line:hover { background: color-mix(in srgb, var(--sysml-gutter-background) 55%, transparent); }
            .sysml-line-number,
            .sysml-object-gutter { background: var(--sysml-gutter-background); }
            .sysml-line-number {
              color: var(--sysml-gutter-foreground);
              padding: 0 0.75rem;
              text-align: right;
              user-select: none;
            }
            .sysml-object-gutter {
              position: relative;
              display: flex;
              align-items: center;
              gap: 0.3rem;
              padding: 0 0.6rem 0 0.25rem;
              border-right: 1px solid var(--sysml-border);
            }
            .sysml-object-gutter > [aria-hidden="true"] { position: absolute; }
            .sysml-object-marker {
              position: relative;
              display: inline-block;
              width: 0.65rem;
              height: 0.65rem;
              flex: 0 0 0.65rem;
              border-radius: 50%;
              background: var(--sysml-marker);
              cursor: help;
            }
            .sysml-object-marker:hover,
            .sysml-object-marker:focus { background: var(--sysml-marker-hover); outline: 2px solid var(--sysml-background); }
            .sysml-object-marker::after {
              content: attr(data-tooltip);
              position: absolute;
              z-index: 10;
              left: 0;
              top: calc(100% + 0.35rem);
              display: none;
              width: max-content;
              max-width: 30rem;
              padding: 0.35rem 0.55rem;
              color: var(--sysml-background);
              background: var(--sysml-foreground);
              border-radius: 0.25rem;
              font-size: 0.8rem;
              white-space: normal;
            }
            .sysml-object-marker:hover::after,
            .sysml-object-marker:focus::after { display: block; }
            .sysml-code { padding: 0 1rem; white-space: pre; tab-size: 4; }
            .syntax-keyword { color: var(--sysml-keyword); font-weight: 600; }
            .syntax-identifier { color: var(--sysml-identifier); }
            .syntax-string { color: var(--sysml-string); }
            .syntax-literal { color: var(--sysml-literal); }
            .syntax-comment { color: var(--sysml-comment); font-style: italic; }
            .syntax-operator { color: var(--sysml-operator); }
            .syntax-punctuation { color: var(--sysml-punctuation); }
            .sysml-reference { text-decoration: underline dotted var(--sysml-marker); text-underline-offset: 0.2em; }
            .sysml-reference .syntax-identifier,
            .sysml-reference .syntax-string { color: var(--sysml-reference); }
            </style>
            </head>
            <body>
            <main class="sysml-source" aria-label="Serialized SysML source">
            """;

    private static final String DOCUMENT_SUFFIX = """
            </main>
            </body>
            </html>
            """;

    private SysMLHtmlDocumentRenderer() {
    }

    /**
     * Builds the standalone HTML page for serialized text. Syntax classes come from re-lexing the text with the SysML
     * lexer rather than from the serializer, so highlighting matches what a parser would see. Each element range yields
     * a gutter marker on its first line; reference ranges become links when {@code linkResolver} provides one.
     *
     * @param source
     *            the plain SysML text whose offsets the ranges refer to
     * @param elementRanges
     *            the rendered elements and their text ranges
     * @param referenceRanges
     *            the reference occurrences and their text ranges, not overlapping
     * @param linkResolver
     *            the hyperlink resolver, or {@code null} to disable navigation (no links, ids or anchors)
     * @return the complete HTML document
     */
    static String render(final String source,
            final List<ElementRange> elementRanges,
            final List<ReferenceRange> referenceRanges,
            final HtmlReferenceLinkResolver linkResolver) {
        final List<SourceLine> lines = sourceLines(source);
        final List<StyledRange> styles = styledRanges(source);
        final Map<Integer, List<ElementRange>> markersByLine = markersByLine(
                lines, elementRanges, source.length());
        final Map<Integer, List<ReferenceRange>> referencesByLine = referencesByLine(
                lines, referenceRanges, source.length());
        final AliasAnchors aliasAnchors = AliasAnchors.create(elementRanges,
                linkResolver != null);
        final StringBuilder html = new StringBuilder(
                DOCUMENT_PREFIX.length() + source.length() * 2);
        html.append(DOCUMENT_PREFIX);
        appendAnchors(html, aliasAnchors.documentAnchors());
        int firstStyleIndex = 0;
        for (int index = 0; index < lines.size(); index++) {
            final SourceLine line = lines.get(index);
            while (firstStyleIndex < styles.size() && styles
                    .get(firstStyleIndex).endOffset() <= line.startOffset()) {
                firstStyleIndex++;
            }
            final LineRendering lineRendering = new LineRendering(styles,
                    firstStyleIndex,
                    markersByLine.getOrDefault(index, List.of()),
                    referencesByLine.getOrDefault(index, List.of()),
                    linkResolver, aliasAnchors);
            appendLine(html, source, line, index + 1, lineRendering);
        }
        html.append(DOCUMENT_SUFFIX);
        return html.toString();
    }

    private static void appendLine(final StringBuilder html,
            final String source, final SourceLine line, final int lineNumber,
            final LineRendering rendering) {
        html.append("<div class=\"sysml-line\" data-line=\"").append(lineNumber)
                .append("\">\n")
                .append("<span class=\"sysml-line-number\" aria-hidden=\"true\">")
                .append(lineNumber)
                .append("</span>\n<span class=\"sysml-object-gutter\">");
        for (final ElementRange marker : rendering.markers()) {
            appendMarker(html, marker.element(),
                    rendering.linkResolver() != null);
            appendAnchors(html,
                    rendering.aliasAnchors().forElement(marker.element()));
        }
        html.append("</span>\n<code class=\"sysml-code\">");
        appendHighlightedText(html, source, line.startOffset(),
                line.endOffset(), rendering);
        html.append("</code>\n</div>\n");
    }

    /**
     * Writes the gutter marker of an element starting on this line. Its id becomes the anchor that reference links
     * point to, so it is only emitted when navigation is enabled.
     */
    private static void appendMarker(final StringBuilder html, final Element element, final boolean navigationEnabled) {
        final String type = element.eClass().getName();
        final String id = elementId(element);
        String displayId = id;
        if (id.isBlank()) {
            displayId = NONE;
        }
        final String tooltip = escapeAttribute(type + " - ID: " + displayId);
        html.append("<span class=\"sysml-object-marker\"");
        if (navigationEnabled && !id.isBlank()) {
            html.append(" id=\"element-").append(escapeAttribute(id)).append("\"");
        }
        html.append(" tabindex=\"0\" data-element-type=\"").append(escapeAttribute(type))
                .append("\" data-element-id=\"").append(escapeAttribute(id))
                .append("\" data-tooltip=\"").append(tooltip)
                .append("\" aria-label=\"").append(tooltip)
                .append("\" title=\"").append(tooltip).append("\"></span>");
    }

    /** Writes invisible navigation anchors for elements that have no marker of their own. */
    private static void appendAnchors(final StringBuilder html, final List<Element> elements) {
        for (final Element element : elements) {
            final String id = elementId(element);
            if (!id.isBlank()) {
                html.append("<span id=\"element-").append(escapeAttribute(id))
                        .append("\" aria-hidden=\"true\"></span>");
            }
        }
    }

    /**
     * Writes one line's text, wrapping lexer tokens in their syntax classes; gaps between tokens (whitespace) stay
     * unstyled. Reference spans are applied inside each piece so tokens and references may cross each other.
     */
    private static void appendHighlightedText(final StringBuilder html,
            final String source, final int startOffset, final int endOffset,
            final LineRendering rendering) {
        int cursor = startOffset;
        for (int index = rendering.firstStyleIndex(); index < rendering
                .styles().size(); index++) {
            final StyledRange style = rendering.styles().get(index);
            if (style.startOffset() >= endOffset) {
                break;
            }
            final int styledStart = Math.max(style.startOffset(), startOffset);
            final int styledEnd = Math.min(style.endOffset(), endOffset);
            if (cursor < styledStart) {
                appendReferenceAwareText(html, source, cursor, styledStart,
                        null, rendering.references(),
                        rendering.linkResolver());
            }
            appendReferenceAwareText(html, source, styledStart, styledEnd,
                    style.cssClass(), rendering.references(),
                    rendering.linkResolver());
            cursor = styledEnd;
        }
        if (cursor < endOffset) {
            appendReferenceAwareText(html, source, cursor, endOffset, null,
                    rendering.references(), rendering.linkResolver());
        }
    }

    /**
     * Writes a slice of uniformly styled text, wrapping the parts covered by references in reference tags. Expects the
     * references sorted by start offset and not overlapping, as produced by a single linear output buffer.
     */
    private static void appendReferenceAwareText(final StringBuilder html,
            final String source, final int startOffset, final int endOffset,
            final String cssClass, final List<ReferenceRange> references,
            final HtmlReferenceLinkResolver linkResolver) {
        int cursor = startOffset;
        for (final ReferenceRange reference : references) {
            final int referenceStart = Math.max(reference.startOffset(),
                    startOffset);
            final int referenceEnd = Math.min(reference.endOffset(), endOffset);
            if (referenceEnd <= startOffset || referenceStart >= endOffset) {
                continue;
            }
            if (cursor < referenceStart) {
                appendSyntaxText(html, source.substring(cursor, referenceStart),
                        cssClass);
            }
            if (referenceStart < referenceEnd) {
                final ReferenceTag tag = referenceTag(reference.target(),
                        linkResolver);
                html.append(tag.opening());
                appendSyntaxText(html,
                        source.substring(referenceStart, referenceEnd),
                        cssClass);
                html.append("</").append(tag.name()).append(">");
                cursor = referenceEnd;
            }
        }
        if (cursor < endOffset) {
            appendSyntaxText(html, source.substring(cursor, endOffset),
                    cssClass);
        }
    }

    private static void appendSyntaxText(final StringBuilder html,
            final String text, final String cssClass) {
        if (cssClass == null) {
            html.append(escapeText(text));
        } else {
            html.append("<span class=\"").append(cssClass).append("\">")
                    .append(escapeText(text)).append("</span>");
        }
    }

    /**
     * Builds the tag of a reference: a link when the resolver supplies one, otherwise a span. The target type, id and
     * proxy URI are exposed as data attributes and in the tooltip so unresolved references remain diagnosable.
     */
    private static ReferenceTag referenceTag(final EObject target,
            final HtmlReferenceLinkResolver linkResolver) {
        final String type = objectType(target);
        final String id = elementId(target);
        final String uri = proxyUri(target);
        String displayId = id;
        if (id.isBlank()) {
            displayId = uri;
        }
        final String tooltip = "Reference -> " + type
                + referenceDisplaySuffix(displayId);
        final Optional<String> link = resolveLink(linkResolver, target);
        String tagName = "span";
        if (link.isPresent()) {
            tagName = "a";
        }
        final StringBuilder opening = new StringBuilder("<").append(tagName)
                .append(" class=\"sysml-reference\"");
        link.ifPresent(href -> opening.append(" href=\"")
                .append(escapeAttribute(href)).append("\""));
        opening.append(" data-reference-target-type=\"")
                .append(escapeAttribute(type))
                .append("\" data-reference-target-id=\"")
                .append(escapeAttribute(id))
                .append("\" data-reference-target-uri=\"")
                .append(escapeAttribute(uri)).append("\" title=\"")
                .append(escapeAttribute(tooltip)).append("\">");
        return new ReferenceTag(tagName, opening.toString());
    }

    /**
     * Returns the target's hyperlink. Elements owned by implied relationships are never linked: they are not rendered
     * and therefore have no anchor to navigate to.
     */
    private static Optional<String> resolveLink(
            final HtmlReferenceLinkResolver linkResolver,
            final EObject target) {
        Optional<String> link = Optional.empty();
        if (linkResolver != null && target != null
                && !isOwnedByImpliedRelationship(target)) {
            link = linkResolver.resolve(target).filter(value -> !value.isBlank());
        }
        return link;
    }

    private static boolean isOwnedByImpliedRelationship(
            final EObject object) {
        EObject current = object;
        while (current != null) {
            if (current instanceof final Relationship relationship
                    && relationship.isImplied()) {
                return true;
            }
            current = current.eContainer();
        }
        return false;
    }

    private static String objectType(final EObject target) {
        return SafeEmfAccess.get(() -> {
            if (target == null || target.eClass() == null) {
                return NONE;
            }
            return target.eClass().getName();
        }, "<unknown>");
    }

    private static String referenceDisplaySuffix(final String displayId) {
        if (displayId.isBlank()) {
            return "";
        }
        return " - " + displayId;
    }

    /**
     * Returns the element id already assigned to {@code object}, or an empty string for non-elements and elements that
     * have none yet: reading an unassigned id would assign one.
     */
    private static String elementId(final EObject object) {
        String id = null;
        if (object instanceof final Element element) {
            id = StoredModel.existingElementId(element);
        }
        return Objects.requireNonNullElse(id, "");
    }

    private static String proxyUri(final EObject target) {
        if (target instanceof final InternalEObject internal
                && internal.eIsProxy() && internal.eProxyURI() != null) {
            return internal.eProxyURI().toString();
        }
        return "";
    }

    /** Groups element markers by the line where each element starts, ordered from outer to inner element. */
    private static Map<Integer, List<ElementRange>> markersByLine(
            final List<SourceLine> lines, final List<ElementRange> ranges,
            final int sourceLength) {
        final Map<Integer, List<ElementRange>> result = new HashMap<>();
        for (final ElementRange range : ranges) {
            final int line = lineAtOffset(lines,
                    Math.min(range.startOffset(), sourceLength));
            result.computeIfAbsent(line, ignored -> new ArrayList<>())
                    .add(range);
        }
        final Comparator<ElementRange> outerToInner = Comparator
                .comparingInt(ElementRange::startOffset)
                .thenComparing(Comparator.comparingInt(ElementRange::endOffset)
                        .reversed())
                .thenComparing(range -> range.element().eClass().getName());
        result.values().forEach(markers -> markers.sort(outerToInner));
        return result;
    }

    /** Groups reference ranges by every line they cover, in text order; empty ranges are dropped. */
    private static Map<Integer, List<ReferenceRange>> referencesByLine(
            final List<SourceLine> lines, final List<ReferenceRange> ranges,
            final int sourceLength) {
        final Map<Integer, List<ReferenceRange>> result = new HashMap<>();
        for (final ReferenceRange range : ranges) {
            final int start = Math.min(range.startOffset(), sourceLength);
            final int end = Math.min(range.endOffset(), sourceLength);
            if (start >= end) {
                continue;
            }
            final int firstLine = lineAtOffset(lines, start);
            final int lastLine = lineAtOffset(lines, end - 1);
            for (int line = firstLine; line <= lastLine; line++) {
                result.computeIfAbsent(line, ignored -> new ArrayList<>())
                        .add(range);
            }
        }
        final Comparator<ReferenceRange> order = Comparator
                .comparingInt(ReferenceRange::startOffset)
                .thenComparingInt(ReferenceRange::endOffset);
        result.values().forEach(references -> references.sort(order));
        return result;
    }

    /** Returns the index of the line containing {@code offset}; offsets on a line separator belong to that line. */
    private static int lineAtOffset(final List<SourceLine> lines,
            final int offset) {
        int low = 0;
        int high = lines.size() - 1;
        while (low <= high) {
            final int middle = (low + high) >>> 1;
            if (lines.get(middle).startOffset() <= offset) {
                low = middle + 1;
            } else {
                high = middle - 1;
            }
        }
        return Math.max(0, high);
    }

    /** Splits on {@code \n}, {@code \r} and {@code \r\n}; always returns at least one line, even for empty text. */
    private static List<SourceLine> sourceLines(final String source) {
        final List<SourceLine> lines = new ArrayList<>();
        int start = 0;
        int offset = 0;
        while (offset < source.length()) {
            final char current = source.charAt(offset);
            if (current == '\r' || current == '\n') {
                lines.add(new SourceLine(start, offset));
                if (current == '\r' && offset + 1 < source.length()
                        && source.charAt(offset + 1) == '\n') {
                    offset++;
                }
                start = ++offset;
                continue;
            }
            offset++;
        }
        if (start < source.length() || lines.isEmpty()) {
            lines.add(new SourceLine(start, source.length()));
        }
        return List.copyOf(lines);
    }

    /** Lexes the text and returns styled token ranges in UTF-16 offsets; whitespace and unknown tokens are skipped. */
    private static List<StyledRange> styledRanges(final String source) {
        final int[] utf16OffsetsByCodePoint = utf16OffsetsByCodePoint(source);
        final SysMLLexer lexer = new SysMLLexer(CharStreams.fromString(source));
        lexer.removeErrorListeners();
        final List<StyledRange> ranges = new ArrayList<>();
        for (Token token = lexer.nextToken(); token
                .getType() != Token.EOF; token = lexer.nextToken()) {
            final String cssClass = cssClass(token.getType());
            if (cssClass == null || token.getStartIndex() < 0) {
                continue;
            }
            final int startCodePoint = Math.min(token.getStartIndex(),
                    utf16OffsetsByCodePoint.length - 1);
            final int endCodePoint = Math.min(token.getStopIndex() + 1,
                    utf16OffsetsByCodePoint.length - 1);
            ranges.add(new StyledRange(utf16OffsetsByCodePoint[startCodePoint],
                    utf16OffsetsByCodePoint[endCodePoint], cssClass));
        }
        return List.copyOf(ranges);
    }

    /** ANTLR indexes code points; trace ranges and String.substring use UTF-16 offsets. */
    private static int[] utf16OffsetsByCodePoint(final String source) {
        final int[] offsets = new int[source.codePointCount(0, source.length())
                + 1];
        int characterOffset = 0;
        for (int index = 0; index < offsets.length - 1; index++) {
            offsets[index] = characterOffset;
            characterOffset += Character
                    .charCount(source.codePointAt(characterOffset));
        }
        offsets[offsets.length - 1] = source.length();
        return offsets;
    }

    /** Maps a lexer token type to its syntax class, or {@code null} when the token is left unstyled. */
    private static String cssClass(final int tokenType) {
        String cssClass;
        if (LITERAL_TOKEN_TYPES.contains(tokenType)) {
            cssClass = "syntax-literal";
        } else if (IDENTIFIER_TOKEN_TYPES.contains(tokenType)) {
            cssClass = "syntax-identifier";
        } else if (tokenType == SysMLLexer.STRING_VALUE) {
            cssClass = "syntax-string";
        } else if (COMMENT_TOKEN_TYPES.contains(tokenType)) {
            cssClass = "syntax-comment";
        } else if (tokenType != SysMLLexer.WS) {
            cssClass = literalCssClass(tokenType);
        } else {
            cssClass = null;
        }
        return cssClass;
    }

    /**
     * Classifies fixed-spelling tokens from their grammar literal: word literals ({@code true}, ...) as literals,
     * alphabetic ones as keywords, delimiters as punctuation, and any other symbol as an operator.
     */
    private static String literalCssClass(final int tokenType) {
        final String literalName = SysMLLexer.VOCABULARY
                .getLiteralName(tokenType);
        if (literalName == null || literalName.length() < 2) {
            return null;
        }
        final String literal = literalName.substring(1,
                literalName.length() - 1);
        final String cssClass;
        if (WORD_LITERALS.contains(literal)) {
            cssClass = "syntax-literal";
        } else if (Character.isLetter(literal.codePointAt(0))) {
            cssClass = "syntax-keyword";
        } else if (PUNCTUATION.contains(literal)) {
            cssClass = "syntax-punctuation";
        } else {
            cssClass = "syntax-operator";
        }
        return cssClass;
    }

    private static String escapeText(final String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">",
                "&gt;");
    }

    private static String escapeAttribute(final String text) {
        return escapeText(text).replace("\"", "&quot;").replace("'", "&#39;");
    }

    /** UTF-16 offsets of a source line, excluding its line separator. */
    private record SourceLine(int startOffset, int endOffset) {
    }

    /** Half-open UTF-16 token range and its syntax-highlighting class. */
    private record StyledRange(int startOffset, int endOffset,
            String cssClass) {
    }

    /** Data needed to render one source line. */
    private record LineRendering(List<StyledRange> styles, int firstStyleIndex,
            List<ElementRange> markers, List<ReferenceRange> references,
            HtmlReferenceLinkResolver linkResolver,
            AliasAnchors aliasAnchors) {
    }

    /** Opening and closing tag used for one rendered reference. */
    private record ReferenceTag(String name, String opening) {
    }

    /** Anchors for omitted elements, attached to their nearest rendered ancestor or the document start. */
    private record AliasAnchors(Map<Element, List<Element>> byRenderedAncestor,
            List<Element> documentAnchors) {

        /**
         * Collects an anchor for every element that has no rendered range (for example an element merged into its
         * owner's syntax), so links to it still land on the nearest rendered ancestor. Elements under implied
         * relationships are skipped because no link targets them.
         */
        static AliasAnchors create(final List<ElementRange> ranges,
                final boolean navigationEnabled) {
            if (!navigationEnabled || ranges.isEmpty()) {
                return new AliasAnchors(Map.of(), List.of());
            }
            final Set<Element> rendered = Collections.newSetFromMap(
                    new IdentityHashMap<>());
            ranges.forEach(range -> rendered.add(range.element()));
            final Map<Element, List<Element>> aliases =
                    new IdentityHashMap<>();
            final List<Element> documentAnchors = new ArrayList<>();
            for (final EObject root : containmentRoots(ranges)) {
                addAlias(root, rendered, aliases, documentAnchors);
                final var iterator = root.eAllContents();
                while (iterator.hasNext()) {
                    addAlias(iterator.next(), rendered, aliases,
                            documentAnchors);
                }
            }
            return new AliasAnchors(aliases, List.copyOf(documentAnchors));
        }

        private static void addAlias(final EObject object,
                final Set<Element> rendered,
                final Map<Element, List<Element>> aliases,
                final List<Element> documentAnchors) {
            if (!(object instanceof final Element element)
                    || rendered.contains(element)
                    || isOwnedByImpliedRelationship(element)) {
                return;
            }
            EObject container = element.eContainer();
            while (container != null && !rendered.contains(container)) {
                container = container.eContainer();
            }
            if (container instanceof final Element renderedContainer) {
                aliases.computeIfAbsent(renderedContainer,
                        ignored -> new ArrayList<>()).add(element);
            } else {
                documentAnchors.add(element);
            }
        }

        private static Set<EObject> containmentRoots(
                final List<ElementRange> ranges) {
            final Set<EObject> roots = Collections.newSetFromMap(
                    new IdentityHashMap<>());
            for (final ElementRange range : ranges) {
                EObject root = range.element();
                while (root.eContainer() != null) {
                    root = root.eContainer();
                }
                roots.add(root);
            }
            return roots;
        }

        List<Element> forElement(final Element element) {
            return this.byRenderedAncestor.getOrDefault(element, List.of());
        }
    }
}
