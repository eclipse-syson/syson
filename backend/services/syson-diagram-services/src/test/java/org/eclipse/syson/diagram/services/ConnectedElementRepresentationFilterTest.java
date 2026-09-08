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
package org.eclipse.syson.diagram.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests eligibility independently of graphical visibility.
 *
 * @author cbrun
 */
public class ConnectedElementRepresentationFilterTest {

    /**
     * Hidden containers, reused descriptions and border nodes must retain semantic and contextual filtering.
     */
    @Test
    void filtersOnlyRepresentableContent() {
        var owner = SysmlFactory.eINSTANCE.createPartUsage();
        var attribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        var externalAttribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        var port = SysmlFactory.eINSTANCE.createPortUsage();
        var view = SysmlFactory.eINSTANCE.createViewUsage();
        var editingContext = mock(IEditingContext.class);
        var search = mock(IObjectSearchService.class);
        when(search.getObject(editingContext, "owner")).thenReturn(Optional.of(owner));
        when(search.getObject(editingContext, "view")).thenReturn(Optional.of(view));
        var node = mock(Node.class);
        when(node.getId()).thenReturn("node");
        when(node.getTargetObjectId()).thenReturn("owner");
        when(node.getDescriptionId()).thenReturn("part");
        var diagram = mock(Diagram.class);
        when(diagram.getNodes()).thenReturn(List.of(node));
        when(diagram.getTargetObjectId()).thenReturn("view");
        var context = mock(DiagramContext.class);
        when(context.diagram()).thenReturn(diagram);
        var partDescription = mock(NodeDescription.class);
        when(partDescription.getId()).thenReturn("part");
        var compartment = mock(NodeDescription.class);
        when(compartment.getId()).thenReturn("compartment");
        when(compartment.getSemanticElementsProvider()).thenReturn(variables -> List.of(owner));
        when(compartment.getShouldRenderPredicate()).thenReturn(variables -> true);
        var item = mock(NodeDescription.class);
        when(item.getId()).thenReturn("item");
        when(item.getSemanticElementsProvider()).thenReturn(variables -> List.of(attribute));
        when(item.getShouldRenderPredicate()).thenReturn(variables ->
                List.of(owner, owner, view).equals(variables.getVariables().get(NodeDescription.ANCESTORS)));
        var border = mock(NodeDescription.class);
        when(border.getId()).thenReturn("border");
        when(border.getSemanticElementsProvider()).thenReturn(variables -> List.of(port));
        when(border.getShouldRenderPredicate()).thenReturn(variables -> true);
        when(partDescription.getChildNodeDescriptions()).thenReturn(List.of(compartment));
        when(partDescription.getBorderNodeDescriptions()).thenReturn(List.of(border));
        when(compartment.getReusedChildNodeDescriptionIds()).thenReturn(List.of("item", "compartment"));
        Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> descriptions = Map.of(
                org.eclipse.sirius.components.view.diagram.DiagramFactory.eINSTANCE.createNodeDescription(), partDescription,
                org.eclipse.sirius.components.view.diagram.DiagramFactory.eINSTANCE.createNodeDescription(), item,
                org.eclipse.sirius.components.view.diagram.DiagramFactory.eINSTANCE.createNodeDescription(), compartment);
        var filter = new ConnectedElementRepresentationFilter(search, editingContext, context, descriptions);

        assertThat(filter.canRenderInside(attribute, node)).isTrue();
        assertThat(filter.canRenderInside(port, node)).isTrue();
        assertThat(filter.canRenderInside(externalAttribute, node)).isFalse();
        when(compartment.getShouldRenderPredicate()).thenReturn(variables -> false);
        assertThat(filter.canRenderInside(attribute, node)).isFalse();
        when(compartment.getShouldRenderPredicate()).thenReturn(variables -> true);

        var compartmentNode = mock(Node.class);
        when(compartmentNode.getId()).thenReturn("compartmentNode");
        when(compartmentNode.getTargetObjectId()).thenReturn("owner");
        when(node.getChildNodes()).thenReturn(List.of(compartmentNode));
        assertThat(filter.getReferenceNode(compartmentNode)).isSameAs(node);
        assertThat(filter.canRenderInside(attribute, filter.getReferenceNode(compartmentNode))).isTrue();
    }
}
