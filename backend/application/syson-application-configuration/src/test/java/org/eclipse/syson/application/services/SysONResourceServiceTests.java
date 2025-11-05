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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.LibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

/**
 * Unit tests for {@link SysONResourceService}.
 *
 * @author flatombe
 */
public class SysONResourceServiceTests {

    private final IEditingContext editingContext = new MockEditingContext();

    private final ILibrarySearchService librarySearchService = new MockLibrarySearchService();

    private final ISysONResourceService sysONResourceService = new SysONResourceService(this.librarySearchService);

    private Resource emptyResource;

    private Resource resourceWithLibraryMetadata;

    private Resource resourceWithImportedElement;

    private Resource resourceWithLibraryMetadataMatchingEditingContext;

    @BeforeEach
    public void setUpResources() {
        this.emptyResource = new ResourceImpl();

        this.resourceWithLibraryMetadata = new ResourceImpl();
        this.resourceWithLibraryMetadata.eAdapters().add(new LibraryMetadataAdapter("namespace", "name", "version"));

        this.resourceWithLibraryMetadataMatchingEditingContext = new ResourceImpl();
        this.resourceWithLibraryMetadataMatchingEditingContext.eAdapters().add(new LibraryMetadataAdapter("testNamespace", "testName", "testVersion"));

        this.resourceWithImportedElement = new ResourceImpl();
        final EObject importedElement = SysmlPackage.eINSTANCE.getSysmlFactory().createNamespace();
        this.resourceWithImportedElement.getContents().add(importedElement);
        ElementUtil.setIsImported(this.resourceWithImportedElement, true);
    }

    @Test
    public void testIsImported() {
        assertThat(this.sysONResourceService.isImported(this.editingContext, this.emptyResource)).isFalse();
        assertThat(this.sysONResourceService.isImported(this.editingContext, this.resourceWithLibraryMetadata)).isTrue();
        assertThat(this.sysONResourceService.isImported(this.editingContext, this.resourceWithImportedElement)).isTrue();
    }

    @Test
    public void testIsFromReferencedLibrary() {
        assertThat(this.sysONResourceService.isFromReferencedLibrary(this.editingContext, this.emptyResource)).isFalse();
        assertThat(this.sysONResourceService.isFromReferencedLibrary(this.editingContext, this.resourceWithLibraryMetadata)).isTrue();
        assertThat(this.sysONResourceService.isFromReferencedLibrary(this.editingContext, this.resourceWithImportedElement)).isFalse();
        assertThat(this.sysONResourceService.isFromReferencedLibrary(this.editingContext, this.resourceWithLibraryMetadataMatchingEditingContext)).isFalse();
    }

    /**
     * A mock editing context used to test {@link SysONResourceService}
     *
     * @author gdaniel
     */
    private static final class MockEditingContext implements IEditingContext {

        @Override
        public String getId() {
            return UUID.nameUUIDFromBytes("mockEditingContext".getBytes()).toString();
        }

    }

    /**
     * A mock {@link LibrarySearchService} used to test referenced libraries from the current editing context.
     *
     * @author gdaniel
     */
    private static final class MockLibrarySearchService implements ILibrarySearchService {

        @Override
        public Page<Library> findAll(Pageable pageable) {
            return null;
        }

        @Override
        public boolean existsByNamespaceAndNameAndVersion(String namespace, String name, String version) {
            return false;
        }

        @Override
        public Optional<Library> findByNamespaceAndNameAndVersion(String namespace, String name, String version) {
            return Optional.empty();
        }

        @Override
        public Optional<Library> findBySemanticData(AggregateReference<SemanticData, UUID> semanticData) {
            return Optional.of(Library.newLibrary()
                    .namespace("testNamespace")
                    .name("testName")
                    .version("testVersion")
                    .description("")
                    .semanticData(AggregateReference.to(UUID.nameUUIDFromBytes("mockEditingContext".getBytes())))
                    .build(null));
        }

        @Override
        public Page<Library> findAllByNamespaceAndName(String namespace, String name, Pageable pageable) {
            return null;
        }

        @Override
        public List<Library> findAllById(Iterable<UUID> ids) {
            return null;
        }

        @Override
        public Optional<Library> findById(UUID id) {
            return Optional.empty();
        }

    }

}
