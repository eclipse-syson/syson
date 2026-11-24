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

import static org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.assertSemanticRoundTripWithoutTextAdapters;
import static org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.assertWhitespaceOnlyRoundTrip;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.ParserRuleFixture;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Preserves enclosing expressions, argument ownership and comments in nested expression syntax.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class ExpressionStructureSerializationTest {

    /** Each expression must retain its complete syntax, including comments, in round-trip mode. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("fixtures")
    void preservesExpressionText(final ParserRuleFixture fixture) throws IOException {
        assertWhitespaceOnlyRoundTrip(fixture, new SysMLModelParser(), new SysMLSerializer());
    }

    /** Normalized output without syntax adapters must reparse into the same expression structure and references. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("fixtures")
    void preservesExpressionModel(final ParserRuleFixture fixture) throws IOException {
        assertSemanticRoundTripWithoutTextAdapters(fixture, new SysMLModelParser(), new SysMLSerializer());
    }

    private static Stream<ParserRuleFixture> fixtures() {
        final Path resources = Path.of("src/test/resources");
        final Stream<ParserRuleFixture> documents = Stream.of(
                "actions/when-trigger-function-call",
                "requirements/satisfy-type-same-spelling-as-subject",
                "connections/repeated-flow-segments",
                "connections/end-reference-body-multiplicity",
                "connections/anonymous-body-reference-multiplicity",
                "expressions/qualified-call-nested-chains",
                "declarations/variant-chain-with-body",

                "expressions/multiple-leading-comments",
                "expressions/cast-nested-grouping",
                "expressions/sequence-nested-trailing-comma",
                "expressions/feature-value-nested-body",
                "expressions/ungrouped-lambda-cast",
                "expressions/default-while-test-name",
                "expressions/lambda-grouped-argument",
                "expressions/commented-additive-operand",
                "expressions/repeated-quoted-path-segments",
                "expressions/nested-constructor-named-argument",
                "expressions/navigation-named-arguments",
                "expressions/lambda-following-argument",
                "expressions/multiple-lambda-parameters",
                "expressions/function-body-second-argument",
                "expressions/commented-invocation-argument",
                "expressions/commented-left-operand",
                "expressions/commented-parenthesized-operand",
                "expressions/leading-invocation-comment",
                "expressions/commented-navigation-receiver",
                "expressions/commented-navigation-function",
                "expressions/commented-path-member",
                "expressions/repeated-comment-text",
                "expressions/standalone-untyped-body-parameter",
                "expressions/subsetted-lambda-parameter")
                .map(name -> ParserRuleFixture.document(resources.resolve("testcases/" + name + ".sysml")));
        return Stream.concat(documents, SerializerRoundTripTestSupport.serializerFragmentFixtures());
    }
}
