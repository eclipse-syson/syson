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

import java.util.List;

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
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Node Tool allowing to create a SatisfyRequirement.
 *
 * @author arichard
 */
public class SatisfyRequirementNodeToolProvider implements INodeToolProvider {

    private static final String TOOL_NAME = "New Satisfy Requirement";

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var addToExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(DiagramMutationAQLService::expose).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var newSatisfyRequirementTool = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(ModelMutationAQLService::createSatisfyRequirement).aqlSelf("selectedObject"))
                .children(addToExposedElements.build())
                .build();

        return builder.name(TOOL_NAME)
                .iconURLsExpression("/icons/full/obj16/SatisfyRequirementUsage.svg")
                .dialogDescription(this.getDialogDescription())
                .body(newSatisfyRequirementTool)
                .build();
    }

    private DialogDescription getDialogDescription() {
        var reqUsageType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getRequirementUsage());
        var defType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getDefinition());
        var selectableTypes = List.of(reqUsageType, defType);

        var selectionDialogTreeDescription = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(ServiceMethod.of1(ViewToolService::getSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT, AQLUtils.aqlSequence(selectableTypes)))
                .childrenExpression(
                        ServiceMethod.of3(ViewToolService::getSelectionDialogChildren).aqlSelf(IEditingContext.EDITING_CONTEXT, TreeRenderer.EXPANDED, AQLUtils.aqlSequence(selectableTypes)))
                .isSelectableExpression(AQLConstants.AQL + "self.oclIsKindOf(" + defType + ") or self.oclIsKindOf(" + reqUsageType + ")")
                .build();

        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTreeDescription)
                .defaultTitleExpression(TOOL_NAME)
                .noSelectionTitleExpression(TOOL_NAME)
                .withSelectionTitleExpression(TOOL_NAME)
                .descriptionExpression(AQLConstants.AQL + "'Create a Satisfy Requirement with ' + self.name + ' as subject:'")
                .noSelectionActionLabelExpression("Create a new Satisfy Requirement")
                .noSelectionActionDescriptionExpression("Create a new Satisfy Requirement without specialization")
                .withSelectionActionLabelExpression("Select an existing Element as specialization")
                .withSelectionActionDescriptionExpression("Create a new specialized Satisfy Requirement")
                .noSelectionActionStatusMessageExpression("It will create a new Satisfy Requirement without specialization")
                .selectionRequiredWithoutSelectionStatusMessageExpression("Select one Element to specialize the new Satisfy Requirement")
                .selectionRequiredWithSelectionStatusMessageExpression(AQLConstants.AQL + "'It will create a Satisfy Requirement specialized with ' + selectedObjects->first().name")
                .optional(true)
                .build();
    }
}
