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
package org.eclipse.syson.services.api;

import org.eclipse.syson.sysml.Element;

/**
 * Service used to check if SysML elements can be moved.
 *
 * @author Arthur Daussy
 */
public interface ISysMLMoveElementCheckerService {

    /**
     * Checks if an element can be moved into a new parent.
     *
     * @param element
     *            the element to move
     * @param newParent
     *            the new parent
     * @return a success {@link MoveStatus} if the element can be moved, a failing {@link MoveStatus} with an
     *         explanation message otherwise
     */
    MoveStatus canMove(Element element, Element newParent);

}
