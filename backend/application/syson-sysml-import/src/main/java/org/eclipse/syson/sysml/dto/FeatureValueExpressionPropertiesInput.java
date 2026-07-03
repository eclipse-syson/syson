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
package org.eclipse.syson.sysml.dto;

/**
 * Optional properties that can be applied to a {@code FeatureValue} expression owner.
 *
 * @param isDefault
 *            whether the feature value is marked as default. A {@code null} value leaves the current state unchanged.
 * @param isInitial
 *            whether the feature value is marked as initial. A {@code null} value leaves the current state unchanged.
 * @author arichard
 */
public record FeatureValueExpressionPropertiesInput(Boolean isDefault, Boolean isInitial) {
}
