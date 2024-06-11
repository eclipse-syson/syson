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
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.StateUsage;
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
    protected Usage addExistingSubElements(Usage usage, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode, Object parentNode, DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var nestedUsages = usage.getNestedUsage();

        nestedUsages.stream().forEach(subUsage -> {
            if (subUsage instanceof StateUsage || subUsage instanceof ActionUsage) {
                this.createView(subUsage, editingContext, diagramContext, selectedNode, convertedNodes);
                Node fakeNode = this.createFakeNode(subUsage, selectedNode, diagramContext, diagramDescription, convertedNodes);
                if (fakeNode != null) {
                    this.addExistingSubElements(subUsage, editingContext, diagramContext, fakeNode, selectedNode, diagramDescription, convertedNodes);
                }
            }
        });
        return usage;
    }

    @Override
    protected Definition addExistingSubElements(Definition definition, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode, Object parentNode,
            DiagramDescription diagramDescription, Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var ownedUsages = definition.getOwnedUsage();

        ownedUsages.stream().forEach(subUsage -> {
            if (subUsage instanceof StateUsage || subUsage instanceof ActionUsage) {
                this.createView(subUsage, editingContext, diagramContext, selectedNode, convertedNodes);
                Node fakeNode = this.createFakeNode(subUsage, selectedNode, diagramContext, diagramDescription, convertedNodes);
                if (fakeNode != null) {
                    this.addExistingSubElements(subUsage, editingContext, diagramContext, fakeNode, selectedNode, diagramDescription, convertedNodes);
                }
            }
        });
        return definition;
    }
}
