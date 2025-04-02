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

/**
 * The entry point of all the SysON test configuration properties.
 *
 * @author arichard
 */
public class SysONTestsProperties {

    public static final String SYSON_TEST_ENABLED = "syson.test.enabled";

    public static final String NO_DEFAULT_LIBRARIES = "no-default-libraries";

    public static final String NO_DEFAULT_LIBRARIES_PROPERTY = SYSON_TEST_ENABLED + "=" + NO_DEFAULT_LIBRARIES;

}
