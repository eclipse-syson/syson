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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.HeaderSeparatorDisplayMode;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.LabelTextAlign;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.diagram.common.view.services.description.ReferencingPerformActionUsageNodeDescriptionService;
import org.eclipse.syson.diagram.common.view.services.description.ToolConstants;
import org.eclipse.syson.diagram.common.view.tools.AcceptActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ActionFlowCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.DecisionActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.DoneActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ForkActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.JoinActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.MergeActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.PerformActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ReferencingPerformActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StartActionNodeToolProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the 'action flow' free form compartment that contains action usages.
 *
 * @author Jerome Gout
 */
public class ActionFlowCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public static final String COMPARTMENT_LABEL = "action flow";

    private final String name;

    public ActionFlowCompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
        this.name = descriptionNameGenerator.getFreeFormCompartmentName(this.eClass, this.eReference);
    }

    @Override
    public NodeDescription create() {
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenLayoutStrategy(this.diagramBuilderHelper.newFreeFormLayoutStrategyDescription().build())
                .defaultHeightExpression("150")
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement()))
                .insideLabel(this.createInsideLabelDescription())
                .isHiddenByDefaultExpression("aql:true")
                .name(this.name)
                .semanticCandidatesExpression(AQLConstants.AQL_SELF)
                .style(this.createCompartmentNodeStyle())
                .userResizable(UserResizableDirection.NONE)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.name).ifPresent(nodeDescription -> {
            cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getComment())).ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
            cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getActionUsage())).ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
            cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getAcceptActionUsage())).ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
            cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getAssignmentActionUsage())).ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
            cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPerformActionUsage())).ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
            cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(StartActionNodeDescriptionProvider.START_ACTION_NAME)).ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
            cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(DoneActionNodeDescriptionProvider.DONE_ACTION_NAME)).ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
            cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(JoinActionNodeDescriptionProvider.JOIN_ACTION_NAME)).ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
            cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(ForkActionNodeDescriptionProvider.FORK_ACTION_NAME)).ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
            cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(MergeActionNodeDescriptionProvider.MERGE_ACTION_NAME)).ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
            cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(DecisionActionNodeDescriptionProvider.DECISION_ACTION_NAME)).ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
            cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(ReferencingPerformActionUsageNodeDescriptionService.REFERENCING_PERFORM_ACTION_NAME))
                    .ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
            nodeDescription.setPalette(this.createCompartmentPalette(cache));
        });
    }

    @Override
    protected NodePalette createCompartmentPalette(IViewDiagramElementFinder cache) {
        var palette = this.diagramBuilderHelper.newNodePalette()
                .dropNodeTool(this.createCompartmentDropFromDiagramTool(cache));

        var toolSections = this.toolDescriptionService.createDefaultNodeToolSections();

        this.getItemCreationToolProviders().forEach(toolProvider -> {
            var nodeTool = toolProvider.create(cache);
            var eType = this.eReference.getEType();
            var toolSectionName = this.toolDescriptionService.getToolSectionName(eType);
            this.toolDescriptionService.addNodeTool(toolSections, toolSectionName, nodeTool);
        });
        this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.BEHAVIOR, new StartActionNodeToolProvider(this.eClass, this.getDescriptionNameGenerator()).create(cache));
        this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.BEHAVIOR, new AcceptActionNodeToolProvider(this.eClass, this.getDescriptionNameGenerator()).create(cache));
        this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.BEHAVIOR, new DecisionActionNodeToolProvider(this.eClass, this.getDescriptionNameGenerator()).create(cache));
        this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.BEHAVIOR, new ForkActionNodeToolProvider(this.eClass, this.getDescriptionNameGenerator()).create(cache));
        this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.BEHAVIOR, new JoinActionNodeToolProvider(this.eClass, this.getDescriptionNameGenerator()).create(cache));
        this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.BEHAVIOR, new MergeActionNodeToolProvider(this.eClass, this.getDescriptionNameGenerator()).create(cache));
        this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.BEHAVIOR, new DoneActionNodeToolProvider(this.eClass, this.getDescriptionNameGenerator()).create(cache));
        this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.BEHAVIOR, new ReferencingPerformActionNodeToolProvider(this.eClass, this.getDescriptionNameGenerator()).create(cache));
        this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.BEHAVIOR, new PerformActionNodeToolProvider(this.eClass, this.getDescriptionNameGenerator()).create(cache));

        toolSections.add(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection());
        toolSections.add(this.toolDescriptionService.relatedElementsNodeToolSection(false));
        this.toolDescriptionService.removeEmptyNodeToolSections(toolSections);

        return palette.toolSections(toolSections.toArray(NodeToolSection[]::new))
                .build();
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        List<NodeDescription> droppableNodes = new ArrayList<>();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getActionUsage())).ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getAcceptActionUsage())).ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPerformActionUsage())).ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, this.eReference)).ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(JoinActionNodeDescriptionProvider.JOIN_ACTION_NAME)).ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(ForkActionNodeDescriptionProvider.FORK_ACTION_NAME)).ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(MergeActionNodeDescriptionProvider.MERGE_ACTION_NAME)).ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(DecisionActionNodeDescriptionProvider.DECISION_ACTION_NAME)).ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(ReferencingPerformActionUsageNodeDescriptionService.REFERENCING_PERFORM_ACTION_NAME)).ifPresent(droppableNodes::add);
        return droppableNodes;
    }

    @Override
    protected InsideLabelDescription createInsideLabelDescription() {
        return this.diagramBuilderHelper.newInsideLabelDescription()
                .labelExpression(COMPARTMENT_LABEL)
                .position(InsideLabelPosition.TOP_CENTER)
                .style(this.createInsideLabelStyle())
                .textAlign(LabelTextAlign.CENTER)
                .build();
    }

    @Override
    protected InsideLabelStyle createInsideLabelStyle() {
        return this.diagramBuilderHelper.newInsideLabelStyle()
                .borderSize(0)
                .headerSeparatorDisplayMode(HeaderSeparatorDisplayMode.NEVER)
                .fontSize(12)
                .italic(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIconExpression("aql:false")
                .withHeader(true)
                .build();
    }

    @Override
    protected List<INodeToolProvider> getItemCreationToolProviders() {
        List<INodeToolProvider> creationToolProviders = new ArrayList<>();
        creationToolProviders.add(new ActionFlowCompartmentNodeToolProvider());
        return creationToolProviders;
    }
}
