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
package org.eclipse.syson.application.expressions.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IReadOnlyObjectPredicate;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.palette.dto.ToolSection;
import org.eclipse.sirius.components.view.diagram.DiagramFactory;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewDiagramDescriptionSearchService;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the expression tools contributed to diagram palettes.
 *
 * @author arichard
 */
class ExpressionsPaletteToolsProviderTest {

    private static final String ELEMENT_ID = "attribute-id";

    private IEMFEditingContext editingContext;

    private IObjectSearchService objectSearchService;

    private Node node;

    private IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService;

    private ExpressionsPaletteToolsProvider provider;

    /**
     * Initializes the provider with an expression-capable attribute target.
     */
    @BeforeEach
    void setUp() {
        this.editingContext = mock(IEMFEditingContext.class);
        this.objectSearchService = mock(IObjectSearchService.class);
        IReadOnlyObjectPredicate readOnlyObjectPredicate = mock(IReadOnlyObjectPredicate.class);
        this.viewDiagramDescriptionSearchService = mock(IViewDiagramDescriptionSearchService.class);
        this.node = mock(Node.class);

        AttributeUsage attributeUsage = SysmlFactory.eINSTANCE.createAttributeUsage();
        when(this.node.getTargetObjectId()).thenReturn(ELEMENT_ID);
        when(this.objectSearchService.getObject(this.editingContext, ELEMENT_ID)).thenReturn(Optional.of(attributeUsage));
        when(readOnlyObjectPredicate.test(attributeUsage)).thenReturn(false);

        this.provider = new ExpressionsPaletteToolsProvider(this.objectSearchService, readOnlyObjectPredicate, this.viewDiagramDescriptionSearchService);
    }

    /**
     * Verifies that expression tools remain available on an owned diagram node.
     */
    @Test
    void expressionToolsAreAvailableOnOwnedNode() {
        NodeDescription nodeDescription = this.nodeDescription("description-id");

        List<ToolSection> toolSections = this.provider.createExtraToolSections(this.editingContext, mock(DiagramContext.class), nodeDescription, this.node);

        assertThat(toolSections).singleElement().extracting(ToolSection::tools).asInstanceOf(InstanceOfAssertFactories.LIST)
                .extracting("id")
                .containsExactly("tool_new_expression");
    }

    /**
     * Verifies that inherited border nodes do not expose expression mutation tools.
     */
    @Test
    void expressionToolsAreUnavailableOnInheritedBorderNode() {
        NodeDescription nodeDescription = this.nodeDescription("inherited-border-node-description-id");
        this.mockViewNodeDescription(nodeDescription, "GV InheritedBorderNode AttributeUsage");

        List<ToolSection> toolSections = this.provider.createExtraToolSections(this.editingContext, mock(DiagramContext.class), nodeDescription, this.node);

        assertThat(toolSections).isEmpty();
    }

    /**
     * Verifies that inherited compartment items do not expose expression mutation tools.
     */
    @Test
    void expressionToolsAreUnavailableOnInheritedCompartmentItem() {
        NodeDescription nodeDescription = this.nodeDescription("inherited-compartment-item-description-id");
        this.mockViewNodeDescription(nodeDescription, "GV InheritedCompartmentItem AttributeUsage");

        List<ToolSection> toolSections = this.provider.createExtraToolSections(this.editingContext, mock(DiagramContext.class), nodeDescription, this.node);

        assertThat(toolSections).isEmpty();
    }

    /**
     * Creates a diagram node description with the given name.
     *
     * @param name
     *            the description name
     * @return the mocked node description
     */
    private NodeDescription nodeDescription(String id) {
        NodeDescription nodeDescription = mock(NodeDescription.class);
        when(nodeDescription.getId()).thenReturn(id);
        return nodeDescription;
    }

    /**
     * Associates a runtime node description with its View description.
     *
     * @param nodeDescription
     *            the runtime node description
     * @param name
     *            the View node description name
     */
    private void mockViewNodeDescription(NodeDescription nodeDescription, String name) {
        org.eclipse.sirius.components.view.diagram.NodeDescription viewNodeDescription = DiagramFactory.eINSTANCE.createNodeDescription();
        viewNodeDescription.setName(name);
        when(this.viewDiagramDescriptionSearchService.findViewNodeDescriptionById(this.editingContext, nodeDescription.getId()))
                .thenReturn(Optional.of(viewNodeDescription));
    }
}
