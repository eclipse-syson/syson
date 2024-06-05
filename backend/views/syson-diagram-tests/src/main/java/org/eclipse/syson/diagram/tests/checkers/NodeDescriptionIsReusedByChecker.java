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
package org.eclipse.syson.diagram.tests.checkers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.sysml.helper.EMFUtils;

/**
 * Checks that the provided {@link NodeDescription} is reused by the expected elements.
 *
 * @author gdaniel
 */
public class NodeDescriptionIsReusedByChecker extends AbstractChecker<NodeDescription> {

    private final DiagramDescription diagramDescription;

    private int expectedReuserCountLowerBound;

    public NodeDescriptionIsReusedByChecker(DiagramDescription diagramDescription) {
        this.diagramDescription = diagramDescription;
    }

    public NodeDescriptionIsReusedByChecker withExpectedReuserCountLowerBound(int reuserCountLowerBound) {
        this.expectedReuserCountLowerBound = reuserCountLowerBound;
        return this;
    }

    @Override
    public void check(NodeDescription nodeDescription) {
        List<NodeDescription> allNodes = EMFUtils.allContainedObjectOfType(this.diagramDescription, NodeDescription.class).toList();
        assertThat(allNodes).map(n -> {
            List<NodeDescription> reusedNodes = new ArrayList<>();
            reusedNodes.addAll(n.getReusedChildNodeDescriptions());
            reusedNodes.addAll(n.getReusedBorderNodeDescriptions());
            return reusedNodes;
        })
                .as("Node %s should be reused by at least %s NodeDescription", nodeDescription.getName(), this.expectedReuserCountLowerBound)
                .filteredOn(reuserList -> reuserList.contains(nodeDescription))
                .hasSizeGreaterThanOrEqualTo(this.expectedReuserCountLowerBound);
    }
}
