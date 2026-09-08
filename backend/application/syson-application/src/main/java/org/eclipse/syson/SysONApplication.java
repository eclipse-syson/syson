/*******************************************************************************
 * Copyright (c) 2023, 2026 Obeo.
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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Main class of the server, used as the entry point which will start the whole server properly initialized with a
 * Spring ApplicationContext (see {@link org.springframework.context.ApplicationContext}).
 * <p>
 * Thanks to the annotations {@link SpringBootConfiguration} and {@link EnableAutoConfiguration}, this class will act as a configuration which allows us to
 * declare beans and configure other features but we will not use this capacity in order to properly separate our code.
 * As such our configurations will be contained in dedicated classes elsewhere.
 * </p>
 * <p>
 * We do not use {@code SpringBootApplication} because it would also scan the {@code org.eclipse.syson} package. The
 * {@code syson-starter} dependency performs that scan, so downstream applications and this executable application use
 * the same SysON component configuration.
 * </p>
 *
 * @author arichard
 */
@SpringBootConfiguration
@EnableAutoConfiguration
public class SysONApplication {

    /**
     * The entry point of the server.
     *
     * @param args
     *            The command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SysONApplication.class, args);
    }
}
