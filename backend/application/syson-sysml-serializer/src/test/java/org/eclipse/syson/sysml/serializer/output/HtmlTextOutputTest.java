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
package org.eclipse.syson.sysml.serializer.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.InternalEObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;
import org.omg.sysml.util.ElementUtil;

/**
 * Tests structured HTML text output.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class HtmlTextOutputTest {

    private static final String LINE_FEED = "\n";

    private static final String INDENTATION = "  ";

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * Output must be a standalone highlighted HTML document with escaped text and line markers; it must not contain
     * script or link tags.
     */
    @Test
    void rendersStandaloneHighlightedHtml() {
        final TextOutput output = new HtmlTextOutput(
                new FormattingOptions(LINE_FEED, INDENTATION));

        output.raw(
                "package P {\n  attribute answer = 42;\n  doc /* hello <&> */\n}");

        final String html = output.content();
        assertTrue(html.startsWith("<!doctype html>"));
        assertTrue(
                html.contains("<span class=\"syntax-keyword\">package</span>"));
        assertTrue(html.contains("<span class=\"syntax-identifier\">P</span>"));
        assertTrue(html.contains("<span class=\"syntax-literal\">42</span>"));
        assertTrue(html.contains(
                "<span class=\"syntax-comment\">/* hello &lt;&amp;&gt; */</span>"));
        assertTrue(html.contains("data-line=\"4\""));
        assertFalse(html.contains("<script"));
        assertFalse(html.contains("<link"));
    }

    /**
     * Nested element markers must appear from parent to child, with correctly escaped identifiers.
     */
    @Test
    void rendersNestedElementMarkersFromOuterToInner() {
        final Element outer = SysMLFactory.eINSTANCE.createPackage();
        ElementUtil.setElementIdOf(outer, "package-<&\"");
        final Element inner = SysMLFactory.eINSTANCE.createPartUsage();
        ElementUtil.setElementIdOf(inner, "part-1");
        final TextOutput output = new HtmlTextOutput(
                new FormattingOptions(LINE_FEED, INDENTATION));

        output.beginElement(outer).beginElement(inner).token("part").space()
                .token("wheel").token(";").endElement(inner).endElement(outer);

        final String html = output.content();
        final int outerMarker = html.indexOf("data-element-type=\"Package\"");
        final int innerMarker = html.indexOf("data-element-type=\"PartUsage\"");
        assertTrue(outerMarker >= 0);
        assertTrue(innerMarker > outerMarker);
        assertTrue(
                html.contains("data-element-id=\"package-&lt;&amp;&quot;\""));
        assertTrue(html.contains("Package - ID: package-&lt;&amp;&quot;"));
        assertTrue(html.contains("PartUsage - ID: part-1"));
    }

    /**
     * HTML must expose the element identifier an element already has, and must not assign one to an element that has
     * none: reading an unassigned SysML element id assigns it.
     */
    @Test
    void reportsOnlyExistingElementIds() {
        final Element identified = SysMLFactory.eINSTANCE.createAttributeUsage();
        final String elementId = ElementUtil.getElementIdOf(identified);
        final Element unidentified = SysMLFactory.eINSTANCE.createAttributeUsage();
        final TextOutput output = new HtmlTextOutput(
                new FormattingOptions(LINE_FEED, INDENTATION));

        output.beginElement(identified).token("attribute").space().token("value")
                .token(";").endElement(identified);
        output.beginElement(unidentified).token("attribute").space().token("other")
                .token(";").endElement(unidentified);

        final String html = output.content();
        assertTrue(html.contains("AttributeUsage - ID: " + elementId));
        assertTrue(html.contains("data-element-id=\"" + elementId + "\""));
        assertTrue(html.contains("data-element-id=\"\""));
        assertTrue(unidentified.eAdapters().isEmpty(), "Rendering must not attach the SysML logic adapter that holds element ids");
    }

    /**
     * A supplementary Unicode character must not shift the highlighting of subsequent tokens.
     */
    @Test
    void keepsLexerAndTraceOffsetsAlignedAfterSupplementaryUnicodeCharacters() {
        final TextOutput output = new HtmlTextOutput(
                new FormattingOptions(LINE_FEED, INDENTATION));

        output.raw("attribute emoji = \"\uD83D\uDE00\"; part next;");

        final String html = output.content();
        assertTrue(
                html.contains("<span class=\"syntax-string\">\"\uD83D\uDE00\"</span>"));
        assertTrue(html.contains("<span class=\"syntax-keyword\">part</span>"));
        assertTrue(
                html.contains("<span class=\"syntax-identifier\">next</span>"));
    }

    /**
     * A mark taken before writing must be zero, and subsequent writes must be detected by changedSince.
     */
    @Test
    void preservesTextOutputMarkSemantics() {
        final TextOutput output = new HtmlTextOutput(
                new FormattingOptions(LINE_FEED, INDENTATION));
        final int mark = output.mark();

        output.token("package").space().token("P")
                .ensureTrailingNewlineIfChanged(mark);

        assertEquals(0, mark);
        assertTrue(output.changedSince(mark));
        assertTrue(output.content()
                .contains("<span class=\"syntax-identifier\">P</span>"));
    }

    /**
     * A reference must expose its target type and identifier without changing the visible name; without a navigation
     * resolver it must not create a link or target anchor.
     */
    @Test
    void exposesReferenceTargetMetadataWithoutChangingVisibleText() {
        final Element target = SysMLFactory.eINSTANCE.createPackage();
        ElementUtil.setElementIdOf(target, "target-1");
        final TextOutput output = new HtmlTextOutput(
                new FormattingOptions(LINE_FEED, INDENTATION));

        output.token("ref").space().referenceName("Target", target).token(";");

        final String html = output.content();
        assertTrue(html.contains("<span class=\"sysml-reference\""));
        assertTrue(html.contains("--sysml-reference: #0b6e4f"));
        assertTrue(html.contains(".sysml-reference .syntax-identifier"));
        assertTrue(html.contains("data-reference-target-type=\"Package\""));
        assertTrue(html.contains("data-reference-target-id=\"target-1\""));
        assertTrue(html
                .contains("<span class=\"syntax-identifier\">Target</span>"));
        assertFalse(html.contains("<a class=\"sysml-reference\""));
        assertFalse(html.contains("id=\"element-target-1\""));
    }

    /**
     * Navigable references must use the supplied href, and emitted elements must receive their anchors.
     */
    @Test
    void rendersNavigableReferencesAndElementAnchors() {
        final Element target = SysMLFactory.eINSTANCE.createPackage();
        ElementUtil.setElementIdOf(target, "target-1");
        final TextOutput output = new HtmlTextOutput(
                new FormattingOptions(LINE_FEED, INDENTATION),
                ignored -> Optional.of("../library/Target.html#element-target-1"));

        output.beginElement(target).token("package").space()
                .referenceName("Target", target).token(";").endElement(target);

        final String html = output.content();
        assertTrue(html.contains("id=\"element-target-1\""));
        assertTrue(html.contains(
                "<a class=\"sysml-reference\" href=\"../library/Target.html#element-target-1\""));
        assertTrue(html.contains("</a>"));
    }

    /**
     * A reference written after a line comment must stay linked whatever the comment's line ending, as the lexer ends
     * the comment at CR, LF or CRLF.
     */
    @ParameterizedTest
    @ValueSource(strings = { "\n", "\r\n", "\r" })
    void linksReferenceAfterLineComment(final String lineEnding) {
        final Element target = SysMLFactory.eINSTANCE.createPartUsage();
        final TextOutput output = new HtmlTextOutput(
                new FormattingOptions(LINE_FEED, INDENTATION),
                ignored -> Optional.of("#target"));

        output.referenceText("x + // note" + lineEnding + "target", List.of(new TextOutput.Reference("target", target)));

        assertTrue(output.content().contains("href=\"#target\""), output.content());
    }

    /**
     * A reference without a navigation destination must remain a span and must not become a link.
     */
    @Test
    void keepsUnnavigableReferencesAsSpans() {
        final Element target = SysMLFactory.eINSTANCE.createPackage();
        ElementUtil.setElementIdOf(target, "target-1");
        final TextOutput output = new HtmlTextOutput(
                new FormattingOptions(LINE_FEED, INDENTATION),
                ignored -> Optional.empty());

        output.referenceName("Target", target);

        final String html = output.content();
        assertTrue(html.contains("<span class=\"sysml-reference\""));
        assertFalse(html.contains("<a class=\"sysml-reference\""));
    }

    /**
     * A contained target without its own textual declaration must receive an invisible anchor so internal links can
     * reach it.
     */
    @Test
    void anchorsContainedTargetsOmittedFromSerializedText() {
        final Element outer = SysMLFactory.eINSTANCE.createPackage();
        ElementUtil.setElementIdOf(outer, "outer");
        final Element omittedTarget = SysMLFactory.eINSTANCE.createFeature();
        ElementUtil.setElementIdOf(omittedTarget, "implicit");
        final OwningMembership membership =
                SysMLFactory.eINSTANCE.createOwningMembership();
        membership.setOwnedMemberElement(omittedTarget);
        outer.getOwnedRelationship().add(membership);
        final TextOutput output = new HtmlTextOutput(
                new FormattingOptions(LINE_FEED, INDENTATION),
                ignored -> Optional.of("#element-implicit"));

        output.beginElement(outer).referenceName("implicit", omittedTarget)
                .endElement(outer);

        final String html = output.content();
        assertTrue(html.contains("href=\"#element-implicit\""));
        assertTrue(html.contains(
                "<span id=\"element-implicit\" aria-hidden=\"true\"></span>"));
        assertTrue(html.contains(
                ".sysml-object-gutter > [aria-hidden=\"true\"] { position: absolute; }"));
    }

    /**
     * A target owned by an implied relationship must not receive a navigation link or an anchor.
     */
    @Test
    void doesNotLinkToElementOwnedByImpliedRelationship() {
        final Element outer = SysMLFactory.eINSTANCE.createPackage();
        ElementUtil.setElementIdOf(outer, "outer");
        final Element hiddenTarget = SysMLFactory.eINSTANCE.createFeature();
        ElementUtil.setElementIdOf(hiddenTarget, "implicit");
        final OwningMembership membership =
                SysMLFactory.eINSTANCE.createOwningMembership();
        membership.setIsImplied(true);
        membership.setOwnedMemberElement(hiddenTarget);
        outer.getOwnedRelationship().add(membership);
        final TextOutput output = new HtmlTextOutput(
                new FormattingOptions(LINE_FEED, INDENTATION),
                ignored -> Optional.of("#element-implicit"));

        output.beginElement(outer).referenceName("implicit", hiddenTarget)
                .endElement(outer);

        final String html = output.content();
        assertFalse(html.contains("href=\"#element-implicit\""), html);
        assertFalse(html.contains("id=\"element-implicit\""), html);
    }

    /**
     * A reference without a target must expose the fallback target type and an empty target identifier.
     */
    @Test
    void preservesReferenceMetadataFallbackForDanglingNames() {
        final TextOutput output = new HtmlTextOutput(
                new FormattingOptions(LINE_FEED, INDENTATION));

        output.referenceName("Missing::Target", null);

        final String html = output.content();
        assertTrue(
                html.contains("data-reference-target-type=\"&lt;none&gt;\""));
        assertTrue(html.contains("data-reference-target-id=\"\""));
    }

    /**
     * A reference to a proxy must expose the proxy URI in its HTML metadata.
     */
    @Test
    void exposesProxyUriForProxyReferenceTargets() {
        final Element target = SysMLFactory.eINSTANCE.createPackage();
        ((InternalEObject) target)
                .eSetProxyURI(URI.createURI("sysml:/model.sysml#Target"));
        final TextOutput output = new HtmlTextOutput(
                new FormattingOptions(LINE_FEED, INDENTATION));

        output.referenceName("Target", target);

        assertTrue(output.content().contains(
                "data-reference-target-uri=\"sysml:/model.sysml#Target\""));
    }

    /**
     * A reference inside composite text must receive its target metadata without removing surrounding text.
     */
    @Test
    void marksReferencesInsideCompositeText() {
        final Element target = SysMLFactory.eINSTANCE.createPackage();
        ElementUtil.setElementIdOf(target, "target-2");
        final TextOutput output = new HtmlTextOutput(
                new FormattingOptions(LINE_FEED, INDENTATION));

        output.referenceText("source + Target",
                List.of(new TextOutput.Reference("Target", target)));

        final String html = output.content();
        assertTrue(html.contains("data-reference-target-id=\"target-2\""));
        assertTrue(html.contains("source"));
        assertTrue(html.contains("<span class=\"sysml-reference\""));
    }

    /**
     * A reference occurrence must be matched on a whole token: it must not bind to the content of a string literal,
     * to part of a longer identifier, or to a dot inside a quoted name.
     */
    @Test
    void matchesReferencesOnWholeTokens() {
        final Element target = SysMLFactory.eINSTANCE.createPackage();
        ElementUtil.setElementIdOf(target, "target-a");
        final TextOutput output = new HtmlTextOutput(
                new FormattingOptions(LINE_FEED, INDENTATION));

        output.referenceText("\"a\" + ab + a + 'x.a'",
                List.of(new TextOutput.Reference("a", target), new TextOutput.Reference("P::'x.a'", target)));

        final String html = output.content();
        final String referenceStart = "title=\"Reference -&gt; Package - target-a\"><span class=\"syntax-identifier\">";
        assertTrue(html.contains("<code class=\"sysml-code\"><span class=\"syntax-string\">\"a\"</span>"), html);
        assertTrue(html.contains("<span class=\"syntax-identifier\">ab</span>"), html);
        assertTrue(html.contains(referenceStart + "a</span></span> <span class=\"syntax-operator\">+</span>"), html);
        assertTrue(html.contains(referenceStart + "'x.a'</span></span></code>"), html);
    }
    /** Comment text must not consume the reference occurrence following it. */
    @Test
    void skipsCommentsWhenMatchingReferences() {
        for (final String comment : List.of("/* target */", "//* target */", "// target\n")) {
            final Element target = SysMLFactory.eINSTANCE.createFeature();
            final TextOutput output = new HtmlTextOutput(new FormattingOptions(LINE_FEED, INDENTATION));
            output.referenceText(comment + " target", List.of(new TextOutput.Reference("target", target)));

            final String html = output.content();
            final int reference = html.indexOf("<span class=\"sysml-reference\"");
            final int endOfComment = html.indexOf("target");
            assertTrue(reference > endOfComment, html);
        }
    }

}
