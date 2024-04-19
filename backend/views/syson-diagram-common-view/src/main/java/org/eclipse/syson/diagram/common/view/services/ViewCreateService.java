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

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewCreationRequest;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewDiagramDescriptionSearchService;
import org.eclipse.syson.services.ElementInitializerSwitch;
import org.eclipse.syson.sysml.AllocationDefinition;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;

/**
 * Creation-related Java shared services used by several diagrams.
 *
 * @author arichard
 */
public class ViewCreateService {

    private final IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService;

    private final IObjectService objectService;

    private final ElementInitializerSwitch elementInitializerSwitch;

    public ViewCreateService(IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService, IObjectService objectService) {
        this.viewDiagramDescriptionSearchService = Objects.requireNonNull(viewDiagramDescriptionSearchService);
        this.objectService = Objects.requireNonNull(objectService);
        this.elementInitializerSwitch = new ElementInitializerSwitch();
    }

    /**
     * Call the {@link ElementInitializerSwitch} on the given {@link Element}. Allows to set various
     * attributes/references.
     *
     * @param element
     *            the given {@link Element}.
     * @return the given {@link Element}.
     */
    public Element elementInitializer(Element element) {
        return this.elementInitializerSwitch.doSwitch(element);
    }

    /**
     * Create the appropriate {@link Membership} according to the given {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @return the newly created {@link Membership}.
     */
    public Membership createMembership(Element element) {
        Membership membership = null;
        if (element instanceof Package) {
            membership = SysmlFactory.eINSTANCE.createObjectiveMembership();
        } else {
            membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        }
        element.getOwnedRelationship().add(membership);
        return membership;
    }

    /**
     * Create a {@link PartUsage} under the given {@link PartUsage}.
     *
     * @param partUsage
     *            the {@link PartUsage} on which we want to create a nested {@link PartUsage}.
     * @return the created element.
     */
    public PartUsage createNestedPartUsage(PartUsage partUsage) {
        PartUsage newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
        newPartUsage.setDeclaredName("part");
        FeatureMembership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        membership.getOwnedRelatedElement().add(newPartUsage);
        partUsage.getOwnedRelationship().add(membership);
        return newPartUsage;
    }

    /**
     * Check if the diagram associated to the given {@link IDiagramContext} contains nodes.
     *
     * @param element
     *            the element on which this service has been called.
     * @param editingContext
     *            the {@link IEditingContext} retrieved from the Variable Manager.
     * @param diagramContext
     *            the {@link IDiagramContext} retrieved from the Variable Manager.
     * @param previousDiagram
     *            the previous {@link Diagram} retrieved from the Variable Manager.
     * @param emptyNodeName
     *            the name of the special empty node description for the given diagram.
     * @return the given {@link Element} if the diagram is empty, <code>null</code> otherwise.
     */
    protected Element getDiagramEmptyCandidate(Element element, IEditingContext editingContext, IDiagramContext diagramContext, Diagram previousDiagram, String emptyNodeName) {
        boolean emptyDiagram = false;
        if (previousDiagram != null && diagramContext != null) {
            List<Node> previousNodes = previousDiagram.getNodes();
            List<ViewCreationRequest> viewCreationRequests = diagramContext.getViewCreationRequests();
            if (viewCreationRequests.isEmpty() && (previousNodes.isEmpty() || previousNodes.stream().anyMatch(node -> this.viewDiagramDescriptionSearchService
                    .findViewNodeDescriptionById(editingContext, node.getDescriptionId()).stream().anyMatch(nd -> emptyNodeName.equals(nd.getName()))))) {
                emptyDiagram = true;
            }
        } else {
            emptyDiagram = true;
        }
        if (emptyDiagram) {
            return element;
        }
        return null;
    }

    public Element createCompartmentItem(Element element, String eReferenceName) {
        EStructuralFeature feature = element.eClass().getEStructuralFeature(eReferenceName);

        if (feature.getEType() instanceof EClass itemEClass) {
            var item = SysmlFactory.eINSTANCE.create(itemEClass);
            var membership = this.getOwningMembership(feature);
            membership.getOwnedRelatedElement().add(this.elementInitializer((Element) item));
            element.getOwnedRelationship().add(membership);
        }
        return element;
    }

