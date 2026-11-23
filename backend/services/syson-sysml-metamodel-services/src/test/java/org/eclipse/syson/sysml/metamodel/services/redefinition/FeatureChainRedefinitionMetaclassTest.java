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
package org.eclipse.syson.sysml.metamodel.services.redefinition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.syson.sysml.metamodel.services.redefinition.FeatureChainRedefinitionAssertions.assertChainSuccess;
import static org.eclipse.syson.sysml.metamodel.services.redefinition.FeatureChainRedefinitionAssertions.assertLocalRedefinition;

import java.io.IOException;
import java.util.List;

import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.eclipse.syson.sysml.metamodel.services.redefine.FeatureChainRedefinitionResult;
import org.eclipse.syson.sysml.util.SysMLTestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Checks exact metaclasses for inherited and abbreviated feature redefinitions.
 *
 * @author Arthur Daussy
 */
public class FeatureChainRedefinitionMetaclassTest {

    private static final String ROOT_PREFIX = "Root::";

    private static final String MODEL = "EffectiveRedefinitionSources::";

    private static final String PLAIN = MODEL + "plain";

    private static final String ROOT_CHILD = MODEL + "Root::child";

    private static final String LEAF_VALUE = MODEL + "Leaf::value";

    private final MetamodelMutationElementService service = new MetamodelMutationElementService();

    private final MetamodelQueryElementService queryService = new MetamodelQueryElementService();

    /**
     * SysML 2.1 Beta 2, 8.2.2.6.3, parses a category-free usage as ReferenceUsage.
     * Retaining that selected metaclass rather than the category of an older redefined source is service policy.
     */
    @Test
    @DisplayName("GIVEN an abbreviated redefinition of an attribute, WHEN materializing it, THEN the new feature remains a ReferenceUsage")
    public void verifyAbbreviatedAttributeRedefinition() throws IOException {
        // GIVEN An abbreviated redefinition is a ReferenceUsage even though its ancestor is an AttributeUsage.
        SysMLTestModel model = this.loadMetaclassModel();
        PartUsage context = model.get(PartUsage.class, MODEL + "abbreviated");
        ReferenceUsage source = model.get(ReferenceUsage.class, MODEL + "Abbreviated::amount");
        AttributeUsage original = model.get(AttributeUsage.class, MODEL + "Root::amount");
        assertThat(source.allRedefinedFeatures()).contains(original);

        // WHEN Materialize the effective abbreviated source.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source));

        // THEN Retain ReferenceUsage as the exact selected metaclass instead of copying the ancestor's category.
        assertChainSuccess(result);
        assertThat(result.chain()).singleElement().satisfies(local -> {
            assertLocalRedefinition(local, context, source);
            assertThat(local).isInstanceOf(ReferenceUsage.class);
            assertThat(local.isIsComposite()).isFalse();
        });
    }

    /**
     * KerML 1.1 Beta 2, 7.3.4.5, allows only one redefinition per source and featuring type.
     * The service reports concurrent local replacements without selecting one or changing the reused prefix.
     */
    @Test
    @DisplayName("GIVEN a local prefix with two concurrent terminal redefinitions, WHEN materializing the original path, THEN the conflict is reported without changing existing features")
    public void verifyCompetingTerminalRedefinitions() throws IOException {
        // GIVEN Two local terminals redefine the same inherited value below an existing child.
        SysMLTestModel model = this.loadMetaclassModel();
        PartUsage context = model.get(PartUsage.class, MODEL + "conflict");
        PartUsage prefix = model.get(PartUsage.class, MODEL + "conflict::child");
        PartUsage source = model.get(PartUsage.class, ROOT_CHILD);
        AttributeUsage value = model.get(AttributeUsage.class, LEAF_VALUE);
        List<Relationship> contextRelationships = List.copyOf(context.getOwnedRelationship());
        List<Relationship> prefixRelationships = List.copyOf(prefix.getOwnedRelationship());

        // WHEN Extend the path through that child.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source, value));

        // THEN Report the ambiguous terminal and preserve the existing local prefix.
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.chain()).isEmpty();
        assertThat(result.messages()).containsExactly(
                "Cannot redefine segment 2 'EffectiveRedefinitionSources::Leaf::value' in context 'EffectiveRedefinitionSources::conflict': Multiple local features redefine this feature: ['EffectiveRedefinitionSources::conflict::child::firstValue', 'EffectiveRedefinitionSources::conflict::child::secondValue']. Resolve the conflicting redefinitions before trying again.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(contextRelationships);
        assertThat(prefix.getOwnedRelationship()).containsExactlyElementsOf(prefixRelationships);
    }

