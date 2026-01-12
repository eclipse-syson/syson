/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.application.migration;

import java.util.Objects;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.emf.migration.api.IMigrationParticipant;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.springframework.stereotype.Service;

/**
 * Diagram migration participant used to migrate SysON diagrams with diagram elements having "targetObjectId" pointing
 * to standard libraries Elements previous to 2025.8.0.
 *
 * Let's say we have a FeatureTyping pointing to the SclaraValues::String Element from the standard library. This
 * element had for "targetObjectId" the "id" of the object instead of its "elementId". But the "id" property changes
 * each time the standard libraries are updated. So the "targetObjectId" reference was pointing to nothing after the
 * update. With this migration participant, the "targetObjectId" now all point to the "elementId" property which is
 * stable before/after standard libraries updates.
 *
 * This migration participant has been added at the same time than the update of SysONIdentityService which allows to
 * get the "elementId" instead of the "id" when it is a library Element.
 *
 * @author arichard
 */
@Service
public class StandardLibrariesElementsReferencesMigrationParticipant implements IMigrationParticipant {

    private static final String PARTICIPANT_VERSION = "2025.8.0-202506301600";

    private final StandardLibrariesIdsResourceToMapParser standardLibrariesIdsResourceToMapParser;

    public StandardLibrariesElementsReferencesMigrationParticipant(StandardLibrariesIdsResourceToMapParser standardLibrariesIdsResourceToMapParser) {
        this.standardLibrariesIdsResourceToMapParser = Objects.requireNonNull(standardLibrariesIdsResourceToMapParser);
    }

    @Override
    public String getVersion() {
        return PARTICIPANT_VERSION;
    }

    @Override
    public String getEObjectUri(JsonResource resource, EObject eObject, EReference eReference, String uri) {
        String migratedURI = uri;
        if (!eReference.isContainment() && (uri.contains(ElementUtil.SYSML_LIBRARY_SCHEME) || uri.contains(ElementUtil.KERML_LIBRARY_SCHEME))) {
            ResourceSet resourceSet = resource.getResourceSet();
            String uriWithoutPrefixedType = uri;
            if (uri.startsWith("sysml:")) {
                if (uri.contains(ElementUtil.SYSML_LIBRARY_SCHEME)) {
                    int indexOf = uri.indexOf(ElementUtil.SYSML_LIBRARY_SCHEME);
                    uriWithoutPrefixedType = uri.substring(indexOf);
                } else if (uri.contains(ElementUtil.KERML_LIBRARY_SCHEME)) {
                    int indexOf = uri.indexOf(ElementUtil.KERML_LIBRARY_SCHEME);
                    uriWithoutPrefixedType = uri.substring(indexOf);
                }
            }
            URI uriAsURI = URI.createURI(uriWithoutPrefixedType);
            EObject referencedEObject = resourceSet.getEObject(uriAsURI, false);
            int uriFragmentIndex = uri.indexOf('#');
            var prefix = uri.substring(0, uriFragmentIndex + 1);
            if (referencedEObject instanceof Element element) {
                migratedURI = prefix + element.getElementId();
            } else {
                var oldId = uri.substring(uriFragmentIndex + 1);
                var elementId = this.standardLibrariesIdsResourceToMapParser.getOldIdToElementIdMap().get(oldId);
                if (elementId != null) {
                    migratedURI = prefix + elementId;
                }
            }
        }
        return migratedURI;
    }
}
