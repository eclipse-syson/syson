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
package org.eclipse.syson.application.expressions.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ITool;
import org.eclipse.sirius.components.collaborative.diagrams.dto.SingleClickOnDiagramElementTool;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolSection;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IReadOnlyObjectPredicate;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.IDiagramElementDescription;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.view.emf.diagram.api.IPaletteToolsProvider;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.springframework.stereotype.Service;

/**
 * Contribute tools to manipulate {@link Expression} to specific elements's diagram palette:
 * <ul>
 * <li>"New expression" on diagram elements which represent SysML elements that can contain a new
 * {@link Expression}.</li>
 * <li>"Edit expression" on diagram elements which represent an {@link Expression} or an element which contains a single
 * (non-ambiguous) one.</li>
 * <li>"Delete expression" on diagram elements which contain a single (non-ambiguous) expression (on actual expression
 * element, the plain Delete is enough).</li>
 * </ul>
 *
 * @author pcdavid
 */
@Service
public class ExpressionsPaletteToolsProvider implements IPaletteToolsProvider {

    private static final String NEW_EXPRESSION_TOOL_ID = "tool_new_expression";

    private static final String NEW_EXPRESSION_TOOL_LABEL = "New expression";

    private static final String EDIT_EXPRESSION_TOOL_ID = "tool_edit_expression";

    private static final String EDIT_EXPRESSION_TOOL_LABEL = "Edit expression";

    private static final String DELETE_EXPRESSION_TOOL_ID = "tool_delete_expression";

    private static final String DELETE_EXPRESSION_TOOL_LABEL = "Delete expression";

    private static final String EXPRESSION_TOOL_SECTION_ID = "toolSection_expression";

    private static final String EXPRESSION_TOOL_SECTION_LABEL = "Expression";

    private final IObjectSearchService objectSearchService;

    private final IReadOnlyObjectPredicate readOnlyObjectPredicate;

    private final MetamodelQueryElementService metamodelQueryElementService;

    public ExpressionsPaletteToolsProvider(IObjectSearchService objectSearchService, IReadOnlyObjectPredicate readOnlyObjectPredicate) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.readOnlyObjectPredicate = Objects.requireNonNull(readOnlyObjectPredicate);
        this.metamodelQueryElementService = new MetamodelQueryElementService();
    }

    @Override
    public List<ToolSection> createExtraToolSections(IEditingContext editingContext, DiagramContext diagramContext, Object diagramElementDescription, Object diagramElement) {
        var tools = new ArrayList<ITool>();

        var optionalTargetObjectId = this.getTargetObjectId(diagramElement);

        if (optionalTargetObjectId.isPresent() && diagramElementDescription instanceof IDiagramElementDescription nodeDescription) {
            var semanticObject = this.objectSearchService.getObject(editingContext, optionalTargetObjectId.get());
            if (semanticObject.isPresent() && semanticObject.get() instanceof Element element) {
                if (editingContext instanceof IEMFEditingContext emfEditingContext && this.canHaveNewExpression(emfEditingContext, element)) {
                    tools.add(new SingleClickOnDiagramElementTool(NEW_EXPRESSION_TOOL_ID, NEW_EXPRESSION_TOOL_LABEL, List.of(), List.of(nodeDescription), "", false, false, List.of()));
                }
                if (this.canEditExpression(element)) {
                    tools.add(new SingleClickOnDiagramElementTool(EDIT_EXPRESSION_TOOL_ID, EDIT_EXPRESSION_TOOL_LABEL, List.of(), List.of(nodeDescription), "", false, false, List.of()));
                    if (!this.metamodelQueryElementService.isTopLevelExpression(element)) {
                        tools.add(new SingleClickOnDiagramElementTool(DELETE_EXPRESSION_TOOL_ID, DELETE_EXPRESSION_TOOL_LABEL, List.of(), List.of(nodeDescription), "", false, false, List.of()));
                    }
                }
            }
        }

        if (!tools.isEmpty()) {
            return List.of(new ToolSection(EXPRESSION_TOOL_SECTION_ID, EXPRESSION_TOOL_SECTION_LABEL, List.of(), tools));
        } else {
            return List.of();
        }
    }

    private Optional<String> getTargetObjectId(Object diagramElement) {
        Optional<String> result = Optional.empty();
        if (diagramElement instanceof Node node) {
            result = Optional.of(node.getTargetObjectId());
        } else if (diagramElement instanceof Edge edge) {
            result = Optional.of(edge.getTargetObjectId());
        }
        return result;
    }

    private boolean canHaveNewExpression(IEMFEditingContext editingContext, Element element) {
        return !this.readOnlyObjectPredicate.test(element) && this.metamodelQueryElementService.canContainExpressionDefinition(element)
                && !this.metamodelQueryElementService.hasSingleExpressionDefinition(element);
    }

    private boolean canEditExpression(Element element) {
        return this.metamodelQueryElementService.isTopLevelExpression(element) || (this.metamodelQueryElementService.hasSingleExpressionDefinition(element)
                && !this.metamodelQueryElementService.hasSingleExpressionDefinition(element.getOwner()));
    }

    @Override
    public List<ITool> createQuickAccessTools(IEditingContext editingContext, DiagramContext diagramContext, Object diagramElementDescription, Object diagramElement) {
        return List.of();
    }

}
