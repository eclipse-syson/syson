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

import java.util.List;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.trees.renderer.TreeRenderer;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.DialogDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.diagram.services.aql.DiagramQueryAQLService;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.tree.services.aql.TreeQueryAQLService;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to create a {@link PortUsage} as the receiver of an accept action usage.
 *
 * @author Jerome Gout
 */
public class AcceptActionPortUsageReceiverToolNodeProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var creationPayloadServiceCall = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(DiagramMutationAQLService::createAcceptActionReceiver).aqlSelf("selectedObject"))
                .build();

        var rootChangContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF)
                .children(creationPayloadServiceCall)
                .build();

        return builder.name("New Port as Receiver")
                .iconURLsExpression("/icons/full/obj16/" + SysmlPackage.eINSTANCE.getPortUsage().getName() + ".svg")
                .body(rootChangContext)
                .preconditionExpression(ServiceMethod.of0(DiagramQueryAQLService::isEmptyAcceptActionUsageReceiver).aqlSelf())
                .dialogDescription(this.getSelectionDialogDescription())
                .build();
    }

    private DialogDescription getSelectionDialogDescription() {
        var receiverTypeName = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPortUsage());
        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(ServiceMethod.of1(TreeQueryAQLService::getSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT, AQLUtils.aqlSequence(List.of(receiverTypeName))))
                .childrenExpression(
                        ServiceMethod.of3(TreeQueryAQLService::getSelectionDialogChildren).aqlSelf(IEditingContext.EDITING_CONTEXT, TreeRenderer.EXPANDED, AQLUtils.aqlSequence(List.of(receiverTypeName))))
                .isSelectableExpression(AQLConstants.AQL_SELF + ".oclIsKindOf(" + receiverTypeName + ")")
                .build();
        String receiverName = SysmlPackage.eINSTANCE.getPortUsage().getName();
        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .defaultTitleExpression("Set the receiver")
                .descriptionExpression(receiverName + " as receiver")
                .noSelectionActionLabelExpression("Create a New " + receiverName)
                .noSelectionActionDescriptionExpression("Set the receiver with a New " + receiverName)
                .withSelectionActionLabelExpression("Select an existing " + receiverName)
                .withSelectionActionDescriptionExpression("Set the receiver with an existing " + receiverName)
                .noSelectionActionStatusMessageExpression("It will set the receiver with a New " + receiverName)
                .selectionRequiredWithoutSelectionStatusMessageExpression("Select one " + receiverName)
                .selectionRequiredWithSelectionStatusMessageExpression(AQLConstants.AQL + "'It will set the receiver with ' + selectedObjects->first().name")
                .optional(true)
                .build();
    }
}
