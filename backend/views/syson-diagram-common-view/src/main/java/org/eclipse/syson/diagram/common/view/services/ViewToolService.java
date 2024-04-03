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
package org.eclipse.syson.diagram.common.view.services;

import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewDeletionRequest;
import org.eclipse.sirius.components.diagrams.components.NodeContainmentKind;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.syson.services.ToolService;
import org.eclipse.syson.sysml.ConstraintDefinition;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Usage;

/**
 * Tool-related Java services used by all diagrams.
 *
 * @author arichard
 */
public class ViewToolService extends ToolService {

    private final IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService;

    public ViewToolService(IObjectService objectService, IRepresentationDescriptionSearchService representationDescriptionSearchService, IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService) {
        super(objectService, representationDescriptionSearchService);
        this.viewRepresentationDescriptionSearchService = Objects.requireNonNull(viewRepresentationDescriptionSearchService);
    }

    /**
     * Called by "Add existing elements" tool from General View diagram or General View Package node. Add nodes that are
     * not present in the diagram or the selectedNode (i.e. a Package).
     *
     * @param pkg
     *            the {@link Package} corresponding to the target object of the Diagram or the {@link Node} Package on
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
     * @return the input {@link Package}.
     */
    public Package addExistingElements(Package pkg, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean recursive) {
        var members = pkg.getOwnedMember();
        var diagramDescription = this.viewRepresentationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());
        DiagramDescription representationDescription = (DiagramDescription) diagramDescription.get();

