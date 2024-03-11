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

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.common.view.nodes.AbstractNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Fake Node Description allowing to store other Node Descriptions that will be reused by other Node Descriptions.
 * Typical use is for compartment Nodes.
 *
 * @author arichard
 */
public class FakeNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    public static final String NAME = "GV Node Fake";

    public FakeNodeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement());
        return this.diagramBuilderHelper.newNodeDescription()
                .defaultHeightExpression("0")
                .defaultWidthExpression("0")
                .domainType(domainType)
                .labelExpression("")
                .name(NAME)
                .semanticCandidatesExpression("")
                .style(this.createFakeNodeStyle())
                .userResizable(true)
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var nameGenerator = new GVDescriptionNameGenerator();
        var optFakeNodeDescription = cache.getNodeDescription(NAME);
        NodeDescription nodeDescription = optFakeNodeDescription.get();
        diagramDescription.getNodeDescriptions().add(nodeDescription);
        
        GeneralViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            listItems.forEach(eReference -> {
                var optNodeDescription = cache.getNodeDescription(nameGenerator.getCompartmentName(type, eReference));
                nodeDescription.getChildrenDescriptions().add(optNodeDescription.get());
            });
        });
    }

    protected NodeStyleDescription createFakeNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor("transparent"))
                .borderRadius(0)
                .color(this.colorProvider.getColor("transparent"))
                .displayHeaderSeparator(false)
                .labelColor(this.colorProvider.getColor("transparent"))
                .showIcon(false)
                .withHeader(false)
                .build();
    }
}
