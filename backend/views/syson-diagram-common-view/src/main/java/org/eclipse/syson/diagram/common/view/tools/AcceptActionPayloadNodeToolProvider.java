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
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.trees.renderer.TreeRenderer;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.DialogDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.tree.services.aql.TreeQueryAQLService;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to create element as a payload of an accept action usage.
 *
 * @author Jerome Gout
 */
public class AcceptActionPayloadNodeToolProvider implements INodeToolProvider {

    private final EClass payloadEClass;

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final IDescriptionNameGenerator nameGenerator;

    public AcceptActionPayloadNodeToolProvider(EClass payloadEClass, IDescriptionNameGenerator nameGenerator) {
        this.payloadEClass = Objects.requireNonNull(payloadEClass);
        this.nameGenerator = Objects.requireNonNull(nameGenerator);
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var creationPayloadServiceCall = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of2(DiagramMutationAQLService::createAcceptActionPayload).aqlSelf(AQLUtils.aqlString(this.payloadEClass.getName()), "selectedObject"))
                .build();

        var rootChangContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF)
                .children(creationPayloadServiceCall)
                .build();

        return builder.name(this.nameGenerator.getCreationToolName("New {0} as Payload", this.payloadEClass))
                .iconURLsExpression("/icons/full/obj16/" + this.payloadEClass.getName() + ".svg")
                .body(rootChangContext)
                .preconditionExpression(AQLConstants.AQL_SELF + ".isEmptyAcceptActionUsagePayload()")
                .dialogDescription(this.getSelectionDialogDescription())
                .build();
    }

    private DialogDescription getSelectionDialogDescription() {
        var payloadTypeName = SysMLMetamodelHelper.buildQualifiedName(this.payloadEClass);
        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(ServiceMethod.of1(TreeQueryAQLService::getSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT, AQLUtils.aqlSequence(List.of(payloadTypeName))))
                .childrenExpression(
                        ServiceMethod.of3(TreeQueryAQLService::getSelectionDialogChildren).aqlSelf(IEditingContext.EDITING_CONTEXT, TreeRenderer.EXPANDED, AQLUtils.aqlSequence(List.of(payloadTypeName))))
                .isSelectableExpression(AQLConstants.AQL_SELF + ".oclIsKindOf(" + payloadTypeName + ")")
                .build();
        String payloadName = this.payloadEClass.getName();
        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .defaultTitleExpression("Set payload")
                .descriptionExpression(payloadName + " as payload")
                .noSelectionActionLabelExpression("Create a New " + payloadName)
                .noSelectionActionDescriptionExpression("Set the payload with a New " + payloadName)
                .withSelectionActionLabelExpression("Select an existing " + payloadName)
                .withSelectionActionDescriptionExpression("Set the payload with an existing " + payloadName)
                .noSelectionActionStatusMessageExpression("It will set the payload with a New " + payloadName)
                .selectionRequiredWithoutSelectionStatusMessageExpression("Select one " + payloadName)
                .selectionRequiredWithSelectionStatusMessageExpression(AQLConstants.AQL + "'It will set the payload with ' + selectedObjects->first().name")
                .optional(true)
                .build();
    }
}
