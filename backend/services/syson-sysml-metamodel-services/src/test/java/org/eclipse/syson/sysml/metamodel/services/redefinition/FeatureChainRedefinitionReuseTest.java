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
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.eclipse.syson.sysml.metamodel.services.redefine.FeatureChainRedefinitionResult;
import org.eclipse.syson.sysml.util.SysMLTestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Checks reuse of local feature chains across selections.
 *
 * @author Arthur Daussy
 */
public class FeatureChainRedefinitionReuseTest {

    private static final String ROOT_CHILD = "FeatureChainReuse::Root::child";

    private static final String CHILD_VALUE = "FeatureChainReuse::Child::value";

    private static final String CHILD_OTHER = "FeatureChainReuse::Child::other";

    private final MetamodelMutationElementService service = new MetamodelMutationElementService();

    private final MetamodelQueryElementService queryService = new MetamodelQueryElementService();

    /**
     * If a redefinition already exist (even if not exact type) then reuse it instead of creating a new one.
     */
    @Test
    @DisplayName("GIVEN a local referential redefinition of child, WHEN materializing the original child.value selection, THEN the prefix and its existing properties are preserved")
    public void verifyAlreadyRedefinedPrefix() throws IOException {
        // GIVEN The local child already redefines the inherited source and deliberately differs in composition.
        SysMLTestModel model = this.loadReuseModel();
        PartUsage context = model.get(PartUsage.class, "FeatureChainReuse::partial");
        PartUsage source = model.get(PartUsage.class, ROOT_CHILD);
        PartUsage local = model.get(PartUsage.class, "FeatureChainReuse::partial::child");
        AttributeUsage value = model.get(AttributeUsage.class, CHILD_VALUE);
        List<Relationship> contextRelationships = List.copyOf(context.getOwnedRelationship());
        List<Redefinition> redefinitions = List.copyOf(local.getOwnedRedefinition());
        assertThat(source.isIsComposite()).isTrue();
        assertThat(local.isIsComposite()).isFalse();

        // WHEN Select the original child.value path.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source, value));

        // THEN Reuse that local child without reinitializing its composition or existing redefinition.
        assertChainSuccess(result);
        assertThat(result.chain()).hasSize(2);
        assertThat(result.chain().getFirst()).isSameAs(local);
        assertLocalRedefinition(result.chain().getLast(), local, value);
        assertThat(local.isIsComposite()).isFalse();
        assertThat(local.getOwnedRedefinition()).containsExactlyElementsOf(redefinitions);
        assertThat(local.getOwnedFeature()).containsExactly(result.chain().getLast());
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(contextRelationships);
    }

    /**
     * Service idempotence contract: selecting an entirely local chain does not add relationships or reinitialize existing features.
     */
    @Test
    @DisplayName("GIVEN a completely local chain, WHEN materializing it, THEN the same features are returned without any new relationship")
    public void verifyCompletelyLocalChain() throws IOException {
        // GIVEN Both child and value already belong to the context's local chain.
        SysMLTestModel model = this.loadReuseModel();
        PartUsage context = model.get(PartUsage.class, "FeatureChainReuse::complete");
        PartUsage child = model.get(PartUsage.class, "FeatureChainReuse::complete::child");
        AttributeUsage value = model.get(AttributeUsage.class, "FeatureChainReuse::complete::child::value");
        List<Relationship> contextRelationships = List.copyOf(context.getOwnedRelationship());
        List<Relationship> childRelationships = List.copyOf(child.getOwnedRelationship());
        List<Relationship> valueRelationships = List.copyOf(value.getOwnedRelationship());

        // WHEN Request the entirely local path.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(child, value));

        // THEN Return both original identities and preserve every stored relationship.
        assertChainSuccess(result);
        assertThat(result.chain()).containsExactly(child, value);
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(contextRelationships);
        assertThat(child.getOwnedRelationship()).containsExactlyElementsOf(childRelationships);
        assertThat(value.getOwnedRelationship()).containsExactlyElementsOf(valueRelationships);
    }

