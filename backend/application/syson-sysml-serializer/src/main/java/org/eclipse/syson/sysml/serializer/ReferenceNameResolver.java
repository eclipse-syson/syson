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
package org.eclipse.syson.sysml.serializer;

import org.omg.sysml.lang.sysml.Element;

/**
 * Lets the caller choose how a referenced element is spelled at a given reference site, typically the shortest name
 * that the namespaces and imports visible from that site make unambiguous.
 * <p>
 * The serializer has no global name-resolution scope of its own; without a resolver it writes qualified names. A
 * non-blank answer replaces that name verbatim, so it must be a valid SysML reference (escaped where needed) that
 * resolves back to {@code target} from {@code context}.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@FunctionalInterface
public interface ReferenceNameResolver {

    /**
     * Returns the name to write for {@code target} when referenced from {@code context}.
     *
     * @param target
     *            the referenced element
     * @param context
     *            the element whose syntax contains the reference (the enclosing name scope)
     * @return the contextual name, or an empty string (or {@code null}) to keep the serializer's default name
     */
    String resolve(Element target, Element context);
}
