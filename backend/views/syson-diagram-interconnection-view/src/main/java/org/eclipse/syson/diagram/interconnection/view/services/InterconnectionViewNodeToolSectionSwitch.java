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
package org.eclipse.syson.diagram.interconnection.view.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.services.AbstractViewNodeToolSectionSwitch;
import org.eclipse.syson.diagram.common.view.services.description.ToolConstants;
import org.eclipse.syson.diagram.common.view.tools.AcceptActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ActionFlowCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.AssignmentActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.DecisionActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.DoneActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ForkActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.JoinActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.MergeActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.PerformActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ReferencingPerformActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.SetAsCompositeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.SetAsRefToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StartActionNodeToolProvider;
import org.eclipse.syson.diagram.interconnection.view.IVDescriptionNameGenerator;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.tools.ChildrenPartUsageCompartmentNodeToolProvider;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Switch retrieving the NodeToolSections for each SysMLv2 concept represented in the Interconnection View diagram.
 *
 * @author gdaniel
 */
public class InterconnectionViewNodeToolSectionSwitch extends AbstractViewNodeToolSectionSwitch {

    private final IViewDiagramElementFinder cache;

    private final ILeveledCreationNodeToolsProvider leveledCreationNodeToolProvider;

    public InterconnectionViewNodeToolSectionSwitch(IViewDiagramElementFinder cache, ILeveledCreationNodeToolsProvider leveledCreationNodeToolProvider) {
        super(new IVDescriptionNameGenerator());
        this.cache = Objects.requireNonNull(cache);
        this.leveledCreationNodeToolProvider = Objects.requireNonNull(leveledCreationNodeToolProvider);
    }

    @Override
    public List<NodeToolSection> defaultCase(EObject object) {
        return List.of();
    }

    @Override
    public List<NodeToolSection> casePartUsage(PartUsage object) {
        var optionalPortNodeDescription = this.cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage()));
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new ChildrenPartUsageCompartmentNodeToolProvider().create(this.cache));
        if (optionalPortNodeDescription.isPresent()) {
            var portNodeDescription = optionalPortNodeDescription.get();
            this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                    this.toolDescriptionService.createNodeTool(portNodeDescription, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE));
            this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                    this.toolDescriptionService.createNodeToolWithDirection(portNodeDescription, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE, FeatureDirectionKind.IN));
            this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                    this.toolDescriptionService.createNodeToolWithDirection(portNodeDescription, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE, FeatureDirectionKind.INOUT));
            this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                    this.toolDescriptionService.createNodeToolWithDirection(portNodeDescription, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE, FeatureDirectionKind.OUT));
        }
        // add tools depending whether the PartUsage is at the first level or not.
        var nodeTools = this.leveledCreationNodeToolProvider.getNodeTools(object, this.descriptionNameGenerator, this.cache);
        for (var nodeTool : nodeTools) {
            this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, nodeTool);
        }
        this.createToolsForCompartmentItems(object, sections, this.cache);
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseActionUsage(ActionUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new StartActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new DoneActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new JoinActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new ForkActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new MergeActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new DecisionActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new AcceptActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new AssignmentActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new ReferencingPerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new PerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage())).ifPresent(itemNodeDescription -> {
            this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                    this.toolDescriptionService.createNodeTool(itemNodeDescription, SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getFeatureMembership(),
                            NodeContainmentKind.BORDER_NODE));
            this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                    .createNodeToolWithDirection(itemNodeDescription, SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.IN));
            this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                    .createNodeToolWithDirection(itemNodeDescription, SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.INOUT));
            this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                    .createNodeToolWithDirection(itemNodeDescription, SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.OUT));
        });
        this.createToolsForCompartmentItems(object, sections, this.cache);
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new ActionFlowCompartmentNodeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    protected List<EReference> getElementCompartmentReferences(Element element) {
        List<EReference> compartmentRefs = new ArrayList<>();
        List<EReference> refs = InterconnectionViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.get(element.eClass());
        if (refs != null) {
            compartmentRefs.addAll(refs);
        }
        compartmentRefs.removeIf(ref -> Objects.equals(ref, SysmlPackage.eINSTANCE.getElement_Documentation()));
        return compartmentRefs;
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions() {
        return this.cache.getNodeDescriptions();
    }

}
