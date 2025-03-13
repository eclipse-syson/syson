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

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.CaseDefinition;
import org.eclipse.syson.sysml.CaseUsage;
import org.eclipse.syson.sysml.Function;
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Case Usage</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.CaseUsageImpl#getActorParameter <em>Actor Parameter</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.CaseUsageImpl#getCaseDefinition <em>Case Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.CaseUsageImpl#getObjectiveRequirement <em>Objective Requirement</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.CaseUsageImpl#getSubjectParameter <em>Subject Parameter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CaseUsageImpl extends CalculationUsageImpl implements CaseUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected CaseUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getCaseUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<PartUsage> getActorParameter() {
        List<PartUsage> actorParameters = this.getParameter().stream()
                .filter(PartUsage.class::isInstance)
                .filter(parameter -> parameter.getOwningMembership() instanceof ActorMembership)
                .map(PartUsage.class::cast)
                .toList();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getCaseUsage_ActorParameter(), actorParameters.size(), actorParameters.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CaseDefinition getCaseDefinition() {
        CaseDefinition caseDefinition = this.basicGetCaseDefinition();
        return caseDefinition != null && caseDefinition.eIsProxy() ? (CaseDefinition) this.eResolveProxy((InternalEObject) caseDefinition) : caseDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public CaseDefinition basicGetCaseDefinition() {
        return this.getType().stream()
                .filter(CaseDefinition.class::isInstance)
                .map(CaseDefinition.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RequirementUsage getObjectiveRequirement() {
        RequirementUsage objectiveRequirement = this.basicGetObjectiveRequirement();
        return objectiveRequirement != null && objectiveRequirement.eIsProxy() ? (RequirementUsage) this.eResolveProxy((InternalEObject) objectiveRequirement) : objectiveRequirement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public RequirementUsage basicGetObjectiveRequirement() {
        return this.getOwnedRelationship().stream()
                .filter(ObjectiveMembership.class::isInstance)
                .map(ObjectiveMembership.class::cast)
                .findFirst()
                .map(om -> om.getOwnedObjectiveRequirement())
                .orElse(null);
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
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.CASE_USAGE__ACTOR_PARAMETER:
                return this.getActorParameter();
            case SysmlPackage.CASE_USAGE__CASE_DEFINITION:
                if (resolve) {
                    return this.getCaseDefinition();
                }
                return this.basicGetCaseDefinition();
            case SysmlPackage.CASE_USAGE__OBJECTIVE_REQUIREMENT:
                if (resolve) {
                    return this.getObjectiveRequirement();
                }
                return this.basicGetObjectiveRequirement();
            case SysmlPackage.CASE_USAGE__SUBJECT_PARAMETER:
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
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.CASE_USAGE__ACTOR_PARAMETER:
                return !this.getActorParameter().isEmpty();
            case SysmlPackage.CASE_USAGE__CASE_DEFINITION:
                return this.basicGetCaseDefinition() != null;
            case SysmlPackage.CASE_USAGE__OBJECTIVE_REQUIREMENT:
                return this.basicGetObjectiveRequirement() != null;
            case SysmlPackage.CASE_USAGE__SUBJECT_PARAMETER:
                return this.basicGetSubjectParameter() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Function getCalculationDefinition() {
        return this.getCaseDefinition();
    }

} // CaseUsageImpl
