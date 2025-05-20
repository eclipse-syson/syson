/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
 * Checks that a border node with the given properties exists.
 *
 * @author arichard
 */
public class CheckBorderNode implements IDiagramChecker {

    private final DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private final DiagramComparator diagramComparator;

    private String parentLabel;

    private String borderNodeDescriptionName;

    public CheckBorderNode(DiagramDescriptionIdProvider diagramDescriptionIdProvider, DiagramComparator diagramComparator) {
        this.diagramDescriptionIdProvider = diagramDescriptionIdProvider;
        this.diagramComparator = diagramComparator;
    }

    public CheckBorderNode withParentLabel(String expectedParentLabel) {
        this.parentLabel = expectedParentLabel;
        return this;
    }

    public CheckBorderNode hasBorderNodeDescriptionName(String expectedBorderNodeDescriptionName) {
        this.borderNodeDescriptionName = Objects.requireNonNull(expectedBorderNodeDescriptionName);
        return this;
    }

    @Override
    public void check(Diagram previousDiagram, Diagram newDiagram) {
        List<Node> newNodes = this.diagramComparator.newNodes(previousDiagram, newDiagram);
        DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
        var parentNode = diagramNavigator.nodeWithTargetObjectLabel(this.parentLabel).getNode();
        assertThat(parentNode.getBorderNodes()).anySatisfy(borderNode -> {
            String borderNodeDescriptionId = this.diagramDescriptionIdProvider.getNodeDescriptionId(this.borderNodeDescriptionName);
            assertThat(borderNode).hasDescriptionId(borderNodeDescriptionId);
            assertThat(newNodes).contains(borderNode);
        });
    }
}
