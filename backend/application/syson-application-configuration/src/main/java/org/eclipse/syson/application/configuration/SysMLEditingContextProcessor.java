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
package org.eclipse.syson.application.configuration;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextProcessor;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.web.services.editingcontext.EditingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * {@link IEditingContextProcessor} for SysML libraries. All SysML standard libraries should be loaded when a project
 * (i.e. an editing context) is loaded.
 * 
 * @author arichard
 */
@Service
public class SysMLEditingContextProcessor implements IEditingContextProcessor {

    private final Logger logger = LoggerFactory.getLogger(SysMLEditingContextProcessor.class);

    private final SysMLStandardLibrariesConfiguration standardLibraries;
    
    public SysMLEditingContextProcessor(SysMLStandardLibrariesConfiguration standardLibraries) {
        this.standardLibraries = Objects.requireNonNull(standardLibraries);
    }

    @Override
    public void preProcess(IEditingContext editingContext) {
        Instant start = Instant.now();
        if (editingContext instanceof EditingContext siriusWebEditingContext) {
            ResourceSet sourceResourceSet = this.standardLibraries.getLibrariesResourceSet();
            ResourceSet targetResourceSet = siriusWebEditingContext.getDomain().getResourceSet();
            sourceResourceSet.getResources().forEach(sourceResource -> {
                Resource targetResource = targetResourceSet.getResource(sourceResource.getURI(), false);
                if (targetResource == null) {
                    targetResource = new JSONResourceFactory().createResource(sourceResource.getURI());
                    Optional<ResourceMetadataAdapter> resourceAdapter = sourceResource.eAdapters().stream()
                            .filter(ResourceMetadataAdapter.class::isInstance)
                            .map(ResourceMetadataAdapter.class::cast)
                            .findFirst();
                    if (resourceAdapter.isPresent()) {
                        targetResource.eAdapters().add(new ResourceMetadataAdapter(resourceAdapter.get().getName()));
                    }
                    targetResourceSet.getResources().add(targetResource);
                    EList<EObject> contents = sourceResource.getContents();
                    for (EObject eObject : contents) {
                        targetResource.getContents().add(EcoreUtil.copy(eObject));
                    }
                }
            });
        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        this.logger.info("Copy all standard libraries in the editing context in {} ms", timeElapsed);
    }

    @Override
    public void postProcess(IEditingContext editingContext) {
    }
}
