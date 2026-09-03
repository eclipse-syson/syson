/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.application.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory.Descriptor;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.services.ComposedReadOnlyObjectPredicate;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.web.application.object.services.DefaultReadOnlyObjectPredicate;
import org.eclipse.syson.sysml.AnalysisCaseDefinition;
import org.eclipse.syson.sysml.AnalysisCaseUsage;
import org.eclipse.syson.sysml.CalculationDefinition;
import org.eclipse.syson.sysml.CalculationUsage;
import org.eclipse.syson.sysml.ConcernDefinition;
import org.eclipse.syson.sysml.ConcernUsage;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RenderingDefinition;
import org.eclipse.syson.sysml.RenderingUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.VerificationCaseDefinition;
import org.eclipse.syson.sysml.VerificationCaseUsage;
import org.eclipse.syson.sysml.ViewpointDefinition;
import org.eclipse.syson.sysml.ViewpointUsage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.eclipse.syson.sysml.metamodel.util.ElementUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link DetailsViewService}.
 *
 * @author gdaniel
 */
public class DetailsViewServiceTest {

    private DetailsViewService detailsViewService;

    @BeforeEach
    public void setUp() {
        // Use a dummy list of CompsedAdapterFactory.Descriptor, we don't test methods that require the one used by
        // SysON for the moment.
        List<Descriptor> composedAdapterFactoryDescriptors = List.of();
        this.detailsViewService = new DetailsViewService(composedAdapterFactoryDescriptors, new IFeedbackMessageService.NoOp(),
                new ComposedReadOnlyObjectPredicate(List.of(new SysONReadOnlyObjectPredicateDelegate()), new DefaultReadOnlyObjectPredicate()), new MetamodelQueryElementService(), List.of());
    }

    @Test
    public void getCoreFeaturesOfPartUsage() {
        List<EStructuralFeature> coreStructuralFeatures = this.detailsViewService.getCoreFeatures(SysmlFactory.eINSTANCE.createPartUsage());
        assertThat(coreStructuralFeatures).containsOnly(SysmlPackage.eINSTANCE.getElement_DeclaredName(),
                SysmlPackage.eINSTANCE.getElement_QualifiedName(),
                SysmlPackage.eINSTANCE.getElement_DeclaredShortName(),
                SysmlPackage.eINSTANCE.getFeature_Direction(),
                SysmlPackage.eINSTANCE.getOccurrenceUsage_IsIndividual());
    }

    /**
     * Verifies that a PartUsage exposes its PartDefinition reference in the {@code Typed by} widget.
     */
    @Test
    public void getTypedByReferenceNameOfPartUsage() {
        assertThat(this.detailsViewService.getTypedByReferenceName(SysmlFactory.eINSTANCE.createPartUsage())).isEqualTo("partDefinition");
    }

    /**
     * Verifies that the {@code Typed by} widget accepts a definition that conforms to a PartUsage.
     */
    @Test
    public void setPartUsageTypedByPartDefinition() {
        PartUsage partUsage = SysmlFactory.eINSTANCE.createPartUsage();
        PartDefinition partDefinition = SysmlFactory.eINSTANCE.createPartDefinition();

        this.detailsViewService.handleFeatureTypingNewValue(partUsage, partDefinition);

        assertThat(partUsage.getPartDefinition()).containsExactly(partDefinition);
    }

    /**
     * Verifies that an AnalysisCaseUsage derives the selected AnalysisCaseDefinition.
     */
    @Test
    public void setAnalysisCaseUsageTypedByAnalysisCaseDefinition() {
        AnalysisCaseUsage analysisCaseUsage = SysmlFactory.eINSTANCE.createAnalysisCaseUsage();
        AnalysisCaseDefinition analysisCaseDefinition = SysmlFactory.eINSTANCE.createAnalysisCaseDefinition();

        this.detailsViewService.handleFeatureTypingNewValue(analysisCaseUsage, analysisCaseDefinition);

        assertThat(analysisCaseUsage.getAnalysisCaseDefinition()).isSameAs(analysisCaseDefinition);
        assertThat(analysisCaseUsage.eIsSet(SysmlPackage.eINSTANCE.getAnalysisCaseUsage_AnalysisCaseDefinition())).isTrue();
    }

    /**
     * Verifies that a CalculationUsage derives the selected CalculationDefinition.
     */
    @Test
    public void setCalculationUsageTypedByCalculationDefinition() {
        CalculationUsage calculationUsage = SysmlFactory.eINSTANCE.createCalculationUsage();
        CalculationDefinition calculationDefinition = SysmlFactory.eINSTANCE.createCalculationDefinition();

        this.detailsViewService.handleFeatureTypingNewValue(calculationUsage, calculationDefinition);

        assertThat(calculationUsage.getCalculationDefinition()).isSameAs(calculationDefinition);
        assertThat(calculationUsage.eIsSet(SysmlPackage.eINSTANCE.getCalculationUsage_CalculationDefinition())).isTrue();
    }

