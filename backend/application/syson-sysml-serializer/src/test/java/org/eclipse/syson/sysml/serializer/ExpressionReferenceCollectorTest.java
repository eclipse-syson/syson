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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.serializer.expression.ExpressionReferenceCollector;
import org.eclipse.syson.sysml.serializer.expression.ExpressionRenderer;
import org.eclipse.syson.sysml.serializer.model.DanglingReferenceIndex;
import org.eclipse.syson.sysml.serializer.model.SerializerModelQueries;
import org.eclipse.syson.sysml.serializer.names.NameRenderer;
import org.eclipse.syson.sysml.serializer.output.StringTextOutput;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.syntax.CommentRenderer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Tests reference candidate normalization.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class ExpressionReferenceCollectorTest {

    private static final String REPEATED_NAME = "A";

    private static final String KNOWN_NAME = "Known";

    @BeforeAll
    static void setUpModel() {
        SysMLLogicStandaloneSetup.doSetup();
        SysMLPackage.eINSTANCE.getName();
    }

    /** A scoped unresolved path must retain the target supplied by its caller, including an alias spelling. */
    @ParameterizedTest
    @ValueSource(strings = { KNOWN_NAME, "Alias" })
    void preservesCallerFallbackInScopedPath(final String spelling) throws IOException {
        final var result = SerializerRoundTripTestSupport.parseDocument("attribute value = " + spelling + ";");
        final Element fallback = SysMLFactory.eINSTANCE.createFeature();
        fallback.setDeclaredName(KNOWN_NAME);
        final ExpressionReferenceCollector collector = this.createReferenceCollector(DanglingReferenceIndex.from(result.danglingLinks()));

        final List<TextOutput.Reference> references = collector.collect(result.model(), spelling, fallback);

        assertEquals(List.of(new TextOutput.Reference(spelling, fallback)), references);
    }

    /** A rendered path with no retained model reference must still expose the target known only to its caller. */
    @Test
    void preservesCallerFallbackWithoutRetainedReference() {
        final Element fallback = SysMLFactory.eINSTANCE.createFeature();
        fallback.setDeclaredName(KNOWN_NAME);
        final SerializationContext context = defaultContext(DanglingReferenceIndex.empty());
        final ExpressionReferenceCollector collector = new ExpressionReferenceCollector(expression -> KNOWN_NAME,
                new ReferenceResolver(context), context);

        final List<TextOutput.Reference> references = collector.collect(SysMLFactory.eINSTANCE.createFeatureReferenceExpression(), KNOWN_NAME, fallback);

        assertEquals(List.of(new TextOutput.Reference(KNOWN_NAME, fallback)), references);
    }

    /** A fallback name matching only a prefix must not bind a different, longer scoped reference. */
    @Test
    void doesNotBindFallbackToReferencePrefix() throws IOException {
        final var result = SerializerRoundTripTestSupport.parseDocument("attribute value = KnownExtra;");
        final Element fallback = SysMLFactory.eINSTANCE.createFeature();
        fallback.setDeclaredName(KNOWN_NAME);
        final ExpressionReferenceCollector collector = this.createReferenceCollector(DanglingReferenceIndex.from(result.danglingLinks()));

        final List<TextOutput.Reference> references = collector.collect(result.model(), "KnownExtra", fallback);

        assertEquals(List.of(new TextOutput.Reference("KnownExtra", null)), references);
    }

    /** A caller fallback must not replace a locally resolved occurrence with another target bearing the same name. */
    @Test
    void keepsLocalTargetWhenCallerSuppliesFallback() throws IOException {
        final var result = SerializerRoundTripTestSupport.parseDocument("attribute value = Known;");
        final Element resolved = SysMLFactory.eINSTANCE.createFeature();
        resolved.setDeclaredName(KNOWN_NAME);
        final Element fallback = SysMLFactory.eINSTANCE.createFeature();
        fallback.setDeclaredName(KNOWN_NAME);
        for (final DanglingLink link : result.danglingLinks()) {
            link.context().eSet(link.reference(), resolved);
        }
        final ExpressionReferenceCollector collector = this.createReferenceCollector(DanglingReferenceIndex.from(result.danglingLinks()));

        final List<TextOutput.Reference> references = collector.collect(result.model(), KNOWN_NAME, fallback);

        assertEquals(List.of(new TextOutput.Reference(KNOWN_NAME, resolved)), references);
    }

    /** A model-wide name index must leave a spelling unbound when it denotes distinct targets in other contexts. */
    @Test
    void doesNotGuessIndexedTargetFromAmbiguousName() throws IOException {
        final var parsed = SerializerRoundTripTestSupport.parseDocument("attribute a = Known; attribute b = Known;");
        for (final DanglingLink link : parsed.danglingLinks()) {
            final Element target = SysMLFactory.eINSTANCE.createFeature();
            target.setDeclaredName(KNOWN_NAME);
            link.context().eSet(link.reference(), target);
        }
        final ExpressionReferenceCollector collector = this.createReferenceCollector(DanglingReferenceIndex.from(parsed.danglingLinks()));
        final List<TextOutput.Reference> references = collector.collect(SysMLFactory.eINSTANCE.createFeature(), KNOWN_NAME);
        assertEquals(List.of(), references);
    }

    /**
     * An unbound candidate must remain unknown when an equivalent spelling names distinct objects; existing
     * candidate targets must not be replaced.
     */
    @Test
    void leavesAmbiguousQuotedCandidateUnbound() {
        final Element firstTarget = SysMLFactory.eINSTANCE.createPackage();
        final Element secondTarget = SysMLFactory.eINSTANCE.createPackage();
        final List<TextOutput.Reference> candidates = List.of(
                new TextOutput.Reference("'target'", null),
                new TextOutput.Reference("target", firstTarget),
                new TextOutput.Reference("'target'", secondTarget));

        final List<TextOutput.Reference> resolved =
                ExpressionReferenceCollector.resolveQuotedTargets(candidates);

        assertEquals(null, resolved.get(0).target());
        assertSame(firstTarget, resolved.get(1).target());
        assertSame(secondTarget, resolved.get(2).target());
    }

    /**
     * Resolving the supplied 20,000 reference candidates must finish within two seconds.
     */
    @Test
    void resolvesLargeCandidateSetWithinTimeLimit() {
        final Element target = SysMLFactory.eINSTANCE.createPackage();
        final List<TextOutput.Reference> candidates = new ArrayList<>();
        for (int index = 0; index < 10_000; index++) {
            candidates.add(new TextOutput.Reference("'name" + index + "'",
                    null));
            candidates.add(new TextOutput.Reference("name" + index, target));
        }

        assertTimeout(Duration.ofSeconds(2),
                () -> ExpressionReferenceCollector
                        .resolveQuotedTargets(candidates));
    }

    /**
     * When candidates match at the same position, the collector must select the longest matching name.
     */
    @Test
    void prefersLongestCandidateAtTheSamePosition() {
        final ExpressionReferenceCollector collector = this.createReferenceCollector(DanglingReferenceIndex.empty());
        final Element shortTarget = SysMLFactory.eINSTANCE.createPackage();
        final Element fullTarget = SysMLFactory.eINSTANCE.createPackage();

        final List<TextOutput.Reference> references = collector
                .visibleOccurrences("[AuditProxy_000008]", List.of(
                        new TextOutput.Reference("A", shortTarget),
                        new TextOutput.Reference("AuditProxy_000008",
                                fullTarget)));

        assertSame(fullTarget, references.get(0).target());
    }

    /**
     * Dots and qualification separators inside quoted identifiers must not split segments. Without linked path
     * information, only the final segment must receive the target.
     */
    @Test
    void keepsQuotedSegmentsAndBindsOnlyTheFinalReferenceSegment() {
        final Element target = SysMLFactory.eINSTANCE.createFeature();

        assertEquals(List.of(new TextOutput.Reference("Namespace", null),
                new TextOutput.Reference("'Name.With.Dots'", target)),
                ExpressionReferenceCollector.atomicReferences(
                        "Namespace::'Name.With.Dots'", target));
        assertEquals(List.of(new TextOutput.Reference("source", null),
                new TextOutput.Reference("'Name.With.Dots'", target)),
                ExpressionReferenceCollector.atomicReferences(
                        "source.'Name.With.Dots'", target));
        assertEquals(List.of(new TextOutput.Reference("source", null),
                new TextOutput.Reference("'qualified::name'", target)),
                ExpressionReferenceCollector.atomicReferences(
                        "source.'qualified::name'", target));
        assertEquals(List.of(new TextOutput.Reference("source", null),
                new TextOutput.Reference("value", target)),
                ExpressionReferenceCollector.atomicReferences(
                        "source.value", target));
        final ExpressionReferenceCollector collector = this.createReferenceCollector(DanglingReferenceIndex.empty());
        assertEquals(List.of(new TextOutput.Reference("'Name.With.Dots'", target)),
                collector.visibleOccurrences("source.'Name.With.Dots'",
                        List.of(new TextOutput.Reference("Namespace::'Name.With.Dots'", target))));
    }

    /**
     * When the path resolver supplies targets for an alias-qualified reference, each segment must bind to its
     * corresponding target, including the qualifier.
     */
    @Test
    void usesLinkedPathWhenQualifierIsAnAlias() {
        final Namespace actualNamespace =
                SysMLFactory.eINSTANCE.createPackage();
        actualNamespace.setDeclaredName("Actual");
        final Element sleigh =
                SysMLFactory.eINSTANCE.createPartDefinition();
        sleigh.setDeclaredName("Sleigh");
        final OwningMembership membership =
                SysMLFactory.eINSTANCE.createOwningMembership();
        membership.setOwnedMemberElement(sleigh);
        actualNamespace.getOwnedRelationship().add(membership);
        final FeatureTyping typing =
                SysMLFactory.eINSTANCE.createFeatureTyping();
        final SerializationOptions options = SerializationOptions.defaults();
        final SerializationContext context = new SerializationContext(options,
                new StringTextOutput(options.formatting()),
                new NameRenderer(), new SerializerModelQueries(),
                DanglingReferenceIndex.empty(),
                (owner, reference, name, target) ->
                    List.of(actualNamespace, sleigh));
        final ExpressionReferenceCollector collector = this.createReferenceCollector(context);

        final List<TextOutput.Reference> references = collector
                .atomicReferenceOccurrences("Alias::Sleigh", sleigh, typing,
                        SysMLPackage.eINSTANCE.getFeatureTyping_Type());

        assertSame(actualNamespace, references.get(0).target());
        assertSame(sleigh, references.get(1).target());
    }

    /** Repeated namespace and member names must bind from the terminal segment back through its owners. */
    @Test
    void bindsRepeatedPathSegmentsFromTheRight() {
        final Namespace namespace = SysMLFactory.eINSTANCE.createPackage();
        namespace.setDeclaredName(REPEATED_NAME);
        final Element child = SysMLFactory.eINSTANCE.createPartDefinition();
        child.setDeclaredName(REPEATED_NAME);
        final OwningMembership membership = SysMLFactory.eINSTANCE.createOwningMembership();
        membership.setOwnedMemberElement(child);
        namespace.getOwnedRelationship().add(membership);
        final ExpressionReferenceCollector collector = this.createReferenceCollector(DanglingReferenceIndex.empty());

        for (final String path : List.of("A::A", "'A'::A", "A::'A'")) {
            final List<TextOutput.Reference> references = collector.atomicReferenceOccurrences(path, child);

            assertSame(namespace, references.get(0).target(), path);
            assertSame(child, references.get(1).target(), path);
        }
    }

    /** Renaming one target must not rewrite an unchanged relative spelling bound in another namespace. */
    @Test
    void keepsQualifiedRenamesInTheirReferenceContext() throws IOException {
        final var result = SerializerRoundTripTestSupport.parseDocument("""
                package P {
                    package Q { part A; }
                    state s { state s1; transition t first s1 do send Q::A then s1; }
                }
                package R {
                    package Q { part A; }
                    state s { state s1; transition t first s1 do send Q::A then s1; }
                }
                """);
        final Element p = declaredElement(result.model(), "P");
        final Element firstTarget = declaredElement(p, REPEATED_NAME);
        final Element secondTarget = declaredElement(declaredElement(result.model(), "R"), REPEATED_NAME);
        for (final DanglingLink link : result.danglingLinks()) {
            if (link.targets().contains("Q::A")) {
                Element target = secondTarget;
                if (EcoreUtil.isAncestor(p, link.context())) {
                    target = firstTarget;
                }
                link.context().eSet(link.reference(), target);
            }
        }
        firstTarget.setDeclaredName("B");

        final String text = new SysMLSerializer().serialize(result,
                SerializationOptions.defaults().withMode(SerializationMode.ROUND_TRIP));

        assertTrue(text.contains("do send P::Q::B then s1;"), text);
        assertTrue(text.contains("do send Q::A then s1;"), text);
    }

    /**
     * When a parser spelling is linked after parsing to a target with another name, the rename must not rewrite the
     * same spelling inside a string literal: {@code send "Old" to Old} must keep its payload {@code "Old"}.
     */
    @Test
    void doesNotRenameSpellingInsideStringLiteral() throws IOException {
        final String text = serializeAfterLinking("""
                package P {
                    part x;
                    state s { state s1; transition t first s1 do send "Old" to Old then s1; }
                }
                """, Map.of("Old", "x"));

        assertTrue(text.contains("do send \"Old\" to P::x then s1;"), text);
    }

    /**
     * Renames must apply to the parser spellings only: a later rename must not rewrite the text an earlier rename
     * inserted, so {@code x.v.w} with {@code v} linked to {@code w} and {@code w} linked to {@code z} must become
     * {@code x.w.z}, not {@code x.z.w}.
     */
    @Test
    void doesNotRenameTextInsertedByAnotherRename() throws IOException {
        final String text = serializeAfterLinking("""
                part w;
                part z;
                action a { assign x.v.w := 1; }
                """, Map.of("v", "w", "w", "z"));

        assertTrue(text.contains("assign x.w.z := 1;"), text);
    }

    /**
     * When linking installs only some targets of a many-valued reference, spellings and targets no longer correspond
     * by position, so no rename must be derived from them: the unresolved {@code Lib::Missing} client must not turn
     * every other {@code Lib::Missing} spelling into {@code Lib::Known}.
     */
    @Test
    void doesNotPairSpellingsWithPartiallyLinkedTargets() throws IOException {
        final String text = serializeAfterLinking("""
                package P {
                    package Lib { part Known; }
                    dependency from Lib::Missing, Lib::Known to Lib::Known;
                    state s { state s1; transition t first s1 do send Lib::Missing then s1; }
                }
                """, Map.of("Lib::Known", "Known"));

        assertTrue(text.contains("do send Lib::Missing then s1;"), text);
    }

    /**
     * A bare parser spelling must not match a segment of a longer qualified name, which the parser would have
     * recorded whole: with {@code A} linked to {@code Q::B} and {@code B} to {@code Q::C}, {@code send A to B} must
     * become {@code send P::Q::B to P::Q::C}, not {@code send P::Q::P::Q::C to P::Q::C}.
     */
    @Test
    void doesNotRenameSegmentOfQualifiedName() throws IOException {
        final String text = serializeAfterLinking("""
                package P {
                    package Q { part B; part C; }
                    state s { state s1; transition t first s1 do send A to B then s1; }
                }
                """, Map.of("A", "B", "B", "C"));

        assertTrue(text.contains("do send P::Q::B to P::Q::C then s1;"), text);
    }

    private static String serializeAfterLinking(final String source, final Map<String, String> targetNameBySpelling)
            throws IOException {
        final ISysMLParseResult<Namespace> parseResult = SerializerRoundTripTestSupport.parseDocument(source);
        for (final DanglingLink link : parseResult.danglingLinks()) {
            final List<Element> targets = link.targets().stream()
                    .filter(targetNameBySpelling::containsKey)
                    .map(spelling -> declaredElement(parseResult.model(), targetNameBySpelling.get(spelling)))
                    .toList();
            if (link.reference().isMany()) {
                final List<Object> values = new ArrayList<>((List<?>) link.context().eGet(link.reference()));
                values.addAll(targets);
                link.context().eSet(link.reference(), values);
            } else if (!targets.isEmpty()) {
                link.context().eSet(link.reference(), targets.get(0));
            }
        }
        return new SysMLSerializer().serialize(parseResult,
                SerializationOptions.defaults().withMode(SerializationMode.ROUND_TRIP));
    }

    private static Element declaredElement(final Element root, final String declaredName) {
        final TreeIterator<EObject> contents = root.eAllContents();
        while (contents.hasNext()) {
            if (contents.next() instanceof final Element element && declaredName.equals(element.getDeclaredName())) {
                return element;
            }
        }
        return fail("No element named " + declaredName);
    }

    private static SerializationContext defaultContext(final DanglingReferenceIndex danglingReferenceIndex) {
        final SerializationOptions options = SerializationOptions.defaults();
        return new SerializationContext(options, new StringTextOutput(options.formatting()), new NameRenderer(),
                new SerializerModelQueries(), danglingReferenceIndex);
    }

    private ExpressionReferenceCollector createReferenceCollector(final DanglingReferenceIndex danglingReferenceIndex) {
        return this.createReferenceCollector(defaultContext(danglingReferenceIndex));
    }

    private ExpressionReferenceCollector createReferenceCollector(final SerializationContext context) {
        final ReferenceResolver referenceResolver = new ReferenceResolver(context);
        final CommentRenderer comments = new CommentRenderer(referenceResolver, context);
        final ExpressionRenderer expressions = new ExpressionRenderer(referenceResolver, comments, context);
        return new ExpressionReferenceCollector(expressions::expressionText, referenceResolver, context);
    }
}
