/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.diagram.general.view.services;

import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;

/**
 * Creation-related Java services used by the {@link GeneralViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class GeneralViewCreateService {

    /**
     * Create a {@link PartUsage} under the given {@link PartUsage}.
     *
     * @param partUsage
     *            the {@link PartUsage} on which we want to create a nested {@link PartUsage}.
     * @return the created element.
     */
    public PartUsage createNestedPartUsage(PartUsage partUsage) {
        PartUsage newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
        newPartUsage.setDeclaredName("part");
        FeatureMembership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        membership.getOwnedRelatedElement().add(newPartUsage);
        partUsage.getOwnedRelationship().add(membership);
        return newPartUsage;
    }
}
