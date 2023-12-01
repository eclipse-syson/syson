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

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuration of JPA.
 *
 * @author sbegaudeau
 */
@Configuration
@EntityScan(basePackages = { "org.eclipse.sirius.web.persistence.entities" })
@EnableJpaRepositories(basePackages = { "org.eclipse.sirius.web.persistence.repositories" })
public class JPAConfiguration {

}
