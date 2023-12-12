/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Fake Node Description allowing to store other Node Descriptions that will be reused by other Node Descriptions.
 * Typical use is for compartment Nodes.
 *
 * @author arichard
 */
public class FakeNodeDescriptionProvider implements INodeDescriptionProvider {

    public static final String NAME = "GV Node Fake";

    protected final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    protected final IColorProvider colorProvider;

    public FakeNodeDescriptionProvider(IColorProvider colorProvider) {
        this.colorProvider = Objects.requireNonNull(colorProvider);
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
        var optFakeNodeDescription = cache.getNodeDescription(NAME);
        var optDefinitionAttributesCompartmentNodeDescription = cache.getNodeDescription(DefinitionAttributesCompartmentNodeDescriptionProvider.NAME);
        var optDefinitionItemsCompartmentNodeDescription = cache.getNodeDescription(DefinitionItemsCompartmentNodeDescriptionProvider.NAME);
        var optDefinitionPortsCompartmentNodeDescription = cache.getNodeDescription(DefinitionPortsCompartmentNodeDescriptionProvider.NAME);
        var optUsageAttributesCompartmentNodeDescription = cache.getNodeDescription(UsageAttributesCompartmentNodeDescriptionProvider.NAME);
        var optUsagePortsCompartmentNodeDescription = cache.getNodeDescription(UsagePortsCompartmentNodeDescriptionProvider.NAME);

        NodeDescription nodeDescription = optFakeNodeDescription.get();
        diagramDescription.getNodeDescriptions().add(nodeDescription);
        nodeDescription.getChildrenDescriptions().add(optDefinitionAttributesCompartmentNodeDescription.get());
        nodeDescription.getChildrenDescriptions().add(optDefinitionItemsCompartmentNodeDescription.get());
        nodeDescription.getChildrenDescriptions().add(optDefinitionPortsCompartmentNodeDescription.get());
        nodeDescription.getChildrenDescriptions().add(optUsageAttributesCompartmentNodeDescription.get());
        nodeDescription.getChildrenDescriptions().add(optUsagePortsCompartmentNodeDescription.get());
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
