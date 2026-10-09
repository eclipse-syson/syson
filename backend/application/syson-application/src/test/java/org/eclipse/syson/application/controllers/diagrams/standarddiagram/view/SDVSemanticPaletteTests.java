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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.FreeFormLayoutStrategy;
import org.eclipse.sirius.components.diagrams.IconLabelNodeStyle;
import org.eclipse.sirius.components.diagrams.ImageNodeStyle;
import org.eclipse.sirius.components.diagrams.ListLayoutStrategy;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.RectangularNodeStyle;
import org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.AllCustomNodesProjectData;
import org.eclipse.syson.application.data.GeneralViewInheritedPortTestProjectData;
import org.eclipse.syson.application.data.GeneralViewItemAndAttributeProjectData;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.application.data.InterconnectionViewWithTopNodesTestProjectData;
import org.eclipse.syson.application.nodes.SysMLImportedPackageNodeStyle;
import org.eclipse.syson.application.nodes.SysMLNoteNodeStyle;
import org.eclipse.syson.application.nodes.SysMLPackageNodeStyle;
import org.eclipse.syson.application.nodes.SysMLViewFrameNodeStyle;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.standard.diagrams.view.services.api.ISDVSemanticPaletteProvider;
import org.eclipse.syson.standard.diagrams.view.services.api.SDVSemanticPalette;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.metamodel.helper.LabelConstants;
import org.eclipse.syson.tests.api.GivenSysONServer;
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
    @GivenSysONServer({ InterconnectionViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @DisplayName("Given an action inside a part free form compartment, when the diagram is rendered, then the compartment follows the part palette and the action uses its own palette")
    public void givenActionInsidePartFreeFormCompartmentWhenDiagramIsRenderedThenCompartmentFollowsPartPaletteAndActionUsesItsOwnPalette() {
        var flux = this.subscribe(InterconnectionViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, InterconnectionViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> {
                    var part = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npartDiagram");
                    var partPalette = this.palette(SysmlPackage.eINSTANCE.getPartUsage());
                    this.assertRectangularStyle(part.getNode(), partPalette.headerBackground(), partPalette.borderColor());
                    var compartment = part.childNodeWithLabel("action flow").getNode();
                    this.assertRectangularStyle(compartment, partPalette.compartmentBackground(), partPalette.borderColor());
                    assertThat(compartment.getStyle().getChildrenLayoutStrategy()).isInstanceOf(FreeFormLayoutStrategy.class);
                    assertThat(compartment.getChildNodes()).hasSize(1);
                    this.assertNodeAndCompartments(compartment.getChildNodes().get(0), this.palette(SysmlPackage.eINSTANCE.getActionUsage()));
                }))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ GeneralViewItemAndAttributeProjectData.SCRIPT_PATH })
    @DisplayName("Given icon label items and SVG border nodes, when the diagram is rendered, then the customizer colors their compartment and preserves the excluded styles")
    public void givenIconLabelItemsAndSVGBorderNodesWhenDiagramIsRenderedThenCustomizerColorsTheirCompartmentAndPreservesExcludedStyles() {
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
        assertThat(node.getChildNodes()).isNotEmpty().allSatisfy(compartment ->
                this.assertRectangularStyle(compartment, palette.compartmentBackground(), palette.borderColor()));
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
}
