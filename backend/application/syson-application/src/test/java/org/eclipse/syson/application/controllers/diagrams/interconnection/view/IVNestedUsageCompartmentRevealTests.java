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
package org.eclipse.syson.application.controllers.diagrams.interconnection.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.data.InterconnectionViewPartDefinitionCompartmentRevealTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Tests compartment visibility after reparenting usages in Interconnection View.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IVNestedUsageCompartmentRevealTests extends AbstractIntegrationTests {

    private static final String EDITING_CONTEXT_ID = InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.EDITING_CONTEXT_ID;

    private static final String NO_COMPARTMENT_DIAGRAM_ID = InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.GraphicalIds.NO_COMPARTMENT_DISPLAYED_DIAGRAM_ID;

    private static final String INTERCONNECTION = "interconnection";

    private final SDVDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private IIdentityService identityService;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private EdgeCreationTester edgeCreationTester;

    /**
     * Initializes the server before each scenario.
     */
    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    /**
     * Provides nesting directions, usage types, and existing compartment visibility states.
     *
     * @return the reparenting scenarios
     */
    private static List<Arguments> reparentingScenarios() {
        var part = SysmlPackage.eINSTANCE.getPartUsage();
        var partDefinition = SysmlPackage.eINSTANCE.getPartDefinition();
        var action = SysmlPackage.eINSTANCE.getActionUsage();
        return List.of(
                Arguments.of(NO_COMPARTMENT_DIAGRAM_ID, part, part, "Add target as nested Part", false, false, INTERCONNECTION),
                Arguments.of(NO_COMPARTMENT_DIAGRAM_ID, part, part, "Become nested Part", true, false, INTERCONNECTION),
                Arguments.of(NO_COMPARTMENT_DIAGRAM_ID, part, action, "Add target as nested Action", false, false, INTERCONNECTION),
                Arguments.of(NO_COMPARTMENT_DIAGRAM_ID, partDefinition, part, "Add target as owned Part", false, false, INTERCONNECTION),
                Arguments.of(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.GraphicalIds.INTERCONNECTION_COMPARTMENT_DISPLAYED_DIAGRAM_ID,
                        partDefinition, part, "Add target as owned Part", false, true, INTERCONNECTION),
                Arguments.of(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.GraphicalIds.PARTS_COMPARTMENT_DISPLAYED_DIAGRAM_ID,
                        partDefinition, part, "Add target as owned Part", false, false, "parts"));
    }

    /**
     * Runs the reparenting scenarios and verifies semantic ownership and compartment visibility with dedicated checks.
     *
     * @param diagramId
     *            the diagram containing the initial visibility state
     * @param parentType
     *            the new parent type
     * @param childType
     *            the moved usage type
     * @param toolName
     *            the reparenting tool to execute
     * @param reverse
     *            whether the child is the connector source
     * @param existingChild
     *            whether the parent already has an exposed child
     * @param expectedCompartment
     *            the compartment that must display the moved child
     */
    @DisplayName("GIVEN an IV diagram, WHEN a usage is reparented, THEN a suitable parent compartment displays it")
    @GivenSysONServer({ InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("reparentingScenarios")
    public void reparentingRevealsSuitableCompartment(String diagramId, EClass parentType, EClass childType, String toolName,
            boolean reverse, boolean existingChild, String expectedCompartment) {
        var diagram = new AtomicReference<Diagram>();
        var parent = new AtomicReference<Element>();
        var child = new AtomicReference<Usage>();
        var previousChild = new AtomicReference<Usage>();

        var prepareElements = this.semanticRunnableFactory.createMutationRunnable(EDITING_CONTEXT_ID, (editingContext, input) -> {
            var root = (Element) this.objectSearchService.getObject(editingContext,
                    InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID).orElseThrow();
            var view = (ViewUsage) this.objectSearchService.getObject(editingContext, diagram.get().getTargetObjectId()).orElseThrow();
            if (parentType == SysmlPackage.eINSTANCE.getPartDefinition()) {
                parent.set(root);
            } else {
                parent.set(SysmlFactory.eINSTANCE.createPartUsage());
                parent.get().setDeclaredName("parent");
                this.addExposedElement(root.getOwner(), parent.get(), view);
            }
            child.set((Usage) SysmlFactory.eINSTANCE.create(childType));
            child.get().setDeclaredName("child");
            this.addExposedElement(root.getOwner(), child.get(), view);
            if (existingChild) {
                previousChild.set(SysmlFactory.eINSTANCE.createPartUsage());
                previousChild.get().setDeclaredName("previousChild");
                this.addExposedElement(parent.get(), previousChild.get(), view);
            }
            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), true);
        });

        var sourceType = parentType;
        if (reverse) {
            sourceType = childType;
        }
        var toolId = this.getToolId(sourceType, toolName);
        Runnable reparent = () -> {
            var parentNode = new DiagramNavigator(diagram.get()).nodeWithTargetObjectId(this.identityService.getId(parent.get())).getNode();
            var childNode = new DiagramNavigator(diagram.get()).nodeWithTargetObjectId(this.identityService.getId(child.get())).getNode();
            assertThat(parentNode.getState()).isEqualTo(ViewModifier.Normal);
            assertThat(childNode.getState()).isEqualTo(ViewModifier.Normal);
            var sourceId = parentNode.getId();
            var targetId = childNode.getId();
            if (reverse) {
                sourceId = childNode.getId();
                targetId = parentNode.getId();
            }
            this.edgeCreationTester.createEdgeUsingNodeId(EDITING_CONTEXT_ID, diagram, sourceId, targetId, toolId);
        };

        var checkOwnership = this.semanticRunnableFactory.createQueryRunnable(EDITING_CONTEXT_ID, (editingContext, input) -> {
            assertThat(child.get().getOwner()).isSameAs(parent.get());
            assertThat(child.get().isIsComposite()).isTrue();
            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), true);
        });

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);
        Consumer<Object> preparedDiagramContentConsumer = assertRefreshedDiagramThat(updatedDiagram -> {
            diagram.set(updatedDiagram);
            var parentNode = new DiagramNavigator(updatedDiagram).nodeWithTargetObjectId(this.identityService.getId(parent.get())).getNode();
            if (diagramId.equals(NO_COMPARTMENT_DIAGRAM_ID)) {
                assertThat(parentNode.getChildNodes()).allMatch(node -> node.getState() == ViewModifier.Hidden);
            }
        });
        Consumer<Object> reparentedDiagramContentConsumer = assertRefreshedDiagramThat(updatedDiagram ->
                this.checkReparentedDiagram(updatedDiagram, parent.get(), child.get(), previousChild.get(), existingChild, expectedCompartment));

        var flux = this.givenDiagramSubscription.subscribe(new DiagramEventInput(UUID.randomUUID(), EDITING_CONTEXT_ID, diagramId));
        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(prepareElements)
                .consumeNextWith(preparedDiagramContentConsumer)
                .then(reparent)
                .consumeNextWith(reparentedDiagramContentConsumer)
                .then(checkOwnership)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    /**
     * Checks that a rejected cycle preserves ownership and leaves the target's compartments hidden.
     */
    @DisplayName("GIVEN nested parts in IV, WHEN nesting the parent under its child, THEN ownership and visibility are unchanged")
    @GivenSysONServer({ InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SCRIPT_PATH })
    @Test
    public void rejectedCycleDoesNotRevealCompartment() {
        var diagram = new AtomicReference<Diagram>();
        var originalOwner = new AtomicReference<Element>();
        var parent = SysmlFactory.eINSTANCE.createPartUsage();
        parent.setDeclaredName("parent");
        var child = SysmlFactory.eINSTANCE.createPartUsage();
        child.setDeclaredName("child");

        var prepareElements = this.semanticRunnableFactory.createMutationRunnable(EDITING_CONTEXT_ID, (editingContext, input) -> {
            var root = (Element) this.objectSearchService.getObject(editingContext,
                    InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID).orElseThrow();
            var view = (ViewUsage) this.objectSearchService.getObject(editingContext, diagram.get().getTargetObjectId()).orElseThrow();
            originalOwner.set(root.getOwner());
            this.addExposedElement(root.getOwner(), parent, view);
            this.addExposedElement(parent, child, view);
            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), true);
        });

        Runnable attemptCycle = () -> {
            var parentNode = new DiagramNavigator(diagram.get()).nodeWithTargetObjectId(this.identityService.getId(parent)).getNode();
            var childNode = this.getCompartment(parentNode, INTERCONNECTION).getChildNodes().stream()
                    .filter(node -> this.identityService.getId(child).equals(node.getTargetObjectId())).findFirst().orElseThrow();
            assertThat(childNode.getChildNodes()).allMatch(node -> node.getState() == ViewModifier.Hidden);
            this.edgeCreationTester.runSingleClickOnTwoDiagramElementsTool(EDITING_CONTEXT_ID, diagram, parentNode.getId(), childNode.getId(),
                    this.getToolId(SysmlPackage.eINSTANCE.getPartUsage(), "Become nested Part"),
                    List.of("Cannot change the owner of parent, this would create a containment cycle"));
        };

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);
        Consumer<Object> preparedDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);
        Consumer<Object> afterRejectedCycleConsumer = assertRefreshedDiagramThat(updatedDiagram -> {
            var parentNode = new DiagramNavigator(updatedDiagram).nodeWithTargetObjectId(this.identityService.getId(parent)).getNode();
            var childNode = this.getCompartment(parentNode, INTERCONNECTION).getChildNodes().stream()
                    .filter(node -> this.identityService.getId(child).equals(node.getTargetObjectId())).findFirst().orElseThrow();
            assertThat(childNode.getChildNodes()).allMatch(node -> node.getState() == ViewModifier.Hidden);
        });

        var checkOwnership = this.semanticRunnableFactory.createQueryRunnable(EDITING_CONTEXT_ID, (editingContext, input) -> {
            assertThat(child.getOwner()).isSameAs(parent);
            assertThat(parent.getOwner()).isSameAs(originalOwner.get());
            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), true);
        });

        var flux = this.givenDiagramSubscription.subscribe(new DiagramEventInput(UUID.randomUUID(), EDITING_CONTEXT_ID, NO_COMPARTMENT_DIAGRAM_ID));
        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(prepareElements)
                .consumeNextWith(preparedDiagramContentConsumer)
                .then(attemptCycle)
                .consumeNextWith(afterRejectedCycleConsumer)
                .then(checkOwnership)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    /**
     * Checks ownership and graphical visibility after moving a usage into its new parent.
     *
     * @param diagram
     *            the refreshed diagram
     * @param parent
     *            the new semantic parent
     * @param child
     *            the moved usage
     * @param previousChild
     *            the previously exposed child, if one was created
     * @param existingChild
     *            whether the previously exposed child must remain in the compartment
     * @param expectedCompartment
     *            the compartment that must display the moved usage
     */
    private void checkReparentedDiagram(Diagram diagram, Element parent, Usage child, Usage previousChild, boolean existingChild, String expectedCompartment) {
        assertThat(child.getOwner()).isSameAs(parent);
        var parentNode = new DiagramNavigator(diagram).nodeWithTargetObjectId(this.identityService.getId(parent)).getNode();
        var compartment = this.getCompartment(parentNode, expectedCompartment);
        assertThat(compartment.getState()).isEqualTo(ViewModifier.Normal);
        assertThat(compartment.getChildNodes())
                .filteredOn(node -> this.identityService.getId(child).equals(node.getTargetObjectId()))
                .singleElement().extracting(Node::getState).isEqualTo(ViewModifier.Normal);
        if (existingChild) {
            assertThat(compartment.getChildNodes()).extracting(Node::getTargetObjectId)
                    .contains(this.identityService.getId(previousChild));
        }
        if (!INTERCONNECTION.equals(expectedCompartment)) {
            assertThat(this.getCompartment(parentNode, INTERCONNECTION).getState()).isEqualTo(ViewModifier.Hidden);
        }
        assertThat(diagram.getNodes()).noneMatch(node -> this.identityService.getId(child).equals(node.getTargetObjectId()));
        assertThat(diagram.getEdges()).isEmpty();
    }

    /**
     * Adds an element to its owner and exposes its membership in the view.
     *
     * @param owner
     *            the element's owner
     * @param element
     *            the element to add
     * @param view
     *            the view in which to expose the element
     */
    private void addExposedElement(Element owner, Element element, ViewUsage view) {
        var membership = new MetamodelMutationElementService().createMembership(owner);
        membership.getOwnedRelatedElement().add(element);
        if (element instanceof Usage usage) {
            usage.setIsComposite(true);
        }
        var expose = SysmlFactory.eINSTANCE.createMembershipExpose();
        expose.setImportedMembership(membership);
        expose.setIsImportAll(true);
        view.getOwnedRelationship().add(expose);
    }

    /**
     * Retrieves the named compartment directly under a node.
     *
     * @param node
     *            the compartment's parent node
     * @param name
     *            the compartment label
     * @return the matching compartment
     */
    private Node getCompartment(Node node, String name) {
        return node.getChildNodes().stream().filter(child -> name.equals(child.getInsideLabel().getText())).findFirst().orElseThrow();
    }

    /**
     * Resolves a standard diagram edge tool through the edge creation tester.
     *
     * @param sourceType
     *            the connector source type
     * @param toolName
     *            the tool label
     * @return the executable tool identifier
     */
    private String getToolId(EClass sourceType, String toolName) {
        return this.edgeCreationTester.getEdgeCreationToolId(EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID,
                this.descriptionNameGenerator.getNodeName(sourceType), toolName);
    }
}
