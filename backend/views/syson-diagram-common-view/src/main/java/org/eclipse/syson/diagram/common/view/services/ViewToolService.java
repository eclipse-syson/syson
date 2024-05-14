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
import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
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
import org.eclipse.syson.services.ElementInitializerSwitch;
import org.eclipse.syson.services.ToolService;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ConstraintDefinition;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Tool-related Java services used by all diagrams.
 *
 * @author arichard
 */
public class ViewToolService extends ToolService {

    protected final IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService;

    private final ElementInitializerSwitch elementInitializerSwitch;

    public ViewToolService(IObjectService objectService, IRepresentationDescriptionSearchService representationDescriptionSearchService,
            IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService) {
        super(objectService, representationDescriptionSearchService);
        this.viewRepresentationDescriptionSearchService = Objects.requireNonNull(viewRepresentationDescriptionSearchService);
        this.elementInitializerSwitch = new ElementInitializerSwitch();
    }

    /**
     * Called by "Add existing elements" tool from General View diagram or General View Package node. Add nodes that are
     * not present in the diagram or the selectedNode (i.e. a Package).
     *
     * @param namespace
     *            the {@link Namespace} corresponding to the target object of the Diagram or the {@link Node} Package on
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
    public Namespace addExistingElements(Namespace namespace, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean recursive) {
        var members = namespace.getOwnedMember();
        var diagramDescription = this.viewRepresentationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());
        DiagramDescription representationDescription = (DiagramDescription) diagramDescription.get();

        members.stream()
            .filter(member -> !this.isPresent(member, this.getChildNodes(diagramContext, selectedNode)))
            .forEach(member -> {
                this.createView(member, editingContext, diagramContext, selectedNode, convertedNodes);
                if (recursive && member instanceof Definition definition) {
                    Node fakeNode = this.createFakeNode(definition, selectedNode, diagramContext, representationDescription, convertedNodes);
                    this.addExistingSubElements(definition, editingContext, diagramContext, fakeNode, selectedNode, representationDescription, convertedNodes);
                } else if (recursive && member instanceof Usage usage) {
                    Node fakeNode = this.createFakeNode(usage, selectedNode, diagramContext, representationDescription, convertedNodes);
                    this.addExistingSubElements(usage, editingContext, diagramContext, fakeNode, selectedNode, representationDescription, convertedNodes);
                } else if (recursive && member instanceof Namespace subNs) {
                    Node fakeNode = this.createFakeNode(subNs, selectedNode, diagramContext, representationDescription, convertedNodes);
                    this.addExistingSubElements(subNs, editingContext, diagramContext, fakeNode, representationDescription, convertedNodes);
                }
            });
        return namespace;
    }

    /**
     * Called by "Add existing nested elements" tool from General View Usage node. Add nodes that are not present in
     * selectedNode (e.g. a PartUsage).
     *
     * @param usage
     *            the {@link Usage} corresponding to the target object of the Diagram or the {@link Node} Usage on which
     *            the tool has been called.
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
     * @return the input {@link Usage}.
     */
    public Usage addExistingNestedElements(Usage usage, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean recursive) {
        var nestedUsages = usage.getNestedUsage();
        var diagramDescription = this.viewRepresentationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());
        DiagramDescription representationDescription = (DiagramDescription) diagramDescription.get();

