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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.SysmlEClassSwitch;

/**
 * Switch retrieving the list of NodeToolSections for each SysMLv2 concept represented in the General View diagram.

 * @author arichard
 */
public class GeneralViewNodeToolSectionSwitch extends SysmlEClassSwitch<List<NodeToolSection>> {

    private final ViewBuilders viewBuilderHelper;

    private final DiagramBuilders diagramBuilderHelper;

    private final List<NodeDescription> allNodeDescriptions;

    private final GVDescriptionNameGenerator nameGenerator = new GVDescriptionNameGenerator();

    public GeneralViewNodeToolSectionSwitch(List<NodeDescription> allNodeDescriptions) {
        this.viewBuilderHelper = new ViewBuilders();
        this.diagramBuilderHelper = new DiagramBuilders();
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
    }

    @Override
    public List<NodeToolSection> caseActionUsage(ActionUsage object) {
        var createSection = this.buildCreateSection(
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getAttributeUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseConstraintUsage(ConstraintUsage object) {
        var createSection = this.buildCreateSection(
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getAttributeUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseDefinition(Definition object) {
        var createSection = this.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection);
    }

    @Override
    public List<NodeToolSection> caseInterfaceDefinition(InterfaceDefinition object) {
        var createSection = this.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection);
    }

    @Override
    public List<NodeToolSection> caseItemDefinition(ItemDefinition object) {
        var createSection = this.buildCreateSection(this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseItemUsage(ItemUsage object) {
        var createSection = this.buildCreateSection(this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()),
                        this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                        this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseOccurrenceDefinition(OccurrenceDefinition object) {
        var createSection = this.buildCreateSection(this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> casePartDefinition(PartDefinition object) {
        var createSection = this.createPartDefinitionElementsToolSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> casePartUsage(PartUsage object) {
        var createSection = this.createPartUsageElementsToolSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> casePortUsage(PortUsage object) {
        var createSection = this.buildCreateSection(this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseRequirementUsage(RequirementUsage object) {
        var createSection = this.buildCreateSection(
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getRequirementUsage()),
                this.createPartUsageAsRequirementSubjectNodeTool());
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseRequirementDefinition(RequirementDefinition object) {
        var createSection = this.buildCreateSection(this.createPartUsageAsRequirementSubjectNodeTool());
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseUsage(Usage object) {
        var createSection = this.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    private NodeToolSection buildCreateSection(NodeTool... nodeTools) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Create")
                .nodeTools(nodeTools)
                .build();
    }

    private NodeTool createPartUsageAsRequirementSubjectNodeTool() {
        var serviceCall = this.viewBuilderHelper.newChangeContext().expression("aql:self.createRequirementSubject(self.eContainer().eContainer())");
        return this.diagramBuilderHelper.newNodeTool()
                .name("New Subject")
                .iconURLsExpression("/icons/full/obj16/Subject.svg")
                .preconditionExpression("aql:self.isEmptySubjectCompartment()")
                .body(serviceCall.build()).build();
    }

    private NodeToolSection createPartDefinitionElementsToolSection() {
        return this.buildCreateSection(
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()));
    }

    private NodeToolSection createPartUsageElementsToolSection() {
        return this.buildCreateSection(
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()));
    }

    private NodeTool createNestedUsageNodeTool(EClass eClass) {
        NodeDescription nodeDesc = this.allNodeDescriptions.stream().filter(nd -> this.nameGenerator.getNodeName(eClass).equals(nd.getName())).findFirst().get();
        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance.elementInitializer()");

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(NodeContainmentKind.CHILD_NODE)
                .elementDescription(nodeDesc)
                .parentViewExpression("aql:self.getParentNode(selectedNode, diagramContext)")
                .semanticElementExpression("aql:newInstance")
                .variableName("newInstanceView");

        var changeContexMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newFeatureMembership")
                .children(createEClassInstance.build(), createView.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getFeatureMembership()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newFeatureMembership")
                .children(changeContexMembership.build());

        return this.diagramBuilderHelper.newNodeTool()
                .name(this.nameGenerator.getCreationToolName("New ", eClass))
                .iconURLsExpression("/icons/full/obj16/" + eClass.getName() + ".svg")
                .body(createMembership.build())
                .build();
    }

    private NodeToolSection addElementsToolSection() {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Add")
                .nodeTools(this.addExistingNestedPartsTool(false), this.addExistingNestedPartsTool(true))
                .build();
    }

    private NodeTool addExistingNestedPartsTool(boolean recursive) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var addExistingelements = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.addExistingElements(editingContext, diagramContext, selectedNode, convertedNodes, " + recursive + ")");

        String title = "Add existing nested elements";
        String iconURL = "/icons/AddExistingElements.svg";
        if (recursive) {
            title += " (recursive)";
            iconURL = "/icons/AddExistingElementsRecursive.svg";
        }

        return builder
                .name(title)
                .iconURLsExpression(iconURL)
                .body(addExistingelements.build())
                .build();
    }

    private List<NodeTool> createToolsForCompartmentItems(Element object) {
        List<NodeTool> compartmentNodeTools = new ArrayList<>();
        GeneralViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((compartmentEClass, listItems) -> {
            if (compartmentEClass.equals(object.eClass())) {
                listItems.forEach(eReference -> {
                    CompartmentNodeToolProvider provider = new CompartmentNodeToolProvider(eReference, this.nameGenerator);
                    compartmentNodeTools.add(provider.create(null));
                });
            }
        });
        return compartmentNodeTools;
    }
}
