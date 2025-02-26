/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.FreeFormLayoutStrategyDescriptionBuilder;
import org.eclipse.sirius.components.view.builder.generated.diagram.NodeToolSectionBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.HeaderSeparatorDisplayMode;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.LabelTextAlign;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.diagram.common.view.services.ViewEdgeToolSwitch;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.common.view.tools.NamespaceImportNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesFactory;
import org.eclipse.syson.sysmlcustomnodes.SysMLPackageNodeStyleDescription;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the package node description in all diagrams.
 *
 * @author arichard
 */
public abstract class AbstractPackageNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    protected final IDescriptionNameGenerator descriptionNameGenerator;

    private final ToolDescriptionService toolDescriptionService;

    public AbstractPackageNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
        this.toolDescriptionService = new ToolDescriptionService(descriptionNameGenerator);
    }

    /**
     * Implementers should provide the list of {@link NodeDescription} that is reused as child in that Package node
     * description.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the list of {@link NodeDescription} that are added as reused child.
     */
    protected abstract List<NodeDescription> getReusedChildren(IViewDiagramElementFinder cache);

    /**
     * Implementers should provide the list of {@link NodeDescription} that can be dropped inside this Package
     * {@link NodeDescription}.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the list of {@link NodeDescription} that can be dropped inside this package.
     */
    protected abstract List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache);

    /**
     * Implementers should provide the list of all {@link NodeDescription} defined in the diagram.<br>
     * This list is used to create edge tools associated to this package.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the list of all {@link NodeDescription} defined in the diagram.
     */
    protected abstract List<NodeDescription> getAllNodeDescriptions(IViewDiagramElementFinder cache);

    /**
     * Implementers should provide the list of tool section descriptions used inside this {@link NodeDescription}.
     *
     * @return the list of tool section descriptions.
     */
    protected abstract List<ToolSectionDescription> getToolSections();


    /**
     * Implementors may provide the list of custom tool to add to a given tool section.
     *
     * @param cache
     *         the cache used to retrieve node descriptions.
     * @param sectionName
     *         the name of the tool section in which the returned tools should be added.
     * @return the list of custom tool specific for the given tool section
     */
    protected List<NodeTool> addCustomTools(IViewDiagramElementFinder cache, String sectionName) {
        return List.of();
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPackage());
        return this.diagramBuilderHelper.newNodeDescription()
                .collapsible(true)
                .childrenLayoutStrategy(new FreeFormLayoutStrategyDescriptionBuilder().build())
                .defaultHeightExpression("101")
                .defaultWidthExpression("300")
                .domainType(domainType)
                .insideLabel(this.createInsideLabelDescription())
                .name(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage()))
                .semanticCandidatesExpression("aql:self.getAllReachable(" + domainType + ")")
                .style(this.createPackageNodeStyle())
                .userResizable(UserResizableDirection.BOTH)
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optPackageNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage()));
        NodeDescription packageNodeDescription = optPackageNodeDescription.get();
        diagramDescription.getNodeDescriptions().add(packageNodeDescription);

        packageNodeDescription.getReusedChildNodeDescriptions().addAll(this.getReusedChildren(cache));

        packageNodeDescription.setPalette(this.createNodePalette(packageNodeDescription, cache));
    }

    protected InsideLabelDescription createInsideLabelDescription() {
        return this.diagramBuilderHelper.newInsideLabelDescription()
                .labelExpression("aql:self.getContainerLabel()")
                .position(InsideLabelPosition.TOP_CENTER)
                .style(this.createInsideLabelStyle())
                .textAlign(LabelTextAlign.CENTER)
                .build();
    }

    protected InsideLabelStyle createInsideLabelStyle() {
        return this.diagramBuilderHelper.newInsideLabelStyle()
                .borderSize(0)
                .headerSeparatorDisplayMode(HeaderSeparatorDisplayMode.NEVER)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIconExpression(AQLUtils.getSelfServiceCallExpression("showIcon"))
                .withHeader(false)
                .build();
    }

    protected NodeStyleDescription createPackageNodeStyle() {
        SysMLPackageNodeStyleDescription nodeStyleDescription = SysMLCustomnodesFactory.eINSTANCE.createSysMLPackageNodeStyleDescription();
        nodeStyleDescription.setBorderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR));
        nodeStyleDescription.setBorderRadius(0);
        nodeStyleDescription.setBackground(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR));
        return nodeStyleDescription;
    }

    private NodePalette createNodePalette(NodeDescription nodeDescription, IViewDiagramElementFinder cache) {
        var changeContext = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.deleteFromModel()");

        var deleteTool = this.diagramBuilderHelper.newDeleteTool()
                .name("Delete from Model")
                .body(changeContext.build());

        var callEditService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF + ".directEditNode(newLabel)");

        var editTool = this.diagramBuilderHelper.newLabelEditTool()
                .name("Edit")
                .initialDirectEditLabelExpression(AQLConstants.AQL_SELF + ".getDefaultInitialDirectEditLabel()")
                .body(callEditService.build());

        var edgeTools = new ArrayList<>(this.getEdgeTools(nodeDescription, cache));

        var nodesWithoutSection = new ArrayList<>();
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getComment())).ifPresent(nodeComment -> {
            nodesWithoutSection.add(this.createNodeTool(nodeComment, SysmlPackage.eINSTANCE.getComment()));
        });

        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .labelEditTool(editTool.build())
                .dropNodeTool(this.createDropFromDiagramTool(cache))
                .toolSections(this.createToolSections(cache))
                .nodeTools(nodesWithoutSection.toArray(NodeTool[]::new))
                .edgeTools(edgeTools.toArray(EdgeTool[]::new))
                .build();
    }

    private List<EdgeTool> getEdgeTools(NodeDescription nodeDescription, IViewDiagramElementFinder cache) {
        ViewEdgeToolSwitch edgeToolSwitch = new ViewEdgeToolSwitch(nodeDescription, this.getAllNodeDescriptions(cache), this.descriptionNameGenerator);
        return edgeToolSwitch.doSwitch(SysmlPackage.eINSTANCE.getPackage());
    }

    private DropNodeTool createDropFromDiagramTool(IViewDiagramElementFinder cache) {
        var dropElementFromDiagram = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression("droppedElement", "dropElementFromDiagram",
                        List.of("droppedNode", "targetElement", "targetNode", IEditingContext.EDITING_CONTEXT, IDiagramContext.DIAGRAM_CONTEXT, "convertedNodes")));

        return this.diagramBuilderHelper.newDropNodeTool()
                .name("Drop from Diagram")
                .acceptedNodeTypes(this.getDroppableNodes(cache).toArray(NodeDescription[]::new))
                .body(dropElementFromDiagram.build())
                .build();
    }

    private NodeToolSection[] createToolSections(IViewDiagramElementFinder cache) {
        var sections = new ArrayList<NodeToolSection>();

        this.getToolSections().forEach(sectionTool -> {
            NodeToolSectionBuilder sectionBuilder = this.diagramBuilderHelper.newNodeToolSection()
                    .name(sectionTool.name())
                    .nodeTools(this.createElementsOfToolSection(cache, sectionTool.name(), sectionTool.elements()));
            sections.add(sectionBuilder.build());
        });

        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        sections.add(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection());

        return sections.toArray(NodeToolSection[]::new);
    }

    private NodeTool[] createElementsOfToolSection(IViewDiagramElementFinder cache, String toolSectionName, List<EClass> elements) {
        var nodeTools = new ArrayList<NodeTool>();

        elements.forEach(definition -> {
            cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(definition)).ifPresent(nodeDescription -> {
                nodeTools.add(this.createNodeTool(nodeDescription, definition));
            });
        });

        nodeTools.addAll(this.addCustomTools(cache, toolSectionName));

        nodeTools.sort((nt1, nt2) -> nt1.getName().compareTo(nt2.getName()));

        return nodeTools.toArray(NodeTool[]::new);
    }

    private NodeTool createNodeTool(NodeDescription nodeDescription, EClass eClass) {
        if (SysmlPackage.eINSTANCE.getNamespaceImport().equals(eClass)) {
            return new NamespaceImportNodeToolProvider(nodeDescription, this.descriptionNameGenerator).create(null);
        }

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance.elementInitializer()");

        var parentViewExpression = "aql:selectedNode";
        if (SysmlPackage.eINSTANCE.getComment().equals(eClass)) {
            // when a comment is Created from a Package Node, the new Comment node should be represented outside of the
            // Package
            parentViewExpression = AQLUtils.getSelfServiceCallExpression("getParentNode", List.of("selectedNode", IDiagramContext.DIAGRAM_CONTEXT));
        }

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(NodeContainmentKind.CHILD_NODE)
                .elementDescription(nodeDescription)
                .parentViewExpression(parentViewExpression)
                .semanticElementExpression("aql:newInstance")
                .variableName("newInstanceView");

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(createView.build(), changeContextNewInstance.build());

        var changeContexMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newOwningMembership")
                .children(createEClassInstance.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getOwningMembership()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newOwningMembership")
                .children(changeContexMembership.build());

        return this.diagramBuilderHelper.newNodeTool()
                .name(this.descriptionNameGenerator.getCreationToolName(eClass))
                .iconURLsExpression("/icons/full/obj16/" + eClass.getName() + ".svg")
                .body(createMembership.build())
                .elementsToSelectExpression("aql:newInstance")
                .build();
    }
}
