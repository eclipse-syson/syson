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

import org.apache.commons.lang3.StringUtils;
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
import org.eclipse.syson.sysml.CaseDefinition;
import org.eclipse.syson.sysml.CaseUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
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
import org.eclipse.syson.sysml.util.ElementUtil;

/**
 * Creation-related Java shared services used by several diagrams.
 *
 * @author arichard
 */
public class ViewCreateService {

    private final IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService;

    private final IObjectService objectService;

    private final ShowDiagramsInheritedMembersService showDiagramsInheritedMembersService;

    private final ElementInitializerSwitch elementInitializerSwitch;

    private final DeleteService deleteService;

    private final UtilService utilService;

    public ViewCreateService(IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService, IObjectService objectService,
            ShowDiagramsInheritedMembersService showDiagramsInheritedMembersService) {
        this.viewDiagramDescriptionSearchService = Objects.requireNonNull(viewDiagramDescriptionSearchService);
        this.objectService = Objects.requireNonNull(objectService);
        this.showDiagramsInheritedMembersService = Objects.requireNonNull(showDiagramsInheritedMembersService);
        this.elementInitializerSwitch = new ElementInitializerSwitch();
        this.deleteService = new DeleteService();
        this.utilService = new UtilService();
    }

    /**
     * Returns {@code true} if the diagram can be created on the provided {@code element}.
     *
     * @param element
     *         the element to check
     * @return {@code true} if the diagram can be created on the provided {@code element}
     */
    public boolean canCreateDiagram(Element element) {
        return !this.utilService.isRootNamespace(element);
    }

    /**
     * Call the {@link ElementInitializerSwitch} on the given {@link Element}. Allows to set various attributes/references.
     *
     * @param element
     *         the given {@link Element}.
     * @return the given {@link Element}.
     */
    public Element elementInitializer(Element element) {
        return this.elementInitializerSwitch.doSwitch(element);
    }

    /**
     * Create the appropriate {@link Membership} child according to the given {@link Element}.
     *
     * @param element
     *         the given {@link Element}.
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
     * Create the appropriate {@link Membership} child according to the given {@link EClass} type.
     *
     * @param element
     *         the given {@link Element}.
     * @param membershipType
     *         the type of {@link Membership} to create.
     * @return the newly created {@link Membership}.
     */
    public Membership createMembership(Element element, EClass membershipType) {
        Membership membership = null;
        var eObject = SysmlFactory.eINSTANCE.create(membershipType);
        if (eObject instanceof Membership m) {
            membership = m;
            element.getOwnedRelationship().add(membership);
        }
        return membership;
    }

    /**
     * Create a {@link PartUsage} under the given {@link PartUsage}.
     *
     * @param partUsage
     *         the {@link PartUsage} on which we want to create a nested {@link PartUsage}.
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
     *         the element on which this service has been called.
     * @param editingContext
     *         the {@link IEditingContext} retrieved from the Variable Manager.
     * @param diagramContext
     *         the {@link IDiagramContext} retrieved from the Variable Manager.
     * @param previousDiagram
     *         the previous {@link Diagram} retrieved from the Variable Manager.
     * @param emptyNodeName
     *         the name of the special empty node description for the given diagram.
     * @return the given {@link Element} if the diagram is empty, <code>null</code> otherwise.
     */
    protected Element getDiagramEmptyCandidate(Element element, IEditingContext editingContext, IDiagramContext diagramContext, Diagram previousDiagram, String emptyNodeName) {
        boolean emptyDiagram = false;
        if (previousDiagram != null && diagramContext != null) {
            List<Node> previousNodes = previousDiagram.getNodes();
            List<ViewCreationRequest> viewCreationRequests = diagramContext.getViewCreationRequests();
            if (viewCreationRequests.isEmpty() && (previousNodes.isEmpty() || previousNodes.stream()
                    .anyMatch(node -> this.viewDiagramDescriptionSearchService
                            .findViewNodeDescriptionById(editingContext, node.getDescriptionId())
                            .stream()
                            .anyMatch(nd -> emptyNodeName.equals(nd.getName()))))) {
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
            if (item instanceof Element elementItem) {
                result = elementItem;
                var membership = this.createAppropriateMembership(feature);
                membership.getOwnedRelatedElement().add(this.elementInitializer(elementItem));
                element.getOwnedRelationship().add(membership);
            }
        }
        return result;
    }

    public Element createCompartmentItemWithDirection(Element element, String eReferenceName, String directionLiteral) {
        EStructuralFeature structuralFeature = element.eClass().getEStructuralFeature(eReferenceName);
        Element result = element;
        if (structuralFeature.getEType() instanceof EClass itemEClass) {
            var item = SysmlFactory.eINSTANCE.create(itemEClass);
            if (item instanceof Element elementItem) {
                result = this.elementInitializer(elementItem);
                if (directionLiteral != null && item instanceof Feature feature) {
                    feature.setDirection(FeatureDirectionKind.get(directionLiteral));
                    result.setDeclaredName(result.getName() + StringUtils.capitalize(directionLiteral));
                }
                var membership = this.createAppropriateMembership(structuralFeature);
                membership.getOwnedRelatedElement().add(result);
                element.getOwnedRelationship().add(membership);
            }
        }
        return result;
    }

    private Membership createAppropriateMembership(EStructuralFeature feature) {
        Membership result = SysmlFactory.eINSTANCE.createFeatureMembership();
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
        } else if (feature.equals(SysmlPackage.eINSTANCE.getElement_Documentation())) {
            result = SysmlFactory.eINSTANCE.createOwningMembership();
        }
        return result;
    }

