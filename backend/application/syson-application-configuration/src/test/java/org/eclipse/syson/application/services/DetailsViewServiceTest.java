/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.web.application.object.services.ComposedReadOnlyObjectPredicate;
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
        // Use a dummy CompsedAdapterFactory, we don't test methods that require the one used by SysON for the moment.
        this.detailsViewService = new DetailsViewService(new ComposedAdapterFactory(), new IFeedbackMessageService.NoOp(),
                new ComposedReadOnlyObjectPredicate(List.of(new SysONReadOnlyObjectPredicateDelegate(new SysONResourceService())), new DefaultReadOnlyObjectPredicate()));
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
                SysmlPackage.eINSTANCE.getFeatureValue_IsDefault(),
                SysmlPackage.eINSTANCE.getFeatureValue_IsInitial());
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
