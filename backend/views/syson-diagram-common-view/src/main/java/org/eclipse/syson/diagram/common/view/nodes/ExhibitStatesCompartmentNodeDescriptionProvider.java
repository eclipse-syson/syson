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
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionCompartmentNodeToolProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the Exhibit States Compartment node description inside the State Transition View diagram.
 *
 * @author adieumegard
 */
public class ExhibitStatesCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public ExhibitStatesCompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getCustomCompartmentLabel() {
        return "exhibit states";
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(acceptedNodeTypes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()))
                .ifPresent(acceptedNodeTypes::add);
        return acceptedNodeTypes;
    }

    @Override
    protected NodePalette createCompartmentPalette(IViewDiagramElementFinder cache) {
        var palette = this.diagramBuilderHelper.newNodePalette().dropNodeTool(this.createCompartmentDropFromDiagramTool(cache));

        // Do not use getItemCreationToolProvider because the compartment contains multiple creation tools.
        palette.toolSections(this.diagramBuilderHelper.newNodeToolSection()
                .nodeTools(new StateTransitionCompartmentNodeToolProvider(false, true).create(cache),
                        new StateTransitionCompartmentNodeToolProvider(true, true).create(cache))
                .build());

        return palette.toolSections(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection()).build();
    }
}
