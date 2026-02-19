/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.sysml.validation.rules;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.validation.rules.api.IValidationRule;
import org.eclipse.syson.sysml.validation.rules.api.IValidationRulesProvider;

import jakarta.validation.constraints.NotNull;

/**
 * Default implementation for {@link IValidationRule}. Consumers may provide their own custom validation rules by
 * providing instances through an {@link IValidationRulesProvider}.
 * <p>
 * <b>Important:</b> if {@link #eClass()} is not from {@link SysmlPackage}, or sub-typing from it, then you will need to
 * register your own {@link EValidator} implementation in the global {@link EValidator.Registry}.
 * </p>
 *
 * @author arichard
 */
public record ValidationRule(@NotNull EClass eClass, @NotNull String ruleName, @NotNull String expression, @NotNull String message, @NotNull int severity) implements IValidationRule {

}
