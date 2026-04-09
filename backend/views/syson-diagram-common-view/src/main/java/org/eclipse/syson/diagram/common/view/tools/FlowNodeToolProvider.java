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

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.diagram.services.aql.DiagramQueryAQLService;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.Flow;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Provides a tool to create and initialize a {@link Flow} from a {@link ConnectionUsage}.
 *
 * @author pcdavid
 */
public class FlowNodeToolProvider implements INodeToolProvider {
    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var creationFlowUsage = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(DiagramMutationAQLService::createFlowUsageWithPayload).aqlSelf("selectedObject"))
                .build();

        var rootChangContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF)
                .children(creationFlowUsage)
                .build();

        return builder.name("New Flow")
                .iconURLsExpression("/icons/full/obj16/Flow.svg")
                .dialogDescription(this.getSelectionDialogDescription())
                .preconditionExpression(ServiceMethod.of0(DiagramQueryAQLService::canCreateFlowUsage).aqlSelf())
                .body(rootChangContext)
                .build();
    }

    private SelectionDialogDescription getSelectionDialogDescription() {
        var domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getType());

        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(ServiceMethod.of1(ViewToolService::getSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT, "Sequence{" + domainType + "}"))
                .childrenExpression(ServiceMethod.of1(ViewToolService::getSelectionDialogChildren).aqlSelf("Sequence{" + domainType + "}"))
                .isSelectableExpression(AQLConstants.AQL_SELF + ".oclIsKindOf(" + domainType + ")")
                .build();
        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .defaultTitleExpression("New Flow")
                .noSelectionTitleExpression("New Flow")
                .withSelectionTitleExpression("New Flow")
                .descriptionExpression("Create an Flow:")
                .noSelectionActionLabelExpression("Create a new Flow")
                .noSelectionActionDescriptionExpression("Create a new Flow without Payload")
                .withSelectionActionLabelExpression("Select a Payload for the new Flow")
                .withSelectionActionDescriptionExpression("Create a new Flow with a Payload")
                .noSelectionActionStatusMessageExpression("It will create a new Flow without Payload")
                .selectionRequiredWithoutSelectionStatusMessageExpression("Select one Element to be added as the new Flow Payload")
                .selectionRequiredWithSelectionStatusMessageExpression(AQLConstants.AQL + "'It will create an Flow with ' + selectedObjects->first().name + ' as Payload'")
                .optional(true)
                .build();
    }

}
