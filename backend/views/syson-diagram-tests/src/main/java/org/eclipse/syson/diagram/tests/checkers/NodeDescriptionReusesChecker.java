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

import java.util.stream.Stream;

import org.eclipse.sirius.components.view.diagram.NodeDescription;

/**
 * Checks that the provided {@link NodeDescription} reuses the expected elements.
 *
 * @author gdaniel
 */
public class NodeDescriptionReusesChecker extends AbstractChecker<NodeDescription> {

    private int expectedReuseCount;

    public NodeDescriptionReusesChecker withExpectedReuseCount(int reuseCount) {
        this.expectedReuseCount = reuseCount;
        return this;
    }

    @Override
    public void check(NodeDescription nodeDescription) {
        Stream<NodeDescription> reusedNodeDescriptions = Stream.concat(nodeDescription.getReusedBorderNodeDescriptions().stream(), nodeDescription.getReusedChildNodeDescriptions().stream());
        assertThat(reusedNodeDescriptions)
                .as("The node %s should reuse %s nodes", nodeDescription.getName(), this.expectedReuseCount)
                .hasSize(this.expectedReuseCount);
    }
}
