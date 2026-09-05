/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.eclipse.syson.sysml.metamodel.services.textual.utils.Severity;
import org.eclipse.syson.sysml.metamodel.services.textual.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * Converts SysML models to AST representations utilizing an external CLI tool.
 *
 * @author gescande.
 */
@Component
public class SysmlToAst {

    /**
     * Diagnostic emitted by the parser when it stopped before the end of the file, which is the only case where the AST
     * printed by the "dump" command is incomplete.
     */
    private static final String INCOMPLETE_PARSE_DIAGNOSTIC = "Expecting end of file";

    /**
     * Prefixes of the parser diagnostics reported to the user when the document could not be parsed entirely.
     */
    private static final List<String> SYNTAX_ERROR_DIAGNOSTICS = List.of(INCOMPLETE_PARSE_DIAGNOSTIC, "Expecting token of type", "Expecting: one of these possible Token sequences");

    private static final int VALIDATION_TIMEOUT_IN_SECONDS = 60;

    private final Logger logger = LoggerFactory.getLogger(SysmlToAst.class);

    private final String cliPath;

    public SysmlToAst(@Value("${org.eclipse.syson.syside.path:#{null}}") final String cliPath) {
        this.cliPath = cliPath;
    }

    public AstParsingResult convert(final InputStream input, final String fileExtension) {
        Path sysmlInputPath = null;
        Path sysIdeInputPath = null;
        Optional<InputStream> astInputStream = Optional.empty();
        List<Status> reports = new ArrayList<>();

        try {
            sysmlInputPath = this.createTempFile(input, "syson", fileExtension);


            if (this.cliPath != null) {
                sysIdeInputPath = Path.of(this.cliPath);
            } else {
                final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                final Resource resource = resolver.getResource(ResourcePatternResolver.CLASSPATH_URL_PREFIX + "syside-cli.js");
                final InputStream sysIdeInputStream = resource.getInputStream();
                sysIdeInputPath = this.createTempFile(sysIdeInputStream, "syside-cli", "js");
            }

            this.logger.info("Call syside application : node " + sysIdeInputPath + " dump " + sysmlInputPath);
            final String[] args = { "node", sysIdeInputPath.toString(), "dump", sysmlInputPath.toString() };
            ProcessBuilder pb = new ProcessBuilder(args);
            final Process sysIdeProcess = pb.start();


            CompletableFuture<String> stdoutFuture = this.readStdOut(sysIdeProcess, reports);
            CompletableFuture<String> stderrFuture = this.readStdErr(sysIdeProcess, reports);

            this.handleStdError(stderrFuture.join(), reports);
            boolean finished = sysIdeProcess.waitFor(60, TimeUnit.SECONDS);

            if (finished) {
                String stdout = stdoutFuture.join();
                int exitCode = sysIdeProcess.exitValue();
                if (exitCode == 0) {
                    final List<String> syntaxErrors = this.findSyntaxErrors(sysIdeInputPath, sysmlInputPath);
                    if (syntaxErrors.isEmpty()) {
                        astInputStream = Optional.of(new ByteArrayInputStream(stdout.getBytes()));
                    } else {
                        // The parser stopped before the end of the file: the AST it produced on its standard output only
                        // covers the beginning of the document. Importing it would silently drop everything located after
                        // the first syntax error, so the import is reported as failed instead.
                        reports.add(new Status(Severity.ERROR,
                                "The SysML file contains syntax errors and cannot be imported. Only the part of the file located before the first error could be parsed."));
                        syntaxErrors.forEach(syntaxError -> reports.add(new Status(Severity.ERROR, syntaxError)));
                    }
                } else {
                    this.logger.error("The process that parse the SysML file ended with an error core : {}", exitCode);
                }
            } else {
                reports.add(new Status(Severity.ERROR, "Process timed out : The upload process was canceled."));
                sysIdeProcess.destroyForcibly();
            }

        } catch (final IOException | InterruptedException e) {
            this.logger.error(e.getMessage());
        } finally {
            if (sysmlInputPath != null) {
                sysmlInputPath.toFile().delete();
            }
            if (this.cliPath == null && sysIdeInputPath != null) {
                sysIdeInputPath.toFile().delete();
            }
        }
        return new AstParsingResult(astInputStream, reports);


    }

