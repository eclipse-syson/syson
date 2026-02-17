/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions.assertThat;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;

/**
 * Checks that a top node with the given properties exist on the diagram.
 *
 * @author gdaniel
 */
public class CheckNodeOnDiagram implements IDiagramChecker {

    private final DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private final DiagramComparator diagramComparator;

    private String nodeDescriptionName;

    private int totalCompartmentCount;

    private int visibleCompartmentCount;

    private String targetObjectLabel;

    public CheckNodeOnDiagram(DiagramDescriptionIdProvider diagramDescriptionIdProvider, DiagramComparator diagramComparator) {
        this.diagramDescriptionIdProvider = diagramDescriptionIdProvider;
        this.diagramComparator = diagramComparator;
    }

    public CheckNodeOnDiagram hasNodeDescriptionName(String expectedNodeDescriptionName) {
        this.nodeDescriptionName = Objects.requireNonNull(expectedNodeDescriptionName);
        return this;
    }

    public CheckNodeOnDiagram hasTotalCompartmentCount(int expectedTotalCompartmentCount) {
        this.totalCompartmentCount = expectedTotalCompartmentCount;
        return this;
    }

    public CheckNodeOnDiagram hasVisibleCompartmentCount(int expectedVisibleCompartmentCount) {
        this.visibleCompartmentCount = expectedVisibleCompartmentCount;
        return this;
    }

    public CheckNodeOnDiagram hasTargetObjectLabel(String expectedTargetObjectLabel) {
        this.targetObjectLabel = Objects.requireNonNull(expectedTargetObjectLabel);
        return this;
    }

    @Override
    public void check(Diagram previousDiagram, Diagram newDiagram) {
        String nodeDescriptionId = this.diagramDescriptionIdProvider.getNodeDescriptionId(this.nodeDescriptionName);
        List<Node> newNodes = this.diagramComparator.newNodes(previousDiagram, newDiagram);
        assertThat(newDiagram.getNodes()).anySatisfy(childNode -> {
            assertThat(childNode).hasDescriptionId(nodeDescriptionId);
            assertThat(childNode.getChildNodes()).hasSize(this.totalCompartmentCount);
            if (this.visibleCompartmentCount != -1) {
                assertThat(childNode.getChildNodes().stream().filter(node -> node.getState() != ViewModifier.Hidden)).hasSize(this.visibleCompartmentCount);
            }
            if (this.targetObjectLabel != null) {
                assertThat(childNode).hasTargetObjectLabel(this.targetObjectLabel);
            }
            assertThat(newNodes).contains(childNode);
        });
    }
}
