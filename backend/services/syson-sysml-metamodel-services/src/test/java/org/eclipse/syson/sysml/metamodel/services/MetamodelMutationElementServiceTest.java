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

import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.Metaclass;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link MetamodelMutationElementService}.
 *
 * @author kabayama
 */
public class MetamodelMutationElementServiceTest {

    private static final String DERIVATION_METADATA = "DerivationMetadata";

    private static final String ORIGINAL_METADATA = "OriginalRequirementMetadata";

    private static final String DERIVED_METADATA = "DerivedRequirementMetadata";

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
        });
        var sourceFlow = this.queryService.getSourceFlowUsageEdge(createdFlow);
        assertThat(sourceFlow).isEqualTo(source);

        assertThat(target.getOwnedRelationship()).isEmpty();
        assertThat(createdFlow.getFlowEnd().getLast().getFeatureMembership().getFirst().getOwnedMemberFeature().getOwnedRedefinition()).anySatisfy(redefinition -> {
            assertThat(redefinition.getRedefinedFeature()).isEqualTo(target);
        });
        var targetFlow = this.queryService.getTargetFlowUsageEdge(createdFlow);
        assertThat(targetFlow).isEqualTo(target);
    }

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
            });
        });
        var targetFlow = this.queryService.getTargetFlowUsageEdge(createdFlow);
        assertThat(targetFlow).isEqualTo(target);
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
}
