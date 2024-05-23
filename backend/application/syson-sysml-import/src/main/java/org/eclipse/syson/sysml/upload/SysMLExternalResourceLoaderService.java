/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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

import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.web.application.document.services.api.IExternalResourceLoaderService;
import org.eclipse.syson.sysml.ASTTransformer;
import org.eclipse.syson.sysml.SysmlToAst;
import org.springframework.stereotype.Service;

import org.eclipse.emf.common.util.URI;

/**
 * Specific {@link IExternalResourceLoaderService} allowing to load SysML textual resources.
 * 
 * @author arichard
 */
@Service
public class SysMLExternalResourceLoaderService implements IExternalResourceLoaderService {

    private final SysmlToAst sysmlToAst;

    public SysMLExternalResourceLoaderService(SysmlToAst sysmlToAst) {
        this.sysmlToAst = Objects.requireNonNull(sysmlToAst);
    }

    @Override
    public boolean canHandle(InputStream inputStream, URI resourceURI, ResourceSet resourceSet) {
        return resourceURI != null && resourceURI.toString().endsWith(".sysml");
    }

    @Override
    public Optional<Resource> getResource(InputStream inputStream, URI resourceURI, ResourceSet resourceSet) {
        Resource resource = null;
        InputStream astStream = this.sysmlToAst.convert(inputStream, resourceURI.fileExtension());
        ASTTransformer tranformer = new ASTTransformer();
        resource = tranformer.convertResource(astStream, resourceSet);
        if (resource != null) {
            resourceSet.getResources().add(resource);
        }
        return Optional.ofNullable(resource);
    }

}
