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
package org.eclipse.syson.diagram.common.view.tools;

import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.util.AQLUtils;

/**
 * {@link ActorCompartmentNodeToolProvider} specialization for {@link RequirementDefinition#getStakeholderParameter()}
 * and {@link RequirementUsage#getStakeholderParameter()}.
 *
 * @author flatombe
 */
public class StakeholdersCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    @Override
    protected String getServiceCallExpression() {
        return "aql:self.createPartUsageAsStakeholder(selectedObject)";
    }

    @Override
    protected SelectionDialogDescription getSelectionDialogDescription() {
        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(AQLUtils.getServiceCallExpression("editingContext", "getStakeholderSelectionDialogElements"))
                .childrenExpression(AQLUtils.getSelfServiceCallExpression("getStakeholderSelectionDialogChildren"))
                .isSelectableExpression("aql:self.oclIsKindOf(sysml::PartUsage)")
                .build();
        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .selectionMessage("Select an existing Part as stakeholder:")
                .build();
    }

    @Override
    protected String getNodeToolName() {
        return "New Stakeholder";
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/StakeholderMembership.svg";
    }

    @Override
    protected boolean revealOnCreate() {
        return true;
    }
}
