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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.eclipse.syson.sysml.metamodel.services.redefinition.FeatureChainRedefinitionAssertions.assertChainSuccess;
import static org.eclipse.syson.sysml.metamodel.services.redefinition.FeatureChainRedefinitionAssertions.assertLocalRedefinition;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.LiteralInfinity;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.metamodel.services.redefine.FeatureChainRedefinitionResult;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.eclipse.syson.sysml.util.SysMLTestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Checks feature-chain selection, validation and rollback.
 *
 * @author Arthur Daussy
 */
public class FeatureChainRedefinitionSelectionTest {

    private static final String BASE_X = "RedefinedFeatures::Base::x";

    private final MetamodelMutationElementService service = new MetamodelMutationElementService();

    /**
     * Competing local redefinitions are identified by their qualified names rather than selecting one arbitrarily.
     */
    @Test
    @DisplayName("GIVEN competing local redefinitions, WHEN materializing their source chain, THEN a failure identifies the conflict without modifying the context")
    public void verifyConflictingLocalRedefinitions() throws IOException {
        // GIVEN Add a second local replacement of Base::x to a context that already has one.
        SysMLTestModel model = this.loadRedefinedFeaturesModel();
        PartDefinition context = model.get(PartDefinition.class, "RedefinedFeatures::Renamed");
        AttributeUsage source = model.get(AttributeUsage.class, BASE_X);
        AttributeUsage duplicate = SysmlFactory.eINSTANCE.createAttributeUsage();
        duplicate.setDeclaredName("otherValue");
        Redefinition redefinition = SysmlFactory.eINSTANCE.createRedefinition();
        redefinition.setRedefiningFeature(duplicate);
        redefinition.setRedefinedFeature(source);
        duplicate.getOwnedRelationship().add(redefinition);
        this.service.addChildInParent(context, duplicate);
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());

        // WHEN Request Base::x despite the two competing local replacements.
        this.assertFailure(this.service.redefineFeatureChain(context, List.of(source)),
                "Cannot redefine segment 1 'RedefinedFeatures::Base::x' in context 'RedefinedFeatures::Renamed': Multiple local features redefine this feature: ['RedefinedFeatures::Renamed::renamed', 'RedefinedFeatures::Renamed::otherValue']. Resolve the conflicting redefinitions before trying again.");
        // THEN Report the ambiguity and preserve the context's original relationships.
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * Directed items in ports follow ordinary feature redefinition and retain their direction in the local port.
     *
     * @param direction
     *            the direction of PowerIP::power
     */
    @ParameterizedTest
    @EnumSource(FeatureDirectionKind.class)
    @DisplayName("GIVEN a directed item in a port, WHEN materializing powerPort.power, THEN both segments are local and the item retains its direction")
    public void verifyDirectedPortFeature(FeatureDirectionKind direction) {
        // GIVEN port def PowerIP { out item power; } and part def Device { port powerPort : PowerIP; }.
        // Each invocation replaces 'out' with the tested direction before selecting the inherited path in device.
        ModelBuilder builder = new ModelBuilder();
        PortDefinition portType = builder.createWithName(PortDefinition.class, "PowerIP");
        ItemUsage power = builder.createInWithName(ItemUsage.class, portType, "power");
        power.setDirection(direction);
        PartDefinition deviceType = builder.createWithName(PartDefinition.class, "Device");
        PortUsage powerPort = builder.createInWithName(PortUsage.class, deviceType, "powerPort");
        builder.setType(powerPort, portType);
        PartUsage context = builder.createWithName(PartUsage.class, "device");
        builder.setType(context, deviceType);

        // WHEN materializing the complete path through the normal service entry point.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(powerPort, power));

        // THEN the directed item is redefined inside the new local port, with its exact category and direction.
        assertChainSuccess(result);
        assertThat(result.chain()).hasSize(2);
        Feature localPort = result.chain().getFirst();
        Feature localPower = result.chain().getLast();
        assertLocalRedefinition(localPort, context, powerPort);
        assertLocalRedefinition(localPower, localPort, power);
        assertThat(localPort).isInstanceOf(PortUsage.class);
        assertThat(localPower).isInstanceOf(ItemUsage.class);
        assertThat(localPower.getDirection()).isEqualTo(direction);
        assertThat(localPort.getOwnedFeature()).containsExactly(localPower);
        assertThat(power.getDirection()).isEqualTo(direction);
    }

