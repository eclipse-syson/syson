/**
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
 */
package org.eclipse.syson.sysml.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.ConcernUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Requirement Usage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.RequirementUsageImpl#getReqId <em>Req Id</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RequirementUsageImpl#getText <em>Text</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RequirementUsageImpl#getActorParameter <em>Actor Parameter</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RequirementUsageImpl#getAssumedConstraint <em>Assumed Constraint</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RequirementUsageImpl#getFramedConcern <em>Framed Concern</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RequirementUsageImpl#getRequiredConstraint <em>Required Constraint</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RequirementUsageImpl#getRequirementDefinition <em>Requirement Definition</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RequirementUsageImpl#getStakeholderParameter <em>Stakeholder Parameter</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RequirementUsageImpl#getSubjectParameter <em>Subject Parameter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RequirementUsageImpl extends ConstraintUsageImpl implements RequirementUsage {
    /**
     * The default value of the '{@link #getReqId() <em>Req Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReqId()
     * @generated
     * @ordered
     */
    protected static final String REQ_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getReqId() <em>Req Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReqId()
     * @generated
     * @ordered
     */
    protected String reqId = REQ_ID_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RequirementUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getRequirementUsage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<PartUsage> getActorParameter() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<ConstraintUsage> getAssumedConstraint() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<ConcernUsage> getFramedConcern() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRequirementUsage_FramedConcern(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getReqId() {
        return reqId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setReqId(String newReqId) {
        String oldReqId = reqId;
        reqId = newReqId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.REQUIREMENT_USAGE__REQ_ID, oldReqId, reqId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<ConstraintUsage> getRequiredConstraint() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public RequirementDefinition getRequirementDefinition() {
        RequirementDefinition requirementDefinition = basicGetRequirementDefinition();
        return requirementDefinition != null && requirementDefinition.eIsProxy() ? (RequirementDefinition)eResolveProxy((InternalEObject)requirementDefinition) : requirementDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RequirementDefinition basicGetRequirementDefinition() {
        // TODO: implement this method to return the 'Requirement Definition' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<PartUsage> getStakeholderParameter() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Usage getSubjectParameter() {
        Usage subjectParameter = basicGetSubjectParameter();
        return subjectParameter != null && subjectParameter.eIsProxy() ? (Usage)eResolveProxy((InternalEObject)subjectParameter) : subjectParameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Usage basicGetSubjectParameter() {
        // TODO: implement this method to return the 'Subject Parameter' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<String> getText() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRequirementUsage_Text(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.REQUIREMENT_USAGE__REQ_ID:
                return getReqId();
            case SysmlPackage.REQUIREMENT_USAGE__TEXT:
                return getText();
            case SysmlPackage.REQUIREMENT_USAGE__ACTOR_PARAMETER:
                return getActorParameter();
            case SysmlPackage.REQUIREMENT_USAGE__ASSUMED_CONSTRAINT:
                return getAssumedConstraint();
            case SysmlPackage.REQUIREMENT_USAGE__FRAMED_CONCERN:
                return getFramedConcern();
            case SysmlPackage.REQUIREMENT_USAGE__REQUIRED_CONSTRAINT:
                return getRequiredConstraint();
            case SysmlPackage.REQUIREMENT_USAGE__REQUIREMENT_DEFINITION:
                if (resolve) return getRequirementDefinition();
                return basicGetRequirementDefinition();
            case SysmlPackage.REQUIREMENT_USAGE__STAKEHOLDER_PARAMETER:
                return getStakeholderParameter();
            case SysmlPackage.REQUIREMENT_USAGE__SUBJECT_PARAMETER:
                if (resolve) return getSubjectParameter();
                return basicGetSubjectParameter();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.REQUIREMENT_USAGE__REQ_ID:
                setReqId((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SysmlPackage.REQUIREMENT_USAGE__REQ_ID:
                setReqId(REQ_ID_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.REQUIREMENT_USAGE__REQ_ID:
                return REQ_ID_EDEFAULT == null ? reqId != null : !REQ_ID_EDEFAULT.equals(reqId);
            case SysmlPackage.REQUIREMENT_USAGE__TEXT:
                return !getText().isEmpty();
            case SysmlPackage.REQUIREMENT_USAGE__ACTOR_PARAMETER:
                return !getActorParameter().isEmpty();
            case SysmlPackage.REQUIREMENT_USAGE__ASSUMED_CONSTRAINT:
                return !getAssumedConstraint().isEmpty();
            case SysmlPackage.REQUIREMENT_USAGE__FRAMED_CONCERN:
                return !getFramedConcern().isEmpty();
            case SysmlPackage.REQUIREMENT_USAGE__REQUIRED_CONSTRAINT:
                return !getRequiredConstraint().isEmpty();
            case SysmlPackage.REQUIREMENT_USAGE__REQUIREMENT_DEFINITION:
                return basicGetRequirementDefinition() != null;
            case SysmlPackage.REQUIREMENT_USAGE__STAKEHOLDER_PARAMETER:
                return !getStakeholderParameter().isEmpty();
            case SysmlPackage.REQUIREMENT_USAGE__SUBJECT_PARAMETER:
                return basicGetSubjectParameter() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (reqId: ");
        result.append(reqId);
        result.append(')');
        return result.toString();
    }

} //RequirementUsageImpl
