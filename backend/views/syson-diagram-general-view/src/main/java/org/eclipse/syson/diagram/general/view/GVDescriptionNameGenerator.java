/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.diagram.common.view.nodes.AbstractActionsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionActionCompartmentToolProvider;
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
        if (SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint().equals(eReference)
                || SysmlPackage.eINSTANCE.getRequirementDefinition_AssumedConstraint().equals(eReference)) {
            name = "New Assume constraint";
        } else if (SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint().equals(eReference)
                || SysmlPackage.eINSTANCE.getRequirementDefinition_RequiredConstraint().equals(eReference)) {
            name = "New Require constraint";
        }
        return name;
    }

    @Override
    public String getCompartmentName(EClass eClass, EReference eReference) {
        if (new StateTransitionActionCompartmentToolProvider(eReference).isHandledAction()) {
            return this.getCompartmentName(this.getDiagramPrefix(), eClass.getName() + SPACE + AbstractActionsCompartmentNodeDescriptionProvider.ACTIONS_COMPARTMENT_LABEL);
        } else {
            return super.getCompartmentName(eClass, eReference);
        }
    }

    @Override
    public String getCompartmentItemName(EClass eClass, EReference eReference) {
        if (new StateTransitionActionCompartmentToolProvider(eReference).isHandledAction()) {
            return this.getCompartmentItemName(this.getDiagramPrefix(), eClass.getName() + SPACE + AbstractActionsCompartmentNodeDescriptionProvider.ACTION);
        } else {
            return super.getCompartmentItemName(eClass, eReference);
        }
    }
}
