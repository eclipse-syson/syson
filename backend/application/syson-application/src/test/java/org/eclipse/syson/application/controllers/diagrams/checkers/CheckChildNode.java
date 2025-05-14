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
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions.assertThat;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;

/**
 * Checks that a child node with the given properties exist.
 *
 * @author gdaniel
 */
public class CheckChildNode implements IDiagramChecker {

    private final DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private final DiagramComparator diagramComparator;

    private String parentLabel;

    private String parentNodeId;

    private String nodeDescriptionName;

    private int compartmentCount;

    public CheckChildNode(DiagramDescriptionIdProvider diagramDescriptionIdProvider, DiagramComparator diagramComparator) {
        this.diagramDescriptionIdProvider = Objects.requireNonNull(diagramDescriptionIdProvider);
        this.diagramComparator = Objects.requireNonNull(diagramComparator);
    }

    public CheckChildNode withParentLabel(String expectedParentLabel) {
        this.parentLabel = expectedParentLabel;
        return this;
    }

    public CheckChildNode withParentNodeId(String expectedParentNodeId) {
        this.parentNodeId = expectedParentNodeId;
        return this;
    }

    public CheckChildNode hasNodeDescriptionName(String expectedNodeDescriptionName) {
        this.nodeDescriptionName = expectedNodeDescriptionName;
        return this;
    }

    public CheckChildNode hasCompartmentCount(int expectedCompartmentCount) {
        this.compartmentCount = expectedCompartmentCount;
        return this;
    }

    @Override
    public void check(Diagram previousDiagram, Diagram newDiagram) {
        List<Node> newNodes = this.diagramComparator.newNodes(previousDiagram, newDiagram);
        DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
        Node parentNode;
        if (this.parentNodeId != null) {
            parentNode = diagramNavigator.nodeWithId(this.parentNodeId).getNode();
        } else {
            parentNode = diagramNavigator.nodeWithTargetObjectLabel(this.parentLabel).getNode();
        }
        assertThat(parentNode).isNotNull();
        assertThat(parentNode.getChildNodes()).anySatisfy(childNode -> {
            assertThat(childNode).hasDescriptionId(this.diagramDescriptionIdProvider.getNodeDescriptionId(this.nodeDescriptionName));
            assertThat(childNode.getChildNodes()).hasSize(this.compartmentCount);
            assertThat(newNodes).contains(childNode);
        });

    }

}
