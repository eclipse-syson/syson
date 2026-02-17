/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
import org.eclipse.syson.diagram.common.view.DescriptionFinder;
import org.eclipse.syson.diagram.common.view.services.ViewEdgeToolService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the port usage border node description in all diagrams.
 *
 * @author frouene
 */
public abstract class AbstractPortUsageBorderNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    protected final IDescriptionNameGenerator descriptionNameGenerator;

    protected final EReference eReference;

    public AbstractPortUsageBorderNodeDescriptionProvider(EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.eReference = Objects.requireNonNull(eReference);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    /**
     * Implementers should provide the expression used to retrieve all semantic candidates.
     *
     * @return the AQL expression to retrieve all semantic candidates for this node.
     */
    protected abstract String getSemanticCandidatesExpression();

    /**
     * Implementers should provide the list of edge tool descriptions used inside this {@link NodeDescription}.
     *
     * @param cache
     *         the cache used to retrieve node descriptions.
     * @param nodeDescription
     *         the actual Usage node description.
     * @return the list of edge tool descriptions.
     */
    protected abstract List<EdgeTool> getEdgeTools(IViewDiagramElementFinder cache, NodeDescription nodeDescription);

    protected abstract OutsideLabelDescription createOutsideLabelDescription();

    protected abstract NodePalette createNodePalette(IViewDiagramElementFinder cache, NodeDescription nodeDescription);

    protected abstract String getName();

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPortUsage());
        return this.diagramBuilderHelper.newNodeDescription()
                .defaultHeightExpression("10")
                .defaultWidthExpression("10")
                .domainType(domainType)
                .outsideLabels(this.createOutsideLabelDescription())
                .name(this.getName())
                .semanticCandidatesExpression(this.getSemanticCandidatesExpression())
                .style(this.createPortUnsetNodeStyle())
                .conditionalStyles(this.createPortUsageConditionalNodeStyles().toArray(ConditionalNodeStyle[]::new))
                .userResizable(UserResizableDirection.NONE)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.getName()).ifPresent(portUsageBorderNodeDescription -> portUsageBorderNodeDescription.setPalette(this.createNodePalette(cache, portUsageBorderNodeDescription)));
    }

    protected String getToolIconURLsExpression(String elementName) {
        return "/icons/full/obj16/" + elementName + ".svg";
    }

    protected NodeStyleDescription createPortUnsetNodeStyle() {
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

    protected List<ConditionalNodeStyle> createPortUsageConditionalNodeStyles() {
        var borderColor = this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR);
        return List.of(
                this.diagramBuilderHelper.newConditionalNodeStyle()
                        .condition(ServiceMethod.of0(UtilService::isInFeature).aqlSelf())
                        .style(this.createImageNodeStyleDescription("images/feature_in.svg", borderColor, true))
                        .build(),
                this.diagramBuilderHelper.newConditionalNodeStyle()
                        .condition(ServiceMethod.of0(UtilService::isOutFeature).aqlSelf())
                        .style(this.createImageNodeStyleDescription("images/feature_out.svg", borderColor, true))
                        .build(),
                this.diagramBuilderHelper.newConditionalNodeStyle()
                        .condition(ServiceMethod.of0(UtilService::isInOutFeature).aqlSelf())
                        .style(this.createImageNodeStyleDescription("images/feature_inout.svg", borderColor, true))
                        .build()
        );
    }

    protected EdgeTool createBindingConnectorAsUsageEdgeTool(IViewDiagramElementFinder cache) {
        return this.getViewEdgeToolService(cache).createBindingConnectorAsUsageEdgeTool(
                new DescriptionFinder(this.descriptionNameGenerator).getConnectableNodeDescriptions(cache.getNodeDescriptions(), SysmlPackage.eINSTANCE.getUsage()));
    }

    protected EdgeTool createConnectionUsageEdgeTool(IViewDiagramElementFinder cache) {
        return this.getViewEdgeToolService(cache)
                .createConnectionUsageEdgeTool(new DescriptionFinder(this.descriptionNameGenerator).getConnectableNodeDescriptions(cache.getNodeDescriptions(), SysmlPackage.eINSTANCE.getUsage()));
    }

    protected EdgeTool createFlowUsageEdgeTool(IViewDiagramElementFinder cache) {
        return this.getViewEdgeToolService(cache)
                .createFlowUsageEdgeTool(new DescriptionFinder(this.descriptionNameGenerator).getConnectableNodeDescriptions(cache.getNodeDescriptions(), SysmlPackage.eINSTANCE.getUsage()));
    }

    protected EdgeTool createInterfaceUsageEdgeTool(IViewDiagramElementFinder cache) {
        return this.getViewEdgeToolService(cache)
                .createInterfaceUsageEdgeTool(new DescriptionFinder(this.descriptionNameGenerator).getConnectableNodeDescriptions(cache.getNodeDescriptions(), SysmlPackage.eINSTANCE.getUsage()));
    }

    protected ViewEdgeToolService getViewEdgeToolService(IViewDiagramElementFinder cache) {
        return new ViewEdgeToolService(this.viewBuilderHelper, this.diagramBuilderHelper, cache.getNodeDescriptions(), this.descriptionNameGenerator);
    }
}
