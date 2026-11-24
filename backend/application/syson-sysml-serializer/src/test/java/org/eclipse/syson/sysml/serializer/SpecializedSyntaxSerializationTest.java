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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport.ParserRuleFixture;
import org.eclipse.syson.sysml.serializer.model.DanglingReferenceIndex;
import org.eclipse.syson.sysml.serializer.model.SerializerModelQueries;
import org.eclipse.syson.sysml.serializer.model.StoredModel;
import org.eclipse.syson.sysml.serializer.names.NameRenderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.omg.sysml.lang.sysml.ActionDefinition;
import org.omg.sysml.lang.sysml.ConstructorExpression;
import org.omg.sysml.lang.sysml.DecisionNode;
import org.omg.sysml.lang.sysml.Dependency;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.PartDefinition;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.TransitionUsage;

/**
 * Exercises specialized syntax and linked model states identified by the serializer coverage review.
 * Fixture text and models are checked independently, including serialization without parser markers.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class SpecializedSyntaxSerializationTest {

    private static final Path TEST_CASES = Path.of("src/test/resources/testcases");

    /** The complete text of every specialization, multiplicity and inline comment must be retained. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("fixtures")
    void preservesSpecializedSyntax(final ParserRuleFixture fixture) throws IOException {
        assertWhitespaceOnlyRoundTrip(fixture, new SysMLModelParser(), new SysMLSerializer());
    }

    /** Parser markers cannot be required to retain the specialization and multiplicity model. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("fixtures")
    void preservesSpecializedModel(final ParserRuleFixture fixture) throws IOException {
        assertSemanticRoundTripWithoutTextAdapters(fixture, new SysMLModelParser(), new SysMLSerializer());
    }

    /** A value's grouping hint remains sufficient when its constructor no longer carries the same hint. */
    @Test
    void preservesConstructorGroupingWithOnlyTheValueMarker() throws IOException {
        final var parsed = parse("expressions/constructor-elements-value");
        final ConstructorExpression constructor = firstElement(parsed.model(), ConstructorExpression.class);
        assertTrue(constructor.eAdapters().remove(ITextAdapter.PARENTHESIZED_EXPRESSION));
        final String expectedXmi = SerializerRoundTripTestSupport.toCanonicalXmi(parsed.model(), "expected.sysmlx");
        final String expectedLinks = SerializerRoundTripTestSupport.canonicalDanglingLinks(parsed);

        final String serialized = new SysMLSerializer().serialize(parsed, SerializationOptions.defaults()
                .withMode(SerializationMode.ROUND_TRIP).withImplicitReferenceKeywordElision(false));
        final String source = ParserRuleFixture.document(TEST_CASES.resolve("expressions/constructor-elements-value.sysml")).readSource();
        assertEquals(source.replaceAll("\\s+", ""), serialized.replaceAll("\\s+", ""), serialized);
        final var reparsed = SerializerRoundTripTestSupport.parseDocument(serialized);
        assertTrue(reparsed.diagnostic().getSeverity() < Diagnostic.ERROR, serialized);
        assertEquals(expectedXmi, SerializerRoundTripTestSupport.toCanonicalXmi(reparsed.model(), "actual.sysmlx"));
        assertEquals(expectedLinks, SerializerRoundTripTestSupport.canonicalDanglingLinks(reparsed));
    }

    /** A surplus unresolved endpoint must not take the position of an installed proxy. */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void preservesPartiallyLinkedProxyEndpoints(final SerializationMode mode) throws IOException {
        assertLinkedFixture("references/partial-dependency-proxy", mode,
                SpecializedSyntaxSerializationTest::installDependencyProxy, ReferencePathResolver.NONE);
    }

    /** A decision source installed by structural linking stays implicit when its guarded branch is written. */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void preservesImplicitDecisionSources(final SerializationMode mode) throws IOException {
        assertLinkedFixture("actions/decision-implicit-source", mode,
                SpecializedSyntaxSerializationTest::installDecisionSource, ReferencePathResolver.NONE);
    }

    /** The public request must pass its confirmed alias path through to reference rendering in both modes. */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void preservesConfirmedAliasPaths(final SerializationMode mode) throws IOException {
        final ReferencePathResolver paths = (context, reference, spelling, target) -> {
            if ("Short::Component".equals(spelling) && target instanceof final PartDefinition definition) {
                return List.of(StoredModel.owningNamespace(definition), definition);
            }
            return List.of();
        };
        assertLinkedFixture("references/confirmed-alias-path", mode,
                SpecializedSyntaxSerializationTest::installAliasTarget, paths);
    }

    /** Subclassification discovery must return the directly declared superclass rather than an empty list. */
    @Test
    void findsDeclaredSupertypeNames() throws IOException {
        final var parsed = parse("actions/perform-repeated-redefinition-keywords");
        final ActionDefinition derived = elements(parsed.model()).stream()
                .filter(ActionDefinition.class::isInstance).map(ActionDefinition.class::cast)
                .filter(action -> "Derived".equals(action.getDeclaredName())).findFirst().orElseThrow();

        assertEquals(List.of("Base"), new SerializerModelQueries().specializationTargets().supertypeNames(derived,
                DanglingReferenceIndex.from(parsed.danglingLinks()), new NameRenderer()::referenceName));
    }

    /** A cached proxy name remains usable when its URI has no fragment; naming must not resolve the proxy. */
    @Test
    void namesAProxyWithoutAUriFragment() throws IOException {
        final var parsed = parse("references/partial-dependency-proxy");
        final PartDefinition original = elements(parsed.model()).stream().filter(PartDefinition.class::isInstance)
                .map(PartDefinition.class::cast).filter(part -> "B".equals(part.getDeclaredName())).findFirst().orElseThrow();
        final PartDefinition proxy = EcoreUtil.copy(original);
        final URI uri = URI.createURI("unresolved:/B");
        ((InternalEObject) proxy).eSetProxyURI(uri);

        assertEquals("B", new NameRenderer().referenceName(proxy));
        assertTrue(proxy.eIsProxy());
        assertSame(uri, ((InternalEObject) proxy).eProxyURI());
    }

    private static Stream<ParserRuleFixture> fixtures() {
        final Stream<ParserRuleFixture> documents = Stream.of(
                "actions/accept-payload-redefinition",
                "actions/decision-implicit-source",
                "actions/perform-repeated-redefinition-keywords",
                "actions/perform-repeated-qualified-redefinitions",
                "actions/perform-comma-redefinitions",
                "cases/analysis-subject-multiplicities",
                "declarations/array-explicit-multiplicity",
                "declarations/variant-chain-specializations",
                "expressions/constructor-elements-value",
                "expressions/lambda-parameter-comment-about",
                "references/partial-dependency-proxy",
                "references/confirmed-alias-path")
                .map(name -> ParserRuleFixture.document(TEST_CASES.resolve(name + ".sysml")));
        final ParserRuleFixture variant = new ParserRuleFixture("parseVariantReference",
                Path.of("src/test/resources/fragments/variantReference/chain-typing.sysml"));
        return Stream.concat(documents, Stream.of(variant));
    }

    private static ISysMLParseResult<? extends Element> parse(final String name) throws IOException {
        return SerializerRoundTripTestSupport.parseAccepted(ParserRuleFixture.document(TEST_CASES.resolve(name + ".sysml")));
    }

    private static void assertLinkedFixture(final String name, final SerializationMode mode,
            final Consumer<ISysMLParseResult<? extends Element>> installReferences, final ReferencePathResolver paths) throws IOException {
        final var parsed = parse(name);
        installReferences.accept(parsed);
        final String expectedXmi = SerializerRoundTripTestSupport.toCanonicalXmi(parsed.model(), "expected.sysmlx");
        final String expectedLinks = SerializerRoundTripTestSupport.canonicalDanglingLinks(parsed);
        if (mode == SerializationMode.NORMALIZED) {
            elements(parsed.model()).forEach(element -> element.eAdapters().removeIf(ITextAdapter.class::isInstance));
        }
        final SerializationRequest request = SerializationRequest.forParseResult(parsed)
                .withOptions(SerializationOptions.defaults().withMode(mode)).withReferencePaths(paths);
        final String serialized = new SysMLSerializer().serialize(request);
        final String source = ParserRuleFixture.document(TEST_CASES.resolve(name + ".sysml")).readSource();
        assertEquals(source.replaceAll("\\s+", ""), serialized.replaceAll("\\s+", ""), serialized);

        final var reparsed = SerializerRoundTripTestSupport.parseDocument(serialized);
        assertTrue(reparsed.diagnostic().getSeverity() < Diagnostic.ERROR, serialized);
        installReferences.accept(reparsed);
        assertEquals(expectedXmi, SerializerRoundTripTestSupport.toCanonicalXmi(reparsed.model(), "actual.sysmlx"), serialized);
        assertEquals(expectedLinks, SerializerRoundTripTestSupport.canonicalDanglingLinks(reparsed), serialized);
    }

    private static void installDependencyProxy(final ISysMLParseResult<? extends Element> parsed) {
        final Dependency dependency = firstElement(parsed.model(), Dependency.class);
        final PartDefinition proxy = SysMLFactory.eINSTANCE.createPartDefinition();
        ((InternalEObject) proxy).eSetProxyURI(URI.createURI("unresolved:/dependency#B"));
        dependency.getClient().add(proxy);
    }

    private static void installDecisionSource(final ISysMLParseResult<? extends Element> parsed) {
        final DecisionNode decision = firstElement(parsed.model(), DecisionNode.class);
        final TransitionUsage transition = firstElement(parsed.model(), TransitionUsage.class);
        final var source = SysMLFactory.eINSTANCE.createMembership();
        source.setMemberElement(decision);
        transition.getOwnedRelationship().add(0, source);
    }

    private static void installAliasTarget(final ISysMLParseResult<? extends Element> parsed) {
        firstElement(parsed.model(), FeatureTyping.class).setType(firstElement(parsed.model(), PartDefinition.class));
    }

    private static <T extends Element> T firstElement(final Element root, final Class<T> type) {
        return elements(root).stream().filter(type::isInstance).map(type::cast).findFirst().orElseThrow();
    }

    private static List<Element> elements(final Element root) {
        final List<Element> elements = new ArrayList<>();
        elements.add(root);
        root.eAllContents().forEachRemaining(object -> {
            if (object instanceof final Element element) {
                elements.add(element);
            }
        });
        return elements;
    }
}
