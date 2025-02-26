/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreAdapterFactory;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.syson.application.libraries.SysONLibraryLoader;
import org.eclipse.syson.util.SysONEContentAdapter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * Tests about loading of SysML Standard libraries.
 *
 * @author arichard
 */
public class SysMLEditingContextProcessorTest {

    private static ResourceSet resourceSet;

    @BeforeAll
    static void loadLibraries() {
        ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory();
        composedAdapterFactory.addAdapterFactory(new EcoreAdapterFactory());
        composedAdapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

        EPackage.Registry ePackageRegistry = new EPackageRegistryImpl();
        ePackageRegistry.put(EcorePackage.eINSTANCE.getNsURI(), EcorePackage.eINSTANCE);

        AdapterFactoryEditingDomain editingDomain = new AdapterFactoryEditingDomain(composedAdapterFactory, new BasicCommandStack());
        resourceSet = editingDomain.getResourceSet();
        resourceSet.setPackageRegistry(ePackageRegistry);
        resourceSet.eAdapters().add(new ECrossReferenceAdapter());
        EditingContext editingContext = new EditingContext(UUID.randomUUID().toString(), editingDomain, Map.of(), List.of());
        SysMLEditingContextProcessor editingContextProcessor = new SysMLEditingContextProcessor(new SysONDefaultLibrariesConfiguration(new SysONLoadDefaultLibrariesOnApplicationStartConfiguration()),
                e -> false);
        editingContextProcessor.preProcess(editingContext);
        assertNotNull(resourceSet);
    }

    @Test
    void loadKerMLLibraries() {
        final SysONLibraryLoader sysonLibraryLoader = new SysONLibraryLoader();
        for (final ClassPathResource classPathResource : sysonLibraryLoader.findAllLibraryResourcesFromApplicationClasspath(SysONDefaultLibrariesConfiguration.KERML)) {
            final URI uri = sysonLibraryLoader.createEmfUriFor(classPathResource, SysONDefaultLibrariesConfiguration.KERML);
            final Resource emfResource = resourceSet.getResource(uri, false);
            assertNotNull(emfResource, "Unable to load %s resource: %s".formatted(SysONDefaultLibrariesConfiguration.KERML.familyName(), classPathResource.getFilename()));

            final List<ResourceMetadataAdapter> resourceMetadataAdapters = emfResource.eAdapters().stream().filter(ResourceMetadataAdapter.class::isInstance).map(ResourceMetadataAdapter.class::cast)
                    .toList();
            assertThat(resourceMetadataAdapters).hasSize(1);
            assertThat(resourceMetadataAdapters.get(0).getName()).isEqualTo(FilenameUtils.getBaseName(classPathResource.getFilename()));
        }
    }

    @Test
    void loadSysMLLibraries() {
        final SysONLibraryLoader sysonLibraryLoader = new SysONLibraryLoader();
        for (final ClassPathResource classPathResource : sysonLibraryLoader.findAllLibraryResourcesFromApplicationClasspath(SysONDefaultLibrariesConfiguration.SYSML)) {
            final URI uri = sysonLibraryLoader.createEmfUriFor(classPathResource, SysONDefaultLibrariesConfiguration.SYSML);
            final Resource emfResource = resourceSet.getResource(uri, false);
            assertNotNull(emfResource, "Unable to load %s resource: %s".formatted(SysONDefaultLibrariesConfiguration.SYSML.familyName(), classPathResource.getFilename()));

            final List<ResourceMetadataAdapter> resourceMetadataAdapters = emfResource.eAdapters().stream().filter(ResourceMetadataAdapter.class::isInstance).map(ResourceMetadataAdapter.class::cast)
                    .toList();
            assertThat(resourceMetadataAdapters).hasSize(1);
            assertThat(resourceMetadataAdapters.get(0).getName()).isEqualTo(FilenameUtils.getBaseName(classPathResource.getFilename()));
        }
    }

    @Test
    public void preProcessStudioEditingContext() {
        ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory();
        composedAdapterFactory.addAdapterFactory(new EcoreAdapterFactory());
        composedAdapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

        EPackage.Registry ePackageRegistry = new EPackageRegistryImpl();
        ePackageRegistry.put(EcorePackage.eINSTANCE.getNsURI(), EcorePackage.eINSTANCE);

        AdapterFactoryEditingDomain editingDomain = new AdapterFactoryEditingDomain(composedAdapterFactory, new BasicCommandStack());
        ResourceSet rSet = editingDomain.getResourceSet();
        rSet.setPackageRegistry(ePackageRegistry);
        rSet.eAdapters().add(new ECrossReferenceAdapter());
        EditingContext editingContext = new EditingContext(UUID.randomUUID().toString(), editingDomain, Map.of(), List.of());
        SysMLEditingContextProcessor editingContextProcessor = new SysMLEditingContextProcessor(new SysONDefaultLibrariesConfiguration(new SysONLoadDefaultLibrariesOnApplicationStartConfiguration()),
                e -> true);
        editingContextProcessor.preProcess(editingContext);

        assertThat(editingContext.getDomain().getResourceSet().eAdapters()).noneMatch(adapter -> adapter instanceof SysONEContentAdapter);
    }
}
