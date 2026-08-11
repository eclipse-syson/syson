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

package org.eclipse.syson.application.controllers.diagrams.general.view.inheritance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariableType;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Test runner utility class to perform inheritance of nested elements through specializations.
 * <p>
 *     The super element contains a nested element inside a compartment. A sub-element
 *     is created empty, and an inheritance specialization is established between
 *     the super and sub-elements.
 *     The test is composed of 4 parts:
 *     <ol>
 *         <li>
 *             Creation of the super element on GV diagram background.
 *         <li>
 *             Creation of the nested element inside the super element.
 *         <li>
 *             Creation of the sub element on GV diagram background.
 *         <li>
 *             Creation of the specialization edge between both super and sub elements.
 *     </ol>
 *     After each step, check are done to validate that the step is correct.
 *     At the end, the inherited nested element should be found inside the compartment of the sub element with the correct name.
 * </p>
 * <p>
 *     This class exposes several set of APIs to configure how each step is performed and to control each validation throughout the test.
 * </p>
 * <ul>
 *     <li>
 *         APIs to control the creation of the super element:
 *         <ul>
 *             <li>
 *                 <code>superElementEClass</code> the <code>EClass</code> of the super element to create on the GV diagram background.
 *             </li>
 *             <li>
 *                 <code>superElementCreationToolName</code> the name of the tool used to create the super element on the GV diagram background.
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         API to validate the creation of the super element:
 *         <ul>
 *             <li>
 *                 <code>superElementExpectedBorderNodes</code> the number of border nodes that should be found after the creation of the super element on the GV diagram background.
 *             </li>
 *             <li>
 *                 <code>superElementExpectedNodes</code> the number of nodes that should be found after the creation of the super element on the GV diagram background (including the element itself).
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         APIs to control the creation of the nested element of the super element:
 *         <ul>
 *             <li>
 *                 <code>nestedElementCreationToolName</code> the name of the tool used to create the nested element in the super element graphical node.
 *             </li>
 *             <li>
 *                 <code>compartmentName</code> the name of the compartment where the nested element lives in the super element graphical node.
 *             </li>
 *             <li>
 *                 <code>nestedElementReferencedElementNodeId</code> the id of the existing node element found in the GV diagram that is referenced by the nested element.
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         API to validate the creation of the nested element of the super element:
 *         <ul>
 *             <li>
 *                 <code>nestedElementExpectedName</code> the name of the nested element in the super element graphical node.
 *             </li>
 *             <li>
 *                 <code>nestedElementExpectedBorderNodes</code> the number of border nodes that should be found after the creation of the nested element in the super element graphical node.
 *             </li>
 *             <li>
 *                 <code>nestedElementExpectedNodes</code> the number of nodes that should be found after the creation of the nested element in the super element graphical node.
 *             </li>
 *             <li>
 *                 <code>nestedElementExpectedEdges</code> the number of edges that should be found after the creation of the nested element in the super element graphical node.
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         APIs to control the creation of the sub-element:
 *         <ul>
 *             <li>
 *                 <code>subElementEClass</code> the <code>EClass</code> used to create the sub-element on the GV diagram background.
 *             </li>
 *             <li>
 *                 <code>subElementCreationToolName</code> the name of the tool used to create the sub-element on the GV diagram background.
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         API to validate the creation of the sub-element:
 *         <ul>
 *             <li>
 *                 <code>subElementExpectedBorderNodes</code> the number of border nodes that should be found after the creation of the sub-element on the GV diagram background.
 *             </li>
 *             <li>
 *                 <code>subElementExpectedNodes</code> the number of nodes that should be found after the creation of the sub-element on the GV diagram background (including the sub-element itself).
 *             </li>
 *             <li>
 *                 <code>subElementExpectedEdges</code> the number of edges that should be found after the creation of the sub-element on the GV diagram background.
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         APIs to control the creation of the specialization between the super element and the sub-element:
 *         <ul>
 *             <li>
 *                 <code>specializationCreationToolName</code> the name of the tool used to create the specialization.
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         API to validate the creation of the specialization between the super element and the sub-element:
 *         <ul>
 *             <li>
 *                 <code>specializationExpectedBorderNodes</code> the number of border nodes that should be found after the creation of the specialization between the super element and the sub-element.
 *             </li>
 *             <li>
 *                 <code>specializationExpectedNodes</code> the number of nodes that should be found after the creation of the specialization between the super element and the sub-element.
 *             </li>
 *             <li>
 *                 <code>specializationExpectedEdges</code> the number of edges that should be found after the creation of the specialization between the super element and the sub-element (including the specialization edge itself).
 *             </li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * @author Jerome Gout
 */
@Service
@SuppressWarnings("checkstyle:HiddenField")
public class GraphicalInheritanceTestRunner {

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

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

    private EClass superElementEClass;

    private String superElementCreationToolName;

    private int superElementExpectedBorderNodes;

