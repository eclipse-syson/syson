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
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Use Case Usage</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.UseCaseUsageImpl#getIncludedUseCase <em>Included Use Case</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.UseCaseUsageImpl#getUseCaseDefinition <em>Use Case Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class UseCaseUsageImpl extends CaseUsageImpl implements UseCaseUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected UseCaseUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getUseCaseUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<UseCaseUsage> getIncludedUseCase() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getUseCaseUsage_IncludedUseCase(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public UseCaseDefinition getUseCaseDefinition() {
        UseCaseDefinition useCaseDefinition = this.basicGetUseCaseDefinition();
        return useCaseDefinition != null && useCaseDefinition.eIsProxy() ? (UseCaseDefinition) this.eResolveProxy((InternalEObject) useCaseDefinition) : useCaseDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public UseCaseDefinition basicGetUseCaseDefinition() {
        // TODO: implement this method to return the 'Use Case Definition' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.USE_CASE_USAGE__INCLUDED_USE_CASE:
                return this.getIncludedUseCase();
            case SysmlPackage.USE_CASE_USAGE__USE_CASE_DEFINITION:
                if (resolve) {
                    return this.getUseCaseDefinition();
                }
                return this.basicGetUseCaseDefinition();
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
            case SysmlPackage.USE_CASE_USAGE__INCLUDED_USE_CASE:
                return !this.getIncludedUseCase().isEmpty();
            case SysmlPackage.USE_CASE_USAGE__USE_CASE_DEFINITION:
                return this.basicGetUseCaseDefinition() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public CaseDefinition getCaseDefinition() {
        return this.getUseCaseDefinition();
    }

} // UseCaseUsageImpl
