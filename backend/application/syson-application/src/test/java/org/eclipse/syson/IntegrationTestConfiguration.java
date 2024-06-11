/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration of the application used during the integration tests.
 *
 * @author sbegaudeau
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "org.eclipse.syson", "org.eclipse.sirius.web" })
public class IntegrationTestConfiguration {
    @Bean
    EPackage ecorePackage() {
        return EcorePackage.eINSTANCE;
    }
}
