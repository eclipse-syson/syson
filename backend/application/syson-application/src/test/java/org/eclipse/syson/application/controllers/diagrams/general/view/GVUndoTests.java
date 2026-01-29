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

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DeleteFromDiagramInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DeleteFromDiagramSuccessPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.application.undo.dto.UndoInput;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.undoredo.UndoMutationRunner;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.testers.DeleteToolRunner;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the undo of graphical nodes in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVUndoTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private DeleteToolRunner deleteFromDiagramRunner;

    @Autowired
    private UndoMutationRunner undoMutationRunner;

    @Autowired
    private ToolTester toolTester;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToEmptyDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                GeneralViewEmptyTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a diagram with a node, WHEN a the node is deleted, THEN the undo restore the node")
    @GivenSysONServer({ GeneralViewEmptyTestProjectData.SCRIPT_PATH })
    @Test
    public void testUndoAfterDelete() {
        var flux = this.givenSubscriptionToEmptyDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newPartToolId = diagramDescriptionIdProvider.getDiagramCreationToolId("New Part");
        assertThat(newPartToolId).as("The 'New Part' tool should exist on the diagram").isNotNull();

        var newInterfaceToolId = diagramDescriptionIdProvider.getDiagramCreationToolId("New Interface");
        assertThat(newInterfaceToolId).as("The 'New Interface' tool should exist on the digram").isNotNull();

        var diagram = new AtomicReference<Diagram>();
        var partNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagram = assertRefreshedDiagramThat(diagram::set);

        Runnable newPartUsageTool = () -> this.toolTester.invokeTool(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, diagram, newPartToolId);

        Consumer<Object> updatedDiagramAfterNewPart = assertRefreshedDiagramThat(diag -> {
            var partNode = new DiagramNavigator(diag).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "part1").getNode();
            partNodeId.set(partNode.getId());
        });

        var deletePartInputId = UUID.randomUUID();

        Runnable invokeDeletePartUsageTool = () -> {
            var input = new DeleteFromDiagramInput(deletePartInputId, GeneralViewEmptyTestProjectData.EDITING_CONTEXT, diagram.get().getId(), List.of(partNodeId.get()), List.of());
            var result = this.deleteFromDiagramRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.deleteFromDiagram.__typename");
            assertThat(typename).isEqualTo(DeleteFromDiagramSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramAfterPartDeletion = assertRefreshedDiagramThat(diag -> {
            var nodeCount = new DiagramNavigator(diag).findDiagramNodeCount();
            assertThat(nodeCount).isEqualTo(1); // empty node to help users to start modeling
            Node node = new DiagramNavigator(diag).nodeWithTargetObjectId(GeneralViewEmptyTestProjectData.SemanticIds.VIEW_USAGE_ID).getNode();
            assertThat(node).isNotNull();
        });

        Runnable invokeUndoDeletePartUsage = () -> {
            var input = new UndoInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, deletePartInputId);
            var result = this.undoMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.undo.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramAfterUndoPartDeletion = assertRefreshedDiagramThat(diag -> {
            var rootNodesCount = diag.getNodes().size();
            assertThat(rootNodesCount).isEqualTo(1);
            Node node = new DiagramNavigator(diag).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "part1").getNode();
            assertThat(node).isNotNull();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagram)
                .then(newPartUsageTool)
                .consumeNextWith(updatedDiagramAfterNewPart)
                .then(invokeDeletePartUsageTool)
                .consumeNextWith(updatedDiagramAfterPartDeletion)
                .then(invokeUndoDeletePartUsage)
                .consumeNextWith(updatedDiagramAfterUndoPartDeletion)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
