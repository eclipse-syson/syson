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
package org.eclipse.syson.sysml.util;

import java.util.function.Supplier;

/**
 * Runs derived SysON queries without materializing proxies or searching unindexed library resources.
 *
 * @author arichard
 */
public final class NonResolvingReads {

    private static final ThreadLocal<Boolean> ACTIVE = new ThreadLocal<>();

    /** Prevents instantiation. */
    private NonResolvingReads() {
    }

    /** Returns whether the current thread is evaluating a read-only query on stored objects. */
    public static boolean isActive() {
        return Boolean.TRUE.equals(ACTIVE.get());
    }

    /** Evaluates the supplied query, restoring the previous resolution policy even when it fails. */
    public static <T> T compute(Supplier<T> query) {
        if (isActive()) {
            return query.get();
        }
        ACTIVE.set(true);
        try {
            return query.get();
        } finally {
            ACTIVE.remove();
        }
    }
}
