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
package org.eclipse.syson.diagram.services;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Type;

/**
 * Shares immutable semantic query results within one diagram render. Instances must not survive that render.
 *
 * @author arichard
 */
public class DiagramQueryCache {

    public static final String VARIABLE_NAME = "sysonDiagramQueryCache";

    private final Map<Type, List<Feature>> inheritedFeatures = new IdentityHashMap<>();

    /**
     * Computes inherited features at most once for each type in this render.
     *
     * @param type
     *            the type whose inherited features are requested
     * @return an immutable snapshot of its inherited features
     */
    public List<Feature> getInheritedFeatures(Type type) {
        return this.inheritedFeatures.computeIfAbsent(type, key -> List.copyOf(key.getInheritedFeature()));
    }
}
