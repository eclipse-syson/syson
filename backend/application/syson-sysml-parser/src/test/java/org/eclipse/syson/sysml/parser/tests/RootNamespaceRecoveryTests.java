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
package org.eclipse.syson.sysml.parser.tests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.parser.internal.SysMLLexer;
import org.junit.runners.Parameterized.Parameters;

/**
 * Exercises root parsing with a supplied lexer that fails after model construction has started.
 *
 * @author arichard
 */
public class RootNamespaceRecoveryTests extends AbstractSysMLRuleTestSuite {

    /**
     * Creates a resource-backed recovery test.
     *
     * @param testFolder the fixture directory
     * @throws Exception if the fixture cannot be parsed
     */
    public RootNamespaceRecoveryTests(String testFolder) throws Exception {
        super(testFolder);
    }

    /**
     * Gets the fixtures for retry after partial model construction.
     *
     * @return the fixture directories
     */
    @Parameters(name = "{0}")
    public static Collection<Object[]> retrieveTestFolders() {
        return retrieveTestFolders("src/test/resources/rootNamespaceRecovery");
    }

    /**
     * Parses through the supplied-lexer entry point, forcing the initial attempt to fail after parsing earlier members.
     *
     * @param parser the parser under test
     * @param input the fixture source
     * @param charset the source character set
     * @return the recovered model, with diagnostics and source adapters
     * @throws IOException if the fixture cannot be read
     */
    @Override
    protected ISysMLParseResult<?> parse(SysMLModelParser parser, InputStream input, Charset charset) throws IOException {
        var lexer = new FailingLexer(CharStreams.fromStream(input, charset));
        var result = parser.parseRootNamespace(lexer);
        assertTrue("The fixture must exercise the initial token adapter failure", lexer.failed);
        return result;
    }

    /**
     * Simulates a token adapter failure while retaining its token for the next parsing attempt.
     */
    private static final class FailingLexer extends SysMLLexer {

        private boolean failed;

        private Token pending;

        /**
         * Creates a lexer that fails at the fixture's retry declaration after earlier members have been parsed.
         *
         * @param input the fixture source
         */
        private FailingLexer(CharStream input) {
            super(input);
        }

        /**
         * Returns the original tokens, failing once before exposing the retry token.
         *
         * @return the next source token
         */
        @Override
        public Token nextToken() {
            Token token = this.pending;
            if (token == null) {
                token = super.nextToken();
            }
            if ("retry".equals(token.getText()) && !this.failed) {
                this.pending = token;
                this.failed = true;
                throw new IllegalStateException("Simulated token adapter failure");
            }
            this.pending = null;
            return token;
        }
    }
}
