/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.standard.diagrams.view.services.api;

import java.util.Objects;

/**
 * The three colors of a semantic family in standard diagrams.
 *
 * @author gcoutable
 */
public record SDVSemanticPalette(String headerBackground, String compartmentBackground, String borderColor) {

    public SDVSemanticPalette {
        Objects.requireNonNull(headerBackground);
        Objects.requireNonNull(compartmentBackground);
        Objects.requireNonNull(borderColor);
    }
}
