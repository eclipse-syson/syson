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
import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Provides the SysML Definition owned-usage references used to configure diagram tools and edges.
 *
 * @author arichard
 */
public class DefinitionOwnedUsageDescriptionService {

    /**
     * Returns the most specific Definition owned-usage reference accepting the given Usage type.
     *
     * @param usageType
     *            the Usage type to own
     * @return the matching owned-usage reference, if any
     */
    public Optional<EReference> getOwnedUsageReference(EClass usageType) {
        return this.getOwnedUsageReferences().stream()
                .filter(reference -> reference.getEReferenceType().isSuperTypeOf(usageType))
                .max(Comparator.comparingInt(reference -> reference.getEReferenceType().getEAllSuperTypes().size()));
    }

    /**
     * Returns the Definition references that own usages.
     *
     * @return the Definition owned-usage references
     */
    private List<EReference> getOwnedUsageReferences() {
        return SysmlPackage.eINSTANCE.getDefinition().getEAllReferences().stream()
                .filter(reference -> reference.getName().startsWith("owned"))
                .filter(reference -> SysmlPackage.eINSTANCE.getUsage().isSuperTypeOf(reference.getEReferenceType()))
                .toList();
    }
}
