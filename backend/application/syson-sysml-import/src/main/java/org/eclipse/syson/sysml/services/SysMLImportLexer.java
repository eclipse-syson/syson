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
import org.antlr.v4.runtime.Token;
import org.eclipse.syson.sysml.parser.internal.SysMLLexer;

/**
 * Supplies default public visibility for imported text while preserving the grammar and original source positions.
 *
 * @author arichard
 */
final class SysMLImportLexer extends SysMLLexer {

    private static final int PUBLIC_TOKEN_TYPE = new SysMLLexer(CharStreams.fromString("public")).nextToken().getType();

    private String previousToken = "";

    private Token pendingImport;

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
        if (this.pendingImport != null) {
            token = this.pendingImport;
            this.pendingImport = null;
        } else {
            token = super.nextToken();
            if ("import".equals(token.getText()) && !Set.of("public", "protected", "private").contains(this.previousToken)) {
                this.pendingImport = token;
                var visibility = new CommonToken(token);
                visibility.setType(PUBLIC_TOKEN_TYPE);
                visibility.setText("public");
                visibility.setStopIndex(token.getStartIndex() - 1);
                token = visibility;
            }
        }
        if (token.getChannel() == Token.DEFAULT_CHANNEL && token.getType() != REGULAR_COMMENT && token.getType() != ML_NOTE && token.getType() != SL_NOTE) {
            this.previousToken = token.getText();
        }
        return token;
    }
}
