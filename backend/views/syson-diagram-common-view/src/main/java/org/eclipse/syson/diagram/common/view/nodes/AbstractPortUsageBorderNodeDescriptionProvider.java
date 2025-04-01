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
package org.eclipse.syson.diagram.common.view.nodes;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ConditionalNodeStyle;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.OutsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.OutsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.OutsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the port usage border node description in all diagrams.
 *
 * @author frouene
 */
public abstract class AbstractPortUsageBorderNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    protected final IDescriptionNameGenerator nameGenerator;

    public AbstractPortUsageBorderNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(colorProvider);
        this.nameGenerator = Objects.requireNonNull(nameGenerator);
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
        var optPortUsageBorderNodeDescription = cache.getNodeDescription(this.getName());

        NodeDescription nodeDescription = optPortUsageBorderNodeDescription.get();
        nodeDescription.setPalette(this.createNodePalette(cache, nodeDescription));
    }

    public String getName() {
        return this.nameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage());
    }

    private NodeStyleDescription createPortUnsetNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .background(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .build();
    }

    private OutsideLabelDescription createOutsideLabelDescription() {
        return this.diagramBuilderHelper.newOutsideLabelDescription()
                .labelExpression(AQLUtils.getSelfServiceCallExpression("getBorderNodeUsageLabel"))
                .position(OutsideLabelPosition.BOTTOM_CENTER)
                .style(this.createOutsideLabelStyle())
                .build();
    }

    private OutsideLabelStyle createOutsideLabelStyle() {
        return this.diagramBuilderHelper.newOutsideLabelStyle()
                .bold(false)
                .borderSize(0)
                .fontSize(12)
                .italic(false)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIconExpression("aql:false")
                .strikeThrough(false)
                .underline(false)
                .build();
    }

    private List<ConditionalNodeStyle> createPortUsageConditionalNodeStyles() {
        var borderColor = this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR);
        return List.of(
                this.diagramBuilderHelper.newConditionalNodeStyle()
                        .condition(AQLUtils.getSelfServiceCallExpression("isInFeature"))
                        .style(this.createImageNodeStyleDescription("images/feature_in.svg", borderColor, true))
                        .build(),
                this.diagramBuilderHelper.newConditionalNodeStyle()
                        .condition(AQLUtils.getSelfServiceCallExpression("isOutFeature"))
                        .style(this.createImageNodeStyleDescription("images/feature_out.svg", borderColor, true))
                        .build(),
                this.diagramBuilderHelper.newConditionalNodeStyle()
                        .condition(AQLUtils.getSelfServiceCallExpression("isInOutFeature"))
                        .style(this.createImageNodeStyleDescription("images/feature_inout.svg", borderColor, true))
                        .build()
        );
    }

    private NodePalette createNodePalette(IViewDiagramElementFinder cache, NodeDescription nodeDescription) {
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


        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .labelEditTool(editTool.build())
                .toolSections(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection())
                .edgeTools(this.getEdgeTools(cache, nodeDescription).toArray(EdgeTool[]::new))
                .build();
    }

}
