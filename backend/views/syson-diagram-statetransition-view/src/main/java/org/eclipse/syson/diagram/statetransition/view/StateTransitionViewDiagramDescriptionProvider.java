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
package org.eclipse.syson.diagram.statetransition.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.RepresentationDescription;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.DiagramToolSectionBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IDiagramElementDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.ArrangeLayoutDirection;
import org.eclipse.sirius.components.view.diagram.DiagramElementDescription;
import org.eclipse.sirius.components.view.diagram.DiagramPalette;
import org.eclipse.sirius.components.view.diagram.DiagramToolSection;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.common.view.ViewDiagramElementFinder;
import org.eclipse.syson.diagram.common.view.diagram.AbstractDiagramDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.diagram.statetransition.view.edges.TransitionEdgeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.nodes.CompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.nodes.DefinitionNodeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.nodes.FakeNodeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.nodes.PackageNodeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.nodes.StateTransitionCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.nodes.StateTransitionViewEmptyDiagramNodeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.nodes.UsageNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Description of the State Transition View diagram using the ViewBuilder API from Sirius Web.
 *
 * @author adieumegard
 */
public class StateTransitionViewDiagramDescriptionProvider extends AbstractDiagramDescriptionProvider {

    public static final String DESCRIPTION_NAME = "State Transition View";

    public static  final List<EClass> DEFINITIONS = List.of(
            SysmlPackage.eINSTANCE.getStateDefinition()
            );

    public static  final List<EClass> USAGES = List.of(
            SysmlPackage.eINSTANCE.getStateUsage()
            );

    public static  final Map<EClass, List<EReference>> COMPARTMENTS_WITH_LIST_ITEMS = Map.ofEntries(
            Map.entry(SysmlPackage.eINSTANCE.getStateDefinition(),      List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAction())),
            Map.entry(SysmlPackage.eINSTANCE.getStateUsage(),           List.of(SysmlPackage.eINSTANCE.getUsage_NestedAction()))
            );

    public static final List<ToolSectionDescription> TOOL_SECTIONS = List.of(
            new ToolSectionDescription("State Transition", List.of(
                    SysmlPackage.eINSTANCE.getStateDefinition(),
                    SysmlPackage.eINSTANCE.getStateUsage(),
                    SysmlPackage.eINSTANCE.getPackage()
                    ))
            );

    private final IDescriptionNameGenerator nameGenerator = new STVDescriptionNameGenerator();

    @Override
    public RepresentationDescription create(IColorProvider colorProvider) {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPackage());

        var diagramDescriptionBuilder = this.diagramBuilderHelper.newDiagramDescription();
        diagramDescriptionBuilder
                .arrangeLayoutDirection(ArrangeLayoutDirection.DOWN)
                .autoLayout(false)
                .domainType(domainType)
                .name(DESCRIPTION_NAME)
                .titleExpression(DESCRIPTION_NAME);

        var diagramDescription = diagramDescriptionBuilder.build();

        var cache = new ViewDiagramElementFinder();
        var diagramElementDescriptionProviders =  new ArrayList<IDiagramElementDescriptionProvider<? extends DiagramElementDescription>>();
        diagramElementDescriptionProviders.add(new FakeNodeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new StateTransitionViewEmptyDiagramNodeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new PackageNodeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new TransitionEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders
                .add(new StateTransitionCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState(), colorProvider, this.nameGenerator));
        diagramElementDescriptionProviders
                .add(new StateTransitionCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState(), colorProvider, this.nameGenerator));

        DEFINITIONS.forEach(definition -> {
            diagramElementDescriptionProviders.add(new DefinitionNodeDescriptionProvider(definition, colorProvider));
        });

        USAGES.forEach(usage -> {
            diagramElementDescriptionProviders.add(new UsageNodeDescriptionProvider(usage, colorProvider));
        });

        COMPARTMENTS_WITH_LIST_ITEMS.forEach((eClass, listItems) -> {
            listItems.forEach(eReference -> {
                diagramElementDescriptionProviders.add(new CompartmentNodeDescriptionProvider(eClass, eReference, colorProvider));
                diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.nameGenerator));
            });
        });

        diagramElementDescriptionProviders.stream().
                map(IDiagramElementDescriptionProvider::create)
                .forEach(cache::put);
        diagramElementDescriptionProviders.forEach(diagramElementDescriptionProvider -> diagramElementDescriptionProvider.link(diagramDescription, cache));

        diagramDescription.setPalette(this.createDiagramPalette(cache));

        return diagramDescription;
    }

    @Override
    protected IDescriptionNameGenerator getNameGenerator() {
        return this.nameGenerator;
    }

    private DiagramPalette createDiagramPalette(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newDiagramPalette()
                .dropTool(this.createDropFromExplorerTool())
                .toolSections(this.createToolSections(cache))
                .build();
    }

    private DiagramToolSection[] createToolSections(IViewDiagramElementFinder cache) {
        var sections = new ArrayList<DiagramToolSection>();

        TOOL_SECTIONS.forEach(sectionTool -> {
            DiagramToolSectionBuilder sectionBuilder = this.diagramBuilderHelper.newDiagramToolSection()
                    .name(sectionTool.name())
                    .nodeTools(this.createElementsOfToolSection(cache, sectionTool.elements()));
            sections.add(sectionBuilder.build());
        });

        // add extra section for existing elements
        sections.add(this.addElementsToolSection(cache));

        return sections.toArray(DiagramToolSection[]::new);
    }

    private NodeTool[] createElementsOfToolSection(IViewDiagramElementFinder cache, List<EClass> elements) {
        var nodeTools = new ArrayList<NodeTool>();

        elements.forEach(definition -> {
            cache.getNodeDescription(this.nameGenerator.getNodeName(definition)).ifPresent(nodeDescription -> nodeTools.add(this.createNodeToolFromDiagramBackground(nodeDescription, definition)));
        });

        nodeTools.sort((nt1, nt2) -> nt1.getName().compareTo(nt2.getName()));

        return nodeTools.toArray(NodeTool[]::new);
    }
}
