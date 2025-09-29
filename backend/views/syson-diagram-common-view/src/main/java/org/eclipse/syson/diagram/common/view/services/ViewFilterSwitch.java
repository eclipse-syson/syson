/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.diagram.common.view.services;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.services.api.ViewDefinitionKind;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Switch allowing to know in which cases a Node should be displayed or not.
 * <p>
 * In General View, a sibling Node and a composition edge should be displayed after the creation of a new nested
 * semantic Usage/Definition.
 * </p>
 * <p>
 * In Interconnection View, the nested Nodes should not be displayed. They are handled by other NodeDescriptions, inside
 * compartments.
 * </p>
 * <p>
 * Root elements return true for this switch because the NodeDescriptions calling ViewNodeService#getExposedElements are
 * the same for root and nested Nodes.
 * </p>
 *
 * @author arichard
 */
public class ViewFilterSwitch extends SysmlSwitch<Boolean> {

    /**
     * The kind of ViewDefinition the ViewUsage associated to the object on which this Switch is applied is typed by.
     */
    private final ViewDefinitionKind kind;

    /**
     * The list of exposed Elements associated the ViewUsage associated to the object on which this Switch is applied.
     */
    private final List<Element> exposedElements;

    /**
     * The parent Element of the object on which this Switch is applied. It can be <code>null</code>.
     */
    private final Element parentElement;

    public ViewFilterSwitch(ViewDefinitionKind kind, List<Element> exposedElements, Element parentElement) {
        this.kind = kind;
        this.exposedElements = Objects.requireNonNull(exposedElements);
        this.parentElement = parentElement;
    }

    @Override
    public Boolean defaultCase(EObject object) {
        Boolean displayAsTreeNode = Boolean.FALSE;
        if (ViewDefinitionKind.isGeneralView(this.kind)) {
            displayAsTreeNode = Boolean.TRUE;
        } else if (ViewDefinitionKind.isActionFlowView(this.kind)) {
            displayAsTreeNode = object instanceof ActionUsage || object instanceof ActionDefinition;
        } else if (ViewDefinitionKind.isStateTransitionView(this.kind)) {
            displayAsTreeNode = object instanceof StateUsage || object instanceof StateDefinition;
        } else {
            displayAsTreeNode = !this.isNestedNode(object);
        }
        return displayAsTreeNode;
    }

    @Override
    public Boolean caseAttributeUsage(AttributeUsage object) {
        // For AttributeUsages we don't want nested Nodes, no matter the type of ViewDefinition.
        // The sub AttributeUsages are displayed in a compartment list called "attributes".
        return !this.isNestedNode(object) && !ViewDefinitionKind.isActionFlowView(this.kind) && !ViewDefinitionKind.isStateTransitionView(this.kind);
    }

    @Override
    public Boolean caseDocumentation(Documentation object) {
        Element owner = object.getOwner();
        return owner instanceof Package || owner instanceof NamespaceImport || owner instanceof ViewUsage;
    }

    @Override
    public Boolean casePortUsage(PortUsage object) {
        // For PortUsages we don't want nested Nodes, no matter the type of ViewDefinition.
        // The sub PortUsages are displayed in a compartment list called "ports" and/or as border nodes.
        return !this.isNestedNode(object) && !ViewDefinitionKind.isActionFlowView(this.kind) && !ViewDefinitionKind.isStateTransitionView(this.kind);
    }

    private boolean isNestedNode(EObject object) {
        boolean isNestedNode = false;
        for (Element exposedElement : this.exposedElements) {
            isNestedNode = !Objects.equals(exposedElement, object) && (this.parentElement == null && EMFUtils.isAncestor(exposedElement, object));
            if (isNestedNode) {
                break;
            }
        }
        return isNestedNode;
    }

}
