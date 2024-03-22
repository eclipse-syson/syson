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
package org.eclipse.syson.diagram.general.view;

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
import org.eclipse.sirius.components.view.diagram.DiagramElementDescription;
import org.eclipse.sirius.components.view.diagram.DiagramPalette;
import org.eclipse.sirius.components.view.diagram.DiagramToolSection;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.common.view.ViewDiagramElementFinder;
import org.eclipse.syson.diagram.common.view.diagram.AbstractDiagramDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.RequirementSubjectCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.diagram.general.view.edges.DependencyEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.FeatureTypingEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.PartDefinitionOwnedItemEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.PartUsageNestedPartEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.RedefinitionEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.SubclassificationEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.SubsettingEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.CompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.FakeNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.GeneralViewEmptyDiagramNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.GeneralViewNodeDescriptionProviderSwitch;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Description of the General View diagram using the ViewBuilder API from Sirius Web.
 *
 * @author arichard
 */
public class GeneralViewDiagramDescriptionProvider extends AbstractDiagramDescriptionProvider {

    public static final String DESCRIPTION_NAME = "General View";

    public static  final List<EClass> DEFINITIONS = List.of(
            SysmlPackage.eINSTANCE.getActionDefinition(),
            SysmlPackage.eINSTANCE.getAttributeDefinition(),
            SysmlPackage.eINSTANCE.getConstraintDefinition(),
            SysmlPackage.eINSTANCE.getEnumerationDefinition(),
            SysmlPackage.eINSTANCE.getInterfaceDefinition(),
            SysmlPackage.eINSTANCE.getItemDefinition(),
            SysmlPackage.eINSTANCE.getMetadataDefinition(),
            SysmlPackage.eINSTANCE.getPartDefinition(),
            SysmlPackage.eINSTANCE.getPortDefinition(),
            SysmlPackage.eINSTANCE.getRequirementDefinition()
            );

    public static  final List<EClass> USAGES = List.of(
            SysmlPackage.eINSTANCE.getActionUsage(),
            SysmlPackage.eINSTANCE.getAttributeUsage(),
            SysmlPackage.eINSTANCE.getConstraintUsage(),
            SysmlPackage.eINSTANCE.getInterfaceUsage(),
            SysmlPackage.eINSTANCE.getItemUsage(),
            SysmlPackage.eINSTANCE.getPartUsage(),
            SysmlPackage.eINSTANCE.getPortUsage(),
            SysmlPackage.eINSTANCE.getRequirementUsage()
            );

