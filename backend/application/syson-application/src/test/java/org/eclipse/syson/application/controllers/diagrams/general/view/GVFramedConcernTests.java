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
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE_STYLE;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.ArrowStyle;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.Label;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingcontext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.FramedConcernMembership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * {@link org.eclipse.syson.sysml.FramedConcernMembership} related test in General View.
 *
 * @author gcoutable
 */
@Transactional
@GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVFramedConcernTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private EdgeCreationTester edgeCreationTester;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private SemanticCheckerService semanticCheckerService;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
    }

    @Test
    public void testCreateFramedConcernWithEdgeBetweenRequirementUsageAndConcernUsage() {
        this.createFramedConcernWithEdge(SysmlPackage.eINSTANCE.getRequirementUsage(), GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_USAGE_ID, "requirement");
    }

    @Test
    public void testCreateFramedConcernWithEdgeBetweenRequirementDefinitionAndConcernUsage() {
        this.createFramedConcernWithEdge(SysmlPackage.eINSTANCE.getRequirementDefinition(), GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_DEFINITION_ID, "RequirementDefinition");
    }

    private void createFramedConcernWithEdge(EClass parentEClass, String graphicalSourceNodeId, String parentLabel) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var edgeCreationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(
                this.descriptionNameGenerator.getNodeName(parentEClass),
                "New framed Concern");

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable createEdgeRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                graphicalSourceNodeId,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.CONCERN_USAGE_ID,
                edgeCreationToolId);

        Consumer<Object> diagramContentConsumerAfterNewEdge = assertRefreshedDiagramThat(newDiagram -> {
            var newEdges = this.diagramComparator.newEdges(diagram.get(), newDiagram);
            var newVisibleEdges = newEdges.stream().filter(edge -> edge.getState() != ViewModifier.Hidden).toList();

            assertThat(newVisibleEdges).hasSize(1).first(EDGE)
                    .hasSourceId(graphicalSourceNodeId)
                    .hasTargetId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.CONCERN_USAGE_ID)
                    .extracting(Edge::getCenterLabel)
                    .extracting(Label::text)
                    .hasToString(LabelConstants.OPEN_QUOTE + LabelConstants.FRAME + LabelConstants.CLOSE_QUOTE);

            assertThat(newVisibleEdges).hasSize(1).first(EDGE)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.InputArrow);
        });

        Consumer<Object> additionalCheck = object -> {
            assertThat(object).isInstanceOf(List.class)
                    .asInstanceOf(type(List.class))
                    .satisfies(framedConcerns -> {
                        assertThat((List<?>) framedConcerns).size().isEqualTo(1);
                        assertThat(framedConcerns.getFirst())
                                .isInstanceOf(FramedConcernMembership.class)
                                .asInstanceOf(type(FramedConcernMembership.class))
                                .satisfies(framedConcernMembership -> {
                                    var ownedConcern = framedConcernMembership.getOwnedConcern();
                                    var ownedSubsetting = ownedConcern.getOwnedSubsetting();
                                    assertThat(ownedSubsetting).isNotEmpty();
                                    assertThat(ownedSubsetting.getFirst().getSubsettedFeature()).isEqualTo(framedConcernMembership.getReferencedConcern());
                                });
                    });
        };

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, SysmlPackage.eINSTANCE.getElement_OwnedRelationship(), SysmlPackage.eINSTANCE.getFramedConcernMembership(), additionalCheck));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createEdgeRunnable)
                .consumeNextWith(diagramContentConsumerAfterNewEdge)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
