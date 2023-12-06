/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.diagrams.description.EdgeDescription;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Common pieces of node descriptions shared by {@link INodeDescriptionProvider} in General View.
 *
 * @author arichard
 */
public abstract class AbstractNodeDescriptionProvider implements INodeDescriptionProvider {

    protected final ViewBuilders viewBuilderHelper = new ViewBuilders();

    protected final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    protected final IColorProvider colorProvider;

    public AbstractNodeDescriptionProvider(IColorProvider colorProvider) {
        this.colorProvider = Objects.requireNonNull(colorProvider);
    }

    protected NodeDescription createDefinitionAttributesCompartment(String name) {
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenDescriptions(this.createDefinitionAttributesCompartmentItem(name))
                .childrenLayoutStrategy(this.diagramBuilderHelper.newListLayoutStrategyDescription().build())
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement()))
                .labelExpression("attributes")
                .name(name + " AttributesCompartment")
                .semanticCandidatesExpression(AQLConstants.AQL_SELF)
                .style(this.createDefinitionCompartmentNodeStyle())
                .userResizable(false)
                .palette(this.createAttributesCompartmentPalette())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    protected NodeDescription createDefinitionAttributesCompartmentItem(String name) {
        return this.diagramBuilderHelper.newNodeDescription()
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_ITEM_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getAttributeUsage()))
                .labelExpression("aql:self.getUsageLabel()")
                .name(name + " AttributesCompartmentItem")
                .semanticCandidatesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute().getName())
                .style(this.createDefinitionCompartmentItemNodeStyle())
                .userResizable(false)
                .palette(this.createCompartmentItemNodePalette())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    protected NodeDescription createDefinitionPortsCompartment(String name) {
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenDescriptions(this.createDefinitionPortsCompartmentItem(name))
                .childrenLayoutStrategy(this.diagramBuilderHelper.newListLayoutStrategyDescription().build())
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement()))
                .labelExpression("ports")
                .name(name + " PortsCompartment")
                .semanticCandidatesExpression(AQLConstants.AQL_SELF)
                .style(this.createDefinitionCompartmentNodeStyle())
                .userResizable(false)
                .palette(this.createPortsCompartmentPalette())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    protected NodeDescription createDefinitionPortsCompartmentItem(String name) {
        return this.diagramBuilderHelper.newNodeDescription()
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_ITEM_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPortUsage()))
                .labelExpression(AQLConstants.DEFAULT_LABEL_EXPRESSION)
                .name(name + " PortsCompartmentItem")
                .semanticCandidatesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getDefinition_OwnedPort().getName())
                .style(this.createDefinitionCompartmentItemNodeStyle())
                .userResizable(false)
                .palette(this.createCompartmentItemNodePalette())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    protected NodeDescription createDefinitionItemsCompartment(String name) {
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenDescriptions(this.createDefinitionItemsCompartmentItem(name))
                .childrenLayoutStrategy(this.diagramBuilderHelper.newListLayoutStrategyDescription().build())
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement()))
                .labelExpression("items")
                .name(name + " ItemsCompartment")
                .semanticCandidatesExpression(AQLConstants.AQL_SELF)
                .style(this.createDefinitionCompartmentNodeStyle())
                .userResizable(false)
                .palette(this.createItemsCompartmentPalette())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    protected NodeDescription createDefinitionItemsCompartmentItem(String name) {
        return this.diagramBuilderHelper.newNodeDescription()
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_ITEM_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getItemUsage()))
                .labelExpression(AQLConstants.DEFAULT_LABEL_EXPRESSION)
                .name(name + " ItemsCompartmentItem")
                .semanticCandidatesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getDefinition_OwnedItem().getName())
                .style(this.createDefinitionCompartmentItemNodeStyle())
                .userResizable(false)
                .palette(this.createCompartmentItemNodePalette())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    protected NodeStyleDescription createDefinitionNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .displayHeaderSeparator(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(true)
                .withHeader(true)
                .build();
    }

    protected NodeStyleDescription createDefinitionCompartmentNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_COMPARTMENT_BACKGROUND_COLOR))
                .displayHeaderSeparator(false)
                .fontSize(12)
                .italic(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(false)
                .withHeader(true)
                .build();
    }

    protected NodeStyleDescription createDefinitionCompartmentItemNodeStyle() {
        return this.diagramBuilderHelper.newIconLabelNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(true)
                .build();
    }

    protected NodeDescription createUsageAttributesCompartment(String name) {
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenDescriptions(this.createUsageAttributesCompartmentItem(name))
                .childrenLayoutStrategy(this.diagramBuilderHelper.newListLayoutStrategyDescription().build())
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement()))
                .labelExpression("attributes")
                .name(name + " AttributesCompartment")
                .semanticCandidatesExpression(AQLConstants.AQL_SELF)
                .style(this.createUsageCompartmentNodeStyle())
                .userResizable(false)
                .palette(this.createAttributesCompartmentPalette())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    protected NodeDescription createUsageAttributesCompartmentItem(String name) {
        return this.diagramBuilderHelper.newNodeDescription()
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_ITEM_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getAttributeUsage()))
                .labelExpression(AQLConstants.DEFAULT_LABEL_EXPRESSION)
                .name(name + " AttributesCompartmentItem")
                .semanticCandidatesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getUsage_NestedAttribute().getName())
                .style(this.createUsageCompartmentItemNodeStyle())
                .userResizable(false)
                .palette(this.createCompartmentItemNodePalette())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    protected NodeDescription createUsagePortsCompartment(String name) {
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenDescriptions(this.createUsagePortsCompartmentItem(name))
                .childrenLayoutStrategy(this.diagramBuilderHelper.newListLayoutStrategyDescription().build())
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement()))
                .labelExpression("ports")
                .name(name + " PortsCompartment")
                .semanticCandidatesExpression(AQLConstants.AQL_SELF)
                .style(this.createUsageCompartmentNodeStyle())
                .userResizable(false)
                .palette(this.createPortsCompartmentPalette())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    protected NodeDescription createUsagePortsCompartmentItem(String name) {
        return this.diagramBuilderHelper.newNodeDescription()
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_ITEM_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPortUsage()))
                .labelExpression(AQLConstants.DEFAULT_LABEL_EXPRESSION)
                .name(name + " PortsCompartmentItem")
                .semanticCandidatesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getUsage_NestedPort().getName())
                .style(this.createUsageCompartmentItemNodeStyle())
                .userResizable(false)
                .palette(this.createCompartmentItemNodePalette())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    protected NodeStyleDescription createUsageNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(10)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .displayHeaderSeparator(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(true)
                .withHeader(true)
                .build();
    }

    protected NodeStyleDescription createUsageCompartmentNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_COMPARTMENT_BACKGROUND_COLOR))
                .displayHeaderSeparator(false)
                .fontSize(12)
                .italic(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(false)
                .withHeader(true)
                .build();
    }

    protected NodeStyleDescription createUsageCompartmentItemNodeStyle() {
        return this.diagramBuilderHelper.newIconLabelNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(true)
                .build();
    }

    protected NodePalette createCompartmentItemNodePalette() {
        var callDeleteService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF + ".deleteFromModel()");

        var deleteTool = this.diagramBuilderHelper.newDeleteTool()
                .name("Delete from Model")
                .body(callDeleteService.build());

        var callEditService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF + ".directEdit(newLabel)");

        var editTool = this.diagramBuilderHelper.newLabelEditTool()
                .name("Edit")
                .initialDirectEditLabelExpression(AQLConstants.AQL_SELF + ".getUsageInitialDirectEditLabel()")
                .body(callEditService.build());

        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .labelEditTool(editTool.build())
                .build();
    }

    protected NodePalette createAttributesCompartmentPalette() {
        return this.diagramBuilderHelper.newNodePalette()
                .nodeTools(this.createCompartmentNodeTool(SysmlPackage.eINSTANCE.getAttributeUsage(), "attribute"))
                .build();
    }

    protected NodePalette createPortsCompartmentPalette() {
        return this.diagramBuilderHelper.newNodePalette()
                .nodeTools(this.createCompartmentNodeTool(SysmlPackage.eINSTANCE.getPortUsage(), "port"))
                .build();
    }

    protected NodePalette createItemsCompartmentPalette() {
        return this.diagramBuilderHelper.newNodePalette()
                .nodeTools(this.createCompartmentNodeTool(SysmlPackage.eINSTANCE.getItemUsage(), "item"))
                .build();
    }

    protected NodeTool createCompartmentNodeTool(EClass eClass, String defaultLabel) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var setValue = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getElement_DeclaredName().getName())
                .valueExpression(defaultLabel);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(setValue.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var changeContextMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newFeatureMembership")
                .children(createInstance.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getFeatureMembership()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newFeatureMembership")
                .children(changeContextMembership.build());

        return builder
                .name(eClass.getName())
                .body(createMembership.build())
                .build();
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
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(new NodeDescription[targetElementDescriptions.size()]))
                .build();
    }

    protected EdgeTool createSubclassificationEdgeTool(List<NodeDescription> targetElementDescriptions) {
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
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(new NodeDescription[targetElementDescriptions.size()]))
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
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(new NodeDescription[targetElementDescriptions.size()]))
                .build();
    }
}
