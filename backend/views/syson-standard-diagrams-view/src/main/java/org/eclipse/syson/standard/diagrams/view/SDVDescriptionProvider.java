/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
package org.eclipse.syson.standard.diagrams.view;

import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.syson.common.view.api.IViewDescriptionProvider;
import org.springframework.stereotype.Service;

/**
 * Allows to register the Standard Diagram View diagram in the application.
 *
 * @author arichard
 */
@Service
public class SDVDescriptionProvider implements IViewDescriptionProvider {

    /**
     * We can't change this Id to "StandardDiagramView" because it would change all Diagram, Nodes and Edges
     * Descriptions Ids.
     */
    @Override
    public String getViewId() {
        return "GeneralViewDiagram";
    }

    @Override
    public IRepresentationDescriptionProvider getRepresentationDescriptionProvider() {
        return new SDVDiagramDescriptionProvider();
    }
}
