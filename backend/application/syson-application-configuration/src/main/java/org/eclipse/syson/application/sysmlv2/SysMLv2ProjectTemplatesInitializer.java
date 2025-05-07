/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.sirius.components.collaborative.api.IRepresentationMetadataPersistenceService;
import org.eclipse.sirius.components.collaborative.api.IRepresentationPersistenceService;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramCreationService;
import org.eclipse.sirius.components.core.RepresentationMetadata;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.description.DiagramDescription;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.events.ICause;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.web.application.project.services.api.IProjectTemplateInitializer;
import org.eclipse.syson.application.sysmlv2.api.IDefaultSysMLv2ResourceProvider;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.springframework.stereotype.Service;

/**
 * Provides SysMLv2-specific project templates initializers.
 *
 * @author arichard
 */
@Service
public class SysMLv2ProjectTemplatesInitializer implements IProjectTemplateInitializer {

    private static final String SYSMLV2_DOCUMENT_NAME = "SysMLv2.sysml";

    private static final String SYSMLV2_LIBRARY_DOCUMENT_NAME = "SysMLv2-Library.sysml";

    private static final String BATMOBILE_DOCUMENT_NAME = "Batmobile.sysml";

    private final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    private final IDiagramCreationService diagramCreationService;

    private final IRepresentationPersistenceService representationPersistenceService;

    private final IRepresentationMetadataPersistenceService representationMetadataPersistenceService;

    private final IDefaultSysMLv2ResourceProvider defaultSysMLv2ResourceProvider;

    private final ElementUtil elementUtil = new ElementUtil();

    public SysMLv2ProjectTemplatesInitializer(IRepresentationDescriptionSearchService representationDescriptionSearchService, IDiagramCreationService diagramCreationService,
            IRepresentationPersistenceService representationPersistenceService, IRepresentationMetadataPersistenceService representationMetadataPersistenceService,
            IDefaultSysMLv2ResourceProvider defaultSysMLv2ResourceProvider) {
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
        this.diagramCreationService = Objects.requireNonNull(diagramCreationService);
        this.representationPersistenceService = Objects.requireNonNull(representationPersistenceService);
        this.representationMetadataPersistenceService = Objects.requireNonNull(representationMetadataPersistenceService);
        this.defaultSysMLv2ResourceProvider = Objects.requireNonNull(defaultSysMLv2ResourceProvider);
    }

    @Override
    public boolean canHandle(String templateId) {
        return SysMLv2ProjectTemplatesProvider.SYSMLV2_TEMPLATE_ID.equals(templateId) || SysMLv2ProjectTemplatesProvider.SYSMLV2_LIBRARY_TEMPLATE_ID.equals(templateId)
                || SysMLv2ProjectTemplatesProvider.BATMOBILE_TEMPLATE_ID.equals(templateId);
    }

    @Override
    public Optional<RepresentationMetadata> handle(ICause cause, String projectTemplateId, IEditingContext editingContext) {
        Optional<RepresentationMetadata> project = Optional.empty();
        if (SysMLv2ProjectTemplatesProvider.SYSMLV2_TEMPLATE_ID.equals(projectTemplateId)) {
            project = this.initializeSysMLv2Project(cause, editingContext);
        } else if (SysMLv2ProjectTemplatesProvider.SYSMLV2_LIBRARY_TEMPLATE_ID.equals(projectTemplateId)) {
            project = this.initializeSysMLv2LibraryProject(cause, editingContext);
        } else if (SysMLv2ProjectTemplatesProvider.BATMOBILE_TEMPLATE_ID.equals(projectTemplateId)) {
            project = this.initializeBatmobileProject(cause, editingContext);
        }
        return project;
    }

    private Optional<RepresentationMetadata> initializeSysMLv2Project(ICause cause, IEditingContext editingContext) {
        return this.getResourceSet(editingContext)
                .flatMap(resourceSet -> {
                    Optional<RepresentationMetadata> result = Optional.empty();
                    var resource = this.defaultSysMLv2ResourceProvider.getDefaultSysMLv2Resource(UUID.randomUUID(), SYSMLV2_DOCUMENT_NAME);
                    resourceSet.getResources().add(resource);

                    var optionalGeneralViewDiagram = this.findDiagramDescription(editingContext, "General View");
                    if (optionalGeneralViewDiagram.isPresent()) {
                        DiagramDescription generalViewDiagram = optionalGeneralViewDiagram.get();
                        var viewUsage = this.getViewUsage(resource);
                        if (viewUsage.isPresent()) {
                            var variableManager = new VariableManager();
                            variableManager.put(VariableManager.SELF, viewUsage);
                            variableManager.put(DiagramDescription.LABEL, generalViewDiagram.getLabel());
                            String label = generalViewDiagram.getLabelProvider().apply(variableManager);
                            Diagram diagram = this.diagramCreationService.create(viewUsage.get(), generalViewDiagram, editingContext);
                            List<String> iconURLs = generalViewDiagram.getIconURLsProvider().apply(variableManager);

                            var representationMetadata = RepresentationMetadata.newRepresentationMetadata(diagram.getId())
                                    .kind(diagram.getKind())
                                    .label(label)
                                    .descriptionId(diagram.getDescriptionId())
                                    .iconURLs(iconURLs)
                                    .build();

                            this.representationMetadataPersistenceService.save(cause, editingContext, representationMetadata, diagram.getTargetObjectId());
                            this.representationPersistenceService.save(cause, editingContext, diagram);
                            result = Optional.of(representationMetadata);
                        }
                    }
                    return result;
                });
    }

