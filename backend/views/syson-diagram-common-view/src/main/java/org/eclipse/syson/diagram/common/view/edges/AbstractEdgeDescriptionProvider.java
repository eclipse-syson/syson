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
package org.eclipse.syson.diagram.common.view.edges;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.view.builder.generated.ChangeContextBuilder;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IEdgeDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.EdgePalette;
import org.eclipse.sirius.components.view.diagram.EdgeReconnectionTool;
import org.eclipse.sirius.components.view.diagram.LabelEditTool;
import org.eclipse.sirius.components.view.diagram.SourceEdgeEndReconnectionTool;
import org.eclipse.sirius.components.view.diagram.TargetEdgeEndReconnectionTool;

/**
 * Common pieces of edge descriptions shared by {@link IEdgeDescriptionProvider} in all view.
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

    /**
     * Implementers should provide the operation which is executed when a reconnection of the source end of the edge is
     * performed.
     * 
     * @return the {@link ChangeContextBuilder} corresponding to the operation to perform when a reconnection of the
     *         source end of the edge occurs.
     */
    protected abstract ChangeContextBuilder getSourceReconnectToolBody();

    /**
     * Implementers should provide the operation which is executed when a reconnection of the target end of the edge is
     * performed.
     * 
     * @return the {@link ChangeContextBuilder} corresponding to the operation to perform when a reconnection of the
     *         target end of the edge occurs.
     */
    protected abstract ChangeContextBuilder getTargetReconnectToolBody();

    /**
     * Implementers can override this method to disable the delete tool on specific edge description.
     * 
     * @return <code>true</code> if the edge can be deleted and <code>false</code> otherwise.
     */
    protected boolean isDeletable() {
        return true;
    }

    /**
     * Implementers can override this method to provide a specific label edit tool to apply on the edge.<br>
     * By default no edit tool is provided.
     * 
     * @return a label edit tool or <code>null</code> if no edit tool needed.
     */
    protected LabelEditTool getEdgeEditTool() {
        return null;
    }

    protected EdgePalette createEdgePalette() {
        var changeContext = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.deleteFromModel()");

        var deleteTool = this.diagramBuilderHelper.newDeleteTool()
                .name("Delete from Model")
                .body(changeContext.build());

        List<EdgeReconnectionTool> reconnectTools = List.of(this.createSourceReconnectTool(), this.createTargetReconnectTool());
        var edgeBuilder = this.diagramBuilderHelper
                .newEdgePalette()
                .edgeReconnectionTools(reconnectTools.toArray(EdgeReconnectionTool[]::new));

        if (this.isDeletable()) {
            edgeBuilder.deleteTool(deleteTool.build());
        }

        var centerLabelEditTool = this.getEdgeEditTool();
        if (centerLabelEditTool != null) {
            edgeBuilder.centerLabelEditTool(centerLabelEditTool);
        }

        return edgeBuilder.build();
    }

    private SourceEdgeEndReconnectionTool createSourceReconnectTool() {
        var builder = this.diagramBuilderHelper.newSourceEdgeEndReconnectionTool();

        return builder
                .name("Reconnect Source")
                .body(this.getSourceReconnectToolBody().build())
                .build();
    }

    private TargetEdgeEndReconnectionTool createTargetReconnectTool() {
        var builder = this.diagramBuilderHelper.newTargetEdgeEndReconnectionTool();

        return builder
                .name("Reconnect Target")
                .body(this.getTargetReconnectToolBody().build())
                .build();
    }
}
