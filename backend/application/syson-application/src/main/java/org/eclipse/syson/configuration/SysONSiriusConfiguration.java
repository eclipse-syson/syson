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
package org.eclipse.syson.configuration;

import java.util.List;

import org.eclipse.sirius.components.compatibility.services.api.ISiriusConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration of the Sirius compatibility layer.
 *
 * @author arichard
 */
@Configuration
public class SysONSiriusConfiguration implements ISiriusConfiguration {

    @Override
    public List<String> getODesignPaths() {
        return List.of();
    }

    @Bean
    @ConditionalOnProperty(prefix = "org.eclipse.sirius.web.features", name = "studioDefinition")
    ISiriusConfiguration domainModelerDefinition() {
        return () -> List.of("description/domain.odesign");
    }
}
