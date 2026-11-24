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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.parser.internal.StructuralModelCompletionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.omg.sysml.lang.sysml.AttributeUsage;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.FeatureMembership;
import org.omg.sysml.lang.sysml.FeatureValue;
import org.omg.sysml.lang.sysml.FlowUsage;
import org.omg.sysml.lang.sysml.LiteralInteger;
import org.omg.sysml.lang.sysml.LiteralRational;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.MultiplicityRange;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.OperatorExpression;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.Package;
import org.omg.sysml.lang.sysml.PartDefinition;
import org.omg.sysml.lang.sysml.PartUsage;
import org.omg.sysml.lang.sysml.ReturnParameterMembership;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.TransitionFeatureKind;
import org.omg.sysml.lang.sysml.TransitionFeatureMembership;
import org.omg.sysml.lang.sysml.TransitionUsage;
import org.omg.sysml.lang.sysml.Usage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Verifies the text written for model states that only programmatic edits can produce and that the grammar cannot
 * express as such. The serializer must not throw, must prefer text that reparses to an equivalent or semantically
 * closest model, and must never write text that silently reparses into an unrelated model.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class EditedModelStateSerializationTest {

    private static final String WHITESPACE = "\\s+";

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /** Enumeration keyword omission is valid only while the value is in its enumeration definition. */
    @ParameterizedTest
    @MethodSource("enumerationMoves")
    void preservesEnumerationMovedOutOfDefinition(final SerializationMode mode, final String source) throws IOException {
        final var parsed = parse(source);
        final Element value = elements(parsed.model()).filter(element -> "v".equals(element.getDeclaredName())).findFirst().orElseThrow();
        final Element replacement = elements(parsed.model()).filter(element -> "replacement".equals(element.getDeclaredName())).findFirst().orElseThrow();
        final var oldMembership = value.getOwningRelationship();
        oldMembership.getOwningRelatedElement().getOwnedRelationship().remove(oldMembership);
        replacement.getOwningRelationship().getOwnedRelatedElement().set(0, value);
        assertEditedModelRoundTrip(parsed, mode);
    }

    private static Stream<Arguments> enumerationMoves() {
        return Stream.of(SerializationMode.values()).flatMap(mode -> Stream.of(
                Arguments.of(mode, "enum def E { v; } enum replacement;"),
                Arguments.of(mode, "enum def E { v; container { part p { enum replacement; } } }")));
    }

    /** A first membership moved and named as an alias must keep its alias identification. */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void preservesFirstMembershipConvertedToAlias(final SerializationMode mode) throws IOException {
        final var parsed = parse("action a { first target; }");
        final Membership membership = elements(parsed.model()).filter(Membership.class::isInstance).map(Membership.class::cast)
                .filter(candidate -> candidate.eAdapters().contains(ITextAdapter.FIRST_MEMBERSHIP)).findFirst().orElseThrow();
        membership.setMemberName("edited");
        parsed.model().getOwnedRelationship().add(membership);
        // Alias targets accept any Element, whereas first targets are restricted to Feature by the grammar.
        final String expectedLinks = SerializerRoundTripTestSupport.canonicalDanglingLinks(parsed).replace(" : Feature ->", " : Element ->");
        assertEditedModelRoundTrip(parsed, mode, expectedLinks);
    }

    /** Flow-end structure must take priority over an old message spelling after a model edit. */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void preservesFlowEndsAddedToMessage(final SerializationMode mode) throws IOException {
        final var parsed = parse("message m; flow a to b;");
        final List<FlowUsage> flows = elements(parsed.model()).filter(FlowUsage.class::isInstance)
                .map(FlowUsage.class::cast).toList();
        flows.getFirst().getOwnedRelationship().addAll(List.copyOf(flows.getLast().getOwnedRelationship()));
        parsed.model().getOwnedRelationship().remove(flows.getLast().getOwningRelationship());
        assertEditedModelRoundTrip(parsed, mode);
    }

    private static void assertEditedModelRoundTrip(final ISysMLParseResult<Namespace> parsed, final SerializationMode mode) throws IOException {
        assertEditedModelRoundTrip(parsed, mode, SerializerRoundTripTestSupport.canonicalDanglingLinks(parsed));
    }

    private static void assertEditedModelRoundTrip(final ISysMLParseResult<Namespace> parsed, final SerializationMode mode,
            final String expectedLinks) throws IOException {
        final String expectedModel = SerializerRoundTripTestSupport.toCanonicalXmi(parsed.model(), "expected.sysml");
        if (mode == SerializationMode.NORMALIZED) {
            elements(parsed.model()).forEach(candidate -> candidate.eAdapters().removeIf(ITextAdapter.class::isInstance));
        }
        final String text = new SysMLSerializer().serialize(parsed, SerializationOptions.defaults().withMode(mode));
        final var reparsed = parse(text);
        assertEquals(Diagnostic.OK, reparsed.diagnostic().getSeverity(), text);
        assertEquals(expectedLinks, SerializerRoundTripTestSupport.canonicalDanglingLinks(reparsed), text);
        assertEquals(expectedModel, SerializerRoundTripTestSupport.toCanonicalXmi(reparsed.model(), "actual.sysml"), text);
    }

    /** A formerly default branch must write an added guard, with or without its old syntax markers. */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void preservesGuardAddedToDefaultBranch(final SerializationMode mode) throws IOException {
        final var parsed = parse("action a { then decide; if true then b; else c; }");
        final List<TransitionUsage> transitions = elements(parsed.model()).filter(TransitionUsage.class::isInstance)
                .map(TransitionUsage.class::cast).toList();
        final TransitionFeatureMembership guard = transitions.stream().flatMap(transition -> transition.getOwnedRelationship().stream())
                .filter(TransitionFeatureMembership.class::isInstance).map(TransitionFeatureMembership.class::cast)
                .filter(membership -> membership.getKind() == TransitionFeatureKind.GUARD).findFirst().orElseThrow();
        transitions.getLast().getOwnedRelationship().add(0, EcoreUtil.copy(guard));
        final String expectedLinks = SerializerRoundTripTestSupport.canonicalDanglingLinks(parsed);
        if (mode == SerializationMode.NORMALIZED) {
            elements(parsed.model()).forEach(element -> element.eAdapters().removeIf(ITextAdapter.class::isInstance));
        }
        final String text = new SysMLSerializer().serialize(parsed, SerializationOptions.defaults().withMode(mode));
        final var reparsed = parse(text);
        assertEquals(Diagnostic.OK, reparsed.diagnostic().getSeverity(), text);
        assertEquals(expectedLinks, SerializerRoundTripTestSupport.canonicalDanglingLinks(reparsed), text);
        assertEquals(2, elements(reparsed.model()).filter(TransitionFeatureMembership.class::isInstance)
                .map(TransitionFeatureMembership.class::cast).filter(membership -> membership.getKind() == TransitionFeatureKind.GUARD).count(), text);
    }

    /** Adding identification must invalidate a reference shorthand even when its parser markers are retained. */
    @ParameterizedTest(name = "{0} ({1})")
    @MethodSource("identifiedReferences")
    void preservesAddedIdentification(final String source, final String metaclass) throws IOException {
        for (final SerializationMode mode : SerializationMode.values()) {
            for (final boolean shortName : List.of(false, true)) {
                final var parsed = parse(source);
                final Element element = elements(parsed.model()).filter(candidate -> candidate.eClass().getName().equals(metaclass))
                        .findFirst().orElseThrow();
                if (shortName) {
                    element.setDeclaredShortName("edited");
                } else {
                    element.setDeclaredName("edited");
                }
                final String expectedLinks = SerializerRoundTripTestSupport.canonicalDanglingLinks(parsed);
                final String expectedModel = SerializerRoundTripTestSupport.toCanonicalXmi(parsed.model(), "expected.sysml");
                if (mode == SerializationMode.NORMALIZED) {
                    elements(parsed.model()).forEach(candidate -> candidate.eAdapters().removeIf(ITextAdapter.class::isInstance));
                }
                final String text = new SysMLSerializer().serialize(parsed, SerializationOptions.defaults().withMode(mode));
                final var reparsed = parse(text);
                assertEquals(Diagnostic.OK, reparsed.diagnostic().getSeverity(), text);
                assertEquals(expectedLinks, SerializerRoundTripTestSupport.canonicalDanglingLinks(reparsed), text);
                assertEquals(expectedModel, SerializerRoundTripTestSupport.toCanonicalXmi(reparsed.model(), "actual.sysml"), text);
            }
        }
    }

    private static Stream<Arguments> identifiedReferences() {
        return Stream.of(Arguments.of("satisfy R;", "SatisfyRequirementUsage"),
                Arguments.of("event target;", "EventOccurrenceUsage"),
                Arguments.of("requirement def R { require C; }", "ConstraintUsage"));
    }

    /** A stale default-value marker must not override the current default and initial flags. */
    @ParameterizedTest(name = "{0} -> default={1}, initial={2}")
    @MethodSource("editedValueFlags")
    void respectsEditedValueFlags(final String sourceOperator, final boolean defaultValue, final boolean initialValue) throws IOException {
        for (final SerializationMode mode : SerializationMode.values()) {
            final var parsed = parse("attribute v " + sourceOperator + " 1;");
            final FeatureValue value = elements(parsed.model()).filter(FeatureValue.class::isInstance)
                    .map(FeatureValue.class::cast).findFirst().orElseThrow();
            value.setIsDefault(defaultValue);
            value.setIsInitial(initialValue);
            if (mode == SerializationMode.NORMALIZED) {
                elements(parsed.model()).forEach(candidate -> candidate.eAdapters().removeIf(ITextAdapter.class::isInstance));
            }
            final String text = new SysMLSerializer().serialize(parsed, SerializationOptions.defaults().withMode(mode));
            final FeatureValue reparsed = elements(parse(text).model()).filter(FeatureValue.class::isInstance)
                    .map(FeatureValue.class::cast).findFirst().orElseThrow();
            assertEquals(defaultValue, reparsed.isDefault(), text);
            assertEquals(initialValue, reparsed.isInitial(), text);
        }
    }

    private static Stream<Arguments> editedValueFlags() {
        return Stream.of("default", "default =", "default :=").flatMap(operator -> Stream.of(
                Arguments.of(operator, false, false), Arguments.of(operator, false, true),
                Arguments.of(operator, true, false), Arguments.of(operator, true, true)));
    }

    /** Changing a reference part to a composite must invalidate its retained ref prefix in both modes. */
    @ParameterizedTest
    @MethodSource("editedCompositeFlags")
    void respectsEditedCompositeFlag(final SerializationMode mode, final String source, final boolean composite) throws IOException {
        final var parsed = parse(source);
        final PartUsage part = elements(parsed.model()).filter(PartUsage.class::isInstance)
                .map(PartUsage.class::cast).findFirst().orElseThrow();
        part.setIsComposite(composite);
        if (mode == SerializationMode.NORMALIZED) {
            elements(parsed.model()).forEach(candidate -> candidate.eAdapters().removeIf(ITextAdapter.class::isInstance));
        }
        final String text = new SysMLSerializer().serialize(parsed, SerializationOptions.defaults().withMode(mode));
        final PartUsage reparsed = elements(parse(text).model()).filter(PartUsage.class::isInstance)
                .map(PartUsage.class::cast).findFirst().orElseThrow();
        assertEquals(composite, reparsed.isComposite(), text);
    }

    private static Stream<Arguments> editedCompositeFlags() {
        return Stream.of(SerializationMode.values()).flatMap(mode -> Stream.of(
                Arguments.of(mode, "ref part p;", true), Arguments.of(mode, "part p {}", false)));
    }

    /**
     * A definition or usage that is both abstract and variation must be written with {@code variation} only, since
     * {@code basicDefinitionPrefix} and {@code refPrefix} accept one of the two keywords, never both; a variation is
     * abstract by definition.
     */
    @Test
    void writesVariationAloneForAbstractVariations() throws IOException {
        final var result = parse("package P { part def A; part p; }");
        elements(result.model()).forEach(element -> {
            if (element instanceof final PartDefinition definition) {
                definition.setIsAbstract(true);
                definition.setIsVariation(true);
            } else if (element instanceof final PartUsage usage) {
                usage.setIsAbstract(true);
                usage.setIsVariation(true);
            }
        });

        final String text = new SysMLSerializer().serialize(result);

        assertEquals("package P { variation part def A; variation part p; }", oneLine(text));
        assertParses(text);
    }

    /**
     * A positive infinite real must be written as an overflowing real literal, which the parser reads back as the same
     * infinite {@code LiteralRational}. A NaN must be written as the grouped real division {@code (0.0 / 0.0)}, whose
     * value is NaN, and never as {@code NaN} or {@code Infinity}, which would reparse as references to unrelated
     * names.
     */
    @Test
    void writesNonFiniteRealsAsParseableExpressions() throws IOException {
        final var result = parse("package P { attribute a = 1.5; attribute b = 1.5; attribute c = 1.5; }");
        final List<LiteralRational> literals = elements(result.model())
                .filter(LiteralRational.class::isInstance)
                .map(LiteralRational.class::cast)
                .toList();
        literals.get(0).setValue(Double.POSITIVE_INFINITY);
        literals.get(1).setValue(Double.NEGATIVE_INFINITY);
        literals.get(2).setValue(Double.NaN);

        final String text = new SysMLSerializer().serialize(result);

        assertEquals("package P { attribute a = 1.0E309; attribute b = -1.0E309; attribute c = (0.0 / 0.0); }",
                oneLine(text));
        final List<Expression> values = featureValues(parse(text).model());
        assertEquals(Double.POSITIVE_INFINITY, ((LiteralRational) values.get(0)).getValue());
        final OperatorExpression negation = (OperatorExpression) values.get(1);
        assertEquals("-", negation.getOperator());
        assertEquals(Double.POSITIVE_INFINITY, operand(negation, LiteralRational.class).getValue());
        assertEquals("/", ((OperatorExpression) values.get(2)).getOperator());
    }

    /**
     * A negative integer or real has no literal spelling in the grammar: it must be written with a leading minus sign,
     * which reparses as the negation of the positive literal, the same value.
     */
    @Test
    void writesNegativeNumbersAsNegations() throws IOException {
        final var result = parse("package P { attribute a = 1.5; attribute b = 2; }");
        elements(result.model()).forEach(element -> {
            if (element instanceof final LiteralRational rational) {
                rational.setValue(-1.5);
            } else if (element instanceof final LiteralInteger integer) {
                integer.setValue(-2);
            }
        });

        final String text = new SysMLSerializer().serialize(result);

        assertEquals("package P { attribute a = -1.5; attribute b = -2; }", oneLine(text));
        final List<Expression> values = featureValues(parse(text).model());
        final OperatorExpression rationalNegation = (OperatorExpression) values.get(0);
        assertEquals("-", rationalNegation.getOperator());
        assertEquals(1.5, operand(rationalNegation, LiteralRational.class).getValue());
        final OperatorExpression integerNegation = (OperatorExpression) values.get(1);
        assertEquals("-", integerNegation.getOperator());
        assertEquals(2, operand(integerNegation, LiteralInteger.class).getValue());
    }

    /** A stored negative value becomes a unary expression and must be grouped before a unary or postfix operation. */
    @ParameterizedTest(name = "{0}: {1}")
    @MethodSource("embeddedNegativeNumbers")
    void groupsEmbeddedNegativeNumbers(final String fixtureName, final Number value, final String expected) throws IOException {
        final String source = Files.readString(Path.of("src/test/resources/testcases/expressions/" + fixtureName + ".sysml"));
        for (final SerializationMode mode : SerializationMode.values()) {
            final var result = parse(source);
            final Element literal = elements(result.model())
                    .filter(element -> element instanceof LiteralInteger || element instanceof LiteralRational)
                    .findFirst().orElseThrow();
            if (literal instanceof final LiteralInteger integer) {
                integer.setValue(value.intValue());
            } else if (literal instanceof final LiteralRational rational) {
                rational.setValue(value.doubleValue());
            }

            final String text = new SysMLSerializer().serialize(result, SerializationOptions.defaults().withMode(mode));

            assertEquals(expected, oneLine(text), mode.toString());
            assertParses(text);
        }
    }

    private static Stream<Arguments> embeddedNegativeNumbers() {
        return Stream.of(
                Arguments.of("negative-literal-negation", -3, "attribute v = -(-3);"),
                Arguments.of("negative-literal-negation-rational", -2.5, "attribute v = -(-2.5);"),
                Arguments.of("negative-literal-negation-rational", Double.NEGATIVE_INFINITY, "attribute v = -(-1.0E309);"),
                Arguments.of("negative-literal-navigation-integer", -3, "attribute v = (-3).f;"),
                Arguments.of("negative-literal-navigation", -2.5, "attribute v = (-2.5).f;"),
                Arguments.of("negative-literal-navigation", Double.NEGATIVE_INFINITY, "attribute v = (-1.0E309).f;"),
                Arguments.of("negative-literal-navigation", -0.0, "attribute v = (-0.0).f;"),
                Arguments.of("negative-literal-index", -3, "attribute v = (-3)#(1);"),
                Arguments.of("negative-literal-receiver", -3, "attribute v = (-3)->f();"),
                Arguments.of("negative-literal-quantity", -3, "attribute v = (-3) [m];"));
    }

    /** The positive magnitude of MIN_VALUE is outside the parser's integer range, so a bare negation cannot reparse. */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void writesMinimumIntegerAsParseableExpression(final SerializationMode mode) throws IOException {
        final var result = parse("attribute v = 2;");
        final LiteralInteger literal = elements(result.model()).filter(LiteralInteger.class::isInstance)
                .map(LiteralInteger.class::cast).findFirst().orElseThrow();
        literal.setValue(Integer.MIN_VALUE);

        final String text = new SysMLSerializer().serialize(result, SerializationOptions.defaults().withMode(mode));

        assertEquals("attribute v = (-2147483647 - 1);", oneLine(text));
        final Expression reparsed = featureValues(parse(text).model()).getFirst();
        assertEquals("-", ((OperatorExpression) reparsed).getOperator());
        final List<Integer> values = elements(reparsed).filter(LiteralInteger.class::isInstance)
                .map(LiteralInteger.class::cast).map(LiteralInteger::getValue).toList();
        assertEquals(List.of(Integer.MAX_VALUE, 1), values);
        assertEquals(Integer.MIN_VALUE, -values.get(0) - values.get(1));
    }

    /**
     * A multiplicity bound edited into an operator expression has no slot in {@code multiplicityExpressionMember},
     * which accepts only a literal or a feature reference, not even a grouped expression. The serializer must not
     * throw and must write the bound as it is, so that the parser reports the error instead of silently reading a
     * different multiplicity.
     */
    @Test
    void writesNonLiteralMultiplicityBoundsAsTheyAre() throws IOException {
        final var result = parse("package P { attribute n = 1; attribute x = n + 1; part p[1]; }");
        final OperatorExpression sum = elements(result.model())
                .filter(OperatorExpression.class::isInstance)
                .map(OperatorExpression.class::cast)
                .findFirst().orElseThrow();
        final LiteralInteger bound = elements(result.model())
                .filter(LiteralInteger.class::isInstance)
                .map(LiteralInteger.class::cast)
                .filter(literal -> literal.getOwningNamespace() instanceof MultiplicityRange)
                .findFirst().orElseThrow();
        final var boundMembership = bound.getOwningMembership();
        boundMembership.getOwnedRelatedElement().set(boundMembership.getOwnedRelatedElement().indexOf(bound), sum);

        final String text = new SysMLSerializer().serialize(result);

        assertTrue(oneLine(text).contains("part p [n + 1];"), text);
        assertEquals(Diagnostic.ERROR,
                SerializerRoundTripTestSupport.parseDocument(text).diagnostic().getSeverity(), text);
    }

    /**
     * A binding connector without its two ends cannot use {@code bind e1 = e2} and falls back to the generic
     * declaration. It must be written with the grammar keyword {@code binding}, never with a keyword derived from its
     * metaclass name such as {@code bindingConnectorAs}, which the parser would read as a name.
     */
    @Test
    void writesGrammarKeywordForBindingsWithoutEnds() {
        final Package root = packageOwning(SysMLFactory.eINSTANCE.createBindingConnectorAsUsage());

        assertEquals("package P { binding x; }", oneLine(new SysMLSerializer().serialize(root)));
    }

    /**
     * Usages built without a parser whose metaclass names are not keywords ({@code ExhibitStateUsage},
     * {@code IncludeUseCaseUsage}, {@code PerformActionUsage}) must be written with their grammar keywords, never with
     * keywords derived from the metaclass names, and the text must parse.
     */
    @Test
    void writesGrammarKeywordsForBuiltUsagesWithCompoundMetaclassNames() throws IOException {
        final Map<Usage, String> expectedTexts = Map.of(
                SysMLFactory.eINSTANCE.createExhibitStateUsage(), "package P { exhibit state x; }",
                SysMLFactory.eINSTANCE.createIncludeUseCaseUsage(), "package P { ref include use case x; }",
                SysMLFactory.eINSTANCE.createPerformActionUsage(), "package P { perform action x; }");
        for (final Map.Entry<Usage, String> expected : expectedTexts.entrySet()) {
            final String text = new SysMLSerializer().serialize(packageOwning(expected.getKey()));

            assertEquals(expected.getValue(), oneLine(text));
            assertParses(text);
        }
    }

    /** Builds a package {@code P} owning {@code element}, named {@code x}, through an owning membership. */
    private static Package packageOwning(final Element element) {
        final Package root = SysMLFactory.eINSTANCE.createPackage();
        root.setDeclaredName("P");
        element.setDeclaredName("x");
        final OwningMembership membership = SysMLFactory.eINSTANCE.createOwningMembership();
        membership.getOwnedRelatedElement().add(element);
        root.getOwnedRelationship().add(membership);
        return root;
    }

    /** Returns the single literal of the given type nested in an operator expression, its only operand here. */
    private static <T extends Element> T operand(final OperatorExpression expression, final Class<T> type) {
        return elements(expression).filter(type::isInstance).map(type::cast).findFirst().orElseThrow();
    }

    /** Returns the values of the attribute declarations, in text order. */
    private static List<Expression> featureValues(final Element root) {
        return elements(root).filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .filter(value -> value.getOwningRelatedElement() instanceof AttributeUsage)
                .map(FeatureValue::getValue)
                .toList();
    }

    private static ISysMLParseResult<Namespace> parse(final String source) throws IOException {
        final ISysMLParseResult<Namespace> result = SerializerRoundTripTestSupport.parseDocument(source);
        assertEquals(0, result.diagnostic().getSeverity(), () -> source + "\n" + result.diagnostic());
        return result;
    }

    private static void assertParses(final String text) throws IOException {
        parse(text);
    }

    private static Stream<Element> elements(final Element root) {
        final Iterable<EObject> contents = root::eAllContents;
        return StreamSupport.stream(contents.spliterator(), false)
                .filter(Element.class::isInstance)
                .map(Element.class::cast);
    }

    /** A package built through the model API whose only member is an alias must keep its body and the alias. */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void writesTheBodyOfAModelBuiltAliasOnlyPackage(final SerializationMode mode) {
        final Namespace root = SysMLFactory.eINSTANCE.createNamespace();
        final Package target = SysMLFactory.eINSTANCE.createPackage();
        target.setDeclaredName("Y");
        final Package owner = SysMLFactory.eINSTANCE.createPackage();
        owner.setDeclaredName("P");
        for (final Package member : List.of(target, owner)) {
            final OwningMembership membership = SysMLFactory.eINSTANCE.createOwningMembership();
            membership.getOwnedRelatedElement().add(member);
            root.getOwnedRelationship().add(membership);
        }
        final Membership alias = SysMLFactory.eINSTANCE.createMembership();
        alias.setMemberName("X");
        alias.setMemberElement(target);
        owner.getOwnedRelationship().add(alias);

        final String text = oneLine(new SysMLSerializer().serialize(root, SerializationOptions.defaults().withMode(mode)));

        assertTrue(text.contains("package P { alias X for Y; }"), text);
    }

    /**
     * The {@code out} that structural completion gives a return parameter written without direction must stay implicit
     * in ROUND_TRIP only while the feature is a return parameter: moved into an ordinary feature membership, it must be
     * written in both modes.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void writesTheDirectionOfAFeatureMovedOutOfAReturnParameter(final SerializationMode mode) throws IOException {
        final var result = parse("part def P; calc def C { return r : Real; }");
        final List<Element> members = result.model().getOwnedRelationship().stream()
                .flatMap(relationship -> relationship.getOwnedRelatedElement().stream()).toList();
        final ReturnParameterMembership returnMembership = (ReturnParameterMembership) members.get(1).getOwnedRelationship().stream()
                .filter(ReturnParameterMembership.class::isInstance).findFirst().orElseThrow();
        new StructuralModelCompletionService().complete(returnMembership);
        if (mode == SerializationMode.ROUND_TRIP) {
            assertEquals("part def P; calc def C { return r : Real; }",
                    oneLine(new SysMLSerializer().serialize(result, SerializationOptions.defaults().withMode(mode))));
        }

        final Element parameter = returnMembership.getOwnedRelatedElement().getFirst();
        final FeatureMembership replacement = SysMLFactory.eINSTANCE.createFeatureMembership();
        replacement.getOwnedRelatedElement().add(parameter);
        members.get(0).getOwnedRelationship().add(replacement);
        members.get(1).getOwnedRelationship().remove(returnMembership);

        final String text = oneLine(new SysMLSerializer().serialize(result, SerializationOptions.defaults().withMode(mode)));

        assertTrue(text.startsWith("part def P { out r : Real; }"), text);
    }

    private static String oneLine(final String text) {
        return text.replaceAll(WHITESPACE, " ").trim();
    }
}
