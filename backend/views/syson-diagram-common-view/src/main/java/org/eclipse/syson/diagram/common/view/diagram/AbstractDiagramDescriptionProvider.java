/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.diagram.common.view.diagram;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.DiagramToolSection;
import org.eclipse.sirius.components.view.diagram.DropTool;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Abstract class used to create diagram descriptions.
 *
 * @author arichard
 */
public abstract class AbstractDiagramDescriptionProvider implements IRepresentationDescriptionProvider {

    protected final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    protected final ViewBuilders viewBuilderHelper = new ViewBuilders();

    protected abstract IDescriptionNameGenerator getNameGenerator();

    protected DiagramToolSection addElementsToolSection(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newDiagramToolSection()
                .name("Add")
                .nodeTools(this.addExistingElementsTool(false), this.addExistingElementsTool(true))
                .build();
    }

    protected NodeTool addExistingElementsTool(boolean recursive) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var addExistingelements = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.addExistingElements(editingContext, diagramContext, selectedNode, convertedNodes, " + recursive + ")");

        String title = "Add existing elements";
        String iconURL = "/icons/AddExistingElements.svg";
        if (recursive) {
            title += " (recursive)";
            iconURL = "/icons/AddExistingElementsRecursive.svg";
        }

        return builder
                .name(title)
                .iconURLsExpression(iconURL)
                .body(addExistingelements.build())
                .build();
    }

    protected DropTool createDropFromExplorerTool() {
        var dropElementFromExplorer = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.dropElementFromExplorer(editingContext, diagramContext, selectedNode, convertedNodes)");

        return this.diagramBuilderHelper.newDropTool()
                .name("Drop from Explorer")
                .body(dropElementFromExplorer.build())
                .build();
    }

    protected NodeTool createNodeToolFromPackage(NodeDescription nodeDescription, EClassifier eClass) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var callElementInitializerService = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.elementInitializer()");

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(callElementInitializerService.build());

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(NodeContainmentKind.CHILD_NODE)
                .elementDescription(nodeDescription)
                .parentViewExpression("aql:selectedNode")
                .semanticElementExpression("aql:newInstance")
                .variableName("newInstanceView");

        var changeContexMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newOwningMembership")
                .children(createEClassInstance.build(), createView.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getOwningMembership()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newOwningMembership")
                .children(changeContexMembership.build());

        return builder
                .name(this.getNameGenerator().getCreationToolName(eClass))
                .iconURLsExpression("/icons/full/obj16/" + eClass.getName() + ".svg")
                .body(createMembership.build())
                .build();
    }
}
