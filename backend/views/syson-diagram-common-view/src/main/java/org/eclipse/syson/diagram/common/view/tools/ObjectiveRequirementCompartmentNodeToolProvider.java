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

import org.eclipse.syson.sysml.RequirementUsage;

/**
 * Node tool provider for objective compartment in the element that need such compartment.
 * <p>
 * This tool creates a new {@link RequirementUsage} and sets it as the objective of the containing element.
 * </p>
 *
 * @author Jerome Gout
 */
public class ObjectiveRequirementCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    @Override
    protected String getServiceCallExpression() {
        return "aql:self.createRequirementUsageAsObjectiveRequirement(null)";
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
