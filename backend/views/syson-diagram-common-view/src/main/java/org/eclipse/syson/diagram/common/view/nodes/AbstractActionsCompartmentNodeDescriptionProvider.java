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

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the Actions compartment node description to be displayed inside the States nodes.
 *
 * @author adieumegard
 */
public abstract class AbstractActionsCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public static final String ACTIONS_COMPARTMENT_LABEL = "actions";

    public static final String ACTION = "action";

    public AbstractActionsCompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    protected abstract List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache);

    @Override
    protected abstract NodePalette createCompartmentPalette(IViewDiagramElementFinder cache);
}
