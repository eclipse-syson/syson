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
package org.eclipse.syson.application.sysmlv2;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.sirius.components.collaborative.api.IRepresentationPersistenceService;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramCreationService;
import org.eclipse.sirius.components.core.RepresentationMetadata;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.description.DiagramDescription;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.application.project.services.api.IProjectTemplateInitializer;
import org.eclipse.syson.application.sysmlv2.api.IDefaultSysMLv2ResourceProvider;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Package;
import org.springframework.stereotype.Service;

/**
 * Provides SysMLv2-specific project templates initializers.
 *
 * @author arichard
 */
@Service
public class SysMLv2ProjectTemplatesInitializer implements IProjectTemplateInitializer {

    private static final String SYSMLV2_DOCUMENT_NAME = "SysMLv2.sysml";

    private static final String BATMOBILE_DOCUMENT_NAME = "Batmobile.sysml";

    private final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    private final IDiagramCreationService diagramCreationService;

    private final IRepresentationPersistenceService representationPersistenceService;

    private final IDefaultSysMLv2ResourceProvider defaultSysMLv2ResourceProvider;

    public SysMLv2ProjectTemplatesInitializer(IRepresentationDescriptionSearchService representationDescriptionSearchService, IDiagramCreationService diagramCreationService,
            IRepresentationPersistenceService representationPersistenceService, IDefaultSysMLv2ResourceProvider defaultSysMLv2ResourceProvider) {
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
        this.diagramCreationService = Objects.requireNonNull(diagramCreationService);
        this.representationPersistenceService = Objects.requireNonNull(representationPersistenceService);
        this.defaultSysMLv2ResourceProvider = Objects.requireNonNull(defaultSysMLv2ResourceProvider);
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
        return this.getResourceSet(editingContext)
                .flatMap(resourceSet -> {
                    Optional<RepresentationMetadata> result = Optional.empty();
                    var resource = this.defaultSysMLv2ResourceProvider.getDefaultSysMLv2Resource(UUID.randomUUID(), SYSMLV2_DOCUMENT_NAME);
                    resourceSet.getResources().add(resource);

                    var optionalGeneralViewDiagram = this.findDiagramDescription(editingContext, "General View");
                    if (optionalGeneralViewDiagram.isPresent()) {
                        DiagramDescription generalViewDiagram = optionalGeneralViewDiagram.get();
                        var semanticTarget = this.getRootPackage(resource);
                        if (semanticTarget.isPresent()) {
                            Diagram diagram = this.diagramCreationService.create("General View", semanticTarget.get(), generalViewDiagram, editingContext);
                            this.representationPersistenceService.save(editingContext, diagram);
                            result = Optional.of(new RepresentationMetadata(diagram.getId(), diagram.getKind(), diagram.getLabel(), diagram.getDescriptionId()));
                        }
                    }
                    return result;
                });
    }

    private Optional<RepresentationMetadata> initializeBatmobileProject(IEditingContext editingContext) {
        return this.getResourceSet(editingContext)
                .flatMap(resourceSet -> {
                    Optional<RepresentationMetadata> result = Optional.empty();
                    var resource = this.defaultSysMLv2ResourceProvider.getEmptyResource(UUID.randomUUID(), BATMOBILE_DOCUMENT_NAME);
                    resourceSet.getResources().add(resource);

                    // Load after adding the resource to the resourceSet, to be sure that references will be resolved.
                    this.defaultSysMLv2ResourceProvider.loadBatmobileResource(resource);

                    var optionalGeneralViewDiagram = this.findDiagramDescription(editingContext, "General View");
                    if (optionalGeneralViewDiagram.isPresent()) {
                        DiagramDescription generalViewDiagram = optionalGeneralViewDiagram.get();
                        var semanticTarget = this.getRootPackage(resource);
                        if (semanticTarget.isPresent()) {
                            Diagram diagram = this.diagramCreationService.create("General View", semanticTarget.get(), generalViewDiagram, editingContext);
                            this.representationPersistenceService.save(editingContext, diagram);
                            result = Optional.of(new RepresentationMetadata(diagram.getId(), diagram.getKind(), diagram.getLabel(), diagram.getDescriptionId()));
                        }
                    }
                    return result;
                });
    }

    private Optional<DiagramDescription> findDiagramDescription(IEditingContext editingContext, String label) {
        return this.representationDescriptionSearchService.findAll(editingContext).values().stream()
                .filter(DiagramDescription.class::isInstance)
                .map(DiagramDescription.class::cast)
                .filter(diagramDescription -> Objects.equals(label, diagramDescription.getLabel()))
                .findFirst();
    }

    private Optional<ResourceSet> getResourceSet(IEditingContext editingContext) {
        return Optional.of(editingContext)
                .filter(IEMFEditingContext.class::isInstance)
                .map(IEMFEditingContext.class::cast)
                .map(IEMFEditingContext::getDomain)
                .map(AdapterFactoryEditingDomain::getResourceSet);
    }

    private Optional<Package> getRootPackage(Resource resource) {
        Object rootElement = resource.getContents().get(0);
        if (rootElement instanceof Namespace rootNamespace) {
            Element rootMember = rootNamespace.getOwnedMember().get(0);
            if (rootMember instanceof Package rootPackage) {
                return Optional.of(rootPackage);
            }
        }
        return Optional.empty();
    }
}
