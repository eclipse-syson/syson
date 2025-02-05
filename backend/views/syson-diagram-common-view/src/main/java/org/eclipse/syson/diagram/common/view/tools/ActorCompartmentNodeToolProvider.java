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
import org.eclipse.syson.util.AQLUtils;

/**
 * Node tool provider for Actor compartment in the element that need such compartment.
 *
 * @author gdaniel
 */
public class ActorCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    @Override
    protected String getServiceCallExpression() {
        return "aql:self.createPartUsageAsActor(selectedObject)";
    }

    @Override
    protected SelectionDialogDescription getSelectionDialogDescription() {
        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(AQLUtils.getServiceCallExpression("editingContext", "getActorSelectionDialogElements"))
                .childrenExpression(AQLUtils.getSelfServiceCallExpression("getActorSelectionDialogChildren"))
                .isSelectableExpression("aql:self.oclIsKindOf(sysml::PartUsage)")
                .build();
        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .selectionMessage("Select an existing Part as actor:")
                .build();
    }

    @Override
    protected String getNodeToolName() {
        return "New Actor";
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/Actor.svg";
    }

    @Override
    protected boolean revealOnCreate() {
        return true;
    }
}