    private int superElementExpectedNodes = 1;

    private String nestedElementCreationToolName;

    private int nestedElementExpectedBorderNodes;

    private int nestedElementExpectedNodes = 1;

    private int nestedElementExpectedEdges;

    private Optional<String> nestedElementReferencedElementNodeId = Optional.empty();

    private String compartmentName;

    private String nestedElementExpectedName;

    private EClass subElementEClass;

    private String subElementCreationToolName;

    private int subElementExpectedBorderNodes;

    private int subElementExpectedNodes = 1;

    private int subElementExpectedEdges;

    private String specializationCreationToolName;

    private int specializationExpectedBorderNodes;

    private int specializationExpectedNodes;

    private int specializationExpectedEdges = 1;

    public GraphicalInheritanceTestRunner superElementEClass(EClass superElementEClass) {
        this.superElementEClass = Objects.requireNonNull(superElementEClass);
        return this;
    }

    public GraphicalInheritanceTestRunner superElementCreationToolName(String superElementCreationToolName) {
        this.superElementCreationToolName = Objects.requireNonNull(superElementCreationToolName);
        return this;
    }

    public GraphicalInheritanceTestRunner superElementExpectedBorderNodes(int superElementExpectedBorderNodes) {
        this.superElementExpectedBorderNodes = superElementExpectedBorderNodes;
        return this;
    }

    public GraphicalInheritanceTestRunner superElementExpectedNodes(int superElementExpectedNodes) {
        this.superElementExpectedNodes = superElementExpectedNodes;
        return this;
    }

    public GraphicalInheritanceTestRunner nestedElementCreationToolName(String nestedElementCreationToolName) {
        this.nestedElementCreationToolName = Objects.requireNonNull(nestedElementCreationToolName);
        return this;
    }

    public GraphicalInheritanceTestRunner nestedElementReferencedElementNodeId(String nestedElementReferencedElementNodeId) {
        this.nestedElementReferencedElementNodeId = Optional.of(Objects.requireNonNull(nestedElementReferencedElementNodeId));
        return this;
    }

    public GraphicalInheritanceTestRunner nestedElementExpectedBorderNodes(int nestedElementExpectedBorderNodes) {
        this.nestedElementExpectedBorderNodes = nestedElementExpectedBorderNodes;
        return this;
    }

    public GraphicalInheritanceTestRunner nestedElementExpectedNodes(int nestedElementExpectedNodes) {
        this.nestedElementExpectedNodes = nestedElementExpectedNodes;
        return this;
    }

    public GraphicalInheritanceTestRunner nestedElementExpectedEdges(int nestedElementExpectedEdges) {
        this.nestedElementExpectedEdges = nestedElementExpectedEdges;
        return this;
    }

    public GraphicalInheritanceTestRunner compartmentName(String compartmentName) {
        this.compartmentName = Objects.requireNonNull(compartmentName);
        return this;
    }

    public GraphicalInheritanceTestRunner nestedElementExpectedName(String nestedElementExpectedName) {
        this.nestedElementExpectedName = Objects.requireNonNull(nestedElementExpectedName);
        return this;
    }

    public GraphicalInheritanceTestRunner subElementEClass(EClass subElementEClass) {
        this.subElementEClass = Objects.requireNonNull(subElementEClass);
        return this;
    }

    public GraphicalInheritanceTestRunner subElementCreationToolName(String subElementCreationToolName) {
        this.subElementCreationToolName = Objects.requireNonNull(subElementCreationToolName);
        return this;
    }

    public GraphicalInheritanceTestRunner subElementExpectedBorderNodes(int subElementExpectedBorderNodes) {
        this.subElementExpectedBorderNodes = subElementExpectedBorderNodes;
        return this;
    }

    public GraphicalInheritanceTestRunner subElementExpectedNodes(int subElementExpectedNodes) {
        this.subElementExpectedNodes = subElementExpectedNodes;
        return this;
    }

    public GraphicalInheritanceTestRunner subElementExpectedEdges(int subElementExpectedEdges) {
        this.subElementExpectedEdges = subElementExpectedEdges;
        return this;
    }

    public GraphicalInheritanceTestRunner specializationCreationToolName(String specializationCreationToolName) {
        this.specializationCreationToolName = Objects.requireNonNull(specializationCreationToolName);
        return this;
    }

    public GraphicalInheritanceTestRunner specializationExpectedBorderNodes(int specializationExpectedBorderNodes) {
        this.specializationExpectedBorderNodes = specializationExpectedBorderNodes;
        return this;
    }

    public GraphicalInheritanceTestRunner specializationExpectedNodes(int specializationExpectedNodes) {
        this.specializationExpectedNodes = specializationExpectedNodes;
        return this;
    }

