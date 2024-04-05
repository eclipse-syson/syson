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
package org.eclipse.syson.diagram.interconnection.view;

import java.util.List;

import org.eclipse.sirius.components.view.RepresentationDescription;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IDiagramElementDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.DiagramPalette;
import org.eclipse.syson.diagram.common.view.ViewDiagramElementFinder;
import org.eclipse.syson.diagram.common.view.diagram.AbstractDiagramDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.edges.BindingConnectorAsUsageEdgeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.ChildPartUsageNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.ChildrenPartUsageCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.CompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.FakeNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.PortUsageBorderNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.RootPartUsageNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Description of the Interconnection View diagram using the ViewBuilder API from Sirius Web.
 *
 * @author arichard
 */
public class InterconnectionViewDiagramDescriptionProvider extends AbstractDiagramDescriptionProvider {

    public static final String DESCRIPTION_NAME = "Interconnection View";

    private final IDescriptionNameGenerator nameGenerator = new IVDescriptionNameGenerator();

    @Override
    public RepresentationDescription create(IColorProvider colorProvider) {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPartUsage());

        var diagramDescriptionBuilder = this.diagramBuilderHelper.newDiagramDescription();
        diagramDescriptionBuilder
                .autoLayout(false)
                .domainType(domainType)
                .name(DESCRIPTION_NAME)
                .titleExpression(DESCRIPTION_NAME);

        var diagramDescription = diagramDescriptionBuilder.build();

        var cache = new ViewDiagramElementFinder();
        var diagramElementDescriptionProviders = List.of(
                new FakeNodeDescriptionProvider(colorProvider),
                new RootPartUsageNodeDescriptionProvider(colorProvider),
                new ChildPartUsageNodeDescriptionProvider(colorProvider, this.getNameGenerator()),
                new CompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), colorProvider),
                new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), colorProvider, this.getNameGenerator()),
                new ChildrenPartUsageCompartmentNodeDescriptionProvider(colorProvider, this.getNameGenerator()),
                new PortUsageBorderNodeDescriptionProvider(colorProvider),
                new BindingConnectorAsUsageEdgeDescriptionProvider(colorProvider)
        );

        diagramElementDescriptionProviders.stream().
                map(IDiagramElementDescriptionProvider::create)
                .forEach(cache::put);
        diagramElementDescriptionProviders.forEach(diagramElementDescriptionProvider -> diagramElementDescriptionProvider.link(diagramDescription, cache));

        var palette = this.createDiagramPalette(cache);
        diagramDescription.setPalette(palette);

        return diagramDescription;
    }

    @Override
    protected IDescriptionNameGenerator getNameGenerator() {
        return this.nameGenerator;
    }

    private DiagramPalette createDiagramPalette(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newDiagramPalette()
                .dropTool(this.createDropFromExplorerTool())
                .build();
    }
}
