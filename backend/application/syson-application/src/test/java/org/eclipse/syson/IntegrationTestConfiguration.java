/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
 * Configuration of the application used during integration tests. SysON components are discovered through the
 * {@code syson-starter} auto-configuration, while Sirius Web test fixtures in {@code org.eclipse.sirius.web.tests}
 * are discovered by the component scan.
 *
 * @author sbegaudeau
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "org.eclipse.sirius.web.tests")
public class IntegrationTestConfiguration {
    @Bean
    EPackage ecorePackage() {
        return EcorePackage.eINSTANCE;
    }
}
