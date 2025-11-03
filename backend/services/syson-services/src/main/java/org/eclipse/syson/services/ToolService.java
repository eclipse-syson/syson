/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.IDiagramElement;
import org.eclipse.sirius.components.diagrams.ListLayoutStrategy;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.syson.services.api.ISysMLMoveElementService;
import org.eclipse.syson.sysml.Element;

/**
 * Tool-related Java services used by SysON representations.
 *
 * @author arichard
 */
public class ToolService {

    protected final IIdentityService identityService;

    protected final IObjectSearchService objectSearchService;

    protected final IFeedbackMessageService feedbackMessageService;

    protected final ISysMLMoveElementService moveService;

    protected final DeleteService deleteService;

    protected final UtilService utilService;

    public ToolService(IIdentityService identityService, IObjectSearchService objectSearchService, IFeedbackMessageService feedbackMessageService, ISysMLMoveElementService moveService) {
        this.identityService = Objects.requireNonNull(identityService);
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.moveService = Objects.requireNonNull(moveService);
        this.deleteService = new DeleteService();
        this.utilService = new UtilService();
    }

    /**
     * Get the parent node of the given {@link Element}. This could be a {@link Node} or a {@link Diagram}.
     *
     * @param element
     *            the given {@link Element}.
     * @param selectedNode
     *            the {@link Node} corresponding to the given {@link Element}, could be <code>null</code>.
     * @param selectedEdge
     *            the {@link Edge} corresponding to the given {@link Element}, could be <code>null</code>.
     * @param diagramContext
     *            the diagram context in which to find the parent node.
     * @return a {@link Node} or a {@link Diagram}.
     */
    public Object getParentNode(Element element, Node selectedNode, Edge selectedEdge, DiagramContext diagramContext) {
        Object parentNode = null;
        if (selectedEdge != null) {
            Node sourceNode = this.getSourceNode(selectedEdge, diagramContext.diagram());
            if (sourceNode != null) {
                parentNode = this.getParentNode(element, sourceNode, diagramContext);
            } else {
                parentNode = this.getParentNode(element, selectedEdge, diagramContext);
            }
        } else {
            parentNode = this.getParentNode(element, selectedNode, diagramContext);
        }
        return parentNode;
    }

    /**
     * Get the parent node of the given {@link Element}. This could be a {@link Node} or a {@link Diagram}.
     *
     * @param element
     *            the given {@link Element}.
     * @param diagramElement
     *            the {@link IDiagramElement} corresponding to the given {@link Element}.
     * @param diagramContext
     *            the diagram context in which to find the parent node.
     * @return a {@link Node} or a {@link Diagram}.
     */
    public Object getParentNode(Element element, IDiagramElement diagramElement, DiagramContext diagramContext) {
        Object parentNode = null;
        var diagram = diagramContext.diagram();
        var nodes = diagram.getNodes();
        var edges = diagram.getEdges();
        if (nodes.contains(diagramElement)) {
            parentNode = diagram;
        } else if (edges.contains(diagramElement)) {
            parentNode = diagram;
        } else {
            parentNode = nodes.stream()
                    .map(subNode -> this.getParentNode(diagramElement, subNode))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        return parentNode;
    }

    protected Node getParentNode(IDiagramElement diagramElement, Node nodeContainer) {
        List<Node> nodes = nodeContainer.getChildNodes();
        nodes.addAll(nodeContainer.getBorderNodes());
        if (nodes.contains(diagramElement)) {
            return nodeContainer;
        }
        return nodes.stream()
                .map(subNode -> this.getParentNode(diagramElement, subNode))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    protected List<Node> getChildNodes(DiagramContext diagramContext, Object selectedNode) {
        var childNodes = new ArrayList<Node>();
        if (selectedNode instanceof Node node) {
            childNodes.addAll(node.getChildNodes());
            if (node.getStyle().getChildrenLayoutStrategy() instanceof ListLayoutStrategy) {
                // childNodes are compartments, so also add childNodes of childNodes
                node.getChildNodes().stream().forEach(cn -> childNodes.addAll(cn.getChildNodes()));
            }
        } else {
            var diagram = diagramContext.diagram();
            childNodes.addAll(diagram.getNodes());
        }
        return childNodes;
    }

    protected boolean isPresent(Element element, List<Node> nodes) {
        return nodes.stream().anyMatch(node -> node.getTargetObjectId().equals(this.identityService.getId(element)));
    }

    /**
     * Get the source node of the given {@link Edge}.
     *
     * @param edge
     *            the given {@link Edge}.
     * @param diagram
     *            the diagram containing the {@link Edge}.
     * @return the source node of the given {@link Edge} if found, <code>null</code> otherwise.
     */
    protected Node getSourceNode(Edge edge, Diagram diagram) {
        Node sourceNode = null;
        String sourceId = edge.getSourceId();
        for (Node node : diagram.getNodes()) {
            if (Objects.equals(sourceId, node.getId())) {
                sourceNode = node;
                break;
            }
            sourceNode = this.getSourceNode(sourceId, node);
            if (sourceNode != null) {
                break;
            }
        }
        return sourceNode;
    }

    protected Node getSourceNode(String sourceId, Node node) {
        Node sourceNode = null;
        for (Node childNode : node.getChildNodes()) {
            if (Objects.equals(sourceId, childNode.getId())) {
                sourceNode = childNode;
                break;
            }
            sourceNode = this.getSourceNode(sourceId, childNode);
            if (sourceNode != null) {
                break;
            }
        }
        if (sourceNode == null) {
            for (Node borderNode : node.getBorderNodes()) {
                if (Objects.equals(sourceId, borderNode.getId())) {
                    sourceNode = borderNode;
                    break;
                }
                sourceNode = this.getSourceNode(sourceId, borderNode);
                if (sourceNode != null) {
                    break;
                }
            }
        }
        return sourceNode;
    }
}
