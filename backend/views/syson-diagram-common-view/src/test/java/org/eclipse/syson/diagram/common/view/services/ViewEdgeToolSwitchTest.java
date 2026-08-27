/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.DescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ViewEdgeToolSwitch}.
 *
 * @author arichard
 */
public class ViewEdgeToolSwitchTest {

    private final DescriptionNameGenerator descriptionNameGenerator = new DescriptionNameGenerator("test");

    @Test
    @DisplayName("GIVEN an EnumerationDefinition, WHEN its edge tools are created, THEN the New Subclassification tool is not available")
    void enumerationDefinitionDoesNotProvideSubclassificationEdgeTool() {
        var nodeDescription = this.createNodeDescription(SysmlPackage.eINSTANCE.getEnumerationDefinition());

        var edgeTools = this.createEdgeToolSwitch(nodeDescription).doSwitch(SysmlPackage.eINSTANCE.getEnumerationDefinition());

        assertThat(edgeTools)
                .extracting(EdgeTool::getName)
                .doesNotContain(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getSubclassification()));
    }

    @Test
    @DisplayName("GIVEN a PartDefinition, WHEN its edge tools are created, THEN the New Subclassification tool is available")
    void partDefinitionProvidesSubclassificationEdgeTool() {
        var nodeDescription = this.createNodeDescription(SysmlPackage.eINSTANCE.getPartDefinition());

        var edgeTools = this.createEdgeToolSwitch(nodeDescription).doSwitch(SysmlPackage.eINSTANCE.getPartDefinition());

        assertThat(edgeTools)
                .extracting(EdgeTool::getName)
                .contains(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getSubclassification()));
    }

    @Test
    @DisplayName("GIVEN Usage node descriptions, WHEN nested usage tools are created, THEN each Add tool is specific to its target type and Become names the source type")
    void usageProvidesSymmetricNestedUsageTools() {
        var partUsageNode = this.createNodeDescription(SysmlPackage.eINSTANCE.getPartUsage());
        var caseUsageNode = this.createNodeDescription(SysmlPackage.eINSTANCE.getCaseUsage());
        var useCaseUsageNode = this.createNodeDescription(SysmlPackage.eINSTANCE.getUseCaseUsage());
        var attributeUsageNode = this.createNodeDescription(SysmlPackage.eINSTANCE.getAttributeUsage());
        var actionUsageNode = this.createNodeDescription("ActionUsage", SysmlPackage.eINSTANCE.getActionUsage());
        var startActionNode = this.createNodeDescription("StartAction", SysmlPackage.eINSTANCE.getActionUsage());
        var doneActionNode = this.createNodeDescription("DoneAction", SysmlPackage.eINSTANCE.getActionUsage());
        var partDefinitionNode = this.createNodeDescription(SysmlPackage.eINSTANCE.getPartDefinition());
        var allUsageNodes = List.of(partUsageNode, caseUsageNode, useCaseUsageNode, attributeUsageNode, actionUsageNode, startActionNode, doneActionNode, partDefinitionNode);
        var partUsageEdgeToolSwitch = new ViewEdgeToolSwitch(partUsageNode, allUsageNodes, this.descriptionNameGenerator);
        var caseUsageEdgeToolSwitch = new ViewEdgeToolSwitch(caseUsageNode, allUsageNodes, this.descriptionNameGenerator);

        var partUsageEdgeTools = partUsageEdgeToolSwitch.doSwitch(SysmlFactory.eINSTANCE.createPartUsage());
        assertThat(partUsageEdgeTools)
                .filteredOn(edgeTool -> edgeTool.getName().startsWith("Add target as nested "))
                .extracting(EdgeTool::getName)
                .containsExactlyInAnyOrder("Add target as nested Part", "Add target as nested Case", "Add target as nested Use Case", "Add target as nested Attribute", "Add target as nested Action");
        assertThat(partUsageEdgeTools)
                .filteredOn(edgeTool -> "Add target as nested Part".equals(edgeTool.getName()))
                .allSatisfy(edgeTool -> {
                    assertThat(edgeTool.getTargetElementDescriptions()).containsExactly(partUsageNode);
                    assertThat(edgeTool.getPreconditionExpression()).contains("isTargetNodeOfType", "PartUsage");
                });
        assertThat(partUsageEdgeTools)
                .filteredOn(edgeTool -> "Add target as nested Use Case".equals(edgeTool.getName()))
                .allSatisfy(edgeTool -> assertThat(edgeTool.getTargetElementDescriptions()).containsExactly(useCaseUsageNode));
        assertThat(partUsageEdgeTools)
                .filteredOn(edgeTool -> "Add target as nested Action".equals(edgeTool.getName()))
                .allSatisfy(edgeTool -> assertThat(edgeTool.getTargetElementDescriptions()).containsExactly(actionUsageNode, startActionNode, doneActionNode));

        var caseUsageEdgeTools = caseUsageEdgeToolSwitch.doSwitch(SysmlFactory.eINSTANCE.createCaseUsage());
        assertThat(caseUsageEdgeTools)
                .filteredOn(edgeTool -> edgeTool.getName().startsWith("Add target as nested "))
                .extracting(EdgeTool::getName)
                .containsExactlyInAnyOrder("Add target as nested Part", "Add target as nested Case", "Add target as nested Use Case", "Add target as nested Attribute", "Add target as nested Action");

        var becomeNestedCase = caseUsageEdgeTools.stream()
                .filter(edgeTool -> edgeTool.getName().equals("Become nested Case"))
                .findFirst();
        assertThat(becomeNestedCase).isPresent();
        assertThat(becomeNestedCase.get().getTargetElementDescriptions())
                .containsExactlyInAnyOrder(partUsageNode, caseUsageNode, useCaseUsageNode, attributeUsageNode, actionUsageNode, startActionNode, doneActionNode);
    }

    @Test
    @DisplayName("GIVEN a Definition node and Usage node descriptions, WHEN its edge tools are created, THEN each owned Usage tool is specific to its target type")
    void definitionProvidesTargetSpecificOwnedUsageTools() {
        var allocationDefinitionNode = this.createNodeDescription(SysmlPackage.eINSTANCE.getAllocationDefinition());
        var allocationUsageNode = this.createNodeDescription(SysmlPackage.eINSTANCE.getAllocationUsage());
        var assignmentActionUsageNode = this.createNodeDescription(SysmlPackage.eINSTANCE.getAssignmentActionUsage());
        var caseUsageNode = this.createNodeDescription(SysmlPackage.eINSTANCE.getCaseUsage());
        var useCaseUsageNode = this.createNodeDescription(SysmlPackage.eINSTANCE.getUseCaseUsage());
        var viewUsageNode = this.createNodeDescription(SysmlPackage.eINSTANCE.getViewUsage());
        var edgeToolSwitch = new ViewEdgeToolSwitch(allocationDefinitionNode,
                List.of(allocationDefinitionNode, allocationUsageNode, assignmentActionUsageNode, caseUsageNode, useCaseUsageNode, viewUsageNode), this.descriptionNameGenerator);

        var edgeTools = edgeToolSwitch.doSwitch(SysmlFactory.eINSTANCE.createAllocationDefinition());

        assertThat(edgeTools)
                .filteredOn(edgeTool -> edgeTool.getName().startsWith("Add target as owned "))
                .extracting(EdgeTool::getName)
                .containsExactlyInAnyOrder("Add target as owned Allocation", "Add target as owned Assignment Action", "Add target as owned Case", "Add target as owned Use Case",
                        "Add target as owned View");
        assertThat(edgeTools)
                .filteredOn(edgeTool -> "Add target as owned Allocation".equals(edgeTool.getName()))
                .allSatisfy(edgeTool -> {
                    assertThat(edgeTool.getTargetElementDescriptions()).containsExactly(allocationUsageNode);
                    assertThat(edgeTool.getPreconditionExpression()).contains("isTargetNodeOfType", "AllocationUsage");
                });
    }

    /**
     * Creates the edge tool switch to test with the given node description.
     *
     * @param nodeDescription
     *            the node description represented by the switch
     * @return the configured edge tool switch
     */
    private ViewEdgeToolSwitch createEdgeToolSwitch(NodeDescription nodeDescription) {
        return new ViewEdgeToolSwitch(nodeDescription, List.of(nodeDescription), this.descriptionNameGenerator);
    }

    /**
     * Creates a minimal node description for an EClass.
     *
     * @param eClass
     *            the EClass represented by the node description
     * @return the node description for the given EClass
     */
    private NodeDescription createNodeDescription(EClass eClass) {
        return this.createNodeDescription(eClass.getName(), eClass);
    }

    /**
     * Creates a minimal node description for an EClass using the given description name.
     *
     * @param name
     *            the description name
     * @param eClass
     *            the EClass represented by the node description
     * @return the node description for the given EClass
     */
    private NodeDescription createNodeDescription(String name, EClass eClass) {
        return new DiagramBuilders().newNodeDescription()
                .name(name)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .build();
    }
}
