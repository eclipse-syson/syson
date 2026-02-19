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
package org.eclipse.syson.sysml.validation.rules.api;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.validation.SysONSysMLValidator;

/**
 * Common interface for the validation rules supported by {@link SysONSysMLValidator}.
 *
 * @author flatombe
 */
public interface IValidationRule {

    /**
     * Provides the type on which this rule applies. Depending on the {@link IValidationRulesProvider} implementation in
     * use, this rule will most likely also apply on sub-types of the provided type.
     *
     * @return a (non-{@code null}) {@link EClass}, most likely from {@link SysmlPackage}, or sub-typing one of its
     *         types.
     */
    EClass eClass();

    /**
     * Provides the name of this rule.
     *
     * @return a (non-{@code null}) {@link String}.
     */
    String ruleName();

    /**
     * Provides the expression used to implement this rule. See {@link SysONSysMLValidator} for the supported expression
     * language(s).
     *
     * @return a (non-{@code null}) {@link String}.
     */
    String expression();

    /**
     * Provides the message, in English natural language, to display when this rule does not succeed.
     *
     * @return a (non-{@code null}) {@link String}
     */
    String message();

    /**
     * Provides the severity associated to this rule.
     *
     * @return an {@code int} as per {@link Diagnostic#getSeverity()}, most likely one of {@link Diagnostic#OK},
     *         {@link Diagnostic#INFO}, {@link Diagnostic#WARNING}, {@link Diagnostic#ERROR}, {@link Diagnostic#CANCEL}.
     */
    int severity();
}
