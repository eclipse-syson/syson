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
package org.eclipse.syson.diagram.common.view.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.acceleo.query.runtime.impl.NullValue;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramService;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramServices;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.IDiagramElement;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.diagrams.elements.NodeElementProps;
import org.eclipse.sirius.components.diagrams.renderer.DiagramRenderingCache;
import org.eclipse.syson.services.NodeDescriptionService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.AnnotatingElement;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Node-related Java services used by several diagrams.
 *
 * @author gdaniel
 */
public class ViewNodeService {

    private final Logger logger = LoggerFactory.getLogger(ViewNodeService.class);

    private final IObjectSearchService objectSearchService;

    private final UtilService utilService;

    public ViewNodeService(IObjectSearchService objectSearchService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.utilService = new UtilService();
    }

    /**
     * Reveals the compartment in {@code node} that can display {@code targetElement}.
     * <p>
     * This method does not assume that the node representing {@code targetElement} is already displayed on the diagram.
     * It looks for the compartment that can (or already does) contain the node, and makes it visible. This means that
     * this method can be called as part of the creation process of {@code targetElement}, even if the node representing
     * it hasn't been rendered yet (e.g. if the node is synchronized).
     * </p>
     *
     * @param node
     *            the node containing the compartments
     * @param targetElement
     *            the semantic element to reveal the compartment of
     * @param diagramContext
     *            the diagram context
     * @param editingContext
     *            the editing context
     * @param convertedNodes
     *            the node descriptions of the diagram
     * @return the node containing the compartments
     */
    public Node revealCompartment(Node node, Element targetElement, DiagramContext diagramContext, IEditingContext editingContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {

        Optional<NodeDescription> nodeDescription = convertedNodes.values().stream().filter(nodeDesc -> Objects.equals(nodeDesc.getId(), node.getDescriptionId()))
                .findFirst();

        List<NodeDescription> allChildNodeDescriptions = nodeDescription.map(nodeDesc -> Stream.concat(
                nodeDesc.getChildNodeDescriptions().stream(),
                convertedNodes.values().stream().filter(convNode -> nodeDesc.getReusedChildNodeDescriptionIds().contains(convNode.getId())))
                .toList())
                .orElse(List.of());

        Object parentObject = this.objectSearchService.getObject(editingContext, node.getTargetObjectId()).orElse(null);

        NodeDescriptionService nodeDescriptionService = new NodeDescriptionService();

        List<NodeDescription> compartmentCandidates = nodeDescriptionService.getNodeDescriptionsForRenderingElementAsChild(targetElement, parentObject, allChildNodeDescriptions,
                convertedNodes);

        if (!compartmentCandidates.isEmpty()) {
            if (compartmentCandidates.size() > 1) {
                this.logger.warn("Multiple compartment candidates found for {} in {}.", targetElement.eClass().getName(), node.toString());
            }
            NodeFinder nodeFinder = new NodeFinder(diagramContext.getDiagram());
            List<Node> candidateNodes = nodeFinder
                    .getAllNodesMatching(n -> compartmentCandidates.stream().map(NodeDescription::getId).anyMatch(id -> Objects.equals(id, n.getDescriptionId()))
                            && Objects.equals(n.getTargetObjectId(), node.getTargetObjectId())
                    );
            new DiagramServices().reveal(new DiagramService(diagramContext), candidateNodes);
        }
        return node;
    }

    /**
     * Check if the compartment associated to the given {@link Element} and reference should be hidden by default.
     *
     * @param self
     *            the {@link Element} associated to the compartment node.
     * @param referenceName
     *            the name of the reference associated to the compartment node.
     * @return <code>true</code> if the compartment should be hidden by default, <code>false</code> otherwise
     */
    public boolean isHiddenByDefault(Element self, String referenceName) {
        boolean isHiddenByDefault = true;
        EStructuralFeature eStructuralFeature = self.eClass().getEStructuralFeature(referenceName);
        if (eStructuralFeature != null) {
            Object referenceValue = self.eGet(eStructuralFeature);
            if (referenceValue instanceof List<?> referenceListValue) {
                isHiddenByDefault = referenceListValue.isEmpty();
            } else {
                isHiddenByDefault = referenceValue == null;
            }
        }
        return isHiddenByDefault;
    }

    /**
     * Check if the compartment associated to the given {@link Element} and references should be hidden by default.
     *
     * @param self
     *            the {@link Element} associated to the compartment node.
     * @param referenceNames
     *            the name of the references associated to the compartment node.
     * @return <code>true</code> if the compartment should be hidden by default, <code>false</code> otherwise
     */
    public boolean isHiddenByDefault(Element self, List<String> referenceNames) {
        return referenceNames.stream().allMatch(referenceName -> this.isHiddenByDefault(self, referenceName));
    }

    /**
     * Retrieves all the {@link Element} elements from {@code contents} removing the null content.
     *
     * @param self
     *            The elements onto which the content is gathered
     * @param contents
     *            The content to assemble
     * @return
     */
    public List<Element> getAllContentsByReferences(Element self, List<Object> contents) {
        List<Element> result = new ArrayList<>();
        contents.stream().filter(Objects::nonNull).filter(elt -> !(elt instanceof NullValue)).forEach(object -> {
            if (object instanceof List<?> l) {
                l.stream().filter(Element.class::isInstance).map(Element.class::cast).map(result::add);
            } else if (object instanceof Element elt) {
                result.add(elt);
            }
        });
        return result;
    }

    public List<PerformActionUsage> getAllReferencingPerformActionUsages(Element self) {
        List<EObject> allPerformActionUsages = this.utilService.getAllReachable(self, SysmlPackage.eINSTANCE.getPerformActionUsage());
        return allPerformActionUsages.stream()
                .filter(PerformActionUsage.class::isInstance)
                .map(PerformActionUsage.class::cast)
                .filter(this::isReferencingPerformActionUsage)
                .toList();
    }

    /**
     * Returns {@code true} if {@code parentNodeElement} is an ancestor of {@code childNodeElement}.
     * <p>
     * This method checks if {@code parentNodeElement} contains (directly or indirectly) {@code childNodeElement} in the
     * provided {@code diagramContext}. It is typically called in edge preconditions to prevent the creation of
     * containment edges that are not necessary because the node is graphically contained in its parent.
     * </p>
     *
     * @param parentNodeElement
     *            the element representing the parent node
     * @param childNodeElement
     *            the element representing the child node
     * @param cache
     *            the rendering cache used in the current rendering process
     * @return {@code true} if {@code parentNodeElement} is an ancestor of {@code childNodeElement}
     */
    public boolean isAncestorOf(org.eclipse.sirius.components.representations.Element parentNodeElement, org.eclipse.sirius.components.representations.Element childNodeElement,
            DiagramRenderingCache cache) {
        boolean result = false;
        if (parentNodeElement.getProps() instanceof NodeElementProps parentNodeProps
                && childNodeElement.getProps() instanceof NodeElementProps childNodeProps) {
            List<String> ancestorIds = cache.getAncestors(childNodeProps.getId()).stream()
                    .map(org.eclipse.sirius.components.representations.Element::getProps)
                    .filter(NodeElementProps.class::isInstance)
                    .map(NodeElementProps.class::cast)
                    .map(NodeElementProps::getId)
                    .toList();
            result = ancestorIds.contains(parentNodeProps.getId());
        } else {
            this.logger.warn("Cannot check graphical containment between {} and {}", parentNodeElement, childNodeElement);
        }
        return result;
    }

    /**
     * Get all the actor {@link PartUsage} in {@code eObject}'s {@link ResourceSet}.
     *
     * @param eObject
     *            the contextual eObject
     * @return the list of actor {@link PartUsage} in {@code eObject}'s {@link ResourceSet}
     */
    public List<PartUsage> getAllReachableActors(EObject eObject) {
        String type = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPartUsage());
        var allPartUsage = this.utilService.getAllReachable(eObject, type);
        return allPartUsage.stream()
                .filter(PartUsage.class::isInstance)
                .map(PartUsage.class::cast)
                .filter(this::isActor)
                .toList();
    }

