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
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SelectionDialogDescription;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Node Tool with Selection Dialog to optionally select an existing StateUsage to associate to the new ExhibitState to create.
 *
 * @author arichard
 */
public class ExhibitStateNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final boolean isParallel;

    public ExhibitStateNodeToolProvider(boolean isParallel) {
        this.isParallel = isParallel;
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var setReferencedFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature().getName())
                .valueExpression("aql:selectedObject");

        var changeContextReferenceSubsetting = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newRefSubsetting")
                .children(setReferencedFeature.build());

        var initializeReferenceSubsetting = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(ViewCreateService::elementInitializer).aql("newRefSubsetting"));

        var createReferenceSubsettingInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getReferenceSubsetting()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newRefSubsetting")
                .children(initializeReferenceSubsetting.build(), changeContextReferenceSubsetting.build());

        var conditionalReferenceSubsettingInstance = this.viewBuilderHelper.newIf()
                .conditionExpression(AQLConstants.AQL + "selectedObject <> null")
                .children(createReferenceSubsettingInstance.build());

        var setIsParallel = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getStateUsage_IsParallel().getName())
                .valueExpression(AQLConstants.AQL + this.isParallel);

        var revealOperation = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(DiagramMutationAQLService::revealCompartment).aql(Node.SELECTED_NODE, "self", DiagramContext.DIAGRAM_CONTEXT, IEditingContext.EDITING_CONTEXT,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var conditionalRevealOperation = this.viewBuilderHelper.newIf()
                .conditionExpression(AQLConstants.AQL + Node.SELECTED_NODE + " <> null")
                .children(revealOperation.build());

        var updateExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(DiagramMutationAQLService::expose).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var exposeAndRevealNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF)
                .children(conditionalRevealOperation.build(), updateExposedElements.build());

        var createExhibit = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of6(DiagramMutationAQLService::createChildState).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE, String.valueOf(this.isParallel),
                        String.valueOf(Boolean.TRUE)))
                .children(conditionalReferenceSubsettingInstance.build(), setIsParallel.build(), exposeAndRevealNewInstance.build());

        var changeContextViewUsageOwner = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(UtilService::getViewUsageOwner).aqlSelf())
                .children(createExhibit.build());

        var toolLabel = this.getToolLabel();

        return builder
                .name(toolLabel)
                .iconURLsExpression("/icons/full/obj16/ExhibitStateUsage.svg")
                .body(changeContextViewUsageOwner.build())
                .dialogDescription(this.getSelectionDialog())
                .build();
    }

    private SelectionDialogDescription getSelectionDialog() {
        var domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getStateUsage());

        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(ServiceMethod.of0(ViewToolService::getExhibitStateSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT))
                .childrenExpression(ServiceMethod.of2(ViewToolService::getExhibitStateSelectionDialogChildren).aqlSelf(IEditingContext.EDITING_CONTEXT, TreeRenderer.EXPANDED))
                .isSelectableExpression(AQLConstants.AQL_SELF + ".oclIsKindOf(" + domainType + ")")
                .build();

        String elementToCreateLabel = "Exhibit State";

        if (this.isParallel) {
            elementToCreateLabel = "Exhibit State Parallel";
        }

        var selectExistingStateUsage = this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .defaultTitleExpression(this.getToolLabel())
                .noSelectionTitleExpression(this.getToolLabel())
                .withSelectionTitleExpression(this.getToolLabel())
                .descriptionExpression(this.getDescriptionExpression())
                .noSelectionActionLabelExpression("Create a new " + elementToCreateLabel)
                .noSelectionActionDescriptionExpression("Create a new standalone " + elementToCreateLabel)
                .withSelectionActionLabelExpression(this.getWithSelectionActionLabelExpression())
                .withSelectionActionDescriptionExpression("Select an existing State to associate to the " + elementToCreateLabel + " you want to create")
                .noSelectionActionStatusMessageExpression("It will create a new standalone " + elementToCreateLabel)
                .selectionRequiredWithoutSelectionStatusMessageExpression("Select an existing State to associate to the " + elementToCreateLabel)
                .selectionRequiredWithSelectionStatusMessageExpression(this.getSelectionRequiredWithSelectionStatusMessageExpression())
                .optional(true);

        return selectExistingStateUsage.build();
    }

    private String getToolLabel() {
        StringBuilder builder = new StringBuilder("New Exhibit ");

        if (this.isParallel) {
            builder.append("Parallel ");
        }

        builder.append("State");
        return builder.toString();
    }

    private String getDescriptionExpression() {
        if (this.isParallel) {
            return "Create a parallel Exhibit State:";
        }
        return "Create an Exhibit State:";
    }

    private String getWithSelectionActionLabelExpression() {
        if (this.isParallel) {
            return "Create a parallel Exhibit State associated with the existing State";
        }
        return "Create an Exhibit State associated with the existing State";
    }

    private String getSelectionRequiredWithSelectionStatusMessageExpression() {
        if (this.isParallel) {
            return AQLConstants.AQL + "'It will create a parallel Exhibit State associated with ' + selectedObjects->first().name";
        }
        return AQLConstants.AQL + "'It will create an Exhibit State associated with ' + selectedObjects->first().name";
    }

}
