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

import java.util.Objects;
import java.util.function.BiPredicate;

import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.util.ElementUtil;

/**
 * Accepts features outside standard SysML and KerML library resources, regardless of their inheritance path.
 * Use this predicate as the traversal filter of {@link MetamodelQueryElementService#streamFeatureChains} to prune
 * standard library branches. Model features typed by standard library types remain eligible, as do features
 * inherited implicitly from model or business library resources. Detached features are accepted.
 *
 * @author Arthur Daussy
 */
public class NonStandardLibraryFeaturePredicate implements BiPredicate<Type, Feature> {

    /**
     * Tests the candidate's resource origin without inspecting its type or specializations.
     *
     * @param context
     *            the accessing context, ignored by this predicate
     * @param feature
     *            the non-null candidate feature
     * @return false if the feature belongs to a standard library resource, true otherwise
     * @throws NullPointerException
     *            if the feature is null
     */
    @Override
    public boolean test(Type context, Feature feature) {
        return !ElementUtil.isStandardLibraryResource(Objects.requireNonNull(feature).eResource());
    }
}
