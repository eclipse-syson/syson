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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.components.core.api.labels.StyledString;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.api.ISysMLMoveElementService;
import org.eclipse.syson.services.api.MoveStatus;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.CaseDefinition;
import org.eclipse.syson.sysml.CaseUsage;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.ConnectionDefinition;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.StakeholderMembership;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Tool-related services doing mutations in diagrams and models.
 *
 * @author arichard
 */
@Service
public class DiagramMutationToolService {

    private final IFeedbackMessageService feedbackMessageService;

    private final ISysMLMoveElementService moveService;

    private final ILabelService labelService;

    private final DeleteService deleteService;

    private final UtilService utilService;

    private final MetamodelMutationElementService metamodelMutationElementService;

    private final Logger logger = LoggerFactory.getLogger(DiagramMutationToolService.class);

    /**
     * Creates a new tool mutation service.
     *
     * @param feedbackMessageService
     *            the service used to report user-facing feedback messages
     * @param moveService
     *            the service used to move SysML semantic elements
     * @param labelService
     *            the service used to compute element labels in feedback messages
     */
    public DiagramMutationToolService(IFeedbackMessageService feedbackMessageService, ISysMLMoveElementService moveService, ILabelService labelService) {
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.moveService = Objects.requireNonNull(moveService);
        this.labelService = Objects.requireNonNull(labelService);
        this.deleteService = new DeleteService();
        this.utilService = new UtilService();
        this.metamodelMutationElementService = new MetamodelMutationElementService();
    }

    /**
     * Moves the given usage under the new container and marks it as composite when the move succeeds.
     *
     * @param usage
     *            the usage to move
     * @param newContainer
     *            the new semantic container
     * @return the moved usage
     */
    public Usage becomeNestedUsage(Usage usage, Element newContainer) {
        if (this.getOwnerHierarchy(newContainer).contains(usage) || Objects.equals(newContainer, usage)) {
            String message = MessageFormat.format("Cannot change the owner of {0}, this would create a containment cycle", String.valueOf(usage.getName()));
            this.feedbackMessageService.addFeedbackMessage(new Message(message, MessageLevel.WARNING));
            this.logger.warn(message);
        } else {
            MoveStatus moveStatus = this.moveService.moveSemanticElement(usage, newContainer);
            if (moveStatus.isSuccess()) {
                usage.setIsComposite(true);
            } else {
                this.feedbackMessageService.addFeedbackMessage(new Message(MessageFormat.format("Unable to move {0} in {1}: {2}", this.getLabel(usage), this.getLabel(newContainer), moveStatus.message()), MessageLevel.WARNING));
            }

        }
        return usage;
    }

    /**
     * Moves the requirement into an objective membership owned by the new container when possible.
     *
     * @param requirement
     *            the requirement to use as objective
     * @param newContainer
     *            the target use case element
     * @return the requirement
     */
    public RequirementUsage becomeObjectiveRequirement(RequirementUsage requirement, Element newContainer) {
        if (newContainer instanceof UseCaseUsage || newContainer instanceof UseCaseDefinition) {
            if (this.utilService.isEmptyObjectiveRequirement(newContainer)) {
                var eContainer = requirement.eContainer();
                if (eContainer instanceof ObjectiveMembership objectiveMembership) {
                    newContainer.getOwnedRelationship().add(objectiveMembership);
                } else if (eContainer instanceof OwningMembership owningMembership) {
                    var newObjectiveMembership = SysmlFactory.eINSTANCE.createObjectiveMembership();
                    newObjectiveMembership.getOwnedRelatedElement().add(requirement);
                    newContainer.getOwnedRelationship().add(newObjectiveMembership);
                    this.deleteService.deleteFromModel(owningMembership);
                }
            }
        }
        return requirement;
    }

