/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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

import java.util.List;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.trees.renderer.TreeRenderer;
import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.tree.services.aql.TreeQueryAQLService;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Node tool provider for references compartment in elements that need such compartment.
 *
 * @author Jerome Gout
 */
public class ReferenceUsageCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    @Override
    protected String getServiceCallExpression() {
        return ServiceMethod.of1(DiagramMutationAQLService::createReferenceUsage).aqlSelf("selectedObject");
    }

    @Override
    protected String getNodeToolName() {
        return "New Reference";
    }

    @Override
    protected boolean revealOnCreate() {
        return true;
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/ReferenceUsage.svg";
    }

    protected SelectionDialogDescription getSelectionDialogDescription() {
        var domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getUsage());

        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(ServiceMethod.of1(TreeQueryAQLService::getSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT, AQLUtils.aqlSequence(List.of(domainType))))
                .childrenExpression(ServiceMethod.of3(TreeQueryAQLService::getSelectionDialogChildren).aqlSelf(IEditingContext.EDITING_CONTEXT, TreeRenderer.EXPANDED, AQLUtils.aqlSequence(List.of(domainType))))
                .isSelectableExpression(AQLConstants.AQL_SELF + ".oclIsKindOf(" + domainType + ")")
                .build();

        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .defaultTitleExpression("New Reference")
                .noSelectionTitleExpression("New Reference")
                .withSelectionTitleExpression("New Reference")
                .noSelectionActionLabelExpression("Create a new reference")
                .noSelectionActionDescriptionExpression("Create a new empty reference")
                .withSelectionActionLabelExpression("Select an existing element")
                .withSelectionActionDescriptionExpression("Create a new reference of selected element")
                .noSelectionActionStatusMessageExpression("It will create en empty reference")
                .selectionRequiredWithoutSelectionStatusMessageExpression("Select one usage to reference")
                .selectionRequiredWithSelectionStatusMessageExpression(AQLConstants.AQL + "'It will reference ' + selectedObjects->first().name")
                .optional(true)
                .build();
    }
}