    /**
     * Create a new RequirementUsage and set it as the objective requirement of the self element.
     *
     * @param self
     *         the element usage to set the objective for
     * @return the created RequirementUsage
     */
    public Element createRequirementUsageAsObjectiveRequirement(Element self, Element selectedObject) {
        Element result = self;
        if (self instanceof CaseUsage
                || self instanceof CaseDefinition) {
            RequirementUsage newRequirementUsage = SysmlFactory.eINSTANCE.createRequirementUsage();
            result = newRequirementUsage;
            var objectiveMembership = this.createMembership(self, SysmlPackage.eINSTANCE.getObjectiveMembership());
            objectiveMembership.getOwnedRelatedElement().add(newRequirementUsage);
            this.elementInitializerSwitch.doSwitch(newRequirementUsage);
            if (selectedObject instanceof RequirementUsage requirementUsage) {
                this.utilService.setSubsetting(newRequirementUsage, requirementUsage);
            } else if (selectedObject instanceof RequirementDefinition requirementDefinition) {
                this.utilService.setFeatureTyping(newRequirementUsage, requirementDefinition);
            }
        }
        return result;
    }

    public Element createReferenceUsageAsSubject(Element self, Element selectedObject) {
        Element result = self;
        if (self instanceof CaseUsage
                || self instanceof CaseDefinition
                || self instanceof RequirementUsage
                || self instanceof RequirementDefinition) {
            ReferenceUsage newReferenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
            result = newReferenceUsage;
            var subjectMembership = this.createMembership(self, SysmlPackage.eINSTANCE.getSubjectMembership());
            subjectMembership.getOwnedRelatedElement().add(newReferenceUsage);
            this.elementInitializerSwitch.doSwitch(newReferenceUsage);
            if (selectedObject instanceof Usage usage) {
                this.utilService.setSubsetting(newReferenceUsage, usage);
            } else if (selectedObject instanceof Definition definition) {
                this.utilService.setFeatureTyping(newReferenceUsage, definition);
            }
        }
        return result;
    }

    public Element createPartUsageAsActor(Element self, Element selectedObject) {
        Element result = self;
        if (self instanceof CaseUsage
                || self instanceof CaseDefinition
                || self instanceof RequirementUsage
                || self instanceof RequirementDefinition) {
            PartUsage newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            result = newPartUsage;
            var actorMembership = this.createMembership(self, SysmlPackage.eINSTANCE.getActorMembership());
            actorMembership.getOwnedRelatedElement().add(newPartUsage);
            this.elementInitializerSwitch.doSwitch(newPartUsage);
            if (selectedObject instanceof ItemUsage usage) {
                this.utilService.setSubsetting(newPartUsage, usage);
            } else if (selectedObject instanceof ItemDefinition definition) {
                this.utilService.setFeatureTyping(newPartUsage, definition);
            }
        }
        return result;
    }