    /**
     * Reconnects the source of a nested actor edge.
     *
     * @param self
     *            the current source semantic element
     * @param newSource
     *            the new source semantic element
     * @param otherEnd
     *            the actor connected by the edge
     * @return the actor connected by the edge
     */
    public Element reconnectSourceNestedActorEdge(Element self, Element newSource, Element otherEnd) {
        if (newSource instanceof UseCaseUsage || newSource instanceof UseCaseDefinition
                || newSource instanceof RequirementUsage || newSource instanceof RequirementDefinition) {
            if (otherEnd.getOwningMembership() instanceof ActorMembership actorMembership) {
                newSource.getOwnedRelationship().add(actorMembership);
            } else {
                String errorMessage = "Cannot reconnect the Actor, it is not owned by an " + ActorMembership.class.getSimpleName();
                this.logger.error(errorMessage);
                this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.ERROR));
            }
        } else {
            String errorMessage = "Cannot reconnect an Actor to a non-UseCase, non-Requirement element";
            this.logger.warn(errorMessage);
            this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
        }
        return otherEnd;
    }

    /**
     * Reconnects the source of a nested stakeholder edge.
     *
     * @param self
     *            the current source semantic element
     * @param newSource
     *            the new requirement source
     * @param otherEnd
     *            the stakeholder connected by the edge
     * @return the stakeholder connected by the edge
     */
    public Element reconnectSourceNestedStakeholderEdge(Element self, Element newSource, Element otherEnd) {
        if (newSource instanceof RequirementUsage || newSource instanceof RequirementDefinition) {
            if (otherEnd.getOwningMembership() instanceof StakeholderMembership stakeholderMembership) {
                newSource.getOwnedRelationship().add(stakeholderMembership);
            } else {
                String errorMessage = "Cannot reconnect the Stakeholder, it is not owned by a " + StakeholderMembership.class.getSimpleName();
                this.logger.error(errorMessage);
                this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.ERROR));
            }
        } else {
            String errorMessage = "Cannot reconnect a Stakeholder to a non-Requirement element";
            this.logger.warn(errorMessage);
            this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
        }
        return otherEnd;
    }

    /**
     * Reconnects the source of a nested subject edge.
     *
     * @param self
     *            the current source semantic element
     * @param newSource
     *            the new case or requirement source
     * @param otherEnd
     *            the subject connected by the edge
     * @return the subject connected by the edge
     */
    public Element reconnectSourceNestedSubjectEdge(Element self, Element newSource, Element otherEnd) {
        if (newSource instanceof CaseUsage || newSource instanceof CaseDefinition
                || newSource instanceof RequirementUsage || newSource instanceof RequirementDefinition) {
            if (otherEnd.getOwningMembership() instanceof SubjectMembership subjectMembership) {
                newSource.getOwnedRelationship().add(subjectMembership);
            } else {
                String errorMessage = "Cannot reconnect the Subject, it is not owned by a " + SubjectMembership.class.getSimpleName();
                this.logger.error(errorMessage);
                this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.ERROR));
            }
        } else {
            String errorMessage = "Cannot reconnect a Subject to a non-UseCase, non-Requirement element";
            this.logger.warn(errorMessage);
            this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
        }
        return otherEnd;
    }

    /**
     * Reconnects the source of a composition edge by moving the other end under the new source.
     *
     * @param self
     *            the current semantic element
     * @param newSource
     *            the new owner of the other end
     * @param otherEnd
     *            the semantic element to move
     * @return the moved semantic element
     */
    public Element reconnnectSourceCompositionEdge(Element self, Element newSource, Element otherEnd) {
        Element result = otherEnd;
        if (this.getOwnerHierarchy(newSource).contains(otherEnd) || Objects.equals(newSource, otherEnd)) {
            String message = MessageFormat.format("Cannot change the owner of {0}, this would create a containment cycle", String.valueOf(otherEnd.getName()));
            this.feedbackMessageService.addFeedbackMessage(new Message(message, MessageLevel.WARNING));
            this.logger.warn(message);
        } else {
            MoveStatus moveStatus = this.moveService.moveSemanticElement(otherEnd, newSource);
            if (!moveStatus.isSuccess()) {
                this.feedbackMessageService.addFeedbackMessage(new Message(MessageFormat.format("Unable to move {0} in {1}: {2}", this.getLabel(self), this.getLabel(newSource),
                        moveStatus.message()), MessageLevel.WARNING));
            }
            result = otherEnd;
        }
        return result;
    }

    /**
     * Reconnects the target of a composition edge by moving the old target to the closest containing package.
     *
     * @param self
     *            the semantic element used to find the closest package
     * @param oldTarget
     *            the previous target
     * @param newTarget
     *            the new target
     * @param otherEnd
     *            the opposite edge end
     * @return the semantic element used as context
     */
    public Element reconnnectTargetCompositionEdge(Element self, Element oldTarget, Element newTarget, Element otherEnd) {
        if (this.getOwnerHierarchy(otherEnd).contains(newTarget) || Objects.equals(otherEnd, newTarget)) {
            String message = MessageFormat.format("Cannot change the owner of {0}, this would create a containment cycle", String.valueOf(otherEnd.getName()));
            this.feedbackMessageService.addFeedbackMessage(new Message(message, MessageLevel.WARNING));
            this.logger.warn(message);
        } else {
            var oldContainer = oldTarget.eContainer();
            if (newTarget instanceof Usage && oldContainer instanceof FeatureMembership featureMembership) {
                var pack = this.getClosestContainingPackageFrom(self);
                if (pack != null) {
                    var owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                    pack.getOwnedRelationship().add(owningMembership);
                    owningMembership.getOwnedRelatedElement().add(oldTarget);
                    var oldMembership = newTarget.eContainer();
                    featureMembership.getOwnedRelatedElement().add(newTarget);
                    if (oldMembership instanceof OwningMembership oldOwningMembership) {
                        this.deleteService.deleteFromModel(oldOwningMembership);
                    }
                }
            }
        }
        return self;
    }

    /**
     * Reconnects an annotating element to a new annotated target.
     *
     * @param self
     *            the annotating element
     * @param newTarget
     *            the new annotated element
     * @return the annotating element
     */
    public Element reconnnectTargetAnnotatedEdge(Element self, Element newTarget) {
        if (!(newTarget instanceof Comment) && !(newTarget instanceof Documentation)) {
            MoveStatus moveStatus = this.moveService.moveSemanticElement(self, newTarget);
            if (!moveStatus.isSuccess()) {
                this.feedbackMessageService.addFeedbackMessage(new Message(MessageFormat.format("Unable to move {0} in {1}: {2}", this.getLabel(self), this.getLabel(newTarget),
                        moveStatus.message()), MessageLevel.WARNING));
            }
        }
        return self;
    }

    /**
     * Reconnects the target of a nested actor edge (the Actor side of the edge). This service is only used because edge
     * preconditions are not taken into account.
     *
     * @param self
     *            the current UseCase or Requirement
     * @return the given self
     */
    public Element reconnectTargetNestedActorEdge(Element self) {
        String errorMessage = "An Actor cannot be disconnected from its UseCase or its Requirement element";
        this.logger.warn(errorMessage);
        this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
        return self;
    }

    /**
     * Create the appropriate compartment item for the given structural feature.
     *
     * @param element
     *            the given element
     * @param eReferenceName
     *            the compartment reference name
     * @return the created element or the given container when nothing was created
     */
    public Element createCompartmentItem(Element element, String eReferenceName) {
        EStructuralFeature feature = element.eClass().getEStructuralFeature(eReferenceName);
        Element result = element;
        if (feature.getEType() instanceof EClass itemEClass) {
            var item = SysmlFactory.eINSTANCE.create(itemEClass);
            if (item instanceof Element elementItem) {
                var membership = this.createAppropriateMembership(feature);
                element.getOwnedRelationship().add(membership);
                membership.getOwnedRelatedElement().add(elementItem);
                result = this.metamodelMutationElementService.initialize(elementItem);
            }
        }
        return result;
    }

    /**
     * Create the appropriate compartment item for the given structural feature and assign the given direction.
     *
     * @param element
     *            the given element
     * @param eReferenceName
     *            the compartment reference name
     * @param directionLiteral
     *            the feature direction literal
     * @return the created element or the given container when nothing was created
     */
    public Element createCompartmentItemWithDirection(Element element, String eReferenceName, String directionLiteral) {
        EStructuralFeature structuralFeature = element.eClass().getEStructuralFeature(eReferenceName);
        Element result = element;
        if (structuralFeature.getEType() instanceof EClass itemEClass) {
            var item = SysmlFactory.eINSTANCE.create(itemEClass);
            if (item instanceof Element elementItem) {
                var membership = this.createAppropriateMembership(structuralFeature);
                element.getOwnedRelationship().add(membership);
                membership.getOwnedRelatedElement().add(elementItem);
                result = this.metamodelMutationElementService.initialize(elementItem);
                if (directionLiteral != null && elementItem instanceof Feature feature) {
                    feature.setDirection(FeatureDirectionKind.get(directionLiteral));
                    result.setDeclaredName(result.getName() + StringUtils.capitalize(directionLiteral));
                }
            }
        }
        return result;
    }

    /**
     * Create a new RequirementUsage and set it as the objective requirement of the self element.
     *
     * @param self
     *            the element usage to set the objective for
     * @param selectedObject
     *            the selected object used for subsetting or typing
     * @return the created requirement usage
     */
    public Element createRequirementUsageAsObjectiveRequirement(Element self, Element selectedObject) {
        Element result = self;
        if (self instanceof CaseUsage
                || self instanceof CaseDefinition) {
            RequirementUsage newRequirementUsage = SysmlFactory.eINSTANCE.createRequirementUsage();
            result = newRequirementUsage;
            var objectiveMembership = this.createMembership(self, SysmlPackage.eINSTANCE.getObjectiveMembership());
            objectiveMembership.getOwnedRelatedElement().add(newRequirementUsage);
            this.metamodelMutationElementService.initialize(newRequirementUsage);
            if (selectedObject instanceof RequirementUsage requirementUsage) {
                this.utilService.setSubsetting(newRequirementUsage, requirementUsage);
            } else if (selectedObject instanceof RequirementDefinition requirementDefinition) {
                this.utilService.setFeatureTyping(newRequirementUsage, requirementDefinition);
            }
        }
        return result;
    }

    /**
     * Create a new subject reference usage.
     *
     * @param self
     *            the containing element
     * @param selectedObject
     *            the selected object used for subsetting or typing
     * @return the created reference usage or the given element when nothing was created
     */
    public Element createReferenceUsageAsSubject(Element self, Element selectedObject) {
        Element result = self;
        if (self instanceof CaseUsage
                || self instanceof CaseDefinition
                || self instanceof RequirementUsage
                || self instanceof RequirementDefinition) {
            ReferenceUsage newReferenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
            var subjectMembership = this.createMembership(self, SysmlPackage.eINSTANCE.getSubjectMembership());
            subjectMembership.getOwnedRelatedElement().add(newReferenceUsage);
            this.metamodelMutationElementService.initialize(newReferenceUsage);
            if (selectedObject instanceof Usage usage) {
                this.utilService.setSubsetting(newReferenceUsage, usage);
            } else if (selectedObject instanceof Definition definition) {
                this.utilService.setFeatureTyping(newReferenceUsage, definition);
            }
            result = newReferenceUsage;
        }
        return result;
    }

    /**
     * Create a new actor part usage.
     *
     * @param self
     *            the containing element
     * @param selectedObject
     *            the selected object used for subsetting or typing
     * @return the created part usage or the given element when nothing was created
     */
    public Element createPartUsageAsActor(Element self, Element selectedObject) {
        Element result = self;
        if (self instanceof CaseUsage
                || self instanceof CaseDefinition
                || self instanceof RequirementUsage
                || self instanceof RequirementDefinition) {
            PartUsage newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            var actorMembership = this.createMembership(self, SysmlPackage.eINSTANCE.getActorMembership());
            actorMembership.getOwnedRelatedElement().add(newPartUsage);
            this.metamodelMutationElementService.initialize(newPartUsage);
            if (selectedObject instanceof ItemUsage usage) {
                this.utilService.setSubsetting(newPartUsage, usage);
            } else if (selectedObject instanceof ItemDefinition definition) {
                this.utilService.setFeatureTyping(newPartUsage, definition);
            }
            result = newPartUsage;
        }
        return result;
    }

    /**
     * Service to create a new <b>stakeholder</b> {@link PartUsage} for a {@link RequirementUsage} or
     * {@link RequirementDefinition}.
     *
     * @param self
     *            a {@link RequirementUsage} or {@link RequirementDefinition}, otherwise no {@link PartUsage} will be
     *            created.
     * @param selectedObject
     *            an {@link ItemUsage} or {@link ItemDefinition} that will be subsetted by (respectively that will type)
     *            the created {@link PartUsage}. If {@code null} then the <b>stakeholder</b> {@link PartUsage} will not
     *            be subsetted.
     * @return the newly-created {@link PartUsage}, contained by {@code self} through a {@link StakeholderMembership}.
     *         If {@code self} was neither a {@link RequirementUsage} nor a {@link RequirementDefinition}, {@code self}
     *         is returned as-is.
     */
    public Element createPartUsageAsStakeholder(Element self, Element selectedObject) {
        Element result = self;
        if (self instanceof RequirementUsage || self instanceof RequirementDefinition) {
            var createdPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            var stakeholderMembership = this.createMembership(self, SysmlPackage.eINSTANCE.getStakeholderMembership());
            stakeholderMembership.getOwnedRelatedElement().add(createdPartUsage);
            this.metamodelMutationElementService.initialize(createdPartUsage);

            if (selectedObject instanceof ItemUsage selectedItemUsage) {
                this.utilService.setSubsetting(createdPartUsage, selectedItemUsage);
            } else if (selectedObject instanceof ItemDefinition selectedItemDefinition) {
                this.utilService.setFeatureTyping(createdPartUsage, selectedItemDefinition);
            }

            result = createdPartUsage;
        }
        return result;
    }

    /**
     * Creates a new action parameter with the given direction.
     *
     * @param self
     *            the owning action or behavior
     * @param direction
     *            the direction name
     * @return the created reference usage
     */
    public Element createActionParameter(Element self, String direction) {
        var newReferenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
        var featureMembership = this.createMembership(self, SysmlPackage.eINSTANCE.getFeatureMembership());
        featureMembership.getOwnedRelatedElement().add(newReferenceUsage);
        this.metamodelMutationElementService.initialize(newReferenceUsage);
        newReferenceUsage.setDirection(FeatureDirectionKind.getByName(direction));
        return newReferenceUsage;
    }

    /**
     * Create a new connection end (a ReferenceUsage) in a given ConnectionDefinition.
     *
     * @param self
     *            the connection definition in which the new end is added
     * @return the modified connection definition
     */
    public Element createConnectionDefinitionEnd(ConnectionDefinition self) {
        int suffix = self.getOwnedEndFeature().size() + 1;
        this.addConnectionEnd(self, "end" + suffix);
        return self;
    }

    /**
     * Create a new interface end (a PortUsage) in a given InterfaceDefinition.
     *
     * @param self
     *            the interface definition in which the new end is added
     * @return the modified interface definition
     */
    public Element createInterfaceDefinitionEnd(InterfaceDefinition self) {
        int suffix = self.getOwnedEndFeature().size() + 1;
        this.addPort(self, "port" + suffix);
        return self;
    }

    /**
     * Create a new accept action under a part or action owner.
     *
     * @param ownerElement
     *            the owner of the new accept action
     * @return the created accept action or the owner when creation is not applicable
     */
    public Element createAcceptAction(Element ownerElement) {
        if (this.isPart(ownerElement) || this.isAction(ownerElement)) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            ownerElement.getOwnedRelationship().add(featureMember);
            var acceptAction = SysmlFactory.eINSTANCE.createAcceptActionUsage();
            featureMember.getOwnedRelatedElement().add(acceptAction);
            return this.metamodelMutationElementService.initialize(acceptAction);
        }
        return ownerElement;
    }

    /**
     * Create or replace the payload parameter of an accept action usage.
     *
     * @param self
     *            the accept action usage
     * @param payloadEClassName
     *            the payload classifier name
     * @return the given accept action usage
     */
    public Element createAcceptActionPayload(AcceptActionUsage self, String payloadEClassName) {
        var classifier = SysmlPackage.eINSTANCE.getEClassifier(payloadEClassName);
        if (classifier instanceof EClass eClass) {
            var payload = SysmlFactory.eINSTANCE.create(eClass);
            if (payload instanceof Type payloadType) {
                payloadType.setDeclaredName(self.getDeclaredName() + "PayloadType");
                var membership = SysmlFactory.eINSTANCE.createOwningMembership();
                membership.getOwnedRelatedElement().add(payloadType);
                var payloadParent = this.getClosestContainingPackageFrom(self);
                payloadParent.getOwnedRelationship().add(membership);
                var featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
                featureTyping.setType(payloadType);
                var referenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
                referenceUsage.setDeclaredName("payload");
                referenceUsage.setDirection(FeatureDirectionKind.INOUT);
                referenceUsage.getOwnedRelationship().add(featureTyping);
                var parameterMembership = this.getPayloadParameterMembership(self);
                var oldParameterContent = parameterMembership.getOwnedMemberParameter();
                if (oldParameterContent != null) {
                    this.deleteService.deleteFromModel(oldParameterContent);
                }
                parameterMembership = this.getPayloadParameterMembership(self);
                parameterMembership.getOwnedRelatedElement().add(referenceUsage);
                self.getOwnedRelationship().add(parameterMembership);
            }
        }
        return self;
    }

    /**
     * Create or replace the receiver parameter of an accept action usage.
     *
     * @param self
     *            the accept action usage
     * @return the given accept action usage
     */
    public Element createAcceptActionReceiver(AcceptActionUsage self) {
        var newPort = SysmlFactory.eINSTANCE.createPortUsage();
        newPort.setDeclaredName(self.getDeclaredName() + "'s receiver");
        var owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        owningMembership.getOwnedRelatedElement().add(newPort);
        var receiverParent = this.getClosestContainingPackageFrom(self);
        receiverParent.getOwnedRelationship().add(owningMembership);

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
            this.deleteService.deleteFromModel(oldParameterContent);
        }
        parameterMembership = this.getReceiverParameterMembership(self);
        parameterMembership.getOwnedRelatedElement().add(referenceUsage);
        self.getOwnedRelationship().add(parameterMembership);
        return self;
    }

    /**
     * Add the standard start action as the child of the given element.
     *
     * @param ownerElement
     *            an element that will own the standard start action
     * @return the action usage returned by the shared utility service
     */
    public ActionUsage addStartAction(Element ownerElement) {
        return this.utilService.retrieveStandardStartAction(ownerElement);
    }

    /**
     * Add the standard start state as the child of the given element.
     *
     * @param ownerElement
     *            an element that will own the standard start state
     * @return the action usage returned by the shared utility service
     */
    public ActionUsage addStartState(Element ownerElement) {
        return this.utilService.retrieveStandardStartState(ownerElement);
    }

    /**
     * Add the standard done action as the child of the given element.
     *
     * @param ownerElement
     *            an element that will own the standard done action
     * @return the action usage returned by the shared utility service
     */
    public ActionUsage addDoneAction(Element ownerElement) {
        return this.utilService.retrieveStandardDoneAction(ownerElement);
    }

    /**
     * Add the standard done state as the child of the given element.
     *
     * @param ownerElement
     *            an element that will own the standard done state
     * @return the action usage returned by the shared utility service
     */
    public ActionUsage addDoneState(Element ownerElement) {
        return this.utilService.retrieveStandardDoneState(ownerElement);
    }

    /**
     * Create a new sub-action usage.
     *
     * @param ownerElement
     *            the owner of the new action usage
     * @return the newly created action usage
     */
    public ActionUsage createSubActionUsage(Element ownerElement) {
        var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        ownerElement.getOwnedRelationship().add(featureMembership);
        var newActionUsage = SysmlFactory.eINSTANCE.createActionUsage();
        featureMembership.getOwnedRelatedElement().add(newActionUsage);
        return this.metamodelMutationElementService.initialize(newActionUsage);
    }

    /**
     * Creation of the semantic elements associated to a Join action.
     *
     * @param ownerElement
     *            the element owning the new Join action
     * @return the new Join action if it has been successfully created or <code>ownerElement</code> otherwise
     */
    public Element createJoinAction(Element ownerElement) {
        if (this.isPart(ownerElement) || this.isAction(ownerElement)) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            var join = SysmlFactory.eINSTANCE.createJoinNode();
            featureMember.getOwnedRelatedElement().add(join);
            ownerElement.getOwnedRelationship().add(featureMember);
            return this.metamodelMutationElementService.initialize(join);
        }
        return ownerElement;
    }

    /**
     * Creation of the semantic elements associated to a Fork action.
     *
     * @param ownerElement
     *            the element owning the new Fork action
     * @return the new Fork action if it has been successfully created or <code>ownerElement</code> otherwise
     */
    public Element createForkAction(Element ownerElement) {
        if (this.isPart(ownerElement) || this.isAction(ownerElement)) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            ownerElement.getOwnedRelationship().add(featureMember);
            var fork = SysmlFactory.eINSTANCE.createForkNode();
            featureMember.getOwnedRelatedElement().add(fork);
            return this.metamodelMutationElementService.initialize(fork);
        }
        return ownerElement;
    }

    /**
     * Creation of the semantic elements associated to a Merge action.
     *
     * @param ownerElement
     *            the element owning the new Merge action
     * @return the new Merge action if it has been successfully created or <code>ownerElement</code> otherwise
     */
    public Element createMergeAction(Element ownerElement) {
        if (this.isPart(ownerElement) || this.isAction(ownerElement)) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            var merge = SysmlFactory.eINSTANCE.createMergeNode();
            featureMember.getOwnedRelatedElement().add(merge);
            ownerElement.getOwnedRelationship().add(featureMember);
            return this.metamodelMutationElementService.initialize(merge);
        }
        return ownerElement;
    }

    /**
     * Creation of the semantic elements associated to a Decision action.
     *
     * @param ownerElement
     *            the element owning the new Decision action
     * @return the new Decision action if it has been successfully created or <code>ownerElement</code> otherwise
     */
    public Element createDecisionAction(Element ownerElement) {
        if (this.isPart(ownerElement) || this.isAction(ownerElement)) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            ownerElement.getOwnedRelationship().add(featureMember);
            var decision = SysmlFactory.eINSTANCE.createDecisionNode();
            featureMember.getOwnedRelatedElement().add(decision);
            return this.metamodelMutationElementService.initialize(decision);
        }
        return ownerElement;
    }

    /**
     * Creation of the semantic elements associated to an Assignment action inside an Action or ActionDefinition.
     *
     * @param ownerElement
     *            the element owning the new Assignment action
     * @return the new Assignment action if it has been successfully created or <code>ownerElement</code> otherwise
     */
    public Element createAssignmentAction(Element ownerElement) {
        if (ownerElement instanceof ActionUsage || ownerElement instanceof ActionDefinition) {
            var featureMember = SysmlFactory.eINSTANCE.createFeatureMembership();
            ownerElement.getOwnedRelationship().add(featureMember);
            var assignmentAction = SysmlFactory.eINSTANCE.createAssignmentActionUsage();
            featureMember.getOwnedRelatedElement().add(assignmentAction);
            return this.metamodelMutationElementService.initialize(assignmentAction);
        }
        return ownerElement;
    }

    /**
     * Create a perform action usage in the given owner.
     *
     * @param ownerElement
     *            the owner of the new perform action
     * @return the created perform action usage
     */
    public Element createPerformAction(Element ownerElement) {
        var featureMember = this.metamodelMutationElementService.createMembership(ownerElement);
        ownerElement.getOwnedRelationship().add(featureMember);
        var perform = SysmlFactory.eINSTANCE.createPerformActionUsage();
        featureMember.getOwnedRelatedElement().add(perform);
        return this.metamodelMutationElementService.initialize(perform);
    }

    /**
     * Creates a new sibling part usage as well as a subsetting edge between those parts.
     *
     * @param self
     *            the {@link PartUsage} to subset by a new part usage
     * @return the new part usage or self if something went wrong
     */
    public PartUsage createPartUsageAndSubsetting(PartUsage self) {
        var parent = self.getOwner();
        if (parent != null) {
            var membership = SysmlFactory.eINSTANCE.createOwningMembership();
            parent.getOwnedRelationship().add(membership);
            var newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            membership.getOwnedRelatedElement().add(newPartUsage);
            this.metamodelMutationElementService.initialize(newPartUsage);
            this.utilService.setSubsetting(self, newPartUsage);
            return newPartUsage;
        }
        return self;
    }

    /**
     * Creates a new sibling part definition as well as a feature typing edge between the part usage and the part
     * definition.
     *
     * @param self
     *            the {@link PartUsage} to type by a new part definition
     * @return the new part definition or self if something went wrong
     */
    public Element createPartDefinitionAndFeatureTyping(PartUsage self) {
        var parent = self.getOwner();
        if (parent != null) {
            var membership = SysmlFactory.eINSTANCE.createOwningMembership();
            parent.getOwnedRelationship().add(membership);
            var newPartDefinition = this.utilService.createPartDefinitionFrom(self);
            membership.getOwnedRelatedElement().add(newPartDefinition);
            this.metamodelMutationElementService.initialize(newPartDefinition);
            this.utilService.setFeatureTyping(self, newPartDefinition);
            return newPartDefinition;
        }
        return self;
    }

    /**
     * Create a namespace import under the given namespace.
     *
     * @param self
     *            the namespace that will own the import
     * @param importedNamespace
     *            the imported namespace
     * @return the created import or the given element when creation is not applicable
     */
    public Element createNamespaceImport(Element self, Namespace importedNamespace) {
        if (self instanceof Namespace namespace) {
            var namespaceImport = SysmlFactory.eINSTANCE.createNamespaceImport();
            namespace.getOwnedRelationship().add(namespaceImport);
            this.metamodelMutationElementService.initialize(namespaceImport);
            namespaceImport.setImportedNamespace(importedNamespace);
            return namespaceImport;
        }
        return self;
    }

    /**
     * Creates a state sub action (entry, do or exit actions) as a child of the given StateUsage or StateDefinition.
     *
     * @param self
     *            the state usage or state definition owning the sub action
     * @param performedAction
     *            an action usage or <code>null</code>; if set the new perform action will reference this action
     * @param kindLiteral
     *            the kind of the state subaction membership owning the perform action
     * @return the new perform action or null if self is not a state
     */
    public PerformActionUsage createStateSubaction(Element self, ActionUsage performedAction, String kindLiteral) {
        if (self instanceof StateUsage || self instanceof StateDefinition) {
            StateSubactionMembership stateSubactionMembership = (StateSubactionMembership) this.createMembership(self, SysmlPackage.eINSTANCE.getStateSubactionMembership());
            stateSubactionMembership.setKind(StateSubactionKind.get(kindLiteral));
            var performAction = SysmlFactory.eINSTANCE.createPerformActionUsage();
            stateSubactionMembership.getOwnedRelatedElement().add(performAction);
            this.metamodelMutationElementService.initialize(performAction);
            if (performedAction != null) {
                var referenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
                referenceSubsetting.setReferencedFeature(performedAction);
                performAction.getOwnedRelationship().add(referenceSubsetting);
            }
            return performAction;
        }
        return null;
    }

    /**
     * Moves the usage into its closest containing package.
     *
     * @param usage
     *            the usage to move
     * @return the moved usage
     */
    public Element moveToClosestContainingPackage(Usage usage) {
        var pack = this.getClosestContainingPackageFrom(usage);
        if (pack != null) {
            var oldMembership = usage.eContainer();
            var owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
            pack.getOwnedRelationship().add(owningMembership);
            owningMembership.getOwnedRelatedElement().add(usage);
            if (oldMembership instanceof OwningMembership oldOwningMembership) {
                this.deleteService.deleteFromModel(oldOwningMembership);
            }
        }
        return usage;
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
        } else if (feature.equals(SysmlPackage.eINSTANCE.getRequirementUsage_FramedConcern()) || feature.equals(SysmlPackage.eINSTANCE.getRequirementDefinition_FramedConcern())) {
            result = SysmlFactory.eINSTANCE.createFramedConcernMembership();
        }
        return result;
    }

    private Membership createMembership(Element element, EClass membershipType) {
        Membership membership = null;
        var eObject = SysmlFactory.eINSTANCE.create(membershipType);
        if (eObject instanceof Membership typedMembership) {
            membership = typedMembership;
            element.getOwnedRelationship().add(membership);
        }
        return membership;
    }

    private void addConnectionEnd(ConnectionDefinition connectionDefinition, String name) {
        var referenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
        this.metamodelMutationElementService.initialize(referenceUsage);
        referenceUsage.setDeclaredName(name);
        referenceUsage.setIsEnd(true);
        var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        featureMembership.getOwnedRelatedElement().add(referenceUsage);
        connectionDefinition.getOwnedRelationship().add(featureMembership);
    }

    private void addPort(InterfaceDefinition interfaceDefinition, String name) {
        var portUsage = SysmlFactory.eINSTANCE.createPortUsage();
        this.metamodelMutationElementService.initialize(portUsage);
        portUsage.setDeclaredName(name);
        portUsage.setIsEnd(true);
        var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        featureMembership.getOwnedRelatedElement().add(portUsage);
        interfaceDefinition.getOwnedRelationship().add(featureMembership);
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
        var parameterMembership = SysmlFactory.eINSTANCE.createParameterMembership();
        parameterMembership.getOwnedRelatedElement().add(reference);
        return parameterMembership;
    }

    private boolean isPart(Element element) {
        return element instanceof PartUsage || element instanceof PartDefinition;
    }

    private boolean isAction(Element element) {
        return element instanceof ActionUsage || element instanceof ActionDefinition;
    }

    private Package getClosestContainingPackageFrom(Element element) {
        var owner = element.eContainer();
        while (!(owner instanceof Package) && owner != null) {
            owner = owner.eContainer();
        }
        return (Package) owner;
    }

    private List<Element> getOwnerHierarchy(Element element) {
        List<Element> ownerHierarchy = new ArrayList<>();
        Element currentElement = element;
        while (currentElement.getOwner() != null) {
            ownerHierarchy.add(currentElement.getOwner());
            currentElement = currentElement.getOwner();
        }
        return ownerHierarchy;
    }

    private String getLabel(Object droppedElement) {
        final String label;
        StyledString styledLabel = this.labelService.getStyledLabel(droppedElement);
        if (styledLabel != null && !styledLabel.toString().isEmpty()) {
            label = styledLabel.toString();
        } else if (droppedElement instanceof EObject droppedEObject) {
            label = droppedEObject.eClass().getName();
        } else {
            label = "";
        }
        return label;
    }
}
