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
package org.eclipse.syson.application.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.sirius.components.core.api.ChildCreationDescription;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.SimpleProjectElementsTestProjectData;
import org.eclipse.syson.application.services.SysMLv2EditService;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.validation.SysONSysMLValidationService;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Checks the integrity diagnostics of redefinitions created by the Explorer's edit service.
 *
 * @author adaussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedefinitionValidationIntegrationTests extends AbstractIntegrationTests {

    private static final String INTEGRITY_RULE = "validateRedefinitionRequiredFeatures";

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private SysMLv2EditService editService;

    @Autowired
    private SysONSysMLValidationService validationService;

    @Test
    @GivenSysONServer({ SimpleProjectElementsTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN a persisted part, WHEN creating a redefinition through the Explorer edit service, THEN missing endpoints are reported until the relation is completed")
    public void validateRedefinitionCreatedFromExplorer() {
        // GIVEN the persisted example contains Package1::p and Package2::PartDefX.
        // New Relationship > Redefinition creates a relationship without either endpoint.
        IEditingContext editingContext = this.editingContextSearchService.findById(SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID)
                .orElseThrow();
        PartUsage local = (PartUsage) this.objectSearchService.getObject(editingContext, SimpleProjectElementsTestProjectData.SemanticIds.PART_ID)
                .orElseThrow();
        PartDefinition base = (PartDefinition) this.objectSearchService.getObject(editingContext, SimpleProjectElementsTestProjectData.SemanticIds.PART_DEF_ID)
                .orElseThrow();
        assertThat(this.editService.getChildCreationDescriptions(editingContext, SimpleProjectElementsTestProjectData.SemanticIds.PART_ID, null))
                .anySatisfy((ChildCreationDescription description) -> assertThat(description.id())
                        .isEqualTo(SysMLv2EditService.ID_PREFIX + "Redefinition"));

        // WHEN the real application service creates the relationship, its null endpoints remain in the live model.
        Redefinition relation = (Redefinition) this.editService.createChild(editingContext, local, SysMLv2EditService.ID_PREFIX + "Redefinition")
                .orElseThrow();
        List<Diagnostic> incomplete = this.getIntegrityDiagnostics(editingContext);

        // THEN one error identifies the exact relationship instead of reporting its dependent semantic constraints.
        assertThat(relation.getRedefiningFeature())
                .isNull();
        assertThat(relation.getRedefinedFeature())
                .isNull();
        assertThat(incomplete)
                .singleElement()
                .satisfies((Diagnostic diagnostic) -> {
                    assertThat(diagnostic.getSeverity())
                            .isEqualTo(Diagnostic.ERROR);
                    assertThat(diagnostic.getData())
                            .isEqualTo(List.of(relation));
                });
        // Completing only the local endpoint keeps the missing-target error; both endpoints must be present.
        relation.setRedefiningFeature(local);
        assertThat(this.getIntegrityDiagnostics(editingContext))
                .hasSize(1);
        PartUsage source = (PartUsage) this.editService.createChild(editingContext, base, SysMLv2EditService.ID_PREFIX + "PartUsage")
                .orElseThrow();
        relation.setRedefinedFeature(source);
        assertThat(this.getIntegrityDiagnostics(editingContext))
                .isEmpty();
        // Other semantic rules remain applicable; this test only establishes endpoint integrity.
        assertThat(relation.getRedefiningFeature())
                .isSameAs(local);
        assertThat(relation.getRedefinedFeature())
                .isSameAs(source);
    }

    private List<Diagnostic> getIntegrityDiagnostics(IEditingContext editingContext) {
        return this.validationService.validate(editingContext).stream()
                .map(Diagnostic.class::cast)
                .filter(diagnostic -> INTEGRITY_RULE.equals(diagnostic.getSource()))
                .toList();
    }
}
