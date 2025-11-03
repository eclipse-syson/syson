/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IReadOnlyObjectPredicate;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.services.ElementInitializerSwitch;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AllocationDefinition;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.CaseDefinition;
import org.eclipse.syson.sysml.CaseUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FlowUsage;
import org.eclipse.syson.sysml.IncludeUseCaseUsage;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.StakeholderMembership;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.util.NodeFinder;

/**
 * Creation-related Java shared services used by several diagrams.
 *
 * @author arichard
 */
public class ViewCreateService {

    private final IObjectSearchService objectSearchService;

    private final IReadOnlyObjectPredicate readOnlyObjectPredicate;

    private final ShowDiagramsInheritedMembersService showDiagramsInheritedMembersService;

    private final ElementInitializerSwitch elementInitializerSwitch;

    private final DeleteService deleteService;

    private final UtilService utilService;

    private final IFeedbackMessageService feedbackMessageService;

    private final MetamodelMutationElementService metamodelElementMutationService;

    public ViewCreateService(IObjectSearchService objectSearchService, IReadOnlyObjectPredicate readOnlyObjectPredicate,
            ShowDiagramsInheritedMembersService showDiagramsInheritedMembersService, IFeedbackMessageService feedbackMessageService) {
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.readOnlyObjectPredicate = Objects.requireNonNull(readOnlyObjectPredicate);
        this.showDiagramsInheritedMembersService = Objects.requireNonNull(showDiagramsInheritedMembersService);
        this.elementInitializerSwitch = new ElementInitializerSwitch();
        this.deleteService = new DeleteService();
        this.utilService = new UtilService();
        this.metamodelElementMutationService = new MetamodelMutationElementService();
    }

