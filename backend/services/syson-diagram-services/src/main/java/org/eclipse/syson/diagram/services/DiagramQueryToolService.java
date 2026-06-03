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

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.api.ViewDefinitionKind;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.util.StandardDiagramsConstants;
import org.springframework.stereotype.Service;

/**
 * Tool-related services doing queries for diagrams.
 *
 * @author arichard
 */
@Service
public class DiagramQueryToolService {

    private final IObjectSearchService objectSearchService;

    private final UtilService utilService;

    public DiagramQueryToolService(IObjectSearchService objectSearchService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.utilService = new UtilService();
    }

    /**
     * Tool precondition for control node actions (Start/Done/Decision/Fork/Join/Merge) invoked on diagram background.
     *
     * @param element
     *            the given {@link Element}.
     * @return <code>true</code> if the tool should be available, <code>false</code> otherwise.
     */
    public boolean isControlNodeActionCreationToolInsideActionOnAFV(Element element, IEditingContext editingContext, DiagramContext diagramContext) {
        if (this.isAFVDiagram(editingContext, diagramContext)) {
            var owner = this.utilService.getViewUsageOwner(element);
            return owner instanceof ActionUsage || owner instanceof ActionDefinition;
        }
        return false;
    }

    /**
     * Tool precondition for control node actions (Start/Done/Decision/Fork/Join/Merge) invoked on a selected node.
     *
     * @param editingContext
     *            the (non-{@code null}) {@link IEditingContext}.
     * @param selectedNode
     *            the selected node. It corresponds to a variable accessible from the variable manager.
     * @return <code>true</code> if the tool should be available, <code>false</code> otherwise.
     */
    public boolean isControlNodeActionCreationToolInAction(IEditingContext editingContext, Node selectedNode) {
        return this.objectSearchService.getObject(editingContext, selectedNode.getTargetObjectId())
                .map(object -> {
                    return object instanceof ActionUsage || object instanceof ActionDefinition;
                }).orElse(false);
    }

    /**
     * Checks if a tool creating the given type should be available for the provided diagram context.
     *
     * @param element
     *            the semantic context element
     * @param editingContext
     *            the editing context
     * @param diagramContext
     *            the diagram context
     * @param newElementType
     *            the type created by the tool
     * @return {@code true} if the tool should be available, {@code false} otherwise
     */
    public boolean toolShouldBeAvailable(Element element, IEditingContext editingContext, DiagramContext diagramContext, EClass newElementType) {
        ViewDefinitionKind viewDefinitionKind = this.utilService.getViewDefinitionKind(element, List.of(), editingContext);
        var elt = this.utilService.getViewUsageOwner(element);

        return switch (viewDefinitionKind) {
            case INTERCONNECTION_VIEW -> this.toolShouldBeAvailableOnInterconnectionView(elt, newElementType);
            case ACTION_FLOW_VIEW -> this.toolShouldBeAvailableOnActionFlowView(elt, newElementType);
            case STATE_TRANSITION_VIEW -> this.toolShouldBeAvailableOnStateTransitionView(elt, newElementType);
            default -> this.toolShouldBeAvailableOnGeneralView(elt, newElementType);
        };
    }

    private boolean toolShouldBeAvailableOnGeneralView(Element element, EClass domainClass) {
        boolean toolShouldBeAvailable = false;
        if (element instanceof Package) {
            toolShouldBeAvailable = true;
        } else if (element instanceof Usage && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        } else if (element instanceof Definition && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        }
        return toolShouldBeAvailable;
    }

    private boolean toolShouldBeAvailableOnInterconnectionView(Element element, EClass domainClass) {
        boolean toolShouldBeAvailable = false;
        if (element instanceof Package) {
            toolShouldBeAvailable = true;
            if (SysmlPackage.eINSTANCE.getAttributeUsage().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = false;
            } else if (SysmlPackage.eINSTANCE.getPortUsage().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = false;
            }
        } else if (element instanceof Usage && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        } else if (element instanceof Definition && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        }
        return toolShouldBeAvailable;
    }

    private boolean toolShouldBeAvailableOnActionFlowView(Element element, EClass domainClass) {
        boolean toolShouldBeAvailable = false;
        if (element instanceof ActionUsage) {
            toolShouldBeAvailable = !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass);
        } else if (element instanceof ActionDefinition) {
            toolShouldBeAvailable = !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass);
        } else {
            if (SysmlPackage.eINSTANCE.getActionUsage().equals(domainClass) || SysmlPackage.eINSTANCE.getActionUsage().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = true;
            } else if (SysmlPackage.eINSTANCE.getActionDefinition().equals(domainClass) || SysmlPackage.eINSTANCE.getActionDefinition().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = true;
            }
        }
        return toolShouldBeAvailable;
    }

    private boolean toolShouldBeAvailableOnStateTransitionView(Element element, EClass domainClass) {
        boolean toolShouldBeAvailable = false;
        if (element instanceof StateUsage) {
            toolShouldBeAvailable = !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass);
        } else if (element instanceof StateDefinition) {
            toolShouldBeAvailable = !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass);
        } else {
            if (SysmlPackage.eINSTANCE.getStateUsage().equals(domainClass) || SysmlPackage.eINSTANCE.getStateUsage().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = true;
            } else if (SysmlPackage.eINSTANCE.getStateDefinition().equals(domainClass) || SysmlPackage.eINSTANCE.getStateDefinition().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = true;
            }
        }
        return toolShouldBeAvailable;
    }

    private boolean isAFVDiagram(IEditingContext editingContext, DiagramContext diagramContext) {
        var objectId = diagramContext.diagram().getTargetObjectId();
        return this.objectSearchService.getObject(editingContext, objectId)
                .map(object -> {
                    if (object instanceof ViewUsage viewUsage) {
                        if (!viewUsage.getType().isEmpty()) {
                            Type type = viewUsage.getType().getFirst();
                            return type instanceof ViewDefinition && StandardDiagramsConstants.AFV_QN.equals(type.getQualifiedName());
                        }
                    }
                    return false;
                }).orElse(false);
    }
}
