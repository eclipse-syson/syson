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
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.RepresentationDescription;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.DiagramToolSectionBuilder;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IDiagramElementDescriptionProvider;
import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.DiagramElementDescription;
import org.eclipse.sirius.components.view.diagram.DiagramPalette;
import org.eclipse.sirius.components.view.diagram.DiagramToolSection;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.DropTool;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.general.view.edges.DependencyEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.FeatureTypingEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.PartDefinitionOwnedItemEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.PartUsageNestedPartEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.RedefinitionEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.SubclassificationEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.SubsettingEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.CompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.EmptyDiagramNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.FakeNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.GeneralViewNodeDescriptionProviderSwitch;
import org.eclipse.syson.diagram.general.view.nodes.PackageNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.DescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Description of the General View diagram using the ViewBuilder API from Sirius Web.
 *
 * @author arichard
 */
public class GeneralViewDiagramDescriptionProvider implements IRepresentationDescriptionProvider {

    public static final String DESCRIPTION_NAME = "General View";

    public static  final List<EClass> DEFINITIONS = List.of(
            SysmlPackage.eINSTANCE.getAttributeDefinition(),
            SysmlPackage.eINSTANCE.getEnumerationDefinition(),
            SysmlPackage.eINSTANCE.getInterfaceDefinition(),
            SysmlPackage.eINSTANCE.getItemDefinition(),
            SysmlPackage.eINSTANCE.getMetadataDefinition(),
            SysmlPackage.eINSTANCE.getPartDefinition(),
            SysmlPackage.eINSTANCE.getPortDefinition());

    public static  final List<EClass> USAGES = List.of(
            SysmlPackage.eINSTANCE.getAttributeUsage(),
            SysmlPackage.eINSTANCE.getInterfaceUsage(),
            SysmlPackage.eINSTANCE.getItemUsage(),
            SysmlPackage.eINSTANCE.getPartUsage(),
            SysmlPackage.eINSTANCE.getPortUsage());

    public static  final Map<EClass, List<EReference>> COMPARTMENTS_WITH_LIST_ITEMS = Map.ofEntries(
            Map.entry(SysmlPackage.eINSTANCE.getAttributeDefinition(),   List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute())),
            Map.entry(SysmlPackage.eINSTANCE.getEnumerationDefinition(), List.of(SysmlPackage.eINSTANCE.getEnumerationDefinition_EnumeratedValue())),
            Map.entry(SysmlPackage.eINSTANCE.getInterfaceDefinition(),   List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedInterface(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getItemDefinition(),        List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute())),
            Map.entry(SysmlPackage.eINSTANCE.getMetadataDefinition(),    List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getPartDefinition(),        List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getPortDefinition(),        List.of(SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), SysmlPackage.eINSTANCE.getDefinition_OwnedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getAttributeUsage(),        List.of(SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getInterfaceUsage(),        List.of(SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getItemUsage(),             List.of(SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getPartUsage(),             List.of(SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getPortUsage(),             List.of(SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedReference()))
            );
    
    public static final Map<String, List<EClass>> TOOL_SECTIONS = Map.ofEntries(
            Map.entry("Structure", List.of(
                    SysmlPackage.eINSTANCE.getAttributeUsage(),
                    SysmlPackage.eINSTANCE.getAttributeDefinition(),
                    SysmlPackage.eINSTANCE.getEnumerationDefinition(),
                    SysmlPackage.eINSTANCE.getItemUsage(),
                    SysmlPackage.eINSTANCE.getItemDefinition(),
                    SysmlPackage.eINSTANCE.getPackage(),
                    SysmlPackage.eINSTANCE.getPartUsage(),
                    SysmlPackage.eINSTANCE.getPartDefinition()
                    )),
            Map.entry("Interconnection", List.of(
                    SysmlPackage.eINSTANCE.getInterfaceUsage(), 
                    SysmlPackage.eINSTANCE.getInterfaceDefinition(), 
                    SysmlPackage.eINSTANCE.getPortUsage(), 
                    SysmlPackage.eINSTANCE.getPortDefinition()
                    )),
            Map.entry("Extension", List.of(
                    SysmlPackage.eINSTANCE.getMetadataDefinition() 
                    ))
            );

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

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

        var cache = new GeneralViewDiagramElementFinder();
        var diagramElementDescriptionProviders = new ArrayList<IDiagramElementDescriptionProvider<? extends DiagramElementDescription>>();
        diagramElementDescriptionProviders.add(new FakeNodeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new EmptyDiagramNodeDescriptionProvider(colorProvider));
   
        diagramElementDescriptionProviders.add(new PartDefinitionOwnedItemEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new PartUsageNestedPartEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new DependencyEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new SubclassificationEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new RedefinitionEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new SubsettingEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new FeatureTypingEdgeDescriptionProvider(colorProvider));

        // create a node description provider for each element found in a section
        var nodeDescriptionProviderSwitch = new GeneralViewNodeDescriptionProviderSwitch(colorProvider);
        TOOL_SECTIONS.forEach((sectionName, elements) -> {
            elements.forEach(eClass -> {
                diagramElementDescriptionProviders.add(nodeDescriptionProviderSwitch.doSwitch(eClass));
            });
        });

        COMPARTMENTS_WITH_LIST_ITEMS.forEach((eClass, listItems) -> {
            listItems.forEach(eReference -> {
                diagramElementDescriptionProviders.add(new CompartmentNodeDescriptionProvider(eClass, eReference, colorProvider));
                diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider));
            });
        });

        diagramElementDescriptionProviders.stream().
                map(IDiagramElementDescriptionProvider::create)
                .forEach(cache::put);
        diagramElementDescriptionProviders.forEach(diagramElementDescriptionProvider -> diagramElementDescriptionProvider.link(diagramDescription, cache));

        var palette = this.createDiagramPalette(cache);
        diagramDescription.setPalette(palette);

        return diagramDescription;
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

        GeneralViewDiagramDescriptionProvider.TOOL_SECTIONS.forEach((sectionName, elements) -> {
            elements.forEach(element -> {
                var optNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(element));
                acceptedNodeTypes.add(optNodeDescription.get());                
            });
        });

