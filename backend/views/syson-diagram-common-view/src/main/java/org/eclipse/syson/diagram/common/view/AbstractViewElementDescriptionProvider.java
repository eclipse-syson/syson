/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.diagram.common.view;

import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.diagram.NodeTool;

/**
 * Abstract class used to share common parts for diagrams, nodes and edges.
 *
 * @author arichard
 */
public class AbstractViewElementDescriptionProvider {

    protected final ViewBuilders viewBuilderHelper = new ViewBuilders();

    protected final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    protected NodeTool addExistingElementsTool(boolean recursive) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var addExistingelements = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.addExistingElements(editingContext, diagramContext, selectedNode, convertedNodes, " + recursive + ")");

        String title = "Add existing elements";
        String iconURL = "/icons/AddExistingElements.svg";
        if (recursive) {
            title += " (recursive)";
            iconURL = "/icons/AddExistingElementsRecursive.svg";
        }

        return builder
                .name(title)
                .iconURLsExpression(iconURL)
                .body(addExistingelements.build())
                .build();
    }
}
