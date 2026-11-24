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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.ParserRuleFixture;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Exercises grammar precedence independently of the serializer's precedence table. Every expression form is placed
 * in every expression operand position; type-name positions are deliberately excluded because they are not operands.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class OperatorPrecedenceMatrixTest {

    // Keep the alternatives of KerMLExpressions.g4 visible here: sharing the implementation's table would hide omissions.
    private static final List<String> BINARY_OPERATORS = List.of("??", "implies", "or", "|", "xor", "and", "&",
            "==", "!=", "===", "!==", "<", ">", "<=", ">=", "..", "+", "-", "*", "/", "%", "**", "^");

    private static final List<String> UNARY_OPERATORS = List.of("not", "+", "-", "~");

    private static final List<String> CLASSIFICATION_OPERATORS = List.of("istype", "hastype", "as", "@");

    @TempDir
    private Path temporaryDirectory;

    /** Marked output must retain the complete source, and unmarked output must preserve model and reference order. */
    @ParameterizedTest(name = "{0} in {1}")
    @MethodSource("operandPositions")
    void preservesEveryOperandPosition(final String operand, final String position) throws IOException {
        final String source = position.replace("$", "(" + operand + ")");
        final Path path = this.temporaryDirectory.resolve("expression.sysml");
        Files.writeString(path, source);
        final ParserRuleFixture fixture = new ParserRuleFixture("parseOwnedExpression", path);
        assertWhitespaceOnlyRoundTrip(fixture, new SysMLModelParser(), new SysMLSerializer());
        assertSemanticRoundTripWithoutTextAdapters(fixture, new SysMLModelParser(), new SysMLSerializer());
    }

    private static Stream<Arguments> operandPositions() {
        final List<String> operands = Stream.of(
                BINARY_OPERATORS.stream().map(operator -> "a " + operator + " b"),
                UNARY_OPERATORS.stream().map(operator -> operator + " a"),
                CLASSIFICATION_OPERATORS.stream().map(operator -> "a " + operator + " T"),
                Stream.of("A @@ T", "A meta T", "all T", "if c ? a else b", "a.b", "a#(i)", "a[u]",
                        "a->f(b)", "a.{b}", "a.?{b}", "f(a)", "new T()", "{in x; f(x)}", "a, b"))
                .flatMap(stream -> stream).toList();
        final List<String> positions = Stream.of(
                BINARY_OPERATORS.stream().flatMap(operator -> Stream.of("$ " + operator + " z", "z " + operator + " $")),
                UNARY_OPERATORS.stream().map(operator -> operator + " $"),
                CLASSIFICATION_OPERATORS.stream().map(operator -> "$ " + operator + " T"),
                Stream.of("if $ ? y else z", "if c ? $ else z", "if c ? y else $", "$.v", "$#(i)", "a#($)",
                        "$[u]", "a[$]", "$->f(z)", "a->f($)", "$.{z}", "$.?{z}", "f($)", "new T(x = $)",
                        "($, z)", "(z, $)"))
                .flatMap(stream -> stream).toList();
        return operands.stream().flatMap(operand -> positions.stream().map(position -> Arguments.of(operand, position)));
    }
}
