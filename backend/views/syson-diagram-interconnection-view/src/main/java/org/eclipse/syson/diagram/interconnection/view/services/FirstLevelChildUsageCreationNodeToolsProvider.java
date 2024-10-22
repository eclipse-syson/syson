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

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.interconnection.view.ILeveledNodeDescriptionNameGenerator;
import org.eclipse.syson.diagram.interconnection.view.tools.PartUsageBindingConnectorAsUsageNodeToolProvider;
import org.eclipse.syson.diagram.interconnection.view.tools.PartUsageFlowConnectionNodeToolProvider;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * First level child creation node tool provider.
 * The creation node tools provided by this class should be added to first level node descriptions only.
 *
 * @author Jerome Gout
 */
public class FirstLevelChildUsageCreationNodeToolsProvider implements ILeveledCreationNodeToolsProvider {
    @Override
    public List<NodeTool> getNodeTools(Element element, IDescriptionNameGenerator descriptionNameGenerator, IViewDiagramElementFinder cache) {
        List<NodeTool> result = List.of();
        if (descriptionNameGenerator instanceof ILeveledNodeDescriptionNameGenerator leveledNodeDescriptionNameGenerator) {
            var firstLevelPartUsageNodeDescriptionName = leveledNodeDescriptionNameGenerator.getFirstLevelNodeName(element.eClass());
            var bindingConnectorAsUsageTool = new PartUsageBindingConnectorAsUsageNodeToolProvider(descriptionNameGenerator, firstLevelPartUsageNodeDescriptionName).create(cache);
            var flowConnectionTool = new PartUsageFlowConnectionNodeToolProvider(descriptionNameGenerator, firstLevelPartUsageNodeDescriptionName).create(cache);
            result = List.of(bindingConnectorAsUsageTool, flowConnectionTool);
        }
        return result;
    }
}
