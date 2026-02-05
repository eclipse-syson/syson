/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.tools.SatisfyNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.SatisfyRequirementNodeToolProvider;
import org.eclipse.syson.sysml.SatisfyRequirementUsage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Compartment node allowing to display {@link SatisfyRequirementUsage}.
 *
 * @author arichard
 */
public class SatisfyRequirementCompartmentNodeDescription extends AbstractCompartmentNodeDescriptionProvider {

    public static final String COMPARTMENT_NAME = " satisfy requirements";

    public SatisfyRequirementCompartmentNodeDescription(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getCompartmentName() {
        return this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference) + COMPARTMENT_NAME;
    }

    @Override
    protected String getCustomCompartmentLabel() {
        return "satisfy requirements";
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference) + COMPARTMENT_NAME).ifPresent(nodeDescription -> {
            cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, this.eReference) + SatisfyRequirementCompartmentItemNodeDescription.COMPARTMENT_ITEM_NAME)
                    .ifPresent(itemNodeDesc -> nodeDescription.getChildrenDescriptions().add(itemNodeDesc));
            nodeDescription.setPalette(this.createCompartmentPalette(cache));
        });
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();
        return acceptedNodeTypes;
    }

    @Override
    protected List<INodeToolProvider> getItemCreationToolProviders() {
        List<INodeToolProvider> creationToolProviders = new ArrayList<>();
        creationToolProviders.add(new SatisfyRequirementNodeToolProvider());
        creationToolProviders.add(new SatisfyNodeToolProvider());
        return creationToolProviders;
    }
}
