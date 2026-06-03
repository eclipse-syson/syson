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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.components.core.api.labels.StyledString;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.api.ISysMLMoveElementService;
import org.eclipse.syson.services.api.MoveStatus;
import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.CaseDefinition;
import org.eclipse.syson.sysml.CaseUsage;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.StakeholderMembership;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
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
                // This is an error, an Actor should always be contained in an ActorMembership.
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
                // This is an error, a Stakeholder should always be contained in a StakeholderMembership.
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
        // due to https://github.com/eclipse-sirius/sirius-web/issues/2930 we cannot prevent the user to do the
        // reconnect,
        // so we just warn him/her that this is not possible.
        String errorMessage = "An Actor cannot be disconnected from its UseCase or its Requirement element";
        this.logger.warn(errorMessage);
        this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
        return self;
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