    /**
     * Service to check whether the given element has a subject defined or not.
     *
     * @param self
     *         a {@link RequirementUsage} or a {@link RequirementDefinition} or a {@link UseCaseUsage} or a {@link UseCaseDefinition}
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
                    .findFirst()
                    .isEmpty();
        }
        // irrelevant case, this service should only be used upon a RequirementUsage/RequirementDefinition/UseCaseUsage
        // or UseCaseDefinition
        return true;
    }

    /**
     * Service to check whether the given UseCaseUsage or UseCaseDefinition has an objective requirement defined or not.
     *
     * @param self
     *         a {@link UseCaseUsage} or a {@link UseCaseDefinition}
     * @return {@code true} if {@code self} contains a subject and {@code false} otherwise.
     */
    public boolean isEmptyObjectiveRequirementCompartment(Element self) {
        return isEmptyObjectiveRequirement(self);
    }

    /**
     * Check whether the given element(that should be a {@link UseCaseDefinition} or a {@link UseCaseUsage}) contains an objective requirement or not.
     *
     * @param self
     *         a {@link UseCaseDefinition} or a {@link UseCaseUsage} in which the objective is looked for
     * @return {@code true} if the given use case contains an objective and {@code false} otherwise.
     */
    public static boolean isEmptyObjectiveRequirement(Element self) {
        return self.getOwnedRelationship().stream()
                .filter(ObjectiveMembership.class::isInstance)
                .map(ObjectiveMembership.class::cast)
                .findFirst()
                .isEmpty();
    }

    /**
     * Service to check whether the given element has a subaction of Kind {@code kind} subject defined or not.
     *
     * @param self
     *         a {@link StateUsage} or a {@link StateDefinition}
     * @return {@code true} if {@code self} contains a subaction of the specified kind and {@code false} otherwise.
     */
    public boolean isEmptyOfActionKindCompartment(Element self, String kind) {
        if (self instanceof StateUsage
                || self instanceof StateDefinition) {
            return self.getOwnedRelationship().stream()
                    .filter(StateSubactionMembership.class::isInstance)
                    .map(StateSubactionMembership.class::cast)
                    .noneMatch(mem -> mem.getKind().getLiteral().equalsIgnoreCase(kind));
        }
        // irrelevant case, this service should only be used upon a StateUsage/StateDefinition
        return true;
    }

