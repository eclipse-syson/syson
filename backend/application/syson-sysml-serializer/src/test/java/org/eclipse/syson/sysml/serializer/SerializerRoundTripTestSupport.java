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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.parser.internal.SysMLLexer;
import org.eclipse.syson.sysml.parser.internal.SysMLParser;
import org.eclipse.syson.sysml.serializer.ParserRuleFixtureDiscovery.ParserRuleFolder;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.junit.jupiter.params.provider.Arguments;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Shares fixture discovery and the two round-trip contracts of the serializer corpus tests.
 * <ul>
 * <li>Text round trip: {@code ROUND_TRIP} output keeps every non-whitespace source character, and every string and
 * unrestricted name exactly, whitespace included.</li>
 * <li>Semantic round trip: {@code NORMALIZED} output, produced after removing every {@link ITextAdapter}, reparses
 * into the same canonical XMI and the same unresolved references. The parse result is kept so dangling links still
 * provide unresolved spellings.</li>
 * </ul>
 * Whole documents are handled as fixtures of the {@code parseRootNamespace} entry point, which the serializer does not
 * treat as a fragment.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SerializerRoundTripTestSupport {

    private static final String SYSML_EXTENSION = ".sysml";

    private static final String TAB = "\t";

    private static final int CONTEXT_LENGTH = 40;

    private static final int ABBREVIATED_TEXT_LENGTH = 240;

    private static final SerializationOptions ROUND_TRIP_OPTIONS = SerializationOptions.defaults()
            .withMode(SerializationMode.ROUND_TRIP)
            .withImplicitReferenceKeywordElision(false);

    private static final SerializationOptions NORMALIZED_OPTIONS = SerializationOptions.defaults()
            .withImplicitReferenceKeywordElision(false);

    private static final Path SERIALIZER_FIXTURE_ROOT = Path.of(System.getProperty("syson.serializer.fixtures", "src/test/resources"));

    private static final String SERIALIZER_TEST_CASES = "src/test/resources/testcases/";

    private static final List<String> NON_REPRESENTATIVE_TEST_CASES = List.of("/perfo/", "/ftb/", "/ReferenceImplementation/");

    private static final Path PARSER_TEST_RESOURCE_ROOT = Path.of("../syson-sysml-parser/src/test/resources");

    private static final Path PARSER_ROOT_NAMESPACE_TEST_ROOT = PARSER_TEST_RESOURCE_ROOT.resolve("rootNamespace");

    private static final Path PARSER_TEST_SOURCE_ROOT = Path.of("../syson-sysml-parser/src/test/java/org/eclipse/syson/sysml/parser/tests");

    private static final Map<String, String> PARTIAL_PARSER_FIXTURES = readPartialParserFixtures();

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    private SerializerRoundTripTestSupport() {
    }

    /** Provides the serializer's own whole-document fixtures. */
    public static Stream<Arguments> sysmlFiles() throws IOException {
        return documentFixtures(SERIALIZER_FIXTURE_ROOT, "serializer round-trip").map(Arguments::of);
    }

    /** Provides the parser's whole-document fixtures that the parser accepts. */
    public static Stream<Arguments> parserRootNamespaceSysmlFiles() throws IOException {
        return documentFixtures(PARSER_ROOT_NAMESPACE_TEST_ROOT, "parser rootNamespace fixture")
                .filter(SerializerRoundTripTestSupport::isParserAcceptedFixture)
                .map(Arguments::of);
    }

    /** Provides every accepted parser-rule fixture, including those that only consume a prefix of their input. */
    public static Stream<Arguments> parserRuleFixtureSysmlFiles() throws IOException {
        return acceptedParserRuleFixtures().map(Arguments::of);
    }

    /**
     * Provides the parser-rule fixtures eligible for exact-text comparison: the root namespace corpus is already
     * covered by {@link #parserRootNamespaceSysmlFiles()}, and listed prefix-only fixtures cannot be compared with
     * their whole input.
     */
    public static Stream<Arguments> completeParserRuleFixtureSysmlFiles() throws IOException {
        return acceptedParserRuleFixtures()
                .filter(fixture -> !ParserFragments.ROOT_NAMESPACE.equals(fixture.parseMethod()))
                .filter(fixture -> !PARTIAL_PARSER_FIXTURES.containsKey(parserFixtureKey(fixture)))
                .map(Arguments::of);
    }

    /**
     * Discovers the parser-rule fixtures that the parser accepts. A fixture expected to parse fails discovery instead
     * of silently leaving the corpus.
     */
    public static Stream<ParserRuleFixture> acceptedParserRuleFixtures() throws IOException {
        requireDirectory(PARSER_TEST_SOURCE_ROOT, "parser test source");
        requireDirectory(PARSER_TEST_RESOURCE_ROOT, "parser test resource");
        return ParserRuleFixtureDiscovery.discover(PARSER_TEST_SOURCE_ROOT).stream()
                .flatMap(SerializerRoundTripTestSupport::parserRuleFixtures)
                .map(SerializerRoundTripTestSupport::requireParseableFixture);
    }

    /**
     * Tells whether the fixture's entry rule consumes the whole input. Prefix-only fixtures cannot be compared
     * character by character with their serialization.
     */
    public static boolean consumesEntireFixture(final ParserRuleFixture fixture) {
        try {
            final String source = Files.readString(fixture.sysmlPath(), StandardCharsets.UTF_8);
            final CommonTokenStream tokens = new CommonTokenStream(new SysMLLexer(CharStreams.fromString(source)));
            final SysMLParser parser = new SysMLParser(tokens);
            parser.setInternalDiagnostics(new ArrayList<>());
            parser.removeErrorListeners();
            SysMLParser.class.getMethod(fixture.grammarRuleName()).invoke(parser);
            if (parser.getNumberOfSyntaxErrors() != 0) {
                fail("Unexpected syntax errors in " + fixture);
            }
            return tokens.LA(1) == Token.EOF;
        } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            throw new IllegalStateException("Unable to check complete parser fixture " + fixture, exception);
        }
    }

    /** Parses the fixture with its entry point and fails, instead of skipping it, when the parser rejects it. */
    @SuppressWarnings("checkstyle:IllegalCatch")
    public static ParserRuleFixture requireParseableFixture(final ParserRuleFixture fixture) {
        try {
            assertParseSucceeded(fixture, "source", parse(new SysMLModelParser(), fixture, fixture.readSource()), RemovedTextAdapters.NONE);
            return fixture;
        } catch (RuntimeException | AssertionError | IOException exception) {
            return fail("Unexpected parser failure during fixture discovery: " + fixture, exception);
        }
    }

    /**
     * Provides the whole corpus once: serializer documents, accepted parser documents and accepted parser-rule fixtures.
     * Fixtures are not parsed during discovery; use {@link #parseAccepted(ParserRuleFixture)}.
     */
    public static Stream<ParserRuleFixture> corpusFixtures() throws IOException {
        requireDirectory(PARSER_TEST_SOURCE_ROOT, "parser test source");
        final Stream<ParserRuleFixture> documents = Stream.concat(
                documentFixtures(SERIALIZER_FIXTURE_ROOT, "serializer round-trip"),
                documentFixtures(PARSER_ROOT_NAMESPACE_TEST_ROOT, "parser rootNamespace fixture").filter(SerializerRoundTripTestSupport::isParserAcceptedFixture))
                .map(ParserRuleFixture::document);
        final Stream<ParserRuleFixture> ruleFixtures = ParserRuleFixtureDiscovery.discover(PARSER_TEST_SOURCE_ROOT).stream()
                .flatMap(SerializerRoundTripTestSupport::parserRuleFixtures);
        return Stream.concat(Stream.concat(documents, serializerFragmentFixtures()), ruleFixtures).distinct();
    }

    /** Provides serializer fragments stored by grammar rule, so they are never parsed as whole documents. */
    public static Stream<ParserRuleFixture> serializerFragmentFixtures() {
        final Path fragments = SERIALIZER_FIXTURE_ROOT.resolve("fragments");
        if (!Files.isDirectory(fragments)) {
            return Stream.empty();
        }
        return list(fragments).filter(Files::isDirectory).flatMap(folder -> {
            final String rule = folder.getFileName().toString();
            final String entryPoint = "parse" + Character.toUpperCase(rule.charAt(0)) + rule.substring(1);
            return list(folder).filter(SerializerRoundTripTestSupport::isSysmlFile)
                    .sorted().map(path -> new ParserRuleFixture(entryPoint, path));
        });
    }

    /**
     * Provides a small corpus that still reaches every kind of reference: the parser-rule fixtures, which cover each
     * grammar rule, and the serializer test cases, which cover each serializer defect. Whole libraries, reference
     * examples and performance corpora only add size. Tests that serialize each fixture several times use it to stay
     * fast.
     */
    public static Stream<ParserRuleFixture> representativeCorpusFixtures() throws IOException {
        return corpusFixtures().filter(SerializerRoundTripTestSupport::isRepresentative);
    }

    private static boolean isRepresentative(final ParserRuleFixture fixture) {
        final String path = fixture.sysmlPath().toString().replace('\\', '/');
        final boolean serializerTestCase = path.contains(SERIALIZER_TEST_CASES) && NON_REPRESENTATIVE_TEST_CASES.stream().noneMatch(path::contains);
        return !ParserFragments.ROOT_NAMESPACE.equals(fixture.parseMethod()) || serializerTestCase;
    }

    /** Parses the fixture with its own entry point and fails when the parser reports an error. */
    public static ISysMLParseResult<? extends Element> parseAccepted(final ParserRuleFixture fixture) throws IOException {
        final ISysMLParseResult<? extends Element> result = parse(new SysMLModelParser(), fixture, fixture.readSource());
        assertParseSucceeded(fixture, "source", result, RemovedTextAdapters.NONE);
        return result;
    }

    /** Parses a whole SysML document held in a string, as the import of a {@code .sysml} file does. */
    public static ISysMLParseResult<Namespace> parseDocument(final String source) throws IOException {
        return new SysMLModelParser().parseRootNamespace(new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    /**
     * Parses a serializer test case, such as a fixture of {@code testcases/edited} that a test edits after parsing.
     *
     * @param testCase
     *            the path of the test case below {@code src/test/resources/testcases}
     * @return the parse result
     * @throws IOException
     *             when the test case cannot be read
     */
    public static ISysMLParseResult<Namespace> parseTestCase(final String testCase) throws IOException {
        return parseDocument(Files.readString(Path.of(SERIALIZER_TEST_CASES, testCase)));
    }

    /** Identifies a fixture in the recorded corpus inventory and in the partial-fixture exclusions. */
    public static String parserFixtureKey(final ParserRuleFixture fixture) {
        return fixture.parseMethod() + TAB + PARSER_TEST_RESOURCE_ROOT.relativize(fixture.sysmlPath()).toString().replace('\\', '/');
    }

    /** Returns the explicit reasons for excluding prefix-only fixtures from exact-text checks. */
    public static Map<String, String> partialParserFixtures() {
        return PARTIAL_PARSER_FIXTURES;
    }

    /** Asserts that a whole document keeps every non-whitespace character through ROUND_TRIP serialization. */
    public static void assertWhitespaceOnlyRoundTrip(final Path sysmlPath, final SysMLModelParser parser, final SysMLSerializer serializer) throws IOException {
        assertWhitespaceOnlyRoundTrip(ParserRuleFixture.document(sysmlPath), parser, serializer);
    }

    /** Asserts that a complete parser-rule fixture keeps every non-whitespace character through ROUND_TRIP serialization. */
    public static void assertWhitespaceOnlyRoundTrip(final ParserRuleFixture fixture, final SysMLModelParser parser, final SysMLSerializer serializer) throws IOException {
        assertTrue(consumesEntireFixture(fixture), "Unexpected partial parser fixture: " + fixture);
        final String source = fixture.readSource();
        final ISysMLParseResult<? extends Element> result = parse(parser, fixture, source);
        assertParseSucceeded(fixture, "source", result, RemovedTextAdapters.NONE);
        final String serialized = serializer.serialize(result, ROUND_TRIP_OPTIONS.withParserEntryPoint(fixture.parseMethod()));
        final String difference = firstDifferenceIgnoringWhitespace(source, serialized);
        if (difference != null) {
            fail("Parsing and serializing changed non-whitespace SysML text.\nFixture: " + fixture + "\n" + difference);
        }
        final String quotedDifference = quotedTokenDifference(source, serialized);
        if (quotedDifference != null) {
            fail("Parsing and serializing changed a string or unrestricted name, whose whitespace is significant.\nFixture: "
                    + fixture + "\n" + quotedDifference);
        }
    }

    /**
     * Compares the string literals and unrestricted names of both texts exactly, whitespace included, and describes the
     * first difference, or returns {@code null} when they are identical.
     */
    static String quotedTokenDifference(final String expected, final String actual) {
        final List<String> expectedTokens = quotedTokens(expected);
        final List<String> actualTokens = quotedTokens(actual);
        if (expectedTokens.equals(actualTokens)) {
            return null;
        }
        return firstTokenDifference(expectedTokens, actualTokens);
    }

    /** Describes the first position where two token lists differ. */
    private static String firstTokenDifference(final List<String> expected, final List<String> actual) {
        int index = 0;
        while (index < expected.size() && index < actual.size() && expected.get(index).equals(actual.get(index))) {
            index++;
        }
        return "Quoted token " + (index + 1) + ", expected " + tokenOrMissing(expected, index) + ", actual "
                + tokenOrMissing(actual, index);
    }

    private static String tokenOrMissing(final List<String> tokens, final int index) {
        if (index < tokens.size()) {
            return "[" + printable(tokens.get(index)) + "]";
        }
        return "<missing>";
    }

    /**
     * Returns the exact text of the string literals and unrestricted names of {@code text}, in order. Whitespace inside
     * these tokens is part of their value, so the whitespace-insensitive comparison must not be the only check.
     */
    private static List<String> quotedTokens(final String text) {
        final List<String> quotedTokens = new ArrayList<>();
        final SysMLLexer lexer = new SysMLLexer(CharStreams.fromString(text));
        lexer.removeErrorListeners();
        for (Token token = lexer.nextToken(); token.getType() != Token.EOF; token = lexer.nextToken()) {
            if (token.getType() == SysMLLexer.STRING_VALUE || token.getType() == SysMLLexer.UNRESTRICTED_NAME) {
                quotedTokens.add(token.getText());
            }
        }
        return quotedTokens;
    }

    /** Asserts that a whole document reparses into the same model after adapter-free NORMALIZED serialization. */
    public static void assertSemanticRoundTripWithoutTextAdapters(final Path sysmlPath, final SysMLModelParser parser, final SysMLSerializer serializer)
            throws IOException {
        assertSemanticRoundTripWithoutTextAdapters(ParserRuleFixture.document(sysmlPath), parser, serializer);
    }

    /**
     * Asserts that a parser-rule fixture reparses, with its own entry point, into the same canonical XMI after
     * adapter-free NORMALIZED serialization. A reparse failure is reported with the removed adapters and the serialized
     * text, which are needed to diagnose the regression.
     */
    @SuppressWarnings("checkstyle:IllegalCatch")
    public static void assertSemanticRoundTripWithoutTextAdapters(final ParserRuleFixture fixture, final SysMLModelParser parser, final SysMLSerializer serializer)
            throws IOException {
        final ISysMLParseResult<? extends Element> result = parse(parser, fixture, fixture.readSource());
        assertParseSucceeded(fixture, "source", result, RemovedTextAdapters.NONE);
        final String expectedLinks = canonicalDanglingLinks(result);
        final String expectedXmi = toCanonicalXmi(result.model(), "expected-" + fixture.sysmlPath().getFileName());

        final RemovedTextAdapters removedAdapters = RemovedTextAdapters.removeFrom(result.model());
        final String serialized = serializer.serialize(result, NORMALIZED_OPTIONS.withParserEntryPoint(fixture.parseMethod()));
        final String context = "\nRemoved ITextAdapter implementations: " + removedAdapters + "\nSerialized text: " + abbreviatedSingleLine(serialized);
        final ISysMLParseResult<? extends Element> reparsed;
        try {
            reparsed = parse(parser, fixture, serialized);
        } catch (RuntimeException | AssertionError exception) {
            fail("The parser could not reparse the adapter-free serialization of " + fixture + context + "\nParser failure: " + exception);
            return;
        }
        assertParseSucceeded(fixture, "adapter-free serialization", reparsed, removedAdapters);

        final String actualLinks = canonicalDanglingLinks(reparsed);
        final String actualXmi = toCanonicalXmi(reparsed.model(), "actual-" + fixture.sysmlPath().getFileName());
        if (!expectedXmi.equals(actualXmi)) {
            fail("Removing ITextAdapter instances before serializing changed the semantic XMI after reparsing " + fixture + context
                    + "\nFirst canonical XMI difference: " + firstLineDifference(expectedXmi, actualXmi));
        }
        if (!expectedLinks.equals(actualLinks)) {
            fail("Removing ITextAdapter instances before serializing changed the unresolved references after reparsing " + fixture
                    + context + "\nFirst dangling link difference: " + firstLineDifference(expectedLinks, actualLinks));
        }
    }

    /**
     * Describes the unresolved references of a parse result, one sorted line per dangling link: the position of its
     * owner in containment order, the reference, the expected target type and the parsed target spellings. The XMI
     * cannot show these references, so two texts naming different unresolved targets would otherwise compare equal.
     */
    static String canonicalDanglingLinks(final ISysMLParseResult<? extends Element> result) {
        final Map<EObject, Integer> positions = new IdentityHashMap<>();
        final Element root = result.model();
        if (root != null) {
            positions.put(root, 0);
            root.eAllContents().forEachRemaining(object -> positions.put(object, positions.size()));
        }
        return result.danglingLinks().stream()
                .map(link -> positions.getOrDefault(link.context(), -1) + " " + link.context().eClass().getName() + "."
                        + link.reference().getName() + " : " + link.eClass().getName() + " -> " + link.targets())
                .sorted()
                .collect(Collectors.joining("\n"));
    }

    private static Stream<Path> documentFixtures(final Path root, final String description) throws IOException {
        requireDirectory(root, description);
        return Files.walk(root)
                .filter(SerializerRoundTripTestSupport::isSysmlFile)
                // Fragment fixtures are exercised with their own parser entry points.
                .filter(path -> !path.startsWith(root.resolve("fragments")))
                .sorted();
    }

    private static Stream<ParserRuleFixture> parserRuleFixtures(final ParserRuleFolder ruleFolder) {
        final Path ruleResourceRoot = PARSER_TEST_RESOURCE_ROOT.resolve(ruleFolder.resourceFolder());
        if (!Files.isDirectory(ruleResourceRoot)) {
            throw new IllegalStateException("The parser rule fixture folder does not exist: " + ruleResourceRoot);
        }
        return list(ruleResourceRoot)
                .filter(Files::isDirectory)
                .map(caseFolder -> caseFolder.resolve("sysml"))
                .filter(Files::isDirectory)
                .flatMap(sysmlFolder -> list(sysmlFolder)
                        .filter(SerializerRoundTripTestSupport::isSysmlFile)
                        .filter(SerializerRoundTripTestSupport::isParserAcceptedFixture)
                        .sorted())
                .map(sysmlPath -> new ParserRuleFixture(ruleFolder.parseMethod(), sysmlPath));
    }

    private static Stream<Path> list(final Path folder) {
        try {
            return Files.list(folder);
        } catch (final IOException exception) {
            throw new UncheckedIOException("Unable to list fixture folder " + folder, exception);
        }
    }

    private static boolean isSysmlFile(final Path path) {
        return path.toString().endsWith(SYSML_EXTENSION);
    }

    private static void requireDirectory(final Path directory, final String description) {
        if (!Files.isDirectory(directory)) {
            fail("The " + description + " folder does not exist: " + directory);
        }
    }

    /**
     * Excludes negative parser fixtures, which the parser tests identify by a nonempty
     * {@code model/<name>-syntax-expected.txt} file next to the {@code sysml} folder.
     */
    private static boolean isParserAcceptedFixture(final Path sysmlPath) {
        final String baseName = sysmlPath.getFileName().toString().replaceFirst("\\.sysml$", "");
        final Path expectedSyntax = sysmlPath.getParent().resolveSibling("model").resolve(baseName + "-syntax-expected.txt");
        try {
            return !Files.isRegularFile(expectedSyntax) || Files.readString(expectedSyntax, StandardCharsets.UTF_8).isBlank();
        } catch (final IOException exception) {
            throw new UncheckedIOException("Unable to read parser syntax expectation for " + sysmlPath, exception);
        }
    }

    /**
     * Reads {@code partial-parser-rule-fixtures.tsv}: one {@code entry point, fixture path, reason} line per prefix-only
     * fixture. A missing reason or a duplicate is a configuration error.
     */
    private static Map<String, String> readPartialParserFixtures() {
        final Path exclusions = Path.of("src/test/resources/partial-parser-rule-fixtures.tsv");
        try {
            final Map<String, String> reasons = new LinkedHashMap<>();
            for (final String line : Files.readAllLines(exclusions, StandardCharsets.UTF_8)) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                final String[] fields = line.split(TAB, -1);
                if (fields.length != 3 || fields[2].isBlank()) {
                    throw new IllegalStateException("An exclusion needs an entry point, path and reason: " + line);
                }
                if (reasons.putIfAbsent(fields[0] + TAB + fields[1], fields[2]) != null) {
                    throw new IllegalStateException("Duplicate partial fixture exclusion: " + line);
                }
            }
            return Map.copyOf(reasons);
        } catch (final IOException exception) {
            throw new UncheckedIOException("Unable to read partial parser fixture exclusions " + exclusions, exception);
        }
    }

    /** Invokes the {@link SysMLModelParser} method named by the fixture, rethrowing the parser's own failure. */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static ISysMLParseResult<? extends Element> parse(final SysMLModelParser parser, final ParserRuleFixture fixture, final String source) throws IOException {
        final Object result;
        try (InputStream input = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8))) {
            result = SysMLModelParser.class.getMethod(fixture.parseMethod(), InputStream.class, Charset.class).invoke(parser, input, StandardCharsets.UTF_8);
        } catch (NoSuchMethodException | IllegalAccessException exception) {
            throw new IllegalStateException("Unable to invoke parser method " + fixture.parseMethod(), exception);
        } catch (final InvocationTargetException exception) {
            throw rethrowParserFailure(fixture, exception.getCause());
        }
        final ISysMLParseResult<?> parseResult = (ISysMLParseResult<?>) result;
        if (!(parseResult.model() instanceof Element)) {
            throw new IllegalStateException("Parser method " + fixture.parseMethod() + " returned a non-Element model for " + fixture.sysmlPath() + ": " + parseResult.model());
        }
        return (ISysMLParseResult<? extends Element>) parseResult;
    }

    private static IllegalStateException rethrowParserFailure(final ParserRuleFixture fixture, final Throwable cause) throws IOException {
        if (cause instanceof final IOException ioException) {
            throw ioException;
        }
        if (cause instanceof final RuntimeException runtimeException) {
            throw runtimeException;
        }
        if (cause instanceof final Error error) {
            throw error;
        }
        return new IllegalStateException("Parser method " + fixture.parseMethod() + " failed", cause);
    }

    private static void assertParseSucceeded(final ParserRuleFixture fixture, final String input, final ISysMLParseResult<? extends Element> result,
            final RemovedTextAdapters removedAdapters) {
        if (result.diagnostic().getSeverity() == Diagnostic.ERROR) {
            fail("The parser rejected the " + input + " of " + fixture + ": " + result.diagnostic() + "\nRemoved ITextAdapter implementations: " + removedAdapters);
        }
    }

    /**
     * Saves the model as XMI whose generated element ids are replaced by their document order, so that two parses of
     * equivalent text compare equal. The root is moved into a new resource rather than copied.
     */
    static String toCanonicalXmi(final Element root, final String resourceName) throws IOException {
        final ResourceSetImpl resourceSet = new ResourceSetImpl();
        resourceSet.getPackageRegistry().put(SysMLPackage.eNS_URI, SysMLPackage.eINSTANCE);
        final Resource resource = new XMIResourceImpl(URI.createURI(resourceName));
        resourceSet.getResources().add(resource);
        if (root != null) {
            resource.getContents().add(root);
        }
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            resource.save(output, xmiSaveOptions());
            return canonicalizeXmi(output.toString(StandardCharsets.UTF_8));
        }
    }

    private static Map<String, Object> xmiSaveOptions() {
        final Map<String, Object> options = new HashMap<>();
        options.put(XMLResource.OPTION_ENCODING, StandardCharsets.UTF_8.name());
        options.put(XMLResource.OPTION_FORMATTED, Boolean.TRUE);
        options.put(XMLResource.OPTION_KEEP_DEFAULT_CONTENT, Boolean.FALSE);
        options.put(XMLResource.OPTION_DEFER_ATTACHMENT, Boolean.TRUE);
        options.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
        options.put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.FALSE);
        options.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
        options.put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<>());
        options.put(XMLResource.OPTION_USE_PACKAGE_NS_URI_AS_LOCATION, Boolean.FALSE);
        return options;
    }

    /** Assigns canonical ids in element order and sorts attributes while retaining child-element order. */
    private static String canonicalizeXmi(final String xmi) throws IOException {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setExpandEntityReferences(false);
            final Document document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xmi.getBytes(StandardCharsets.UTF_8)));
            final Map<String, String> canonicalIds = new LinkedHashMap<>();
            collectElementIds(document.getDocumentElement(), canonicalIds);
            final StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            appendCanonicalElement(document.getDocumentElement(), canonicalIds, builder, 0);
            return builder.toString();
        } catch (ParserConfigurationException | SAXException exception) {
            throw new IOException("Unable to canonicalize generated XMI identifiers", exception);
        }
    }

    private static void collectElementIds(final org.w3c.dom.Element element, final Map<String, String> canonicalIds) {
        if (element.hasAttribute("elementId")) {
            canonicalIds.computeIfAbsent(element.getAttribute("elementId"), ignored -> "element-" + canonicalIds.size());
        }
        childElements(element).forEach(child -> collectElementIds(child, canonicalIds));
    }

    private static void appendCanonicalElement(final org.w3c.dom.Element element, final Map<String, String> canonicalIds, final StringBuilder builder, final int depth) {
        final String indentation = "  ".repeat(depth);
        builder.append(indentation).append('<').append(element.getTagName());
        for (final Attr attribute : sortedAttributes(element)) {
            final String value = canonicalIds.getOrDefault(attribute.getValue(), attribute.getValue());
            builder.append(' ').append(attribute.getName()).append("=\"").append(escapeXml(value)).append('"');
        }
        final List<org.w3c.dom.Element> children = childElements(element);
        if (children.isEmpty()) {
            builder.append("/>\n");
            return;
        }
        builder.append(">\n");
        children.forEach(child -> appendCanonicalElement(child, canonicalIds, builder, depth + 1));
        builder.append(indentation).append("</").append(element.getTagName()).append(">\n");
    }

    private static List<Attr> sortedAttributes(final org.w3c.dom.Element element) {
        final NamedNodeMap attributes = element.getAttributes();
        final List<Attr> sorted = new ArrayList<>(attributes.getLength());
        for (int i = 0; i < attributes.getLength(); i++) {
            sorted.add((Attr) attributes.item(i));
        }
        sorted.sort(Comparator.comparing(Attr::getName));
        return sorted;
    }

    private static List<org.w3c.dom.Element> childElements(final org.w3c.dom.Element element) {
        final List<org.w3c.dom.Element> children = new ArrayList<>();
        for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof final org.w3c.dom.Element childElement) {
                children.add(childElement);
            }
        }
        return children;
    }

    private static String escapeXml(final String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    /**
     * Compares the two texts with whitespace skipped on both sides and describes the first mismatching character, or
     * returns {@code null} when they only differ by whitespace.
     */
    static String firstDifferenceIgnoringWhitespace(final String expected, final String actual) {
        int expectedIndex = skipWhitespace(expected, 0);
        int actualIndex = skipWhitespace(actual, 0);
        while (expectedIndex < expected.length() && actualIndex < actual.length() && expected.charAt(expectedIndex) == actual.charAt(actualIndex)) {
            expectedIndex = skipWhitespace(expected, expectedIndex + 1);
            actualIndex = skipWhitespace(actual, actualIndex + 1);
        }
        if (expectedIndex == expected.length() && actualIndex == actual.length()) {
            return null;
        }
        return "Expected " + describePosition(expected, expectedIndex) + "\nActual   " + describePosition(actual, actualIndex);
    }

    private static int skipWhitespace(final String text, final int start) {
        int index = start;
        while (index < text.length() && Character.isWhitespace(text.charAt(index))) {
            index++;
        }
        return index;
    }

    private static String describePosition(final String text, final int index) {
        final int line = (int) text.substring(0, index).chars().filter(character -> character == '\n').count() + 1;
        final int column = index - text.lastIndexOf('\n', index - 1);
        String token = "<end-of-input>";
        if (index < text.length()) {
            token = printable(String.valueOf(text.charAt(index)));
        }
        final String context = printable(text.substring(Math.max(0, index - CONTEXT_LENGTH), Math.min(text.length(), index + CONTEXT_LENGTH)));
        return "at line " + line + ", column " + column + " (offset " + index + "): " + token + "\n  context: " + context;
    }

    private static String printable(final String text) {
        return text.replace("\r", "\\r").replace("\n", "\\n").replace(TAB, "\\t");
    }

    private static String abbreviatedSingleLine(final String text) {
        final String singleLine = printable(text);
        if (singleLine.length() <= ABBREVIATED_TEXT_LENGTH) {
            return singleLine;
        }
        return singleLine.substring(0, ABBREVIATED_TEXT_LENGTH) + "...";
    }

    private static String firstLineDifference(final String expected, final String actual) {
        final String[] expectedLines = expected.split("\\R", -1);
        final String[] actualLines = actual.split("\\R", -1);
        for (int i = 0; i < Math.max(expectedLines.length, actualLines.length); i++) {
            final String expectedLine = lineOrMissing(expectedLines, i);
            final String actualLine = lineOrMissing(actualLines, i);
            if (!expectedLine.equals(actualLine)) {
                return "line " + (i + 1) + ", expected '" + visibleSpaces(expectedLine) + "', actual '" + visibleSpaces(actualLine) + "'";
            }
        }
        return "<none>";
    }

    private static String lineOrMissing(final String[] lines, final int index) {
        if (index < lines.length) {
            return lines[index];
        }
        return "<missing>";
    }

    private static String visibleSpaces(final String line) {
        return line.replace("\\", "\\\\").replace(TAB, "\\t").replace(" ", "<space>");
    }

    /**
     * SysML source parsed with one {@link SysMLModelParser} entry point.
     *
     * @param parseMethod
     *            the {@link SysMLModelParser} method name, such as {@code parseRootNamespace}
     * @param sysmlPath
     *            the fixture file
     */
    public record ParserRuleFixture(String parseMethod, Path sysmlPath) {

        /** Creates the fixture of a whole document. */
        static ParserRuleFixture document(final Path sysmlPath) {
            return new ParserRuleFixture(ParserFragments.ROOT_NAMESPACE, sysmlPath);
        }

        /** Maps {@code parseFooBar} to the generated ANTLR rule method {@code fooBar}. */
        String grammarRuleName() {
            final String rule = this.parseMethod.substring("parse".length());
            return Character.toLowerCase(rule.charAt(0)) + rule.substring(1);
        }

        String readSource() throws IOException {
            return Files.readString(this.sysmlPath, StandardCharsets.UTF_8);
        }

        @Override
        public String toString() {
            return this.parseMethod + " " + this.sysmlPath;
        }
    }

    /** Records which syntax adapters were removed, so that a semantic round-trip failure names what the model lost. */
    private static final class RemovedTextAdapters {

        static final RemovedTextAdapters NONE = new RemovedTextAdapters();

        private final Map<String, Integer> countsByClass = new LinkedHashMap<>();

        /** Removes every {@link ITextAdapter} from {@code root} and its contents. */
        static RemovedTextAdapters removeFrom(final EObject root) {
            final RemovedTextAdapters removed = new RemovedTextAdapters();
            if (root != null) {
                removed.removeDirectAdapters(root);
                root.eAllContents().forEachRemaining(removed::removeDirectAdapters);
            }
            return removed;
        }

        private void removeDirectAdapters(final EObject object) {
            final List<ITextAdapter> textAdapters = object.eAdapters().stream()
                    .filter(ITextAdapter.class::isInstance)
                    .map(ITextAdapter.class::cast)
                    .toList();
            textAdapters.forEach(adapter -> this.countsByClass.merge(adapter.getClass().getName(), 1, Integer::sum));
            object.eAdapters().removeAll(textAdapters);
        }

        @Override
        public String toString() {
            if (this.countsByClass.isEmpty()) {
                return "<none>";
            }
            return new TreeMap<>(this.countsByClass).entrySet().stream()
                    .map(entry -> entry.getKey() + " x" + entry.getValue())
                    .collect(Collectors.joining(", "));
        }
    }
}
