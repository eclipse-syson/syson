/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.provider.SysmlItemProviderAdapterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration of the EMF support for SysON (i.e. SysML metamodel).
 *
 * @author arichard
 */
@Configuration
public class SysMLv2EMFConfiguration {
    @Bean
    AdapterFactory sysmlAdapterFactory() {
        return new SysmlItemProviderAdapterFactory();
    }

    @Bean
    EPackage sysmlEPackage() {
        return SysmlPackage.eINSTANCE;
    }
}
