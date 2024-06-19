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
package org.eclipse.syson.diagram.common.view.tools;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.diagram.common.view.nodes.ActionFlowCompartmentNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to add an assignment action in actions body for all diagrams.
 *
 * @author Jerome Gout
 */
public class AssignmentActionNodeToolProvider extends AbstractFreeFormCompartmentNodeToolProvider {

    public AssignmentActionNodeToolProvider(EClass ownerEClass, IDescriptionNameGenerator descriptionNameGenerator) {
        super(ownerEClass, ActionFlowCompartmentNodeDescriptionProvider.COMPARTMENT_LABEL, descriptionNameGenerator);
    }

    @Override
    protected String getNodeDescriptionName() {
        return this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getAssignmentActionUsage());
    }

    @Override
    protected String getCreationServiceCallExpression() {
        return AQLUtils.getSelfServiceCallExpression("createAssignmentAction");
    }

    @Override
    protected String getLabel() {
        return this.getDescriptionNameGenerator().getCreationToolName(SysmlPackage.eINSTANCE.getAssignmentActionUsage());
    }

    @Override
    protected String getIconPath() {
        return this.getIconPathFromType(SysmlPackage.eINSTANCE.getAssignmentActionUsage());
    }
}
