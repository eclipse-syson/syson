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

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;

/**
 * String-backed {@link TextOutput}.
 *
 * <p>
 * This class is the default output implementation for tests and simple facade calls. It keeps spacing deterministic by
 * delaying single spaces until the next inline token.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class StringTextOutput implements TextOutput {

    private final StringBuilder buffer = new StringBuilder();

    private final FormattingOptions formatting;

    private final ElementTrace trace;

    private final ReferenceSink referenceSink;

    private boolean lineStart = true;

    private boolean pendingTokenSeparator;

    private int indentationLevel;

    /**
     * Creates a plain-text output that records neither element nor reference ranges.
     *
     * @param formatting
     *            the line, indentation and token separators to use
     */
    public StringTextOutput(final FormattingOptions formatting) {
        this(formatting, ElementTrace.NONE, ReferenceSink.NONE);
    }

    StringTextOutput(final FormattingOptions formatting,
            final ElementTrace trace, final ReferenceSink referenceSink) {
        this.formatting = Objects.requireNonNull(formatting);
        this.trace = Objects.requireNonNull(trace);
        this.referenceSink = Objects.requireNonNull(referenceSink);
    }

    @Override
    public TextOutput beginElement(final Element element) {
        this.trace.begin(element, this.buffer.length());
        return this;
    }

    @Override
    public TextOutput endElement(final Element element) {
        this.trace.end(element, this.buffer.length());
        return this;
    }

    @Override
    public TextOutput token(final String token) {
        if (token == null || token.isEmpty()) {
            return this;
        }
        this.writePendingSpacing();
        this.buffer.append(token);
        return this;
    }

    @Override
    public TextOutput referenceName(final String name, final EObject target) {
        if (name == null || name.isEmpty()) {
            return this;
        }
        this.writePendingSpacing();
        final int startOffset = this.buffer.length();
        this.buffer.append(name);
        this.referenceSink.reference(target, startOffset, this.buffer.length());
        return this;
    }

    /** Plain-text output never evaluates the reference metadata supplier. */
    @Override
    public TextOutput referenceText(final String text,
            final Supplier<List<Reference>> references) {
        return this.token(text);
    }

    @Override
    public TextOutput space() {
        if (!this.lineStart) {
            this.pendingTokenSeparator = true;
        }
        return this;
    }

    @Override
    public TextOutput newline() {
        this.pendingTokenSeparator = false;
        this.buffer.append(this.formatting.lineSeparator());
        this.lineStart = true;
        return this;
    }

    @Override
    public TextOutput beginBlock() {
        this.token(SysMLTokens.OPEN_BRACE);
        this.newline();
        this.indentationLevel++;
        return this;
    }

    @Override
    public TextOutput endBlock() {
        if (!this.lineStart) {
            this.newline();
        }
        this.indentationLevel = Math.max(0, this.indentationLevel - 1);
        this.token(SysMLTokens.CLOSE_BRACE);
        return this;
    }

    @Override
    public TextOutput raw(final String text) {
        if (text == null || text.isEmpty()) {
            return this;
        }
        this.writePendingSpacing();
        this.buffer.append(text);
        this.lineStart = text.endsWith(this.formatting.lineSeparator());
        this.pendingTokenSeparator = false;
        return this;
    }

    @Override
    public int mark() {
        return this.buffer.length();
    }

    @Override
    public boolean changedSince(final int mark) {
        return this.buffer.length() > mark;
    }

    @Override
    public TextOutput ensureTrailingNewlineIfChanged(final int mark) {
        if (this.changedSince(mark) && !this.endsWithLineSeparator()) {
            this.newline();
        }
        return this;
    }

    @Override
    public String content() {
        return this.buffer.toString();
    }

    private boolean endsWithLineSeparator() {
        final String lineSeparator = this.formatting.lineSeparator();
        final int start = this.buffer.length() - lineSeparator.length();
        return start >= 0 && this.buffer.indexOf(lineSeparator, start) == start;
    }

    /** Flushes the indentation owed at a line start and the requested token separator before new text. */
    private void writePendingSpacing() {
        if (this.lineStart) {
            this.buffer.append(this.formatting.indentationUnit()
                    .repeat(this.indentationLevel));
            this.lineStart = false;
        }
        if (this.pendingTokenSeparator) {
            this.buffer.append(this.formatting.tokenSeparator());
            this.pendingTokenSeparator = false;
        }
    }
}