    public List<Element> getAllReachableRequirements(EObject eObject) {
        List<EObject> allRequirementUsages = this.utilService.getAllReachable(eObject, SysmlPackage.eINSTANCE.getRequirementUsage(), false);
        List<EObject> allRequirementDefinitions = this.utilService.getAllReachable(eObject, SysmlPackage.eINSTANCE.getRequirementDefinition(), false);
        return Stream.concat(allRequirementUsages.stream(), allRequirementDefinitions.stream())
                .filter(Element.class::isInstance)
                .map(Element.class::cast)
                .toList();
    }

    /**
     * Returns {@code true} if the provided {@code element} is an actor, {@code false} otherwise.
     * <p>
     * An actor (typically of a UseCase or Requirement) is a kind of parameter stored in an {@link ActorMembership}.
     * </p>
     *
     * @param element
     *            the element to check
     * @return {@code true} if the provided {@code element} is an actor, {@code false} otherwise
     */
    public boolean isActor(Element element) {
        return element instanceof PartUsage && element.getOwningMembership() instanceof ActorMembership;
    }

    /**
     * Returns {@code true} if the provided annotated element of the given {@code element} is represented on the
     * diagram, {@code false} otherwise.
     *
     * @param element
     *            the element to check
     * @param diagramContext
     *            the diagram context
     * @param editingContext
     *            the editing context
     * @return {@code true} if the provided annotated element of {@code element} is represented on the diagram,
     *         {@code false} otherwise
     */
    public boolean showAnnotatingNode(Element element, IDiagramContext diagramContext, IEditingContext editingContext) {
        boolean displayAnnotatingNode = false;
        if (element instanceof AnnotatingElement ae && diagramContext != null && editingContext != null) {
            EList<Element> annotatedElements = ae.getAnnotatedElement();
            IDiagramElement matchingDiagramElement = null;

            // specific case of an AnnotatingNode related to the ViewUsage corresponding to the diagram or the container
            // of the ViewUsage.
            displayAnnotatingNode = this.isAnnotatingNodeOnRoot(diagramContext, editingContext, annotatedElements);

            if (!displayAnnotatingNode) {
                for (Node node : diagramContext.getDiagram().getNodes()) {
                    matchingDiagramElement = this.getOneMatchingAnnotatedNode(node, annotatedElements, diagramContext, editingContext);
                    if (matchingDiagramElement != null) {
                        displayAnnotatingNode = true;
                        break;
                    }
                }
            }
            if (!displayAnnotatingNode) {
                for (Edge edge : diagramContext.getDiagram().getEdges()) {
                    matchingDiagramElement = this.getOneMatchingAnnotatedEdge(edge, annotatedElements, diagramContext, editingContext);
                    if (matchingDiagramElement != null) {
                        displayAnnotatingNode = true;
                        break;
                    }
                }
            }
        } else {
            // drag & drop from explorer view
            displayAnnotatingNode = true;
        }
        return displayAnnotatingNode;
    }

