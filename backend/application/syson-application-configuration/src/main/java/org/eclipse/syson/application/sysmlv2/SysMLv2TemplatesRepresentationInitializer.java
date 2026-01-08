/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
import org.eclipse.sirius.components.core.api.IEditingContextPersistenceService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.events.SemanticDataUpdatedEvent;
import org.eclipse.syson.diagram.services.DiagramMutationDiagramService;
import org.eclipse.syson.model.services.ModelMutationElementService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.util.StandardDiagramsConstants;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
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

    private final IEditingContextPersistenceService editingContextPersistenceService;

    private final DiagramMutationDiagramService diagramMutationDiagramService;

    private final ModelMutationElementService modelMutationElementService;

    public SysMLv2TemplatesRepresentationInitializer(DiagramMutationDiagramService diagramMutationDiagramService, ModelMutationElementService modelMutationElementService,
            IEditingContextPersistenceService editingContextPersistenceService) {
        this.diagramMutationDiagramService = Objects.requireNonNull(diagramMutationDiagramService);
        this.modelMutationElementService = Objects.requireNonNull(modelMutationElementService);
        this.editingContextPersistenceService = Objects.requireNonNull(editingContextPersistenceService);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void onSemanticDataUpdatedEvent(SemanticDataUpdatedEvent semanticDataUpdatedEvent) {
        if (semanticDataUpdatedEvent.causedBy() instanceof SysMLv2TemplatesInitialization templateInitialization) {
            var editingContext = templateInitialization.editingContext();
            var resource = templateInitialization.resource();

            var optViewUsage = this.getOrCreateViewUsage(resource);

            if (optViewUsage.isPresent()) {
                this.diagramMutationDiagramService.createDiagram(optViewUsage.get(), editingContext, SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
                this.editingContextPersistenceService.persist(semanticDataUpdatedEvent, editingContext);
            }
        }
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
                viewUsage = this.modelMutationElementService.createViewUsage(rootPackage, "view1");
                viewUsage.ifPresent(view -> {
                    this.modelMutationElementService.featureTypeViewUsage(view, StandardDiagramsConstants.GV_QN);
                });
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
}