    /**
     * Create a new Part Usage which is used as the end given Allocation Definition.
     *
     * @param self
     *         the Allocation Definition in which the new end is added.
     * @param endParent
     *         the owner of the new part usage used as the end.
     * @return the self modified element is returned
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
     *         a {@link Node}
     * @param editingContext
     *         the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable manager.
     * @param diagramService
     *         the {@link IDiagramService} used to retrieve diagram
     * @return the semantic element of the parent graphical node of the given one or <code>null</code> if unable to find it.
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
        boolean showInheritedMembers = this.showDiagramsInheritedMembersService.getShowInheritedMembers();
        boolean showInheritedMembersFromStandardLibraries = this.showDiagramsInheritedMembersService.getShowInheritedMembersFromStandardLibraries();

        if (!showInheritedMembers && !showInheritedMembersFromStandardLibraries) {
            return List.of();
        }

        List<Feature> inheritedElements = new ArrayList<>();
        EStructuralFeature eStructuralFeature = type.eClass().getEStructuralFeature(eReferenceName);
        if (eStructuralFeature instanceof EReference eReference) {
            type.getInheritedFeature().stream()
                    .filter(feature -> new InheritedCompartmentItemFilterSwitch(eReference).doSwitch(feature))
                    .forEach(inheritedElements::add);
        }
        // Removes all features that have been redefined, subsetted, subclassed...
        var alreadySpecializedFeatures = new ArrayList<Feature>();
        type.getOwnedFeature().stream()
                .flatMap(f -> this.getSpecializationTypeHierarchy(f).stream())
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .filter(inheritedElements::contains)
                .forEach(alreadySpecializedFeatures::add);
        inheritedElements.stream()
                .flatMap(f -> this.getSpecializationTypeHierarchy(f).stream())
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .filter(inheritedElements::contains)
                .forEach(alreadySpecializedFeatures::add);
        inheritedElements.removeAll(alreadySpecializedFeatures);
        inheritedElements.remove(type);

        if (showInheritedMembers && !showInheritedMembersFromStandardLibraries) {
            inheritedElements.removeIf(ElementUtil::isFromStandardLibrary);
        } else if (showInheritedMembersFromStandardLibraries && !showInheritedMembers) {
            inheritedElements.removeIf(elt -> !ElementUtil.isFromStandardLibrary(elt));
        }
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
        if (this.isPart(ownerElement) || this.isAction(ownerElement)) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            var acceptAction = SysmlFactory.eINSTANCE.createAcceptActionUsage();
            this.elementInitializerSwitch.doSwitch(acceptAction);
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
        if (memberships.isEmpty()) {
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
            if (payloadParameter != null && !payloadParameter.getOwnedRelationship().isEmpty()) {
                var type = payloadParameter.getOwnedRelationship().stream()
                        .filter(FeatureTyping.class::isInstance)
                        .map(FeatureTyping.class::cast)
                        .map(FeatureTyping::getType)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null);
                return type == null;
            }
        }
        return result;
    }

    public Element createSuccessionEdge(Element successionSource, Element successionTarget, Node sourceNode, IEditingContext editingContext, IDiagramService diagramService) {
        EObject successionOwner = this.getSourceOwner(sourceNode, editingContext, diagramService);
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
     *         an element that will own the standard start action.
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
     *         an element that will own the standard done action.
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
     * Create a new action {@link ActionUsage} inside the given element which should be an {@link ActionUsage} or an {@link ActionDefinition}.
     *
     * @param ownerElement
     *         the owner of the new action usage.
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
     *         the node element that represents the start action in the diagram.
     * @param editingContext
     *         the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable manager.
     * @param diagramService
     *         the {@link IDiagramService} used to retrieve diagram
     * @return the element that owned the start action.
     */
    public Element removeStartAction(Node selectedNode, IEditingContext editingContext, IDiagramService diagramService) {
        Element owner = this.getSourceOwner(selectedNode, editingContext, diagramService);
        owner.getOwnedRelationship().stream()
                .filter(Membership.class::isInstance)
                .map(Membership.class::cast)
                .filter(m -> {
                    return m.getMemberElement() instanceof ActionUsage au && this.utilService.isStandardStartAction(au);
                })
                .findFirst()
                .ifPresent(this.deleteService::deleteFromModel);
        return owner;
    }

