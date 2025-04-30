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
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.nodes.AbstractActionsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.AbstractCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.description.ToolConstants;
import org.eclipse.syson.diagram.common.view.tools.PerformActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ReferencingPerformActionNodeToolProvider;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the perform actions Compartment node description of Part usage and definition inside the General View diagram.
 *
 * @author Jerome Gout
 */
public class PerformActionsCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public static final String PERFORM_ACTIONS_COMPARTMENT_NAME = " perform " + AbstractActionsCompartmentNodeDescriptionProvider.ACTIONS_COMPARTMENT_LABEL;

    public PerformActionsCompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        NodeDescription nd = super.create();
        nd.setName(this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference) + PERFORM_ACTIONS_COMPARTMENT_NAME);
        return nd;
    }

    @Override
    protected String getCustomCompartmentLabel() {
        return "perform actions";
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, this.eReference) + PERFORM_ACTIONS_COMPARTMENT_NAME).ifPresent(acceptedNodeTypes::add);
        return acceptedNodeTypes;
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference) + PERFORM_ACTIONS_COMPARTMENT_NAME).ifPresent(nd -> {
            cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, this.eReference) + PERFORM_ACTIONS_COMPARTMENT_NAME).ifPresent(nd.getChildrenDescriptions()::add);
            nd.setPalette(this.createCompartmentPalette(cache));
        });
    }

    @Override
    protected NodePalette createCompartmentPalette(IViewDiagramElementFinder cache) {
        var palette = this.diagramBuilderHelper.newNodePalette().dropNodeTool(this.createCompartmentDropFromDiagramTool(cache));
        var toolSections = this.toolDescriptionService.createDefaultNodeToolSections();
        // in perform actions compartment only perform action tools are allowed
        this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.BEHAVIOR, new PerformActionNodeToolProvider(this.eClass, this.getDescriptionNameGenerator()).create(cache));
        this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.BEHAVIOR, new ReferencingPerformActionNodeToolProvider(this.eClass, this.getDescriptionNameGenerator()).create(cache));

        toolSections.add(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection());
        this.toolDescriptionService.removeEmptyNodeToolSections(toolSections);

        return palette.toolSections(toolSections.toArray(NodeToolSection[]::new)).build();
    }
}
