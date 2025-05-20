/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.actionflow.view.ActionFlowViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.AbstractFakeNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Fake Node Description allowing to store other Node Descriptions that will be reused by other Node Descriptions.
 * Typical use is for compartment Nodes.
 *
 * @author arichard
 */
public class FakeNodeDescriptionProvider extends AbstractFakeNodeDescriptionProvider {

    public FakeNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getName() {
        return "AFV Node Fake";
    }

    @Override
    protected List<NodeDescription> getChildrenDescription(IViewDiagramElementFinder cache) {
        var childrenNodes = new ArrayList<NodeDescription>();

        ActionFlowViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            listItems.forEach(eReference -> cache.getNodeDescription(this.descriptionNameGenerator.getCompartmentName(type, eReference)).ifPresent(childrenNodes::add));
        });

        cache.getNodeDescription(this.descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getPerformActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction()))
                .ifPresent(childrenNodes::add);

        this.addReusableCustomNodes(cache, childrenNodes);

        return childrenNodes;
    }
}
