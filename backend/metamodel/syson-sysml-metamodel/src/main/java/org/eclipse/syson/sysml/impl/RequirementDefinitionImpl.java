/*******************************************************************************
* Copyright (c) 2023, 2025 Obeo.
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
package org.eclipse.syson.sysml.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.ConcernUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.FramedConcernMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.StakeholderMembership;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Requirement Definition</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.RequirementDefinitionImpl#getReqId <em>Req Id</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.RequirementDefinitionImpl#getText <em>Text</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.RequirementDefinitionImpl#getActorParameter <em>Actor Parameter</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.RequirementDefinitionImpl#getAssumedConstraint <em>Assumed
 * Constraint</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.RequirementDefinitionImpl#getFramedConcern <em>Framed Concern</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.RequirementDefinitionImpl#getRequiredConstraint <em>Required
 * Constraint</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.RequirementDefinitionImpl#getStakeholderParameter <em>Stakeholder
 * Parameter</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.RequirementDefinitionImpl#getSubjectParameter <em>Subject
 * Parameter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RequirementDefinitionImpl extends ConstraintDefinitionImpl implements RequirementDefinition {
    /**
     * The default value of the '{@link #getReqId() <em>Req Id</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getReqId()
     * @generated
     * @ordered
     */
    protected static final String REQ_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getReqId() <em>Req Id</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getReqId()
     * @generated
     * @ordered
     */
    protected String reqId = REQ_ID_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected RequirementDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getRequirementDefinition();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<PartUsage> getActorParameter() {
        List<PartUsage> actorParameters = this.getFeatureMembership().stream()
                .filter(ActorMembership.class::isInstance)
                .map(ActorMembership.class::cast)
                .map(ActorMembership::getOwnedActorParameter)
                .toList();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRequirementDefinition_ActorParameter(), actorParameters.size(), actorParameters.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<ConstraintUsage> getAssumedConstraint() {
        List<ConstraintUsage> assumedConstraints = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(RequirementConstraintMembership.class::isInstance)
                .map(RequirementConstraintMembership.class::cast)
                .filter(rcm -> RequirementConstraintKind.ASSUMPTION.equals(rcm.getKind()))
                .flatMap(rcm -> rcm.getOwnedRelatedElement().stream())
                .filter(ConstraintUsage.class::isInstance)
                .map(ConstraintUsage.class::cast)
                .forEach(assumedConstraints::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRequirementDefinition_AssumedConstraint(), assumedConstraints.size(), assumedConstraints.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<ConcernUsage> getFramedConcern() {
        List<ConcernUsage> framedConcerns = new ArrayList<>();
        this.getFeatureMembership().stream()
                .filter(FramedConcernMembership.class::isInstance)
                .map(FramedConcernMembership.class::cast)
                .map(FramedConcernMembership::getOwnedConcern)
                .forEach(framedConcerns::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRequirementDefinition_FramedConcern(), framedConcerns.size(), framedConcerns.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getReqId() {
        return this.reqId;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setReqId(String newReqId) {
        String oldReqId = this.reqId;
        this.reqId = newReqId;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.REQUIREMENT_DEFINITION__REQ_ID, oldReqId, this.reqId));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<ConstraintUsage> getRequiredConstraint() {
        List<ConstraintUsage> requiredConstraints = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(RequirementConstraintMembership.class::isInstance)
                .map(RequirementConstraintMembership.class::cast)
                .filter(rcm -> RequirementConstraintKind.REQUIREMENT.equals(rcm.getKind()))
                .flatMap(rcm -> rcm.getOwnedRelatedElement().stream())
                .filter(ConstraintUsage.class::isInstance)
                .map(ConstraintUsage.class::cast)
                .forEach(requiredConstraints::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRequirementDefinition_RequiredConstraint(), requiredConstraints.size(), requiredConstraints.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<PartUsage> getStakeholderParameter() {
        final List<PartUsage> stakeholders = this.getParameter().stream()
                .filter(PartUsage.class::isInstance)
                .map(PartUsage.class::cast)
                .filter(parameter -> parameter.getOwningMembership() instanceof StakeholderMembership)
                .toList();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRequirementDefinition_StakeholderParameter(), stakeholders.size(), stakeholders.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Usage getSubjectParameter() {
        Usage subjectParameter = this.basicGetSubjectParameter();
        return subjectParameter != null && subjectParameter.eIsProxy() ? (Usage) this.eResolveProxy((InternalEObject) subjectParameter) : subjectParameter;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Usage basicGetSubjectParameter() {
        return this.getOwnedRelationship().stream()
                .filter(SubjectMembership.class::isInstance)
                .map(SubjectMembership.class::cast)
                .findFirst()
                .map(sm -> sm.getOwnedSubjectParameter())
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<String> getText() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRequirementDefinition_Text(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.REQUIREMENT_DEFINITION__REQ_ID:
                return this.getReqId();
            case SysmlPackage.REQUIREMENT_DEFINITION__TEXT:
                return this.getText();
            case SysmlPackage.REQUIREMENT_DEFINITION__ACTOR_PARAMETER:
                return this.getActorParameter();
            case SysmlPackage.REQUIREMENT_DEFINITION__ASSUMED_CONSTRAINT:
                return this.getAssumedConstraint();
            case SysmlPackage.REQUIREMENT_DEFINITION__FRAMED_CONCERN:
                return this.getFramedConcern();
            case SysmlPackage.REQUIREMENT_DEFINITION__REQUIRED_CONSTRAINT:
                return this.getRequiredConstraint();
            case SysmlPackage.REQUIREMENT_DEFINITION__STAKEHOLDER_PARAMETER:
                return this.getStakeholderParameter();
            case SysmlPackage.REQUIREMENT_DEFINITION__SUBJECT_PARAMETER:
                if (resolve) {
                    return this.getSubjectParameter();
                }
                return this.basicGetSubjectParameter();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.REQUIREMENT_DEFINITION__REQ_ID:
                this.setReqId((String) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SysmlPackage.REQUIREMENT_DEFINITION__REQ_ID:
                this.setReqId(REQ_ID_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.REQUIREMENT_DEFINITION__REQ_ID:
                return REQ_ID_EDEFAULT == null ? this.reqId != null : !REQ_ID_EDEFAULT.equals(this.reqId);
            case SysmlPackage.REQUIREMENT_DEFINITION__TEXT:
                return !this.getText().isEmpty();
            case SysmlPackage.REQUIREMENT_DEFINITION__ACTOR_PARAMETER:
                return !this.getActorParameter().isEmpty();
            case SysmlPackage.REQUIREMENT_DEFINITION__ASSUMED_CONSTRAINT:
                return !this.getAssumedConstraint().isEmpty();
            case SysmlPackage.REQUIREMENT_DEFINITION__FRAMED_CONCERN:
                return !this.getFramedConcern().isEmpty();
            case SysmlPackage.REQUIREMENT_DEFINITION__REQUIRED_CONSTRAINT:
                return !this.getRequiredConstraint().isEmpty();
            case SysmlPackage.REQUIREMENT_DEFINITION__STAKEHOLDER_PARAMETER:
                return !this.getStakeholderParameter().isEmpty();
            case SysmlPackage.REQUIREMENT_DEFINITION__SUBJECT_PARAMETER:
                return this.basicGetSubjectParameter() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (this.eIsProxy()) {
            return super.toString();
        }

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (reqId: ");
        result.append(this.reqId);
        result.append(')');
        return result.toString();
    }

} // RequirementDefinitionImpl