    /**
     * Verifies that a ConcernUsage derives the selected ConcernDefinition.
     */
    @Test
    public void setConcernUsageTypedByConcernDefinition() {
        ConcernUsage concernUsage = SysmlFactory.eINSTANCE.createConcernUsage();
        ConcernDefinition concernDefinition = SysmlFactory.eINSTANCE.createConcernDefinition();

        this.detailsViewService.handleFeatureTypingNewValue(concernUsage, concernDefinition);

        assertThat(concernUsage.getConcernDefinition()).isSameAs(concernDefinition);
        assertThat(concernUsage.eIsSet(SysmlPackage.eINSTANCE.getConcernUsage_ConcernDefinition())).isTrue();
    }

    /**
     * Verifies that a RenderingUsage derives the selected RenderingDefinition.
     */
    @Test
    public void setRenderingUsageTypedByRenderingDefinition() {
        RenderingUsage renderingUsage = SysmlFactory.eINSTANCE.createRenderingUsage();
        RenderingDefinition renderingDefinition = SysmlFactory.eINSTANCE.createRenderingDefinition();

        this.detailsViewService.handleFeatureTypingNewValue(renderingUsage, renderingDefinition);

        assertThat(renderingUsage.getRenderingDefinition()).isSameAs(renderingDefinition);
        assertThat(renderingUsage.eIsSet(SysmlPackage.eINSTANCE.getRenderingUsage_RenderingDefinition())).isTrue();
    }

    /**
     * Verifies that a VerificationCaseUsage derives the selected VerificationCaseDefinition.
     */
    @Test
    public void setVerificationCaseUsageTypedByVerificationCaseDefinition() {
        VerificationCaseUsage verificationCaseUsage = SysmlFactory.eINSTANCE.createVerificationCaseUsage();
        VerificationCaseDefinition verificationCaseDefinition = SysmlFactory.eINSTANCE.createVerificationCaseDefinition();

        this.detailsViewService.handleFeatureTypingNewValue(verificationCaseUsage, verificationCaseDefinition);

        assertThat(verificationCaseUsage.getVerificationCaseDefinition()).isSameAs(verificationCaseDefinition);
        assertThat(verificationCaseUsage.eIsSet(SysmlPackage.eINSTANCE.getVerificationCaseUsage_VerificationCaseDefinition())).isTrue();
    }

    /**
     * Verifies that a ViewpointUsage derives the selected ViewpointDefinition.
     */
    @Test
    public void setViewpointUsageTypedByViewpointDefinition() {
        ViewpointUsage viewpointUsage = SysmlFactory.eINSTANCE.createViewpointUsage();
        ViewpointDefinition viewpointDefinition = SysmlFactory.eINSTANCE.createViewpointDefinition();

        this.detailsViewService.handleFeatureTypingNewValue(viewpointUsage, viewpointDefinition);

        assertThat(viewpointUsage.getViewpointDefinition()).isSameAs(viewpointDefinition);
        assertThat(viewpointUsage.eIsSet(SysmlPackage.eINSTANCE.getViewpointUsage_ViewpointDefinition())).isTrue();
    }

    /**
     * Verifies that dropping a PartUsage into the {@code Typed by} widget does not mutate the model.
     */
    @Test
    public void rejectPartUsageAsPartUsageTypedByValue() {
        PartUsage partUsage = SysmlFactory.eINSTANCE.createPartUsage();
        PartUsage droppedPartUsage = SysmlFactory.eINSTANCE.createPartUsage();

        this.detailsViewService.handleFeatureTypingNewValue(partUsage, droppedPartUsage);

        assertThat(partUsage.getOwnedRelationship()).isEmpty();
    }

    @Test
    public void getCoreFeaturesOfFeatureChaining() {
        List<EStructuralFeature> coreStructuralFeatures = this.detailsViewService.getCoreFeatures(SysmlFactory.eINSTANCE.createFeatureChaining());
        assertThat(coreStructuralFeatures).containsOnly(SysmlPackage.eINSTANCE.getElement_DeclaredName(),
                SysmlPackage.eINSTANCE.getElement_QualifiedName(),
                SysmlPackage.eINSTANCE.getElement_DeclaredShortName(),
                SysmlPackage.eINSTANCE.getFeatureChaining_ChainingFeature());
    }

