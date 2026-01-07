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
package org.eclipse.syson.tree.explorer.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.emfjson.resource.JsonResourceFactoryImpl;
import org.eclipse.syson.application.services.SysONResourceService;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerFilterService;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link SysONExplorerFilterService} class.
 *
 * @author gdaniel
 */
public class SysONExplorerFilterServiceTest {

    private final ISysONResourceService sysONResourceService = new SysONResourceService(new LibrarySearchServiceNoOp());

    private final ISysONExplorerFilterService filterService = new SysONExplorerFilterService(this.sysONResourceService);

    private final Resource.Factory resourceFactory = new JsonResourceFactoryImpl();

    @Test
    public void isKerMLStandardLibraryNotResource() {
        assertThat(this.filterService.isKerMLStandardLibrary(SysmlFactory.eINSTANCE.createPartUsage())).isFalse();
    }

    @Test
    public void isKerMLStandardLibraryNotKerMLResource() {
        Resource resource = this.resourceFactory.createResource(URI.createURI("test"));
        assertThat(this.filterService.isKerMLStandardLibrary(resource)).isFalse();
    }

    @Test
    public void isKerMLStandardLibraryKerMLResource() {
        Resource resource = this.createKerMLResource();
        assertThat(this.filterService.isKerMLStandardLibrary(resource)).isTrue();
    }

    @Test
    public void isSysMLStandardLibraryNotResource() {
        assertThat(this.filterService.isSysMLStandardLibrary(SysmlFactory.eINSTANCE.createPartUsage())).isFalse();
    }

    @Test
    public void isSysMLStandardLibraryNotSysMLResource() {
        Resource resource = this.resourceFactory.createResource(URI.createURI("test"));
        assertThat(this.filterService.isSysMLStandardLibrary(resource)).isFalse();
    }

    @Test
    public void isSysMLStandardLibrarySysMLResource() {
        Resource resource = this.createSysMLResource();
        assertThat(this.filterService.isSysMLStandardLibrary(resource)).isTrue();
    }

    @Test
    public void isEmptyImportedDocumentNotConsideredUserLlibrary() {
        Resource resource = this.createSysMLResource();
        var namespace = SysmlFactory.eINSTANCE.createNamespace();
        var importedAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
        importedAnnotation.setSource("org.eclipse.syson.sysml.imported");
        namespace.getEAnnotations().add(importedAnnotation);
        resource.getContents().add(namespace);
        assertThat(this.filterService.isUserLibrary(new IEditingContext.NoOp(), resource)).isFalse();
    }

    @Test
    public void hideKerMLStandardLibraries() {
        Resource kerMLResource = this.createKerMLResource();
        PartUsage partUsage = SysmlFactory.eINSTANCE.createPartUsage();
        Resource resource = this.resourceFactory.createResource(URI.createURI("test"));
        List<Object> initialElements = List.of(kerMLResource, partUsage, resource);
        List<Object> filteredElements = this.filterService.hideKerMLStandardLibraries(initialElements);
        assertThat(filteredElements).hasSize(2);
        assertThat(filteredElements.get(0)).isEqualTo(partUsage);
        assertThat(filteredElements.get(1)).isEqualTo(resource);
    }

    @Test
    public void hideSysMLStandardLibraries() {
        Resource sysMLResource = this.createSysMLResource();
        PartUsage partUsage = SysmlFactory.eINSTANCE.createPartUsage();
        Resource resource = this.resourceFactory.createResource(URI.createURI("test"));
        List<Object> initialElements = List.of(sysMLResource, partUsage, resource);
        List<Object> filteredElements = this.filterService.hideSysMLStandardLibraries(initialElements);
        assertThat(filteredElements).hasSize(2);
        assertThat(filteredElements.get(0)).isEqualTo(partUsage);
        assertThat(filteredElements.get(1)).isEqualTo(resource);
    }

    @Test
    public void hideMemberships() {
        Membership membership = SysmlFactory.eINSTANCE.createOwningMembership();
        PartUsage partUsage = SysmlFactory.eINSTANCE.createPartUsage();
        membership.getOwnedRelatedElement().add(partUsage);
        ActionUsage actionUsage = SysmlFactory.eINSTANCE.createActionUsage();
        List<Object> initialElements = List.of(membership, actionUsage);
        List<Object> filteredElements = this.filterService.hideMemberships(initialElements);
        assertThat(filteredElements).hasSize(2);
        assertThat(filteredElements.get(0)).isEqualTo(partUsage);
        assertThat(filteredElements.get(1)).isEqualTo(actionUsage);
    }

    @Test
    public void hideRootNamespace() {
        Namespace rootNamespace = SysmlFactory.eINSTANCE.createNamespace();
        PartUsage partUsage = SysmlFactory.eINSTANCE.createPartUsage();
        Membership membership = SysmlFactory.eINSTANCE.createOwningMembership();
        membership.getOwnedRelatedElement().add(partUsage);
        rootNamespace.getOwnedRelationship().add(membership);
        List<Object> initialElements = List.of(rootNamespace);
        List<Object> filteredElements = this.filterService.hideRootNamespace(initialElements);
        assertThat(filteredElements).hasSize(1);
        assertThat(filteredElements.get(0)).isEqualTo(partUsage);
    }

    /**
     * Check that a non-imported resource can never be a user library.
     */
    @Test
    public void isUserLibraryNotImportedResource() {
        Resource resource = new ResourceImpl();
        assertThat(this.filterService.isUserLibrary(new IEditingContext.NoOp(), resource)).isFalse();
    }

    private Resource createKerMLResource() {
        return this.resourceFactory.createResource(URI.createURI(ElementUtil.KERML_LIBRARY_SCHEME + ":///test"));
    }

    private Resource createSysMLResource() {
        return this.resourceFactory.createResource(URI.createURI(ElementUtil.SYSML_LIBRARY_SCHEME + ":///test"));
    }

}
