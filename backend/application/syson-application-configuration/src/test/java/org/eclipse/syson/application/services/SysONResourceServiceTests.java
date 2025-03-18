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
package org.eclipse.syson.application.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link SysONResourceService}.
 *
 * @author flatombe
 */
public class SysONResourceServiceTests {

    private final ISysONResourceService sysONResourceService = new SysONResourceService();

    private Resource emptyResource;

    private Resource resourceWithLibraryMetadata;

    private Resource resourceWithImportedElement;

    @BeforeEach
    public void setUpResources() {
        this.emptyResource = new ResourceImpl();

        this.resourceWithLibraryMetadata = new ResourceImpl();
        this.resourceWithLibraryMetadata.eAdapters().add(new LibraryMetadataAdapter("namespace", "name", "version"));

        this.resourceWithImportedElement = new ResourceImpl();
        final EObject importedElement = SysmlPackage.eINSTANCE.getSysmlFactory().createNamespace();
        this.resourceWithImportedElement.getContents().add(importedElement);
        ElementUtil.setIsImported(this.resourceWithImportedElement, true);
    }

    @Test
    public void testIsImported() {
        assertThat(this.sysONResourceService.isImported(this.emptyResource)).isFalse();
        assertThat(this.sysONResourceService.isImported(this.resourceWithLibraryMetadata)).isTrue();
        assertThat(this.sysONResourceService.isImported(this.resourceWithImportedElement)).isTrue();
    }

    @Test
    public void testIsFromReferencedLibrary() {
        assertThat(this.sysONResourceService.isFromReferencedLibrary(this.emptyResource)).isFalse();
        assertThat(this.sysONResourceService.isFromReferencedLibrary(this.resourceWithLibraryMetadata)).isTrue();
        assertThat(this.sysONResourceService.isFromReferencedLibrary(this.resourceWithImportedElement)).isFalse();
    }

}
