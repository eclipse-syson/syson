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
package org.eclipse.syson.application.data;

import org.eclipse.syson.application.libraries.SysONLibraryImportTestServer;

/**
 * Identifiers for the "ProjectUsingMyLibraryV1" project.
 * <p>
 * Tests relying on this test data should use the {@link SysONLibraryImportTestServer} annotation to ensure the project is loaded in the application.
 * </p>
 *
 * @author gdaniel
 */
public class ProjectWithLibraryDependencyTestProjectData {

    public static final String PROJECT_NAME = "ProjectUsingMyLibraryV1";

    public static final String LIBRARY_PROJECT_ID = "c493d950-f81a-4e56-a454-f9e5ce1c839f";

    public static final String EDITING_CONTEXT = "09ba2e1b-7f2b-4c5f-a536-9624ba470f74";

}
