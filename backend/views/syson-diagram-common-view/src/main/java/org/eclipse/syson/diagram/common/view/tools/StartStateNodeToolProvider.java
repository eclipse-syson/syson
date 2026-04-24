/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.diagram.common.view.tools;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.diagram.common.view.nodes.StartStateNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StateTransitionCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Used to add the standard start state in states body for all diagrams.
 *
 * @author Jerome Gout
 */
public class StartStateNodeToolProvider extends AbstractFreeFormCompartmentNodeToolProvider {

    public StartStateNodeToolProvider(EClass ownerEClass, IDescriptionNameGenerator descriptionNameGenerator) {
        super(ownerEClass, StateTransitionCompartmentNodeDescriptionProvider.STATE_COMPARTMENT_NAME, descriptionNameGenerator);
    }

    @Override
    protected String getNodeDescriptionName() {
        return this.getDescriptionNameGenerator().getNodeName(StartStateNodeDescriptionProvider.START_STATE_NAME);
    }

    @Override
    protected String getCreationServiceCallExpression() {
        return ServiceMethod.of0(ViewCreateService::addStartState).aqlSelf();
    }

    @Override
    protected String getLabel() {
        return "New Start State";
    }

    @Override
    protected String getIconPath() {
        return "/icons/start_action.svg";
    }
}
