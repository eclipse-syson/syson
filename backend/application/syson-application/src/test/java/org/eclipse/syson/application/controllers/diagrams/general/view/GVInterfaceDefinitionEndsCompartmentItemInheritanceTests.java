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

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the inheritance of PortUsage as end inside ends compartment of InterfaceDefinition.
 *
 * @author Jerome Gout
 */
@Transactional
@GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVInterfaceDefinitionEndsCompartmentItemInheritanceTests extends AbstractIntegrationTests {

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
    private ToolTester toolTester;

    @Autowired
    private EdgeCreationTester edgeCreationTester;

    @Autowired
    private DiagramComparator diagramComparator;

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

    @DisplayName("GIVEN a base InterfaceDefinition with a port as end, WHEN a InterfaceDefinition is subclassifying the base InterfaceDefinition, THEN the InterfaceDefinition ends are inherited from the base InterfaceDefinition")
    @Test
    public void checkActionDefinitionParameterFilter() {
        var flux = this.givenSubscriptionToDiagram();
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        List<ToolVariable> toolVariables = new ArrayList<>();
        // Create an element in the base element to inherit
        String createPortAsEndToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getInterfaceDefinition()), "New Port as end");
        Runnable newCreationTool = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram.get().getId(), GeneralViewWithTopNodesTestProjectData.GraphicalIds.INTERFACE_DEFINITION_ID, createPortAsEndToolId, toolVariables);
        Consumer<Object> createdPortAsEndDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewBorderNodeCount(1) // The port is displayed as a border node
                    .hasNewNodeCount(3) // The new port is displayed in both ends and ports compartments + as border node.
                    .hasNewEdgeCount(0)
                    .check(diagram.get(), newDiagram);

            var portsCompartment = new DiagramNavigator(newDiagram)
                    .nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.INTERFACE_DEFINITION_ID)
                    .childNodeWithLabel("ports")
                    .getNode();
            assertThat(portsCompartment.getChildNodes()).hasSize(1);
            assertThat(portsCompartment.getChildNodes().getFirst().getInsideLabel().getText()).isEqualTo("port1"); // The created element has the expected name
            var endsCompartment = new DiagramNavigator(newDiagram)
                    .nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.INTERFACE_DEFINITION_ID)
                    .childNodeWithLabel("ends")
                    .getNode();
            assertThat(endsCompartment.getChildNodes()).hasSize(1);
            assertThat(endsCompartment.getChildNodes().getFirst().getInsideLabel().getText()).isEqualTo("port1"); // The created element has the expected name
            diagram.set(newDiagram);
        });


        // Create a new element that will inherit from the base element
        AtomicReference<String> newInterfaceDefinitionNodeId = new AtomicReference<>();
        String createInterfaceDefinitionToolId = diagramDescriptionIdProvider.getDiagramCreationToolId("New Interface Definition");
        Runnable createInterfaceDefinitionRunnable = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, null, createInterfaceDefinitionToolId);
        Consumer<Object> createdActionDefinitionDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewBorderNodeCount(2) // source and target as border nodes
                    .hasNewNodeCount(3) // The new InterfaceDefinition is created + source and target nodes
                    .hasNewEdgeCount(0)
                    .check(diagram.get(), newDiagram, true);
            var newNodes = this.diagramComparator.newNodes(diagram.get(), newDiagram);
            newInterfaceDefinitionNodeId.set(newNodes.getFirst().getId());
            diagram.set(newDiagram);
        });


        // Create the specialization between the newly created element and the base element
        String createFeatureTypeToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(GVInterfaceDefinitionEndsCompartmentItemInheritanceTests.this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getInterfaceDefinition()), "New Subclassification");
        Runnable createFeatureTyping = () -> GVInterfaceDefinitionEndsCompartmentItemInheritanceTests.this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, newInterfaceDefinitionNodeId.get(), GeneralViewWithTopNodesTestProjectData.GraphicalIds.INTERFACE_DEFINITION_ID, createFeatureTypeToolId);

        // Check new created element inherits from the base element, and thus, contains a list item with '^' in its label
        Consumer<Object> createFeatureTypeDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(GVInterfaceDefinitionEndsCompartmentItemInheritanceTests.this.diagramComparator)
                    .hasNewBorderNodeCount(1)
                    .hasNewNodeCount(3) // ^port1 in both ports and ends compartments + ^port as border node
                    .hasNewEdgeCount(1) // The specialization edge
                    .check(diagram.get(), newDiagram);

            var endsCompartment = new DiagramNavigator(newDiagram)
                    .nodeWithId(newInterfaceDefinitionNodeId.get())
                    .childNodeWithLabel("ends")
                    .getNode();
            assertThat(endsCompartment.getChildNodes()).hasSize(3);
            assertThat(endsCompartment.getChildNodes().getFirst().getInsideLabel().getText()).isEqualTo("^" + "port1"); // The inheriting element has the same name prefixed with '^'
            var portsCompartment = new DiagramNavigator(newDiagram)
                    .nodeWithId(newInterfaceDefinitionNodeId.get())
                    .childNodeWithLabel("ports")
                    .getNode();
            assertThat(portsCompartment.getChildNodes()).hasSize(3);
            assertThat(portsCompartment.getChildNodes().getFirst().getInsideLabel().getText()).isEqualTo("^" + "port1"); // The inheriting element has the same name prefixed with '^'
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newCreationTool)
                .consumeNextWith(createdPortAsEndDiagramConsumer)
                .then(createInterfaceDefinitionRunnable)
                .consumeNextWith(createdActionDefinitionDiagramConsumer)
                .then(createFeatureTyping)
                .consumeNextWith(createFeatureTypeDiagramConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
