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

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Node tool provider for Actor compartment in the element that need such compartment.
 *
 * @author gdaniel
 */
public class ActorCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    private final EClass parentEClass;

    private final EReference eReference;

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public ActorCompartmentNodeToolProvider(EClass parentEClass, EReference eReference, IDescriptionNameGenerator descriptionNameGenerator) {
        super();
        this.parentEClass = Objects.requireNonNull(parentEClass);
        this.eReference = Objects.requireNonNull(eReference);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    protected String getServiceCallExpression() {
        return "";
    }

    @Override
    protected String getNodeToolName() {
        return "New Actor";
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/Actor.svg";
    }

    @Override
    protected boolean revealOnCreate() {
        return true;
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var setType = this.viewBuilderHelper.newSetValue()
                .featureName("type")
                .valueExpression("aql:selectedObject");

        var changeContextFeatureTyping = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newFeatureTyping")
                .children(setType.build());

        var initializeFeatureTyping = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression("newFeatureTyping", "elementInitializer"));

        var createFeatureTypingInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getFeatureTyping()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newFeatureTyping")
                .children(initializeFeatureTyping.build(), changeContextFeatureTyping.build());

        var setName = this.viewBuilderHelper.newSetValue()
                .featureName("declaredName")
                .valueExpression("actor");

        var changeContextInitializeNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression("newInstance", "elementInitializer"));

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance");

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPartUsage()))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(changeContextInitializeNewInstance.build(), setName.build(), createFeatureTypingInstance.build(), changeContextNewInstance.build());

        var nodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getCompartmentItemName(this.parentEClass, this.eReference))
                .orElse(null);

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(NodeContainmentKind.CHILD_NODE)
                .elementDescription(nodeDescription)
                .parentViewExpression(AQLUtils.getSelfServiceCallExpression("getParentViewExpression", "selectedNode"))
                .semanticElementExpression("aql:newInstance")
                .variableName("newInstanceView");

        var reveal = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression("selectedNode", "revealCompartment", List.of("self", "diagramContext", "editingContext", "convertedNodes")));

        var domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPartUsage());

        var selectExistingUsage = this.diagramBuilderHelper.newSelectionDialogDescription()
                .selectionCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getAllReachable", List.of(domainType, "false")))
                .selectionMessage("Select an existing PartUsage as actor:");

        var changeContexMembership = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("createMembership", SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getActorMembership())))
                .children(createEClassInstance.build(), createView.build(), reveal.build());

        return builder
                .name(this.getNodeToolName())
                .iconURLsExpression(this.getNodeToolIconURLsExpression())
                .body(changeContexMembership.build())
                .dialogDescription(selectExistingUsage.build())
                .preconditionExpression(this.getPreconditionExpression())
                .build();
    }
}