    /**
     * Returns {@code true} if the diagram can be created on the provided {@code element}.
     *
     * @param element
     *         the element to check
     * @return {@code true} if the diagram can be created on the provided {@code element}
     */
    public boolean canCreateDiagram(Element element) {
        return true;
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

    public Element createCompartmentItem(Element element, String eReferenceName) {
        EStructuralFeature feature = element.eClass().getEStructuralFeature(eReferenceName);
        Element result = element;
        if (feature.getEType() instanceof EClass itemEClass) {
            var item = SysmlFactory.eINSTANCE.create(itemEClass);
            if (item instanceof Element elementItem) {
                var membership = this.createAppropriateMembership(feature);
                element.getOwnedRelationship().add(membership);
                membership.getOwnedRelatedElement().add(elementItem);
                result = this.elementInitializer(elementItem);
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
                var membership = this.createAppropriateMembership(structuralFeature);
                element.getOwnedRelationship().add(membership);
                membership.getOwnedRelatedElement().add(elementItem);
                result = this.elementInitializer(elementItem);
                if (directionLiteral != null && elementItem instanceof Feature feature) {
                    feature.setDirection(FeatureDirectionKind.get(directionLiteral));
                    result.setDeclaredName(result.getName() + StringUtils.capitalize(directionLiteral));
                }
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
     * Service to create a new <b>stakeholder</b> {@link PartUsage} for a {@link RequirementUsage} or
     * {@link RequirementDefinition}.
     *
     * @param self
     *         a {@link RequirementUsage} or {@link RequirementDefinition}, otherwise no {@link PartUsage} will be
     *         created.
     * @param selectedObject
     *         a {@link ItemUsage} or {@link ItemDefinition} that will be subsetted by (respectively that will type)
     *         the created {@link PartUsage}.
     * @return the newly-created {@link PartUsage}, contained by {@code self} through a {@link StakeholderMembership}.
     * If {@code self} was neither a {@link RequirementUsage} nor a {@link RequirementDefinition}, {@code self}
     * is returned as-is.
     */
    public Element createPartUsageAsStakeholder(Element self, Element selectedObject) {
        Objects.requireNonNull(self);
        Objects.requireNonNull(selectedObject);

        if ((self instanceof RequirementUsage || self instanceof RequirementDefinition) && (selectedObject instanceof ItemUsage || selectedObject instanceof ItemDefinition)) {
            var createdPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            var stakeholderMembership = this.createMembership(self, SysmlPackage.eINSTANCE.getStakeholderMembership());
            stakeholderMembership.getOwnedRelatedElement().add(createdPartUsage);
            this.elementInitializerSwitch.doSwitch(createdPartUsage);

            if (selectedObject instanceof ItemUsage selectedItemUsage) {
                this.utilService.setSubsetting(createdPartUsage, selectedItemUsage);
            } else if (selectedObject instanceof ItemDefinition selectedItemDefinition) {
                this.utilService.setFeatureTyping(createdPartUsage, selectedItemDefinition);
            }

            return createdPartUsage;
        } else {
            return self;
        }
    }

    public Element createActionParameter(Element self, String direction) {
        var newReferenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
        var featureMembership = this.createMembership(self, SysmlPackage.eINSTANCE.getFeatureMembership());
        featureMembership.getOwnedRelatedElement().add(newReferenceUsage);
        this.elementInitializerSwitch.doSwitch(newReferenceUsage);
        newReferenceUsage.setDirection(FeatureDirectionKind.getByName(direction));
        return newReferenceUsage;
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
        return this.utilService.isEmptyObjectiveRequirement(self);
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

    /**
     * Create a new IncludeUseCaseUsage.
     *
     * @param source
     *            the source usage.
     * @param target
     *            the target usage.
     * @return a new {@link IncludeUseCaseUsage}.
     */
    public IncludeUseCaseUsage createIncludeUseCaseUsage(UseCaseUsage source, UseCaseUsage target) {
        var ownerMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        source.getOwnedRelationship().add(ownerMembership);

        IncludeUseCaseUsage includeUsage = SysmlFactory.eINSTANCE.createIncludeUseCaseUsage();
        ownerMembership.getOwnedRelatedElement().add(includeUsage);

        ReferenceSubsetting refSub = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        includeUsage.getOwnedRelationship().add(refSub);

        refSub.setReferencedFeature(target);
        return includeUsage;
    }

    public Element createAllocateEdge(Element source, Element target, Node sourceNode, IEditingContext editingContext, IDiagramService diagramService) {
        var owner = source.getOwner();
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
     * Retrieve the parent node's semantic element of the given node
     *
     * @param sourceNode
     *            a {@link Node}
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramService
     *            the {@link IDiagramService} used to retrieve diagram
     * @return the semantic element of the parent graphical node of the given Node or <code>null</code> if unable to
     *         find it.
     */
    private Element getSourceOwner(Node sourceNode, IEditingContext editingContext, IDiagramService diagramService) {
        Diagram diagram = diagramService.getDiagramContext().diagram();
        String id;
        var parentNode = new NodeFinder(diagram).getParent(sourceNode);
        if (parentNode instanceof Node node) {
            id = node.getTargetObjectId();
        } else {
            // parent is diagram
            id = diagram.getTargetObjectId();
        }
        return this.objectSearchService.getObject(editingContext, id)
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
            ownerElement.getOwnedRelationship().add(featureMember);
            var acceptAction = SysmlFactory.eINSTANCE.createAcceptActionUsage();
            featureMember.getOwnedRelatedElement().add(acceptAction);
            this.elementInitializerSwitch.doSwitch(acceptAction);
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
                    // its' membership container will be automatically deleted with it
                    this.deleteService.deleteFromModel(oldParameterContent);
                }
                parameterMembership = this.getPayloadParameterMembership(self);
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
            // its' membership container will be automatically deleted with it
            this.deleteService.deleteFromModel(oldParameterContent);
        }
        parameterMembership = this.getReceiverParameterMembership(self);
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


    public Element createSuccessionEdge(Element successionSource, Element successionTarget, Node sourceNode, Node targetNode, IEditingContext editingContext, IDiagramService diagramService) {
        if (!this.isInSameGraphicalContainer(sourceNode, targetNode, diagramService)) {
            // The current implementation only rely on the semantic features "sourceFeature" and "targetFeature" to find source and target
            // In order to avoid duplicated edges in case the source/target is displayed more than once we forbid the display of cross container edge
            this.feedbackMessageService.addFeedbackMessage(new Message("Can't create cross container SuccessionAsUsage", MessageLevel.WARNING));
            return successionSource;
        }
        EObject successionOwner = this.getSourceOwner(sourceNode, editingContext, diagramService);
        return this.createSuccessionEdge(successionSource, successionTarget, successionOwner);
    }

    private Element createSuccessionEdge(Element successionSource, Element successionTarget, EObject successionOwner) {
        if (successionOwner instanceof Element ownerElement) {
            var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
            ownerElement.getOwnedRelationship().add(featureMembership);
            var succession = SysmlFactory.eINSTANCE.createSuccessionAsUsage();
            featureMembership.getOwnedRelatedElement().add(succession);
            this.elementInitializerSwitch.doSwitch(succession);
            var sourceEnd = this.createEndFeatureMembershipFor(successionSource);
            var targetEnd = this.createEndFeatureMembershipFor(successionTarget);
            succession.getOwnedRelationship().add(sourceEnd);
            succession.getOwnedRelationship().add(targetEnd);
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
    public ActionUsage addStartAction(Element ownerElement) {
        return this.utilService.retrieveStandardStartAction(ownerElement);
    }

    /**
     * Add the standard done action as the child of the given element.
     *
     * @param ownerElement
     *         an element that will own the standard done action.
     * @return the {@link Membership} element containing the done action in its memberElement feature.
     */
    public ActionUsage addDoneAction(Element ownerElement) {
        return this.utilService.retrieveStandardDoneAction(ownerElement);
    }

    /**
     * Create a new action {@link ActionUsage} inside the given element which should be an {@link ActionUsage} or an {@link ActionDefinition}.
     *
     * @param ownerElement
     *         the owner of the new action usage.
     * @return the newly created action usage.
     */
    public ActionUsage createSubActionUsage(Element ownerElement) {
        var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        ownerElement.getOwnedRelationship().add(featureMembership);
        var newActionUsage = SysmlFactory.eINSTANCE.createActionUsage();
        featureMembership.getOwnedRelatedElement().add(newActionUsage);
        this.elementInitializerSwitch.doSwitch(newActionUsage);
        return newActionUsage;
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
            featureMember.getOwnedRelatedElement().add(join);
            ownerElement.getOwnedRelationship().add(featureMember);
            this.elementInitializerSwitch.doSwitch(join);
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
            ownerElement.getOwnedRelationship().add(featureMember);
            var fork = SysmlFactory.eINSTANCE.createForkNode();
            featureMember.getOwnedRelatedElement().add(fork);
            this.elementInitializerSwitch.doSwitch(fork);
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
            featureMember.getOwnedRelatedElement().add(merge);
            ownerElement.getOwnedRelationship().add(featureMember);
            this.elementInitializerSwitch.doSwitch(merge);
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
            ownerElement.getOwnedRelationship().add(featureMember);
            var decision = SysmlFactory.eINSTANCE.createDecisionNode();
            featureMember.getOwnedRelatedElement().add(decision);
            this.elementInitializerSwitch.doSwitch(decision);
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
            ownerElement.getOwnedRelationship().add(featureMember);
            var assignmentAction = SysmlFactory.eINSTANCE.createAssignmentActionUsage();
            featureMember.getOwnedRelatedElement().add(assignmentAction);
            this.elementInitializerSwitch.doSwitch(assignmentAction);
            return assignmentAction;
        }
        return ownerElement;
    }

    public Element createPerformAction(Element ownerElement) {
        // no subsetting relationship for the performed action since it is the same as the perform action
        var featureMember = this.metamodelElementMutationService.createMembership(ownerElement);
        ownerElement.getOwnedRelationship().add(featureMember);
        var perform = SysmlFactory.eINSTANCE.createPerformActionUsage();
        featureMember.getOwnedRelatedElement().add(perform);
        this.elementInitializerSwitch.doSwitch(perform);
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
            var membership = SysmlFactory.eINSTANCE.createOwningMembership();
            parent.getOwnedRelationship().add(membership);
            var newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            membership.getOwnedRelatedElement().add(newPartUsage);
            this.elementInitializer(newPartUsage);
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
            var membership = SysmlFactory.eINSTANCE.createOwningMembership();
            parent.getOwnedRelationship().add(membership);
            var newPartDefinition = this.utilService.createPartDefinitionFrom(self);
            membership.getOwnedRelatedElement().add(newPartDefinition);
            this.elementInitializerSwitch.doSwitch(newPartDefinition);
            // create feature typing edge between self and new part definition
            this.utilService.setFeatureTyping(self, newPartDefinition);
            return newPartDefinition;
        }
        return self;
    }

    public Element createNamespaceImport(Element self, Namespace importedNamespace) {
        if (self instanceof Namespace namespace) {
            var namespaceImport = SysmlFactory.eINSTANCE.createNamespaceImport();
            namespace.getOwnedRelationship().add(namespaceImport);
            this.elementInitializer(namespaceImport);
            namespaceImport.setImportedNamespace(importedNamespace);
            return namespaceImport;
        }
        return self;
    }

    public InterfaceUsage createInterfaceUsage(PortUsage sourcePort, PortUsage targetPort) {
        Namespace interfaceContainer = this.getClosestContainingDefinitionOrPackageFrom(sourcePort);
        if (interfaceContainer == null) {
            return null;
        }

        InterfaceUsage interfaceUsage = SysmlFactory.eINSTANCE.createInterfaceUsage();
        this.addChildInParent(interfaceContainer, interfaceUsage);
        this.elementInitializer(interfaceUsage);
        // Edges should have an empty default name. This is not the case when using the initializer, because
        // InterfaceUsage can be a node, which requires a default name.
        interfaceUsage.setDeclaredName("");

        EndFeatureMembership sourceEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        interfaceUsage.getOwnedRelationship().add(sourceEndFeatureMembership);
        Feature sourceFeature = SysmlFactory.eINSTANCE.createFeature();
        sourceFeature.setIsEnd(true);
        sourceEndFeatureMembership.getOwnedRelatedElement().add(sourceFeature);
        this.elementInitializer(sourceFeature);
        ReferenceSubsetting sourceReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        sourceFeature.getOwnedRelationship().add(sourceReferenceSubsetting);
        this.elementInitializer(sourceReferenceSubsetting);
        sourceReferenceSubsetting.setReferencedFeature(sourcePort);

        EndFeatureMembership targetEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        interfaceUsage.getOwnedRelationship().add(targetEndFeatureMembership);
        Feature targetFeature = SysmlFactory.eINSTANCE.createFeature();
        targetFeature.setIsEnd(true);
        targetEndFeatureMembership.getOwnedRelatedElement().add(targetFeature);
        this.elementInitializer(targetFeature);
        ReferenceSubsetting targetReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        targetFeature.getOwnedRelationship().add(targetReferenceSubsetting);
        this.elementInitializer(targetReferenceSubsetting);
        targetReferenceSubsetting.setReferencedFeature(targetPort);

        return interfaceUsage;
    }

    public FlowUsage createFlowUsage(Feature source, Feature target) {
        Namespace flowContainer = this.getClosestContainingDefinitionOrPackageFrom(source);
        if (flowContainer == null) {
            return null;
        }
        FeatureMembership featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        flowContainer.getOwnedRelationship().add(featureMembership);

        FlowUsage flowUsage = SysmlFactory.eINSTANCE.createFlowUsage();
        this.addChildInParent(flowContainer, flowUsage);
        this.elementInitializer(flowUsage);

        flowUsage.getOwnedRelationship().add(this.utilService.createFlowConnectionEnd(source));
        flowUsage.getOwnedRelationship().add(this.utilService.createFlowConnectionEnd(target));

        return flowUsage;
    }

    public BindingConnectorAsUsage createBindingConnectorAsUsage(Feature source, Feature target) {

        Type container = this.utilService.getConnectorContainer(source, target);
        if (container == null) {
            this.feedbackMessageService.addFeedbackMessage(new Message("Unable to find a suitable Type to hold the new binding connector.", MessageLevel.WARNING));
            return null;
        }

        BindingConnectorAsUsage bindingConnectorAsUsage = SysmlFactory.eINSTANCE.createBindingConnectorAsUsage();
        this.addChildInParent(container, bindingConnectorAsUsage);
        this.elementInitializer(bindingConnectorAsUsage);

        this.utilService.setConnectorEnds(bindingConnectorAsUsage, source, target, container);

        return bindingConnectorAsUsage;
    }

    /**
     * Creates a state sub action (entry, do or exit actions) as a child of the given StateUsage or StateDefinition.
     *
     * @param self
     *         the StateUsage or StateDefinition owning the sub action
     * @param performedAction
     *         an ActionUsage or <code>null</code>. If set the new PerformAction should reference this action.
     * @param kindLiteral
     *         the kind of the StateSubactionMembership owning the PerformAction
     * @return the new PerformAction or null if self is not a State
     */
    public PerformActionUsage createStateSubaction(Element self, ActionUsage performedAction, String kindLiteral) {
        if (self instanceof StateUsage || self instanceof StateDefinition) {
            StateSubactionMembership stateSubactionMembership = (StateSubactionMembership) this.createMembership(self,  SysmlPackage.eINSTANCE.getStateSubactionMembership());
            stateSubactionMembership.setKind(StateSubactionKind.get(kindLiteral));
            var performAction = SysmlFactory.eINSTANCE.createPerformActionUsage();
            stateSubactionMembership.getOwnedRelatedElement().add(performAction);
            this.elementInitializerSwitch.doSwitch(performAction);
            if (performedAction != null) {
                // set the reference subsetting relationship to the performed action
                var referenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
                referenceSubsetting.setReferencedFeature(performedAction);
                performAction.getOwnedRelationship().add(referenceSubsetting);
            }
            return performAction;
        }
        return null;
    }

    /**
     * Create a new TransitionUsage and set it as the child of the parent of the source {@link Feature}. Sets its source
     * and target.
     *
     * @param sourceUsage
     *            the {@link Feature} used as a source for the transition
     * @param targetUsage
     *            the {@link Feature} used as a target for the transition
     * @param source
     *            the node of the source
     * @param target
     *            the node of the target
     * @param diagramService
     *            service used to navigate inside the diagram
     * @param editingContext
     *            the current editing context
     * @return the given source {@link Feature}.
     */
    public Feature createTransitionUsage(Feature sourceUsage, Feature targetUsage, Node source, Node target, IDiagramService diagramService, IEditingContext editingContext) {
        if (this.isInSameGraphicalContainer(source, target, diagramService)) {
            // Check source and target have the same parent
            Element semanticContainer = this.getEdgeSemanticContainer(source, target, diagramService.getDiagramContext().diagram(), editingContext);
            if (semanticContainer != null) {
                Element sourceParentElement = sourceUsage.getOwner();
                if (this.utilService.isParallelState(sourceParentElement)) {
                    // Should probably not be here as the transition creation should not be allowed.
                    return sourceUsage;
                }
                // Create transition usage and add it to the parent element
                // sourceParentElement <>-> FeatureMembership -> RelatedElement = TransitionUsage
                TransitionUsage newTransitionUsage = SysmlFactory.eINSTANCE.createTransitionUsage();
                var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
                featureMembership.getOwnedRelatedElement().add(newTransitionUsage);
                sourceParentElement.getOwnedRelationship().add(featureMembership);

                // Create EndFeature
                // TransitionUsage <>-> Membership -> MemberElement = sourceAction
                var sourceMembership = SysmlFactory.eINSTANCE.createMembership();
                newTransitionUsage.getOwnedRelationship().add(sourceMembership);
                sourceMembership.setMemberElement(sourceUsage);

                // Create Succession
                // TransitionUsage <>-> FeatureMembership -> RelatedElement = succession
                Succession succession = SysmlFactory.eINSTANCE.createSuccession();
                this.elementInitializerSwitch.doSwitch(succession);
                var successionFeatureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
                successionFeatureMembership.getOwnedRelatedElement().add(succession);
                newTransitionUsage.getOwnedRelationship().add(successionFeatureMembership);

                // Set Succession Source and Target Features
                succession.getOwnedRelationship().add(this.createConnectorEndFeatureMembership(sourceUsage));
                succession.getOwnedRelationship().add(this.createConnectorEndFeatureMembership(targetUsage));
                this.elementInitializerSwitch.doSwitch(newTransitionUsage);
            } else {
                this.feedbackMessageService.addFeedbackMessage(new Message("Unable to find a suitable semantic owner for the new transition", MessageLevel.WARNING));
            }

        } else {
            this.feedbackMessageService.addFeedbackMessage(new Message("Can't create cross container TransitionUsage", MessageLevel.WARNING));
        }

        return sourceUsage;
    }

    public Element createPartUsageAndBindingConnectorAsUsage(PartUsage self) {
        var parent = self.getOwner();
        if (parent != null) {
            // create a new port on given part usage
            var newSelfPort = SysmlFactory.eINSTANCE.createPortUsage();
            this.addChildInParent(self, newSelfPort);
            this.elementInitializer(newSelfPort);
            // create a new part usage as a self sibling
            var newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            this.addChildInParent(parent, newPartUsage);
            this.elementInitializer(newPartUsage);
            // create a new port on the new part usage
            var newPartUsagePort = SysmlFactory.eINSTANCE.createPortUsage();
            this.addChildInParent(newPartUsage, newPartUsagePort);
            this.elementInitializer(newPartUsagePort);
            // create binding connector as usage edge between both new ports
            this.createBindingConnectorAsUsage(newSelfPort, newPartUsagePort);
            return newPartUsage;
        }
        return self;
    }

    public Element createPartUsageAndFlowConnection(PartUsage self) {
        var parent = self.getOwner();
        if (parent != null) {
            // create a new port on given part usage
            var newSelfPort = SysmlFactory.eINSTANCE.createPortUsage();
            this.addChildInParent(self, newSelfPort);
            this.elementInitializer(newSelfPort);
            // create a new part usage as a self sibling
            var newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            this.addChildInParent(parent, newPartUsage);
            this.elementInitializer(newPartUsage);
            // create a new port on the new part usage
            var newPartUsagePort = SysmlFactory.eINSTANCE.createPortUsage();
            this.addChildInParent(newPartUsage, newPartUsagePort);
            this.elementInitializer(newPartUsagePort);
            // create flow connection edge between both new ports
            this.createFlowUsage(newSelfPort, newPartUsagePort);
            return newPartUsage;
        }
        return self;
    }

    public Element createPartUsageAndInterface(PartUsage self) {
        var parent = self.getOwner();
        if (parent != null) {
            // create a new port on given part usage
            var newSelfPort = SysmlFactory.eINSTANCE.createPortUsage();
            this.addChildInParent(self, newSelfPort);
            this.elementInitializer(newSelfPort);
            // create a new part usage as a self sibling
            var newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            this.addChildInParent(parent, newPartUsage);
            this.elementInitializer(newPartUsage);
            // create a new port on the new part usage
            var newPartUsagePort = SysmlFactory.eINSTANCE.createPortUsage();
            this.addChildInParent(newPartUsage, newPartUsagePort);
            this.elementInitializer(newPartUsagePort);
            // create interface edge between both new ports
            this.createInterfaceUsage(newSelfPort, newPartUsagePort);
            return newPartUsage;
        }
        return self;
    }

    private Element getEdgeSemanticContainer(Node source, Node target, Diagram diagram, IEditingContext editingContext) {
        final Element semanticContainer;
        Element semanticSourceGraphicalParent = this.getGraphicalContainerSemanticElement(source, diagram, editingContext);
        if (semanticSourceGraphicalParent != null && !this.readOnlyObjectPredicate.test(semanticSourceGraphicalParent)) {
            semanticContainer = semanticSourceGraphicalParent;
        } else {
            Element semanticTargetGraphicalParent = this.getGraphicalContainerSemanticElement(target, diagram, editingContext);
            if (semanticTargetGraphicalParent != null && !this.readOnlyObjectPredicate.test(semanticTargetGraphicalParent)) {
                semanticContainer = semanticTargetGraphicalParent;
            } else {
                semanticContainer = null;
            }
        }
        return semanticContainer;
    }

    private Element getGraphicalContainerSemanticElement(Node node, Diagram diagram, IEditingContext editingContext) {
        Object parent = new NodeFinder(diagram).getParent(node);
        final Element semanticParent;
        if (parent instanceof Node parentNode) {
            semanticParent = this.objectSearchService.getObject(editingContext, parentNode.getTargetObjectId())
                    .filter(Element.class::isInstance)
                    .map(Element.class::cast)
                    .orElse(null);
        } else if (parent instanceof Diagram parentDiagram) {
            semanticParent = this.objectSearchService.getObject(editingContext, parentDiagram.getTargetObjectId())
                    .filter(Element.class::isInstance)
                    .map(Element.class::cast)
                    .orElse(null);

        } else {
            semanticParent = null;
        }
        return semanticParent;
    }

    /**
     * <>-> EndFeatureMembership -> RelatedElement = Feature <>-> ReferenceSubsetting -> ReferencedFeature = feature
     *
     * @param feature
     *            The feature to reference subset
     * @return
     */
    private EndFeatureMembership createConnectorEndFeatureMembership(Feature feature) {
        var successionSourceEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        var successionSourceEndFeatureFeature = SysmlFactory.eINSTANCE.createFeature();
        successionSourceEndFeatureMembership.getOwnedRelatedElement().add(successionSourceEndFeatureFeature);

        var successionSourceRefSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        successionSourceRefSubsetting.setReferencedFeature(feature);
        successionSourceEndFeatureFeature.getOwnedRelationship().add(successionSourceRefSubsetting);
        return successionSourceEndFeatureMembership;
    }

    private boolean isInSameGraphicalContainer(Node sourceNode, Node targetNode, IDiagramService diagramService) {
        Diagram diagram = diagramService.getDiagramContext().diagram();
        var sourceParentNode = new NodeFinder(diagram).getParent(sourceNode);
        var targetParentNode = new NodeFinder(diagram).getParent(targetNode);
        return Objects.equals(sourceParentNode, targetParentNode);
    }

    private Namespace getClosestContainingDefinitionOrPackageFrom(Element element) {
        var owner = element.eContainer();
        while (!(owner instanceof Package || owner instanceof Definition) && owner != null) {
            owner = owner.eContainer();
        }
        return (Namespace) owner;
    }

    private void addChildInParent(Element parent, Element child) {
        // Parent could be a Package where children are stored
        // in OwingMembership and not in FeatureMembership.
        Membership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        if (parent instanceof Package) {
            membership = SysmlFactory.eINSTANCE.createOwningMembership();
        }
        membership.getOwnedRelatedElement().add(child);
        parent.getOwnedRelationship().add(membership);
    }
}
