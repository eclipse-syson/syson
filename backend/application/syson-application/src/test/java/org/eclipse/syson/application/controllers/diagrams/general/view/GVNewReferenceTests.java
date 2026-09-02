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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariableType;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.graphql.tests.api.IExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the New Reference tools.
 *
 * @author Jerome Gout
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVNewReferenceTests extends AbstractIntegrationTests {

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private ToolTester nodeCreationTester;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private IExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @Autowired
    private IObjectSearchService objectSearchService;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a SysML Project with a PartUsage, WHEN invoking New Reference on diagram, THEN a new reference to the PartUsage can be done")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void checkDiagramNewReference() {
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        String creationToolId = diagramDescriptionIdProvider.getDiagramCreationToolId("New Reference");
        AtomicReference<Diagram> diagram = new AtomicReference<>();

        var flux = this.givenSubscriptionToDiagram();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        List<ToolVariable> emptySelectionToolVariables = new ArrayList<>();
        emptySelectionToolVariables.add(new ToolVariable("selectedObject", "", ToolVariableType.OBJECT_ID));
        Runnable invokeCreationToolWithNoSelection = () -> this.nodeCreationTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, null, creationToolId, emptySelectionToolVariables);
        Consumer<Object> diagramCheckWithNoSelection = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .check(diagram.get(), newDiagram);
            var newNodes = this.diagramComparator.newNodes(diagram.get(), newDiagram);
            assertThat(newNodes.getFirst().getInsideLabel().getText()).endsWith("reference1");
            diagram.set(newDiagram);
        });

        List<ToolVariable> selectionToolVariables = new ArrayList<>();
        selectionToolVariables.add(new ToolVariable("selectedObject", GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID, ToolVariableType.OBJECT_ID));
        Runnable invokeCreationToolWithSelection = () -> this.nodeCreationTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, null, creationToolId, selectionToolVariables);
        Consumer<Object> diagramCheckWithSelection = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .check(diagram.get(), newDiagram);
            var newNodes = this.diagramComparator.newNodes(diagram.get(), newDiagram);
            assertThat(newNodes.getFirst().getInsideLabel().getText()).endsWith("reference2 ::> part");
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(invokeCreationToolWithNoSelection)
                .consumeNextWith(diagramCheckWithNoSelection)
                .then(invokeCreationToolWithSelection)
                .consumeNextWith(diagramCheckWithSelection)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }


    private static Stream<Arguments> nestedReferencesCompartmentParameters() {
        return Stream.of(
                Arguments.of(Named.of("AttributeUsage", SysmlPackage.eINSTANCE.getAttributeUsage()), GeneralViewWithTopNodesTestProjectData.GraphicalIds.ATTRIBUTE_USAGE_ID),
                Arguments.of(Named.of("ItemUsage", SysmlPackage.eINSTANCE.getItemUsage()), GeneralViewWithTopNodesTestProjectData.GraphicalIds.ITEM_USAGE_ID),
                Arguments.of(Named.of("MetadataDefinition", SysmlPackage.eINSTANCE.getMetadataDefinition()), GeneralViewWithTopNodesTestProjectData.GraphicalIds.METADATA_DEFINITION_ID),
                Arguments.of(Named.of("PortUsage", SysmlPackage.eINSTANCE.getPortUsage()), GeneralViewWithTopNodesTestProjectData.GraphicalIds.PORT_USAGE_ID),
                Arguments.of(Named.of("PortDefinition", SysmlPackage.eINSTANCE.getPortDefinition()), GeneralViewWithTopNodesTestProjectData.GraphicalIds.PORT_DEFINITION_ID)
        );
    }
    @DisplayName("GIVEN a SysML Project with a PartUsage, WHEN invoking New Reference on $owner, THEN a new nested reference to the PartUsage can be done")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("nestedReferencesCompartmentParameters")
    public void checkNestedNewReference(EClass owner, String ownerNodeId) {
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        String creationToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(owner), "New Reference");
        AtomicReference<Diagram> diagram = new AtomicReference<>();

        var flux = this.givenSubscriptionToDiagram();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        List<ToolVariable> selectionToolVariables = new ArrayList<>();
        selectionToolVariables.add(new ToolVariable("selectedObject", GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID, ToolVariableType.OBJECT_ID));
        Runnable invokeCreationToolWithSelection = () -> this.nodeCreationTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram.get().getId(), ownerNodeId, creationToolId, selectionToolVariables);
        Consumer<Object> nodeCheckWithSelection = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .check(diagram.get(), newDiagram);
            var newNodes = this.diagramComparator.newNodes(diagram.get(), newDiagram);
            assertThat(newNodes.getFirst().getInsideLabel().getText()).endsWith("reference1 ::> part");
            var referencesCompartment = new DiagramNavigator(newDiagram)
                    .nodeWithId(ownerNodeId)
                    .childNodeWithLabel("references")
                    .getNode();
            assertThat(referencesCompartment.getChildNodes()).hasSize(1);
            assertThat(referencesCompartment.getChildNodes().getFirst().getInsideLabel().getText()).endsWith("reference1 ::> part");
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(invokeCreationToolWithSelection)
                .consumeNextWith(nodeCheckWithSelection)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
