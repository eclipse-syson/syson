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

import java.util.Optional;

import org.eclipse.emf.ecore.EObject;

/**
 * Lets the caller make references navigable in HTML output, for instance towards the element's page in an
 * application.
 * <p>
 * The returned value becomes the {@code href} of the reference anchor. It is attribute-escaped but otherwise inserted
 * as-is: the serializer neither validates nor sanitizes the URL scheme. Implementations must therefore only return
 * safe URLs (relative paths, fragments, {@code http}/{@code https}); never return {@code javascript:} or
 * {@code data:} URLs, or URLs built from untrusted text without validation.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@FunctionalInterface
public interface HtmlReferenceLinkResolver {

    /**
     * Returns the link to use for the given reference target. Called once per rendered reference occurrence, never
     * for targets owned by implied relationships.
     *
     * @param target
     *            the referenced model object, possibly an unresolved proxy
     * @return a safe URL inserted as-is into {@code href}, or an empty value (or blank string) to render the reference
     *         as a non-navigable span
     */
    Optional<String> resolve(EObject target);
}
