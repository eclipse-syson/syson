 /*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.CaseDefinition;
import org.eclipse.syson.sysml.CaseUsage;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Case Usage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.CaseUsageImpl#getActorParameter <em>Actor Parameter</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.CaseUsageImpl#getCaseDefinition <em>Case Definition</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.CaseUsageImpl#getObjectiveRequirement <em>Objective Requirement</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.CaseUsageImpl#getSubjectParameter <em>Subject Parameter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CaseUsageImpl extends CalculationUsageImpl implements CaseUsage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CaseUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getCaseUsage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<PartUsage> getActorParameter() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getCaseUsage_ActorParameter(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public CaseDefinition getCaseDefinition() {
        CaseDefinition caseDefinition = basicGetCaseDefinition();
        return caseDefinition != null && caseDefinition.eIsProxy() ? (CaseDefinition)eResolveProxy((InternalEObject)caseDefinition) : caseDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CaseDefinition basicGetCaseDefinition() {
        // TODO: implement this method to return the 'Case Definition' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public RequirementUsage getObjectiveRequirement() {
        RequirementUsage objectiveRequirement = basicGetObjectiveRequirement();
        return objectiveRequirement != null && objectiveRequirement.eIsProxy() ? (RequirementUsage)eResolveProxy((InternalEObject)objectiveRequirement) : objectiveRequirement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RequirementUsage basicGetObjectiveRequirement() {
        // TODO: implement this method to return the 'Objective Requirement' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
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
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.CASE_USAGE__ACTOR_PARAMETER:
                return getActorParameter();
            case SysmlPackage.CASE_USAGE__CASE_DEFINITION:
                if (resolve) return getCaseDefinition();
                return basicGetCaseDefinition();
            case SysmlPackage.CASE_USAGE__OBJECTIVE_REQUIREMENT:
                if (resolve) return getObjectiveRequirement();
                return basicGetObjectiveRequirement();
            case SysmlPackage.CASE_USAGE__SUBJECT_PARAMETER:
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
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.CASE_USAGE__ACTOR_PARAMETER:
                return !getActorParameter().isEmpty();
            case SysmlPackage.CASE_USAGE__CASE_DEFINITION:
                return basicGetCaseDefinition() != null;
            case SysmlPackage.CASE_USAGE__OBJECTIVE_REQUIREMENT:
                return basicGetObjectiveRequirement() != null;
            case SysmlPackage.CASE_USAGE__SUBJECT_PARAMETER:
                return basicGetSubjectParameter() != null;
        }
        return super.eIsSet(featureID);
    }

} //CaseUsageImpl
