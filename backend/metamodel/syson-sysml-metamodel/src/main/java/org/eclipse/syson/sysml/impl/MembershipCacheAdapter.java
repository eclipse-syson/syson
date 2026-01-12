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
package org.eclipse.syson.sysml.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;

/**
 * Adapter in charge of keeping a cache of the membership computations.
 *
 * @author Arthur Daussy
 */
public class MembershipCacheAdapter extends EContentAdapter {

    private final Map<Namespace, EList<Membership>> membershipCache = new HashMap<>();

    /**
     * Gets the caches computation of {@link Namespace#getMembership()} for the given {@link Namespace}.
     * @param namespace a {@link Namespace}
     * @return a filled {@link Optional} if the computation has been cached, {@link Optional#empty()} otherwise
     */
    public Optional<EList<Membership>> getMembership(Namespace namespace) {
        return Optional.ofNullable(this.membershipCache.get(namespace));
    }

    public void addToCache(Namespace namespace, EList<Membership> memberships) {
        this.membershipCache.put(namespace, memberships);
    }
}
