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
package org.eclipse.syson.sysml.services;

import java.util.Set;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.Token;
import org.eclipse.syson.sysml.parser.internal.SysMLLexer;

/**
 * Supplies default public visibility for imported text while preserving the grammar and original source positions.
 *
 * @author arichard
 */
final class SysMLImportLexer extends SysMLLexer {

    /**
     * The {@link Set} of visibilities ({@code 'public'}, {@code 'protected'}, {@code 'private'}).
     */
    private static final Set<String> VISIBILITIES = Set.of("public", "protected", "private");

    /**
     * The {@link Set} of structure ends ({@code ';'}, {@code '}'}).
     */
    private static final Set<String> STRUCTURE_ENDS = Set.of(";", "}");

    /**
     * The token type for {@code 'public'}.
     */
    private static final int PUBLIC_TOKEN_TYPE = new SysMLLexer(CharStreams.fromString("public")).nextToken().getType();

    /**
     * The token type for {@code ';'}.
     */
    private static final int SEMICOLON_TOKEN_TYPE = new SysMLLexer(CharStreams.fromString(";")).nextToken().getType();

    /**
     * The previous {@link Token}.
     */
    private Token previousToken;

    /**
     * The {@link Token} that need to be inserted before the super {@link nextToken()}.
     */
    private Token pendingToken;

    /**
     * Creates the token adapter for an uploaded document or editor fragment.
     *
     * @param input the original source
     */
    SysMLImportLexer(CharStream input) {
        super(input);
    }

    /**
     * Inserts a zero-width public token before bare imports, leaving explicit visibility unchanged.
     *
     * @return the next token at its original source location
     */
    @Override
    public Token nextToken() {
        Token token;

        if (this.pendingToken != null) {
            token = this.pendingToken;
            this.pendingToken = null;
        } else if (this.previousToken != null && this._input.LA(1) == IntStream.EOF && !STRUCTURE_ENDS.contains(this.previousToken.getText())) {
            // missing ';' or '}' at the end of the stream
            // insert ';'
            var semicolon = this.copyToken(this.previousToken, ";", SEMICOLON_TOKEN_TYPE);
            token = semicolon;
        } else {
            token = super.nextToken();
            if ("import".equals(token.getText()) && (this.previousToken == null || !VISIBILITIES.contains(this.previousToken.getText()))) {
                // import with missing visibility
                this.pendingToken = token;
                var visibility = this.copyToken(token, "public", PUBLIC_TOKEN_TYPE);
                token = visibility;
            }
        }
        if (token.getChannel() == Token.DEFAULT_CHANNEL && token.getType() != REGULAR_COMMENT && token.getType() != ML_NOTE && token.getType() != SL_NOTE) {
            this.previousToken = token;
        }

        return token;
    }

    /**
     * Creates a copy of the given {@link Token} with the given text and type.
     *
     * @param oldToken
     *            the old {@link Token} to copy
     * @param text
     *            the text
     * @param type
     *            the type
     * @return a copy of the given {@link Token} with the given text and type
     */
    private CommonToken copyToken(Token oldToken, String text, int type) {
        var visibility = new CommonToken(oldToken);
        visibility.setText(text);
        visibility.setType(type);
        visibility.setStopIndex(oldToken.getStartIndex() - 1);
        return visibility;
    }
}
