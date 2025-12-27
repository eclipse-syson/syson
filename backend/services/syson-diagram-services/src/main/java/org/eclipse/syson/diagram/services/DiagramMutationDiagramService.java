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
import java.util.Map;
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.api.IRepresentationMetadataPersistenceService;
import org.eclipse.sirius.components.collaborative.api.IRepresentationPersistenceService;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramCreationService;
import org.eclipse.sirius.components.core.RepresentationMetadata;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.DiagramDescription;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.emf.utils.SiriusEMFCopier;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.syson.model.services.ModelMutationElementService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.util.NodeFinder;
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
    private final ModelMutationElementService modelMutationElementService;
    private final DiagramMutationExposeService diagramMutationExposeService;

    public DiagramMutationDiagramService(IDiagramCreationService diagramCreationService, IRepresentationDescriptionSearchService representationDescriptionSearchService,
            IRepresentationMetadataPersistenceService representationMetadataPersistenceService, IRepresentationPersistenceService representationPersistenceService,
            ModelMutationElementService modelMutationElementService, DiagramMutationExposeService diagramMutationExposeService) {
        this.diagramCreationService = Objects.requireNonNull(diagramCreationService);
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
        this.representationMetadataPersistenceService = Objects.requireNonNull(representationMetadataPersistenceService);
        this.representationPersistenceService = Objects.requireNonNull(representationPersistenceService);
        this.modelMutationElementService = Objects.requireNonNull(modelMutationElementService);
        this.diagramMutationExposeService = Objects.requireNonNull(diagramMutationExposeService);
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

                        Diagram diagram = this.diagramCreationService.create(editingContext, diagramDescription, viewUsage);
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

    /**
     * Duplicates the given element (with its owning Relationship) and exposes the duplicated element. Note that this method will do nothing on {@link org.eclipse.syson.sysml.Relationship}.
     *
     * @param element
     *         the element to duplicate
     * @param editingContext
     *         the {@link IEditingContext}
     * @param diagramContext
     *         the {@link DiagramContext}
     * @param node
     *         the node that will be duplicated
     * @param convertedNodes
     *         the map of the converted nodes for this diagram
     * @return the duplicated element if any
     */
    public Element duplicateElementAndExpose(Element element, IEditingContext editingContext, DiagramContext diagramContext, Node node, Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        OwningMembership owningMembership = element.getOwningMembership();
        Element result = null;
        if (owningMembership != null) {
            OwningMembership copiedMembership = (OwningMembership) new SiriusEMFCopier().copyWithoutContent(owningMembership);
            var optDuplicate = this.modelMutationElementService.duplicateElement(owningMembership.getMemberElement(), true, true);
            if (optDuplicate.isPresent()) {
                result = optDuplicate.get();
                copiedMembership.getOwnedRelatedElement().add(result);
                if (owningMembership.eContainer() instanceof Element owner) {
                    owner.getOwnedRelationship().add(copiedMembership);
                }
            }
        }

        if (result != null) {
            Node creationRequestSource = null;
            // The expose API requires to pass the node used for a basic creation request.
            // For a duplication, this node would either be the parent (bordered node) , grandparent (for list item) or the diagram
            Object parent = new NodeFinder(diagramContext.diagram()).getParent(node);
            if (parent instanceof Node parentNode) {
                // In case the parent node is compartment go up once again
                boolean isParentCompartment = convertedNodes.entrySet().stream()
                        .filter(entry -> entry.getValue().getId().equals(parentNode.getDescriptionId()))
                        .map(Map.Entry::getKey)
                        .map(nodeDescription -> nodeDescription.getName().contains("Compartment"))
                        .findFirst()
                        .orElse(false);
                if (isParentCompartment) {
                    Object grandParent = new NodeFinder(diagramContext.diagram()).getParent(parentNode);
                    if (grandParent instanceof Node grandParentNode) {
                        creationRequestSource = grandParentNode;
                    }
                } else {
                    creationRequestSource = parentNode;
                }
            }
            this.diagramMutationExposeService.expose(result, editingContext, diagramContext, creationRequestSource, convertedNodes);
        }
        return result;
    }
}
