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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.migration.MigrationService;
import org.eclipse.sirius.components.emf.migration.api.IMigrationParticipant;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.sirius.web.application.editingcontext.services.api.IResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * SysON resource loader applying the SysON JSON resource processor when loading project documents.
 *
 * @author cbrun
 */
@Service
@Primary
public class SysONResourceLoader implements IResourceLoader {

    private final Logger logger = LoggerFactory.getLogger(SysONResourceLoader.class);

    private final List<IMigrationParticipant> migrationParticipants;

    /**
     * Creates a new resource loader.
     *
     * @param migrationParticipants
     *            the migration participants to apply when requested
     */
    public SysONResourceLoader(List<IMigrationParticipant> migrationParticipants) {
        this.migrationParticipants = List.copyOf(migrationParticipants);
    }

    @Override
    public Optional<Resource> toResource(ResourceSet resourceSet, String id, String name, String content, boolean applyMigrationParticipants, boolean isReadOnly) {
        Optional<Resource> optionalResource = Optional.empty();

        HashMap<Object, Object> loadOptions = new HashMap<>();
        JsonResource.IJsonResourceProcessor processor = new JsonResource.IJsonResourceProcessor.NoOp();
        if (applyMigrationParticipants) {
            var migrationService = new MigrationService(this.migrationParticipants);
            loadOptions.put(JsonResource.OPTION_EXTENDED_META_DATA, migrationService);
            processor = migrationService;
        }
        loadOptions.put(JsonResource.OPTION_JSON_RESSOURCE_PROCESSOR, new SysONJsonResourceProcessor(processor));

        var resource = new JSONResourceFactory().createResource(new JSONResourceFactory().createResourceURI(id));
        try (var inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
            resourceSet.getResources().add(resource);
            resource.eAdapters().add(new ResourceMetadataAdapter(name, isReadOnly));
            resource.load(inputStream, loadOptions);
            optionalResource = Optional.of(resource);
        } catch (IOException | IllegalArgumentException exception) {
            this.logger.atWarn()
                    .setMessage("An error occured while loading document {}")
                    .addArgument(id)
                    .setCause(exception)
                    .log();
            resourceSet.getResources().remove(resource);
        }
        return optionalResource;
    }
}
