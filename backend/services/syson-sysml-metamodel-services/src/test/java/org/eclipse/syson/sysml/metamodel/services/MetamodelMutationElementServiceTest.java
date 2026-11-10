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
package org.eclipse.syson.sysml.metamodel.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import java.util.List;
import java.util.Objects;

import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.Metaclass;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Test class for {@link MetamodelMutationElementService}.
 *
 * @author kabayama
 */
public class MetamodelMutationElementServiceTest {

    private static final String DERIVATION_METADATA = "DerivationMetadata";

    private static final String ORIGINAL_METADATA = "OriginalRequirementMetadata";

    private static final String DERIVED_METADATA = "DerivedRequirementMetadata";

    private static final String HOMONYM = "x";

    private final MetamodelMutationElementService mutationService = new MetamodelMutationElementService();

    private final MetamodelQueryElementService queryService = new MetamodelQueryElementService();

    private Package owningPackage;

    @BeforeEach
    public void setUp() {
        this.owningPackage = SysmlFactory.eINSTANCE.createPackage();
        this.owningPackage.setDeclaredName("ReqTrace");
    }

    @DisplayName("GIVEN two requirements, WHEN creating a requirement derivation between them, THEN the derivation is recognized as one and its ends are resolved")
    @Test
    public void createRequirementDerivation() {
        var original = this.createRequirement("article5Compliance");
        var derived = this.createRequirement("paymentDelayPrevention");
        ConnectionUsage derivation = this.mutationService.createRequirementDerivation(derived, original, this.owningPackage,
                this.createMetadataDefinition(DERIVATION_METADATA),
                this.createMetadataDefinition(ORIGINAL_METADATA),
                this.createMetadataDefinition(DERIVED_METADATA));

        assertThat(derivation).isNotNull();
        assertThat(derivation.getOwner()).isSameAs(this.owningPackage);
        assertThat(this.appliedMetadataNamesOf(derivation)).containsExactly(DERIVATION_METADATA);
        assertThat(derivation.getConnectorEnd()).hasSize(2);
        // A derivation is anonymous, the way it is when written in text.
        assertThat(derivation.getDeclaredName()).isNull();
    }

    @DisplayName("GIVEN a created requirement derivation, WHEN reading its ends, THEN the original and the derived requirements are the ones given at creation")
    @Test
    public void createdRequirementDerivationHasItsEndsAnnotated() {
        var original = this.createRequirement("article5Compliance");
        var derived = this.createRequirement("paymentDelayPrevention");
        ConnectionUsage derivation = this.mutationService.createRequirementDerivation(derived, original, this.owningPackage,
                this.createMetadataDefinition(DERIVATION_METADATA),
                this.createMetadataDefinition(ORIGINAL_METADATA),
                this.createMetadataDefinition(DERIVED_METADATA));

        assertThat(this.appliedMetadataNamesOf(derivation.getConnectorEnd().get(0))).containsExactly(ORIGINAL_METADATA);
        assertThat(this.appliedMetadataNamesOf(derivation.getConnectorEnd().get(1))).containsExactly(DERIVED_METADATA);
        assertThat(this.queryService.getDerivationOriginalEnd(derivation)).isSameAs(original);
        assertThat(this.queryService.getDerivationDerivedEnd(derivation)).isSameAs(derived);
    }

    @DisplayName("GIVEN no metadata definition, WHEN applying a prefix metadata, THEN nothing is applied")
    @Test
    public void applyPrefixMetadataWithoutDefinition() {
        var original = this.createRequirement("article5Compliance");
        assertThat(this.mutationService.applyPrefixMetadata(original, null)).isNull();
        assertThat(this.appliedMetadataNamesOf(original)).isEmpty();
    }

