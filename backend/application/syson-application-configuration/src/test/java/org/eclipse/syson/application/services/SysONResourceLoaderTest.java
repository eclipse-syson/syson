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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SysONResourceLoader}.
 *
 * @author cbrun
 */
public class SysONResourceLoaderTest {

    private static final String TARGET_ELEMENT_ID = "11111111-1111-4111-8111-111111111111";

    private static final String IMPORT_ELEMENT_ID = "22222222-2222-4222-8222-222222222222";

    @Test
    @DisplayName("GIVEN a parent-relative cross-document reference, WHEN loading a flat document URI, THEN the reference is resolved")
    void parentRelativeCrossDocumentReferenceIsResolvedOnLoad() {
        ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getPackageRegistry().put(SysmlPackage.eNS_URI, SysmlPackage.eINSTANCE);

        UUID targetDocumentId = UUID.randomUUID();
        JsonResource targetResource = this.createResource(resourceSet, targetDocumentId);
        Package targetPackage = SysmlFactory.eINSTANCE.createPackage();
        targetPackage.setElementId(TARGET_ELEMENT_ID);
        targetPackage.setDeclaredName("TargetPackage");
        targetResource.getContents().add(targetPackage);

        String content = """
                {
                  "json": { "version": "1.0", "encoding": "utf-8" },
                  "ns": { "sysml": "%s" },
                  "content": [{
                    "id": "%s",
                    "eClass": "sysml:NamespaceImport",
                    "data": {
                      "elementId": "%s",
                      "importedNamespace": "sysml:Package ../../%s#%s"
                    }
                  }]
                }
                """.formatted(SysmlPackage.eNS_URI, IMPORT_ELEMENT_ID, IMPORT_ELEMENT_ID, targetDocumentId, TARGET_ELEMENT_ID);

        var sourceResource = new SysONResourceLoader(List.of()).toResource(resourceSet, UUID.randomUUID().toString(), "Source.sysml", content, false, false);

        assertThat(sourceResource).isPresent();
        assertThat(this.findNamespaceImport(sourceResource.get()).getImportedNamespace()).isSameAs(targetPackage);
    }

    private JsonResource createResource(ResourceSet resourceSet, UUID documentId) {
        JsonResource resource = new JSONResourceFactory().createResource(new JSONResourceFactory().createResourceURI(documentId.toString()));
        resourceSet.getResources().add(resource);
        return resource;
    }

    private NamespaceImport findNamespaceImport(Resource resource) {
        NamespaceImport result = null;
        var iterator = resource.getAllContents();
        while (result == null && iterator.hasNext()) {
            if (iterator.next() instanceof NamespaceImport namespaceImport) {
                result = namespaceImport;
            }
        }
        assertThat(result).isNotNull();
        return result;
    }
}
