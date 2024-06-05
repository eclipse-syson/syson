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

import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgePalette;

/**
 * Checks that the provided {@link EdgeDescription} has the expected direct edit tools.
 *
 * @author gdaniel
 */
public class EdgeDescriptionHasReconnectToolChecker extends AbstractChecker<EdgeDescription> {

    private int expectedReconnectToolCount;

    public EdgeDescriptionHasReconnectToolChecker withExpectedReconnectToolCount(int reconnectToolCount) {
        this.expectedReconnectToolCount = reconnectToolCount;
        return this;
    }

    @Override
    public void check(EdgeDescription edgeDescription) {
        assertThat(edgeDescription.getPalette())
                .as("EdgeDescription %s should have a palette", edgeDescription.getName())
                .isNotNull()
                .extracting(EdgePalette::getEdgeReconnectionTools)
                .as("EdgeDescription %s should have %s reconnection tools", edgeDescription.getName(), this.expectedReconnectToolCount)
                .asList()
                .hasSize(this.expectedReconnectToolCount);
    }

}
