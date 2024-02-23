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
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the Compartment node description.
 * 
 * @author arichard
 */
public class CompartmentNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    private final EClass eClass;

    private final EReference eReference;

    public CompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider) {
        super(colorProvider);
        this.eClass = Objects.requireNonNull(eClass);
        this.eReference = Objects.requireNonNull(eReference);
    }

    @Override
    public NodeDescription create() {
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenLayoutStrategy(this.diagramBuilderHelper.newListLayoutStrategyDescription().bottomGapExpression("10").build())
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement()))
                .labelExpression(this.getCompartmentLabel())
                .name(GVDescriptionNameGenerator.getCompartmentName(this.eClass, this.eReference))
                .semanticCandidatesExpression(AQLConstants.AQL_SELF)
                .style(this.createCompartmentNodeStyle())
                .userResizable(false)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optCompartmentNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getCompartmentName(this.eClass, this.eReference));
        NodeDescription nodeDescription = optCompartmentNodeDescription.get();

        GeneralViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            if (type.equals(this.eClass)) {
                listItems.forEach(ref -> {
                    if (ref.equals(this.eReference)) {
                        var optCompartmentItemNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getCompartmentItemName(type, ref));
                        nodeDescription.getChildrenDescriptions().add(optCompartmentItemNodeDescription.get());
                    }
                });
            }
        });

        nodeDescription.setPalette(this.createCompartmentPalette(cache));
    }

    private String getCompartmentLabel() {
        String defaultName = "";
        EClassifier eType = this.eReference.getEType();
        if (eType instanceof EClass eTypeClass && SysmlPackage.eINSTANCE.getUsage().isSuperTypeOf(eTypeClass)) {
            char[] charArray = eTypeClass.getName().toCharArray();
            charArray[0] = Character.toLowerCase(charArray[0]);
            defaultName = new String(charArray);
            if (defaultName.endsWith("Usage")) {
                defaultName = defaultName.substring(0, defaultName.length() - 5) + "s";
            }
        } else {
            defaultName = eType.getName();
        }
        return defaultName;
    }

    private NodeStyleDescription createCompartmentNodeStyle() {
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

    private NodePalette createCompartmentPalette(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newNodePalette()
                .dropNodeTool(this.createCompartmentDropFromDiagramTool(cache))
                .nodeTools(this.createCompartmentNodeTool(this.eReference.getEType(), this.getNewElementDefaultName(this.eReference.getEType())))
                .build();
    }

    private DropNodeTool createCompartmentDropFromDiagramTool(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();

        GeneralViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            listItems.forEach(ref -> {
                if (this.eReference.getEType().equals(ref.getEType())) {
                    var optCompartmentItemNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getCompartmentItemName(type, ref));
                    acceptedNodeTypes.add(optCompartmentItemNodeDescription.get());
                }
            });
        });

        var dropElementFromDiagram = this.viewBuilderHelper.newChangeContext()
                .expression("aql:droppedElement.dropElementFromDiagram(droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes)");

        return this.diagramBuilderHelper.newDropNodeTool()
                .name("Drop from Diagram")
                .acceptedNodeTypes(acceptedNodeTypes.toArray(NodeDescription[]::new))
                .body(dropElementFromDiagram.build())
                .build();
    }

    private NodeTool createCompartmentNodeTool(EClassifier type, String label) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var setValue = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getElement_DeclaredName().getName())
                .valueExpression(label);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(setValue.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(type))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var changeContextMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newFeatureMembership")
                .children(createInstance.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(getMembership(type)))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newFeatureMembership")
                .children(changeContextMembership.build());

        return builder
                .name("New " + type.getName())
                .iconURLsExpression("/icons/full/obj16/" + eClass.getName() + ".svg")
                .body(createMembership.build())
                .build();
    }

    private EClass getMembership(EClassifier type) {
        EClass membershipClass = SysmlPackage.eINSTANCE.getFeatureMembership();
        if (type.equals(SysmlPackage.eINSTANCE.getEnumerationUsage())) {
            membershipClass = SysmlPackage.eINSTANCE.getVariantMembership();
        }
        return membershipClass;
    }
}
