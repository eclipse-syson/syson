/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.diagram.general.view;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.DescriptionNameGenerator;

/**
 * Name generator for all General View description providers.
 *
 * @author arichard
 */
public class GVDescriptionNameGenerator extends DescriptionNameGenerator {

    public GVDescriptionNameGenerator() {
        super("GV");
    }

    @Override
    public String getCreationToolName(EReference eReference) {
        String name = super.getCreationToolName(eReference);
        if (SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint().equals(eReference)) {
            name = "New Assumed constraint";
        } else if (SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint().equals(eReference)) {
            name = "New Required constraint";
        }
        return name;
    }
}
