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
package org.eclipse.syson.diagram.common.view;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.diagram.DiagramElementDescription;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Finder of {@link DiagramElementDescription} that provides methods to select {@link DiagramElementDescription} among a list of given descriptions given some criteria.
 *
 * @author Arthur Daussy
 */
public class DescriptionFinder {

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public DescriptionFinder(IDescriptionNameGenerator descriptionNameGenerator) {
        this.descriptionNameGenerator = descriptionNameGenerator;
    }

    /**
     * Returns the all {@link NodeDescription}s representing the given type that can be a source or a target of and edge tool.
     *
     * <p>Technical descriptions such as compartment are excluded. This method returns both "regular" nodes and "bordered" nodes.</p>
     *
     * @param allNodeDescriptions
     *         collection of all {@link NodeDescription}
     * @return the list of description representing usages
     */
    public List<NodeDescription> getConnectableNodeDescriptions(List<NodeDescription> allNodeDescriptions, EClass type) {
        return allNodeDescriptions.stream()
                .filter(nodeDescription -> this.isDomain(nodeDescription, type))
                .filter(nodeDescription -> !this.descriptionNameGenerator.isCompartmentNodeDescriptionName(nodeDescription.getName())
                        && !this.descriptionNameGenerator.isCompartmentItemNodeDescriptionName(nodeDescription.getName()))
                .toList();
    }

    private boolean isDomain(NodeDescription nodeDescription, EClass type) {
        return nodeDescription.getDomainType() != null
                && SysmlPackage.eINSTANCE.getEClassifier(nodeDescription.getDomainType().replaceFirst("sysml::", "")) instanceof EClass domain
                && type.isSuperTypeOf(domain);
    }
}
