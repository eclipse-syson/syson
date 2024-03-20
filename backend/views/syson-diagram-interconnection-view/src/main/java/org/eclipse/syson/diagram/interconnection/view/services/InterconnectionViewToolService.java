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
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewDiagramDescriptionProvider;
import org.eclipse.syson.services.ToolService;
import org.eclipse.syson.sysml.PartUsage;

/**
 * Tool-related Java services used by the {@link InterconnectionViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class InterconnectionViewToolService extends ToolService {

    private final IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService;

    public InterconnectionViewToolService(IObjectService objectService, IRepresentationDescriptionSearchService representationDescriptionSearchService, IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService) {
        super(objectService, representationDescriptionSearchService);
        this.viewRepresentationDescriptionSearchService = Objects.requireNonNull(viewRepresentationDescriptionSearchService);
    }

    /**
     * Called by "Add existing elements" tool from Interconnection View PartUsage node. Add nodes that are not present
     * in selectedNode (i.e. a PartUsage).
     *
     * @param partUsage
     *            the {@link PartUsage} corresponding to the target object of the Diagram or the {@link Node} Package on
     *            which the tool has been called.
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the tool has been called (may be null if the tool has been called from the
     *            diagram). It corresponds to a variable accessible from the variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @param recursive
     *            whether the tool should add existing elements recursively or not.
     * @return the input {@link PartUsage}.
     */
    public PartUsage addExistingElements(PartUsage partUsage, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean recursive) {
        var nestedParts = partUsage.getNestedPart();
        var diagramDescription = this.viewRepresentationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());
        DiagramDescription representationDescription = (DiagramDescription) diagramDescription.get();

        nestedParts.stream()
                .filter(subPartUsage -> !this.isPresent(subPartUsage, this.getChildNodes(diagramContext, selectedNode)))
                .forEach(subPartUsage -> {
                    this.createView(subPartUsage, editingContext, diagramContext, selectedNode, convertedNodes);
                    if (recursive) {
                        Node fakeNode = createFakeNode(subPartUsage, selectedNode, diagramContext, representationDescription, convertedNodes);
                        addExistingSubElements(subPartUsage, editingContext, diagramContext, fakeNode, representationDescription, convertedNodes);
                    }
                });
        return partUsage;
    }

    private PartUsage addExistingSubElements(PartUsage partUsage, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode, DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var nestedParts = partUsage.getNestedPart();

        nestedParts.stream()
                .forEach(subPartUsage -> {
                    this.createView(subPartUsage, editingContext, diagramContext, selectedNode, convertedNodes);
                    Node fakeNode = createFakeNode(subPartUsage, selectedNode, diagramContext, diagramDescription, convertedNodes);
                    addExistingSubElements(subPartUsage, editingContext, diagramContext, fakeNode, diagramDescription, convertedNodes);
                });
        return partUsage;
    }
}
