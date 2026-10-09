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
import static org.assertj.core.api.Assertions.fail;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.HideDiagramElementInput;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.FreeFormLayoutStrategy;
import org.eclipse.sirius.components.diagrams.IconLabelNodeStyle;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.RectangularNodeStyle;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions;
import org.eclipse.sirius.components.diagrams.tests.graphql.HideDiagramElementMutationRunner;
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
import org.eclipse.syson.standard.diagrams.view.services.api.ISDVThemeProvider;
import org.eclipse.syson.standard.diagrams.view.services.api.SDVTheme;
import org.eclipse.syson.sysml.SysmlPackage;
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
 * Tests themes on rendered standard diagrams.
 *
 * @author gcoutable
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SDVThemeTests extends AbstractIntegrationTests {

    private final SDVDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private ISDVThemeProvider themeProvider;

    @Autowired
    private HideDiagramElementMutationRunner hideDiagramElementMutationRunner;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    private Flux<DiagramRefreshedEventPayload> givenDiagramSubscription(String editingContextId, String diagramId) {
        return this.givenDiagramSubscription.subscribe(new DiagramEventInput(UUID.randomUUID(), editingContextId, diagramId));
    }

    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN nodes from different families, WHEN the diagram is rendered, THEN definitions, usages, and compartments receive their family theme colors")
    public void givenNodesFromSevenFamiliesWhenDiagramIsRenderedThenDefinitionsUsagesAndCompartmentsReceiveTheirFamilyThemeColors() {
        var flux = this.givenDiagramSubscription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        Consumer<Object> initialDiagramRefresh = assertRefreshedDiagramThat(diagram -> this.expectedFamilies().forEach((canonicalType, nodeIds) -> {
            var theme = this.theme(canonicalType);
            nodeIds.forEach(nodeId -> this.assertNodeAndCompartments(new DiagramNavigator(diagram).nodeWithId(nodeId).getNode(), theme));
        }));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramRefresh)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN a description without a theme, WHEN the diagram is rendered, THEN its original background color and border color and transparent compartments are retained")
    public void givenDescriptionWithoutThemeWhenDiagramIsRenderedThenItsOriginalBackgroundColorAndBorderColorAndTransparentCompartmentsAreRetained() {
        var flux = this.givenDiagramSubscription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        Consumer<Object> initialDiagramRefresh = assertRefreshedDiagramThat(diagram -> {
            var enumeration = new DiagramNavigator(diagram).nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ENUMERATION_DEFINITION_ID).getNode();
            this.assertRectangularStyle(enumeration, "#ffffff", "#000000");
            assertThat(enumeration.getChildNodes()).isNotEmpty().allSatisfy(compartment ->
                    this.assertRectangularStyle(compartment, "transparent", "#000000"));
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramRefresh)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ AllCustomNodesProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN custom SysML node styles, WHEN the diagram is rendered, THEN package, imported package, note, and view frame receive the neutral theme")
    public void givenCustomSysMLNodeStylesWhenDiagramIsRenderedThenPackageImportedPackageNoteAndViewFrameReceiveTheNeutralTheme() {
        var customNodeContainingElementTheme = this.theme(SysmlPackage.eINSTANCE.getPackage());
        var customEmptyNodeTheme = this.theme(SysmlPackage.eINSTANCE.getNamespaceImport());
        var flux = this.givenDiagramSubscription(AllCustomNodesProjectData.EDITING_CONTEXT_ID, AllCustomNodesProjectData.GraphicalIds.DIAGRAM_ID);
        Consumer<Object> initialDiagramRefresh = assertRefreshedDiagramThat(diagram -> {
            var navigator = new DiagramNavigator(diagram);
            this.assertCustomStyle(navigator.nodeWithLabel(AllCustomNodesProjectData.Labels.PACKAGE).getNode(), customNodeContainingElementTheme, SysMLPackageNodeStyle.class);
            this.assertCustomStyle(navigator.nodeWithLabel(AllCustomNodesProjectData.Labels.VIEW_FRAME).getNode(), customNodeContainingElementTheme, SysMLViewFrameNodeStyle.class);
            this.assertCustomStyle(navigator.nodeWithLabel(AllCustomNodesProjectData.Labels.IMPORTED_PACKAGE).getNode(), customEmptyNodeTheme, SysMLImportedPackageNodeStyle.class);
            this.assertCustomStyle(navigator.nodeWithLabel(AllCustomNodesProjectData.Labels.NOTE).getNode(), customEmptyNodeTheme, SysMLNoteNodeStyle.class);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramRefresh)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ InterconnectionViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN an action inside a part free form compartment, WHEN the diagram is rendered, THEN the compartment follows the part theme and the action uses its own theme")
    public void givenActionInsidePartFreeFormCompartmentWhenDiagramIsRenderedThenCompartmentFollowsPartThemeAndActionUsesItsOwnTheme() {
        var flux = this.givenDiagramSubscription(InterconnectionViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, InterconnectionViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        Consumer<Object> initialDiagramRefresh = assertRefreshedDiagramThat(diagram -> {
            var part = new DiagramNavigator(diagram).nodeWithId(InterconnectionViewWithTopNodesTestProjectData.GraphicalIds.PART_DIAGRAM_ID);
            var partTheme = this.theme(SysmlPackage.eINSTANCE.getPartUsage());
            this.assertRectangularStyle(part.getNode(), partTheme.nodeBackgroundColor(), partTheme.borderColor());
            var compartment = part.childNodeWithLabel("action flow").getNode();
            this.assertRectangularStyle(compartment, partTheme.compartmentBackgroundColor(), partTheme.borderColor());
            assertThat(compartment.getStyle().getChildrenLayoutStrategy()).isInstanceOf(FreeFormLayoutStrategy.class);
            assertThat(compartment.getChildNodes()).hasSize(1);
            this.assertNodeAndCompartments(compartment.getChildNodes().get(0), this.theme(SysmlPackage.eINSTANCE.getActionUsage()));
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramRefresh)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ GeneralViewItemAndAttributeProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN an icon label item, WHEN the diagram is rendered, THEN the icon label style has not changed")
    public void givenIconLabelItemWhenDiagramIsRenderedThenIconLabelStyleHasNotChanged() {
        var flux = this.givenDiagramSubscription(GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID, GeneralViewItemAndAttributeProjectData.GraphicalIds.DIAGRAM_ID);
        Consumer<Object> initialDiagramRefresh = assertRefreshedDiagramThat(diagram -> {
            var navigator = new DiagramNavigator(diagram);
            var item = navigator.nodeWithId(GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_ICON_AND_LABEL_ID).getNode();
            assertThat(item.getStyle()).isInstanceOf(IconLabelNodeStyle.class);
            DiagramAssertions.assertThat((IconLabelNodeStyle) item.getStyle()).hasBackgroundColor("transparent");
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramRefresh)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ GeneralViewInheritedPortTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN a port border node without direction, WHEN the diagram is rendered, THEN its original background color and border color are preserved")
    public void givenPortBorderNodeWithoutDirectionWhenDiagramIsRenderedThenItsOriginalBackgroundColorAndBorderColorArePreserved() {
        var flux = this.givenDiagramSubscription(GeneralViewInheritedPortTestProjectData.EDITING_CONTEXT_ID, GeneralViewInheritedPortTestProjectData.GraphicalIds.DIAGRAM_ID);
        Consumer<Object> initialDiagramRefresh = assertRefreshedDiagramThat(diagram -> {
            var part = new DiagramNavigator(diagram).nodeWithId(GeneralViewInheritedPortTestProjectData.GraphicalIds.PART_2_ID).getNode();
            this.assertRectangularStyle(part, this.theme(SysmlPackage.eINSTANCE.getPartUsage()).nodeBackgroundColor(), this.theme(SysmlPackage.eINSTANCE.getPartUsage()).borderColor());
            assertThat(part.getBorderNodes()).hasSize(1);
            var borderNode = part.getBorderNodes().get(0);
            DiagramAssertions.assertThat(borderNode).isBorderNode();
            this.assertRectangularStyle(borderNode, "#ffffff", "#000000");
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramRefresh)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN two compartments sharing a description on two different families, WHEN the compartments are revealed, THEN their background colors follow their family theme")
    public void givenTwoCompartmentSharingDescriptionOnTwoDifferentFamiliesWhenCompartmentsAreRevealedThenCompartmentBackgroundColorsFollowTheirFamilyTheme() {
        var flux = this.givenDiagramSubscription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        var compartmentIds = new AtomicReference<Set<String>>();
        var partTheme = this.theme(SysmlPackage.eINSTANCE.getPartDefinition());
        var allocationTheme = this.theme(SysmlPackage.eINSTANCE.getAllocationDefinition());
        Consumer<Object> initialDiagramRefresh = assertRefreshedDiagramThat(diagram -> {
            var navigator = new DiagramNavigator(diagram);
            var partCompartment = navigator.nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_DEFINITION_ID).childNodeWithLabel("perform actions").getNode();
            var allocationCompartment = navigator.nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ALLOCATION_DEFINITION_ID).childNodeWithLabel("perform actions").getNode();

            assertThat(allocationCompartment.getDescriptionId()).isEqualTo(partCompartment.getDescriptionId());
            compartmentIds.set(Set.of(partCompartment.getId(), allocationCompartment.getId()));
        });

        Runnable revealCompartments = () -> {
            var input = new HideDiagramElementInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                    GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID, compartmentIds.get(), false);
            var result = this.hideDiagramElementMutationRunner.run(input);
            assertThat(result.errors()).isEmpty();
            String typename = JsonPath.read(result.data(), "$.data.hideDiagramElement.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramRefresh = assertRefreshedDiagramThat(diagram -> {
            var navigator = new DiagramNavigator(diagram);
            var partCompartment = navigator.nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_DEFINITION_ID).childNodeWithLabel("perform actions").getNode();
            var allocationCompartment = navigator.nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ALLOCATION_DEFINITION_ID).childNodeWithLabel("perform actions").getNode();

            DiagramAssertions.assertThat(partCompartment).hasState(ViewModifier.Normal);
            DiagramAssertions.assertThat(allocationCompartment).hasState(ViewModifier.Normal);
            DiagramAssertions.assertThat((RectangularNodeStyle) partCompartment.getStyle()).hasBackground(partTheme.compartmentBackgroundColor());
            DiagramAssertions.assertThat((RectangularNodeStyle) allocationCompartment.getStyle()).hasBackground(allocationTheme.compartmentBackgroundColor());
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramRefresh)
                .then(revealCompartments)
                .consumeNextWith(updatedDiagramRefresh)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private SDVTheme theme(EClass canonicalType) {
        return this.themeProvider.findTheme(this.descriptionNameGenerator.getNodeName(canonicalType)).orElseThrow();
    }

    private Map<EClass, List<String>> expectedFamilies() {
        // Use the EClass of an element of a family to identify a family.
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
                SysmlPackage.eINSTANCE.getMetadataDefinition(), List.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.METADATA_DEFINITION_ID));
    }

    private void assertNodeAndCompartments(Node node, SDVTheme theme) {
        DiagramAssertions.assertThat(node).isNotBorderNode();
        this.assertRectangularStyle(node, theme.nodeBackgroundColor(), theme.borderColor());
        assertThat(node.getChildNodes())
                .isNotEmpty()
                .allSatisfy(compartment -> this.assertRectangularStyle(compartment, theme.compartmentBackgroundColor(), theme.borderColor()));
    }

    private void assertRectangularStyle(Node node, String backgroundColor, String borderColor) {
        assertThat(node.getStyle()).as("Style of node %s", node).isInstanceOf(RectangularNodeStyle.class);
        DiagramAssertions.assertThat((RectangularNodeStyle) node.getStyle()).as("Style of node %s", node)
                .hasBackground(backgroundColor)
                .hasBorderColor(borderColor);
    }

    private void assertCustomStyle(Node node, SDVTheme theme, Class<?> styleType) {
        assertThat(node.getStyle()).isInstanceOf(styleType);
        var backgroundColor = switch (node.getStyle()) {
            case SysMLPackageNodeStyle style -> style.getBackground();
            case SysMLImportedPackageNodeStyle style -> style.getBackground();
            case SysMLNoteNodeStyle style -> style.getBackground();
            case SysMLViewFrameNodeStyle style -> style.getBackground();
            default -> fail("Unexpected custom style");
        };
        var borderColor = switch (node.getStyle()) {
            case SysMLPackageNodeStyle style -> style.getBorderColor();
            case SysMLImportedPackageNodeStyle style -> style.getBorderColor();
            case SysMLNoteNodeStyle style -> style.getBorderColor();
            case SysMLViewFrameNodeStyle style -> style.getBorderColor();
            default -> fail("Unexpected custom style");
        };
        assertThat(backgroundColor).isEqualTo(theme.nodeBackgroundColor());
        assertThat(borderColor).isEqualTo(theme.borderColor());
    }
}
