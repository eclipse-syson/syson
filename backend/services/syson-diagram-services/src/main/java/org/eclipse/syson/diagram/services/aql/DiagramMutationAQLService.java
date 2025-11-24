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
package org.eclipse.syson.diagram.services.aql;

import java.util.Map;
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.syson.diagram.services.DiagramMutationDiagramService;
import org.eclipse.syson.diagram.services.DiagramMutationDndService;
import org.eclipse.syson.diagram.services.DiagramMutationElementService;
import org.eclipse.syson.diagram.services.DiagramMutationExposeService;
import org.eclipse.syson.diagram.services.DiagramMutationLabelService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.StateUsage;

/**
 * Entry point for all diagram-related services doing mutations in diagrams and called by AQL expressions in diagram
 * descriptions. This class is not a @Service class but act as it was, because it is called by IJavaServiceProvider.
 *
 * @author arichard
 */
public class DiagramMutationAQLService {

    private final DiagramMutationDndService diagramMutationDndService;

    private final DiagramMutationElementService diagramMutationElementService;

    private final DiagramMutationExposeService diagramMutationExposeService;

    private final DiagramMutationLabelService diagramMutationLabelService;

    private final DiagramMutationDiagramService diagramMutationDiagramService;

    public DiagramMutationAQLService(DiagramMutationDndService diagramMutationDndService, DiagramMutationElementService diagramMutationElementService,
            DiagramMutationExposeService diagramMutationExposeService, DiagramMutationLabelService diagramMutationLabelService, DiagramMutationDiagramService diagramMutationDiagramService) {
        this.diagramMutationDndService = Objects.requireNonNull(diagramMutationDndService);
        this.diagramMutationElementService = Objects.requireNonNull(diagramMutationElementService);
        this.diagramMutationExposeService = Objects.requireNonNull(diagramMutationExposeService);
        this.diagramMutationLabelService = Objects.requireNonNull(diagramMutationLabelService);
        this.diagramMutationDiagramService = Objects.requireNonNull(diagramMutationDiagramService);
    }

    /**
     * {@link DiagramMutationExposeService#addToExposedElements(Element, boolean, IEditingContext, DiagramContext, Node, Map)}.
     */
    public Element addToExposedElements(Element element, boolean recursive, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        return this.diagramMutationExposeService.addToExposedElements(element, recursive, editingContext, diagramContext, selectedNode, convertedNodes);
    }

    /**
     * {@link DiagramMutationDiagramService#createDiagram(Element, IEditingContext)}.
     */
    public Element createDiagram(Element element, IEditingContext editingContext) {
        return this.diagramMutationDiagramService.createDiagram(element, editingContext);
    }

    /**
     * {@link DiagramMutationElementService#createChildState(Element, IEditingContext, DiagramContext, Node, Map, boolean, boolean)}.
     */
    public StateUsage createChildState(Element parentState, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean isParallel, boolean isExhibit) {
        return this.diagramMutationElementService.createChildState(parentState, editingContext, diagramContext, selectedNode, convertedNodes, isParallel, isExhibit);
    }

    /**
     * {@link DiagramMutationLabelService#directEdit(Element, String)}.
     */
    public Element directEdit(Element element, String newLabel) {
        return this.diagramMutationLabelService.directEdit(element, newLabel);
    }

    /**
     * {@link DiagramMutationLabelService#directEditNode(Element, String)}.
     */
    public Element directEditNode(Element element, String newLabel) {
        return this.diagramMutationLabelService.directEditNode(element, newLabel);
    }

    /**
     * {@link DiagramMutationLabelService#directEditListItem(Element, String)}.
     */
    public Element directEditListItem(Element element, String newLabel) {
        return this.diagramMutationLabelService.directEditListItem(element, newLabel);
    }

    /**
     * {@link DiagramMutationDndService#dropElementFromDiagram(Element, Node, Element, Node, IEditingContext, DiagramContext, Map)}.
     */
    public Element dropElementFromDiagram(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, DiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        return this.diagramMutationDndService.dropElementFromDiagram(droppedElement, droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes);
    }

    /**
     * {@link DiagramMutationDndService#dropElementFromDiagramInRequirementAssumeConstraintCompartment(Element, Node, Element, Node, IEditingContext, DiagramContext, Map)}.
     */
    public Element dropElementFromDiagramInRequirementAssumeConstraintCompartment(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext,
            DiagramContext diagramContext, Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        return this.diagramMutationDndService.dropElementFromDiagramInRequirementAssumeConstraintCompartment(droppedElement, droppedNode, targetElement, targetNode, editingContext, diagramContext,
                convertedNodes);
    }

