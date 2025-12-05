/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;

/**
 * Used to find nodes inside a given Diagram.
 *
 * @author Jerome Gout
 */
public class NodeFinder {

    private final Diagram diagram;

    public NodeFinder(Diagram diagram) {
        this.diagram = Objects.requireNonNull(diagram);
    }

    /**
     * Gets the parent of the given {@link Node}. It can either be another {@link Node} or a {@link Diagram}
     *
     * @param currentNode
     *            a non <code>null</code> node to search the parent for
     * @return a parent {@link Node}, the {@link Diagram} or <code>null</code> if the given node is not in the current
     *         diagram
     */
    public Object getParent(Node currentNode) {
        Object result = null;
        if (currentNode != null) {
            String currentNodeId = currentNode.getId();
            for (Node node : this.diagram.getNodes()) {
                if (Objects.equals(currentNodeId, node.getId())) {
                    result = this.diagram;
                } else {
                    result = this.doSearchFirst(node, n ->
                            this.getAllChildrenCandidates(n, currentNode.isBorderNode())
                                    .anyMatch(subNode -> Objects.equals(currentNodeId, subNode.getId()))).orElse(null);
                }
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Gets the first node in the diagram matching a given {@code predicate}.
     * <p>
     * See the exhaustive {@link #getAllNodesMatching(Predicate)} method if you need to find all the nodes matching the
     * given predicate.
     * </p>
     *
     * @param predicate
     *            the predicate used to filter nodes
     * @return the first nodes matching the given {@code predicate}
     */
    public Optional<Node> getOneNodeMatching(Predicate<Node> predicate) {
        Objects.requireNonNull(predicate);
        for (Node node : this.diagram.getNodes()) {
            Optional<Node> result = this.doSearchFirst(node, predicate);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    /**
     * Gets all the nodes in the diagram matching a given {@code filter}.
     * <p>
     * This method iterates the entire content of the diagram. See the short-circuiting
     * {@link #getOneNodeMatching(Predicate)} method if you need to find a single node matching the given filter.
     * </p>
     *
     * @param filter
     *            the predicate used to filter nodes
     * @return the nodes matching the given {@code filter}
     */
    public List<Node> getAllNodesMatching(Predicate<Node> filter) {
        Objects.requireNonNull(filter);
        List<Node> result = new ArrayList<>();
        for (Node node : this.diagram.getNodes()) {
            this.doSearchAll(node, filter, result);
        }
        return result;
    }

    private Optional<Node> doSearchFirst(Node node, Predicate<Node> predicate) {
        if (predicate.test(node)) {
            return Optional.of(node);
        }
        Optional<Node> result = Optional.empty();
        for (Node childNode : this.getAllChildrenNodes(node)) {
            result = this.doSearchFirst(childNode, predicate);
            if (result.isPresent()) {
                break;
            }
        }
        return result;
    }

    private void doSearchAll(Node node, Predicate<Node> filter, List<Node> accumulator) {
        if (filter.test(node)) {
            accumulator.add(node);
        }
        for (Node childNode : this.getAllChildrenNodes(node)) {
            this.doSearchAll(childNode, filter, accumulator);
        }
    }

    private List<Node> getAllChildrenNodes(Node parent) {
        return Stream.concat(parent.getChildNodes().stream(), parent.getBorderNodes().stream()).toList();
    }

    private Stream<Node> getAllChildrenCandidates(Node node, boolean isBorderNode) {
        if (isBorderNode) {
            // In case we are looking for a bordered node, first search in the BorderedNode list to avoid browsing all nested nodes if not necessary
            return Stream.concat(node.getBorderNodes().stream(), node.getChildNodes().stream());
        } else {
            return Stream.concat(node.getChildNodes().stream(), node.getBorderNodes().stream());
        }
    }
}
