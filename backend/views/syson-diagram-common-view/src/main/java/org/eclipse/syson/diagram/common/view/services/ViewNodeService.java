/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramService;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramServices;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.IDiagramElement;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.diagrams.elements.NodeElementProps;
import org.eclipse.sirius.components.diagrams.renderer.DiagramRenderingCache;
import org.eclipse.syson.services.NodeDescriptionService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AnnotatingElement;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expose;
import org.eclipse.syson.sysml.MembershipExpose;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.util.NodeFinder;
import org.eclipse.syson.util.StandardDiagramsConstants;
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

    private final MetamodelQueryElementService queryElementService;

    private final UtilService utilService;

    private final ElementUtil elementUtil;

    public ViewNodeService(IObjectSearchService objectSearchService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.queryElementService = new MetamodelQueryElementService();
        this.utilService = new UtilService();
        this.elementUtil = new ElementUtil();
    }

    /**
     * Get the list of elements to expose for a given {@link Element}.
     *
     * @param element
     *            the given {@link Element}
     * @param domainType
     *            the {@link EClass} domainType to filter
     * @param ancestors
     *            the list of ancestors of the node (semantic elements corresponding to graphical ancestors). It
     *            corresponds to a variable accessible from the variable manager.
     * @param editingContext
     *            the {@link IEditingContext} of the node. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link DiagramContext} of the node. It corresponds to a variable accessible from the variable
     *            manager.
     * @return
     */
    public List<Element> getExposedElements(Element element, EClass domainType, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.getExposedElements(element, null, domainType, ancestors, editingContext, diagramContext);
    }

    /**
     * Get the list of elements to expose for a given {@link Element}.
     *
     * @param element
     *            the given {@link Element}
     * @param element
     *            the parent element of the given {@link Element}
     * @param domainType
     *            the {@link EClass} domainType to filter
     * @param ancestors
     *            the list of ancestors of the element (semantic elements corresponding to graphical ancestors). It
     *            corresponds to a variable accessible from the variable manager.
     * @param editingContext
     *            the {@link IEditingContext} of the element. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link DiagramContext} of the element. It corresponds to a variable accessible from the variable
     *            manager.
     * @return
     */
    public List<Element> getExposedElements(Element element, Element parent, EClass domainType, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        List<Element> elementsToExpose = new ArrayList<>();
        if (element instanceof ViewUsage viewUsage) {
            var exposedElements = new ArrayList<Element>();
            if (parent == null) {
                // we only want to display exposed elements that could be displayed as root nodes or nested tree nodes,
                // but not nodes that could be displayed inside other nodes
                exposedElements.addAll(this.getDirectExposedElements(viewUsage));
            } else {
                exposedElements.addAll(viewUsage.getExposedElement());
            }
            var filteredExposedElements = exposedElements.stream()
                    .filter(elt -> this.isTypeOf(elt, domainType) && (parent == null || (!Objects.equals(parent, elt) && EMFUtils.isAncestor(parent, elt))))
                    .toList();
            elementsToExpose.addAll(filteredExposedElements);
            // if it is not a General View, we don't want to display nested nodes as tree (i.e. sibling nodes +
            // composition edges), if it an ActionFlow view, we only want to display Action Nodes as root nodes, ...
            var viewDefKind = this.utilService.getViewDefinitionKind(viewUsage, ancestors, editingContext);
            for (Element filteredExposedElement : filteredExposedElements) {
                boolean canBeDisplayed = new ViewFilterSwitch(viewDefKind, exposedElements, parent).doSwitch(filteredExposedElement);
                if (!canBeDisplayed) {
                    elementsToExpose.remove(filteredExposedElement);
                }
            }
        } else if (ancestors != null) {
            // Retrieve ViewUsage exposing the given element
            var viewUsageContainingElement = ancestors.stream()
                    .filter(ViewUsage.class::isInstance)
                    .map(ViewUsage.class::cast)
                    .findFirst();
            if (viewUsageContainingElement.isPresent()) {
                // expose all elements of this ViewUsage
                elementsToExpose.addAll(this.getExposedElements(viewUsageContainingElement.get(), element, domainType, ancestors, editingContext,
                        diagramContext));
            }
        }
        return elementsToExpose;
    }

    /**
     * Check if the given {@link Element} displayed in the given {@link DiagramContext} is of the given
     * {@link ViewDefinition}.
     *
     * @param element
     *            the given {@link Element}.
     * @param viewDefinition
     *            the {@link ViewDefinition} to check.
     * @param ancestors
     *            the list of ancestors of the element (semantic elements corresponding to graphical ancestors). It
     *            corresponds to a variable accessible from the variable manager.
     * @param editingContext
     *            the {@link IEditingContext} of the element. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link DiagramContext} of the element. It corresponds to a variable accessible from the variable
     *            manager.
     * @return true if the given {@link Element} displayed in the given {@link DiagramContext} is of the given
     *         {@link ViewDefinition}, false otherwise.
     */
    public boolean isView(Element element, String viewDefinition, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        boolean isView = false;
        if (element instanceof ViewUsage viewUsage) {
            var types = viewUsage.getType();
            if ((types == null || types.isEmpty()) && Objects.equals(StandardDiagramsConstants.GV_QN, viewDefinition)) {
                // a ViewUsage without a ViewDefinition is considered as a GeneralView
                isView = true;
            } else {
                var generalViewViewDef = this.elementUtil.findByNameAndType(viewUsage, viewDefinition, ViewDefinition.class);
                Type type = types.get(0);
                isView = Objects.equals(type, generalViewViewDef);
            }
        } else {
            // Retrieve ViewUsage exposing the given element
            var viewUsageContainingElement = ancestors.stream().filter(ViewUsage.class::isInstance).map(ViewUsage.class::cast).findFirst();
            if (viewUsageContainingElement.isPresent()) {
                isView = this.isView(viewUsageContainingElement.get(), viewDefinition, ancestors, editingContext, diagramContext);
            }
        }
        return isView;
    }

    /**
     * Check if the given {@link Element} displayed in the given {@link DiagramContext} is of the given
     * {@link ViewDefinition}.
     *
     * @param element
     *            the given {@link Element}.
     * @param viewDefinition
     *            the {@link ViewDefinition} to check.
     * @param selectedNode
     *            the selected node. It corresponds to a variable accessible from the variable manager.
     * @param editingContext
     *            the {@link IEditingContext} of the element. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link DiagramContext} of the element. It corresponds to a variable accessible from the variable
     *            manager.
     * @return true if the given {@link Element} displayed in the given {@link DiagramContext} is of the given
     *         {@link ViewDefinition}, false otherwise.
     */
    public boolean isView(Element element, String viewDefinition, Node selectedNode, IEditingContext editingContext, DiagramContext diagramContext) {
        boolean isView = false;
        if (element instanceof ViewUsage viewUsage) {
            var types = viewUsage.getType();
            if ((types == null || types.isEmpty()) && Objects.equals(StandardDiagramsConstants.GV_QN, viewDefinition)) {
                isView = true;
            } else {
                var generalViewViewDef = this.elementUtil.findByNameAndType(viewUsage, viewDefinition, ViewDefinition.class);
                Type type = types.get(0);
                isView = Objects.equals(type, generalViewViewDef);
            }
        } else {
            // Retrieve ViewUsage exposing the given element
            var ancestors = this.getAncestors(selectedNode, diagramContext.diagram(), editingContext);
            var viewUsageContainingElement = ancestors.stream().filter(ViewUsage.class::isInstance).map(ViewUsage.class::cast).findFirst();
            if (viewUsageContainingElement.isPresent()) {
                // expose all elements of this ViewUsage
                isView = this.isView(viewUsageContainingElement.get(), viewDefinition, selectedNode, editingContext, diagramContext);
            }
        }
        return isView;
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
     *            the {@link DiagramContext} of the element. It corresponds to a variable accessible from the variable
     *            manager.
     * @param editingContext
     *            the {@link IEditingContext} of the element. It corresponds to a variable accessible from the variable
     *            manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @return the node containing the compartments
     */
    public Node revealCompartment(Node node, Element targetElement, DiagramContext diagramContext, IEditingContext editingContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {

        if (!this.utilService.isUnsynchronized(targetElement)
                && !this.needToRevealCompartment(targetElement, this.isView(targetElement, StandardDiagramsConstants.GV_QN, node, editingContext, diagramContext))) {
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

        List<NodeDescription> compartmentCandidates = nodeDescriptionService.getNodeDescriptionsForRenderingElementAsChild(targetElement, parentObject, allChildNodeDescriptions,
                convertedNodes, editingContext, diagramContext);

        if (!compartmentCandidates.isEmpty()) {
            if (compartmentCandidates.size() > 1) {
                this.logger.warn("Multiple compartment candidates found for {} in {}.", targetElement.eClass().getName(), node.toString());
            }
            NodeFinder nodeFinder = new NodeFinder(diagramContext.diagram());
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
     * Get all Actors ({@link PartUsage}) in {@link ViewUsage}'s exposed elements.
     *
     * @param element
     *            the {@link Element} for which we want the Actors.
     * @param domainType
     *            the domain type to the elements to retrieve
     * @param ancestors
     *            the given ancestors (semantic elements of the graphical nodes that are the ancestors) of the Node on
     *            which this service has been called.
     * @param editingContext
     *            the given {@link IEditingContext} in which this service has been called.
     * @param diagramContext
     *            the given {@link DiagramContext} in which this service has been called.
     * @return the list of Actor {@link PartUsage}s in {@link ViewUsage}'s exposed elements.
     */
    public List<PartUsage> getExposedActors(Element element, EClass domainType, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.getExposedElements(element, domainType, ancestors, editingContext, diagramContext).stream()
                .filter(PartUsage.class::isInstance)
                .map(PartUsage.class::cast)
                .filter(this.queryElementService::isActor)
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
    public boolean showAnnotatingNode(Element element, DiagramContext diagramContext, IEditingContext editingContext) {
        boolean displayAnnotatingNode = false;
        if (element instanceof AnnotatingElement ae && diagramContext != null && editingContext != null) {
            EList<Element> annotatedElements = ae.getAnnotatedElement();
            IDiagramElement matchingDiagramElement = null;

            // specific case of an AnnotatingNode related to the ViewUsage corresponding to the diagram or the container
            // of the ViewUsage.
            displayAnnotatingNode = this.isAnnotatingNodeOnRoot(diagramContext, editingContext, annotatedElements);

            if (!displayAnnotatingNode) {
                for (Node node : diagramContext.diagram().getNodes()) {
                    matchingDiagramElement = this.getOneMatchingAnnotatedNode(node, annotatedElements, diagramContext, editingContext);
                    if (matchingDiagramElement != null) {
                        displayAnnotatingNode = true;
                        break;
                    }
                }
            }
            if (!displayAnnotatingNode) {
                for (Edge edge : diagramContext.diagram().getEdges()) {
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

    /**
     * Get the elements we want to display for a ViewUsage, based on its exposed elements. For example for an exposed
     * Package, we do not want to also expose its children. The children of the Package will be display as child node
     * inside the Package node.
     *
     * @param viewUsage
     *            the given {@link ViewUsage}.
     * @return a set of {@link Element}s.
     */
    protected Set<Element> getDirectExposedElements(ViewUsage viewUsage) {
        var directExposedElements = new HashSet<Element>();
        List<Expose> exposedElements = viewUsage.getOwnedRelationship().stream()
                .filter(Expose.class::isInstance)
                .map(Expose.class::cast)
                .toList();
        for (Expose exposedElement : exposedElements) {
            Element importedElement = exposedElement.getImportedElement();
            if (importedElement instanceof Package) {
                directExposedElements.add(importedElement);
            } else if (exposedElement instanceof MembershipExpose membershipExpose) {
                var importedMembership = membershipExpose.getImportedMembership();
                if (importedMembership != null) {
                    var memberElement = importedMembership.getMemberElement();
                    if (memberElement != null && !this.isChildOfExposedPackage(memberElement, exposedElements)) {
                        directExposedElements.add(memberElement);
                        if (exposedElement.isIsRecursive()) {
                            directExposedElements.addAll(this.getRecursiveContents(memberElement));
                        }
                    }
                }

            }
        }
        return directExposedElements;
    }

    /**
     * Check if the given {@link Element} is a child of an exposed Package. In this case, we don't want to display it on
     * the diagram background, or as a ViewUsage child node. It will be displayed inside another node.
     *
     * @param element
     *            the element that could be a child of an exposed element
     * @param exposedElements
     *            the list of exposed elements
     * @return <code>true</code> if the given {@link Element} is a child of an exposed element, <code>false</code>
     *         otherwise.
     */
    protected boolean isChildOfExposedPackage(Element element, List<Expose> exposedElements) {
        boolean isChildOfExposedElement = false;
        for (Expose exposedElement : exposedElements) {
            Element importedElement = exposedElement.getImportedElement();
            if (importedElement instanceof Package && !Objects.equals(element, importedElement) && EMFUtils.isAncestor(importedElement, element)) {
                isChildOfExposedElement = true;
                break;
            }
        }
        return isChildOfExposedElement;
    }

    protected List<Element> getRecursiveContents(Element element) {
        var contents = new ArrayList<Element>();
        var ownedElements = element.getOwnedElement().stream().filter(Objects::nonNull).toList();
        contents.addAll(ownedElements);
        ownedElements.forEach(oe -> {
            contents.addAll(this.getRecursiveContents(oe));
        });
        return contents;
    }

    protected boolean isAnnotatingNodeOnRoot(DiagramContext diagramContext, IEditingContext editingContext, EList<Element> annotatedElements) {
        boolean isAnnotatingNodeOnRoot = false;
        String diagramTargetObjectId = diagramContext.diagram().getTargetObjectId();
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

    protected boolean isReferencingPerformActionUsage(PerformActionUsage pau) {
        // the given PerformActionUsage is a referencing PerformActionUsage if it contains a reference subsetting
        // pointing to an action.
        ReferenceSubsetting referenceSubSetting = pau.getOwnedReferenceSubsetting();
        return referenceSubSetting != null && referenceSubSetting.getReferencedFeature() instanceof ActionUsage;
    }

    protected Edge getOneMatchingAnnotatedEdge(Edge edge, List<Element> annotatedElements, DiagramContext diagramContext, IEditingContext editingContext) {
        Edge matchingAnnotatedEdge = null;
        Optional<Object> semanticNodeOpt = this.objectSearchService.getObject(editingContext, edge.getTargetObjectId());
        if (semanticNodeOpt.isPresent()) {
            if (annotatedElements.contains(semanticNodeOpt.get())) {
                boolean isDeletingAnnotatingEdge = diagramContext.viewDeletionRequests().stream()
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

    protected Node getOneMatchingAnnotatedNode(Node node, List<Element> annotatedElements, DiagramContext diagramContext, IEditingContext editingContext) {
        Node matchingAnnotatedNode = null;
        Optional<Object> semanticNodeOpt = this.objectSearchService.getObject(editingContext, node.getTargetObjectId());
        if (semanticNodeOpt.isPresent()) {
            if (annotatedElements.contains(semanticNodeOpt.get())) {
                boolean isDeletingAnnotatingNode = diagramContext.viewDeletionRequests().stream()
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

    protected Node getFirstMatchingChildAnnotatedNode(Node node, List<Element> annotatedElements, DiagramContext diagramContext, IEditingContext editingContext) {
        List<Node> childrenNodes = Stream.concat(node.getChildNodes().stream(), node.getBorderNodes().stream()).toList();
        for (Node childNode : childrenNodes) {
            Node matchingChildAnnotatedNode = this.getOneMatchingAnnotatedNode(childNode, annotatedElements, diagramContext, editingContext);
            if (matchingChildAnnotatedNode != null) {
                return matchingChildAnnotatedNode;
            }
        }
        return null;
    }

    /**
     * Check if the compartment associated to the given new {@link Element} created should be revealed or not.
     *
     * @param element
     *            the given new {@link Element}
     * @param isGeneralView
     *            if the ViewDefinition in which this new element will be displayed is a GeneralView or not.
     * @return <code>true</code> if the compartment need to be revealed, <code>false</code> otherwise.
     */
    protected boolean needToRevealCompartment(Element element, boolean isGeneralView) {
        return new RevealCompartmentSwitch(isGeneralView).doSwitch(element);
    }

    /**
     * Get the list of ancestors of the element (semantic elements corresponding to graphical ancestors).
     *
     * @param selectedNode
     *            the selected Node. It corresponds to a variable accessible from the variable manager.
     * @param diagram
     *            the diagram containing the selected Node.
     * @param editingContext
     *            the {@link IEditingContext} of the element. It corresponds to a variable accessible from the variable
     *            manager.
     * @return a list of Objects.
     */
    protected List<Object> getAncestors(Node selectedNode, Diagram diagram, IEditingContext editingContext) {
        var ancestors = new ArrayList<>();
        var parent = new NodeFinder(diagram).getParent(selectedNode);
        if (parent instanceof Node parentNode) {
            var parentObject = this.objectSearchService.getObject(editingContext, parentNode.getTargetObjectId());
            if (parentObject.isPresent()) {
                ancestors.add(parentObject.get());
                ancestors.addAll(this.getAncestors(parentNode, diagram, editingContext));
            }
        } else if (parent instanceof Diagram) {
            var parentObject = this.objectSearchService.getObject(editingContext, diagram.getTargetObjectId());
            if (parentObject.isPresent()) {
                ancestors.add(parentObject.get());
            }
        }
        return ancestors;
    }

    /**
     * Check if the given {@link Element}'s {@link EClass} is the same than the given domainType.
     *
     * @param element
     *            the given {@link Element}.
     * @param domainType
     *            the given domainType as an {@link EClass}.
     * @return <code>true</code> if the given {@link Element}'s {@link EClass} is the same than the given domainType,
     *         <code>false</code> otherwise.
     */
    protected boolean isTypeOf(Element element, EClass domainType) {
        return element != null && Objects.equals(element.eClass(), domainType);
    }
}
