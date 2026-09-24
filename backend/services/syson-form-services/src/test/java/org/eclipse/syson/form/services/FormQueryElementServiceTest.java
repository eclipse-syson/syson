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
package org.eclipse.syson.form.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link FormQueryElementServiceTest}.
 *
 * @author gdaniel
 */
public class FormQueryElementServiceTest {

    private FormQueryElementService formQueryElementService;

    @BeforeEach
    public void setUp() {
        this.formQueryElementService = new FormQueryElementService(List.of(), element -> false, new MetamodelQueryElementService(), List.of());
    }

    @Test
    public void getCoreFeaturesOfPartUsage() {
        List<EStructuralFeature> coreStructuralFeatures = this.formQueryElementService.getCoreFeatures(SysmlFactory.eINSTANCE.createPartUsage());
        assertThat(coreStructuralFeatures).containsOnly(SysmlPackage.eINSTANCE.getElement_DeclaredName(),
                SysmlPackage.eINSTANCE.getElement_QualifiedName(),
                SysmlPackage.eINSTANCE.getElement_DeclaredShortName(),
                SysmlPackage.eINSTANCE.getFeature_Direction(),
                SysmlPackage.eINSTANCE.getOccurrenceUsage_IsIndividual());
    }

    @Test
    public void getTypedByReferenceNameOfPartUsage() {
        assertThat(this.formQueryElementService.getTypedByReferenceName(SysmlFactory.eINSTANCE.createPartUsage())).isEqualTo("partDefinition");
    }

    @Test
    public void getCoreFeaturesOfFeatureChaining() {
        List<EStructuralFeature> coreStructuralFeatures = this.formQueryElementService.getCoreFeatures(SysmlFactory.eINSTANCE.createFeatureChaining());
        assertThat(coreStructuralFeatures).containsOnly(SysmlPackage.eINSTANCE.getElement_DeclaredName(),
                SysmlPackage.eINSTANCE.getElement_QualifiedName(),
                SysmlPackage.eINSTANCE.getElement_DeclaredShortName(),
                SysmlPackage.eINSTANCE.getFeatureChaining_ChainingFeature());
    }

    @Test
    public void getCoreFeaturesOfFeatureValue() {
        List<EStructuralFeature> coreStructuralFeatures = this.formQueryElementService.getCoreFeatures(SysmlFactory.eINSTANCE.createFeatureValue());
        assertThat(coreStructuralFeatures).containsOnly(SysmlPackage.eINSTANCE.getMembership_Visibility(),
                SysmlPackage.eINSTANCE.getMembership_MemberElement(),
                SysmlPackage.eINSTANCE.getFeatureValue_IsDefault(),
                SysmlPackage.eINSTANCE.getFeatureValue_IsInitial());
    }

    @Test
    public void getCoreFeaturesOfMembership() {
        List<EStructuralFeature> coreStructuralFeatures = this.formQueryElementService.getCoreFeatures(SysmlFactory.eINSTANCE.createOwningMembership());
        assertThat(coreStructuralFeatures).containsOnly(SysmlPackage.eINSTANCE.getMembership_Visibility(),
                SysmlPackage.eINSTANCE.getMembership_MemberElement());
    }

    @Test
    public void getCoreFeaturesOfRequirementDefinition() {
        List<EStructuralFeature> coreStructuralFeatures = this.formQueryElementService.getCoreFeatures(SysmlFactory.eINSTANCE.createRequirementDefinition());
        assertThat(coreStructuralFeatures).containsOnly(SysmlPackage.eINSTANCE.getElement_DeclaredName(),
                SysmlPackage.eINSTANCE.getElement_QualifiedName(),
                SysmlPackage.eINSTANCE.getElement_DeclaredShortName(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_ReqId());
    }

    @Test
    public void getCoreFeaturesOfRequirementUsage() {
        List<EStructuralFeature> coreStructuralFeatures = this.formQueryElementService.getCoreFeatures(SysmlFactory.eINSTANCE.createRequirementUsage());
        assertThat(coreStructuralFeatures).containsOnly(SysmlPackage.eINSTANCE.getElement_DeclaredName(),
                SysmlPackage.eINSTANCE.getElement_QualifiedName(),
                SysmlPackage.eINSTANCE.getElement_DeclaredShortName(),
                SysmlPackage.eINSTANCE.getFeature_Direction(),
                SysmlPackage.eINSTANCE.getRequirementUsage_ReqId());
    }

    @Test
    public void isReadOnlyElementUsesInjectedPredicate() {
        FormQueryElementService readOnlyDetailsViewService = new FormQueryElementService(List.of(), element -> true, new MetamodelQueryElementService(), List.of());

        assertThat(readOnlyDetailsViewService.isReadOnly(SysmlFactory.eINSTANCE.createPartUsage())).isTrue();
    }

    @Test
    public void isReadOnlyLibraryPackageIsStandardEAttribute() {
        assertThat(this.formQueryElementService.isReadOnly(SysmlPackage.eINSTANCE.getLibraryPackage_IsStandard()));
    }
}
