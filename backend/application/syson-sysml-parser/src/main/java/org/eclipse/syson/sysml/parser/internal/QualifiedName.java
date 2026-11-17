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
package org.eclipse.syson.sysml.parser.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * A qualified name.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class QualifiedName {

    /**
     * The separator (::) constant.
     */
    public static final String SEPARATOR = "::";

    /**
     * The global ($) constant.
     */
    public static final String GLOBAL = "$";

    /**
     * The conjugated (~) constant.
     */
    public static final String CONJUGATED = "~";

    /**
     * Tells if the qualified name is global (starts with <code>"$::"</code>).
     */
    private boolean global;

    /**
     * The {@link List} of segments.
     */
    private final List<String> segments = new ArrayList<>();

    /**
     * Sets if this qualified name is global (starts with <code>"$::"</code>).
     *
     * @param global
     *            the global value
     */
    public void setGlobal(boolean global) {
        this.global = global;
    }

    /**
     * Tells if the qualified name is global (starts with <code>"$::"</code>). If <code>true</code> the
     * <code>"$::"</code> is not in the {@link #segments}.
     *
     * @return <code>true</code> if the qualified name is global (starts with <code>"$::"</code>), <code>false</code>
     *         otherwise
     */
    public boolean isGlobal() {
        return this.global;
    }

    /**
     * Adds a segment to this qualified name.
     *
     * @param segment
     *            the segment to add
     */
    public void addSegment(String segment) {
        this.segments.add(segment);
    }

    @Override
    public String toString() {
        StringJoiner joiner;

        if (this.isGlobal()) {
            joiner = new StringJoiner(SEPARATOR, GLOBAL + SEPARATOR, "");
        } else {
            joiner = new StringJoiner(SEPARATOR);
        }

        for (String segment : this.segments) {
            joiner.add(segment);
        }

        return joiner.toString();
    }

}
