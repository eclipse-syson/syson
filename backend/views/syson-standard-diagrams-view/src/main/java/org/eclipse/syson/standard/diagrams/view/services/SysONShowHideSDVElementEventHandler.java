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
package org.eclipse.syson.standard.diagrams.view.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.api.IRepresentationSearchService;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramChangeKind;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramEventHandler;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramInput;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramQueryService;
import org.eclipse.sirius.components.collaborative.diagrams.dto.HideDiagramElementInput;
import org.eclipse.sirius.components.collaborative.diagrams.messages.ICollaborativeDiagramMessageService;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.events.HideDiagramElementEvent;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewDiagramDescriptionSearchService;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.util.NodeFinder;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.Sinks.One;

/**
 * Called when a show/hide action has been called on a Standard Diagram View. It allows to hide some elements when some
 * other are shown.
 * <p>
 * Two cases on the GV:
 * <p>
 * - the first one is when a compartment is displayed, then if it contains an element that is already shown in the
 * diagram as a tree node with a composition edge, then this tree node and the composition edge will be hidden.
 * <p>
 * - the second one is when a compartment is hidden, then if it contains an element that could be shown in the diagram
 * as a tree node with a composition edge, then this tree node and the composition edge will be shown.
 *
 * @author arichard
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Service
public class SysONShowHideSDVElementEventHandler implements IDiagramEventHandler {

    private final IEditingContextSearchService editingContextSearchService;

    private final IRepresentationSearchService representationSearchService;

    private final IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService;

    private final IDiagramIdProvider diagramIdProvider;

    private final IDiagramQueryService diagramQueryService;

    private final ICollaborativeDiagramMessageService messageService;

    public SysONShowHideSDVElementEventHandler(IEditingContextSearchService editingContextSearchService, IRepresentationSearchService representationSearchService,
            IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService, IDiagramIdProvider diagramIdProvider, IDiagramQueryService diagramQueryService,
            ICollaborativeDiagramMessageService messageService) {
        this.editingContextSearchService = Objects.requireNonNull(editingContextSearchService);
        this.representationSearchService = Objects.requireNonNull(representationSearchService);
        this.viewDiagramDescriptionSearchService = Objects.requireNonNull(viewDiagramDescriptionSearchService);
        this.diagramIdProvider = Objects.requireNonNull(diagramIdProvider);
        this.diagramQueryService = Objects.requireNonNull(diagramQueryService);
        this.messageService = Objects.requireNonNull(messageService);
    }

    @Override
    public boolean canHandle(IDiagramInput diagramInput) {
        if (diagramInput instanceof HideDiagramElementInput hideInput) {
            String editingContextId = hideInput.editingContextId();
            var optEditingContext = this.editingContextSearchService.findById(editingContextId);
            if (optEditingContext.isPresent()) {
                var optDiagram = this.representationSearchService.findById(optEditingContext.get(), hideInput.representationId(), Diagram.class);
                if (optDiagram.isPresent()) {
                    return SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID.equals(optDiagram.get().getDescriptionId());
                }
            }
        }
        return false;
    }

    @Override
    public void handle(One<IPayload> payloadSink, Many<ChangeDescription> changeDescriptionSink, IEditingContext editingContext, DiagramContext diagramContext, IDiagramInput diagramInput) {
        if (diagramInput instanceof HideDiagramElementInput hideInput) {
            this.handleHideDiagramElement(payloadSink, changeDescriptionSink, diagramContext, hideInput);
        }
    }

    private void handleHideDiagramElement(One<IPayload> payloadSink, Many<ChangeDescription> changeDescriptionSink, DiagramContext diagramContext, HideDiagramElementInput diagramInput) {
        List<String> errors = new ArrayList<>(diagramInput.elementIds().size());
        Set<String> resolvedIds = new HashSet<>();
        Set<String> nodeIdsToHide = new HashSet<>();
        boolean hide = diagramInput.hide();

        for (String id : diagramInput.elementIds()) {
            var optionalEdge = this.diagramQueryService.findEdgeById(diagramContext.diagram(), id);
            var optionalNode = this.diagramQueryService.findNodeById(diagramContext.diagram(), id);

            if (optionalEdge.isPresent()) {
                resolvedIds.add(id);
            } else if (optionalNode.isPresent()) {
                var node = optionalNode.get();
                resolvedIds.add(id);
                // if it is a compartment node and the event is about to show it, then we want to hide all tree nodes
                // that are linked to the graphical parent node of the compartment node through a composition edge.
                // 1 - check if it a compartment node
                // 2 - get the parent node of the compartment node
                // 3 - get the semantic elements Ids corresponding to the compartment children
                // 4 - get the the nodes linked to the parent node through composition edges
                // 5 - get the semantic elements Ids corresponding to 4
                // 6 - hide all nodes of 4 with semantic element matching contained in 3 & 5
                var optEditingContext = this.editingContextSearchService.findById(diagramInput.editingContextId());
                if (!hide && this.isCompartmentNode(node, diagramContext.diagram(), optEditingContext.get())) {
                    var parentNode = this.getParentNode(node, diagramContext.diagram());
                    if (parentNode != null) {
                        List<String> compartmentSemanticChildren = this.getSemanticElements(node.getChildNodes());
                        List<Node> linkedNodes = this.getLinkedNodes(parentNode, diagramContext.diagram());
                        linkedNodes.forEach(ln -> {
                            if (compartmentSemanticChildren.contains(ln.getTargetObjectId())) {
                                nodeIdsToHide.add(ln.getId());
                            }
                        });
                    }
                }
            }
            if (optionalEdge.isEmpty() && optionalNode.isEmpty()) {
                errors.add(this.messageService.edgeNotFound(id));
                errors.add(this.messageService.nodeNotFound(id));
            }
        }

        if (resolvedIds.size() > 0) {
            diagramContext.diagramEvents().add(new HideDiagramElementEvent(resolvedIds, hide));
            diagramContext.diagramEvents().add(new HideDiagramElementEvent(nodeIdsToHide, true));
        }

        this.sendResponse(payloadSink, changeDescriptionSink, errors, resolvedIds.size() > 0, diagramContext, diagramInput);
    }

    private List<Node> getLinkedNodes(Node node, Diagram diagram) {
        List<Node> linkedNodes = new ArrayList<>();
        var nodeId = node.getId();
        diagram.getEdges().forEach(edge -> {
            if (Objects.equals(edge.getSourceId(), nodeId)) {
                this.diagramQueryService.findNodeById(diagram, edge.getTargetId()).ifPresent(linkedNodes::add);
            }
        });
        return linkedNodes;
    }

    private List<String> getSemanticElements(List<Node> nodes) {
        return nodes.stream().map(this::getSemanticElement).toList();
    }

    private String getSemanticElement(Node node) {
        return node.getTargetObjectId();
    }

    private boolean isCompartmentNode(Node node, Diagram diagram, IEditingContext editingContext) {
        var optViewDD = this.viewDiagramDescriptionSearchService.findById(editingContext, diagram.getDescriptionId());
        if (optViewDD.isPresent()) {
            var descriptionId = node.getDescriptionId();
            var optViewNodeDescription = EMFUtils.allContainedObjectOfType(optViewDD.get(), NodeDescription.class)
                    .filter(nodeDesc -> Objects.equals(descriptionId, this.diagramIdProvider.getId(nodeDesc)))
                    .filter(nodeDesc -> nodeDesc.getName().startsWith(SDVDescriptionNameGenerator.PREFIX + " Compartment"))
                    .findFirst();
            if (optViewNodeDescription.isPresent()) {
                return true;
            }
        }
        return false;
    }

    private Node getParentNode(Node node, Diagram diagram) {
        var nodeFinder = new NodeFinder(diagram);
        var parent = nodeFinder.getParent(node);
        if (parent instanceof Node parentNode) {
            return parentNode;
        }
        return null;
    }

    private void sendResponse(One<IPayload> payloadSink, Many<ChangeDescription> changeDescriptionSink, List<String> errors, boolean atLeastOneSuccess, DiagramContext diagramContext,
            HideDiagramElementInput diagramInput) {
        var changeDescription = new ChangeDescription(DiagramChangeKind.DIAGRAM_ELEMENT_VISIBILITY_CHANGE, diagramInput.representationId(), diagramInput);
        IPayload payload = new SuccessPayload(diagramInput.id());
        if (!errors.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append(this.messageService.deleteFailed());
            for (String error : errors) {
                errorMessage.append(error);
            }

            changeDescription = new ChangeDescription(ChangeKind.NOTHING, diagramInput.representationId(), diagramInput);
            if (atLeastOneSuccess) {
                changeDescription = new ChangeDescription(DiagramChangeKind.DIAGRAM_ELEMENT_VISIBILITY_CHANGE, diagramInput.representationId(), diagramInput);
            }

            payload = new ErrorPayload(diagramInput.id(), errorMessage.toString());
        }

        payloadSink.tryEmitValue(payload);
        changeDescriptionSink.tryEmitNext(changeDescription);
    }
}
