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
import java.util.Optional;

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
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.SysmlEClassSwitch;

/**
 * Switch retrieving the list of NodeToolSections for each SysMLv2 concept represented in the General View diagram.

 * @author arichard
 */
public class GeneralViewNodeToolSectionSwitch extends SysmlEClassSwitch<Void> {

    private final ViewBuilders viewBuilderHelper;

    private final DiagramBuilders diagramBuilderHelper;

    private final List<NodeToolSection> nodeToolSections;

    private final NodeDescription nodeDescription;

    private final List<NodeDescription> allNodeDescriptions;

    private final GVDescriptionNameGenerator nameGenerator = new GVDescriptionNameGenerator();

    public GeneralViewNodeToolSectionSwitch(NodeDescription nodeDescription, List<NodeDescription> allNodeDescriptions) {
        this.viewBuilderHelper = new ViewBuilders();
        this.diagramBuilderHelper = new DiagramBuilders();
        this.nodeToolSections = new ArrayList<>();
        this.nodeDescription = Objects.requireNonNull(nodeDescription);
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
    }

    public List<NodeToolSection> getNodeToolSections() {
        return this.nodeToolSections;
    }

    @Override
    public Void caseDefinition(Definition object) {
        this.createToolsForCompartmentItems(object);
        return super.caseDefinition(object);
    }

    @Override
    public Void casePartDefinition(PartDefinition object) {
        this.nodeToolSections.add(this.createPartDefinitionElementsToolSection(this.allNodeDescriptions.stream().filter(nodeDesc -> this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()).equals(nodeDesc.getName())).findFirst().get(),
                this.allNodeDescriptions.stream().filter(nodeDesc -> this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getItemUsage()).equals(nodeDesc.getName())).findFirst().get()));
        this.nodeToolSections.add(this.addElementsToolSection());
        return super.casePartDefinition(object);
    }

    @Override
    public Void casePartUsage(PartUsage object) {
        this.nodeToolSections.add(this.createPartUsageElementsToolSection(this.nodeDescription));
        this.nodeToolSections.add(this.addElementsToolSection());
        return super.casePartUsage(object);
    }

    @Override
    public Void caseUsage(Usage object) {
        this.createToolsForCompartmentItems(object);
        return super.caseUsage(object);
    }

    @Override
    public Void caseRequirementUsage(RequirementUsage object) {
        this.nodeToolSections.add(this.createPartUsageAsRequirementSubject(object));
        return super.caseRequirementUsage(object);
    }

    private NodeToolSection createPartUsageAsRequirementSubject(RequirementUsage requirementUsage) {
        var serviceCall = this.viewBuilderHelper.newChangeContext().expression("aql:self.createRequirementUsageSubject(self.eContainer().eContainer())");
        var createSubjectTool = this.diagramBuilderHelper.newNodeTool()
            .name("New Subject")
            .iconURLsExpression("/icons/full/obj16/Subject.svg")
            .preconditionExpression("aql:self.isEmptySubjectCompartment()")
            .body(serviceCall.build()).build();
        return this.diagramBuilderHelper.newNodeToolSection()
            .name("Create")
            .nodeTools(createSubjectTool)
            .build();
    }

    private NodeToolSection createPartDefinitionElementsToolSection(NodeDescription partUsageNodeDescription, NodeDescription itemUsageNodeDescription) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Create")
                .nodeTools(this.createNestedUsageNodeTool(partUsageNodeDescription, SysmlPackage.eINSTANCE.getPartUsage()),
                        this.createNestedUsageNodeTool(itemUsageNodeDescription, SysmlPackage.eINSTANCE.getItemUsage()))
                .build();
    }

    private NodeToolSection createPartUsageElementsToolSection(NodeDescription nodeDesc) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Create")
                .nodeTools(this.createNestedPartNodeTool(nodeDesc))
                .build();
    }

    private NodeTool createNestedUsageNodeTool(NodeDescription nodeDesc, EClass eClass) {
        var setValue = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getElement_DeclaredName().getName())
                .valueExpression(eClass.getName());

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(setValue.build());

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

    private NodeTool createNestedPartNodeTool(NodeDescription nodeDesc) {
        var setValue = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getElement_DeclaredName().getName())
                .valueExpression(SysmlPackage.eINSTANCE.getPartUsage().getName());

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(setValue.build());

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPartUsage()))
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
                .name(this.nameGenerator.getCreationToolName("New ", SysmlPackage.eINSTANCE.getPartUsage()))
                .iconURLsExpression("/icons/full/obj16/" + SysmlPackage.eINSTANCE.getPartUsage().getName() + ".svg")
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

    private void createToolsForCompartmentItems(Element object) {
        List<NodeTool> compartmentNodeTools = new ArrayList<>();
        GeneralViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((compartmentEClass, listItems) -> {
            if (compartmentEClass.equals(object.eClass())) {
                listItems.forEach(eReference -> {
                    CompartmentNodeToolProvider provider = new CompartmentNodeToolProvider(eReference, this.nameGenerator);
                    compartmentNodeTools.add(provider.create(null));
                });
            }
        });
        Optional<NodeToolSection> createToolSection = this.nodeToolSections.stream()
                .filter(toolSection -> toolSection.getName().equals("Create"))
                .findFirst();
        if (createToolSection.isPresent()) {
            createToolSection.get().getNodeTools().addAll(compartmentNodeTools);
        } else {
            NodeToolSection toolSection = this.diagramBuilderHelper.newNodeToolSection()
                    .name("Create")
                    .nodeTools(compartmentNodeTools.toArray(NodeTool[]::new))
                    .build();
            this.nodeToolSections.add(toolSection);
        }
    }
}
