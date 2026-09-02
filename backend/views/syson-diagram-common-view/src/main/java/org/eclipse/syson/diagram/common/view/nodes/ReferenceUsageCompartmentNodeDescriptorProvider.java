/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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

package org.eclipse.syson.diagram.common.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.services.description.ToolConstants;
import org.eclipse.syson.diagram.common.view.tools.ReferenceUsageCompartmentNodeToolProvider;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the references Compartment node description of several nodes (PortUsage and PortDefinition for instance) inside the General View diagram.
 *
 * @author Jerome Gout
 */
public class ReferenceUsageCompartmentNodeDescriptorProvider extends AbstractCompartmentNodeDescriptionProvider {

    public ReferenceUsageCompartmentNodeDescriptorProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, this.eReference)).ifPresent(acceptedNodeTypes::add);
        return acceptedNodeTypes;
    }

    @Override
    protected NodePalette createCompartmentPalette(IViewDiagramElementFinder cache) {
        var palette = this.diagramBuilderHelper.newNodePalette().dropNodeTool(this.createCompartmentDropFromDiagramTool(cache));
        var toolSections = this.toolDescriptionService.createDefaultNodeToolSections();

        this.toolDescriptionService.addNodeTool(toolSections, ToolConstants.STRUCTURE, new ReferenceUsageCompartmentNodeToolProvider().create(cache));

        toolSections.add(this.diagramDefaultToolsFactory.createDefaultHideRevealNodeToolSection());
        this.toolDescriptionService.removeEmptyNodeToolSections(toolSections);

        return palette.toolSections(toolSections.toArray(NodeToolSection[]::new)).build();
    }
}
