/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.standard.diagrams.view.nodes;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.nodes.AbstractUsageNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.DecisionActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.DoneActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ForkActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.JoinActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.MergeActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StartActionNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.standard.diagrams.view.SDVDiagramDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.services.SDVNodeToolSectionSwitch;
import org.eclipse.syson.standard.diagrams.view.services.SDVNodeToolsWithoutSectionSwitch;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;

/**
 * Node description provider for all SysMLv2 Usage elements in the General View diagram.
 *
 * @author arichard
 */
public class UsageNodeDescriptionProvider extends AbstractUsageNodeDescriptionProvider {

    public UsageNodeDescriptionProvider(EClass eClass, IColorProvider colorProvider) {
        super(eClass, colorProvider, new SDVDescriptionNameGenerator());
    }

    @Override
    protected String getSemanticCandidatesExpression(String domainType) {
        return AQLUtils.getSelfServiceCallExpression("getExposedElements",
                List.of(domainType, org.eclipse.sirius.components.diagrams.description.NodeDescription.ANCESTORS, IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT));
    }

    @Override
    protected String createPreconditionExpression() {
        // Actors are handled with a different NodeDescription: ActorNodeDescriptionProvider.
        // We can't represent actors with conditional styles because they require to remove inside labels and add
        // outside labels, and set the keepAspectRatio property, which are all defined in the NodeDescription and not in
        // the style.
        return AQLConstants.AQL + "not self.isActor()";
    }

    @Override
    protected Set<NodeDescription> getReusedChildren(IViewDiagramElementFinder cache) {
        var reusedChildren = new LinkedHashSet<NodeDescription>();

        SDVDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            if (type.equals(this.eClass)) {
                listItems.forEach(eReference -> {
                    // list compartment
                    cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(type, eReference)).ifPresent(reusedChildren::add);
                });
            }
        });

        SDVDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            if (type.equals(this.eClass)) {
                listItems.forEach(eReference -> {
                    // free form compartment
                    cache.getNodeDescription(this.getDescriptionNameGenerator().getFreeFormCompartmentName(this.eClass, eReference)).ifPresent(reusedChildren::add);
                });
            }
        });

        return reusedChildren;
    }

    @Override
    protected Set<NodeDescription> getReusedBorderNodes(IViewDiagramElementFinder cache) {
        var borderNodes = new LinkedHashSet<NodeDescription>();
        SDVDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            if (type.equals(this.eClass)) {
                listItems.forEach(eReference -> {
                    if (eReference.equals(SysmlPackage.eINSTANCE.getUsage_NestedPort())) {
                        cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage())).ifPresent(borderNodes::add);
                    }
                });
            }
        });
        if (SysmlPackage.eINSTANCE.getActionUsage().equals(this.eClass)) {
            cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage())).ifPresent(borderNodes::add);
        }
        return borderNodes;
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions(IViewDiagramElementFinder cache) {
        var allNodes = new ArrayList<NodeDescription>();

        SDVDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(definition)).ifPresent(allNodes::add));
        SDVDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(usage)).ifPresent(allNodes::add));
        SDVDiagramDescriptionProvider.ANNOTATINGS.forEach(usage -> cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(usage)).ifPresent(allNodes::add));
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(allNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(StartActionNodeDescriptionProvider.START_ACTION_NAME)).ifPresent(allNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(DoneActionNodeDescriptionProvider.DONE_ACTION_NAME)).ifPresent(allNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(JoinActionNodeDescriptionProvider.JOIN_ACTION_NAME)).ifPresent(allNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(ForkActionNodeDescriptionProvider.FORK_ACTION_NAME)).ifPresent(allNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(MergeActionNodeDescriptionProvider.MERGE_ACTION_NAME)).ifPresent(allNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(DecisionActionNodeDescriptionProvider.DECISION_ACTION_NAME)).ifPresent(allNodes::add);
        return allNodes;
    }

    @Override
    protected List<NodeToolSection> getToolSections(NodeDescription nodeDescription, IViewDiagramElementFinder cache) {
        SDVNodeToolSectionSwitch toolSectionSwitch = new SDVNodeToolSectionSwitch(cache, this.getAllNodeDescriptions(cache));
        return toolSectionSwitch.doSwitch(this.eClass);
    }

    @Override
    protected List<NodeTool> getToolsWithoutSection(NodeDescription nodeDescription, IViewDiagramElementFinder cache) {
        SDVNodeToolsWithoutSectionSwitch toolsWithoutSectionSwitch = new SDVNodeToolsWithoutSectionSwitch(cache, this.getAllNodeDescriptions(cache));
        return toolsWithoutSectionSwitch.doSwitch(this.eClass);
    }
}
