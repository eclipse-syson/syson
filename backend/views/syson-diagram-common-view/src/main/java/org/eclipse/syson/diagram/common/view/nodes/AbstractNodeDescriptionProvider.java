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
package org.eclipse.syson.diagram.common.view.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.UserColor;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.ImageNodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.provider.DefaultToolsFactory;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Common pieces of node descriptions shared by {@link INodeDescriptionProvider} in all diagram View.
 *
 * @author arichard
 */
public abstract class AbstractNodeDescriptionProvider implements INodeDescriptionProvider {

    protected final ViewBuilders viewBuilderHelper = new ViewBuilders();

    protected final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    protected final IColorProvider colorProvider;

    protected final DefaultToolsFactory defaultToolsFactory = new DefaultToolsFactory();

    public AbstractNodeDescriptionProvider(IColorProvider colorProvider) {
        this.colorProvider = Objects.requireNonNull(colorProvider);
    }

    protected void orderToolSectionsTools(List<NodeToolSection> toolSections) {
        toolSections.forEach(toolSection -> {
            // EList cannot be sorted
            EList<NodeTool> nodeTools = toolSection.getNodeTools();
            List<NodeTool> sortedNodeTools = new ArrayList<>(nodeTools);
            sortedNodeTools.sort((nt1, nt2) -> nt1.getName().compareTo(nt2.getName()));
            toolSection.getNodeTools().clear();
            toolSection.getNodeTools().addAll(sortedNodeTools);
        });
    }

    protected ImageNodeStyleDescription createImageNodeStyleDescription(String shapeId) {
        return this.createImageNodeStyleDescription(shapeId, this.colorProvider.getColor("transparent"), false);
    }

    protected ImageNodeStyleDescription createImageNodeStyleDescription(String imagePath, UserColor borderColor, boolean isRotable) {
        return this.diagramBuilderHelper.newImageNodeStyleDescription()
                .borderColor(borderColor)
                .borderRadius(0)
                .positionDependentRotation(isRotable)
                .shape(imagePath)
                .build();
    }

    protected NodeTool getDeleteFromDiagramTool() {
        return this.diagramBuilderHelper.newNodeTool()
                .name("Delete from Diagram")
                .iconURLsExpression("/images/graphicalDelete.svg")
                .body(this.diagramBuilderHelper.newDeleteView()
                        .children(this.viewBuilderHelper.newChangeContext()
                                .expression(
                                        ServiceMethod.of3(DiagramMutationAQLService::removeFromExposedElements).aqlSelf(Node.SELECTED_NODE, IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT))
                                .build())
                        .build())
                .build();
    }
}
