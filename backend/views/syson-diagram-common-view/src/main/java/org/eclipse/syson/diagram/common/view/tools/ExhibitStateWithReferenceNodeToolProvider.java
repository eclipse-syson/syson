/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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

import java.util.Objects;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
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

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression("newInstance", "elementInitializer"));

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getExhibitStateUsage()))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var nodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getStateUsage())).orElse(null);

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(NodeContainmentKind.CHILD_NODE)
                .elementDescription(nodeDescription)
                .parentViewExpression("aql:selectedNode")
                .semanticElementExpression("aql:selectedObject")
                .variableName("newInstanceView");

        var domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getStateUsage());

        var selectExistingStateUsage = this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getAllReachable", domainType))
                .selectionMessage("Select an existing State to associate to the ExhibitState you want to create:");

        var changeContexMembership = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("createMembership"))
                .children(createEClassInstance.build(), createReferenceSubsettingInstance.build(), createView.build());

        var toolLabel = this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getExhibitStateUsage()) + " with referenced State";

        return builder
                .name(toolLabel)
                .iconURLsExpression("/icons/full/obj16/ExhibitStateUsage.svg")
                .body(changeContexMembership.build())
                .dialogDescription(selectExistingStateUsage.build())
                .build();
    }

}
