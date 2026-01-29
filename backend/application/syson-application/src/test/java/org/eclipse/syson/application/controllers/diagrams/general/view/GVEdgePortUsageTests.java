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
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE_STYLE;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.ArrowStyle;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingContext.checkers.ISemanticChecker;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.data.GeneralViewPortTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.BindingConnector;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
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
 * Tests the creation of edges in the General View Diagram for ports.
 *
 * @author frouene
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVEdgePortUsageTests extends AbstractIntegrationTests {

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
    private EdgeCreationTester edgeCreationTester;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    private SemanticCheckerService semanticCheckerService;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), GeneralViewPortTestProjectData.EDITING_CONTEXT_ID, GeneralViewPortTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewPortTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewPortTestProjectData.SemanticIds.PACKAGE_1_ID);
    }

    @DisplayName("GIVEN a SysML Project with Ports, WHEN Flow edge tool creation is request between two ports, THEN a new Flow edge is created")
    @GivenSysONServer({ GeneralViewPortTestProjectData.SCRIPT_PATH })
    @Test
    public void givenSysMLProjectWithPortsWhenFlowUsageEdgeToolCreationIsRequestedThenNewFlowUsageEdgeIsCreated() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewPortTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String creationToolId = diagramDescriptionIdProvider
                .getEdgeCreationToolId(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort()), "New Flow (flow)");
        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewPortTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                GeneralViewPortTestProjectData.GraphicalIds.PORT_1_BORDER_NODE_ID,
                GeneralViewPortTestProjectData.GraphicalIds.PORT_2_BORDER_NODE_ID,
                creationToolId);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            List<Edge> newEdges = this.diagramComparator.newEdges(initialDiagram, newDiagram);
            assertThat(newEdges).hasSize(1).first(EDGE)
                    .hasSourceId(GeneralViewPortTestProjectData.GraphicalIds.PORT_1_BORDER_NODE_ID)
                    .hasTargetId(GeneralViewPortTestProjectData.GraphicalIds.PORT_2_BORDER_NODE_ID)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.InputFillClosedArrow);
        });

        ISemanticChecker semanticChecker = this.semanticCheckerService.getElementInParentSemanticChecker("Package 1", SysmlPackage.eINSTANCE.getNamespace_OwnedMember(),
                SysmlPackage.eINSTANCE.getFlowUsage());

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(semanticChecker);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

    }

    @DisplayName("GIVEN a SysML Project with ports, WHEN binding connector as usage edge tool creation is request between two ports, THEN a new binding connector edge is created")
    @GivenSysONServer({ GeneralViewPortTestProjectData.SCRIPT_PATH })
    @Test
    public void givenSysMLProjectWithPortsWhenBindingConnectorAsUsageEdgeToolCreationIsRequestedThenNewBindingConnectorEdgeIsCreated() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewPortTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(
                this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort()),
                "New Binding Connector As Usage (bind)");
        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewPortTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                GeneralViewPortTestProjectData.GraphicalIds.PORT_1_BORDER_NODE_ID,
                GeneralViewPortTestProjectData.GraphicalIds.PORT_2_BORDER_NODE_ID,
                creationToolId);

        List<String> bindingConnectorIds = new ArrayList<>();
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            List<Edge> newEdges = this.diagramComparator.newEdges(initialDiagram, newDiagram);
            assertThat(newEdges).hasSize(1).first(EDGE)
                    .hasSourceId(GeneralViewPortTestProjectData.GraphicalIds.PORT_1_BORDER_NODE_ID)
                    .hasTargetId(GeneralViewPortTestProjectData.GraphicalIds.PORT_2_BORDER_NODE_ID)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.None);
            bindingConnectorIds.addAll(newEdges.stream().map(Edge::getTargetObjectId).toList());
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(BindingConnector.class, () -> bindingConnectorIds.get(0), bindingConnector -> {
            assertThat(bindingConnector.getOwner().getName()).isEqualTo("Package 1");
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a SysML Project with ports, WHEN interface edge tool creation is request between two ports, THEN a new interface edge is created")
    @GivenSysONServer({ GeneralViewPortTestProjectData.SCRIPT_PATH })
    @Test
    public void givenSysMLProjectWithPortsWhenInterfaceEdgeToolCreationIsRequestedThenNewInterfaceEdgeIsCreated() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewPortTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(
                this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort()),
                "New Interface (connect)");
        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewPortTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                GeneralViewPortTestProjectData.GraphicalIds.PORT_1_BORDER_NODE_ID,
                GeneralViewPortTestProjectData.GraphicalIds.PORT_2_BORDER_NODE_ID,
                creationToolId);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            List<Edge> newEdges = this.diagramComparator.newEdges(initialDiagram, newDiagram);
            assertThat(newEdges).hasSize(1).first(EDGE)
                    .hasSourceId(GeneralViewPortTestProjectData.GraphicalIds.PORT_1_BORDER_NODE_ID)
                    .hasTargetId(GeneralViewPortTestProjectData.GraphicalIds.PORT_2_BORDER_NODE_ID)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.None);
        });

        ISemanticChecker semanticChecker = this.semanticCheckerService.getElementInParentSemanticChecker("Package 1", SysmlPackage.eINSTANCE.getNamespace_OwnedMember(),
                SysmlPackage.eINSTANCE.getInterfaceUsage());

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(semanticChecker);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

}
