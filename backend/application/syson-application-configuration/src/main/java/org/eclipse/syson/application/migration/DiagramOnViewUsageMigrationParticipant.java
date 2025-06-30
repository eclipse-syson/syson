/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.emf.migration.api.IMigrationParticipant;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationContentSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Migration participant used to migrate SysON diagrams previous to 2025.6.0.
 *
 * Diagrams must now be created under ViewUsage elements only. Existing diagrams must be migrated. The migration
 * consists in creating a ViewUsage element under the element associated to the existing diagram. Then the existing
 * diagram should be associated to this new ViewUsage. The new ViewUsage element should have the same name than the
 * existing diagram. The new ViewUsage element should by typed with a ViewDefinition from the standard library
 * "StandardViewDefintions", this ViewDefinition must have the same name than the name of the Sirius DiagramDescription.
 *
 * @author arichard
 */
@Service
public class DiagramOnViewUsageMigrationParticipant implements IMigrationParticipant {

    public static final String PARTICIPANT_VERSION = "2025.6.0-202505150000";

    private final JdbcClient jdbcClient;

    private final IIdentityService identityService;

    private final IRepresentationMetadataSearchService representationMetadataSearchService;

    private final IRepresentationContentSearchService representationContentSearchService;

    public DiagramOnViewUsageMigrationParticipant(JdbcClient jdbcClient, IIdentityService identityService, IRepresentationMetadataSearchService representationMetadataSearchService,
            IRepresentationContentSearchService representationContentSearchService) {
        this.jdbcClient = Objects.requireNonNull(jdbcClient);
        this.identityService = Objects.requireNonNull(identityService);
        this.representationMetadataSearchService = Objects.requireNonNull(representationMetadataSearchService);
        this.representationContentSearchService = Objects.requireNonNull(representationContentSearchService);
    }

    @Override
    public String getVersion() {
        return PARTICIPANT_VERSION;
    }

    @Transactional
    @Override
    public void postObjectLoading(JsonResource resource, EObject eObject, JsonObject jsonObject) {
        if (eObject instanceof Element element && !(element instanceof ViewUsage)) {
            var diagramsUUIDs = this.getDiagramsFromObjectId(this.identityService.getId(element));
            for (UUID diagramUUID : diagramsUUIDs) {
                var optRepresentationMetadata = this.representationMetadataSearchService.findMetadataById(diagramUUID);
                var optRepresentationContent = this.representationContentSearchService.findContentById(diagramUUID);
                if (optRepresentationMetadata.isPresent() && optRepresentationContent.isPresent() && this.isSysONDiagram(optRepresentationMetadata.get())) {
                    var representationMetadata = optRepresentationMetadata.get();
                    var representationContent = optRepresentationContent.get();
                    element.eAdapters().add(new DiagramOnViewUsageMigrationAdapter(representationMetadata, representationContent));
                }
            }
        }
    }

    private List<UUID> getDiagramsFromObjectId(String elementId) {
        var sql = """
                SELECT representationMetadata.id
                FROM representation_metadata representationMetadata
                WHERE representationMetadata.target_object_id = :targetObjectId
                """;
        var diagramsUUIDs = this.jdbcClient.sql(sql)
                .param("targetObjectId", elementId)
                .query(UUID.class)
                .list();
        return diagramsUUIDs;
    }

    private boolean isSysONDiagram(RepresentationMetadata representationMetadata) {
        boolean isSysONDiagram = false;
        String representationDescriptionId = representationMetadata.getDescriptionId();
        if (Objects.equals(SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            isSysONDiagram = true;
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.INTERCONNECTION_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            isSysONDiagram = true;
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.ACTION_FLOW_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            isSysONDiagram = true;
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.STATE_TRANSITION_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            isSysONDiagram = true;
        }
        return isSysONDiagram;
    }
}
