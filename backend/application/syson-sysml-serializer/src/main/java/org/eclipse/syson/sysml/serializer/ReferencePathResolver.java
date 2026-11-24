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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Supplies the model target reached by each segment of a qualified reference, as the linker resolved it.
 * <p>
 * A segment may name an alias rather than the element it designates, so the serializer cannot infer the path from
 * the terminal target alone. With this information, round-trip output keeps a parser spelling such as an alias path
 * instead of treating it as a stale rename, and HTML output can link each segment to its own target.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@FunctionalInterface
public interface ReferencePathResolver {

    /** Confirms no path: qualified references are attributed to their terminal target only. */
    ReferencePathResolver NONE = (context, reference, qualifiedName, terminalTarget) -> List.of();

    /**
     * Returns the target of each segment, in qualified-name segment order. A leading root-namespace marker
     * ({@code $}) has no target and must not be counted. A list whose size does not match the segment count is
     * ignored.
     *
     * @param context
     *            the object carrying the reference
     * @param reference
     *            the resolved model reference
     * @param qualifiedName
     *            the reference text, as written by the parser or the serializer
     * @param terminalTarget
     *            the target installed in the model
     * @return the segment targets, or an empty list when the linker cannot confirm this spelling
     */
    List<EObject> resolve(EObject context, EReference reference,
            String qualifiedName, EObject terminalTarget);
}
