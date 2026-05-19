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
package org.eclipse.syson.services;

import java.util.Objects;

import org.eclipse.sirius.components.core.api.IReadOnlyObjectPredicate;
import org.eclipse.syson.services.api.ISysMLMoveElementCheckerService;
import org.eclipse.syson.services.api.MoveStatus;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link ISysMLMoveElementCheckerService}.
 *
 * @author Arthur Daussy
 */
@Service
public class SysMLMoveElementCheckerService implements ISysMLMoveElementCheckerService {

    private final IReadOnlyObjectPredicate readOnlyObjectPredicate;

    public SysMLMoveElementCheckerService(IReadOnlyObjectPredicate readOnlyObjectPredicate) {
        this.readOnlyObjectPredicate = Objects.requireNonNull(readOnlyObjectPredicate);
    }

    @Override
    public MoveStatus canMove(Element element, Element newParent) {
        final MoveStatus moveStatus;
        if (element == newParent) {
            // DnD is quite sensitive in the frontend, we want to avoid sending a message each time a user do a micro
            // DnD on the item itself. Instead we ignore the DnD. Drawbacks: the model is persisted in DB.
            moveStatus = MoveStatus.buildSuccess();
        } else if (EMFUtils.isAncestor(element, newParent)) {
            moveStatus = MoveStatus.buildFailure("Unable to move an Element to one of its descendant");
        } else if (this.readOnlyObjectPredicate.test(element)) {
            moveStatus = MoveStatus.buildFailure("Unable to move a read only Element");
        } else if (this.readOnlyObjectPredicate.test(newParent)) {
            moveStatus = MoveStatus.buildFailure("Unable to move a Element to a read only Element");
        } else {
            moveStatus = MoveStatus.buildSuccess();
        }
        return moveStatus;
    }
}
