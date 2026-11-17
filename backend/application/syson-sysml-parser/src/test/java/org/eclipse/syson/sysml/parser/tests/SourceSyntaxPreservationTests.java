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
package org.eclipse.syson.sysml.parser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.parser.adapter.ICommentAdapter;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.parser.adapter.ParsedComment;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.sysml.lang.sysml.ConnectionUsage;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.ReferenceUsage;
import org.omg.sysml.lang.sysml.MetadataUsage;
import org.omg.sysml.lang.sysml.InvocationExpression;
import org.omg.sysml.lang.sysml.ParameterMembership;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.NullExpression;
import org.omg.sysml.lang.sysml.Package;

/**
 * Checks concrete spelling markers independently of later serializer output.
 * These adapters preserve source choices that the semantic model cannot distinguish.
 *
 * @author cbrun
 */
public class SourceSyntaxPreservationTests {

    @BeforeClass
    public static void setUp() {
        AbstractSysMLRuleTestSuite.beforeClass();
    }

    @AfterClass
    public static void tearDown() {
        AbstractSysMLRuleTestSuite.afterClass();
    }

    @Test
    public void distinguishesTheColonOnAnonymousMetadata() throws Exception {
        ISysMLParseResult<MetadataUsage> withColon = new SysMLModelParser().parseMetadataUsage(input("metadata : Marker;"), StandardCharsets.UTF_8);
        ISysMLParseResult<MetadataUsage> withoutColon = new SysMLModelParser().parseMetadataUsage(input("metadata Marker;"), StandardCharsets.UTF_8);

        assertEquals(Diagnostic.OK, withColon.diagnostic().getSeverity());
        assertEquals(Diagnostic.OK, withoutColon.diagnostic().getSeverity());
        assertTrue(withColon.model().eAdapters().contains(ITextAdapter.METADATA_COLON));
        assertFalse(withoutColon.model().eAdapters().contains(ITextAdapter.METADATA_COLON));
        assertEquals(withoutColon.model().getOwnedRelationship().size(), withColon.model().getOwnedRelationship().size());
    }

    @Test
    public void distinguishesTheOptionalConnectionKeyword() throws Exception {
        String ends = "connect a references source to b references target;";
        ISysMLParseResult<ConnectionUsage> explicit = new SysMLModelParser().parseConnectionUsage(input("connection " + ends), StandardCharsets.UTF_8);
        ISysMLParseResult<ConnectionUsage> implicit = new SysMLModelParser().parseConnectionUsage(input(ends), StandardCharsets.UTF_8);

        assertEquals(Diagnostic.OK, explicit.diagnostic().getSeverity());
        assertEquals(Diagnostic.OK, implicit.diagnostic().getSeverity());
        assertTrue(explicit.model().eAdapters().contains(ITextAdapter.CONNECTION_KEYWORD));
        assertFalse(implicit.model().eAdapters().contains(ITextAdapter.CONNECTION_KEYWORD));
        assertEquals(implicit.model().getOwnedRelationship().size(), explicit.model().getOwnedRelationship().size());
    }

    @Test
    public void retainsFinalCommentsInANonemptyBody() throws Exception {
        this.checkFinalBodyComments("package P { attribute x; // first\n //* second */\n }", 1);
    }

    @Test
    public void retainsCommentsInAnOtherwiseEmptyBody() throws Exception {
        this.checkFinalBodyComments("package P { // first\n //* second */\n }", 0);
    }

    private void checkFinalBodyComments(String source, int memberCount) throws Exception {
        ISysMLParseResult<Namespace> parsed = new SysMLModelParser().parseRootNamespace(input(source), StandardCharsets.UTF_8);
        assertEquals(Diagnostic.OK, parsed.diagnostic().getSeverity());
        Package body = (Package) parsed.model().getOwnedRelationship().get(0).getOwnedRelatedElement().get(0);
        assertEquals(memberCount, body.getOwnedRelationship().size());
        ICommentAdapter comments = (ICommentAdapter) body.eAdapters().stream().filter(ICommentAdapter.class::isInstance).findFirst().orElseThrow();
        assertEquals(List.of("// first", "//* second */"), comments.getTrailingComments().stream().map(ParsedComment::text).toList());
    }

