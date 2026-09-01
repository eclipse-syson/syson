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
import org.eclipse.sirius.components.collaborative.diagrams.dto.SingleClickOnDiagramElementTool;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IReadOnlyObjectPredicate;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.description.IDiagramElementDescription;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewDiagramDescriptionSearchService;
import org.eclipse.sirius.components.palette.dto.ITool;
import org.eclipse.sirius.components.palette.dto.ToolSection;
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

    private static final String INHERITED_BORDER_NODE_DESCRIPTION_NAME_FRAGMENT = "InheritedBorderNode";

    private static final String INHERITED_COMPARTMENT_ITEM_DESCRIPTION_NAME_FRAGMENT = "InheritedCompartmentItem";

    private static final String NEW_EXPRESSION_TOOL_ID = "tool_new_expression";

    private static final String NEW_EXPRESSION_TOOL_LABEL = "New expression";

    private static final String EDIT_EXPRESSION_TOOL_ID = "tool_edit_expression";

    private static final String EDIT_EXPRESSION_TOOL_LABEL = "Edit expression";

    private static final String DELETE_EXPRESSION_TOOL_ID = "tool_delete_expression";

    private static final String DELETE_EXPRESSION_TOOL_LABEL = "Delete expression";

    private static final String EXPRESSION_TOOL_SECTION_ID = "edit-section";

    private static final String EXPRESSION_TOOL_SECTION_LABEL = "Edit";

    private final IObjectSearchService objectSearchService;

    private final IReadOnlyObjectPredicate readOnlyObjectPredicate;

    private final IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService;

    private final MetamodelQueryElementService metamodelQueryElementService;

    /**
     * Creates the provider.
     *
     * @param objectSearchService
     *            the service used to retrieve diagram semantic targets
     * @param readOnlyObjectPredicate
     *            the predicate used to identify read-only semantic targets
     * @param viewDiagramDescriptionSearchService
     *            the service used to resolve runtime descriptions to their View descriptions
     */
    public ExpressionsPaletteToolsProvider(IObjectSearchService objectSearchService, IReadOnlyObjectPredicate readOnlyObjectPredicate,
            IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.readOnlyObjectPredicate = Objects.requireNonNull(readOnlyObjectPredicate);
        this.viewDiagramDescriptionSearchService = Objects.requireNonNull(viewDiagramDescriptionSearchService);
        this.metamodelQueryElementService = new MetamodelQueryElementService();
    }

    /**
     * Contributes expression tools only when the selected diagram element is not inherited content.
     *
     * @param editingContext
     *            the current editing context
     * @param diagramContext
     *            the current diagram context
     * @param diagramElementDescription
     *            the selected diagram element description
     * @param diagramElement
     *            the selected diagram element
     * @return the expression tool section, or no section when no expression action applies
     */
    @Override
    public List<ToolSection> createExtraToolSections(IEditingContext editingContext, DiagramContext diagramContext, Object diagramElementDescription, Object diagramElement) {
        var tools = new ArrayList<ITool>();

        var optionalTargetObjectId = this.getTargetObjectId(diagramElement);

        if (optionalTargetObjectId.isPresent() && diagramElementDescription instanceof IDiagramElementDescription nodeDescription
                && !this.isInheritedNodeDescription(editingContext, nodeDescription)) {
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

    /**
     * Indicates whether the description represents inherited content, which must not expose expression mutation
     * tools.
     *
     * @param editingContext
     *            the current editing context
     * @param diagramElementDescription
     *            the description of the selected diagram element
     * @return {@code true} when the description represents an inherited border node or compartment item
     */
    private boolean isInheritedNodeDescription(IEditingContext editingContext, IDiagramElementDescription diagramElementDescription) {
        if (diagramElementDescription instanceof NodeDescription nodeDescription) {
            return this.viewDiagramDescriptionSearchService.findViewNodeDescriptionById(editingContext, nodeDescription.getId())
                    .map(org.eclipse.sirius.components.view.diagram.NodeDescription::getName)
                    .map(descriptionName -> descriptionName.contains(INHERITED_BORDER_NODE_DESCRIPTION_NAME_FRAGMENT)
                            || descriptionName.contains(INHERITED_COMPARTMENT_ITEM_DESCRIPTION_NAME_FRAGMENT))
                    .orElse(false);
        }
        return false;
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
