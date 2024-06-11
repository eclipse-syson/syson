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
package org.eclipse.syson.services.diagrams;

import java.util.List;

import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.syson.diagram.common.view.services.NodeFinder;
import org.springframework.stereotype.Service;

/**
 * Provides utility methods to compare diagrams.
 *
 * @author gdaniel
 */
@Service
public class DiagramComparator {

    public List<Node> newNodes(Diagram previousDiagram, Diagram newDiagram) {
        NodeFinder previousDiagramNodeFinder = new NodeFinder(previousDiagram);
        NodeFinder newDiagramNodeFinder = new NodeFinder(newDiagram);
        List<String> previousNodeIds = previousDiagramNodeFinder.getAllNodesMatching(n -> true).stream()
                .map(Node::getId)
                .toList();
        return newDiagramNodeFinder.getAllNodesMatching(n -> !previousNodeIds.contains(n.getId()));
    }

    public List<Edge> newEdges(Diagram previousDiagram, Diagram newDiagram) {
        List<String> previousEdgeIds = previousDiagram.getEdges().stream()
                .map(Edge::getId)
                .toList();
        return newDiagram.getEdges().stream()
                .filter(edge -> !previousEdgeIds.contains(edge.getId()))
                .toList();
    }

}
