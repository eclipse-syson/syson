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

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Node tool provider for Subject compartment in the element that need such compartment.
 *
 * @author Jerome Gout
 */
public class SubjectCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    @Override
    protected String getServiceCallExpression() {
        return ServiceMethod.of1(ViewCreateService::createReferenceUsageAsSubject).aqlSelf("selectedObject");
    }

    @Override
    protected SelectionDialogDescription getSelectionDialogDescription() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getType());
        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(ServiceMethod.of0(ViewToolService::getSubjectSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT))
                .childrenExpression(ServiceMethod.of0(ViewToolService::getSubjectSelectionDialogChildren).aqlSelf())
                .isSelectableExpression(AQLConstants.AQL_SELF + ".oclIsKindOf(" + domainType + ")")
                .build();
        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .selectionMessage("Select an existing Type as subject:")
                .build();
    }

    @Override
    protected String getNodeToolName() {
        return "New Subject";
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/Subject.svg";
    }

    @Override
    protected String getPreconditionExpression() {
        return ServiceMethod.of0(ViewCreateService::isEmptySubjectCompartment).aqlSelf();
    }

    @Override
    protected boolean revealOnCreate() {
        return true;
    }
}