    /**
     * An attribute directly owned by the context needs no redefinition and is returned unchanged.
     */
    @Test
    @DisplayName("GIVEN a directly owned attribute, WHEN materializing its chain, THEN the existing attribute is returned without modifying the context")
    public void verifyDirectlyOwnedAttribute() throws IOException {
        // GIVEN Base::x already belongs directly to the configuration context.
        SysMLTestModel model = this.loadRedefinedFeaturesModel();
        PartDefinition context = model.get(PartDefinition.class, "RedefinedFeatures::Base");
        AttributeUsage source = model.get(AttributeUsage.class, BASE_X);
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());

        // WHEN Request that owned attribute.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source));

        // THEN Reuse the original object without adding a redefinition.
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.chain()).containsExactly(source);
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
        assertThat(source.getOwnedRedefinition()).isEmpty();
    }

    /**
     * An inaccessible attribute is identified in the failure message, including literal apostrophes and braces in its name.
     */
    @Test
    @DisplayName("GIVEN an inaccessible attribute with special characters in its name, WHEN materializing its chain, THEN the failure preserves its name and identifies the context")
    public void verifyInaccessibleAttribute() throws IOException {
        // GIVEN The requested attribute belongs to a specialization and is inaccessible from Base; its name contains formatting characters.
        SysMLTestModel model = this.loadRedefinedFeaturesModel();
        PartDefinition context = model.get(PartDefinition.class, "RedefinedFeatures::Base");
        AttributeUsage source = model.get(AttributeUsage.class, "RedefinedFeatures::Renamed::renamed");
        source.setDeclaredName("owner's {0}");
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());

        // WHEN Request the inaccessible feature.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source));
        this.assertFailure(result,
                "Cannot redefine segment 1 '%s' in context 'RedefinedFeatures::Base': the feature is not accessible from this context. Select an accessible feature chain again."
                        .formatted(source.getQualifiedName()));
        // THEN Report its actual name safely and preserve the original context.
        // The failure location identifies the original selection using model identity.
        assertThat(result.failureContext()).isSameAs(context);
        assertThat(result.failedSegmentIndex()).isEqualTo(0);
        assertThat(result.failedFeature()).isSameAs(source);
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * Failure on an inaccessible segment rolls back the inherited prefix.
     */
    @Test
    @DisplayName("GIVEN a valid inherited prefix followed by an inaccessible attribute, WHEN materializing the chain, THEN segment 2 is reported and no prefix is created")
    public void verifyInaccessibleTerminalLeavesNoPrefix() throws IOException {
        // GIVEN child is accessible, but Leaf::z cannot be selected directly below it.
        SysMLTestModel model = this.loadNestedFeatureRedefinitionsModel();
        PartUsage context = model.get(PartUsage.class, "NestedFeatureRedefinitions::left");
        PartUsage child = model.get(PartUsage.class, "NestedFeatureRedefinitions::Root::child");
        AttributeUsage z = model.get(AttributeUsage.class, "NestedFeatureRedefinitions::Leaf::z");
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());
        List<Relationship> sourceRelationships = List.copyOf(child.getOwnedRelationship());

        // WHEN Materialize child.z, which fails only after creating the local child.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(child, z));

        // THEN Remove the new prefix and preserve the inherited child definition.
        this.assertFailure(result,
                "Cannot redefine segment 2 'NestedFeatureRedefinitions::Leaf::z' in context 'NestedFeatureRedefinitions::left': the feature is not accessible from this context. Select an accessible feature chain again.");
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
        assertThat(context.getOwnedFeature()).isEmpty();
        assertThat(child.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
    }

    /**
     * An inherited attribute becomes an anonymous local attribute with an explicit redefinition, without modifying its source.
     */
    @Test
    @DisplayName("GIVEN an inherited attribute, WHEN materializing its chain, THEN an anonymous local attribute redefines the unchanged source")
    public void verifyInheritedAttribute() throws IOException {
        // GIVEN The context inherits Base::x and has no local replacement.
        SysMLTestModel model = this.loadRedefinedFeaturesModel();
        PartDefinition context = model.get(PartDefinition.class, "RedefinedFeatures::Inherited");
        AttributeUsage source = model.get(AttributeUsage.class, BASE_X);
        Type sourceOwner = source.getOwningType();
        List<Relationship> sourceRelationships = List.copyOf(source.getOwnedRelationship());
        List<Relationship> sourceOwnerRelationships = List.copyOf(sourceOwner.getOwnedRelationship());

        // WHEN Materialize the inherited attribute.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source));

        // THEN Create an anonymous local attribute whose redefinition targets the original source.
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.messages()).isEmpty();
        assertThat(result.chain()).hasSize(1);
        Feature local = result.chain().getFirst();
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
     * KerML 1.1 Beta 2, 7.3.4.4-5: a redefinition of an ordered feature must remain ordered.
     * The fixture declares a multi-valued ordered attribute so the chain command exercises this requirement end to end.
     */
    @Test
    @DisplayName("GIVEN an ordered inherited attribute, WHEN materializing its chain, THEN its local redefinition remains ordered")
    public void verifyInheritedOrderedAttribute() throws IOException {
        // GIVEN The source declares an ordered unbounded multiplicity.
        SysMLTestModel model = this.loadRedefinedFeaturesModel();
        PartDefinition context = model.get(PartDefinition.class, "RedefinedFeatures::OrderedInherited");
        AttributeUsage source = model.get(AttributeUsage.class, "RedefinedFeatures::OrderedBase::values");
        assertThat(source.isIsOrdered()).isTrue();
        assertThat(source.getMultiplicity()).isInstanceOfSatisfying(MultiplicityRange.class, range -> {
            assertThat(range.getBound()).hasSize(2);
            assertThat(range.getBound().getFirst()).isInstanceOfSatisfying(LiteralInteger.class, lower -> assertThat(lower.getValue()).isZero());
            assertThat(range.getBound().getLast()).isInstanceOf(LiteralInfinity.class);
        });

        // WHEN Materialize the attribute in its specialization.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source));

        // THEN Propagate ordering without copying the source's multiplicity object.
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.chain()).singleElement().satisfies(local -> {
            assertLocalRedefinition(local, context, source);
            assertThat(local.isIsOrdered()).isTrue();
        });
        assertThat(source.isIsOrdered()).isTrue();
    }

    /**
     * Missing context, chain or segment and an empty selection produce failure messages without modifying the context.
     */
    @Test
    @DisplayName("GIVEN missing or empty inputs, WHEN materializing a chain, THEN a failure identifies the invalid input without modifying the context")
    public void verifyInvalidInputs() throws IOException {
        // GIVEN Snapshot an unchanged context before submitting malformed calls.
        SysMLTestModel model = this.loadRedefinedFeaturesModel();
        PartDefinition context = model.get(PartDefinition.class, "RedefinedFeatures::Inherited");
        AttributeUsage source = model.get(AttributeUsage.class, BASE_X);
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());

        // WHEN Exercise missing contexts, missing chains and missing segments.
        FeatureChainRedefinitionResult missingContext = this.service.redefineFeatureChain(null, List.of(source));
        this.assertFailure(missingContext, "No configuration context was provided. Select a context before redefining a feature.");
        // THEN Each rejection identifies the missing input and preserves the context.
        FeatureChainRedefinitionResult missingChain = this.service.redefineFeatureChain(context, null);
        this.assertFailure(missingChain, "Cannot redefine a feature in context 'RedefinedFeatures::Inherited': No feature chain was provided. Select a feature to redefine.");
        FeatureChainRedefinitionResult missingSegment = this.service.redefineFeatureChain(context, Arrays.asList((Feature) null));
        this.assertFailure(missingSegment, "Cannot redefine a feature in context 'RedefinedFeatures::Inherited': segment 1 is missing. Select the feature chain again.");
        FeatureChainRedefinitionResult emptyChain = this.service.redefineFeatureChain(context, List.of());
        this.assertFailure(emptyChain, "Cannot redefine a feature in context 'RedefinedFeatures::Inherited': the selected feature chain is empty. Select a feature to redefine.");
        assertThat(missingContext.failureContext()).isNull();
        assertThat(missingContext.failedSegmentIndex()).isNull();
        assertThat(missingContext.failedFeature()).isNull();
        assertThat(missingChain.failureContext()).isSameAs(context);
        assertThat(missingChain.failedSegmentIndex()).isNull();
        assertThat(missingChain.failedFeature()).isNull();
        assertThat(missingSegment.failureContext()).isSameAs(context);
        assertThat(missingSegment.failedSegmentIndex()).isEqualTo(0);
        assertThat(missingSegment.failedFeature()).isNull();
        assertThat(emptyChain.failureContext()).isSameAs(context);
        assertThat(emptyChain.failedSegmentIndex()).isNull();
        assertThat(emptyChain.failedFeature()).isNull();
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * A specialized definition receives the same local hierarchy as an occurrence.
     */
    @Test
    @DisplayName("GIVEN a specialized definition, WHEN materializing child.value, THEN each segment is owned by its preceding local context")
    public void verifyNestedFeaturesInDefinition() throws IOException {
        // GIVEN The definition inherits child, whose type exposes value.
        SysMLTestModel model = this.loadNestedFeatureRedefinitionsModel();
        PartDefinition context = model.get(PartDefinition.class, "NestedFeatureRedefinitions::Specialized");
        PartUsage child = model.get(PartUsage.class, "NestedFeatureRedefinitions::Root::child");
        AttributeUsage value = model.get(AttributeUsage.class, "NestedFeatureRedefinitions::Child::value");

        // WHEN Materialize child.value in the specialized definition.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(child, value));

        // THEN Attach value below the newly created child rather than below the shared source.
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.chain()).hasSize(2);
        assertLocalRedefinition(result.chain().get(0), context, child);
        assertLocalRedefinition(result.chain().get(1), result.chain().get(0), value);
    }

    /**
     * Both inherited segments become local while the shared definitions and the other occurrence remain unchanged.
     */
    @Test
    @DisplayName("GIVEN two occurrences sharing a definition, WHEN materializing child.value in one occurrence, THEN both segments become local only in that occurrence")
    public void verifyNestedInheritedFeatures() throws IOException {
        // GIVEN Two occurrences share Root and Child definitions.
        SysMLTestModel model = this.loadNestedFeatureRedefinitionsModel();
        PartUsage context = model.get(PartUsage.class, "NestedFeatureRedefinitions::left");
        PartUsage other = model.get(PartUsage.class, "NestedFeatureRedefinitions::right");
        PartDefinition root = model.get(PartDefinition.class, "NestedFeatureRedefinitions::Root");
        PartDefinition childType = model.get(PartDefinition.class, "NestedFeatureRedefinitions::Child");
        PartUsage child = model.get(PartUsage.class, "NestedFeatureRedefinitions::Root::child");
        AttributeUsage value = model.get(AttributeUsage.class, "NestedFeatureRedefinitions::Child::value");
        List<Relationship> otherRelationships = List.copyOf(other.getOwnedRelationship());
        List<Relationship> rootRelationships = List.copyOf(root.getOwnedRelationship());
        List<Relationship> childRelationships = List.copyOf(child.getOwnedRelationship());
        List<Relationship> childTypeRelationships = List.copyOf(childType.getOwnedRelationship());

        // WHEN Materialize child.value in the left occurrence.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(child, value));

        // THEN Keep the local chain in the left occurrence and preserve the right occurrence and shared definitions.
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.messages()).isEmpty();
        assertThat(result.chain()).hasSize(2);
        Feature localChild = result.chain().get(0);
        Feature localValue = result.chain().get(1);
        assertLocalRedefinition(localChild, context, child);
        assertLocalRedefinition(localValue, localChild, value);
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
     * Replaying the original selection reuses the local attribute even though the inherited source is no longer an effective member.
     */
    @Test
    @DisplayName("GIVEN an already materialized chain, WHEN replaying the original selection, THEN the same local feature is returned without duplication")
    public void verifyRepeatedOriginalChain() throws IOException {
        // GIVEN Keep the original inherited selection after creating its first local replacement.
        SysMLTestModel model = this.loadRedefinedFeaturesModel();
        PartDefinition context = model.get(PartDefinition.class, "RedefinedFeatures::Inherited");
        AttributeUsage source = model.get(AttributeUsage.class, BASE_X);
        List<Feature> chain = List.<Feature>of(source);
        // WHEN Submit the original selection again.
        FeatureChainRedefinitionResult first = this.service.redefineFeatureChain(context, chain);
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());

        FeatureChainRedefinitionResult repeated = this.service.redefineFeatureChain(context, chain);

        // THEN Return the same local object without adding another membership or redefinition.
        assertThat(repeated.isSuccess()).isTrue();
        assertThat(repeated.chain()).containsExactlyElementsOf(first.chain());
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
        assertThat(first.chain().getFirst().getOwnedRedefinition()).hasSize(1);
    }

    /**
     * Three inherited levels require three distinct local features, with no fixed-depth restriction.
     */
    @Test
    @DisplayName("GIVEN a three-segment inherited chain, WHEN materializing child.deeper.z, THEN all three levels are created in order")
    public void verifyThreeInheritedLevels() throws IOException {
        // GIVEN The selected path is child.deeper.z across three declaring types.
        SysMLTestModel model = this.loadNestedFeatureRedefinitionsModel();
        PartUsage context = model.get(PartUsage.class, "NestedFeatureRedefinitions::left");
        PartUsage child = model.get(PartUsage.class, "NestedFeatureRedefinitions::Root::child");
        PartUsage deeper = model.get(PartUsage.class, "NestedFeatureRedefinitions::Child::deeper");
        AttributeUsage z = model.get(AttributeUsage.class, "NestedFeatureRedefinitions::Leaf::z");

        // WHEN Materialize the complete path from the outer occurrence.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(child, deeper, z));

        // THEN Each local segment owns the next one, and only the selected three segments are returned.
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.chain()).hasSize(3);
        assertLocalRedefinition(result.chain().get(0), context, child);
        assertLocalRedefinition(result.chain().get(1), result.chain().get(0), deeper);
        assertLocalRedefinition(result.chain().get(2), result.chain().get(1), z);
        assertThat(result.chain().get(0).getOwnedFeature()).containsExactly(result.chain().get(1));
        assertThat(result.chain().get(1).getOwnedFeature()).containsExactly(result.chain().get(2));
    }

    /**
     * Materializing an undirected feature must preserve EMF's unset direction state so subsequent flow
     * creation can recognize that it needs a directed reference inside the endpoint.
     */
    @Test
    @DisplayName("GIVEN an undirected inherited attribute, WHEN materializing its redefinition, THEN its local direction remains unset")
    public void verifyUndirectedFeatureDirectionRemainsUnset() {
        // GIVEN part def Base { attribute value; } and part context : Base.
        ModelBuilder builder = new ModelBuilder();
        PartDefinition base = builder.createWithName(PartDefinition.class, "Base");
        AttributeUsage source = builder.createInWithName(AttributeUsage.class, base, "value");
        PartUsage context = builder.createWithName(PartUsage.class, "context");
        builder.setType(context, base);
        assertThat(source.isSetDirection()).isFalse();

        // WHEN creating a local redefinition through the chain service.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(source));

        // THEN both the direction value and the unset state are preserved on the new local feature.
        assertChainSuccess(result);
        assertThat(result.chain()).singleElement().satisfies(local -> {
            assertLocalRedefinition(local, context, source);
            assertThat(local.getDirection()).isNull();
            assertThat(local.isSetDirection()).isFalse();
        });
    }

    /**
     * Plain KerML features remain outside the supported SysML usage scope.
     */
    @Test
    @DisplayName("GIVEN a chain outside the supported scope, WHEN materializing it, THEN a failure explains the limitation without modifying the context")
    public void verifyUnsupportedChainsLeaveContextUnchanged() throws IOException {
        // GIVEN A part context and a plain KerML feature outside the supported usage categories.
        SysMLTestModel model = this.loadRedefinedFeaturesModel();
        PartDefinition context = model.get(PartDefinition.class, "RedefinedFeatures::Inherited");
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());
        Feature feature = SysmlFactory.eINSTANCE.createFeature();
        // WHEN Request a plain KerML feature through the chain service.
        // THEN Return a failure with the original context relationships preserved.
        this.assertFailure(this.service.redefineFeatureChain(context, List.of(feature)),
                "Cannot redefine segment 1 unnamed Feature (id: %s) in context 'RedefinedFeatures::Inherited': redefining a Feature is not supported. Select a SysML usage."
                        .formatted(feature.getElementId()));
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
    }

    /**
     * A plain KerML feature is rejected at its actual segment position and the new local prefix is rolled back.
     */
    @Test
    @DisplayName("GIVEN a valid prefix followed by a plain KerML feature, WHEN materializing the chain, THEN segment 2 is rejected and the new prefix is rolled back")
    public void verifyUnsupportedFeatureLeavesNoPrefix() throws IOException {
        // GIVEN Add a plain KerML feature to Child after the valid inherited child prefix.
        SysMLTestModel model = this.loadNestedFeatureRedefinitionsModel();
        PartUsage context = model.get(PartUsage.class, "NestedFeatureRedefinitions::left");
        PartUsage child = model.get(PartUsage.class, "NestedFeatureRedefinitions::Root::child");
        Type childType = model.get(Type.class, "NestedFeatureRedefinitions::Child");
        Feature unsupported = new ModelBuilder().createInWithName(Feature.class, childType, "unsupported");
        List<Relationship> relationships = List.copyOf(context.getOwnedRelationship());
        List<Relationship> sourceRelationships = List.copyOf(child.getOwnedRelationship());

        // WHEN Materialize child.unsupported so the second segment is rejected.
        FeatureChainRedefinitionResult result = this.service.redefineFeatureChain(context, List.of(child, unsupported));

        // THEN Remove the newly attached child and preserve all source relationships.
        this.assertFailure(result,
                "Cannot redefine segment 2 'NestedFeatureRedefinitions::Child::unsupported' in context 'NestedFeatureRedefinitions::left': redefining a Feature is not supported. Select a SysML usage.");
        // The failure location uses original model objects even after newly created prefixes have been removed.
        assertThat(result.failureContext()).isSameAs(context);
        assertThat(result.failedSegmentIndex()).isEqualTo(1);
        assertThat(result.failedFeature()).isSameAs(unsupported);
        assertThat(context.getOwnedRelationship()).containsExactlyElementsOf(relationships);
        assertThat(context.getOwnedFeature()).isEmpty();
        assertThat(child.getOwnedRelationship()).containsExactlyElementsOf(sourceRelationships);
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

    /** Loads a named feature-chain XMI fixture. */
    private SysMLTestModel loadModel(String name) throws IOException {
        return SysMLTestModel.load("/feature-chains/" + name + ".xmi");
    }

    /**
     * Loads the two-occurrence model; its XMI comment contains the original SysML model.
     */
    private SysMLTestModel loadNestedFeatureRedefinitionsModel() throws IOException {
        return this.loadModel("nested-feature-redefinitions");
    }

    /**
     * Loads the independent redefinition fixture; its XMI comment contains the original SysML model.
     */
    private SysMLTestModel loadRedefinedFeaturesModel() throws IOException {
        return this.loadModel("redefined-features");
    }
}
