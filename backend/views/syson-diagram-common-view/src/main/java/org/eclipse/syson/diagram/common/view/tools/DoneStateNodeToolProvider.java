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
import org.eclipse.syson.diagram.common.view.nodes.DoneStateNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StateTransitionCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Used to add the standard done state in states body for all diagrams.
 *
 * @author Jerome Gout
 */
public class DoneStateNodeToolProvider extends AbstractFreeFormCompartmentNodeToolProvider {

    public DoneStateNodeToolProvider(EClass ownerEClass, IDescriptionNameGenerator descriptionNameGenerator) {
        super(ownerEClass, StateTransitionCompartmentNodeDescriptionProvider.STATE_COMPARTMENT_NAME, descriptionNameGenerator);
    }

    @Override
    protected String getNodeDescriptionName() {
        return this.getDescriptionNameGenerator().getNodeName(DoneStateNodeDescriptionProvider.DONE_STATE_NAME);
    }

    @Override
    protected String getCreationServiceCallExpression() {
        return ServiceMethod.of0(ViewCreateService::addDoneState).aqlSelf();
    }

    @Override
    protected String getLabel() {
        return "New Done State";
    }

    @Override
    protected String getIconPath() {
        return "/icons/done_action.svg";
    }
}