    @Test
    public void retainsQuotingOnTheNamedArgumentRedefinition() throws Exception {
        ISysMLParseResult<Feature> quoted = new SysMLModelParser().parseNamedArgument(input("'feature' = \"mass\""), StandardCharsets.UTF_8);
        ISysMLParseResult<Feature> unquoted = new SysMLModelParser().parseNamedArgument(input("featureName = \"mass\""), StandardCharsets.UTF_8);
        assertEquals(Diagnostic.OK, quoted.diagnostic().getSeverity());
        assertEquals(Diagnostic.OK, unquoted.diagnostic().getSeverity());
        Redefinition quotedReference = (Redefinition) quoted.model().getOwnedRelationship().stream()
                .filter(Redefinition.class::isInstance).findFirst().orElseThrow();
        Redefinition unquotedReference = (Redefinition) unquoted.model().getOwnedRelationship().stream()
                .filter(Redefinition.class::isInstance).findFirst().orElseThrow();
        assertTrue(quotedReference.eAdapters().contains(ITextAdapter.QUOTED_NAME));
        assertFalse(unquotedReference.eAdapters().contains(ITextAdapter.QUOTED_NAME));
    }

    @Test
    public void retainsTypingClauseBoundariesWithoutChangingTypingCount() throws Exception {
        ISysMLParseResult<?> separate = new SysMLModelParser().parseReferenceUsage(input("ref value : A, B : C;"), StandardCharsets.UTF_8);
        ISysMLParseResult<?> joined = new SysMLModelParser().parseReferenceUsage(input("ref value : A, B, C;"), StandardCharsets.UTF_8);
        assertEquals(Diagnostic.OK, separate.diagnostic().getSeverity());
        assertEquals(Diagnostic.OK, joined.diagnostic().getSeverity());
        List<FeatureTyping> separateTypings = ((Feature) separate.model()).getOwnedRelationship().stream()
                .filter(FeatureTyping.class::isInstance).map(FeatureTyping.class::cast).toList();
        List<FeatureTyping> joinedTypings = ((Feature) joined.model()).getOwnedRelationship().stream()
                .filter(FeatureTyping.class::isInstance).map(FeatureTyping.class::cast).toList();
        assertEquals(3, separateTypings.size());
        assertEquals(3, joinedTypings.size());
        assertEquals(List.of(true, false, true), separateTypings.stream().map(t -> t.eAdapters().contains(ITextAdapter.TYPING_CLAUSE_START)).toList());
        assertEquals(List.of(true, false, false), joinedTypings.stream().map(t -> t.eAdapters().contains(ITextAdapter.TYPING_CLAUSE_START)).toList());
    }

    @Test
    public void retainsExplicitStateAndRepresentationKeywords() throws Exception {
        SysMLModelParser parser = new SysMLModelParser();
        assertMarker(parser.parseExhibitStateUsage(input("exhibit state a;"), StandardCharsets.UTF_8), ITextAdapter.EXPLICIT_STATE_USAGE_KEYWORD, true);
        assertMarker(parser.parseExhibitStateUsage(input("exhibit a;"), StandardCharsets.UTF_8), ITextAdapter.EXPLICIT_STATE_USAGE_KEYWORD, false);
        assertMarker(parser.parseTextualRepresentation(input("rep language \"text\" /* body */"), StandardCharsets.UTF_8), ITextAdapter.EXPLICIT_REP_KEYWORD, true);
        assertMarker(parser.parseTextualRepresentation(input("language \"text\" /* body */"), StandardCharsets.UTF_8), ITextAdapter.EXPLICIT_REP_KEYWORD, false);
    }

    @Test
    public void retainsOmittedDeclarationsAndBlockBodies() throws Exception {
        SysMLModelParser parser = new SysMLModelParser();
        assertMarker(parser.parseAssertConstraintUsage(input("assert a;"), StandardCharsets.UTF_8), ITextAdapter.DECLARATION_KEYWORD_OMITTED, true);
        assertMarker(parser.parseAssertConstraintUsage(input("assert constraint a;"), StandardCharsets.UTF_8), ITextAdapter.DECLARATION_KEYWORD_OMITTED, false);
        assertMarker(parser.parseCalculationDefinition(input("calc def C {}"), StandardCharsets.UTF_8), ITextAdapter.BODY_BLOCK, true);
        assertMarker(parser.parseCalculationDefinition(input("calc def C;"), StandardCharsets.UTF_8), ITextAdapter.BODY_BLOCK, false);
    }

