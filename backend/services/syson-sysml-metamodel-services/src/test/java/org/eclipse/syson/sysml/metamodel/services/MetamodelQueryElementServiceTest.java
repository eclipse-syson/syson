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

import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.LiteralBoolean;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MetamodelQueryElementService}.
 *
 * @author arichard
 */
public class MetamodelQueryElementServiceTest {

    private RequirementUsage originalRequirement;

    private RequirementUsage derivedRequirement;

    /**
     * Verifies that a feature value expression is found from its owning feature.
     */
    @Test
    public void findSingleExpressionDefinitionInFeatureValueOfSuccessionAsUsage() {
        var service = new MetamodelQueryElementService();
        SuccessionAsUsage successionAsUsage = SysmlFactory.eINSTANCE.createSuccessionAsUsage();
        FeatureValue featureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        LiteralBoolean expression = SysmlFactory.eINSTANCE.createLiteralBoolean();

        successionAsUsage.getOwnedRelationship().add(featureValue);
        featureValue.getOwnedRelatedElement().add(expression);

        assertThat(service.findSingleExpressionDefinition(successionAsUsage)).containsSame(expression);
        assertThat(service.hasSingleExpressionDefinition(successionAsUsage)).isTrue();
    }

    /**
     * Verifies that an expression reached both directly and through a feature value is counted only once.
     */
    @Test
    public void findSingleExpressionDefinitionInFeatureValueOfPartUsage() {
        var service = new MetamodelQueryElementService();
        PartUsage partUsage = SysmlFactory.eINSTANCE.createPartUsage();
        FeatureValue featureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        LiteralBoolean expression = SysmlFactory.eINSTANCE.createLiteralBoolean();

        partUsage.getOwnedRelationship().add(featureValue);
        featureValue.getOwnedRelatedElement().add(expression);

        assertThat(service.findSingleExpressionDefinition(partUsage)).containsSame(expression);
        assertThat(service.hasSingleExpressionDefinition(partUsage)).isTrue();
    }

    /**
     * Verifies that a directly owned expression is found.
     */
    @Test
    public void findSingleExpressionDefinitionDirectlyOwnedByFeatureValue() {
        var service = new MetamodelQueryElementService();
        FeatureValue featureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        LiteralBoolean expression = SysmlFactory.eINSTANCE.createLiteralBoolean();

        featureValue.getOwnedRelatedElement().add(expression);

        assertThat(service.findSingleExpressionDefinition(featureValue)).containsSame(expression);
    }

    /**
     * Verifies that distinct feature value expressions remain ambiguous.
     */
    @Test
    public void findSingleExpressionDefinitionWithMultipleFeatureValueExpressions() {
        var service = new MetamodelQueryElementService();
        SuccessionAsUsage successionAsUsage = SysmlFactory.eINSTANCE.createSuccessionAsUsage();
        FeatureValue firstFeatureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        FeatureValue secondFeatureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        LiteralBoolean firstExpression = SysmlFactory.eINSTANCE.createLiteralBoolean();
        LiteralBoolean secondExpression = SysmlFactory.eINSTANCE.createLiteralBoolean();

        successionAsUsage.getOwnedRelationship().add(firstFeatureValue);
        successionAsUsage.getOwnedRelationship().add(secondFeatureValue);
        firstFeatureValue.getOwnedRelatedElement().add(firstExpression);
        secondFeatureValue.getOwnedRelatedElement().add(secondExpression);

        assertThat(service.findSingleExpressionDefinition(successionAsUsage)).isEmpty();
        assertThat(service.hasSingleExpressionDefinition(successionAsUsage)).isFalse();
    }

    /**
     * Verifies that a {@link ConnectionUsage} annotated with {@code #derivation} is detected as a requirement
     * derivation.
     */
    @Test
    public void isRequirementDerivationOnAnnotatedConnection() {
        var service = new MetamodelQueryElementService();
        var derivation = this.createDerivation(true, false);

        assertThat(service.isRequirementDerivation(derivation)).isTrue();
    }

    /**
     * Verifies that a plain {@link ConnectionUsage} is not detected as a requirement derivation.
     */
    @Test
    public void isRequirementDerivationOnPlainConnection() {
        var service = new MetamodelQueryElementService();
        var connection = SysmlFactory.eINSTANCE.createConnectionUsage();

        assertThat(service.isRequirementDerivation(connection)).isFalse();
    }

    /**
     * Verifies that the ends of a derivation are resolved from the {@code #original} and {@code #derive} metadata, and
     * not from the order in which the ends are declared.
     */
    @Test
    public void derivationEndsAreResolvedFromTheirMetadata() {
        var service = new MetamodelQueryElementService();
        var derivation = this.createDerivation(true, true);

        assertThat(service.getDerivationOriginalEnd(derivation)).isSameAs(this.originalRequirement);
        assertThat(service.getDerivationDerivedEnd(derivation)).isSameAs(this.derivedRequirement);
    }

