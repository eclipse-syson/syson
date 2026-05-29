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

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.trees.renderer.TreeRenderer;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.DialogDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.model.services.aql.ModelMutationAQLService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to create a Framed {@link org.eclipse.syson.sysml.ConcernUsage} in a {@link org.eclipse.syson.sysml.RequirementUsage} or a {@link org.eclipse.syson.sysml.RequirementDefinition}.
 *
 * @author gcoutable
 */
public class FramedConcernNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var updateExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(DiagramMutationAQLService::expose).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var selectedElementToExpose = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getFramedConcernMembership_ReferencedConcern().getName())
                .children(updateExposedElements.build());

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(ModelMutationAQLService::createFramedConcern).aqlSelf("selectedObject"))
                .children(selectedElementToExpose.build());

        return builder
                .name(this.getToolName())
                .iconURLsExpression("/icons/full/obj16/ConcernUsage.svg")
                .body(body.build())
                .dialogDescription(this.getSelectionDialogDescription())
                .build();
    }

    private String getToolName() {
        return "New Framed Concern";
    }

    private DialogDescription getSelectionDialogDescription() {
        String concernUsageType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getConcernUsage());

        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .isSelectableExpression(AQLConstants.AQL_SELF + ".oclIsKindOf(" + concernUsageType + ")")
                .elementsExpression(ServiceMethod.of0(ViewToolService::getConcernReferenceSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT))
                .childrenExpression(ServiceMethod.of2(ViewToolService::getConcernReferenceSelectionDialogChildren).aqlSelf(IEditingContext.EDITING_CONTEXT, TreeRenderer.EXPANDED))
                .build();

        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .defaultTitleExpression(this.getToolName())
                .noSelectionTitleExpression(this.getToolName())
                .withSelectionTitleExpression(this.getToolName())
                .descriptionExpression("Create a Framed Concern")
                .noSelectionActionLabelExpression("Create a New Framed Concern")
                .noSelectionActionDescriptionExpression("Create a New Framed Concern without referencing an existing Concern")
                .withSelectionActionLabelExpression("Select an existing Concern to frame")
                .withSelectionActionDescriptionExpression("Create a New Framed Concern referencing the selected Concern")
                .noSelectionActionStatusMessageExpression("It will create a New Framed Concern without referencing an existing Concern")
                .selectionRequiredWithoutSelectionStatusMessageExpression("Select one Concern to frame")
                .selectionRequiredWithSelectionStatusMessageExpression(AQLConstants.AQL + "'It will create a new Concern referencing ' + selectedObjects->first().name")
                .optional(true)
                .build();
    }
}
