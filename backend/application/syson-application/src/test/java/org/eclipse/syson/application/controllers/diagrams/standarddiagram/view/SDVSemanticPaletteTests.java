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
package org.eclipse.syson.application.controllers.diagrams.standarddiagram.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.FreeFormLayoutStrategy;
import org.eclipse.sirius.components.diagrams.IconLabelNodeStyle;
import org.eclipse.sirius.components.diagrams.ImageNodeStyle;
import org.eclipse.sirius.components.diagrams.ListLayoutStrategy;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.RectangularNodeStyle;
import org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.ActionFlowCompartmentTestProjectData;
import org.eclipse.syson.application.data.AllCustomNodesProjectData;
import org.eclipse.syson.application.data.GeneralViewInheritedPortTestProjectData;
import org.eclipse.syson.application.data.GeneralViewItemAndAttributeProjectData;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.application.nodes.SysMLImportedPackageNodeStyle;
import org.eclipse.syson.application.nodes.SysMLNoteNodeStyle;
import org.eclipse.syson.application.nodes.SysMLPackageNodeStyle;
import org.eclipse.syson.application.nodes.SysMLViewFrameNodeStyle;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.standard.diagrams.view.services.api.ISDVSemanticPaletteProvider;
import org.eclipse.syson.standard.diagrams.view.services.api.SDVSemanticPalette;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortionKind;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.metamodel.helper.LabelConstants;
import org.eclipse.syson.tests.api.GivenSysONServer;
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
 * Tests semantic palettes on rendered standard diagrams.
 *
 * @author gcoutable
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "sirius.web.style.customization.enabled=true")
public class SDVSemanticPaletteTests extends AbstractIntegrationTests {

