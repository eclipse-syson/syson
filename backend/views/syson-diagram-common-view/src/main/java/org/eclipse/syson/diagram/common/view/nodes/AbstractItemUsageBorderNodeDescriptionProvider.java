/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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

import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ConditionalNodeStyle;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.OutsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.OutsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.diagram.common.view.services.ViewEdgeToolService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Common behavior for all item usage border node description.
 *
 * @author gcoutabe
 */
public abstract class AbstractItemUsageBorderNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    protected final IDescriptionNameGenerator descriptionNameGenerator;

    protected final EReference eReference;

    public AbstractItemUsageBorderNodeDescriptionProvider(EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.eReference = Objects.requireNonNull(eReference);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    protected abstract String getSemanticCandidatesExpression();

    protected abstract String getName();

    protected abstract OutsideLabelDescription createOutsideLabelDescription();

    protected abstract List<EdgeTool> getEdgeTools(IViewDiagramElementFinder cache, NodeDescription nodeDescription);

    protected abstract NodePalette createNodePalette(IViewDiagramElementFinder cache, NodeDescription nodeDescription);

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getItemUsage());
        return this.diagramBuilderHelper.newNodeDescription()
                .defaultHeightExpression("10")
                .defaultWidthExpression("10")
                .domainType(domainType)
                .outsideLabels(this.createOutsideLabelDescription())
                .name(this.getName())
                .semanticCandidatesExpression(this.getSemanticCandidatesExpression())
                .style(this.createItemUnsetNodeStyle())
                .conditionalStyles(this.createItemUsageConditionalNodeStyles().toArray(ConditionalNodeStyle[]::new))
                .userResizable(UserResizableDirection.NONE)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    protected IDescriptionNameGenerator getDescriptionNameGenerator() {
        return this.descriptionNameGenerator;
    }

    private NodeStyleDescription createItemUnsetNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .background(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .build();
    }

    protected OutsideLabelStyle createOutsideLabelStyle() {
        return this.diagramBuilderHelper.newOutsideLabelStyle()
                .bold(false)
                .borderSize(0)
                .fontSize(12)
                .italic(false)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIconExpression(AQLConstants.AQL_FALSE)
                .strikeThrough(false)
                .underline(false)
                .build();
    }

    private List<ConditionalNodeStyle> createItemUsageConditionalNodeStyles() {
        var borderColor = this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR);
        return List.of(
                this.diagramBuilderHelper.newConditionalNodeStyle()
                        .condition(ServiceMethod.of0(UtilService::isInFeature).aqlSelf())
                        .style(this.createImageNodeStyleDescription("/images/feature_in.svg", borderColor, true))
                        .build(),
                this.diagramBuilderHelper.newConditionalNodeStyle()
                        .condition(ServiceMethod.of0(UtilService::isOutFeature).aqlSelf())
                        .style(this.createImageNodeStyleDescription("/images/feature_out.svg", borderColor, true))
                        .build(),
                this.diagramBuilderHelper.newConditionalNodeStyle()
                        .condition(ServiceMethod.of0(UtilService::isInOutFeature).aqlSelf())
                        .style(this.createImageNodeStyleDescription("/images/feature_inout.svg", borderColor, true))
                        .build());
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optItemUsageBorderNodeDescription = cache.getNodeDescription(this.getName());

        NodeDescription nodeDescription = optItemUsageBorderNodeDescription.get();
        nodeDescription.setPalette(this.createNodePalette(cache, nodeDescription));
    }

    private List<NodeDescription> getBindingConnectorAsUsageToolTarget(IViewDiagramElementFinder cache) {
        var nodeDescriptions = new ArrayList<NodeDescription>();
        cache.getNodeDescription(this.getName()).ifPresent(nodeDescriptions::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), this.eReference)).ifPresent(nodeDescriptions::add);
        return nodeDescriptions;
    }

    protected EdgeTool createBindingConnectorAsUsageEdgeTool(IViewDiagramElementFinder cache) {
        return this.getViewEdgeToolService(cache).createBindingConnectorAsUsageEdgeTool(this.getBindingConnectorAsUsageToolTarget(cache));
    }

    protected EdgeTool createFlowUsageEdgeTool(IViewDiagramElementFinder cache) {
        return this.getViewEdgeToolService(cache).createFlowUsageEdgeTool(this.getFlowUsageToolTargetDescriptions(cache, this.descriptionNameGenerator));
    }

    protected ViewEdgeToolService getViewEdgeToolService(IViewDiagramElementFinder cache) {
        return new ViewEdgeToolService(this.viewBuilderHelper, this.diagramBuilderHelper, cache.getNodeDescriptions(), this.descriptionNameGenerator);
    }

}
