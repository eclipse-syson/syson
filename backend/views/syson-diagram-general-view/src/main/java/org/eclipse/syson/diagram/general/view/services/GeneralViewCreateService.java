/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewCreationRequest;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.GeneralViewEmptyDiagramNodeDescriptionProvider;
import org.eclipse.syson.services.ElementInitializerSwitch;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;

/**
 * Creation-related Java services used by the {@link GeneralViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class GeneralViewCreateService {

    private final IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService;

    private final ElementInitializerSwitch elementInitializerSwitch;

    public GeneralViewCreateService(IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService) {
        this.viewRepresentationDescriptionSearchService = Objects.requireNonNull(viewRepresentationDescriptionSearchService);
        this.elementInitializerSwitch = new ElementInitializerSwitch();
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
     * Create a {@link PartUsage} under the given {@link PartUsage}.
     *
     * @param partUsage
     *            the {@link PartUsage} on which we want to create a nested {@link PartUsage}.
     * @return the created element.
     */
    public PartUsage createNestedPartUsage(PartUsage partUsage) {
        PartUsage newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
        newPartUsage.setDeclaredName("part");
        FeatureMembership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        membership.getOwnedRelatedElement().add(newPartUsage);
        partUsage.getOwnedRelationship().add(membership);
        return newPartUsage;
    }

    /**
     * Check if the diagram associated to the given {@link IDiagramContext} contains nodes.
     *
     * @param element
     *            the element on which this service has been called.
     * @param editingContext
     *            the {@link IEditingContext} retrieved from the Variable Manager.
     * @param diagramContext
     *            the {@link IDiagramContext} retrieved from the Variable Manager.
     * @param previousDiagram
     *            the previous {@link Diagram} retrieved from the Variable Manager.
     * @return the given {@link Element} if the diagram is empty, <code>null</code> otherwise.
     */
    public Element getDiagramEmptyCandidate(Element element, IEditingContext editingContext, IDiagramContext diagramContext, Diagram previousDiagram) {
        boolean emptyDiagram = false;
        if (previousDiagram != null && diagramContext != null) {
            List<Node> previousNodes = previousDiagram.getNodes();
            List<ViewCreationRequest> viewCreationRequests = diagramContext.getViewCreationRequests();
            if (viewCreationRequests.isEmpty() && (previousNodes.isEmpty() || previousNodes.stream().anyMatch(node -> this.viewRepresentationDescriptionSearchService
                    .findViewNodeDescriptionById(editingContext, node.getDescriptionId()).stream().anyMatch(nd -> GeneralViewEmptyDiagramNodeDescriptionProvider.NAME.equals(nd.getName()))))) {
                emptyDiagram = true;
            }
        } else {
            emptyDiagram = true;
        }
        if (emptyDiagram) {
            return element;
        }
        return null;
    }
}
