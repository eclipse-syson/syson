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
package org.eclipse.syson.diagram.statetransition.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.LabelTextAlign;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.common.view.nodes.AbstractCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the 'state transition' free form compartment that contains nested/owned states.
 * 
 * @author gdaniel
 */
public class StateTransitionCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public static final String STATE_COMPARTMENT_NAME = "state transition";

    private final String name;

    public StateTransitionCompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
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
                .isHiddenByDefaultExpression(this.isHiddenByDefaultExpression())
                .name(this.name)
                .semanticCandidatesExpression(AQLConstants.AQL_SELF)
                .style(this.createCompartmentNodeStyle())
                .userResizable(false)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.name).ifPresent(nodeDescription -> {
            StateTransitionViewDiagramDescriptionProvider.USAGES.stream().forEach(eClass -> {
                cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(eClass)).ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
            });
            cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getStateDefinition())).ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
            nodeDescription.setPalette(this.createCompartmentPalette(cache));
        });
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        List<NodeDescription> droppableNodes = new ArrayList<>();
        StateTransitionViewDiagramDescriptionProvider.USAGES.stream().forEach(eClass -> {
            cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(eClass)).ifPresent(droppableNodes::add);
        });
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, this.eReference)).ifPresent(droppableNodes::add);        
        return droppableNodes;
    }

    @Override
    protected InsideLabelDescription createInsideLabelDescription() {
        return this.diagramBuilderHelper.newInsideLabelDescription()
                .labelExpression(STATE_COMPARTMENT_NAME)
                .position(InsideLabelPosition.TOP_CENTER)
                .style(this.createInsideLabelStyle())
                .textAlign(LabelTextAlign.CENTER)
                .build();
    }

    @Override
    protected InsideLabelStyle createInsideLabelStyle() {
        return this.diagramBuilderHelper.newInsideLabelStyle()
                .borderSize(0)
                .displayHeaderSeparator(false)
                .fontSize(12)
                .italic(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(false)
                .withHeader(false)
                .build();
    }

    @Override
    protected NodePalette createCompartmentPalette(IViewDiagramElementFinder cache) {
        var palette = this.diagramBuilderHelper.newNodePalette().dropNodeTool(this.createCompartmentDropFromDiagramTool(cache));

        // Do not use getItemCreationToolProvider because the compartment contains multiple creation tools.
        palette.toolSections(this.diagramBuilderHelper.newNodeToolSection()
                .nodeTools(new StateTransitionCompartmentNodeToolProvider(false, false).create(cache), 
                        new StateTransitionCompartmentNodeToolProvider(true, false).create(cache),
                        new StateTransitionCompartmentNodeToolProvider(false, true).create(cache),
                        new StateTransitionCompartmentNodeToolProvider(true, true).create(cache)
                        ).build());

        return palette.toolSections(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection()).build();
    }
}
