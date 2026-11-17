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
package org.eclipse.syson.sysml.parser.tests;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests rule conjugatedPortDefinition.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class ConjugatedPortDefinitionTests extends AbstractSysMLRuleTestSuite {

    /**
     * Constructor.
     *
     * @param testFolder
     *            the test folder
     * @throws Exception
     *             if something went wrong
     */
    public ConjugatedPortDefinitionTests(String testFolder) throws Exception {
        super(testFolder);
    }

    @Override
    protected ISysMLParseResult<?> parse(SysMLModelParser parser, InputStream is, Charset charset) throws IOException {
        return parser.parseConjugatedPortDefinition(is, charset);
    }

    /**
     * Gets test folders from resources/feature.
     *
     * @return test folders from resources/feature
     */
    @Parameters(name = "{0}")
    public static Collection<Object[]> retrieveTestFolders() {
        return retrieveTestFolders("src/test/resources/conjugatedPortDefinition");
    }

}
