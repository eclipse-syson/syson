/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.diagram.general.view.services.nodeactions.managevisibility;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramQueryService;
import org.eclipse.sirius.components.collaborative.diagrams.api.nodeactions.IManageVisibilityMenuActionHandler;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.IDiagramElement;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.events.HideDiagramElementEvent;
import org.eclipse.sirius.components.representations.IStatus;
import org.eclipse.sirius.components.representations.Success;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Handler for the menu action on the manage visibility modal that will reveal children that also have children.
 *
 * @author mcharfadi
 */
@Service
public class GeneralViewManageVisibilityRevealValuedContentHandler implements IManageVisibilityMenuActionHandler {

    private final IDiagramQueryService diagramQueryService;

    public GeneralViewManageVisibilityRevealValuedContentHandler(IDiagramQueryService diagramQueryService) {
        this.diagramQueryService = Objects.requireNonNull(diagramQueryService);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, IDiagramContext diagramContext, IDiagramElement diagramElement, String actionId) {
        return actionId.equals(GeneralViewManageVisibilityRevealValuedContentAction.ACTION_ID);
    }

    @Override
    public IStatus handle(IEditingContext editingContext, IDiagramContext diagramContext, IDiagramElement diagramElement, String actionId) {
        Optional<Node> optionalNode = this.diagramQueryService.findNodeById(diagramContext.getDiagram(), diagramElement.getId());
        Set<String> nodesToReveal = new HashSet<>();
        Set<String> nodesToHide = new HashSet<>();
        if (optionalNode.isPresent()) {
            optionalNode.get().getChildNodes().forEach(node -> {
                if (node.getChildNodes().isEmpty()) {
                    nodesToHide.add(node.getId());
                } else {
                    nodesToReveal.add(node.getId());
                }
            });
            diagramContext.getDiagramEvents().add(new HideDiagramElementEvent(nodesToReveal, false));
            diagramContext.getDiagramEvents().add(new HideDiagramElementEvent(nodesToHide, true));
        }
        return new Success();
    }

}
