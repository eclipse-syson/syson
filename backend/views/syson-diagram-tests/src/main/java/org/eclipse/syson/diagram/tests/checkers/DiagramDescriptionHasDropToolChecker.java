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

import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DiagramPalette;

/**
 * Checks that the provided {@link DiagramDescription} has the expected drop tool.
 *
 * @author gdaniel
 */
public class DiagramDescriptionHasDropToolChecker extends AbstractChecker<DiagramDescription> {

    @Override
    public void check(DiagramDescription diagramDescription) {
        assertThat(diagramDescription.getPalette())
                .as("DiagramDescription should have a palette")
                .isNotNull()
                .extracting(DiagramPalette::getDropTool)
                .as("DiagramDescription should have a drop tool")
                .isNotNull();
    }
}
