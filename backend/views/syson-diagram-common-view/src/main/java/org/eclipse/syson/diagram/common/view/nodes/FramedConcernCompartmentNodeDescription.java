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

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Compartment node allowing to display {@link org.eclipse.syson.sysml.ConcernUsage} owned by a {@link org.eclipse.syson.sysml.FramedConcernMembership}.
 *
 * @author gcoutable
 */
public class FramedConcernCompartmentNodeDescription extends AbstractCompartmentNodeDescriptionProvider {

    public static final String COMPARTMENT_NAME = " frames";

    public FramedConcernCompartmentNodeDescription(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getCompartmentName() {
        return this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference) + COMPARTMENT_NAME;
    }

    @Override
    protected String getCustomCompartmentLabel() {
        return "frames";
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference) + COMPARTMENT_NAME).ifPresent(nodeDescription -> {
            cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, this.eReference) + FramedConcernCompartmentItemNodeDescription.COMPARTMENT_ITEM_NAME)
                    .ifPresent(itemNodeDescription -> nodeDescription.getChildrenDescriptions().add(itemNodeDescription));
            nodeDescription.setPalette(this.createCompartmentPalette(cache));
        });
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        return List.of();
    }
}