        members.stream()
                .filter(member -> !this.isPresent(member, this.getChildNodes(diagramContext, selectedNode)))
                .forEach(member -> {
                    this.createView(member, editingContext, diagramContext, selectedNode, convertedNodes);
                    if (recursive && member instanceof PartDefinition partDef) {
                        Node fakeNode = this.createFakeNode(partDef, selectedNode, diagramContext, representationDescription, convertedNodes);
                        this.addExistingSubElements(partDef, editingContext, diagramContext, fakeNode, selectedNode, representationDescription, convertedNodes);
                    } else if (recursive && member instanceof PartUsage partUsage) {
                        Node fakeNode = this.createFakeNode(partUsage, selectedNode, diagramContext, representationDescription, convertedNodes);
                        this.addExistingSubElements(partUsage, editingContext, diagramContext, fakeNode, selectedNode, representationDescription, convertedNodes);
                    } else if (recursive && member instanceof Package subPkg) {
                        Node fakeNode = this.createFakeNode(subPkg, selectedNode, diagramContext, representationDescription, convertedNodes);
                        this.addExistingSubElements(subPkg, editingContext, diagramContext, fakeNode, representationDescription, convertedNodes);
                    }
                });
        return pkg;
    }

    /**
     * Called by "Add existing nested PartUsage" tool from General View PartUsage node. Add nodes that are not present
     * in selectedNode (i.e. a PartUsage).
     *
     * @param partUsage
     *            the {@link PartUsage} corresponding to the target object of the Diagram or the {@link Node} PartUsage
     *            on which the tool has been called.
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

        Object parentNode = this.getParentNode(partUsage, selectedNode, diagramContext);
        nestedParts.stream()
                .filter(subPartUsage -> !this.isPresent(subPartUsage, this.getChildNodes(diagramContext, parentNode)))
                .forEach(subPartUsage -> {
                    this.createView(subPartUsage, editingContext, diagramContext, parentNode, convertedNodes);
                    if (recursive) {
                        Node fakeNode = this.createFakeNode(subPartUsage, parentNode, diagramContext, representationDescription, convertedNodes);
                        this.addExistingSubElements(subPartUsage, editingContext, diagramContext, fakeNode, parentNode, representationDescription, convertedNodes);
                    }
                });
        return partUsage;
    }

    /**
     * Called by "Add existing nested elements" tool from General View PartDefinition node. Add nodes that are not
     * present in selectedNode (i.e. a PartUsage/ItemUsage).
     *
     * @param partDef
     *            the {@link PartDefinition} corresponding to the target object of the Diagram or the {@link Node}
     *            PartDefinition on which the tool has been called.
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
     * @return the input {@link PartDefinition}.
     */
    public PartDefinition addExistingElements(PartDefinition partDef, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean recursive) {
        var nestedItems = partDef.getOwnedItem();
        var diagramDescription = this.viewRepresentationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());
        DiagramDescription representationDescription = (DiagramDescription) diagramDescription.get();

        Object parentNode = this.getParentNode(partDef, selectedNode, diagramContext);
        nestedItems.stream()
                .filter(member -> !this.isPresent(member, this.getChildNodes(diagramContext, parentNode)))
                .forEach(member -> {
                    this.createView(member, editingContext, diagramContext, parentNode, convertedNodes);
                    if (recursive && member instanceof PartUsage subPartUsage) {
                        Node fakeNode = this.createFakeNode(subPartUsage, parentNode, diagramContext, representationDescription, convertedNodes);
                        this.addExistingSubElements(subPartUsage, editingContext, diagramContext, fakeNode, parentNode, representationDescription, convertedNodes);
                    }
                });
        return partDef;
    }

    public Usage becomeNestedUsage(Usage usage, Element newContainer) {
        var eContainer = usage.eContainer();
        if (eContainer instanceof FeatureMembership featureMembership) {
            newContainer.getOwnedRelationship().add(featureMembership);
        } else if (eContainer instanceof OwningMembership owningMembership) {
            var newFeatureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
            newFeatureMembership.getOwnedRelatedElement().add(usage);
            newContainer.getOwnedRelationship().add(newFeatureMembership);
            EcoreUtil.delete(owningMembership);
        }
        return usage;
    }

    public PartUsage addAsNestedPart(PartDefinition partDefinition, PartUsage partUsage) {
        var eContainer = partUsage.eContainer();
        if (eContainer instanceof FeatureMembership featureMembership) {
            partDefinition.getOwnedRelationship().add(featureMembership);
        } else if (eContainer instanceof OwningMembership owningMembership) {
            var newFeatureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
            newFeatureMembership.getOwnedRelatedElement().add(partUsage);
            partDefinition.getOwnedRelationship().add(newFeatureMembership);
            EcoreUtil.delete(owningMembership);
        }
        return partUsage;
    }

    private Package addExistingSubElements(Package pkg, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode, DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var members = pkg.getOwnedMember();

        members.stream()
                .forEach(member -> {
                    this.createView(member, editingContext, diagramContext, selectedNode, convertedNodes);
                    if (member instanceof PartDefinition partDef) {
                        Node fakeNode = this.createFakeNode(partDef, selectedNode, diagramContext, diagramDescription, convertedNodes);
                        this.addExistingSubElements(partDef, editingContext, diagramContext, fakeNode, selectedNode, diagramDescription, convertedNodes);
                    } else if (member instanceof PartUsage partUsage) {
                        Node fakeNode = this.createFakeNode(partUsage, selectedNode, diagramContext, diagramDescription, convertedNodes);
                        this.addExistingSubElements(partUsage, editingContext, diagramContext, fakeNode, selectedNode, diagramDescription, convertedNodes);
                    } else if (member instanceof Package subPkg) {
                        Node fakeNode = this.createFakeNode(subPkg, selectedNode, diagramContext, diagramDescription, convertedNodes);
                        this.addExistingSubElements(subPkg, editingContext, diagramContext, fakeNode, diagramDescription, convertedNodes);
                    }
                });
        return pkg;
    }

    private PartUsage addExistingSubElements(PartUsage partUsage, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode, Object parentNode, DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var nestedParts = partUsage.getNestedPart();

        nestedParts.stream()
                .forEach(subPartUsage -> {
                    this.createView(subPartUsage, editingContext, diagramContext, parentNode, convertedNodes);
                    Node fakeNode = this.createFakeNode(subPartUsage, parentNode, diagramContext, diagramDescription, convertedNodes);
                    this.addExistingSubElements(subPartUsage, editingContext, diagramContext, fakeNode, parentNode, diagramDescription, convertedNodes);
                });
        return partUsage;
    }

    private PartDefinition addExistingSubElements(PartDefinition partDef, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode, Object parentNode, DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var nestedItems = partDef.getOwnedItem();

        nestedItems.stream()
                .forEach(member -> {
                    this.createView(member, editingContext, diagramContext, parentNode, convertedNodes);
                    if (member instanceof PartUsage subPartUsage) {
                        Node fakeNode = this.createFakeNode(subPartUsage, parentNode, diagramContext, diagramDescription, convertedNodes);
                        this.addExistingSubElements(subPartUsage, editingContext, diagramContext, fakeNode, parentNode, diagramDescription, convertedNodes);
                    }
                });
        return partDef;
    }

    public Element dropRequirementSubjectFromDiagram(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (targetElement instanceof RequirementUsage || targetElement instanceof RequirementDefinition) {
            boolean noExistingSubject = targetElement.getOwnedRelationship().stream()
                    .filter(SubjectMembership.class::isInstance)
                    .map(SubjectMembership.class::cast)
                    .findFirst().isEmpty();
            if (noExistingSubject) {
                this.moveElement(droppedElement, droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes);
            }
        }
        return droppedElement;
    }

    public Element dropElementFromDiagramInRequirementAssumeConstraintCompartment(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (droppedElement instanceof ConstraintUsage droppedConstraint && (targetElement instanceof RequirementUsage || targetElement instanceof RequirementDefinition)) {
            this.moveContraintInRequirementConstraintCompartment(droppedConstraint, targetElement, RequirementConstraintKind.ASSUMPTION);
            this.createView(droppedElement, editingContext, diagramContext, targetNode, convertedNodes, NodeContainmentKind.CHILD_NODE);
            diagramContext.getViewDeletionRequests().add(ViewDeletionRequest.newViewDeletionRequest().elementId(droppedNode.getId()).build());
        }
        return droppedElement;
    }

    public Element dropElementFromDiagramInRequirementRequireConstraintCompartment(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (droppedElement instanceof ConstraintUsage droppedConstraint && (targetElement instanceof RequirementUsage || targetElement instanceof RequirementDefinition)) {
            this.moveContraintInRequirementConstraintCompartment(droppedConstraint, targetElement, RequirementConstraintKind.REQUIREMENT);
            this.createView(droppedElement, editingContext, diagramContext, targetNode, convertedNodes, NodeContainmentKind.CHILD_NODE);
            diagramContext.getViewDeletionRequests().add(ViewDeletionRequest.newViewDeletionRequest().elementId(droppedNode.getId()).build());
        }
        return droppedElement;
    }

    private void moveContraintInRequirementConstraintCompartment(ConstraintUsage droppedConstraint, Element requirement, RequirementConstraintKind kind) {
        var oldMembership = droppedConstraint.eContainer();
        var membership = SysmlFactory.eINSTANCE.createRequirementConstraintMembership();
        membership.getOwnedRelatedElement().add(droppedConstraint);
        membership.setKind(kind);
        requirement.getOwnedRelationship().add(membership);
        EcoreUtil.delete(oldMembership);
    }

    public Element dropElementFromDiagramInConstraintCompartment(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (droppedElement instanceof ConstraintUsage droppedConstraint) {
            if (targetElement instanceof ConstraintUsage || targetElement instanceof ConstraintDefinition) {
                var oldMembership = droppedConstraint.eContainer();
                var membership = SysmlFactory.eINSTANCE.createFeatureMembership();
                membership.getOwnedRelatedElement().add(droppedConstraint);
                targetElement.getOwnedRelationship().add(membership);
                EcoreUtil.delete(oldMembership);
                this.createView(droppedElement, editingContext, diagramContext, targetNode, convertedNodes, NodeContainmentKind.CHILD_NODE);
                diagramContext.getViewDeletionRequests().add(ViewDeletionRequest.newViewDeletionRequest().elementId(droppedNode.getId()).build());
            }
        }
        return droppedElement;
    }
}
