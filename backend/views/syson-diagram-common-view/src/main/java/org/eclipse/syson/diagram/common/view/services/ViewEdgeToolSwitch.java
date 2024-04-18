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
package org.eclipse.syson.diagram.common.view.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.description.EdgeDescription;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.SysmlEClassSwitch;

/**
 * Switch retrieving the list of EdgeTools for each SysMLv2 concept represented in all diagrams.
 *
 * @author arichard
 */
public class ViewEdgeToolSwitch extends SysmlEClassSwitch<List<EdgeTool>> {

    private static final String METAMODEL_ICONS_PATH = "/icons/full/obj16/";

    private static final String SVG = ".svg";

    private static final String USAGE = "usage";

    private final ViewBuilders viewBuilderHelper;

    private final DiagramBuilders diagramBuilderHelper;

    private final NodeDescription nodeDescription;

    private final List<NodeDescription> allNodeDescriptions;

    private final IDescriptionNameGenerator nameGenerator;

    public ViewEdgeToolSwitch(NodeDescription nodeDescription, List<NodeDescription> allNodeDescriptions, IDescriptionNameGenerator nameGenerator) {
        this.viewBuilderHelper = new ViewBuilders();
        this.diagramBuilderHelper = new DiagramBuilders();
        this.nodeDescription = Objects.requireNonNull(nodeDescription);
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
        this.nameGenerator = Objects.requireNonNull(nameGenerator);
    }

