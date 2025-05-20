/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.standard.diagrams.view.services;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.standard.diagrams.view.InterconnectionViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Creation-related Java services used by the {@link InterconnectionViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class InterconnectionViewCreateService {

    /**
     * Returns {@code true} if the diagram can be created on the provided {@code element}.
     *
     * @param element
     *            the element to check
     * @return {@code true} if the diagram can be created on the provided {@code element}
     */
    public boolean canCreateDiagram(Element element) {
        List<EClass> acceptedRootTypes = List.of(
                SysmlPackage.eINSTANCE.getUsage(),
                SysmlPackage.eINSTANCE.getDefinition());
        return acceptedRootTypes.stream().anyMatch(rootType -> rootType.isSuperTypeOf(element.eClass()));
    }
}
