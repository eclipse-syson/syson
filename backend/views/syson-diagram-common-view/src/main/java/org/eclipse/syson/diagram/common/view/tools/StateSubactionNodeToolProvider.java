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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.ChangeContext;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.DialogDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.common.view.services.ViewNodeService;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Node Tool of StateUsage and StateDefinition to create StateSubaction child elements.
 *
 * @author Jerome Gout
 */
public class StateSubactionNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final StateSubactionKind kind;

    private final boolean isReferencing;

    public StateSubactionNodeToolProvider(StateSubactionKind kind, boolean isReferencing) {
        this.kind = kind;
        this.isReferencing = isReferencing;
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {

        var tool = this.diagramBuilderHelper.newNodeTool()
                .name(this.getNodeToolLabel())
                .iconURLsExpression("/icons/full/obj16/PerformActionUsage.svg")
                .body(this.getCreateSubactionOperation())
                .preconditionExpression(ServiceMethod.of1(ViewCreateService::isEmptyOfActionKindCompartment).aqlSelf(AQLUtils.aqlString(this.kind.getLiteral())));

        if (this.isReferencing) {
            tool.dialogDescription(this.getExistingActionSelectionDialog());
        }

        return tool.build();
    }

    private String getNodeToolLabel() {
        String result =  "New " + StringUtils.capitalize(this.kind.getName()) + " Action";
        if (this.isReferencing) {
            result += " with referenced Action";
        }
        return result;
    }

    private ChangeContext getCreateSubactionOperation() {
        var revealOperation = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(ViewNodeService::revealCompartment).aql(Node.SELECTED_NODE, AQLConstants.SELF, DiagramContext.DIAGRAM_CONTEXT, IEditingContext.EDITING_CONTEXT,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var performedAction = "selectedObject";
        if (!this.isReferencing) {
            performedAction = "null";
        }
        return this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of2(ViewCreateService::createStateSubaction).aqlSelf(performedAction, AQLUtils.aqlString(this.kind.getLiteral())))
                .children(revealOperation.build())
                .build();
    }

    private DialogDescription getExistingActionSelectionDialog() {
        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(ServiceMethod.of0(ViewToolService::getActionReferenceSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT))
                .childrenExpression(ServiceMethod.of0(ViewToolService::getActionReferenceSelectionDialogChildren).aqlSelf())
                .build();

        var selectExistingActionUsage = this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .selectionMessage("Select an existing Action to associate to the " + StringUtils.capitalize(this.kind.getName()) + " action you want to create:");

        return selectExistingActionUsage.build();
    }
}
