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

import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.util.AQLUtils;

/**
 * Node tool provider for objective compartment in the element that need such compartment.
 * <p>
 * This tool opens a selection dialog to select the existing requirement to use as the objective. The selected
 * requirement isn't used as is, but is set as the type/subsetted feature of a new {@link RequirementUsage} representing
 * the objective.
 * </p>
 *
 * @author gdaniel
 */
public class ObjectiveRequirementWithBaseRequirementCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    public ObjectiveRequirementWithBaseRequirementCompartmentNodeToolProvider() {
        super();
    }

    @Override
    protected String getServiceCallExpression() {
        return "aql:self.createRequirementUsageAsObjectiveRequirement(selectedObject)";
    }

    @Override
    protected SelectionDialogDescription getSelectionDialogDescription() {
        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getAllReachableRequirements"))
                .selectionMessage("Select an existing Requirement as objective:")
                .build();
    }

    @Override
    protected String getNodeToolName() {
        return "New Objective from existing Requirement";
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
