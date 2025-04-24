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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventProcessorInitializationHook;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationContent;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.repositories.IRepresentationContentRepository;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationContentSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataUpdateService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hook that allows to update the target object of existing diagrams. This hook is coupled with the migration
 * participant about Diagram on ViewUsage. The target object of existing diagrams is moved to the ViewUsages that have
 * been created during the migration participant process.
 *
 * @author arichard
 */
@Service
public class DiagramOnViewUsageMigrationHook implements IEditingContextEventProcessorInitializationHook {

    private final IRepresentationMetadataSearchService representationMetadataSearchService;

    private final IRepresentationMetadataUpdateService representationMetadataUpdateService;

    private final IRepresentationContentSearchService representationContentSearchService;

    private final IRepresentationContentRepository representationContentRepository;

    private final IIdentityService identityService;

    private final JdbcClient jdbcClient;

    public DiagramOnViewUsageMigrationHook(IRepresentationMetadataSearchService representationMetadataSearchService, IRepresentationMetadataUpdateService representationMetadataUpdateService,
            IRepresentationContentSearchService representationContentSearchService, IRepresentationContentRepository representationContentRepository,
            IIdentityService identityService, JdbcClient jdbcClient) {
        this.representationMetadataSearchService = Objects.requireNonNull(representationMetadataSearchService);
        this.representationMetadataUpdateService = Objects.requireNonNull(representationMetadataUpdateService);
        this.representationContentSearchService = Objects.requireNonNull(representationContentSearchService);
        this.representationContentRepository = Objects.requireNonNull(representationContentRepository);
        this.identityService = Objects.requireNonNull(identityService);
        this.jdbcClient = Objects.requireNonNull(jdbcClient);
    }

    @Transactional
    @Override
    public void preProcess(IEditingContext editingContext) {
        var resources = this.getDiagramOnViewUsageMigrationParticipantResources(editingContext);
        // this.representationMetadataSearchService.findAllRepresentationMetadataBySemanticData(new
        // UUIDParser().parse(editingContext.getId()).map(AggregateReference<SemanticData, UUID>::to).get());
        // or eAdapter
        for (Resource resource : resources) {
            resource.getAllContents().forEachRemaining(eObject -> {
                if (eObject instanceof Element element && !(element instanceof ViewUsage)) {
                    var diagramsUUIDs = this.getDiagramsFromObjectId(this.identityService.getId(element));
                    for (UUID diagramUUID : diagramsUUIDs) {
                        var optRepresentationMetadata = this.representationMetadataSearchService.findMetadataById(diagramUUID);
                        if (optRepresentationMetadata.isPresent() && this.isSysONDiagram(optRepresentationMetadata.get())) {
                            var representationMetadata = optRepresentationMetadata.get();
                            var diagramName = representationMetadata.getLabel();
                            var viewUsage = this.getViewUsage(element, diagramName);
                            if (viewUsage != null) {
                                var optRepresentationContent = this.representationContentSearchService.findContentById(diagramUUID);
                                var representationContent = optRepresentationContent.get();
                                this.updateDiagramTarget(this.identityService.getId(viewUsage), representationMetadata, representationContent);
                            }
                        }
                    }
                }
            });
        }
    }

    private List<Resource> getDiagramOnViewUsageMigrationParticipantResources(IEditingContext editingContext) {
        List<Resource> resourcesWithDiagramOnViewUsageMigrationParticpant = new ArrayList<>();
        if (editingContext instanceof IEMFEditingContext emfEditingContext) {
            ResourceSet resourceSet = emfEditingContext.getDomain().getResourceSet();
            for (Resource resource : resourceSet.getResources().stream().filter(res -> !ElementUtil.isStandardLibraryResource(res)).toList()) {
                var optAdapter = resource.eAdapters().stream()
                        .filter(ResourceMetadataAdapter.class::isInstance)
                        .map(ResourceMetadataAdapter.class::cast)
                        .findFirst();
                if (optAdapter.isPresent()) {
                    var optMigrationData = optAdapter.get().getAllMigrationData().stream()
                            .filter(migData -> migData.migrationVersion().equals(DiagramOnViewUsageMigrationParticipant.PARTICIPANT_VERSION))
                            .findFirst();
                    if (optMigrationData.isPresent()) {
                        resourcesWithDiagramOnViewUsageMigrationParticpant.add(resource);
                    }
                }
            }
        }
        return resourcesWithDiagramOnViewUsageMigrationParticpant;
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

    private ViewUsage getViewUsage(Element container, String name) {
        return container.getOwnedElement().stream()
                .filter(ViewUsage.class::isInstance)
                .map(ViewUsage.class::cast)
                .filter(vu -> Objects.equals(vu.getDeclaredName(), name))
                .findFirst()
                .orElseGet(() -> null);
    }

    private void updateDiagramTarget(String targetObjectId, RepresentationMetadata representationMetadata, RepresentationContent representationContent) {
        this.representationMetadataUpdateService.updateTargetObjectId(null, representationMetadata.getId(), targetObjectId);

        var newContent = this.updateRepresentationContent(representationContent, targetObjectId);
        representationContent.updateContent(null, newContent);

        this.representationContentRepository.save(representationContent);
    }

    private String updateRepresentationContent(RepresentationContent representationContent, String newTargetObjectId) {
        return representationContent.getContent().replaceFirst("\"targetObjectId\":(\s)*\"[a-z0-9-]*\"", "\"targetObjectId\":\"" + newTargetObjectId + "\"");
    }
}
