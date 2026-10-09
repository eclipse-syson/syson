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

import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.RectangularNodeAppearanceInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ResetNodeAppearanceInput;
import org.eclipse.sirius.components.diagrams.CollapsingState;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.LineStyle;
import org.eclipse.sirius.components.diagrams.RectangularNodeStyle;
import org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions;
import org.eclipse.sirius.components.diagrams.tests.graphql.EditRectangularNodeAppearanceExecutor;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.graphql.ResetNodeAppearanceExecutor;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.standard.diagrams.view.services.api.ISDVSemanticPaletteProvider;
import org.eclipse.syson.standard.diagrams.view.services.api.SDVSemanticPalette;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Tests semantic palette compatibility with user appearance and collapsed nodes.
 *
 * @author gcoutable
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "sirius.web.style.customization.enabled=true")
public class SDVSemanticPaletteAppearanceTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private ISDVSemanticPaletteProvider semanticPaletteProvider;

    @Autowired
    private EditRectangularNodeAppearanceExecutor editRectangularNodeAppearanceExecutor;

    @Autowired
    private ResetNodeAppearanceExecutor resetNodeAppearanceExecutor;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @DisplayName("Given a semantic palette, when node appearance is customized manually and reset, then manual colors override the palette and reset restores it")
    public void givenSemanticPaletteWhenNodeAppearanceIsCustomizedManuallyAndResetThenManualColorsOverrideThePaletteAndResetRestoresIt() {
        var flux = this.givenDiagramSubscription.subscribe(new DiagramEventInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID));
        var initialDiagram = new AtomicReference<Diagram>();
        var palette = this.semanticPaletteProvider.findPalette(new SDVDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPartUsage())).orElseThrow();
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> {
                    initialDiagram.set(diagram);
                    this.assertPartPalette(diagram, palette);
                }))
                .then(() -> this.editRectangularNodeAppearanceExecutor.execute(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, initialDiagram.get().getId(),
                        List.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID), new RectangularNodeAppearanceInput("red", "blue", 8, 3, LineStyle.Dash)).isSuccess())
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> this.assertManualPartAppearance(diagram, palette)))
                .then(() -> this.resetNodeAppearanceExecutor.execute(new ResetNodeAppearanceInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                        initialDiagram.get().getId(), List.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID),
                        List.of("BACKGROUND", "BORDER_COLOR", "BORDER_RADIUS", "BORDER_SIZE", "BORDER_STYLE"))).isSuccess())
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> this.assertPartPalette(diagram, palette)))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH, "/scripts/database-content/SemanticPaletteCollapsedPart.sql" })
    @DisplayName("Given a collapsed part node, when the diagram is rendered, then the visible node retains its family header background")
    public void givenCollapsedPartNodeWhenDiagramIsRenderedThenVisibleNodeRetainsItsFamilyHeaderBackground() {
        var flux = this.givenDiagramSubscription.subscribe(new DiagramEventInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID));
        var palette = this.semanticPaletteProvider.findPalette(new SDVDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPartUsage())).orElseThrow();
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> {
                    var part = new DiagramNavigator(diagram).nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID).getNode();
                    DiagramAssertions.assertThat(part).hasCollapsingState(CollapsingState.COLLAPSED);
                    DiagramAssertions.assertThat((RectangularNodeStyle) part.getStyle()).hasBackground(palette.headerBackground()).hasBorderColor(palette.borderColor());
                }))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private void assertPartPalette(Diagram diagram, SDVSemanticPalette palette) {
        var part = new DiagramNavigator(diagram).nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID).getNode();
        DiagramAssertions.assertThat((RectangularNodeStyle) part.getStyle()).hasBackground(palette.headerBackground()).hasBorderColor(palette.borderColor());
    }

    private void assertManualPartAppearance(Diagram diagram, SDVSemanticPalette palette) {
        var part = new DiagramNavigator(diagram).nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID).getNode();
        DiagramAssertions.assertThat((RectangularNodeStyle) part.getStyle()).hasBackground("red").hasBorderColor("blue").hasBorderRadius(8).hasBorderSize(3).hasBorderStyle(LineStyle.Dash);
        var compartment = part.getChildNodes().get(0);
        DiagramAssertions.assertThat((RectangularNodeStyle) compartment.getStyle()).hasBackground(palette.compartmentBackground()).hasBorderColor(palette.borderColor());
    }
}
