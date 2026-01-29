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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.controllers.utils.TestNameGenerator;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.LabelConstants;
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
 * Objective compartment related tests.
 *
 * @author arichard
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVObjectiveDocumentationTests extends AbstractIntegrationTests {

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

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private static Stream<Arguments> objectiveDocumentation() {
        return Stream.of(
                Arguments.of("case def", "CaseDefinition", SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition(),
                        SysmlPackage.eINSTANCE.getCaseDefinition_ObjectiveRequirement()),
                Arguments.of("use case def", "UseCaseDefinition", SysmlPackage.eINSTANCE.getUseCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition(),
                        SysmlPackage.eINSTANCE.getCaseDefinition_ObjectiveRequirement()),
                Arguments.of("case", "case", SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ObjectiveRequirement()),
                Arguments.of("ref use case", "useCase", SysmlPackage.eINSTANCE.getUseCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ObjectiveRequirement()))
                .map(TestNameGenerator::namedArguments);
    }

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a diagram with a UseCaseDefinition node, WHEN an objective Documentation node is created, THEN the Documentation node is visible inside the objective compartment")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("objectiveDocumentation")
    public void testCreateObjectiveDocumentation(String parentLabelPrefix, String parentLabel, EClass parentClass, EClass compartmentClass, EReference compartmentRef) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newObjectiveDocumentationToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getCompartmentName(compartmentClass, compartmentRef),
                "New Objective Documentation");
        assertThat(newObjectiveDocumentationToolId).as("The tool 'New Objective Documentation' should exist on the UseCaseDefinition").isNotNull();

        var diagramId = new AtomicReference<String>();
        var objectiveCompartmentNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var node = new DiagramNavigator(diag).nodeWithLabel(LabelConstants.OPEN_QUOTE + parentLabelPrefix + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + parentLabel)
                    .childNodeWithLabel("objective").getNode();
            assertThat(node).isNotNull();
            assertThat(node.getChildNodes()).hasSize(0);

            diagramId.set(diag.getId());
            objectiveCompartmentNodeId.set(node.getId());
        });

        Runnable newObjectiveDocumentationTool = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), objectiveCompartmentNodeId.get(),
                newObjectiveDocumentationToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var node = new DiagramNavigator(diag).nodeWithLabel(LabelConstants.OPEN_QUOTE + parentLabelPrefix + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + parentLabel)
                    .childNodeWithLabel("objective").getNode();
            assertThat(node).isNotNull();
            assertThat(node.getChildNodes()).hasSize(2);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newObjectiveDocumentationTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
