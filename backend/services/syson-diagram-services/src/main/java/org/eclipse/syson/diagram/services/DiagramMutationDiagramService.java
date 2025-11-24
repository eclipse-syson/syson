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
package org.eclipse.syson.diagram.services;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.api.IRepresentationMetadataPersistenceService;
import org.eclipse.sirius.components.collaborative.api.IRepresentationPersistenceService;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramCreationService;
import org.eclipse.sirius.components.core.RepresentationMetadata;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.description.DiagramDescription;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.springframework.stereotype.Service;

/**
 * Diagram-related services doing mutations in diagrams and models.
 *
 * @author arichard
 */
@Service
public class DiagramMutationDiagramService {

    private final IDiagramCreationService diagramCreationService;

    private final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    private final IRepresentationMetadataPersistenceService representationMetadataPersistenceService;

    private final IRepresentationPersistenceService representationPersistenceService;

    public DiagramMutationDiagramService(IDiagramCreationService diagramCreationService, IRepresentationDescriptionSearchService representationDescriptionSearchService,
            IRepresentationMetadataPersistenceService representationMetadataPersistenceService, IRepresentationPersistenceService representationPersistenceService) {
        this.diagramCreationService = Objects.requireNonNull(diagramCreationService);
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
        this.representationMetadataPersistenceService = Objects.requireNonNull(representationMetadataPersistenceService);
        this.representationPersistenceService = Objects.requireNonNull(representationPersistenceService);
    }

    /**
     * Create a General View diagram on the given {@link Element} if it is a ViewUsage, do nothing otherwise.
     *
     * @param element
     *            the current context of the service.
     * @param editingContext
     *            the given {@link IEditingContext} in which this service has been called.
     * @return create a General View diagram on the given {@link Element} if it is a ViewUsage, do nothing otherwise.
     */
    public Element createDiagram(Element element, IEditingContext editingContext) {
        if (element instanceof ViewUsage viewUsage) {
            this.representationDescriptionSearchService.findById(editingContext, SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID)
                    .filter(DiagramDescription.class::isInstance)
                    .map(DiagramDescription.class::cast)
                    .ifPresent(diagramDescription -> {
                        var variableManager = new VariableManager();
                        variableManager.put(VariableManager.SELF, viewUsage);
                        variableManager.put(DiagramDescription.LABEL, viewUsage.getDeclaredName());
                        String label = diagramDescription.getLabelProvider().apply(variableManager);
                        List<String> iconURLs = diagramDescription.getIconURLsProvider().apply(variableManager);

                        Diagram diagram = this.diagramCreationService.create(viewUsage, diagramDescription, editingContext);
                        var representationMetadata = RepresentationMetadata.newRepresentationMetadata(diagram.getId())
                                .kind(diagram.getKind())
                                .label(label)
                                .descriptionId(diagram.getDescriptionId())
                                .iconURLs(iconURLs)
                                .build();
                        this.representationMetadataPersistenceService.save(null, editingContext, representationMetadata, diagram.getTargetObjectId());
                        this.representationPersistenceService.save(null, editingContext, diagram);
                    });
        }
        return element;
    }
}
