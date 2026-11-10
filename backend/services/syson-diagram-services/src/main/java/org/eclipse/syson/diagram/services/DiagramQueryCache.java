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

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.api.ViewDefinitionKind;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.ViewUsage;

/**
 * Shares immutable semantic query results within one diagram render. Instances must not survive that render.
 *
 * @author arichard
 */
public class DiagramQueryCache {

    /**
     * Render cache variable; evaluations outside rendering bind it to null to select the uncached query path.
     */
    public static final String VARIABLE_NAME = "sysonDiagramQueryCache";

    private final Map<Type, List<Feature>> inheritedFeatures = new IdentityHashMap<>();

    private final Map<ViewUsage, ViewDefinitionKind> viewKinds = new IdentityHashMap<>();

    private final UtilService utilService = new UtilService();

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

    /**
     * Resolves each view's kind once in this render, including an unsupported type's null result.
     *
     * @param viewUsage
     *            the view whose kind is requested
     * @param editingContext
     *            the editing context used by the existing resolution service
     * @return the resolved kind, the general kind for an untyped view, or null for an unsupported type
     */
    public ViewDefinitionKind getViewDefinitionKind(ViewUsage viewUsage, IEditingContext editingContext) {
        if (!this.viewKinds.containsKey(viewUsage)) {
            this.viewKinds.put(viewUsage, this.utilService.getViewDefinitionKind(viewUsage, List.of(), editingContext));
        }
        return this.viewKinds.get(viewUsage);
    }
}
