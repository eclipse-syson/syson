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
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE_STYLE;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.EditLabelInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.EditLabelSuccessPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnDiagramElementToolInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnDiagramElementToolSuccessPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariableType;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.ArrowStyle;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.events.ReconnectEdgeKind;
import org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions;
import org.eclipse.sirius.components.diagrams.tests.graphql.EditLabelMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.InvokeSingleClickOnDiagramElementToolMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.PaletteQueryRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingcontext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeReconnectionTester;
import org.eclipse.syson.application.data.GeneralViewFlowConnectionItemUsagesProjectData;
import org.eclipse.syson.application.data.GeneralViewFlowUsageProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FlowEnd;
import org.eclipse.syson.sysml.FlowUsage;
import org.eclipse.syson.sysml.PayloadFeature;
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
 * Tests on {@link FlowUsage} on the General View Diagram.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVFlowUsageTests extends AbstractIntegrationTests {

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
    private EditLabelMutationRunner editLabelMutationRunner;

    @Autowired
    private InvokeSingleClickOnDiagramElementToolMutationRunner invokeSingleClickOnDiagramElementToolMutationRunner;

    @Autowired
    private PaletteQueryRunner paletteQueryRunner;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private EdgeReconnectionTester edgeReconnectionTester;

    @Autowired
    private IIdentityService identityService;

    private SemanticCheckerService semanticCheckerService;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), GeneralViewFlowConnectionItemUsagesProjectData.EDITING_CONTEXT_ID, GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewFlowConnectionItemUsagesProjectData.EDITING_CONTEXT_ID,
                GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.PACKAGE_1_ID);
    }

    @DisplayName("GIVEN a SysML Project with ItemUsages on ActionUsage, WHEN creating a FlowUsage between them, THEN an edge should be displayed to represent that new flow")
    @GivenSysONServer({ GeneralViewFlowConnectionItemUsagesProjectData.SCRIPT_PATH })
    @Test
    public void checkFlowConnectionCreation() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewFlowConnectionItemUsagesProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getBehavior_Parameter()),
                "New Flow (flow)");
        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewFlowConnectionItemUsagesProjectData.EDITING_CONTEXT_ID,
                diagram,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_2_OUT_ITEM_ID,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_IN_ITEM_ID,
                creationToolId);

        AtomicReference<String> newFlow = new AtomicReference<>();
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            newFlow.set(newEdge.getTargetObjectId());
            assertThat(newEdge).hasSourceId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_2_OUT_ITEM_ID);
            assertThat(newEdge).hasTargetId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_IN_ITEM_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputFillClosedArrow);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(FlowUsage.class, () -> newFlow.get(), flow -> {
            assertThat(this.identityService.getId(flow.getSourceOutputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                    .isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_2_OUT_ITEM_ID);
            assertThat(this.identityService.getId(flow.getTargetInputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                    .isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_3_IN_ITEM_ID);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a SysML Project with ItemUsages on ActionUsage, WHEN creating a BindingConnectorAsUsage between them, THEN an edge should be displayed to represent that new binding")
    @GivenSysONServer({ GeneralViewFlowConnectionItemUsagesProjectData.SCRIPT_PATH })
    @Test
    public void checkItemUsageBindingConnectorAsUsage() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewFlowConnectionItemUsagesProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getBehavior_Parameter()),
                "New Binding Connector As Usage (bind)");
        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewFlowConnectionItemUsagesProjectData.EDITING_CONTEXT_ID,
                diagram,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_2_OUT_ITEM_ID,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_IN_ITEM_ID,
                creationToolId);

        AtomicReference<String> newBinding = new AtomicReference<>();
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            newBinding.set(newEdge.getTargetObjectId());
            assertThat(newEdge)
                    .hasSourceId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_2_OUT_ITEM_ID)
                    .hasTargetId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_IN_ITEM_ID)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.None);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("GIVEN a FlowUsage, WHEN reconnecting the target, THEN the new target of the FlowUsage is correct")
    @GivenSysONServer({ GeneralViewFlowConnectionItemUsagesProjectData.SCRIPT_PATH })
    public void reconnectFlowUsageTarget() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable creationToolRunnable = () -> this.edgeReconnectionTester.reconnectEdge(GeneralViewFlowConnectionItemUsagesProjectData.EDITING_CONTEXT_ID,
                diagram,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.FLOW_CONNECTION_ID,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_IN_ITEM_ID,
                ReconnectEdgeKind.TARGET);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            DiagramAssertions.assertThat(newEdge).hasSourceId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_1_OUT_ITEM_ID);
            DiagramAssertions.assertThat(newEdge).hasTargetId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_IN_ITEM_ID);
            DiagramAssertions.assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputFillClosedArrow);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(FlowUsage.class, () -> GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.FLOW_CONNECTION_ID, flowUsage -> {
            assertThat(this.identityService.getId(flowUsage.getSourceOutputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                    .isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_1_OUT_ITEM_ID);
            assertThat(this.identityService.getId(flowUsage.getTargetInputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                    .isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_3_IN_ITEM_ID);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("GIVEN a FlowUsage, WHEN reconnecting the source, THEN the new source of the FlowUsage is correct")
    @GivenSysONServer({ GeneralViewFlowConnectionItemUsagesProjectData.SCRIPT_PATH })
    public void reconnectFlowUsageSource() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable creationToolRunnable = () -> this.edgeReconnectionTester.reconnectEdge(GeneralViewFlowConnectionItemUsagesProjectData.EDITING_CONTEXT_ID,
                diagram,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.FLOW_CONNECTION_ID,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_OUT_ITEM_ID,
                ReconnectEdgeKind.SOURCE);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            DiagramAssertions.assertThat(newEdge).hasSourceId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_OUT_ITEM_ID);
            DiagramAssertions.assertThat(newEdge).hasTargetId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_2_IN_ITEM_ID);
            DiagramAssertions.assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputFillClosedArrow);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(FlowUsage.class, () -> GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.FLOW_CONNECTION_ID,
                flowUsage -> {
                    assertThat(this.identityService.getId(flowUsage.getSourceOutputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                            .isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_3_OUT_ITEM_ID);
                    assertThat(this.identityService.getId(flowUsage.getTargetInputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                            .isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_2_IN_ITEM_ID);
                });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("GIVEN a connection WHEN we create a flow usage in it THEN the flow is correctly setup")
    @GivenSysONServer({ GeneralViewFlowUsageProjectData.SCRIPT_PATH })
    public void createFlowUsageInConnection() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewFlowUsageProjectData.EDITING_CONTEXT_ID,
                GeneralViewFlowUsageProjectData.GraphicalIds.DIAGRAM_ID);

        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);

        var diagramId = new AtomicReference<String>();
        var connectionEdgeId = new AtomicReference<String>();
        var connectionEdgeLabelId = new AtomicReference<String>();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewFlowUsageProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        String flowCreationToolId = diagramDescriptionIdProvider.getNodeCreationToolIdOnEdge(this.descriptionNameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getConnectionUsage()), "New Flow");

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            var connectionEdge = new DiagramNavigator(diagram).edgeWithId(GeneralViewFlowUsageProjectData.GraphicalIds.CONNECTION_EDGE_ID).getEdge();
            connectionEdgeId.set(connectionEdge.getId());
            connectionEdgeLabelId.set(connectionEdge.getCenterLabel().id());
        });

        Runnable renameAndTypeTheConnection = () -> this.editLabel(diagramId.get(), connectionEdgeLabelId.get(), "cable : HDMICable");

        Consumer<Object> validateLabelEditResult = assertRefreshedDiagramThat(diagram -> this.assertEdgeLabelText(connectionEdgeId.get(), diagram, "cable : HDMICable"));

        Runnable validateSemanticEffectOfLabelEdit = this.semanticRunnableFactory.createRunnable(GeneralViewFlowUsageProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    this.assertConnectionType(editingContext, GeneralViewFlowUsageProjectData.SemanticIds.CONNECT_ID, GeneralViewFlowUsageProjectData.SemanticIds.HDMI_CABLE_ID);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        Runnable createFlowUsageOnConnection = () -> {
            var selectedObjectVariable = new ToolVariable("selectedObject", GeneralViewFlowUsageProjectData.SemanticIds.VIDEO_SIGNAL_ID, ToolVariableType.OBJECT_ID);
            var input = new InvokeSingleClickOnDiagramElementToolInput(UUID.randomUUID(), GeneralViewFlowUsageProjectData.EDITING_CONTEXT_ID, diagramId.get(), List.of(connectionEdgeId.get()),
                    flowCreationToolId, 0, 0,
                    List.of(selectedObjectVariable));
            var result = this.invokeSingleClickOnDiagramElementToolMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.invokeSingleClickOnDiagramElementTool.__typename");
            assertThat(typename).isEqualTo(InvokeSingleClickOnDiagramElementToolSuccessPayload.class.getSimpleName());
            List<String> messages = JsonPath.read(result.data(), "$.data.invokeSingleClickOnDiagramElementTool.messages[*].body");
            assertThat(messages).hasSize(0);
        };

        Consumer<Object> validateEffectOnLabel = assertRefreshedDiagramThat(diagram -> this.assertEdgeLabelText(connectionEdgeId.get(), diagram, "cable : HDMICable \u25b6 Flow"));

        Runnable validateSemanticEffectOfFlowCreation = this.semanticRunnableFactory.createRunnable(GeneralViewFlowUsageProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    var optionalConnection = this.objectSearchService.getObject(editingContext, GeneralViewFlowUsageProjectData.SemanticIds.CONNECT_ID);
                    assertThat(optionalConnection).containsInstanceOf(ConnectionUsage.class);
                    ConnectionUsage connection = (ConnectionUsage) optionalConnection.get();
                    // The flow usage has been created
                    var optionalFlowUsage = connection.getOwnedFeature().stream().filter(FlowUsage.class::isInstance).map(FlowUsage.class::cast).findFirst();
                    assertThat(optionalFlowUsage).isPresent();
                    var flowUsage = optionalFlowUsage.get();

                    // The flow has a FeatureMembership with a PayloadFeature typed by "Video Signal" (the passed
                    // payload)
                    var optionalPayloadFeature = flowUsage.getOwnedFeature().stream().filter(PayloadFeature.class::isInstance).map(PayloadFeature.class::cast).findFirst();
                    assertThat(optionalPayloadFeature).isPresent();
                    var payloadFeature = optionalPayloadFeature.get();
                    var payloadType = ((FeatureTyping) payloadFeature.getOwnedRelationship().get(0)).getType();
                    assertThat(this.identityService.getId(payloadType)).isEqualTo(GeneralViewFlowUsageProjectData.SemanticIds.VIDEO_SIGNAL_ID);

                    // The flow has two FlowEnds: one redefining HDMICable::inputSide, the other HDMICable::outputSide
                    var flowEnds = flowUsage.getOwnedFeature().stream().filter(FlowEnd.class::isInstance).map(FlowEnd.class::cast).toList();
                    assertThat(flowEnds).hasSize(2);
                    var sourceEnd = flowEnds.get(0);
                    assertThat(sourceEnd.getOwnedFeature().get(0).getOwnedRedefinition().get(0).getRedefinedFeature().getQualifiedName()).isEqualTo("Package1::HDMICable::inputSide");
                    var targetEnd = flowEnds.get(1);
                    assertThat(targetEnd.getOwnedFeature().get(0).getOwnedRedefinition().get(0).getRedefinedFeature().getQualifiedName()).isEqualTo("Package1::HDMICable::outputSide");
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(renameAndTypeTheConnection)
                .consumeNextWith(validateLabelEditResult)
                .then(validateSemanticEffectOfLabelEdit)
                .then(createFlowUsageOnConnection)
                .consumeNextWith(validateEffectOnLabel)
                .then(validateSemanticEffectOfFlowCreation)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("GIVEN an untype connection WHEN we opening its palette THEN the flow tool is not proposed")
    @GivenSysONServer({ GeneralViewFlowUsageProjectData.SCRIPT_PATH })
    public void createFlowUsageInUntypedConnectionConnection() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewFlowUsageProjectData.EDITING_CONTEXT_ID,
                GeneralViewFlowUsageProjectData.GraphicalIds.DIAGRAM_ID);

        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);

        var diagramId = new AtomicReference<String>();
        var connectionEdgeId = new AtomicReference<String>();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewFlowUsageProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        String flowCreationToolId = diagramDescriptionIdProvider.getNodeCreationToolIdOnEdge(this.descriptionNameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getConnectionUsage()), "New Flow");

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            var connectionEdge = new DiagramNavigator(diagram).edgeWithId(GeneralViewFlowUsageProjectData.GraphicalIds.CONNECTION_EDGE_ID).getEdge();
            connectionEdgeId.set(connectionEdge.getId());
        });

        Runnable getEdgePalette = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", GeneralViewFlowUsageProjectData.EDITING_CONTEXT_ID,
                    "representationId", diagramId.get(),
                    "diagramElementIds", List.of(connectionEdgeId.get()));
            var result = this.paletteQueryRunner.run(variables);
            Map<String, Object> behaviorSection = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.palette.paletteEntries[1]");
            assertThat(behaviorSection.get("label")).isEqualTo("Behavior");
            List<Object> behaviorTools = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.palette.paletteEntries[1].tools");
            assertThat(behaviorTools).isEmpty();
            List<String> allTools = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.palette.paletteEntries[*].tools[*].id");
            assertThat(allTools).doesNotContain(flowCreationToolId);
        };


        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(getEdgePalette)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private void editLabel(String diagramId, String labelId, String newText) {
        var input = new EditLabelInput(UUID.randomUUID(), GeneralViewFlowUsageProjectData.EDITING_CONTEXT_ID, diagramId, labelId, newText);
        var result = this.editLabelMutationRunner.run(input);
        String typename = JsonPath.read(result.data(), "$.data.editLabel.__typename");
        assertThat(typename).isEqualTo(EditLabelSuccessPayload.class.getSimpleName());
        List<String> messages = JsonPath.read(result.data(), "$.data.editLabel.messages[*].body");
        assertThat(messages).hasSize(0);
    }

    private void assertEdgeLabelText(String connectionEdgeId, Diagram diagram, String expectedLabelText) {
        var edge = new DiagramNavigator(diagram).edgeWithId(connectionEdgeId).getEdge();
        DiagramAssertions.assertThat(edge.getCenterLabel()).hasText(expectedLabelText);
    }

    private void assertConnectionType(IEditingContext editingContext, String connectionElementId, String expectedTypeElementId) {
        var optionalConnection = this.objectSearchService.getObject(editingContext, connectionElementId);
        assertThat(optionalConnection).containsInstanceOf(ConnectionUsage.class);
        ConnectionUsage connection = (ConnectionUsage) optionalConnection.get();
        assertThat(connection.getType()).hasSize(1);
        var connectionType = connection.getType().get(0);
        var connectionTypeId = this.identityService.getId(connectionType);
        assertThat(connectionTypeId).isEqualTo(expectedTypeElementId);
    }
}
