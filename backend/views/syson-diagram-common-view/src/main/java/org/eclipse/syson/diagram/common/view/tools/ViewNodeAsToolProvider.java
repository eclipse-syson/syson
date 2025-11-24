/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.diagram.common.view.tools;

import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Node Tool executed when "View as > XXXX View" is called and allowing to move the selected node inside a new ViewUsage
 * typed by the XXXX View..
 *
 * @author arichard
 */
public class ViewNodeAsToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final String viewDefinition;

    private final String label;

    public ViewNodeAsToolProvider(String viewDefinition, String label) {
        this.viewDefinition = Objects.requireNonNull(viewDefinition);
        this.label = Objects.requireNonNull(label);
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newNodeTool()
                .name(this.label)
                .iconURLsExpression("/icons/full/obj16/ViewUsage.svg")
                .body(this.viewBuilderHelper.newChangeContext()
                        .expression(
                                ServiceMethod.of4(DiagramMutationAQLService::viewNodeAs).aqlSelf(this.viewDefinition, IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE))
                        .children(this.viewBuilderHelper.newChangeContext()
                                .expression(ServiceMethod.of1(DiagramMutationAQLService::createDiagram).aqlSelf(IEditingContext.EDITING_CONTEXT))
                                .build())
                        .build())
                .build();
    }

}
