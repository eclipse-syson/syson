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
package org.eclipse.syson.application.nodes.dto;

import org.eclipse.sirius.components.diagrams.LineStyle;

/**
 * Input for the edition of a SysMLNote node's appearance.
 *
 * @author arichard
 */
public record SysMLNoteNodeAppearanceInput(String background, String borderColor, Integer borderSize, LineStyle borderStyle) {
}
