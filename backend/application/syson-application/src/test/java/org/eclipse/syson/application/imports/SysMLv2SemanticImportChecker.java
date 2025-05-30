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
package org.eclipse.syson.application.imports;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.syson.sysml.upload.SysMLExternalResourceLoaderService;

/**
 * Checker that imports a SysML file content then do some semantic checks.
 *
 * @author Arthur Daussy
 */
public class SysMLv2SemanticImportChecker {

    private final SysMLExternalResourceLoaderService sysmlResourceLoader;

    private final List<Consumer<Resource>> semanticCheckers = new ArrayList<>();

    private final EditingContext editingContext;


    public SysMLv2SemanticImportChecker(SysMLExternalResourceLoaderService sysmlResourceLoader, EditingContext editingContext) {
        this.editingContext = Objects.requireNonNull(editingContext);
        this.sysmlResourceLoader = Objects.requireNonNull(sysmlResourceLoader);
    }

    public void check(String importedText) throws IOException {
        UUID uuid = UUID.randomUUID();

        try (var inputStream = new ByteArrayInputStream(importedText.getBytes())) {
            Optional<Resource> optLoadedResources = this.sysmlResourceLoader.getResource(inputStream, this.createFakeURI(uuid), this.editingContext.getDomain().getResourceSet(), false);

            assertTrue(optLoadedResources.isPresent());

            Resource resource = optLoadedResources.get();
            for (Consumer<Resource> checker : this.semanticCheckers) {
                checker.accept(resource);
            }
        }
    }

    public SysMLv2SemanticImportChecker checkImportedModel(Consumer<Resource> checker) {
        this.semanticCheckers.add(checker);
        return this;
    }

    private URI createFakeURI(UUID uuid) {
        return URI.createURI("fakeUri:/" + uuid.toString() + "/test.sysml");
    }
}
