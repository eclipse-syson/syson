/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.sysml.metamodel.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.util.SysMLTestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests the identity, category and membership role of materialized effective sources.
 *
 * @author Arthur Daussy
 */
public class FeatureChainRedefinitionMetaclassTest {

    private static final String MODEL = "EffectiveRedefinitionSources::";

    private static final String PLAIN = MODEL + "plain";

    private static final String ROOT_CHILD = MODEL + "Root::child";

    private static final String LEAF_VALUE = MODEL + "Leaf::value";

    private final MetamodelMutationElementService service = new MetamodelMutationElementService();

    private final MetamodelQueryElementService queryService = new MetamodelQueryElementService();

    /**
     * KerML 1.1 Beta 2, 7.3.4.5, removes replaced features from inherited memberships.
     * The service targets the effective object selected by the query, including renamed, anonymous, transitive and diamond cases.
     */
    @ParameterizedTest
    @CsvSource({
        "renamed, Renamed::renamed",
        "transitive, Transitive::finalValue",
        "anonymous, Anonymous::amount",
        "diamond, Right::rightAmount",
        "reverseDiamond, Right::rightAmount"
    })
    @DisplayName("GIVEN an effective inherited redefinition, WHEN materializing its selected chain, THEN the new relationship targets that effective source")
    public void effectiveAttributeSource(String contextName, String sourceName) throws IOException {
        var model = this.loadMetaclassModel();
        var context = model.get(PartUsage.class, MODEL + contextName);
        var source = model.get(AttributeUsage.class, MODEL + sourceName);
        var original = model.get(AttributeUsage.class, MODEL + "Root::amount");
        var sourceRelationships = List.copyOf(source.getOwnedRelationship());
        var selections = this.queryService.streamFeatureChains(context, null, (root, chain) -> chain.getLast() == source).toList();
        assertThat(selections).containsExactly(List.of(source));
        assertThat(source).isNotSameAs(original);

        var result = this.service.redefineFeatureChain(context, selections.getFirst());

        this.assertSuccess(result);
        assertThat(result.chain()).singleElement().satisfies(local -> {
            this.assertLocalRedefinition(local, context, source);
            assertThat(local.getOwnedRedefinition()).extracting(redefinition -> redefinition.getRedefinedFeature()).doesNotContain(original);
        });
        assertThat(source.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
    }

    /**
     * Service contract: new features retain the selected source's exact metaclass, without copying typings or adding category defaults.
     */
    @ParameterizedTest
    @CsvSource({"child, PartUsage", "amount, AttributeUsage", "reference, ReferenceUsage", "endpoint, PortUsage", "work, ActionUsage"})
    @DisplayName("GIVEN a supported inherited usage category, WHEN materializing it, THEN the exact metaclass is retained without names or default children")
    public void exactMetaclass(String sourceName, String metaclass) throws IOException {
        var model = this.loadMetaclassModel();
        var context = model.get(PartUsage.class, PLAIN);
        var source = model.get(Feature.class, MODEL + "Root::" + sourceName);
        assertThat(source.eClass().getName()).isEqualTo(metaclass);
        var sourceRelationships = List.copyOf(source.getOwnedRelationship());

        var result = this.service.redefineFeatureChain(context, List.of(source));

        this.assertSuccess(result);
        assertThat(result.chain()).singleElement().satisfies(local -> {
            this.assertLocalRedefinition(local, context, source);
            assertThat(local.getOwnedRelationship()).containsExactlyElementsOf(local.getOwnedRedefinition());
            assertThat(local.getOwnedFeature()).isEmpty();
            assertThat(local.getOwnedTyping()).isEmpty();
            assertThat(local.getOwningFeatureMembership().eClass()).isSameAs(SysmlPackage.eINSTANCE.getFeatureMembership());
            assertThat(local.isIsComposite()).isEqualTo(source.isIsComposite());
        });
        assertThat(source.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
    }

    /**
     * The selected category must be retained through intermediate contexts as well as at the terminal feature (service contract).
     */
    @ParameterizedTest
    @CsvSource({"child, Leaf::value", "reference, Leaf::value", "endpoint, Endpoint::signal", "work, Work::setting"})
    @DisplayName("GIVEN a usage exposing an inherited attribute, WHEN materializing both segments, THEN the prefix retains its category and owns only the requested attribute")
    public void typedIntermediateCategory(String prefixName, String terminalName) throws IOException {
        var model = this.loadMetaclassModel();
        var context = model.get(PartUsage.class, PLAIN);
        var prefix = model.get(Feature.class, MODEL + "Root::" + prefixName);
        var terminal = model.get(AttributeUsage.class, MODEL + terminalName);

        var result = this.service.redefineFeatureChain(context, List.of(prefix, terminal));

        this.assertSuccess(result);
        assertThat(result.chain()).hasSize(2);
        var localPrefix = result.chain().getFirst();
        var localTerminal = result.chain().getLast();
        this.assertLocalRedefinition(localPrefix, context, prefix);
        this.assertLocalRedefinition(localTerminal, localPrefix, terminal);
        assertThat(localPrefix.getOwnedFeature()).containsExactly(localTerminal);
        assertThat(localPrefix.getOwnedRelationship()).hasSize(2);
        assertThat(localTerminal.getOwnedFeature()).isEmpty();
    }

    /**
     * SysML 2.1 Beta 2, 8.2.2.6.3, parses a category-free usage as ReferenceUsage.
     * Retaining that selected metaclass rather than the category of an older redefined source is service policy.
     */
    @Test
    @DisplayName("GIVEN an abbreviated redefinition of an attribute, WHEN materializing it, THEN the new feature remains a ReferenceUsage")
    public void abbreviatedAttributeRedefinition() throws IOException {
        var model = this.loadMetaclassModel();
        var context = model.get(PartUsage.class, MODEL + "abbreviated");
        var source = model.get(ReferenceUsage.class, MODEL + "Abbreviated::amount");
        var original = model.get(AttributeUsage.class, MODEL + "Root::amount");
        assertThat(source.allRedefinedFeatures()).contains(original);

        var result = this.service.redefineFeatureChain(context, List.of(source));

        this.assertSuccess(result);
        assertThat(result.chain()).singleElement().satisfies(local -> {
            this.assertLocalRedefinition(local, context, source);
            assertThat(local).isInstanceOf(ReferenceUsage.class);
            assertThat(local.isIsComposite()).isFalse();
        });
    }

    /**
     * Existing local features are reused without conversion, whether the selection names the original part or the effective reference (service contract).
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN a local ReferenceUsage redefining a part, WHEN materializing an original or effective child.value selection, THEN the same reference is reused")
    public void reuseDifferentMetaclass(boolean originalSelection) throws IOException {
        var model = this.loadMetaclassModel();
        var context = model.get(PartUsage.class, MODEL + "localReference");
        var localReference = model.get(ReferenceUsage.class, MODEL + "localReference::child");
        var original = model.get(PartUsage.class, ROOT_CHILD);
        var value = model.get(AttributeUsage.class, LEAF_VALUE);
        var prefixRelationships = List.copyOf(localReference.getOwnedRelationship());
        List<Feature> selection = List.of(original, value);
        if (!originalSelection) {
            var selections = this.queryService.streamFeatureChains(context, null,
                    (root, chain) -> chain.getFirst() == localReference && chain.getLast() == value).toList();
            assertThat(selections).containsExactly(List.of(localReference, value));
            selection = selections.getFirst();
        }

        var result = this.service.redefineFeatureChain(context, selection);

        this.assertSuccess(result);
        assertThat(result.chain()).hasSize(2);
        assertThat(result.chain().getFirst()).isSameAs(localReference);
        this.assertLocalRedefinition(result.chain().getLast(), localReference, value);
        assertThat(localReference.isIsComposite()).isFalse();
        assertThat(localReference.getOwnedRelationship()).containsAll(prefixRelationships).hasSize(prefixRelationships.size() + 1);
        assertThat(context.getOwnedFeature()).containsExactly(localReference);
    }

    /**
     * SysML 2.1 Beta 2, 8.3.12.6, permits composite ports within ports while ports in ordinary part contexts must be referential.
     */
    @Test
    @DisplayName("GIVEN a nested port, WHEN materializing endpoint.nested, THEN the outer port remains referential and the nested port remains composite")
    public void nestedPortComposition() throws IOException {
        var model = this.loadMetaclassModel();
        var context = model.get(PartUsage.class, PLAIN);
        var endpoint = model.get(Feature.class, MODEL + "Root::endpoint");
        var nested = model.get(Feature.class, MODEL + "Endpoint::nested");
        assertThat(endpoint.isIsComposite()).isFalse();
        assertThat(nested.isIsComposite()).isTrue();

        var result = this.service.redefineFeatureChain(context, List.of(endpoint, nested));

        this.assertSuccess(result);
        assertThat(result.chain()).hasSize(2);
        this.assertLocalRedefinition(result.chain().getFirst(), context, endpoint);
        this.assertLocalRedefinition(result.chain().getLast(), result.chain().getFirst(), nested);
        assertThat(result.chain().getFirst().isIsComposite()).isFalse();
        assertThat(result.chain().getLast().isIsComposite()).isTrue();
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.8, requires redefining connector ends to remain ends.
     * A body declaration using the end keyword has an ordinary FeatureMembership.
     */
    @Test
    @DisplayName("GIVEN an inherited end declared in a connection body, WHEN materializing it, THEN its ordinary membership and end flag are preserved")
    public void endInConnectionBody() throws IOException {
        var model = this.loadMetaclassModel();
        var context = model.get(ConnectionUsage.class, MODEL + "link");
        var source = model.get(PartUsage.class, MODEL + "Link::source");
        assertThat(source.getOwningFeatureMembership().eClass()).isSameAs(SysmlPackage.eINSTANCE.getFeatureMembership());

        var result = this.service.redefineFeatureChain(context, List.of(source));

        this.assertSuccess(result);
        assertThat(result.chain()).singleElement().satisfies(local -> {
            this.assertLocalRedefinition(local, context, source);
            assertThat(local.getOwningFeatureMembership().eClass()).isSameAs(SysmlPackage.eINSTANCE.getFeatureMembership());
            assertThat(local.isIsEnd()).isTrue();
            assertThat(local.isIsComposite()).isFalse();
            assertThat(context.getConnectorEnd()).contains(local);
        });
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.3, gives connector ends declared in a connect clause an EndFeatureMembership.
     * Retaining this role preserves their end semantics when materializing an inherited end.
     */
    @Test
    @DisplayName("GIVEN an inherited end from a connect clause, WHEN materializing it, THEN its EndFeatureMembership and end flag are preserved")
    public void endMembership() throws IOException {
        var model = this.loadMetaclassModel();
        var context = model.get(ConnectionUsage.class, MODEL + "wireCopy");
        var wire = model.get(ConnectionUsage.class, MODEL + "Wired::wire");
        var source = wire.getConnectorEnd().getFirst();
        assertThat(source.getOwningFeatureMembership()).isInstanceOf(EndFeatureMembership.class);

        var result = this.service.redefineFeatureChain(context, List.of(source));

        this.assertSuccess(result);
        assertThat(result.chain()).singleElement().satisfies(local -> {
            this.assertLocalRedefinition(local, context, source);
            assertThat(local.getOwningFeatureMembership()).isInstanceOf(EndFeatureMembership.class);
            assertThat(local.isIsEnd()).isTrue();
            assertThat(local.isIsComposite()).isFalse();
            assertThat(context.getConnectorEnd()).contains(local);
        });
    }

    /**
     * KerML 1.1 Beta 2, 7.3.4.5, allows only one redefinition per source and featuring type.
     * The service reports concurrent local replacements without selecting one or changing the reused prefix.
     */
    @Test
    @DisplayName("GIVEN a local prefix with two concurrent terminal redefinitions, WHEN materializing the original path, THEN the conflict is reported without changing existing features")
    public void competingTerminalRedefinitions() throws IOException {
        var model = this.loadMetaclassModel();
        var context = model.get(PartUsage.class, MODEL + "conflict");
        var prefix = model.get(PartUsage.class, MODEL + "conflict::child");
        var source = model.get(PartUsage.class, ROOT_CHILD);
        var value = model.get(AttributeUsage.class, LEAF_VALUE);
        var contextRelationships = List.copyOf(context.getOwnedRelationship());
        var prefixRelationships = List.copyOf(prefix.getOwnedRelationship());

        var result = this.service.redefineFeatureChain(context, List.of(source, value));

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.chain()).isEmpty();
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment 2 'EffectiveRedefinitionSources::Leaf::value' in context 'EffectiveRedefinitionSources::conflict': Multiple local features redefine this feature: ['EffectiveRedefinitionSources::conflict::child::firstValue', 'EffectiveRedefinitionSources::conflict::child::secondValue']. Resolve the conflicting redefinitions before trying again.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(contextRelationships);
        assertThat(prefix.getOwnedRelationship()).containsExactlyElementsOf(prefixRelationships);
    }

    /**
     * Checks exact category, semantic ownership, anonymity and the direct redefinition target.
     */
    private void assertLocalRedefinition(Feature local, Type owner, Feature source) {
        assertThat(local).isNotSameAs(source);
        assertThat(local.eClass()).isSameAs(source.eClass());
        assertThat(local.getOwningType()).isSameAs(owner);
        assertThat(local.getDeclaredName()).isNull();
        assertThat(local.getDeclaredShortName()).isNull();
        assertThat(local.getOwnedRedefinition()).singleElement().satisfies(redefinition -> {
            assertThat(redefinition.getRedefiningFeature()).isSameAs(local);
            assertThat(redefinition.getRedefinedFeature()).isSameAs(source);
            assertThat(redefinition.isIsImplied()).isFalse();
        });
    }

    private void assertSuccess(FeatureChainRedefinitionResult result) {
        assertThat(result.isSuccess()).as("Unexpected failure: %s", result.messages()).isTrue();
        assertThat(result.messages()).isEmpty();
    }

    /**
     * Loads independent category and effective-source scenarios; the XMI comment preserves the original SysML text.
     */
    private SysMLTestModel loadMetaclassModel() throws IOException {
        return SysMLTestModel.load("/feature-chains/effective-redefinition-sources.xmi");
    }
}
