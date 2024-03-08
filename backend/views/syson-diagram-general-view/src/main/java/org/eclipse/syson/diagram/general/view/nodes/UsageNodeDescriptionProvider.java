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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.general.view.services.GeneralViewEdgeToolSwitch;
import org.eclipse.syson.diagram.general.view.services.GeneralViewNodeToolSectionSwitch;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Node description provider for all SysMLv2 Usage elements.
 * 
 * @author arichard
 */
public class UsageNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    private final EClass eClass;

    public UsageNodeDescriptionProvider(EClass eClass, IColorProvider colorProvider) {
        super(colorProvider);
        this.eClass = Objects.requireNonNull(eClass);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(this.eClass);
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenLayoutStrategy(this.diagramBuilderHelper.newListLayoutStrategyDescription().areChildNodesDraggableExpression("false").build())
                .collapsible(true)
                .defaultHeightExpression(ViewConstants.DEFAULT_CONTAINER_NODE_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(domainType)
                .labelExpression("aql:self.getContainerLabel()")
                .name(GVDescriptionNameGenerator.getNodeName(this.eClass))
                .semanticCandidatesExpression("aql:self.getAllReachable(" + domainType + ")")
                .style(this.createUsageNodeStyle())
                .userResizable(true)
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        NodeDescription nodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(this.eClass)).get();
        diagramDescription.getNodeDescriptions().add(nodeDescription);

        var allTargetNodeDescriptions = new ArrayList<NodeDescription>();

        GeneralViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> {
            var optNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(definition));
            allTargetNodeDescriptions.add(optNodeDescription.get());
        });

        GeneralViewDiagramDescriptionProvider.USAGES.forEach(usage -> {
            var optNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(usage));
            allTargetNodeDescriptions.add(optNodeDescription.get());
        });
        
        GeneralViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            if (type.equals(this.eClass)) {
                listItems.forEach(eReference -> {
                    var optNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getCompartmentName(type, eReference));
                    nodeDescription.getReusedChildNodeDescriptions().add(optNodeDescription.get());
                });
            }
        });

        var optPackageNodeDescription = cache.getNodeDescription(PackageNodeDescriptionProvider.NAME);
        allTargetNodeDescriptions.add(optPackageNodeDescription.get());

        nodeDescription.setPalette(this.createNodePalette(nodeDescription, allTargetNodeDescriptions));
    }

    private NodeStyleDescription createUsageNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(10)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .displayHeaderSeparator(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(true)
                .withHeader(true)
                .build();
    }

    private NodePalette createNodePalette(NodeDescription nodeDescription, List<NodeDescription> allNodeDescriptions) {
        var changeContext = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.deleteFromModel()");

        var deleteTool = this.diagramBuilderHelper.newDeleteTool()
                .name("Delete from Model")
                .body(changeContext.build());

        var callEditService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF + ".directEdit(newLabel)");

        var editTool = this.diagramBuilderHelper.newLabelEditTool()
                .name("Edit")
                .initialDirectEditLabelExpression(AQLConstants.AQL_SELF + ".getDefaultInitialDirectEditLabel()")
                .body(callEditService.build());

        var edgeTools = new ArrayList<EdgeTool>();
        edgeTools.addAll(this.getEdgeTools(nodeDescription, allNodeDescriptions));

        var toolSections = new ArrayList<NodeToolSection>();
        toolSections.addAll(this.getToolSections(nodeDescription, allNodeDescriptions));

        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .labelEditTool(editTool.build())
                .edgeTools(edgeTools.toArray(EdgeTool[]::new))
                .toolSections(toolSections.toArray(NodeToolSection[]::new))
                .build();
    }

    private List<EdgeTool> getEdgeTools(NodeDescription nodeDescription, List<NodeDescription> allNodeDescriptions) {
        GeneralViewEdgeToolSwitch edgeToolSwitch = new GeneralViewEdgeToolSwitch(nodeDescription, allNodeDescriptions);
        edgeToolSwitch.doSwitch(this.eClass);
        return edgeToolSwitch.getEdgeTools();
    }

    private List<NodeToolSection> getToolSections(NodeDescription nodeDescription, List<NodeDescription> allNodeDescriptions) {
        GeneralViewNodeToolSectionSwitch toolSectionSwitch = new GeneralViewNodeToolSectionSwitch(nodeDescription, allNodeDescriptions);
        toolSectionSwitch.doSwitch(this.eClass);
        return toolSectionSwitch.getNodeToolSections();
    }
}
