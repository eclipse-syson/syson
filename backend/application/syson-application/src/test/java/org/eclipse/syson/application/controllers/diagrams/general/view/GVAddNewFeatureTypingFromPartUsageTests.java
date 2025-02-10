/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.NodeCreationTester;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

/**
 * Tests the invocation of the "New Feature Typing" tool from a Part Usage in the General View diagram.
 *
 * @author Jerome Gout
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVAddNewFeatureTypingFromPartUsageTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramReference givenDiagram;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private IObjectService objectService;

    @Autowired
    private NodeCreationTester nodeCreationTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private DiagramCheckerService diagramCheckerService;

    private Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private final IDescriptionNameGenerator descriptionNameGenerator = new GVDescriptionNameGenerator();

    private final UtilService utilService = new UtilService();

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                SysMLv2Identifiers.GENERAL_VIEW_EMPTY_EDITING_CONTEXT_ID,
                SysMLv2Identifiers.GENERAL_VIEW_EMPTY_DIAGRAM);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(SysMLv2Identifiers.GENERAL_VIEW_EMPTY_EDITING_CONTEXT_ID,
                SysMLv2Identifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        this.diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    private static Stream<Arguments> partUsageNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), "part1", 7),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationUsage(), "allocation1", 2),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceUsage(), "interface1", 4)
                );
    }

    @DisplayName("Given a SysML Project, when the New Feature Typing tool is requested on a PartUsage, then a new PartDefinition node and a FeatureTyping edge are created")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("partUsageNodeParameters")
    public void testApplyTool(EClass eClass, String nodeName, int definitionCompartmentCount) {
        String creationToolId = this.diagramDescriptionIdProvider.getDiagramCreationToolId(this.descriptionNameGenerator.getCreationToolName(eClass));

        this.verifier.then(() -> this.nodeCreationTester.createNodeOnDiagram(SysMLv2Identifiers.GENERAL_VIEW_EMPTY_EDITING_CONTEXT_ID,
                this.diagram,
                creationToolId));

        var diagramAfterAddingElement = this.givenDiagram.getDiagram(this.verifier);

        this.verifier.then(() -> this.nodeCreationTester.renameRootNode(SysMLv2Identifiers.GENERAL_VIEW_EMPTY_EDITING_CONTEXT_ID,
                diagramAfterAddingElement,
                nodeName,
                this.getNewName(nodeName)));

        var diagramAfterRenameElement = this.givenDiagram.getDiagram(this.verifier);

        String toolId = this.diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(eClass), "New Feature Typing");
        assertThat(toolId).as("The tool 'New Feature Typing' should exist on a ").isNotNull();

        this.verifier.then(() -> this.nodeCreationTester.createNode(SysMLv2Identifiers.GENERAL_VIEW_EMPTY_EDITING_CONTEXT_ID,
                diagramAfterRenameElement,
                this.getNewName(nodeName),
                toolId));

        Runnable semanticChecker = this.semanticRunnableFactory.createRunnable(SysMLv2Identifiers.GENERAL_VIEW_EMPTY_EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Object semanticRootObject = this.objectService.getObject(editingContext, SysMLv2Identifiers.GENERAL_VIEW_EMPTY_DIAGRAM_OBJECT).orElse(null);
                    assertThat(semanticRootObject).isInstanceOf(Element.class);
                    Element semanticRootElement = (Element) semanticRootObject;
                    assertThat(semanticRootElement).isNotNull();
                    EObject eObject = SysmlFactory.eINSTANCE.create(eClass);
                    var definitionEClass = this.utilService.getPartDefinitionEClassFrom((PartUsage) eObject);
                    // should find the PartUsage
                    var partUsage = semanticRootElement.getOwnedElement().stream().filter(eClass::isInstance).findFirst();
                    assertThat(partUsage).isNotEmpty();
                    // should find the PartDefinition
                    var partDefinition = semanticRootElement.getOwnedElement().stream().filter(definitionEClass::isInstance).findFirst();
                    assertThat(partDefinition).isNotEmpty();
                    // PartUsage should contain a FeatureTyping relationship pointing to the PartDefinition (through its type property)
                    assertThat(partUsage.get().getOwnedRelationship()).isNotEmpty();
                    var featureTypingRelationship = partUsage.get().getOwnedRelationship().stream()
                            .filter(FeatureTyping.class::isInstance)
                            .map(FeatureTyping.class::cast)
                            .findFirst();
                    assertThat(featureTypingRelationship).isNotEmpty();
                    assertThat(featureTypingRelationship.get().getType()).isEqualTo(partDefinition.get());
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        this.verifier.then(semanticChecker);

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    // we should have 1 more node (the new PartDefinition) and one more edge (the new FeatureTyping)
                    // since compartment nodes of the new PartDefinition are added as well, we need to count them also.
                    .hasNewNodeCount(1 + definitionCompartmentCount)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, diagramAfterAddingElement, this.verifier);
    }

    private String getNewName(String nodeName) {
        return nodeName + "_new";
    }
}
