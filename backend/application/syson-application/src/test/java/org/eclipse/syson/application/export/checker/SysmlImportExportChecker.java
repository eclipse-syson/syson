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
package org.eclipse.syson.application.export.checker;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.sirius.components.events.ICause;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.sirius.web.application.editingcontext.services.api.IEditingDomainFactory;
import org.eclipse.sirius.web.domain.boundedcontexts.project.Project;
import org.eclipse.syson.application.configuration.SysMLEditingContextProcessor;
import org.eclipse.syson.sysml.export.SysMLv2DocumentExporter;
import org.eclipse.syson.sysml.upload.SysMLExternalResourceLoaderService;
import org.springframework.http.MediaType;

/**
 * Checker that imports a SysML file content then export it and checks the result.
 *
 * @author Arthur Daussy
 */
public class SysmlImportExportChecker {

    private final SysMLExternalResourceLoaderService sysmlLoader;

    private final IEditingDomainFactory editingDomainFactory;

    private final SysMLv2DocumentExporter exporter;

    private final SysMLEditingContextProcessor sysMLEditingContextProcessor;

    public SysmlImportExportChecker(SysMLExternalResourceLoaderService sysmlLoader, IEditingDomainFactory editingDomainFactory, SysMLv2DocumentExporter exporter, SysMLEditingContextProcessor sysMLEditingContextProcessor) {
        super();
        this.sysMLEditingContextProcessor = Objects.requireNonNull(sysMLEditingContextProcessor);
        this.sysmlLoader = Objects.requireNonNull(sysmlLoader);
        this.editingDomainFactory = editingDomainFactory;
        this.exporter = exporter;
    }

    public void check(String importedText, String expectedResult) throws IOException {

        UUID uuid = UUID.randomUUID();
        AdapterFactoryEditingDomain editingDomain = this.editingDomainFactory.createEditingDomain(this.createFakeProject(uuid));
        EditingContext editingContext = new EditingContext(UUID.randomUUID().toString(), editingDomain, Map.of(), List.of());
        this.sysMLEditingContextProcessor.preProcess(editingContext);

        try (var inputStream = new ByteArrayInputStream(importedText.getBytes())) {
            Optional<Resource> optLoadedResources = this.sysmlLoader.getResource(inputStream, this.createFakeURI(uuid), editingDomain.getResourceSet(), false);

            assertTrue(optLoadedResources.isPresent());
            Optional<byte[]> bytes = this.exporter.getBytes(optLoadedResources.get(), MediaType.TEXT_HTML.toString());
            assertTrue(bytes.isPresent());

            String content = new String(bytes.get());
            assertEquals(this.normalize(expectedResult), this.normalize(content));
        }

    }

    private URI createFakeURI(UUID uuid) {
        return URI.createURI("fakeUri:/" + uuid.toString() + "/test.sysml");
    }

    private Project createFakeProject(UUID uuid) {
        return Project.newProject().name(uuid.toString()).natures(List.of()).build(new ICause.NoOp());
    }

    private String normalize(String content) {
        return content.replaceAll("\t", "    ")
                .replaceAll("\r\n", "\n")
                .replaceAll("\r", "\n");
    }

}
