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

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.HideDiagramElementInput;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.tests.graphql.HideDiagramElementMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.PaletteQueryRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GVSimpleNestedAndTreeElementsTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.tests.api.GivenSysONServer;
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
 * Tests the group palette tools in the General View diagram.
 *
 * @author tgiraudet
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVGroupPaletteTests extends AbstractIntegrationTests {

    private static final String PART_A_ID = GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PART_A_ID;

    private static final String PARTDEF_A_ID = GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PARTDEF_A_ID;

    private static final String ACTION_A_ID = GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID;

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
    private PaletteQueryRunner paletteQueryRunner;

    @Autowired
    private HideDiagramElementMutationRunner hideDiagramElementMutationRunner;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a GV diagram, WHEN the group palette is requested on a multi-selection of nodes with content, THEN it contains a 'Show/Hide' section with all the show/hide tools")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void groupPaletteContainsShowHideSection() {
        var flux = this.givenSubscriptionToDiagram();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> assertThat(this.compartmentsOf(diagram, PART_A_ID)).isNotEmpty());

        Runnable getGroupPalette = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                    "representationId", GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID,
                    "diagramElementIds", List.of(PART_A_ID, PARTDEF_A_ID));
            var result = this.paletteQueryRunner.run(variables);
            var toolLabels = JsonPath.<List<String>>read(result.data(), "$.data.viewer.editingContext.representation.description.palette.paletteEntries[?(@.label == 'Show/Hide')].tools[*].label");
            assertThat(toolLabels).containsExactly("Hide", "Hide all content", "Show all content", "Reset content", "Show valued content");
        };

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(getGroupPalette)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram with several visible nodes, WHEN the group 'Delete from diagram' tool is applied on a multi-selection, THEN all the selected nodes are removed")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void invokeDeleteFromDiagramToolOnMultiSelection() {
        var flux = this.givenSubscriptionToDiagram();
        var deleteFromDiagramToolId = this.getGroupToolId("Delete from diagram");

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> assertThat(diagram.getNodes())
                .extracting(Node::getId)
                .contains(PART_A_ID, PARTDEF_A_ID));

        Runnable deleteFromDiagramTool = () -> this.invokeGroupTool(deleteFromDiagramToolId, List.of(PART_A_ID, PARTDEF_A_ID));

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> assertThat(diagram.getNodes())
                .extracting(Node::getId)
                .doesNotContain(PART_A_ID, PARTDEF_A_ID));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(deleteFromDiagramTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram with several visible nodes, WHEN the group 'Hide' tool is applied on a multi-selection, THEN all the selected nodes are hidden")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void invokeHideToolOnMultiSelection() {
        var flux = this.givenSubscriptionToDiagram();
        var hideToolId = this.getGroupToolId("Hide");

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            assertThat(this.nodeWithId(diagram, PART_A_ID).getState()).isEqualTo(ViewModifier.Normal);
            assertThat(this.nodeWithId(diagram, PARTDEF_A_ID).getState()).isEqualTo(ViewModifier.Normal);
        });

        Runnable hideTool = () -> this.invokeGroupTool(hideToolId, List.of(PART_A_ID, PARTDEF_A_ID));

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            assertThat(this.nodeWithId(diagram, PART_A_ID).getState()).isEqualTo(ViewModifier.Hidden);
            assertThat(this.nodeWithId(diagram, PARTDEF_A_ID).getState()).isEqualTo(ViewModifier.Hidden);
            assertThat(this.nodeWithId(diagram, ACTION_A_ID).getState())
                    .as("The nodes that are not part of the selection should not be impacted")
                    .isEqualTo(ViewModifier.Normal);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(hideTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram with several nodes with a hidden content, WHEN the group 'Show all content' tool is applied on a multi-selection, THEN the content of all the selected nodes is revealed")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void invokeShowAllContentToolOnMultiSelection() {
        var flux = this.givenSubscriptionToDiagram();
        var showAllContentToolId = this.getGroupToolId("Show all content");

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            assertThat(this.compartmentsOf(diagram, PART_A_ID)).isNotEmpty().allMatch(this::isHidden);
            assertThat(this.compartmentsOf(diagram, PARTDEF_A_ID)).isNotEmpty().allMatch(this::isHidden);
        });

        Runnable showAllContentTool = () -> this.invokeGroupTool(showAllContentToolId, List.of(PART_A_ID, PARTDEF_A_ID));

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            assertThat(this.compartmentsOf(diagram, PART_A_ID)).isNotEmpty().noneMatch(this::isHidden);
            assertThat(this.compartmentsOf(diagram, PARTDEF_A_ID)).isNotEmpty().noneMatch(this::isHidden);
            assertThat(this.compartmentsOf(diagram, ACTION_A_ID))
                    .as("The content of the nodes that are not part of the selection should not be impacted")
                    .isNotEmpty()
                    .allMatch(this::isHidden);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(showAllContentTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram with several nodes with a visible content, WHEN the group 'Hide all content' tool is applied on a multi-selection, THEN the content of all the selected nodes is hidden")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void invokeHideAllContentToolOnMultiSelection() {
        var flux = this.givenSubscriptionToDiagram();
        var hideAllContentToolId = this.getGroupToolId("Hide all content");

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            var payloadTypename = this.revealCompartments(diagram, PART_A_ID, PARTDEF_A_ID);
            assertThat(payloadTypename).isEqualTo(SuccessPayload.class.getSimpleName());
        });

        Consumer<Object> diagramContentConsumerAfterReveal = assertRefreshedDiagramThat(diagram -> {
            assertThat(this.compartmentsOf(diagram, PART_A_ID)).isNotEmpty().noneMatch(this::isHidden);
            assertThat(this.compartmentsOf(diagram, PARTDEF_A_ID)).isNotEmpty().noneMatch(this::isHidden);
        });

        Runnable hideAllContentTool = () -> this.invokeGroupTool(hideAllContentToolId, List.of(PART_A_ID, PARTDEF_A_ID));

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            assertThat(this.compartmentsOf(diagram, PART_A_ID)).isNotEmpty().allMatch(this::isHidden);
            assertThat(this.compartmentsOf(diagram, PARTDEF_A_ID)).isNotEmpty().allMatch(this::isHidden);
            assertThat(this.nodeWithId(diagram, PART_A_ID).getState())
                    .as("The selected nodes themselves should remain visible")
                    .isEqualTo(ViewModifier.Normal);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .consumeNextWith(diagramContentConsumerAfterReveal)
                .then(hideAllContentTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram with several nodes with a visible content, WHEN the group 'Reset content' tool is applied on a multi-selection, THEN the content of all the selected nodes recovers its default visibility")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void invokeResetContentToolOnMultiSelection() {
        var flux = this.givenSubscriptionToDiagram();
        var resetContentToolId = this.getGroupToolId("Reset content");

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            var payloadTypename = this.revealCompartments(diagram, PART_A_ID, PARTDEF_A_ID);
            assertThat(payloadTypename).isEqualTo(SuccessPayload.class.getSimpleName());
        });

        Consumer<Object> diagramContentConsumerAfterReveal = assertRefreshedDiagramThat(diagram -> {
            assertThat(this.compartmentsOf(diagram, PART_A_ID)).isNotEmpty().noneMatch(this::isHidden);
            assertThat(this.compartmentsOf(diagram, PARTDEF_A_ID)).isNotEmpty().noneMatch(this::isHidden);
        });

        Runnable resetContentTool = () -> this.invokeGroupTool(resetContentToolId, List.of(PART_A_ID, PARTDEF_A_ID));

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            assertThat(this.compartmentsOf(diagram, PART_A_ID))
                    .as("Compartments are hidden by default, they should be hidden again once their visibility modifiers are reset")
                    .isNotEmpty()
                    .allMatch(this::isHidden);
            assertThat(this.compartmentsOf(diagram, PARTDEF_A_ID)).isNotEmpty().allMatch(this::isHidden);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .consumeNextWith(diagramContentConsumerAfterReveal)
                .then(resetContentTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram with several nodes with a hidden content, WHEN the group 'Show valued content' tool is applied on a multi-selection, THEN only the content that is not empty is revealed")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void invokeShowValuedContentToolOnMultiSelection() {
        var flux = this.givenSubscriptionToDiagram();
        var showValuedContentToolId = this.getGroupToolId("Show valued content");

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            assertThat(this.compartmentsOf(diagram, ACTION_A_ID)).isNotEmpty().allMatch(this::isHidden);
            assertThat(this.compartmentsOf(diagram, PART_A_ID)).isNotEmpty().allMatch(this::isHidden);
        });

        Runnable showValuedContentTool = () -> this.invokeGroupTool(showValuedContentToolId, List.of(ACTION_A_ID, PART_A_ID));

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            assertThat(this.compartmentsOf(diagram, ACTION_A_ID))
                    .filteredOn(compartment -> !this.isHidden(compartment))
                    .extracting(compartment -> compartment.getInsideLabel().getText())
                    .as("Only the compartments of actionA containing something should be revealed")
                    .containsExactlyInAnyOrder("actions", "action flow");
            assertThat(this.compartmentsOf(diagram, PART_A_ID))
                    .as("partA has no content, none of its compartments should be revealed")
                    .isNotEmpty()
                    .allMatch(this::isHidden);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(showValuedContentTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private String getGroupToolId(String toolName) {
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        return new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider).getGroupNodeToolId(toolName);
    }

    private void invokeGroupTool(String toolId, List<String> diagramElementIds) {
        this.toolTester.invokeTool(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID, GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID, diagramElementIds, toolId,
                List.of());
    }

    /**
     * Reveals the compartments of the given nodes, and returns the type name of the payload of the mutation.
     */
    private String revealCompartments(Diagram diagram, String... nodeIds) {
        Set<String> compartmentIds = Stream.of(nodeIds)
                .flatMap(nodeId -> this.compartmentsOf(diagram, nodeId).stream())
                .map(Node::getId)
                .collect(Collectors.toSet());
        var input = new HideDiagramElementInput(UUID.randomUUID(), GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID, compartmentIds, false);
        var result = this.hideDiagramElementMutationRunner.run(input);
        return JsonPath.read(result.data(), "$.data.hideDiagramElement.__typename");
    }

    private Node nodeWithId(Diagram diagram, String nodeId) {
        return new DiagramNavigator(diagram).nodeWithId(nodeId).getNode();
    }

    private List<Node> compartmentsOf(Diagram diagram, String nodeId) {
        return this.nodeWithId(diagram, nodeId).getChildNodes();
    }

    private boolean isHidden(Node node) {
        return node.getModifiers().contains(ViewModifier.Hidden);
    }
}
