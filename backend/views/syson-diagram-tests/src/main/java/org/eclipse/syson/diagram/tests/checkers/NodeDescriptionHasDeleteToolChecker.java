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
import org.eclipse.sirius.components.view.diagram.NodePalette;

/**
 * Checks that the provided {@link NodeDescription} has the expected delete tool.
 *
 * @author gdaniel
 */
public class NodeDescriptionHasDeleteToolChecker extends AbstractChecker<NodeDescription> {

    @Override
    public void check(NodeDescription nodeDescription) {
        assertThat(nodeDescription.getPalette())
                .as("NodeDescription %s should have a palette", nodeDescription.getName())
                .isNotNull()
                .extracting(NodePalette::getDeleteTool)
                .as("NodeDescription %s should have a delete tool", nodeDescription.getName())
                .isNotNull();
    }
}
