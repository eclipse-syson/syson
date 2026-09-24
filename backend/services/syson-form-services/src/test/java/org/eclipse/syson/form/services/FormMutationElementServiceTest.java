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

import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.sysml.AnalysisCaseDefinition;
import org.eclipse.syson.sysml.AnalysisCaseUsage;
import org.eclipse.syson.sysml.CalculationDefinition;
import org.eclipse.syson.sysml.CalculationUsage;
import org.eclipse.syson.sysml.ConcernDefinition;
import org.eclipse.syson.sysml.ConcernUsage;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link FormMutationElementServiceTest}.
 *
 * @author gdaniel
 */
public class FormMutationElementServiceTest {

    private FormMutationElementService formMutationElementService;

    @BeforeEach
    public void setUp() {
        MetamodelQueryElementService metamodelQueryElementService = new MetamodelQueryElementService();
        FormQueryElementService formQueryElementService = new FormQueryElementService(List.of(), element -> false, metamodelQueryElementService, List.of());
        this.formMutationElementService = new FormMutationElementService(new IFeedbackMessageService.NoOp(), metamodelQueryElementService, formQueryElementService);
    }

    @Test
    public void setPartUsageTypedByPartDefinition() {
        PartUsage partUsage = SysmlFactory.eINSTANCE.createPartUsage();
        PartDefinition partDefinition = SysmlFactory.eINSTANCE.createPartDefinition();

        this.formMutationElementService.handleFeatureTypingNewValue(partUsage, partDefinition);

        assertThat(partUsage.getPartDefinition()).containsExactly(partDefinition);
    }

    @Test
    public void setAnalysisCaseUsageTypedByAnalysisCaseDefinition() {
        AnalysisCaseUsage analysisCaseUsage = SysmlFactory.eINSTANCE.createAnalysisCaseUsage();
        AnalysisCaseDefinition analysisCaseDefinition = SysmlFactory.eINSTANCE.createAnalysisCaseDefinition();

        this.formMutationElementService.handleFeatureTypingNewValue(analysisCaseUsage, analysisCaseDefinition);

        assertThat(analysisCaseUsage.getAnalysisCaseDefinition()).isSameAs(analysisCaseDefinition);
        assertThat(analysisCaseUsage.eIsSet(SysmlPackage.eINSTANCE.getAnalysisCaseUsage_AnalysisCaseDefinition())).isTrue();
    }

    @Test
    public void setCalculationUsageTypedByCalculationDefinition() {
        CalculationUsage calculationUsage = SysmlFactory.eINSTANCE.createCalculationUsage();
        CalculationDefinition calculationDefinition = SysmlFactory.eINSTANCE.createCalculationDefinition();

        this.formMutationElementService.handleFeatureTypingNewValue(calculationUsage, calculationDefinition);

        assertThat(calculationUsage.getCalculationDefinition()).isSameAs(calculationDefinition);
        assertThat(calculationUsage.eIsSet(SysmlPackage.eINSTANCE.getCalculationUsage_CalculationDefinition())).isTrue();
    }

    @Test
    public void setConcernUsageTypedByConcernDefinition() {
        ConcernUsage concernUsage = SysmlFactory.eINSTANCE.createConcernUsage();
        ConcernDefinition concernDefinition = SysmlFactory.eINSTANCE.createConcernDefinition();

        this.formMutationElementService.handleFeatureTypingNewValue(concernUsage, concernDefinition);

        assertThat(concernUsage.getConcernDefinition()).isSameAs(concernDefinition);
        assertThat(concernUsage.eIsSet(SysmlPackage.eINSTANCE.getConcernUsage_ConcernDefinition())).isTrue();
    }

    @Test
    public void setRenderingUsageTypedByRenderingDefinition() {
        RenderingUsage renderingUsage = SysmlFactory.eINSTANCE.createRenderingUsage();
        RenderingDefinition renderingDefinition = SysmlFactory.eINSTANCE.createRenderingDefinition();

        this.formMutationElementService.handleFeatureTypingNewValue(renderingUsage, renderingDefinition);

        assertThat(renderingUsage.getRenderingDefinition()).isSameAs(renderingDefinition);
        assertThat(renderingUsage.eIsSet(SysmlPackage.eINSTANCE.getRenderingUsage_RenderingDefinition())).isTrue();
    }

    @Test
    public void setVerificationCaseUsageTypedByVerificationCaseDefinition() {
        VerificationCaseUsage verificationCaseUsage = SysmlFactory.eINSTANCE.createVerificationCaseUsage();
        VerificationCaseDefinition verificationCaseDefinition = SysmlFactory.eINSTANCE.createVerificationCaseDefinition();

        this.formMutationElementService.handleFeatureTypingNewValue(verificationCaseUsage, verificationCaseDefinition);

        assertThat(verificationCaseUsage.getVerificationCaseDefinition()).isSameAs(verificationCaseDefinition);
        assertThat(verificationCaseUsage.eIsSet(SysmlPackage.eINSTANCE.getVerificationCaseUsage_VerificationCaseDefinition())).isTrue();
    }

    @Test
    public void setViewpointUsageTypedByViewpointDefinition() {
        ViewpointUsage viewpointUsage = SysmlFactory.eINSTANCE.createViewpointUsage();
        ViewpointDefinition viewpointDefinition = SysmlFactory.eINSTANCE.createViewpointDefinition();

        this.formMutationElementService.handleFeatureTypingNewValue(viewpointUsage, viewpointDefinition);

        assertThat(viewpointUsage.getViewpointDefinition()).isSameAs(viewpointDefinition);
        assertThat(viewpointUsage.eIsSet(SysmlPackage.eINSTANCE.getViewpointUsage_ViewpointDefinition())).isTrue();
    }

    @Test
    public void rejectPartUsageAsPartUsageTypedByValue() {
        PartUsage partUsage = SysmlFactory.eINSTANCE.createPartUsage();
        PartUsage droppedPartUsage = SysmlFactory.eINSTANCE.createPartUsage();

        this.formMutationElementService.handleFeatureTypingNewValue(partUsage, droppedPartUsage);

        assertThat(partUsage.getOwnedRelationship()).isEmpty();
    }
}
