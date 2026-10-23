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
package org.eclipse.syson.diagram.common.view.nodes;

import java.util.List;

import org.eclipse.sirius.components.view.Operation;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.diagram.DiagramElementDescription;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.services.aql.DiagramQueryAQLService;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.ServiceMethod;
import org.springframework.stereotype.Service;

/**
 * Adapts legacy edge actions to a single connector palette, preserving each action's target restrictions.
 *
 * @author pcdavid
 */
@Service
public class ToolConversionHelper {
    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    /**
     * Creates one connector palette containing the legacy actions in their original order.
     *
     * @param edgeTools
     *            the actions to migrate
     * @return a single edge tool, or an empty list when there are no actions
     */
    public List<EdgeTool> convertToEdgeToolsWithPalette(List<EdgeTool> edgeTools) {
        if (edgeTools.isEmpty()) {
            return List.of();
        }
        var combinedTargetDescriptions = edgeTools.stream()
                .flatMap(tool -> tool.getTargetElementDescriptions().stream())
                .distinct()
                .toArray(DiagramElementDescription[]::new);

        List<NodeTool> nodeTools = edgeTools.stream()
                .map(edgeTool -> this.diagramBuilderHelper.newNodeTool()
                        .name(edgeTool.getName())
                        .iconURLsExpression(edgeTool.getIconURLsExpression())
                        .preconditionExpression(this.convertPrecondition(edgeTool))
                        .dialogDescription(edgeTool.getDialogDescription())
                        .body(edgeTool.getBody().toArray(new Operation[0]))
                        .elementsToSelectExpression(edgeTool.getElementsToSelectExpression())
                        .build())
                .toList();

        NodePalette edgeToolPalette = this.diagramBuilderHelper.newNodePalette()
                .nodeTools(nodeTools.toArray(new NodeTool[0]))
                .build();

        return List.of(this.diagramBuilderHelper.newEdgeTool()
                .targetElementDescriptions(combinedTargetDescriptions)
                .palette(edgeToolPalette)
                .build());
    }

    private String convertPrecondition(EdgeTool edgeTool) {
        // Description IDs are assigned after the providers have linked the complete view. Resolve the named
        // descriptions at palette evaluation time instead of computing IDs on an unfinished view.
        var targetNames = edgeTool.getTargetElementDescriptions().stream()
                .map(description -> AQLUtils.aqlString(description.getName().replace("\\", "\\\\").replace("'", "\\'")))
                .distinct()
                .toList();
        String targetPrecondition = ServiceMethod.of2(DiagramQueryAQLService::isTargetDescription)
                .aql("edgeTarget", "editingContext", AQLUtils.aqlSequence(targetNames));
        String originalPrecondition = edgeTool.getPreconditionExpression();
        if (originalPrecondition == null || originalPrecondition.isBlank()) {
            return targetPrecondition;
        }
        String expression = originalPrecondition.strip();
        if (expression.startsWith(AQLConstants.AQL)) {
            expression = expression.substring(AQLConstants.AQL.length());
        }
        return AQLConstants.AQL + "(" + targetPrecondition.substring(AQLConstants.AQL.length()) + ") and (" + expression + ")";
    }
}
