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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.syson.sysml.ExhibitStateUsage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * {@link ExhibitStateUsage} compartment item Node description.
 *
 * @author adieumegard
 */
public class ExhibitStatesCompartmentItemNodeDescriptionProvider extends CompartmentItemNodeDescriptionProvider {

    public ExhibitStatesCompartmentItemNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(eClass, eReference, colorProvider, nameGenerator);
    }

    @Override
    protected String getSemanticCandidateExpression() {
        return AQLUtils.getSelfServiceCallExpression("getAllReachableExhibitedStates");
    }

}
