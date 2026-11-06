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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.metamodel.services.data.SysMLTestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the materialization of selected feature chains.
 *
 * @author Arthur Daussy
 */
public class RedefineFeatureChainTest {

    private static final String BASE_X = "RedefinedFeatures::Base::x";

    private final MetamodelMutationElementService service = new MetamodelMutationElementService();

    /**
     * An inherited attribute becomes an anonymous local attribute with an explicit redefinition, without modifying its source.
     */
    @Test
    @DisplayName("GIVEN an inherited attribute, WHEN materializing its chain, THEN an anonymous local attribute redefines the unchanged source")
    public void inheritedAttribute() throws IOException {
        var model = this.loadRedefinedFeaturesModel();
        var context = model.get(PartDefinition.class, "RedefinedFeatures::Inherited");
        var source = model.get(AttributeUsage.class, BASE_X);
        var sourceOwner = source.getOwningType();
        var sourceRelationships = List.copyOf(source.getOwnedRelationship());
        var sourceOwnerRelationships = List.copyOf(sourceOwner.getOwnedRelationship());

        var result = this.service.redefineFeatureChain(context, List.of(source));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.messages()).isEmpty();
        assertThat(result.chain()).hasSize(1);
        var local = result.chain().getFirst();
        assertThat(local).isNotSameAs(source);
        assertThat(local.eClass()).isSameAs(source.eClass());
        assertThat(local.getOwningType()).isSameAs(context);
        assertThat(local.getDeclaredName()).isNull();
        assertThat(local.getDeclaredShortName()).isNull();
        assertThat(local.isIsComposite()).isFalse();
        assertThat(context.getOwnedFeature()).containsExactly(local);
        assertThat(local.getOwnedRelationship()).hasSize(1);
        assertThat(local.getOwnedRedefinition()).singleElement().satisfies(redefinition -> {
            assertThat(redefinition.getRedefinedFeature()).isSameAs(source);
            assertThat(redefinition.getRedefiningFeature()).isSameAs(local);
            assertThat(redefinition.isIsImplied()).isFalse();
        });
        assertThat(context.getFeatureMembership()).extracting(membership -> membership.getOwnedMemberFeature()).contains(local);
        assertThat(source.getOwningType()).isSameAs(sourceOwner);
        assertThat(source.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
        assertThat(sourceOwner.getOwnedRelationship()).containsExactlyElementsOf(sourceOwnerRelationships);
        assertThatThrownBy(() -> result.chain().add(source)).isInstanceOf(UnsupportedOperationException.class);
    }

    /**
     * Replaying the original selection reuses the local attribute even though the inherited source is no longer an effective member.
     */
    @Test
    @DisplayName("GIVEN an already materialized chain, WHEN replaying the original selection, THEN the same local feature is returned without duplication")
    public void repeatedOriginalChain() throws IOException {
        var model = this.loadRedefinedFeaturesModel();
        var context = model.get(PartDefinition.class, "RedefinedFeatures::Inherited");
        var source = model.get(AttributeUsage.class, BASE_X);
        var chain = List.<Feature>of(source);
        var first = this.service.redefineFeatureChain(context, chain);
        var relationships = List.copyOf(context.getOwnedRelationship());

        var repeated = this.service.redefineFeatureChain(context, chain);

        assertThat(repeated.isSuccess()).isTrue();
        assertThat(repeated.chain()).containsExactlyElementsOf(first.chain());
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
        assertThat(first.chain().getFirst().getOwnedRedefinition()).hasSize(1);
    }

