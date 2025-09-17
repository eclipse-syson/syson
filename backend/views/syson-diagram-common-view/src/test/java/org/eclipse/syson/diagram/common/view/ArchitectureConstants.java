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
package org.eclipse.syson.diagram.common.view;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;

/**
 * Constants shared across multiple tests.
 *
 * @author arichard
 */
public final class ArchitectureConstants {
    public static final String SYSON_DIAGRAM_COMMON_VIEW_ROOT_PACKAGE = "org.eclipse.syson.diagram.common.view..";

    public static final JavaClasses CLASSES = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(SYSON_DIAGRAM_COMMON_VIEW_ROOT_PACKAGE);

    private ArchitectureConstants() {
        // Prevent instantiation
    }
}
