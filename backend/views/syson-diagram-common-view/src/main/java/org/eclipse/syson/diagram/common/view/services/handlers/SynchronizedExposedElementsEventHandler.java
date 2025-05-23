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
package org.eclipse.syson.diagram.common.view.services.handlers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.IRepresentationSearchService;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramChangeKind;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramEventHandler;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramInput;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewCreationRequest;
import org.eclipse.sirius.components.diagrams.components.NodeContainmentKind;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.syson.services.NodeDescriptionService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.ViewUsage;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.Sinks.One;

/**
 * {@link IDiagramEventHandler} that tries to create a Node for each ViewUsage#exposedElements that doesn't have a Node
 * yet. Only works on the diagram background for now, but should also handle child nodes at some point. Works in
 * combination with {@link SynchronizeExposedElementsInputProcessor}.
 *
 * @author arichard
 */
@Service
public class SynchronizedExposedElementsEventHandler implements IDiagramEventHandler {

    private final IRepresentationSearchService representationSearchService;

    private final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    private final IObjectSearchService objectSearchService;

    private final IIdentityService identityService;

    private final UtilService utilService;

    private final NodeDescriptionService nodeDescriptionService;

    public SynchronizedExposedElementsEventHandler(IRepresentationSearchService representationSearchService, IRepresentationDescriptionSearchService representationDescriptionSearchService,
            IObjectSearchService objectSearchService, IIdentityService identityService) {
        this.representationSearchService = Objects.requireNonNull(representationSearchService);
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.identityService = Objects.requireNonNull(identityService);
        this.utilService = new UtilService();
        this.nodeDescriptionService = new NodeDescriptionService();
    }

    @Override
    public boolean canHandle(IDiagramInput diagramInput) {
        return diagramInput instanceof SynchronizedExposedElementsDiagramInput;
    }

    @Override
    public void handle(One<IPayload> payloadSink, Many<ChangeDescription> changeDescriptionSink, IEditingContext editingContext, IDiagramContext diagramContext, IDiagramInput diagramInput) {
        if (diagramInput instanceof SynchronizedExposedElementsDiagramInput synchronizedElementsDiagramInput) {
            String representationId = synchronizedElementsDiagramInput.representationId();
            Optional<Diagram> optionalDiagram = this.representationSearchService.findById(editingContext, representationId, Diagram.class);
            if (optionalDiagram.isPresent()) {
                Diagram diagram = optionalDiagram.get();
                Optional<ViewUsage> optionalDiagramTarget = this.objectSearchService.getObject(editingContext, diagram.getTargetObjectId())
                        .filter(ViewUsage.class::isInstance)
                        .map(ViewUsage.class::cast);
                if (optionalDiagramTarget.isPresent()) {
                    this.synchronizeExposedElements(optionalDiagramTarget.get(), diagram, editingContext, diagramContext);
                    if (!diagramContext.getViewCreationRequests().isEmpty()) {
                        ChangeDescription changeDescription = new ChangeDescription(DiagramChangeKind.DIAGRAM_LAYOUT_CHANGE, diagramInput.representationId(), diagramInput);
                        payloadSink.tryEmitValue(new SuccessPayload(diagramInput.id()));
                        changeDescriptionSink.tryEmitNext(changeDescription);
                    }
                }
            }
        }
    }

    private void synchronizeExposedElements(ViewUsage viewUsage, Diagram diagram, IEditingContext editingContext, IDiagramContext diagramContext) {
        var exposedElements = viewUsage.getExposedElement();
        for (Element exposedElement : exposedElements) {
            String exposedElementId = this.identityService.getId(exposedElement);
            Optional<Node> node = this.getNode(diagram, exposedElementId);
            if (node.isEmpty()) {
                var owner = this.utilService.getOwningElement(exposedElement);
                var diagramDescription = this.representationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());
                List<NodeDescription> candidates = diagramDescription
                        .filter(org.eclipse.sirius.components.diagrams.description.DiagramDescription.class::isInstance)
                        .map(org.eclipse.sirius.components.diagrams.description.DiagramDescription.class::cast)
                        .map(org.eclipse.sirius.components.diagrams.description.DiagramDescription::getNodeDescriptions)
                        .orElse(List.of())
                        .stream()
                        .filter(nodeDescription -> this.nodeDescriptionService.canNodeDescriptionRenderElement(nodeDescription, exposedElement, owner))
                        .toList();
                for (NodeDescription candidate : candidates) {
                    var viewCreationRequest = ViewCreationRequest.newViewCreationRequest()
                            .containmentKind(NodeContainmentKind.CHILD_NODE)
                            .descriptionId(candidate.getId())
                            .parentElementId(diagram.getId())
                            .targetObjectId(exposedElementId)
                            .build();
                    diagramContext.getViewCreationRequests().add(viewCreationRequest);
                }
            }
        }
    }

    private Optional<Node> getNode(Diagram diagram, String targetObjectId) {
        Optional<Node> optionalNode = Optional.empty();
        List<Node> nodes = diagram.getNodes();
        for (Node node : nodes) {
            if (Objects.equals(targetObjectId, node.getTargetObjectId())) {
                optionalNode = Optional.of(node);
                break;
            }
        }
        if (optionalNode.isEmpty()) {
            for (Node node : nodes) {
                var optChildNode = this.getChildNode(node, targetObjectId);
                if (optChildNode.isPresent() && Objects.equals(targetObjectId, optChildNode.get().getTargetObjectId())) {
                    optionalNode = Optional.of(optChildNode.get());
                    break;
                }
            }
        }
        return optionalNode;
    }

    private Optional<Node> getChildNode(Node node, String targetObjectId) {
        Optional<Node> optionalChildNode = Optional.empty();
        List<Node> childNodes = node.getChildNodes();
        for (Node childNode : childNodes) {
            if (Objects.equals(targetObjectId, childNode.getTargetObjectId())) {
                optionalChildNode = Optional.of(childNode);
                break;
            }
        }
        if (optionalChildNode.isEmpty()) {
            for (Node childNode : childNodes) {
                var optChildNode = this.getChildNode(childNode, targetObjectId);
                if (optChildNode.isPresent() && Objects.equals(targetObjectId, optChildNode.get().getTargetObjectId())) {
                    optionalChildNode = Optional.of(optChildNode.get());
                    break;
                }
            }
        }
        return optionalChildNode;
    }
}
