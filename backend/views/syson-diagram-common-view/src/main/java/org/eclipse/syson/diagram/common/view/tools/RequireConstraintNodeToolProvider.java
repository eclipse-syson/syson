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
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to create a Require {@link org.eclipse.syson.sysml.ConstraintUsage} in a {@link org.eclipse.syson.sysml.RequirementUsage} or a {@link org.eclipse.syson.sysml.RequirementDefinition}.
 *
 * @author gcoutable
 */
public class RequireConstraintNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var updateExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(DiagramMutationAQLService::expose).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        // We expose both the reference constraint and owned constraint because the tool `show content as tree` should display the owned constraint.
        // However, if the reference constraint and the owned constraint are different, it means the owned constraint is subsetted by reference, and thus we hide the owned constraint graphical node
        //  thanks to DiagramMutationExposeService#hideNodeIfHasReferenceSubset
        var exposeReferenceConstraint = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getRequirementConstraintMembership_ReferencedConstraint().getName())
                .children(updateExposedElements.build());

        var exposeOwnedConstraint = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getRequirementConstraintMembership_OwnedConstraint().getName())
                .children(updateExposedElements.build());

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of2(ModelMutationAQLService::createConstraint).aqlSelf("selectedObject", "requirementConstraint"))
                .children(exposeOwnedConstraint.build(), exposeReferenceConstraint.build());

        String requirementConstraintKindType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getRequirementConstraintKind());
        var letRequirementConstraintLiteral = this.viewBuilderHelper.newLet()
                .variableName("requirementConstraint")
                .valueExpression(AQLConstants.AQL + requirementConstraintKindType + ".getEEnumLiteral('" + RequirementConstraintKind.REQUIREMENT.getLiteral() + "').instance")
                .children(body.build());

        return builder
                .name(this.getToolName())
                .iconURLsExpression("/icons/full/obj16/ConstraintUsage.svg")
                .body(letRequirementConstraintLiteral.build())
                .dialogDescription(this.getDialogDescription())
                .build();
    }

    private String getToolName() {
        return "New Require constraint";
    }

    private DialogDescription getDialogDescription() {
        String constraintUsageType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getConstraintUsage());

        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .isSelectableExpression(AQLConstants.AQL_SELF + ".oclIsKindOf(" + constraintUsageType + ")")
                .elementsExpression(ServiceMethod.of0(ViewToolService::getConstraintReferenceSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT))
                .childrenExpression(ServiceMethod.of2(ViewToolService::getConstraintReferenceSelectionDialogChildren).aqlSelf(IEditingContext.EDITING_CONTEXT, TreeRenderer.EXPANDED))
                .build();

        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .defaultTitleExpression(this.getToolName())
                .noSelectionTitleExpression(this.getToolName())
                .withSelectionTitleExpression(this.getToolName())
                .descriptionExpression("Create a Require Constraint")
                .noSelectionActionLabelExpression("Create a New Require Constraint")
                .noSelectionActionDescriptionExpression("Create a New Require Constraint without referencing an existing Constraint")
                .withSelectionActionLabelExpression("Select an existing Constraint to require")
                .withSelectionActionDescriptionExpression("Create a New Require Constraint referencing the selected Constraint")
                .noSelectionActionStatusMessageExpression("It will create a New Require Constraint without referencing an existing Constraint")
                .selectionRequiredWithoutSelectionStatusMessageExpression("Select one Constraint to require")
                .selectionRequiredWithSelectionStatusMessageExpression(AQLConstants.AQL + "'It will create a new Constraint referencing ' + selectedObjects->first().name")
                .optional(true)
                .build();
    }
}
