/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.diagram.common.view.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.DescriptionFinder;
import org.eclipse.syson.diagram.common.view.nodes.DecisionActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.DoneActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ForkActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.JoinActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.MergeActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StartActionNodeDescriptionProvider;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.ControlNode;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.SysmlEClassSwitch;

/**
 * Switch retrieving the list of EdgeTools for each SysMLv2 concept represented in all diagrams.
 *
 * @author arichard
 */
public class ViewEdgeToolSwitch extends SysmlEClassSwitch<List<EdgeTool>> {

    private static final String USAGE = "usage";

    private static final String DEFINITION = "definition";

    private final NodeDescription nodeDescription;

    private final List<NodeDescription> allNodeDescriptions;

    private final IDescriptionNameGenerator descriptionNameGenerator;

    private final ViewBuilders viewBuilderHelper;

    private final DiagramBuilders diagramBuilderHelper;

    private final ViewEdgeToolService edgeToolService;

    public ViewEdgeToolSwitch(NodeDescription nodeDescription, List<NodeDescription> allNodeDescriptions, IDescriptionNameGenerator nameGenerator) {
        this.nodeDescription = Objects.requireNonNull(nodeDescription);
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
        this.descriptionNameGenerator = Objects.requireNonNull(nameGenerator);
        this.viewBuilderHelper = new ViewBuilders();
        this.diagramBuilderHelper = new DiagramBuilders();
        this.edgeToolService = new ViewEdgeToolService(this.viewBuilderHelper, this.diagramBuilderHelper, allNodeDescriptions, nameGenerator);
    }

