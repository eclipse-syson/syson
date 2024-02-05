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
package org.eclipse.syson.diagram.general.view.edges;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IEdgeDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.EdgePalette;
import org.eclipse.sirius.components.view.diagram.EdgeReconnectionTool;

/**
 * Common pieces of edge descriptions shared by {@link IEdgeDescriptionProvider} in General View.
 *
 * @author arichard
 */
public abstract class AbstractEdgeDescriptionProvider implements IEdgeDescriptionProvider {

    protected final ViewBuilders viewBuilderHelper = new ViewBuilders();

    protected final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    protected IColorProvider colorProvider;

    public AbstractEdgeDescriptionProvider(IColorProvider colorProvider) {
        this.colorProvider = Objects.requireNonNull(colorProvider);
    }

    protected EdgePalette createEdgePalette(List<EdgeReconnectionTool> edgeReconnectionTools) {
        var changeContext = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.deleteFromModel()");

        var deleteTool = this.diagramBuilderHelper.newDeleteTool()
                .name("Delete from Model")
                .body(changeContext.build());

        return this.diagramBuilderHelper
                .newEdgePalette()
                .deleteTool(deleteTool.build())
                .edgeReconnectionTools(edgeReconnectionTools.toArray(EdgeReconnectionTool[]::new))
                .build();
    }
}
