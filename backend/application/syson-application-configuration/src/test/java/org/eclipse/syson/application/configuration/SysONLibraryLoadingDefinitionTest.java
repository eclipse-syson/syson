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
package org.eclipse.syson.application.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.function.Function;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.syson.application.libraries.SysONLibraryLoadingDefinition;
import org.junit.jupiter.api.Test;

/**
 * Test about the library loading definition.
 *
 * @author frouene
 */
public class SysONLibraryLoadingDefinitionTest {

    private static final String VALID_FAMILY_NAME = "TestLibrary";
    private static final String VALID_SCHEME = "test";
    private static final String VALID_RELATIVE_PATH = "name/library";
    private static final List<String> VALID_EXTENSIONS = List.of("json", "xml");
    private static final Function<URI, Resource> VALID_LOADING_BEHAVIOR = mock(Function.class);

    @Test
    void testValidDefinition() {
        SysONLibraryLoadingDefinition definition = new SysONLibraryLoadingDefinition(
                VALID_FAMILY_NAME,
                VALID_SCHEME,
                VALID_RELATIVE_PATH,
                VALID_EXTENSIONS,
                VALID_LOADING_BEHAVIOR
        );

        assertNotNull(definition);
        assertEquals(VALID_FAMILY_NAME, definition.familyName());
        assertEquals(VALID_SCHEME, definition.scheme());
        assertEquals(VALID_RELATIVE_PATH, definition.relativePathToDirectoryContainingLibraryFiles());
        assertEquals(VALID_EXTENSIONS, definition.fileExtensions());
        assertEquals(VALID_LOADING_BEHAVIOR, definition.resourceLoadingBehavior());
    }

    @Test
    void testAbsolutePath() {
        assertThrows(IllegalArgumentException.class, () ->
                new SysONLibraryLoadingDefinition(
                        VALID_FAMILY_NAME,
                        VALID_SCHEME,
                        "/name/library",
                        VALID_EXTENSIONS,
                        VALID_LOADING_BEHAVIOR
                )
        );
    }

    @Test
    void testWindowsSeparators() {
        assertThrows(IllegalArgumentException.class, () ->
                new SysONLibraryLoadingDefinition(
                        VALID_FAMILY_NAME,
                        VALID_SCHEME,
                        "name\\library",
                        VALID_EXTENSIONS,
                        VALID_LOADING_BEHAVIOR
                )
        );
    }

}
