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
import org.eclipse.syson.diagram.common.view.nodes.AbstractCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;

/**
 * Used to create the Compartment node description inside the General View diagram.
 *
 * @author Jerome Gout
 */
public class CompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public CompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider) {
        super(eClass, eReference, colorProvider, new GVDescriptionNameGenerator());
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();
        var nameGenerator = new GVDescriptionNameGenerator();

        GeneralViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            listItems.forEach(ref -> {
                if (this.getReference().getEType().equals(ref.getEType())) {
                    var optCompartmentItemNodeDescription = cache.getNodeDescription(nameGenerator.getCompartmentItemName(type, ref));
                    acceptedNodeTypes.add(optCompartmentItemNodeDescription.get());
                }
            });
        });

        return acceptedNodeTypes;
    }
}
