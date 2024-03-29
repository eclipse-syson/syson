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
package org.eclipse.syson.diagram.requirement.view;

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
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.common.view.ViewDiagramElementFinder;
import org.eclipse.syson.diagram.common.view.diagram.AbstractDiagramDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.RequirementDefinitionSubjectCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.RequirementUsageSubjectCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.diagram.requirement.view.edges.DependencyEdgeDescriptionProvider;
import org.eclipse.syson.diagram.requirement.view.edges.FeatureTypingEdgeDescriptionProvider;
import org.eclipse.syson.diagram.requirement.view.edges.RedefinitionEdgeDescriptionProvider;
import org.eclipse.syson.diagram.requirement.view.edges.SubclassificationEdgeDescriptionProvider;
import org.eclipse.syson.diagram.requirement.view.edges.SubsettingEdgeDescriptionProvider;
import org.eclipse.syson.diagram.requirement.view.nodes.CompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.requirement.view.nodes.DefinitionNodeDescriptionProvider;
import org.eclipse.syson.diagram.requirement.view.nodes.FakeNodeDescriptionProvider;
import org.eclipse.syson.diagram.requirement.view.nodes.PackageNodeDescriptionProvider;
import org.eclipse.syson.diagram.requirement.view.nodes.RequirementViewEmptyDiagramNodeDescriptionProvider;
import org.eclipse.syson.diagram.requirement.view.nodes.UsageNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Description of the Requirement View diagram using the ViewBuilder API from Sirius Web.
 *
 * @author Jerome Gout
 */
public class RequirementViewDiagramDescriptionProvider extends AbstractDiagramDescriptionProvider {

    public static final String DESCRIPTION_NAME = "Requirement View";

    public static  final List<EClass> DEFINITIONS = List.of(
            SysmlPackage.eINSTANCE.getConstraintDefinition(),
            SysmlPackage.eINSTANCE.getRequirementDefinition()
            );

    public static  final List<EClass> USAGES = List.of(
            SysmlPackage.eINSTANCE.getConstraintUsage(),
            SysmlPackage.eINSTANCE.getRequirementUsage()
            );

    public static  final Map<EClass, List<EReference>> COMPARTMENTS_WITH_LIST_ITEMS = Map.ofEntries(
            Map.entry(SysmlPackage.eINSTANCE.getConstraintDefinition(),      List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedConstraint())),
            Map.entry(SysmlPackage.eINSTANCE.getRequirementDefinition(),     List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(),
                    SysmlPackage.eINSTANCE.getRequirementDefinition_AssumedConstraint(),
                    SysmlPackage.eINSTANCE.getRequirementDefinition_RequiredConstraint())),
            Map.entry(SysmlPackage.eINSTANCE.getConstraintUsage(),           List.of(SysmlPackage.eINSTANCE.getUsage_NestedConstraint())),
            Map.entry(SysmlPackage.eINSTANCE.getRequirementUsage(),          List.of(SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint(),
                    SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint()))
            );

    public static final List<ToolSectionDescription> TOOL_SECTIONS = List.of(
            new ToolSectionDescription("Requirement", List.of(
                    SysmlPackage.eINSTANCE.getConstraintDefinition(),
                    SysmlPackage.eINSTANCE.getConstraintUsage(),
                    SysmlPackage.eINSTANCE.getPackage(),
                    SysmlPackage.eINSTANCE.getRequirementUsage(),
                    SysmlPackage.eINSTANCE.getRequirementDefinition()
                    ))
            );

    private final IDescriptionNameGenerator nameGenerator = new RVDescriptionNameGenerator();

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
        var diagramElementDescriptionProviders =  new ArrayList<IDiagramElementDescriptionProvider<? extends DiagramElementDescription>>();
        diagramElementDescriptionProviders.add(new DependencyEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new FeatureTypingEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new RedefinitionEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new SubclassificationEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new SubsettingEdgeDescriptionProvider(colorProvider));

        diagramElementDescriptionProviders.add(new FakeNodeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new RequirementViewEmptyDiagramNodeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new PackageNodeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new RequirementUsageSubjectCompartmentNodeDescriptionProvider(colorProvider, this.nameGenerator));
        diagramElementDescriptionProviders.add(new RequirementDefinitionSubjectCompartmentNodeDescriptionProvider(colorProvider, this.nameGenerator));
        diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter(), colorProvider, this.nameGenerator));
        diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementDefinition(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter(), colorProvider, this.nameGenerator));


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
        // add requirement subject compartment to requirement usage/definition nodes
        this.linkRequirementUsageSubjectCompartment(cache);
        diagramElementDescriptionProviders.forEach(diagramElementDescriptionProvider -> diagramElementDescriptionProvider.link(diagramDescription, cache));

        diagramDescription.setPalette(this.createDiagramPalette(cache));

        return diagramDescription;
    }

    private void linkRequirementUsageSubjectCompartment(IViewDiagramElementFinder cache) {
        NodeDescription nodeDescription = cache.getNodeDescription(this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getRequirementUsage())).get();
        cache.getNodeDescription(this.nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(nodeDescription.getReusedChildNodeDescriptions()::add);
        NodeDescription requirementDefinitionNodeDescription = cache.getNodeDescription(this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getRequirementDefinition())).get();
        cache.getNodeDescription(this.nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementDefinition(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter()))
                .ifPresent(requirementDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
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
            nodeTools.add(this.createNodeToolFromPackage(cache.getNodeDescription(this.nameGenerator.getNodeName(definition)).get(), definition));
        });

        nodeTools.sort((nt1, nt2) -> nt1.getName().compareTo(nt2.getName()));

        return nodeTools.toArray(NodeTool[]::new);
    }
}
