/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the invocation of the "New Feature Typing" tool from a Part Usage in the General View diagram.
 *
 * @author Jerome Gout
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class GVAddNewFeatureTypingFromPartUsageTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private ToolTester nodeCreationTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private final UtilService utilService = new UtilService();

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, GeneralViewEmptyTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    private static Stream<Arguments> partUsageNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), "part1", 11, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationUsage(), "allocation1", 6, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceUsage(), "interface1", 17, 4));
    }

    @DisplayName("GIVEN a SysML Project, WHEN the New Feature Typing tool is requested on a PartUsage, THEN a new PartDefinition node and a FeatureTyping edge are created")
    @GivenSysONServer({ GeneralViewEmptyTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("partUsageNodeParameters")
    public void testApplyTool(EClass eClass, String nodeName, int definitionCompartmentCount, int newBorderNodes) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        String creationToolId = diagramDescriptionIdProvider.getDiagramCreationToolId(this.descriptionNameGenerator.getCreationToolName(eClass));
        Runnable invokeToolRunnable = () -> this.nodeCreationTester.invokeTool(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, diagram, creationToolId);

        AtomicReference<Diagram> diagramAfterAddingElement = new AtomicReference<>();
        Consumer<Object> diagramAfterAddingContentConsumer = assertRefreshedDiagramThat(diagramAfterAddingElement::set);

        var newName = this.getNewName(nodeName);
        Runnable renameRunnable = () -> this.nodeCreationTester.renameRootNode(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                    diagramAfterAddingElement,
                    nodeName,
                    newName);

        AtomicReference<Diagram> diagramAfterRenameElement = new AtomicReference<>();
        Consumer<Object> diagramAfterRenameContentConsumer = assertRefreshedDiagramThat(diagramAfterRenameElement::set);

        String toolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(eClass), "New Feature Typing");
        assertThat(toolId).as("The tool 'New Feature Typing' should exist on a ").isNotNull();

        Runnable invokeToolRunnable2 = () -> this.nodeCreationTester.invokeTool(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                diagramAfterRenameElement,
                newName,
                toolId);

        Runnable semanticChecker = this.semanticRunnableFactory.createRunnable(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Object semanticRootObject = this.objectSearchService.getObject(editingContext, GeneralViewEmptyTestProjectData.SemanticIds.PACKAGE_1_ID).orElse(null);
                    assertThat(semanticRootObject).isInstanceOf(Element.class);
                    Element semanticRootElement = (Element) semanticRootObject;
                    assertThat(semanticRootElement).isNotNull();
                    EObject eObject = SysmlFactory.eINSTANCE.create(eClass);
                    var definitionEClass = this.utilService.getPartDefinitionEClassFrom((PartUsage) eObject);
                    // should find the PartUsage
                    var partUsage = semanticRootElement.getOwnedElement().stream().filter(eClass::isInstance).filter(elt -> Objects.equals(newName, elt.getDeclaredName())).findFirst();
                    assertThat(partUsage).isNotEmpty();
                    // should find the PartDefinition
                    var partDefinition = semanticRootElement.getOwnedElement().stream().filter(definitionEClass::isInstance).findFirst();
                    assertThat(partDefinition).isNotEmpty();
                    // PartUsage should contain a FeatureTyping relationship pointing to the PartDefinition (through its
                    // type property)
                    assertThat(partUsage.get().getOwnedRelationship()).isNotEmpty();
                    var featureTypingRelationship = partUsage.get().getOwnedRelationship().stream()
                            .filter(FeatureTyping.class::isInstance)
                            .map(FeatureTyping.class::cast)
                            .findFirst();
                    assertThat(featureTypingRelationship).isNotEmpty();
                    assertThat(featureTypingRelationship.get().getType()).isEqualTo(partDefinition.get());
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        Consumer<Object> diagramAfterRenameContentConsumer2 = assertRefreshedDiagramThat(newDiagram -> new CheckDiagramElementCount(this.diagramComparator)
                    // we should have 1 more node (the new PartDefinition) and one more edge (the new FeatureTyping)
                    // since compartment nodes of the new PartDefinition are added as well, we need to count them also.
                    .hasNewNodeCount(1 + definitionCompartmentCount)
                    .hasNewEdgeCount(1)
                    .hasNewBorderNodeCount(newBorderNodes)
                    .check(diagramAfterAddingElement.get(), newDiagram));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(invokeToolRunnable)
                .consumeNextWith(diagramAfterAddingContentConsumer)
                .then(renameRunnable)
                .consumeNextWith(diagramAfterRenameContentConsumer)
                .then(invokeToolRunnable2)
                .then(semanticChecker)
                .consumeNextWith(diagramAfterRenameContentConsumer2)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private String getNewName(String nodeName) {
        return nodeName + "_new";
    }
}
