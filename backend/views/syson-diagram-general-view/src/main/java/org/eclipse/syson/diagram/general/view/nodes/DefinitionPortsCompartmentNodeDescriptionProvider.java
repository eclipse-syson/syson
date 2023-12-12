/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.ArrayList;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the Definition ports compartment node description.
 *
 * @author arichard
 */
public class DefinitionPortsCompartmentNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    public static final String NAME = "GV Node DefinitionPortsCompartment";

    public DefinitionPortsCompartmentNodeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    public NodeDescription create() {
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenLayoutStrategy(this.diagramBuilderHelper.newListLayoutStrategyDescription().build())
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement()))
                .labelExpression("ports")
                .name(NAME)
                .semanticCandidatesExpression(AQLConstants.AQL_SELF)
                .style(this.createUsageCompartmentNodeStyle())
                .userResizable(false)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optDefinitionPortsCompartmentNodeDescription = cache.getNodeDescription(DefinitionPortsCompartmentNodeDescriptionProvider.NAME);
        var optDefinitionPortsCompartmentItemNodeDescription = cache.getNodeDescription(DefinitionPortsCompartmentItemNodeDescriptionProvider.NAME);

        NodeDescription nodeDescription = optDefinitionPortsCompartmentNodeDescription.get();
        nodeDescription.getChildrenDescriptions().add(optDefinitionPortsCompartmentItemNodeDescription.get());
        nodeDescription.setPalette(this.createAttributesCompartmentPalette(cache));
    }

    private NodePalette createAttributesCompartmentPalette(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newNodePalette()
                .dropNodeTool(this.createPortsCompartmentDropFromDiagramTool(cache)).nodeTools(this.createCompartmentNodeTool(SysmlPackage.eINSTANCE.getPortUsage(), "port"))
                .build();
    }

    private DropNodeTool createPortsCompartmentDropFromDiagramTool(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();

        var optDefinitionPortsCompartmentItemNodeDescription = cache.getNodeDescription(DefinitionPortsCompartmentItemNodeDescriptionProvider.NAME);
        var optUsagePortsCompartmentItemNodeDescription = cache.getNodeDescription(UsagePortsCompartmentItemNodeDescriptionProvider.NAME);

        acceptedNodeTypes.add(optDefinitionPortsCompartmentItemNodeDescription.get());
        acceptedNodeTypes.add(optUsagePortsCompartmentItemNodeDescription.get());

        var dropElementFromDiagram = this.viewBuilderHelper.newChangeContext()
                .expression("aql:droppedElement.dropElementFromDiagram(droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes)");

        return this.diagramBuilderHelper.newDropNodeTool()
                .name("Drop from Diagram")
                .acceptedNodeTypes(acceptedNodeTypes.toArray(new NodeDescription[acceptedNodeTypes.size()]))
                .body(dropElementFromDiagram.build())
                .build();
    }
}
