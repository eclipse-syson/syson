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
package org.eclipse.syson.sysml.linker.additionalmembers;

import org.omg.sysml.lang.sysml.ReturnParameterMembership;
import org.omg.sysml.lang.sysml.Type;
import org.omg.sysml.util.TypeUtil;

/**
 * Completes the result parameter of invocation, constructor and feature-reference expressions.
 * <p>
 * This completion only inspects and modifies containment. It can be called after parsing, before reference resolution, once all parsed relationships have been attached to the expression.
 * </p>
 *
 * @author Arthur Daussy
 */
public final class ExpressionResultParameterCompleter {

    private ExpressionResultParameterCompleter() {
        // Utility class.
    }

    /**
     * Completes the result parameter if it is absent.
     */
    public static void completeResultParameter(Type expression) {
        if (expression.getOwnedRelationship().stream()
                .noneMatch(ReturnParameterMembership.class::isInstance)) {
            TypeUtil.addResultParameterTo(expression);
        }
    }
}
