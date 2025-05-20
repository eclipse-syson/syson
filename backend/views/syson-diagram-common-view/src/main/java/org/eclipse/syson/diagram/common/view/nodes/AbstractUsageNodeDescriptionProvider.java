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
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.NodeDescriptionBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
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
import org.eclipse.sirius.components.view.diagram.OutsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.ToolSection;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
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

    private String precondition;

    public AbstractUsageNodeDescriptionProvider(EClass eClass, IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(colorProvider);
        this.eClass = Objects.requireNonNull(eClass);
        this.descriptionNameGenerator = Objects.requireNonNull(nameGenerator);
        this.utilServices = new UtilService();
    }

    /**
     * Implementers might provide the name of the Usage node description.<br>
     * Default implementation returns the name based on the name of the semantic type.
     *
     * @return the name of the Usage node description
     */
    protected String getNodeDescriptionName() {
        return this.getDescriptionNameGenerator().getNodeName(this.eClass);
    }

    /**
     * Set an optional precondition for the future node.
     *
     * @param aPrecondition
     *            a precondition (or <code>null</code> to unset the precondition).
     * @return this for convenience
     */
    public AbstractUsageNodeDescriptionProvider setPrecondition(String aPrecondition) {
        this.precondition = aPrecondition;
        return this;
    }

    /**
     * Implementers should provide the set of {@link NodeDescription} that is reused as child in that Usage node
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

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(this.eClass);
        NodeDescriptionBuilder builder = this.diagramBuilderHelper.newNodeDescription()
                .collapsible(true)
                .defaultHeightExpression(ViewConstants.DEFAULT_CONTAINER_NODE_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(domainType)
                .insideLabel(this.createInsideLabelDescription())
                .outsideLabels(this.createOutsideLabelDescriptions().toArray(OutsideLabelDescription[]::new))
                .name(this.getNodeDescriptionName())
                .semanticCandidatesExpression(this.getSemanticCandidatesExpression(domainType))
                .preconditionExpression(this.createPreconditionExpression())
                .style(this.createUsageNodeStyle())
                .userResizable(UserResizableDirection.BOTH)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED);
        if (this.precondition != null) {
            builder.preconditionExpression(this.precondition);
        }

        return builder.build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.getNodeDescriptionName()).ifPresent(nodeDescription -> {
            diagramDescription.getNodeDescriptions().add(nodeDescription);
            nodeDescription.getReusedChildNodeDescriptions().addAll(this.getReusedChildren(cache));
            nodeDescription.getReusedBorderNodeDescriptions().addAll(this.getReusedBorderNodes(cache));
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

    protected List<OutsideLabelDescription> createOutsideLabelDescriptions() {
        return List.of();
    }

    protected String createPreconditionExpression() {
        return null;
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
                .nodeTools(toolsWithoutSection.toArray(NodeTool[]::new))
                .toolSections(toolSections.toArray(NodeToolSection[]::new))
                .build();
    }

    private List<EdgeTool> getEdgeTools(NodeDescription nodeDescription, IViewDiagramElementFinder cache) {
        ViewEdgeToolSwitch edgeToolSwitch = new ViewEdgeToolSwitch(nodeDescription, this.getAllNodeDescriptions(cache), this.getDescriptionNameGenerator());
        return edgeToolSwitch.doSwitch(this.eClass);
    }
}
