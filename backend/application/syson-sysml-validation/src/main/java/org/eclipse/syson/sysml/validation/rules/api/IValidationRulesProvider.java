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

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.validation.SysONSysMLValidator;

/**
 * Interface for providing the {@link IValidationRule} to use for validation.
 *
 * @author flatombe
 */
public interface IValidationRulesProvider {

    /**
     * Provides the validation rules to apply on a type of model element.
     * <p>
     * Note: For a given type, the list of rules may change arbitrarily between different calls (e.g. to allow
     * applications to provide a UI to select the validation rules to apply).
     * </p>
     * <p>
     * Note: the default consumer of this method is {@link SysONSysMLValidator} that expects rules to pertain to types
     * from, or inheriting from, {@link SysmlPackage}. When providing rules from other EPackages, consider registering
     * your own {@link EValidator} implementation in the global {@link EValidator.Registry}.
     * </p>
     *
     * @param eClass
     *            the (non-{@code null}) {@link EClass} for which we want the applicable validation rules.
     * @return a (non-{@code null}) {@link List} of {@link IValidationRule}.
     */
    List<? extends IValidationRule> getValidationRules(EClass eClass);

    /**
     * Provides the AQL service classes to register to support all the possible {@link IValidationRule validation rules}
     * provided by {@code this}.
     *
     * @return a (non-{@code null}) {@link List} of {@link Class}.
     */
    List<Class<?>> getAQLServiceClasses();

    /**
     * Provides the AQL service objects to register to support all the possible {@link IValidationRule validation rules}
     * provided by {@code this}.
     *
     * @return a (non-{@code null}) {@link List} of {@link Object}.
     */
    List<Object> getAQLServiceInstances();

    /**
     * Provides the {@link EPackage} to register in the AQL environment to support all the possible
     * {@link IValidationRule validation rules} provided by {@code this}.
     *
     * @return a (non-{@code null}) {@link List} of {@link EPackage}.
     */
    List<EPackage> getEPackages();

}
