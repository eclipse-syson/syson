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
package org.eclipse.syson.diagram.common.view.services;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.DecisionNode;
import org.eclipse.syson.sysml.ForkNode;
import org.eclipse.syson.sysml.JoinNode;
import org.eclipse.syson.sysml.MergeNode;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Switch allowing to know in which cases a compartment should be revealed. It is used by
 * ViewNodeService#revealCompartment().
 *
 * @author arichard
 */
public class RevealCompartmentSwitch extends SysmlSwitch<Boolean> {

    /**
     * If the ViewUsage associated to the object on which this Switch is applied is typed by a GeneralView
     * ViewDefinition or not.
     */
    private final boolean isGeneralView;

    public RevealCompartmentSwitch(boolean isGeneralView) {
        this.isGeneralView = isGeneralView;
    }

    @Override
    public Boolean defaultCase(EObject object) {
        return Boolean.TRUE;
    }

    @Override
    public Boolean caseActionUsage(ActionUsage object) {
        return !this.isGeneralView;
    }

    @Override
    public Boolean caseConstraintUsage(ConstraintUsage object) {
        return !this.isGeneralView;
    }

    @Override
    public Boolean caseDecisionNode(DecisionNode object) {
        return Boolean.TRUE;
    }

    @Override
    public Boolean caseForkNode(ForkNode object) {
        return Boolean.TRUE;
    }

    @Override
    public Boolean caseJoinNode(JoinNode object) {
        return Boolean.TRUE;
    }

    @Override
    public Boolean caseMergeNode(MergeNode object) {
        return Boolean.TRUE;
    }

    @Override
    public Boolean caseOccurrenceDefinition(OccurrenceDefinition object) {
        return !this.isGeneralView;
    }

    @Override
    public Boolean caseOccurrenceUsage(OccurrenceUsage object) {
        return !this.isGeneralView;
    }

    @Override
    public Boolean caseStateUsage(StateUsage object) {
        return !this.isGeneralView;
    }
}
