/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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

import static org.eclipse.syson.application.services.EditingContextActionProvider.EMPTY_SYSML_ID;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLParserPool;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.api.IEditingContextActionHandler;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.EditingContext;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.components.emf.utils.EMFResourceUtils;
import org.eclipse.sirius.components.representations.Failure;
import org.eclipse.sirius.components.representations.IStatus;
import org.eclipse.sirius.components.representations.Success;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.syson.application.configuration.StereotypeDescriptionRegistryConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


/**
 * Handler used to perform an action on the editingContext.
 *
 * @author arichard
 */
@Service
public class EditingContextActionHandler implements IEditingContextActionHandler {

    private static final XMLParserPool PARSER_POOL = new XMLParserPoolImpl();

    private static final List<String> HANDLED_ACTIONS = List.of(EMPTY_SYSML_ID);

    private final Logger logger = LoggerFactory.getLogger(EditingContextActionHandler.class);

    @Override
    public boolean canHandle(IEditingContext editingContext, String actionId) {
        return HANDLED_ACTIONS.contains(actionId);
    }

    @Override
    public IStatus handle(IEditingContext editingContext, String actionId) {
        return Optional.of(editingContext)
                .filter(EditingContext.class::isInstance)
                .map(EditingContext.class::cast)
                .map(EditingContext::getDomain)
                .map(AdapterFactoryEditingDomain::getResourceSet)
                .map(resourceSet -> this.performActionOnResourceSet(resourceSet, actionId))
                .orElse(new Failure("Something went wrong while handling this action."));
    }

    private IStatus performActionOnResourceSet(ResourceSet resourceSet, String actionId) {
        return switch (actionId) {
            case EMPTY_SYSML_ID -> this.createResourceAndReturnSuccess(resourceSet, this::createEmptySysMLResource);
            default -> new Failure("Unknown action.");
        };

    }

    private IStatus createResourceAndReturnSuccess(ResourceSet resourceSet, Consumer<ResourceSet> createResource) {
        createResource.accept(resourceSet);
        return new Success(ChangeKind.SEMANTIC_CHANGE, Map.of());
    }

    private void createEmptySysMLResource(ResourceSet resourceSet) {
        JsonResource resource = new JSONResourceFactory().createResourceFromPath(UUID.randomUUID().toString());
        resource.getContents().addAll(StereotypeDescriptionRegistryConfigurer.getEmptySysMLv2Content());
        resource.eAdapters().add(new ResourceMetadataAdapter("SysMLv2"));
        resourceSet.getResources().add(resource);
    }

    public Optional<Resource> getResourceFromClassPathResource(ClassPathResource classPathResource) {

        try (var inputStream = classPathResource.getInputStream()) {
            URI uri = new JSONResourceFactory().createResourceURI(UUID.randomUUID().toString());
            return Optional.of(this.loadFromXMIAndTransformToJSONResource(uri, inputStream));
        } catch (IOException exception) {
            this.logger.error(exception.getMessage(), exception);
            return Optional.empty();
        }
    }

    private Resource loadFromXMIAndTransformToJSONResource(URI uri, InputStream inputStream) throws IOException {
        Resource inputResource = new XMIResourceImpl(uri);
        Map<String, Object> xmiLoadOptions = new EMFResourceUtils().getXMILoadOptions(PARSER_POOL);
        inputResource.load(inputStream, xmiLoadOptions);
        return this.transformToJSON(uri, inputResource);
    }

    private JsonResource transformToJSON(URI uri, Resource inputResource) throws IOException {
        JsonResource outputResource = new JSONResourceFactory().createResource(uri);
        outputResource.getContents().addAll(inputResource.getContents());
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Map<String, Object> jsonSaveOptions = new EMFResourceUtils().getFastJSONSaveOptions();
            jsonSaveOptions.put(JsonResource.OPTION_ENCODING, JsonResource.ENCODING_UTF_8);
            jsonSaveOptions.put(JsonResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
            outputResource.save(outputStream, jsonSaveOptions);
        }
        return outputResource;
    }

}
