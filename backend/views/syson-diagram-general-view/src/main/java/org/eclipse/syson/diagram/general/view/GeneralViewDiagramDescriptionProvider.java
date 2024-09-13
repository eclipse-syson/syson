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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.RepresentationDescription;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramToolSectionBuilder;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IDiagramElementDescriptionProvider;
import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.ArrangeLayoutDirection;
import org.eclipse.sirius.components.view.diagram.DiagramElementDescription;
import org.eclipse.sirius.components.view.diagram.DiagramPalette;
import org.eclipse.sirius.components.view.diagram.DiagramToolSection;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.common.view.ViewDiagramElementFinder;
import org.eclipse.syson.diagram.common.view.edges.AnnotationEdgeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ActionFlowCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.AnnotatingNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.DecisionActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.DoneActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ForkActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.InheritedCompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.JoinActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.MergeActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.MergedReferencesCompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StartActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StateTransitionCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StatesCompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StatesCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.common.view.tools.ExhibitStateWithReferenceNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.diagram.general.view.edges.AllocateEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.DefinitionOwnedActionUsageEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.DefinitionOwnedUsageEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.DependencyEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.FeatureTypingEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.NestedActorEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.RedefinitionEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.SubclassificationEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.SubsettingEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.SuccessionEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.TransitionEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.UsageNestedActionUsageEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.edges.UsageNestedUsageEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.ActionItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.ActionsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.ActorNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.AllocationDefinitionEndsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.CompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.FakeNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.GeneralViewEmptyDiagramNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.GeneralViewNodeDescriptionProviderSwitch;
import org.eclipse.syson.diagram.general.view.nodes.ReferencingPerformActionUsageNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.RequirementDefinitionActorsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.RequirementDefinitionSubjectCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.RequirementUsageActorsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.RequirementUsageSubjectCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.UseCaseDefinitionActorsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.UseCaseDefinitionObjectiveRequirementCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.UseCaseDefinitionSubjectCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.UseCaseUsageActorsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.UseCaseUsageObjectiveRequirementCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.UseCaseUsageSubjectCompartmentNodeDescriptionProvider;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Description of the General View diagram using the ViewBuilder API from Sirius Web.
 *
 * @author arichard
 */
public class GeneralViewDiagramDescriptionProvider implements IRepresentationDescriptionProvider {

    public static final String DESCRIPTION_NAME = "General View";

    public static  final List<EClass> DEFINITIONS = List.of(
            SysmlPackage.eINSTANCE.getActionDefinition(),
            SysmlPackage.eINSTANCE.getAllocationDefinition(),
            SysmlPackage.eINSTANCE.getAttributeDefinition(),
            SysmlPackage.eINSTANCE.getConstraintDefinition(),
            SysmlPackage.eINSTANCE.getEnumerationDefinition(),
            SysmlPackage.eINSTANCE.getInterfaceDefinition(),
            SysmlPackage.eINSTANCE.getItemDefinition(),
            SysmlPackage.eINSTANCE.getMetadataDefinition(),
            SysmlPackage.eINSTANCE.getOccurrenceDefinition(),
            SysmlPackage.eINSTANCE.getPartDefinition(),
            SysmlPackage.eINSTANCE.getPortDefinition(),
            SysmlPackage.eINSTANCE.getRequirementDefinition(),
            SysmlPackage.eINSTANCE.getUseCaseDefinition(),
            SysmlPackage.eINSTANCE.getStateDefinition()
            );

    public static  final List<EClass> USAGES = List.of(
            SysmlPackage.eINSTANCE.getAcceptActionUsage(),
            SysmlPackage.eINSTANCE.getActionUsage(),
            SysmlPackage.eINSTANCE.getAssignmentActionUsage(),
            SysmlPackage.eINSTANCE.getAllocationUsage(),
            SysmlPackage.eINSTANCE.getAttributeUsage(),
            SysmlPackage.eINSTANCE.getConstraintUsage(),
            SysmlPackage.eINSTANCE.getExhibitStateUsage(),
            SysmlPackage.eINSTANCE.getInterfaceUsage(),
            SysmlPackage.eINSTANCE.getItemUsage(),
            SysmlPackage.eINSTANCE.getOccurrenceUsage(),
            SysmlPackage.eINSTANCE.getPartUsage(),
            SysmlPackage.eINSTANCE.getPerformActionUsage(),
            SysmlPackage.eINSTANCE.getPortUsage(),
            SysmlPackage.eINSTANCE.getRequirementUsage(),
            SysmlPackage.eINSTANCE.getUseCaseUsage(),
            SysmlPackage.eINSTANCE.getStateUsage()
            );

