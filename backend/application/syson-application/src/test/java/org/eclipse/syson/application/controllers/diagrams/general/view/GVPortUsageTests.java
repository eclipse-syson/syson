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
package org.eclipse.syson.application.controllers.diagrams.general.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * PortUsage related-tests in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVPortUsageTests extends AbstractIntegrationTests {

    private static final String PACKAGE = "Package";

    private static final String PACKAGE1 = "Package1";

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private ToolTester toolTester;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        return flux;
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a diagram with a Package, a SubPackage with a PartDefinition, WHEN a PortUsage is created from the PartDefinition, THEN the new PortUsage is visible as border node and hidden a sibling node of the PartDefinition node")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testCreatePortInSubPackage() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newPackageToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage()), "New Package");
        assertThat(newPackageToolId).as("The tool 'New Package' should exist on the Package").isNotNull();

        var newPartDefToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage()), "New Part Definition");
        assertThat(newPartDefToolId).as("The tool 'New Part Definition' should exist on the Package").isNotNull();

        var newPortUsageToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartDefinition()), "New Port");
        assertThat(newPortUsageToolId).as("The tool 'New Port' should exist on the PartDefinition").isNotNull();

        var diagramId = new AtomicReference<String>();
        var packageNodeId = new AtomicReference<String>();
        var subPackageNodeId = new AtomicReference<String>();
        var partDefNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            diagramId.set(diag.getId());
            var packageNode = new DiagramNavigator(diag).nodeWithLabel(PACKAGE).getNode();
            packageNodeId.set(packageNode.getId());
            assertThat(packageNode.getChildNodes()).hasSize(0);
            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(3);
        });

        Runnable newPackageTool = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), packageNodeId.get(), newPackageToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumerAfterNewPkg = assertRefreshedDiagramThat(diag -> {
            var subPackageNode = new DiagramNavigator(diag).nodeWithLabel(PACKAGE).childNodeWithLabel(PACKAGE1).getNode();
            assertThat(subPackageNode).isNotNull();
            subPackageNodeId.set(subPackageNode.getId());
        });

        Runnable newPartDefTool = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), subPackageNodeId.get(), newPartDefToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumerAfterNewPartDef = assertRefreshedDiagramThat(diag -> {
            var partDefNode = new DiagramNavigator(diag).nodeWithLabel(PACKAGE).childNodeWithLabel(PACKAGE1)
                    .childNodeWithLabel(LabelConstants.OPEN_QUOTE + "part def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "PartDefinition1").getNode();
            assertThat(partDefNode).isNotNull();
            assertThat(partDefNode.getBorderNodes()).hasSize(0);
            partDefNodeId.set(partDefNode.getId());
        });

        Runnable newPortUsageTool = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), partDefNodeId.get(), newPortUsageToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumerAfterNewPortUsage = assertRefreshedDiagramThat(diag -> {
            var pkgNode = new DiagramNavigator(diag).nodeWithLabel(PACKAGE).getNode();
            assertThat(pkgNode.getChildNodes()).hasSize(1);

            var partDefNode = new DiagramNavigator(diag).nodeWithLabel(PACKAGE).childNodeWithLabel(PACKAGE1)
                    .childNodeWithLabel(LabelConstants.OPEN_QUOTE + "part def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "PartDefinition1").getNode();
            assertThat(partDefNode).isNotNull();

            assertThat(partDefNode.getBorderNodes()).hasSize(1);
            assertThat(partDefNode.getBorderNodes().get(0).getState()).isEqualTo(ViewModifier.Normal);
            assertThat(partDefNode.getBorderNodes().get(0).getOutsideLabels().get(0).text()).isEqualTo("port1");

            var port1Node = new DiagramNavigator(diag).nodeWithLabel(PACKAGE).childNodeWithLabel(PACKAGE1)
                    .childNodeWithLabel(LabelConstants.OPEN_QUOTE + "port" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "port1").getNode();
            assertThat(port1Node).isNotNull();
            assertThat(port1Node.getState()).isEqualTo(ViewModifier.Hidden);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newPackageTool)
                .consumeNextWith(updatedDiagramContentConsumerAfterNewPkg)
                .then(newPartDefTool)
                .consumeNextWith(updatedDiagramContentConsumerAfterNewPartDef)
                .then(newPortUsageTool)
                .consumeNextWith(updatedDiagramContentConsumerAfterNewPortUsage)
                .thenCancel()
                .verify(Duration.ofSeconds(100));
    }
}
