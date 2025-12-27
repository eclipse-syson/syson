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
package org.eclipse.syson.application.sysmlv2;

import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.collaborative.api.IRepresentationMetadataPersistenceService;
import org.eclipse.sirius.components.collaborative.api.IRepresentationPersistenceService;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramCreationService;
import org.eclipse.sirius.components.core.RepresentationMetadata;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextPersistenceService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.description.DiagramDescription;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.events.SemanticDataUpdatedEvent;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.util.StandardDiagramsConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Used to create the representation of a newly created SysMLv2 project.
 *
 * @author arichard
 */
@Service
public class SysMLv2TemplatesRepresentationInitializer {

    private final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    private final IDiagramCreationService diagramCreationService;

    private final IRepresentationMetadataPersistenceService representationMetadataPersistenceService;

    private final IRepresentationPersistenceService representationPersistenceService;

    private final IEditingContextPersistenceService editingContextPersistenceService;

    private final ElementUtil elementUtil = new ElementUtil();

    public SysMLv2TemplatesRepresentationInitializer(IRepresentationDescriptionSearchService representationDescriptionSearchService, IDiagramCreationService diagramCreationService,
            IRepresentationMetadataPersistenceService representationMetadataPersistenceService, IRepresentationPersistenceService representationPersistenceService,
            IEditingContextPersistenceService editingContextPersistenceService) {
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
        this.diagramCreationService = Objects.requireNonNull(diagramCreationService);
        this.representationMetadataPersistenceService = Objects.requireNonNull(representationMetadataPersistenceService);
        this.representationPersistenceService = Objects.requireNonNull(representationPersistenceService);
        this.editingContextPersistenceService = Objects.requireNonNull(editingContextPersistenceService);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void onSemanticDataUpdatedEvent(SemanticDataUpdatedEvent semanticDataUpdatedEvent) {
        if (semanticDataUpdatedEvent.causedBy() instanceof SysMLv2TemplatesInitialization templateInitialization) {
            var editingContext = templateInitialization.editingContext();
            var resource = templateInitialization.resource();

            // General View is the description name of SDVDiagramDescriptionProvider
            var optionalGeneralViewDiagram = this.findDiagramDescription(editingContext, StandardDiagramsConstants.GV);
            if (optionalGeneralViewDiagram.isPresent()) {
                DiagramDescription generalViewDiagram = optionalGeneralViewDiagram.get();
                var optViewUsage = this.getOrCreateViewUsage(resource);
                if (optViewUsage.isPresent()) {
                    var variableManager = new VariableManager();
                    var viewUsage = optViewUsage.get();
                    variableManager.put(VariableManager.SELF, viewUsage);
                    variableManager.put(DiagramDescription.LABEL, viewUsage.getDeclaredName());
                    var diagram = this.diagramCreationService.create(editingContext, generalViewDiagram, viewUsage);
                    var label = generalViewDiagram.getLabelProvider().apply(variableManager);
                    var iconURLs = generalViewDiagram.getIconURLsProvider().apply(variableManager);

                    var representationMetadata = RepresentationMetadata.newRepresentationMetadata(diagram.getId())
                            .kind(diagram.getKind())
                            .label(label)
                            .descriptionId(diagram.getDescriptionId())
                            .iconURLs(iconURLs)
                            .build();

                    this.editingContextPersistenceService.persist(semanticDataUpdatedEvent, editingContext);
                    this.representationMetadataPersistenceService.save(semanticDataUpdatedEvent, editingContext, representationMetadata, diagram.getTargetObjectId());
                    this.representationPersistenceService.save(semanticDataUpdatedEvent, editingContext, diagram);
                }
            }
        }
    }

    private Optional<DiagramDescription> findDiagramDescription(IEditingContext editingContext, String label) {
        return this.representationDescriptionSearchService.findAll(editingContext).values().stream()
                .filter(DiagramDescription.class::isInstance)
                .map(DiagramDescription.class::cast)
                .filter(diagramDescription -> Objects.equals(label, diagramDescription.getLabel()))
                .findFirst();
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

    private Optional<ViewUsage> getOrCreateViewUsage(Resource resource) {
        Optional<Package> optRootPackage = this.getRootPackage(resource);
        if (optRootPackage.isPresent()) {
            var rootPackage = optRootPackage.get();
            var viewUsage = this.getViewUsage(rootPackage);
            if (viewUsage.isEmpty()) {
                viewUsage = this.createViewUsage(rootPackage);
            }
            return viewUsage;
        }
        return Optional.empty();
    }

    private Optional<ViewUsage> getViewUsage(Element element) {
        return element.getOwnedElement().stream()
                .filter(ViewUsage.class::isInstance)
                .map(ViewUsage.class::cast)
                .filter(vu -> Objects.equals(vu.getDeclaredName(), "view1"))
                .findFirst();
    }

    private Optional<ViewUsage> createViewUsage(Element element) {
        var viewUsageMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        var viewUsage = SysmlFactory.eINSTANCE.createViewUsage();
        element.getOwnedRelationship().add(viewUsageMembership);
        viewUsageMembership.getOwnedRelatedElement().add(viewUsage);
        viewUsage.setDeclaredName("view1");
        viewUsage.setElementId(ElementUtil.generateUUID(viewUsage).toString());

        var featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
        viewUsage.getOwnedRelationship().add(featureTyping);
        var generalViewViewDef = this.elementUtil.findByNameAndType(element, StandardDiagramsConstants.GV_QN, ViewDefinition.class);
        featureTyping.setType(generalViewViewDef);
        featureTyping.setTypedFeature(viewUsage);

        return Optional.ofNullable(viewUsage);
    }
}
