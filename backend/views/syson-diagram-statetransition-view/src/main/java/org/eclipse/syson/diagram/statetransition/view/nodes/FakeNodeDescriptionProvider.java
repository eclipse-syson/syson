/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
package org.eclipse.syson.diagram.statetransition.view.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractFakeNodeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Fake Node Description allowing to store other Node Descriptions that will be reused by other Node Descriptions.
 * Typical use is for compartment Nodes.
 *
 * @author arichard
 */
public class FakeNodeDescriptionProvider extends AbstractFakeNodeDescriptionProvider {

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public FakeNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    protected String getName() {
        return "STV Node Fake";
    }

    @Override
    protected List<NodeDescription> getChildrenDescription(IViewDiagramElementFinder cache) {
        var childrenNodes = new ArrayList<NodeDescription>();

        StateTransitionViewDiagramDescriptionProvider.COMPARTMENTS_WITH_MERGED_LIST_ITEMS.forEach((type, listItems) -> {
            listItems.forEach(eReference -> {
                cache.getNodeDescription(this.descriptionNameGenerator.getCompartmentName(type, eReference)).ifPresent(childrenNodes::add);   
            });
        });

        // don't forget to add custom compartments
        cache.getNodeDescription(this.descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState())).ifPresent(childrenNodes::add);
        return childrenNodes;
    }
}
