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
package org.eclipse.syson.services;

import java.util.List;
import java.util.Objects;

import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Type;

/**
 * A virtual element representing an ordered chain of features relative to a context.
 *
 * @param context
 *            the accessing type, excluded from the features
 * @param features
 *            the features in traversal order
 * @author Jerome Gout
 */
public record ContextualFeatureChain(Type context, List<Feature> features) {

    public ContextualFeatureChain {
        Objects.requireNonNull(context);
        features = List.copyOf(features);
        if (features.isEmpty()) {
            throw new IllegalArgumentException("A feature chain must contain at least one feature");
        }
    }

}
