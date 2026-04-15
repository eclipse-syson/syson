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
package org.eclipse.syson.application.configuration;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextProcessor;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.application.studio.services.api.IStudioCapableEditingContextPredicate;
import org.eclipse.syson.application.services.api.IStandardLibrariesLoader;
import org.eclipse.syson.sysml.util.LibraryNamespaceProvider;
import org.eclipse.syson.util.SysONEContentAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * {@link IEditingContextProcessor} for SysML libraries. All SysML default libraries should be loaded when a project
 * (i.e. an editing context) is loaded.
 *
 * @author arichard
 */
@Service
public class SysMLEditingContextProcessor implements IEditingContextProcessor {

    private final Logger logger = LoggerFactory.getLogger(SysMLEditingContextProcessor.class);

    private final IStudioCapableEditingContextPredicate studioCapableEditingContextPredicate;

    private final IStandardLibrariesLoader standardLibrariesLoader;

    public SysMLEditingContextProcessor(IStandardLibrariesLoader standardLibrariesLoader, IStudioCapableEditingContextPredicate studioCapableEditingContextPredicate) {
        this.standardLibrariesLoader = Objects.requireNonNull(standardLibrariesLoader);
        this.studioCapableEditingContextPredicate = Objects.requireNonNull(studioCapableEditingContextPredicate);
    }

    @Override
    public void preProcess(IEditingContext editingContext) {
        // Do not initialize the editing context as a SysON editing context if it contains a studio.
        if (editingContext instanceof IEMFEditingContext siriusWebEditingContext
                && !this.studioCapableEditingContextPredicate.test(editingContext.getId())) {

            Instant start = Instant.now();
            ResourceSet targetResourceSet = siriusWebEditingContext.getDomain().getResourceSet();
            targetResourceSet.eAdapters().add(new SysONEContentAdapter());

            // Register an adapter to ease the access to Namespaces using their full qualified name stored in Libraries
            LibraryNamespaceProvider libraryNamespaceProvider = new LibraryNamespaceProvider(targetResourceSet);
            targetResourceSet.eAdapters().add(libraryNamespaceProvider);

            this.standardLibrariesLoader.loadStandardLibraries(targetResourceSet);

            // At this point only immutable PackageLibraries are present in the ResourceSet so can we register them as
            // immutable in the LibraryNamespaceProvider
            for (Resource r : targetResourceSet.getResources()) {
                libraryNamespaceProvider.addImmutableLibrariesNamespaces(r);
            }

            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toMillis();
            this.logger.info("Copied all default libraries in the editing context in {} ms", timeElapsed);
        }
    }
}
