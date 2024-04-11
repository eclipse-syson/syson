/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
package org.eclipse.syson.application.configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.sirius.components.collaborative.api.IRepresentationPersistenceService;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramCreationService;
import org.eclipse.sirius.components.core.RepresentationMetadata;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.description.DiagramDescription;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.sirius.web.persistence.entities.DocumentEntity;
import org.eclipse.sirius.web.persistence.repositories.IDocumentRepository;
import org.eclipse.sirius.web.persistence.repositories.IProjectRepository;
import org.eclipse.sirius.web.services.api.id.IDParser;
import org.eclipse.sirius.web.services.api.projects.IProjectTemplateInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import io.micrometer.core.instrument.MeterRegistry;

/**
 * Provides SysMLv2-specific project templates initializers.
 *
 * @author arichard
 */
@Configuration
public class SysMLv2ProjectTemplatesInitializer implements IProjectTemplateInitializer {

    private static final String SYSMLV2_DOCUMENT_NAME = "SysMLv2";

    private static final String BATMOBILE_DOCUMENT_NAME = "Batmobile";

    private final Logger logger = LoggerFactory.getLogger(SysMLv2ProjectTemplatesInitializer.class);

    private final IProjectRepository projectRepository;

    private final IDocumentRepository documentRepository;

    private final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    private final IDiagramCreationService diagramCreationService;

    private final IRepresentationPersistenceService representationPersistenceService;

    private final StereotypeBuilder stereotypeBuilder;

    public SysMLv2ProjectTemplatesInitializer(IProjectRepository projectRepository, IDocumentRepository documentRepository,
            IRepresentationDescriptionSearchService representationDescriptionSearchService, IDiagramCreationService diagramCreationService,
            IRepresentationPersistenceService representationPersistenceService, MeterRegistry meterRegistry) {
        this.projectRepository = Objects.requireNonNull(projectRepository);
        this.documentRepository = Objects.requireNonNull(documentRepository);
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
        this.diagramCreationService = Objects.requireNonNull(diagramCreationService);
        this.representationPersistenceService = Objects.requireNonNull(representationPersistenceService);
        this.stereotypeBuilder = new StereotypeBuilder("studio-template-initializer", meterRegistry);
    }

    @Override
    public boolean canHandle(String templateId) {
        return SysMLv2ProjectTemplatesProvider.SYSMLV2_TEMPLATE_ID.equals(templateId) || SysMLv2ProjectTemplatesProvider.BATMOBILE_TEMPLATE_ID.equals(templateId);
    }

    @Override
    public Optional<RepresentationMetadata> handle(String templateId, IEditingContext editingContext) {
        Optional<RepresentationMetadata> project = Optional.empty();
        if (SysMLv2ProjectTemplatesProvider.SYSMLV2_TEMPLATE_ID.equals(templateId)) {
            project = this.initializeSysMLv2Project(editingContext);
        } else if (SysMLv2ProjectTemplatesProvider.BATMOBILE_TEMPLATE_ID.equals(templateId)) {
            project = this.initializeBatmobileProject(editingContext);
        }
        return project;
    }

    private Optional<RepresentationMetadata> initializeSysMLv2Project(IEditingContext editingContext) {
        Optional<RepresentationMetadata> result = Optional.empty();
        Optional<AdapterFactoryEditingDomain> optionalEditingDomain = Optional.of(editingContext)
                .filter(IEMFEditingContext.class::isInstance)
                .map(IEMFEditingContext.class::cast)
                .map(IEMFEditingContext::getDomain);
        Optional<UUID> editingContextUUID = new IDParser().parse(editingContext.getId());
        if (optionalEditingDomain.isPresent() && editingContextUUID.isPresent()) {
            AdapterFactoryEditingDomain adapterFactoryEditingDomain = optionalEditingDomain.get();
            ResourceSet resourceSet = adapterFactoryEditingDomain.getResourceSet();

            var optionalSysMLv2DocumentEntity = this.projectRepository.findById(editingContextUUID.get()).map(projectEntity -> {
                DocumentEntity documentEntity = new DocumentEntity();
                documentEntity.setProject(projectEntity);
                documentEntity.setName(SYSMLV2_DOCUMENT_NAME);
                documentEntity.setContent(this.getSysMLv2Content());

                documentEntity = this.documentRepository.save(documentEntity);
                return documentEntity;
            });

            if (optionalSysMLv2DocumentEntity.isPresent()) {
                DocumentEntity documentEntity = optionalSysMLv2DocumentEntity.get();

                JsonResource resource = new JSONResourceFactory().createResourceFromPath(documentEntity.getId().toString());
                try (var inputStream = new ByteArrayInputStream(documentEntity.getContent().getBytes())) {
                    resource.load(inputStream, null);

                    var optionalGeneralViewDiagram = this.findDiagramDescription(editingContext, "General View");
                    if (optionalGeneralViewDiagram.isPresent()) {
                        DiagramDescription generalViewDiagram = optionalGeneralViewDiagram.get();
                        Object semanticTarget = resource.getContents().get(0);

                        Diagram diagram = this.diagramCreationService.create("General View", semanticTarget, generalViewDiagram, editingContext);
                        this.representationPersistenceService.save(editingContext, diagram);

                        result = Optional.of(new RepresentationMetadata(diagram.getId(), diagram.getKind(), diagram.getLabel(), diagram.getDescriptionId()));
                    }
                } catch (IOException exception) {
                    this.logger.warn(exception.getMessage(), exception);
                }

                resource.eAdapters().add(new ResourceMetadataAdapter(SYSMLV2_DOCUMENT_NAME));

                resourceSet.getResources().add(resource);
            }
        }
        return result;
    }

