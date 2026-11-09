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
package org.eclipse.syson.sysml.metamodel.services;

/**
 * Outcome of initializing the properties of a redefining feature.
 *
 * @author Arthur Daussy
 */
public record RedefinitionStatus(boolean isSuccess, String message) {

    public static RedefinitionStatus success() {
        return new RedefinitionStatus(true, "");
    }

    public static RedefinitionStatus failure(String message) {
        return new RedefinitionStatus(false, message);
    }
}
