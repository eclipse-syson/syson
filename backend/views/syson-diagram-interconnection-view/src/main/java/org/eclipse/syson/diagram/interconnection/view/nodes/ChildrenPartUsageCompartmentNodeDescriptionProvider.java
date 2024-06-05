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
package org.eclipse.syson.diagram.interconnection.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.common.view.nodes.AbstractCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.tools.ChildrenPartUsageCompartmentNodeToolProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the free form compartment used by the {@link FirstLevelChildPartUsageNodeDescriptionProvider}.
 *
 * @author arichard
 */
public class ChildrenPartUsageCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public ChildrenPartUsageCompartmentNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), colorProvider, nameGenerator);
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
                .name(this.descriptionNameGenerator.getFreeFormCompartmentName(this.eClass, this.eReference))
                .semanticCandidatesExpression(AQLConstants.AQL_SELF)
                .style(this.createCompartmentNodeStyle())
                .userResizable(false)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optCompartmentFreeFormNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getFreeFormCompartmentName(this.eClass, this.eReference));
        var optChildPartUsageNodeDescription = cache.getNodeDescription(ChildPartUsageNodeDescriptionProvider.NAME);

        NodeDescription compartmentFreeFormNodeDescription = optCompartmentFreeFormNodeDescription.get();
        NodeDescription childPartUsageNodeDescription = optChildPartUsageNodeDescription.get();

        compartmentFreeFormNodeDescription.getChildrenDescriptions().add(childPartUsageNodeDescription);

        compartmentFreeFormNodeDescription.setPalette(this.createCompartmentPalette(cache));
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        List<NodeDescription> droppableNodes = new ArrayList<>();
        cache.getNodeDescription(FirstLevelChildPartUsageNodeDescriptionProvider.NAME).ifPresent(droppableNodes::add);
        cache.getNodeDescription(ChildPartUsageNodeDescriptionProvider.NAME).ifPresent(droppableNodes::add);
        return droppableNodes;
    }

    @Override
    protected INodeToolProvider getItemCreationToolProvider() {
        return new ChildrenPartUsageCompartmentNodeToolProvider();
    }

    @Override
    protected InsideLabelStyle createInsideLabelStyle() {
        return this.diagramBuilderHelper.newInsideLabelStyle()
                .displayHeaderSeparator(false)
                .fontSize(12)
                .italic(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(false)
                .withHeader(false)
                .build();
    }
}
