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
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.services.data.SysMLTestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests local-prefix reuse for selections collected before model mutation.
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
     * The service reuses local prefixes; a directly owned feature needs no redefinition relationship to be local.
     */
    @Test
    @DisplayName("GIVEN a directly owned prefix, WHEN materializing child.value, THEN the prefix is reused and only the terminal attribute is created")
    public void directlyOwnedPrefix() throws IOException {
        var model = this.loadReuseModel();
        var context = model.get(PartUsage.class, "FeatureChainReuse::directlyOwned");
        var child = model.get(PartUsage.class, "FeatureChainReuse::directlyOwned::child");
        var value = model.get(AttributeUsage.class, CHILD_VALUE);
        var contextRelationships = List.copyOf(context.getOwnedRelationship());
        var typing = List.copyOf(child.getOwnedTyping());

        var result = this.service.redefineFeatureChain(context, List.of(child, value));

        this.assertSuccess(result);
        assertThat(result.chain()).hasSize(2);
        assertThat(result.chain().getFirst()).isSameAs(child);
        this.assertLocalRedefinition(result.chain().getLast(), child, value);
        assertThat(child.getOwnedRedefinition()).isEmpty();
        assertThat(child.getOwnedTyping()).containsExactlyElementsOf(typing);
        assertThat(child.getOwnedFeature()).containsExactly(result.chain().getLast());
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(contextRelationships);
    }

    /**
     * If a redefinition already exist (even if not exact type) then reuse it instead of creating a new one.
     */
    @Test
    @DisplayName("GIVEN a local referential redefinition of child, WHEN materializing the original child.value selection, THEN the prefix and its existing properties are preserved")
    public void alreadyRedefinedPrefix() throws IOException {
        var model = this.loadReuseModel();
        var context = model.get(PartUsage.class, "FeatureChainReuse::partial");
        var source = model.get(PartUsage.class, ROOT_CHILD);
        var local = model.get(PartUsage.class, "FeatureChainReuse::partial::child");
        var value = model.get(AttributeUsage.class, CHILD_VALUE);
        var contextRelationships = List.copyOf(context.getOwnedRelationship());
        var redefinitions = List.copyOf(local.getOwnedRedefinition());
        assertThat(source.isIsComposite()).isTrue();
        assertThat(local.isIsComposite()).isFalse();

        var result = this.service.redefineFeatureChain(context, List.of(source, value));

        this.assertSuccess(result);
        assertThat(result.chain()).hasSize(2);
        assertThat(result.chain().getFirst()).isSameAs(local);
        this.assertLocalRedefinition(result.chain().getLast(), local, value);
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
    public void completelyLocalChain() throws IOException {
        var model = this.loadReuseModel();
        var context = model.get(PartUsage.class, "FeatureChainReuse::complete");
        var child = model.get(PartUsage.class, "FeatureChainReuse::complete::child");
        var value = model.get(AttributeUsage.class, "FeatureChainReuse::complete::child::value");
        var contextRelationships = List.copyOf(context.getOwnedRelationship());
        var childRelationships = List.copyOf(child.getOwnedRelationship());
        var valueRelationships = List.copyOf(value.getOwnedRelationship());

        var result = this.service.redefineFeatureChain(context, List.of(child, value));

        this.assertSuccess(result);
        assertThat(result.chain()).containsExactly(child, value);
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(contextRelationships);
        assertThat(child.getOwnedRelationship()).containsExactlyElementsOf(childRelationships);
        assertThat(value.getOwnedRelationship()).containsExactlyElementsOf(valueRelationships);
    }

    /**
     * Successive selections share a local prefix; KerML 1.1 Beta 2, 7.3.4.5, allows a source to be redefined only once per featuring type.
     * Both original chains are collected before mutation, and either selection order must preserve their semantic identities.
     */
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("GIVEN two previously collected chains sharing child, WHEN materializing them in either order, THEN both paths share one local prefix")
    public void previouslySelectedSharedPrefix(boolean reverseOrder) throws IOException {
        var model = this.loadReuseModel();
        var context = model.get(PartUsage.class, "FeatureChainReuse::shared");
        var child = model.get(PartUsage.class, ROOT_CHILD);
        var value = model.get(AttributeUsage.class, CHILD_VALUE);
        var other = model.get(AttributeUsage.class, CHILD_OTHER);
        var sourceRelationships = List.copyOf(child.getOwnedRelationship());
        var selections = this.queryService.streamFeatureChains(context, null, (root, chain) -> chain.getLast() == value || chain.getLast() == other).toList();
        assertThat(selections).containsExactly(List.of(child, value), List.of(child, other));
        if (reverseOrder) {
            selections = selections.reversed();
        }

        var first = this.service.redefineFeatureChain(context, selections.getFirst());
        var second = this.service.redefineFeatureChain(context, selections.getLast());

        this.assertSuccess(first);
        this.assertSuccess(second);
        var localChild = first.chain().getFirst();
        assertThat(second.chain().getFirst()).isSameAs(localChild);
        this.assertLocalRedefinition(localChild, context, child);
        this.assertLocalRedefinition(first.chain().getLast(), localChild, selections.getFirst().getLast());
        this.assertLocalRedefinition(second.chain().getLast(), localChild, selections.getLast().getLast());
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
    public void repeatedNestedSelection() throws IOException {
        var model = this.loadReuseModel();
        var context = model.get(PartUsage.class, "FeatureChainReuse::shared");
        var child = model.get(PartUsage.class, ROOT_CHILD);
        var value = model.get(AttributeUsage.class, CHILD_VALUE);
        var original = List.<Feature>of(child, value);
        var first = this.service.redefineFeatureChain(context, original);
        this.assertSuccess(first);
        var localChild = first.chain().getFirst();
        var localValue = first.chain().getLast();
        var contextRelationships = List.copyOf(context.getOwnedRelationship());
        var childRelationships = List.copyOf(localChild.getOwnedRelationship());
        var valueRelationships = List.copyOf(localValue.getOwnedRelationship());

        var fromOriginal = this.service.redefineFeatureChain(context, original);
        var fromLocal = this.service.redefineFeatureChain(context, first.chain());

        this.assertSuccess(fromOriginal);
        this.assertSuccess(fromLocal);
        assertThat(fromOriginal.chain()).containsExactly(localChild, localValue);
        assertThat(fromLocal.chain()).containsExactly(localChild, localValue);
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(contextRelationships);
        assertThat(localChild.getOwnedRelationship()).containsExactlyElementsOf(childRelationships);
        assertThat(localValue.getOwnedRelationship()).containsExactlyElementsOf(valueRelationships);
    }

    /**
     * The service localizes the selected occurrence; a redefinition within a shared definition is not owned by that occurrence.
     * KerML 1.1 Beta 2, 7.3.4.5, places the replacement in its owning type rather than modifying the inherited source.
     */
    @Test
    @DisplayName("GIVEN a chain already redefined in a shared definition, WHEN configuring one occurrence, THEN new local redefinitions preserve the definition and sibling occurrence")
    public void inheritedRedefinitionIsNotLocal() throws IOException {
        var model = this.loadReuseModel();
        var context = model.get(PartUsage.class, "FeatureChainReuse::inherited");
        var sibling = model.get(PartUsage.class, "FeatureChainReuse::sibling");
        var definition = model.get(PartDefinition.class, "FeatureChainReuse::ConfiguredRoot");
        var child = model.get(PartUsage.class, "FeatureChainReuse::ConfiguredRoot::child");
        var value = model.get(AttributeUsage.class, "FeatureChainReuse::ConfiguredRoot::child::value");
        var definitionRelationships = List.copyOf(definition.getOwnedRelationship());
        var childRelationships = List.copyOf(child.getOwnedRelationship());
        var valueRelationships = List.copyOf(value.getOwnedRelationship());
        var siblingRelationships = List.copyOf(sibling.getOwnedRelationship());
        var selections = this.queryService.streamFeatureChains(context, null, (root, chain) -> chain.getLast() == value).toList();
        assertThat(selections).containsExactly(List.of(child, value));

        var result = this.service.redefineFeatureChain(context, selections.getFirst());

        this.assertSuccess(result);
        assertThat(result.chain()).hasSize(2);
        this.assertLocalRedefinition(result.chain().getFirst(), context, child);
        this.assertLocalRedefinition(result.chain().getLast(), result.chain().getFirst(), value);
        assertThat(context.getOwnedFeature()).containsExactly(result.chain().getFirst());
        assertThat(definition.getOwnedRelationship()).containsExactlyElementsOf(definitionRelationships);
        assertThat(child.getOwnedRelationship()).containsExactlyElementsOf(childRelationships);
        assertThat(value.getOwnedRelationship()).containsExactlyElementsOf(valueRelationships);
        assertThat(sibling.getOwnedRelationship()).containsExactlyElementsOf(siblingRelationships);
        assertThat(sibling.getOwnedFeature()).isEmpty();
    }

    /**
     * Reuse is based on semantic identity and redefinition, never on names alone (service contract).
     * shortcut: inherited homonyms are currently hidden; after fixing <a href="https://github.com/eclipse-syson/syson/issues/2603">#2603</a>,
     * expect successful creation of a distinct local prefix and terminal feature, preserving the unrelated homonym.
     * Any resulting name conflict is left to validation.
     */
    @Test
    @DisplayName("GIVEN an unrelated owned homonym of child, WHEN materializing the original chain, THEN creation is currently rejected because of #2603")
    public void unrelatedPrefixHomonym() throws IOException {
        var model = this.loadReuseModel();
        var context = model.get(PartUsage.class, "FeatureChainReuse::homonym");
        var homonym = model.get(PartUsage.class, "FeatureChainReuse::homonym::child");
        var child = model.get(PartUsage.class, ROOT_CHILD);
        var value = model.get(AttributeUsage.class, CHILD_VALUE);
        var contextRelationships = List.copyOf(context.getOwnedRelationship());
        var homonymRelationships = List.copyOf(homonym.getOwnedRelationship());
        assertThat(homonym.redefines(child)).isFalse();

        var result = this.service.redefineFeatureChain(context, List.of(child, value));

        this.assertFailure(result,
                "Cannot redefine segment 1 'FeatureChainReuse::Root::child' in context 'FeatureChainReuse::homonym': the feature is not accessible from this context. Select an accessible feature chain again.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(contextRelationships);
        assertThat(homonym.getOwnedRelationship()).containsExactlyElementsOf(homonymRelationships);
        assertThat(homonym.getOwnedFeature()).isEmpty();
    }

    /**
     * An unrelated terminal homonym must not be mistaken for a redefinition or change the reused prefix.
     * shortcut: inherited homonyms are currently hidden; after fixing <a href="https://github.com/eclipse-syson/syson/issues/2603">#2603</a>,
     * expect the existing local prefix to be reused and a distinct terminal redefinition to be created, preserving the homonym.
     * Any resulting name conflict is left to validation.
     */
    @Test
    @DisplayName("GIVEN a redefined prefix with an unrelated terminal homonym, WHEN materializing the original chain, THEN segment 2 is currently rejected because of #2603")
    public void unrelatedTerminalHomonym() throws IOException {
        var model = this.loadReuseModel();
        var context = model.get(PartUsage.class, "FeatureChainReuse::homonymLeaf");
        var localChild = model.get(PartUsage.class, "FeatureChainReuse::homonymLeaf::child");
        var homonym = model.get(AttributeUsage.class, "FeatureChainReuse::homonymLeaf::child::value");
        var child = model.get(PartUsage.class, ROOT_CHILD);
        var value = model.get(AttributeUsage.class, CHILD_VALUE);
        var contextRelationships = List.copyOf(context.getOwnedRelationship());
        var childRelationships = List.copyOf(localChild.getOwnedRelationship());
        var homonymRelationships = List.copyOf(homonym.getOwnedRelationship());
        assertThat(homonym.redefines(value)).isFalse();

        var result = this.service.redefineFeatureChain(context, List.of(child, value));

        this.assertFailure(result,
                "Cannot redefine segment 2 'FeatureChainReuse::Child::value' in context 'FeatureChainReuse::homonymLeaf': the feature is not accessible from this context. Select an accessible feature chain again.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(contextRelationships);
        assertThat(localChild.getOwnedRelationship()).containsExactlyElementsOf(childRelationships);
        assertThat(homonym.getOwnedRelationship()).containsExactlyElementsOf(homonymRelationships);
    }

    /**
     * Checks source identity, local ownership and anonymity without relying on effective names.
     */
    private void assertLocalRedefinition(Feature local, Type owner, Feature source) {
        assertThat(local).isNotSameAs(source);
        assertThat(local.eClass()).isSameAs(source.eClass());
        assertThat(local.getOwningType()).isSameAs(owner);
        assertThat(local.getDeclaredName()).isNull();
        assertThat(local.getDeclaredShortName()).isNull();
        assertThat(local.getOwnedRedefinition()).singleElement().satisfies(redefinition -> {
            assertThat(redefinition.getRedefinedFeature()).isSameAs(source);
            assertThat(redefinition.getRedefiningFeature()).isSameAs(local);
            assertThat(redefinition.isIsImplied()).isFalse();
        });
    }

    private void assertSuccess(FeatureChainRedefinitionResult result) {
        assertThat(result.isSuccess()).as("Unexpected failure: %s", result.messages()).isTrue();
        assertThat(result.messages()).isEmpty();
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
        var resourceSet = new ResourceSetImpl();
        resourceSet.getPackageRegistry().put(SysmlPackage.eNS_URI, SysmlPackage.eINSTANCE);
        var resource = new XMIResourceImpl(URI.createURI("test:/feature-chain-reuse.xmi"));
        resourceSet.getResources().add(resource);
        try (var input = this.getClass().getResourceAsStream("/feature-chains/feature-chain-reuse.xmi")) {
            assertThat(input).isNotNull();
            resource.load(input, Map.of());
        }
        assertThat(resource.getErrors()).isEmpty();
        return new SysMLTestModel(resource);
    }
}
