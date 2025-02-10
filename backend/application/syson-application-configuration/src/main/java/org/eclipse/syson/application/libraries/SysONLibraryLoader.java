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
package org.eclipse.syson.application.libraries;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * Utilities for loading {@link SysONLibraryLoadingDefinition library} resources from the classpath of the application
 * into EMF resources.
 *
 * @author flatombe
 */
public class SysONLibraryLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysONLibraryLoader.class);

    /**
     * Loads all the resources of a library into a {@link ResourceSet}.
     *
     * @param resourceSet
     *            the (non-{@code null}) {@link ResourceSet}. Its resources are modified as part of this loading
     *            operation.
     * @param definition
     *            the (non-{@code null}) {@link SysONLibraryLoadingDefinition} to load.
     * @return a (non-{@code null}) {@link List} of the (non-{@code null}) {@link Resource} that have been added to
     *         {@code resourceSet}.
     */
    public List<Resource> loadLibraryResources(final ResourceSet resourceSet, final SysONLibraryLoadingDefinition definition) {
        final List<Resource> loadedEmfResources = new ArrayList<>();

        for (final ClassPathResource resource : this.findAllLibraryResourcesFromApplicationClasspath(definition)) {
            final URI uri = this.createEmfUriFor(resource, definition);
            final Resource existingResource = resourceSet.getResource(uri, false);
            if (existingResource == null) {
                this.createAndLoadEmfResourceFor(resource, uri, resourceSet, definition.resourceLoadingBehavior()).ifPresent(emfResource -> {
                    LOGGER.info("Loaded %s resource: %s".formatted(definition.familyName(), resource.getFilename()));
                    loadedEmfResources.add(emfResource);
                });
            } else {
                LOGGER.warn("Could not load resource '%s' because there already is a resource with the same URI: %s".formatted(resource.getFilename(), uri.toString()));
            }
        }

        return loadedEmfResources;
    }

    /**
     * Creates the EMF {@link URI} for a {@link ClassPathResource} from a {@link SysONLibraryLoadingDefinition}.
     *
     * @param resource
     *            the (non-{@code null}) {@link ClassPathResource}.
     * @param definition
     *            the (non-{@code null}) {@link SysONLibraryLoadingDefinition}.
     * @return a (non-{@code null}) {@link URI} that uniquely identifies {@code resource}.
     */
    public URI createEmfUriFor(final ClassPathResource resource, final SysONLibraryLoadingDefinition definition) {
        final URI uri = URI.createURI(definition.scheme() + ":///" + UUID.nameUUIDFromBytes(resource.getPath().getBytes()));
        return uri;
    }

    /**
     * Attempts to create the EMF {@link Resource} corresponding to a library
     * {@link org.springframework.core.io.Resource} from the classpath, load its contents and add it to the given
     * {@link ResourceSet}.
     *
     * @param springResource
     *            the {@link org.springframework.core.io.Resource} to load.
     * @param uri
     *            the (non-{@code null}) {@link URI} to use for the created {@link Resource}.
     * @param resourceSet
     *            the (non-{@code null}) {@link ResourceSet} to load the created {@link Resource} into.
     * @param resourceLoadingBehavior
     *            the (non-{@code null}) behavior for creating a {@link Resource} from {@code uri}.
     * @return the (non-{@code null}) {@link Optional} representing the newly-created {@link Resource}.
     */
    public Optional<Resource> createAndLoadEmfResourceFor(final org.springframework.core.io.Resource springResource,
            final URI uri, final ResourceSet resourceSet, final Function<URI, Resource> resourceLoadingBehavior) {
        final Resource emfResource = resourceLoadingBehavior.apply(uri);
        try (InputStream inputStream = new ByteArrayInputStream(springResource.getContentAsByteArray())) {
            resourceSet.getResources().add(emfResource);
            emfResource.load(inputStream, Map.of());
            emfResource.eAdapters().add(new ResourceMetadataAdapter(FilenameUtils.getBaseName(springResource.getFilename())));
            return Optional.of(emfResource);
        } catch (final IOException ioException) {
            resourceSet.getResources().remove(emfResource);
            LOGGER.error("There was an unexpected issue while loading the contents of '%s'".formatted(springResource.getFilename()), ioException);
            return Optional.empty();
        }
    }

    /**
     * Explores the classpath of the application to find all resources corresponding to a library.
     *
     * @param definition
     *            the (non-{@code null}) {@link SysONLibraryLoadingDefinition}.
     * @return a (non-{@code null}) {@link List} of (non-{@code null}) {@link ClassPathResource}.
     */
    public List<ClassPathResource> findAllLibraryResourcesFromApplicationClasspath(final SysONLibraryLoadingDefinition definition) {
        Objects.requireNonNull(definition);

        final List<ClassPathResource> allResourcesForLibraryLoadingDefinition = new ArrayList<>();
        final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        for (final String fileExtension : definition.fileExtensions()) {
            final String locationPattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + definition.relativePathToDirectoryContainingLibraryFiles().toString() + "/*." + fileExtension;

            final List<org.springframework.core.io.Resource> resources = new ArrayList<>();
            try {
                Stream.of(resolver.getResources(locationPattern)).forEach(resources::add);
            } catch (final IOException ioException) {
                LOGGER.error("There was an unexpected issue while attempting to retrieve %s resources at location: %s".formatted(definition.familyName(), locationPattern), ioException);
            }

            resources.stream().map(resource -> new ClassPathResource(definition.relativePathToDirectoryContainingLibraryFiles().resolve(resource.getFilename()).toString()))
                    .forEach(allResourcesForLibraryLoadingDefinition::add);
        }

        return allResourcesForLibraryLoadingDefinition;
    }
}
