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
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the attribute usage node description.
 *
 * @author arichard
 */
public class AttributeUsageNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    public static final String NAME = "GV Node AttributeUsage";

    public AttributeUsageNodeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getAttributeUsage());
        return this.diagramBuilderHelper.newNodeDescription()
                .defaultHeightExpression(ViewConstants.DEFAULT_CONTAINER_NODE_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(domainType)
                .labelExpression("aql:self.getContainerLabel()")
                .name(NAME)
                .semanticCandidatesExpression("aql:self.getAllReachable(" + domainType + ")")
                .style(this.createUsageNodeStyle())
                .userResizable(true)
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var dependencyTargetNodeDescriptions = new ArrayList<NodeDescription>();

        var optAttributeDefinitionNodeDescription = cache.getNodeDescription(AttributeDefinitionNodeDescriptionProvider.NAME);
        var optAttributeUsageNodeDescription = cache.getNodeDescription(AttributeUsageNodeDescriptionProvider.NAME);
        var optEnumerationDefinitionNodeDescription = cache.getNodeDescription(EnumerationDefinitionNodeDescriptionProvider.NAME);
        var optInterfaceDefinitionNodeDescription = cache.getNodeDescription(InterfaceDefinitionNodeDescriptionProvider.NAME);
        var optInterfaceUsageNodeDescription = cache.getNodeDescription(InterfaceUsageNodeDescriptionProvider.NAME);
        var optItemDefinitionNodeDescription = cache.getNodeDescription(ItemDefinitionNodeDescriptionProvider.NAME);
        var optItemUsageNodeDescription = cache.getNodeDescription(ItemUsageNodeDescriptionProvider.NAME);
        var optMetadataDefinitionNodeDescription = cache.getNodeDescription(MetadataDefinitionNodeDescriptionProvider.NAME);
        var optPackageNodeDescription = cache.getNodeDescription(PackageNodeDescriptionProvider.NAME);
        var optPartDefinitionNodeDescription = cache.getNodeDescription(PartDefinitionNodeDescriptionProvider.NAME);
        var optPartUsageNodeDescription = cache.getNodeDescription(PartUsageNodeDescriptionProvider.NAME);
        var optPortDefinitionNodeDescription = cache.getNodeDescription(PortDefinitionNodeDescriptionProvider.NAME);
        var optPortUsageNodeDescription = cache.getNodeDescription(PortUsageNodeDescriptionProvider.NAME);

        dependencyTargetNodeDescriptions.add(optAttributeDefinitionNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optAttributeUsageNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optEnumerationDefinitionNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optInterfaceDefinitionNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optInterfaceUsageNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optItemDefinitionNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optItemUsageNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optMetadataDefinitionNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optPackageNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optPartDefinitionNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optPartUsageNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optPortDefinitionNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optPortUsageNodeDescription.get());

        NodeDescription nodeDescription = optAttributeUsageNodeDescription.get();
        diagramDescription.getNodeDescriptions().add(nodeDescription);
        nodeDescription.setPalette(this.createNodePalette(nodeDescription, dependencyTargetNodeDescriptions));
    }

    @Override
    protected NodeStyleDescription createUsageNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(10)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .displayHeaderSeparator(false)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(true)
                .withHeader(false)
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

        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .labelEditTool(editTool.build())
                .edgeTools(this.createDependencyEdgeTool(allNodeDescriptions),
                        this.createRedefinitionEdgeTool(allNodeDescriptions.stream().filter(nodeDesc -> NAME.equals(nodeDesc.getName())).toList()),
                        this.createSubsettingEdgeTool(allNodeDescriptions.stream().filter(nodeDesc -> NAME.equals(nodeDesc.getName())).toList()))
                .build();
    }
}
