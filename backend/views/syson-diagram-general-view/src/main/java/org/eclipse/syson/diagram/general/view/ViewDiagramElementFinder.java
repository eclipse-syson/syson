/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.diagram.general.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.IViewObjectCache;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;

/**
 * Store and retrieve created element.
 *
 * @author mcharfadi
 */
public class ViewDiagramElementFinder implements IViewObjectCache, IViewDiagramElementFinder {

    private final Map<String, List<EObject>> data = new HashMap<>();

    @Override
    public void put(EObject eObject) {
        var eObjects = this.data.getOrDefault(eObject.eClass().getName(), new ArrayList<>());
        eObjects.add(eObject);
        this.data.put(eObject.eClass().getName(), eObjects);
    }

    @Override
    public Optional<NodeDescription> getNodeDescription(String name) {
        return this.data.getOrDefault("NodeDescription", List.of()).stream()
                .filter(NodeDescription.class::isInstance)
                .map(NodeDescription.class::cast)
                .filter(nodeDescription -> nodeDescription.getName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<EdgeDescription> getEdgeDescription(String name) {
        return this.data.getOrDefault("EdgeDescription", List.of()).stream()
                .filter(EdgeDescription.class::isInstance)
                .map(EdgeDescription.class::cast)
                .filter(edgeDescription -> edgeDescription.getName().equals(name))
                .findFirst();
    }


}
