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
package org.eclipse.syson.diagram.common.view.services;

import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Provides the SysML nested-usage references used to configure diagram tools and edges.
 *
 * @author arichard
 */
public class UsageNestedUsageDescriptionService {

    /**
     * Returns every nested-usage reference declared by {@code Usage}, in a stable order.
     *
     * @return the nested-usage references
     */
    public List<EReference> getNestedUsageReferences() {
        return SysmlPackage.eINSTANCE.getUsage().getEAllReferences().stream()
                .filter(reference -> reference.getName().startsWith("nested"))
                .filter(reference -> reference.getEReferenceType() instanceof EClass eClass && SysmlPackage.eINSTANCE.getUsage().isSuperTypeOf(eClass))
                .sorted(Comparator.comparing(EReference::getName))
                .toList();
    }

    /**
     * Determines whether instances of the given SysML type can be added as a nested usage.
     *
     * @param usageType
     *            the SysML type to check
     * @return {@code true} if a nested-usage reference accepts the given type, {@code false} otherwise
     */
    public boolean canBeNested(EClass usageType) {
        return this.getNestedUsageReferences().stream()
                .map(EReference::getEReferenceType)
                .anyMatch(nestedUsageType -> nestedUsageType.isSuperTypeOf(usageType));
    }
}
