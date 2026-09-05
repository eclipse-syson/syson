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
package org.eclipse.syson.sysml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.eclipse.syson.sysml.metamodel.services.textual.utils.Severity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test of the SysmlToAst class.
 *
 * @author wrumph
 */
public class SysmlToAstTest {

    /**
     * A document whose third line is invalid. Everything located after that line, such as the "B" definition, cannot be
     * parsed.
     */
    private static final String INVALID_CONTENT = """
            package Bad {
              part def A;
              satisfy requirement Requirements::X by Y;
              part def B;
            }
            """;

    private static final String VALID_CONTENT = """
            package Good {
              part def A;
              part def B;
            }
            """;

    /**
     * A valid document using a construct that the parser does not support, which makes it report a syntax error while
     * still parsing the whole file. Such a document must still be imported.
     */
    private static final String RECOVERED_CONTENT = """
            package Recovered {
              part def A;
              constant attribute ro;
              part def B;
            }
            """;

    @BeforeAll
    public static void beforeAll() {
        assumeTrue(isNodeAvailable(), "node is required to run the SysML parser");
    }

    private static boolean isNodeAvailable() {
        boolean available;
        try {
            available = new ProcessBuilder("node", "--version").start().waitFor() == 0;
        } catch (IOException e) {
            available = false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            available = false;
        }
        return available;
    }

    @DisplayName("Given a valid document, when it is converted, then its AST is returned without any error")
    @Test
    void convertValidDocument() {
        AstParsingResult result = this.convert(VALID_CONTENT);

        assertThat(result.ast()).isPresent();
        assertThat(result.reports()).isEmpty();
    }

    @DisplayName("Given a document containing a syntax error, when it is converted, then no AST is returned and the error is reported")
    @Test
    void convertDocumentWithSyntaxError() {
        AstParsingResult result = this.convert(INVALID_CONTENT);

        assertThat(result.ast()).isEmpty();
        assertThat(result.reports()).isNotEmpty();
        assertThat(result.reports()).allMatch(status -> status.severity() == Severity.ERROR);
        assertThat(result.reports()).anyMatch(status -> status.message().contains("contains syntax errors"));
        assertThat(result.reports()).anyMatch(status -> status.message().startsWith("line 3:"));
    }

    @DisplayName("Given a document the parser fully parses despite a syntax error, when it is converted, then its AST is returned")
    @Test
    void convertDocumentWithRecoveredSyntaxError() {
        AstParsingResult result = this.convert(RECOVERED_CONTENT);

        assertThat(result.ast()).isPresent();
        assertThat(result.reports()).isEmpty();
    }

    private AstParsingResult convert(String content) {
        InputStream input = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        return new SysmlToAst(null).convert(input, "sysml");
    }
}