    /**
     * The service reuses local prefixes; a directly owned feature needs no redefinition relationship to be local.
     */
    @Test
    @DisplayName("GIVEN a directly owned prefix, WHEN materializing child.value, THEN the prefix is reused and only the terminal attribute is created")
    public void verifyDirectlyOwnedPrefix() throws IOException {
        // GIVEN The context owns a typed child that has no redefinition.
        SysMLTestModel model = this.loadReuseModel();
        PartUsage context = model.get(PartUsage.class, "FeatureChainReuse::directlyOwned");
        PartUsage child = model.get(PartUsage.class, "FeatureChainReuse::directlyOwned::child");
        AttributeUsage value = model.get(AttributeUsage.class, CHILD_VALUE);
        List<Relationship> contextRelationships = List.copyOf(context.getOwnedRelationship());
        List<FeatureTyping> typing = List.copyOf(child.getOwnedTyping());

        // WHEN Extend this directly owned child to its inherited value.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(child, value));

        // THEN Preserve its existing typing and add only the missing local terminal.
        assertChainSuccess(result);
        assertThat(result.chain()).hasSize(2);
        assertThat(result.chain().getFirst()).isSameAs(child);
        assertLocalRedefinition(result.chain().getLast(), child, value);
        assertThat(child.getOwnedRedefinition()).isEmpty();
        assertThat(child.getOwnedTyping()).containsExactlyElementsOf(typing);
        assertThat(child.getOwnedFeature()).containsExactly(result.chain().getLast());
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(contextRelationships);
    }

    /**
     * The service localizes the selected occurrence; a redefinition within a shared definition is not owned by that occurrence.
     * KerML 1.1 Beta 2, 7.3.4.5, places the replacement in its owning type rather than modifying the inherited source.
     */
    @Test
    @DisplayName("GIVEN a chain already redefined in a shared definition, WHEN configuring one occurrence, THEN new local redefinitions preserve the definition and sibling occurrence")
    public void verifyInheritedRedefinitionIsNotLocal() throws IOException {
        // GIVEN The shared definition already contains a redefined child, inherited by two occurrences.
        SysMLTestModel model = this.loadReuseModel();
        PartUsage context = model.get(PartUsage.class, "FeatureChainReuse::inherited");
        PartUsage sibling = model.get(PartUsage.class, "FeatureChainReuse::sibling");
        PartDefinition definition = model.get(PartDefinition.class, "FeatureChainReuse::ConfiguredRoot");
        PartUsage child = model.get(PartUsage.class, "FeatureChainReuse::ConfiguredRoot::child");
        AttributeUsage value = model.get(AttributeUsage.class, "FeatureChainReuse::ConfiguredRoot::child::value");
        List<Relationship> definitionRelationships = List.copyOf(definition.getOwnedRelationship());
        List<Relationship> childRelationships = List.copyOf(child.getOwnedRelationship());
        List<Relationship> valueRelationships = List.copyOf(value.getOwnedRelationship());
        List<Relationship> siblingRelationships = List.copyOf(sibling.getOwnedRelationship());
        List<List<Feature>> selections = this.queryService.streamFeatureChains(context, null, (root, chain) -> chain.getLast() == value).toList();
        assertThat(selections).containsExactly(List.of(child, value));

        // WHEN Materialize that effective inherited child.value in one occurrence.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, selections.getFirst());

        // THEN Create occurrence-local replacements without editing the shared definition or sibling occurrence.
        assertChainSuccess(result);
        assertThat(result.chain()).hasSize(2);
        assertLocalRedefinition(result.chain().getFirst(), context, child);
        assertLocalRedefinition(result.chain().getLast(), result.chain().getFirst(), value);
        assertThat(context.getOwnedFeature()).containsExactly(result.chain().getFirst());
        assertThat(definition.getOwnedRelationship()).containsExactlyElementsOf(definitionRelationships);
        assertThat(child.getOwnedRelationship()).containsExactlyElementsOf(childRelationships);
        assertThat(value.getOwnedRelationship()).containsExactlyElementsOf(valueRelationships);
        assertThat(sibling.getOwnedRelationship()).containsExactlyElementsOf(siblingRelationships);
        assertThat(sibling.getOwnedFeature()).isEmpty();
    }

