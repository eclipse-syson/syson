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
package org.eclipse.syson.sysml.validation;

import java.util.Objects;

import org.eclipse.emf.ecore.EValidator.Registry;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.validation.rules.api.IValidationRulesProvider;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Configuration of the SysMLv2 Validator.
 *
 * @author arichard
 * @see SysONSysMLValidationService
 */
@Configuration
public class SysMLValidatorRegistrationConfiguration {

    private final IValidationRulesProvider validationRulesProvider;

    private final Registry eValidatorRegistry;

    public SysMLValidatorRegistrationConfiguration(final IValidationRulesProvider validationRulesProvider, final Registry eValidatorRegistry) {
        this.validationRulesProvider = Objects.requireNonNull(validationRulesProvider);
        this.eValidatorRegistry = Objects.requireNonNull(eValidatorRegistry);
    }

    @PostConstruct
    public void registerDomainValidator() {
        this.eValidatorRegistry.put(SysmlPackage.eINSTANCE, new SysONSysMLValidator(this.validationRulesProvider));
    }
}
