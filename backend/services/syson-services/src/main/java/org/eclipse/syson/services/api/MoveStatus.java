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

/**
 * A status of a move action.
 *
 * @author Arthur Daussy
 */
public record MoveStatus(boolean isSuccess, String message) {

    public static MoveStatus buildSuccess() {
        return new MoveStatus(true, "");
    }

    public static MoveStatus buildFailure(String failureMessage) {
        return new MoveStatus(false, failureMessage);
    }
}