    private boolean isAnnotatingNodeOnRoot(IDiagramContext diagramContext, IEditingContext editingContext, EList<Element> annotatedElements) {
        boolean isAnnotatingNodeOnRoot = false;
        String diagramTargetObjectId = diagramContext.getDiagram().getTargetObjectId();
        Element diagramTargetObject = this.objectSearchService.getObject(editingContext, diagramTargetObjectId).stream()
                .filter(Element.class::isInstance)
                .map(Element.class::cast)
                .findFirst()
                .orElse(null);
        if (diagramTargetObject instanceof ViewUsage viewUsage) {
            if (annotatedElements.contains(viewUsage)) {
                isAnnotatingNodeOnRoot = true;
            } else {
                isAnnotatingNodeOnRoot = annotatedElements.contains(viewUsage.getOwner());
            }
        }
        return isAnnotatingNodeOnRoot;
    }

    private boolean isReferencingPerformActionUsage(PerformActionUsage pau) {
        // the given PerformActionUsage is a referencing PerformActionUsage if it contains a reference subsetting
        // pointing to an action.
        ReferenceSubsetting referenceSubSetting = pau.getOwnedReferenceSubsetting();
        return referenceSubSetting != null && referenceSubSetting.getReferencedFeature() instanceof ActionUsage;
    }

    private Edge getOneMatchingAnnotatedEdge(Edge edge, List<Element> annotatedElements, IDiagramContext diagramContext, IEditingContext editingContext) {
        Edge matchingAnnotatedEdge = null;
        Optional<Object> semanticNodeOpt = this.objectSearchService.getObject(editingContext, edge.getTargetObjectId());
        if (semanticNodeOpt.isPresent()) {
            if (annotatedElements.contains(semanticNodeOpt.get())) {
                boolean isDeletingAnnotatingEdge = diagramContext.getViewDeletionRequests().stream()
                        .anyMatch(viewDeletionRequest -> Objects.equals(viewDeletionRequest.getElementId(), edge.getId()));
                if (!isDeletingAnnotatingEdge) {
                    // annotating edge is present and it is not planned to be removed
                    matchingAnnotatedEdge = edge;
                }
                // Do not browse children of annotating edge which is planned to be removed
                return matchingAnnotatedEdge;
            }
        }
        return matchingAnnotatedEdge;
    }

    private Node getOneMatchingAnnotatedNode(Node node, List<Element> annotatedElements, IDiagramContext diagramContext, IEditingContext editingContext) {
        Node matchingAnnotatedNode = null;
        Optional<Object> semanticNodeOpt = this.objectSearchService.getObject(editingContext, node.getTargetObjectId());
        if (semanticNodeOpt.isPresent()) {
            if (annotatedElements.contains(semanticNodeOpt.get())) {
                boolean isDeletingAnnotatingNode = diagramContext.getViewDeletionRequests().stream()
                        .anyMatch(viewDeletionRequest -> Objects.equals(viewDeletionRequest.getElementId(), node.getId()));
                if (!isDeletingAnnotatingNode) {
                    // annotating node is present and it is not planned to be removed
                    matchingAnnotatedNode = node;
                }
                // Do not browse children of annotating node which is planned to be removed
                return matchingAnnotatedNode;
            }
        }
        matchingAnnotatedNode = this.getFirstMatchingChildAnnotatedNode(node, annotatedElements, diagramContext, editingContext);
        return matchingAnnotatedNode;
    }

    private Node getFirstMatchingChildAnnotatedNode(Node node, List<Element> annotatedElements, IDiagramContext diagramContext, IEditingContext editingContext) {
        List<Node> childrenNodes = Stream.concat(node.getChildNodes().stream(), node.getBorderNodes().stream()).toList();
        for (Node childNode : childrenNodes) {
            Node matchingChildAnnotatedNode = this.getOneMatchingAnnotatedNode(childNode, annotatedElements, diagramContext, editingContext);
            if (matchingChildAnnotatedNode != null) {
                return matchingChildAnnotatedNode;
            }
        }
        return null;
    }
}
