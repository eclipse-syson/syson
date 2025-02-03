/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.services.api;

import org.eclipse.syson.sysml.Element;

/**
 * Service that moves an element inside a new parent.
 *
 * @author Arthur Daussy
 */
public interface ISysMLMoveElementService {

    /**
     * Moves an element into a new parent.
     *
     * @param element
     *            the element to move
     * @param newParent
     *            the new parent
     * @return a success {@link MoveStatus} if the element has been moved, a failing {@link MoveStatus} with an
     *         explanation message otherwise
     */
    MoveStatus moveSemanticElement(Element element, Element newParent);

}
