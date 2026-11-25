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
package org.eclipse.syson.application.export.checker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.web.application.document.services.LoadingReport;
import org.eclipse.sirius.web.application.document.services.api.ExternalResourceLoadingResult;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
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

    private final SysMLv2DocumentExporter exporter;

    private final EditingContext editingContext;

    private Consumer<Resource> loadedModelModifier;

    private String textToImport;

    private String expectedResult;

    public SysmlImportExportChecker(SysMLExternalResourceLoaderService sysmlLoader, SysMLv2DocumentExporter exporter, EditingContext editingContext) {
        super();
        this.editingContext = Objects.requireNonNull(editingContext);
        this.sysmlLoader = Objects.requireNonNull(sysmlLoader);
        this.exporter = Objects.requireNonNull(exporter);
    }

    /**
     * Optional consumer used to modify the loaded model.
     *
     * @param modifier
     *         a consumer to modify the loaded {@link Resource}
     * @return this for convenience
     */
    public SysmlImportExportChecker modifyLoadedModel(Consumer<Resource> modifier) {
        this.loadedModelModifier = modifier;
        return this;
    }

    /**
     * Expected String result.
     *
     * @param expected
     *         the expected result
     * @return this for convenience
     */
    public SysmlImportExportChecker expectedResult(String expected) {
        this.expectedResult = expected;
        return this;
    }

    /**
     * Expected String result.
     *
     * @param toImport
     *         the text about to be imported
     * @return this for convenience
     */
    public SysmlImportExportChecker textToImport(String toImport) {
        this.textToImport = toImport;
        return this;
    }

    /** Checks the expected text and verifies that importing the download introduces no diagnostics. */
    public void check() throws IOException {
        Objects.requireNonNull(this.textToImport);
        Objects.requireNonNull(this.expectedResult);
        try (var inputStream = new ByteArrayInputStream(this.textToImport.getBytes(StandardCharsets.UTF_8))) {
            Optional<ExternalResourceLoadingResult> optExternalResourceLoadingResult = this.sysmlLoader.getResource(inputStream, this.createFakeURI(UUID.randomUUID()),
                    this.editingContext.getDomain().getResourceSet(),
                    false);

            assertTrue(optExternalResourceLoadingResult.isPresent());
            ExternalResourceLoadingResult externalResourceLoadingResult = optExternalResourceLoadingResult.get();
            if (this.loadedModelModifier != null) {
                // Modify loaded model
                this.loadedModelModifier.accept(externalResourceLoadingResult.resource());
            }
            Optional<byte[]> bytes = this.exporter.getBytes(externalResourceLoadingResult.resource(), MediaType.TEXT_HTML.toString());
            assertTrue(bytes.isPresent());

            String content = new String(bytes.get(), StandardCharsets.UTF_8);
            assertEquals(this.normalize(this.expectedResult), this.normalize(content));
            var resources = this.editingContext.getDomain().getResourceSet();
            resources.getResources().remove(externalResourceLoadingResult.resource());
            var reimported = this.sysmlLoader.getResource(new ByteArrayInputStream(bytes.get()), this.createFakeURI(UUID.randomUUID()), resources, false).orElseThrow();
            var originalReport = (LoadingReport) externalResourceLoadingResult.loadingReport();
            var reimportReport = (LoadingReport) reimported.loadingReport();
            assertTrue(originalReport.content().containsAll(reimportReport.content()), () -> "Reimport diagnostics: " + reimportReport.content());
        }
    }

    private URI createFakeURI(UUID uuid) {
        return URI.createURI("fakeUri:/" + uuid.toString() + "/test.sysml");
    }

    private String normalize(String content) {
        return content.replaceAll("\t", "    ")
                .replaceAll("\r\n", "\n")
                .replaceAll("\r", "\n");
    }

}