    /**
     * Directed parameters are redefined by the new references inside the flow ends, rather than by the flow itself.
     */
    @DisplayName("GIVEN two ActionUsage containing one parameter with direction each, WHEN creating a FlowUsage between the two parameters, THEN both FlowEnd of the created FlowUsage are redefining their respective parameter directly")
    @Test
    public void createFlowUsageBetweenFeatureWithDirection() {
        var source = SysmlFactory.eINSTANCE.createReferenceUsage();
        source.setDeclaredName("parameter1");
        source.setIsEnd(true);
        source.setDirection(FeatureDirectionKind.OUT);
        var sourceContainer = SysmlFactory.eINSTANCE.createActionUsage();
        sourceContainer.setDeclaredName("action1");
        this.mutationService.addChildInParent(sourceContainer, source);

        var target = SysmlFactory.eINSTANCE.createReferenceUsage();
        target.setDeclaredName("parameter2");
        target.setIsEnd(true);
        target.setDirection(FeatureDirectionKind.IN);
        var targetContainer = SysmlFactory.eINSTANCE.createActionUsage();
        targetContainer.setDeclaredName("action2");
        this.mutationService.addChildInParent(targetContainer, target);

        this.mutationService.addChildInParent(this.owningPackage, sourceContainer);
        this.mutationService.addChildInParent(this.owningPackage, targetContainer);

        var createdFlow = this.mutationService.createFlowUsage(source, target, sourceContainer, targetContainer, this.owningPackage);
        assertThat(source.getOwnedRelationship()).isEmpty();
        assertThat(createdFlow.getFlowEnd().getFirst().getFeatureMembership().getFirst().getOwnedMemberFeature().getOwnedRedefinition()).anySatisfy(redefinition -> {
            assertThat(redefinition.getRedefinedFeature()).isEqualTo(source);
            assertThat(redefinition.getRedefiningFeature()).isSameAs(redefinition.getOwningRelatedElement());
        });
        var sourceFlow = this.queryService.getSourceFlowUsageEdge(createdFlow);
        assertThat(sourceFlow).isEqualTo(source);

        assertThat(target.getOwnedRelationship()).isEmpty();
        assertThat(createdFlow.getFlowEnd().getLast().getFeatureMembership().getFirst().getOwnedMemberFeature().getOwnedRedefinition()).anySatisfy(redefinition -> {
            assertThat(redefinition.getRedefinedFeature()).isEqualTo(target);
            assertThat(redefinition.getRedefiningFeature()).isSameAs(redefinition.getOwningRelatedElement());
        });
        var targetFlow = this.queryService.getTargetFlowUsageEdge(createdFlow);
        assertThat(targetFlow).isEqualTo(target);
    }

