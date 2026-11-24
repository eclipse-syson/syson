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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.junit.jupiter.api.Test;
import org.omg.sysml.lang.sysml.AttributeDefinition;
import org.omg.sysml.lang.sysml.DataType;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.FeatureValue;
import org.omg.sysml.lang.sysml.Multiplicity;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.Subclassification;
import org.omg.sysml.lang.sysml.Subsetting;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;
import org.omg.sysml.util.ElementUtil;

/**
 * Verifies that semantic relationships marked as implied do not leak into
 * textual or HTML serialization.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class ImpliedRelationshipSerializationTest {

    private static final String WHITESPACE = "\\s+";

    private static final String DATA_VALUE_ID =
            "eeef8c48-5018-5f4f-b1d7-a199b05d86ed";

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * Implied typing, subclassification, subsetting, and redefinition relationships must not produce textual
     * specialization clauses.
     */
    @Test
    void omitsImpliedSpecializationClauses() throws Exception {
        final String source = """
                package P {
                    part def Base;
                    part def Child :> Base;
                    part typed : Base;
                    part subset :> typed;
                    part redefined :>> typed;
                }
                """;
        final var result = parse(source);
        markMatchingRelationshipsImplied(result.model(), relationship ->
            relationship instanceof FeatureTyping
                    || relationship instanceof Subclassification
                    || relationship instanceof Subsetting
                    || relationship instanceof Redefinition);

        final String serialized = new SysMLSerializer().serialize(result);

        assertEqualIgnoringWhitespace("""
                package P {
                    part def Base;
                    part def Child;
                    part typed;
                    part subset;
                    part redefined;
                }
                """, serialized);
    }

    /**
     * An element owned by an implied relationship must not produce a textual declaration or an HTML anchor.
     */
    @Test
    void omitsElementsOwnedByAnImpliedRelationship() throws Exception {
        final var result = parse("""
                package P {
                    part visible;
                    part hidden;
                }
                """);
        final OwningMembership hiddenMembership = elementsInSubtree(result.model())
                .filter(OwningMembership.class::isInstance)
                .map(OwningMembership.class::cast)
                .filter(membership -> "hidden".equals(
                        membership.getOwnedMemberElement().getDeclaredName()))
                .findFirst().orElseThrow();
        final Element hidden = hiddenMembership.getOwnedMemberElement();
        hiddenMembership.setIsImplied(true);

        final SysMLSerializer serializer = new SysMLSerializer();
        final String serialized = serializer.serialize(result);
        final String html = serializer.serializeToHtml(result);

        assertEqualIgnoringWhitespace("""
                package P {
                    part visible;
                }
                """, serialized);
        assertFalse(html.contains(
                "element-" + ElementUtil.getElementIdOf(hidden)), html);
    }

    /**
     * An implied specialization to a proxy must not produce a textual clause or HTML metadata exposing the
     * relationship or target identifiers.
     */
    @Test
    void omitsImpliedProxySpecializationFromTextAndHtml() throws Exception {
        final var result = parse("attribute def SystemOfQuantities;");
        final AttributeDefinition definition = elementsInSubtree(result.model())
                .filter(AttributeDefinition.class::isInstance)
                .map(AttributeDefinition.class::cast)
                .findFirst().orElseThrow();
        final DataType dataValueProxy = SysMLFactory.eINSTANCE.createDataType();
        ((InternalEObject) dataValueProxy).eSetProxyURI(
                URI.createURI("Base.kermlx#" + DATA_VALUE_ID));
        final Subclassification implied =
                SysMLFactory.eINSTANCE.createSubclassification();
        implied.setElementId("implied-subclassification");
        implied.setIsImplied(true);
        implied.setSubclassifier(definition);
        implied.setSuperclassifier(dataValueProxy);
        definition.getOwnedRelationship().add(implied);

        final SysMLSerializer serializer = new SysMLSerializer();
        final String text = serializer.serialize(result);
        final String html = serializer.serializeToHtml(result);

        assertEqualIgnoringWhitespace("attribute def SystemOfQuantities;", text);
        assertFalse(html.contains(DATA_VALUE_ID), html);
        assertFalse(html.contains("element-implied-subclassification"), html);
    }

    /**
     * Inserting implied relationships among explicit ones must not change the serialized text or the order of explicit
     * clauses.
     */
    @Test
    void preservesExplicitClauseOrderAmongImpliedRelationships() throws Exception {
        final var result = parse("""
                package P {
                    item firstTarget;
                    item secondTarget;
                    item source subsets firstTarget [1] subsets secondTarget;
                }
                """);
        final SysMLSerializer serializer = new SysMLSerializer();
        final String expected = serializer.serialize(result);
        final Element source = elementsInSubtree(result.model())
                .filter(element -> "source".equals(element.getDeclaredName()))
                .findFirst().orElseThrow();
        final int relationshipCount = source.getOwnedRelationship().size();
        // Insert backwards so positions still refer to the original relationships.
        for (int index = relationshipCount; index >= 0; index--) {
            final FeatureTyping implied = SysMLFactory.eINSTANCE.createFeatureTyping();
            implied.setIsImplied(true);
            source.getOwnedRelationship().add(index, implied);
        }

        assertEquals(expected, serializer.serialize(result));
    }

    /**
     * Feature values and multiplicities carried by implied relationships must not be exported.
     */
    @Test
    void omitsImpliedFeatureValuesAndMultiplicity() throws Exception {
        final var result = parse("""
                package P {
                    attribute value = 42;
                    part many[0..*];
                }
                """);
        markMatchingRelationshipsImplied(result.model(), relationship -> relationship instanceof FeatureValue
                || relationship.getOwnedRelatedElement().stream()
                        .anyMatch(Multiplicity.class::isInstance));

        assertEqualIgnoringWhitespace("""
                package P {
                    attribute value;
                    part many;
                }
                """, new SysMLSerializer().serialize(result));
    }

    /**
     * An implied relationship used as the root must produce empty text and an empty HTML code block without a
     * relationship marker.
     */
    @Test
    void omitsAnImpliedRelationshipUsedAsTheSerializationRoot() {
        final Relationship implied =
                SysMLFactory.eINSTANCE.createNamespaceImport();
        implied.setIsImplied(true);

        assertEquals("", new SysMLSerializer().serialize(implied));
        final String html = new SysMLSerializer().serializeToHtml(implied);
        assertTrue(html.contains("<code class=\"sysml-code\"></code>"), html);
        assertFalse(html.contains("data-element-type=\"NamespaceImport\""),
                html);
    }

    private static ISysMLParseResult<? extends Element> parse(
            final String source) throws Exception {
        return SerializerRoundTripTestSupport.parseDocument(source);
    }

    private static void markMatchingRelationshipsImplied(final Element root,
            final Predicate<Relationship> predicate) {
        elementsInSubtree(root).filter(Relationship.class::isInstance)
                .map(Relationship.class::cast).filter(predicate)
                .forEach(relationship -> relationship.setIsImplied(true));
    }

    private static Stream<Element> elementsInSubtree(
            final Element root) {
        final var descendants = root.eAllContents();
        final Stream.Builder<Element> elements =
                Stream.builder();
        elements.add(root);
        descendants.forEachRemaining(object -> {
            if (object instanceof final Element element) {
                elements.add(element);
            }
        });
        return elements.build();
    }

    private static void assertEqualIgnoringWhitespace(final String expected,
            final String actual) {
        assertEquals(expected.replaceAll(WHITESPACE, ""),
                actual.replaceAll(WHITESPACE, ""));
    }
}
