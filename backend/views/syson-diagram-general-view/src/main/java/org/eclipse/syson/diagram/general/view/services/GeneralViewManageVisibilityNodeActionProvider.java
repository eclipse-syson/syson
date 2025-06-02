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
package org.eclipse.syson.diagram.general.view.services;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.collaborative.diagrams.api.IActionsProvider;
import org.eclipse.sirius.components.collaborative.diagrams.dto.Action;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.diagrams.IDiagramElement;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.DiagramDescription;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Node action to open the manage visibility modal.
 *
 * @author mcharfadi
 */
@Service
public class GeneralViewManageVisibilityNodeActionProvider implements IActionsProvider {

    private static final String ACTION_ID = "siriusweb_manage_visibility";

    private final IObjectService objectService;

    private final IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService;

    private final IDiagramIdProvider idProvider;

    public GeneralViewManageVisibilityNodeActionProvider(IObjectService objectService, IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService, IDiagramIdProvider idProvider) {
        this.objectService = Objects.requireNonNull(objectService);
        this.viewRepresentationDescriptionSearchService = Objects.requireNonNull(viewRepresentationDescriptionSearchService);
        this.idProvider = Objects.requireNonNull(idProvider);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canHandle(IEditingContext editingContext, DiagramDescription diagramDescription, IDiagramElement diagramElement) {
        if (diagramElement instanceof Node node && !node.getChildNodes().isEmpty()) {
            var semanticObject = objectService.getObject(editingContext, node.getTargetObjectId());
            var nodeDescriptionId = node.getDescriptionId();
            var optionalNodeDescription = diagramDescription.getNodeDescriptions().stream()
                    .filter(nd -> nd.getId().equals(nodeDescriptionId))
                    .findFirst();

            if (optionalNodeDescription.isPresent() && semanticObject.isPresent() && semanticObject.get() instanceof EObject eObject) {
                if (eObject instanceof Definition || eObject instanceof Usage) {
                    var viewDiagramDescription = this.viewRepresentationDescriptionSearchService.findById(editingContext, diagramDescription.getId());
                    if (viewDiagramDescription.isPresent() && viewDiagramDescription.get().getName().equals(GeneralViewDiagramDescriptionProvider.DESCRIPTION_NAME)) {
                        return isNodeDescriptionSimpleNode(viewDiagramDescription.get(), nodeDescriptionId, eObject.eClass());
                    }
                }
            }
        }

        return false;
    }

    @Override
    public List<Action> handle(IEditingContext editingContext, DiagramDescription diagramDescription, IDiagramElement diagramElement) {
        return List.of(new Action(ACTION_ID, List.of(), ""));
    }

    private boolean isNodeDescriptionSimpleNode(org.eclipse.sirius.components.view.RepresentationDescription viewDiagramDescription, String nodeDescriptionId, EClass eClass) {
        var optionalViewNodeDescription = EMFUtils.eAllContentStreamWithSelf(viewDiagramDescription)
                .filter(org.eclipse.sirius.components.view.diagram.NodeDescription.class::isInstance)
                .map(org.eclipse.sirius.components.view.diagram.NodeDescription.class::cast)
                .filter(nodeDesc -> this.idProvider.getId(nodeDesc).equals(nodeDescriptionId))
                .findFirst();

        if (optionalViewNodeDescription.isPresent()) {
            var viewNodeName = optionalViewNodeDescription.get().getName();
            var generatedNodeName = new GVDescriptionNameGenerator().getNodeName(eClass);
            return Objects.equals(viewNodeName, generatedNodeName);
        }
        return false;
    }
}