    /**
     * Undirected parameters receive directed references, which are redefined by the new references inside the flow ends.
     */
    @DisplayName("GIVEN two ActionUsage containing one parameter without direction each, WHEN creating a FlowUsage between the two parameters, THEN a ReferenceUsage with a direction is created in each parameter and both FlowEnd of the created FlowUsage are redefining the created ReferenceUsage of their respective parameter")
    @Test
    public void createFlowUsageBetweenFeatureWithoutDirection() {
        var source = SysmlFactory.eINSTANCE.createReferenceUsage();
        source.setDeclaredName("parameter1");
        source.setIsEnd(true);
        var sourceContainer = SysmlFactory.eINSTANCE.createActionUsage();
        sourceContainer.setDeclaredName("action1");
        this.mutationService.addChildInParent(sourceContainer, source);

        var target = SysmlFactory.eINSTANCE.createReferenceUsage();
        target.setDeclaredName("parameter2");
        target.setIsEnd(true);
        var targetContainer = SysmlFactory.eINSTANCE.createActionUsage();
        targetContainer.setDeclaredName("action2");
        this.mutationService.addChildInParent(targetContainer, target);

        this.mutationService.addChildInParent(this.owningPackage, sourceContainer);
        this.mutationService.addChildInParent(this.owningPackage, targetContainer);

        var createdFlow = this.mutationService.createFlowUsage(source, target, sourceContainer, targetContainer, this.owningPackage);
        assertThat(source.getOwnedRelationship()).isNotEmpty();
        assertThat(source.getOwnedRelationship().getFirst().getOwnedRelatedElement()).anySatisfy(element -> {
            assertThat(element).isInstanceOf(ReferenceUsage.class)
                    .asInstanceOf(type(ReferenceUsage.class))
                    .satisfies(sourceReferenceUsage -> {
                        assertThat(sourceReferenceUsage.getDirection()).isEqualTo(FeatureDirectionKind.OUT);
                    });
            assertThat(createdFlow.getFlowEnd().getFirst().getFeatureMembership().getFirst().getOwnedMemberFeature().getOwnedRedefinition()).anySatisfy(redefinition -> {
                assertThat(redefinition.getRedefinedFeature()).isEqualTo(element);
                assertThat(redefinition.getRedefiningFeature()).isSameAs(redefinition.getOwningRelatedElement());
            });
        });
        var sourceFlow = this.queryService.getSourceFlowUsageEdge(createdFlow);
        assertThat(sourceFlow).isEqualTo(source);

        assertThat(target.getOwnedRelationship()).isNotEmpty();
        assertThat(target.getOwnedRelationship().getFirst().getOwnedRelatedElement()).anySatisfy(element -> {
            assertThat(element).isInstanceOf(ReferenceUsage.class)
                    .asInstanceOf(type(ReferenceUsage.class))
                    .satisfies(targetReferenceUsage -> {
                        assertThat(targetReferenceUsage.getDirection()).isEqualTo(FeatureDirectionKind.IN);
                    });
            assertThat(createdFlow.getFlowEnd().getLast().getFeatureMembership().getFirst().getOwnedMemberFeature().getOwnedRedefinition()).anySatisfy(redefinition -> {
                assertThat(redefinition.getRedefinedFeature()).isEqualTo(element);
                assertThat(redefinition.getRedefiningFeature()).isSameAs(redefinition.getOwningRelatedElement());
            });
        });
        var targetFlow = this.queryService.getTargetFlowUsageEdge(createdFlow);
        assertThat(targetFlow).isEqualTo(target);
    }

    /**
     * Binding creation materializes both feature chains using the requested inherited identities, leaving owned homonyms intact.
     *
     * @param inheritedShortName whether the inherited feature's short name participates in the conflict
     * @param ownedShortName whether the owned feature's short name participates in the conflict
     */
    @ParameterizedTest
    @CsvSource({"false, false", "false, true", "true, false", "true, true"})
    public void bindingChainsPreserveHomonyms(boolean inheritedShortName, boolean ownedShortName) {
        var builder = new ModelBuilder();
        var root = builder.createRootNamespace();
        var source = this.createInheritedEnd(builder, root, "source", FeatureDirectionKind.OUT, inheritedShortName, ownedShortName);
        var target = this.createInheritedEnd(builder, root, "target", FeatureDirectionKind.IN, inheritedShortName, ownedShortName);

        var binding = this.mutationService.createBindingConnectorAsUsage(source.inherited(), target.inherited(), source.occurrence(), target.occurrence(), root);

        assertThat(binding.getOwner()).isSameAs(root);
        assertThat(binding.getConnectorEnd()).hasSize(2);
        this.assertInheritedChain(binding.getConnectorEnd().getFirst(), source);
        this.assertInheritedChain(binding.getConnectorEnd().getLast(), target);
        this.assertHomonymUnchanged(source);
        this.assertHomonymUnchanged(target);
    }

    /**
     * Flow creation creates distinct redefining features for directed inherited ends without reusing owned homonyms.
     *
     * @param inheritedShortName whether the inherited feature's short name participates in the conflict
     * @param ownedShortName whether the owned feature's short name participates in the conflict
     */
    @ParameterizedTest
    @CsvSource({"false, false", "false, true", "true, false", "true, true"})
    public void flowRedefinitionsPreserveHomonyms(boolean inheritedShortName, boolean ownedShortName) {
        var builder = new ModelBuilder();
        var root = builder.createRootNamespace();
        var source = this.createInheritedEnd(builder, root, "source", FeatureDirectionKind.OUT, inheritedShortName, ownedShortName);
        var target = this.createInheritedEnd(builder, root, "target", FeatureDirectionKind.IN, inheritedShortName, ownedShortName);

        var flow = this.mutationService.createFlowUsage(source.inherited(), target.inherited(), source.occurrence(), target.occurrence(), root);

        assertThat(flow.getOwner()).isSameAs(root);
        assertThat(flow.getFlowEnd()).hasSize(2);
        var sourceEnd = flow.getFlowEnd().getFirst();
        var targetEnd = flow.getFlowEnd().getLast();
        this.assertInheritedChain(sourceEnd, source);
        this.assertInheritedChain(targetEnd, target);
        var sourceRedefining = sourceEnd.getOwnedFeature().getFirst();
        var targetRedefining = targetEnd.getOwnedFeature().getFirst();
        assertThat(sourceRedefining).isNotSameAs(targetRedefining);
        this.assertFlowRedefinition(sourceRedefining, sourceEnd, source);
        this.assertFlowRedefinition(targetRedefining, targetEnd, target);
        this.assertHomonymUnchanged(source);
        this.assertHomonymUnchanged(target);
    }

