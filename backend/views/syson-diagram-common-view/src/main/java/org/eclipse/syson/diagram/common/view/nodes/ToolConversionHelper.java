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
import java.util.stream.Collectors;

import org.eclipse.sirius.components.view.Operation;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.diagram.DiagramElementDescription;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Temporary helper.
 *
 * @author pcdavid
 */
@Service
public class ToolConversionHelper {
    private final Logger logger = LoggerFactory.getLogger(ToolConversionHelper.class);

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    public EdgeTool convertToSingleEdgeToolWithPalette(List<EdgeTool> edgeTools) {
        var combinedTargetDescriptions = edgeTools.stream()
                .flatMap(tool -> tool.getTargetElementDescriptions().stream())
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

        return this.diagramBuilderHelper.newEdgeTool()
                .targetElementDescriptions(combinedTargetDescriptions)
                .palette(edgeToolPalette)
                .build();
    }

    private String convertPrecondition(EdgeTool edgeTool) {
        var originalPrecondition = edgeTool.getPreconditionExpression();
        if (originalPrecondition == null || originalPrecondition.isBlank()) {
            var expectedTypes = edgeTool.getTargetElementDescriptions().stream().map(e -> e.getDomainType().replace("sysml::", "")).collect(Collectors.toSet());
            var conjunction = expectedTypes.stream()
                    .map(expectedType -> "edgeTarget.isTargetNodeOfType(editingContext, '%s')".formatted(expectedType))
                    .collect(Collectors.joining(" or "));
            return "aql:(%s)".formatted(conjunction);
        } else {
            this.logger.warn("Can not convert precondition '{}' for EdgeTool '{}'", originalPrecondition, edgeTool.getName());
            return originalPrecondition;
        }
    }
}