    /**
     * Verifies that the ends of a derivation whose ends are not annotated fall back on the end order defined by the
     * {@code DerivationConnections::Derivation} connection definition.
     */
    @Test
    public void derivationEndsFallBackOnEndOrder() {
        var service = new MetamodelQueryElementService();
        var derivation = this.createDerivation(false, false);

        assertThat(service.getDerivationOriginalEnd(derivation)).isSameAs(this.originalRequirement);
        assertThat(service.getDerivationDerivedEnd(derivation)).isSameAs(this.derivedRequirement);
    }

    /**
     * Builds the equivalent of {@code #derivation connection { end #original ::> original; end #derive ::> derived; }}.
     *
     * @param annotateEnds
     *            whether the ends are annotated with the {@code #original} and {@code #derive} metadata
     * @param derivedEndFirst
     *            whether the derived end is declared before the original one
     * @return the created derivation
     */
    private ConnectionUsage createDerivation(boolean annotateEnds, boolean derivedEndFirst) {
        var library = SysmlFactory.eINSTANCE.createLibraryPackage();
        library.setDeclaredName("RequirementDerivation");
        var derivationMetadata = this.addMetadataDefinition(library, "DerivationMetadata");
        var originalMetadata = this.addMetadataDefinition(library, "OriginalRequirementMetadata");
        var derivedMetadata = this.addMetadataDefinition(library, "DerivedRequirementMetadata");

        this.originalRequirement = SysmlFactory.eINSTANCE.createRequirementUsage();
        this.originalRequirement.setDeclaredName("article5Compliance");
        this.derivedRequirement = SysmlFactory.eINSTANCE.createRequirementUsage();
        this.derivedRequirement.setDeclaredName("paymentDelayPrevention");

        var derivation = SysmlFactory.eINSTANCE.createConnectionUsage();
        this.applyMetadata(derivation, derivationMetadata);

        MetadataDefinition originalEndMetadata = null;
        MetadataDefinition derivedEndMetadata = null;
        if (annotateEnds) {
            originalEndMetadata = originalMetadata;
            derivedEndMetadata = derivedMetadata;
        }
        var originalEnd = this.createEnd(this.originalRequirement, originalEndMetadata);
        var derivedEnd = this.createEnd(this.derivedRequirement, derivedEndMetadata);
        if (derivedEndFirst) {
            derivation.getOwnedRelationship().add(derivedEnd);
            derivation.getOwnedRelationship().add(originalEnd);
        } else {
            derivation.getOwnedRelationship().add(originalEnd);
            derivation.getOwnedRelationship().add(derivedEnd);
        }
        return derivation;
    }

    private MetadataDefinition addMetadataDefinition(LibraryPackage library, String name) {
        var metadataDefinition = SysmlFactory.eINSTANCE.createMetadataDefinition();
        metadataDefinition.setDeclaredName(name);
        var membership = SysmlFactory.eINSTANCE.createOwningMembership();
        membership.getOwnedRelatedElement().add(metadataDefinition);
        library.getOwnedRelationship().add(membership);
        return metadataDefinition;
    }

    private void applyMetadata(Element element, MetadataDefinition metadataDefinition) {
        var metadataUsage = SysmlFactory.eINSTANCE.createMetadataUsage();
        var featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
        featureTyping.setType(metadataDefinition);
        metadataUsage.getOwnedRelationship().add(featureTyping);
        var membership = SysmlFactory.eINSTANCE.createOwningMembership();
        membership.getOwnedRelatedElement().add(metadataUsage);
        element.getOwnedRelationship().add(membership);
    }

    /**
     * Builds the equivalent of {@code end #original ::> referencedRequirement;}.
     * <p>
     * The SysML importer owns the ends of a connection body through a plain {@link FeatureMembership} and flags the end
     * feature with {@code isEnd = true}, rather than using an {@code EndFeatureMembership}, so the same shape is used
     * here.
     * </p>
     *
     * @param referencedRequirement
     *            the requirement referenced by the end
     * @param endMetadata
     *            the metadata applied on the end, or {@code null} for an end without metadata
     * @return the membership owning the created end
     */
    private FeatureMembership createEnd(RequirementUsage referencedRequirement, MetadataDefinition endMetadata) {
        var end = SysmlFactory.eINSTANCE.createReferenceUsage();
        end.setIsEnd(true);
        var referenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        referenceSubsetting.setReferencedFeature(referencedRequirement);
        end.getOwnedRelationship().add(referenceSubsetting);
        if (endMetadata != null) {
            this.applyMetadata(end, endMetadata);
        }
        var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        featureMembership.getOwnedRelatedElement().add(end);
        return featureMembership;
    }
}
