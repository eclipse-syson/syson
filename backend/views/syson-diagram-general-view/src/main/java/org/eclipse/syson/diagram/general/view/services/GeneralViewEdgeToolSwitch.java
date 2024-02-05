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
package org.eclipse.syson.diagram.general.view.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.diagrams.description.EdgeDescription;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.SysmlEClassSwitch;

/**
 * Switch retrieving the list of EdgeTools for each SysMLv2 concept represented in the General View diagram.
 * 
 * @author arichard
 */
public class GeneralViewEdgeToolSwitch extends SysmlEClassSwitch<Void> {

    private static final String METAMODEL_ICNS_PATH = "/icons/full/obj16/";

    private static final String SVG = ".svg";

    private final ViewBuilders viewBuilderHelper;

    private final DiagramBuilders diagramBuilderHelper;

    private final List<EdgeTool> edgeTools;

    private final NodeDescription nodeDescription;

    private final List<NodeDescription> allNodeDescriptions;

    public GeneralViewEdgeToolSwitch(NodeDescription nodeDescription, List<NodeDescription> allNodeDescriptions) {
        this.viewBuilderHelper = new ViewBuilders();
        this.diagramBuilderHelper = new DiagramBuilders();
        this.edgeTools = new ArrayList<>();
        this.nodeDescription = Objects.requireNonNull(nodeDescription);
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
    }

    public List<EdgeTool> getEdgeTools() {
        return this.edgeTools;
    }

    @Override
    public Void caseDefinition(Definition object) {
        this.edgeTools.add(this.createDependencyEdgeTool(this.allNodeDescriptions));
        this.edgeTools.add(this.createSubclassificationEdgeTool(List.of(this.nodeDescription)));
        return super.caseDefinition(object);
    }

    @Override
    public Void casePackage(Package object) {
        this.edgeTools.add(this.createDependencyEdgeTool(this.allNodeDescriptions));
        return super.casePackage(object);
    }

    @Override
    public Void casePartDefinition(PartDefinition object) {
        this.edgeTools.add(this.createAddAsNestedPartEdgeTool(
                this.allNodeDescriptions.stream().filter(nodeDesc -> GVDescriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()).equals(nodeDesc.getName())).toList()));
        return super.casePartDefinition(object);
    }

    @Override
    public Void casePartUsage(PartUsage object) {
        this.edgeTools.add(this.createBecomeNestedPartEdgeTool(
                this.allNodeDescriptions.stream().filter(nodeDesc -> GVDescriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()).equals(nodeDesc.getName())
                        || GVDescriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartDefinition()).equals(nodeDesc.getName())).toList()));
        return super.casePartUsage(object);
    }

    @Override
    public Void caseUsage(Usage object) {
        this.edgeTools.add(this.createDependencyEdgeTool(this.allNodeDescriptions));
        this.edgeTools.add(this.createRedefinitionEdgeTool(List.of(this.nodeDescription)));
        this.edgeTools.add(this.createSubsettingEdgeTool(List.of(this.nodeDescription)));
        return super.caseUsage(object);
    }

    protected EdgeTool createDependencyEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var setName = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getElement_DeclaredName().getName())
                .valueExpression("dependency");

        var setClient = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getDependency_Client().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setSupplier = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getDependency_Supplier().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(setName.build(), setClient.build(), setSupplier.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getDependency()))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var changeContextMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newOwningMembership")
                .children(createInstance.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getOwningMembership()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newOwningMembership")
                .children(changeContextMembership.build());

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE + ".getContainerPackage()")
                .children(createMembership.build());

        return builder
                .name("New " + SysmlPackage.eINSTANCE.getDependency().getName())
                .iconURLsExpression(METAMODEL_ICNS_PATH + SysmlPackage.eINSTANCE.getDependency().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createSubclassificationEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var setName = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getElement_DeclaredName().getName())
                .valueExpression("specializes");

        var setSubclassifier = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubclassification_Subclassifier().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setSuperclassifier = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubclassification_Superclassifier().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var setSpecific = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_Specific().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setGeneral = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_General().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(setName.build(), setSubclassifier.build(), setSuperclassifier.build(), setSpecific.build(), setGeneral.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getSubclassification()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE)
                .children(createInstance.build());

        return builder
                .name("New " + SysmlPackage.eINSTANCE.getSubclassification().getName())
                .iconURLsExpression(METAMODEL_ICNS_PATH + SysmlPackage.eINSTANCE.getSubclassification().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    protected EdgeTool createRedefinitionEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var setName = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getElement_DeclaredName().getName())
                .valueExpression("redefines");

        var setRedefiningFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getRedefinition_RedefiningFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setRedefinedFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getRedefinition_RedefinedFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var setSubsettingFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubsetting_SubsettingFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setSubsettedFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubsetting_SubsettedFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var setSpecific = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_Specific().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setGeneral = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_General().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(setName.build(), setRedefiningFeature.build(), setRedefinedFeature.build(), setSubsettingFeature.build(), setSubsettedFeature.build(), setSpecific.build(),
                        setGeneral.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getRedefinition()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE)
                .children(createInstance.build());

        return builder
                .name("New " + SysmlPackage.eINSTANCE.getRedefinition().getName())
                .iconURLsExpression(METAMODEL_ICNS_PATH + SysmlPackage.eINSTANCE.getRedefinition().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    protected EdgeTool createSubsettingEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var setName = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getElement_DeclaredName().getName())
                .valueExpression("subsets");

        var setSubsettingFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubsetting_SubsettingFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setSubsettedFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubsetting_SubsettedFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var setSpecific = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_Specific().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setGeneral = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_General().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(setName.build(), setSubsettingFeature.build(), setSubsettedFeature.build(), setSpecific.build(),
                        setGeneral.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getSubsetting()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE)
                .children(createInstance.build());

        return builder
                .name("New " + SysmlPackage.eINSTANCE.getSubsetting().getName())
                .iconURLsExpression(METAMODEL_ICNS_PATH + SysmlPackage.eINSTANCE.getSubsetting().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createAddAsNestedPartEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE + ".addAsNestedPart(" + EdgeDescription.SEMANTIC_EDGE_TARGET + ")");

        return builder
                .name("Add Part Usage as nested " + SysmlPackage.eINSTANCE.getPartUsage().getName())
                .iconURLsExpression(METAMODEL_ICNS_PATH + SysmlPackage.eINSTANCE.getMembership().getName() + SVG)
                .body(callService.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createBecomeNestedPartEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE + ".becomeNestedPart(" + EdgeDescription.SEMANTIC_EDGE_TARGET + ")");

        return builder
                .name("Become nested " + SysmlPackage.eINSTANCE.getPartUsage().getName())
                .iconURLsExpression(METAMODEL_ICNS_PATH + SysmlPackage.eINSTANCE.getMembership().getName() + SVG)
                .body(callService.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }
}
