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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserInterpreter;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.DecisionState;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.eclipse.syson.sysml.parser.internal.SysMLLexer;
import org.eclipse.syson.sysml.parser.internal.SysMLParser;
import org.eclipse.syson.sysml.serializer.ParserRuleFixtureDiscovery.ParserRuleFolder;
import org.junit.jupiter.api.Test;

/**
 * Records grammar alternatives, including nested optional and loop choices.
 *
 * @author cbrun
 */
class SysMLGrammarCoverageTest {

    private static final Path SERIALIZER_RESOURCES = Path.of(
            System.getProperty("syson.serializer.fixtures", "src/test/resources"));

    private static final Path GRAMMAR_RECOVERY_FIXTURES = Path.of("src/test/grammar-coverage");

    private static final Path PARSER_RESOURCES = Path
            .of("../syson-sysml-parser/src/test/resources");

    private static final Path PARSER_TEST_SOURCES = Path.of(
            "../syson-sysml-parser/src/test/java/org/eclipse/syson/sysml/parser/tests");

    /**
     * The instrumented interpreter must consume the entire sample input and record at least one visited grammar
     * alternative.
     */
    @Test
    void recordsAlternativesInRootNamespace() {
        final CommonTokenStream tokens = new CommonTokenStream(
                new SysMLLexer(CharStreams.fromString("package P { part p; }")));
        final SysMLParser grammar = new SysMLParser(tokens);
        final RecordingInterpreter interpreter = new RecordingInterpreter(grammar,
                tokens);
        interpreter.setErrorHandler(new BailErrorStrategy());
        interpreter.removeErrorListeners();

        interpreter.parse(SysMLParser.RULE_rootNamespace);

        assertEquals(Token.EOF, tokens.LA(1));
        assertFalse(interpreter.alternatives.isEmpty());
    }

    /**
     * Every grammar decision alternative and every non-WS token type must be witnessed by fully consumed corpus
     * inputs; all serializer fixtures must be fully accepted.
     */
    @Test
    void coversGrammarDecisionsInSupportedFixtures() throws IOException {
        final Map<Integer, Set<Integer>> coveredAlternativesByDecision = new HashMap<>();
        final Set<Integer> lexedTokenTypes = new HashSet<>();
        final SysMLParser grammar = new SysMLParser(new CommonTokenStream(
                new SysMLLexer(CharStreams.fromString(""))));
        final List<GrammarInput> inputs = discoverGrammarInputs(grammar);

        int accepted = 0;
        int acceptedPrefix = 0;
        int skipped = 0;
        for (final GrammarInput input : inputs) {
            final CommonTokenStream tokens = new CommonTokenStream(new SysMLLexer(
                    CharStreams.fromString(
                            Files.readString(input.path(), StandardCharsets.UTF_8))));
            final Map<Integer, Set<Integer>> fixtureAlternativesByDecision = new HashMap<>();
            final RecordingInterpreter interpreter = new RecordingInterpreter(
                    grammar, tokens, fixtureAlternativesByDecision);
            interpreter.setBuildParseTree(false);
            interpreter.setErrorHandler(new BailErrorStrategy());
            interpreter.removeErrorListeners();
            boolean parsed = false;
            try {
                interpreter.parse(input.ruleIndex());
                parsed = interpreter.getNumberOfSyntaxErrors() == 0;
            } catch (final ParseCancellationException exception) {
                // Parser fixtures include intentionally invalid syntax.
            }
            final boolean consumed = parsed && tokens.LA(1) == Token.EOF;
            if (input.required()) {
                assertTrue(consumed, () -> "Unparsed serializer fixture "
                        + input.path() + " at " + tokens.LT(1));
            }
            if (consumed) {
                accepted++;
                tokens.fill();
                tokens.getTokens().stream().map(Token::getType)
                        .filter(type -> type > 0).forEach(lexedTokenTypes::add);
                fixtureAlternativesByDecision.forEach((decision, hits) -> coveredAlternativesByDecision
                        .computeIfAbsent(decision, ignored -> new HashSet<>())
                        .addAll(hits));
            } else if (parsed && input.allowPrefix()) {
                acceptedPrefix++;
            } else {
                skipped++;
            }
        }

        final List<String> missing = grammar.getATN().decisionToState.stream()
                .flatMap(state -> IntStream
                        .rangeClosed(1, state.getNumberOfTransitions())
                        .filter(alt -> !coveredAlternativesByDecision
                                .getOrDefault(state.decision, Set.of())
                        .contains(alt))
                        .mapToObj(alt -> grammar.getRuleNames()[state.ruleIndex]
                                + " state " + state.stateNumber + " decision "
                                + state.decision + " alternative " + alt))
                .toList();
        final int acceptedCount = accepted;
        final int prefixCount = acceptedPrefix;
        final int skippedCount = skipped;
        assertTrue(missing.isEmpty(), () -> acceptedCount + " complete of "
                + inputs.size() + " SysML inputs (" + prefixCount
                + " parser-rule prefixes ignored, " + skippedCount
                + " rejected) leave " + missing.size()
                + " grammar alternatives uncovered:\n"
                + String.join("\n", missing.subList(0,
                        Math.min(80, missing.size()))));
        final List<String> missingTokens = IntStream
                .rangeClosed(1, SysMLLexer.VOCABULARY.getMaxTokenType())
                .filter(type -> type != SysMLLexer.WS)
                .filter(type -> !lexedTokenTypes.contains(type))
                .mapToObj(type -> type + " "
                        + SysMLLexer.VOCABULARY.getDisplayName(type))
                .toList();
        assertTrue(missingTokens.isEmpty(), () -> "Unwitnessed lexer token types: "
                + String.join(", ", missingTokens));
    }

