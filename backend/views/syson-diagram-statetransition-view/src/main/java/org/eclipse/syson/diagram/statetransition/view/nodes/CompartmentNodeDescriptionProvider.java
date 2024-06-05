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
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the Compartment node description inside the State Transition View diagram.
 *
 * @author adieumegard
 */
public class CompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public CompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();

        StateTransitionViewDiagramDescriptionProvider.COMPARTMENTS_WITH_MERGED_LIST_ITEMS.forEach((type, listItems) -> {
            listItems.forEach(eReference -> {
                if (this.eReference.getEType().equals(eReference.getEType())) {
                    var optCompartmentItemNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getCompartmentItemName(type, eReference));
                    if (optCompartmentItemNodeDescription.isPresent()) {
                        acceptedNodeTypes.add(optCompartmentItemNodeDescription.get());
                    }
                }
            });
        });

        return acceptedNodeTypes;
    }

    @Override
    protected INodeToolProvider getItemCreationToolProvider() {
        return null;
    }
}
