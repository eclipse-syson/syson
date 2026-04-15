/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.application.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.IDAdapter;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.syson.application.configuration.SysONDefaultLibrariesConfiguration;
import org.eclipse.syson.application.services.api.IStandardLibrariesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Loads SysML and KerML standard libraries into a resource set.
 *
 * @author gdaniel
 */
@Service
public class StandardLibrariesLoader implements IStandardLibrariesLoader {

    private final SysONDefaultLibrariesConfiguration defaultLibraries;

    public StandardLibrariesLoader(SysONDefaultLibrariesConfiguration defaultLibraries) {
        this.defaultLibraries = Objects.requireNonNull(defaultLibraries);
    }

    @Override
    public void loadStandardLibraries(ResourceSet targetResourceSet) {
        ResourceSet sourceResourceSet = this.defaultLibraries.getLibrariesResourceSet();

        // Use a common copier for all the resources to make sure cross-references are correctly copied.
        SysONCopier copier = new SysONCopier();
        sourceResourceSet.getResources().forEach(sourceResource -> {
            Resource targetResource = targetResourceSet.getResource(sourceResource.getURI(), false);
            if (targetResource == null) {
                Map<String, Object> options = new HashMap<>();
                // allows to persist references to standard libraries elements with URI containing elementId instead
                // of id from SiriusWeb
                options.put(JsonResource.OPTION_FORCE_DEFAULT_REFERENCE_SERIALIZATION, Boolean.TRUE);
                targetResource = new JSONResourceFactory().createResource(sourceResource.getURI(), options);
                Optional<ResourceMetadataAdapter> resourceAdapter = sourceResource.eAdapters().stream()
                        .filter(ResourceMetadataAdapter.class::isInstance)
                        .map(ResourceMetadataAdapter.class::cast)
                        .findFirst();
                if (resourceAdapter.isPresent()) {
                    targetResource.eAdapters().add(new ResourceMetadataAdapter(resourceAdapter.get().getName(), true));
                }
                targetResourceSet.getResources().add(targetResource);
                EList<EObject> contents = sourceResource.getContents();
                for (EObject eObject : contents) {
                    targetResource.getContents().add(this.copy(eObject, (JsonResource) targetResource, copier));
                }
            }
        });

        // Copy all the references after the elements to make sure cross-references are correctly copied.
        copier.copyReferences();
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
    private final class SysONCopier extends EcoreUtil.Copier {

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