    @Override
    public List<EdgeTool> caseActionUsage(ActionUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                    || this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getActionDefinition())).collect(Collectors.toList());
        targetNodes.removeIf(nodeDesc -> this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPortUsage()).equals(nodeDesc.getName()));
        targetNodes.removeIf(nodeDesc -> this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAttributeUsage()).equals(nodeDesc.getName()));
        edgeTools.add(this.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getActionUsage(), targetNodes));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseAllocationUsage(AllocationUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                || this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getAllocationDefinition())).toList();
        edgeTools.add(this.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getAllocationUsage(), targetNodes));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseAttributeUsage(AttributeUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                || this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getMetadataDefinition())
                || this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getOccurrenceDefinition())
                || this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getAttributeDefinition())).toList();
        edgeTools.add(this.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getAttributeUsage(), targetNodes));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseConstraintUsage(ConstraintUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                    || this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getConstraintDefinition())).collect(Collectors.toList());
        targetNodes.removeIf(nodeDesc -> this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPortUsage()).equals(nodeDesc.getName()));
        targetNodes.removeIf(nodeDesc -> this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAttributeUsage()).equals(nodeDesc.getName()));
        edgeTools.add(this.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getConstraintUsage(), targetNodes));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseDefinition(Definition object) {
        var edgeTools = new ArrayList<EdgeTool>();
        edgeTools.add(this.createDependencyEdgeTool(this.allNodeDescriptions));
        edgeTools.add(this.createSubclassificationEdgeTool(List.of(this.nodeDescription)));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseInterfaceUsage(InterfaceUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                || this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getInterfaceDefinition())).collect(Collectors.toList());
        targetNodes.removeIf(nodeDesc -> this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPortUsage()).equals(nodeDesc.getName()));
        targetNodes.removeIf(nodeDesc -> this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAttributeUsage()).equals(nodeDesc.getName()));
        edgeTools.add(this.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getInterfaceUsage(), targetNodes));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseItemUsage(ItemUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                || this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getItemDefinition())
                || this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getPartDefinition())).collect(Collectors.toList());
        targetNodes.removeIf(nodeDesc -> this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPortUsage()).equals(nodeDesc.getName()));
        targetNodes.removeIf(nodeDesc -> this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAttributeUsage()).equals(nodeDesc.getName()));
        edgeTools.add(this.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getItemUsage(), targetNodes));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> casePackage(Package object) {
        var edgeTools = new ArrayList<EdgeTool>();
        edgeTools.add(this.createDependencyEdgeTool(this.allNodeDescriptions));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> casePartDefinition(PartDefinition object) {
        var edgeTools = new ArrayList<EdgeTool>();
        edgeTools.add(this.createAddAsNestedPartEdgeTool(
                this.allNodeDescriptions.stream().filter(nodeDesc -> this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getPartUsage())).toList()));
        edgeTools.addAll(this.caseDefinition(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> casePartUsage(PartUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                || this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getOccurrenceDefinition())
                || this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getPartDefinition())).collect(Collectors.toList());
        targetNodes.removeIf(nodeDesc -> this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPortUsage()).equals(nodeDesc.getName()));
        targetNodes.removeIf(nodeDesc -> this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAttributeUsage()).equals(nodeDesc.getName()));
        edgeTools.add(this.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getPartUsage(), targetNodes));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> casePortUsage(PortUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                || this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getPortDefinition())).toList();
        edgeTools.add(this.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getPortUsage(), targetNodes));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseRequirementUsage(RequirementUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                    || this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getRequirementDefinition())).collect(Collectors.toList());
        targetNodes.removeIf(nodeDesc -> this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getPortUsage()));
        targetNodes.removeIf(nodeDesc -> this.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getAttributeUsage()));
        edgeTools.add(this.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getRequirementUsage(), targetNodes));
        var objectiveTargets = this.allNodeDescriptions.stream()
                .filter(n -> this.isTheNodeDescriptionFor(n, SysmlPackage.eINSTANCE.getUseCaseUsage())
                            || this.isTheNodeDescriptionFor(n, SysmlPackage.eINSTANCE.getUseCaseDefinition())).toList();
        edgeTools.add(this.createBecomeObjectiveRequirementEdgeTool(objectiveTargets));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseUsage(Usage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        edgeTools.add(this.createDependencyEdgeTool(this.allNodeDescriptions));
        edgeTools.add(this.createRedefinitionEdgeTool(List.of(this.nodeDescription)));
        edgeTools.add(this.createSubsettingEdgeTool(List.of(this.nodeDescription)));
        edgeTools.add(this.createAllocateEdgeTool(this.allNodeDescriptions));
        var definitionNodeDescription = this.getNodeDescription(this.getDefinitionFromUsage(object));
        if (definitionNodeDescription.isPresent()) {
            edgeTools.add(this.createFeatureTypingEdgeTool(List.of(definitionNodeDescription.get())));
        }
        return edgeTools;
    }

    private Optional<NodeDescription> getNodeDescription(EClassifier classifier) {
        if (classifier == null) {
            return Optional.empty();
        }
        return this.allNodeDescriptions.stream()
                .filter(nd -> nd.getDomainType().equals(classifier.getEPackage().getNsPrefix() + "::" + classifier.getName()))
                .findFirst();
    }

    private EClassifier getDefinitionFromUsage(Usage object) {
        String definitionFromUsage = object.eClass().getName().replace("Usage", "Definition");
        return SysmlPackage.eINSTANCE.getEClassifier(definitionFromUsage);
    }

    private boolean isTheNodeDescriptionFor(NodeDescription nd, EClass eClass) {
        return this.nameGenerator.getNodeName(eClass).equals(nd.getName());
    }

    private EdgeTool createDependencyEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callElementInitializerService = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.elementInitializer()");

        var setClient = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getDependency_Client().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setSupplier = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getDependency_Supplier().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(callElementInitializerService.build(), setClient.build(), setSupplier.build());

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
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getDependency()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getDependency().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createSubclassificationEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callElementInitializerService = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.elementInitializer()");

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
                .children(callElementInitializerService.build(), setSubclassifier.build(), setSuperclassifier.build(), setSpecific.build(), setGeneral.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getSubclassification()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE)
                .children(createInstance.build());

        return builder
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getSubclassification()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getSubclassification().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createRedefinitionEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callElementInitializerService = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.elementInitializer()");

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
                .children(callElementInitializerService.build(), setRedefiningFeature.build(), setRedefinedFeature.build(), setSubsettingFeature.build(), setSubsettedFeature.build(), setSpecific.build(),
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
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getRedefinition()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getRedefinition().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createSubsettingEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callElementInitializerService = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.elementInitializer()");

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
                .children(callElementInitializerService.build(), setSubsettingFeature.build(), setSubsettedFeature.build(), setSpecific.build(),
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
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getSubsetting()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getSubsetting().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createFeatureTypingEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callElementInitializerService = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.elementInitializer()");

        var setTypedFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getFeatureTyping_TypedFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setType = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getFeatureTyping_Type().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var setSpecific = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_Specific().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setGeneral = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_General().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(callElementInitializerService.build(), setTypedFeature.build(), setType.build(), setSpecific.build(), setGeneral.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getFeatureTyping()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE)
                .children(createInstance.build());

        return builder
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getFeatureTyping()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getFeatureTyping().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createAddAsNestedPartEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE + ".addAsNestedPart(" + EdgeDescription.SEMANTIC_EDGE_TARGET + ")");

        return builder
                .name(this.nameGenerator.getCreationToolName("Add Part as nested ", SysmlPackage.eINSTANCE.getPartUsage()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getMembership().getName() + SVG)
                .body(callService.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createBecomeNestedElementEdgeTool(EClass eClass, List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE + ".becomeNestedUsage(" + EdgeDescription.SEMANTIC_EDGE_TARGET + ")");

        return builder
                .name(this.nameGenerator.getCreationToolName("Become nested ", eClass))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getMembership().getName() + SVG)
                .body(callService.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createBecomeObjectiveRequirementEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE + ".becomeObjectiveRequirement(" + EdgeDescription.SEMANTIC_EDGE_TARGET + ")");

        return builder
                .name(this.nameGenerator.getCreationToolName("Become objective", SysmlPackage.eINSTANCE.getRequirementUsage()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + "Objective.svg")
                .body(callService.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createAllocateEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();
        var onlyUsages = targetElementDescriptions.stream()
                .filter(nd -> nd.getName().endsWith("Usage"))
                .toArray(NodeDescription[]::new);

        var params = List.of(EdgeDescription.SEMANTIC_EDGE_TARGET, EdgeDescription.EDGE_SOURCE, IEditingContext.EDITING_CONTEXT, IDiagramService.DIAGRAM_SERVICES);
        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE + ".createAllocateEdge(" +  String.join(",", params) + ")");

        return builder.name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getAllocationUsage()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getDependency().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(onlyUsages)
                .build();
    }
}
