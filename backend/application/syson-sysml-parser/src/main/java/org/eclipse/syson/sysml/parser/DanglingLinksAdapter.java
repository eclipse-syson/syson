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
package org.eclipse.syson.sysml.parser;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.notify.impl.AdapterImpl;

/**
 * Keeps the parser dangling links associated with a loaded EMF resource.
 *
 * <p>
 * The links are intentionally kept in memory: they reference the parsed model objects and preserve the source names
 * after the linker has installed resolved targets. They are not a replacement for persisted model data.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class DanglingLinksAdapter extends AdapterImpl {

    private final Set<DanglingLink> danglingLinks;

    /**
     * Retains an unmodifiable snapshot of the supplied links in their iteration order. Later changes to the set do
     * not affect the snapshot; the links and their referenced model objects remain shared.
     *
     * @param danglingLinks
     *            the parser links whose source spellings must remain available while the resource is loaded
     * @throws NullPointerException
     *             if {@code danglingLinks} is {@code null}
     */
    public DanglingLinksAdapter(final Set<DanglingLink> danglingLinks) {
        this.danglingLinks = Collections.unmodifiableSet(
                new LinkedHashSet<>(Objects.requireNonNull(danglingLinks)));
    }

    /**
     * Returns an unmodifiable snapshot in the input set's iteration order. The links and their model objects remain shared.
     */
    public Set<DanglingLink> danglingLinks() {
        return this.danglingLinks;
    }

    @Override
    public boolean isAdapterForType(final Object type) {
        return type == DanglingLinksAdapter.class;
    }
}
