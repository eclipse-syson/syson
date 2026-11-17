/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.sysml;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.syson.sysml.services.SysMLParsingService;
import org.eclipse.syson.sysml.upload.SysMLExternalResourceLoaderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link SysMLExternalResourceLoaderService}.
 *
 * @author Arthur Daussy
 */
public class SysMLExternalResourceLoaderServiceTests {

    private static final String PACKAGE_P1 = "package p1;";

    /**
     * Checks supported textual extensions and rejects JSON content before invoking the parser.
     */
    @DisplayName("GIVEN uploaded content and an extension, WHEN checking support, THEN only textual SysML and KerML resources are accepted")
    @Test
    public void testCandHandle() {
        var loader = new SysMLExternalResourceLoaderService(new SysMLParsingService());
        // Incorrect content
        assertFalse(loader.canHandle(new ByteArrayInputStream("{}".getBytes()), URI.createFileURI("/test/model.sysml"), new ResourceSetImpl()));
        // Incorrect extension
        assertFalse(loader.canHandle(new ByteArrayInputStream(PACKAGE_P1.getBytes()), URI.createFileURI("/test/model.sysml2"),
                new ResourceSetImpl()));
        // Valid file
        assertTrue(loader.canHandle(new ByteArrayInputStream(PACKAGE_P1.getBytes()), URI.createFileURI("/test/model.sysml"),
                new ResourceSetImpl()));
        // Valid file
        assertTrue(loader.canHandle(new ByteArrayInputStream(PACKAGE_P1.getBytes()), URI.createFileURI("/test/model.kerml"),
                new ResourceSetImpl()));
        // Valid file
        assertTrue(loader.canHandle(new ByteArrayInputStream(PACKAGE_P1.getBytes()), URI.createFileURI("/test/model.SYSML"),
                new ResourceSetImpl()));
        // Valid file
        assertTrue(loader.canHandle(new ByteArrayInputStream(PACKAGE_P1.getBytes()), URI.createFileURI("/test/model.KERML"),
                new ResourceSetImpl()));
        // Valid file
        assertTrue(loader.canHandle(new ByteArrayInputStream(PACKAGE_P1.getBytes()), URI.createFileURI("/test/model.SysML"),
                new ResourceSetImpl()));
        // Valid file
        assertTrue(loader.canHandle(new ByteArrayInputStream(PACKAGE_P1.getBytes()), URI.createFileURI("/test/model.KerML"),
                new ResourceSetImpl()));
    }

}
