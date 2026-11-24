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
package org.eclipse.syson.sysml.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Generates a standalone HTML view next to every serializer SysML fixture.
 *
 * <p>
 * This class deliberately does not match Surefire's default test naming patterns. Run its {@link #main(String[])}
 * method from an IDE, or target the class explicitly with Maven.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SerializerHtmlFixtureGenerator {

    /** This manual generator reports what it wrote on the console, which is its only user interface. */
    private static final PrintStream CONSOLE_REPORT = System.out;

    private static final String RESOURCE_ROOT_PROPERTY = "serializer.html.resourceRoot";

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * Generates a sibling {@code .sysml.html} file for each fixture below a resource root, overwriting existing HTML.
     * Reports the number of generated files and collects fixture failures before reporting them as one exception.
     *
     * @param args
     *            zero or one resource-root argument; when absent or blank, {@code serializer.html.resourceRoot} is
     *            consulted before the usual module-relative test resource directories
     * @throws IOException
     *             if the resource tree cannot be opened for traversal
     * @throws IllegalArgumentException
     *             if more than one argument is supplied or the chosen root is not a directory
     * @throws IllegalStateException
     *             if no resource root can be located or any fixture fails parsing, serialization or writing
     */
    public static void main(final String[] args) throws IOException {
        generateHtmlFixtures(resolveResourceRoot(args));
    }

    // Batch generation must report every failing fixture, including failures from
    // parser or serializer internals, instead of stopping at the first one.
    @SuppressWarnings("checkstyle:IllegalCatch")
    private static void generateHtmlFixtures(final Path resourceRoot) throws IOException {
        List<Path> sources;
        try (Stream<Path> paths = Files.walk(resourceRoot)) {
            sources = paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".sysml"))
                    .sorted(Comparator.comparing(Path::toString)).toList();
        }
        final SysMLModelParser parser = new SysMLModelParser();
        final SysMLSerializer serializer = new SysMLSerializer();
        final List<GenerationFailure> failures = new ArrayList<>();
        int generated = 0;
        for (final Path source : sources) {
            try {
                writeHtmlFixture(source, parser, serializer);
                generated++;
            } catch (RuntimeException | IOException exception) {
                failures.add(
                        new GenerationFailure(source, exception.getMessage()));
            }
        }
        CONSOLE_REPORT.println("Generated " + generated + " HTML file(s) from "
                + sources.size() + " SysML fixture(s) in " + resourceRoot
                + ".");
        if (!failures.isEmpty()) {
            final String details = failures.stream()
                    .map(GenerationFailure::describe)
                    .collect(Collectors.joining(System.lineSeparator()));
            throw new IllegalStateException("Failed to generate "
                    + failures.size() + " HTML fixture(s):"
                    + System.lineSeparator() + details);
        }
    }

    private static void writeHtmlFixture(final Path source,
            final SysMLModelParser parser, final SysMLSerializer serializer)
            throws IOException {
        ISysMLParseResult<? extends Element> parseResult;
        try (InputStream input = Files.newInputStream(source)) {
            parseResult = parser.parseRootNamespace(input,
                    StandardCharsets.UTF_8);
        }
        if (parseResult.model() == null
                || parseResult.diagnostic().getSeverity() == Diagnostic.ERROR) {
            throw new IllegalStateException(
                    "Parser rejected the fixture: " + parseResult.diagnostic());
        }
        final String html = serializer.serializeToHtml(parseResult);
        if (html.isBlank()) {
            throw new IllegalStateException(
                    "Serializer produced an empty HTML document");
        }
        final Path htmlPath = source
                .resolveSibling(source.getFileName() + ".html");
        Files.writeString(htmlPath, html, StandardCharsets.UTF_8);
    }

    private static Path resolveResourceRoot(final String[] args) {
        if (args.length > 1) {
            throw new IllegalArgumentException(
                    "Expected at most one resource-root argument");
        }
        Path resourceRoot = null;
        if (args.length == 1 && !args[0].isBlank()) {
            resourceRoot = requireResourceRoot(Path.of(args[0]));
        } else {
            final String configuredRoot = System
                    .getProperty(RESOURCE_ROOT_PROPERTY, "");
            if (!configuredRoot.isBlank()) {
                resourceRoot = requireResourceRoot(Path.of(configuredRoot));
            } else {
                for (final Path candidate : List.of(
                        Path.of("src/test/resources"), Path.of(
                                "syson-sysml-serializer/src/test/resources"))) {
                    if (Files.isDirectory(candidate)) {
                        resourceRoot = candidate.toAbsolutePath().normalize();
                        break;
                    }
                }
            }
        }
        if (resourceRoot == null) {
            throw new IllegalStateException(
                    "Unable to locate syson-sysml-serializer/src/test/resources");
        }
        return resourceRoot;
    }

    private static Path requireResourceRoot(final Path path) {
        final Path normalized = path.toAbsolutePath().normalize();
        if (!Files.isDirectory(normalized)) {
            throw new IllegalArgumentException(
                    "The resource root does not exist: " + normalized);
        }
        return normalized;
    }

    /** Describes one fixture whose HTML generation failed. */
    private record GenerationFailure(Path source, String message) {

        String describe() {
            String failureMessage = this.message;
            if (failureMessage == null) {
                failureMessage = "unknown failure";
            }
            return this.source + ": " + failureMessage;
        }
    }
}
