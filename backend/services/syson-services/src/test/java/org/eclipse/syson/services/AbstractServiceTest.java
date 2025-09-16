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
package org.eclipse.syson.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.emf.utils.EMFResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Abstract tests class, providing useful methods.
 *
 * @author arichard
 */
public abstract class AbstractServiceTest {

    private final Logger logger = LoggerFactory.getLogger(AbstractServiceTest.class);

    public Resource loadResourcesFrom(ResourceSet resourceSet, String modelPath) {
        ClassPathResource classPathResource = new ClassPathResource(modelPath);
        String path = classPathResource.getPath();
        URI uri = URI.createURI(IEMFEditingContext.RESOURCE_SCHEME + ":///" + UUID.nameUUIDFromBytes(path.getBytes()));
        Resource emfResource = null;
        Optional<Resource> existingEMFResource = resourceSet.getResources().stream().filter(r -> uri.equals(r.getURI())).findFirst();
        if (existingEMFResource.isEmpty()) {
            try (var inputStream = new ByteArrayInputStream(classPathResource.getContentAsByteArray())) {
                if (classPathResource.getFilename().endsWith(".json")) {
                    emfResource = this.loadFromJSON(uri, inputStream);
                } else {
                    emfResource = this.loadFromXMI(uri, inputStream);
                }
                resourceSet.getResources().add(emfResource);
                emfResource.eAdapters().add(new ResourceMetadataAdapter(modelPath, false));
            } catch (IOException e) {
                this.logger.warn("An error occured while loading model: {}.", e.getMessage());
                resourceSet.getResources().remove(emfResource);
            }
        }
        return emfResource;
    }

    private Resource loadFromXMI(URI uri, InputStream inputStream) throws IOException {
        Resource inputResource = new XMIResourceImpl(uri);
        Map<String, Object> xmiLoadOptions = new EMFResourceUtils().getXMILoadOptions(new XMLParserPoolImpl());
        inputResource.load(inputStream, xmiLoadOptions);
        return inputResource;
    }

    private Resource loadFromJSON(URI uri, InputStream inputStream) throws IOException {
        Resource inputResource = new JSONResourceFactory().createResource(uri);
        inputResource.load(inputStream, Map.of());
        return inputResource;
    }
}
