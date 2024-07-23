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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.diagram.common.view.nodes.AbstractCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.IVDescriptionNameGenerator;
import org.eclipse.syson.diagram.interconnection.view.tools.ChildrenPartUsageCompartmentNodeToolProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the free form compartment used by the {@link FirstLevelChildUsageNodeDescriptionProvider}.
 *
 * @author arichard
 */
public class ChildrenUsageCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public ChildrenUsageCompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IVDescriptionNameGenerator nameGenerator) {
        super(eClass, eReference, colorProvider, nameGenerator);
    }

    @Override
    protected IVDescriptionNameGenerator getDescriptionNameGenerator() {
        return (IVDescriptionNameGenerator) super.getDescriptionNameGenerator();
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
                .name(this.getDescriptionNameGenerator().getFreeFormCompartmentName(this.eClass, this.eReference))
                .semanticCandidatesExpression(AQLConstants.AQL_SELF)
                .style(this.createCompartmentNodeStyle())
                .userResizable(UserResizableDirection.NONE)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optCompartmentFreeFormNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getFreeFormCompartmentName(this.eClass, this.eReference));
        var optChildPartUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPartUsage()));

        NodeDescription compartmentFreeFormNodeDescription = optCompartmentFreeFormNodeDescription.get();
        NodeDescription childPartUsageNodeDescription = optChildPartUsageNodeDescription.get();

        compartmentFreeFormNodeDescription.getChildrenDescriptions().add(childPartUsageNodeDescription);

        compartmentFreeFormNodeDescription.setPalette(this.createCompartmentPalette(cache));
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        List<NodeDescription> droppableNodes = new ArrayList<>();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getFirstLevelNodeName(SysmlPackage.eINSTANCE.getPartUsage())).ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPartUsage())).ifPresent(droppableNodes::add);
        return droppableNodes;
    }

    @Override
    protected List<INodeToolProvider> getItemCreationToolProviders() {
        List<INodeToolProvider> creationToolProviders = new ArrayList<>();
        creationToolProviders.add(new ChildrenPartUsageCompartmentNodeToolProvider());
        return creationToolProviders;
    }

    @Override
    protected InsideLabelStyle createInsideLabelStyle() {
        return this.diagramBuilderHelper.newInsideLabelStyle()
                .borderSize(0)
                .displayHeaderSeparator(false)
                .fontSize(12)
                .italic(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIconExpression("aql:false")
                .withHeader(true)
                .build();
    }
}
