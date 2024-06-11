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

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.syson.diagram.common.view.nodes.AbstractCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionCompartmentNodeToolProvider;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to handle the Ends compartment of Allocation Definition.
 *
 * @author Jerome Gout
 */
public class StatesCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public StatesCompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(eClass, eReference, colorProvider, nameGenerator);
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        return List.of();
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
                .nodeTools(new StateTransitionCompartmentNodeToolProvider(false).create(cache), new StateTransitionCompartmentNodeToolProvider(true).create(cache)).build());

        return palette.toolSections(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection()).build();
    }

}