    @Test
    public void retainsOptionalBindingAndInterfaceConnectKeywords() throws Exception {
        SysMLModelParser parser = new SysMLModelParser();
        assertMarker(parser.parseBindingConnectorAsUsage(input("binding bind a = b;"), StandardCharsets.UTF_8), ITextAdapter.EXPLICIT_DECLARATION_KEYWORD, true);
        assertMarker(parser.parseBindingConnectorAsUsage(input("bind a = b;"), StandardCharsets.UTF_8), ITextAdapter.EXPLICIT_DECLARATION_KEYWORD, false);
        assertMarker(parser.parseInterfaceUsage(input("interface connect a to b;"), StandardCharsets.UTF_8), ITextAdapter.EXPLICIT_INTERFACE_CONNECT_KEYWORD, true);
        assertMarker(parser.parseInterfaceUsage(input("interface a to b;"), StandardCharsets.UTF_8), ITextAdapter.EXPLICIT_INTERFACE_CONNECT_KEYWORD, false);
    }

    @Test
    public void retainsModifierOrderWithTheSameFeatureProperties() throws Exception {
        ISysMLParseResult<ReferenceUsage> orderedFirst = new SysMLModelParser().parseReferenceUsage(input("ref value ordered nonunique;"), StandardCharsets.UTF_8);
        ISysMLParseResult<ReferenceUsage> nonuniqueFirst = new SysMLModelParser().parseReferenceUsage(input("ref value nonunique ordered;"), StandardCharsets.UTF_8);
        assertMarker(orderedFirst, ITextAdapter.NONUNIQUE_AFTER_ORDERED, true);
        assertMarker(nonuniqueFirst, ITextAdapter.NONUNIQUE_AFTER_ORDERED, false);
        assertTrue(orderedFirst.model().isOrdered());
        assertTrue(nonuniqueFirst.model().isOrdered());
        assertFalse(orderedFirst.model().isUnique());
        assertFalse(nonuniqueFirst.model().isUnique());
    }

    @Test
    public void distinguishesEmptyParenthesesFromTheNullKeyword() throws Exception {
        SysMLModelParser parser = new SysMLModelParser();
        ISysMLParseResult<NullExpression> parentheses = parser.parseNullExpression(input("()"), StandardCharsets.UTF_8);
        ISysMLParseResult<NullExpression> keyword = parser.parseNullExpression(input("null"), StandardCharsets.UTF_8);
        assertMarker(parentheses, ITextAdapter.EMPTY_PARENTHESIZED_EXPRESSION, true);
        assertMarker(keyword, ITextAdapter.EMPTY_PARENTHESIZED_EXPRESSION, false);
        assertTrue(parentheses.model().eAdapters().contains(ITextAdapter.PARENTHESIZED_EXPRESSION));
        assertFalse(keyword.model().eAdapters().contains(ITextAdapter.PARENTHESIZED_EXPRESSION));
        assertEquals(keyword.model().eClass(), parentheses.model().eClass());
    }

    @Test
    public void keepsTrailingArgumentCommentsWithTheirArgument() throws Exception {
        ISysMLParseResult<InvocationExpression> parsed = new SysMLModelParser().parseInvocationExpression(
                input("f(a //* first */, (b + c) //* second */)"), StandardCharsets.UTF_8);
        assertEquals(Diagnostic.OK, parsed.diagnostic().getSeverity());
        List<Feature> arguments = parsed.model().getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .flatMap(membership -> membership.getOwnedRelatedElement().stream())
                .filter(Feature.class::isInstance).map(Feature.class::cast).toList();
        assertEquals(2, arguments.size());
        assertEquals(List.of("//* first */"), trailingCommentText(arguments.get(0)));
        assertEquals(List.of("//* second */"), trailingCommentText(arguments.get(1)));
    }

    private static List<String> trailingCommentText(EObject element) {
        return element.eAdapters().stream().filter(ICommentAdapter.class::isInstance).map(ICommentAdapter.class::cast)
                .flatMap(adapter -> adapter.getTrailingComments().stream()).map(ParsedComment::text).toList();
    }

    private static void assertMarker(ISysMLParseResult<? extends EObject> parsed, ITextAdapter marker, boolean expected) {
        assertEquals(Diagnostic.OK, parsed.diagnostic().getSeverity());
        assertEquals(expected, parsed.model().eAdapters().contains(marker));
    }

    private static ByteArrayInputStream input(String source) {
        return new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
    }
}
