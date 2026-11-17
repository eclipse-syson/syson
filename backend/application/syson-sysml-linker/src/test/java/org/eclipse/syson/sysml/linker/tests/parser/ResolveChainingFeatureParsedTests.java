/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.sysml.linker.tests.parser;

import java.io.IOException;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

/**
 * Parser-backed linker resolution tests.
 *
 * @author Arthur Daussy
 */
public class ResolveChainingFeatureParsedTests extends AbstractParsedResolveTestSuite {

    /**
     * Constructor.
     *
     * @param testFolder
     *            the test folder
     * @throws IOException
     *             if test files can't be read
     */
    public ResolveChainingFeatureParsedTests(String testFolder) throws IOException {
        super(testFolder);
    }

    /**
     * Gets test folders from resources/feature.
     *
     * @return test folders from resources/feature
     */
    @Parameters(name = "{0}")
    public static Collection<Object[]> retrieveTestFolders() {
        return retrieveTestFolders("src/test/resources/org/eclipse/syson/sysml/linker/resolveChainingFeature");
    }

}
