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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.nodes.AbstractActionsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionActionCompartmentToolProvider;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the Actions compartment node description to be displayed inside the States nodes.
 *
 * @author adieumegard
 */
public class ActionsCompartmentNodeDescriptionProvider extends AbstractActionsCompartmentNodeDescriptionProvider {

    public ActionsCompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();

        GeneralViewDiagramDescriptionProvider.COMPARTMENTS_WITH_MERGED_LIST_ITEMS.forEach((type, listItems) -> {
            listItems.forEach(eReference -> {
                if (this.eReference.getEType().equals(eReference.getEType())) {
                    var optCompartmentItemNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(type, eReference));
                    if (optCompartmentItemNodeDescription.isPresent()) {
                        acceptedNodeTypes.add(optCompartmentItemNodeDescription.get());
                    }
                }
            });
        });

        return acceptedNodeTypes;
    }

    @Override
    protected NodePalette createCompartmentPalette(IViewDiagramElementFinder cache) {
        var palette = this.diagramBuilderHelper.newNodePalette()
                .dropNodeTool(this.createCompartmentDropFromDiagramTool(cache));

        var toolSections = this.toolDescriptionService.createDefaultNodeToolSections();
        List<EReference> refList = GeneralViewDiagramDescriptionProvider.COMPARTMENTS_WITH_MERGED_LIST_ITEMS.get(this.eClass);
        if (refList != null) {
            refList.forEach(eRef -> {
                var eType = eRef.getEType();
                var toolSectionName = this.toolDescriptionService.getToolSectionName(eType);
                this.toolDescriptionService.addNodeTool(toolSections, toolSectionName, new StateTransitionActionCompartmentToolProvider(eRef).create(cache));
            });
        }
        toolSections.add(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection());
        this.toolDescriptionService.removeEmptyNodeToolSections(toolSections);

        return palette.toolSections(toolSections.toArray(NodeToolSection[]::new))
                .build();
    }
}