    /**
     * An attribute directly owned by the context needs no redefinition and is returned unchanged.
     */
    @Test
    @DisplayName("GIVEN a directly owned attribute, WHEN materializing its chain, THEN the existing attribute is returned without modifying the context")
    public void directlyOwnedAttribute() throws IOException {
        var model = this.loadRedefinedFeaturesModel();
        var context = model.get(PartDefinition.class, "RedefinedFeatures::Base");
        var source = model.get(AttributeUsage.class, BASE_X);
        var relationships = List.copyOf(context.getOwnedRelationship());

        var result = this.service.redefineFeatureChain(context, List.of(source));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.chain()).containsExactly(source);
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
        assertThat(source.getOwnedRedefinition()).isEmpty();
    }

    /**
     * Missing context, chain or segment and an empty selection produce failure messages without modifying the context.
     */
    @Test
    @DisplayName("GIVEN missing or empty inputs, WHEN materializing a chain, THEN a failure identifies the invalid input without modifying the context")
    public void invalidInputs() throws IOException {
        var model = this.loadRedefinedFeaturesModel();
        var context = model.get(PartDefinition.class, "RedefinedFeatures::Inherited");
        var source = model.get(AttributeUsage.class, BASE_X);
        var relationships = List.copyOf(context.getOwnedRelationship());

        this.assertFailure(this.service.redefineFeatureChain(null, List.of(source)), "No configuration context was provided. Select a context before redefining a feature.");
        this.assertFailure(this.service.redefineFeatureChain(context, null), "Cannot redefine a feature in context 'RedefinedFeatures::Inherited': No feature chain was provided. Select a feature to redefine.");
        this.assertFailure(this.service.redefineFeatureChain(context, Arrays.asList((Feature) null)), "Cannot redefine a feature in context 'RedefinedFeatures::Inherited': segment 1 is missing. Select the feature chain again.");
        this.assertFailure(this.service.redefineFeatureChain(context, List.of()), "Cannot redefine a feature in context 'RedefinedFeatures::Inherited': the selected feature chain is empty. Select a feature to redefine.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * An inaccessible attribute is identified in the failure message, including literal apostrophes and braces in its name.
     */
    @Test
    @DisplayName("GIVEN an inaccessible attribute with special characters in its name, WHEN materializing its chain, THEN the failure preserves its name and identifies the context")
    public void inaccessibleAttribute() throws IOException {
        var model = this.loadRedefinedFeaturesModel();
        var context = model.get(PartDefinition.class, "RedefinedFeatures::Base");
        var source = model.get(AttributeUsage.class, "RedefinedFeatures::Renamed::renamed");
        source.setDeclaredName("owner's {0}");
        var relationships = List.copyOf(context.getOwnedRelationship());

        this.assertFailure(this.service.redefineFeatureChain(context, List.of(source)),
                "Cannot redefine segment 1 '%s' in context 'RedefinedFeatures::Base': the feature is not accessible from this context. Select an accessible feature chain again."
                        .formatted(source.getQualifiedName()));
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * Parameters and other feature categories are rejected explicitly until their implementation lots.
     */
    @Test
    @DisplayName("GIVEN a chain outside the supported scope, WHEN materializing it, THEN a failure explains the limitation without modifying the context")
    public void unsupportedChainsLeaveContextUnchanged() throws IOException {
        var model = this.loadRedefinedFeaturesModel();
        var context = model.get(PartDefinition.class, "RedefinedFeatures::Inherited");
        var relationships = List.copyOf(context.getOwnedRelationship());
        var parameter = SysmlFactory.eINSTANCE.createAttributeUsage();
        parameter.setDirection(FeatureDirectionKind.IN);
        this.assertFailure(this.service.redefineFeatureChain(context, List.of(parameter)),
                "Cannot redefine segment 1 unnamed AttributeUsage (id: %s) in context 'RedefinedFeatures::Inherited': redefining a parameter is not supported yet because the required parameter order must be preserved."
                        .formatted(parameter.getElementId()));
        var reference = SysmlFactory.eINSTANCE.createReferenceUsage();
        this.assertFailure(this.service.redefineFeatureChain(context, List.of(reference)),
                "Cannot redefine segment 1 unnamed ReferenceUsage (id: %s) in context 'RedefinedFeatures::Inherited': redefining a ReferenceUsage is not supported yet. Only non-directed parts and attributes can currently be redefined."
                        .formatted(reference.getElementId()));
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * Competing local redefinitions are identified by their qualified names rather than selecting one arbitrarily.
     */
    @Test
    @DisplayName("GIVEN competing local redefinitions, WHEN materializing their source chain, THEN a failure identifies the conflict without modifying the context")
    public void conflictingLocalRedefinitions() throws IOException {
        var model = this.loadRedefinedFeaturesModel();
        var context = model.get(PartDefinition.class, "RedefinedFeatures::Renamed");
        var source = model.get(AttributeUsage.class, BASE_X);
        var duplicate = SysmlFactory.eINSTANCE.createAttributeUsage();
        duplicate.setDeclaredName("otherValue");
        var redefinition = SysmlFactory.eINSTANCE.createRedefinition();
        redefinition.setRedefiningFeature(duplicate);
        redefinition.setRedefinedFeature(source);
        duplicate.getOwnedRelationship().add(redefinition);
        this.service.addChildInParent(context, duplicate);
        var relationships = List.copyOf(context.getOwnedRelationship());

        this.assertFailure(this.service.redefineFeatureChain(context, List.of(source)),
                "Cannot redefine segment 1 'RedefinedFeatures::Base::x' in context 'RedefinedFeatures::Renamed': Multiple local features redefine this feature: ['RedefinedFeatures::Renamed::renamed', 'RedefinedFeatures::Renamed::otherValue']. Resolve the conflicting redefinitions before trying again.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * Both inherited segments become local while the shared definitions and the other occurrence remain unchanged.
     */
    @Test
    @DisplayName("GIVEN two occurrences sharing a definition, WHEN materializing child.value in one occurrence, THEN both segments become local only in that occurrence")
    public void nestedInheritedFeatures() throws IOException {
        var model = this.loadNestedFeatureRedefinitionsModel();
        var context = model.get(PartUsage.class, "NestedFeatureRedefinitions::left");
        var other = model.get(PartUsage.class, "NestedFeatureRedefinitions::right");
        var root = model.get(PartDefinition.class, "NestedFeatureRedefinitions::Root");
        var childType = model.get(PartDefinition.class, "NestedFeatureRedefinitions::Child");
        var child = model.get(PartUsage.class, "NestedFeatureRedefinitions::Root::child");
        var value = model.get(AttributeUsage.class, "NestedFeatureRedefinitions::Child::value");
        var otherRelationships = List.copyOf(other.getOwnedRelationship());
        var rootRelationships = List.copyOf(root.getOwnedRelationship());
        var childRelationships = List.copyOf(child.getOwnedRelationship());
        var childTypeRelationships = List.copyOf(childType.getOwnedRelationship());

        var result = this.service.redefineFeatureChain(context, List.of(child, value));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.messages()).isEmpty();
        assertThat(result.chain()).hasSize(2);
        var localChild = result.chain().get(0);
        var localValue = result.chain().get(1);
        this.assertLocalRedefinition(localChild, context, child);
        this.assertLocalRedefinition(localValue, localChild, value);
        assertThat(localChild.isIsComposite()).isTrue();
        assertThat(localValue.isIsComposite()).isFalse();
        assertThat(context.getOwnedFeature()).containsExactly(localChild);
        assertThat(localChild.getOwnedFeature()).containsExactly(localValue);
        assertThat(localValue.getOwnedFeature()).isEmpty();
        assertThat(localChild.getOwnedTyping()).isEmpty();
        assertThat(localChild.getType()).contains(childType);
        assertThat(other.getOwnedRelationship()).containsExactlyElementsOf(otherRelationships);
        assertThat(other.getOwnedFeature()).isEmpty();
        assertThat(root.getOwnedRelationship()).containsExactlyElementsOf(rootRelationships);
        assertThat(child.getOwnedRelationship()).containsExactlyElementsOf(childRelationships);
        assertThat(childType.getOwnedRelationship()).containsExactlyElementsOf(childTypeRelationships);
        assertThat(value.getOwnedRedefinition()).isEmpty();
    }

    /**
     * A specialized definition receives the same local hierarchy as an occurrence.
     */
    @Test
    @DisplayName("GIVEN a specialized definition, WHEN materializing child.value, THEN each segment is owned by its preceding local context")
    public void nestedFeaturesInDefinition() throws IOException {
        var model = this.loadNestedFeatureRedefinitionsModel();
        var context = model.get(PartDefinition.class, "NestedFeatureRedefinitions::Specialized");
        var child = model.get(PartUsage.class, "NestedFeatureRedefinitions::Root::child");
        var value = model.get(AttributeUsage.class, "NestedFeatureRedefinitions::Child::value");

        var result = this.service.redefineFeatureChain(context, List.of(child, value));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.chain()).hasSize(2);
        this.assertLocalRedefinition(result.chain().get(0), context, child);
        this.assertLocalRedefinition(result.chain().get(1), result.chain().get(0), value);
    }

    /**
     * Three inherited levels require three distinct local features, with no fixed-depth restriction.
     */
    @Test
    @DisplayName("GIVEN a three-segment inherited chain, WHEN materializing child.deeper.z, THEN all three levels are created in order")
    public void threeInheritedLevels() throws IOException {
        var model = this.loadNestedFeatureRedefinitionsModel();
        var context = model.get(PartUsage.class, "NestedFeatureRedefinitions::left");
        var child = model.get(PartUsage.class, "NestedFeatureRedefinitions::Root::child");
        var deeper = model.get(PartUsage.class, "NestedFeatureRedefinitions::Child::deeper");
        var z = model.get(AttributeUsage.class, "NestedFeatureRedefinitions::Leaf::z");

        var result = this.service.redefineFeatureChain(context, List.of(child, deeper, z));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.chain()).hasSize(3);
        this.assertLocalRedefinition(result.chain().get(0), context, child);
        this.assertLocalRedefinition(result.chain().get(1), result.chain().get(0), deeper);
        this.assertLocalRedefinition(result.chain().get(2), result.chain().get(1), z);
        assertThat(result.chain().get(0).getOwnedFeature()).containsExactly(result.chain().get(1));
        assertThat(result.chain().get(1).getOwnedFeature()).containsExactly(result.chain().get(2));
    }

    /**
     * A valid inherited prefix must not be attached when a later segment is inaccessible.
     */
    @Test
    @DisplayName("GIVEN a valid inherited prefix followed by an inaccessible attribute, WHEN materializing the chain, THEN segment 2 is reported and no prefix is created")
    public void inaccessibleTerminalLeavesNoPrefix() throws IOException {
        var model = this.loadNestedFeatureRedefinitionsModel();
        var context = model.get(PartUsage.class, "NestedFeatureRedefinitions::left");
        var child = model.get(PartUsage.class, "NestedFeatureRedefinitions::Root::child");
        var z = model.get(AttributeUsage.class, "NestedFeatureRedefinitions::Leaf::z");
        var relationships = List.copyOf(context.getOwnedRelationship());
        var sourceRelationships = List.copyOf(child.getOwnedRelationship());

        var result = this.service.redefineFeatureChain(context, List.of(child, z));

        this.assertFailure(result,
                "Cannot redefine segment 2 'NestedFeatureRedefinitions::Leaf::z' in context 'NestedFeatureRedefinitions::left': the feature is not accessible from this context. Select an accessible feature chain again.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
        assertThat(context.getOwnedFeature()).isEmpty();
        assertThat(child.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
    }

    /**
     * A parameter encountered after a valid prefix is reported at its actual position without attaching the prefix.
     */
    @Test
    @DisplayName("GIVEN a valid inherited prefix followed by a parameter, WHEN materializing the chain, THEN segment 2 is rejected without creating the prefix")
    public void unsupportedParameterLeavesNoPrefix() throws IOException {
        var model = this.loadNestedFeatureRedefinitionsModel();
        var context = model.get(PartUsage.class, "NestedFeatureRedefinitions::left");
        var child = model.get(PartUsage.class, "NestedFeatureRedefinitions::Root::child");
        var value = model.get(AttributeUsage.class, "NestedFeatureRedefinitions::Child::value");
        value.setDirection(FeatureDirectionKind.IN);
        var relationships = List.copyOf(context.getOwnedRelationship());
        var sourceRelationships = List.copyOf(child.getOwnedRelationship());

        var result = this.service.redefineFeatureChain(context, List.of(child, value));

        this.assertFailure(result,
                "Cannot redefine segment 2 'NestedFeatureRedefinitions::Child::value' in context 'NestedFeatureRedefinitions::left': redefining a parameter is not supported yet because the required parameter order must be preserved.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
        assertThat(context.getOwnedFeature()).isEmpty();
        assertThat(child.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
    }

    /**
     * Checks semantic identity, ownership and anonymity independently of the feature category.
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

    /**
     * Loads the two-occurrence model; its XMI comment contains the original SysML model.
     */
    private SysMLTestModel loadNestedFeatureRedefinitionsModel() throws IOException {
        return this.loadModel("nested-feature-redefinitions");
    }

    /**
     * Checks the failure contract and the complete expected user-facing message, including punctuation and element labels.
     */
    private void assertFailure(FeatureChainRedefinitionResult result, String expectedMessage) {
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.chain()).isEmpty();
        assertThat(result.messages()).containsExactly(expectedMessage);
        assertThatThrownBy(() -> result.messages().add("another message")).isInstanceOf(UnsupportedOperationException.class);
    }

    /**
     * Loads the independent redefinition fixture; its XMI comment contains the original SysML model.
     */
    private SysMLTestModel loadRedefinedFeaturesModel() throws IOException {
        return this.loadModel("redefined-features");
    }

    private SysMLTestModel loadModel(String name) throws IOException {
        var resourceSet = new ResourceSetImpl();
        resourceSet.getPackageRegistry().put(SysmlPackage.eNS_URI, SysmlPackage.eINSTANCE);
        var resource = new XMIResourceImpl(URI.createURI("test:/" + name + ".xmi"));
        resourceSet.getResources().add(resource);
        try (var input = this.getClass().getResourceAsStream("/feature-chains/" + name + ".xmi")) {
            assertThat(input).isNotNull();
            resource.load(input, Map.of());
        }
        assertThat(resource.getErrors()).isEmpty();
        return new SysMLTestModel(resource);
    }
}
