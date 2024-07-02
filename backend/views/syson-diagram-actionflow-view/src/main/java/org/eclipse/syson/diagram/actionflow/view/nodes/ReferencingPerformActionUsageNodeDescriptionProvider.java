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
package org.eclipse.syson.diagram.actionflow.view.nodes;

import java.util.List;
import java.util.Set;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.services.description.ReferencingPerformActionUsageNodeDescriptionService;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the "referencing" PerformActionUsage node description in the Action Flow View diagram.<br>
 * There are two separate node descriptions mapped to {@link PerformActionUsage}:<br>
 * - {@link ReferencingPerformActionUsageNodeDescriptionProvider}: when the performed action is a separated action
 * usage<br>
 * - regular {@link UsageNodeDescriptionProvider} with PerformActionUsage: when the performed action is the perform
 * action itself.
 *
 * @author Jerome Gout
 */
public class ReferencingPerformActionUsageNodeDescriptionProvider extends UsageNodeDescriptionProvider {

    public ReferencingPerformActionUsageNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(SysmlPackage.eINSTANCE.getPerformActionUsage(), colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getNodeDescriptionName() {
        return this.getDescriptionNameGenerator().getNodeName(ReferencingPerformActionUsageNodeDescriptionService.REFERENCING_PERFORM_ACTION_NAME);
    }

    @Override
    protected String getSemanticCandidatesExpression(String domainType) {
        return AQLUtils.getSelfServiceCallExpression("getAllReferencingPerformActionUsages");
    }

    @Override
    protected List<NodeToolSection> getToolSections(NodeDescription nodeDescription, IViewDiagramElementFinder cache) {
        return List.of();
    }

    @Override
    protected Set<NodeDescription> getReusedChildren(IViewDiagramElementFinder cache) {
        return Set.of();
    }
}