        Object parentNode = this.getParentNode(usage, selectedNode, diagramContext);
        nestedUsages.stream()
            .filter(nestedUsage -> !this.isPresent(nestedUsage, this.getChildNodes(diagramContext, selectedNode)) && !this.isPresent(nestedUsage, this.getChildNodes(diagramContext, parentNode)))
            .forEach(nestedUsage -> {
                this.createView(nestedUsage, editingContext, diagramContext, parentNode, convertedNodes);
                if (recursive) {
                    Node fakeNode = this.createFakeNode(nestedUsage, parentNode, diagramContext, representationDescription, convertedNodes);
                    this.addExistingSubElements(nestedUsage, editingContext, diagramContext, fakeNode, parentNode, representationDescription, convertedNodes);
                }
            });
        return usage;
    }

    /**
     * Called by "Add existing nested elements" tool from General View Definition node. Add nodes that are not present
     * in selectedNode (e.g. a PartUsage/ItemUsage).
     *
     * @param definition
     *            the {@link Definition} corresponding to the target object of the Diagram or the {@link Node}
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
     * @return the input {@link Definition}.
     */
    public Definition addExistingNestedElements(Definition definition, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean recursive) {
        var nestedUsages = definition.getOwnedUsage();
        var diagramDescription = this.viewRepresentationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());
        DiagramDescription representationDescription = (DiagramDescription) diagramDescription.get();

        Object parentNode = this.getParentNode(definition, selectedNode, diagramContext);
        nestedUsages.stream()
            .filter(nestedUsage -> !this.isPresent(nestedUsage, this.getChildNodes(diagramContext, selectedNode)) && !this.isPresent(nestedUsage, this.getChildNodes(diagramContext, parentNode)))
            .forEach(nestedUsage -> {
                this.createView(nestedUsage, editingContext, diagramContext, parentNode, convertedNodes);
                if (recursive) {
                    Node fakeNode = this.createFakeNode(nestedUsage, parentNode, diagramContext, representationDescription, convertedNodes);
                    this.addExistingSubElements(nestedUsage, editingContext, diagramContext, fakeNode, parentNode, representationDescription, convertedNodes);
                }
            });
        return definition;
    }

    /**
     * Check if a tool that will create an instance of the given type should be available for a diagram associated to
     * the given {@link Namespace}.
     *
     * @param ns
     *            the given {@link Namespace}.
     * @param type
     *            the given type.
     * @return <code>true</code> if the tool should be available, <code>false</code> otherwise.
     */
    public boolean toolShouldBeAvailable(Namespace ns, String type) {
        boolean toolShouldBeAvailable = false;
        EClass domainClass = SysMLMetamodelHelper.toEClass(type);
        if (ns instanceof Package) {
            toolShouldBeAvailable = true;
        } else if (ns instanceof Usage && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        } else if (ns instanceof Definition && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        }
        return toolShouldBeAvailable;
    }

    public Usage becomeNestedUsage(Usage usage, Element newContainer) {
        this.changeOwner(usage, newContainer);
        usage.setIsComposite(true);
        return usage;
    }

    private Element changeOwner(Element element, Element newContainer) {
        var eContainer = element.eContainer();
        if (eContainer instanceof FeatureMembership featureMembership) {
            newContainer.getOwnedRelationship().add(featureMembership);
        } else if (eContainer instanceof OwningMembership owningMembership) {
            var newFeatureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
            newFeatureMembership.getOwnedRelatedElement().add(element);
            newContainer.getOwnedRelationship().add(newFeatureMembership);
            EcoreUtil.delete(owningMembership);
        }
        return element;
    }

    public RequirementUsage becomeObjectiveRequirement(RequirementUsage requirement, Element newContainer) {
        if (newContainer instanceof UseCaseUsage || newContainer instanceof UseCaseDefinition) {
            if (ViewCreateService.isEmptyObjectiveRequirement(newContainer)) {
                var eContainer = requirement.eContainer();
                if (eContainer instanceof ObjectiveMembership objectiveMembership) {
                    // requirement is already an objective of another usecaseXXX
                    newContainer.getOwnedRelationship().add(objectiveMembership);
                } else if (eContainer instanceof OwningMembership owningMembership) {
                    var newObjectiveMembership = SysmlFactory.eINSTANCE.createObjectiveMembership();
                    newObjectiveMembership.getOwnedRelatedElement().add(requirement);
                    newContainer.getOwnedRelationship().add(newObjectiveMembership);
                    EcoreUtil.delete(owningMembership);
                }
            }
        }
        return requirement;
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

    public Namespace addExistingSubElements(Namespace namespace, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode, DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var members = namespace.getOwnedMember();

        members.stream()
            .forEach(member -> {
                this.createView(member, editingContext, diagramContext, selectedNode, convertedNodes);
                if (member instanceof Definition definition) {
                    Node fakeNode = this.createFakeNode(definition, selectedNode, diagramContext, diagramDescription, convertedNodes);
                    this.addExistingSubElements(definition, editingContext, diagramContext, fakeNode, selectedNode, diagramDescription, convertedNodes);
                } else if (member instanceof Usage usage) {
                    Node fakeNode = this.createFakeNode(usage, selectedNode, diagramContext, diagramDescription, convertedNodes);
                    this.addExistingSubElements(usage, editingContext, diagramContext, fakeNode, selectedNode, diagramDescription, convertedNodes);
                } else if (member instanceof Namespace subNs) {
                    Node fakeNode = this.createFakeNode(subNs, selectedNode, diagramContext, diagramDescription, convertedNodes);
                    this.addExistingSubElements(subNs, editingContext, diagramContext, fakeNode, diagramDescription, convertedNodes);
                }
            });
        return namespace;
    }

    public Usage addExistingSubElements(Usage usage, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode, Object parentNode, DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var nestedUsages = usage.getNestedUsage();

        nestedUsages.stream()
            .forEach(subUsage -> {
                this.createView(subUsage, editingContext, diagramContext, parentNode, convertedNodes);
                Node fakeNode = this.createFakeNode(subUsage, parentNode, diagramContext, diagramDescription, convertedNodes);
                this.addExistingSubElements(subUsage, editingContext, diagramContext, fakeNode, parentNode, diagramDescription, convertedNodes);
            });
        return usage;
    }

    public Definition addExistingSubElements(Definition definition, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode, Object parentNode,
            DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var ownedUsages = definition.getOwnedUsage();

        ownedUsages.stream()
            .forEach(subUsage -> {
                this.createView(subUsage, editingContext, diagramContext, parentNode, convertedNodes);
                Node fakeNode = this.createFakeNode(subUsage, parentNode, diagramContext, diagramDescription, convertedNodes);
                this.addExistingSubElements(subUsage, editingContext, diagramContext, fakeNode, parentNode, diagramDescription, convertedNodes);
            });
        return definition;
    }

    public Element dropSubjectFromDiagram(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (targetElement instanceof RequirementUsage
                || targetElement instanceof RequirementDefinition
                || targetElement instanceof UseCaseUsage
                || targetElement instanceof UseCaseDefinition) {
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

    public Element dropObjectiveRequirementFromDiagram(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (targetElement instanceof UseCaseUsage
                || targetElement instanceof UseCaseDefinition) {
            if (ViewCreateService.isEmptyObjectiveRequirement(targetElement)) {
                this.moveElement(droppedElement, droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes);
            }
        }
        return droppedElement;
    }

    public Element dropElementFromDiagramInRequirementAssumeConstraintCompartment(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext,
            IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (droppedElement instanceof ConstraintUsage droppedConstraint && (targetElement instanceof RequirementUsage || targetElement instanceof RequirementDefinition)) {
            this.moveContraintInRequirementConstraintCompartment(droppedConstraint, targetElement, RequirementConstraintKind.ASSUMPTION);
            this.createView(droppedElement, editingContext, diagramContext, targetNode, convertedNodes, NodeContainmentKind.CHILD_NODE);
            diagramContext.getViewDeletionRequests().add(ViewDeletionRequest.newViewDeletionRequest().elementId(droppedNode.getId()).build());
        }
        return droppedElement;
    }

    public Element dropElementFromDiagramInRequirementRequireConstraintCompartment(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext,
            IDiagramContext diagramContext,
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

    public Element dropElementFromDiagramInConstraintCompartment(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext,
            IDiagramContext diagramContext,
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

    public Element reconnnectSourceCompositionEdge(Element self, Element newSource, Element otherEnd) {
        return this.changeOwner(otherEnd, newSource);
    }

    public Element reconnnectTargetCompositionEdge(Element self, Element oldTarget, Element newTarget) {
        var oldContainer = oldTarget.eContainer();
        if (newTarget instanceof Usage && oldContainer instanceof FeatureMembership featureMembership) {
            // move the old target to the innermost package
            var pack = this.getClosestContainingPackageFrom(self);
            if (pack != null) {
                var owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                pack.getOwnedRelationship().add(owningMembership);
                owningMembership.getOwnedRelatedElement().add(oldTarget);
                // reuse feature membership of the previous nested
                var oldMembership = newTarget.eContainer();
                featureMembership.getOwnedRelatedElement().add(newTarget);
                EcoreUtil.delete(oldMembership);
            }
        }
        return self;
    }

    private Package getClosestContainingPackageFrom(Element element) {
        var owner = element.eContainer();
        while (!(owner instanceof Package) && owner != null) {
            owner = owner.eContainer();
        }
        return (Package) owner;
    }

    /**
     * Called by "New Action" tool from free-form for both nested and owned ActionUsage .
     *
     * @param actionUsage
     *            the {@link ActionUsage} corresponding to the target object on which the tool has been called.
     * @param nodeName
     *            the name of the node description corresponding to the ActionUsage in the context of the tool.
     * @param compartmentName
     *            the name of the free-form compartment
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
    public ActionUsage createSubActionUsageAndView(Element actionUsage, String nodeName, String compartmentName, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        ObjectiveMembership membership = SysmlFactory.eINSTANCE.createObjectiveMembership();
        actionUsage.getOwnedRelationship().add(membership);
        ActionUsage childAction = SysmlFactory.eINSTANCE.createActionUsage();
        membership.getOwnedRelatedElement().add(childAction);
        this.elementInitializerSwitch.doSwitch(childAction);
        // get the children action usage compartment
        Optional<org.eclipse.sirius.components.view.diagram.NodeDescription> nodeChildActionUsage = convertedNodes.keySet().stream()
                .filter(n -> nodeName.equals(n.getName()))
                .findFirst();
        if (nodeChildActionUsage.isPresent()) {
            NodeDescription nodeDescription = convertedNodes.get(nodeChildActionUsage.get());
            if (nodeDescription.getId().equals(selectedNode.getDescriptionId()) && compartmentName != null) {
                selectedNode.getChildNodes().stream()
                        .filter(child -> compartmentName.equals(child.getInsideLabel().getText()))
                        .findFirst()
                        .ifPresent(compartmentNode -> {
                            this.createView(childAction, editingContext, diagramContext, compartmentNode, convertedNodes);
                        });
            } else {
                this.createView(childAction, editingContext, diagramContext, selectedNode, convertedNodes);
            }
        }
        return childAction;
    }
}
