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
import java.util.regex.Pattern;
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

    private static final int MAX_VALIDATION_REPORT_BYTES = 64 * 1024;

    private static final String VALIDATION_ERRORS_HEADER = "There are validation errors:";

    private static final Pattern SYNTAX_DIAGNOSTIC = Pattern.compile("line \\d+: (Expecting|unexpected character: ->)");

    private static final String PARTIAL_IMPORT_WARNING = "The file contains a syntax error. The elements that follow it may be missing from the imported model.";

    private static final String VALIDATION_UNAVAILABLE_WARNING = "The file could not be checked for syntax errors. The imported model may be incomplete.";

    private final Logger logger = LoggerFactory.getLogger(SysmlToAst.class);

    private final String cliPath;

    public SysmlToAst(@Value("${org.eclipse.syson.syside.path:#{null}}") final String cliPath) {
        this.cliPath = cliPath;
    }

    public AstParsingResult convert(final InputStream input, final String fileExtension) {
        return this.convert(input, fileExtension, false);
    }

    /**
     * Converts the document, optionally collecting validation diagnostics for the upload report.
     * Validation does not decide whether the AST is imported.
     *
     * @param input
     *            the document to parse
     * @param fileExtension
     *            the document extension
     * @param includeValidationReport
     *            whether to collect the CLI validation report
     * @return the AST and any reported diagnostics
     */
    public AstParsingResult convert(final InputStream input, final String fileExtension, final boolean includeValidationReport) {
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
                    astInputStream = Optional.of(new ByteArrayInputStream(stdout.getBytes()));
                    if (includeValidationReport) {
                        this.collectValidationReport(sysIdeInputPath, sysmlInputPath, reports);
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
     * Runs the parser a second time to find out whether it dropped part of the file.
     * <p>
     * The parser reports lexer and parser diagnostics before linking and semantic ones, so only a syntax error can
     * come first, and only a syntax error makes the parser stop before the end of the file. The report is
     * human-readable text quoting the source, so only that first diagnostic is interpreted; the rest is repeated
     * verbatim so the author can locate the problem. Later diagnostics are deliberately left alone, because the
     * command line parser runs without the standard library and reports the names a valid model borrows from it as
     * unresolved.
     * </p>
     */
    private void collectValidationReport(Path cli, Path input, List<Status> reports) {
        Path reportFile = null;
        Process process = null;
        try {
            reportFile = Files.createTempFile("syside-validation", ".txt");
            ProcessBuilder builder = new ProcessBuilder("node", cli.toString(), "dump", "--validate", "--stdlib", "none", input.toString());
            builder.environment().put("FORCE_COLOR", "0");
            // Native redirection avoids pipe deadlocks without introducing another asynchronous reader.
            process = builder.redirectOutput(ProcessBuilder.Redirect.DISCARD).redirectError(reportFile.toFile()).start();
            if (process.waitFor(60, TimeUnit.SECONDS)) {
                this.reportSyntaxError(this.readValidationReport(reportFile), process.exitValue(), reports);
            } else {
                reports.add(new Status(Severity.WARNING, VALIDATION_UNAVAILABLE_WARNING));
            }
        } catch (IOException e) {
            this.logger.warn("Could not collect the SysML validation report.", e);
            reports.add(new Status(Severity.WARNING, VALIDATION_UNAVAILABLE_WARNING));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            reports.add(new Status(Severity.WARNING, VALIDATION_UNAVAILABLE_WARNING));
        } finally {
            if (process != null && process.isAlive()) {
                process.destroyForcibly();
            }
            if (reportFile != null) {
                reportFile.toFile().delete();
            }
        }
    }

    private String readValidationReport(Path reportFile) throws IOException {
        try (InputStream stream = Files.newInputStream(reportFile)) {
            String report = new String(stream.readNBytes(MAX_VALIDATION_REPORT_BYTES), StandardCharsets.UTF_8);
            if (stream.read() != -1) {
                report = report + System.lineSeparator() + "[Report shortened]";
            }
            return report;
        }
    }

    private void reportSyntaxError(String report, int exitCode, List<Status> reports) {
        String firstDiagnostic = this.firstDiagnostic(report);
        if (firstDiagnostic == null) {
            if (exitCode != 0) {
                reports.add(new Status(Severity.WARNING, VALIDATION_UNAVAILABLE_WARNING));
            }
        } else if (SYNTAX_DIAGNOSTIC.matcher(firstDiagnostic).lookingAt()) {
            // The report quotes the source, so it is appended as is instead of being formatted.
            reports.add(new Status(Severity.WARNING, PARTIAL_IMPORT_WARNING + System.lineSeparator() + report));
        }
    }

    private String firstDiagnostic(String report) {
        String[] lines = report.split("\\R");
        for (int i = 0; i < lines.length - 1; i++) {
            if (VALIDATION_ERRORS_HEADER.equals(lines[i].strip())) {
                return lines[i + 1];
            }
        }
        return null;
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
