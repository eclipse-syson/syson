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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.syson.services.NodeDescriptionService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.util.NodeFinder;

/**
 * Detect representations inside selected nodes without changing their visibility.
 *
 * @author cbrun
 */
public class ConnectedElementRepresentationFilter {

    private final IObjectSearchService objectSearchService;

    private final NodeDescriptionService nodeDescriptionService;

    private final IEditingContext editingContext;

    private final DiagramContext diagramContext;

    private final Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes;

    /**
     * Create a filter for one invocation of the connected elements tool.
     *
     * @param objectSearchService
     *            the semantic object lookup
     * @param editingContext
     *            the project context
     * @param diagramContext
     *            the current diagram
     * @param convertedNodes
     *            the converted descriptions
     */
    public ConnectedElementRepresentationFilter(IObjectSearchService objectSearchService, IEditingContext editingContext, DiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        this.objectSearchService = objectSearchService;
        this.nodeDescriptionService = new NodeDescriptionService(objectSearchService);
        this.editingContext = editingContext;
        this.diagramContext = diagramContext;
        this.convertedNodes = convertedNodes;
    }

    /**
     * Resolve a compartment selection to the enclosing representation of the same semantic element.
     *
     * @param selectedNode
     *            the selected node
     * @return the reference node
     */
    public Node getReferenceNode(Node selectedNode) {
        var finder = new NodeFinder(this.diagramContext.diagram());
        var reference = selectedNode;
        while (finder.getParent(reference) instanceof Node parent && Objects.equals(parent.getTargetObjectId(), reference.getTargetObjectId())) {
            reference = parent;
        }
        return reference;
    }

    /**
     * Check border and nested descriptions, including compartments that are currently hidden.
     *
     * @param candidate
     *            the connected element
     * @param reference
     *            the normalized selected node
     * @return whether the candidate has a representation inside the reference node
     */
    public boolean canRenderInside(Element candidate, Node reference) {
        var owner = this.objectSearchService.getObject(this.editingContext, reference.getTargetObjectId()).filter(Element.class::isInstance).map(Element.class::cast);
        var description = this.convertedNodes.values().stream().filter(value -> Objects.equals(value.getId(), reference.getDescriptionId())).findFirst();
        if (owner.isEmpty() || description.isEmpty()) {
            return false;
        }
        var ancestors = new ArrayList<Object>();
        var finder = new NodeFinder(this.diagramContext.diagram());
        Object current = reference;
        while (current instanceof Node node) {
            this.objectSearchService.getObject(this.editingContext, node.getTargetObjectId()).ifPresent(ancestors::add);
            current = finder.getParent(node);
        }
        this.objectSearchService.getObject(this.editingContext, this.diagramContext.diagram().getTargetObjectId()).ifPresent(ancestors::add);
        return this.canRenderInside(candidate, owner.get(), description.get(), ancestors, new HashSet<>());
    }

    /**
     * Traverse only containers representing the same owner, guarding reused-description cycles.
     *
     * @param candidate
     *            the connected element
     * @param owner
     *            the selected semantic element
     * @param description
     *            the current container description
     * @param ancestors
     *            the semantic ancestors of its children
     * @param visited
     *            the descriptions already inspected for this owner
     * @return whether a nested description renders the candidate
     */
    private boolean canRenderInside(Element candidate, Element owner, NodeDescription description, List<Object> ancestors, Set<String> visited) {
        if (!visited.add(description.getId())) {
            return false;
        }
        var children = Stream.concat(Stream.concat(description.getChildNodeDescriptions().stream(), description.getBorderNodeDescriptions().stream()),
                this.convertedNodes.values().stream().filter(value -> description.getReusedChildNodeDescriptionIds().contains(value.getId())
                        || description.getReusedBorderNodeDescriptionIds().contains(value.getId()))).distinct().toList();
        boolean canRender = false;
        for (var child : children) {
            if (this.nodeDescriptionService.canNodeDescriptionRenderElement(child, candidate, owner, this.editingContext, this.diagramContext, ancestors)) {
                canRender = true;
                break;
            }
            if (this.nodeDescriptionService.canNodeDescriptionRenderElement(child, owner, owner, this.editingContext, this.diagramContext, ancestors)) {
                var nestedAncestors = new ArrayList<Object>();
                nestedAncestors.add(owner);
                nestedAncestors.addAll(ancestors);
                if (this.canRenderInside(candidate, owner, child, nestedAncestors, visited)) {
                    canRender = true;
                    break;
                }
            }
        }
        return canRender;
    }
}