    private OwningMembership getOwningMembership(EStructuralFeature feature) {
        OwningMembership result = SysmlFactory.eINSTANCE.createFeatureMembership();
        if (feature.getEType().equals(SysmlPackage.eINSTANCE.getEnumerationUsage())) {
            result = SysmlFactory.eINSTANCE.createVariantMembership();
        } else if (feature.equals(SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint())
                || feature.equals(SysmlPackage.eINSTANCE.getRequirementDefinition_AssumedConstraint())) {
            result = SysmlFactory.eINSTANCE.createRequirementConstraintMembership();
            ((RequirementConstraintMembership) result).setKind(RequirementConstraintKind.ASSUMPTION);
        } else if (feature.equals(SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint())
                || feature.equals(SysmlPackage.eINSTANCE.getRequirementDefinition_RequiredConstraint())) {
            result = SysmlFactory.eINSTANCE.createRequirementConstraintMembership();
            ((RequirementConstraintMembership) result).setKind(RequirementConstraintKind.REQUIREMENT);
        }
        return result;
    }

    /**
     * Create a new PartUsage and set it as the subject of the self element.
     *
     * @param self
     *            the element usage to set the subject for
     * @param subjectParent
     *            the parent of the new part usage used as the subject.
     * @return
     */
    public Element createPartUsageAsSubject(Element self, Element subjectParent) {
        if (self instanceof RequirementUsage
                || self instanceof RequirementDefinition
                || self instanceof UseCaseUsage
                || self instanceof UseCaseDefinition) {
            // create the part usage that is used as the subject element
            PartUsage newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            newPartUsage.setDeclaredName(self.getDeclaredName() + "'s subject");
            var membership = SysmlFactory.eINSTANCE.createOwningMembership();
            membership.getOwnedRelatedElement().add(newPartUsage);
            subjectParent.getOwnedRelationship().add(membership);
            // create subject model tree
            var featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
            featureTyping.setType(newPartUsage);
            var referenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
            referenceUsage.setDeclaredName("subject");
            referenceUsage.getOwnedRelationship().add(featureTyping);
            var subjectMembership = SysmlFactory.eINSTANCE.createSubjectMembership();
            subjectMembership.getOwnedRelatedElement().add(referenceUsage);
            self.getOwnedRelationship().add(subjectMembership);
        }
        return self;
    }

    /**
     * Create a new RequirementUsage and set it as the objective requirement of the self element.
     *
     * @param self
     *            the element usage to set the objective for
     * @return
     */
    public Element createRequirementUsageAsObjectiveRequirement(Element self) {
        if (self instanceof UseCaseUsage
                || self instanceof UseCaseDefinition) {
            RequirementUsage newRequirementUsage = SysmlFactory.eINSTANCE.createRequirementUsage();
            newRequirementUsage.setDeclaredName(self.getDeclaredName() + "'s objective");
            var objectiveMembership = SysmlFactory.eINSTANCE.createObjectiveMembership();
            objectiveMembership.getOwnedRelatedElement().add(newRequirementUsage);
            self.getOwnedRelationship().add(objectiveMembership);
        }
        return self;
    }

    /**
     * Service to check whether the given element has a subject defined or not.
     *
     * @param self
     *            a {@link RequirementUsage} or a {@link RequirementDefinition} or a {@link UseCaseUsage} or a
     *            {@link UseCaseDefinition}
     * @return {@code true} if {@code self} contains a subject and {@code false} otherwise.
     */
    public boolean isEmptySubjectCompartment(Element self) {
        if (self instanceof RequirementUsage
                || self instanceof RequirementDefinition
                || self instanceof UseCaseUsage
                || self instanceof UseCaseDefinition) {
            return self.getOwnedRelationship().stream()
                    .filter(SubjectMembership.class::isInstance)
                    .map(SubjectMembership.class::cast)
                    .findFirst().isEmpty();
        }
        // irrelevant case, this service should only be used upon a RequirementUsage/RequirementDefinition/UseCaseUsage
        // or UseCaseDefinition
        return true;
    }

