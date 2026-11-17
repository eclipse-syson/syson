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
package org.eclipse.syson.sysml.upload;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.web.application.document.services.LoadingReport;
import org.eclipse.sirius.web.application.document.services.api.ExternalResourceLoadingResult;
import org.eclipse.sirius.web.application.document.services.api.IExternalResourceLoaderService;
import org.eclipse.syson.sysml.metamodel.util.ElementUtil;
import org.eclipse.syson.sysml.services.SysMLParsingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Specific {@link IExternalResourceLoaderService} allowing to load SysML textual resources.
 *
 * @author arichard
 */
@Service
public class SysMLExternalResourceLoaderService implements IExternalResourceLoaderService {

    private final Logger logger = LoggerFactory.getLogger(SysMLExternalResourceLoaderService.class);

    private final SysMLParsingService parsingService;

    /**
     * Creates a loader backed by the Java parser and linker.
     *
     * @param parsingService the shared parsing pipeline
     */
    public SysMLExternalResourceLoaderService(SysMLParsingService parsingService) {
        this.parsingService = Objects.requireNonNull(parsingService);
    }

    @Override
    public boolean canHandle(InputStream inputStream, URI resourceURI, ResourceSet resourceSet) {
        boolean canHandle = true;
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        bufferedInputStream.mark(Integer.MAX_VALUE);
        try (var reader = new BufferedReader(new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            if (line != null && line.startsWith("{")) {
                canHandle = false;
            }
            bufferedInputStream.reset();
        } catch (IOException exception) {
            this.logger.warn(exception.getMessage(), exception);
            canHandle = false;
        }
        if (canHandle) {
            canHandle = resourceURI != null && (resourceURI.toString().toLowerCase().endsWith(".sysml") || resourceURI.toString().toLowerCase().endsWith(".kerml"));
        }
        return canHandle;
    }

    @Override
    public Optional<ExternalResourceLoadingResult> getResource(InputStream inputStream, URI resourceURI, ResourceSet resourceSet, boolean applyMigrationParticipants) {
        var result = this.parsingService.parseAndLink(resourceSet, null, inputStream);
        List<String> reports = result.messages().stream().map(message -> message.level() + ": " + message.body()).toList();
        // The upload API requires a resource even when only an error report is available.
        Resource resource = new JSONResourceFactory().createResourceFromPath(null);
        if (result.root() != null) {
            resource.getContents().add(result.root());
            ElementUtil.setIsImported(resource, true);
            resourceSet.getResources().add(resource);
            result.applyImports();
        }
        return Optional.of(new ExternalResourceLoadingResult(resource, new LoadingReport(reports)));

    }
}
