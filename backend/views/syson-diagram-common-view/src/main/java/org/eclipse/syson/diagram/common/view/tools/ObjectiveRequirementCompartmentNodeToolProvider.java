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
package org.eclipse.syson.diagram.common.view.tools;

/**
 * Node tool provider for Objective Requirement compartment in the element that need such compartment.
 *
 * @author Jerome Gout
 */
public class ObjectiveRequirementCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    public ObjectiveRequirementCompartmentNodeToolProvider() {
        super();
    }

    @Override
    protected String getServiceCallExpression() {
        return "aql:self.createRequirementUsageAsObjectiveRequirement()";
    }

    @Override
    protected String getNodeToolName() {
        return "New Objective";
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/Objective.svg";
    }

    @Override
    protected String getPreconditionExpression() {
        return "aql:self.isEmptyObjectiveRequirementCompartment()";
    }

    @Override
    protected boolean revealOnCreate() {
        return true;
    }
}
