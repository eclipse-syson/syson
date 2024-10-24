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

package org.eclipse.syson.diagram.interconnection.view.services;

import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to provide the list of context specific creation node tools.
 * This is used for handling first level child node descriptions and nested node descriptions with the same meta model element.
 *
 * @author Jerome Gout
 */
public interface ILeveledCreationNodeToolsProvider {
    /**
     * Returns the list of creation node tools that are depending of the node description context.
     *
     * @param element
     *         the element for which the creation node tools are intended
     * @param descriptionNameGenerator
     *         the description name generator to be used for node tool name
     * @param cache
     *         the node description cache, which contains all node descriptions
     * @return the list of node tools that should be added to the creation tool section of a node description depending of context.
     *
     */
    List<NodeTool> getNodeTools(Element element, IDescriptionNameGenerator descriptionNameGenerator,  IViewDiagramElementFinder cache);
}