    public static final List<EClass> ANNOTATINGS = List.of(
            SysmlPackage.eINSTANCE.getDocumentation(), SysmlPackage.eINSTANCE.getComment());

    public static  final Map<EClass, List<EReference>> COMPARTMENTS_WITH_LIST_ITEMS = Map.ofEntries(
            Map.entry(SysmlPackage.eINSTANCE.getActionDefinition(),      List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction())),
            Map.entry(SysmlPackage.eINSTANCE.getAllocationDefinition(),  List.of(SysmlPackage.eINSTANCE.getElement_Documentation())),
            Map.entry(SysmlPackage.eINSTANCE.getAttributeDefinition(),   List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute())),
            Map.entry(SysmlPackage.eINSTANCE.getConstraintDefinition(),  List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedConstraint())),
            Map.entry(SysmlPackage.eINSTANCE.getEnumerationDefinition(), List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getEnumerationDefinition_EnumeratedValue())),
            Map.entry(SysmlPackage.eINSTANCE.getExhibitStateUsage(),     List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedState())),
            Map.entry(SysmlPackage.eINSTANCE.getInterfaceDefinition(),   List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedInterface(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getItemDefinition(),        List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute())),
            Map.entry(SysmlPackage.eINSTANCE.getMetadataDefinition(),    List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getOccurrenceDefinition(),  List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedOccurrence())),
            Map.entry(SysmlPackage.eINSTANCE.getPartDefinition(),        List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), SysmlPackage.eINSTANCE.getDefinition_OwnedState())),
            Map.entry(SysmlPackage.eINSTANCE.getPortDefinition(),        List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), SysmlPackage.eINSTANCE.getDefinition_OwnedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getRequirementDefinition(), List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement(), SysmlPackage.eINSTANCE.getRequirementDefinition_AssumedConstraint(), SysmlPackage.eINSTANCE.getRequirementDefinition_RequiredConstraint())),
            Map.entry(SysmlPackage.eINSTANCE.getUseCaseDefinition(),     List.of(SysmlPackage.eINSTANCE.getElement_Documentation())),
            Map.entry(SysmlPackage.eINSTANCE.getStateDefinition(),       List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedState())),
            Map.entry(SysmlPackage.eINSTANCE.getAcceptActionUsage(),     List.of(SysmlPackage.eINSTANCE.getElement_Documentation())),
            Map.entry(SysmlPackage.eINSTANCE.getActionUsage(),           List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), SysmlPackage.eINSTANCE.getUsage_NestedAction())),
            Map.entry(SysmlPackage.eINSTANCE.getAllocationUsage(),       List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAllocation())),
            Map.entry(SysmlPackage.eINSTANCE.getAttributeUsage(),        List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getConstraintUsage(),       List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedConstraint())),
            Map.entry(SysmlPackage.eINSTANCE.getInterfaceUsage(),        List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getItemUsage(),             List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getOccurrenceUsage(),       List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedOccurrence())),
            Map.entry(SysmlPackage.eINSTANCE.getPartUsage(),             List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedPort(), SysmlPackage.eINSTANCE.getUsage_NestedAction(), SysmlPackage.eINSTANCE.getUsage_NestedState())),
            Map.entry(SysmlPackage.eINSTANCE.getPerformActionUsage(),    List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), SysmlPackage.eINSTANCE.getUsage_NestedAction())),
            Map.entry(SysmlPackage.eINSTANCE.getPortUsage(),             List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getRequirementUsage(),      List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint(), SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint())),
            Map.entry(SysmlPackage.eINSTANCE.getStateUsage(),            List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedState())),
            Map.entry(SysmlPackage.eINSTANCE.getUseCaseUsage(),          List.of(SysmlPackage.eINSTANCE.getElement_Documentation()))
            );

    public static  final Map<EClass, List<EReference>> COMPARTMENTS_WITH_MERGED_LIST_ITEMS = Map.ofEntries(
            Map.entry(SysmlPackage.eINSTANCE.getExhibitStateUsage(),    List.of(SysmlPackage.eINSTANCE.getStateUsage_EntryAction(), SysmlPackage.eINSTANCE.getStateUsage_DoAction(), SysmlPackage.eINSTANCE.getStateUsage_ExitAction())),
            Map.entry(SysmlPackage.eINSTANCE.getStateDefinition(),      List.of(SysmlPackage.eINSTANCE.getStateDefinition_EntryAction(), SysmlPackage.eINSTANCE.getStateDefinition_DoAction(), SysmlPackage.eINSTANCE.getStateDefinition_ExitAction())),
            Map.entry(SysmlPackage.eINSTANCE.getStateUsage(),           List.of(SysmlPackage.eINSTANCE.getStateUsage_EntryAction(), SysmlPackage.eINSTANCE.getStateUsage_DoAction(), SysmlPackage.eINSTANCE.getStateUsage_ExitAction()))

            );

    public static final ToolSectionDescription STRUCTURE_TOOL_SECTIONS = new ToolSectionDescription("Structure", List.of(
            SysmlPackage.eINSTANCE.getAttributeUsage(),
            SysmlPackage.eINSTANCE.getAttributeDefinition(),
            SysmlPackage.eINSTANCE.getEnumerationDefinition(),
            SysmlPackage.eINSTANCE.getItemUsage(),
            SysmlPackage.eINSTANCE.getItemDefinition(),
            SysmlPackage.eINSTANCE.getPackage(),
            SysmlPackage.eINSTANCE.getPartUsage(),
            SysmlPackage.eINSTANCE.getPartDefinition()
            ));

    public static final ToolSectionDescription INTERCONNECTION_TOOL_SECTIONS = new ToolSectionDescription("Interconnection", List.of(
            SysmlPackage.eINSTANCE.getAllocationUsage(),
            SysmlPackage.eINSTANCE.getAllocationDefinition(),
            SysmlPackage.eINSTANCE.getInterfaceUsage(),
            SysmlPackage.eINSTANCE.getInterfaceDefinition(),
            SysmlPackage.eINSTANCE.getPortUsage(),
            SysmlPackage.eINSTANCE.getPortDefinition()
            ));

    public static final ToolSectionDescription ACTION_FLOW_TOOL_SECTIONS = new ToolSectionDescription("ActionFlow", List.of(
            SysmlPackage.eINSTANCE.getAcceptActionUsage(),
            SysmlPackage.eINSTANCE.getActionUsage(),
            SysmlPackage.eINSTANCE.getActionDefinition(),
            SysmlPackage.eINSTANCE.getAssignmentActionUsage()
            ));

    public static final ToolSectionDescription REQUIREMENT_TOOL_SECTIONS =  new ToolSectionDescription("Requirement", List.of(
            SysmlPackage.eINSTANCE.getConstraintUsage(),
            SysmlPackage.eINSTANCE.getConstraintDefinition(),
            SysmlPackage.eINSTANCE.getRequirementUsage(),
            SysmlPackage.eINSTANCE.getRequirementDefinition()
            ));

    public static final ToolSectionDescription ANALYSIS_TOOL_SECTIONS = new ToolSectionDescription("Analysis", List.of(
            SysmlPackage.eINSTANCE.getUseCaseUsage(),
            SysmlPackage.eINSTANCE.getUseCaseDefinition()
            ));

    public static final ToolSectionDescription TEMPORAL_TOOL_SECTIONS = new ToolSectionDescription("Temporal", List.of(
            SysmlPackage.eINSTANCE.getOccurrenceUsage(),
            SysmlPackage.eINSTANCE.getOccurrenceDefinition()
            ));

    public static final ToolSectionDescription EXTENSION_TOOL_SECTIONS = new ToolSectionDescription("Extension", List.of(
            SysmlPackage.eINSTANCE.getMetadataDefinition()
            ));

    public static final ToolSectionDescription STATE_TRANSITION_TOOL_SECTIONS = new ToolSectionDescription("StateTransition", List.of(
            SysmlPackage.eINSTANCE.getExhibitStateUsage(),
            SysmlPackage.eINSTANCE.getStateDefinition(),
            SysmlPackage.eINSTANCE.getStateUsage()));

    public static final List<ToolSectionDescription> TOOL_SECTIONS = List.of(
            STRUCTURE_TOOL_SECTIONS,
            INTERCONNECTION_TOOL_SECTIONS,
            ACTION_FLOW_TOOL_SECTIONS,
            REQUIREMENT_TOOL_SECTIONS,
            ANALYSIS_TOOL_SECTIONS,
            TEMPORAL_TOOL_SECTIONS,
            EXTENSION_TOOL_SECTIONS,
            STATE_TRANSITION_TOOL_SECTIONS);

    /**
     * Following elements have additional creation tools one for each direction (in, out, and inout).
     */
    public static final List<EClass> DIRECTIONAL_ELEMENTS = List.of(
            SysmlPackage.eINSTANCE.getItemUsage(),
            SysmlPackage.eINSTANCE.getPortUsage());

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator = new GVDescriptionNameGenerator();

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
        var diagramElementDescriptionProviders = new ArrayList<IDiagramElementDescriptionProvider<? extends DiagramElementDescription>>();
        diagramElementDescriptionProviders.add(new FakeNodeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new GeneralViewEmptyDiagramNodeDescriptionProvider(colorProvider));

        diagramElementDescriptionProviders.add(new DefinitionOwnedActionUsageEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new UsageNestedActionUsageEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        this.addDefinitionOwnedUsageEdgeDescriptionProviders(colorProvider, diagramElementDescriptionProviders);
        this.addUsageCompositeEdgeProviders(colorProvider, diagramElementDescriptionProviders);
        this.addEdgeDescriptionProviders(colorProvider, diagramElementDescriptionProviders);
        this.addCustomNodeDescriptionProviders(colorProvider, diagramElementDescriptionProviders);

        ANNOTATINGS.forEach(annotating -> {
            diagramElementDescriptionProviders.add(new AnnotatingNodeDescriptionProvider(annotating, colorProvider, this.getDescriptionNameGenerator()));
        });

        // create a node description provider for each element found in a section
        var nodeDescriptionProviderSwitch = new GeneralViewNodeDescriptionProviderSwitch(colorProvider);
        TOOL_SECTIONS.forEach(sectionTool -> {
            sectionTool.elements().forEach(eClass -> {
                diagramElementDescriptionProviders.add(nodeDescriptionProviderSwitch.doSwitch(eClass));
            });
        });
        // create nodes that are not in a section
        diagramElementDescriptionProviders.add(nodeDescriptionProviderSwitch.doSwitch(SysmlPackage.eINSTANCE.getPerformActionUsage()));

        COMPARTMENTS_WITH_LIST_ITEMS.forEach((eClass, listItems) -> {
            listItems.forEach(eReference -> {
                if (SysmlPackage.eINSTANCE.getExhibitStateUsage().equals(eClass) && SysmlPackage.eINSTANCE.getUsage_NestedState().equals(eReference)) {
                    diagramElementDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    diagramElementDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    diagramElementDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), false));
                    diagramElementDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), false));
                } else if (SysmlPackage.eINSTANCE.getStateUsage().equals(eClass) && SysmlPackage.eINSTANCE.getUsage_NestedState().equals(eReference)) {
                    diagramElementDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    diagramElementDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    diagramElementDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), false));
                    diagramElementDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), false));
                } else if (SysmlPackage.eINSTANCE.getStateDefinition().equals(eClass) && SysmlPackage.eINSTANCE.getDefinition_OwnedState().equals(eReference)) {
                    diagramElementDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    diagramElementDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    diagramElementDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), false));
                    diagramElementDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), false));
                } else if ((SysmlPackage.eINSTANCE.getPartUsage().equals(eClass) && SysmlPackage.eINSTANCE.getUsage_NestedAction().equals(eReference))
                        || (SysmlPackage.eINSTANCE.getPartDefinition().equals(eClass) && SysmlPackage.eINSTANCE.getDefinition_OwnedAction().equals(eReference))) {
                    diagramElementDescriptionProviders.add(new ActionItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                    diagramElementDescriptionProviders.add(new CompartmentNodeDescriptionProvider(eClass, eReference, colorProvider));
                    diagramElementDescriptionProviders.add(new InheritedCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                } else {
                    diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                    diagramElementDescriptionProviders.add(new CompartmentNodeDescriptionProvider(eClass, eReference, colorProvider));
                    diagramElementDescriptionProviders.add(new InheritedCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                }
            });
        });

        COMPARTMENTS_WITH_MERGED_LIST_ITEMS.forEach((eClass, listItems) -> {
            listItems.forEach(eReference -> {
                diagramElementDescriptionProviders.add(new ActionsCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
            });
            diagramElementDescriptionProviders.add(new MergedReferencesCompartmentItemNodeDescriptionProvider(eClass, listItems, colorProvider, this.getDescriptionNameGenerator()));
        });

        diagramElementDescriptionProviders.stream()
                .map(IDiagramElementDescriptionProvider::create)
                .forEach(cache::put);

        // link elements each other
        diagramElementDescriptionProviders.forEach(diagramElementDescriptionProvider -> diagramElementDescriptionProvider.link(diagramDescription, cache));

        // link custom compartments. This must be done after the general link to make sure custom compartments are
        // defined after regular ones.
        this.linkRequirementSubjectCompartment(cache);
        this.linkRequirementActorsCompartment(cache);
        this.linkUseCaseSubjectCompartment(cache);
        this.linkUseCaseActorsCompartment(cache);
        this.linkUseCaseObjectiveRequirementCompartment(cache);
        this.linkAllocationDefinitionEndsCompartment(cache);
        this.linkStatesCompartment(cache);

        var palette = this.createDiagramPalette(cache);
        diagramDescription.setPalette(palette);

        return diagramDescription;
    }

    protected IDescriptionNameGenerator getDescriptionNameGenerator() {
        return this.descriptionNameGenerator;
    }

    private void addCustomNodeDescriptionProviders(IColorProvider colorProvider,
            ArrayList<IDiagramElementDescriptionProvider<? extends DiagramElementDescription>> diagramElementDescriptionProviders) {
        diagramElementDescriptionProviders.add(new RequirementUsageSubjectCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new RequirementDefinitionSubjectCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementDefinition(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new RequirementUsageActorsCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new RequirementDefinitionActorsCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementDefinition(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_ActorParameter(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new UseCaseUsageSubjectCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new UseCaseDefinitionSubjectCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getUseCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getUseCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_SubjectParameter(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new UseCaseUsageActorsCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new UseCaseDefinitionActorsCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getUseCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ActorParameter(), colorProvider,
                this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders
                .add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getUseCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_ActorParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new UseCaseUsageObjectiveRequirementCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new UseCaseDefinitionObjectiveRequirementCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getUseCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ObjectiveRequirement(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getUseCaseDefinition(),
                SysmlPackage.eINSTANCE.getCaseDefinition_ObjectiveRequirement(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new AllocationDefinitionEndsCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getAllocationDefinition(),
                SysmlPackage.eINSTANCE.getConnectionDefinition_ConnectionEnd(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new ActionFlowCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction(), colorProvider,
                this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new ActionFlowCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new ActionFlowCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new ActionFlowCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction(), colorProvider,
                this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders
                .add(new ActionFlowCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), colorProvider,
                        this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new StateTransitionCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new StateTransitionCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new StartActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new DoneActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new JoinActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new ForkActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new MergeActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new DecisionActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new ReferencingPerformActionUsageNodeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new ActorNodeDescriptionProvider(colorProvider));
    }

    private void addEdgeDescriptionProviders(IColorProvider colorProvider, ArrayList<IDiagramElementDescriptionProvider<? extends DiagramElementDescription>> diagramElementDescriptionProviders) {
        diagramElementDescriptionProviders.add(new AnnotationEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new DependencyEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new SubclassificationEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new RedefinitionEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new SubsettingEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new FeatureTypingEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new AllocateEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new SuccessionEdgeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new TransitionEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
    }

    private void addDefinitionOwnedUsageEdgeDescriptionProviders(IColorProvider colorProvider,
            ArrayList<IDiagramElementDescriptionProvider<? extends DiagramElementDescription>> diagramElementDescriptionProviders) {
        diagramElementDescriptionProviders.add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getAllocationUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedAllocation(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getAttributeUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getConstraintUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedConstraint(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getInterfaceUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedInterface(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem(), colorProvider,
                this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPart(), colorProvider,
                this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), colorProvider,
                this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedState(), colorProvider,
                this.getDescriptionNameGenerator()));
    }

    private void addUsageCompositeEdgeProviders(IColorProvider colorProvider, ArrayList<IDiagramElementDescriptionProvider<? extends DiagramElementDescription>> diagramElementDescriptionProviders) {
        diagramElementDescriptionProviders.add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getAllocationUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAllocation(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getAttributeUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), colorProvider,
                this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getConstraintUsage(), SysmlPackage.eINSTANCE.getUsage_NestedConstraint(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(
                new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getOccurrenceUsage(), SysmlPackage.eINSTANCE.getUsage_NestedOccurrence(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(
                new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(
                new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getUsage_NestedRequirement(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getUsage_NestedConstraint(),
                colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(
                new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedUsage(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(
                new NestedActorEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getUseCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ActorParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(
                new NestedActorEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getUseCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_ActorParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(
                new NestedActorEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(
                new NestedActorEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_ActorParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
    }

    private void linkRequirementSubjectCompartment(IViewDiagramElementFinder cache) {
        NodeDescription requirementUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getRequirementUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(requirementUsageNodeDescription.getReusedChildNodeDescriptions()::add);
        NodeDescription requirementDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getRequirementDefinition())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getRequirementDefinition(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter()))
                .ifPresent(requirementDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private void linkRequirementActorsCompartment(IViewDiagramElementFinder cache) {
        NodeDescription requirementUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getRequirementUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter()))
                .ifPresent(requirementUsageNodeDescription.getReusedChildNodeDescriptions()::add);
        NodeDescription requirementDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getRequirementDefinition())).get();
        cache.getNodeDescription(
                this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_ActorParameter()))
                .ifPresent(requirementDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private void linkUseCaseSubjectCompartment(IViewDiagramElementFinder cache) {
        NodeDescription useCaseUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getUseCaseUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getUseCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter()))
            .ifPresent(useCaseUsageNodeDescription.getReusedChildNodeDescriptions()::add);
        NodeDescription useCaseDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getUseCaseDefinition())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getUseCaseDefinition(),
                SysmlPackage.eINSTANCE.getCaseDefinition_SubjectParameter()))
            .ifPresent(useCaseDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private void linkUseCaseActorsCompartment(IViewDiagramElementFinder cache) {
        NodeDescription useCaseUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getUseCaseUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getUseCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ActorParameter()))
                .ifPresent(useCaseUsageNodeDescription.getReusedChildNodeDescriptions()::add);
        NodeDescription useCaseDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getUseCaseDefinition())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getUseCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_ActorParameter()))
                .ifPresent(useCaseDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private void linkUseCaseObjectiveRequirementCompartment(IViewDiagramElementFinder cache) {
        NodeDescription useCaseUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getUseCaseUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getUseCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ObjectiveRequirement()))
            .ifPresent(useCaseUsageNodeDescription.getReusedChildNodeDescriptions()::add);
        NodeDescription useCaseDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getUseCaseDefinition())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getUseCaseDefinition(),
                SysmlPackage.eINSTANCE.getCaseDefinition_ObjectiveRequirement()))
            .ifPresent(useCaseDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private void linkAllocationDefinitionEndsCompartment(IViewDiagramElementFinder cache) {
        NodeDescription allocationDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getAllocationDefinition())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getAllocationDefinition(), SysmlPackage.eINSTANCE.getConnectionDefinition_ConnectionEnd()))
            .ifPresent(allocationDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private void linkStatesCompartment(IViewDiagramElementFinder cache) {
        NodeDescription stateDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getStateDefinition())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getStateDefinition(),
                SysmlPackage.eINSTANCE.getDefinition_OwnedState()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME)
                .ifPresent(stateDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getStateDefinition(),
                SysmlPackage.eINSTANCE.getDefinition_OwnedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(stateDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
        NodeDescription stateUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getStateUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(),
                SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME)
                .ifPresent(stateUsageNodeDescription.getReusedChildNodeDescriptions()::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(),
                SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(stateUsageNodeDescription.getReusedChildNodeDescriptions()::add);
        NodeDescription exhibitStateUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getExhibitStateUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getExhibitStateUsage(),
                SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME)
                .ifPresent(exhibitStateUsageNodeDescription.getReusedChildNodeDescriptions()::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getExhibitStateUsage(),
                SysmlPackage.eINSTANCE.getUsage_NestedState()) +
                StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(exhibitStateUsageNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private DiagramPalette createDiagramPalette(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newDiagramPalette()
                .dropNodeTool(this.createDropFromDiagramTool(cache))
                .dropTool(this.toolDescriptionService.createDropFromExplorerTool())
                .toolSections(this.createToolSections(cache))
                .build();
    }

    private DropNodeTool createDropFromDiagramTool(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();

        GeneralViewDiagramDescriptionProvider.TOOL_SECTIONS.forEach(sectionTool -> {
            sectionTool.elements().forEach(element -> {
                cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(element)).ifPresent(acceptedNodeTypes::add);
            });
        });

        var optPackageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPackage()));
        acceptedNodeTypes.add(optPackageNodeDescription.get());

        var dropElementFromDiagram = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression("droppedElement", "dropElementFromDiagram",
                        List.of("droppedNode", "targetElement", "targetNode", "editingContext", "diagramContext", "convertedNodes")));

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
                    .nodeTools(this.createElementsOfToolSection(cache, sectionTool.name(), sectionTool.elements()));
            sections.add(sectionBuilder.build());
        });

        // add extra section for existing elements
        sections.add(this.toolDescriptionService.addElementsDiagramToolSection());

        return sections.toArray(DiagramToolSection[]::new);
    }

    private NodeTool[] createElementsOfToolSection(IViewDiagramElementFinder cache, String toolSectionName, List<EClass> elements) {
        var nodeTools = new ArrayList<NodeTool>();

        elements.forEach(element -> {
            nodeTools.addAll(this.getCreationToolsForElement(element, toolSectionName, cache));
        });

        nodeTools.addAll(this.addCustomTools(cache, toolSectionName));

        Collections.sort(nodeTools, Comparator.comparing(NodeTool::getName));

        return nodeTools.toArray(NodeTool[]::new);
    }

    private List<NodeTool> getCreationToolsForElement(EClass element, String toolSectionName, IViewDiagramElementFinder cache) {
        List<NodeTool> elementCreationTools = new ArrayList<>();
        NodeDescription nodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(element)).orElse(null);
        if (nodeDescription != null) {
            NodeTool nodeTool = this.toolDescriptionService.createNodeToolFromDiagramBackground(nodeDescription, element);
            nodeTool.setPreconditionExpression(AQLUtils.getSelfServiceCallExpression("toolShouldBeAvailable", "'" + element.getName() + "'"));
            elementCreationTools.add(nodeTool);
            if (DIRECTIONAL_ELEMENTS.contains(element)) {
                // this element has a direction, we need to create also 3 more tools for each direction.
                elementCreationTools.add(this.toolDescriptionService.createNodeToolFromDiagramWithDirection(nodeDescription, element, FeatureDirectionKind.IN));
                elementCreationTools.add(this.toolDescriptionService.createNodeToolFromDiagramWithDirection(nodeDescription, element, FeatureDirectionKind.OUT));
                elementCreationTools.add(this.toolDescriptionService.createNodeToolFromDiagramWithDirection(nodeDescription, element, FeatureDirectionKind.INOUT));
            }
        }
        return elementCreationTools;
    }

    private List<NodeTool> addCustomTools(IViewDiagramElementFinder cache, String sectionName) {
        var nodeTools = new ArrayList<NodeTool>();
        if ("StateTransition".equals(sectionName)) {
            nodeTools.add(new ExhibitStateWithReferenceNodeToolProvider(this.getDescriptionNameGenerator()).create(cache));
        }
        return nodeTools;
    }
}
