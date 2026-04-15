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
package org.eclipse.syson.diagram.services.utils;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.services.api.ViewDefinitionKind;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Switch allowing to know in which cases a node should be displayed or not.
 *
 * @author arichard
 */
public class ViewFilterSwitch extends SysmlSwitch<Boolean> {

    private final ViewDefinitionKind kind;

    private final List<Element> exposedElements;

    private final Element parentElement;

    private final MetamodelQueryElementService metamodelQueryElementService;

    public ViewFilterSwitch(ViewDefinitionKind kind, List<Element> exposedElements, Element parentElement, MetamodelQueryElementService metamodelQueryElementService) {
        this.kind = kind;
        this.exposedElements = Objects.requireNonNull(exposedElements);
        this.parentElement = parentElement;
        this.metamodelQueryElementService = Objects.requireNonNull(metamodelQueryElementService);
    }

    @Override
    public Boolean defaultCase(EObject object) {
        Boolean displayAsTreeNode = Boolean.FALSE;
        if (ViewDefinitionKind.isGeneralView(this.kind)) {
            displayAsTreeNode = this.parentElement == null || this.isDirectNestedNode(object);
        } else if (ViewDefinitionKind.isActionFlowView(this.kind)) {
            displayAsTreeNode = object instanceof ActionUsage || object instanceof ActionDefinition;
        } else if (ViewDefinitionKind.isStateTransitionView(this.kind)) {
            displayAsTreeNode = object instanceof StateUsage || object instanceof StateDefinition;
        } else {
            displayAsTreeNode = !this.isIndirectNestedNode(object);
        }
        return displayAsTreeNode;
    }

    @Override
    public Boolean caseAttributeUsage(AttributeUsage object) {
        return ViewDefinitionKind.isGeneralView(this.kind);
    }

    @Override
    public Boolean caseDocumentation(Documentation object) {
        Element owner = object.getOwner();
        return owner instanceof Package || owner instanceof NamespaceImport || owner instanceof ViewUsage;
    }

    @Override
    public Boolean casePackage(Package object) {
        return !this.isIndirectNestedNode(object) && !ViewDefinitionKind.isActionFlowView(this.kind) && !ViewDefinitionKind.isStateTransitionView(this.kind);
    }

    @Override
    public Boolean caseReferenceUsage(ReferenceUsage object) {
        return this.metamodelQueryElementService.isSubject(object)
                || (!this.isIndirectNestedNode(object) && !ViewDefinitionKind.isActionFlowView(this.kind) && !ViewDefinitionKind.isStateTransitionView(this.kind));
    }

    private boolean isIndirectNestedNode(EObject object) {
        boolean isDirectNestedNode = false;
        if (this.parentElement instanceof Namespace elt && !elt.getOwnedMember().contains(object)) {
            isDirectNestedNode = true;
        } else {
            for (Element exposedElement : this.exposedElements) {
                if (Objects.equals(exposedElement, object)) {
                    continue;
                } else if (this.parentElement == null && EMFUtils.isAncestor(exposedElement, object)) {
                    isDirectNestedNode = true;
                }
                if (isDirectNestedNode) {
                    break;
                }
            }
        }
        return isDirectNestedNode;
    }

    private boolean isDirectNestedNode(EObject object) {
        boolean isDirectNestedNode = false;
        if (this.parentElement instanceof Namespace elt && elt.getOwnedMember().contains(object)) {
            isDirectNestedNode = true;
        } else if (this.parentElement instanceof Package pkg && !this.subPackageContainsElement(pkg, object)) {
            isDirectNestedNode = true;
        }
        return isDirectNestedNode;
    }

    private boolean subPackageContainsElement(Package pkg, EObject object) {
        return pkg.getOwnedElement().stream()
                .filter(Package.class::isInstance)
                .map(Package.class::cast)
                .anyMatch(subPkg -> EMFUtils.isAncestor(subPkg, object));
    }
}
