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
import org.eclipse.syson.diagram.interconnection.view.tools.PartUsageBindingConnectorAsUsageNodeToolProvider;
import org.eclipse.syson.diagram.interconnection.view.tools.PartUsageFlowConnectionNodeToolProvider;
import org.eclipse.syson.diagram.interconnection.view.tools.PartUsageInterfaceNodeToolProvider;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Child creation node tools provider.
 * The creation node tools provided by this class should only be added to nested child node descriptions (not first level one).
 *
 * @author Jerome Gout
 */
public class ChildUsageCreationNodeToolsProvider implements ILeveledCreationNodeToolsProvider {
    @Override
    public List<NodeTool> getNodeTools(Element element, IDescriptionNameGenerator descriptionNameGenerator, IViewDiagramElementFinder cache) {
        List<NodeTool> result = List.of();
        var nodeDescriptionName = descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage());
        var bindingConnectorAsUsageTool = new PartUsageBindingConnectorAsUsageNodeToolProvider(descriptionNameGenerator, nodeDescriptionName).create(cache);
        var flowConnectionTool = new PartUsageFlowConnectionNodeToolProvider(descriptionNameGenerator, nodeDescriptionName).create(cache);
        var interfaceTool = new PartUsageInterfaceNodeToolProvider(descriptionNameGenerator, nodeDescriptionName).create(cache);
        result = List.of(bindingConnectorAsUsageTool, flowConnectionTool, interfaceTool);
        return result;
    }
}