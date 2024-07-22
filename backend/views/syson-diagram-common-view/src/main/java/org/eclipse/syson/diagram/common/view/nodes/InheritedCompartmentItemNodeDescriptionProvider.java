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
package org.eclipse.syson.diagram.common.view.nodes;

import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.LabelOverflowStrategy;
import org.eclipse.sirius.components.view.diagram.LabelTextAlign;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the inherited compartment item node description.
 *
 * @author arichard
 */
public class InheritedCompartmentItemNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    private final EClass eClass;

    private final EReference eReference;

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public InheritedCompartmentItemNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.eClass = Objects.requireNonNull(eClass);
        this.eReference = Objects.requireNonNull(eReference);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        return this.diagramBuilderHelper.newNodeDescription()
                .defaultHeightExpression(ViewConstants.DEFAULT_COMPARTMENT_NODE_ITEM_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(SysMLMetamodelHelper.buildQualifiedName(this.eReference.getEType()))
                .insideLabel(this.createInsideLabelDescription())
                .name(this.descriptionNameGenerator.getInheritedCompartmentItemName(this.eClass, this.eReference))
                .semanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getInheritedCompartmentItems", "'" + this.eReference.getName() + "'"))
                .style(this.createCompartmentItemNodeStyle())
                .userResizable(UserResizableDirection.NONE)
                .palette(this.createInheritedCompartmentItemNodePalette())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    protected InsideLabelDescription createInsideLabelDescription() {
        return this.diagramBuilderHelper.newInsideLabelDescription()
                .labelExpression(AQLConstants.AQL + "'^' + self.getCompartmentItemLabel()")
                .overflowStrategy(LabelOverflowStrategy.WRAP)
                .position(InsideLabelPosition.TOP_LEFT)
                .style(this.createInsideLabelStyle())
                .textAlign(LabelTextAlign.LEFT)
                .build();
    }

    protected InsideLabelStyle createInsideLabelStyle() {
        return this.diagramBuilderHelper.newInsideLabelStyle()
                .borderSize(0)
                .displayHeaderSeparator(false)
                .italic(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIconExpression("aql:true")
                .withHeader(false)
                .build();
    }

    private NodeStyleDescription createCompartmentItemNodeStyle() {
        return this.diagramBuilderHelper.newIconLabelNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .background(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .build();
    }

    private NodePalette createInheritedCompartmentItemNodePalette() {
        return this.diagramBuilderHelper.newNodePalette()
                .toolSections(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection())
                .build();
    }
}
