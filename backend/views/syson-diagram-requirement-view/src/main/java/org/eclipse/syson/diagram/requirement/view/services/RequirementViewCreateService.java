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
package org.eclipse.syson.diagram.requirement.view.services;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.requirement.view.RequirementViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.requirement.view.nodes.RequirementViewEmptyDiagramNodeDescriptionProvider;
import org.eclipse.syson.sysml.Element;

/**
 * Creation-related Java services used by the {@link RequirementViewDiagramDescriptionProvider}.
 *
 * @author Jerome Gout
 */
public class RequirementViewCreateService extends ViewCreateService {

    public RequirementViewCreateService(IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService) {
        super(viewRepresentationDescriptionSearchService);
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
        return this.getDiagramEmptyCandidate(element, editingContext, diagramContext, previousDiagram, RequirementViewEmptyDiagramNodeDescriptionProvider.NAME);
    }
}
