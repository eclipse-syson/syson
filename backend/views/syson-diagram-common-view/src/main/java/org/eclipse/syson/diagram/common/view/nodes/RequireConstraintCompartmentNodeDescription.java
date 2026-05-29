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
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.tools.RequireConstraintNodeToolProvider;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Compartment node allowing to display {@link org.eclipse.syson.sysml.ConstraintUsage} owned by a {@link org.eclipse.syson.sysml.RequirementConstraintMembership} with {@link org.eclipse.syson.sysml.RequirementConstraintKind#REQUIREMENT}.
 *
 * @author gcoutable
 */
public class RequireConstraintCompartmentNodeDescription extends AbstractCompartmentNodeDescriptionProvider {

    public static final String COMPARTMENT_NAME = " require";

    public RequireConstraintCompartmentNodeDescription(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getCompartmentName() {
        return this.getDescriptionNameGenerator().getCompartmentName(this.eClass, this.eReference) + COMPARTMENT_NAME;
    }

    @Override
    protected String getCustomCompartmentLabel() {
        return "require constraints";
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getNodeDescription(this.getCompartmentName()).ifPresent(nodeDescription -> {
            cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(this.eClass, this.eReference) + RequireConstraintCompartmentItemNodeDescription.COMPARTMENT_ITEM_NAME)
                    .ifPresent(itemNodeDescription -> nodeDescription.getChildrenDescriptions().add(itemNodeDescription));
            nodeDescription.setPalette(this.createCompartmentPalette(cache));
        });
    }

    @Override
    protected List<INodeToolProvider> getItemCreationToolProviders() {
        return List.of(new RequireConstraintNodeToolProvider());
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        return List.of();
    }
}
