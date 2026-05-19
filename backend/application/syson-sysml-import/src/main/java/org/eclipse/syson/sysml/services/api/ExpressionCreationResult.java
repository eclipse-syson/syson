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
package org.eclipse.syson.sysml.services.api;

import java.util.List;

import org.eclipse.sirius.components.representations.Message;
import org.eclipse.syson.sysml.Expression;

/**
 * The result of creating a new expression from text. {@code createdExpression} may be <code>null</code> to indicate the
 * expression could not be created, in which case {@cope messages} will contain the reason(s) for the failure.
 *
 * @author pcdavid
 */
public record ExpressionCreationResult(Expression createdExpression, List<Message> messages) {
}
