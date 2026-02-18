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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.common.view.nodes.ActionFlowCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.common.view.services.ViewNodeService;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.model.services.aql.ModelMutationAQLService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
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
        return this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getActionUsage());
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
                .expression(ServiceMethod.of0(ViewCreateService::elementInitializer).aql("newRefSubsetting"));

        var createReferenceSubsettingInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getReferenceSubsetting()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newRefSubsetting")
                .children(initializeReferenceSubsetting.build(), changeContextReferenceSubsetting.build());

        var changeContextInitializeNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(ViewCreateService::elementInitializer).aql("newInstance"));

        var addToExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(DiagramMutationAQLService::expose).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var reveal = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(ViewNodeService::revealCompartment).aql(Node.SELECTED_NODE, "newInstance", DiagramContext.DIAGRAM_CONTEXT, IEditingContext.EDITING_CONTEXT,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(addToExposedElements.build(), createReferenceSubsettingInstance.build(), reveal.build());

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPerformActionUsage()))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build(), changeContextInitializeNewInstance.build());

        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(ServiceMethod.of0(ViewToolService::getActionReferenceSelectionDialogElements).aql(IEditingContext.EDITING_CONTEXT))
                .childrenExpression(ServiceMethod.of0(ViewToolService::getActionReferenceSelectionDialogChildren).aqlSelf())
                .build();

        var selectExistingStateUsage = this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .selectionMessage("Select an existing Action to perform:")
                .optional(false);

        var createMembership = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(ModelMutationAQLService::createMembership).aqlSelf())
                .children(createEClassInstance.build());

        return builder
                .name(this.getLabel())
                .iconURLsExpression(this.getIconPath())
                .body(createMembership.build())
                .dialogDescription(selectExistingStateUsage.build())
                .preconditionExpression(this.getPreconditionServiceCallExpression())
                .build();
    }
}
