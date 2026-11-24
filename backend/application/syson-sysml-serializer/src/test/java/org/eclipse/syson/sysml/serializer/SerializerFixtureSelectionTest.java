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

import static org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.acceptedParserRuleFixtures;
import static org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.canonicalDanglingLinks;
import static org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.consumesEntireFixture;
import static org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.firstDifferenceIgnoringWhitespace;
import static org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.parseDocument;
import static org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.parserFixtureKey;
import static org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.parserRootNamespaceSysmlFiles;
import static org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.partialParserFixtures;
import static org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.quotedTokenDifference;
import static org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.requireParseableFixture;
import static org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.toCanonicalXmi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.ParserRuleFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.provider.Arguments;

/**
 * Makes fixture selection changes, unexpected discovery failures and blind spots of the round-trip comparisons
 * observable.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class SerializerFixtureSelectionTest {

    private static final Path PARSER_TEST_RESOURCE_ROOT = Path
            .of("../syson-sysml-parser/src/test/resources");

    private static final Map<String, String> PARTIAL_PARSER_FIXTURES = partialParserFixtures();

    /**
     * The text round trip must not ignore whitespace inside unrestricted names and strings, where it is part of the
     * value, nor be misled by quotes inside comments; whitespace between tokens must stay insignificant.
     */
    @Test
    void textComparisonKeepsWhitespaceInsideQuotedTokens() {
        final String source = "part def 'a  b' { attribute x = \" s \"; /* it's */ }";
        assertNull(firstDifferenceIgnoringWhitespace(source, "part def 'a b' { attribute x = \"s\"; /* it's */ }"));
        assertNotNull(quotedTokenDifference(source, "part def 'a b' { attribute x = \" s \"; /* it's */ }"));
        assertNotNull(quotedTokenDifference(source, "part def 'a  b' { attribute x = \"s\"; /* it's */ }"));
        assertNull(quotedTokenDifference(source, "part def 'a  b'{attribute x=\" s \";/*it's*/}"));
    }

    /**
     * The semantic round trip must notice a different unresolved target: the canonical XMI cannot, because unresolved
     * references are kept as dangling links outside the model.
     */
    @Test
    void semanticComparisonKeepsUnresolvedTargetSpellings() throws IOException {
        final var expected = parseDocument("package P { part p : A; }");
        final var actual = parseDocument("package P { part p : B; }");
        final String expectedLinks = canonicalDanglingLinks(expected);
        final String actualLinks = canonicalDanglingLinks(actual);

        assertEquals(toCanonicalXmi(expected.model(), "expected"), toCanonicalXmi(actual.model(), "actual"));
        assertNotEquals(expectedLinks, actualLinks);
        assertEquals(expectedLinks, canonicalDanglingLinks(parseDocument("package P {\n\tpart p : A;\n}")));
    }

    /**
     * The completeness check must accept a fully consumed fixture and reject a fixture for which the parser only
     * consumes a prefix.
     */
    @Test
    void exactRuleFixturesMustConsumeTheirEntireInput() {
        assertTrue(consumesEntireFixture(new ParserRuleFixture(
                "parseActionUsage", PARSER_TEST_RESOURCE_ROOT.resolve(
                        "actionUsage/withTypings/sysml/withTypings.sysml"))));
        assertFalse(consumesEntireFixture(new ParserRuleFixture(
                "parseXorExpression", PARSER_TEST_RESOURCE_ROOT.resolve(
                        "xorExpression/missingXorKeyword/sysml/missingXorKeyword.sysml"))));
    }

    /**
     * Every partial-input exclusion must refer to a discovered fixture that still leaves input unconsumed; obsolete
     * exclusions must fail this test.
     */
    @Test
    void partialFixtureExclusionsRemainNecessary() throws IOException {
        final List<ParserRuleFixture> fixtures;
        try (Stream<ParserRuleFixture> discovered = acceptedParserRuleFixtures()) {
            fixtures = discovered.toList();
        }
        final List<String> keys = fixtures.stream().map(SerializerRoundTripTestSupport::parserFixtureKey).toList();
        assertTrue(keys.containsAll(PARTIAL_PARSER_FIXTURES.keySet()), "A partial fixture exclusion refers to an undiscovered fixture");
        for (final ParserRuleFixture fixture : fixtures) {
            final String reason = PARTIAL_PARSER_FIXTURES.get(parserFixtureKey(fixture));
            if (reason != null) {
                assertFalse(consumesEntireFixture(fixture), "Remove the obsolete exclusion for " + fixture + ": " + reason);
            }
        }
    }

    /**
     * The selected parser fixtures must match the recorded inventory exactly; additions, removals, and selection
     * changes must fail this test.
     */
    @Test
    void parserFixtureSelectionMatchesRecordedInventory() throws IOException {
        final List<String> inventory = new ArrayList<>();
        try (Stream<Arguments> fixtures = parserRootNamespaceSysmlFiles()) {
            fixtures.map(arguments -> (Path) arguments.get()[0])
                    .map(path -> parserFixtureKey(new ParserRuleFixture("parseRootNamespace", path)))
                    .forEach(key -> inventory.add("root-namespace\t" + key));
        }
        try (Stream<ParserRuleFixture> fixtures = acceptedParserRuleFixtures()) {
            fixtures.forEach(fixture -> {
                final String key = parserFixtureKey(fixture);
                inventory.add("semantic\t" + key);
                if (!"parseRootNamespace".equals(fixture.parseMethod()) && !PARTIAL_PARSER_FIXTURES.containsKey(key)) {
                    inventory.add("text\t" + key);
                }
            });
        }
        inventory.sort(Comparator.naturalOrder());
        final Path actual = Path.of("target/parser-fixture-selection.tsv");
        Files.createDirectories(actual.getParent());
        Files.write(actual, inventory, StandardCharsets.UTF_8);
        assertIterableEquals(Files.readAllLines(Path.of("src/test/resources/parser-fixture-selection.tsv"), StandardCharsets.UTF_8),
                inventory, "Parser fixture selection changed. Review " + actual + " before updating the recorded inventory.");
    }

    /**
     * A missing fixture file must fail discovery, preserve the IOException cause, and identify the missing file.
     */
    @Test
    void discoveryFailsOnMissingFixtureInput(@TempDir final Path directory) {
        final ParserRuleFixture fixture = new ParserRuleFixture("parseRootNamespace", directory.resolve("missing.sysml"));

        final AssertionError failure = assertThrows(AssertionError.class, () -> requireParseableFixture(fixture));

        assertInstanceOf(IOException.class, failure.getCause());
        assertTrue(failure.getMessage().contains("missing.sysml"));
    }

    /**
     * An unexpected parser exception must fail discovery with its cause and entry point preserved; it must not
     * silently exclude the fixture.
     */
    @Test
    void discoveryFailsOnUnexpectedParserExceptions(@TempDir final Path directory) throws IOException {
        final Path source = directory.resolve("unexpected.sysml");
        Files.writeString(source, "package P;", StandardCharsets.UTF_8);
        final ParserRuleFixture fixture = new ParserRuleFixture("parseMissingRule", source);

        final AssertionError failure = assertThrows(AssertionError.class, () -> requireParseableFixture(fixture));

        assertInstanceOf(IllegalStateException.class, failure.getCause());
        assertTrue(failure.getMessage().contains("parseMissingRule"));
    }

    /**
     * Unexpected parser error diagnostics must fail discovery and identify the fixture; they must not silently exclude
     * it.
     */
    @Test
    void discoveryFailsOnUnexpectedParserDiagnostics(@TempDir final Path directory) throws IOException {
        final Path source = directory.resolve("invalid.sysml");
        Files.writeString(source, "not an integer", StandardCharsets.UTF_8);
        final ParserRuleFixture fixture = new ParserRuleFixture("parseRootNamespace", source);

        final AssertionError failure = assertThrows(AssertionError.class, () -> requireParseableFixture(fixture));

        assertInstanceOf(AssertionError.class, failure.getCause());
        assertTrue(failure.getMessage().contains("invalid.sysml"));
    }
}
