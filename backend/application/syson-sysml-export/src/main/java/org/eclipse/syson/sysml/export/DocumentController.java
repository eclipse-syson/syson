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
package org.eclipse.syson.sysml.export;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.sirius.web.services.api.document.Document;
import org.eclipse.sirius.web.services.api.document.IDocumentService;
import org.eclipse.sirius.web.services.api.id.IDParser;
import org.eclipse.sirius.web.services.editingcontext.EditingContext;
import org.eclipse.syson.sysml.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The entry point of the HTTP API to download documents.
 * <p>
 * This endpoint will be available on the API base path prefix with download segment and followed by the document Id
 * used as a suffix. As such, users will be able to send document download request to the following URL:
 * </p>
 *
 * <pre>
 * PROTOCOL://DOMAIN.TLD(:PORT)/API_BASE_PATH/editingcontexts/EDITING_CONTEXT_ID/documents/DOCUMENT_ID
 * </pre>
 *
 * <p>
 * In a development environment, the URL will most likely be:
 * </p>
 *
 * <pre>
 * http://localhost:8080/api/editingcontexts/EDITING_CONTEXT_ID/documents/DOCUMENT_ID
 * </pre>
 *
 * <p>
 * Only documents of type xmi are supported.
 * </p>
 *
 * @author smonnier
 */
@Controller
@RequestMapping("/api/editingcontexts/{editingContextId}/documents")
public class DocumentController {

    private final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    private final IEditingContextSearchService editingContextSearchService;

    private final IDocumentService documentService;

    private final ISysMLModelToTextService converter;

    public DocumentController(IDocumentService documentService, IEditingContextSearchService editingContextSearchService, ISysMLModelToTextService converter) {
        this.converter = converter;
        this.documentService = Objects.requireNonNull(documentService);
        this.editingContextSearchService = Objects.requireNonNull(editingContextSearchService);
    }

    @GetMapping(path = "/{documentId}")
    @ResponseBody
    public ResponseEntity<Resource> getDocument(@PathVariable String editingContextId, @PathVariable String documentId) {

        Optional<Document> optionalDocument = new IDParser().parse(documentId).flatMap(documentUUID -> this.documentService.getDocument(editingContextId, documentUUID));

        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();

            EditingContext editingContext = (EditingContext) editingContextSearchService.findById(editingContextId).get();
            ResourceSet resourceSet = editingContext.getDomain().getResourceSet();
            URI uri = new JSONResourceFactory().createResourceURI(document.getId().toString());

            ResourceSet loadingResourceSet = new ResourceSetImpl();
            loadingResourceSet.setPackageRegistry(resourceSet.getPackageRegistry());

            JsonResource jsonResource = new JSONResourceFactory().createResource(uri);
            loadingResourceSet.getResources().add(jsonResource);
            try (var inputStream = new ByteArrayInputStream(document.getContent().getBytes())) {
                jsonResource.load(inputStream, null);
            } catch (IOException exception) {
                this.logger.warn(exception.getMessage(), exception);
            }

            resourceSet.getResources().add(jsonResource);

            String contentSysml = converter.toText((Element) jsonResource.getContents().get(0));

            byte[] bytes = contentSysml.getBytes();

            // @formatter:off
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename(document.getName())
                    .build();
            // @formatter:on

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(contentDisposition);
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.setContentLength(bytes.length);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(bytes));
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
}
