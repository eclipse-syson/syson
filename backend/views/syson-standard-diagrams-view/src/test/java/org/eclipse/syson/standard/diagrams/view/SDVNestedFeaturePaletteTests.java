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
package org.eclipse.syson.standard.diagrams.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilder;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.services.description.ToolConstants;
import org.eclipse.syson.services.ColorProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.ActorNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.metamodel.helper.EMFUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests nested feature tool availability in standard and specialized node palettes.
 *
 * @author Jerome Gout
 */
public class SDVNestedFeaturePaletteTests {

    @Test
    @DisplayName("GIVEN standard and specialized nodes, WHEN building diagram palettes, THEN each Structure section offers nested feature redefinition")
    public void nestedFeatureToolIsAvailableInStandardAndSpecializedPalettes() {
        var view = new ViewBuilder().build();
        var names = new SDVDescriptionNameGenerator();
        var expectedNodeNames = List.of(
                names.getNodeName(SysmlPackage.eINSTANCE.getPartDefinition()),
                names.getNodeName(SysmlPackage.eINSTANCE.getPortUsage()),
                names.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort()),
                names.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort()),
                names.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem()),
                names.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem()),
                names.getBorderNodeName(SysmlPackage.eINSTANCE.getReferenceUsage()),
                names.getNodeName(SysmlPackage.eINSTANCE.getViewUsage()),
                ActorNodeDescriptionProvider.NAME);

        var diagram = (DiagramDescription) new SDVDiagramDescriptionProvider().create(new ColorProvider(view));
        view.getDescriptions().add(diagram);
        var descriptions = EMFUtils.allContainedObjectOfType(diagram, NodeDescription.class).toList();

        for (var nodeName : expectedNodeNames) {
            var description = descriptions.stream().filter(node -> nodeName.equals(node.getName())).findFirst().orElseThrow();
            assertThat(description.getPalette()).as("Palette of %s", nodeName).isNotNull();
            assertThat(description.getPalette().getToolSections())
                    .as("Structure section of %s", nodeName)
                    .filteredOn(section -> ToolConstants.STRUCTURE.equals(section.getName()))
                    .singleElement()
                    .satisfies(section -> assertThat(section.getNodeTools())
                            .filteredOn(tool -> "Redefine a nested feature".equals(tool.getName()))
                            .singleElement()
                            .satisfies(tool -> assertThat(tool.getDialogDescription()).isNotNull()));
        }
    }
}
