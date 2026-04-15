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
package org.eclipse.syson.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.web.application.editingcontext.services.EditingContextAdapter;
import org.eclipse.syson.application.services.StandardLibrariesLoader;
import org.eclipse.syson.application.services.api.IStandardLibrariesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Loads cached SysML and KerML libraries into a resource set.
 * <p>
 * This service is intended to be used in a <i>testing</i> environment, and shouldn't be used in production. Caching standard libraries relies on the assumption that only one client accesses SysON in
 * linear, non-concurrent calls. This is clearly not the case in a regular deployment of SysON.
 * </p>
 * <p>
 * This class overrides SysON's default behavior by first looking for standard libraries in a cache, and delegate to the default implementation if there isn't anything in the cache for a given editing
 * context.
 * </p>
 * <p>
 * This service is used by the application if the property {@code org.eclipse.syson.test.cacheStandardLibraries} is set to {@code true}.
 * </p>
 *
 * @author gdaniel
 */
@Service
@ConditionalOnBooleanProperty("org.eclipse.syson.test.cacheStandardLibraries")
@Primary
public class CachedStandardLibrariesLoader implements IStandardLibrariesLoader {

    private final StandardLibrariesLoader standardLibrariesLoader;

    private final Cache<String, List<Resource>> standardLibrariesCache = Caffeine.newBuilder()
            .maximumSize(50)
            .build();

    private final Logger logger = LoggerFactory.getLogger(CachedStandardLibrariesLoader.class);

    public CachedStandardLibrariesLoader(StandardLibrariesLoader standardLibrariesLoader) {
        this.standardLibrariesLoader = Objects.requireNonNull(standardLibrariesLoader);
    }

    public void cache(String editingContextId, List<Resource> standardLibraryResources) {
        // Remove all the adapters from existing resources, this ensures that the list of adapters do not grow while resources are loaded from the cache and cached repeatedly.
        standardLibraryResources.forEach(resource -> resource.eAdapters().clear());
        this.standardLibrariesCache.put(editingContextId, standardLibraryResources);
    }

    public void invalidateCache() {
        this.standardLibrariesCache.invalidateAll();
    }

    @Override
    public void loadStandardLibraries(ResourceSet targetResourceSet) {
        Optional<String> optionalEditingContextId = targetResourceSet.eAdapters().stream()
                .filter(EditingContextAdapter.class::isInstance)
                .map(EditingContextAdapter.class::cast)
                .map(EditingContextAdapter::getEditingContextId)
                .findFirst();
        if (optionalEditingContextId.isPresent()) {
            String editingContextId = optionalEditingContextId.get();
            List<Resource> standardLibraries = this.standardLibrariesCache.getIfPresent(editingContextId);
            if (standardLibraries != null) {
                this.logger.atDebug()
                        .setMessage("Found cached standard libraries for editing context {}")
                        .addArgument(() -> editingContextId)
                        .log();
                targetResourceSet.getResources().addAll(standardLibraries);
            } else {
                this.logger.atDebug()
                        .setMessage("No cached standard libraries for editing context {}")
                        .addArgument(() -> editingContextId)
                        .log();
                this.standardLibrariesLoader.loadStandardLibraries(targetResourceSet);
                // We don't cache the standard libraries here, because we have to cache them anyway before the disposal of the editing context (see EditingContextCachingService). If at some point
                // Sirius Web evolves in the right direction, we could get rid of this caching service, and directly cache the loaded resources here.
            }
        }
    }
}
