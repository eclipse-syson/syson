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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Preserves SysML declarations, comments, and action syntax across serialization.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class SysMLSyntaxPreservationTest {

    private static final String WHITESPACE_REGEX = "\\s+";

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /** A comment before a sequence operand must preserve both its value and its position, without duplicating it. */
    @ParameterizedTest
    @ValueSource(strings = { "(1, //* note */ 2)", "(//* note */ 1, 2)", "(1, // note\n2)", "(// note\n1, 2)",
        "(1, // note\r2)", "(1, // note\r\n2)" })
    void preservesCommentedSequenceOperands(final String expression) throws IOException {
        final String source = "attribute x = " + expression + ";";
        final var result = parseRootNamespace(source);
        assertEquals(Diagnostic.OK, result.diagnostic().getSeverity());

        for (final SerializationMode mode : SerializationMode.values()) {
            final String text = new SysMLSerializer().serialize(result, SerializationOptions.defaults().withMode(mode));

            assertEquals(source.replaceAll(WHITESPACE_REGEX, ""), text.replaceAll(WHITESPACE_REGEX, ""), mode.toString());
        }
    }

    /**
     * A trailing line comment after a use-case actor must remain in the serialized text.
     */
    @Test
    void preservesTrailingCommentOfCaseFeatureMembership() throws Exception {
        final String source = "use case def U { actor user; // actor comment\n}";
        final var parseResult = parseRootNamespace(source);
        final String serialized = new SysMLSerializer().serialize(parseResult);

        assertEquals(source.replaceAll(WHITESPACE_REGEX, ""),
                serialized.replaceAll(WHITESPACE_REGEX, ""));
    }

    /**
     * A trailing line comment after an attribute declaration must remain in the serialized text.
     */
    @Test
    void preservesTrailingCommentOfGenericUsage() throws Exception {
        final String source = "part def P { attribute a = 1; // attribute comment\n}";
        final var parseResult = parseRootNamespace(source);

        final String serialized = new SysMLSerializer().serialize(parseResult);

        assertEquals(source.replaceAll(WHITESPACE_REGEX, ""),
                serialized.replaceAll(WHITESPACE_REGEX, ""));
    }

    /**
     * A transition source written without the first keyword must remain written without that keyword.
     */
    @Test
    void preservesTransitionSourceWithoutFirstKeyword() throws Exception {
        final String source = "state s { transition start then off; }";
        final var parseResult = parseRootNamespace(source);

        final String serialized = new SysMLSerializer().serialize(parseResult);

        assertEquals(source.replaceAll(WHITESPACE_REGEX, ""),
                serialized.replaceAll(WHITESPACE_REGEX, ""));
    }

    /**
     * A reference usage with both typing and redefinition must retain both clauses.
     */
    @Test
    void preservesTypedRedefinedReferenceUsage() throws Exception {
        final String source = "item def F { ref endpoint; } "
                + "item def U { ref link : F :>> endpoint; }";
        final var parseResult = parseRootNamespace(source);

        final String serialized = new SysMLSerializer().serialize(parseResult);

        assertEquals(source.replaceAll(WHITESPACE_REGEX, ""),
                serialized.replaceAll(WHITESPACE_REGEX, ""));
    }

    /**
     * A declaration whose leading clauses impose a specific layout (subsetting before typing, or an anonymous
     * reference subsetting) must not lose the clauses that layout does not order itself.
     */
    @ParameterizedTest
    @ValueSource(strings = {
        "part def T; part a; part b; part def X { part p :> a : T :>> b; }",
        "part def T; part r; part s; part def X { part ::> r : T :> s; }",
    })
    void preservesEverySpecializationClause(final String source) throws Exception {
        final String serialized = new SysMLSerializer().serialize(parseRootNamespace(source));

        assertEquals(source.replaceAll(WHITESPACE_REGEX, ""),
                serialized.replaceAll(WHITESPACE_REGEX, ""));
    }

    /**
     * A usage that references its target instead of declaring a name must keep that target when its body holds feature
     * chains: those chains belong to the body members, not to the reference.
     */
    @ParameterizedTest
    @ValueSource(strings = {
        "requirement r { frame cost { redefines p = s.p.v; } }",
        "requirement r { frame concerns.cost { redefines p = s.p.v; } }",
        "requirement r { require cost { redefines p = s.p.v; } }",
        "part x { perform run { redefines p = s.p.v; } }",
        "part x { exhibit on { redefines p = s.p.v; } }",
    })
    void preservesReferencedTargetBeforeBodyFeatureChains(final String source) throws Exception {
        final String serialized = new SysMLSerializer().serialize(parseRootNamespace(source));

        assertEquals(source.replaceAll(WHITESPACE_REGEX, ""),
                serialized.replaceAll(WHITESPACE_REGEX, ""));
    }

    /**
     * A transition effect must be exported for every action form the grammar allows after {@code do}, including an
     * accept action, which must not be mistaken for the trigger, and a named send action.
     */
    @ParameterizedTest
    @ValueSource(strings = {
        "state def S { state s1; state s2; transition t first s1 accept E do accept F then s2; }",
        "state def S { attribute x; state s1; state s2; transition t first s1 accept E do assign x := 1 then s2; }",
        "state def S { state s1; state s2; transition t first s1 accept E do action s send x to y then s2; }",
    })
    void preservesTransitionEffect(final String source) throws Exception {
        final String serialized = new SysMLSerializer().serialize(parseRootNamespace(source));

        assertEquals(source.replaceAll(WHITESPACE_REGEX, ""),
                serialized.replaceAll(WHITESPACE_REGEX, ""));
    }

    /**
     * A standalone {@code EffectBehaviorMember} must keep the form of its performed action: an assignment, an accept
     * or a perform must not be rewritten as a send, and no statement terminator may be added, since the grammar has
     * none after an effect.
     */
    @ParameterizedTest
    @ValueSource(strings = {
        "do assign x := 1",
        "do accept E",
        "do send E via p to q",
        "do a.b",
        "do a.b { in x : V; }",
        "do action d : D { in x : V; }",
        "do action { action n; }",
    })
    void preservesEffectBehaviorMemberFragment(final String source) throws Exception {
        final var parseResult = new SysMLModelParser().parseEffectBehaviorMember(
                new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        assertEquals(Diagnostic.OK, parseResult.diagnostic().getSeverity());

        final String serialized = new SysMLSerializer().serialize(parseResult,
                SerializationOptions.defaults().withParserEntryPoint("parseEffectBehaviorMember"));

        assertEquals(source.replaceAll(WHITESPACE_REGEX, ""),
                serialized.replaceAll(WHITESPACE_REGEX, ""));
    }

    /**
     * Owned multiplicities [1] and [0..*] must remain attached to their declarations in the serialized text.
     */
    @Test
    void preservesOwnedMultiplicities() throws Exception {
        final String source = "part def Assembly { part single[1]; "
                + "part many[0..*]; }";
        final var parseResult = parseRootNamespace(source);

        final String serialized = new SysMLSerializer().serialize(parseResult);

        assertEquals(source.replaceAll(WHITESPACE_REGEX, ""),
                serialized.replaceAll(WHITESPACE_REGEX, ""));
    }

    /**
     * The first one then two construction must retain both endpoints and its succession keywords.
     */
    @Test
    void preservesFirstMembershipBeforeSuccession() throws Exception {
        final String source = "action def A { action one; action two; "
                + "first one then two; }";
        final var parseResult = parseRootNamespace(source);

        final String serialized = new SysMLSerializer().serialize(parseResult);

        assertEquals(source.replaceAll(WHITESPACE_REGEX, ""),
                serialized.replaceAll(WHITESPACE_REGEX, ""));
    }

    /**
     * Typed assertions with or without not must be preserved, and argument comments must remain in calls with or
     * without a receiver.
     */
    @Test
    void preservesTypedBareAssertionsAndReceiverArgumentComments() throws Exception {
        for (final String source : List.of("assert c : C;", "assert not c : C;",
                "attribute a = x->f(1//* comment */ , 2);",
                "attribute a = f(1//* comment */ , 2);")) {
            final var parsed = parseRootNamespace(source);
            assertEquals(0, parsed.diagnostic().getSeverity());
            final String text = new SysMLSerializer().serialize(parsed,
                    SerializationOptions.defaults().withMode(SerializationMode.ROUND_TRIP));
            assertEquals(source, text.replaceAll(WHITESPACE_REGEX, " ").trim());
        }
    }

    /**
     * Inline succession targets must remain in the output after descendant text adapters are removed, and the output
     * must remain parseable.
     */
    @Test
    void preservesInlineSuccessionTargetsWithoutTextAdapters() throws Exception {
        for (final String source : List.of(
                "part vehicle { timeslice parked; then timeslice moving; }",
                "action travel { action startEngine; then action drive; }")) {
            final var parser = new SysMLModelParser();
            final var parsed = parser.parseRootNamespace(new ByteArrayInputStream(
                    source.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
            assertEquals(0, parsed.diagnostic().getSeverity());
            // Native server persistence does not retain the parser's syntax adapters.
            parsed.model().eAllContents().forEachRemaining(object -> object.eAdapters().removeIf(ITextAdapter.class::isInstance));

            final String text = new SysMLSerializer().serialize(parsed.model());
            final var reparsed = parser.parseRootNamespace(new ByteArrayInputStream(
                    text.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

            assertEquals(0, reparsed.diagnostic().getSeverity(), text);
            assertEquals(source, text.replaceAll(WHITESPACE_REGEX, " ").trim());
        }
    }

    /**
     * All 27 syntax examples must retain their non-whitespace characters, including locales, sends, transitions,
     * actions, expressions, and calls with varied user-defined names.
     */
    @ParameterizedTest
    @ValueSource(strings = {
        "comment locale \"\" /*Empty locale */",
        "doc locale \"en_US\" /*Documentation */",
        "comment locale \"en\\\"US\" /*Escaped locale */",
        "action a { send signal to controlPort; }",
        "action a { send signal via channel; }",
        "action a { send signal via channel to controlPort; }",
        "state s { entry send signal to controlPort; }",
        "state s { entry send signal via channel; }",
        "state s { transition initial then preparation; }",
        "state s { transition first initial then ready; }",
        "state s { transition first initial then preparation; }",
        "state s { do action work { first start; then action step; then done; } }",
        "state s { do action workOperations { first start; then action step; then done; } }",
        "state s { do action workOperations { first start; then action step; then finish; } }",
        "attribute a = x and custom(y);",
        "attribute a = if condition ? custom(y) else z;",
        "attribute a = if condition ? if other ? custom(y) else z else w;",
        "attribute a = x and custom(y + z);",
        "attribute a = x->Custom(y);",
        "attribute a = x->allTrue();",
        "attribute a = forAll(x);",
        "attribute a = x->exists{ in v : T; v };",
        "attribute a = x->custom{ v : T; v };",
        "attribute a = x->forAll{ v : T; v };",
        "attribute a = x->forAll({ in v : T; v });",
        "attribute a = x->custom{ in v : T; private attribute local = v; local };",
        "attribute a = x->forAll{ in v : T; private attribute local = v; local };"
    })
    void preservesSyntaxRegardlessOfNames(final String source) throws Exception {
        final var parsed = new SysMLModelParser().parseRootNamespace(
                new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);
        assertTrue(parsed.diagnostic().getSeverity() < Diagnostic.ERROR,
                () -> parsed.diagnostic().toString());

        final String serialized = new SysMLSerializer().serialize(parsed);

        assertEquals(source.replaceAll("\\s+", ""),
                serialized.replaceAll("\\s+", ""));
    }

    /**
     * With the default options, which elide implicit reference keywords, a succession target written as the name of
     * the next declaration must keep its end multiplicity ({@code then [2] b;}).
     */
    @Test
    void preservesSuccessionTargetMultiplicityWithDefaultOptions() throws Exception {
        final String source = Files.readString(
                Path.of("src/test/resources/testcases/actions/succession-target-multiplicity.sysml"), StandardCharsets.UTF_8);
        final var parsed = parseRootNamespace(source);

        final String serialized = new SysMLSerializer().serialize(parsed, SerializationOptions.defaults());

        assertEquals(source.replaceAll(WHITESPACE_REGEX, ""),
                serialized.replaceAll(WHITESPACE_REGEX, ""));
    }

    /**
     * A succession end multiplicity must be separated from its endpoint by a space ({@code first [1] a then [2] b}),
     * not glued to it ({@code [1]a}), in both serialization modes.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void separatesSuccessionEndMultiplicityFromEndpoint(final SerializationMode mode) throws Exception {
        final String source = "action def A { action a; action b; first [1] a then [2] b; succession s first [1] a then [2] b; }";
        final var parsed = parseRootNamespace(source);

        final String serialized = new SysMLSerializer().serialize(parsed, SerializationOptions.defaults().withMode(mode));

        assertEquals(source, serialized.replaceAll(WHITESPACE_REGEX, " ").trim());
    }

    private static ISysMLParseResult<Namespace> parseRootNamespace(final String source) throws IOException {
        return SerializerRoundTripTestSupport.parseDocument(source);
    }
}
