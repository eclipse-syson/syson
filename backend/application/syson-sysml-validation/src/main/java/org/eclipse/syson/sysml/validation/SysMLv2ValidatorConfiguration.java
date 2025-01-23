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

import org.eclipse.emf.ecore.EValidator.Registry;
import org.eclipse.syson.sysml.SysmlPackage;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Configuration of the SysMLv2 Validator.
 *
 * @author arichard
 */
@Configuration
public class SysMLv2ValidatorConfiguration {

    private final Registry eValidatorRegistry;

    public SysMLv2ValidatorConfiguration(Registry eValidatorRegistry) {
        this.eValidatorRegistry = Objects.requireNonNull(eValidatorRegistry);
    }

    @PostConstruct
    public void registerDomainValidator() {
        this.eValidatorRegistry.put(SysmlPackage.eINSTANCE, new SysMLv2Validator());
    }
}
