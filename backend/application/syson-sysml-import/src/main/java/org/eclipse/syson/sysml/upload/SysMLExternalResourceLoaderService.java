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
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.web.application.document.services.LoadingReport;
import org.eclipse.sirius.web.application.document.services.api.ExternalResourceLoadingResult;
import org.eclipse.sirius.web.application.document.services.api.IExternalResourceLoaderService;
import org.eclipse.syson.sysml.ASTTransformer;
import org.eclipse.syson.sysml.SysmlToAst;
import org.eclipse.syson.sysml.util.ElementUtil;
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

    private final SysmlToAst sysmlToAst;

    public SysMLExternalResourceLoaderService(SysmlToAst sysmlToAst) {
        this.sysmlToAst = Objects.requireNonNull(sysmlToAst);
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
        InputStream astStream = this.sysmlToAst.convert(inputStream, resourceURI.fileExtension());
        ASTTransformer transformer = new ASTTransformer();
        Resource resource = transformer.convertResource(astStream, resourceSet);
        if (resource != null) {
            ElementUtil.setIsImported(resource, true);
            resourceSet.getResources().add(resource);
        }
        var loadingReport = new LoadingReport(transformer.logTransformationMessages());
        var result = new ExternalResourceLoadingResult(resource, loadingReport);
        return Optional.ofNullable(result);
    }
}
