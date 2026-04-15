/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
import java.util.stream.Stream;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramService;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramServices;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.FreeFormLayoutStrategy;
import org.eclipse.sirius.components.diagrams.ListLayoutStrategy;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.syson.diagram.services.utils.RevealCompartmentSwitch;
import org.eclipse.syson.services.NodeDescriptionService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.util.NodeFinder;
import org.eclipse.syson.util.StandardDiagramsConstants;
import org.springframework.stereotype.Service;

/**
 * Mutation services related to diagram compartments.
 *
 * @author arichard
 */
@Service
public class DiagramMutationCompartmentService {

    private final IObjectSearchService objectSearchService;

    private final UtilService utilService;

    private final DiagramQueryViewService diagramQueryViewService;

    public DiagramMutationCompartmentService(IObjectSearchService objectSearchService, DiagramQueryViewService diagramQueryViewService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.utilService = new UtilService();
        this.diagramQueryViewService = Objects.requireNonNull(diagramQueryViewService);
    }

    /**
     * Reveals the compartment in {@code node} that can display {@code targetElement}.
     */
    public Node revealCompartment(Node node, Element targetElement, DiagramContext diagramContext, IEditingContext editingContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {

        if (!this.utilService.isUnsynchronized(targetElement)
                && !this.needToRevealCompartment(targetElement, this.diagramQueryViewService.isView(targetElement, StandardDiagramsConstants.GV_QN, node, editingContext, diagramContext))) {
            return node;
        }
        var nodeDescription = convertedNodes.values().stream()
                .filter(nodeDesc -> Objects.equals(nodeDesc.getId(), node.getDescriptionId()))
                .findFirst();

        List<NodeDescription> allChildNodeDescriptions = nodeDescription.map(nodeDesc -> Stream.concat(
                nodeDesc.getChildNodeDescriptions().stream(),
                convertedNodes.values().stream().filter(convNode -> nodeDesc.getReusedChildNodeDescriptionIds().contains(convNode.getId())))
                .toList())
                .orElse(List.of());

        var parentObject = this.objectSearchService.getObject(editingContext, node.getTargetObjectId()).orElse(null);
        NodeDescriptionService nodeDescriptionService = new NodeDescriptionService(this.objectSearchService);

        var compartmentDescriptionCandidates = nodeDescriptionService.getNodeDescriptionsForRenderingElementAsChild(targetElement, parentObject, allChildNodeDescriptions,
                convertedNodes, editingContext, diagramContext).stream()
                .map(NodeDescription::getId)
                .toList();

        if (!compartmentDescriptionCandidates.isEmpty()) {
            NodeFinder nodeFinder = new NodeFinder(diagramContext.diagram());
            List<Node> compartmentNodeCandidates = nodeFinder
                    .getAllNodesMatching(n -> compartmentDescriptionCandidates.stream().anyMatch(id -> Objects.equals(id, n.getDescriptionId()))
                            && Objects.equals(n.getTargetObjectId(), node.getTargetObjectId()));
            var noCompartmentToHandleTargetElement = compartmentNodeCandidates.stream()
                    .allMatch(candidate -> ViewModifier.Hidden.equals(candidate.getState()));
            if (noCompartmentToHandleTargetElement) {
                compartmentNodeCandidates.stream().reduce((previousCandidate, newCandidate) -> {
                    if (previousCandidate.getStyle().getChildrenLayoutStrategy() instanceof ListLayoutStrategy && newCandidate.getStyle().getChildrenLayoutStrategy() instanceof FreeFormLayoutStrategy) {
                        return newCandidate;
                    }
                    return previousCandidate;
                }).ifPresent(compartmentToReveal -> new DiagramServices().reveal(new DiagramService(diagramContext), List.of(compartmentToReveal)));
            }
        }
        return node;
    }

    private boolean needToRevealCompartment(Element element, boolean isGeneralView) {
        return new RevealCompartmentSwitch(isGeneralView).doSwitch(element);
    }
}
