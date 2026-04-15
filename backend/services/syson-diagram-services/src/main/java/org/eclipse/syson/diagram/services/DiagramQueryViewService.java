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
package org.eclipse.syson.diagram.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.util.NodeFinder;
import org.eclipse.syson.util.StandardDiagramsConstants;
import org.springframework.stereotype.Service;

/**
 * Query services related to diagram view kinds.
 *
 * @author arichard
 */
@Service
public class DiagramQueryViewService {

    private final IObjectSearchService objectSearchService;

    private final ElementUtil elementUtil;

    private final DiagramQueryExposeService diagramQueryExposeService;

    public DiagramQueryViewService(IObjectSearchService objectSearchService, DiagramQueryExposeService diagramQueryExposeService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.elementUtil = new ElementUtil();
        this.diagramQueryExposeService = Objects.requireNonNull(diagramQueryExposeService);
    }

    /**
     * Check if the given {@link Element} displayed in the given {@link DiagramContext} is of the given
     * {@link ViewDefinition}.
     */
    public boolean isView(Element element, String viewDefinition, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        boolean isView = false;
        if (element instanceof ViewUsage viewUsage) {
            var types = viewUsage.getType();
            if ((types == null || types.isEmpty()) && Objects.equals(StandardDiagramsConstants.GV_QN, viewDefinition)) {
                isView = true;
            } else {
                var expectedViewDefinition = this.elementUtil.findByNameAndType(viewUsage, viewDefinition, ViewDefinition.class);
                Type type = types.get(0);
                isView = Objects.equals(type, expectedViewDefinition);
            }
        } else {
            var viewUsageContainingElement = ancestors.stream().filter(ViewUsage.class::isInstance).map(ViewUsage.class::cast).findFirst();
            if (viewUsageContainingElement.isPresent()) {
                isView = this.isView(viewUsageContainingElement.get(), viewDefinition, ancestors, editingContext, diagramContext);
            }
        }
        return isView;
    }

    /**
     * Check if the given {@link Element} displayed in the given {@link DiagramContext} is of the given
     * {@link ViewDefinition}.
     */
    public boolean isView(Element element, String viewDefinition, Node selectedNode, IEditingContext editingContext, DiagramContext diagramContext) {
        boolean isView = false;
        if (element instanceof ViewUsage viewUsage) {
            var types = viewUsage.getType();
            if ((types == null || types.isEmpty()) && Objects.equals(StandardDiagramsConstants.GV_QN, viewDefinition)) {
                isView = true;
            } else {
                var expectedViewDefinition = this.elementUtil.findByNameAndType(viewUsage, viewDefinition, ViewDefinition.class);
                Type type = types.get(0);
                isView = Objects.equals(type, expectedViewDefinition);
            }
        } else {
            var ancestors = this.getAncestors(selectedNode, diagramContext.diagram(), editingContext);
            var viewUsageContainingElement = ancestors.stream().filter(ViewUsage.class::isInstance).map(ViewUsage.class::cast).findFirst();
            if (viewUsageContainingElement.isPresent()) {
                isView = this.isView(viewUsageContainingElement.get(), viewDefinition, selectedNode, editingContext, diagramContext);
            }
        }
        return isView;
    }

    /**
     * Check if the compartment associated to the given {@link Element} should be hidden by default.
     */
    public boolean isHiddenByDefault(Element self, String compartmentName, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        boolean isHiddenByDefault = true;
        if ("GV Compartment interconnection FreeForm".equals(compartmentName)) {
            var exposedParts = this.diagramQueryExposeService.getExposedElements(self, SysmlPackage.eINSTANCE.getPartUsage(), ancestors, editingContext, diagramContext);
            var exposedActions = this.diagramQueryExposeService.getExposedElements(self, SysmlPackage.eINSTANCE.getActionUsage(), ancestors, editingContext, diagramContext);
            var exposedSatisfyRequirements = this.diagramQueryExposeService.getExposedElements(self, SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), ancestors, editingContext, diagramContext);
            if (!exposedParts.isEmpty() || !exposedActions.isEmpty() || !exposedSatisfyRequirements.isEmpty()) {
                isHiddenByDefault = false;
            }
        }
        return isHiddenByDefault;
    }

    private List<Object> getAncestors(Node selectedNode, Diagram diagram, IEditingContext editingContext) {
        var ancestors = new ArrayList<>();
        var parent = new NodeFinder(diagram).getParent(selectedNode);
        if (parent instanceof Node parentNode) {
            var parentObject = this.objectSearchService.getObject(editingContext, parentNode.getTargetObjectId());
            if (parentObject.isPresent()) {
                ancestors.add(parentObject.get());
                ancestors.addAll(this.getAncestors(parentNode, diagram, editingContext));
            }
        } else if (parent instanceof Diagram) {
            var parentObject = this.objectSearchService.getObject(editingContext, diagram.getTargetObjectId());
            if (parentObject.isPresent()) {
                ancestors.add(parentObject.get());
            }
        }
        return ancestors;
    }
}
