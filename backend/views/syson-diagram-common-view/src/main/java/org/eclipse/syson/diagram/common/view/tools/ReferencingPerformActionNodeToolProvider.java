/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.common.view.nodes.ActionFlowCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.description.ReferencingPerformActionUsageNodeDescriptionService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to add the 'perform' action in actions body for all diagrams.
 *
 * @author Jerome Gout
 */
public class ReferencingPerformActionNodeToolProvider extends AbstractFreeFormCompartmentNodeToolProvider {

    public ReferencingPerformActionNodeToolProvider(EClass ownerEClass, IDescriptionNameGenerator descriptionNameGenerator) {
        super(ownerEClass, ActionFlowCompartmentNodeDescriptionProvider.COMPARTMENT_LABEL, descriptionNameGenerator);
    }

    @Override
    protected String getNodeDescriptionName() {
        return this.getDescriptionNameGenerator().getNodeName(ReferencingPerformActionUsageNodeDescriptionService.REFERENCING_PERFORM_ACTION_NAME);
    }

    @Override
    protected String getCreationServiceCallExpression() {
        return "";
    }

    @Override
    protected String getLabel() {
        return "New Perform";
    }

    @Override
    protected String getIconPath() {
        return "/icons/full/obj16/PerformActionUsage.svg";
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var setReferencedFeature = this.viewBuilderHelper.newSetValue()
                .featureName("referencedFeature")
                .valueExpression("aql:selectedObject");

        var changeContextReferenceSubsetting = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newRefSubsetting")
                .children(setReferencedFeature.build());

        var initializeReferenceSubsetting = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression("newRefSubsetting", "elementInitializer"));

        var createReferenceSubsettingInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getReferenceSubsetting()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newRefSubsetting")
                .children(initializeReferenceSubsetting.build(), changeContextReferenceSubsetting.build());

        var changeContextInitializeNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression("newInstance", "elementInitializer"));

        var params = List.of(
                AQLUtils.aqlString(this.compartmentName),
                "selectedNode",
                IEditingContext.EDITING_CONTEXT,
                IDiagramContext.DIAGRAM_CONTEXT,
                "convertedNodes");

        var createView = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("createViewInFreeFormCompartment", params));

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(createView.build(), createReferenceSubsettingInstance.build());

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPerformActionUsage()))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build(), changeContextInitializeNewInstance.build());

        var domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getActionUsage());

        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(AQLUtils.getServiceCallExpression("editingContext", "getActionReferenceSelectionDialogElements"))
                .childrenExpression(AQLUtils.getSelfServiceCallExpression("getActionReferenceSelectionDialogChildren"))
                .build();

        var selectExistingStateUsage = this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .selectionMessage("Select an existing Action to perform:");

        var changeContexMembership = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("createMembership"))
                .children(createEClassInstance.build());

        var reveal = this.viewBuilderHelper.newChangeContext()
                .expression(
                        AQLUtils.getServiceCallExpression("selectedNode", "revealCompartment", List.of("self", IDiagramContext.DIAGRAM_CONTEXT, IEditingContext.EDITING_CONTEXT, "convertedNodes")));

        return builder
                .name(this.getLabel())
                .iconURLsExpression(this.getIconPath())
                .body(changeContexMembership.build(), reveal.build())
                .dialogDescription(selectExistingStateUsage.build())
                .preconditionExpression(this.getPreconditionServiceCallExpression())
                .build();
    }
}
