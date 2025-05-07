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
package org.eclipse.syson.application.libraries;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.syson.application.libraries.imports.SysONLibraryImportTests;
import org.eclipse.syson.application.libraries.update.SysONLibraryUpdateTests;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

/**
 * Annotation to setup the SysON server for testing the import of a published library.
 *
 * @see SysONLibraryImportTests
 * @see SysONLibraryUpdateTests
 * @author flatombe
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@SqlGroup({
    @Sql(scripts = {
        "/scripts/database-content/syson-test-database-import.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
    @Sql(scripts = {
        "/scripts/cleanup.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
})
public @interface SysONLibraryImportTestServer {

}
