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
package org.eclipse.syson.diagram.actionflow.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.RepresentationDescription;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.DiagramToolSectionBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IDiagramElementDescriptionProvider;
import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.ArrangeLayoutDirection;
import org.eclipse.sirius.components.view.diagram.DiagramElementDescription;
import org.eclipse.sirius.components.view.diagram.DiagramPalette;
import org.eclipse.sirius.components.view.diagram.DiagramToolSection;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.actionflow.view.edges.AllocateEdgeDescriptionProvider;
import org.eclipse.syson.diagram.actionflow.view.edges.DependencyEdgeDescriptionProvider;
import org.eclipse.syson.diagram.actionflow.view.edges.FeatureTypingEdgeDescriptionProvider;
import org.eclipse.syson.diagram.actionflow.view.edges.RedefinitionEdgeDescriptionProvider;
import org.eclipse.syson.diagram.actionflow.view.edges.SubclassificationEdgeDescriptionProvider;
import org.eclipse.syson.diagram.actionflow.view.edges.SubsettingEdgeDescriptionProvider;
import org.eclipse.syson.diagram.actionflow.view.edges.SuccessionEdgeDescriptionProvider;
import org.eclipse.syson.diagram.actionflow.view.nodes.ActionFlowViewEmptyDiagramNodeDescriptionProvider;
import org.eclipse.syson.diagram.actionflow.view.nodes.CompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.actionflow.view.nodes.DefinitionNodeDescriptionProvider;
import org.eclipse.syson.diagram.actionflow.view.nodes.FakeNodeDescriptionProvider;
import org.eclipse.syson.diagram.actionflow.view.nodes.PackageNodeDescriptionProvider;
import org.eclipse.syson.diagram.actionflow.view.nodes.UsageNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.ViewDiagramElementFinder;
import org.eclipse.syson.diagram.common.view.nodes.ActionFlowCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.DecisionActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.DoneActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ForkActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.JoinActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.MergeActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StartActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Description of the Action Flow View diagram using the ViewBuilder API from Sirius Web.
 *
 * @author Jerome Gout
 */
public class ActionFlowViewDiagramDescriptionProvider implements IRepresentationDescriptionProvider {

    public static final String DESCRIPTION_NAME = "Action Flow View";

    public static final List<EClass> DEFINITIONS = List.of(
            SysmlPackage.eINSTANCE.getActionDefinition()
            );

    public static final List<EClass> USAGES = List.of(
            SysmlPackage.eINSTANCE.getAcceptActionUsage(),
            SysmlPackage.eINSTANCE.getActionUsage()
            );

    public static final Map<EClass, List<EReference>> COMPARTMENTS_WITH_LIST_ITEMS = Map.ofEntries(
            Map.entry(SysmlPackage.eINSTANCE.getActionDefinition(), List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAction())),
            Map.entry(SysmlPackage.eINSTANCE.getActionUsage(),      List.of(SysmlPackage.eINSTANCE.getUsage_NestedItem(), SysmlPackage.eINSTANCE.getUsage_NestedAction()))
            );

    public static final List<ToolSectionDescription> TOOL_SECTIONS = List.of(
            new ToolSectionDescription("Action Flow", List.of(
                    SysmlPackage.eINSTANCE.getAcceptActionUsage(),
                    SysmlPackage.eINSTANCE.getActionUsage(),
                    SysmlPackage.eINSTANCE.getActionDefinition(),
                    SysmlPackage.eINSTANCE.getPackage()))
            );

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ToolDescriptionService toolDescriptionService = new ToolDescriptionService();

    private final IDescriptionNameGenerator descriptionNameGenerator = new AFVDescriptionNameGenerator();

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
        var diagramElementDescriptionProviders = new ArrayList<IDiagramElementDescriptionProvider<? extends DiagramElementDescription>>();
        diagramElementDescriptionProviders.add(new AllocateEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new DependencyEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new FeatureTypingEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new RedefinitionEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new SubclassificationEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new SubsettingEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new SuccessionEdgeDescriptionProvider(colorProvider));

        diagramElementDescriptionProviders.add(new ActionFlowCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new ActionFlowCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(),
                colorProvider, this.getDescriptionNameGenerator()));

        diagramElementDescriptionProviders.add(new FakeNodeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new ActionFlowViewEmptyDiagramNodeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new PackageNodeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new StartActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new DoneActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new JoinActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new ForkActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new MergeActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new DecisionActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));

        DEFINITIONS.forEach(definition -> {
            diagramElementDescriptionProviders.add(new DefinitionNodeDescriptionProvider(definition, colorProvider));
        });

        USAGES.forEach(usage -> {
            diagramElementDescriptionProviders.add(new UsageNodeDescriptionProvider(usage, colorProvider, this.getDescriptionNameGenerator()));
        });

        COMPARTMENTS_WITH_LIST_ITEMS.forEach((eClass, listItems) -> {
            listItems.forEach(eReference -> {
                diagramElementDescriptionProviders.add(new CompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
            });
        });

        diagramElementDescriptionProviders.stream().map(IDiagramElementDescriptionProvider::create)
        .forEach(cache::put);
        diagramElementDescriptionProviders.forEach(diagramElementDescriptionProvider -> diagramElementDescriptionProvider.link(diagramDescription, cache));

        diagramDescription.setPalette(this.createDiagramPalette(cache));

        return diagramDescription;
    }

    protected IDescriptionNameGenerator getDescriptionNameGenerator() {
        return this.descriptionNameGenerator;
    }

    private DiagramPalette createDiagramPalette(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newDiagramPalette()
                .dropTool(this.toolDescriptionService.createDropFromExplorerTool())
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
        sections.add(this.toolDescriptionService.addElementsDiagramToolSection());

        return sections.toArray(DiagramToolSection[]::new);
    }

    private NodeTool[] createElementsOfToolSection(IViewDiagramElementFinder cache, List<EClass> elements) {
        var nodeTools = new ArrayList<NodeTool>();

        elements.forEach(definition -> {
            nodeTools.add(this.toolDescriptionService.createNodeToolFromDiagramBackground(cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(definition)).get(), definition,
                    this.getDescriptionNameGenerator()));
        });

        nodeTools.sort((nt1, nt2) -> nt1.getName().compareTo(nt2.getName()));

        return nodeTools.toArray(NodeTool[]::new);
    }
}
