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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the Compartment item node description.
 *
 * @author arichard
 */
public class CompartmentItemNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    private final EClass eClass;

    private final EReference eReference;

    public CompartmentItemNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider) {
        super(colorProvider);
        this.eClass = Objects.requireNonNull(eClass);
        this.eReference = Objects.requireNonNull(eReference);
    }

    @Override
    public NodeDescription create() {
        return this.diagramBuilderHelper.newNodeDescription()
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_ITEM_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(this.eReference.getEType()))
                .labelExpression(AQLConstants.AQL_SELF + ".getCompartmentItemUsageLabel()")
                .name(GVDescriptionNameGenerator.getCompartmentItemName(this.eClass, this.eReference))
                .semanticCandidatesExpression(AQLConstants.AQL_SELF + "." + this.eReference.getName())
                .style(this.createCompartmentItemNodeStyle())
                .userResizable(false)
                .palette(this.createCompartmentItemNodePalette())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    private NodeStyleDescription createCompartmentItemNodeStyle() {
        return this.diagramBuilderHelper.newIconLabelNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(true)
                .build();
    }

    private NodePalette createCompartmentItemNodePalette() {
        var callDeleteService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF + ".deleteFromModel()");

        var deleteTool = this.diagramBuilderHelper.newDeleteTool()
                .name("Delete from Model")
                .body(callDeleteService.build());

        var callEditService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF + ".directEdit(newLabel)");

        var editTool = this.diagramBuilderHelper.newLabelEditTool()
                .name("Edit")
                .initialDirectEditLabelExpression(AQLConstants.AQL_SELF + ".getUsageInitialDirectEditLabel()")
                .body(callEditService.build());

        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .labelEditTool(editTool.build())
                .build();
    }
}