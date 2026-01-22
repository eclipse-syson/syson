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
package org.eclipse.syson.standard.diagrams.view.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramEventConsumer;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DropOnDiagramInput;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.ViewCreationRequest;
import org.eclipse.sirius.components.diagrams.ViewDeletionRequest;
import org.eclipse.sirius.components.diagrams.components.NodeContainmentKind;
import org.eclipse.sirius.components.diagrams.components.NodeIdProvider;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.diagrams.events.HideDiagramElementEvent;
import org.eclipse.sirius.components.diagrams.events.IDiagramEvent;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramConversionData;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.syson.diagram.services.DiagramQueryElementService;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.springframework.stereotype.Service;

/**
 * Service that will be executed after a DropOnDiagramInput in order to hide compartments.
 * If it's an interconnection compartment then we keep the default behavior of {@link org.eclipse.syson.diagram.common.view.services.ViewNodeService#isHiddenByDefault(Element, String)}
 *
 * @author mcharfadi
 */
@Service
public class DiagramMutationDropDefaultVisibility implements IDiagramEventConsumer {

    private final DiagramQueryElementService diagramQueryElementService;

    private final IObjectSearchService objectSearchService;

    public DiagramMutationDropDefaultVisibility(DiagramQueryElementService diagramQueryElementService, IObjectSearchService objectSearchService) {
        this.diagramQueryElementService = diagramQueryElementService;
        this.objectSearchService = objectSearchService;
    }

    @Override
    public void accept(IEditingContext editingContext, Diagram previousDiagram, List<IDiagramEvent> diagramEvents, List<ViewDeletionRequest> viewDeletionRequests, List<ViewCreationRequest> viewCreationRequests, ChangeDescription changeDescription) {
        if (changeDescription.getInput() instanceof DropOnDiagramInput input && this.isStandardDiagram(previousDiagram) && editingContext instanceof EditingContext siriusEditingContext
                && siriusEditingContext.getViewConversionData().get(previousDiagram.getDescriptionId()) instanceof ViewDiagramConversionData viewDiagramConversionData) {

            IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();
            var interconnectionCompartmentName = descriptionNameGenerator.getFreeFormCompartmentName("interconnection");
            var interConnectionCompartment = viewDiagramConversionData.convertedNodes().entrySet().stream()
                .filter(nodeDescriptionNodeDescriptionEntry -> nodeDescriptionNodeDescriptionEntry.getKey().getName().equals(interconnectionCompartmentName))
                .map(Map.Entry::getValue)
                .map(NodeDescription::getId)
                .findFirst();

            Set<String> compartmentsToHide = new HashSet<>();
            input.objectIds().forEach(objectId -> {
                var semanticParent = this.objectSearchService.getObject(editingContext, objectId);
                if (semanticParent.isPresent() && semanticParent.get() instanceof Element element) {
                    var optionalParentNodeDescriptionId = this.diagramQueryElementService.getNodeDescriptionId(element, previousDiagram, editingContext);
                    if (optionalParentNodeDescriptionId.isPresent()) {
                        var parentNodeId = new NodeIdProvider().getNodeId(previousDiagram.getId(),
                                optionalParentNodeDescriptionId.get(),
                                NodeContainmentKind.CHILD_NODE,
                                objectId);

                        var parentNodeDescription = viewDiagramConversionData.convertedNodes().values().stream()
                                .filter(nodeDescription -> nodeDescription.getId().equals(optionalParentNodeDescriptionId.get()))
                                .findFirst();

                        parentNodeDescription.ifPresent(nodeDescription -> nodeDescription.getReusedChildNodeDescriptionIds().stream()
                                .filter(reusedChildNodeDescriptionId -> interConnectionCompartment.isEmpty() || !interConnectionCompartment.get().equals(reusedChildNodeDescriptionId))
                                .forEach(reusedChildNodeDescriptionId -> {
                                    var containerNodeToHideId = new NodeIdProvider().getNodeId(parentNodeId,
                                            reusedChildNodeDescriptionId,
                                            NodeContainmentKind.CHILD_NODE,
                                            objectId);
                                    compartmentsToHide.add(containerNodeToHideId);
                                }));

                        diagramEvents.add(new HideDiagramElementEvent(compartmentsToHide, true));
                    }
                }
            });
        }

    }

    private boolean isStandardDiagram(Diagram diagram) {
        return diagram != null && Objects.equals(SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID, diagram.getDescriptionId());
    }
}
