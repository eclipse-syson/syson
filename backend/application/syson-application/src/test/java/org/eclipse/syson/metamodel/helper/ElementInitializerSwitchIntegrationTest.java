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
package org.eclipse.syson.metamodel.helper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.UUID;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.editingContext.checkers.ISemanticChecker;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.metamodel.services.ElementInitializerSwitch;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Integration test for {@link org.eclipse.syson.sysml.metamodel.services.ElementInitializerSwitch}. Mostly used to test element initialization in a context where the standard libraries are required.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ElementInitializerSwitchIntegrationTest extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    private StepVerifier.Step<DiagramRefreshedEventPayload> verifier;

    private SemanticCheckerService semanticCheckerService;

    private ElementInitializerSwitch elementInitializerSwitch;

    private ElementUtil elementUtil;

    @BeforeEach
    public void setUp() {
        this.elementInitializerSwitch = new ElementInitializerSwitch();
        this.elementUtil = new ElementUtil();
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @DisplayName("GIVEN a ViewUsage, WHEN it is initialized, THEN it's typed by default with the GeneralView ViewDefinition")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testViewUsageDefaultType() {
        ISemanticChecker semanticChecker = (editingContext) -> {
            Package semanticRootObject = (Package) this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID).orElseThrow();
            var view1 = SysmlFactory.eINSTANCE.createViewUsage();
            new MetamodelMutationElementService().addChildInParent(semanticRootObject, view1);
            var initializedView1 = this.elementInitializerSwitch.doSwitch(view1);
            var generalViewViewDef = this.elementUtil.findByNameAndType(view1, "StandardViewDefinitions::GeneralView", ViewDefinition.class);
            assertThat(((ViewUsage) initializedView1).getType()).contains(generalViewViewDef);
        };
        this.semanticCheckerService.checkEditingContext(semanticChecker, this.verifier);
    }

}
