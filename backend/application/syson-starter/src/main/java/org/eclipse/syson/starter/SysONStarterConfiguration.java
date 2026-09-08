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

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Auto-configuration of SysON components. Sirius Web components are configured by the {@code sirius-web-starter}
 * dependency, so this configuration only scans the {@code org.eclipse.syson} package.
 *
 * @author arichard
 */
@AutoConfiguration
@ComponentScan(basePackages = "org.eclipse.syson")
public class SysONStarterConfiguration {
}
