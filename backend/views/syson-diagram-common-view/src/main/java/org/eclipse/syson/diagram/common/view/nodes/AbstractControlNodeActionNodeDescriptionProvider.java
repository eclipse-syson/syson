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
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.description.EdgeDescription;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.OutsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.OutsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.OutsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to gather all common parts of Control node description of an Action.
 *
 * @author Jerome Gout
 */
public abstract class AbstractControlNodeActionNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public AbstractControlNodeActionNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    /**
     * Implementers should provide the name of the actual node description.
     *
     * @return the String representing the name of the actual control node node description.
     */
    protected abstract String getNodeDescriptionName();

    /**
     * Implementers should provide the domain type {@link EClass} associated to the actual node description.
     *
     * @return The {@link EClass} representing the semantic element of the node description.
     */
    protected abstract EClass getDomainType();

    /**
     * Implementers should provide the path of the image associated to the actual node description.
     *
     * @return The String representing the path to access the image associated to the node description.
     */
    protected abstract String getImagePath();

    /**
     * Implementers should provide the label used by the removal tool of the actual node description.
     *
     * @return the String representing the label of the removal tool of the node description.
     */
    protected abstract String getRemoveToolLabel();

    protected String getDefaultWidthExpression() {
        return "100";
    }

    protected String getDefaultHeightExpression() {
        return "12";
    }

    protected boolean isNodeResizable() {
        return true;
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(this.getDomainType());
        return this.diagramBuilderHelper.newNodeDescription()
                .collapsible(false)
                .domainType(domainType)
                .defaultWidthExpression(this.getDefaultWidthExpression())
                .defaultHeightExpression(this.getDefaultHeightExpression())
                .outsideLabels(this.createOutsideLabelDescription())
                .name(this.descriptionNameGenerator.getNodeName(this.getNodeDescriptionName()))
                .semanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getAllReachable", domainType))
                .style(this.createImageNodeStyleDescription(this.getImagePath()))
                .userResizable(this.isNodeResizable())
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    private OutsideLabelDescription createOutsideLabelDescription() {
        return this.diagramBuilderHelper.newOutsideLabelDescription()
                .labelExpression("aql:self.name")
                .position(OutsideLabelPosition.BOTTOM_CENTER)
                .style(this.createOutsideLabelStyle())
                .build();
    }

    private OutsideLabelStyle createOutsideLabelStyle() {
        return this.diagramBuilderHelper.newOutsideLabelStyle()
                .borderSize(0)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(false)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(this.getNodeDescriptionName())).ifPresent(nodeDescription -> {
            nodeDescription.setPalette(this.createNodePalette(cache));
            diagramDescription.getNodeDescriptions().add(nodeDescription);
        });
    }

    private NodePalette createNodePalette(IViewDiagramElementFinder cache) {
        var changeContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("deleteFromModel"));

        var deleteTool = this.diagramBuilderHelper.newDeleteTool()
                .name(this.getRemoveToolLabel())
                .body(changeContext.build());

        var edgeTools = new ArrayList<EdgeTool>();
        edgeTools.addAll(this.getEdgeTools(cache));

        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .edgeTools(edgeTools.toArray(EdgeTool[]::new))
                .toolSections(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection())
                .build();
    }

    private List<EdgeTool> getEdgeTools(IViewDiagramElementFinder cache) {
        var targetElementDescriptions = this.getForkTargetDescriptions(cache);

        var builder = this.diagramBuilderHelper.newEdgeTool();
        var params = List.of(EdgeDescription.SEMANTIC_EDGE_TARGET, EdgeDescription.EDGE_SOURCE, IEditingContext.EDITING_CONTEXT, IDiagramService.DIAGRAM_SERVICES);
        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, "createSuccessionEdge", params));

        var createForkEdgeTool = builder.name(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getSuccession()))
                .iconURLsExpression("/icons/full/obj16/Succession.svg")
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();

        return List.of(createForkEdgeTool);
    }

    private List<NodeDescription> getForkTargetDescriptions(IViewDiagramElementFinder cache) {
        var targets = new ArrayList<NodeDescription>();
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage())).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionDefinition())).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAcceptActionUsage())).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAssignmentActionUsage())).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(JoinActionNodeDescriptionProvider.JOIN_ACTION_NAME)).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(ForkActionNodeDescriptionProvider.FORK_ACTION_NAME)).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(MergeActionNodeDescriptionProvider.MERGE_ACTION_NAME)).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(DecisionActionNodeDescriptionProvider.DECISION_ACTION_NAME)).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(DoneActionNodeDescriptionProvider.DONE_ACTION_NAME)).ifPresent(targets::add);
        return targets;
    }
}
