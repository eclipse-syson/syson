/*******************************************************************************
 * Copyright (c) 2023, 2026 Obeo.
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
package org.eclipse.syson.standard.diagrams.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.view.RepresentationDescription;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramToolSectionBuilder;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IDiagramElementDescriptionProvider;
import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.ArrangeLayoutDirection;
import org.eclipse.sirius.components.view.diagram.DiagramPalette;
import org.eclipse.sirius.components.view.diagram.DiagramToolSection;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.ListLayoutStrategyDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.common.view.ViewDiagramElementFinder;
import org.eclipse.syson.diagram.common.view.edges.AnnotationEdgeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ActionFlowCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.AnnotatingNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.DecisionActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.DoneActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ForkActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ImportedPackageNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.InheritedCompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.InterconnectionCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.JoinActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.MergeActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.SatisfyRequirementCompartmentItemNodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.SatisfyRequirementCompartmentNodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.StartActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StateTransitionCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StatesCompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StatesCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.common.view.services.description.ToolConstants;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.common.view.tools.ExhibitStateWithReferenceNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.NamespaceImportNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.SetAsViewToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.standard.diagrams.view.edges.AllocateEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.BindingConnectorAsUsageEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.ConnectionUsageEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.DefinitionOwnedUsageEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.DependencyEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.FeatureTypingEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.FeatureValueEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.FlowUsageEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.IncludeUseCaseDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.InterfaceUsageEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.NestedActorEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.NestedSubjectEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.RedefinitionEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.ReferenceSubsettingEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.SatisfyRequirementEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.SubclassificationEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.SubsettingEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.SuccessionEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.TransitionEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.edges.UsageNestedUsageEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.ActionDefinitionParameterBorderNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.ActionDefinitionParametersCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.ActionItemNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.ActionUsageParametersCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.ActorNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.AllocationDefinitionEndsCompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.AllocationDefinitionEndsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.CaseDefinitionActorsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.CaseDefinitionObjectiveRequirementCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.CaseDefinitionSubjectCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.CaseUsageActorsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.CaseUsageObjectiveRequirementCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.CaseUsageSubjectCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.CompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.ConnectionDefinitionEndsCompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.ConnectionDefinitionEndsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.FakeNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.GeneralViewEmptyDiagramNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.InheritedPortUsageBorderNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.InterfaceDefinitionEndsCompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.InterfaceDefinitionEndsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.ObjectiveDocumentationCompartmentItemNodeDescription;
import org.eclipse.syson.standard.diagrams.view.nodes.PerformActionsCompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.PerformActionsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.PortDefinitionOwnedItemBorderNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.PortDefinitionOwnedItemCompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.PortUsageBorderNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.PortUsageNestedItemBorderNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.PortUsageNestedItemCompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.ReferenceUsageBorderNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.RequirementDefinitionActorsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.RequirementDefinitionStakeholdersCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.RequirementDefinitionSubjectCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.RequirementUsageActorsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.RequirementUsageStakeholdersCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.RequirementUsageSubjectCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.nodes.SDVNodeDescriptionProviderSwitch;
import org.eclipse.syson.standard.diagrams.view.nodes.SubjectNodeDescriptionProvider;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.StandardDiagramsConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Description of the SysMLv2 Standard Diagram Views using the ViewBuilder API from Sirius Web.
 *
 * @author arichard
 */
public class SDVDiagramDescriptionProvider implements IRepresentationDescriptionProvider {

    public static final String DESCRIPTION_NAME = "General View";

    // @formatter:off

    public static  final List<EClass> DEFINITIONS = List.of(
            SysmlPackage.eINSTANCE.getActionDefinition(),
            SysmlPackage.eINSTANCE.getAllocationDefinition(),
            SysmlPackage.eINSTANCE.getAttributeDefinition(),
            SysmlPackage.eINSTANCE.getCaseDefinition(),
            SysmlPackage.eINSTANCE.getConcernDefinition(),
            SysmlPackage.eINSTANCE.getConnectionDefinition(),
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
            SysmlPackage.eINSTANCE.getCaseUsage(),
            SysmlPackage.eINSTANCE.getConcernUsage(),
            SysmlPackage.eINSTANCE.getConstraintUsage(),
            SysmlPackage.eINSTANCE.getExhibitStateUsage(),
            SysmlPackage.eINSTANCE.getInterfaceUsage(),
            SysmlPackage.eINSTANCE.getItemUsage(),
            SysmlPackage.eINSTANCE.getOccurrenceUsage(),
            SysmlPackage.eINSTANCE.getPartUsage(),
            SysmlPackage.eINSTANCE.getPerformActionUsage(),
            SysmlPackage.eINSTANCE.getPortUsage(),
            SysmlPackage.eINSTANCE.getRequirementUsage(),
            SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(),
            SysmlPackage.eINSTANCE.getUseCaseUsage(),
            SysmlPackage.eINSTANCE.getStateUsage(),
            SysmlPackage.eINSTANCE.getViewUsage()
            );

    public static final List<EClass> ANNOTATINGS = List.of(
            SysmlPackage.eINSTANCE.getComment(),
            SysmlPackage.eINSTANCE.getDocumentation(),
            SysmlPackage.eINSTANCE.getTextualRepresentation()
            );

