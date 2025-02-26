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

import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.emfjson.resource.JsonResourceFactoryImpl;
import org.eclipse.syson.application.libraries.SysONLibraryLoader;
import org.eclipse.syson.application.libraries.SysONLibraryLoadingDefinition;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration to load KerML and SysML default libraries.
 *
 * @author arichard
 */
@Configuration
public class SysONDefaultLibrariesConfiguration {

    public static final SysONLibraryLoadingDefinition KERML = new SysONLibraryLoadingDefinition(
            "KerML Standard Library",
            ElementUtil.KERML_LIBRARY_SCHEME,
            Paths.get("kerml.libraries"),
            Collections.singletonList(JsonResourceFactoryImpl.EXTENSION),
            new JSONResourceFactory()::createResource);

    public static final SysONLibraryLoadingDefinition SYSML = new SysONLibraryLoadingDefinition(
            "SysML Standard Library",
            ElementUtil.SYSML_LIBRARY_SCHEME,
            Paths.get("sysml.libraries"),
            Collections.singletonList(JsonResourceFactoryImpl.EXTENSION),
            new JSONResourceFactory()::createResource);

    private static final Logger LOGGER = LoggerFactory.getLogger(SysONDefaultLibrariesConfiguration.class);

    private ResourceSet librariesResourceSet;

    public SysONDefaultLibrariesConfiguration(SysONLoadDefaultLibrariesOnApplicationStartConfiguration startConfiguration) {
        if (startConfiguration.mustLoadDefaultLibrariesOnStart()) {
            this.getLibrariesResourceSet();
        }
    }

    public ResourceSet getLibrariesResourceSet() {
        if (this.librariesResourceSet == null) {
            this.librariesResourceSet = this.createAndInitializeResourceSetWithLibraries();
        }
        return this.librariesResourceSet;
    }

    protected ResourceSet createAndInitializeResourceSetWithLibraries() {
        Instant start = Instant.now();

        final EPackage.Registry ePackageRegistry = new EPackageRegistryImpl();
        final ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.setPackageRegistry(ePackageRegistry);

        this.populateWithDefaultEPackages(ePackageRegistry);
        this.populateWithDefaultLibraries(resourceSet);

        EMFUtils.resolveAllNonDerived(resourceSet);

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        LOGGER.info("Initialization of default libraries completed in %sms".formatted(timeElapsed));

        return resourceSet;
    }

    /**
     * Provides the default libraries. The default libraries are made available to all SysML projects of the
     * application.
     *
     * Override this to customize the standard libraries or add extra default libraries to all SysML projects.
     *
     * @return a (non-{@code null}) {@link List} of (non-{@code null}) {@link SysONLibraryLoadingDefinition}.
     */
    protected List<SysONLibraryLoadingDefinition> getDefaultLibraries() {
        return Stream.of(KERML, SYSML).toList();
    }

    /**
     * Provides the default metamodels to register.
     *
     * @return a (non-{@code null}) {@link List} of (non-{@code null}) {@link EPackage}.
     */
    protected List<EPackage> getDefaultEPackages() {
        return Collections.singletonList(SysmlPackage.eINSTANCE);
    }

    protected void populateWithDefaultLibraries(final ResourceSet resourceSet) {
        final SysONLibraryLoader sysonLibraryLoader = new SysONLibraryLoader();
        for (final SysONLibraryLoadingDefinition libraryLoadingDefinition : this.getDefaultLibraries()) {
            sysonLibraryLoader.loadLibraryResources(resourceSet, libraryLoadingDefinition);
        }
    }

    protected void populateWithDefaultEPackages(final EPackage.Registry ePackageRegistry) {
        for (final EPackage ePackage : this.getDefaultEPackages()) {
            ePackageRegistry.put(ePackage.getNsURI(), ePackage);
            LOGGER.info("Registered EPackage '%s' (prefix '%s') against URI '%s'".formatted(ePackage.getName(), ePackage.getNsPrefix(), ePackage.getNsURI()));
        }
    }
}
