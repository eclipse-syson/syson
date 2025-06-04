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

package org.eclipse.syson.diagram.interconnection.view.tools;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Node tool provider to create a flow connection edge from a Part usage port to a new Part usage port.
 *
 * @author Jerome Gout
 */
public class PartUsageFlowConnectionNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator;

    private final String partUsageNodeDescriptionName;

    public PartUsageFlowConnectionNodeToolProvider(IDescriptionNameGenerator descriptionNameGenerator, String partUsageNodeDescriptionName) {
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
        this.partUsageNodeDescriptionName =  Objects.requireNonNull(partUsageNodeDescriptionName);
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        NodeDescription nodeDescription = cache.getNodeDescription(this.partUsageNodeDescriptionName).get();

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(NodeContainmentKind.CHILD_NODE)
                .elementDescription(nodeDescription)
                .parentViewExpression(AQLUtils.getSelfServiceCallExpression("getParentNode", List.of(Node.SELECTED_NODE, IDiagramContext.DIAGRAM_CONTEXT)))
                .semanticElementExpression(AQLConstants.AQL_SELF)
                .variableName("newView");

        var creationBindingConnectorAsUsageServiceCall = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("createPartUsageAndFlowConnection"))
                .children(createView.build())
                .build();

        var rootChangContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF)
                .children(creationBindingConnectorAsUsageServiceCall)
                .build();

        return builder.name(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getFlowConnectionUsage()))
                .iconURLsExpression("/icons/full/obj16/FlowConnectionUsage.svg")
                .body(rootChangContext)
                .build();
    }
}
