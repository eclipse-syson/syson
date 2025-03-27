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
package org.eclipse.syson.application.controllers.diagrams.checkers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.syson.services.diagrams.DiagramComparator;

/**
 * Checks that the diagram contains the given number of elements.
 *
 * @author gdaniel
 */
public class CheckDiagramElementCount implements IDiagramChecker {

    private final DiagramComparator diagramComparator;

    private int newNodeCount;

    private int newEdgeCount;

    private int newBorderNodeCount;

    public CheckDiagramElementCount(DiagramComparator diagramComparator) {
        this.diagramComparator = diagramComparator;
    }

    public CheckDiagramElementCount hasNewNodeCount(int expectedNodeCount) {
        this.newNodeCount = expectedNodeCount;
        return this;
    }

    public CheckDiagramElementCount hasNewEdgeCount(int expectedEdgeCount) {
        this.newEdgeCount = expectedEdgeCount;
        return this;
    }

    public CheckDiagramElementCount hasNewBorderNodeCount(int expectedBorderNodeCount) {
        this.newBorderNodeCount = expectedBorderNodeCount;
        return this;
    }

    @Override
    public void check(Diagram previousDiagram, Diagram newDiagram) {
        List<Node> newNodes = this.diagramComparator.newNodes(previousDiagram, newDiagram);
        assertThat(newNodes).as("The diagram should contain " + this.newNodeCount + " new nodes").hasSize(this.newNodeCount);
        List<Edge> newEdges = this.diagramComparator.newEdges(previousDiagram, newDiagram);
        assertThat(newEdges).as("The diagram should contain " + this.newEdgeCount + " new edges").hasSize(this.newEdgeCount);
        assertThat(newNodes.stream().filter(Node::isBorderNode).count()).as("The diagram should contain " + this.newBorderNodeCount + " new border nodes").isEqualTo(this.newBorderNodeCount);
    }
}