    public static  final Map<EClass, List<EReference>> COMPARTMENTS_WITH_LIST_ITEMS = Map.ofEntries(
            Map.entry(SysmlPackage.eINSTANCE.getActionDefinition(),      List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAction())),
            Map.entry(SysmlPackage.eINSTANCE.getAttributeDefinition(),   List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute())),
            Map.entry(SysmlPackage.eINSTANCE.getConstraintDefinition(),  List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedConstraint())),
            Map.entry(SysmlPackage.eINSTANCE.getEnumerationDefinition(), List.of(SysmlPackage.eINSTANCE.getEnumerationDefinition_EnumeratedValue())),
            Map.entry(SysmlPackage.eINSTANCE.getInterfaceDefinition(),   List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedInterface(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getItemDefinition(),        List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute())),
            Map.entry(SysmlPackage.eINSTANCE.getMetadataDefinition(),    List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getPartDefinition(),        List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getPortDefinition(),        List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), SysmlPackage.eINSTANCE.getDefinition_OwnedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getRequirementDefinition(), List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement())),

            Map.entry(SysmlPackage.eINSTANCE.getActionUsage(),           List.of(SysmlPackage.eINSTANCE.getUsage_NestedAction(), SysmlPackage.eINSTANCE.getUsage_NestedItem())),
            Map.entry(SysmlPackage.eINSTANCE.getAttributeUsage(),        List.of(SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getConstraintUsage(),       List.of(SysmlPackage.eINSTANCE.getUsage_NestedConstraint())),
            Map.entry(SysmlPackage.eINSTANCE.getInterfaceUsage(),        List.of(SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getItemUsage(),             List.of(SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getPartUsage(),             List.of(SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getPortUsage(),             List.of(SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getRequirementUsage(),      List.of(SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint(), SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint()))
            );

    public static final List<ToolSectionDescription> TOOL_SECTIONS = List.of(
            new ToolSectionDescription("Structure", List.of(
                    SysmlPackage.eINSTANCE.getAttributeUsage(),
                    SysmlPackage.eINSTANCE.getAttributeDefinition(),
                    SysmlPackage.eINSTANCE.getEnumerationDefinition(),
                    SysmlPackage.eINSTANCE.getItemUsage(),
                    SysmlPackage.eINSTANCE.getItemDefinition(),
                    SysmlPackage.eINSTANCE.getPackage(),
                    SysmlPackage.eINSTANCE.getPartUsage(),
                    SysmlPackage.eINSTANCE.getPartDefinition()
                    )),
            new ToolSectionDescription("Interconnection", List.of(
                    SysmlPackage.eINSTANCE.getInterfaceUsage(),
                    SysmlPackage.eINSTANCE.getInterfaceDefinition(),
                    SysmlPackage.eINSTANCE.getPortUsage(),
                    SysmlPackage.eINSTANCE.getPortDefinition()
                    )),
            new ToolSectionDescription("ActionFlow", List.of(
                    SysmlPackage.eINSTANCE.getActionUsage(),
                    SysmlPackage.eINSTANCE.getActionDefinition()
                    )),
            new ToolSectionDescription("Requirement", List.of(
                    SysmlPackage.eINSTANCE.getConstraintUsage(),
                    SysmlPackage.eINSTANCE.getConstraintDefinition(),
                    SysmlPackage.eINSTANCE.getRequirementUsage(),
                    SysmlPackage.eINSTANCE.getRequirementDefinition()
                    )),
            new ToolSectionDescription("Extension", List.of(
                    SysmlPackage.eINSTANCE.getMetadataDefinition()
                    ))
            );

    private final IDescriptionNameGenerator nameGenerator = new GVDescriptionNameGenerator();

    @Override
    public RepresentationDescription create(IColorProvider colorProvider) {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPackage());

        var diagramDescriptionBuilder = this.diagramBuilderHelper.newDiagramDescription();
        diagramDescriptionBuilder
                .autoLayout(false)
                .domainType(domainType)
                .name(DESCRIPTION_NAME)
                .titleExpression(DESCRIPTION_NAME);

        var diagramDescription = diagramDescriptionBuilder.build();

        var cache = new ViewDiagramElementFinder();
        var diagramElementDescriptionProviders = new ArrayList<IDiagramElementDescriptionProvider<? extends DiagramElementDescription>>();
        diagramElementDescriptionProviders.add(new FakeNodeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new GeneralViewEmptyDiagramNodeDescriptionProvider(colorProvider));

        diagramElementDescriptionProviders.add(new PartDefinitionOwnedItemEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new PartUsageNestedPartEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new DependencyEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new SubclassificationEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new RedefinitionEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new SubsettingEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new FeatureTypingEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new RequirementSubjectCompartmentNodeDescriptionProvider(colorProvider, this.nameGenerator));
        diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter(), colorProvider, this.nameGenerator));

        // create a node description provider for each element found in a section
        var nodeDescriptionProviderSwitch = new GeneralViewNodeDescriptionProviderSwitch(colorProvider);
        TOOL_SECTIONS.forEach(sectionTool -> {
            sectionTool.elements().forEach(eClass -> {
                diagramElementDescriptionProviders.add(nodeDescriptionProviderSwitch.doSwitch(eClass));
            });
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
        // add requirement subject compartment to requirement usage node
        this.linkRequirementUsageSubjectCompartment(cache);
        // link elements each other
        diagramElementDescriptionProviders.forEach(diagramElementDescriptionProvider -> diagramElementDescriptionProvider.link(diagramDescription, cache));

        var palette = this.createDiagramPalette(cache);
        diagramDescription.setPalette(palette);

        return diagramDescription;
    }

    private void linkRequirementUsageSubjectCompartment(IViewDiagramElementFinder cache) {
        NodeDescription nodeDescription = cache.getNodeDescription(this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getRequirementUsage())).get();
        cache.getNodeDescription(this.nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
    }

    @Override
    protected IDescriptionNameGenerator getNameGenerator() {
        return this.nameGenerator;
    }

    private DiagramPalette createDiagramPalette(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newDiagramPalette()
                .dropNodeTool(this.createDropFromDiagramTool(cache))
                .dropTool(this.createDropFromExplorerTool())
                .toolSections(this.createToolSections(cache))
                .build();
    }

    private DropNodeTool createDropFromDiagramTool(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();

        GeneralViewDiagramDescriptionProvider.TOOL_SECTIONS.forEach(sectionTool -> {
            sectionTool.elements().forEach(element -> {
                var optNodeDescription = cache.getNodeDescription(this.nameGenerator.getNodeName(element));
                acceptedNodeTypes.add(optNodeDescription.get());
            });
        });

        var optPackageNodeDescription = cache.getNodeDescription(this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage()));
        acceptedNodeTypes.add(optPackageNodeDescription.get());

        var dropElementFromDiagram = this.viewBuilderHelper.newChangeContext()
                .expression("aql:droppedElement.dropElementFromDiagram(droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes)");

        return this.diagramBuilderHelper.newDropNodeTool()
                .name("Drop from Diagram")
                .acceptedNodeTypes(acceptedNodeTypes.toArray(NodeDescription[]::new))
                .body(dropElementFromDiagram.build())
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
            nodeTools.add(this.createNodeToolFromPackage(cache.getNodeDescription(this.nameGenerator.getNodeName(definition)).get(), definition));
        });

        nodeTools.sort((nt1, nt2) -> nt1.getName().compareTo(nt2.getName()));

        return nodeTools.toArray(NodeTool[]::new);
    }
}