    /**
     * Creation of the semantic elements associated to a Join action.
     *
     * @param ownerElement
     *         the element owning the new Join action.
     * @return the new Join action if it has been successfully created or <code>ownerElement</code> otherwise.
     */
    public Element createJoinAction(Element ownerElement) {
        if (this.isPart(ownerElement) || this.isAction(ownerElement)) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            var join = SysmlFactory.eINSTANCE.createJoinNode();
            this.elementInitializerSwitch.doSwitch(join);
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
     *         the element owning the new Fork action.
     * @return the new Fork action if it has been successfully created or <code>ownerElement</code> otherwise.
     */
    public Element createForkAction(Element ownerElement) {
        if (this.isPart(ownerElement) || this.isAction(ownerElement)) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            var fork = SysmlFactory.eINSTANCE.createForkNode();
            this.elementInitializerSwitch.doSwitch(fork);
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
     *         the element owning the new Merge action.
     * @return the new Merge action if it has been successfully created or <code>ownerElement</code> otherwise.
     */
    public Element createMergeAction(Element ownerElement) {
        if (this.isPart(ownerElement) || this.isAction(ownerElement)) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            var merge = SysmlFactory.eINSTANCE.createMergeNode();
            this.elementInitializerSwitch.doSwitch(merge);
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
     *         the element owning the new Decision action.
     * @return the new Decision action if it has been successfully created or <code>ownerElement</code> otherwise.
     */
    public Element createDecisionAction(Element ownerElement) {
        if (this.isPart(ownerElement) || this.isAction(ownerElement)) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            var decision = SysmlFactory.eINSTANCE.createDecisionNode();
            this.elementInitializerSwitch.doSwitch(decision);
            featureMember.getOwnedRelatedElement().add(decision);
            ownerElement.getOwnedRelationship().add(featureMember);
            return decision;
        }
        return ownerElement;
    }

    /**
     * Creation of the semantic elements associated to an Assignment action inside an Action or ActionDefinition.
     *
     * @param ownerElement
     *         the element owning the new Assignment action.
     * @return the new Assignment action if it has been successfully created or <code>ownerElement</code> otherwise.
     */
    public Element createAssignmentAction(Element ownerElement) {
        if (ownerElement instanceof ActionUsage || ownerElement instanceof ActionDefinition) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            var assignmentAction = SysmlFactory.eINSTANCE.createAssignmentActionUsage();
            this.elementInitializerSwitch.doSwitch(assignmentAction);
            featureMember.getOwnedRelatedElement().add(assignmentAction);
            ownerElement.getOwnedRelationship().add(featureMember);
            return assignmentAction;
        }
        return ownerElement;
    }

    public Element createPerform(Element ownerElement) {
        // create an action usage as the performed action of this perform action
        var performedAction = SysmlFactory.eINSTANCE.createActionUsage();
        this.elementInitializerSwitch.doSwitch(performedAction);
        performedAction.setDeclaredName("performedAction");
        var performedFeatureMember = this.createMembership(ownerElement);
        performedFeatureMember.getOwnedRelatedElement().add(performedAction);
        ownerElement.getOwnedRelationship().add(performedFeatureMember);
        // create the perform action
        var perform = SysmlFactory.eINSTANCE.createPerformActionUsage();
        this.elementInitializerSwitch.doSwitch(perform);
        // set the reference subsetting relationship to the performed action
        var referenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        referenceSubsetting.setReferencedFeature(performedAction);
        perform.getOwnedRelationship().add(referenceSubsetting);
        var featureMember = this.createMembership(ownerElement);
        featureMember.getOwnedRelatedElement().add(perform);
        ownerElement.getOwnedRelationship().add(featureMember);
        return perform;
    }

    public Element createPerformAction(Element ownerElement) {
        var perform = SysmlFactory.eINSTANCE.createPerformActionUsage();
        this.elementInitializerSwitch.doSwitch(perform);
        // no subsetting relationship for the performed action since it is the same as the perform action
        var featureMember = this.createMembership(ownerElement);
        featureMember.getOwnedRelatedElement().add(perform);
        ownerElement.getOwnedRelationship().add(featureMember);
        return perform;
    }

    private boolean isPart(Element element) {
        return element instanceof PartUsage || element instanceof PartDefinition;
    }

    private boolean isAction(Element element) {
        return element instanceof ActionUsage || element instanceof ActionDefinition;
    }

    /**
     * Creates a new sibling part usage as well as a subsetting edge between those parts.
     *
     * @param self
     *         the {@link PartUsage} to subset by a new part usage
     * @return the new part usage or self if something went wrong.
     */
    public PartUsage createPartUsageAndSubsetting(PartUsage self) {
        var parent = self.getOwner();
        if (parent != null) {
            // create a new part usage
            var newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            this.elementInitializerSwitch.doSwitch(newPartUsage);
            var membership = SysmlFactory.eINSTANCE.createOwningMembership();
            membership.getOwnedRelatedElement().add(newPartUsage);
            parent.getOwnedRelationship().add(membership);
            // create subsetting edge between self and new part usage
            this.utilService.setSubsetting(self, newPartUsage);
            return newPartUsage;
        }
        return self;
    }

    /**
     * Creates a new sibling part definition as well as a feature typing edge between the part usage and the part definition.
     *
     * @param self
     *         the {@link PartUsage} to type by a new part definition
     * @return the new part definition or self if something went wrong.
     */
    public Element createPartDefinitionAndFeatureTyping(PartUsage self) {
        var parent = self.getOwner();
        if (parent != null) {
            // create a new definition associated to the given part usage
            var newPartDefinition = this.utilService.createPartDefinitionFrom(self);
            this.elementInitializerSwitch.doSwitch(newPartDefinition);
            var membership = SysmlFactory.eINSTANCE.createOwningMembership();
            membership.getOwnedRelatedElement().add(newPartDefinition);
            parent.getOwnedRelationship().add(membership);
            // create feature typing edge between self and new part definition
            this.utilService.setFeatureTyping(self, newPartDefinition);
            return newPartDefinition;
        }
        return self;
    }
}
