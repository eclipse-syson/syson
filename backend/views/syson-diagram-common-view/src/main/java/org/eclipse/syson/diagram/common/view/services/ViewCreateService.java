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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewCreationRequest;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewDiagramDescriptionSearchService;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.services.ElementInitializerSwitch;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AllocationDefinition;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
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

    private final DeleteService deleteService;

    private final UtilService utilService;

    public ViewCreateService(IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService, IObjectService objectService) {
        this.viewDiagramDescriptionSearchService = Objects.requireNonNull(viewDiagramDescriptionSearchService);
        this.objectService = Objects.requireNonNull(objectService);
        this.elementInitializerSwitch = new ElementInitializerSwitch();
        this.deleteService = new DeleteService();
        this.utilService = new UtilService();
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
     * Create the appropriate {@link Membership} child according to the given {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @return the newly created {@link Membership}.
     */
    public Membership createMembership(Element element) {
        Membership membership = null;
        if (element instanceof Package) {
            membership = SysmlFactory.eINSTANCE.createOwningMembership();
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
        Element result = element;
        if (feature.getEType() instanceof EClass itemEClass) {
            var item = SysmlFactory.eINSTANCE.create(itemEClass);
            result = (Element) item;
            var membership = this.getOwningMembership(feature);
            membership.getOwnedRelatedElement().add(this.elementInitializer((Element) item));
            element.getOwnedRelationship().add(membership);
        }
        return result;
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
     * @return the ReferenceUsage created in {@code self}
     */
    public Element createPartUsageAsSubject(Element self, Element subjectParent) {
        Element result = self;
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
            result = referenceUsage;
            referenceUsage.setDeclaredName("subject");
            referenceUsage.getOwnedRelationship().add(featureTyping);
            var subjectMembership = SysmlFactory.eINSTANCE.createSubjectMembership();
            subjectMembership.getOwnedRelatedElement().add(referenceUsage);
            self.getOwnedRelationship().add(subjectMembership);
        }
        return result;
    }

    /**
     * Create a new RequirementUsage and set it as the objective requirement of the self element.
     *
     * @param self
     *            the element usage to set the objective for
     * @return the created RequirementUsage
     */
    public Element createRequirementUsageAsObjectiveRequirement(Element self) {
        Element result = self;
        if (self instanceof UseCaseUsage
                || self instanceof UseCaseDefinition) {
            RequirementUsage newRequirementUsage = SysmlFactory.eINSTANCE.createRequirementUsage();
            result = newRequirementUsage;
            newRequirementUsage.setDeclaredName(self.getDeclaredName() + "'s objective");
            var objectiveMembership = SysmlFactory.eINSTANCE.createObjectiveMembership();
            objectiveMembership.getOwnedRelatedElement().add(newRequirementUsage);
            self.getOwnedRelationship().add(objectiveMembership);
        }
        return result;
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
     * Service to check whether the given element has a subaction of Kind {@code kind} subject defined or not.
     *
     * @param self
     *            a {@link StateUsage} or a {@link StateDefinition}
     * @return {@code true} if {@code self} contains a subaction of the specified kind and {@code false} otherwise.
     */
    public boolean isEmptyOfActionKindCompartment(Element self, String kind) {
        if (self instanceof StateUsage
                || self instanceof StateDefinition) {
            return !self.getOwnedRelationship().stream()
                    .filter(StateSubactionMembership.class::isInstance)
                    .map(StateSubactionMembership.class::cast)
                    .anyMatch(mem -> mem.getKind().getLiteral().equalsIgnoreCase(kind));
        }
        // irrelevant case, this service should only be used upon a StateUsage/StateDefinition
        return true;
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
        referenceUsage.setIsEnd(true);
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
        var parentNode = new NodeFinder(diagram).getParent(sourceNode);
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

    public List<Feature> getInheritedCompartmentItems(Type type, String eReferenceName) {
        List<Feature> inheritedElements = new ArrayList<>();
        EStructuralFeature eStructuralFeature = type.eClass().getEStructuralFeature(eReferenceName);
        if (eStructuralFeature instanceof EReference eReference) {
            type.getInheritedFeature().stream()
                    .filter(feature -> new InheritedCompartmentItemFilterSwitch(eReference).doSwitch(feature))
                    .forEach(inheritedElements::add);
        }
        // Removes all features that have been redefined, subsetted, subclassed...
        List<Element> alreadySpecializedFeatures = new ArrayList<>();
        type.getOwnedFeature().stream()
                .flatMap(f -> this.getSpecializationTypeHierarchy(f).stream())
                .filter(general -> inheritedElements.contains(general))
                .forEach(alreadySpecializedFeatures::add);
        inheritedElements.stream()
                .flatMap(f -> this.getSpecializationTypeHierarchy(f).stream())
                .filter(general -> inheritedElements.contains(general))
                .forEach(alreadySpecializedFeatures::add);
        inheritedElements.removeAll(alreadySpecializedFeatures);
        inheritedElements.remove(type);
        return inheritedElements;
    }

    private List<Type> getSpecializationTypeHierarchy(Type type) {
        List<Type> specializationTypeHierarchy = new ArrayList<>();
        this.getSpecializationTypeHierarchy(type, specializationTypeHierarchy);
        return specializationTypeHierarchy;
    }

    private void getSpecializationTypeHierarchy(Type type, List<Type> specializationTypeHierarchy) {
        if (type != null) {
            List<Type> specializationTypes = new ArrayList<>();
            type.getOwnedSpecialization().stream()
                    .map(Specialization::getGeneral)
                    .forEach(general -> {
                        if (general != null && !specializationTypeHierarchy.contains(general) && !specializationTypes.contains(general)) {
                            specializationTypes.add(general);
                            specializationTypeHierarchy.add(general);
                        }
                    });
            specializationTypes.forEach(speType -> {
                this.getSpecializationTypeHierarchy(speType, specializationTypeHierarchy);
            });
        }
    }

    private Package getClosestContainingPackageFrom(Element element) {
        var owner = element.eContainer();
        while (!(owner instanceof Package) && owner != null) {
            owner = owner.eContainer();
        }
        return (Package) owner;
    }

    public Element createAcceptAction(Element ownerElement) {
        Feature acceptSubActionsStdAction = this.utilService.findByName(ownerElement, "Actions::Action::acceptSubactions");
        if (ownerElement instanceof ActionUsage || ownerElement instanceof ActionDefinition && acceptSubActionsStdAction != null) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            var acceptAction = SysmlFactory.eINSTANCE.createAcceptActionUsage();
            this.elementInitializerSwitch.doSwitch(acceptAction);
            var subsetting = SysmlFactory.eINSTANCE.createSubsetting();
            subsetting.setSubsettingFeature(acceptAction);
            subsetting.setSubsettedFeature(acceptSubActionsStdAction);
            subsetting.setIsImplied(true);
            acceptAction.getOwnedRelationship().add(subsetting);
            featureMember.getOwnedRelatedElement().add(acceptAction);
            ownerElement.getOwnedRelationship().add(featureMember);
            return acceptAction;
        }
        return ownerElement;
    }

    public Element createAcceptActionPayload(AcceptActionUsage self, String payloadEClassName) {
        var classifier = SysmlPackage.eINSTANCE.getEClassifier(payloadEClassName);
        if (classifier instanceof EClass eClass) {
            var payload = SysmlFactory.eINSTANCE.create(eClass);
            if (payload instanceof Type payloadType) {
                // create the payload definition which is the type of the payload
                payloadType.setDeclaredName(self.getDeclaredName() + "PayloadType");
                var membership = SysmlFactory.eINSTANCE.createOwningMembership();
                membership.getOwnedRelatedElement().add(payloadType);
                var payloadParent = this.getClosestContainingPackageFrom(self);
                payloadParent.getOwnedRelationship().add(membership);
                // reference this payload in the accept action
                var featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
                featureTyping.setType(payloadType);
                var referenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
                referenceUsage.setDeclaredName("payload");
                referenceUsage.setDirection(FeatureDirectionKind.INOUT);
                referenceUsage.getOwnedRelationship().add(featureTyping);
                var parameterMembership = this.getPayloadParameterMembership(self);
                var oldParameterContent = parameterMembership.getOwnedMemberParameter();
                if (oldParameterContent != null) {
                    // there is already a playload parameter, we need to delete it.
                    this.deleteService.deleteFromModel(oldParameterContent);
                }
                parameterMembership.getOwnedRelatedElement().add(referenceUsage);
                self.getOwnedRelationship().add(parameterMembership);
            }
        }
        return self;
    }

    private ParameterMembership getPayloadParameterMembership(AcceptActionUsage acceptActionUsage) {
        var membership = acceptActionUsage.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast)
                .findFirst()
                .orElse(null);
        if (membership == null) {
            membership = SysmlFactory.eINSTANCE.createParameterMembership();
        } else {
            membership.getOwnedRelatedElement().clear();
        }
        return membership;
    }

    private ParameterMembership getReceiverParameterMembership(AcceptActionUsage acceptActionUsage) {
        final ParameterMembership result;
        var memberships = acceptActionUsage.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast)
                .toList();
        if (memberships.size() == 0) {
            // add an empty payload
            acceptActionUsage.getOwnedRelationship().add(this.createParameterMembershipWithReferenceUsage());
            result = SysmlFactory.eINSTANCE.createParameterMembership();
        } else if (memberships.size() == 1) {
            result = SysmlFactory.eINSTANCE.createParameterMembership();
        } else {
            result = memberships.get(1);
        }
        return result;
    }

    private ParameterMembership createParameterMembershipWithReferenceUsage() {
        var reference = SysmlFactory.eINSTANCE.createReferenceUsage();
        var pm = SysmlFactory.eINSTANCE.createParameterMembership();
        pm.getOwnedRelatedElement().add(reference);
        return pm;
    }

    public Element createAcceptActionReceiver(AcceptActionUsage self) {
        // create the port usage
        var newPort = SysmlFactory.eINSTANCE.createPortUsage();
        newPort.setDeclaredName(self.getDeclaredName() + "'s receiver");
        var owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        owningMembership.getOwnedRelatedElement().add(newPort);
        var receiverParent = this.getClosestContainingPackageFrom(self);
        receiverParent.getOwnedRelationship().add(owningMembership);
        // reference this port usage as the receiver of the accept action
        var feature = SysmlFactory.eINSTANCE.createFeature();
        feature.setDirection(FeatureDirectionKind.OUT);
        var returnParameterMembership = SysmlFactory.eINSTANCE.createReturnParameterMembership();
        returnParameterMembership.getOwnedRelatedElement().add(feature);
        var membership = SysmlFactory.eINSTANCE.createMembership();
        membership.setMemberElement(newPort);
        var featureReferenceExpression = SysmlFactory.eINSTANCE.createFeatureReferenceExpression();
        featureReferenceExpression.getOwnedRelationship().add(membership);
        featureReferenceExpression.getOwnedRelationship().add(returnParameterMembership);
        var featureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        featureValue.getOwnedRelatedElement().add(featureReferenceExpression);
        var referenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
        referenceUsage.setDeclaredName("receiver");
        referenceUsage.setDirection(FeatureDirectionKind.IN);
        referenceUsage.getOwnedRelationship().add(featureValue);
        var parameterMembership = this.getReceiverParameterMembership(self);
        Feature oldParameterContent = parameterMembership.getOwnedMemberParameter();
        if (oldParameterContent != null) {
            // there is already an element, we need to delete this element
            this.deleteService.deleteFromModel(oldParameterContent);
        }
        parameterMembership.getOwnedRelatedElement().add(referenceUsage);
        self.getOwnedRelationship().add(parameterMembership);
        return self;
    }

    public boolean isEmptyAcceptActionUsageReceiver(Element element) {
        if (element instanceof AcceptActionUsage aau) {
            var receiverExp = aau.getReceiverArgument();
            if (receiverExp != null) {
                var receiverMembership = receiverExp.getOwnedRelationship().stream()
                        .filter(Membership.class::isInstance)
                        .map(Membership.class::cast)
                        .findFirst()
                        .orElse(null);
                if (receiverMembership != null) {
                    return receiverMembership.getMemberElement() == null;
                }
            }
        }
        return true;
    }

    public boolean isEmptyAcceptActionUsagePayload(Element element) {
        boolean result = true;
        if (element instanceof AcceptActionUsage aau) {
            var payloadParameter = aau.getPayloadParameter();
            if (payloadParameter != null && payloadParameter.getOwnedRelationship().size() > 0) {
                var type = payloadParameter.getOwnedRelationship().stream()
                        .filter(FeatureTyping.class::isInstance)
                        .map(FeatureTyping.class::cast)
                        .map(FeatureTyping::getType)
                        .filter(t -> t != null)
                        .findFirst()
                        .orElse(null);
                return type == null;
            }
        }
        return result;
    }

    public Element createSuccessionEdge(Element successionSource, Element successionTarget) {
        EObject successionOwner = successionSource.getOwner();
        if (this.utilService.isStandardStartAction(successionSource)) {
            // When the source of the succession is the standard start action,
            // successionSource is a Membership instead of an ActionUsage
            // In this case, its owner cannot be obtained by getOwner() method.
            successionOwner = successionSource.eContainer();
        }
        return this.createSuccessionEdge(successionSource, successionTarget, successionOwner);
    }

    private Element createSuccessionEdge(Element successionSource, Element successionTarget, EObject successionOwner) {
        if (successionOwner instanceof Element ownerElement) {
            var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
            var succession = SysmlFactory.eINSTANCE.createSuccessionAsUsage();
            this.elementInitializerSwitch.doSwitch(succession);
            var sourceEnd = this.createEndFeatureMembershipFor(successionSource);
            var targetEnd = this.createEndFeatureMembershipFor(successionTarget);
            succession.getOwnedRelationship().add(sourceEnd);
            succession.getOwnedRelationship().add(targetEnd);
            // we are also using source and target features to store edge ends
            // to be able to retrieve Membership element holding standard actions.
            succession.getSource().add(successionSource);
            succession.getTarget().add(successionTarget);
            featureMembership.getOwnedRelatedElement().add(succession);
            ownerElement.getOwnedRelationship().add(featureMembership);
        }
        return successionSource;
    }

    private EndFeatureMembership createEndFeatureMembershipFor(Element sourceOrTarget) {
        var endFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        var referenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
        referenceUsage.setIsEnd(true);
        var referenceSubSetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        if (sourceOrTarget instanceof Membership membership) {
            // this is a standard start or end action, the actual action is inside the memberElement feature.
            if (membership.getMemberElement() instanceof ActionUsage au) {
                referenceSubSetting.setReferencedFeature(au);
            }
        } else if (sourceOrTarget instanceof ActionUsage au) {
            referenceSubSetting.setReferencedFeature(au);
        }
        referenceUsage.getOwnedRelationship().add(referenceSubSetting);
        endFeatureMembership.getOwnedRelatedElement().add(referenceUsage);
        return endFeatureMembership;
    }

    /**
     * Add the standard start action as the child of the given element.
     *
     * @param ownerElement
     *            an element that will own the standard start action.
     * @return the {@link Membership} element containing the start action in its memberElement feature.
     */
    public Membership addStartAction(Element ownerElement) {
        var standardStartAction = this.utilService.retrieveStandardStartAction(ownerElement);
        if (standardStartAction != null) {
            var membership = SysmlFactory.eINSTANCE.createMembership();
            membership.setMemberElement(standardStartAction);
            ownerElement.getOwnedRelationship().add(membership);
            return membership;
        }
        return null;
    }

    /**
     * Add the standard done action as the child of the given element.
     *
     * @param ownerElement
     *            an element that will own the standard done action.
     * @return the {@link Membership} element containing the done action in its memberElement feature.
     */
    public Membership addDoneAction(Element ownerElement) {
        var standardDoneAction = this.utilService.retrieveStandardDoneAction(ownerElement);
        if (standardDoneAction != null) {
            var membership = SysmlFactory.eINSTANCE.createMembership();
            membership.setMemberElement(standardDoneAction);
            ownerElement.getOwnedRelationship().add(membership);
            return membership;
        }
        return null;
    }

    /**
     * Create a new action {@link ActionUsage} inside the given element which should be an {@link ActionUsage} or an
     * {@link ActionDefintion}.
     *
     * @param ownerElement
     *            the owner of the new action usage.
     * @return the newly created action usage.
     */
    public ActionUsage createSubActionUsage(Element ownerElement) {
        var newActionUsage = SysmlFactory.eINSTANCE.createActionUsage();
        this.elementInitializerSwitch.doSwitch(newActionUsage);
        var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        featureMembership.getOwnedRelatedElement().add(newActionUsage);
        ownerElement.getOwnedRelationship().add(featureMembership);
        return newActionUsage;
    }

    /**
     * Removal service for Start action inside an action usage or definition.
     *
     * @param selectedNode
     *            the node element that represents the start action in the diagram.
     * @param editingContext
     * @param diagramService
     * @return the element that owned the start action.
     */
    public Element removeStartAction(Node selectedNode, IEditingContext editingContext, IDiagramService diagramService) {
        Element owner = this.getSourceOwner(selectedNode, editingContext, diagramService);
        var membership = owner.getOwnedRelationship().stream()
                .filter(Membership.class::isInstance)
                .map(Membership.class::cast)
                .filter(m -> {
                    return m.getMemberElement() instanceof ActionUsage au && this.utilService.isStandardStartAction(au);
                })
                .findFirst()
                .orElse(null);
        if (membership != null) {
            this.deleteService.deleteFromModel(membership);
        }
        return owner;
    }

    /**
     * Creation of the semantic elements associated to a Join action.
     *
     * @param ownerElement
     *            the element owning the new Join action.
     * @return the new Join action if it has been successfully created or <code>ownerElement</code> otherwise.
     */
    public Element createJoinAction(Element ownerElement) {
        Feature joinsStdAction = this.utilService.findByName(ownerElement, "Actions::Action::joins");
        if (ownerElement instanceof ActionUsage || ownerElement instanceof ActionDefinition && joinsStdAction != null) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            var join = SysmlFactory.eINSTANCE.createJoinNode();
            this.elementInitializerSwitch.doSwitch(join);
            var subsetting = SysmlFactory.eINSTANCE.createSubsetting();
            subsetting.setSubsettingFeature(join);
            subsetting.setSubsettedFeature(joinsStdAction);
            subsetting.setIsImplied(true);
            join.getOwnedRelationship().add(subsetting);
            featureMember.getOwnedRelatedElement().add(join);
            ownerElement.getOwnedRelationship().add(featureMember);
            return join;
        }
        return ownerElement;
    }

    /**
     * Creation of the semantic elements associated to a Fork action.
     *
     * @param ownerElement
     *            the element owning the new Fork action.
     * @return the new Fork action if it has been successfully created or <code>ownerElement</code> otherwise.
     */
    public Element createForkAction(Element ownerElement) {
        Feature forksStdAction = this.utilService.findByName(ownerElement, "Actions::Action::forks");
        if (ownerElement instanceof ActionUsage || ownerElement instanceof ActionDefinition && forksStdAction != null) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            var fork = SysmlFactory.eINSTANCE.createForkNode();
            this.elementInitializerSwitch.doSwitch(fork);
            var subsetting = SysmlFactory.eINSTANCE.createSubsetting();
            subsetting.setSubsettingFeature(fork);
            subsetting.setSubsettedFeature(forksStdAction);
            subsetting.setIsImplied(true);
            fork.getOwnedRelationship().add(subsetting);
            featureMember.getOwnedRelatedElement().add(fork);
            ownerElement.getOwnedRelationship().add(featureMember);
            return fork;
        }
        return ownerElement;
    }

    /**
     * Creation of the semantic elements associated to a Merge action.
     *
     * @param ownerElement
     *            the element owning the new Merge action.
     * @return the new Merge action if it has been successfully created or <code>ownerElement</code> otherwise.
     */
    public Element createMergeAction(Element ownerElement) {
        Feature mergesStdAction = this.utilService.findByName(ownerElement, "Actions::Action::merges");
        if (ownerElement instanceof ActionUsage || ownerElement instanceof ActionDefinition && mergesStdAction != null) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            var merge = SysmlFactory.eINSTANCE.createMergeNode();
            this.elementInitializerSwitch.doSwitch(merge);
            var subsetting = SysmlFactory.eINSTANCE.createSubsetting();
            subsetting.setSubsettingFeature(merge);
            subsetting.setSubsettedFeature(mergesStdAction);
            subsetting.setIsImplied(true);
            merge.getOwnedRelationship().add(subsetting);
            featureMember.getOwnedRelatedElement().add(merge);
            ownerElement.getOwnedRelationship().add(featureMember);
            return merge;
        }
        return ownerElement;
    }

    /**
     * Creation of the semantic elements associated to a Decision action.
     *
     * @param ownerElement
     *            the element owning the new Decision action.
     * @return the new Decision action if it has been successfully created or <code>ownerElement</code> otherwise.
     */
    public Element createDecisionAction(Element ownerElement) {
        Feature decisionsStdAction = this.utilService.findByName(ownerElement, "Actions::Action::decisions");
        if (ownerElement instanceof ActionUsage || ownerElement instanceof ActionDefinition && decisionsStdAction != null) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            var decision = SysmlFactory.eINSTANCE.createDecisionNode();
            this.elementInitializerSwitch.doSwitch(decision);
            var subsetting = SysmlFactory.eINSTANCE.createSubsetting();
            subsetting.setSubsettingFeature(decision);
            subsetting.setSubsettedFeature(decisionsStdAction);
            subsetting.setIsImplied(true);
            decision.getOwnedRelationship().add(subsetting);
            featureMember.getOwnedRelatedElement().add(decision);
            ownerElement.getOwnedRelationship().add(featureMember);
            return decision;
        }
        return ownerElement;
    }
}
