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
 * Checks that the provided {@link NodeDescription} has the expected children.
 * <p>
 * This checker checks both direct children (accessible via {@link NodeDescription#getChildrenDescriptions()} and reused
 * ones (accessible via {@link NodeDescription#getReusedChildNodeDescriptions()}). It doesn't check border nodes.
 * </p>
 *
 * @author gdaniel
 */
public class NodeDescriptionHasChildrenChecker extends AbstractChecker<NodeDescription> {

    private int expectedChildCountLowerBound;

    public NodeDescriptionHasChildrenChecker withExpectedChildCountLowerBound(int childCountLowerBound) {
        this.expectedChildCountLowerBound = childCountLowerBound;
        return this;
    }

    @Override
    public void check(NodeDescription nodeDescription) {
        Stream<NodeDescription> children = Stream.concat(nodeDescription.getChildrenDescriptions().stream(), nodeDescription.getReusedChildNodeDescriptions().stream());
        assertThat(children)
                .as("NodeDescription %s should contain or reuse at least %s NodeDescription as its child", this.expectedChildCountLowerBound, nodeDescription.getName())
                .hasSizeGreaterThanOrEqualTo(this.expectedChildCountLowerBound);
    }
}
