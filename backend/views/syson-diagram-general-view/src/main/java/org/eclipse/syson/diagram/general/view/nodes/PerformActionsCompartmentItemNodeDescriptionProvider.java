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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create an item node description of the perform actions compartment of Parts for general view diagram.
 *
 * @author Jerome Gout
 */
public class PerformActionsCompartmentItemNodeDescriptionProvider extends CompartmentItemNodeDescriptionProvider {

    public PerformActionsCompartmentItemNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        var nodeDescription = super.create();
        nodeDescription.setName(this.getDescriptionNameGenerator().getCompartmentItemName(this.getEClass(), this.getEReference()) + PerformActionsCompartmentNodeDescriptionProvider.PERFORM_ACTIONS_COMPARTMENT_NAME);
        return nodeDescription;
    }

    @Override
    protected String getSemanticCandidateExpression() {
        return AQLUtils.getSelfServiceCallExpression("getAllPerformActions");
    }
}
