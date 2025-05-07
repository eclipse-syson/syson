/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.FreeFormLayoutStrategyDescriptionBuilder;
import org.eclipse.sirius.components.view.builder.generated.diagram.NodeToolSectionBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.HeaderSeparatorDisplayMode;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.LabelOverflowStrategy;
import org.eclipse.sirius.components.view.diagram.LabelTextAlign;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.Tool;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.diagram.common.view.nodes.AbstractNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.NamespaceImportNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesFactory;
import org.eclipse.syson.sysmlcustomnodes.SysMLViewFrameNodeStyleDescription;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Node description provider for View Usage elements in the General View diagram.
 *
 * @author frouene
 */
public class ViewUsageNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    protected final IDescriptionNameGenerator descriptionNameGenerator;

    public ViewUsageNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getViewUsage());
        return this.diagramBuilderHelper.newNodeDescription()
                .collapsible(true)
                .childrenLayoutStrategy(new FreeFormLayoutStrategyDescriptionBuilder().build())
                .defaultHeightExpression("101")
                .defaultWidthExpression("300")
                .domainType(domainType)
                .insideLabel(this.createInsideLabelDescription())
                .name(this.getNodeDescriptionName())
                .semanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getAllReachable", domainType))
                .style(this.createViewFrameNodeStyle())
                .userResizable(UserResizableDirection.BOTH)
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.getNodeDescriptionName()).ifPresent(viewUsageNodeDescription -> {
            viewUsageNodeDescription.setPalette(this.createNodePalette(viewUsageNodeDescription, cache));

            viewUsageNodeDescription.getReusedChildNodeDescriptions().addAll(this.getReusedChildren(cache));

            diagramDescription.getNodeDescriptions().add(viewUsageNodeDescription);
        });
    }

    private String getNodeDescriptionName() {
        return this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getViewUsage());
    }

    protected NodeStyleDescription createViewFrameNodeStyle() {

        SysMLViewFrameNodeStyleDescription nodeStyleDescription = SysMLCustomnodesFactory.eINSTANCE.createSysMLViewFrameNodeStyleDescription();
        nodeStyleDescription.setBorderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR));
        nodeStyleDescription.setBorderRadius(10);
        nodeStyleDescription.setBackground(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR));
        return nodeStyleDescription;
    }

    protected InsideLabelDescription createInsideLabelDescription() {
        return this.diagramBuilderHelper.newInsideLabelDescription()
                .labelExpression(AQLUtils.getSelfServiceCallExpression("getContainerLabel"))
                .position(InsideLabelPosition.TOP_CENTER)
                .style(this.createInsideLabelStyle())
                .textAlign(LabelTextAlign.CENTER)
                .overflowStrategy(LabelOverflowStrategy.ELLIPSIS)
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

        var nodesWithoutSection = new ArrayList<>();
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getComment()))
                .ifPresent(nodeComment -> nodesWithoutSection.add(this.createNodeTool(nodeComment, SysmlPackage.eINSTANCE.getComment())));
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getDocumentation()))
                .ifPresent(nodeDocumentation -> nodesWithoutSection.add(this.createNodeTool(nodeDocumentation, SysmlPackage.eINSTANCE.getDocumentation())));
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getTextualRepresentation()))
                .ifPresent(nodeTextualRepresentation -> nodesWithoutSection.add(this.createNodeTool(nodeTextualRepresentation, SysmlPackage.eINSTANCE.getTextualRepresentation())));

        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .labelEditTool(editTool.build())
                .toolSections(this.createToolSections(cache))
                .nodeTools(nodesWithoutSection.toArray(NodeTool[]::new))
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
        sections.add(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection());

        return sections.toArray(NodeToolSection[]::new);
    }

    private List<NodeDescription> getReusedChildren(IViewDiagramElementFinder cache) {
        var reusedChildren = new ArrayList<NodeDescription>();

        GeneralViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(definition)).ifPresent(reusedChildren::add));
        GeneralViewDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(usage)).ifPresent(reusedChildren::add));
        GeneralViewDiagramDescriptionProvider.ANNOTATINGS.forEach(annotating -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(annotating)).ifPresent(reusedChildren::add));
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(reusedChildren::add);
        return reusedChildren;
    }

    private List<ToolSectionDescription> getToolSections() {
        return List.of(
                GeneralViewDiagramDescriptionProvider.REQUIREMENTS_TOOL_SECTIONS,
                GeneralViewDiagramDescriptionProvider.STRUCTURE_TOOL_SECTIONS,
                GeneralViewDiagramDescriptionProvider.BEHAVIOR_TOOL_SECTIONS,
                GeneralViewDiagramDescriptionProvider.ANALYSIS_TOOL_SECTIONS,
                GeneralViewDiagramDescriptionProvider.EXTENSION_TOOL_SECTIONS);
    }

    private NodeTool[] createElementsOfToolSection(IViewDiagramElementFinder cache, String toolSectionName, List<EClass> elements) {
        var nodeTools = new ArrayList<NodeTool>();

        elements.forEach(definition -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(definition))
                .ifPresent(nodeDescription -> nodeTools.add(this.createNodeTool(nodeDescription, definition))));


        nodeTools.sort(Comparator.comparing(Tool::getName));

        return nodeTools.toArray(NodeTool[]::new);
    }

    private NodeTool createNodeTool(NodeDescription nodeDescription, EClass eClass) {
        if (SysmlPackage.eINSTANCE.getNamespaceImport().equals(eClass)) {
            return new NamespaceImportNodeToolProvider(nodeDescription, this.descriptionNameGenerator).create(null);
        }

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance.elementInitializer()");

        var parentViewExpression = "aql:selectedNode";
        if (SysmlPackage.eINSTANCE.getComment().equals(eClass) || SysmlPackage.eINSTANCE.getDocumentation().equals(eClass) || SysmlPackage.eINSTANCE.getTextualRepresentation().equals(eClass)) {
            // when a comment, documentation or textualRepresentation is created, the new node should be represented outside
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

        var changeContextMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newOwningMembership")
                .children(createEClassInstance.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getOwningMembership()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newOwningMembership")
                .children(changeContextMembership.build());

        var changeContextViewUsageOwner = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("getViewUsageOwner"))
                .children(createMembership.build());

        return this.diagramBuilderHelper.newNodeTool()
                .name(this.descriptionNameGenerator.getCreationToolName(eClass))
                .iconURLsExpression("/icons/full/obj16/" + eClass.getName() + ".svg")
                .body(changeContextViewUsageOwner.build())
                .elementsToSelectExpression("aql:newInstance")
                .build();
    }
}
