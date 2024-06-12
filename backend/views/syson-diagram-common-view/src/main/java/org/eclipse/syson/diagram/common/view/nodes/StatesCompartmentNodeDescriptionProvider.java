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
import org.eclipse.syson.diagram.common.view.tools.StateTransitionCompartmentNodeToolProvider;
import org.eclipse.syson.sysml.ExhibitStateUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to handle the {@link StateUsage} and {@link ExhibitStateUsage} compartment of {@link StateUsage} and
 * {@link StateDefinition}.
 *
 * @author adieumegard
 */
public class StatesCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public StatesCompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(eClass, eReference, colorProvider, nameGenerator);
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        List<NodeDescription> droppableNodes = new ArrayList<>();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, SysmlPackage.eINSTANCE.getDefinition_OwnedState()))
                .ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(droppableNodes::add);
        return droppableNodes;
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference)).ifPresent(nodeDescription -> {
            cache.getNodeDescription(this.getDescriptionNameGenerator().getInheritedCompartmentItemName(this.eClass, SysmlPackage.eINSTANCE.getDefinition_OwnedState()))
                    .ifPresent(node -> nodeDescription.getChildrenDescriptions().add(node));
            cache.getNodeDescription(this.getDescriptionNameGenerator().getInheritedCompartmentItemName(this.eClass, SysmlPackage.eINSTANCE.getUsage_NestedState()))
                    .ifPresent(node -> nodeDescription.getChildrenDescriptions().add(node));
            cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, SysmlPackage.eINSTANCE.getDefinition_OwnedState()))
                    .ifPresent(node -> nodeDescription.getChildrenDescriptions().add(node));
            cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, SysmlPackage.eINSTANCE.getUsage_NestedState()))
                    .ifPresent(node -> nodeDescription.getChildrenDescriptions().add(node));
            nodeDescription.setPalette(this.createCompartmentPalette(cache));
        });
    }

    @Override
    protected String getCustomCompartmentLabel() {
        return "states";
    }

    @Override
    protected NodePalette createCompartmentPalette(IViewDiagramElementFinder cache) {
        var palette = this.diagramBuilderHelper.newNodePalette().dropNodeTool(this.createCompartmentDropFromDiagramTool(cache));

        // Do not use getItemCreationToolProvider because the compartment contains multiple creation tools.
        palette.toolSections(this.diagramBuilderHelper.newNodeToolSection()
                .nodeTools(new StateTransitionCompartmentNodeToolProvider(false, false).create(cache), new StateTransitionCompartmentNodeToolProvider(true, false).create(cache)).build());

        return palette.toolSections(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection()).build();
    }

}
