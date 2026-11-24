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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.serializer.model.DanglingReferenceIndex;
import org.eclipse.syson.sysml.serializer.model.SerializerModelQueries;
import org.eclipse.syson.sysml.serializer.names.NameRenderer;
import org.eclipse.syson.sysml.serializer.output.StringTextOutput;
import org.eclipse.syson.sysml.serializer.rendering.SysMLTextRenderer;
import org.junit.jupiter.api.Test;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Measures parsing and serialization of every SysML fixture below
 * {@link #PERFORMANCE_DIRECTORY}. Edit that path to select another corpus;
 * it is resolved from the serializer module's project directory.
 *
 * <p>
 * This is deliberately an explicitly selected integration test: its
 * {@code IT} suffix keeps it out of the regular Surefire test patterns. Run it
 * with:
 * </p>
 *
 * <pre>
 * mvn -q
 *     -Dtest=SysMLSerializationPerformanceIT -Dsurefire.failIfNoSpecifiedTests=false test
 * </pre>
 *
 * <p>
 * The figures are diagnostic measurements, not pass/fail thresholds. This is
 * not a replacement for a JMH benchmark when statistically rigorous numbers
 * are required. Fixture bytes are loaded before the timer, so the parsing
 * duration measures the parser rather than filesystem access.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SysMLSerializationPerformanceIT {

    /** The measurement table is the purpose of this manual entry point, so it is printed to the console. */
    private static final PrintStream CONSOLE_REPORT = System.out;

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    private static final String ITERATIONS_PROPERTY =
            "syson.serializer.performance.iterations";

    private static final String WARMUP_ITERATIONS_PROPERTY =
            "syson.serializer.performance.warmups";

    private static final String FIXTURE_PROPERTY =
            "syson.serializer.performance.fixture";

    /** Edit this path to select the performance corpus. */
    private static final Path PERFORMANCE_DIRECTORY = Path.of("src", "test",
            "resources", "testcases", "perfo");

    private static final int DEFAULT_ITERATIONS = 3;

    private static final int DEFAULT_WARMUP_ITERATIONS = 1;

    /**
     * Each selected fixture must parse without errors, and each measured rendering must be nonempty and match the
     * facade output. Parsing and serialization timings must be reported without imposing a duration threshold.
     */
    @Test
    void measurePerformanceFixtures() throws IOException {
        final int iterations = positiveIntegerProperty(ITERATIONS_PROPERTY,
                DEFAULT_ITERATIONS);
        final int warmupIterations = positiveIntegerProperty(
                WARMUP_ITERATIONS_PROPERTY, DEFAULT_WARMUP_ITERATIONS);

        final List<Path> fixtures = performanceFixtures(PERFORMANCE_DIRECTORY,
                System.getProperty(FIXTURE_PROPERTY, ""));
        assertFalse(fixtures.isEmpty(), "No performance fixture found in "
                + PERFORMANCE_DIRECTORY.toAbsolutePath());

        final SysMLModelParser parser = new SysMLModelParser();
        warmUpParser(parser);
        printHeader(iterations, warmupIterations);
        for (final Path fixture : fixtures) {
            measureFixture(parser, fixture, iterations, warmupIterations);
        }
    }

    private static void measureFixture(final SysMLModelParser parser,
            final Path fixture, final int iterations,
            final int warmupIterations) throws IOException {
        final byte[] source = Files.readAllBytes(fixture);
        for (int iteration = 0; iteration < warmupIterations; iteration++) {
            parseFixture(parser, fixture, source);
        }
        final ParseSample firstParse = parseFixture(parser, fixture, source);
        final List<Long> parseDurationsNanos = new ArrayList<>(iterations);
        parseDurationsNanos.add(firstParse.durationNanos());
        for (int iteration = 1; iteration < iterations; iteration++) {
            parseDurationsNanos.add(
                    parseFixture(parser, fixture, source).durationNanos());
        }
        final ISysMLParseResult<? extends Element> parseResult = firstParse
                .result();
        final String expectedText = new SysMLSerializer().serialize(parseResult);

        for (int iteration = 0; iteration < warmupIterations; iteration++) {
            measureSerialization(parseResult, expectedText);
        }
        final List<SerializationSample> samples = new ArrayList<>(iterations);
        for (int iteration = 0; iteration < iterations; iteration++) {
            samples.add(measureSerialization(parseResult, expectedText));
        }

        printResult(fixture, source.length, medianParseDuration(parseDurationsNanos),
                sampleWithMedianTotalDuration(samples));
        CONSOLE_REPORT.printf("  render median: %.1f ms; SHA-256: %s%n",
                milliseconds(samples.stream()
                        .mapToLong(SerializationSample::renderingNanos).sorted()
                        .skip(samples.size() / 2).findFirst().orElseThrow()),
                sha256(expectedText));
    }

    private static ParseSample parseFixture(final SysMLModelParser parser,
            final Path fixture, final byte[] source) throws IOException {
        final long start = System.nanoTime();
        final ISysMLParseResult<? extends Element> result = parser
                .parseRootNamespace(new ByteArrayInputStream(source),
                        StandardCharsets.UTF_8);
        final long duration = System.nanoTime() - start;
        requireSuccessfulParse(fixture, result);
        return new ParseSample(result, duration);
    }

    private static SerializationSample measureSerialization(
            final ISysMLParseResult<? extends Element> parseResult,
            final String expectedText) {
        long start = System.nanoTime();
        final DanglingReferenceIndex danglingReferences = DanglingReferenceIndex
                .from(parseResult.danglingLinks());
        final long indexNanos = System.nanoTime() - start;

        start = System.nanoTime();
        final SerializationOptions options = SerializationOptions.defaults();
        final StringTextOutput output = new StringTextOutput(
                options.formatting());
        final SerializationContext context = new SerializationContext(options,
                output, new NameRenderer(), new SerializerModelQueries(),
                danglingReferences);
        final SysMLTextRenderer renderer = new SysMLTextRenderer(context);
        final long setupNanos = System.nanoTime() - start;

        start = System.nanoTime();
        renderer.serialize(parseResult.model());
        final long renderingNanos = System.nanoTime() - start;

        start = System.nanoTime();
        final String serialized = output.content();
        final long materializationNanos = System.nanoTime() - start;
        assertFalse(serialized.isEmpty(), "Serializer produced empty text");
        assertEquals(expectedText, serialized,
                "Repeated rendering differs from facade output");
        return new SerializationSample(indexNanos, setupNanos, renderingNanos,
                materializationNanos, serialized.length());
    }

    private static String sha256(final String text) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(text.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new AssertionError(exception);
        }
    }

    private static void requireSuccessfulParse(final Path fixture,
            final ISysMLParseResult<? extends Element> result) {
        assertNotNull(result.model(), "Parser produced no model for " + fixture);
        if (result.diagnostic().getSeverity() == Diagnostic.ERROR) {
            throw new IllegalStateException("Parser rejected " + fixture + ": "
                    + result.diagnostic());
        }
    }

    private static List<Path> performanceFixtures(final Path directory,
            final String selectedFixture) throws IOException {
        try (Stream<Path> files = Files.walk(directory)) {
            return files.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString()
                            .endsWith(".sysml"))
                    .filter(path -> selectedFixture.isBlank()
                            || path.getFileName().toString()
                                    .equals(selectedFixture))
                    .sorted(Comparator.comparingLong(
                            SysMLSerializationPerformanceIT::fileSize).thenComparing(
                                    Path::toString))
                    .toList();
        }
    }

    private static long fileSize(final Path path) {
        try {
            return Files.size(path);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Cannot read fixture size for " + path, exception);
        }
    }

    private static void warmUpParser(final SysMLModelParser parser)
            throws IOException {
        final byte[] source = "package PerformanceWarmup {}"
                .getBytes(StandardCharsets.UTF_8);
        parser.parseRootNamespace(new ByteArrayInputStream(source),
                StandardCharsets.UTF_8);
    }

    private static int positiveIntegerProperty(final String property,
            final int defaultValue) {
        final int value = Integer.getInteger(property, defaultValue);
        if (value < 1) {
            throw new IllegalArgumentException(
                    property + " must be greater than zero");
        }
        return value;
    }

    /** Selects one complete timing sample by total duration; even counts use the upper middle sample. */
    private static SerializationSample sampleWithMedianTotalDuration(
            final List<SerializationSample> samples) {
        final List<SerializationSample> sorted = samples.stream()
                .sorted(Comparator.comparingLong(SerializationSample::totalNanos))
                .toList();
        return sorted.get(sorted.size() / 2);
    }

    private static long medianParseDuration(final List<Long> durations) {
        final List<Long> sorted = durations.stream().sorted().toList();
        return sorted.get(sorted.size() / 2);
    }

    private static void printHeader(final int iterations,
            final int warmupIterations) {
        CONSOLE_REPORT.printf("%nSerializer performance: %d measured iteration(s), "
                + "%d warm-up iteration(s)%n", iterations, warmupIterations);
        CONSOLE_REPORT.println("Times are milliseconds; serialization columns "
                + "come from the median total iteration.");
        CONSOLE_REPORT.println("source B/s is source bytes divided by median "
                + "serialization total.");
        CONSOLE_REPORT.printf("%-24s %10s %10s %10s %10s %10s %12s %12s %12s%n",
                "fixture", "source KB", "parse", "index", "setup", "render",
                "toString", "serialize total", "source B/s");
    }

    private static void printResult(final Path fixture, final int sourceBytes,
            final long parseNanos, final SerializationSample sample) {
        CONSOLE_REPORT.printf("%-24s %10.1f %10.1f %10.1f %10.1f %10.1f %12.1f %12.1f %,12.0f%n",
                fixture.getFileName(), sourceBytes / 1024.0,
                milliseconds(parseNanos), milliseconds(sample.indexNanos()),
                milliseconds(sample.setupNanos()),
                milliseconds(sample.renderingNanos()),
                milliseconds(sample.materializationNanos()),
                milliseconds(sample.totalNanos()),
                bytesPerSecond(sourceBytes, sample.totalNanos()));
        CONSOLE_REPORT.printf("  output: %,d characters%n", sample.outputLength());
    }

    private static double milliseconds(final long nanoseconds) {
        return nanoseconds / 1_000_000.0;
    }

    private static double bytesPerSecond(final long bytes,
            final long nanoseconds) {
        return bytes * 1_000_000_000.0 / nanoseconds;
    }

    /** Timings captured during one serialization iteration. */
    private record SerializationSample(long indexNanos, long setupNanos,
            long renderingNanos, long materializationNanos, int outputLength) {

        long totalNanos() {
            return this.indexNanos + this.setupNanos + this.renderingNanos
                    + this.materializationNanos;
        }
    }

    /** One measured parse and the resulting model used by serialization. */
    private record ParseSample(ISysMLParseResult<? extends Element> result,
            long durationNanos) {
    }
}
