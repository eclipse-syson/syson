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
 * Optional typed properties associated with an expression mutation.
 *
 * @param featureValue
 *            properties to apply when the expression belongs to a {@code FeatureValue}.
 * @author arichard
 */
public record ExpressionPropertiesInput(FeatureValueExpressionPropertiesInput featureValue) {
}
