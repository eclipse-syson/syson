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
import java.util.Optional;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventProcessorInitializationHook;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationContent;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.repositories.IRepresentationContentRepository;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataUpdateService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hook that allows to update the diagramDescription property of existing diagrams. This hook is coupled with the
 * migration participant {@link OneDiagramDescriptionMigrationParticipant}.
 *
 * @author arichard
 */
@Service
public class OneDiagramDescriptionMigrationHook implements IEditingContextEventProcessorInitializationHook {

    private final IRepresentationMetadataUpdateService representationMetadataUpdateService;

    private final IRepresentationContentRepository representationContentRepository;

    public OneDiagramDescriptionMigrationHook(IRepresentationMetadataUpdateService representationMetadataUpdateService, IRepresentationContentRepository representationContentRepository) {
        this.representationMetadataUpdateService = Objects.requireNonNull(representationMetadataUpdateService);
        this.representationContentRepository = Objects.requireNonNull(representationContentRepository);
    }

    @Transactional
    @Override
    public void preProcess(IEditingContext editingContext) {
        var resources = this.getOneDiagramDescriptionMigrationParticipantResources(editingContext);
        for (Resource resource : resources) {
            resource.getAllContents().forEachRemaining(eObject -> {
                if (eObject instanceof Element element) {
                    var adapter = this.isMigrationParticipantCandidate(element);
                    if (adapter.isPresent()) {
                        var migrationAdapter = adapter.get();
                        var representationMetadata = migrationAdapter.getRepresentationMetadata();
                        var representationContent = migrationAdapter.getRepresentationContent();
                        this.updateDiagramDescription(representationMetadata, representationContent);
                        element.eAdapters().remove(migrationAdapter);
                    }
                }
            });
        }
    }

    private List<Resource> getOneDiagramDescriptionMigrationParticipantResources(IEditingContext editingContext) {
        List<Resource> resourcesWithMigrationParticpant = new ArrayList<>();
        if (editingContext instanceof IEMFEditingContext emfEditingContext) {
            ResourceSet resourceSet = emfEditingContext.getDomain().getResourceSet();
            for (Resource resource : resourceSet.getResources().stream().filter(res -> !ElementUtil.isStandardLibraryResource(res)).toList()) {
                var optAdapter = resource.eAdapters().stream()
                        .filter(ResourceMetadataAdapter.class::isInstance)
                        .map(ResourceMetadataAdapter.class::cast)
                        .findFirst();
                if (optAdapter.isPresent()) {
                    var optMigrationData = optAdapter.get().getAllMigrationData().stream()
                            .filter(migData -> migData.migrationVersion().equals(OneDiagramDescriptionMigrationParticipant.PARTICIPANT_VERSION))
                            .findFirst();
                    if (optMigrationData.isPresent()) {
                        resourcesWithMigrationParticpant.add(resource);
                    }
                }
            }
        }
        return resourcesWithMigrationParticpant;
    }

    private Optional<OneDiagramDescriptionMigrationAdapter> isMigrationParticipantCandidate(Element element) {
        return element.eAdapters().stream()
                .filter(OneDiagramDescriptionMigrationAdapter.class::isInstance)
                .map(OneDiagramDescriptionMigrationAdapter.class::cast)
                .findFirst();
    }

    private void updateDiagramDescription(RepresentationMetadata representationMetadata, RepresentationContent representationContent) {
        this.representationMetadataUpdateService.updateDescriptionId(null, representationMetadata.getId(), SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);

        var newContent = this.updateRepresentationContent(representationContent);
        representationContent.updateContent(null, newContent);

        this.representationContentRepository.save(representationContent);
    }

    private String updateRepresentationContent(RepresentationContent representationContent) {
        return representationContent.getContent().replaceFirst(
                "\"descriptionId\":(\s)*\"\\QsiriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=\\E[a-z0-9-]*\\Q&sourceElementId=\\E[a-z0-9-]*\"",
                "\"descriptionId\":\"" + SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID + "\"");
    }
}
