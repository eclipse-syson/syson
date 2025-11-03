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
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.model.services.aql.ModelMutationAQLService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Node Tool with Selection Dialog to select an existing StateUsage to associate to the new ExhibitState to create.
 *
 * @author arichard
 */
public class ExhibitStateWithReferenceNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public ExhibitStateWithReferenceNodeToolProvider(IDescriptionNameGenerator descriptionNameGenerator) {
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
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

        var nodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getExhibitStateUsage())).orElse(null);

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(NodeContainmentKind.CHILD_NODE)
                .elementDescription(nodeDescription)
                .parentViewExpression(AQLUtils.getSelfServiceCallExpression("getParentViewExpression", Node.SELECTED_NODE))
                .semanticElementExpression("aql:newInstance")
                .variableName("newInstanceView");

        var revealOperation = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(Node.SELECTED_NODE, "revealCompartment",
                        List.of("newInstance", DiagramContext.DIAGRAM_CONTEXT, IEditingContext.EDITING_CONTEXT, ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE)));

        var updateExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(DiagramMutationAQLService::expose).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression("newInstance", "elementInitializer"))
                .children(createReferenceSubsettingInstance.build(), updateExposedElements.build());

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getExhibitStateUsage()))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(createView.build(), changeContextNewInstance.build(), revealOperation.build());

        var domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getStateUsage());

        var selectionDialogTree = this.diagramBuilderHelper.newSelectionDialogTreeDescription()
                .elementsExpression(AQLUtils.getSelfServiceCallExpression("getAllReachable", domainType))
                .build();

        var selectExistingStateUsage = this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionDialogTreeDescription(selectionDialogTree)
                .selectionMessage("Select an existing State to associate to the ExhibitState you want to create:");

        var changeContextMembership = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(ModelMutationAQLService::createMembership).aqlSelf())
                .children(createEClassInstance.build());

        var changeContextViewUsageOwner = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("getViewUsageOwner"))
                .children(changeContextMembership.build());

        var toolLabel = this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getExhibitStateUsage()) + " with referenced State";

        return builder
                .name(toolLabel)
                .iconURLsExpression("/icons/full/obj16/ExhibitStateUsage.svg")
                .body(changeContextViewUsageOwner.build())
                .dialogDescription(selectExistingStateUsage.build())
                .build();
    }

}
