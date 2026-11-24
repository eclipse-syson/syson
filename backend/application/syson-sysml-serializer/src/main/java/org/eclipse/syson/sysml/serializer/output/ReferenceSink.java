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

import org.eclipse.emf.ecore.EObject;

/**
 * Receives reference ranges while an HTML document is being assembled.
 * Offsets are half-open UTF-16 positions in the plain SysML text.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@FunctionalInterface
interface ReferenceSink {

    ReferenceSink NONE = (target, startOffset, endOffset) -> {
        // Nothing to collect.
    };

    /**
     * Records a reference occurrence in the unescaped SysML text, after its spelling has been written.
     *
     * @param target
     *            the referenced object, possibly a proxy, or {@code null} for an unresolved parser spelling
     * @param startOffset
     *            the inclusive UTF-16 offset of the reference's first character in the plain-text output
     * @param endOffset
     *            the exclusive UTF-16 offset just after the reference in the same output
     */
    void reference(EObject target, int startOffset, int endOffset);
}
