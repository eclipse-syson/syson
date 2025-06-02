/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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

import java.util.List;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.common.view.nodes.ActionFlowCompartmentNodeDescriptionProvider;
import org.eclipse.syson.util.AQLUtils;

/**
 * Node tool provider for creating both nested and owned ActionUsage inside free-form compartment.
 *
 * @author Jerome Gout
 */
public class ActionFlowCompartmentNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    public ActionFlowCompartmentNodeToolProvider() {
        super();
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var params = List.of(
                AQLUtils.aqlString(ActionFlowCompartmentNodeDescriptionProvider.COMPARTMENT_LABEL),
                Node.SELECTED_NODE,
                IEditingContext.EDITING_CONTEXT,
                IDiagramContext.DIAGRAM_CONTEXT,
                ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE);

        var createViewOperation = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("createViewInFreeFormCompartment", params))
                .build();

        var revealOperation = this.viewBuilderHelper.newChangeContext()
                .expression("aql:selectedNode.revealCompartment(self, diagramContext, editingContext, convertedNodes)")
                .build();

        var creationServiceCall = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("createSubActionUsage"))
                .children(createViewOperation, revealOperation)
                .build();

        return builder.name("New Action")
                .iconURLsExpression("/icons/full/obj16/ActionUsage.svg")
                .body(creationServiceCall)
                .build();
    }
}
