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

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.DialogDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SelectionDialogTreeDescription;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.sysml.PortionKind;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to create a snapshot in an in any subclass of {@link org.eclipse.syson.sysml.OccurrenceUsage}.
 *
 * <p>
 * Displays the selection dialog to choose which snapshot to create.
 * </p>
 *
 * @author gcoutable
 */
public class OccurrenceUsageSnapshotNodeToolProvider implements INodeToolProvider {

    private static final List<EClass> SUPPORTED_PORTION_KIND_ELEMENTS = List.of(
            SysmlPackage.eINSTANCE.getAcceptActionUsage(),
            SysmlPackage.eINSTANCE.getActionUsage(),
            SysmlPackage.eINSTANCE.getAllocationUsage(),
            SysmlPackage.eINSTANCE.getAssignmentActionUsage(),
            SysmlPackage.eINSTANCE.getCaseUsage(),
            SysmlPackage.eINSTANCE.getConcernUsage(),
            SysmlPackage.eINSTANCE.getConstraintUsage(),
            SysmlPackage.eINSTANCE.getExhibitStateUsage(),
            SysmlPackage.eINSTANCE.getInterfaceUsage(),
            SysmlPackage.eINSTANCE.getItemUsage(),
            SysmlPackage.eINSTANCE.getOccurrenceUsage(),
            SysmlPackage.eINSTANCE.getPartUsage(),
            SysmlPackage.eINSTANCE.getPerformActionUsage(),
            SysmlPackage.eINSTANCE.getPortUsage(),
            SysmlPackage.eINSTANCE.getRequirementUsage(),
            SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(),
            SysmlPackage.eINSTANCE.getStateUsage(),
            SysmlPackage.eINSTANCE.getUseCaseUsage(),
            SysmlPackage.eINSTANCE.getViewUsage()
    );

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {

        var setIsPortion = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getFeature_IsPortion().getName())
                .valueExpression("true");

        var setSnapshot = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getOccurrenceUsage_PortionKind().getName())
                .valueExpression(PortionKind.SNAPSHOT.getLiteral())
                .children(setIsPortion.build());

        var updateExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(DiagramMutationAQLService::expose).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(ViewCreateService::elementInitializer).aql("newInstance"))
                .children(
                    updateExposedElements.build(),
                    setSnapshot.build()
                );

        var letNewSnapshot = this.viewBuilderHelper.newLet()
                .variableName("newInstance")
                .valueExpression("aql:self.createOccurrenceInOccurrence(objectToCreate)")
                .children(changeContextNewInstance.build());

        var letObjectToCreate = this.viewBuilderHelper.newLet()
                .variableName("objectToCreate")
                .valueExpression("aql:if selectedObject == null then sysml::OccurrenceUsage else selectedObject endif")
                .children(letNewSnapshot.build());

        return this.diagramBuilderHelper.newNodeTool()
                .name("New Snapshot")
                .iconURLsExpression("/icons/full/obj16/OccurrenceUsage.svg")
                .body(letObjectToCreate.build())
                .dialogDescription(this.getDescriptionDialog())
                .build();
    }

    private DialogDescription getDescriptionDialog() {
        return this.diagramBuilderHelper.newSelectionDialogDescription()
                .optional(true)
                .selectionMessage("Choose the snapshot to create")
                .noSelectionLabel("Create a snapshot occurrence usage")
                .selectionDialogTreeDescription(this.getDialogTreeDescriptionDialog())
                .build();
    }

    private SelectionDialogTreeDescription getDialogTreeDescriptionDialog() {
        String elementsExpression = SUPPORTED_PORTION_KIND_ELEMENTS.stream().map(SysMLMetamodelHelper::buildQualifiedName).collect(Collectors.joining(",", "aql:Sequence{", "}"));
        return this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(elementsExpression)
                .isSelectableExpression(AQLConstants.AQL_TRUE)
                .build();
    }
}
