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
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.services.description.ToolConstants;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionCompartmentNodeToolProvider;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the Exhibit States Compartment node description inside the State Transition View diagram.
 *
 * @author adieumegard
 */
public class StatesCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public static final String STATES_NAME = " states";

    public static final String EXHIBIT_STATES_NAME = " exhibit" + STATES_NAME;

    private final boolean showsExhibitOnly;

    public StatesCompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator, boolean showsExhibitOnly) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
        this.showsExhibitOnly = showsExhibitOnly;
    }

    @Override
    public NodeDescription create() {
        NodeDescription nd = super.create();
        if (this.showsExhibitOnly) {
            nd.setName(this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference) + EXHIBIT_STATES_NAME);
        } else {
            nd.setName(this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference) + STATES_NAME);
        }
        return nd;
    }

    @Override
    protected String getCustomCompartmentLabel() {
        if (this.showsExhibitOnly) {
            return "exhibit states";
        } else {
            return "states";
        }
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();
        if (this.showsExhibitOnly) {
            cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, this.eReference) + EXHIBIT_STATES_NAME).ifPresent(acceptedNodeTypes::add);
        } else {
            cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, this.eReference) + STATES_NAME).ifPresent(acceptedNodeTypes::add);
        }
        return acceptedNodeTypes;
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        if (this.showsExhibitOnly) {
            cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference) + EXHIBIT_STATES_NAME).ifPresent(nd -> {
                cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, this.eReference) + EXHIBIT_STATES_NAME).ifPresent(nd.getChildrenDescriptions()::add);
                nd.setPalette(this.createCompartmentPalette(cache));
            });
        } else {
            cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference) + STATES_NAME).ifPresent(nd -> {
                cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, this.eReference) + STATES_NAME).ifPresent(nd.getChildrenDescriptions()::add);
                nd.setPalette(this.createCompartmentPalette(cache));
            });
        }
    }

    @Override
    protected NodePalette createCompartmentPalette(IViewDiagramElementFinder cache) {
        var palette = this.diagramBuilderHelper.newNodePalette().dropNodeTool(this.createCompartmentDropFromDiagramTool(cache));
        var toolSections = this.toolDescriptionService.createDefaultNodeToolSections();

        // Do not use getItemCreationToolProvider because the compartment contains multiple creation tools.
        if (this.showsExhibitOnly) {
            this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(false, true).create(cache));
            this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.BEHAVIOR,  new StateTransitionCompartmentNodeToolProvider(true, true).create(cache));
        } else {
            this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(false, false).create(cache));
            this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.BEHAVIOR,  new StateTransitionCompartmentNodeToolProvider(true, false).create(cache));
        }
        toolSections.add(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection());
        this.toolDescriptionService.removeEmptyNodeToolSections(toolSections);

        return palette.toolSections(toolSections.toArray(NodeToolSection[]::new))
                .build();
    }
}
