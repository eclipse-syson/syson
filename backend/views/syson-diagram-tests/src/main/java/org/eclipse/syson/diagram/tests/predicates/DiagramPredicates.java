/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.diagram.tests.predicates;

import java.util.function.Predicate;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DiagramElementDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.DoneActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StartActionNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Predicates for elements in a SysON {@link DiagramDescription}.
 *
 * @author gdaniel
 */
public class DiagramPredicates {

    private final Predicate<NodeDescription> isFakeNode;

    private final Predicate<NodeDescription> isEmptyDiagramNode;

    private final Predicate<NodeDescription> isCompartmentNode;

    private final Predicate<NodeDescription> isInheritedCompartmentItemNode;

    private final Predicate<NodeDescription> isInheritedBorderNode;

    private final Predicate<NodeDescription> isNamespaceImportNode;

    private final Predicate<NodeDescription> isStartOrDoneNode;

    public DiagramPredicates(String diagramPrefix, IDescriptionNameGenerator descriptionNameGenerator) {
        this.isFakeNode = n -> n.getName().equals(diagramPrefix + " Node Fake");
        this.isEmptyDiagramNode = n -> n.getName().equals(diagramPrefix + " Node EmptyDiagram");
        String compartmentNameFragment = this.getCompartmentNameFragment(descriptionNameGenerator);
        this.isCompartmentNode = n -> n.getName().contains(compartmentNameFragment);
        String inheritedCompartmentItemNameFragment = this.getInheritedCompartmentItemNameFragment(descriptionNameGenerator);
        this.isInheritedCompartmentItemNode = n -> n.getName().contains(inheritedCompartmentItemNameFragment);
        String inheritedBorderNodeNameFragment = this.getInheritedBorderNodeNameFragment(descriptionNameGenerator);
        this.isInheritedBorderNode = n -> n.getName().contains(inheritedBorderNodeNameFragment);
        this.isNamespaceImportNode = n -> n.getName().equals(diagramPrefix + " Node NamespaceImport");
        this.isStartOrDoneNode = n -> n.getName().equals(descriptionNameGenerator.getNodeName(StartActionNodeDescriptionProvider.START_ACTION_NAME))
                || n.getName().equals(descriptionNameGenerator.getNodeName(DoneActionNodeDescriptionProvider.DONE_ACTION_NAME));
    }

    public Predicate<NodeDescription> isFakeNode() {
        return this.isFakeNode;
    }

    public Predicate<NodeDescription> isEmptyDiagramNode() {
        return this.isEmptyDiagramNode;
    }

    public Predicate<NodeDescription> isCompartmentNode() {
        return this.isCompartmentNode;
    }

    public Predicate<NodeDescription> isInheritedCompartmentItemNode() {
        return this.isInheritedCompartmentItemNode;
    }

    public Predicate<NodeDescription> isInheritedBorderNode() {
        return this.isInheritedBorderNode;
    }

    public Predicate<NodeDescription> getIsNamespaceImportNode() {
        return this.isNamespaceImportNode;
    }

    public Predicate<NodeDescription> isStartOrDoneNode() {
        return this.isStartOrDoneNode;
    }

    public Predicate<DiagramElementDescription> hasDomainType(EClass eClass) {
        return diagramElementDescription -> diagramElementDescription.getDomainType().equals(SysMLMetamodelHelper.buildQualifiedName(eClass));
    }

    public Predicate<DiagramElementDescription> hasName(String name) {
        return diagramElementDescription -> diagramElementDescription.getName().equals(name);
    }

    private String getCompartmentNameFragment(IDescriptionNameGenerator nameGenerator) {
        EClass element = SysmlPackage.eINSTANCE.getElement();
        EReference reference = SysmlPackage.eINSTANCE.getElement_Documentation();
        String fullName = nameGenerator.getCompartmentName(element, reference);

        return fullName
                .replace(element.getName(), "")
                .replace(reference.getName(), "")
                .strip()
                .concat(" ");
    }

    private String getInheritedCompartmentItemNameFragment(IDescriptionNameGenerator nameGenerator) {
        EClass element = SysmlPackage.eINSTANCE.getElement();
        EReference reference = SysmlPackage.eINSTANCE.getElement_Documentation();
        String fullName = nameGenerator.getInheritedCompartmentItemName(element, reference);

        return fullName.replace(element.getName(), "")
                .replace(reference.getName(), "")
                .strip()
                .concat(" ");
    }

    private String getInheritedBorderNodeNameFragment(IDescriptionNameGenerator nameGenerator) {
        EClass element = SysmlPackage.eINSTANCE.getElement();
        String fullName = nameGenerator.getInheritedBorderNodeName(element);

        return fullName.replace(element.getName(), "")
                .strip()
                .concat(" ");
    }
}
