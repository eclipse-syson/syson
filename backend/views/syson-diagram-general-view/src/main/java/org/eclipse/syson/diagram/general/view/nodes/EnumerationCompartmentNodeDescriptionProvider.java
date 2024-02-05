/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the Enumeration Definition enumerations compartment node description.
 *
 * @author arichard
 */
public class EnumerationCompartmentNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    public static final String NAME = "GV Node EnumerationDefinitionCompartment";

    public EnumerationCompartmentNodeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    public NodeDescription create() {
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenLayoutStrategy(this.diagramBuilderHelper.newListLayoutStrategyDescription().build())
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement()))
                .labelExpression("enums")
                .name(NAME)
                .semanticCandidatesExpression(AQLConstants.AQL_SELF)
                .style(this.createEnumerationCompartmentNodeStyle())
                .userResizable(false)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEnumerationCompartmentNodeDescription = cache.getNodeDescription(EnumerationCompartmentNodeDescriptionProvider.NAME);
        var optEnumerationCompartmentItemNodeDescription = cache.getNodeDescription(EnumerationCompartmentItemNodeDescriptionProvider.NAME);

        NodeDescription nodeDescription = optEnumerationCompartmentNodeDescription.get();
        nodeDescription.getChildrenDescriptions().add(optEnumerationCompartmentItemNodeDescription.get());
        nodeDescription.setPalette(this.createEnumerationCompartmentPalette(cache));
    }

    private NodeStyleDescription createEnumerationCompartmentNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_COMPARTMENT_BACKGROUND_COLOR))
                .displayHeaderSeparator(false)
                .fontSize(12)
                .italic(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(false)
                .withHeader(true)
                .build();
    }

    private NodePalette createEnumerationCompartmentPalette(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newNodePalette()
                .dropNodeTool(this.createEnumerationCompartmentDropFromDiagramTool(cache))
                .nodeTools(this.createEnumerationCompartmentNodeTool(SysmlPackage.eINSTANCE.getEnumerationUsage(), "enumeration"))
                .build();
    }

    private NodeTool createEnumerationCompartmentNodeTool(EClass eClass, String defaultLabel) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var setValue = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getElement_DeclaredName().getName())
                .valueExpression(defaultLabel);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(setValue.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var changeContextMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newVariantMembership")
                .children(createInstance.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getVariantMembership()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newVariantMembership")
                .children(changeContextMembership.build());

        return builder
                .name("New " + eClass.getName())
                .iconURLsExpression("/icons/full/obj16/" + eClass.getName() + ".svg")
                .body(createMembership.build())
                .build();
    }

    private DropNodeTool createEnumerationCompartmentDropFromDiagramTool(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();

        var optEnumerationCompartmentItemNodeDescription = cache.getNodeDescription(EnumerationCompartmentItemNodeDescriptionProvider.NAME);

        acceptedNodeTypes.add(optEnumerationCompartmentItemNodeDescription.get());

        var dropElementFromDiagram = this.viewBuilderHelper.newChangeContext()
                .expression("aql:droppedElement.dropElementFromDiagram(droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes)");

        return this.diagramBuilderHelper.newDropNodeTool()
                .name("Drop from Diagram")
                .acceptedNodeTypes(acceptedNodeTypes.toArray(NodeDescription[]::new))
                .body(dropElementFromDiagram.build())
                .build();
    }
}
