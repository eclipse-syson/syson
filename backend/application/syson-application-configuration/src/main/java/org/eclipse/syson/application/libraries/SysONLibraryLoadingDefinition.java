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

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.syson.application.configuration.SysONDefaultLibrariesConfiguration;

/**
 * Defines how to identify, retrieve and load the different files constituting a SysON library.
 *
 * Note that the resources must be in the classpath of the application.
 *
 * @author flatombe
 * @see SysONDefaultLibrariesConfiguration#KERML
 * @see SysONDefaultLibrariesConfiguration#SYSML
 */
public record SysONLibraryLoadingDefinition(
        String familyName,
        String scheme,
        Path relativePathToDirectoryContainingLibraryFiles,
        List<String> fileExtensions,
        Function<URI, Resource> resourceLoadingBehavior) {

    /**
     * Creates a new {@link SysONLibraryLoadingDefinition}.
     *
     * @param familyName
     *            the (non-{@code null}) full label of this library.
     * @param scheme
     *            the (non-{@code null}) scheme to use for the EMF {@link URI}s used to load resources of this library.
     * @param relativePathToDirectoryContainingLibraryFiles
     *            the (non-{@code null}) relative {@link Path} to the <b>directory</b> containing the library files. If
     *            set to {@code foo/bar} then the library files are expected to be in directory
     *            {@code src/main/resources/foo/bar}.
     * @param fileExtensions
     *            the (non-{@code null}) {@link List} of file extensions to look for in
     *            {@code relativePathToDirectoryContainingLibraryFiles}.
     * @param resourceLoadingBehavior
     *            the (non-{@code null}) {@link Function} able to load a {@link URI} into a {@link Resource}.
     */
    public SysONLibraryLoadingDefinition {
        Objects.requireNonNull(familyName);
        Objects.requireNonNull(scheme);
        Objects.requireNonNull(relativePathToDirectoryContainingLibraryFiles);
        Objects.requireNonNull(fileExtensions);
        Objects.requireNonNull(resourceLoadingBehavior);

        if (relativePathToDirectoryContainingLibraryFiles.isAbsolute()) {
            throw new IllegalArgumentException("Path '%s' is absolute, but a relative one was expected.".formatted(relativePathToDirectoryContainingLibraryFiles.toString()));
        }
        if (relativePathToDirectoryContainingLibraryFiles.toFile().exists() && !relativePathToDirectoryContainingLibraryFiles.toFile().isDirectory()) {
            throw new IllegalArgumentException("Path '%s' exists but is not a directory.".formatted(relativePathToDirectoryContainingLibraryFiles.toString()));
        }
    }

}
