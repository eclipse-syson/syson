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
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
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
 * Node Tool allowing to create a SatisfyRequirement with the following syntax: "satisfy req by subj", where req is a
 * ReferenceSubsetting to an existing RequirementUsage and subj is a SubjectMembership containing a ReferenceUsage
 * pointing to a Feature.
 *
 * @author arichard
 */
public class SatisfyNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        String reqUsageType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getRequirementUsage());

        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(ServiceMethod.of1(ViewToolService::getSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT, "Sequence{" + reqUsageType + "}"))
                .childrenExpression(ServiceMethod.of1(ViewToolService::getSelectionDialogChildren).aqlSelf("Sequence{" + reqUsageType + "}"))
                .isSelectableExpression(AQLConstants.AQL_SELF + ".oclIsKindOf(" + reqUsageType + ")")
                .build();
        var selectionDialog = this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .selectionMessage("Select an existing Requirement:")
                .build();

        var updateExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(DiagramMutationAQLService::expose).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var applyNewSatisfyTool = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(ModelMutationAQLService::createSatisfy).aqlSelf("selectedObject"))
                .children(updateExposedElements.build())
                .build();

        return this.diagramBuilderHelper.newNodeTool()
                .name("New Satisfy")
                .iconURLsExpression("/icons/full/obj16/SatisfyRequirementUsage.svg")
                .dialogDescription(selectionDialog)
                .body(applyNewSatisfyTool)
                .build();
    }
}
