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
package org.eclipse.syson.diagram.requirement.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractFakeNodeDescriptionProvider;
import org.eclipse.syson.diagram.requirement.view.RVDescriptionNameGenerator;
import org.eclipse.syson.diagram.requirement.view.RequirementViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Fake Node Description allowing to store other Node Descriptions that will be reused by other Node Descriptions.
 * Typical use is for compartment Nodes.
 *
 * @author Jerome Gout
 */
public class FakeNodeDescriptionProvider extends AbstractFakeNodeDescriptionProvider {

    public FakeNodeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    protected String getName() {
        return "RV Node Fake";
    }

    @Override
    protected List<NodeDescription> getChildrenDescription(IViewDiagramElementFinder cache) {
        var nameGenerator = new RVDescriptionNameGenerator();
        var childrenNodes = new ArrayList<NodeDescription>();

        RequirementViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            listItems.forEach(eReference -> cache.getNodeDescription(nameGenerator.getCompartmentName(type, eReference)).ifPresent(childrenNodes::add));
        });
        // don't forget to add requirement usage/definition subject compartment
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        return childrenNodes;
    }
}
