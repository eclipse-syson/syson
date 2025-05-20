/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.ViewUsage;

/**
 * Services for node descriptions.
 *
 * @author Jerome Gout
 */
public class NodeDescriptionService {

    private final IObjectSearchService objectSearchService;

    public NodeDescriptionService(IObjectSearchService objectSearchService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
    }

    /**
     * Returns the child descriptions of {@code nodeDescriptions} that can render {@code element}.
     * <p>
     * This method looks for child {@link NodeDescription}s in child nodes, border nodes, as well as reused nodes.
     * </p>
     *
     * @param element
     *            the element to render
     * @param ownerObject
     *            the semantic parent of the element to render
     * @param nodeDescriptions
     *            the {@link NodeDescription}s to search in
     * @param convertedNodes
     *            the converted nodes
     * @param editingContext
     *            the editing context
     * @param diagramContext
     *            the diagram context
     * @return the child descriptions of {@code nodeDescriptions} that can render {@code element}
     */
    public List<NodeDescription> getChildNodeDescriptionsForRendering(Element element, Object ownerObject, List<NodeDescription> nodeDescriptions,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, IEditingContext editingContext, IDiagramContext diagramContext) {
        List<NodeDescription> candidates = new ArrayList<>();
        for (NodeDescription node : nodeDescriptions) {
            List<NodeDescription> allChildren = new ArrayList<>();
            allChildren.addAll(node.getChildNodeDescriptions());
            allChildren.addAll(node.getBorderNodeDescriptions());
            allChildren.addAll(convertedNodes.values().stream().filter(convNode -> node.getReusedChildNodeDescriptionIds().contains(convNode.getId())).toList());
            allChildren.addAll(convertedNodes.values().stream().filter(convNode -> node.getReusedBorderNodeDescriptionIds().contains(convNode.getId())).toList());
            for (NodeDescription child : allChildren) {
                if (this.canNodeDescriptionRenderElement(child, element, ownerObject, editingContext, diagramContext)) {
                    candidates.add(child);
                }
            }
        }
        return candidates;
    }

    /**
     * Returns the list of candidate node descriptions that can be used to render the given semantic element knowing its
     * parent.
     *
     * @param element
     *            the semantic element for which the node description is searched
     * @param ownerObject
     *            the parent element of the semantic element
     * @param nodeDescriptions
     *            the list of possible parent node descriptions among which the search is performed
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @param editingContext
     *            the editing context
     * @param diagramContext
     *            the diagram context
     * @return the list of node descriptions that can be used to render the given semantic element.
     */
    public List<NodeDescription> getNodeDescriptionsForRenderingElementAsChild(Element element, Object ownerObject, List<NodeDescription> nodeDescriptions,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, IEditingContext editingContext, IDiagramContext diagramContext) {
        List<NodeDescription> candidates = new ArrayList<>();
        for (NodeDescription node : nodeDescriptions) {
            List<NodeDescription> allChildNodeDescriptions = Stream.concat(
                    node.getChildNodeDescriptions().stream(),
                    convertedNodes.values().stream().filter(convNode -> node.getReusedChildNodeDescriptionIds().contains(convNode.getId())))
                    .toList();
            for (NodeDescription childNodeDescription : allChildNodeDescriptions) {
                if (this.canNodeDescriptionRenderElement(childNodeDescription, element, ownerObject, editingContext, diagramContext)) {
                    candidates.add(node);
                }
            }
        }
        return candidates;
    }

    /**
     * Check whether the node will be used to render 'element'. This is the case if the semanticElementProvider returns
     * 'element' when applied to its parent, and the shouldRenderPredicate (i.e. the node's precondition) returns true.
     *
     * @param nodeDescription
     *            the node description to evaluate
     * @param element
     *            the semantic element to render
     * @param ownerObject
     *            the parent element
     * @param editingContext
     *            the editing context
     * @param diagramContext
     *            the diagram context
     * @return <code>true</code> if the given node description can be used to render the given semantic element and
     *         <code>false</code> otherwise.
     */
    public boolean canNodeDescriptionRenderElement(NodeDescription nodeDescription, Element element, Object ownerObject, IEditingContext editingContext, IDiagramContext diagramContext) {
        boolean canNodeDescriptionRenderElement = false;
        VariableManager semanticElementsProviderVariableManager = new VariableManager();
        semanticElementsProviderVariableManager.put(VariableManager.SELF, ownerObject);
        semanticElementsProviderVariableManager.put(IEditingContext.EDITING_CONTEXT, editingContext);
        semanticElementsProviderVariableManager.put(IDiagramContext.DIAGRAM_CONTEXT, diagramContext);
        var ancestors = new ArrayList<>();
        this.objectSearchService.getObject(editingContext, diagramContext.getDiagram().getTargetObjectId())
                .filter(ViewUsage.class::isInstance)
                .map(ViewUsage.class::cast)
                .ifPresent(ancestors::add);
        semanticElementsProviderVariableManager.put(NodeDescription.ANCESTORS, ancestors);

        List<?> candidatesList = nodeDescription.getSemanticElementsProvider().apply(semanticElementsProviderVariableManager);

        if (candidatesList.contains(element)) {
            VariableManager shouldRenderPredicateVariableManager = new VariableManager();
            shouldRenderPredicateVariableManager.put(VariableManager.SELF, element);
            shouldRenderPredicateVariableManager.put(IEditingContext.EDITING_CONTEXT, editingContext);
            shouldRenderPredicateVariableManager.put(IDiagramContext.DIAGRAM_CONTEXT, diagramContext);
            shouldRenderPredicateVariableManager.put(NodeDescription.ANCESTORS, null);
            canNodeDescriptionRenderElement = nodeDescription.getShouldRenderPredicate().test(shouldRenderPredicateVariableManager);
        }

        return canNodeDescriptionRenderElement;
    }
}
