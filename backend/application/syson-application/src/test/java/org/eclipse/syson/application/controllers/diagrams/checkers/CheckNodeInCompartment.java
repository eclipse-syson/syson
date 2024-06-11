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
 * Checks that a node with the given properties exist in the specified compartment.
 *
 * @author gdaniel
 */
public class CheckNodeInCompartment implements IDiagramChecker {

    private final DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private final DiagramComparator diagramComparator;

    private String parentLabel;

    private String compartmentName;

    private String nodeDescriptionName;

    private int compartmentCount;

    public CheckNodeInCompartment(DiagramDescriptionIdProvider diagramDescriptionIdProvider, DiagramComparator diagramComparator) {
        this.diagramDescriptionIdProvider = diagramDescriptionIdProvider;
        this.diagramComparator = diagramComparator;
    }

    public CheckNodeInCompartment withParentLabel(String expectedParentLabel) {
        this.parentLabel = expectedParentLabel;
        return this;
    }

    public CheckNodeInCompartment withCompartmentName(String expectedCompartmentName) {
        this.compartmentName = expectedCompartmentName;
        return this;
    }

    public CheckNodeInCompartment hasNodeDescriptionName(String expectedNodeDescriptionName) {
        this.nodeDescriptionName = Objects.requireNonNull(expectedNodeDescriptionName);
        return this;
    }

    public CheckNodeInCompartment hasCompartmentCount(int expectedCompartmentCount) {
        this.compartmentCount = expectedCompartmentCount;
        return this;
    }

    @Override
    public void check(Diagram previousDiagram, Diagram newDiagram) {
        List<Node> newNodes = this.diagramComparator.newNodes(previousDiagram, newDiagram);
        DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
        var compartmentNode = diagramNavigator.nodeWithTargetObjectLabel(this.parentLabel)
                .childNodeWithLabel(this.compartmentName)
                .getNode();
        assertThat(compartmentNode.getChildNodes()).anySatisfy(childNode -> {
            assertThat(childNode).hasDescriptionId(this.diagramDescriptionIdProvider.getNodeDescriptionId(this.nodeDescriptionName));
            assertThat(childNode.getChildNodes()).hasSize(this.compartmentCount);
            assertThat(newNodes).contains(childNode);
        });
    }
}
