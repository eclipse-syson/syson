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
package org.eclipse.syson.diagram.common.view.services;

import java.util.Objects;
import java.util.stream.Stream;

import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;

/**
 * Used to find parent nodes inside a given Diagram.
 *
 * @author Jerome Gout
 */
public class ParentNodeFinder {

    private final Diagram diagram;

    public ParentNodeFinder(Diagram diagram) {
        this.diagram = Objects.requireNonNull(diagram);
    }

    /**
     * Gets the parent of the given {@link Node}. It can either be another {@link Node} or a {@link Diagram}
     *
     * @param searchNode
     *            a non <code>null</code> node to search
     * @return a parent {@link Node}, the {@link Diagram} or <code>null</code> if the given node is not in the current
     *         diagram
     */
    public Object getParent(Node searchNode) {
        return this.doSearch(searchNode.getId(), searchNode.isBorderNode());
    }

    private Object doSearch(String searchedNodeId, boolean isBorderNode) {
        for (Node node : this.diagram.getNodes()) {
            final Object result;
            if (searchedNodeId.equals(node.getId())) {
                result = this.diagram;
            } else {
                result = this.searchIn(node, searchedNodeId, isBorderNode);
            }
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private Node searchIn(Node current, String searchedNodeId, boolean isBorderNode) {
        if (this.getAllChildrenCandidates(current, isBorderNode).anyMatch(n -> searchedNodeId.equals(n.getId()))) {
            return current;
        } else {
            Node result = null;
            for (Node child : current.getChildNodes()) {
                result = this.searchIn(child, searchedNodeId, isBorderNode);
                if (result != null) {
                    break;
                }
            }
            return result;
        }
    }

    private Stream<Node> getAllChildrenCandidates(Node node, boolean isBorderNode) {
        if (isBorderNode) {
            return node.getBorderNodes().stream();
        } else {
            return node.getChildNodes().stream();
        }
    }
}
