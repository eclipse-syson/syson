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
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.services.ElementInitializerSwitch;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Usage;

/**
 * Tool-related Java services used by the {@link StateTransitionViewDiagramDescriptionProvider}.
 *
 * @author adieumegard
 */
public class StateTransitionViewToolService extends ViewToolService {

    private final ElementInitializerSwitch elementInitializerSwitch;

    public StateTransitionViewToolService(IObjectService objectService, IRepresentationDescriptionSearchService representationDescriptionSearchService,
            IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService) {
        super(objectService, representationDescriptionSearchService, viewRepresentationDescriptionSearchService);
        this.elementInitializerSwitch = new ElementInitializerSwitch();
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
     * @return the given {@link StateDefinition}.
     */
    public StateDefinition createChildState(StateDefinition stateDefinition, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean isParallel) {
        StateUsage childState = this.createChildState(stateDefinition, isParallel);
        this.createView(childState, editingContext, diagramContext, selectedNode, convertedNodes);
        return stateDefinition;
    }

    /**
     * Create a child State onto {@code stateDefinition}.
     *
     * @param stateDefinition
     *            The parent {@link StateDefinition}
     * @param isParallel
     *            Whether the created state is parallel or not
     * @return the created {@link StateUsage}.
     */
    private StateUsage createChildState(StateDefinition stateDefinition, boolean isParallel) {
        var owningMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        StateUsage childState = SysmlFactory.eINSTANCE.createStateUsage();
        childState.setIsParallel(isParallel);
        owningMembership.getOwnedRelatedElement().add(childState);
        stateDefinition.getOwnedRelationship().add(owningMembership);
        this.elementInitializerSwitch.doSwitch(childState);
        return childState;
    }

    /**
     * Precondition for the "New State" tool from StateTransition View StateDefinition node. Ensures that child states
     * of a StateDefinition are either all Parallel or none are.
     *
     * @param stateDefinition
     *            the {@link StateDefinition} corresponding to the target object on which the tool has been called.
     * @param isParallel
     *            whether or not the created State will be set as parallel.
     * @return whether an owned ({@code parallel} or not) StateUsage can be created in {@code stateDefinition}.
     */
    public boolean canCreateChildState(StateDefinition stateDefinition, boolean isParallel) {
        boolean isEmpty = stateDefinition.getOwnedState().isEmpty();
        boolean allMatch = stateDefinition.getOwnedState().stream().allMatch(st -> {
            return st.isIsParallel() == isParallel;
        });
        return isEmpty || allMatch;
    }

    /**
     * Create a new TransitionUsage and set it as the child of the parent of the sourceAction element. Sets its source
     * and target.
     *
     * @param sourceAction
     *            the ActionUsage used as a source for the transition
     * @param targetAction
     *            the ActionUsage used as a target for the transition
     * @return the given source {@link ActionUsage}.
     */
    public ActionUsage createTransitionUsage(ActionUsage sourceAction, ActionUsage targetAction) {
        // Check source and target have the same parent
        Element sourceParentElement = sourceAction.getOwner();
        Element targetParentElement = targetAction.getOwner();
        if (sourceParentElement != targetParentElement) {
            // Should probably not be here as the transition creation should not be allowed.
            return sourceAction;
        }
        // Create transition usage and add it to the parent element
        TransitionUsage newTransitionUsage = SysmlFactory.eINSTANCE.createTransitionUsage();
        this.elementInitializerSwitch.doSwitch(newTransitionUsage);
        var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        featureMembership.getOwnedRelatedElement().add(newTransitionUsage);
        sourceParentElement.getOwnedRelationship().add(featureMembership);

        // Create EndFeature
        var sourceMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        newTransitionUsage.getOwnedRelationship().add(sourceMembership);
        sourceMembership.setMemberElement(sourceAction);

        // Create Succession
        Succession succession = SysmlFactory.eINSTANCE.createSuccession();
        this.elementInitializerSwitch.doSwitch(succession);
        var successionFeatureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        successionFeatureMembership.getOwnedRelatedElement().add(succession);
        newTransitionUsage.getOwnedRelationship().add(successionFeatureMembership);

        // Set Succession
        succession.getSource().add(sourceAction);
        succession.getTarget().add(targetAction);

        return sourceAction;
    }

    @Override
    public Usage addExistingSubElements(Usage usage, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode, Object parentNode, DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var nestedUsages = usage.getNestedUsage();

        nestedUsages.stream().forEach(subUsage -> {
            this.createView(subUsage, editingContext, diagramContext, selectedNode, convertedNodes);
            Node fakeNode = this.createFakeNode(subUsage, selectedNode, diagramContext, diagramDescription, convertedNodes);
            this.addExistingSubElements(subUsage, editingContext, diagramContext, fakeNode, selectedNode, diagramDescription, convertedNodes);
        });
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
                this.addExistingSubElements(subUsage, editingContext, diagramContext, fakeNode, selectedNode, diagramDescription, convertedNodes);
            }
        });
        return definition;
    }
}