    public static  final Map<EClass, List<EReference>> COMPARTMENTS_WITH_LIST_ITEMS = Map.ofEntries(
            Map.entry(SysmlPackage.eINSTANCE.getAcceptActionUsage(),       List.of(SysmlPackage.eINSTANCE.getElement_Documentation())),
            Map.entry(SysmlPackage.eINSTANCE.getActionDefinition(),        List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction())),
            Map.entry(SysmlPackage.eINSTANCE.getActionUsage(),             List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), SysmlPackage.eINSTANCE.getUsage_NestedAction())),
            Map.entry(SysmlPackage.eINSTANCE.getAllocationDefinition(),    List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedPart())),
            Map.entry(SysmlPackage.eINSTANCE.getAllocationUsage(),         List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAllocation())),
            Map.entry(SysmlPackage.eINSTANCE.getAttributeDefinition(),     List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute())),
            Map.entry(SysmlPackage.eINSTANCE.getAttributeUsage(),          List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getCaseDefinition(),          List.of(SysmlPackage.eINSTANCE.getElement_Documentation())),
            Map.entry(SysmlPackage.eINSTANCE.getCaseUsage(),               List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getConcernDefinition(),       List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement(), SysmlPackage.eINSTANCE.getRequirementDefinition_AssumedConstraint(), SysmlPackage.eINSTANCE.getRequirementDefinition_RequiredConstraint())),
            Map.entry(SysmlPackage.eINSTANCE.getConcernUsage(),            List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint(), SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint(), SysmlPackage.eINSTANCE.getUsage_NestedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getConnectionDefinition(),    List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedPart())),
            Map.entry(SysmlPackage.eINSTANCE.getConstraintDefinition(),    List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedConstraint())),
            Map.entry(SysmlPackage.eINSTANCE.getConstraintUsage(),         List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedConstraint(), SysmlPackage.eINSTANCE.getUsage_NestedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getEnumerationDefinition(),   List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getEnumerationDefinition_EnumeratedValue())),
            Map.entry(SysmlPackage.eINSTANCE.getExhibitStateUsage(),       List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAction(), SysmlPackage.eINSTANCE.getUsage_NestedState())),
            Map.entry(SysmlPackage.eINSTANCE.getInterfaceUsage(),          List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getInterfaceDefinition(),     List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedInterface(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), SysmlPackage.eINSTANCE.getDefinition_OwnedPart())),
            Map.entry(SysmlPackage.eINSTANCE.getItemDefinition(),          List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute())),
            Map.entry(SysmlPackage.eINSTANCE.getItemUsage(),               List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedPort(), SysmlPackage.eINSTANCE.getUsage_NestedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getMetadataDefinition(),      List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedReference())),
            Map.entry(SysmlPackage.eINSTANCE.getOccurrenceDefinition(),    List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedOccurrence())),
            Map.entry(SysmlPackage.eINSTANCE.getOccurrenceUsage(),         List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedOccurrence())),
            Map.entry(SysmlPackage.eINSTANCE.getPartDefinition(),          List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), SysmlPackage.eINSTANCE.getDefinition_OwnedState(), SysmlPackage.eINSTANCE.getDefinition_OwnedPart())),
            Map.entry(SysmlPackage.eINSTANCE.getPartUsage(),               List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedAction(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), SysmlPackage.eINSTANCE.getUsage_NestedPort(), SysmlPackage.eINSTANCE.getUsage_NestedState())),
            Map.entry(SysmlPackage.eINSTANCE.getPerformActionUsage(),      List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), SysmlPackage.eINSTANCE.getUsage_NestedAction())),
            Map.entry(SysmlPackage.eINSTANCE.getPortDefinition(),          List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), SysmlPackage.eINSTANCE.getDefinition_OwnedReference(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem())),
            Map.entry(SysmlPackage.eINSTANCE.getPortUsage(),               List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedReference(), SysmlPackage.eINSTANCE.getUsage_NestedPort(), SysmlPackage.eINSTANCE.getUsage_NestedItem())),
            Map.entry(SysmlPackage.eINSTANCE.getRequirementDefinition(),   List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter(), SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement(), SysmlPackage.eINSTANCE.getRequirementDefinition_AssumedConstraint(), SysmlPackage.eINSTANCE.getRequirementDefinition_RequiredConstraint())),
            Map.entry(SysmlPackage.eINSTANCE.getRequirementUsage(),        List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter(), SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint(), SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint(), SysmlPackage.eINSTANCE.getUsage_NestedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint(), SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint(), SysmlPackage.eINSTANCE.getUsage_NestedPort())),
            Map.entry(SysmlPackage.eINSTANCE.getStateDefinition(),         List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), SysmlPackage.eINSTANCE.getDefinition_OwnedState())),
            Map.entry(SysmlPackage.eINSTANCE.getStateUsage(),              List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAction(), SysmlPackage.eINSTANCE.getUsage_NestedState())),
            Map.entry(SysmlPackage.eINSTANCE.getUseCaseDefinition(),       List.of(SysmlPackage.eINSTANCE.getElement_Documentation())),
            Map.entry(SysmlPackage.eINSTANCE.getUseCaseUsage(),            List.of(SysmlPackage.eINSTANCE.getElement_Documentation(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), SysmlPackage.eINSTANCE.getUsage_NestedPort()))
            );

    public static final ToolSectionDescription REQUIREMENTS_TOOL_SECTION = new ToolSectionDescription(ToolConstants.REQUIREMENTS, List.of(
            SysmlPackage.eINSTANCE.getConcernUsage(),
            SysmlPackage.eINSTANCE.getConcernDefinition(),
            SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(),
            SysmlPackage.eINSTANCE.getRequirementUsage(),
            SysmlPackage.eINSTANCE.getRequirementDefinition(),
            SysmlPackage.eINSTANCE.getConstraintUsage(),
            SysmlPackage.eINSTANCE.getConstraintDefinition()
            ));

    public static final ToolSectionDescription STRUCTURE_TOOL_SECTION = new ToolSectionDescription(ToolConstants.STRUCTURE, List.of(
            SysmlPackage.eINSTANCE.getAttributeUsage(),
            SysmlPackage.eINSTANCE.getAttributeDefinition(),
            SysmlPackage.eINSTANCE.getConnectionDefinition(),
            SysmlPackage.eINSTANCE.getEnumerationDefinition(),
            SysmlPackage.eINSTANCE.getInterfaceUsage(),
            SysmlPackage.eINSTANCE.getInterfaceDefinition(),
            SysmlPackage.eINSTANCE.getItemUsage(),
            SysmlPackage.eINSTANCE.getItemDefinition(),
            SysmlPackage.eINSTANCE.getPackage(),
            SysmlPackage.eINSTANCE.getPartUsage(),
            SysmlPackage.eINSTANCE.getPartDefinition(),
            SysmlPackage.eINSTANCE.getPortUsage(),
            SysmlPackage.eINSTANCE.getPortDefinition(),
            SysmlPackage.eINSTANCE.getReferenceUsage()
            ));

    public static final ToolSectionDescription BEHAVIOR_TOOL_SECTION = new ToolSectionDescription(ToolConstants.BEHAVIOR, List.of(
            SysmlPackage.eINSTANCE.getAllocationUsage(),
            SysmlPackage.eINSTANCE.getAllocationDefinition(),
            SysmlPackage.eINSTANCE.getAcceptActionUsage(),
            SysmlPackage.eINSTANCE.getActionUsage(),
            SysmlPackage.eINSTANCE.getActionDefinition(),
            SysmlPackage.eINSTANCE.getAssignmentActionUsage(),
            SysmlPackage.eINSTANCE.getExhibitStateUsage(),
            SysmlPackage.eINSTANCE.getOccurrenceUsage(),
            SysmlPackage.eINSTANCE.getOccurrenceDefinition(),
            SysmlPackage.eINSTANCE.getStateUsage(),
            SysmlPackage.eINSTANCE.getStateDefinition(),
            SysmlPackage.eINSTANCE.getViewUsage()
            ));

    public static final ToolSectionDescription ANALYSIS_TOOL_SECTION = new ToolSectionDescription(ToolConstants.ANALYSIS, List.of(
            SysmlPackage.eINSTANCE.getCaseUsage(),
            SysmlPackage.eINSTANCE.getCaseDefinition(),
            SysmlPackage.eINSTANCE.getUseCaseUsage(),
            SysmlPackage.eINSTANCE.getUseCaseDefinition()
            ));

    public static final ToolSectionDescription EXTENSION_TOOL_SECTION = new ToolSectionDescription(ToolConstants.EXTENSION, List.of(
            SysmlPackage.eINSTANCE.getMetadataDefinition()
            ));

    public static final ToolSectionDescription VIEW_AS_TOOL_SECTION = new ToolSectionDescription(ToolConstants.VIEW_AS, List.of(
            ));

    public static final List<ToolSectionDescription> TOOL_SECTIONS = List.of(
            REQUIREMENTS_TOOL_SECTION,
            STRUCTURE_TOOL_SECTION,
            BEHAVIOR_TOOL_SECTION,
            ANALYSIS_TOOL_SECTION,
            EXTENSION_TOOL_SECTION,
            VIEW_AS_TOOL_SECTION);

    /**
     * Following elements have additional creation tools one for each direction (in, out, and inout).
     */
    public static final List<EClass> DIRECTIONAL_ELEMENTS = List.of(
            SysmlPackage.eINSTANCE.getItemUsage(),
            SysmlPackage.eINSTANCE.getPortUsage());

    // @formatter:on

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private final ToolDescriptionService toolDescriptionService = new ToolDescriptionService(this.descriptionNameGenerator);

    @Override
    public RepresentationDescription create(IColorProvider colorProvider) {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getNamespace());

        var diagramDescriptionBuilder = this.diagramBuilderHelper.newDiagramDescription();
        diagramDescriptionBuilder
                .arrangeLayoutDirection(ArrangeLayoutDirection.DOWN)
                .autoLayout(false)
                .domainType(domainType)
                .preconditionExpression(ServiceMethod.of0(ViewCreateService::canCreateDiagram).aqlSelf())
                .name(DESCRIPTION_NAME)
                .titleExpression("aql:'view'+ Sequence{self.existingViewUsagesCountForRepresentationCreation(), 1}->sum()");

        var diagramDescription = diagramDescriptionBuilder.build();

        var cache = new ViewDiagramElementFinder();
        var diagramElementDescriptionProviders = this.createDiagramElementDescriptionProviders(colorProvider);

        diagramElementDescriptionProviders.forEach(provider -> {
            var diagramElementDescription = provider.create();
            cache.put(diagramElementDescription);
        });

        // link elements each other
        diagramElementDescriptionProviders.forEach(diagramElementDescriptionProvider -> diagramElementDescriptionProvider.link(diagramDescription, cache));

        // link custom compartments. This must be done after the general link to make sure custom compartments are
        // defined after regular ones.
        this.linkRequirementSubjectCompartment(cache);
        this.linkRequirementActorsCompartment(cache);
        this.linkRequirementStakeholdersCompartment(cache);
        this.linkConcernSubjectCompartment(cache);
        this.linkConcernActorsCompartment(cache);
        this.linkConcernStakeholdersCompartment(cache);
        this.linkSatisfyRequirementUsageSubjectCompartment(cache);
        this.linkSatisfyRequirementUsageActorsCompartment(cache);
        this.linkSatisfyRequirementUsageStakeholdersCompartment(cache);
        this.linkCaseSubjectCompartment(cache);
        this.linkCaseActorsCompartment(cache);
        this.linkCaseObjectiveRequirementCompartment(cache);
        this.linkAllocationDefinitionEndsCompartment(cache);
        this.linkConnectionDefinitionEndsCompartment(cache);
        this.linkInterfaceDefinitionEndsCompartment(cache);
        this.linkStatesCompartment(cache);
        this.linkActionParametersCompartment(cache);
        this.linkPartPerformActionsCompartment(cache);
        this.linkActionPerformActionsCompartment(cache);
        this.linkInterconnectionCompartment(cache);
        this.linkSatisfyRequirementsCompartment(cache);

        var palette = this.createDiagramPalette(cache);
        diagramDescription.setPalette(palette);

        return diagramDescription;
    }

    protected IDescriptionNameGenerator getDescriptionNameGenerator() {
        return this.descriptionNameGenerator;
    }

    private List<IDiagramElementDescriptionProvider<?>> createDiagramElementDescriptionProviders(IColorProvider colorProvider) {
        var diagramElementDescriptionProviders = new ArrayList<IDiagramElementDescriptionProvider<?>>();

        diagramElementDescriptionProviders.add(new FakeNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new GeneralViewEmptyDiagramNodeDescriptionProvider(colorProvider));
        diagramElementDescriptionProviders.add(new PortUsageBorderNodeDescriptionProvider(SysmlPackage.eINSTANCE.getUsage_NestedPort(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new PortUsageBorderNodeDescriptionProvider(SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new InheritedPortUsageBorderNodeDescriptionProvider(SysmlPackage.eINSTANCE.getUsage_NestedPort(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders
                .add(new InheritedPortUsageBorderNodeDescriptionProvider(SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new ActionDefinitionParameterBorderNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new PortDefinitionOwnedItemBorderNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        diagramElementDescriptionProviders.add(new PortUsageNestedItemBorderNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));

        diagramElementDescriptionProviders.add(new ReferenceUsageBorderNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));

        diagramElementDescriptionProviders.addAll(this.createAllDefinitionOwnedUsageEdgeDescriptionProviders(colorProvider));
        diagramElementDescriptionProviders.addAll(this.createAllUsageCompositeEdgeDescriptionProviders(colorProvider));
        diagramElementDescriptionProviders.addAll(this.createAllEdgeDescriptionProviders(colorProvider));
        diagramElementDescriptionProviders.addAll(this.createAllCompartmentNodeDescriptionProviders(colorProvider));
        diagramElementDescriptionProviders.addAll(this.createAllCustomNodeDescriptionProviders(colorProvider));

        ANNOTATINGS.forEach(annotating -> {
            diagramElementDescriptionProviders.add(new AnnotatingNodeDescriptionProvider(annotating, colorProvider, this.getDescriptionNameGenerator()));
        });

        // create a node description provider for each element found in a section
        var nodeDescriptionProviderSwitch = new SDVNodeDescriptionProviderSwitch(colorProvider);
        TOOL_SECTIONS.forEach(sectionTool -> {
            sectionTool.elements().forEach(eClass -> {
                diagramElementDescriptionProviders.add(nodeDescriptionProviderSwitch.doSwitch(eClass));
            });
        });

        // create nodes that are not in a section
        diagramElementDescriptionProviders.add(nodeDescriptionProviderSwitch.doSwitch(SysmlPackage.eINSTANCE.getPerformActionUsage()));

        return diagramElementDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createAllCompartmentNodeDescriptionProviders(IColorProvider colorProvider) {
        final List<IDiagramElementDescriptionProvider<?>> compartmentNodeDescriptionProviders = new ArrayList<>();

        // Compartment "subject" (SubjectParameter) is defined for:
        // CaseDefinition, CaseUsage, ConcernDefinition, ConcernUsage, RequirementDefinition, RequirementUsage,
        // SatisfyRequirementUsage
        compartmentNodeDescriptionProviders.addAll(this.createCompartmentsForSubjectParameter(colorProvider));

        // Compartment "actors" (ActorParameter) is defined for:
        // CaseDefinition, CaseUsage, ConcernDefinition, ConcernUsage, RequirementDefinition, RequirementUsage,
        // SatisfyRequirementUsage
        compartmentNodeDescriptionProviders.addAll(this.createCompartmentsForActorParameter(colorProvider));

        // Compartment "stakeholders" (StakeholderParameter) is defined for:
        // ConcernDefinition, ConcernUsage, RequirementDefinition, RequirementUsage, SatisfyRequirementUsage
        compartmentNodeDescriptionProviders.addAll(this.createCompartmentsForStakeholderParameter(colorProvider));

        // Compartment "objective" (ObjectiveRequirement) is defined for:
        // CaseDefinition, CaseUsage
        compartmentNodeDescriptionProviders.addAll(this.createCompartmentsForObjectiveRequirement(colorProvider));

        // Compartment "ends" (ConnectionEnd) is defined for:
        // AllocationDefinition, ConnectionDefinition, InterfaceDefinition
        compartmentNodeDescriptionProviders.addAll(this.createCompartmentsForConnectionEnd(colorProvider));

        // Compartment "actions" (OwnedAction) is defined for:
        // ActionDefinition
        compartmentNodeDescriptionProviders.addAll(this.createCompartmentsForOwnedAction(colorProvider));

        // Compartment "parameters" (reference usages) is defined for:
        // ActionDefinition, ActionUsage
        compartmentNodeDescriptionProviders.addAll(this.createCompartmentsForActionParameters(colorProvider));

        // Compartment "action flow" (NestedAction) is defined for:
        // ActionUsage, PartUsage, PerformActionUsage
        compartmentNodeDescriptionProviders.addAll(this.createCompartmentsForNestedAction(colorProvider));

        // Compartment "interconnection" (many usages and defintions) is defined for:
        // PartUsage, PartDefinition
        compartmentNodeDescriptionProviders.addAll(this.createInterconnectionCompartment(colorProvider));

        // Compartment "satisfy requirements" (many usages and defintions) is defined for:
        // PartUsage, PartDefinition
        compartmentNodeDescriptionProviders.addAll(this.createSatisfyRequirementsCompartments(colorProvider));

        // Compartment "state transition" (OwnedState) is defined for:
        // PartDefinition
        compartmentNodeDescriptionProviders.add(new StateTransitionCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState(),
                colorProvider, this.getDescriptionNameGenerator()));

        // Compartment "state transition" (NestedState) is defined for:
        // PartUsage
        compartmentNodeDescriptionProviders.add(new StateTransitionCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState(),
                colorProvider, this.getDescriptionNameGenerator()));

        // Compartment "state transition" (NestedState) is defined for:
        // State Usage
        compartmentNodeDescriptionProviders.add(new StateTransitionCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState(),
                colorProvider, this.getDescriptionNameGenerator()));

        // Compartment "state transition" (OwnedState) is defined for:
        // State Definition
        compartmentNodeDescriptionProviders.add(new StateTransitionCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState(),
                colorProvider, this.getDescriptionNameGenerator()));

        // Compartment "state transition" (NestedState) is defined for:
        // Exhibit State Usage
        compartmentNodeDescriptionProviders.add(new StateTransitionCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getExhibitStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState(),
                colorProvider, this.getDescriptionNameGenerator()));

        compartmentNodeDescriptionProviders.addAll(this.createCompartmentsForListItems(colorProvider));

        return compartmentNodeDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createCompartmentsForListItems(IColorProvider colorProvider) {
        final List<IDiagramElementDescriptionProvider<?>> compartmentNodeDescriptionProviders = new ArrayList<>();

        COMPARTMENTS_WITH_LIST_ITEMS.forEach((eClass, eReferences) -> {
            eReferences.forEach(eReference -> {
                if (SysmlPackage.eINSTANCE.getExhibitStateUsage().equals(eClass) && SysmlPackage.eINSTANCE.getUsage_NestedState().equals(eReference)) {
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), false));
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), false));
                } else if (SysmlPackage.eINSTANCE.getStateUsage().equals(eClass) && SysmlPackage.eINSTANCE.getUsage_NestedState().equals(eReference)) {
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), false));
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), false));
                } else if (SysmlPackage.eINSTANCE.getStateDefinition().equals(eClass) && SysmlPackage.eINSTANCE.getDefinition_OwnedState().equals(eReference)) {
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), false));
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), false));
                } else if ((SysmlPackage.eINSTANCE.getStateUsage().equals(eClass) && SysmlPackage.eINSTANCE.getUsage_NestedAction().equals(eReference))
                        || (SysmlPackage.eINSTANCE.getStateDefinition().equals(eClass) && SysmlPackage.eINSTANCE.getDefinition_OwnedAction().equals(eReference))) {
                    compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                    compartmentNodeDescriptionProviders.add(new CompartmentNodeDescriptionProvider(eClass, eReference, colorProvider));
                    compartmentNodeDescriptionProviders.add(new InheritedCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                    compartmentNodeDescriptionProviders.add(new PerformActionsCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                    compartmentNodeDescriptionProviders.add(new PerformActionsCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                } else if ((SysmlPackage.eINSTANCE.getPartUsage().equals(eClass) && SysmlPackage.eINSTANCE.getUsage_NestedAction().equals(eReference))
                        || (SysmlPackage.eINSTANCE.getPartDefinition().equals(eClass) && SysmlPackage.eINSTANCE.getDefinition_OwnedAction().equals(eReference))) {
                    compartmentNodeDescriptionProviders.add(new ActionItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                    compartmentNodeDescriptionProviders.add(new CompartmentNodeDescriptionProvider(eClass, eReference, colorProvider));
                    compartmentNodeDescriptionProviders.add(new InheritedCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                    compartmentNodeDescriptionProviders.add(new PerformActionsCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                    compartmentNodeDescriptionProviders.add(new PerformActionsCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                } else if ((SysmlPackage.eINSTANCE.getPartUsage().equals(eClass) && SysmlPackage.eINSTANCE.getUsage_NestedState().equals(eReference))
                        || (SysmlPackage.eINSTANCE.getPartDefinition().equals(eClass) && SysmlPackage.eINSTANCE.getDefinition_OwnedState().equals(eReference))) {
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), true));
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), false));
                    compartmentNodeDescriptionProviders.add(new StatesCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator(), false));
                } else if ((SysmlPackage.eINSTANCE.getActionUsage().equals(eClass) && SysmlPackage.eINSTANCE.getUsage_NestedAction().equals(eReference))
                        || (SysmlPackage.eINSTANCE.getActionDefinition().equals(eClass) && SysmlPackage.eINSTANCE.getDefinition_OwnedAction().equals(eReference))) {
                    compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                    compartmentNodeDescriptionProviders.add(new CompartmentNodeDescriptionProvider(eClass, eReference, colorProvider));
                    compartmentNodeDescriptionProviders.add(new InheritedCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                    compartmentNodeDescriptionProviders.add(new PerformActionsCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                    compartmentNodeDescriptionProviders.add(new PerformActionsCompartmentNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                } else if (SysmlPackage.eINSTANCE.getPortDefinition().equals(eClass) && SysmlPackage.eINSTANCE.getDefinition_OwnedItem().equals(eReference)) {
                    compartmentNodeDescriptionProviders.add(new CompartmentNodeDescriptionProvider(eClass, eReference, colorProvider));
                    compartmentNodeDescriptionProviders.add(new PortDefinitionOwnedItemCompartmentItemNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
                }  else if (SysmlPackage.eINSTANCE.getPortUsage().equals(eClass) && SysmlPackage.eINSTANCE.getUsage_NestedItem().equals(eReference)) {
                    compartmentNodeDescriptionProviders.add(new CompartmentNodeDescriptionProvider(eClass, eReference, colorProvider));
                    compartmentNodeDescriptionProviders.add(new PortUsageNestedItemCompartmentItemNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
                } else {
                    compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                    compartmentNodeDescriptionProviders.add(new CompartmentNodeDescriptionProvider(eClass, eReference, colorProvider));
                    compartmentNodeDescriptionProviders.add(new InheritedCompartmentItemNodeDescriptionProvider(eClass, eReference, colorProvider, this.getDescriptionNameGenerator()));
                }
            });
        });

        return compartmentNodeDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createCompartmentsForNestedAction(IColorProvider colorProvider) {
        final List<IDiagramElementDescriptionProvider<?>> compartmentNodeDescriptionProviders = new ArrayList<>();

        compartmentNodeDescriptionProviders.add(new ActionFlowCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction(), colorProvider,
                this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new ActionFlowCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction(), colorProvider,
                this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new ActionFlowCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction(),
                colorProvider, this.getDescriptionNameGenerator()));

        return compartmentNodeDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createInterconnectionCompartment(IColorProvider colorProvider) {
        final List<IDiagramElementDescriptionProvider<?>> compartmentNodeDescriptionProviders = new ArrayList<>();

        compartmentNodeDescriptionProviders.add(new InterconnectionCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartUsage(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new InterconnectionCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartDefinition(), colorProvider, this.getDescriptionNameGenerator()));

        return compartmentNodeDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createCompartmentsForOwnedAction(IColorProvider colorProvider) {
        final List<IDiagramElementDescriptionProvider<?>> compartmentNodeDescriptionProviders = new ArrayList<>();
        compartmentNodeDescriptionProviders.add(new ActionFlowCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(),
                colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders
                .add(new ActionFlowCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), colorProvider,
                        this.getDescriptionNameGenerator()));

        return compartmentNodeDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createCompartmentsForActionParameters(IColorProvider colorProvider) {
        final List<IDiagramElementDescriptionProvider<?>> compartmentNodeDescriptionProviders = new ArrayList<>();
        compartmentNodeDescriptionProviders.add(new ActionDefinitionParametersCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders
                .add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedReference(), colorProvider,
                this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new ActionUsageParametersCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders
                .add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedReference(), colorProvider,
                        this.getDescriptionNameGenerator()));
        return compartmentNodeDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createSatisfyRequirementsCompartments(IColorProvider colorProvider) {
        final List<IDiagramElementDescriptionProvider<?>> compartmentNodeDescriptionProviders = new ArrayList<>();
        compartmentNodeDescriptionProviders.add(new SatisfyRequirementCompartmentNodeDescription(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement(),
                colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders
                .add(new SatisfyRequirementCompartmentItemNodeDescription(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement(),
                        colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new SatisfyRequirementCompartmentNodeDescription(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedRequirement(),
                colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new SatisfyRequirementCompartmentItemNodeDescription(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedRequirement(),
                colorProvider, this.getDescriptionNameGenerator()));
        return compartmentNodeDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createCompartmentsForConnectionEnd(IColorProvider colorProvider) {
        final List<IDiagramElementDescriptionProvider<?>> compartmentNodeDescriptionProviders = new ArrayList<>();

        compartmentNodeDescriptionProviders.add(new AllocationDefinitionEndsCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new AllocationDefinitionEndsCompartmentItemNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new InheritedCompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getAllocationDefinition(),
                SysmlPackage.eINSTANCE.getConnectionDefinition_ConnectionEnd(), colorProvider, this.getDescriptionNameGenerator()));

        compartmentNodeDescriptionProviders.add(new ConnectionDefinitionEndsCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new ConnectionDefinitionEndsCompartmentItemNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new InheritedCompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConnectionDefinition(),
                SysmlPackage.eINSTANCE.getConnectionDefinition_ConnectionEnd(), colorProvider,
                this.getDescriptionNameGenerator()));

        compartmentNodeDescriptionProviders.add(new InterfaceDefinitionEndsCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new InterfaceDefinitionEndsCompartmentItemNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new InheritedCompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getInterfaceDefinition(),
                SysmlPackage.eINSTANCE.getInterfaceDefinition_InterfaceEnd(), colorProvider,
                this.getDescriptionNameGenerator()));

        return compartmentNodeDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createCompartmentsForObjectiveRequirement(IColorProvider colorProvider) {
        final List<IDiagramElementDescriptionProvider<?>> compartmentNodeDescriptionProviders = new ArrayList<>();

        compartmentNodeDescriptionProviders.add(new CaseDefinitionObjectiveRequirementCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getCaseDefinition(),
                SysmlPackage.eINSTANCE.getCaseDefinition_ObjectiveRequirement(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders
                .add(new ObjectiveDocumentationCompartmentItemNodeDescription(SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_ObjectiveRequirement(),
                        colorProvider, this.getDescriptionNameGenerator()));

        compartmentNodeDescriptionProviders.add(new CaseUsageObjectiveRequirementCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ObjectiveRequirement(),
                colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders
                .add(new ObjectiveDocumentationCompartmentItemNodeDescription(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ObjectiveRequirement(),
                        colorProvider, this.getDescriptionNameGenerator()));

        return compartmentNodeDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createCompartmentsForSubjectParameter(IColorProvider colorProvider) {
        final List<IDiagramElementDescriptionProvider<?>> compartmentNodeDescriptionProviders = new ArrayList<>();

        compartmentNodeDescriptionProviders.add(new CaseDefinitionSubjectCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_SubjectParameter(),
                colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CaseUsageSubjectCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter(),
                colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new RequirementDefinitionSubjectCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConcernDefinition(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConcernDefinition(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new RequirementUsageSubjectCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConcernUsage(),
                SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new RequirementUsageSubjectCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(),
                SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConcernUsage(),
                SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(),
                SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new RequirementDefinitionSubjectCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementDefinition(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new RequirementUsageSubjectCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter(),
                colorProvider, this.getDescriptionNameGenerator()));

        return compartmentNodeDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createCompartmentsForActorParameter(IColorProvider colorProvider) {
        final List<IDiagramElementDescriptionProvider<?>> compartmentNodeDescriptionProviders = new ArrayList<>();

        compartmentNodeDescriptionProviders.add(new CaseDefinitionActorsCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders
                .add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_ActorParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CaseUsageActorsCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ActorParameter(), colorProvider,
                this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new RequirementDefinitionActorsCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConcernDefinition(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_ActorParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConcernDefinition(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_ActorParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new RequirementUsageActorsCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConcernUsage(),
                SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new RequirementUsageActorsCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(),
                SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConcernUsage(),
                SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(),
                SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new RequirementDefinitionActorsCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementDefinition(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_ActorParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new RequirementUsageActorsCompartmentNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter(),
                colorProvider, this.getDescriptionNameGenerator()));

        return compartmentNodeDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createCompartmentsForStakeholderParameter(IColorProvider colorProvider) {
        final List<IDiagramElementDescriptionProvider<?>> compartmentNodeDescriptionProviders = new ArrayList<>();

        compartmentNodeDescriptionProviders.add(new RequirementDefinitionStakeholdersCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConcernDefinition(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_StakeholderParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConcernDefinition(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_StakeholderParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new RequirementUsageStakeholdersCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConcernUsage(),
                SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new RequirementUsageStakeholdersCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(),
                SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConcernUsage(),
                SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(),
                SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new RequirementDefinitionStakeholdersCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementDefinition(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_StakeholderParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementDefinition(),
                SysmlPackage.eINSTANCE.getRequirementDefinition_StakeholderParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders.add(new RequirementUsageStakeholdersCompartmentNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(),
                SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter(), colorProvider, this.getDescriptionNameGenerator()));
        compartmentNodeDescriptionProviders
                .add(new CompartmentItemNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter(),
                        colorProvider, this.getDescriptionNameGenerator()));

        return compartmentNodeDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createAllCustomNodeDescriptionProviders(IColorProvider colorProvider) {
        final var customNodeDescriptionProviders = new ArrayList<IDiagramElementDescriptionProvider<?>>();

        customNodeDescriptionProviders.add(new StartActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        customNodeDescriptionProviders.add(new DoneActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        customNodeDescriptionProviders.add(new JoinActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        customNodeDescriptionProviders.add(new ForkActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        customNodeDescriptionProviders.add(new MergeActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        customNodeDescriptionProviders.add(new DecisionActionNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        customNodeDescriptionProviders.add(new ActorNodeDescriptionProvider(colorProvider));
        customNodeDescriptionProviders.add(new SubjectNodeDescriptionProvider(colorProvider));
        customNodeDescriptionProviders.add(new ImportedPackageNodeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));

        return customNodeDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createAllEdgeDescriptionProviders(IColorProvider colorProvider) {
        final var edgeDescriptionProviders = new ArrayList<IDiagramElementDescriptionProvider<?>>();

        edgeDescriptionProviders.add(new AnnotationEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        edgeDescriptionProviders.add(new DependencyEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        edgeDescriptionProviders.add(new SubclassificationEdgeDescriptionProvider(colorProvider));
        edgeDescriptionProviders.add(new RedefinitionEdgeDescriptionProvider(colorProvider));
        edgeDescriptionProviders.add(new SubsettingEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        edgeDescriptionProviders.add(new ReferenceSubsettingEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        edgeDescriptionProviders.add(new FeatureTypingEdgeDescriptionProvider(colorProvider));
        edgeDescriptionProviders.add(new AllocateEdgeDescriptionProvider(colorProvider));
        edgeDescriptionProviders.add(new SuccessionEdgeDescriptionProvider(colorProvider));
        edgeDescriptionProviders.add(new TransitionEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        edgeDescriptionProviders.add(new InterfaceUsageEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        edgeDescriptionProviders.add(new FlowUsageEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        edgeDescriptionProviders.add(new BindingConnectorAsUsageEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        edgeDescriptionProviders.add(new FeatureValueEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        edgeDescriptionProviders.add(new IncludeUseCaseDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        edgeDescriptionProviders.add(new ConnectionUsageEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));
        edgeDescriptionProviders.add(new SatisfyRequirementEdgeDescriptionProvider(colorProvider, this.getDescriptionNameGenerator()));

        return edgeDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createAllDefinitionOwnedUsageEdgeDescriptionProviders(IColorProvider colorProvider) {
        final var definitionOwnedUsageEdgeDescriptionProviders = new ArrayList<IDiagramElementDescriptionProvider<?>>();

        definitionOwnedUsageEdgeDescriptionProviders
                .add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getAcceptActionUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(),
                        colorProvider, this.getDescriptionNameGenerator()));
        definitionOwnedUsageEdgeDescriptionProviders
                .add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(),
                        colorProvider, this.getDescriptionNameGenerator()));
        definitionOwnedUsageEdgeDescriptionProviders
                .add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getAllocationUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedAllocation(),
                        colorProvider, this.getDescriptionNameGenerator()));
        definitionOwnedUsageEdgeDescriptionProviders
                .add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getAttributeUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(),
                        colorProvider, this.getDescriptionNameGenerator()));
        definitionOwnedUsageEdgeDescriptionProviders
                .add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement(),
                        colorProvider, this.getDescriptionNameGenerator()));
        definitionOwnedUsageEdgeDescriptionProviders
                .add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getConstraintUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedConstraint(),
                        colorProvider, this.getDescriptionNameGenerator()));
        definitionOwnedUsageEdgeDescriptionProviders
                .add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getInterfaceUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedInterface(),
                        colorProvider, this.getDescriptionNameGenerator()));
        definitionOwnedUsageEdgeDescriptionProviders
                .add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem(), colorProvider,
                        this.getDescriptionNameGenerator()));
        definitionOwnedUsageEdgeDescriptionProviders
                .add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPart(), colorProvider,
                        this.getDescriptionNameGenerator()));
        definitionOwnedUsageEdgeDescriptionProviders
                .add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), colorProvider,
                        this.getDescriptionNameGenerator()));
        definitionOwnedUsageEdgeDescriptionProviders
                .add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement(),
                        colorProvider, this.getDescriptionNameGenerator()));
        definitionOwnedUsageEdgeDescriptionProviders
                .add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedState(), colorProvider,
                        this.getDescriptionNameGenerator()));
        definitionOwnedUsageEdgeDescriptionProviders
                .add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getExhibitStateUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedState(), colorProvider,
                        this.getDescriptionNameGenerator()));
        definitionOwnedUsageEdgeDescriptionProviders
                .add(new DefinitionOwnedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getOccurrenceUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedOccurrence(), colorProvider,
                        this.getDescriptionNameGenerator()));

        return definitionOwnedUsageEdgeDescriptionProviders;
    }

    private List<IDiagramElementDescriptionProvider<?>> createAllUsageCompositeEdgeDescriptionProviders(IColorProvider colorProvider) {
        final var usageCompositeEdgeDescriptionProviders = new ArrayList<IDiagramElementDescriptionProvider<?>>();

        usageCompositeEdgeDescriptionProviders.add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getAcceptActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction(),
                colorProvider, this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction(),
                colorProvider, this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getAllocationUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAllocation(),
                colorProvider, this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders
                .add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getAttributeUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getConstraintUsage(), SysmlPackage.eINSTANCE.getUsage_NestedConstraint(),
                colorProvider, this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getExhibitStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), colorProvider, this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getOccurrenceUsage(), SysmlPackage.eINSTANCE.getUsage_NestedOccurrence(),
                colorProvider, this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), colorProvider, this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction(),
                colorProvider, this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort(), colorProvider, this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getUsage_NestedRequirement(),
                colorProvider, this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getUsage_NestedConstraint(),
                colorProvider, this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getUsage_NestedRequirement(),
                colorProvider, this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new UsageNestedUsageEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState(), colorProvider, this.getDescriptionNameGenerator()));

        usageCompositeEdgeDescriptionProviders.add(
                new NestedActorEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ActorParameter(), colorProvider, this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedActorEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getUseCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ActorParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedActorEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_ActorParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedActorEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getUseCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_ActorParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedActorEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedActorEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedActorEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_ActorParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedActorEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_StakeholderParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedActorEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedActorEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));

        usageCompositeEdgeDescriptionProviders.add(
                new NestedSubjectEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedSubjectEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getUseCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedSubjectEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getIncludeUseCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedSubjectEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_SubjectParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedSubjectEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getUseCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_SubjectParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedSubjectEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedSubjectEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));
        usageCompositeEdgeDescriptionProviders.add(
                new NestedSubjectEdgeDescriptionProvider(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter(), colorProvider,
                        this.getDescriptionNameGenerator()));

        return usageCompositeEdgeDescriptionProviders;
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

    private void linkRequirementStakeholdersCompartment(IViewDiagramElementFinder cache) {
        NodeDescription requirementUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getRequirementUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter()))
                .ifPresent(requirementUsageNodeDescription.getReusedChildNodeDescriptions()::add);
        NodeDescription requirementDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getRequirementDefinition())).get();
        cache.getNodeDescription(
                        this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_StakeholderParameter()))
                .ifPresent(requirementDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private void linkConcernSubjectCompartment(IViewDiagramElementFinder cache) {
        NodeDescription concernUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getConcernUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getConcernUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(concernUsageNodeDescription.getReusedChildNodeDescriptions()::add);
        NodeDescription concernDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getConcernDefinition())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getConcernDefinition(),
                        SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter()))
                .ifPresent(concernDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private void linkSatisfyRequirementUsageSubjectCompartment(IViewDiagramElementFinder cache) {
        NodeDescription satisfyRequirementUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage())).get();
        cache.getNodeDescription(
                        this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(satisfyRequirementUsageNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private void linkConcernActorsCompartment(IViewDiagramElementFinder cache) {
        NodeDescription concernUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getConcernUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getConcernUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter()))
                .ifPresent(concernUsageNodeDescription.getReusedChildNodeDescriptions()::add);
        NodeDescription concernDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getConcernDefinition())).get();
        cache.getNodeDescription(
                        this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getConcernDefinition(),
                                SysmlPackage.eINSTANCE.getRequirementDefinition_ActorParameter()))
                .ifPresent(concernDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private void linkSatisfyRequirementUsageActorsCompartment(IViewDiagramElementFinder cache) {
        NodeDescription satisfyRequirementUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage())).get();
        cache.getNodeDescription(
                        this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter()))
                .ifPresent(satisfyRequirementUsageNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private void linkConcernStakeholdersCompartment(IViewDiagramElementFinder cache) {
        NodeDescription concernUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getConcernUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getConcernUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter()))
                .ifPresent(concernUsageNodeDescription.getReusedChildNodeDescriptions()::add);
        NodeDescription concernDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getConcernDefinition())).get();
        cache.getNodeDescription(
                        this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getConcernDefinition(),
                                SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter()))
                .ifPresent(concernDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private void linkSatisfyRequirementUsageStakeholdersCompartment(IViewDiagramElementFinder cache) {
        NodeDescription satisfyRequirementUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage())).get();
        cache.getNodeDescription(
                        this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter()))
                .ifPresent(satisfyRequirementUsageNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private void linkCaseSubjectCompartment(IViewDiagramElementFinder cache) {
        NodeDescription caseUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getCaseUsage())).get();
        NodeDescription useCaseUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getUseCaseUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter()))
                .ifPresent(compartment -> {
                    caseUsageNodeDescription.getReusedChildNodeDescriptions().add(compartment);
                    useCaseUsageNodeDescription.getReusedChildNodeDescriptions().add(compartment);
                });
        NodeDescription caseDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getCaseDefinition())).get();
        NodeDescription useCaseDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getUseCaseDefinition())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getCaseDefinition(),
                        SysmlPackage.eINSTANCE.getCaseDefinition_SubjectParameter()))
                .ifPresent(compartment -> {
                    caseDefinitionNodeDescription.getReusedChildNodeDescriptions().add(compartment);
                    useCaseDefinitionNodeDescription.getReusedChildNodeDescriptions().add(compartment);
                });
    }

    private void linkCaseActorsCompartment(IViewDiagramElementFinder cache) {
        NodeDescription caseUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getCaseUsage())).get();
        NodeDescription useCaseUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getUseCaseUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ActorParameter()))
                .ifPresent(compartment -> {
                    caseUsageNodeDescription.getReusedChildNodeDescriptions().add(compartment);
                    useCaseUsageNodeDescription.getReusedChildNodeDescriptions().add(compartment);
                });
        NodeDescription caseDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getCaseDefinition())).get();
        NodeDescription useCaseDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getUseCaseDefinition())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_ActorParameter()))
                .ifPresent(compartment -> {
                    caseDefinitionNodeDescription.getReusedChildNodeDescriptions().add(compartment);
                    useCaseDefinitionNodeDescription.getReusedChildNodeDescriptions().add(compartment);
                });
    }

    private void linkCaseObjectiveRequirementCompartment(IViewDiagramElementFinder cache) {
        NodeDescription caseUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getCaseUsage())).get();
        NodeDescription useCaseUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getUseCaseUsage())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ObjectiveRequirement()))
                .ifPresent(compartment -> {
                    caseUsageNodeDescription.getReusedChildNodeDescriptions().add(compartment);
                    useCaseUsageNodeDescription.getReusedChildNodeDescriptions().add(compartment);
                });
        NodeDescription caseDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getCaseDefinition())).get();
        NodeDescription useCaseDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getUseCaseDefinition())).get();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getCaseDefinition(),
                        SysmlPackage.eINSTANCE.getCaseDefinition_ObjectiveRequirement()))
                .ifPresent(compartment -> {
                    caseDefinitionNodeDescription.getReusedChildNodeDescriptions().add(compartment);
                    useCaseDefinitionNodeDescription.getReusedChildNodeDescriptions().add(compartment);
                });
    }

    private void linkAllocationDefinitionEndsCompartment(IViewDiagramElementFinder cache) {
        NodeDescription allocationDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getAllocationDefinition())).get();
        cache.getNodeDescription(
                        this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getAllocationDefinition(), SysmlPackage.eINSTANCE.getConnectionDefinition_ConnectionEnd()))
                .ifPresent(allocationDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private void linkConnectionDefinitionEndsCompartment(IViewDiagramElementFinder cache) {
        NodeDescription connectionDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getConnectionDefinition())).get();
        cache.getNodeDescription(
                this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getConnectionDefinition(), SysmlPackage.eINSTANCE.getConnectionDefinition_ConnectionEnd()))
                .ifPresent(connectionDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
    }

    private void linkInterfaceDefinitionEndsCompartment(IViewDiagramElementFinder cache) {
        NodeDescription connectionDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getInterfaceDefinition())).get();
        cache.getNodeDescription(
                this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getInterfaceDefinition(), SysmlPackage.eINSTANCE.getInterfaceDefinition_InterfaceEnd()))
                .ifPresent(connectionDefinitionNodeDescription.getReusedChildNodeDescriptions()::add);
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
        // add "states" compartment node as reusedChild of PartUsage and PartDefinition
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPartUsage())).ifPresent(nd ->
                cache.getNodeDescription(this.descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME)
                        .ifPresent(nd.getReusedChildNodeDescriptions()::add)
        );
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPartDefinition())).ifPresent(nd ->
                cache.getNodeDescription(this.descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME)
                        .ifPresent(nd.getReusedChildNodeDescriptions()::add)
        );
        // add "exhibit states" compartment node as reusedChild of PartUsage and PartDefinition
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPartUsage())).ifPresent(nd ->
                cache.getNodeDescription(this.descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                        .ifPresent(nd.getReusedChildNodeDescriptions()::add)
        );
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPartDefinition())).ifPresent(nd ->
                cache.getNodeDescription(this.descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                        .ifPresent(nd.getReusedChildNodeDescriptions()::add)
        );
    }

    private void linkActionParametersCompartment(IViewDiagramElementFinder cache) {
        NodeDescription actionDefinitionNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getActionDefinition())).get();
        String documentationCompartmentName = this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getElement_Documentation());

        // We want to add the Parameters container just after the documentation. If the documentation is not found, we
        // will add it at the first index.
        Integer documentationContainerIndex = actionDefinitionNodeDescription.getReusedChildNodeDescriptions().stream()
                .filter(nodeDescription -> documentationCompartmentName.equals(nodeDescription.getName()))
                .findFirst()
                .map(documentationContainerDescription -> actionDefinitionNodeDescription.getReusedChildNodeDescriptions().indexOf(documentationContainerDescription))
                .orElse(-1);
        cache.getNodeDescription(
                this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedReference())
                        + ActionDefinitionParametersCompartmentNodeDescriptionProvider.COMPARTMENT_NAME)
                .ifPresent(parametersCompartmentNodeDescription -> {
                    actionDefinitionNodeDescription.getReusedChildNodeDescriptions().add(documentationContainerIndex + 1, parametersCompartmentNodeDescription);
                });

        NodeDescription actionUsageNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getActionUsage())).get();
        String usageDocumentationCompartmentName = this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getElement_Documentation());

        // We want to add the Parameters container just after the documentation. If the documentation is not found, we
        // will add it at the first index.
        Integer usageDocumentationContainerIndex = actionUsageNodeDescription.getReusedChildNodeDescriptions().stream()
                .filter(nodeDescription -> usageDocumentationCompartmentName.equals(nodeDescription.getName()))
                .findFirst()
                .map(documentationContainerDescription -> actionUsageNodeDescription.getReusedChildNodeDescriptions().indexOf(documentationContainerDescription))
                .orElse(-1);
        cache.getNodeDescription(
                this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedReference())
                        + ActionUsageParametersCompartmentNodeDescriptionProvider.COMPARTMENT_NAME)
                .ifPresent(parametersCompartmentNodeDescription -> {
                    actionUsageNodeDescription.getReusedChildNodeDescriptions().add(usageDocumentationContainerIndex + 1, parametersCompartmentNodeDescription);
                });
    }

    private void linkPartPerformActionsCompartment(IViewDiagramElementFinder cache) {
        // perform actions compartment in PartUsages
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction()) +
                        PerformActionsCompartmentNodeDescriptionProvider.PERFORM_ACTIONS_COMPARTMENT_NAME)
                .ifPresent(compartmentNodeDescription -> {
                    USAGES.stream()
                            .filter(eClass -> Objects.equals(eClass, SysmlPackage.eINSTANCE.getPartUsage())
                                    || (eClass.getEAllSuperTypes().contains(SysmlPackage.eINSTANCE.getPartUsage()) && !Objects.equals(eClass, SysmlPackage.eINSTANCE.getViewUsage())))
                            .forEach(partUsage -> {
                                this.addCompartmentNodeDescriptionInNodeDescription(cache, compartmentNodeDescription, partUsage);
                            });
                });
        // perform actions compartment in PartDefinitions
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction()) +
                        PerformActionsCompartmentNodeDescriptionProvider.PERFORM_ACTIONS_COMPARTMENT_NAME)
                .ifPresent(compartmentNodeDescription -> {
                    DEFINITIONS.stream()
                            .filter(eClass -> Objects.equals(eClass, SysmlPackage.eINSTANCE.getPartDefinition()) || eClass.getEAllSuperTypes().contains(SysmlPackage.eINSTANCE.getPartDefinition()))
                            .forEach(partDefinition -> {
                                this.addCompartmentNodeDescriptionInNodeDescription(cache, compartmentNodeDescription, partDefinition);
                            });
                });
    }

    private void linkActionPerformActionsCompartment(IViewDiagramElementFinder cache) {
        // perform actions compartment in ActionUsages
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction()) +
                PerformActionsCompartmentNodeDescriptionProvider.PERFORM_ACTIONS_COMPARTMENT_NAME)
                .ifPresent(compartmentNodeDescription -> {
                    USAGES.stream()
                            .filter(eClass -> Objects.equals(eClass, SysmlPackage.eINSTANCE.getActionUsage()) || eClass.getEAllSuperTypes().contains(SysmlPackage.eINSTANCE.getActionUsage()))
                            .forEach(actionUsage -> {
                                this.addCompartmentNodeDescriptionInNodeDescription(cache, compartmentNodeDescription, actionUsage);
                            });
                });
        // perform actions compartment in ActionDefinitions
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction()) +
                PerformActionsCompartmentNodeDescriptionProvider.PERFORM_ACTIONS_COMPARTMENT_NAME)
                .ifPresent(compartmentNodeDescription -> {
                    DEFINITIONS.stream()
                            .filter(eClass -> Objects.equals(eClass, SysmlPackage.eINSTANCE.getActionDefinition()) || eClass.getEAllSuperTypes().contains(SysmlPackage.eINSTANCE.getActionDefinition()))
                            .forEach(actionDefinition -> {
                                this.addCompartmentNodeDescriptionInNodeDescription(cache, compartmentNodeDescription, actionDefinition);
                            });
                });
    }

    private void linkInterconnectionCompartment(IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPartUsage()))
                .ifPresent(nd -> cache.getNodeDescription(this.descriptionNameGenerator.getFreeFormCompartmentName(InterconnectionCompartmentNodeDescriptionProvider.COMPARTMENT_NAME))
                        .ifPresent(compartmentNodeDescription -> {
                            nd.getReusedChildNodeDescriptions().add(compartmentNodeDescription);
                            if (nd.getStyle().getChildrenLayoutStrategy() instanceof ListLayoutStrategyDescription listLayout) {
                                listLayout.getGrowableNodes().add(compartmentNodeDescription);
                            }
                        }));
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPartDefinition()))
                .ifPresent(nd -> cache.getNodeDescription(this.descriptionNameGenerator.getFreeFormCompartmentName(InterconnectionCompartmentNodeDescriptionProvider.COMPARTMENT_NAME))
                        .ifPresent(compartmentNodeDescription -> {
                            nd.getReusedChildNodeDescriptions().add(compartmentNodeDescription);
                            if (nd.getStyle().getChildrenLayoutStrategy() instanceof ListLayoutStrategyDescription listLayout) {
                                listLayout.getGrowableNodes().add(compartmentNodeDescription);
                            }
                        }));
    }

    private void linkSatisfyRequirementsCompartment(IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPartUsage()))
                .ifPresent(nd -> cache.getNodeDescription(this.descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedRequirement())
                        + SatisfyRequirementCompartmentNodeDescription.COMPARTMENT_NAME)
                        .ifPresent(compartmentNodeDescription -> {
                            nd.getReusedChildNodeDescriptions().add(compartmentNodeDescription);
                        }));
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPartDefinition()))
                .ifPresent(nd -> cache
                        .getNodeDescription(this.descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement())
                                + SatisfyRequirementCompartmentNodeDescription.COMPARTMENT_NAME)
                        .ifPresent(compartmentNodeDescription -> {
                            nd.getReusedChildNodeDescriptions().add(compartmentNodeDescription);
                        }));
    }

    private void addCompartmentNodeDescriptionInNodeDescription(IViewDiagramElementFinder cache, NodeDescription compartmentNodeDescription, EClass eClass) {
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(eClass))
                .ifPresent(nodeDescription -> {
                    nodeDescription.getReusedChildNodeDescriptions().add(compartmentNodeDescription);
                });
    }

    private DiagramPalette createDiagramPalette(IViewDiagramElementFinder cache) {
        List<NodeTool> nodeTools = new ArrayList<>();

        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getComment()))
                .ifPresent(commentNodeDescription -> {
                    var createCommentNodeTool = this.toolDescriptionService.createNodeToolFromDiagramBackground(commentNodeDescription, SysmlPackage.eINSTANCE.getComment());
                    if (createCommentNodeTool != null) {
                        nodeTools.add(createCommentNodeTool);
                    }
                });

        return this.diagramBuilderHelper.newDiagramPalette()
                .dropNodeTool(this.createDropFromDiagramTool(cache))
                .dropTool(this.toolDescriptionService.createDropFromExplorerTool())
                .nodeTools(nodeTools.toArray(NodeTool[]::new))
                .toolSections(this.createToolSections(cache))
                .build();
    }

    private DropNodeTool createDropFromDiagramTool(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();

        SDVDiagramDescriptionProvider.TOOL_SECTIONS.forEach(sectionTool -> {
            sectionTool.elements().forEach(element -> {
                cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(element)).ifPresent(acceptedNodeTypes::add);
            });
        });

        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getNamespaceImport()))
                .ifPresent(acceptedNodeTypes::add);

        return this.diagramBuilderHelper.newDropNodeTool()
                .name("Drop from Diagram")
                .acceptedNodeTypes(acceptedNodeTypes.toArray(NodeDescription[]::new))
                .body(this.viewBuilderHelper.newChangeContext()
                        .expression(ServiceMethod.of6(DiagramMutationAQLService::dropElementFromDiagram).aql("droppedElement", "droppedNode", "targetElement", "targetNode",
                                IEditingContext.EDITING_CONTEXT,
                                DiagramContext.DIAGRAM_CONTEXT, ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE))
                        .build())
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
        if (BEHAVIOR_TOOL_SECTION.name().equals(sectionName)) {
            nodeTools.add(new ExhibitStateWithReferenceNodeToolProvider(this.getDescriptionNameGenerator()).create(cache));
        } else if (STRUCTURE_TOOL_SECTION.name().equals(sectionName)) {
            NodeDescription nodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getNamespaceImport())).orElse(null);
            nodeTools.add(new NamespaceImportNodeToolProvider(nodeDescription, this.getDescriptionNameGenerator()).create(cache));
        } else if (VIEW_AS_TOOL_SECTION.name().equals(sectionName)) {
            nodeTools.add(new SetAsViewToolProvider(AQLUtils.aqlString(StandardDiagramsConstants.GV_QN), StandardDiagramsConstants.GV).create(cache));
            nodeTools.add(new SetAsViewToolProvider(AQLUtils.aqlString(StandardDiagramsConstants.IV_QN), StandardDiagramsConstants.IV).create(cache));
            nodeTools.add(new SetAsViewToolProvider(AQLUtils.aqlString(StandardDiagramsConstants.AFV_QN), StandardDiagramsConstants.AFV).create(cache));
            nodeTools.add(new SetAsViewToolProvider(AQLUtils.aqlString(StandardDiagramsConstants.STV_QN), StandardDiagramsConstants.STV).create(cache));
        }
        return nodeTools;
    }
}
