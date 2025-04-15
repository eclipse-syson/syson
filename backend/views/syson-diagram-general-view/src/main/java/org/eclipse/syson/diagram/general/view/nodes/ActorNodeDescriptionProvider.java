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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.List;
import java.util.Set;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.LabelTextAlign;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.OutsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.OutsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Node description provider for actor {@link PartUsage} in the General View diagram.
 * <p>
 * Actor {@link PartUsage} have a specific representation with a stick figure and no compartments.
 * <p>
 *
 * @author gdaniel
 */
public class ActorNodeDescriptionProvider extends UsageNodeDescriptionProvider {

    public static final String NAME = "GV Node Actor";

    public ActorNodeDescriptionProvider(IColorProvider colorProvider) {
        super(SysmlPackage.eINSTANCE.getPartUsage(), colorProvider);
    }

    @Override
    protected String createPreconditionExpression() {
        return AQLUtils.getSelfServiceCallExpression("isActor()");
    }

    @Override
    protected String getNodeDescriptionName() {
        return NAME;
    }

    @Override
    protected InsideLabelDescription createInsideLabelDescription() {
        // Actors don't have an inside label, they are represented as an image with an outside label.
        return null;
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(this.eClass);
        return this.diagramBuilderHelper.newNodeDescription()
                .collapsible(true)
                // Use the same ratio as the actor.svg image to make sure the keepAspectRatio property works as
                // expected.
                .defaultHeightExpression("100")
                .defaultWidthExpression("70")
                .domainType(domainType)
                .insideLabel(this.createInsideLabelDescription())
                .outsideLabels(this.createOutsideLabelDescriptions().toArray(OutsideLabelDescription[]::new))
                .name(this.getNodeDescriptionName())
                .semanticCandidatesExpression(this.getSemanticCandidatesExpression(domainType))
                .preconditionExpression(this.createPreconditionExpression())
                .style(this.createActorNodeStyle())
                .userResizable(UserResizableDirection.BOTH)
                .keepAspectRatio(true)
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    @Override
    protected List<OutsideLabelDescription> createOutsideLabelDescriptions() {
        return List.of(this.diagramBuilderHelper.newOutsideLabelDescription()
                .labelExpression(AQLUtils.getSelfServiceCallExpression("getContainerLabel"))
                .position(OutsideLabelPosition.BOTTOM_CENTER)
                .textAlign(LabelTextAlign.CENTER)
                .style(this.diagramBuilderHelper.newOutsideLabelStyle()
                        .borderSize(0)
                        .borderSize(0)
                        .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                        .showIconExpression(AQLUtils.getSelfServiceCallExpression("showIcon"))
                        .build())
                .build());
    }

    private NodeStyleDescription createActorNodeStyle() {
        return this.diagramBuilderHelper.newImageNodeStyleDescription()
                .borderSize(0)
                .shape("images/actor.svg")
                .build();
    }

    @Override
    protected String getSemanticCandidatesExpression(String domainType) {
        return AQLUtils.getSelfServiceCallExpression("getAllReachableActors");
    }

    @Override
    protected Set<NodeDescription> getReusedChildren(IViewDiagramElementFinder cache) {
        // No reused children to avoid the creation of graphical elements inside the image node.
        return Set.of();
    }

    @Override
    protected Set<NodeDescription> getReusedBorderNodes(IViewDiagramElementFinder cache) {
        // No reused children to avoid the creation of graphical elements inside the image node.
        return Set.of();
    }

    @Override
    protected List<NodeToolSection> getToolSections(NodeDescription nodeDescription, IViewDiagramElementFinder cache) {
        // No tool section to avoid the creation of graphical elements inside the image node.
        return List.of();
    }

}