    /**
     * KerML 1.1 Beta 2, 7.3.4.5, removes replaced features from inherited memberships.
     * The service targets the effective object selected by the query, including renamed, anonymous, transitive and diamond cases.
     *
     * @param contextName
     *            the fixture context in which the inherited source is selected
     * @param sourceName
     *            the source usage name relative to the fixture namespace
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
    public void verifyEffectiveAttributeSource(String contextName, String sourceName) throws IOException {
        // GIVEN The query exposes an effective replacement rather than its original ancestor.
        SysMLTestModel model = this.loadMetaclassModel();
        PartUsage context = model.get(PartUsage.class, MODEL + contextName);
        AttributeUsage source = model.get(AttributeUsage.class, MODEL + sourceName);
        AttributeUsage original = model.get(AttributeUsage.class, MODEL + "Root::amount");
        List<Relationship> sourceRelationships = List.copyOf(source.getOwnedRelationship());
        List<List<Feature>> selections = this.queryService.streamFeatureChains(context, null, (root, chain) -> chain.getLast() == source).toList();
        assertThat(selections).containsExactly(List.of(source));
        assertThat(source).isNotSameAs(original);

        // WHEN Materialize the effective feature returned by the query.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, selections.getFirst());

        // THEN Target the replacement directly instead of redirecting the new link to its ancestor.
        assertChainSuccess(result);
        assertThat(result.chain()).singleElement().satisfies(local -> {
            assertLocalRedefinition(local, context, source);
            assertThat(local.getOwnedRedefinition()).extracting(redefinition -> redefinition.getRedefinedFeature()).doesNotContain(original);
        });
        assertThat(source.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
    }

    /**
     * KerML 1.1 Beta 2, 8.3.3.3.8, requires redefining connector ends to remain ends.
     * A body declaration using the end keyword has an ordinary FeatureMembership.
     */
    @Test
    @DisplayName("GIVEN an inherited end declared in a connection body, WHEN materializing it, THEN its ordinary membership and end flag are preserved")
    public void verifyEndInConnectionBody() throws IOException {
        // GIVEN The source end was declared in a connection body and has an ordinary FeatureMembership.
        SysMLTestModel model = this.loadMetaclassModel();
        ConnectionUsage context = model.get(ConnectionUsage.class, MODEL + "link");
        PartUsage source = model.get(PartUsage.class, MODEL + "Link::source");
        assertThat(source.getOwningFeatureMembership().eClass()).isSameAs(SysmlPackage.eINSTANCE.getFeatureMembership());

        // WHEN Materialize this end in the connection usage.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source));

        // THEN Retain the ordinary membership role and expose the local feature among connector ends.
        assertChainSuccess(result);
        assertThat(result.chain()).singleElement().satisfies(local -> {
            assertLocalRedefinition(local, context, source);
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
    public void verifyEndMembership() throws IOException {
        // GIVEN A connect-clause end has an EndFeatureMembership.
        SysMLTestModel model = this.loadMetaclassModel();
        ConnectionUsage context = model.get(ConnectionUsage.class, MODEL + "wireCopy");
        ConnectionUsage wire = model.get(ConnectionUsage.class, MODEL + "Wired::wire");
        Feature source = wire.getConnectorEnd().getFirst();
        assertThat(source.getOwningFeatureMembership()).isInstanceOf(EndFeatureMembership.class);

        // WHEN Materialize this inherited end.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source));

        // THEN Retain its specialized membership so connector-end derivation can find it.
        assertChainSuccess(result);
        assertThat(result.chain()).singleElement().satisfies(local -> {
            assertLocalRedefinition(local, context, source);
            assertThat(local.getOwningFeatureMembership()).isInstanceOf(EndFeatureMembership.class);
            assertThat(local.isIsEnd()).isTrue();
            assertThat(local.isIsComposite()).isFalse();
            assertThat(context.getConnectorEnd()).contains(local);
        });
    }

    /**
     * Service contract: new features retain the selected source's exact metaclass, without copying typings or adding category defaults.
     *
     * @param sourceName
     *            the source usage name relative to the fixture namespace
     * @param metaclass
     *            the exact SysML metaclass expected on the local redefinition
     */
    @ParameterizedTest
    @CsvSource({"child, PartUsage", "amount, AttributeUsage", "reference, ReferenceUsage", "endpoint, PortUsage", "work, ActionUsage"})
    @DisplayName("GIVEN a supported inherited usage category, WHEN materializing it, THEN the exact metaclass is retained without names or default children")
    public void verifyExactMetaclass(String sourceName, String metaclass) throws IOException {
        // GIVEN Select one supported usage category without adding category-specific initialization defaults.
        SysMLTestModel model = this.loadMetaclassModel();
        PartUsage context = model.get(PartUsage.class, PLAIN);
        Feature source = model.get(Feature.class, MODEL + ROOT_PREFIX + sourceName);
        assertThat(source.eClass().getName()).isEqualTo(metaclass);
        List<Relationship> sourceRelationships = List.copyOf(source.getOwnedRelationship());

        // WHEN Materialize that source.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source));

        // THEN Preserve its exact metaclass and create only the required redefinition structure.
        assertChainSuccess(result);
        assertThat(result.chain()).singleElement().satisfies(local -> {
            assertLocalRedefinition(local, context, source);
            assertThat(local.getOwnedRelationship()).containsExactlyElementsOf(local.getOwnedRedefinition());
            assertThat(local.getOwnedFeature()).isEmpty();
            assertThat(local.getOwnedTyping()).isEmpty();
            assertThat(local.getOwningFeatureMembership().eClass()).isSameAs(SysmlPackage.eINSTANCE.getFeatureMembership());
            assertThat(local.isIsComposite()).isEqualTo(source.isIsComposite());
        });
        assertThat(source.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
    }

    /**
     * SysML 2.1 Beta 2, 8.3.12.6, permits composite ports within ports while ports in ordinary part contexts must be referential.
     */
    @Test
    @DisplayName("GIVEN a nested port, WHEN materializing endpoint.nested, THEN the outer port remains referential and the nested port remains composite")
    public void verifyNestedPortComposition() throws IOException {
        // GIVEN The outer port is referential while its nested port is composite.
        SysMLTestModel model = this.loadMetaclassModel();
        PartUsage context = model.get(PartUsage.class, PLAIN);
        Feature endpoint = model.get(Feature.class, MODEL + "Root::endpoint");
        Feature nested = model.get(Feature.class, MODEL + "Endpoint::nested");
        assertThat(endpoint.isIsComposite()).isFalse();
        assertThat(nested.isIsComposite()).isTrue();

        // WHEN Materialize both ports in sequence.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(endpoint, nested));

        // THEN Evaluate composition in each local owner so the nested port keeps its permitted composition.
        assertChainSuccess(result);
        assertThat(result.chain()).hasSize(2);
        assertLocalRedefinition(result.chain().getFirst(), context, endpoint);
        assertLocalRedefinition(result.chain().getLast(), result.chain().getFirst(), nested);
        assertThat(result.chain().getFirst().isIsComposite()).isFalse();
        assertThat(result.chain().getLast().isIsComposite()).isTrue();
    }

    /**
     * Existing local features are reused without conversion, whether the selection names the original part or the effective reference (service contract).
     *
     * @param originalSelection
     *            whether to select the original source instead of its effective local replacement
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN a local ReferenceUsage redefining a part, WHEN materializing an original or effective child.value selection, THEN the same reference is reused")
    public void verifyReuseDifferentMetaclass(boolean originalSelection) throws IOException {
        // GIVEN An existing local ReferenceUsage already redefines a PartUsage source.
        SysMLTestModel model = this.loadMetaclassModel();
        PartUsage context = model.get(PartUsage.class, MODEL + "localReference");
        ReferenceUsage localReference = model.get(ReferenceUsage.class, MODEL + "localReference::child");
        PartUsage original = model.get(PartUsage.class, ROOT_CHILD);
        AttributeUsage value = model.get(AttributeUsage.class, LEAF_VALUE);
        List<Relationship> prefixRelationships = List.copyOf(localReference.getOwnedRelationship());
        List<Feature> selection = List.of(original, value);
        if (!originalSelection) {
            List<List<Feature>> selections = this.queryService.streamFeatureChains(context, null,
                    (root, chain) -> chain.getFirst() == localReference && chain.getLast() == value).toList();
            assertThat(selections).containsExactly(List.of(localReference, value));
            selection = selections.getFirst();
        }

        // WHEN Extend either the original or effective prefix selection to value.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, selection);

        // THEN Reuse the existing reference despite the metaclass difference and preserve its relationships.
        assertChainSuccess(result);
        assertThat(result.chain()).hasSize(2);
        assertThat(result.chain().getFirst()).isSameAs(localReference);
        assertLocalRedefinition(result.chain().getLast(), localReference, value);
        assertThat(localReference.isIsComposite()).isFalse();
        assertThat(localReference.getOwnedRelationship()).containsAll(prefixRelationships).hasSize(prefixRelationships.size() + 1);
        assertThat(context.getOwnedFeature()).containsExactly(localReference);
    }

    /**
     * The selected category must be retained through intermediate contexts as well as at the terminal feature (service contract).
     *
     * @param prefixName
     *            the intermediate usage name declared in Root
     * @param terminalName
     *            the terminal feature name relative to the fixture namespace
     */
    @ParameterizedTest
    @CsvSource({"child, Leaf::value", "reference, Leaf::value", "endpoint, Endpoint::signal", "work, Work::setting"})
    @DisplayName("GIVEN a usage exposing an inherited attribute, WHEN materializing both segments, THEN the prefix retains its category and owns only the requested attribute")
    public void verifyTypedIntermediateCategory(String prefixName, String terminalName) throws IOException {
        // GIVEN The intermediate usage exposes the terminal through its inherited type.
        SysMLTestModel model = this.loadMetaclassModel();
        PartUsage context = model.get(PartUsage.class, PLAIN);
        Feature prefix = model.get(Feature.class, MODEL + ROOT_PREFIX + prefixName);
        AttributeUsage terminal = model.get(AttributeUsage.class, MODEL + terminalName);

        // WHEN Materialize the two-segment path.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(prefix, terminal));

        // THEN Attach the terminal below the local prefix without copying that prefix's typing.
        assertChainSuccess(result);
        assertThat(result.chain()).hasSize(2);
        Feature localPrefix = result.chain().getFirst();
        Feature localTerminal = result.chain().getLast();
        assertLocalRedefinition(localPrefix, context, prefix);
        assertLocalRedefinition(localTerminal, localPrefix, terminal);
        assertThat(localPrefix.getOwnedFeature()).containsExactly(localTerminal);
        assertThat(localPrefix.getOwnedRelationship()).hasSize(2);
        assertThat(localTerminal.getOwnedFeature()).isEmpty();
    }

    /**
     * Loads independent category and effective-source scenarios; the XMI comment preserves the original SysML text.
     */
    private SysMLTestModel loadMetaclassModel() throws IOException {
        return SysMLTestModel.load("/feature-chains/effective-redefinition-sources.xmi");
    }
}
