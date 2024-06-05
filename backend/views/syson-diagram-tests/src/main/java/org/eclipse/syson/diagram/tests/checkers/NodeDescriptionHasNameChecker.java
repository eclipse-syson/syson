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

import org.eclipse.sirius.components.view.diagram.NodeDescription;

/**
 * Checks that the provided {@link NodeDescription} has the expected name.
 *
 * @author gdaniel
 */
public class NodeDescriptionHasNameChecker extends AbstractChecker<NodeDescription> {

    private String expectedPrefix;

    public NodeDescriptionHasNameChecker withExpectedPrefix(String prefix) {
        this.expectedPrefix = prefix;
        return this;
    }

    @Override
    public void check(NodeDescription nodeDescription) {
        assertThat(nodeDescription.getName())
                .as("NodeDescription %s name should start with %s", nodeDescription.getName(), this.expectedPrefix)
                .startsWith(this.expectedPrefix);
    }
}
