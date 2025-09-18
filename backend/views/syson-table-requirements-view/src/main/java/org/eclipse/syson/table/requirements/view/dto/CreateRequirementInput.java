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
package org.eclipse.syson.table.requirements.view.dto;

import java.util.UUID;

import org.eclipse.sirius.components.core.api.IInput;

/**
 * The input object of the CreateRequirement mutation.
 *
 * @author arichard
 */
public record CreateRequirementInput(UUID id, String editingContextId, String tableId) implements IInput {

}
