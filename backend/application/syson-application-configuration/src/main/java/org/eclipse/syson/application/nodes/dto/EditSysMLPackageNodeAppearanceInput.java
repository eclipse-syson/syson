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

import java.util.List;
import java.util.UUID;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramInput;

/**
 * Input for the edition of a SysMLPackage node's appearance.
 *
 * @author arichard
 */
public record EditSysMLPackageNodeAppearanceInput(UUID id, String editingContextId, String representationId, List<String> nodeIds, SysMLPackageNodeAppearanceInput appearance)
        implements IDiagramInput {
}