    private final SDVDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private ISDVSemanticPaletteProvider semanticPaletteProvider;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private ToolTester toolTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private IObjectSearchService objectSearchService;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @DisplayName("Given nodes from seven families, when the diagram is rendered, then definitions usages and compartments receive their family palette")
    public void givenNodesFromSevenFamiliesWhenDiagramIsRenderedThenDefinitionsUsagesAndCompartmentsReceiveTheirFamilyPalette() {
        var flux = this.subscribe(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> this.expectedFamilies().forEach((canonicalType, nodeIds) -> {
                    var palette = this.palette(canonicalType);
                    nodeIds.forEach(nodeId -> this.assertNodeAndCompartments(new DiagramNavigator(diagram).nodeWithId(nodeId).getNode(), palette));
                })))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @DisplayName("Given a description without a palette, when the diagram is rendered, then its original background border and transparent compartments are retained")
    public void givenDescriptionWithoutPaletteWhenDiagramIsRenderedThenItsOriginalBackgroundBorderAndTransparentCompartmentsAreRetained() {
        var flux = this.subscribe(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> {
                    var enumeration = new DiagramNavigator(diagram).nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ENUMERATION_DEFINITION_ID).getNode();
                    this.assertRectangularStyle(enumeration, "#ffffff", "#000000");
                    assertThat(enumeration.getChildNodes()).isNotEmpty().allSatisfy(compartment ->
                        this.assertRectangularStyle(compartment, "transparent", "#000000"));
                }))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ AllCustomNodesProjectData.SCRIPT_PATH })
    @DisplayName("Given custom SysML node styles, when the diagram is rendered, then package imported package note and view frame receive the neutral palette")
    public void givenCustomSysMLNodeStylesWhenDiagramIsRenderedThenPackageImportedPackageNoteAndViewFrameReceiveTheNeutralPalette() {
        var palette = this.palette(SysmlPackage.eINSTANCE.getPackage());
        var flux = this.subscribe(AllCustomNodesProjectData.EDITING_CONTEXT_ID, AllCustomNodesProjectData.GraphicalIds.DIAGRAM_ID);
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> {
                    var navigator = new DiagramNavigator(diagram);
                    this.assertCustomStyle(navigator.nodeWithLabel(AllCustomNodesProjectData.Labels.PACKAGE).getNode(), palette, SysMLPackageNodeStyle.class);
                    this.assertCustomStyle(navigator.nodeWithLabel(AllCustomNodesProjectData.Labels.IMPORTED_PACKAGE).getNode(), palette, SysMLImportedPackageNodeStyle.class);
                    this.assertCustomStyle(navigator.nodeWithLabel(AllCustomNodesProjectData.Labels.NOTE).getNode(), palette, SysMLNoteNodeStyle.class);
                    this.assertCustomStyle(navigator.nodeWithLabel(AllCustomNodesProjectData.Labels.VIEW_FRAME).getNode(), palette, SysMLViewFrameNodeStyle.class);
                }))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ ActionFlowCompartmentTestProjectData.SCRIPT_PATH })
    @DisplayName("Given nested actions in a free form compartment, when the diagram is rendered, then the compartment and nested nodes receive distinct palette backgrounds and SVG controls are preserved")
    public void givenNestedActionsInFreeFormCompartmentWhenDiagramIsRenderedThenCompartmentAndNestedNodesReceiveDistinctPaletteBackgroundsAndSVGControlsArePreserved() {
        var palette = this.palette(SysmlPackage.eINSTANCE.getActionUsage());
        var flux = this.subscribe(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID, ActionFlowCompartmentTestProjectData.GraphicalIds.DIAGRAM_ID);
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> {
                    var navigator = new DiagramNavigator(diagram);
                    var compartment = navigator.nodeWithId(ActionFlowCompartmentTestProjectData.GraphicalIds.ROOT_ACTION_USAGE).childNodeWithLabel("action flow").getNode();
                    this.assertRectangularStyle(compartment, palette.compartmentBackground(), "#000000");
                    assertThat(compartment.getCustomizedStyleProperties()).contains("BORDER_COLOR");
                    assertThat(compartment.getStyle().getChildrenLayoutStrategy()).isInstanceOf(FreeFormLayoutStrategy.class);
                    var nestedAction = navigator.nodeWithId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID).getNode();
                    this.assertRectangularStyle(nestedAction, palette.headerBackground(), palette.borderColor());
                    assertThat(compartment.getChildNodes()).contains(nestedAction);
                    var nestedCompartment = navigator.nodeWithId(nestedAction.getId()).childNodeWithLabel("action flow").getNode();
                    assertThat(nestedCompartment.getCustomizedStyleProperties()).doesNotContain("BORDER_COLOR");
                    this.assertRectangularStyle(nestedCompartment, palette.compartmentBackground(), palette.borderColor());
                    assertThat(nestedCompartment.getStyle().getChildrenLayoutStrategy()).isInstanceOf(FreeFormLayoutStrategy.class);
                    assertThat(navigator.nodeWithId(ActionFlowCompartmentTestProjectData.GraphicalIds.START_ID).getNode().getStyle()).isInstanceOf(ImageNodeStyle.class);
                    assertThat(navigator.nodeWithId(ActionFlowCompartmentTestProjectData.GraphicalIds.DONE_ID).getNode().getStyle()).isInstanceOf(ImageNodeStyle.class);
                }))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @DisplayName("Given a part node, when an action is created in its free form compartment, then the part retains structure colors and the nested action and its compartments use behavior colors")
    public void givenPartNodeWhenActionIsCreatedInItsFreeFormCompartmentThenPartRetainsStructureColorsAndNestedActionAndItsCompartmentsUseBehaviorColors() {
        var flux = this.subscribe(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        var diagram = new AtomicReference<Diagram>();
        var description = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var ids = new DiagramDescriptionIdProvider(description, this.diagramIdProvider);
        var toolId = ids.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Action");
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram::set))
                .then(() -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID, toolId))
                .consumeNextWith(assertRefreshedDiagramThat(updated -> {
                    var part = new DiagramNavigator(updated).nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID);
                    this.assertNodeAndCompartments(part.getNode(), this.palette(SysmlPackage.eINSTANCE.getPartUsage()));
                    var compartment = part.childNodeWithLabel("action flow").getNode();
                    assertThat(compartment.getStyle().getChildrenLayoutStrategy()).isInstanceOf(FreeFormLayoutStrategy.class);
                    assertThat(compartment.getChildNodes()).hasSize(1);
                    this.assertNodeAndCompartments(compartment.getChildNodes().get(0), this.palette(SysmlPackage.eINSTANCE.getActionUsage()));
                }))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ GeneralViewItemAndAttributeProjectData.SCRIPT_PATH })
    @DisplayName("Given list items and border parameters, when the diagram is rendered, then list items are transparent and border SVG styles retain their original appearance")
    public void givenListItemsAndBorderParametersWhenDiagramIsRenderedThenListItemsAreTransparentAndBorderSVGStylesRetainTheirOriginalAppearance() {
        var flux = this.subscribe(GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID, GeneralViewItemAndAttributeProjectData.GraphicalIds.DIAGRAM_ID);
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> {
                    var navigator = new DiagramNavigator(diagram);
                    var item = navigator.nodeWithId(GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_ICON_AND_LABEL_ID).getNode();
                    assertThat(item.getStyle()).isInstanceOf(IconLabelNodeStyle.class);
                    DiagramAssertions.assertThat((IconLabelNodeStyle) item.getStyle()).hasBackgroundColor("transparent");
                    var compartment = navigator.nodeWithId(item.getId()).parentNode().getNode();
                    var palette = this.palette(SysmlPackage.eINSTANCE.getActionUsage());
                    this.assertRectangularStyle(compartment, palette.compartmentBackground(), palette.borderColor());
                    assertThat(compartment.getStyle().getChildrenLayoutStrategy()).isInstanceOf(ListLayoutStrategy.class);
                    var borderNode = navigator.nodeWithId(GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_BORDERED_NODE_ID).getNode();
                    DiagramAssertions.assertThat(borderNode).isBorderNode();
                    assertThat(borderNode.getStyle()).isInstanceOf(ImageNodeStyle.class);
                    var style = (ImageNodeStyle) borderNode.getStyle();
                    assertThat(style.getImageURL()).isEqualTo("/images/feature_in.svg");
                    assertThat(style.getBorderColor()).isEqualTo("#000000");
                }))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ GeneralViewInheritedPortTestProjectData.SCRIPT_PATH })
    @DisplayName("Given an ordinary port border node, when the diagram is rendered, then its original background and border are preserved")
    public void givenOrdinaryPortBorderNodeWhenDiagramIsRenderedThenItsOriginalBackgroundAndBorderArePreserved() {
        var flux = this.subscribe(GeneralViewInheritedPortTestProjectData.EDITING_CONTEXT_ID, GeneralViewInheritedPortTestProjectData.GraphicalIds.DIAGRAM_ID);
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> {
                    var part = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart2").getNode();
                    this.assertRectangularStyle(part, this.palette(SysmlPackage.eINSTANCE.getPartUsage()).headerBackground(), this.palette(SysmlPackage.eINSTANCE.getPartUsage()).borderColor());
                    assertThat(part.getBorderNodes()).hasSize(1);
                    var borderNode = part.getBorderNodes().get(0);
                    DiagramAssertions.assertThat(borderNode).isBorderNode();
                    this.assertRectangularStyle(borderNode, "#ffffff", "#000000");
                }))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @DisplayName("Given an action node, when fork and join controls are created, then both controls receive the behavior palette while retaining their dimensions")
    public void givenActionNodeWhenForkAndJoinControlsAreCreatedThenBothControlsReceiveTheBehaviorPaletteWhileRetainingTheirDimensions() {
        var flux = this.subscribe(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        var diagram = new AtomicReference<Diagram>();
        var description = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var ids = new DiagramDescriptionIdProvider(description, this.diagramIdProvider);
        var actionDescription = this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage());
        var forkTool = ids.getNodeToolId(actionDescription, "New Fork");
        var joinTool = ids.getNodeToolId(actionDescription, "New Join");
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram::set))
                .then(() -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID, forkTool))
                .consumeNextWith(assertRefreshedDiagramThat(updated -> {
                    diagram.set(updated);
                    this.assertBehaviorControl(updated, "forkNode1");
                }))
                .then(() -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID, joinTool))
                .consumeNextWith(assertRefreshedDiagramThat(updated -> this.assertBehaviorControl(updated, "joinNode1")))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @DisplayName("Given a typed part with an inherited attribute, when its diagram is refreshed, then inherited items are transparent and their compartment uses the owner palette")
    public void givenTypedPartWithInheritedAttributeWhenDiagramIsRefreshedThenInheritedItemsAreTransparentAndTheirCompartmentUsesOwnerPalette() {
        var flux = this.subscribe(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> assertThat(diagram.getNodes()).isNotEmpty()))
                .then(this.prepareInheritance())
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> this.assertInheritedAttribute(diagram)))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @DisplayName("Given a part node, when its portion kind becomes snapshot then timeslice, then it retains the structure palette")
    public void givenPartNodeWhenItsPortionKindBecomesSnapshotThenTimesliceThenItRetainsTheStructurePalette() {
        var flux = this.subscribe(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(this::assertPartPalette))
                .then(this.setPartPortionKind(PortionKind.SNAPSHOT))
                .consumeNextWith(assertRefreshedDiagramThat(this::assertPartPalette))
                .then(this.setPartPortionKind(PortionKind.TIMESLICE))
                .consumeNextWith(assertRefreshedDiagramThat(this::assertPartPalette))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @DisplayName("Given palette descriptions, when owner prefixes and unknown descriptions are resolved, then only exact names and delimited compartments receive a palette")
    public void givenPaletteDescriptionsWhenOwnerPrefixesAndUnknownDescriptionsAreResolvedThenOnlyExactNamesAndDelimitedCompartmentsReceiveAPalette() {
        var type = SysmlPackage.eINSTANCE.getPartUsage();
        var reference = SysmlPackage.eINSTANCE.getUsage_NestedAttribute();
        var palette = this.palette(type);
        for (var name : List.of(this.descriptionNameGenerator.getCompartmentName(type, reference), this.descriptionNameGenerator.getCompartmentItemName(type, reference),
                this.descriptionNameGenerator.getInheritedCompartmentItemName(type, reference), this.descriptionNameGenerator.getFreeFormCompartmentName(type, reference))) {
            assertThat(this.semanticPaletteProvider.findPalette(name)).as(name).contains(palette);
        }
        for (var name : List.of("Other Node PartUsage", "GV Node PartUsageOther", "GV Compartment PartUsageOther nestedAttribute", "Unknown")) {
            assertThat(this.semanticPaletteProvider.findPalette(name)).as(name).isEmpty();
        }
    }

    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @DisplayName("Given descriptions with specialized semantic roles, when their palette is resolved, then variants use the explicitly assigned family")
    public void givenDescriptionsWithSpecializedSemanticRolesWhenTheirPaletteIsResolvedThenVariantsUseTheExplicitlyAssignedFamily() {
        var sysml = SysmlPackage.eINSTANCE;
        var expectedVariants = Map.of(
                sysml.getPartUsage(), List.of(sysml.getReferenceUsage(), sysml.getConnectionDefinition(), sysml.getConnectionUsage()),
                sysml.getActionUsage(), List.of(sysml.getAcceptActionUsage(), sysml.getPerformActionUsage(), sysml.getSendActionUsage(),
                        sysml.getIfActionUsage(), sysml.getForLoopActionUsage(), sysml.getWhileLoopActionUsage(), sysml.getFlowUsage(), sysml.getTransitionUsage()),
                sysml.getStateUsage(), List.of(sysml.getExhibitStateUsage()),
                sysml.getRequirementUsage(), List.of(sysml.getAssertConstraintUsage(), sysml.getConstraintDefinition()),
                sysml.getCaseUsage(), List.of(sysml.getUseCaseDefinition(), sysml.getUseCaseUsage(), sysml.getAnalysisCaseUsage(), sysml.getVerificationCaseUsage(), sysml.getCalculationUsage()),
                sysml.getOccurrenceUsage(), List.of(sysml.getEventOccurrenceUsage()),
                sysml.getPackage(), List.of(sysml.getNamespaceImport(), sysml.getViewDefinition(), sysml.getViewUsage(), sysml.getViewpointUsage(), sysml.getMetadataUsage(), sysml.getComment(), sysml.getDocumentation()));
        expectedVariants.forEach((canonicalType, variants) -> variants.forEach(type -> {
            var descriptionName = this.descriptionNameGenerator.getNodeName(type);
            assertThat(this.semanticPaletteProvider.findPalette(descriptionName)).as(descriptionName).contains(this.palette(canonicalType));
        }));
    }

    private Flux<DiagramRefreshedEventPayload> subscribe(String editingContextId, String diagramId) {
        return this.givenDiagramSubscription.subscribe(new DiagramEventInput(UUID.randomUUID(), editingContextId, diagramId));
    }

    private SDVSemanticPalette palette(EClass canonicalType) {
        return this.semanticPaletteProvider.findPalette(this.descriptionNameGenerator.getNodeName(canonicalType)).orElseThrow();
    }

    private Map<EClass, List<String>> expectedFamilies() {
        return Map.of(
                SysmlPackage.eINSTANCE.getPartUsage(), List.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_DEFINITION_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID,
                        GeneralViewWithTopNodesTestProjectData.GraphicalIds.ITEM_DEFINITION_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.ITEM_USAGE_ID,
                        GeneralViewWithTopNodesTestProjectData.GraphicalIds.PORT_DEFINITION_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.PORT_USAGE_ID,
                        GeneralViewWithTopNodesTestProjectData.GraphicalIds.INTERFACE_DEFINITION_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.INTERFACE_USAGE_ID),
                SysmlPackage.eINSTANCE.getActionUsage(), List.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_DEFINITION_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID,
                        GeneralViewWithTopNodesTestProjectData.GraphicalIds.ALLOCATION_DEFINITION_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.ALLOCATION_USAGE_ID),
                SysmlPackage.eINSTANCE.getStateUsage(), List.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_DEFINITION_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_USAGE_ID),
                SysmlPackage.eINSTANCE.getRequirementUsage(), List.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_DEFINITION_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_USAGE_ID,
                        GeneralViewWithTopNodesTestProjectData.GraphicalIds.CONCERN_DEFINITION_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.CONCERN_USAGE_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.CONSTRAINT_USAGE_ID,
                        GeneralViewWithTopNodesTestProjectData.GraphicalIds.SATISFY_REQUIREMENT_USAGE_ID),
                SysmlPackage.eINSTANCE.getCaseUsage(), List.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.CASE_DEFINITION_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.CASE_USAGE_ID),
                SysmlPackage.eINSTANCE.getOccurrenceUsage(), List.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.OCCURRENCE_DEFINITION_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.OCCURRENCE_USAGE_ID),
                SysmlPackage.eINSTANCE.getPackage(), List.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.METADATA_DEFINITION_ID));
    }

    private void assertNodeAndCompartments(Node node, SDVSemanticPalette palette) {
        DiagramAssertions.assertThat(node).isNotBorderNode();
        this.assertRectangularStyle(node, palette.headerBackground(), palette.borderColor());
        assertThat(node.getChildNodes()).isNotEmpty().allSatisfy(compartment -> {
            this.assertRectangularStyle(compartment, palette.compartmentBackground(), palette.borderColor());
            compartment.getChildNodes().stream().filter(item -> item.getStyle() instanceof IconLabelNodeStyle)
                    .forEach(item -> DiagramAssertions.assertThat((IconLabelNodeStyle) item.getStyle()).hasBackgroundColor("transparent"));
        });
    }

    private void assertRectangularStyle(Node node, String background, String borderColor) {
        assertThat(node.getStyle()).as("Style of node %s", node).isInstanceOf(RectangularNodeStyle.class);
        DiagramAssertions.assertThat((RectangularNodeStyle) node.getStyle()).as("Style of node %s", node).hasBackground(background).hasBorderColor(borderColor);
    }

    private void assertCustomStyle(Node node, SDVSemanticPalette palette, Class<?> styleType) {
        assertThat(node.getStyle()).isInstanceOf(styleType);
        var background = switch (node.getStyle()) {
            case SysMLPackageNodeStyle style -> style.getBackground();
            case SysMLImportedPackageNodeStyle style -> style.getBackground();
            case SysMLNoteNodeStyle style -> style.getBackground();
            case SysMLViewFrameNodeStyle style -> style.getBackground();
            default -> throw new IllegalArgumentException("Unexpected custom style");
        };
        var borderColor = switch (node.getStyle()) {
            case SysMLPackageNodeStyle style -> style.getBorderColor();
            case SysMLImportedPackageNodeStyle style -> style.getBorderColor();
            case SysMLNoteNodeStyle style -> style.getBorderColor();
            case SysMLViewFrameNodeStyle style -> style.getBorderColor();
            default -> throw new IllegalArgumentException("Unexpected custom style");
        };
        assertThat(background).isEqualTo(palette.headerBackground());
        assertThat(borderColor).isEqualTo(palette.borderColor());
    }

    private void assertBehaviorControl(Diagram diagram, String label) {
        var action = new DiagramNavigator(diagram).nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID);
        var controls = action.childNodeWithLabel("action flow").getNode().getChildNodes();
        var control = controls.stream().filter(node -> node.getOutsideLabels().stream().anyMatch(outsideLabel -> label.equals(outsideLabel.text()))).findFirst().orElseThrow();
        var palette = this.palette(SysmlPackage.eINSTANCE.getActionUsage());
        this.assertRectangularStyle(control, palette.headerBackground(), palette.borderColor());
        DiagramAssertions.assertThat((RectangularNodeStyle) control.getStyle()).hasBorderSize(0).hasBorderRadius(0);
    }

    private void assertInheritedAttribute(Diagram diagram) {
        var part = new DiagramNavigator(diagram).nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID);
        var compartment = part.childNodeWithLabel("attributes").getNode();
        var inheritedItems = compartment.getChildNodes().stream().filter(node -> node.getInsideLabel().getText().contains("^paletteAttribute")).toList();
        assertThat(inheritedItems).hasSize(1);
        assertThat(inheritedItems.get(0).getStyle()).isInstanceOf(IconLabelNodeStyle.class);
        DiagramAssertions.assertThat((IconLabelNodeStyle) inheritedItems.get(0).getStyle()).hasBackgroundColor("transparent");
        var palette = this.palette(SysmlPackage.eINSTANCE.getPartUsage());
        this.assertRectangularStyle(compartment, palette.compartmentBackground(), palette.borderColor());
    }

    private Runnable prepareInheritance() {
        return this.semanticRunnableFactory.createMutationRunnable(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, (context, input) -> {
            var definition = (PartDefinition) this.objectSearchService.getObject(context, GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_DEFINITION_ID).orElseThrow();
            var part = (PartUsage) this.objectSearchService.getObject(context, GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID).orElseThrow();
            var attribute = SysmlFactory.eINSTANCE.createAttributeUsage();
            attribute.setDeclaredName("paletteAttribute");
            var membership = SysmlFactory.eINSTANCE.createFeatureMembership();
            membership.getOwnedRelatedElement().add(attribute);
            definition.getOwnedRelationship().add(membership);
            var typing = SysmlFactory.eINSTANCE.createFeatureTyping();
            typing.setType(definition);
            typing.setTypedFeature(part);
            part.getOwnedRelationship().add(typing);
            assertThat(part.getPartDefinition()).contains(definition);
            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), true);
        });
    }

    private Runnable setPartPortionKind(PortionKind portionKind) {
        return this.semanticRunnableFactory.createMutationRunnable(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, (context, input) -> {
            var part = (PartUsage) this.objectSearchService.getObject(context, GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID).orElseThrow();
            part.setPortionKind(portionKind);
            assertThat(part.getPortionKind()).isEqualTo(portionKind);
            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), true);
        });
    }

    private void assertPartPalette(Diagram diagram) {
        var part = new DiagramNavigator(diagram).nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID).getNode();
        this.assertNodeAndCompartments(part, this.palette(SysmlPackage.eINSTANCE.getPartUsage()));
    }
}