    /**
     * Service to check whether the given UseCaseUsage or UseCaseDefinition has an objective requirement defined or not.
     *
     * @param self
     *            a {@link UseCaseUsage} or a {@link UseCaseDefinition}
     * @return {@code true} if {@code self} contains a subject and {@code false} otherwise.
     */
    public boolean isEmptyObjectiveRequirementCompartment(Element self) {
        return isEmptyObjectiveRequirement(self);
    }

    /**
     * Check whether the given element(that should be a {@link UseCaseDefinition} or a {@link UseCaseUsage}) contains an
     * objective requirement or not.
     * 
     * @param self
     *            a {@link UseCaseDefinition} or a {@link UseCaseUsage} in which the objective is looked for
     * @return {@code true} if the given use case contains an objective and {@code false} otherwise.
     */
    public static boolean isEmptyObjectiveRequirement(Element self) {
        return self.getOwnedRelationship().stream()
                .filter(ObjectiveMembership.class::isInstance)
                .map(ObjectiveMembership.class::cast)
                .findFirst().isEmpty();
    }

    /**
     * Create a new Part Usage which is used as the end given Allocation Definition.
     *
     * @param self
     *            the Allocation Definition in which the new end is added.
     * @param endParent
     *            the owner of the new part usage used as the end.
     * @return
     */
    public Element createPartUsageAsAllocationDefinitionEnd(AllocationDefinition self, Element endParent) {
        // create the part usage that is used as the end element
        PartUsage newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
        newPartUsage.setDeclaredName(self.getDeclaredName() + "'s end");
        var membership = SysmlFactory.eINSTANCE.createOwningMembership();
        membership.getOwnedRelatedElement().add(newPartUsage);
        endParent.getOwnedRelationship().add(membership);
        var featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
        featureTyping.setType(newPartUsage);
        var referenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
        referenceUsage.setDeclaredName("end");
        referenceUsage.getOwnedRelationship().add(featureTyping);
        var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        featureMembership.getOwnedRelatedElement().add(referenceUsage);
        self.getOwnedRelationship().add(featureMembership);
        return self;
    }

    public Element createAllocateEdge(Element source, Element target, Node sourceNode, IEditingContext editingContext, IDiagramService diagramService) {
        var owner = this.getSourceOwner(sourceNode, editingContext, diagramService);
        var ownerMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        owner.getOwnedRelationship().add(ownerMembership);
        var allocation = SysmlFactory.eINSTANCE.createAllocationUsage();
        allocation.setDeclaredName("allocate");
        ownerMembership.getOwnedRelatedElement().add(allocation);
        // create both ends ReferenceSubsetting
        this.addEndToAllocateEdge(allocation, source);
        this.addEndToAllocateEdge(allocation, target);
        return source;
    }

    private void addEndToAllocateEdge(AllocationUsage edge, Element end) {
        if (end instanceof Usage usage) {
            var featureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
            edge.getOwnedRelationship().add(featureMembership);
            var feature = SysmlFactory.eINSTANCE.createFeature();
            featureMembership.getOwnedRelatedElement().add(feature);
            var reference = SysmlFactory.eINSTANCE.createReferenceSubsetting();
            feature.getOwnedRelationship().add(reference);
            reference.setReferencedFeature(usage);
        }
    }

    /**
     * Retrieve the parent node semantic element of the given node
     * 
     * @param sourceNode
     *            a {@link Node}
     * @param editingContext
     * @return the semantic element of the parent graphical node of the given one or <code>null</code> if unable to find
     *         it.
     */
    private Element getSourceOwner(Node sourceNode, IEditingContext editingContext, IDiagramService diagramService) {
        Diagram diagram = diagramService.getDiagramContext().getDiagram();
        String id;
        var parentNode = new ParentNodeFinder(diagram).getParent(sourceNode);
        if (parentNode instanceof Node node) {
            id = node.getTargetObjectId();
        } else {
            // parent is diagram
            id = diagram.getTargetObjectId();
        }
        return this.objectService.getObject(editingContext, id)
                .filter(Element.class::isInstance)
                .map(Element.class::cast)
                .orElse(null);
    }
}
