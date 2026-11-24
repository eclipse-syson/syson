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
package org.eclipse.syson.sysml.serializer.output;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.omg.sysml.lang.sysml.Element;

/**
 * Collects the model ranges needed while producing an HTML document.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class ElementTrace {

    static final ElementTrace NONE = new ElementTrace(false);

    private final boolean enabled;

    private final Deque<OpenRange> openRanges = new ArrayDeque<>();

    private final List<ElementRange> completedRanges = new ArrayList<>();

    ElementTrace() {
        this(true);
    }

    private ElementTrace(final boolean enabled) {
        this.enabled = enabled;
    }

    void begin(final Element element, final int offset) {
        if (this.enabled && element != null) {
            this.openRanges.push(new OpenRange(element, offset));
        }
    }

    /** Closes the top range by identity; a mismatched end discards that range without recording it. */
    void end(final Element element, final int offset) {
        if (!this.enabled || element == null || this.openRanges.isEmpty()) {
            return;
        }
        final OpenRange openRange = this.openRanges.pop();
        if (openRange.element() == element) {
            this.completedRanges.add(
                    new ElementRange(element, openRange.startOffset(), offset));
        }
    }

    /** Returns an immutable snapshot in closing order, so nested ranges precede their parents. */
    List<ElementRange> ranges() {
        return List.copyOf(this.completedRanges);
    }

    /** Start of an element whose output range has not yet been closed. */
    private record OpenRange(Element element, int startOffset) {
    }

    /** Half-open UTF-16 range occupied by an element in the output buffer. */
    record ElementRange(Element element, int startOffset, int endOffset) {
    }
}
