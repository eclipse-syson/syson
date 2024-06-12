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
package org.eclipse.syson.diagram.common.view.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.FreeFormLayoutStrategyDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.LabelTextAlign;
import org.eclipse.sirius.components.view.diagram.ListLayoutStrategyDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.ToolSection;
import org.eclipse.syson.diagram.common.view.services.ViewEdgeToolSwitch;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Node description provider for all SysMLv2 Usage elements.
 *
 * @author arichard
 */
public abstract class AbstractUsageNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    protected final EClass eClass;

    protected final UtilService utilServices;

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public AbstractUsageNodeDescriptionProvider(EClass eClass, IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(colorProvider);
        this.eClass = Objects.requireNonNull(eClass);
        this.descriptionNameGenerator = Objects.requireNonNull(nameGenerator);
        this.utilServices = new UtilService();
    }

    /**
     * Implementers should provide the list of {@link NodeDescription} that is reused as child in that Usage node
     * description.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the list of {@link NodeDescription} that are added as reused child.
     */
    protected abstract List<NodeDescription> getReusedChildren(IViewDiagramElementFinder cache);

    /**
     * Implementers should provide the list of all {@link NodeDescription} defined in the diagram.<br>
     * This list is used to create edge tools associated to this Usage node description.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the list of all {@link NodeDescription} defined in the diagram.
     */
    protected abstract List<NodeDescription> getAllNodeDescriptions(IViewDiagramElementFinder cache);

    /**
     * Implementers should provide the list of {@link ToolSection} defined on the given {@link NodeDescription}.
     *
     * @param nodeDescription
     *            the actual Usage node description
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the list of {@link ToolSection} of this Usage node.
     */
    protected abstract List<NodeToolSection> getToolSections(NodeDescription nodeDescription, IViewDiagramElementFinder cache);

    /**
     * Implementers should provide the expression used to retrieve all semantic candidates.<br>
     * By default, the expression retrieves all reachable element of the given semantic type.
     *
     * @param domainType
     *            the semantic type of the element.
     * @return the AQL expression to retrieve all semantic candidates for this node.
     */
    protected abstract String getSemanticCandidatesExpression(String domainType);

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(this.eClass);
        return this.diagramBuilderHelper.newNodeDescription()
                .collapsible(true)
                .defaultHeightExpression(ViewConstants.DEFAULT_CONTAINER_NODE_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(domainType)
                .insideLabel(this.createInsideLabelDescription())
                .name(this.getDescriptionNameGenerator().getNodeName(this.eClass))
                .semanticCandidatesExpression(this.getSemanticCandidatesExpression(domainType))
                .style(this.createUsageNodeStyle())
                .userResizable(true)
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(this.eClass)).ifPresent(nodeDescription -> {
            diagramDescription.getNodeDescriptions().add(nodeDescription);
            nodeDescription.getReusedChildNodeDescriptions().addAll(this.getReusedChildren(cache));
            List<NodeDescription> growableNodes = new ArrayList<>();
            nodeDescription.getReusedChildNodeDescriptions().stream()
                    .filter(nodeDesc -> nodeDesc.getChildrenLayoutStrategy() instanceof FreeFormLayoutStrategyDescription)
                    .forEach(growableNodes::add);
            ListLayoutStrategyDescription layoutStrategy = this.diagramBuilderHelper.newListLayoutStrategyDescription()
                    .areChildNodesDraggableExpression("false")
                    .growableNodes(growableNodes.toArray(NodeDescription[]::new))
                    .build();
            nodeDescription.setChildrenLayoutStrategy(layoutStrategy);
            nodeDescription.setPalette(this.createNodePalette(nodeDescription, cache));
        });
    }

    protected InsideLabelDescription createInsideLabelDescription() {
        return this.diagramBuilderHelper.newInsideLabelDescription()
                .labelExpression(AQLUtils.getSelfServiceCallExpression("getContainerLabel"))
                .position(InsideLabelPosition.TOP_CENTER)
                .style(this.createInsideLabelStyle())
                .textAlign(LabelTextAlign.CENTER)
                .build();
    }

    protected InsideLabelStyle createInsideLabelStyle() {
        return this.diagramBuilderHelper.newInsideLabelStyle()
                .displayHeaderSeparator(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(true)
                .withHeader(true)
                .build();
    }

    protected IDescriptionNameGenerator getDescriptionNameGenerator() {
        return this.descriptionNameGenerator;
    }

    private NodeStyleDescription createUsageNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(10)
                .background(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .build();
    }

    private NodePalette createNodePalette(NodeDescription nodeDescription, IViewDiagramElementFinder cache) {
        var changeContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("deleteFromModel"));

        var deleteTool = this.diagramBuilderHelper.newDeleteTool()
                .name("Delete from Model")
                .body(changeContext.build());

        var callEditService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("directEdit", "newLabel"));

        var editTool = this.diagramBuilderHelper.newLabelEditTool()
                .name("Edit")
                .initialDirectEditLabelExpression(AQLUtils.getSelfServiceCallExpression("getDefaultInitialDirectEditLabel"))
                .body(callEditService.build());

        var edgeTools = new ArrayList<EdgeTool>();
        edgeTools.addAll(this.getEdgeTools(nodeDescription, cache));

        var toolSections = new ArrayList<NodeToolSection>();
        toolSections.addAll(this.getToolSections(nodeDescription, cache));
        this.orderToolSectionsTools(toolSections);
        toolSections.add(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection());

        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .labelEditTool(editTool.build())
                .edgeTools(edgeTools.toArray(EdgeTool[]::new))
                .toolSections(toolSections.toArray(NodeToolSection[]::new))
                .build();
    }

    private List<EdgeTool> getEdgeTools(NodeDescription nodeDescription, IViewDiagramElementFinder cache) {
        ViewEdgeToolSwitch edgeToolSwitch = new ViewEdgeToolSwitch(nodeDescription, this.getAllNodeDescriptions(cache), this.getDescriptionNameGenerator());
        return edgeToolSwitch.doSwitch(this.eClass);
    }
}
