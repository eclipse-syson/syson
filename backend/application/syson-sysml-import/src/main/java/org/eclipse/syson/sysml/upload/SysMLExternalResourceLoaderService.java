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
import java.util.ArrayList;
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
import org.eclipse.syson.sysml.ASTTransformer;
import org.eclipse.syson.sysml.AstParsingResult;
import org.eclipse.syson.sysml.SysmlToAst;
import org.eclipse.syson.sysml.textual.utils.Status;
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
        AstParsingResult astResult = this.sysmlToAst.convert(inputStream, resourceURI.fileExtension());

        List<String> reports = new ArrayList<>(astResult.reports().stream().map(Status::toString).toList());
        final Resource resource;
        if (astResult.ast().isPresent()) {
            ASTTransformer transformer = new ASTTransformer();
            resource = transformer.convertResource(astResult.ast().get(), resourceSet);
            if (resource != null) {
                ElementUtil.setIsImported(resource, true);
                resourceSet.getResources().add(resource);
            }
            reports.addAll(transformer.logTransformationMessages());

        } else {
            // It seems there is a problem in the API, to be able to report an error, we need a resource.
            // https://github.com/eclipse-sirius/sirius-web/issues/6281
            resource = new JSONResourceFactory().createResourceFromPath(null);
        }
        return Optional.of(new ExternalResourceLoadingResult(resource, new LoadingReport(reports)));

    }
}