    @Override
    public List<EdgeTool> caseAcceptActionUsage(AcceptActionUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        edgeTools.add(this.edgeToolService.createSuccessionEdgeTool(this.getSuccessionEdgeTargets()));
        edgeTools.addAll(this.caseUsage(object));
        // since AcceptActionDefinition does not exist, this.caseUsage did not manage the FeatureTyping edge, we need to add it manually.
        this.addFeatureTypingEdgeToolForActionUsageSubType(edgeTools);
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseActionUsage(ActionUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        edgeTools.add(this.edgeToolService.createSuccessionEdgeTool(this.getSuccessionEdgeTargets()));
        edgeTools.add(this.edgeToolService.createTransitionUsageEdgeTool(SysmlPackage.eINSTANCE.getTransitionUsage(), this.getSuccessionEdgeTargets()));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseAllocationUsage(AllocationUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                || this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getAllocationDefinition())).toList();
        edgeTools.add(this.edgeToolService.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getAllocationUsage(), targetNodes));
        targetNodes.forEach(targetNode -> edgeTools.add(this.edgeToolService.createAddAsNestedEdgeTool(targetNode)));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseAttributeUsage(AttributeUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var attributeBecomeNestedOf = this.allNodeDescriptions.stream()
                .filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                    || nodeDesc.getName().toLowerCase().endsWith(DEFINITION)
                )
                .toList();
        edgeTools.add(this.edgeToolService.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getAttributeUsage(), attributeBecomeNestedOf));

        var addElementAsNestedOfAttribute = this.allNodeDescriptions.stream()
                .filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                    || this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getMetadataDefinition())
                    || this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getOccurrenceDefinition())
                    || this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getAttributeDefinition()))
                .filter(nodeDesc -> !this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getPartUsage()))
                .toList();
        addElementAsNestedOfAttribute.forEach(nestedNode -> edgeTools.add(this.edgeToolService.createAddAsNestedEdgeTool(nestedNode)));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseConstraintUsage(ConstraintUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                || this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getConstraintDefinition())).collect(Collectors.toList());
        targetNodes.removeIf(nodeDesc -> this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPortUsage()).equals(nodeDesc.getName()));
        targetNodes.removeIf(nodeDesc -> this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAttributeUsage()).equals(nodeDesc.getName()));
        edgeTools.add(this.edgeToolService.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getConstraintUsage(), targetNodes));
        targetNodes.forEach(targetNode -> edgeTools.add(this.edgeToolService.createAddAsNestedEdgeTool(targetNode)));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseControlNode(ControlNode object) {
        var edgeTools = new ArrayList<EdgeTool>();
        edgeTools.add(this.edgeToolService.createSuccessionEdgeTool(this.getSuccessionEdgeTargets()));
        edgeTools.add(this.edgeToolService.createTransitionUsageEdgeTool(SysmlPackage.eINSTANCE.getTransitionUsage(), this.getSuccessionEdgeTargets()));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseDefinition(Definition object) {
        var edgeTools = new ArrayList<EdgeTool>();
        // Add an edge tool to the Definition to add the targeted AttributeUsage as a nested AttributeUsage.
        var addAttributeAsNestedOfDefinition = this.allNodeDescriptions.stream()
                .filter(nodeDesc -> this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getAttributeUsage()))
                .toList();
        addAttributeAsNestedOfDefinition.forEach(nodeDesc -> edgeTools.add(this.edgeToolService.createAddAsNestedEdgeTool(nodeDesc)));

        edgeTools.add(this.edgeToolService.createDependencyEdgeTool(this.allNodeDescriptions));
        edgeTools.add(this.edgeToolService.createSubclassificationEdgeTool(List.of(this.nodeDescription)));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseInterfaceUsage(InterfaceUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                || this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getInterfaceDefinition())).collect(Collectors.toList());
        targetNodes.removeIf(nodeDesc -> this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPortUsage()).equals(nodeDesc.getName()));
        targetNodes.removeIf(nodeDesc -> this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAttributeUsage()).equals(nodeDesc.getName()));
        edgeTools.add(this.edgeToolService.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getInterfaceUsage(), targetNodes));
        targetNodes.forEach(targetNode -> edgeTools.add(this.edgeToolService.createAddAsNestedEdgeTool(targetNode)));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseItemUsage(ItemUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                || this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getItemDefinition())
                || this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getPartDefinition())).collect(Collectors.toList());
        targetNodes.removeIf(nodeDesc -> this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPortUsage()).equals(nodeDesc.getName()));
        targetNodes.removeIf(nodeDesc -> this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAttributeUsage()).equals(nodeDesc.getName()));
        edgeTools.add(this.edgeToolService.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getItemUsage(), targetNodes));
        targetNodes.forEach(targetNode -> edgeTools.add(this.edgeToolService.createAddAsNestedEdgeTool(targetNode)));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> casePackage(Package object) {
        var edgeTools = new ArrayList<EdgeTool>();
        edgeTools.add(this.edgeToolService.createDependencyEdgeTool(this.allNodeDescriptions));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> casePartDefinition(PartDefinition object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream()
                .filter(nodeDesc -> this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getPartUsage()))
                .toList();
        targetNodes.forEach(targetNode -> edgeTools.add(this.edgeToolService.createAddAsNestedEdgeTool(targetNode)));
        edgeTools.addAll(this.caseDefinition(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> casePartUsage(PartUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                || this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getOccurrenceDefinition())
                || this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getPartDefinition())).collect(Collectors.toList());
        targetNodes.removeIf(nodeDesc -> this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPortUsage()).equals(nodeDesc.getName()));
        targetNodes.removeIf(nodeDesc -> this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAttributeUsage()).equals(nodeDesc.getName()));
        edgeTools.add(this.edgeToolService.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getPartUsage(), targetNodes));
        var addAsNestedTargetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE))
                .toList();
        addAsNestedTargetNodes.forEach(targetNode -> edgeTools.add(this.edgeToolService.createAddAsNestedEdgeTool(targetNode)));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> casePortUsage(PortUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                || this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getPortDefinition())).toList();
        edgeTools.add(this.edgeToolService.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getPortUsage(), targetNodes));
        targetNodes.forEach(targetNode -> edgeTools.add(this.edgeToolService.createAddAsNestedEdgeTool(targetNode)));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseRequirementUsage(RequirementUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> nodeDesc.getName().toLowerCase().endsWith(USAGE)
                || this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getRequirementDefinition())).collect(Collectors.toList());
        targetNodes.removeIf(nodeDesc -> this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getPortUsage()));
        targetNodes.removeIf(nodeDesc -> this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getAttributeUsage()));
        edgeTools.add(this.edgeToolService.createBecomeNestedElementEdgeTool(SysmlPackage.eINSTANCE.getRequirementUsage(), targetNodes));
        targetNodes.forEach(targetNode -> edgeTools.add(this.edgeToolService.createAddAsNestedEdgeTool(targetNode)));
        var objectiveTargets = this.allNodeDescriptions.stream()
                .filter(n -> this.edgeToolService.isTheNodeDescriptionFor(n, SysmlPackage.eINSTANCE.getUseCaseUsage())
                        || this.edgeToolService.isTheNodeDescriptionFor(n, SysmlPackage.eINSTANCE.getUseCaseDefinition()))
                .toList();
        edgeTools.add(this.edgeToolService.createBecomeObjectiveRequirementEdgeTool(objectiveTargets));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseStateDefinition(StateDefinition object) {
        var edgeTools = new ArrayList<EdgeTool>();
        edgeTools.addAll(this.caseDefinition(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseStateUsage(StateUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        var targetNodes = this.allNodeDescriptions.stream().filter(nodeDesc -> this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getStateUsage()).equals(nodeDesc.getName())).collect(Collectors.toList());
        edgeTools.add(this.edgeToolService.createTransitionUsageEdgeTool(SysmlPackage.eINSTANCE.getTransitionUsage(), targetNodes));
        edgeTools.addAll(this.caseUsage(object));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseUsage(Usage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        // special actions (such as Start or Done) should not be considered to create edges.
        var targetDescriptions = this.allNodeDescriptions.stream()
                .filter(this::isRegularNodeDescription)
                .toList();
        edgeTools.add(this.edgeToolService.createDependencyEdgeTool(targetDescriptions));
        edgeTools.add(this.edgeToolService.createRedefinitionEdgeTool(List.of(this.nodeDescription)));
        edgeTools.add(this.edgeToolService.createSubsettingEdgeTool(List.of(this.nodeDescription)));
        edgeTools.add(this.edgeToolService.createReferenceSubsettingEdgeTool(targetDescriptions.stream().filter(this::isFeatureNodeDescription).toList()));
        edgeTools.add(this.edgeToolService.createAllocateEdgeTool(targetDescriptions));

        // Add an edge tool to the Usage to add the targeted AttributeUsage as a nested AttributeUsage.
        var addAsNested = this.allNodeDescriptions.stream()
                .filter(nodeDesc -> this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getAttributeUsage()))
                .toList();
        addAsNested.forEach(nodeDesc -> edgeTools.add(this.edgeToolService.createAddAsNestedEdgeTool(nodeDesc)));

        var definitionNodeDescription = this.edgeToolService.getNodeDescription(this.edgeToolService.getDefinitionFromUsage(object));
        if (definitionNodeDescription.isPresent()) {
            edgeTools.add(this.edgeToolService.createFeatureTypingEdgeTool(List.of(definitionNodeDescription.get())));
        }
        edgeTools.add(this.edgeToolService.createConnectionUsageEdgeTool(new DescriptionFinder(this.descriptionNameGenerator).getConnectableNodeDescriptions(this.allNodeDescriptions, SysmlPackage.eINSTANCE.getUsage())));
        return edgeTools;
    }

    @Override
    public List<EdgeTool> caseUseCaseUsage(UseCaseUsage object) {
        var edgeTools = new ArrayList<EdgeTool>();
        edgeTools.addAll(this.caseUsage(object));
        edgeTools.add(this.edgeToolService.createIncludeUseCaseUsageTool(List.of(this.nodeDescription)));
        return edgeTools;
    }

    private boolean isRegularNodeDescription(NodeDescription nodeDesc) {
        boolean isSpecial = this.descriptionNameGenerator.getNodeName(StartActionNodeDescriptionProvider.START_ACTION_NAME).equals(nodeDesc.getName());
        isSpecial = isSpecial || this.descriptionNameGenerator.getNodeName(DoneActionNodeDescriptionProvider.DONE_ACTION_NAME).equals(nodeDesc.getName());
        isSpecial = isSpecial || this.descriptionNameGenerator.getNodeName(JoinActionNodeDescriptionProvider.JOIN_ACTION_NAME).equals(nodeDesc.getName());
        isSpecial = isSpecial || this.descriptionNameGenerator.getNodeName(ForkActionNodeDescriptionProvider.FORK_ACTION_NAME).equals(nodeDesc.getName());
        isSpecial = isSpecial || this.descriptionNameGenerator.getNodeName(MergeActionNodeDescriptionProvider.MERGE_ACTION_NAME).equals(nodeDesc.getName());
        isSpecial = isSpecial || this.descriptionNameGenerator.getNodeName(DecisionActionNodeDescriptionProvider.DECISION_ACTION_NAME).equals(nodeDesc.getName());
        return !isSpecial;
    }

    private void addFeatureTypingEdgeToolForActionUsageSubType(List<EdgeTool> edgeTools) {
        Optional<NodeDescription> optActionDefinitionNodeDescription = this.edgeToolService.getNodeDescription(SysmlPackage.eINSTANCE.getActionDefinition());
        optActionDefinitionNodeDescription.ifPresent(nd -> edgeTools.add(this.edgeToolService.createFeatureTypingEdgeTool(List.of(nd))));
    }

    private List<NodeDescription> getSuccessionEdgeTargets() {
        return this.allNodeDescriptions.stream()
                .filter(this::isSuccessionTargetNodeDescription)
                .toList();
    }

    private boolean isSuccessionTargetNodeDescription(NodeDescription nodeDesc) {
        boolean result = this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getActionUsage());
        result = result || this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getAcceptActionUsage());
        result = result || this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getAssignmentActionUsage());
        result = result || this.edgeToolService.isTheNodeDescriptionFor(nodeDesc, SysmlPackage.eINSTANCE.getPerformActionUsage());
        result = result || this.descriptionNameGenerator.getNodeName(DoneActionNodeDescriptionProvider.DONE_ACTION_NAME).equals(nodeDesc.getName());
        result = result || this.descriptionNameGenerator.getNodeName(JoinActionNodeDescriptionProvider.JOIN_ACTION_NAME).equals(nodeDesc.getName());
        result = result || this.descriptionNameGenerator.getNodeName(ForkActionNodeDescriptionProvider.FORK_ACTION_NAME).equals(nodeDesc.getName());
        result = result || this.descriptionNameGenerator.getNodeName(MergeActionNodeDescriptionProvider.MERGE_ACTION_NAME).equals(nodeDesc.getName());
        result = result || this.descriptionNameGenerator.getNodeName(DecisionActionNodeDescriptionProvider.DECISION_ACTION_NAME).equals(nodeDesc.getName());
        return result;
    }

    private boolean isFeatureNodeDescription(NodeDescription nodeDesc) {
        var domainType = SysMLMetamodelHelper.toEClass(nodeDesc.getDomainType());
        if (domainType != null) {
            return domainType.getEAllSuperTypes().contains(SysmlPackage.eINSTANCE.getFeature());
        }
        return false;
    }
}