    private static List<GrammarInput> discoverGrammarInputs(final SysMLParser grammar) throws IOException {
        final List<GrammarInput> inputs = new ArrayList<>();
        sysmlFiles(SERIALIZER_RESOURCES).stream()
                .filter(path -> !path.startsWith(SERIALIZER_RESOURCES.resolve("fragments")))
                .forEach(path -> inputs.add(new GrammarInput(path, SysMLParser.RULE_rootNamespace, true, false)));
        for (final var fixture : SerializerRoundTripTestSupport.serializerFragmentFixtures().toList()) {
            final int ruleIndex = Arrays.asList(grammar.getRuleNames()).indexOf(fixture.grammarRuleName());
            assertTrue(ruleIndex >= 0, () -> "Unknown parser rule " + fixture.grammarRuleName());
            inputs.add(new GrammarInput(fixture.sysmlPath(), ruleIndex, true, false));
        }
        // The interpreter skips parser actions, including those that report recovery diagnostics.
        // These inputs exercise recovery decisions without entering the serializer round-trip corpus.
        sysmlFiles(GRAMMAR_RECOVERY_FIXTURES)
                .forEach(path -> inputs.add(new GrammarInput(path, SysMLParser.RULE_rootNamespace, true, false)));
        sysmlFiles(PARSER_RESOURCES.resolve("rootNamespace"))
                .forEach(path -> inputs.add(new GrammarInput(path,
                        SysMLParser.RULE_rootNamespace, false, false)));
        for (final ParserRuleFolder folder : ParserRuleFixtureDiscovery
                .discover(PARSER_TEST_SOURCES)) {
            final String parseMethodName = folder.parseMethod();
            if (!"parseRootNamespace".equals(parseMethodName)) {
                final String ruleName = Character.toLowerCase(parseMethodName.charAt(5))
                        + parseMethodName.substring(6);
                final int ruleIndex = Arrays.asList(grammar.getRuleNames())
                        .indexOf(ruleName);
                assertTrue(ruleIndex >= 0, () -> "Unknown parser rule " + ruleName);
                sysmlFiles(PARSER_RESOURCES.resolve(folder.resourceFolder()))
                        .forEach(path -> inputs.add(
                                new GrammarInput(path, ruleIndex, false, true)));
            }
        }
        return inputs;
    }

    private static List<Path> sysmlFiles(final Path root) throws IOException {
        try (Stream<Path> paths = Files.walk(root)) {
            return paths.filter(path -> path.toString().endsWith(".sysml"))
                    .sorted().toList();
        }
    }

    /** A source file and the parser rule used to read it. */
    private record GrammarInput(Path path, int ruleIndex, boolean required,
            boolean allowPrefix) {
    }

    /** Records selected alternatives into the supplied mutable map, including choices made before a parse fails. */
    private static final class RecordingInterpreter extends ParserInterpreter {

        private final Map<Integer, Set<Integer>> alternatives;

        RecordingInterpreter(final SysMLParser grammar,
                final CommonTokenStream tokens) {
            this(grammar, tokens, new HashMap<>());
        }

        RecordingInterpreter(final SysMLParser grammar,
                final CommonTokenStream tokens,
                final Map<Integer, Set<Integer>> alternatives) {
            super(grammar.getGrammarFileName(), grammar.getVocabulary(),
                    Arrays.asList(grammar.getRuleNames()), grammar.getATN(),
                    tokens);
            this.alternatives = alternatives;
        }

        @Override
        protected int visitDecisionState(final DecisionState state) {
            final int alternative = super.visitDecisionState(state);
            this.alternatives.computeIfAbsent(state.decision,
                    ignored -> new HashSet<>()).add(alternative);
            return alternative;
        }
    }
}