    private Optional<RepresentationMetadata> initializeSysMLv2LibraryProject(ICause cause, IEditingContext editingContext) {
        return this.getResourceSet(editingContext)
                .flatMap(resourceSet -> {
                    Optional<RepresentationMetadata> result = Optional.empty();
                    var resource = this.defaultSysMLv2ResourceProvider.getDefaultSysMLv2LibraryResource(UUID.randomUUID(), SYSMLV2_LIBRARY_DOCUMENT_NAME);
                    resourceSet.getResources().add(resource);

                    var optionalGeneralViewDiagram = this.findDiagramDescription(editingContext, "General View");
                    if (optionalGeneralViewDiagram.isPresent()) {
                        DiagramDescription generalViewDiagram = optionalGeneralViewDiagram.get();
                        var viewUsage = this.getViewUsage(resource);
                        if (viewUsage.isPresent()) {
                            var variableManager = new VariableManager();
                            variableManager.put(VariableManager.SELF, viewUsage);
                            variableManager.put(DiagramDescription.LABEL, generalViewDiagram.getLabel());
                            String label = generalViewDiagram.getLabelProvider().apply(variableManager);
                            Diagram diagram = this.diagramCreationService.create(viewUsage.get(), generalViewDiagram, editingContext);
                            List<String> iconURLs = generalViewDiagram.getIconURLsProvider().apply(variableManager);

                            var representationMetadata = RepresentationMetadata.newRepresentationMetadata(diagram.getId())
                                    .kind(diagram.getKind())
                                    .label(label)
                                    .descriptionId(diagram.getDescriptionId())
                                    .iconURLs(iconURLs)
                                    .build();

                            this.representationMetadataPersistenceService.save(cause, editingContext, representationMetadata, diagram.getTargetObjectId());
                            this.representationPersistenceService.save(cause, editingContext, diagram);
                            result = Optional.of(representationMetadata);
                        }
                    }
                    return result;
                });
    }

    private Optional<RepresentationMetadata> initializeBatmobileProject(ICause cause, IEditingContext editingContext) {
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
                        var viewUsage = this.getViewUsage(resource);
                        if (viewUsage.isPresent()) {
                            var variableManager = new VariableManager();
                            variableManager.put(VariableManager.SELF, viewUsage);
                            variableManager.put(DiagramDescription.LABEL, generalViewDiagram.getLabel());
                            String label = generalViewDiagram.getLabelProvider().apply(variableManager);
                            Diagram diagram = this.diagramCreationService.create(viewUsage.get(), generalViewDiagram, editingContext);
                            List<String> iconURLs = generalViewDiagram.getIconURLsProvider().apply(variableManager);

                            var representationMetadata = RepresentationMetadata.newRepresentationMetadata(diagram.getId())
                                    .kind(diagram.getKind())
                                    .label(label)
                                    .descriptionId(diagram.getDescriptionId())
                                    .iconURLs(iconURLs)
                                    .build();

                            this.representationMetadataPersistenceService.save(cause, editingContext, representationMetadata, diagram.getTargetObjectId());
                            this.representationPersistenceService.save(cause, editingContext, diagram);
                            result = Optional.of(representationMetadata);
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

    private Optional<ViewUsage> getViewUsage(Resource resource) {
        Optional<Package> rootPackage = this.getRootPackage(resource);
        if (rootPackage.isPresent()) {
            var viewUsage = this.createViewUsage(rootPackage.get());
            return Optional.ofNullable(viewUsage);
        }
        return Optional.empty();
    }

    private ViewUsage createViewUsage(Element element) {
        var viewUsageMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        var viewUsage = SysmlFactory.eINSTANCE.createViewUsage();
        element.getOwnedRelationship().add(viewUsageMembership);
        viewUsageMembership.getOwnedRelatedElement().add(viewUsage);
        viewUsage.setDeclaredName("General View");
        viewUsage.setElementId(ElementUtil.generateUUID(viewUsage).toString());

        var featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
        viewUsage.getOwnedRelationship().add(featureTyping);
        var generalViewViewDef = this.elementUtil.findByNameAndType(element, "StandardViewDefinitions::GeneralView", ViewDefinition.class);
        featureTyping.setType(generalViewViewDef);
        featureTyping.setTypedFeature(viewUsage);

        return viewUsage;
    }
}
