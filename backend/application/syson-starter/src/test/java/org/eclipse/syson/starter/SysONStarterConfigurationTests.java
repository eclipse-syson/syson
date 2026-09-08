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
package org.eclipse.syson.starter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.annotation.ComponentScan;

/**
 * Tests for the SysON starter auto-configuration contract.
 *
 * @author arichard
 */
public class SysONStarterConfigurationTests {

    /**
     * Verifies that Spring Boot discovers the SysON starter configuration.
     */
    @Test
    public void shouldRegisterAutoConfiguration() {
        var importCandidates = ImportCandidates.load(AutoConfiguration.class, SysONStarterConfiguration.class.getClassLoader());

        assertThat(importCandidates.getCandidates()).contains(SysONStarterConfiguration.class.getName());
    }

    /**
     * Verifies that the starter scans SysON components.
     */
    @Test
    public void shouldScanSysONComponents() {
        var componentScan = SysONStarterConfiguration.class.getAnnotation(ComponentScan.class);

        assertThat(componentScan.basePackages()).containsExactly("org.eclipse.syson");
    }
}
