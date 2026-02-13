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
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.util.ElementUtil;
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
                new ComposedReadOnlyObjectPredicate(List.of(new SysONReadOnlyObjectPredicateDelegate()), new DefaultReadOnlyObjectPredicate()), List.of());
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
