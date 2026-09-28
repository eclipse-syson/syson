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
package org.eclipse.syson.standard.diagrams.view.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.eclipse.sirius.components.collaborative.diagrams.dto.SingleClickOnTwoDiagramElementsCandidate;
import org.eclipse.sirius.components.collaborative.diagrams.dto.SingleClickOnTwoDiagramElementsTool;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.palette.dto.ITool;
import org.eclipse.sirius.components.palette.dto.Palette;
import org.eclipse.sirius.components.palette.dto.PaletteDivider;
import org.eclipse.sirius.components.palette.dto.ToolSection;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.Test;

/**
 * Tests the diagram and element scope of {@link SDVConnectorPaletteTargetFilter}.
 *
 * @author arichard
 */
public class SDVConnectorPaletteTargetFilterTest {

    /**
     * Verifies that only connectors between standard view nodes are filtered.
     */
    @Test
    void onlyHandlesStandardViewNodes() {
        var filter = new SDVConnectorPaletteTargetFilter();
        var diagram = mock(Diagram.class);
        var node = mock(Node.class);
        var edge = mock(Edge.class);
        when(diagram.getDescriptionId()).thenReturn(SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);

        assertThat(filter.canHandle(null, diagram, node, node)).isTrue();
        assertThat(filter.canHandle(null, diagram, edge, node)).isFalse();
        assertThat(filter.canHandle(null, diagram, node, edge)).isFalse();

        when(diagram.getDescriptionId()).thenReturn("otherDiagram");
        assertThat(filter.canHandle(null, diagram, node, node)).isFalse();
    }

    /**
     * Verifies that only connector tools accepting the selected target remain in the palette.
     */
    @Test
    void filtersConnectorToolsByTargetDescription() {
        var filter = new SDVConnectorPaletteTargetFilter();
        var targetNode = mock(Node.class);
        when(targetNode.getDescriptionId()).thenReturn("target");

        var acceptedDescription = mock(NodeDescription.class);
        when(acceptedDescription.getId()).thenReturn("target");
        var rejectedDescription = mock(NodeDescription.class);
        when(rejectedDescription.getId()).thenReturn("other");

        var acceptedTool = new SingleClickOnTwoDiagramElementsTool("accepted", "Accepted", List.of(),
                List.of(new SingleClickOnTwoDiagramElementsCandidate(List.of(), List.of(acceptedDescription))), "");
        var rejectedTool = new SingleClickOnTwoDiagramElementsTool("rejected", "Rejected", List.of(),
                List.of(new SingleClickOnTwoDiagramElementsCandidate(List.of(), List.of(rejectedDescription))), "");
        var otherTool = mock(ITool.class);
        var divider = new PaletteDivider("divider");
        var mixedSection = new ToolSection("mixed", "Mixed", List.of(), List.of(acceptedTool, rejectedTool));
        var emptySection = new ToolSection("empty", "Empty", List.of(), List.of(rejectedTool));
        var palette = new Palette("palette", List.of(otherTool), List.of(acceptedTool, rejectedTool, mixedSection, emptySection, otherTool, divider));

        var result = filter.customize(null, null, null, targetNode, palette);

        assertThat(result.quickAccessTools()).containsExactly(otherTool);
        assertThat(result.paletteEntries()).containsExactly(acceptedTool, new ToolSection("mixed", "Mixed", List.of(), List.of(acceptedTool)), otherTool, divider);
    }

    /**
     * Verifies that a missing palette is left unchanged.
     */
    @Test
    void preservesMissingPalette() {
        var filter = new SDVConnectorPaletteTargetFilter();

        assertThat(filter.customize(null, null, null, mock(Node.class), null)).isNull();
    }
}
