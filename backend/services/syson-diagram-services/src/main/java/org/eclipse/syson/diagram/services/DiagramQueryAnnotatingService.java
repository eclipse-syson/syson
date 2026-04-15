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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.IDiagramElement;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.syson.sysml.AnnotatingElement;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.ViewUsage;
import org.springframework.stereotype.Service;

/**
 * Query services related to annotating nodes.
 *
 * @author arichard
 */
@Service
public class DiagramQueryAnnotatingService {

    private final IObjectSearchService objectSearchService;

    public DiagramQueryAnnotatingService(IObjectSearchService objectSearchService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
    }

    /**
     * Returns {@code true} if the provided annotated element of the given {@code element} is represented on the
     * diagram, {@code false} otherwise.
     */
    public boolean showAnnotatingNode(Element element, DiagramContext diagramContext, IEditingContext editingContext) {
        boolean displayAnnotatingNode = false;
        if (element instanceof AnnotatingElement ae && diagramContext != null && editingContext != null) {
            EList<Element> annotatedElements = ae.getAnnotatedElement();
            IDiagramElement matchingDiagramElement = null;

            displayAnnotatingNode = this.isAnnotatingNodeOnRoot(diagramContext, editingContext, annotatedElements);

            if (!displayAnnotatingNode) {
                for (Node node : diagramContext.diagram().getNodes()) {
                    matchingDiagramElement = this.getOneMatchingAnnotatedNode(node, annotatedElements, diagramContext, editingContext);
                    if (matchingDiagramElement != null) {
                        displayAnnotatingNode = true;
                        break;
                    }
                }
            }
            if (!displayAnnotatingNode) {
                for (Edge edge : diagramContext.diagram().getEdges()) {
                    matchingDiagramElement = this.getOneMatchingAnnotatedEdge(edge, annotatedElements, diagramContext, editingContext);
                    if (matchingDiagramElement != null) {
                        displayAnnotatingNode = true;
                        break;
                    }
                }
            }
        } else {
            displayAnnotatingNode = true;
        }
        return displayAnnotatingNode;
    }

    private boolean isAnnotatingNodeOnRoot(DiagramContext diagramContext, IEditingContext editingContext, EList<Element> annotatedElements) {
        boolean isAnnotatingNodeOnRoot = false;
        String diagramTargetObjectId = diagramContext.diagram().getTargetObjectId();
        Element diagramTargetObject = this.objectSearchService.getObject(editingContext, diagramTargetObjectId).stream()
                .filter(Element.class::isInstance)
                .map(Element.class::cast)
                .findFirst()
                .orElse(null);
        if (diagramTargetObject instanceof ViewUsage viewUsage) {
            if (annotatedElements.contains(viewUsage)) {
                isAnnotatingNodeOnRoot = true;
            } else {
                isAnnotatingNodeOnRoot = annotatedElements.contains(viewUsage.getOwner());
            }
        }
        return isAnnotatingNodeOnRoot;
    }

    private Edge getOneMatchingAnnotatedEdge(Edge edge, List<Element> annotatedElements, DiagramContext diagramContext, IEditingContext editingContext) {
        Edge matchingAnnotatedEdge = null;
        Optional<Object> semanticNodeOpt = this.objectSearchService.getObject(editingContext, edge.getTargetObjectId());
        if (semanticNodeOpt.isPresent()) {
            if (annotatedElements.contains(semanticNodeOpt.get())) {
                boolean isDeletingAnnotatingEdge = diagramContext.viewDeletionRequests().stream()
                        .anyMatch(viewDeletionRequest -> Objects.equals(viewDeletionRequest.getElementId(), edge.getId()));
                if (!isDeletingAnnotatingEdge) {
                    matchingAnnotatedEdge = edge;
                }
                return matchingAnnotatedEdge;
            }
        }
        return matchingAnnotatedEdge;
    }

    private Node getOneMatchingAnnotatedNode(Node node, List<Element> annotatedElements, DiagramContext diagramContext, IEditingContext editingContext) {
        Node matchingAnnotatedNode = null;
        Optional<Object> semanticNodeOpt = this.objectSearchService.getObject(editingContext, node.getTargetObjectId());
        if (semanticNodeOpt.isPresent()) {
            if (annotatedElements.contains(semanticNodeOpt.get())) {
                boolean isDeletingAnnotatingNode = diagramContext.viewDeletionRequests().stream()
                        .anyMatch(viewDeletionRequest -> Objects.equals(viewDeletionRequest.getElementId(), node.getId()));
                if (!isDeletingAnnotatingNode) {
                    matchingAnnotatedNode = node;
                }
                return matchingAnnotatedNode;
            }
        }
        matchingAnnotatedNode = this.getFirstMatchingChildAnnotatedNode(node, annotatedElements, diagramContext, editingContext);
        return matchingAnnotatedNode;
    }

    private Node getFirstMatchingChildAnnotatedNode(Node node, List<Element> annotatedElements, DiagramContext diagramContext, IEditingContext editingContext) {
        List<Node> childrenNodes = Stream.concat(node.getChildNodes().stream(), node.getBorderNodes().stream()).toList();
        for (Node childNode : childrenNodes) {
            Node matchingChildAnnotatedNode = this.getOneMatchingAnnotatedNode(childNode, annotatedElements, diagramContext, editingContext);
            if (matchingChildAnnotatedNode != null) {
                return matchingChildAnnotatedNode;
            }
        }
        return null;
    }
}
