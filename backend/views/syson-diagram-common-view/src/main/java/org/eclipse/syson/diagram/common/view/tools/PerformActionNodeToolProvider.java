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

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.trees.renderer.TreeRenderer;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to add the 'perform action' action in actions body for all diagrams.
 *
 * @author Jerome Gout
 */
public class PerformActionNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    @Override
    protected String getServiceCallExpression() {
        return ServiceMethod.of0(ViewCreateService::createPerformAction).aqlSelf();
    }

    @Override
    protected String getNodeToolName() {
        return "New Perform action";
    }

    @Override
    protected boolean revealOnCreate() {
        return false;
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/PerformActionUsage.svg";
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var setReferencedFeature = this.viewBuilderHelper.newSetValue()
                .featureName("referencedFeature")
                .valueExpression(AQLConstants.AQL + "selectedObject");

        var changeContextReferenceSubsetting = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + "newRefSubsetting")
                .children(setReferencedFeature.build());

        var initializeReferenceSubsetting = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(ViewCreateService::elementInitializer).aql("newRefSubsetting"));

        var createReferenceSubsettingInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getReferenceSubsetting()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newRefSubsetting")
                .children(initializeReferenceSubsetting.build(), changeContextReferenceSubsetting.build());

        var addToExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(DiagramMutationAQLService::expose).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + "newInstance")
                .children(addToExposedElements.build(), createReferenceSubsettingInstance.build());

        var letNewInstance = this.viewBuilderHelper.newLet()
                .variableName("newInstance")
                .valueExpression(this.getServiceCallExpression())
                .children(changeContextNewInstance.build());

        var rootChangContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF)
                .children(letNewInstance.build());

        return builder
                .name(this.getNodeToolName())
                .iconURLsExpression(this.getNodeToolIconURLsExpression())
                .body(rootChangContext.build())
                .dialogDescription(this.getSelectionDialogDescription())
                .preconditionExpression(this.getPreconditionExpression())
                .build();
    }

    @Override
    protected SelectionDialogDescription getSelectionDialogDescription() {
        String actionUsageType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getActionUsage());

        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .isSelectableExpression(AQLConstants.AQL_SELF + ".oclIsKindOf(" + actionUsageType + ")")
                .elementsExpression(ServiceMethod.of0(ViewToolService::getActionReferenceSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT))
                .childrenExpression(ServiceMethod.of2(ViewToolService::getActionReferenceSelectionDialogChildren).aqlSelf(IEditingContext.EDITING_CONTEXT, TreeRenderer.EXPANDED))
                .build();

        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .defaultTitleExpression(this.getNodeToolName())
                .noSelectionTitleExpression(this.getNodeToolName())
                .withSelectionTitleExpression(this.getNodeToolName())
                .descriptionExpression("Create a Perform action:")
                .noSelectionActionLabelExpression("Create a new Perform action")
                .noSelectionActionDescriptionExpression("Create a new Perform action without referencing an existing Action")
                .withSelectionActionLabelExpression("Select an existing Action to perform:")
                .withSelectionActionDescriptionExpression("Create a new Perform action referencing the selected Action")
                .noSelectionActionStatusMessageExpression("It will create a new Perform action without referencing an existing Action")
                .selectionRequiredWithoutSelectionStatusMessageExpression("Select one Action to be referenced by the new Perform action")
                .selectionRequiredWithSelectionStatusMessageExpression(AQLConstants.AQL + "'It will create a Perform action referencing ' + selectedObjects->first().name")
                .optional(true)
                .build();
    }
}