        var optPackageNodeDescription = cache.getNodeDescription(PackageNodeDescriptionProvider.NAME);
        acceptedNodeTypes.add(optPackageNodeDescription.get());

        var dropElementFromDiagram = this.viewBuilderHelper.newChangeContext()
                .expression("aql:droppedElement.dropElementFromDiagram(droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes)");

        return this.diagramBuilderHelper.newDropNodeTool()
                .name("Drop from Diagram")
                .acceptedNodeTypes(acceptedNodeTypes.toArray(NodeDescription[]::new))
                .body(dropElementFromDiagram.build())
                .build();
    }

    private DropTool createDropFromExplorerTool() {
        var dropElementFromExplorer = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.dropElementFromExplorer(editingContext, diagramContext, selectedNode, convertedNodes)");

        return this.diagramBuilderHelper.newDropTool()
                .name("Drop from Explorer")
                .body(dropElementFromExplorer.build())
                .build();
    }

    private DiagramToolSection createElementsToolSection(IViewDiagramElementFinder cache) {
        var nodeTools = new ArrayList<NodeTool>();

        GeneralViewDiagramDescriptionProvider.TOOL_SECTIONS.forEach((sectionName, elements) -> {
            elements.forEach(element -> {
                var optNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(element));
                nodeTools.add(this.createNodeToolFromPackage(optNodeDescription.get(), element));                
            });
        });

        nodeTools.add(this.createNodeToolFromPackage(cache.getNodeDescription(PackageNodeDescriptionProvider.NAME).get(), SysmlPackage.eINSTANCE.getPackage()));

        nodeTools.sort((nt1, nt2) -> nt1.getName().compareTo(nt2.getName()));

        return this.diagramBuilderHelper.newDiagramToolSection()
                .name("Create")
                .nodeTools(nodeTools.toArray(NodeTool[]::new))
                .build();
    }

    private NodeTool createNodeToolFromPackage(NodeDescription nodeDescription, EClassifier eClass) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var callElementInitializerService = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.elementInitializer()");

        var setValue = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getElement_DeclaredName().getName())
                .valueExpression(eClass.getName());

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(setValue.build(), callElementInitializerService.build());

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(NodeContainmentKind.CHILD_NODE)
                .elementDescription(nodeDescription)
                .parentViewExpression("aql:selectedNode")
                .semanticElementExpression("aql:newInstance")
                .variableName("newInstanceView");

        var changeContexMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newOwningMembership")
                .children(createEClassInstance.build(), createView.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getOwningMembership()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newOwningMembership")
                .children(changeContexMembership.build());

        return builder
                .name(DescriptionNameGenerator.getCreationToolName(eClass))
                .iconURLsExpression("/icons/full/obj16/" + eClass.getName() + ".svg")
                .body(createMembership.build())
                .build();
    }

    private DiagramToolSection addElementsToolSection(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newDiagramToolSection()
                .name("Add")
                .nodeTools(this.addExistingElementsTool())
                .build();
    }

    private NodeTool addExistingElementsTool() {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var addExistingelements = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.addExistingElements(editingContext, diagramContext, selectedNode, convertedNodes)");

        return builder
                .name("Add existing elements")
                .iconURLsExpression("/icons/AddExistingElements.svg")
                .body(addExistingelements.build())
                .build();
    }
    
    private DiagramToolSection[] createToolSections(IViewDiagramElementFinder cache) {
        var sections = new ArrayList<DiagramToolSection>();
        
        TOOL_SECTIONS.forEach((sectionName, elements) -> {
            DiagramToolSectionBuilder sectionBuilder = this.diagramBuilderHelper.newDiagramToolSection()
                    .name(sectionName)
                    .nodeTools(this.createElementsOfToolSection(cache, elements));
            sections.add(sectionBuilder.build());
        });
        
        sections.add(this.addElementsToolSection(cache));
        
        return sections.toArray(DiagramToolSection[]::new);
    }
    
    private NodeTool[] createElementsOfToolSection(IViewDiagramElementFinder cache, List<EClass> elements) {
        var nodeTools = new ArrayList<NodeTool>();

        elements.forEach(definition -> {
            nodeTools.add(this.createNodeToolFromPackage(cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(definition)).get(), definition));
        });

        nodeTools.sort((nt1, nt2) -> nt1.getName().compareTo(nt2.getName()));

        return nodeTools.toArray(NodeTool[]::new);
    }
}
