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
package org.eclipse.syson.diagram.common.view.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.FreeFormLayoutStrategyDescription;
import org.eclipse.sirius.components.view.diagram.HeaderSeparatorDisplayMode;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.LabelOverflowStrategy;
import org.eclipse.sirius.components.view.diagram.LabelTextAlign;
import org.eclipse.sirius.components.view.diagram.ListLayoutStrategyDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.ToolSection;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.common.view.services.ViewEdgeToolSwitch;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Node description provider for all SysMLv2 Definitions elements.
 *
 * @author arichard
 */
public abstract class AbstractDefinitionNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    protected final EClass eClass;

    protected final UtilService utilServices;

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public AbstractDefinitionNodeDescriptionProvider(EClass eClass, IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(colorProvider);
        this.eClass = Objects.requireNonNull(eClass);
        this.descriptionNameGenerator = Objects.requireNonNull(nameGenerator);
        this.utilServices = new UtilService();
    }

    /**
     * Implementers should provide the set of {@link NodeDescription} that is reused as child in that Definition node
     * description.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the set of {@link NodeDescription} that are added as reused child.
     */
    protected abstract Set<NodeDescription> getReusedChildren(IViewDiagramElementFinder cache);

    /**
     * Implementers should provide the set of {@link NodeDescription} that are reused as border nodes in that Usage node
     * description.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the set of {@link NodeDescription} that are reused as border nodes.
     */
    protected abstract Set<NodeDescription> getReusedBorderNodes(IViewDiagramElementFinder cache);

    /**
     * Implementers should provide the list of all {@link NodeDescription} defined in the diagram.<br>
     * This list is used to create edge tools associated to this Definition node description.
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
     *            the actual Definition node description
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the list of {@link ToolSection} of this Definition node.
     */
    protected abstract List<NodeToolSection> getToolSections(NodeDescription nodeDescription, IViewDiagramElementFinder cache);

    /**
     * Implementers should provide the list of {@link NodeTool} (without ToolSection) defined on the given
     * {@link NodeDescription}.
     *
     * @param nodeDescription
     *            the actual Usage node description
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the list of {@link NodeTool} of this Usage node.
     */
    protected abstract List<NodeTool> getToolsWithoutSection(NodeDescription nodeDescription, IViewDiagramElementFinder cache);

    /**
     * Implementers should provide the expression used to retrieve all semantic candidates.<br>
     * By default, the expression retrieves all reachable element of the given semantic type.
     *
     * @param domainType
     *            the semantic type of the element.
     * @return the AQL expression to retrieve all semantic candidates for this node.
     */
    protected abstract String getSemanticCandidatesExpression(String domainType);

    /**
     * Shall retrieve a list of {@link NodeDescription} containing the {@link NodeDescription} of nodes that can be
     * dropped form the Diagram to this {@link NodeDescription}. DEfault implementation returns an empty list, to be
     * overridden by implementors.
     *
     * @param cache
     *            The cache
     * @return The list of droppable nodes.
     */
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        return new ArrayList<>();
    }

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
                .style(this.createDefinitionNodeStyle())
                .userResizable(UserResizableDirection.BOTH)
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(this.eClass)).ifPresent(nodeDescription -> {
            diagramDescription.getNodeDescriptions().add(nodeDescription);
            nodeDescription.getReusedChildNodeDescriptions().addAll(this.getReusedChildren(cache));
            nodeDescription.getReusedBorderNodeDescriptions().addAll(this.getReusedBorderNodes(cache));
            List<NodeDescription> growableNodes = new ArrayList<>();
            nodeDescription.getReusedChildNodeDescriptions().stream()
                    .filter(nodeDesc -> nodeDesc.getStyle().getChildrenLayoutStrategy() instanceof FreeFormLayoutStrategyDescription)
                    .forEach(growableNodes::add);
            var childrenLayoutStrategy = nodeDescription.getStyle().getChildrenLayoutStrategy();
            if (childrenLayoutStrategy instanceof ListLayoutStrategyDescription listLayout) {
                listLayout.getGrowableNodes().addAll(growableNodes);
            }
            nodeDescription.setPalette(this.createNodePalette(nodeDescription, cache));
        });
    }

    protected InsideLabelDescription createInsideLabelDescription() {
        return this.diagramBuilderHelper.newInsideLabelDescription()
                .labelExpression(AQLUtils.getSelfServiceCallExpression("getContainerLabel"))
                .position(InsideLabelPosition.TOP_CENTER)
                .style(this.createInsideLabelStyle())
                .textAlign(LabelTextAlign.CENTER)
                .overflowStrategy(LabelOverflowStrategy.WRAP)
                .build();
    }

    protected InsideLabelStyle createInsideLabelStyle() {
        return this.diagramBuilderHelper.newInsideLabelStyle()
                .borderSize(0)
                .headerSeparatorDisplayMode(HeaderSeparatorDisplayMode.IF_CHILDREN)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIconExpression(AQLUtils.getSelfServiceCallExpression("showIcon"))
                .withHeader(true)
                .build();
    }

    protected NodeStyleDescription createDefinitionNodeStyle() {
        var layoutStrategy = this.diagramBuilderHelper.newListLayoutStrategyDescription()
                .areChildNodesDraggableExpression("false")
                .growableNodes(new ArrayList<>().toArray(NodeDescription[]::new))
                .build();

        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .background(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .childrenLayoutStrategy(layoutStrategy)
                .build();
    }

    protected NodePalette createNodePalette(NodeDescription nodeDescription, IViewDiagramElementFinder cache) {
        var changeContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("deleteFromModel"));

        var deleteTool = this.diagramBuilderHelper.newDeleteTool()
                .name("Delete from Model")
                .body(changeContext.build());

        var callEditService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("directEditNode", "newLabel"));

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

        var toolsWithoutSection = new ArrayList<NodeTool>();
        toolsWithoutSection.addAll(this.getToolsWithoutSection(nodeDescription, cache));

        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .labelEditTool(editTool.build())
                .edgeTools(edgeTools.toArray(EdgeTool[]::new))
                .dropNodeTool(this.createDropFromDiagramTool(cache))
                .nodeTools(toolsWithoutSection.toArray(NodeTool[]::new))
                .quickAccessTools(this.getDeleteFromDiagramTool())
                .toolSections(toolSections.toArray(NodeToolSection[]::new))
                .build();
    }

    protected IDescriptionNameGenerator getDescriptionNameGenerator() {
        return this.descriptionNameGenerator;
    }

    private List<EdgeTool> getEdgeTools(NodeDescription nodeDescription, IViewDiagramElementFinder cache) {
        ViewEdgeToolSwitch edgeToolSwitch = new ViewEdgeToolSwitch(nodeDescription, this.getAllNodeDescriptions(cache), this.getDescriptionNameGenerator());
        return edgeToolSwitch.doSwitch(this.eClass);
    }

    /**
     * Builds a {@link DropNodeTool} which will be added to the {@link NodeDescription} palette. May be implemented to
     * extend default behavior which is to return null.
     *
     * @param cache
     *            The cache
     * @return The created {@link DropNodeTool}
     */
    private DropNodeTool createDropFromDiagramTool(IViewDiagramElementFinder cache) {
        var dropElementFromDiagram = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression("droppedElement", "dropElementFromDiagram",
                        List.of("droppedNode", "targetElement", "targetNode", IEditingContext.EDITING_CONTEXT, IDiagramContext.DIAGRAM_CONTEXT,
                                ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE)));

        return this.diagramBuilderHelper.newDropNodeTool()
                .name("Drop from Diagram")
                .acceptedNodeTypes(this.getDroppableNodes(cache).toArray(NodeDescription[]::new))
                .body(dropElementFromDiagram.build())
                .build();
    }
}
