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

package org.eclipse.syson.diagram.interconnection.view;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;

/**
 * Interface used for description name generator that should handle leveled nodes.
 *
 * @author Jerome Gout
 */
public interface ILeveledNodeDescriptionNameGenerator {

    /**
     * Returns the name of a {@link NodeDescription} at first level for the given {@link EClass}.
     *
     * @param eClass
     *         the {@link EClass} used to compute the name of the {@link NodeDescription}.
     * @return a string used to name a {@link NodeDescription}.
     */
    String getFirstLevelNodeName(EClass eClass);

    /**
     * Returns the name of a {@link NodeDescription} at nested level for the given {@link EClass}.
     * This method should not be used for first level nodes.
     *
     * @param eClass
     *         the {@link EClass} used to compute the name of the {@link NodeDescription}.
     * @return a string used to name a {@link NodeDescription}.
     */
    String getChildNodeName(EClass eClass);
}
