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

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Type;

/**
 * Accepts an accessible feature chain if at least one segment still needs a local redefinition from its root context.
 * A fully local chain is excluded: its first segment is owned by the context and every subsequent segment by its
 * predecessor. Local declarations count as available even without a Redefinition relationship. Empty chains are excluded.
 * <p>
 * Use this predicate as the chain selection filter of {@link MetamodelQueryElementService#streamFeatureChains}:
 * a local parent may have inherited descendants that still need redefinition. Selection occurs before the result limit.
 * This predicate does not validate
 * chain accessibility or configurability and runs in at most linear time in the chain length, without caching.
 * </p>
 *
 * @author Arthur Daussy
 */
public class RequiresRedefinitionFeatureChainPredicate implements BiPredicate<Type, List<Feature>> {

    /**
     * Checks ownership at every segment, starting from the root context.
     *
     * @param context
     *            the non-null root accessing context, excluded from the chain
     * @param chain
     *            a non-null accessible chain containing non-null features in path order
     * @return true if a segment is inherited rather than owned by the preceding context
     * @throws NullPointerException
     *            if the context or chain is null
     */
    @Override
    public boolean test(Type context, List<Feature> chain) {
        Type expectedOwner = Objects.requireNonNull(context);
        Objects.requireNonNull(chain);
        for (Feature feature : chain) {
            if (feature.getOwningType() != expectedOwner) {
                return true;
            }
            expectedOwner = feature;
        }
        return false;
    }
}
