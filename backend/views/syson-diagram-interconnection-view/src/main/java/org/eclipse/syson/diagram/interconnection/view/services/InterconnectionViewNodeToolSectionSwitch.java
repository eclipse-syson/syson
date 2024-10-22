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

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.services.AbstractViewNodeToolSectionSwitch;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
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

    private final ToolDescriptionService toolDescriptionService;

    private final ILeveledCreationNodeToolsProvider leveledCreationNodeToolProvider;

    public InterconnectionViewNodeToolSectionSwitch(IViewDiagramElementFinder cache, ILeveledCreationNodeToolsProvider leveledCreationNodeToolProvider) {
        super(new IVDescriptionNameGenerator());
        this.cache = Objects.requireNonNull(cache);
        this.toolDescriptionService = new ToolDescriptionService(this.descriptionNameGenerator);
        this.leveledCreationNodeToolProvider = Objects.requireNonNull(leveledCreationNodeToolProvider);
    }

    @Override
    public List<NodeToolSection> casePartUsage(PartUsage object) {
        var optionalPortNodeDescription = this.cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage()));

        var createSection = this.toolDescriptionService.buildCreateSection(
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getComment()),
                        SysmlPackage.eINSTANCE.getComment(), SysmlPackage.eINSTANCE.getOwningMembership(), null),
                new ChildrenPartUsageCompartmentNodeToolProvider().create(null));
        if (optionalPortNodeDescription.isPresent()) {
            var portNodeDescription = optionalPortNodeDescription.get();
            createSection.getNodeTools().add(this.toolDescriptionService.createNodeTool(portNodeDescription, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE));
            createSection.getNodeTools().add(this.toolDescriptionService.createNodeToolWithDirection(portNodeDescription, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE, FeatureDirectionKind.IN));
            createSection.getNodeTools().add(this.toolDescriptionService.createNodeToolWithDirection(portNodeDescription, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE, FeatureDirectionKind.INOUT));
            createSection.getNodeTools().add(this.toolDescriptionService.createNodeToolWithDirection(portNodeDescription, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE, FeatureDirectionKind.OUT));
        }

        // add tools depending whether the PartUsage is at the first level or not.
        createSection.getNodeTools().addAll(this.leveledCreationNodeToolProvider.getNodeTools(object, this.descriptionNameGenerator, this.cache));

        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        var editSection = this.toolDescriptionService.buildEditSection(
                new SetAsCompositeToolProvider().create(this.cache),
                new SetAsRefToolProvider().create(this.cache));
        return List.of(createSection, editSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseActionUsage(ActionUsage object) {
        var createSection = this.toolDescriptionService.buildCreateSection(
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getComment()),
                        SysmlPackage.eINSTANCE.getComment(), SysmlPackage.eINSTANCE.getOwningMembership(), null),
                new StartActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new DoneActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new JoinActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new ForkActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new MergeActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new DecisionActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new AcceptActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new AssignmentActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new ReferencingPerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new PerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));

        NodeDescription itemNodeDescription = this.cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage())).get();
        createSection.getNodeTools().addAll(List.of(
                this.toolDescriptionService.createNodeTool(itemNodeDescription, SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getFeatureMembership(),
                        NodeContainmentKind.BORDER_NODE),
                this.toolDescriptionService.createNodeToolWithDirection(itemNodeDescription, SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getFeatureMembership(),
                        NodeContainmentKind.BORDER_NODE, FeatureDirectionKind.IN),
                this.toolDescriptionService.createNodeToolWithDirection(itemNodeDescription, SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getFeatureMembership(),
                        NodeContainmentKind.BORDER_NODE, FeatureDirectionKind.INOUT),
                this.toolDescriptionService.createNodeToolWithDirection(itemNodeDescription, SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getFeatureMembership(),
                        NodeContainmentKind.BORDER_NODE, FeatureDirectionKind.OUT)));

        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        createSection.getNodeTools().add(new ActionFlowCompartmentNodeToolProvider().create(null));
        var editSection = this.toolDescriptionService.buildEditSection(
                new SetAsCompositeToolProvider().create(this.cache),
                new SetAsRefToolProvider().create(this.cache));
        return List.of(createSection, editSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> defaultCase(EObject object) {
        return List.of();
    }

    @Override
    protected List<EReference> getElementCompartmentReferences(Element element) {
        List<EReference> refs = InterconnectionViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.get(element.eClass());
        return Objects.requireNonNullElseGet(refs, List::of);
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions() {
        return this.cache.getNodeDescriptions();
    }

}
