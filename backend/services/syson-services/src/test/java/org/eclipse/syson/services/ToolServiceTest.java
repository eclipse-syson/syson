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
package org.eclipse.syson.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.INodeStyle;
import org.eclipse.sirius.components.diagrams.ListLayoutStrategy;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.syson.services.api.ISysMLMoveElementService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the diagram-tree traversal helpers provided by {@link ToolService}.
 * <p>
 * The tests construct small mocked diagram hierarchies to verify parent and source-node lookup, child-node
 * collection, and semantic-node identity matching. They provide fast regression coverage for the structural logic
 * used by diagram tools, including nested and border nodes, without depending on a persisted representation.
 * </p>
 * <p>
 * They do not validate diagram creation, event handling, or a complete Sirius Web tool execution. Those behaviours
 * require the surrounding application services and are intentionally outside this unit-test suite.
 * </p>
 *
 * @author arichard
 */
class ToolServiceTest {

    @DisplayName("GIVEN a diagram hierarchy, WHEN finding a parent, THEN direct and nested diagram elements resolve to their container")
    @Test
    void testGetParentNode() {
        ToolService service = this.createService(mock(IIdentityService.class));
        Diagram diagram = mock(Diagram.class);
        DiagramContext diagramContext = mock(DiagramContext.class);
        when(diagramContext.diagram()).thenReturn(diagram);
        Node topLevelNode = this.node("top-level");
        Node containerNode = this.node("container");
        Node nestedNode = this.node("nested");
        Edge edge = mock(Edge.class);
        when(containerNode.getChildNodes()).thenReturn(new ArrayList<>(List.of(nestedNode)));
        when(diagram.getNodes()).thenReturn(new ArrayList<>(List.of(topLevelNode, containerNode)));
        when(diagram.getEdges()).thenReturn(new ArrayList<>(List.of(edge)));
        Element element = SysmlFactory.eINSTANCE.createPartUsage();

        assertThat(service.getParentNode(element, topLevelNode, diagramContext)).isSameAs(diagram);
        assertThat(service.getParentNode(element, edge, diagramContext)).isSameAs(diagram);
        assertThat(service.getParentNode(element, nestedNode, diagramContext)).isSameAs(containerNode);
        assertThat(service.getParentNode(element, mock(Node.class), diagramContext)).isNull();
    }

    @DisplayName("GIVEN nodes and borders, WHEN looking up an edge source, THEN nested and border nodes are found")
    @Test
    void testGetSourceNode() {
        ToolService service = this.createService(mock(IIdentityService.class));
        Diagram diagram = mock(Diagram.class);
        Node root = this.node("root");
        Node child = this.node("child");
        Node border = this.node("border");
        when(root.getChildNodes()).thenReturn(new ArrayList<>(List.of(child)));
        when(root.getBorderNodes()).thenReturn(new ArrayList<>(List.of(border)));
        when(diagram.getNodes()).thenReturn(new ArrayList<>(List.of(root)));
        Edge edge = mock(Edge.class);

        when(edge.getSourceId()).thenReturn("root");
        assertThat(service.getSourceNode(edge, diagram)).isSameAs(root);
        when(edge.getSourceId()).thenReturn("child");
        assertThat(service.getSourceNode(edge, diagram)).isSameAs(child);
        when(edge.getSourceId()).thenReturn("border");
        assertThat(service.getSourceNode(edge, diagram)).isSameAs(border);
        when(edge.getSourceId()).thenReturn("missing");
        assertThat(service.getSourceNode(edge, diagram)).isNull();
    }

    @DisplayName("GIVEN a selected node or a diagram, WHEN collecting children, THEN list compartments and top-level nodes are included")
    @Test
    void testGetChildNodesAndIsPresent() {
        IIdentityService identityService = mock(IIdentityService.class);
        ToolService service = this.createService(identityService);
        Diagram diagram = mock(Diagram.class);
        DiagramContext diagramContext = mock(DiagramContext.class);
        when(diagramContext.diagram()).thenReturn(diagram);
        Node listNode = this.node("list");
        Node compartment = this.node("compartment");
        Node item = this.node("item");
        INodeStyle style = mock(INodeStyle.class);
        when(style.getChildrenLayoutStrategy()).thenReturn(ListLayoutStrategy.newListLayoutStrategy().build());
        when(listNode.getStyle()).thenReturn(style);
        when(listNode.getChildNodes()).thenReturn(new ArrayList<>(List.of(compartment)));
        when(compartment.getChildNodes()).thenReturn(new ArrayList<>(List.of(item)));
        Node topLevelNode = this.node("top-level");
        when(diagram.getNodes()).thenReturn(new ArrayList<>(List.of(topLevelNode)));

        assertThat(service.getChildNodes(diagramContext, listNode)).containsExactly(compartment, item);
        assertThat(service.getChildNodes(diagramContext, null)).containsExactly(topLevelNode);

        Element element = SysmlFactory.eINSTANCE.createPartUsage();
        when(identityService.getId(element)).thenReturn("item");
        assertThat(service.isPresent(element, List.of(compartment, item))).isTrue();
        when(identityService.getId(element)).thenReturn("missing");
        assertThat(service.isPresent(element, List.of(compartment, item))).isFalse();
    }

    private ToolService createService(IIdentityService identityService) {
        return new ToolService(identityService, mock(IObjectSearchService.class), mock(IFeedbackMessageService.class), mock(ISysMLMoveElementService.class));
    }

    private Node node(String id) {
        Node node = mock(Node.class);
        when(node.getId()).thenReturn(id);
        when(node.getTargetObjectId()).thenReturn(id);
        when(node.getChildNodes()).thenReturn(new ArrayList<>());
        when(node.getBorderNodes()).thenReturn(new ArrayList<>());
        return node;
    }
}