    /**
     * Detects the syntax errors that made the parser stop before the end of the file.
     *
     * <p>
     * The {@code dump} command used to produce the AST always exits with the code 0 and prints nothing on its standard
     * error, even when the file it parsed is invalid: in that case it silently prints a partial AST covering only the
     * beginning of the file. The syntax errors are only reported by the {@code --validate} option, which cannot be used
     * for the main call since it makes the CLI print no AST at all as soon as it finds any validation error.
     * </p>
     *
     * <p>
     * The validation pass is thus run separately, and only the diagnostics reporting that the parser did not reach the
     * end of the file are taken into account. The other diagnostics are ignored on purpose:
     * </p>
     * <ul>
     * <li>the standard library resolution errors ("Could not find implicit specialization...") are expected here, since
     * the CLI is called standalone and SysON resolves the standard libraries itself on the Java side;</li>
     * <li>the semantic checks ("A Feature must be typed by at least one type"...) are reported for many valid documents
     * and do not cause any loss of information during the import.</li>
     * </ul>
     *
     * @param sysIdeInputPath the path of the CLI
     * @param sysmlInputPath the path of the file to parse
     * @return the syntax errors that truncated the parsed document, or an empty list if the whole file was parsed
     */
    private List<String> findSyntaxErrors(final Path sysIdeInputPath, final Path sysmlInputPath) {
        final List<String> syntaxErrors = new ArrayList<>();
        try {
            final String[] args = { "node", sysIdeInputPath.toString(), "dump", "--validate", "--stdlib", "none", sysmlInputPath.toString() };
            final Process validationProcess = new ProcessBuilder(args).start();
            final CompletableFuture<String> validationStdErr = CompletableFuture.supplyAsync(() -> {
                try {
                    return this.readStream(validationProcess.getErrorStream());
                } catch (IOException e) {
                    this.logger.error("Error while reading the validation report : " + e.getMessage(), e);
                    return "";
                }
            });
            final String stderr = validationStdErr.join();
            if (!validationProcess.waitFor(VALIDATION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)) {
                validationProcess.destroyForcibly();
            } else if (stderr.contains(INCOMPLETE_PARSE_DIAGNOSTIC)) {
                stderr.lines()
                        .map(String::trim)
                        .filter(line -> line.startsWith("line "))
                        .filter(line -> SYNTAX_ERROR_DIAGNOSTICS.stream().anyMatch(line::contains))
                        .forEach(syntaxErrors::add);
            }
        } catch (final IOException e) {
            // The document has already been parsed successfully at this point: a failure of the validation pass must not
            // prevent the import, it only means that no syntax error can be reported.
            this.logger.error("Error while looking for syntax errors : " + e.getMessage(), e);
        } catch (final InterruptedException e) {
            this.logger.error("Error while looking for syntax errors : " + e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return syntaxErrors;
    }

    private CompletableFuture<String> readStdErr(Process sysIdeProcess, List<Status> messages) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return this.readStream(sysIdeProcess.getErrorStream());
                    } catch (IOException e) {
                        this.logger.error("Error while reading AST : " + e.getMessage(), e);
                        messages.add(new Status(Severity.ERROR, "Error while building AST on stdErr."));
                        return "";
                    }
                });
    }

    private CompletableFuture<String> readStdOut(Process sysIdeProcess, List<Status> messages) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return this.readAstStream(sysIdeProcess.getInputStream());
                    } catch (IOException e) {
                        this.logger.error("Error while reading AST : " + e.getMessage(), e);
                        messages.add(new Status(Severity.ERROR, "Error while building AST on stdOut."));
                        return "";
                    }
                });
    }

    private void handleStdError(String stderr, List<Status> reports) {
        if (stderr != null && !stderr.isBlank()) {
            this.logger.error("AST parsing errors :" + System.lineSeparator() + stderr);

            if (stderr.contains("JSON.stringify")) {
                // This case occurs when the provided JSon file is too big
                reports.add(new Status(Severity.ERROR, "Error: File size exceeds limit : The selected SysML file is too large to be processed by SysON." +
                        " Please optimize the model or split it into smaller sub-packages and try again."));
            } else {
                reports.add(new Status(Severity.ERROR, "An unhandled exception has occurred during file parsing. Contact your administrator."));
            }
        }
    }

    private String readStream(InputStream stream) throws IOException {
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            return reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        }
    }

    private String readAstStream(InputStream stream) throws IOException {
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            if (line != null) {
                while (line != null && !line.contains("{")) {
                    line = reader.readLine();
                }
                if (line != null) {
                    builder.append(line);
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                }
            }
            return builder.toString();
        }
    }

    private Path createTempFile(final InputStream input, final String fileName, final String fileExtension) throws IOException {
        final Path inputPath = Files.createTempFile(fileName, "." + fileExtension);
        final OutputStream outStream = new FileOutputStream(inputPath.toString());
        final byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        input.close();
        outStream.close();
        return inputPath;
    }
}
