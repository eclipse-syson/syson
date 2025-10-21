/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.ParameterCompartmentNodeToolProvider;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * ActionUsage Parameters Compartment node description.
 *
 * @author arichard
 */
public class ActionUsageParametersCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public static final String COMPARTMENT_NAME = "parameters";

    public ActionUsageParametersCompartmentNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedReference(), colorProvider, descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        NodeDescription nd = super.create();
        nd.setName(this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference) + COMPARTMENT_NAME);
        return nd;
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference) + COMPARTMENT_NAME).ifPresent(nodeDescription -> {
            cache.getNodeDescription(this.getDescriptionNameGenerator().getInheritedCompartmentItemName(this.eClass, this.eReference))
                    .ifPresent(node -> nodeDescription.getChildrenDescriptions().add(node));
            cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, this.eReference))
                    .ifPresent(node -> nodeDescription.getChildrenDescriptions().add(node));
            nodeDescription.setPalette(this.createCompartmentPalette(cache));
        });
    }

    @Override
    protected String getCustomCompartmentLabel() {
        return "parameters";
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();
        return acceptedNodeTypes;
    }

    @Override
    protected List<INodeToolProvider> getItemCreationToolProviders() {
        List<INodeToolProvider> creationToolProviders = new ArrayList<>();
        creationToolProviders.add(new ParameterCompartmentNodeToolProvider(FeatureDirectionKind.IN));
        creationToolProviders.add(new ParameterCompartmentNodeToolProvider(FeatureDirectionKind.INOUT));
        creationToolProviders.add(new ParameterCompartmentNodeToolProvider(FeatureDirectionKind.OUT));
        return creationToolProviders;
    }
}
