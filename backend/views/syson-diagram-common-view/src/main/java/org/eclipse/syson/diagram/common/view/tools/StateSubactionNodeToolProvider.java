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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.view.ChangeContext;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.DialogDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.util.AQLUtils;

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
                .preconditionExpression(AQLUtils.getSelfServiceCallExpression("isEmptyOfActionKindCompartment", AQLUtils.aqlString(this.kind.getLiteral())));

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
                .expression(AQLUtils.getServiceCallExpression("selectedNode", "revealCompartment", List.of("self", IDiagramContext.DIAGRAM_CONTEXT, IEditingContext.EDITING_CONTEXT, "convertedNodes")));

        var params = new ArrayList<String>();
        if (this.isReferencing) {
            params.add("selectedObject");
        } else {
            params.add("null");
        }
        params.add(AQLUtils.aqlString(this.kind.getLiteral()));
        return this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("createStateSubaction", params))
                .children(revealOperation.build())
                .build();
    }

    private DialogDescription getExistingActionSelectionDialog() {
        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(AQLUtils.getServiceCallExpression("editingContext", "getStateSubactionReferenceSelectionDialogElements"))
                .childrenExpression(AQLUtils.getSelfServiceCallExpression("getStateSubactionReferenceSelectionDialogChildren"))
                .build();

        var selectExistingActionUsage = this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .selectionMessage("Select an existing Action to associate to the " + StringUtils.capitalize(this.kind.getName()) + " action you want to create:");

        return selectExistingActionUsage.build();
    }
}
