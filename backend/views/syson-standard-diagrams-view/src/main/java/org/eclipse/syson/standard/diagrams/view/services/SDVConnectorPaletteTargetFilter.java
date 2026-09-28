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

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.dto.SingleClickOnTwoDiagramElementsTool;
import org.eclipse.sirius.components.collaborative.diagrams.palette.api.IDiagramConnectorPaletteCustomizer;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.palette.dto.IPaletteEntry;
import org.eclipse.sirius.components.palette.dto.ITool;
import org.eclipse.sirius.components.palette.dto.Palette;
import org.eclipse.sirius.components.palette.dto.ToolSection;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.springframework.stereotype.Service;

/**
 * Keeps only connector tools that accept the selected target node in standard view diagrams.
 * <p>
 * This Spring service applies to connector palettes between nodes of standard view diagrams, including nodes
 * contributed by downstream applications. Applications requiring different filtering must exclude this bean and
 * register their own {@link IDiagramConnectorPaletteCustomizer}. Adding {@code @Primary} to another customizer is
 * insufficient because Sirius Web invokes every customizer in the Spring bean list.
 * </p>
 *
 * @author arichard
 */
@Service
public class SDVConnectorPaletteTargetFilter implements IDiagramConnectorPaletteCustomizer {

    /**
     * Applies the target filter to connectors between nodes of a standard view diagram.
     *
     * @param editingContext
     *            the editing context
     * @param diagram
     *            the diagram
     * @param sourceDiagramElement
     *            the connector source
     * @param targetDiagramElement
     *            the connector target
     * @return {@code true} if the source and target are nodes in a standard view diagram
     */
    @Override
    public boolean canHandle(IEditingContext editingContext, Diagram diagram, Object sourceDiagramElement, Object targetDiagramElement) {
        return SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID.equals(diagram.getDescriptionId())
                && sourceDiagramElement instanceof Node && targetDiagramElement instanceof Node;
    }

    /**
     * Removes connector tools whose declared candidates do not include the selected target.
     *
     * @param editingContext
     *            the editing context
     * @param diagram
     *            the diagram
     * @param sourceDiagramElement
     *            the connector source
     * @param targetDiagramElement
     *            the connector target
     * @param palette
     *            the palette to filter
     * @return the filtered palette
     */
    @Override
    public Palette customize(IEditingContext editingContext, Diagram diagram, Object sourceDiagramElement, Object targetDiagramElement, Palette palette) {
        if (palette == null) {
            return null;
        }

        String targetDescriptionId = ((Node) targetDiagramElement).getDescriptionId();
        List<IPaletteEntry> entries = palette.paletteEntries().stream()
                .map(entry -> this.filterEntry(entry, targetDescriptionId))
                .filter(Objects::nonNull)
                .toList();
        return new Palette(palette.id(), palette.quickAccessTools(), entries);
    }

    /**
     * Filters connector tools in an entry, removing a section when no tools remain.
     *
     * @param entry
     *            the palette entry to filter
     * @param targetDescriptionId
     *            the selected target's diagram description identifier
     * @return the filtered entry, or {@code null} if no compatible tool remains
     */
    private IPaletteEntry filterEntry(IPaletteEntry entry, String targetDescriptionId) {
        IPaletteEntry filteredEntry = entry;
        if (entry instanceof ToolSection section) {
            List<ITool> tools = section.tools().stream()
                    .filter(tool -> this.acceptsTarget(tool, targetDescriptionId))
                    .toList();
            if (tools.isEmpty()) {
                filteredEntry = null;
            } else {
                filteredEntry = new ToolSection(section.id(), section.label(), section.iconURL(), tools);
            }
        } else if (entry instanceof ITool tool && !this.acceptsTarget(tool, targetDescriptionId)) {
            filteredEntry = null;
        }
        return filteredEntry;
    }

    /**
     * Checks whether a connector tool declares the selected target as a candidate.
     *
     * @param tool
     *            the tool to inspect
     * @param targetDescriptionId
     *            the selected target's diagram description identifier
     * @return {@code true} if the tool accepts the target, or is not a connector tool
     */
    private boolean acceptsTarget(ITool tool, String targetDescriptionId) {
        return !(tool instanceof SingleClickOnTwoDiagramElementsTool connectorTool)
                || connectorTool.candidates().stream()
                        .anyMatch(candidate -> candidate.targets().stream().anyMatch(target -> targetDescriptionId.equals(target.getId())));
    }
}
