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
package org.eclipse.syson.diagram.interconnection.view.services;

import java.util.Map;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewForUsageDiagramDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.ChildPartUsageNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.FirstLevelChildPartUsageNodeDescriptionProvider;
import org.eclipse.syson.services.ElementInitializerSwitch;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;

/**
 * Tool-related Java services used by the {@link InterconnectionViewForUsageDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class InterconnectionViewToolService extends ViewToolService {

    private final ElementInitializerSwitch elementInitializerSwitch;

    public InterconnectionViewToolService(IObjectService objectService, IRepresentationDescriptionSearchService representationDescriptionSearchService,
            IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService, IFeedbackMessageService feedbackMessageService) {
        super(objectService, representationDescriptionSearchService, viewRepresentationDescriptionSearchService, feedbackMessageService);
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
        Node parentNode = this.getRealParentNode(selectedNode, convertedNodes);
        if (parentNode != null) {
            this.createView(childPart, editingContext, diagramContext, parentNode, convertedNodes);
        }
        return childPart;
    }

    private Node getRealParentNode(Node selectedNode, Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        Node parentNode = null;
        Optional<org.eclipse.sirius.components.view.diagram.NodeDescription> nodeChildPartUsage = convertedNodes.keySet().stream()
                .filter(n -> FirstLevelChildPartUsageNodeDescriptionProvider.NAME.equals(n.getName())).findFirst();
        if (nodeChildPartUsage.isPresent()) {
            NodeDescription nodeDescription = convertedNodes.get(nodeChildPartUsage.get());
            if (nodeDescription != null && nodeDescription.getId().equals(selectedNode.getDescriptionId())) {
                parentNode = selectedNode.getChildNodes().get(1);
            }
        }
        if (parentNode == null) {
            nodeChildPartUsage = convertedNodes.keySet().stream()
                    .filter(n -> ChildPartUsageNodeDescriptionProvider.NAME.equals(n.getName())).findFirst();
            if (nodeChildPartUsage.isPresent()) {
                NodeDescription nodeDescription = convertedNodes.get(nodeChildPartUsage.get());
                if (nodeDescription != null && nodeDescription.getId().equals(selectedNode.getDescriptionId())) {
                    parentNode = selectedNode.getChildNodes().get(1);
                }
            }
        }
        if (parentNode == null) {
            parentNode = selectedNode;
        }
        return parentNode;
    }
}
