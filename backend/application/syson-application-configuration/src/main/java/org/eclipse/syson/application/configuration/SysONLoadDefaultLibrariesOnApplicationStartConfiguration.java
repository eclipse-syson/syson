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
package org.eclipse.syson.application.configuration;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration to load KerML and SysML default libraries on server start.
 *
 * @author arichard
 */
@Configuration
public class SysONLoadDefaultLibrariesOnApplicationStartConfiguration {

    public boolean mustLoadDefaultLibrariesOnStart() {
        return true;
    }
}
