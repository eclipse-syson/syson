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
package org.eclipse.syson.diagram.interconnection.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.IVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Used to create the Attributes Compartment node description inside the Interconnection View
 * {@link ChildPartUsageNodeDescriptionProvider}.
 *
 * @author arichard
 */
public class CompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public CompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider) {
        super(eClass, eReference, colorProvider, new IVDescriptionNameGenerator());
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();
        cache.getNodeDescription(this.nameGenerator.getCompartmentItemName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute())).ifPresent(acceptedNodeTypes::add);
        return acceptedNodeTypes;
    }
}
