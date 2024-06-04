/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.diagram.statetransition.view.services;

import java.util.Map;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.diagram.statetransition.view.STVDescriptionNameGenerator;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.nodes.StateTransitionCompartmentNodeDescriptionProvider;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Usage;

/**
 * Tool-related Java services used by the {@link StateTransitionViewDiagramDescriptionProvider}.
 *
 * @author adieumegard
 */
public class StateTransitionViewToolService extends ViewToolService {

    public StateTransitionViewToolService(IObjectService objectService, IRepresentationDescriptionSearchService representationDescriptionSearchService,
            IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService, IFeedbackMessageService feedbackMessageService) {
        super(objectService, representationDescriptionSearchService, viewRepresentationDescriptionSearchService, feedbackMessageService);
    }

    @Override
    public Usage addExistingSubElements(Usage usage, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode, Object parentNode, DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (!(usage instanceof StateUsage)) {
            var nestedUsages = usage.getNestedUsage();
    
            nestedUsages.stream()
                .forEach(subUsage -> {
                    this.createView(subUsage, editingContext, diagramContext, selectedNode, convertedNodes);
                    Node fakeNode = this.createFakeNode(subUsage, selectedNode, diagramContext, diagramDescription, convertedNodes);
                    if (fakeNode != null) {
                        this.addExistingSubElements(subUsage, editingContext, diagramContext, fakeNode, selectedNode, diagramDescription, convertedNodes);
                    }
                });
        }
        return usage;
    }

    @Override
    public Definition addExistingSubElements(Definition definition, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode, Object parentNode,
            DiagramDescription diagramDescription, Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var ownedUsages = definition.getOwnedUsage();

        ownedUsages.stream().forEach(subUsage -> {
            if (subUsage instanceof StateUsage) {
                this.createView(subUsage, editingContext, diagramContext, selectedNode, convertedNodes);
                Node fakeNode = this.createFakeNode(subUsage, selectedNode, diagramContext, diagramDescription, convertedNodes);
                if (fakeNode != null) {
                    this.addExistingSubElements(subUsage, editingContext, diagramContext, fakeNode, selectedNode, diagramDescription, convertedNodes);
                }
            }
        });
        return definition;
    }

    /**
     * Called by "New State" tool from StateTransition View StateDefinition node.
     *
     * @param stateDefinition
     *            the {@link StateDefinition} corresponding to the target object on which the tool has been called.
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
     * @param isParallel
     *            whether or not the created State is set as parallel.
     * @return the created {@link StateUsage}.
     */
    public StateUsage createChildState(StateDefinition stateDefinition, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean isParallel) {
        StateUsage childState = this.createChildState(stateDefinition, isParallel);

        if (selectedNode.getInsideLabel().getText().equals(StateTransitionCompartmentNodeDescriptionProvider.STATE_COMPARTMENT_NAME)) {
            this.createView(childState, editingContext, diagramContext, selectedNode, convertedNodes);
        } else {
            selectedNode.getChildNodes().stream().filter(child -> child.getInsideLabel().getText().equals(StateTransitionCompartmentNodeDescriptionProvider.STATE_COMPARTMENT_NAME)).findFirst()
                    .ifPresent(compartmentNode -> {
                        this.createView(childState, editingContext, diagramContext, compartmentNode, convertedNodes);
                    });
        }

        return childState;
    }

    /**
     * Called by "New State" tool from StateTransition View StateUsage node.
     *
     * @param stateUsage
     *            the {@link StateUsage} corresponding to the target object on which the tool has been called.
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
     * @param isParallel
     *            whether or not the created State is set as parallel.
     * @return the created {@link StateUsage}.
     */
    public StateUsage createChildState(StateUsage stateUsage, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean isParallel) {
        StateUsage childState = this.createChildState(stateUsage, isParallel);

        if (selectedNode.getInsideLabel().getText().equals(StateTransitionCompartmentNodeDescriptionProvider.STATE_COMPARTMENT_NAME)) {
            this.createView(childState, editingContext, diagramContext, selectedNode, convertedNodes);
        } else {
            selectedNode.getChildNodes().stream().filter(child -> child.getInsideLabel().getText().equals(StateTransitionCompartmentNodeDescriptionProvider.STATE_COMPARTMENT_NAME)).findFirst()
                    .ifPresent(compartmentNode -> {
                        this.createView(childState, editingContext, diagramContext, compartmentNode, convertedNodes);
                    });
        }

        return childState;
    }

    /**
     * Create a new {@link ActionUsage} as a child of {@code parentState}. The {@code actionKind} string is matched
     * against possible {@link StateSubactionKind} values to set the new {@link ActionUsage} of the correct kind.
     * 
     * @param parentState
     *            The state onto which the action is added
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
     * @param actionKind
     *            The value against which the {@link StateSubactionKind} of the {@link ActionUsage} membership is set.
     * @return
     */
    public ActionUsage createOwnedAction(Element parentState, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, String actionKind) {
        ActionUsage childAction = this.createChildAction(parentState, actionKind);

        if (childAction != null) {
            if (selectedNode.getInsideLabel().getText().equals(STVDescriptionNameGenerator.ACTIONS)) {
                this.createView(childAction, editingContext, diagramContext, selectedNode, convertedNodes);
            } else {
                selectedNode.getChildNodes().stream().filter(child -> child.getInsideLabel().getText().equals(STVDescriptionNameGenerator.ACTIONS)).findFirst()
                    .ifPresent(compartmentNode -> {
                        this.createView(childAction, editingContext, diagramContext, compartmentNode, convertedNodes);
                    });
            }
        }

        return childAction;
    }

    /**
     * Create a child State onto {@code stateDefinition}.
     *
     * @param parentState
     *            The parent {@link StateDefinition} or {@link StateUsage}
     * @param isParallel
     *            Whether the created state is parallel or not
     * @return the created {@link StateUsage}.
     */
    private StateUsage createChildState(Element parentState, boolean isParallel) {
        var owningMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        StateUsage childState = SysmlFactory.eINSTANCE.createStateUsage();
        childState.setIsParallel(isParallel);
        owningMembership.getOwnedRelatedElement().add(childState);
        parentState.getOwnedRelationship().add(owningMembership);
        this.elementInitializerSwitch.doSwitch(childState);
        return childState;
    }
    
    /**
     * Create a child Action onto {@code stateDefinition}.
     * 
     * @param parentState
     *            The parent {@link StateDefinition} or {@link StateUsage}
     * @param actionKind
     *            The string value representing the kind of Action. Expected values are "Entry", "Do" or "Exit".
     * @return The created {@link ActionUsage} or null if the kind value is not correct
     */
    private ActionUsage createChildAction(Element parentState, String actionKind) {
        var owningMembership = SysmlFactory.eINSTANCE.createStateSubactionMembership();
        switch (actionKind) {
            case "Entry":
                owningMembership.setKind(StateSubactionKind.ENTRY);
                break;
            case "Do":
                owningMembership.setKind(StateSubactionKind.DO);
                break;
            case "Exit":
                owningMembership.setKind(StateSubactionKind.EXIT);
                break;
            default:
                return null;
        }
        ActionUsage childAction = SysmlFactory.eINSTANCE.createActionUsage();
        owningMembership.getOwnedRelatedElement().add(childAction);
        parentState.getOwnedRelationship().add(owningMembership);
        this.elementInitializerSwitch.doSwitch(childAction);
        return childAction;
    }
}
