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
package org.eclipse.syson;

import org.eclipse.syson.application.configuration.SysONLoadDefaultLibrariesOnApplicationStartConfiguration;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Test configuration to not load KerML and SysML default libraries on server start.
 *
 * @author arichard
 */
@Configuration
@Primary
@Conditional(OnNoDefaultLibrariesTests.class)
public class SysONNoLoadDefaultLibrariesOnApplicationStartTestConfiguration extends SysONLoadDefaultLibrariesOnApplicationStartConfiguration {

    @Override
    public boolean mustLoadDefaultLibrariesOnStart() {
        return false;
    }
}
