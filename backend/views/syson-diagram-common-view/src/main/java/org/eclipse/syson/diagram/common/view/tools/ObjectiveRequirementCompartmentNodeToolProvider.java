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

import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.services.aql.DiagramQueryAQLService;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Node tool provider for objective compartment in the element that need such compartment.
 *
 * <p>
 * This tool opens a selection dialog to create a {@link RequirementUsage} that serves as an objective.
 * The user can choose to create it as a standalone element or select an existing requirement to be set as its type or subset.
 * </p>
 *
 * @author Jerome Gout
 */
public class ObjectiveRequirementCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    @Override
    protected String getServiceCallExpression() {
        return ServiceMethod.of1(ViewCreateService::createRequirementUsageAsObjectiveRequirement).aqlSelf("selectedObject");
    }

    @Override
    protected SelectionDialogDescription getSelectionDialogDescription() {
        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(ServiceMethod.of0(DiagramQueryAQLService::getAllReachableRequirements).aqlSelf())
                .build();
        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .defaultTitleExpression(this.getNodeToolName())
                .noSelectionTitleExpression(this.getNodeToolName())
                .withSelectionTitleExpression(this.getNodeToolName())
                .descriptionExpression("Create an objective:")
                .noSelectionActionLabelExpression("Create a new objective")
                .noSelectionActionDescriptionExpression("Create a new objective without specialization")
                .withSelectionActionLabelExpression("Select an existing Element as objective")
                .withSelectionActionDescriptionExpression("Create a new specialized objective")
                .noSelectionActionStatusMessageExpression("It will create a new objective without specialization")
                .selectionRequiredWithoutSelectionStatusMessageExpression("Select one Element to specialize the new objective")
                .selectionRequiredWithSelectionStatusMessageExpression(AQLConstants.AQL + "'It will create an objective specialized with ' + selectedObjects->first().name")
                .optional(true)
                .build();
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
        return ServiceMethod.of0(ViewCreateService::isEmptyObjectiveRequirementCompartment).aqlSelf();
    }

    @Override
    protected boolean revealOnCreate() {
        return true;
    }
}