    /**
     * Creates an inherited directed end and an unrelated owned homonym with an existing typing relationship.
     *
     * @param builder the model builder providing inverse references
     * @param root the namespace containing the definitions and occurrence
     * @param name the occurrence name
     * @param direction the direction of the inherited end
     * @param inheritedShortName whether the inherited short name conflicts
     * @param ownedShortName whether the owned short name conflicts
     * @return the features and the original state of the owned homonym
     */
    private InheritedEndFixture createInheritedEnd(ModelBuilder builder, Element root, String name, FeatureDirectionKind direction, boolean inheritedShortName, boolean ownedShortName) {
        var definition = builder.createInWithName(PartDefinition.class, root, name + "Definition");
        var inherited = builder.createInWithName(ReferenceUsage.class, definition, "inherited");
        inherited.setIsEnd(true);
        inherited.setDirection(direction);
        if (inheritedShortName) {
            inherited.setDeclaredShortName(HOMONYM);
        } else {
            inherited.setDeclaredName(HOMONYM);
        }
        var occurrence = builder.createInWithName(PartUsage.class, root, name);
        builder.setType(occurrence, definition);
        var owned = builder.createInWithName(ReferenceUsage.class, occurrence, "owned");
        if (ownedShortName) {
            owned.setDeclaredShortName(HOMONYM);
        } else {
            owned.setDeclaredName(HOMONYM);
        }
        builder.setType(owned, builder.createInWithName(PartDefinition.class, root, name + "Value"));
        assertThat(owned.redefines(inherited)).isFalse();
        assertThat(occurrence.getInheritedFeature()).containsExactly(inherited);
        return new InheritedEndFixture(occurrence, inherited, owned, owned.getDeclaredName(), owned.getDeclaredShortName(), List.copyOf(owned.getOwnedRelationship()));
    }

    /**
     * Checks the persisted chain, including ownership by its reference subsetting, rather than only path lookup.
     *
     * @param connectorEnd the new connector end
     * @param fixture the requested inherited end
     */
    private void assertInheritedChain(Feature connectorEnd, InheritedEndFixture fixture) {
        var reference = connectorEnd.getOwnedReferenceSubsetting();
        assertThat(reference).isNotNull();
        var chain = reference.getReferencedFeature();
        assertThat(chain.eContainer()).isSameAs(reference);
        assertThat(chain.getChainingFeature()).containsExactly(fixture.occurrence(), fixture.inherited());
        assertThat(chain.getChainingFeature()).doesNotContain(fixture.owned());
    }

    /**
     * Checks both ends of the newly created redefinition and the identity of its owning flow feature.
     *
     * @param redefining the new flow reference feature
     * @param flowEnd the feature containing the new reference
     * @param fixture the requested inherited end
     */
    private void assertFlowRedefinition(Feature redefining, Feature flowEnd, InheritedEndFixture fixture) {
        assertThat(redefining).isNotSameAs(fixture.owned()).isNotSameAs(fixture.inherited());
        assertThat(redefining.getOwner()).isSameAs(flowEnd);
        assertThat(redefining.getOwnedRedefinition()).singleElement().satisfies(redefinition -> {
            assertThat(redefinition.getOwningRelatedElement()).isSameAs(redefining);
            assertThat(redefinition.getRedefinedFeature()).isSameAs(fixture.inherited());
            assertThat(redefinition.getRedefiningFeature()).isSameAs(redefining);
        });
    }

