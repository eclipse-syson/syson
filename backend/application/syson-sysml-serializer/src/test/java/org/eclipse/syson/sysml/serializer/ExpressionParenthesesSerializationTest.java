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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Preserves expression grouping and omits unnecessary parentheses.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class ExpressionParenthesesSerializationTest {

    private static final String WHITESPACE_REGEX = "\\s+";

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * Serialization must not add unnecessary parentheses after not around the tested feature chain and invocation.
     */
    @Test
    void omitsUnnecessaryParenthesesAfterNot() throws Exception {
        final String source = "not featureConfiguration.suboccurrences->includes(f)";
        final var parseResult = new SysMLModelParser().parseOwnedExpression(
                new ByteArrayInputStream(
                        source.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);

        final String serialized = new SysMLSerializer().serialize(parseResult);

        assertEquals(source, serialized.strip());
    }

    /**
     * A conditional branch that merely contains another conditional must be exported whole, not reduced to the nested
     * conditional.
     */
    @Test
    void preservesConditionalBranchContainingNestedConditional() throws Exception {
        final String source = "if c ? (if d ? 1 else 2) + 3 else 0";
        final var parseResult = new SysMLModelParser().parseOwnedExpression(
                new ByteArrayInputStream(
                        source.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);

        final String serialized = new SysMLSerializer().serialize(parseResult);

        assertEquals(source, serialized.strip());
    }

    /**
     * Grouping must come from operator precedence when no parser adapter records it, as for any model reloaded from
     * SysON storage: dropping these parentheses would change the meaning of the expression.
     */
    @ParameterizedTest
    @ValueSource(strings = { SysMLTokens.CONJUGATION_OPERATOR + "(a * b)", "+(a * b)", "-(a * b)", "-(a .. b)", "(a + b).c", "(a or b)->size()", "(a == b) as Boolean",
        "-(a * b) ^ c" })
    void parenthesizesByPrecedenceWithoutParserAdapters(final String source) throws Exception {
        final var parseResult = new SysMLModelParser().parseOwnedExpression(
                new ByteArrayInputStream(
                        source.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);
        parseResult.model().eAdapters().removeIf(ITextAdapter.class::isInstance);
        parseResult.model().eAllContents().forEachRemaining(
                object -> object.eAdapters().removeIf(ITextAdapter.class::isInstance));

        final String serialized = new SysMLSerializer().serialize(parseResult);

        assertEquals(source, serialized.strip());
    }

    /**
     * Serialization must retain the parentheses in not (a and b).
     */
    @Test
    void preservesRequiredParenthesesAfterNot() throws Exception {
        final String source = "not (a and b)";
        final var parseResult = new SysMLModelParser().parseOwnedExpression(
                new ByteArrayInputStream(
                        source.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);

        final String serialized = new SysMLSerializer().serialize(parseResult);

        assertEquals(source, serialized.strip());
    }

    /**
     * Without ITextAdapter instances, serialization must reconstruct the expected parentheses for all ten
     * operator-grouping examples.
     */
    @ParameterizedTest
    @ValueSource(strings = { "(1 + 2) % 3", "1 % (2 * 3)", "1 % (2 % 3)", "1 + (2 - 3)",
        "(true or false) xor true", "true implies (false implies true)", "(true ?? false) implies true",
        "(1 == 2) < 3", "(1 .. 2) + 3", "(2 ** 3) ** 4" })
    void preservesOperatorGroupingWithoutTextAdapters(final String source) throws IOException {
        final var parsed = new SysMLModelParser().parseOwnedExpression(
                new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        assertEquals(0, parsed.diagnostic().getSeverity());
        parsed.model().eAdapters().removeIf(ITextAdapter.class::isInstance);
        parsed.model().eAllContents().forEachRemaining(object -> object.eAdapters().removeIf(ITextAdapter.class::isInstance));

        final String serialized = new SysMLSerializer().serialize(parsed);

        assertEquals(source.replaceAll(WHITESPACE_REGEX, ""), serialized.replaceAll(WHITESPACE_REGEX, ""));
    }
}
