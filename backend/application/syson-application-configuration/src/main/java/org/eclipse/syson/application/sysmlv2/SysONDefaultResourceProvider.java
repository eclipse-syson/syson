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

package org.eclipse.syson.application.sysmlv2;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.migration.MigrationService;
import org.eclipse.sirius.components.emf.migration.api.IMigrationParticipant;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.syson.application.sysmlv2.api.IDefaultSysMLv2ResourceProvider;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 * Provides the content of a default SysMLv2 resource.
 *
 * @author gcoutable
 */
@Service
public class SysONDefaultResourceProvider implements IDefaultSysMLv2ResourceProvider {

    private final List<IMigrationParticipant> migrationParticipants;

    private final Logger logger = LoggerFactory.getLogger(SysONDefaultResourceProvider.class);

    public SysONDefaultResourceProvider(List<IMigrationParticipant> migrationParticipants) {
        this.migrationParticipants = Objects.requireNonNull(migrationParticipants);
    }

    @Override
    public Resource getEmptyResource(UUID resourcePath, String name) {
        var resource = new JSONResourceFactory().createResourceFromPath(resourcePath.toString());

        var resourceMetadataAdapter = new ResourceMetadataAdapter(name);
        var migrationService = new MigrationService(this.migrationParticipants);

        resourceMetadataAdapter.setMigrationData(migrationService.getMostRecentParticipantMigrationData());

        resource.eAdapters().add(resourceMetadataAdapter);

        return resource;
    }

    @Override
    public Resource getDefaultSysMLv2Resource(UUID resourcePath, String name) {
        var resource = this.getEmptyResource(resourcePath, name);

        var rootNamespace = SysmlFactory.eINSTANCE.createNamespace();
        var rootMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        var package1 = SysmlFactory.eINSTANCE.createPackage();
        rootNamespace.getOwnedRelationship().add(rootMembership);
        rootMembership.getOwnedRelatedElement().add(package1);
        package1.setDeclaredName("Package 1");
        package1.setElementId(ElementUtil.generateUUID(package1).toString());

        resource.getContents().add(rootNamespace);
        return resource;
    }

    @Override
    public void loadBatmobileResource(Resource resource) {
        try (var inputStream = new ClassPathResource("templates/Batmobile.json").getInputStream()) {
            Instant start = Instant.now();
            resource.load(inputStream, null);
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toMillis();
            this.logger.info("Batmobile loaded in %sms".formatted(timeElapsed));
        } catch (IOException exception) {
            this.logger.warn(exception.getMessage(), exception);
        }
    }
}
