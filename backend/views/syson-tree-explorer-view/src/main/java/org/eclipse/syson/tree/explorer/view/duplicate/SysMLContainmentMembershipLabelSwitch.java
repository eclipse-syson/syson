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
package org.eclipse.syson.tree.explorer.view.duplicate;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.ElementFilterMembership;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.FramedConcernMembership;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.RequirementVerificationMembership;
import org.eclipse.syson.sysml.ResultExpressionMembership;
import org.eclipse.syson.sysml.ReturnParameterMembership;
import org.eclipse.syson.sysml.StakeholderMembership;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.TransitionFeatureMembership;
import org.eclipse.syson.sysml.VariantMembership;
import org.eclipse.syson.sysml.ViewRenderingMembership;
import org.eclipse.syson.util.SysmlEClassSwitch;

/**
 * Returns a user-friendly label to be used to express the addition of an Element through the use of a {@link org.eclipse.syson.sysml.Membership}.
 *
 * @author Arthur Daussy
 */
public class SysMLContainmentMembershipLabelSwitch extends SysmlEClassSwitch<String> {

    @Override
    public String caseOwningMembership(OwningMembership object) {
        return "Add as an owning member";
    }

    @Override
    public String caseFeatureMembership(FeatureMembership object) {
        return "Add as a feature member";
    }

    @Override
    public String caseActorMembership(ActorMembership object) {
        return "Add as an actor member";
    }

    @Override
    public String caseObjectiveMembership(ObjectiveMembership object) {
        return "Add as an objective member";
    }

    @Override
    public String caseVariantMembership(VariantMembership object) {
        return "Add as a variant member";
    }

    @Override
    public String caseFeatureValue(FeatureValue object) {
        return "Add as a feature value";
    }

    @Override
    public String caseElementFilterMembership(ElementFilterMembership object) {
        return "Add as an element filter member";
    }

    @Override
    public String caseResultExpressionMembership(ResultExpressionMembership object) {
        return "Add as a result expression member";
    }

    @Override
    public String caseRequirementConstraintMembership(RequirementConstraintMembership object) {
        return "Add as a requirement constraint member";
    }

    @Override
    public String caseFramedConcernMembership(FramedConcernMembership object) {
        return "Add as a framed concern member";
    }

    @Override
    public String caseRequirementVerificationMembership(RequirementVerificationMembership object) {
        return "Add as a requirement verification member";
    }

    @Override
    public String caseStateSubactionMembership(StateSubactionMembership object) {
        return "Add as a state sub-action member";
    }

    @Override
    public String caseViewRenderingMembership(ViewRenderingMembership object) {
        return "Add as a view rendering member";
    }

    @Override
    public String caseTransitionFeatureMembership(TransitionFeatureMembership object) {
        return "Add as a transition feature member";
    }

    @Override
    public String caseParameterMembership(ParameterMembership object) {
        return "Add as a parameter member";
    }

    @Override
    public String caseStakeholderMembership(StakeholderMembership object) {
        return "Add as a stakeholder member";
    }

    @Override
    public String caseSubjectMembership(SubjectMembership object) {
        return "Add as a subject member";
    }

    @Override
    public String caseReturnParameterMembership(ReturnParameterMembership object) {
        return "Add as a return parameter member";
    }

    @Override
    public String caseEndFeatureMembership(EndFeatureMembership object) {
        return "Add as an end feature member";
    }

    @Override
    public String caseMembership(Membership object) {
        return "Add as a member";
    }

    @Override
    public String defaultCase(EObject object) {
        return null;
    }
}
