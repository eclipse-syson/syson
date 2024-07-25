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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.RepresentationDescription;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IDiagramElementDescriptionProvider;
import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.DiagramElementDescription;
import org.eclipse.sirius.components.view.diagram.DiagramPalette;
import org.eclipse.syson.diagram.common.view.ViewDiagramElementFinder;
import org.eclipse.syson.diagram.common.view.nodes.ActionFlowCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.DecisionActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.DoneActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ForkActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.InheritedCompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.JoinActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.MergeActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StartActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.interconnection.view.edges.AllocateEdgeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.edges.BindingConnectorAsUsageEdgeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.edges.FlowConnectionUsageEdgeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.edges.InterfaceUsageEdgeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.edges.SuccessionEdgeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.ChildUsageNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.ChildrenUsageCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.CompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.FakeNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.FirstLevelChildUsageNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.ItemUsageBorderNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.PortUsageBorderNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.RootNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.RootPortUsageBorderNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Description of the Interconnection View for Usage diagram using the ViewBuilder API from Sirius Web.
 *
 * @author arichard
 */
public class InterconnectionViewDiagramDescriptionProvider implements IRepresentationDescriptionProvider {

    public static final String DESCRIPTION_NAME = "Interconnection View";

    public static final Map<EClass, List<EReference>> COMPARTMENTS_WITH_LIST_ITEMS = Map.ofEntries(
            Map.entry(SysmlPackage.eINSTANCE.getPartUsage(), List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute())),
            Map.entry(SysmlPackage.eINSTANCE.getActionUsage(), List.of(SysmlPackage.eINSTANCE.getElement_Documentation())));

    public static final Map<EClass, List<EReference>> COMPARTMENTS_WITH_FREE_FORM_ITEMS = Map.ofEntries(
            Map.entry(SysmlPackage.eINSTANCE.getPartUsage(), List.of(SysmlPackage.eINSTANCE.getUsage_NestedPart())),
            Map.entry(SysmlPackage.eINSTANCE.getActionUsage(), List.of(SysmlPackage.eINSTANCE.getUsage_NestedAction())));

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final IVDescriptionNameGenerator nameGenerator = new IVDescriptionNameGenerator();

    private final ToolDescriptionService toolDescriptionService = new ToolDescriptionService(this.nameGenerator);

    @Override
    public RepresentationDescription create(IColorProvider colorProvider) {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement());

        var diagramDescriptionBuilder = this.diagramBuilderHelper.newDiagramDescription();
        diagramDescriptionBuilder
                .autoLayout(false)
                .domainType(domainType)
                .preconditionExpression(domainType)
                .preconditionExpression(AQLUtils.getSelfServiceCallExpression("canCreateDiagram"))
                .name(DESCRIPTION_NAME)
                .titleExpression(DESCRIPTION_NAME);

        var diagramDescription = diagramDescriptionBuilder.build();

        var cache = new ViewDiagramElementFinder();
        var diagramElementDescriptionProviders = new ArrayList<IDiagramElementDescriptionProvider<? extends DiagramElementDescription>>();
        diagramElementDescriptionProviders.addAll(List.of(
                new FakeNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()),
                new RootNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()),
                new FirstLevelChildUsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartUsage(), "getPartUsages", colorProvider, this.getDescriptionNameGenerator()),
                new FirstLevelChildUsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getActionUsage(), "getActionUsages", colorProvider, this.getDescriptionNameGenerator()),
                new ChildUsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartUsage(), colorProvider, this.getDescriptionNameGenerator()),
                new ChildUsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getActionUsage(), colorProvider, this.getDescriptionNameGenerator()),
                new ChildUsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getAcceptActionUsage(), colorProvider, this.getDescriptionNameGenerator()),
                new ChildUsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), colorProvider, this.getDescriptionNameGenerator()),
                new RootPortUsageBorderNodeDescriptionProvider("getPortUsages", colorProvider, this.getDescriptionNameGenerator()),
                new PortUsageBorderNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()),
                new ItemUsageBorderNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()),
                new BindingConnectorAsUsageEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()),
                new AllocateEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()),
                new InterfaceUsageEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()),
                new FlowConnectionUsageEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()),
                new SuccessionEdgeDescriptionProvider(colorProvider)
        ));

        COMPARTMENTS_WITH_LIST_ITEMS.forEach((eClass, listItems) -> {
            listItems.forEach(eReference -> {
                diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                diagramElementDescriptionProviders.add(new CompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                diagramElementDescriptionProviders.add(new InheritedCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
            });
        });

        COMPARTMENTS_WITH_FREE_FORM_ITEMS.forEach((eClass, listItems) -> {
            listItems.forEach(eReference -> {
                if (Objects.equals(SysmlPackage.eINSTANCE.getActionUsage(), eClass) && Objects.equals(SysmlPackage.eINSTANCE.getUsage_NestedAction(), eReference)) {
                    diagramElementDescriptionProviders.add(new ActionFlowCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                } else {
                    diagramElementDescriptionProviders.add(new ChildrenUsageCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                }
            });
        });

        diagramElementDescriptionProviders.addAll(this.getActionFlowNodeDescriptionProviders(colorProvider));

        diagramElementDescriptionProviders.stream()
                .map(IDiagramElementDescriptionProvider::create)
                .forEach(cache::put);
        diagramElementDescriptionProviders.forEach(diagramElementDescriptionProvider -> diagramElementDescriptionProvider.link(diagramDescription, cache));

        var palette = this.createDiagramPalette(cache);
        diagramDescription.setPalette(palette);

        return diagramDescription;
    }

    protected IVDescriptionNameGenerator getDescriptionNameGenerator() {
        return this.nameGenerator;
    }

    private DiagramPalette createDiagramPalette(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newDiagramPalette()
                .dropTool(this.toolDescriptionService.createDropFromExplorerTool())
                .build();
    }

    private List<IDiagramElementDescriptionProvider<? extends DiagramElementDescription>> getActionFlowNodeDescriptionProviders(IColorProvider colorProvider) {
        List<IDiagramElementDescriptionProvider<? extends DiagramElementDescription>> providers = new ArrayList<>();
        providers.add(new StartActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        providers.add(new DoneActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        providers.add(new JoinActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        providers.add(new ForkActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        providers.add(new MergeActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        providers.add(new DecisionActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        return providers;
    }
}
