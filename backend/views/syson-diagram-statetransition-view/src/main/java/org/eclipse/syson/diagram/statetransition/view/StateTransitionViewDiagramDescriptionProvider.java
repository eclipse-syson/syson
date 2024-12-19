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
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.RepresentationDescription;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramToolSectionBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IDiagramElementDescriptionProvider;
import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.ArrangeLayoutDirection;
import org.eclipse.sirius.components.view.diagram.DiagramElementDescription;
import org.eclipse.sirius.components.view.diagram.DiagramPalette;
import org.eclipse.sirius.components.view.diagram.DiagramToolSection;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.common.view.ViewDiagramElementFinder;
import org.eclipse.syson.diagram.common.view.edges.AnnotationEdgeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.AnnotatingNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ImportedPackageNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.MergedReferencesCompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StateTransitionCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StatesCompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StatesCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.description.ToolConstants;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.common.view.tools.ExhibitStateWithReferenceNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.NamespaceImportNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.diagram.statetransition.view.edges.TransitionEdgeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.nodes.CompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.nodes.DefinitionNodeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.nodes.FakeNodeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.nodes.PackageNodeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.nodes.StateTransitionActionsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.nodes.StateTransitionViewEmptyDiagramNodeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.nodes.UsageNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Description of the State Transition View diagram using the ViewBuilder API from Sirius Web.
 *
 * @author adieumegard
 */
public class StateTransitionViewDiagramDescriptionProvider implements IRepresentationDescriptionProvider {

    public static final List<EClass> ANNOTATINGS = List.of(SysmlPackage.eINSTANCE.getComment(), SysmlPackage.eINSTANCE.getDocumentation());

    public static final String DESCRIPTION_NAME = "State Transition View";

    // @formatter:off

    public static  final List<EClass> DEFINITIONS = List.of(
            SysmlPackage.eINSTANCE.getStateDefinition()
            );

    public static  final List<EClass> USAGES = List.of(
            SysmlPackage.eINSTANCE.getExhibitStateUsage(),
            SysmlPackage.eINSTANCE.getStateUsage()
            );

