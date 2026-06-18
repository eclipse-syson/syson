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
package org.eclipse.syson.application.nodes.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ITool;
import org.eclipse.sirius.components.collaborative.diagrams.dto.SingleClickOnDiagramElementTool;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolSection;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.IDiagramElementDescription;
import org.eclipse.sirius.components.view.emf.diagram.api.IPaletteToolsProvider;
import org.eclipse.syson.sysml.ForkNode;
import org.eclipse.syson.sysml.JoinNode;
import org.springframework.stereotype.Service;

/**
 * Used to contribute the <em>Rotate</em> tool for {@link ForkNode} and {@link JoinNode} graphical nodes.
 * <p>
 *     This {@link IPaletteToolsProvider} only declare the tool.
 *     The behavior is contributed in the frontend.
 * </p>
 *
 * @author gcoutable
 */
@Service
public class ForkJoinNodePaletteToolProvider implements IPaletteToolsProvider {

    private static final String FORK_JOIN_NODE_ROTATE_TOOL_ID = "fork_join_node_rotate_tool";

    private static final String FORK_JOIN_NODE_ROTATE_TOOL_LABEL = "Rotate";

    private static final String FORK_JOIN_NODE_ROTATE_TOOL_SECTION_ID = "edit-section";

    private static final String FORK_JOIN_NODE_ROTATE_TOOL_SECTION_LABEL = "Edit";

    private final IObjectSearchService objectSearchService;

    public ForkJoinNodePaletteToolProvider(IObjectSearchService objectSearchService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
    }

    @Override
    public List<ToolSection> createExtraToolSections(IEditingContext editingContext, DiagramContext diagramContext, Object diagramElementDescription, Object diagramElement) {
        List<ITool> extraTool = new ArrayList<>();

        var optionalTargetObjectId = this.getTargetObjectId(diagramElement);

        if (optionalTargetObjectId.isPresent() && diagramElementDescription instanceof IDiagramElementDescription nodeDescription) {
            var semanticObject = this.objectSearchService.getObject(editingContext, optionalTargetObjectId.get());
            if (semanticObject.isPresent() && (semanticObject.get() instanceof ForkNode || semanticObject.get() instanceof JoinNode)) {
                ITool tool = SingleClickOnDiagramElementTool.newSingleClickOnDiagramElementTool(FORK_JOIN_NODE_ROTATE_TOOL_ID)
                        .label(FORK_JOIN_NODE_ROTATE_TOOL_LABEL)
                        .targetDescriptions(List.of(nodeDescription))
                        .iconURL(List.of())
                        .keyBindings(List.of())
                        .build();
                extraTool.add(tool);
            }
        }

        if (!extraTool.isEmpty()) {
            return List.of(new ToolSection(FORK_JOIN_NODE_ROTATE_TOOL_SECTION_ID, FORK_JOIN_NODE_ROTATE_TOOL_SECTION_LABEL, List.of(), extraTool));
        } else {
            return List.of();
        }
    }

    private Optional<String> getTargetObjectId(Object diagramElement) {
        Optional<String> result = Optional.empty();
        if (diagramElement instanceof Node node) {
            result = Optional.of(node.getTargetObjectId());
        }
        return result;
    }

    @Override
    public List<ITool> createQuickAccessTools(IEditingContext editingContext, DiagramContext diagramContext, Object diagramElementDescription, Object diagramElement) {
        return List.of();
    }
}
