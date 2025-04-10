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
package org.eclipse.syson.diagram.common.view.edges;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ChangeContextBuilder;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IEdgeDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.DeleteTool;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgePalette;
import org.eclipse.sirius.components.view.diagram.EdgeReconnectionTool;
import org.eclipse.sirius.components.view.diagram.LabelEditTool;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SourceEdgeEndReconnectionTool;
import org.eclipse.sirius.components.view.diagram.TargetEdgeEndReconnectionTool;
import org.eclipse.sirius.components.view.diagram.provider.DefaultToolsFactory;
import org.eclipse.syson.util.AQLUtils;

/**
 * Common pieces of edge descriptions shared by {@link IEdgeDescriptionProvider} in all view.
 *
 * @author arichard
 */
public abstract class AbstractEdgeDescriptionProvider implements IEdgeDescriptionProvider {

    protected final ViewBuilders viewBuilderHelper = new ViewBuilders();

    protected final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    protected final DefaultToolsFactory defaultToolsFactory = new DefaultToolsFactory();

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
     * Implementers should provide the list of {@link NodeTool} (without ToolSection) for this {@link EdgeDescription}.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the list of {@link NodeTool} of this edge.
     */
    protected List<NodeTool> getToolsWithoutSection(IViewDiagramElementFinder cache) {
        return new ArrayList<>();
    };

    /**
     * Implementers may provide the precondition expression which is verified when a reconnection of the source end of
     * the edge is performed.
     *
     * @return the {@link String} corresponding to the precondition expression to check when a reconnection of the
     *         source end of the edge occurs.
     */
    protected String getSourceReconnectToolPreconditionExpression() {
        return null;
    };

    /**
     * Implementers may provide the precondition expression which is verified when a reconnection of the target end of the edge is performed.
     * @return the {@link String} corresponding to the precondition expression to check when a reconnection of the target end of the edge occurs.
     */
    protected String getTargetReconnectToolPreconditionExpression() {
        return null;
    };

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

    /**
     * Implementers can override this method to provide a specific delete tool to apply on the edge.
     * <p>
     * This method provides a default implementation that deletes the selected edge.
     * </p>
     *
     * @return the delete tool to use for the edge
     */
    protected DeleteTool getEdgeDeleteTool() {
        var changeContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("deleteFromModel"));

        return this.diagramBuilderHelper.newDeleteTool()
                .name("Delete from Model")
                .body(changeContext.build())
                .build();
    }

    protected EdgePalette createEdgePalette(IViewDiagramElementFinder cache) {
        List<EdgeReconnectionTool> reconnectTools = List.of(this.createSourceReconnectTool(), this.createTargetReconnectTool());
        var edgeBuilder = this.diagramBuilderHelper
                .newEdgePalette()
                .edgeReconnectionTools(reconnectTools.toArray(EdgeReconnectionTool[]::new));

        if (this.isDeletable()) {
            edgeBuilder.deleteTool(this.getEdgeDeleteTool());
        }

        var centerLabelEditTool = this.getEdgeEditTool();
        if (centerLabelEditTool != null) {
            edgeBuilder.centerLabelEditTool(centerLabelEditTool);
        }

        edgeBuilder.toolSections(this.defaultToolsFactory.createDefaultHideRevealEdgeToolSection());

        var toolsWithoutSection = new ArrayList<NodeTool>();
        toolsWithoutSection.addAll(this.getToolsWithoutSection(cache));

        edgeBuilder.nodeTools(toolsWithoutSection.toArray(NodeTool[]::new));

        return edgeBuilder.build();
    }

    private SourceEdgeEndReconnectionTool createSourceReconnectTool() {
        var builder = this.diagramBuilderHelper.newSourceEdgeEndReconnectionTool();

        return builder
                .name("Reconnect Source")
                .body(this.getSourceReconnectToolBody().build())
                .preconditionExpression(this.getSourceReconnectToolPreconditionExpression())
                .build();
    }

    private TargetEdgeEndReconnectionTool createTargetReconnectTool() {
        var builder = this.diagramBuilderHelper.newTargetEdgeEndReconnectionTool();

        return builder
                .name("Reconnect Target")
                .body(this.getTargetReconnectToolBody().build())
                .preconditionExpression(this.getTargetReconnectToolPreconditionExpression())
                .build();
    }
}
