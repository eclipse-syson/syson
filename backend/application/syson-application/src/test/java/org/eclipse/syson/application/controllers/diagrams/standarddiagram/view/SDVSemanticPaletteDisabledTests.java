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
import java.util.UUID;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.diagrams.RectangularNodeStyle;
import org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.AllCustomNodesProjectData;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.application.nodes.SysMLPackageNodeStyle;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Tests that disabling semantic styling preserves the standard appearance.
 *
 * @author gcoutable
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "sirius.web.style.customization.enabled=false")
public class SDVSemanticPaletteDisabledTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @DisplayName("Given disabled style customization, when a part diagram is rendered, then node and compartment styles retain their original colors")
    public void givenDisabledStyleCustomizationWhenPartDiagramIsRenderedThenNodeAndCompartmentStylesRetainTheirOriginalColors() {
        var flux = this.givenDiagramSubscription.subscribe(new DiagramEventInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID));
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> {
                    var part = new DiagramNavigator(diagram).nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID).getNode();
                    DiagramAssertions.assertThat((RectangularNodeStyle) part.getStyle()).hasBackground("#ffffff").hasBorderColor("#000000");
                    assertThat(part.getChildNodes()).isNotEmpty().allSatisfy(compartment ->
                        DiagramAssertions.assertThat((RectangularNodeStyle) compartment.getStyle()).hasBackground("transparent"));
                }))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ AllCustomNodesProjectData.SCRIPT_PATH })
    @DisplayName("Given disabled style customization, when a custom package node is rendered, then its original background and border are retained")
    public void givenDisabledStyleCustomizationWhenCustomPackageNodeIsRenderedThenItsOriginalBackgroundAndBorderAreRetained() {
        var flux = this.givenDiagramSubscription.subscribe(new DiagramEventInput(UUID.randomUUID(), AllCustomNodesProjectData.EDITING_CONTEXT_ID, AllCustomNodesProjectData.GraphicalIds.DIAGRAM_ID));
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> {
                    var style = new DiagramNavigator(diagram).nodeWithLabel(AllCustomNodesProjectData.Labels.PACKAGE).getNode().getStyle();
                    assertThat(style).isInstanceOf(SysMLPackageNodeStyle.class);
                    assertThat(((SysMLPackageNodeStyle) style).getBackground()).isEqualTo("#ffffff");
                    assertThat(((SysMLPackageNodeStyle) style).getBorderColor()).isEqualTo("#000000");
                }))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
