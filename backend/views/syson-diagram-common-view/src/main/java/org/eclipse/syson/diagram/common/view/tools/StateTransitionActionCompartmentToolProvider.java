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
package org.eclipse.syson.diagram.common.view.tools;

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;

/**
 * Node tool provider for creating nested/owned states in the "state transition" State compartment.
 *
 * @author adieumegard
 */
public class StateTransitionActionCompartmentToolProvider extends AbstractCompartmentNodeToolProvider {

    private final EStructuralFeature actionStructuralFeature;

    public StateTransitionActionCompartmentToolProvider(EStructuralFeature actionStructuralFeature) {
        super();
        this.actionStructuralFeature = actionStructuralFeature;
    }

    @Override
    protected String getServiceCallExpression() {
        return AQLUtils.getSelfServiceCallExpression("createOwnedAction",
                List.of(IEditingContext.EDITING_CONTEXT, IDiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE, ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE,
                        "'" + this.getActionKindValue() + "'"));
    }

    private String getActionKindValue() {
        String switchValue = "";
        if (this.isEntryAction()) {
            switchValue = StateSubactionKind.ENTRY.getLiteral();
        } else if (this.isDoAction()) {
            switchValue = StateSubactionKind.DO.getLiteral();
        } else if (this.isExitAction()) {
            switchValue = StateSubactionKind.EXIT.getLiteral();
        }
        if (switchValue.length() > 1) {
            switchValue = switchValue.substring(0, 1).toUpperCase() + switchValue.substring(1, switchValue.length());
        }
        return switchValue;
    }

    @Override
    protected String getNodeToolName() {
        return "New " + this.getActionKindValue() + " Action";
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/ActionUsage.svg";
    }

    @Override
    protected boolean revealOnCreate() {
        return true;
    }

    public boolean isHandledAction() {
        return this.isEntryAction() || this.isDoAction() || this.isExitAction();
    }

    @Override
    protected String getPreconditionExpression() {
        return AQLUtils.getSelfServiceCallExpression("isEmptyOfActionKindCompartment", "'" + this.getActionKindValue() + "'");
    }

    private boolean isEntryAction() {
        return SysmlPackage.eINSTANCE.getStateDefinition_EntryAction().equals(this.actionStructuralFeature) || SysmlPackage.eINSTANCE.getStateUsage_EntryAction().equals(this.actionStructuralFeature);
    }

    private boolean isDoAction() {
        return SysmlPackage.eINSTANCE.getStateDefinition_DoAction().equals(this.actionStructuralFeature) || SysmlPackage.eINSTANCE.getStateUsage_DoAction().equals(this.actionStructuralFeature);
    }

    private boolean isExitAction() {
        return SysmlPackage.eINSTANCE.getStateDefinition_ExitAction().equals(this.actionStructuralFeature) || SysmlPackage.eINSTANCE.getStateUsage_ExitAction().equals(this.actionStructuralFeature);
    }
}
