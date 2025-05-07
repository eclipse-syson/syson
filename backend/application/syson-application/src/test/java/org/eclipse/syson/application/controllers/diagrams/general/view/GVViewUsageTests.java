/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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

import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckChildNode;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.NodeCreationTester;
import org.eclipse.syson.application.controllers.utils.TestNameGenerator;
import org.eclipse.syson.application.data.GeneralViewViewTestProjectData;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
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

/**
 * Tests related to View in the General View Diagram.
 *
 * @author frouene
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVViewUsageTests extends AbstractIntegrationTests {

    private final IDescriptionNameGenerator descriptionNameGenerator = new GVDescriptionNameGenerator();

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
    private NodeCreationTester nodeCreationTester;

    @Autowired
    private DiagramComparator diagramComparator;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private StepVerifier.Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private DiagramDescription diagramDescription;

    private static Stream<Arguments> childNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeDefinition(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getEnumerationDefinition(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getItemDefinition(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getPackage(), 0),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), 9),
                Arguments.of(SysmlPackage.eINSTANCE.getPartDefinition(), 9),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationUsage(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationDefinition(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceUsage(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceDefinition(), 5),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPortDefinition(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getAcceptActionUsage(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), 5),
                Arguments.of(SysmlPackage.eINSTANCE.getActionDefinition(), 5),
                Arguments.of(SysmlPackage.eINSTANCE.getAssignmentActionUsage(), 1),
                Arguments.of(SysmlPackage.eINSTANCE.getConcernUsage(), 7),
                Arguments.of(SysmlPackage.eINSTANCE.getConcernDefinition(), 8),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintUsage(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintDefinition(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getConnectionDefinition(), 1),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementUsage(), 7),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementDefinition(), 8),
                Arguments.of(SysmlPackage.eINSTANCE.getUseCaseUsage(), 5),
                Arguments.of(SysmlPackage.eINSTANCE.getUseCaseDefinition(), 5),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceUsage(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceDefinition(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getMetadataDefinition(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), 7),
                Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), 6),
                Arguments.of(SysmlPackage.eINSTANCE.getStateDefinition(), 6),
                Arguments.of(SysmlPackage.eINSTANCE.getExhibitStateUsage(), 6),
                Arguments.of(SysmlPackage.eINSTANCE.getViewUsage(), 1)
        ).map(TestNameGenerator::namedArguments);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewViewTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewViewTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewViewTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(this.diagramDescription, this.diagramIdProvider);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @Sql(scripts = { GeneralViewViewTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("childNodeParameters")
    @DisplayName("Given a General View with view usage node, when child nodes are created, then nodes are added to the diagram")
    public void checkViewUsageChildNodeCreation(EClass eClass, int compartmentCount) {
        String creationToolId = this.diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getViewUsage()),
                this.descriptionNameGenerator.getCreationToolName(eClass));

        this.verifier.then(() -> this.nodeCreationTester.createNode(GeneralViewViewTestProjectData.EDITING_CONTEXT_ID,
                this.diagram.get().getId(),
                GeneralViewViewTestProjectData.GraphicalIds.VIEW_USAGE_ID,
                creationToolId, List.of()));

        Consumer<DiagramRefreshedEventPayload> updatedDiagramConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(newDiagram -> {
                    int createdNodesExpectedCount = 1 + compartmentCount;
                    new CheckDiagramElementCount(this.diagramComparator)
                            .hasNewEdgeCount(0)
                            .hasNewNodeCount(createdNodesExpectedCount)
                            .check(this.diagram.get(), newDiagram);

                    String newNodeDescriptionName = this.descriptionNameGenerator.getNodeName(eClass);

                    new CheckChildNode(this.diagramDescriptionIdProvider, this.diagramComparator)
                            .withParentNodeId(GeneralViewViewTestProjectData.GraphicalIds.VIEW_USAGE_ID)
                            .hasNodeDescriptionName(newNodeDescriptionName)
                            .hasCompartmentCount(compartmentCount)
                            .check(this.diagram.get(), newDiagram);
                }, () -> fail("Missing diagram"));

        this.verifier.consumeNextWith(updatedDiagramConsumer);

    }
}