    public GraphicalInheritanceTestRunner specializationExpectedEdges(int specializationExpectedEdges) {
        this.specializationExpectedEdges = specializationExpectedEdges;
        return this;
    }

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    private void isReady() {
        Objects.requireNonNull(this.superElementEClass);
        Objects.requireNonNull(this.superElementCreationToolName);
        Objects.requireNonNull(this.nestedElementExpectedName);
        Objects.requireNonNull(this.subElementEClass);
        Objects.requireNonNull(this.subElementCreationToolName);
        Objects.requireNonNull(this.specializationCreationToolName);
    }

    public void run() {
        this.isReady();

        var flux = this.givenSubscriptionToDiagram();
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        // Create the super element on diagram
        AtomicReference<String> superElementNodeId = new AtomicReference<>();
        String superElementToolId = diagramDescriptionIdProvider.getDiagramCreationToolId(this.superElementCreationToolName);
        Runnable createSuperElement = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, null, superElementToolId);
        Consumer<Object> superElementCreationDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewBorderNodeCount(this.superElementExpectedBorderNodes)
                    .hasNewNodeCount(this.superElementExpectedNodes)
                    .hasNewEdgeCount(0)
                    .check(diagram.get(), newDiagram, true);
            var newNodes = this.diagramComparator.newNodes(diagram.get(), newDiagram);
            superElementNodeId.set(newNodes.getFirst().getId());
            diagram.set(newDiagram);
        });

        List<ToolVariable> toolVariables = new ArrayList<>();
        this.nestedElementReferencedElementNodeId.ifPresent(s -> toolVariables.add(new ToolVariable("selectedObject", s, ToolVariableType.OBJECT_ID)));

        // Create the nested element in the super element (inside given compartment)
        String nestedElementToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(this.superElementEClass), this.nestedElementCreationToolName);
        Runnable createNestedElement = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram.get().getId(), superElementNodeId.get(), nestedElementToolId, toolVariables);
        Consumer<Object> nestedElementCreationDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewBorderNodeCount(this.nestedElementExpectedBorderNodes)
                    .hasNewNodeCount(this.nestedElementExpectedNodes)
                    .hasNewEdgeCount(this.nestedElementExpectedEdges)
                    .check(diagram.get(), newDiagram, true);

            var itemsCompartment = new DiagramNavigator(newDiagram)
                    .nodeWithId(superElementNodeId.get())
                    .childNodeWithLabel(this.compartmentName)
                    .getNode();
            assertThat(itemsCompartment.getChildNodes()).hasSize(1);
            assertThat(itemsCompartment.getChildNodes().getFirst().getInsideLabel().getText()).isEqualTo(this.nestedElementExpectedName); // The created element has the expected name
            diagram.set(newDiagram);
        });

        // Create the sub element
        AtomicReference<String> subElementNodeId = new AtomicReference<>();
        String createSubElementToolId = diagramDescriptionIdProvider.getDiagramCreationToolId(this.subElementCreationToolName);
        Runnable createSubElement = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, null, createSubElementToolId);
        Consumer<Object> createdSubElementDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewBorderNodeCount(this.subElementExpectedBorderNodes)
                    .hasNewNodeCount(this.subElementExpectedNodes)
                    .hasNewEdgeCount(this.subElementExpectedEdges)
                    .check(diagram.get(), newDiagram, true);
            var newNodes = this.diagramComparator.newNodes(diagram.get(), newDiagram);
            subElementNodeId.set(newNodes.getFirst().getId());
            diagram.set(newDiagram);
        });

        // Create the specialization between the sub element and the super element
        String createSpecializationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(this.subElementEClass), this.specializationCreationToolName);
        Runnable createSpecialization = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, subElementNodeId.get(), superElementNodeId.get(), createSpecializationToolId);

        // Check new created element inherits from the base element, and thus, contains a list item with '^' in its label
        Consumer<Object> createSpecializationDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewBorderNodeCount(this.specializationExpectedBorderNodes)
                    .hasNewNodeCount(this.specializationExpectedNodes)
                    .hasNewEdgeCount(this.specializationExpectedEdges)
                    .check(diagram.get(), newDiagram);

            var parameterCompartment = new DiagramNavigator(newDiagram)
                    .nodeWithId(subElementNodeId.get())
                    .childNodeWithLabel(this.compartmentName)
                    .getNode();
            assertThat(parameterCompartment.getChildNodes()).hasSize(1);
            assertThat(parameterCompartment.getChildNodes().getFirst().getInsideLabel().getText()).isEqualTo("^" + this.nestedElementExpectedName); // The inheriting element has the same name prefixed with '^'
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createSuperElement)
                .consumeNextWith(superElementCreationDiagramConsumer)
                .then(createNestedElement)
                .consumeNextWith(nestedElementCreationDiagramConsumer)
                .then(createSubElement)
                .consumeNextWith(createdSubElementDiagramConsumer)
                .then(createSpecialization)
                .consumeNextWith(createSpecializationDiagramConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
