/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
import static org.eclipse.sirius.components.trees.tests.TreeEventPayloadConsumer.assertRefreshedTreeThat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.trees.api.TreeFilter;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.components.trees.description.TreeDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.application.views.explorer.ExplorerEventInput;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.explorer.ExplorerEventSubscriptionRunner;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.ViewAsOnNodeTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.tree.explorer.view.SysONTreeFilterProvider;
import org.eclipse.syson.tree.explorer.view.SysONTreeViewDescriptionProvider;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the ViewAs > XXXX tool on graphical nodes in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVViewAsOnNodeTests extends AbstractIntegrationTests {

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

    @Autowired
    private ExplorerEventSubscriptionRunner explorerEventSubscriptionRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @Autowired
    private SysONTreeViewDescriptionProvider sysonTreeViewDescriptionProvider;

    @Autowired
    private SysONTreeFilterProvider sysonTreeFilterProvider;

    @Autowired
    private IRepresentationDescriptionSearchService representationDescriptionSearchService;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                ViewAsOnNodeTestProjectData.EDITING_CONTEXT_ID,
                ViewAsOnNodeTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a GV diagram with 3 PartUsages (linked by composition, partA, partB, partC), WHEN the 'View as > Interconnection View' tool is applied on partB, THEN a new ViewUsage (typed with IV) is created and visible in the GV and contain partB and partC.")
    @GivenSysONServer({ ViewAsOnNodeTestProjectData.SCRIPT_PATH })
    @Test
    public void testViewAsIVOnPartUsage() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(ViewAsOnNodeTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var viewAsInterconnectionViewToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()),
                "Interconnection View");
        assertThat(viewAsInterconnectionViewToolId).as("The tool 'View as > Interconnection View' should exist on partB").isNotNull();

        var diagramId = new AtomicReference<String>();
        var partBNodeId = new AtomicReference<String>();
        var view2Id = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            diagramId.set(diag.getId());
            assertThat(diag.getNodes()).hasSize(3);
            assertThat(diag.getEdges()).hasSize(2);

            var partBNode = new DiagramNavigator(diag).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "pB").getNode();
            partBNodeId.set(partBNode.getId());
        });

        Runnable viewAsInterconnectionViewTool = () -> this.toolTester.invokeTool(ViewAsOnNodeTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), partBNodeId.get(), viewAsInterconnectionViewToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumerAfterToolExecution = assertRefreshedDiagramThat(diag -> {
            assertThat(diag.getNodes()).hasSize(2);
            assertThat(diag.getEdges()).hasSize(1);

            var view2NodeNavigator = new DiagramNavigator(diag)
                    .nodeWithLabel(LabelConstants.OPEN_QUOTE + "view" + LabelConstants.CLOSE_QUOTE + " view2 : StandardViewDefinitions::InterconnectionView");
            var newViewUsageNode = view2NodeNavigator.getNode();
            assertThat(newViewUsageNode).isNotNull();
            view2Id.set(newViewUsageNode.getTargetObjectId());

            var partBNodeNavigator = view2NodeNavigator.childNodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "pB");
            var partBNode = partBNodeNavigator.getNode();
            assertThat(partBNode).isNotNull();

            var partCNodeNavigator = partBNodeNavigator.childNodeWithLabel("interconnection")
                    .childNodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "pC");
            var partCNode = partCNodeNavigator.getNode();
            assertThat(partCNode).isNotNull();
        });

        List<String> expandedIds = new ArrayList<>();
        expandedIds.add(ViewAsOnNodeTestProjectData.DOCUMENT_ID);
        expandedIds.add(ViewAsOnNodeTestProjectData.SemanticIds.PACKAGE_1_ID);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(viewAsInterconnectionViewTool)
                .consumeNextWith(updatedDiagramContentConsumerAfterToolExecution)
                .then(() -> expandedIds.add(view2Id.get()))
                .thenCancel()
                .verify(Duration.ofSeconds(10));

        // the explorer view has a new ViewUsage with a diagram
        var sysONExplorerTreeDescriptionId = this.sysonTreeViewDescriptionProvider.getDescriptionId();
        var optionalEditingContext = this.editingContextSearchService.findById(ViewAsOnNodeTestProjectData.EDITING_CONTEXT_ID);
        TreeDescription treeDescription = optionalEditingContext.flatMap(editingContext -> this.representationDescriptionSearchService.findById(editingContext, sysONExplorerTreeDescriptionId))
                .filter(TreeDescription.class::isInstance).map(TreeDescription.class::cast).orElse(null);
        var defaultFilters = this.sysonTreeFilterProvider.get(null, treeDescription).stream()
                .filter(TreeFilter::defaultState).map(TreeFilter::id).toList();

        String explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(sysONExplorerTreeDescriptionId, expandedIds, defaultFilters);
        var input = new ExplorerEventInput(UUID.randomUUID(), ViewAsOnNodeTestProjectData.EDITING_CONTEXT_ID, explorerRepresentationId);
        var explorerFlux = this.explorerEventSubscriptionRunner.run(input).flux();
        TestTransaction.flagForCommit();
        TestTransaction.end();

        var initialExplorerContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            TreeItem sysmlv2Model = tree.getChildren().get(0);
            assertThat(sysmlv2Model.getLabel().toString()).isEqualTo("SysMLv2.sysml");
            assertThat(sysmlv2Model.getChildren()).hasSize(1);
            TreeItem pkg1 = sysmlv2Model.getChildren().get(0);
            assertThat(pkg1.getLabel().toString()).isEqualTo("Package1");

            assertThat(pkg1.getChildren()).hasSize(3);
            TreeItem view1 = pkg1.getChildren().get(0);
            assertThat(view1.getLabel().toString()).isEqualTo("view1 [GeneralView]");
            TreeItem gv = pkg1.getChildren().get(1);
            assertThat(gv.getLabel().toString()).isEqualTo("pA");
            TreeItem view2 = pkg1.getChildren().get(2);
            assertThat(view2.getLabel().toString()).isEqualTo("view2 [InterconnectionView]");
            assertThat(view2.getChildren()).hasSize(2);
            TreeItem diagramView2 = view2.getChildren().get(0);
            assertThat(diagramView2.getLabel().toString()).isEqualTo("view2");
        });

        StepVerifier.create(explorerFlux)
                .consumeNextWith(initialExplorerContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram, WHEN the 'View as > Interconnection View' tool is applied on a Package, THEN a new ViewUsage (typed with IV) is created and visible in the GV and contain the Package.")
    @GivenSysONServer({ ViewAsOnNodeTestProjectData.SCRIPT_PATH })
    @Test
    public void testViewAsIVOnPackage() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(ViewAsOnNodeTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var packageToolId = diagramDescriptionIdProvider.getDiagramCreationToolId(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getPackage()));
        assertThat(packageToolId).as("The tool 'New Package' should exist on diagram").isNotNull();

        var viewAsGeneralViewToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage()), "General View");
        assertThat(viewAsGeneralViewToolId).as("The tool 'View as > General View' should exist on Package").isNotNull();

        var diagramId = new AtomicReference<String>();
        var packageNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            diagramId.set(diag.getId());
            assertThat(diag.getNodes()).hasSize(3);
            assertThat(diag.getEdges()).hasSize(2);
        });

        Runnable packageTool = () -> this.toolTester.invokeTool(ViewAsOnNodeTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), diagramId.get(), packageToolId, List.of());

        Consumer<Object> updatedDiagramContentConsumerAfterPackageToolExecution = assertRefreshedDiagramThat(diag -> {
            assertThat(diag.getNodes()).hasSize(4);
            assertThat(diag.getEdges()).hasSize(2);

            var packageNode = new DiagramNavigator(diag).nodeWithLabel("Package1").getNode();
            assertThat(packageNode).isNotNull();
            packageNodeId.set(packageNode.getId());
        });

        Runnable viewAsGeneralViewTool = () -> this.toolTester.invokeTool(ViewAsOnNodeTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), packageNodeId.get(), viewAsGeneralViewToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumerAfterViewAsToolExecution = assertRefreshedDiagramThat(diag -> {
            assertThat(diag.getNodes()).hasSize(4);
            assertThat(diag.getEdges()).hasSize(2);

            var view2NodeNavigator = new DiagramNavigator(diag)
                    .nodeWithLabel(LabelConstants.OPEN_QUOTE + "view" + LabelConstants.CLOSE_QUOTE + " view2 : StandardViewDefinitions::GeneralView");
            var newViewUsageNode = view2NodeNavigator.getNode();
            assertThat(newViewUsageNode).isNotNull();

            var packageNode = view2NodeNavigator.childNodeWithLabel("Package1").getNode();
            assertThat(packageNode).isNotNull();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(packageTool)
                .consumeNextWith(updatedDiagramContentConsumerAfterPackageToolExecution)
                .then(viewAsGeneralViewTool)
                .consumeNextWith(updatedDiagramContentConsumerAfterViewAsToolExecution)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
