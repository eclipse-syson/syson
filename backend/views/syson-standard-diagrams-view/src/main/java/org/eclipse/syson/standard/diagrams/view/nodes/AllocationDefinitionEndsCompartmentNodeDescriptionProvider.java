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
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.AllocationDefinitionEndCompartmentNodeToolProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to handle the Ends compartment of Allocation Definition.
 *
 * @author Jerome Gout
 */
public class AllocationDefinitionEndsCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public AllocationDefinitionEndsCompartmentNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(SysmlPackage.eINSTANCE.getAllocationDefinition(), SysmlPackage.eINSTANCE.getConnectionDefinition_ConnectionEnd(), colorProvider, nameGenerator);
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        return List.of();
    }

    @Override
    protected String getCustomCompartmentLabel() {
        return "ends";
    }

    @Override
    protected List<INodeToolProvider> getItemCreationToolProviders() {
        List<INodeToolProvider> creationToolProviders = new ArrayList<>();
        creationToolProviders.add(new AllocationDefinitionEndCompartmentNodeToolProvider());
        return creationToolProviders;
    }
}
