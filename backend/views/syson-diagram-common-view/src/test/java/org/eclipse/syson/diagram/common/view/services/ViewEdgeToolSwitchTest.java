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
        return new DiagramBuilders().newNodeDescription()
                .name(eClass.getName())
                .domainType(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .build();
    }
}
