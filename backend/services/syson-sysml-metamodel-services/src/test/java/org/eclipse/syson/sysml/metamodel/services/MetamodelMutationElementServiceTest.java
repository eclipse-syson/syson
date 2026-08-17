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

import java.util.List;
import java.util.Objects;

import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Metaclass;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
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

    private MetamodelMutationElementService mutationService;

    private MetamodelQueryElementService queryService;

    private Package owningPackage;

    private RequirementUsage original;

    private RequirementUsage derived;

    @BeforeEach
    public void setUp() {
        this.mutationService = new MetamodelMutationElementService();
        this.queryService = new MetamodelQueryElementService();

        this.owningPackage = SysmlFactory.eINSTANCE.createPackage();
        this.owningPackage.setDeclaredName("ReqTrace");
        this.original = this.createRequirement("article5Compliance");
        this.derived = this.createRequirement("paymentDelayPrevention");
    }

    @DisplayName("GIVEN two requirements, WHEN creating a requirement derivation between them, THEN the derivation is recognized as one and its ends are resolved")
    @Test
    public void createRequirementDerivation() {
        ConnectionUsage derivation = this.mutationService.createRequirementDerivation(this.derived, this.original, this.owningPackage,
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
        ConnectionUsage derivation = this.mutationService.createRequirementDerivation(this.derived, this.original, this.owningPackage,
                this.createMetadataDefinition(DERIVATION_METADATA),
                this.createMetadataDefinition(ORIGINAL_METADATA),
                this.createMetadataDefinition(DERIVED_METADATA));

        assertThat(this.appliedMetadataNamesOf(derivation.getConnectorEnd().get(0))).containsExactly(ORIGINAL_METADATA);
        assertThat(this.appliedMetadataNamesOf(derivation.getConnectorEnd().get(1))).containsExactly(DERIVED_METADATA);
        assertThat(this.queryService.getDerivationOriginalEnd(derivation)).isSameAs(this.original);
        assertThat(this.queryService.getDerivationDerivedEnd(derivation)).isSameAs(this.derived);
    }

    @DisplayName("GIVEN no metadata definition, WHEN applying a prefix metadata, THEN nothing is applied")
    @Test
    public void applyPrefixMetadataWithoutDefinition() {
        assertThat(this.mutationService.applyPrefixMetadata(this.original, null)).isNull();
        assertThat(this.appliedMetadataNamesOf(this.original)).isEmpty();
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
