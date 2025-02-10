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
package org.eclipse.syson.application.configuration;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextProcessor;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.IDAdapter;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.sirius.web.application.studio.services.api.IStudioCapableEditingContextPredicate;
import org.eclipse.syson.util.SysONEContentAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * {@link IEditingContextProcessor} for SysML libraries. All SysML default libraries should be loaded when a project
 * (i.e. an editing context) is loaded.
 *
 * @author arichard
 */
@Service
public class SysMLEditingContextProcessor implements IEditingContextProcessor {

    private final Logger logger = LoggerFactory.getLogger(SysMLEditingContextProcessor.class);

    private final SysONDefaultLibrariesConfiguration defaultLibraries;

    private final IStudioCapableEditingContextPredicate studioCapableEditingContextPredicate;

    public SysMLEditingContextProcessor(SysONDefaultLibrariesConfiguration standardLibraries, IStudioCapableEditingContextPredicate studioCapableEditingContextPredicate) {
        this.defaultLibraries = Objects.requireNonNull(standardLibraries);
        this.studioCapableEditingContextPredicate = Objects.requireNonNull(studioCapableEditingContextPredicate);
    }

    @Override
    public void preProcess(IEditingContext editingContext) {
        if (editingContext instanceof IEMFEditingContext siriusWebEditingContext
                && !this.studioCapableEditingContextPredicate.test(editingContext.getId())) {
            // Do not initialize the editing context as a SysON editing context if it contains a studio.
            siriusWebEditingContext.getDomain().getResourceSet().eAdapters().add(new SysONEContentAdapter());

            Instant start = Instant.now();
            ResourceSet sourceResourceSet = this.defaultLibraries.getLibrariesResourceSet();
            ResourceSet targetResourceSet = siriusWebEditingContext.getDomain().getResourceSet();
            /*
             * Use a common copier for all the resources to make sure cross-references are correctly copied.
             */
            SysONCopier copier = new SysONCopier();
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
                        targetResource.getContents().add(this.copy(eObject, (JsonResource) targetResource, copier));
                    }
                }
            });
            /*
             * Copy all the references after the elements to make sure cross-references are correctly copied.
             */
            copier.copyReferences();
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toMillis();
            this.logger.info("Copied all default libraries in the editing context in {} ms", timeElapsed);
        }
    }

    @Override
    public void postProcess(IEditingContext editingContext) {
    }

    private EObject copy(EObject eObject, JsonResource resource, SysONCopier copier) {
        copier.setResource(resource);
        var result = copier.copy(eObject);
        return result;
    }

    /**
     * Copier that also copies the IDAdapter.
     *
     * @author arichard
     */
    private final class SysONCopier extends Copier {

        private static final long serialVersionUID = 1L;

        private final Logger logger = LoggerFactory.getLogger(SysONCopier.class);

        private JsonResource resource;

        public void setResource(JsonResource resource) {
            this.resource = resource;
        }

        @Override
        public EObject copy(EObject eObject) {
            EObject copy = null;
            if (this.resource != null) {
                copy = super.copy(eObject);
                var adapter = this.findIDAdapter(eObject);
                if (adapter != null) {
                    var oldId = adapter.getId().toString();
                    this.resource.setID(copy, oldId);
                }
            } else {
                this.logger.error("SysONCopier requires a JsonResource to make a copy");
            }
            return copy;
        }

        private IDAdapter findIDAdapter(EObject eObject) {
            for (var adapter : eObject.eAdapters()) {
                if (adapter instanceof IDAdapter idAdapter) {
                    return idAdapter;
                }
            }
            return null;
        }
    }
}