    /**
     * Verifies that creating chains and redefinitions does not modify the pre-existing owned homonym.
     *
     * @param fixture the original occurrence and homonym state
     */
    private void assertHomonymUnchanged(InheritedEndFixture fixture) {
        assertThat(fixture.occurrence().getOwnedFeature()).containsExactly(fixture.owned());
        assertThat(fixture.owned().getOwner()).isSameAs(fixture.occurrence());
        assertThat(fixture.owned().getDeclaredName()).isEqualTo(fixture.declaredName());
        assertThat(fixture.owned().getDeclaredShortName()).isEqualTo(fixture.declaredShortName());
        assertThat(fixture.owned().getOwnedRelationship()).containsExactlyElementsOf(fixture.relationships());
        assertThat(fixture.owned().getOwnedRedefinition()).isEmpty();
        assertThat(fixture.inherited().getOwnedRelationship()).isEmpty();
        assertThat(fixture.occurrence().getInheritedFeature()).containsExactly(fixture.inherited());
    }

    @Test
    @DisplayName("GIVEN two PartUsage, WHEN creating an allocate edge, then an AllocationUsage is created with the correct ends")
    public void createAllocateEdge() {
        Package parentPackage = SysmlFactory.eINSTANCE.createPackage();
        PartUsage partUsage1 = SysmlFactory.eINSTANCE.createPartUsage();
        this.mutationService.addChildInParent(parentPackage, partUsage1);
        PartUsage partUsage2 = SysmlFactory.eINSTANCE.createPartUsage();
        this.mutationService.addChildInParent(parentPackage, partUsage2);
        AllocationUsage allocationUsage = this.mutationService.createAllocateEdge(partUsage1, partUsage2);
        assertThat(allocationUsage).isNotNull();
        assertThat(allocationUsage.getFeature())
                .hasSize(2)
                .anySatisfy(feature -> assertThat(feature.getOwnedReferenceSubsetting().getReferencedFeature()).isEqualTo(partUsage1))
                .anySatisfy(feature -> assertThat(feature.getOwnedReferenceSubsetting().getReferencedFeature()).isEqualTo(partUsage2));
    }

    private RequirementUsage createRequirement(String name) {
        RequirementUsage requirement = SysmlFactory.eINSTANCE.createRequirementUsage();
        requirement.setDeclaredName(name);
        OwningMembership membership = SysmlFactory.eINSTANCE.createOwningMembership();
        membership.getOwnedRelatedElement().add(requirement);
        this.owningPackage.getOwnedRelationship().add(membership);
        return requirement;
    }

    private MetadataDefinition createMetadataDefinition(String name) {
        MetadataDefinition metadataDefinition = SysmlFactory.eINSTANCE.createMetadataDefinition();
        metadataDefinition.setDeclaredName(name);
        return metadataDefinition;
    }

    private List<String> appliedMetadataNamesOf(Element element) {
        return element.getOwnedRelationship().stream()
                .filter(OwningMembership.class::isInstance)
                .map(OwningMembership.class::cast)
                .flatMap(membership -> membership.getOwnedRelatedElement().stream())
                .filter(MetadataUsage.class::isInstance)
                .map(MetadataUsage.class::cast)
                .map(MetadataUsage::getMetadataDefinition)
                .filter(Objects::nonNull)
                .map(Metaclass::getDeclaredName)
                .toList();
    }

    /**
     * An inherited end, its unrelated owned homonym and the homonym's state before connector creation.
     *
     * @param occurrence the typed occurrence
     * @param inherited the inherited end
     * @param owned the unrelated owned homonym
     * @param declaredName the original declared name
     * @param declaredShortName the original declared short name
     * @param relationships the original relationships
     */
    private record InheritedEndFixture(PartUsage occurrence, ReferenceUsage inherited, ReferenceUsage owned, String declaredName, String declaredShortName, List<Relationship> relationships) {
    }
}
