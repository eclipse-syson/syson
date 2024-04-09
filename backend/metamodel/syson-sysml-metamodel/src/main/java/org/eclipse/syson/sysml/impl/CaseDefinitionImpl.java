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
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Case Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.CaseDefinitionImpl#getActorParameter <em>Actor Parameter</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.CaseDefinitionImpl#getObjectiveRequirement <em>Objective Requirement</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.CaseDefinitionImpl#getSubjectParameter <em>Subject Parameter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CaseDefinitionImpl extends CalculationDefinitionImpl implements CaseDefinition {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CaseDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getCaseDefinition();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<PartUsage> getActorParameter() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getCaseDefinition_ActorParameter(), data.size(), data.toArray());
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
     * @generated NOT
     */
    public RequirementUsage basicGetObjectiveRequirement() {
        return this.getOwnedRelationship().stream()
                .filter(ObjectiveMembership.class::isInstance)
                .map(ObjectiveMembership.class::cast)
                .map(om -> om.getOwnedObjectiveRequirement())
                .findFirst()
                .orElse(null);
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
     * @generated NOT
     */
    public Usage basicGetSubjectParameter() {
        return this.getOwnedRelationship().stream()
                .filter(SubjectMembership.class::isInstance)
                .map(SubjectMembership.class::cast)
                .map(sm -> sm.getOwnedSubjectParameter())
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.CASE_DEFINITION__ACTOR_PARAMETER:
                return getActorParameter();
            case SysmlPackage.CASE_DEFINITION__OBJECTIVE_REQUIREMENT:
                if (resolve) return getObjectiveRequirement();
                return basicGetObjectiveRequirement();
            case SysmlPackage.CASE_DEFINITION__SUBJECT_PARAMETER:
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
            case SysmlPackage.CASE_DEFINITION__ACTOR_PARAMETER:
                return !getActorParameter().isEmpty();
            case SysmlPackage.CASE_DEFINITION__OBJECTIVE_REQUIREMENT:
                return basicGetObjectiveRequirement() != null;
            case SysmlPackage.CASE_DEFINITION__SUBJECT_PARAMETER:
                return basicGetSubjectParameter() != null;
        }
        return super.eIsSet(featureID);
    }

} //CaseDefinitionImpl