    public static  final Map<EClass, List<EReference>> COMPARTMENTS_WITH_LIST_ITEMS = Map.ofEntries(
            Map.entry(SysmlPackage.eINSTANCE.getStateDefinition(),      List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedState())),
            Map.entry(SysmlPackage.eINSTANCE.getStateUsage(),           List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedState())),
            Map.entry(SysmlPackage.eINSTANCE.getExhibitStateUsage(),    List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
            );

    public static  final Map<EClass, List<EReference>> COMPARTMENTS_WITH_MERGED_LIST_ITEMS = Map.ofEntries(
            Map.entry(SysmlPackage.eINSTANCE.getStateDefinition(),      List.of(SysmlPackage.eINSTANCE.getStateDefinition_EntryAction(), SysmlPackage.eINSTANCE.getStateDefinition_DoAction(), SysmlPackage.eINSTANCE.getStateDefinition_ExitAction())),
            Map.entry(SysmlPackage.eINSTANCE.getStateUsage(),           List.of(SysmlPackage.eINSTANCE.getStateUsage_EntryAction(), SysmlPackage.eINSTANCE.getStateUsage_DoAction(), SysmlPackage.eINSTANCE.getStateUsage_ExitAction())),
            Map.entry(SysmlPackage.eINSTANCE.getExhibitStateUsage(),    List.of(SysmlPackage.eINSTANCE.getStateUsage_EntryAction(), SysmlPackage.eINSTANCE.getStateUsage_DoAction(), SysmlPackage.eINSTANCE.getStateUsage_ExitAction()))
            );

    public static final ToolSectionDescription STRUCTURE_TOOL_SECTION = new ToolSectionDescription(ToolConstants.STRUCTURE,
            List.of(SysmlPackage.eINSTANCE.getPackage()));

    public static final ToolSectionDescription BEHAVIOR_TOOL_SECTION = new ToolSectionDescription(ToolConstants.BEHAVIOR,
            List.of(SysmlPackage.eINSTANCE.getExhibitStateUsage(),
                    SysmlPackage.eINSTANCE.getStateDefinition(),
                    SysmlPackage.eINSTANCE.getStateUsage()
            ));

    public static final List<ToolSectionDescription> TOOL_SECTIONS = List.of(STRUCTURE_TOOL_SECTION, BEHAVIOR_TOOL_SECTION);

    // @formatter:on

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator = new STVDescriptionNameGenerator();

    private final ToolDescriptionService toolDescriptionService = new ToolDescriptionService(this.descriptionNameGenerator);

    @Override
    public RepresentationDescription create(IColorProvider colorProvider) {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getNamespace());

        var diagramDescriptionBuilder = this.diagramBuilderHelper.newDiagramDescription();
        diagramDescriptionBuilder
                .arrangeLayoutDirection(ArrangeLayoutDirection.DOWN)
                .autoLayout(false)
                .domainType(domainType)
                .preconditionExpression(AQLUtils.getSelfServiceCallExpression("canCreateDiagram"))
                .name(DESCRIPTION_NAME)
                .titleExpression(DESCRIPTION_NAME);

        var diagramDescription = diagramDescriptionBuilder.build();

        var cache = new ViewDiagramElementFinder();
        var diagramElementDescriptionProviders =  new ArrayList<IDiagramElementDescriptionProvider<? extends DiagramElementDescription>>();
        diagramElementDescriptionProviders.add(new FakeNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new StateTransitionViewEmptyDiagramNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new PackageNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new ImportedPackageNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new TransitionEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders
                .add(new StateTransitionCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders
                .add(new StateTransitionCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders
                .add(new StateTransitionCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getExhibitStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new AnnotationEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new ImportedPackageNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));

        DEFINITIONS.forEach(definition -> {
            diagramElementDescriptionProviders.add(new DefinitionNodeDescriptionProvider(definition, colorProvider, this.getDescriptionNameGenerator()));
        });

        USAGES.forEach(usage -> {
            diagramElementDescriptionProviders.add(new UsageNodeDescriptionProvider(usage, colorProvider, this.getDescriptionNameGenerator()));
        });

        COMPARTMENTS_WITH_MERGED_LIST_ITEMS.forEach((eClass, listItems) -> {
            listItems.forEach(eReference -> {
                diagramElementDescriptionProviders.add(new StateTransitionActionsCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
            });
            diagramElementDescriptionProviders.add(new MergedReferencesCompartmentItemNodeDescriptionProvider(eClass, listItems, colorProvider, this.getDescriptionNameGenerator()));
        });

        ANNOTATINGS.forEach(annotating -> {
            diagramElementDescriptionProviders.add(new AnnotatingNodeDescriptionProvider(annotating, colorProvider, this.getDescriptionNameGenerator()));
        });

        COMPARTMENTS_WITH_LIST_ITEMS.forEach((eClass, listItems) -> {
            listItems.forEach(eReference -> {
                if (SysmlPackage.eINSTANCE.getExhibitStateUsage().equals(eClass) && SysmlPackage.eINSTANCE.getUsage_NestedState().equals(eReference)) {
                    diagramElementDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    diagramElementDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                } else if (SysmlPackage.eINSTANCE.getStateUsage().equals(eClass) && SysmlPackage.eINSTANCE.getUsage_NestedState().equals(eReference)) {
                    diagramElementDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    diagramElementDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                } else if (SysmlPackage.eINSTANCE.getStateDefinition().equals(eClass) && SysmlPackage.eINSTANCE.getDefinition_OwnedState().equals(eReference)) {
                    diagramElementDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    diagramElementDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                } else {
                    diagramElementDescriptionProviders.add(new CompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                    diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                }
            });
        });

        diagramElementDescriptionProviders.stream().
                map(IDiagramElementDescriptionProvider::create)
                .forEach(cache::put);

        this.linkStatesCompartment(cache);

        diagramElementDescriptionProviders.forEach(diagramElementDescriptionProvider -> diagramElementDescriptionProvider.link(diagramDescription, cache));

        diagramDescription.setPalette(this.createDiagramPalette(cache));

        return diagramDescription;
    }

    private void linkStatesCompartment(IViewDiagramElementFinder cache) {
        NodeDescription stateDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getStateDefinition())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getStateDefinition(),
                SysmlPackage.eINSTANCE.getDefinition_OwnedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(stateDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
        NodeDescription stateUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getStateUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(),
                SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(stateUsageNodeDescription.getReusedChildNodeDescriptions()::add);
        NodeDescription exhibitStateUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getExhibitStateUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getExhibitStateUsage(),
                SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(exhibitStateUsageNodeDescription.getReusedChildNodeDescriptions()::add);
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
                    .nodeTools(this.createElementsOfToolSection(cache, sectionTool.name(), sectionTool.elements()));
            sections.add(sectionBuilder.build());
        });

        // add extra section for existing elements
        sections.add(this.toolDescriptionService.relatedElementsDiagramToolSection());

        return sections.toArray(DiagramToolSection[]::new);
    }

    private NodeTool[] createElementsOfToolSection(IViewDiagramElementFinder cache, String name, List<EClass> elements) {
        var nodeTools = new ArrayList<NodeTool>();

        elements.forEach(element -> {
            cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(element))
                    .ifPresent(nodeDescription -> {
                        NodeTool nodeTool = null;
                        if (SysmlPackage.eINSTANCE.getNamespaceImport().equals(element)) {
                            nodeTool = new NamespaceImportNodeToolProvider(nodeDescription, this.descriptionNameGenerator).create(cache);
                        } else {
                            nodeTool = this.toolDescriptionService.createNodeToolFromDiagramBackground(nodeDescription, element);
                        }
                        nodeTools.add(nodeTool);
                    });
        });

        nodeTools.addAll(this.addCustomTools(cache, name));

        nodeTools.sort(Comparator.comparing(NodeTool::getName));

        return nodeTools.toArray(NodeTool[]::new);
    }

    private List<NodeTool> addCustomTools(IViewDiagramElementFinder cache, String sectionName) {
        var nodeTools = new ArrayList<NodeTool>();
        if (BEHAVIOR_TOOL_SECTION.name().equals(sectionName)) {
            nodeTools.add(new ExhibitStateWithReferenceNodeToolProvider(this.getDescriptionNameGenerator()).create(cache));
            NodeDescription nodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getNamespaceImport())).orElse(null);
            nodeTools.add(new NamespaceImportNodeToolProvider(nodeDescription, this.getDescriptionNameGenerator()).create(cache));
        }
        return nodeTools;
    }
}
