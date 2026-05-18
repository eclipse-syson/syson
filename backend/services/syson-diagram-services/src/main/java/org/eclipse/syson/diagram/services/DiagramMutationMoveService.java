/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.components.core.api.labels.StyledString;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.services.api.ISysMLMoveElementService;
import org.eclipse.syson.services.api.MoveStatus;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.ViewUsage;
import org.springframework.stereotype.Service;

/**
 * Move-related services doing mutations in diagrams.
 *
 * @author arichard
 */
@Service
public class DiagramMutationMoveService {

    private final IFeedbackMessageService feedbackMessageService;

    private final ILabelService labelService;

    private final ISysMLMoveElementService moveService;

    private final DiagramMutationExposeService diagramMutationElementService;

    private final DiagramQueryElementService diagramQueryElementService;

    private final DeleteService deleteService;

    public DiagramMutationMoveService(IFeedbackMessageService feedbackMessageService, ILabelService labelService, ISysMLMoveElementService moveService,
            DiagramMutationExposeService diagramMutationElementService, DiagramQueryElementService diagramQueryElementService) {
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.labelService = Objects.requireNonNull(labelService);
        this.moveService = Objects.requireNonNull(moveService);
        this.diagramMutationElementService = Objects.requireNonNull(diagramMutationElementService);
        this.diagramQueryElementService = Objects.requireNonNull(diagramQueryElementService);
        this.deleteService = new DeleteService();
    }

    public void moveElement(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, DiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        MoveStatus moveStatus = this.moveService.moveSemanticElement(droppedElement, targetElement);
        if (moveStatus.isSuccess()) {
            ViewUsage viewUsage = this.diagramQueryElementService.getViewUsage(editingContext, diagramContext, droppedNode);
            if (viewUsage != null) {
                this.diagramMutationElementService.removeFromExposedElements(droppedElement, droppedNode, editingContext, diagramContext);
            }
            this.diagramMutationElementService.expose(droppedElement, editingContext, diagramContext, targetNode, convertedNodes);
        } else {
            this.feedbackMessageService.addFeedbackMessage(new Message(MessageFormat.format("Unable to move {0} in {1}: {2}", this.getLabel(droppedElement), this.getLabel(targetElement), moveStatus.message()), MessageLevel.WARNING));
        }

    }

    public void moveConstraintInRequirementConstraintCompartment(ConstraintUsage droppedConstraint, Element requirement, RequirementConstraintKind kind) {
        var oldMembership = droppedConstraint.eContainer();
        var membership = SysmlFactory.eINSTANCE.createRequirementConstraintMembership();
        membership.getOwnedRelatedElement().add(droppedConstraint);
        membership.setKind(kind);
        requirement.getOwnedRelationship().add(membership);
        if (oldMembership instanceof OwningMembership owningMembership) {
            this.deleteService.deleteFromModel(owningMembership);
        }
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
