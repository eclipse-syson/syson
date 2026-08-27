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

import com.google.gson.JsonObject;

import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.emfjson.resource.JsonResource;

/**
 * JSON resource processor applying SysON-specific reference loading behavior while preserving the behavior of another
 * processor.
 *
 * @author cbrun
 */
public class SysONJsonResourceProcessor implements JsonResource.IJsonResourceProcessor {

    private final JsonResource.IJsonResourceProcessor delegate;

    /**
     * Creates a new processor.
     *
     * @param delegate
     *            the processor to invoke before SysON-specific behavior
     */
    public SysONJsonResourceProcessor(JsonResource.IJsonResourceProcessor delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public void preDeserialization(JsonResource resource, JsonObject jsonObject) {
        this.delegate.preDeserialization(resource, jsonObject);
    }

    @Override
    public void postSerialization(JsonResource resource, JsonObject jsonObject) {
        this.delegate.postSerialization(resource, jsonObject);
    }

    @Override
    public void postObjectLoading(JsonResource resource, EObject eObject, JsonObject jsonObject, boolean isTopObject) {
        this.delegate.postObjectLoading(resource, eObject, jsonObject, isTopObject);
    }

    @Override
    public Object getValue(JsonResource resource, EObject eObject, EStructuralFeature feature, Object value) {
        return this.delegate.getValue(resource, eObject, feature, value);
    }

    @Override
    public String getEObjectUri(JsonResource resource, EObject eObject, EReference eReference, String uri) {
        String updatedUri = this.delegate.getEObjectUri(resource, eObject, eReference, uri);
        if (updatedUri != null) {
            int prefixEnd = updatedUri.indexOf(' ');
            if (prefixEnd >= 0) {
                String prefix = updatedUri.substring(0, prefixEnd + 1);
                String referenceUri = updatedUri.substring(prefixEnd + 1);
                if (referenceUri.startsWith("../") && resource.getURI() != null && resource.getURI().segmentCount() == 1) {
                    // Upload sanitization deresolves links against the folder-bearing file name before the document gets its
                    // flat persistent URI.
                    referenceUri = IEMFEditingContext.RESOURCE_SCHEME + ":///" + referenceUri.replaceFirst("^(?:\\.\\./)+", "");
                }
                updatedUri = prefix + referenceUri;
            }
        }
        return updatedUri;
    }
}
