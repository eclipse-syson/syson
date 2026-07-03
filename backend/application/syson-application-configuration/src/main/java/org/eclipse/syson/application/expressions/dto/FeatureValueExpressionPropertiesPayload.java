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
package org.eclipse.syson.application.expressions.dto;

/**
 * Properties exposed by the expression editor for expressions owned by a SysML {@code FeatureValue}.
 *
 * @param isDefault
 *            whether the feature value is marked as default.
 * @param isInitial
 *            whether the feature value is marked as initial.
 * @author arichard
 */
public record FeatureValueExpressionPropertiesPayload(boolean isDefault, boolean isInitial) {
}
