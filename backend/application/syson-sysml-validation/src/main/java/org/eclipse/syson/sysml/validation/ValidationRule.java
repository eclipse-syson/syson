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
package org.eclipse.syson.sysml.validation;

import java.util.Objects;

import org.eclipse.emf.ecore.EClass;

/**
 * POJO describing a validation rule.
 *
 * @author arichard
 */
public class ValidationRule {

    private final EClass eClass;

    private final String name;

    private final String expression;

    private final String message;

    public ValidationRule(EClass eClass, String name, String expression, String message) {
        this.eClass = Objects.requireNonNull(eClass);
        this.name = Objects.requireNonNull(name);
        this.expression = Objects.requireNonNull(expression);
        this.message = Objects.requireNonNull(message);
    }

    public EClass getEClass() {
        return this.eClass;
    }

    public String getName() {
        return this.name;
    }

    public String getExpression() {
        return this.expression;
    }

    public String getMessage() {
        return this.message;
    }
}