    @Test
    public void getCoreFeaturesOfFeatureValue() {
        List<EStructuralFeature> coreStructuralFeatures = this.detailsViewService.getCoreFeatures(SysmlFactory.eINSTANCE.createFeatureValue());
        assertThat(coreStructuralFeatures).containsOnly(SysmlPackage.eINSTANCE.getMembership_Visibility(),
                SysmlPackage.eINSTANCE.getMembership_MemberElement(),
                SysmlPackage.eINSTANCE.getFeatureValue_IsDefault(),
                SysmlPackage.eINSTANCE.getFeatureValue_IsInitial());
    }

    @Test
    public void getCoreFeaturesOfMembership() {
        List<EStructuralFeature> coreStructuralFeatures = this.detailsViewService.getCoreFeatures(SysmlFactory.eINSTANCE.createOwningMembership());
        assertThat(coreStructuralFeatures).containsOnly(SysmlPackage.eINSTANCE.getMembership_Visibility(),
                SysmlPackage.eINSTANCE.getMembership_MemberElement());
    }

    @Test
    public void getCoreFeaturesOfRequirementDefinition() {
        List<EStructuralFeature> coreStructuralFeatures = this.detailsViewService.getCoreFeatures(SysmlFactory.eINSTANCE.createRequirementDefinition());
        assertThat(coreStructuralFeatures).containsOnly(SysmlPackage.eINSTANCE.getElement_DeclaredName(),
                SysmlPackage.eINSTANCE.getElement_QualifiedName(),
                SysmlPackage.eINSTANCE.getElement_DeclaredShortName(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_ReqId());
    }

    @Test
    public void getCoreFeaturesOfRequirementUsage() {
        List<EStructuralFeature> coreStructuralFeatures = this.detailsViewService.getCoreFeatures(SysmlFactory.eINSTANCE.createRequirementUsage());
        assertThat(coreStructuralFeatures).containsOnly(SysmlPackage.eINSTANCE.getElement_DeclaredName(),
                SysmlPackage.eINSTANCE.getElement_QualifiedName(),
                SysmlPackage.eINSTANCE.getElement_DeclaredShortName(),
                SysmlPackage.eINSTANCE.getFeature_Direction(),
                SysmlPackage.eINSTANCE.getRequirementUsage_ReqId());
    }

    @Test
    public void isReadOnlyElementInImportedLibrary() {
        Resource resource = new JSONResourceFactory().createResourceFromPath("testResource");
        Namespace namespace = SysmlFactory.eINSTANCE.createNamespace();
        resource.getContents().add(namespace);
        LibraryPackage libraryPackage = SysmlFactory.eINSTANCE.createLibraryPackage();
        OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        namespace.getOwnedRelationship().add(owningMembership);
        owningMembership.getOwnedRelatedElement().add(libraryPackage);
        ElementUtil.setIsImported(resource, true);
        assertThat(this.detailsViewService.isReadOnly(libraryPackage)).isFalse();
    }

    @Test
    public void isReadOnlyElementInImportedLibraryFlaggedAsReadOnly() {
        Resource resource = new JSONResourceFactory().createResourceFromPath("testResource");
        Namespace namespace = SysmlFactory.eINSTANCE.createNamespace();
        resource.getContents().add(namespace);
        LibraryPackage libraryPackage = SysmlFactory.eINSTANCE.createLibraryPackage();
        OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        namespace.getOwnedRelationship().add(owningMembership);
        owningMembership.getOwnedRelatedElement().add(libraryPackage);
        ElementUtil.setIsImported(resource, true);
        resource.eAdapters().add(new ResourceMetadataAdapter("test", true));
        assertThat(this.detailsViewService.isReadOnly(libraryPackage)).isTrue();
    }

    @Test
    public void isReadOnlyElementInImportedModel() {
        Resource resource = new JSONResourceFactory().createResourceFromPath("testResource");
        Namespace namespace = SysmlFactory.eINSTANCE.createNamespace();
        resource.getContents().add(namespace);
        Package pack = SysmlFactory.eINSTANCE.createPackage();
        OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        namespace.getOwnedRelationship().add(owningMembership);
        owningMembership.getOwnedRelatedElement().add(pack);
        ElementUtil.setIsImported(resource, true);
        assertThat(this.detailsViewService.isReadOnly(pack)).isFalse();
    }

    @Test
    public void isReadOnlyLibraryPackageIsStandardEAttribute() {
        assertThat(this.detailsViewService.isReadOnly(SysmlPackage.eINSTANCE.getLibraryPackage_IsStandard()));
    }
}