    /**
     * Successive selections share a local prefix; KerML 1.1 Beta 2, 7.3.4.5, allows a source to be redefined only once per featuring type.
     * Both original chains are collected before mutation, and either selection order must preserve their semantic identities.
     *
     * @param reverseOrder
     *            whether to materialize the sibling paths in reverse order
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN two previously collected chains sharing child, WHEN materializing them in either order, THEN both paths share one local prefix")
    public void verifyPreviouslySelectedSharedPrefix(boolean reverseOrder) throws IOException {
        // GIVEN Collect child.value and child.other before either selection changes the model.
        SysMLTestModel model = this.loadReuseModel();
        PartUsage context = model.get(PartUsage.class, "FeatureChainReuse::shared");
        PartUsage child = model.get(PartUsage.class, ROOT_CHILD);
        AttributeUsage value = model.get(AttributeUsage.class, CHILD_VALUE);
        AttributeUsage other = model.get(AttributeUsage.class, CHILD_OTHER);
        List<Relationship> sourceRelationships = List.copyOf(child.getOwnedRelationship());
        List<List<Feature>> selections = this.queryService.streamFeatureChains(context, null, (root, chain) -> chain.getLast() == value || chain.getLast() == other).toList();
        assertThat(selections).containsExactly(List.of(child, value), List.of(child, other));
        if (reverseOrder) {
            selections = selections.reversed();
        }

        // WHEN Materialize both selections in the requested order.
        FeatureChainRedefinitionResult first = this.service.redefineFeatureChain(context, selections.getFirst());
        FeatureChainRedefinitionResult second = this.service.redefineFeatureChain(context, selections.getLast());

        // THEN Reuse a single local child and create the two distinct local terminals below it.
        assertChainSuccess(first);
        assertChainSuccess(second);
        Feature localChild = first.chain().getFirst();
        assertThat(second.chain().getFirst()).isSameAs(localChild);
        assertLocalRedefinition(localChild, context, child);
        assertLocalRedefinition(first.chain().getLast(), localChild, selections.getFirst().getLast());
        assertLocalRedefinition(second.chain().getLast(), localChild, selections.getLast().getLast());
        assertThat(context.getOwnedFeature()).containsExactly(localChild);
        assertThat(localChild.getOwnedFeature()).containsExactly(first.chain().getLast(), second.chain().getLast());
        assertThat(child.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
        assertThat(value.getOwnedRedefinition()).isEmpty();
        assertThat(other.getOwnedRedefinition()).isEmpty();
    }

    /**
     * Service idempotence contract: old source selections and the resulting local chain both identify the existing occurrence.
     * KerML 1.1 Beta 2, 7.3.4.5, distinguishes the replacement feature from the inherited source it hides.
     */
    @Test
    @DisplayName("GIVEN a materialized nested chain, WHEN replaying its original and local selections, THEN the same local objects are returned without duplication")
    public void verifyRepeatedNestedSelection() throws IOException {
        // GIVEN Create child.value once and retain both the original and returned local paths.
        SysMLTestModel model = this.loadReuseModel();
        PartUsage context = model.get(PartUsage.class, "FeatureChainReuse::shared");
        PartUsage child = model.get(PartUsage.class, ROOT_CHILD);
        AttributeUsage value = model.get(AttributeUsage.class, CHILD_VALUE);
        List<Feature> original = List.<Feature>of(child, value);
        // WHEN Repeat using the original path, then the local path.
        FeatureChainRedefinitionResult first = this.service.redefineFeatureChain(context, original);
        // THEN Both requests reuse the same two objects and preserve their relationships.
        assertChainSuccess(first);
        Feature localChild = first.chain().getFirst();
        Feature localValue = first.chain().getLast();
        List<Relationship> contextRelationships = List.copyOf(context.getOwnedRelationship());
        List<Relationship> childRelationships = List.copyOf(localChild.getOwnedRelationship());
        List<Relationship> valueRelationships = List.copyOf(localValue.getOwnedRelationship());

        FeatureChainRedefinitionResult fromOriginal = this.service.redefineFeatureChain(context, original);
        FeatureChainRedefinitionResult fromLocal = this.service.redefineFeatureChain(context, first.chain());

        assertChainSuccess(fromOriginal);
        assertChainSuccess(fromLocal);
        assertThat(fromOriginal.chain()).containsExactly(localChild, localValue);
        assertThat(fromLocal.chain()).containsExactly(localChild, localValue);
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(contextRelationships);
        assertThat(localChild.getOwnedRelationship()).containsExactlyElementsOf(childRelationships);
        assertThat(localValue.getOwnedRelationship()).containsExactlyElementsOf(valueRelationships);
    }

