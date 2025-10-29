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
package org.eclipse.syson.diagram.services.aql;

import java.util.Objects;

import org.eclipse.syson.diagram.services.DiagramMutationLabelService;
import org.eclipse.syson.sysml.Element;

/**
 * Entry point for all diagram-related services doing mutations in diagrams and called by AQL expressions in diagram
 * descriptions. This class is not a @Service class but act as it was, because it is called by IJavaServiceProvider.
 *
 * @author arichard
 */
public class DiagramMutationAQLService {

    private final DiagramMutationLabelService diagramMutationLabelService;

    public DiagramMutationAQLService(DiagramMutationLabelService diagramMutationLabelService) {
        this.diagramMutationLabelService = Objects.requireNonNull(diagramMutationLabelService);
    }

    /**
     * {@link DiagramMutationLabelService#directEdit(Element, String)}.
     */
    public Element directEdit(Element element, String newLabel) {
        return this.diagramMutationLabelService.directEdit(element, newLabel);
    }

    /**
     * {@link DiagramMutationLabelService#directEditNode(Element, String)}.
     */
    public Element directEditNode(Element element, String newLabel) {
        return this.diagramMutationLabelService.directEditNode(element, newLabel);
    }

    /**
     * {@link DiagramMutationLabelService#directEditListItem(Element, String)}.
     */
    public Element directEditListItem(Element element, String newLabel) {
        return this.diagramMutationLabelService.directEditListItem(element, newLabel);
    }

    /**
     * {@link DiagramMutationLabelService#editMultiplicityRangeCenterLabel(Element, String)}.
     */
    public Element editMultiplicityRangeCenterLabel(Element element, String newLabel) {
        return this.diagramMutationLabelService.editMultiplicityRangeCenterLabel(element, newLabel);
    }

    /**
     * {@link DiagramMutationLabelService#editEdgeCenterLabel(Element, String)}.
     */
    public Element editEdgeCenterLabel(Element element, String newLabel) {
        return this.diagramMutationLabelService.editEdgeCenterLabel(element, newLabel);
    }
}
