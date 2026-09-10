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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.web.application.document.services.LoadingReport;
import org.eclipse.sirius.web.application.document.services.api.ExternalResourceLoadingResult;
import org.eclipse.syson.sysml.metamodel.services.textual.utils.Severity;
import org.eclipse.syson.sysml.upload.SysMLExternalResourceLoaderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests the import of a file whose parsing produced diagnostics, and the report it returns.
 *
 * @author wrumph
 */
public class SysmlToAstTest {

    private static final String VALID_CONTENT = "package Good { part def A; part def B; }";

    private static final String STUB_AST = "{\"$type\":\"Namespace\",\"children\":[]}";

    private static final String PARTIAL_IMPORT_WARNING = "may be missing from the imported model";

    private static final String VALIDATION_ERRORS_HEADER = "There are validation errors:";

    @BeforeAll
    public static void beforeAll() throws IOException, InterruptedException {
        // The embedded parser requires Node, which is provided by CI.
        assumeTrue(new ProcessBuilder("node", "--version").start().waitFor() == 0, "node is required to run the SysML parser");
    }

    @DisplayName("GIVEN a syntax error leaving an unparsed tail, WHEN the file is uploaded, THEN the parsed elements are imported and the report locates the error")
    @Test
    void importPartialDocument() {
        String content = """
                package Bad {
                  part def A;
                  satisfy requirement Requirements::X by Y;
                  part def B;
                }
                """;

        var result = this.upload(content);

        assertThat(this.partDefinitionNames(result)).contains("A").doesNotContain("B");
        assertThat(this.reportOf(result)).anyMatch(message -> message.contains(PARTIAL_IMPORT_WARNING) && message.contains("line 3:"));
    }

    @DisplayName("GIVEN a valid file using the standard library, WHEN it is uploaded, THEN it is imported without a warning about missing elements")
    @Test
    void importDocumentUsingTheStandardLibrary() {
        String content = """
                package Good {
                  part def A {
                    attribute mass : ScalarValues::Real;
                  }
                  part def B;
                }
                """;

        var result = this.upload(content);

        assertThat(this.partDefinitionNames(result)).contains("A", "B");
        // The command line parser has no standard library, so it reports the borrowed names as unresolved.
        // Nothing of that must reach the report: no data was lost.
        assertThat(this.reportOf(result)).noneMatch(message -> message.contains(PARTIAL_IMPORT_WARNING) || message.contains(VALIDATION_ERRORS_HEADER));
    }

    @DisplayName("GIVEN a syntax error the parser recovers from, WHEN the file is converted, THEN the complete AST is kept and a warning is reported")
    @Test
    void retainDocumentWithRecoveredSyntaxError() throws IOException {
        String content = """
                package Recovered {
                  part def A;
                  constant attribute ro;
                  part def B;
                }
                """;

        AstParsingResult result = new SysmlToAst(null).convert(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), "sysml", true);

        assertThat(this.readAst(result)).contains("\"declaredName\": \"B\"");
        assertThat(result.reports()).hasSize(1).allMatch(status -> status.severity() == Severity.WARNING);
    }

    @DisplayName("GIVEN a text editor conversion, WHEN no report is requested, THEN no validation is run and nothing is reported")
    @Test
    void skipValidationForTextEditors(@TempDir Path tempDir) throws IOException {
        AstParsingResult result = this.convertWithStub(tempDir, "process.stderr.write('unexpected validation'); process.exitCode = 2;", false);

        assertThat(this.readAst(result)).isEqualTo(STUB_AST);
        assertThat(result.reports()).isEmpty();
    }

    @DisplayName("GIVEN a large validation report, WHEN the file is converted, THEN the conversion completes and the report is shortened")
    @Test
    @Timeout(15)
    void shortenLargeValidationReport(@TempDir Path tempDir) throws IOException {
        AstParsingResult result = this.convertWithStub(tempDir, """
                process.stdout.write('x'.repeat(128 * 1024));
                process.stderr.write('There are validation errors:\\n');
                process.stderr.write('line 3: Expecting end of file but found ::. [::]\\n');
                process.stderr.write('line 3: A Feature must be typed by at least one type. [x]\\n'.repeat(16 * 1024));
                process.exitCode = 1;
                """, true);

        assertThat(this.readAst(result)).isEqualTo(STUB_AST);
        assertThat(result.reports()).hasSize(1);
        assertThat(result.reports().getFirst().message())
                .contains(PARTIAL_IMPORT_WARNING)
                .endsWith("[Report shortened]")
                .hasSizeLessThan(66 * 1024);
    }

    @DisplayName("GIVEN a validation that fails without reporting anything, WHEN the file is converted, THEN the AST is kept and no element is claimed to be missing")
    @Test
    void reportValidationFailureWithoutClaimingMissingElements(@TempDir Path tempDir) throws IOException {
        AstParsingResult result = this.convertWithStub(tempDir, "process.exitCode = 2;", true);

        assertThat(this.readAst(result)).isEqualTo(STUB_AST);
        assertThat(result.reports()).hasSize(1).allMatch(status -> status.severity() == Severity.WARNING);
        assertThat(result.reports().getFirst().message()).doesNotContain(PARTIAL_IMPORT_WARNING);
    }

    private ExternalResourceLoadingResult upload(String content) {
        var loader = new SysMLExternalResourceLoaderService(new SysmlToAst(null));
        return loader.getResource(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)),
                URI.createURI("test:/model.sysml"), new ResourceSetImpl(), false).orElseThrow();
    }

    private List<String> partDefinitionNames(ExternalResourceLoadingResult result) {
        List<String> names = new ArrayList<>();
        result.resource().getAllContents().forEachRemaining(object -> {
            if (object instanceof PartDefinition definition) {
                names.add(definition.getDeclaredName());
            }
        });
        return names;
    }

    private List<String> reportOf(ExternalResourceLoadingResult result) {
        return ((LoadingReport) result.loadingReport()).content();
    }

    private AstParsingResult convertWithStub(Path directory, String validation, boolean includeValidationReport) throws IOException {
        Path cli = directory.resolve("cli.js");
        Files.writeString(cli, """
                if (process.argv.includes('--validate')) {
                    %s
                } else {
                    process.stdout.write('%s');
                }
                """.formatted(validation, STUB_AST), StandardCharsets.UTF_8);
        var input = new ByteArrayInputStream(VALID_CONTENT.getBytes(StandardCharsets.UTF_8));
        var converter = new SysmlToAst(cli.toString());
        if (includeValidationReport) {
            return converter.convert(input, "sysml", true);
        }
        return converter.convert(input, "sysml");
    }

    private String readAst(AstParsingResult result) throws IOException {
        assertThat(result.ast()).isPresent();
        try (var ast = result.ast().orElseThrow()) {
            return new String(ast.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
