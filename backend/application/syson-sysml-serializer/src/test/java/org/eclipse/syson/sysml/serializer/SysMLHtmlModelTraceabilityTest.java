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
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.FeatureChainExpression;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;
import org.omg.sysml.util.ElementUtil;

/**
 * Connects highlighted HTML and qualified reference segments to their original EMF elements.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class SysMLHtmlModelTraceabilityTest {

    /** The feature name repeated in every navigated part, so that only scoping can tell its targets apart. */
    private static final String FEATURE_NAME = "a";

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /** Each occurrence of an identically named feature must link to the target of its own feature chain. */
    @ParameterizedTest
    @CsvSource({ "x.a + y.a, 1, 1, 0", "x.a + y.a + x.a, 2, 1, 0", "x.a + x.a + y.a, 2, 1, 0", "a.a, 0, 0, 1" })
    void linksRepeatedFeatureNamesToTheirOwnTargets(final String expression, final int xCount, final int yCount, final int aCount)
            throws IOException {
        final var result = parseRootNamespace("package P { part x { attribute a; } part y { attribute a; } part a { attribute a; } "
                + "attribute value = " + expression + "; }");
        final Element x = namedElement(result.model(), "x");
        final Element y = namedElement(result.model(), "y");
        final Element a = namedMember(namedElement(result.model(), "P"), FEATURE_NAME);
        final Element xa = namedElement(x, FEATURE_NAME);
        final Element ya = namedElement(y, FEATURE_NAME);
        final Element aa = namedElement(a, FEATURE_NAME);
        for (final DanglingLink link : result.danglingLinks()) {
            Element target = null;
            if (link.targets().contains("x")) {
                target = x;
            } else if (link.targets().contains("y")) {
                target = y;
            } else if (link.targets().contains(FEATURE_NAME)) {
                target = a;
                if (((Relationship) link.context()).getOwningRelatedElement() instanceof final FeatureChainExpression chain) {
                    target = Map.of("x", xa, "y", ya).getOrDefault(chainSourceName(result, chain), aa);
                }
            }
            if (target != null) {
                link.context().eSet(link.reference(), target);
            }
        }
        final Map<Element, String> links = Map.of(xa, "#x-a", ya, "#y-a", a, "#a", aa, "#a-a");
        final SerializationRequest request = SerializationRequest.forParseResult(result)
                .withHtmlLinks(target -> Optional.ofNullable(links.get(target)));

        final String html = new SysMLSerializer().serializeToHtml(request);

        assertEquals(xCount, linkCount(html, "#x-a"), html);
        assertEquals(yCount, linkCount(html, "#y-a"), html);
        assertEquals(aCount, linkCount(html, "#a"), html);
        assertEquals(aCount, linkCount(html, "#a-a"), html);
    }

    /**
     * An alias names the feature it is written for: when that feature references another one ({@code p ::> q}), the
     * link of the alias target must still lead to the named feature, not to the feature it references.
     */
    @Test
    void linksAnAliasTargetToTheNamedFeature() throws IOException {
        final var result = SerializerRoundTripTestSupport.parseTestCase("edited/alias-of-a-referencing-feature.sysml");
        final Element q = namedElement(result.model(), "q");
        final Element p = namedElement(result.model(), "p");
        for (final DanglingLink link : result.danglingLinks()) {
            Element target = p;
            if (link.targets().contains("q")) {
                target = q;
            }
            link.context().eSet(link.reference(), target);
        }
        final Map<Element, String> links = Map.of(p, "#p", q, "#q");

        final String html = new SysMLSerializer().serializeToHtml(SerializationRequest.forParseResult(result)
                .withHtmlLinks(target -> Optional.ofNullable(links.get(target))));

        assertEquals(1, linkCount(html, "#p"), html);
        assertEquals(1, linkCount(html, "#q"), html);
    }

    /**
     * Each typing of a list is written with its own qualifier, so a namespace qualifying two types
     * ({@code : P::A, P::B}) must be linked at both places, in both typing clause forms.
     */
    @Test
    void linksAQualifierRepeatedInATypingList() throws IOException {
        final var result = SerializerRoundTripTestSupport.parseTestCase("edited/repeated-typing-qualifiers.sysml");
        final Element pkg = namedElement(result.model(), "P");
        final Element a = namedElement(result.model(), "A");
        final Element b = namedElement(result.model(), "B");
        for (final DanglingLink link : result.danglingLinks()) {
            Element target = a;
            if (link.targets().contains("P::B")) {
                target = b;
            }
            link.context().eSet(link.reference(), target);
        }
        final Map<Element, String> links = Map.of(pkg, "#P", a, "#A", b, "#B");

        final String html = new SysMLSerializer().serializeToHtml(SerializationRequest.forParseResult(result)
                .withHtmlLinks(target -> Optional.ofNullable(links.get(target))));

        assertEquals(4, linkCount(html, "#P"), html);
        assertEquals(2, linkCount(html, "#A"), html);
        assertEquals(2, linkCount(html, "#B"), html);
    }

    /**
     * A conjugated port typing is linked to the conjugated definition that its port definition owns, which has no name of
     * its own: the qualifier of {@code ~P::Pt} must still link to package {@code P}, found from the port definition.
     */
    @Test
    void linksTheQualifierOfAConjugatedTyping() throws IOException {
        final var result = SerializerRoundTripTestSupport.parseTestCase("edited/conjugated-typing-links.sysml");
        final Element pkg = namedElement(result.model(), "P");
        final Element conjugated = namedElement(pkg, "Pt").getOwnedRelationship().get(0).getOwnedRelatedElement().get(0);
        for (final DanglingLink link : result.danglingLinks()) {
            link.context().eSet(link.reference(), conjugated);
        }
        final Map<Element, String> links = Map.of(pkg, "#P", conjugated, "#Pt");

        final String html = new SysMLSerializer().serializeToHtml(SerializationRequest.forParseResult(result)
                .withHtmlLinks(target -> Optional.ofNullable(links.get(target))));

        assertEquals(1, linkCount(html, "#P"), html);
        assertEquals(1, linkCount(html, "#Pt"), html);
    }

    /** Returns the spelling of the parser link held inside a chain's source, which tells which feature it navigates. */
    private static String chainSourceName(final ISysMLParseResult<?> result, final FeatureChainExpression chain) {
        return result.danglingLinks().stream()
                .filter(source -> EcoreUtil.isAncestor(chain, source.context()))
                .flatMap(source -> source.targets().stream())
                .filter(name -> !FEATURE_NAME.equals(name))
                .findFirst().orElse(FEATURE_NAME);
    }

    private static Element namedMember(final Element namespace, final String name) {
        return namespace.getOwnedRelationship().stream()
                .flatMap(relationship -> relationship.getOwnedRelatedElement().stream())
                .filter(element -> name.equals(element.getDeclaredName()))
                .findFirst().orElseThrow();
    }

    private static int linkCount(final String html, final String anchor) {
        return html.split(Pattern.quote("href=\"" + anchor + "\""), -1).length - 1;
    }

    private static Element namedElement(final Element root, final String name) {
        final var contents = root.eAllContents();
        while (contents.hasNext()) {
            if (contents.next() instanceof final Element element && name.equals(element.getDeclaredName())) {
                return element;
            }
        }
        return fail("Missing element " + name);
    }

    /**
     * HTML serialization must highlight the source and expose the type and identifier of the original EMF element.
     */
    @Test
    void serializeToHtmlHighlightsSourceAndPreservesModelIdentity() throws Exception {
        final String source = "package Traceable;";
        final var parseResult = new SysMLModelParser().parsePackage_(
                new ByteArrayInputStream(
                        source.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);
        final String elementId = ElementUtil
                .getElementIdOf(parseResult.model());

        final String html = new SysMLSerializer().serializeToHtml(parseResult);

        assertTrue(html.startsWith("<!doctype html>"));
        assertTrue(html.contains(
                "<span class=\"syntax-keyword\">package</span>"));
        assertTrue(html.contains("data-element-type=\"Package\""));
        assertTrue(html.contains("data-element-id=\"" + elementId + "\""));
        assertTrue(html.contains("Package - ID: " + elementId));
    }

    /**
     * Every resolved qualified-name segment must expose its own target metadata, including both the namespace and the
     * final definition.
     */
    @Test
    void serializeToHtmlLinksEveryResolvedQualifiedNameSegment()
            throws Exception {
        final String source = """
                package P {
                    part def Sleigh;
                    part def Reindeer {
                        ref part pulling : P::Sleigh;
                    }
                }
                """;
        final var parseResult = parseRootNamespace(source);
        Namespace packageElement = null;
        Element sleigh = null;
        final var contents = parseResult.model().eAllContents();
        while (contents.hasNext()) {
            final var content = contents.next();
            if (content instanceof final Namespace namespace
                    && "P".equals(namespace.getDeclaredName())) {
                packageElement = namespace;
            } else if (content instanceof final Element element
                    && "Sleigh".equals(element.getDeclaredName())) {
                sleigh = element;
            }
        }
        final Namespace resolvedPackage = Objects.requireNonNull(packageElement);
        final Element resolvedSleigh = Objects.requireNonNull(sleigh);
        final var typingLink = parseResult.danglingLinks().stream()
                .filter(link -> link.targets().contains("P::Sleigh"))
                .findFirst().orElseThrow();
        typingLink.context().eSet(typingLink.reference(), resolvedSleigh);
        // HTML only exposes ids that elements already have; serializing never assigns one.
        final String packageId = ElementUtil.getElementIdOf(resolvedPackage);
        final String sleighId = ElementUtil.getElementIdOf(resolvedSleigh);

        final String html = new SysMLSerializer().serializeToHtml(parseResult);

        assertTrue(html.contains("data-reference-target-id=\"" + packageId + "\""));
        assertTrue(html.contains("data-reference-target-id=\"" + sleighId + "\""));
    }

    private static ISysMLParseResult<Namespace> parseRootNamespace(final String source) throws IOException {
        return SerializerRoundTripTestSupport.parseDocument(source);
    }
}