    /**
     * Reuse is based on semantic identity and redefinition, never on names alone (service contract).
     * With #2603 fixed, an unrelated owned homonym leaves the inherited feature accessible.
     * Any resulting name conflict is left to validation.
     */
    @Test
    @DisplayName("GIVEN an unrelated owned homonym of child, WHEN materializing the inherited chain, THEN a distinct local chain is created and the homonym is preserved")
    public void verifyUnrelatedPrefixHomonym() throws IOException {
        // GIVEN The context owns another child with the same name but no redefinition of the selected source.
        // GIVEN: the owned child is unrelated to the selected inherited child despite having the same name.
        SysMLTestModel model = this.loadReuseModel();
        PartUsage context = model.get(PartUsage.class, "FeatureChainReuse::homonym");
        PartUsage homonym = model.get(PartUsage.class, "FeatureChainReuse::homonym::child");
        PartUsage child = model.get(PartUsage.class, ROOT_CHILD);
        AttributeUsage value = model.get(AttributeUsage.class, CHILD_VALUE);
        List<Relationship> contextRelationships = List.copyOf(context.getOwnedRelationship());
        List<Relationship> homonymRelationships = List.copyOf(homonym.getOwnedRelationship());
        assertThat(homonym.redefines(child)).isFalse();

        // WHEN: materializing the inherited chain must not reuse an unrelated homonym.
        // WHEN Materialize the inherited child.value path.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(child, value));

        // THEN: a separate chain is created and the unrelated child is unchanged.
        // THEN Preserve the unrelated homonym and create a separate semantic replacement.
        assertChainSuccess(result);
        Feature localChild = result.chain().getFirst();
        Feature localValue = result.chain().getLast();
        assertLocalRedefinition(localChild, context, child);
        assertLocalRedefinition(localValue, localChild, value);
        assertThat(context.getOwnedFeature()).containsExactly(homonym, localChild);
        assertThat(context.getOwnedRelationship()).containsAll(contextRelationships).hasSize(contextRelationships.size() + 1);
        assertThat(homonym.getOwnedRelationship()).containsExactlyElementsOf(homonymRelationships);
        assertThat(homonym.getOwnedFeature()).isEmpty();
    }

    /**
     * An unrelated terminal homonym must not be mistaken for a redefinition or replace the reused prefix.
     * With #2603 fixed, the inherited terminal remains accessible; any resulting name conflict is left to validation.
     */
    @Test
    @DisplayName("GIVEN a redefined prefix with an unrelated terminal homonym, WHEN materializing the inherited chain, THEN the prefix is reused and a distinct terminal is created")
    public void verifyUnrelatedTerminalHomonym() throws IOException {
        // GIVEN The local child already owns a value that does not redefine the selected inherited value.
        // GIVEN: the local prefix already redefines child, while its owned value is unrelated to the inherited value.
        SysMLTestModel model = this.loadReuseModel();
        PartUsage context = model.get(PartUsage.class, "FeatureChainReuse::homonymLeaf");
        PartUsage localChild = model.get(PartUsage.class, "FeatureChainReuse::homonymLeaf::child");
        AttributeUsage homonym = model.get(AttributeUsage.class, "FeatureChainReuse::homonymLeaf::child::value");
        PartUsage child = model.get(PartUsage.class, ROOT_CHILD);
        AttributeUsage value = model.get(AttributeUsage.class, CHILD_VALUE);
        List<Relationship> contextRelationships = List.copyOf(context.getOwnedRelationship());
        List<Relationship> childRelationships = List.copyOf(localChild.getOwnedRelationship());
        List<Relationship> homonymRelationships = List.copyOf(homonym.getOwnedRelationship());
        assertThat(homonym.redefines(value)).isFalse();

        // WHEN: the inherited terminal is selected through the original prefix.
        // WHEN Extend the original path through that local child.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(child, value));

        // THEN: only a new terminal is attached; the prefix and unrelated value retain their identities.
        // THEN Preserve the existing value and add the distinct redefining terminal.
        assertChainSuccess(result);
        assertThat(result.chain().getFirst()).isSameAs(localChild);
        Feature localValue = result.chain().getLast();
        assertLocalRedefinition(localValue, localChild, value);
        assertThat(localChild.getOwnedFeature()).containsExactly(homonym, localValue);
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(contextRelationships);
        assertThat(localChild.getOwnedRelationship()).containsAll(childRelationships).hasSize(childRelationships.size() + 1);
        assertThat(homonym.getOwnedRelationship()).containsExactlyElementsOf(homonymRelationships);
    }

    private void assertFailure(FeatureChainRedefinitionResult result, String message) {
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.chain()).isEmpty();
        assertThat(result.messages()).containsExactly(message);
    }

    /**
     * Loads the independent reuse scenarios; the XMI comment contains the original SysML declarations.
     */
    private SysMLTestModel loadReuseModel() throws IOException {
        return SysMLTestModel.load("/feature-chains/feature-chain-reuse.xmi");
    }
}
