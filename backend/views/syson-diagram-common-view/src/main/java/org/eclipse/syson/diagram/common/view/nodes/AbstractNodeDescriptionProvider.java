/*******************************************************************************
 * Copyright (c) 2023, 2026 Obeo.
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
import java.util.Map;
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
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.diagram.services.aql.DiagramQueryAQLService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.StandardDiagramsConstants;

/**
 * Common pieces of node descriptions shared by {@link INodeDescriptionProvider} in all diagram View.
 *
 * @author arichard
 */
public abstract class AbstractNodeDescriptionProvider implements INodeDescriptionProvider {

    protected final ViewBuilders viewBuilderHelper = new ViewBuilders();

    protected final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    protected final DefaultToolsFactory defaultToolsFactory = new DefaultToolsFactory();

    protected final IColorProvider colorProvider;

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
                                        ServiceMethod.of3(DiagramMutationAQLService::removeFromExposedElements).aqlSelf(Node.SELECTED_NODE, IEditingContext.EDITING_CONTEXT,
                                                DiagramContext.DIAGRAM_CONTEXT))
                                .build())
                        .build())
                .build();
    }

    protected NodeTool getDuplicateElementAndNodeTool() {
        return this.diagramBuilderHelper.newNodeTool()
                .name("Duplicate Element")
                .iconURLsExpression("/images/content_copy.svg")
                .keyBindings(this.viewBuilderHelper.newKeyBinding().ctrl(true).key("d").build())
                .preconditionExpression(AQLConstants.AQL + "self.oclIsKindOf(sysml::Element) and not self.oclIsKindOf(sysml::Relationship)")
                .body(this.viewBuilderHelper.newChangeContext()
                        .expression(
                                ServiceMethod.of4(DiagramMutationAQLService.class, DiagramMutationAQLService::duplicateElementAndExpose, Element.class, IEditingContext.class,
                                                DiagramContext.class, List.class, Map.class)
                                        .aqlSelf(IEditingContext.EDITING_CONTEXT,
                                                DiagramContext.DIAGRAM_CONTEXT,
                                                "Sequence{selectedNode}",
                                                ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE))
                        .build())
                .build();
    }

    protected NodeTool getDuplicateElementsAndNodesTool() {
        return this.diagramBuilderHelper.newNodeTool()
                .name("Duplicate Element")
                .iconURLsExpression("/images/content_copy.svg")
                .keyBindings(this.viewBuilderHelper.newKeyBinding().ctrl(true).key("d").build())
                .preconditionExpression(AQLConstants.AQL
                        + "selectedNodes->notEmpty() and selectedEdges->isEmpty() and self->forAll(e | e.oclIsKindOf(sysml::Element) and not e.oclIsKindOf(sysml::Relationship))")
                .body(this.viewBuilderHelper.newChangeContext()
                        .expression(
                                ServiceMethod.of4(DiagramMutationAQLService.class, DiagramMutationAQLService::duplicateElementAndExpose, Element.class, IEditingContext.class,
                                                DiagramContext.class, List.class, Map.class)
                                        .aqlSelf(IEditingContext.EDITING_CONTEXT,
                                                DiagramContext.DIAGRAM_CONTEXT,
                                                "selectedNodes",
                                                ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE))
                        .build())
                .build();
    }

    protected NodeTool getShowContentAsNestedTool() {
        return this.diagramBuilderHelper.newNodeTool()
                .name("Show content as Nested")
                .iconURLsExpression("/icons/full/obj16/ShowTool.svg")
                .preconditionExpression(ServiceMethod.of4(DiagramQueryAQLService.class, DiagramQueryAQLService::isView,
                        Element.class, String.class, Node.class, IEditingContext.class, DiagramContext.class)
                        .aqlSelf(AQLUtils.aqlString(StandardDiagramsConstants.GV_QN), Node.SELECTED_NODE, IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT))
                .body(this.diagramBuilderHelper.newDeleteView()
                        .children(this.viewBuilderHelper.newChangeContext()
                                .expression(
                                        ServiceMethod.of3(DiagramMutationAQLService::showContentAsNested).aqlSelf(Node.SELECTED_NODE, IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT))
                                .build())
                        .build())
                .build();
    }

    protected NodeTool getShowContentAsTreeTool() {
        return this.diagramBuilderHelper.newNodeTool()
                .name("Show content as Tree")
                .iconURLsExpression("/icons/full/obj16/ShowTool.svg")
                .preconditionExpression(ServiceMethod.of4(DiagramQueryAQLService.class, DiagramQueryAQLService::isView,
                        Element.class, String.class, Node.class, IEditingContext.class, DiagramContext.class)
                        .aqlSelf(AQLUtils.aqlString(StandardDiagramsConstants.GV_QN), Node.SELECTED_NODE, IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT))
                .body(this.diagramBuilderHelper.newDeleteView()
                        .children(this.viewBuilderHelper.newChangeContext()
                                .expression(
                                        ServiceMethod.of3(DiagramMutationAQLService::showContentAsTree).aqlSelf(Node.SELECTED_NODE, IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT))
                                .build())
                        .build())
                .build();
    }
}