    /**
     * {@link DiagramMutationDndService#dropElementFromDiagramInRequirementRequireConstraintCompartment(Element, Node, Element, Node, IEditingContext, DiagramContext, Map)}.
     */
    public Element dropElementFromDiagramInRequirementRequireConstraintCompartment(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext,
            DiagramContext diagramContext, Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        return this.diagramMutationDndService.dropElementFromDiagramInRequirementRequireConstraintCompartment(droppedElement, droppedNode, targetElement, targetNode, editingContext, diagramContext,
                convertedNodes);
    }

    /**
     * {@link DiagramMutationDndService#dropElementFromDiagramInConstraintCompartment(Element, Node, Element, Node, IEditingContext, DiagramContext, Map)}.
     */
    public Element dropElementFromDiagramInConstraintCompartment(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext,
            DiagramContext diagramContext, Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        return this.diagramMutationDndService.dropElementFromDiagramInConstraintCompartment(droppedElement, droppedNode, targetElement, targetNode, editingContext, diagramContext,
                convertedNodes);
    }

    /**
     * {@link DiagramMutationDndService#dropElementFromExplorer(Element, IEditingContext, DiagramContext, Node, Map)}.
     */
    public Element dropElementFromExplorer(Element element, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        return this.diagramMutationDndService.dropElementFromExplorer(element, editingContext, diagramContext, selectedNode, convertedNodes);
    }

    /**
     * {@link DiagramMutationDndService#dropObjectiveRequirementFromDiagram(Element, Node, Element, Node, IEditingContext, DiagramContext, Map)}.
     */
    public Element dropObjectiveRequirementFromDiagram(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, DiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        return this.diagramMutationDndService.dropObjectiveRequirementFromDiagram(droppedElement, droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes);
    }

    /**
     * {@link DiagramMutationDndService#dropSubjectFromDiagram(Element, Node, Element, Node, IEditingContext, DiagramContext, Map)}.
     */
    public Element dropSubjectFromDiagram(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, DiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        return this.diagramMutationDndService.dropSubjectFromDiagram(droppedElement, droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes);
    }

    /**
     * {@link DiagramMutationLabelService#editMultiplicityRangeCenterLabel(Element, String)}.
     */
    public Element editMultiplicityRangeCenterLabel(Element element, String newLabel) {
        return this.diagramMutationLabelService.editMultiplicityRangeCenterLabel(element, newLabel);
    }

    /**
     * {@link DiagramMutationLabelService#editEdgeCenterLabel(Element, String)}.
     */
    public Element editEdgeCenterLabel(Element element, String newLabel) {
        return this.diagramMutationLabelService.editEdgeCenterLabel(element, newLabel);
    }

    /**
     * {@link DiagramMutationExposeService#expose(Element, IEditingContext, DiagramContext, Node, Map)}.
     */
    public Element expose(Element element, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        return this.diagramMutationExposeService.expose(element, editingContext, diagramContext, selectedNode, convertedNodes);
    }

    /**
     * {@link DiagramMutationExposeService#removeFromExposedElements(Element, Node, IEditingContext, DiagramContext)}.
     */
    public boolean removeFromExposedElements(Element element, Node selectedNode, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.diagramMutationExposeService.removeFromExposedElements(element, selectedNode, editingContext, diagramContext);
    }

    /**
     * {@link DiagramMutationExposeService#showContentAsNested(Element, Node, IEditingContext, DiagramContext)}.
     */
    public Element showContentAsNested(Element element, Node selectedNode, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.diagramMutationElementService.showContentAsNested(element, selectedNode, editingContext, diagramContext);
    }

    /**
     * {@link DiagramMutationExposeService#showContentAsTrees(Element, Node, IEditingContext, DiagramContext)}.
     */
    public Element showContentAsTree(Element element, Node selectedNode, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.diagramMutationElementService.showContentAsTree(element, selectedNode, editingContext, diagramContext);
    }

    /**
     * {@link DiagramMutationElementService#viewNodeAs(Element, String, IEditingContext, DiagramContext, Node)}.
     */
    public Element viewNodeAs(Element element, String newViewDefinition, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode) {
        return this.diagramMutationElementService.viewNodeAs(element, newViewDefinition, editingContext, diagramContext, selectedNode);
    }
}
