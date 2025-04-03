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
package org.eclipse.syson.diagram.interconnection.view.services;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewDiagramDescriptionProvider;
import org.eclipse.syson.services.ElementInitializerSwitch;
import org.eclipse.syson.services.NodeDescriptionService;
import org.eclipse.syson.services.api.ISysMLMoveElementService;
import org.eclipse.syson.services.api.ISysMLReadOnlyService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tool-related Java services used by the {@link InterconnectionViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class InterconnectionViewToolService extends ViewToolService {

    private final Logger logger = LoggerFactory.getLogger(InterconnectionViewToolService.class);

    private final ElementInitializerSwitch elementInitializerSwitch;

    public InterconnectionViewToolService(IIdentityService identityService, IObjectSearchService objectSearchService, IRepresentationDescriptionSearchService representationDescriptionSearchService,
            IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService, IFeedbackMessageService feedbackMessageService, ISysMLMoveElementService moveService,
            ISysMLReadOnlyService readOnlyService) {
        super(identityService, objectSearchService, representationDescriptionSearchService, viewRepresentationDescriptionSearchService, feedbackMessageService, moveService, readOnlyService);
        this.elementInitializerSwitch = new ElementInitializerSwitch();
    }

    /**
     * Called by "New Part" tool from Interconnection View Child PartUsage node.
     *
     * @param partUsage
     *            the {@link PartUsage} corresponding to the target object on which the tool has been called.
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the tool has been called. It corresponds to a variable accessible from the
     *            variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @return the created PartUsage
     */
    public PartUsage createChildPart(PartUsage partUsage, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        FeatureMembership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        partUsage.getOwnedRelationship().add(membership);
        PartUsage childPart = SysmlFactory.eINSTANCE.createPartUsage();
        membership.getOwnedRelatedElement().add(childPart);
        this.elementInitializerSwitch.doSwitch(childPart);
        Node parentNode = this.getRealParentNode(childPart, partUsage, selectedNode, convertedNodes);
        if (parentNode != null) {
            this.createView(childPart, editingContext, diagramContext, parentNode, convertedNodes);
        }
        return childPart;
    }

    private Node getRealParentNode(Element childElement, Element parentElement, Node selectedNode, Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        Node parentNode = null;
        Optional<NodeDescription> nodeDescription = convertedNodes.values().stream()
                .filter(description -> Objects.equals(description.getId(), selectedNode.getDescriptionId()))
                .findFirst();

        List<NodeDescription> allChildNodeDescriptions = nodeDescription.map(nodeDesc -> Stream.concat(
                nodeDesc.getChildNodeDescriptions().stream(),
                convertedNodes.values().stream().filter(convNode -> nodeDesc.getReusedChildNodeDescriptionIds().contains(convNode.getId())))
                .toList())
                .orElse(List.of());

        NodeDescriptionService nodeDescriptionService = new NodeDescriptionService();
        List<NodeDescription> compartmentCandidates = nodeDescriptionService.getNodeDescriptionsForRenderingElementAsChild(childElement, parentElement, allChildNodeDescriptions, convertedNodes);
        if (!compartmentCandidates.isEmpty()) {
            if (compartmentCandidates.size() > 1) {
                this.logger.warn("Multiple compartment candidates found for {} in {}.", childElement.eClass().getName(), selectedNode.toString());
            }
            parentNode = selectedNode.getChildNodes().stream()
                    .filter(childNode -> Objects.equals(childNode.getDescriptionId(), compartmentCandidates.get(0).getId()))
                    .findFirst()
                    .orElse(null);
        }

        if (parentNode == null) {
            parentNode = selectedNode;
        }

        return parentNode;
    }
}