    private Optional<RepresentationMetadata> initializeBatmobileProject(IEditingContext editingContext) {
        Optional<RepresentationMetadata> result = Optional.empty();
        Optional<AdapterFactoryEditingDomain> optionalEditingDomain = Optional.of(editingContext)
                .filter(IEMFEditingContext.class::isInstance)
                .map(IEMFEditingContext.class::cast)
                .map(IEMFEditingContext::getDomain);
        Optional<UUID> editingContextUUID = new IDParser().parse(editingContext.getId());
        if (optionalEditingDomain.isPresent() && editingContextUUID.isPresent()) {
            AdapterFactoryEditingDomain adapterFactoryEditingDomain = optionalEditingDomain.get();
            ResourceSet resourceSet = adapterFactoryEditingDomain.getResourceSet();

            var optionalBatmobileDocumentEntity = this.projectRepository.findById(editingContextUUID.get()).map(projectEntity -> {
                DocumentEntity documentEntity = new DocumentEntity();
                documentEntity.setProject(projectEntity);
                documentEntity.setName(BATMOBILE_DOCUMENT_NAME);
                documentEntity.setContent(this.getBatmobileContent());

                documentEntity = this.documentRepository.save(documentEntity);
                return documentEntity;
            });

            if (optionalBatmobileDocumentEntity.isPresent()) {
                DocumentEntity documentEntity = optionalBatmobileDocumentEntity.get();

                JsonResource resource = new JSONResourceFactory().createResourceFromPath(documentEntity.getId().toString());
                try (var inputStream = new ByteArrayInputStream(documentEntity.getContent().getBytes())) {
                    resource.load(inputStream, null);

                    var optionalGeneralViewDiagram = this.findDiagramDescription(editingContext, "General View");
                    if (optionalGeneralViewDiagram.isPresent()) {
                        DiagramDescription generalViewDiagram = optionalGeneralViewDiagram.get();
                        Object semanticTarget = resource.getContents().get(0);

                        Diagram diagram = this.diagramCreationService.create("General View", semanticTarget, generalViewDiagram, editingContext);

                        this.representationPersistenceService.save(editingContext, diagram);

                        result = Optional.of(new RepresentationMetadata(diagram.getId(), diagram.getKind(), diagram.getLabel(), diagram.getDescriptionId()));
                    }
                } catch (IOException exception) {
                    this.logger.warn(exception.getMessage(), exception);
                }

                resource.eAdapters().add(new ResourceMetadataAdapter(BATMOBILE_DOCUMENT_NAME));

                resourceSet.getResources().add(resource);
            }
        }
        return result;
    }

    private Optional<DiagramDescription> findDiagramDescription(IEditingContext editingContext, String label) {
        return this.representationDescriptionSearchService.findAll(editingContext).values().stream()
                .filter(DiagramDescription.class::isInstance)
                .map(DiagramDescription.class::cast)
                .filter(diagramDescription -> Objects.equals(label, diagramDescription.getLabel()))
                .findFirst();
    }

    private String getSysMLv2Content() {
        return this.stereotypeBuilder.getStereotypeBody(StereotypeRegistryConfigurer.getEmptySysMLv2Content());
    }

    private String getBatmobileContent() {
        return this.stereotypeBuilder.getStereotypeBodyFromJSONResource(new ClassPathResource("templates/Batmobile.json"));
    }
}