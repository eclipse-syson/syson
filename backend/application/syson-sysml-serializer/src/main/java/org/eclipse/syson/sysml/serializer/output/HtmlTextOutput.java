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
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.serializer.HtmlReferenceLinkResolver;
import org.eclipse.syson.sysml.serializer.tokens.AtomicTokenScanner;
import org.eclipse.syson.sysml.serializer.tokens.ReferenceNameSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;

/**
 * {@link TextOutput} producing a standalone, syntax-highlighted HTML document.
 *
 * <p>
 * Plain SysML emission is delegated to {@link StringTextOutput}, so marks and trace offsets always refer to the
 * unescaped SysML text rather than to the HTML representation.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class HtmlTextOutput implements TextOutput {

    private static final char ASCII_LIMIT = 128;

    private final ElementTrace trace = new ElementTrace();

    private final List<ReferenceRange> referenceRanges = new ArrayList<>();

    private final StringTextOutput textOutput;

    private final HtmlReferenceLinkResolver linkResolver;

    /**
     * Creates an HTML output whose references are highlighted but not linked.
     *
     * @param formatting
     *            the formatting of the underlying SysML text
     */
    public HtmlTextOutput(final FormattingOptions formatting) {
        this(formatting, null);
    }

    /**
     * Creates an HTML output whose reference targets may become hyperlinks.
     *
     * @param formatting
     *            the formatting of the underlying SysML text
     * @param linkResolver
     *            the resolver of reference hyperlinks, or {@code null} for no links
     */
    public HtmlTextOutput(final FormattingOptions formatting, final HtmlReferenceLinkResolver linkResolver) {
        this.linkResolver = linkResolver;
        this.textOutput = new StringTextOutput(formatting, this.trace, (target, startOffset, endOffset) -> this.referenceRanges
                .add(new ReferenceRange(target, startOffset, endOffset)));
    }

    @Override
    public TextOutput beginElement(final Element element) {
        this.textOutput.beginElement(element);
        return this;
    }

    @Override
    public TextOutput endElement(final Element element) {
        this.textOutput.endElement(element);
        return this;
    }

    @Override
    public TextOutput token(final String token) {
        this.textOutput.token(token);
        return this;
    }

    @Override
    public TextOutput referenceName(final String name, final EObject target) {
        this.textOutput.referenceName(name, target);
        return this;
    }

    /**
     * Splits composite text such as an expression into plain tokens and reference names, so each occurrence becomes
     * its own highlighted range. Occurrences are matched in order; one that cannot be found in the remaining text is
     * skipped and stays plain text.
     */
    @Override
    public TextOutput referenceText(final String text, final List<Reference> references) {
        int nextTextOffset = 0;
        for (final Reference reference : references) {
            final ReferenceMatch match = this.findReferenceOrLeafMatch(text, reference.name(), nextTextOffset);
            final int start = match.startOffset();
            if (start >= 0) {
                final int end = start + match.matchedText().length();
                if (start > nextTextOffset) {
                    this.textOutput.token(text.substring(nextTextOffset, start));
                }
                this.textOutput.referenceName(text.substring(start, end), reference.target());
                nextTextOffset = end;
            }
        }
        if (nextTextOffset < text.length()) {
            this.textOutput.token(text.substring(nextTextOffset));
        }
        return this;
    }

    @Override
    public TextOutput space() {
        this.textOutput.space();
        return this;
    }

    @Override
    public TextOutput newline() {
        this.textOutput.newline();
        return this;
    }

    @Override
    public TextOutput beginBlock() {
        this.textOutput.beginBlock();
        return this;
    }

    @Override
    public TextOutput endBlock() {
        this.textOutput.endBlock();
        return this;
    }

    @Override
    public TextOutput raw(final String text) {
        this.textOutput.raw(text);
        return this;
    }

    @Override
    public int mark() {
        return this.textOutput.mark();
    }

    @Override
    public boolean changedSince(final int mark) {
        return this.textOutput.changedSince(mark);
    }

    @Override
    public TextOutput ensureTrailingNewlineIfChanged(final int mark) {
        this.textOutput.ensureTrailingNewlineIfChanged(mark);
        return this;
    }

    @Override
    public String content() {
        return SysMLHtmlDocumentRenderer.render(this.textOutput.content(),
                this.trace.ranges(), this.referenceRanges, this.linkResolver);
    }

    /**
     * Searches forward for the full spelling first, then its leaf. Both are matched on whole tokens (see
     * {@link #tokenIndexOf(String, String, int)}), so a name never binds to string-literal content or to part of a
     * longer name.
     */
    private ReferenceMatch findReferenceOrLeafMatch(final String text, final String name, final int searchOffset) {
        if (name == null || name.isBlank()) {
            return new ReferenceMatch(-1, "");
        }
        final int start = tokenIndexOf(text, name, searchOffset);
        ReferenceMatch match = new ReferenceMatch(start, name);
        if (start < 0) {
            final String leaf = referenceLeaf(name);
            match = new ReferenceMatch(tokenIndexOf(text, leaf, searchOffset), leaf);
        }
        return match;
    }

    /**
     * Returns the first offset, at or after {@code searchOffset}, where {@code name} starts at a lexical token start
     * and does not continue into an adjacent identifier character. String literals ({@code STRING_VALUE}) and
     * unrestricted names ({@code UNRESTRICTED_NAME}) are stepped over as single tokens, so their content is never
     * matched. {@code searchOffset} must be a token boundary.
     *
     * @return the match offset, or {@code -1} when there is none
     */
    private static int tokenIndexOf(final String text, final String name, final int searchOffset) {
        int index = searchOffset;
        while (index < text.length()) {
            if (text.startsWith(name, index) && !continuesIdentifier(text, name, index)) {
                return index;
            }
            index = nextTokenStart(text, index);
        }
        return -1;
    }

    /** Tells whether a match of {@code name} at {@code start} would cut an identifier or number token after it. */
    private static boolean continuesIdentifier(final String text, final String name, final int start) {
        final int end = start + name.length();
        return end < text.length() && isIdentifierPart(name.charAt(name.length() - 1))
                && isIdentifierPart(text.charAt(end));
    }

    /** Skips a whole string literal, unrestricted name, comment or identifier/number token, or a single other character. */
    private static int nextTokenStart(final String text, final int index) {
        final int atomicTokenEnd = AtomicTokenScanner.atomicTokenEnd(text, index);
        if (atomicTokenEnd > index) {
            return atomicTokenEnd;
        }
        int next = index + 1;
        if (isIdentifierPart(text.charAt(index))) {
            while (next < text.length() && isIdentifierPart(text.charAt(next))) {
                next++;
            }
        }
        return next;
    }

    private static boolean isIdentifierPart(final char character) {
        final boolean asciiLetterOrDigit = character < ASCII_LIMIT && Character.isLetterOrDigit(character);
        return asciiLetterOrDigit || character == '_';
    }

    /**
     * Returns the last segment of a qualified ({@code ::}) or feature-chain ({@code .}) name; separators inside a
     * quoted name belong to that name.
     */
    private static String referenceLeaf(final String name) {
        final int namespaceSeparator = ReferenceNameSyntax.lastSeparatorOutsideQuotes(name,
                SysMLTokens.NAMESPACE_SEPARATOR);
        final int pathSeparator = ReferenceNameSyntax.lastSeparatorOutsideQuotes(name, SysMLTokens.PATH_SEPARATOR);
        int leafStart = 0;
        if (namespaceSeparator > pathSeparator) {
            leafStart = namespaceSeparator + SysMLTokens.NAMESPACE_SEPARATOR.length();
        } else if (pathSeparator >= 0) {
            leafStart = pathSeparator + SysMLTokens.PATH_SEPARATOR.length();
        }
        return name.substring(leafStart);
    }

    /** Matched spelling and UTF-16 start offset; a negative start means no match. */
    private record ReferenceMatch(int startOffset, String matchedText) {
    }
}
