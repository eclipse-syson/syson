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
package org.eclipse.syson.representation.services;

import com.tngtech.archunit.core.domain.JavaClasses;

import org.eclipse.syson.tests.architecture.AbstractCodingRulesTests;

/**
 * Coding rules tests.
 *
 * @author arichard
 */
public class CodingRulesTests extends AbstractCodingRulesTests {

    @Override
    protected String getProjectRootPackage() {
        return ArchitectureConstants.SYSON_REPRESENTATION_SERVICES_ROOT_PACKAGE;
    }

    @Override
    protected JavaClasses getClasses() {
        return ArchitectureConstants.CLASSES;
    }
}
