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

import java.util.Objects;

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
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Node Tool of StateUsage and StateDefinition to create StateSubaction child elements.
 *
 * @author Jerome Gout
 */
public class StateSubactionNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final StateSubactionKind kind;

    public StateSubactionNodeToolProvider(StateSubactionKind kind) {
        this.kind = Objects.requireNonNull(kind);
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newNodeTool()
                .name(this.getNodeToolLabel())
                .iconURLsExpression("/icons/full/obj16/PerformActionUsage.svg")
                .body(this.getCreateSubactionOperation())
                .preconditionExpression(ServiceMethod.of1(ViewCreateService::isEmptyOfActionKindCompartment).aqlSelf(AQLUtils.aqlString(this.kind.getLiteral())))
                .dialogDescription(this.getExistingActionSelectionDialog())
                .build();
    }

    private String getNodeToolLabel() {
        return "New " + StringUtils.capitalize(this.kind.getName()) + " Action";
    }

    private ChangeContext getCreateSubactionOperation() {
        var revealOperation = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(DiagramMutationAQLService::revealCompartment).aql(Node.SELECTED_NODE, AQLConstants.SELF, DiagramContext.DIAGRAM_CONTEXT, IEditingContext.EDITING_CONTEXT,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        return this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of2(ViewCreateService::createStateSubaction).aqlSelf("selectedObject", AQLUtils.aqlString(this.kind.getLiteral())))
                .children(revealOperation.build())
                .build();
    }

    private DialogDescription getExistingActionSelectionDialog() {
        String actionUsageType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getActionUsage());

        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .isSelectableExpression(AQLConstants.AQL_SELF + ".oclIsKindOf(" + actionUsageType + ")")
                .elementsExpression(ServiceMethod.of0(ViewToolService::getActionReferenceSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT))
                .childrenExpression(ServiceMethod.of0(ViewToolService::getActionReferenceSelectionDialogChildren).aqlSelf())
                .build();

        var actionKind = StringUtils.capitalize(this.kind.getName());
        var selectExistingActionUsage = this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .defaultTitleExpression(this.getNodeToolLabel())
                .noSelectionTitleExpression(this.getNodeToolLabel())
                .withSelectionTitleExpression(this.getNodeToolLabel())
                .descriptionExpression("Create a " + actionKind + " Action:")
                .noSelectionActionLabelExpression("Create a new " + actionKind + " Action")
                .noSelectionActionDescriptionExpression("Create a new " + actionKind + " Action without reference to an existing Action")
                .withSelectionActionLabelExpression("Select an existing Action referenced by the " + actionKind + " Action you want to create")
                .withSelectionActionDescriptionExpression("Create a new " + actionKind + " Action referencing the selected Action")
                .noSelectionActionStatusMessageExpression("It will create a new " + actionKind + " Action without reference to an existing Action")
                .selectionRequiredWithoutSelectionStatusMessageExpression("Select an Action referenced by the new " + actionKind + " Action")
                .selectionRequiredWithSelectionStatusMessageExpression(AQLConstants.AQL + "'It will create a " + actionKind + " Action referencing ' + selectedObjects->first().name")
                .optional(true);

        return selectExistingActionUsage.build();
    }
}
