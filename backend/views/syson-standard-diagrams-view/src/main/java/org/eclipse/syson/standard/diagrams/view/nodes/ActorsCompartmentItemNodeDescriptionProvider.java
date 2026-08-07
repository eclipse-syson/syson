/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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

package org.eclipse.syson.standard.diagrams.view.nodes;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.model.services.aql.ModelQueryAQLService;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Used to create the actors compartment item node description.
 * <p>
 *  Generic {@link CompartmentItemNodeDescriptionProvider} cannot be used for those compartments due to implementation of actors getters
 *  (the references of kind {@code SysmlPackage.eINSTANCE.getXXX_ActorParameter()}). These getters collect owned actors *and* inherited ones
 *  which is not suitable since we want to handle them separately.
 * </p>
 * @author Jerome Gout
 */
public class ActorsCompartmentItemNodeDescriptionProvider extends CompartmentItemNodeDescriptionProvider {

    public ActorsCompartmentItemNodeDescriptionProvider(EClass eClass, EReference eReference,
            IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getSemanticCandidateExpression() {
        return ServiceMethod.of0(ModelQueryAQLService::getActors).aqlSelf();
    }
}
