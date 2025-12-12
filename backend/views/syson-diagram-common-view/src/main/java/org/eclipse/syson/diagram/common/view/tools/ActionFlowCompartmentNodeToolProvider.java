/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.common.view.services.ViewNodeService;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Node tool provider for creating both nested and owned ActionUsage inside free-form compartment.
 *
 * @author Jerome Gout
 */
public class ActionFlowCompartmentNodeToolProvider implements INodeToolProvider {

    private static final String NEW_INSTANCE = "newInstance";

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var addToExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(DiagramMutationAQLService::expose).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var revealOperation = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(ViewNodeService::revealCompartment).aql(Node.SELECTED_NODE, AQLConstants.SELF, DiagramContext.DIAGRAM_CONTEXT, IEditingContext.EDITING_CONTEXT,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var creationServiceCall = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + NEW_INSTANCE)
                .children(addToExposedElements.build(), revealOperation.build())
                .build();

        var letNewInstance = this.viewBuilderHelper.newLet()
                .variableName(NEW_INSTANCE)
                .valueExpression(ServiceMethod.of0(ViewCreateService::createSubActionUsage).aqlSelf())
                .children(creationServiceCall)
                .build();

        return builder.name("New Action")
                .iconURLsExpression("/icons/full/obj16/ActionUsage.svg")
                .body(letNewInstance)
                .elementsToSelectExpression(AQLConstants.AQL + NEW_INSTANCE)
                .build();
    }
}
