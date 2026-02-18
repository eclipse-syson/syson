/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Node tool provider for Actor compartment in the element that need such compartment.
 *
 * @author gdaniel
 */
public class ActorCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    @Override
    protected String getServiceCallExpression() {
        return ServiceMethod.of1(ViewCreateService::createPartUsageAsActor).aqlSelf("selectedObject");
    }

    @Override
    protected SelectionDialogDescription getSelectionDialogDescription() {
        String partUsageType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPartUsage());
        String partDefType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPartDefinition());

        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(ServiceMethod.of0(ViewToolService::getActorSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT))
                .childrenExpression(ServiceMethod.of0(ViewToolService::getActorSelectionDialogChildren).aqlSelf())
                .isSelectableExpression(AQLConstants.AQL_SELF + ".oclIsKindOf(" + partUsageType + ") or self.oclIsKindOf(" + partDefType + ")")
                .build();
        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .selectionMessage("Select an existing Part as actor:")
                .optional(false)
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
