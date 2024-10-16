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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.AnalysisCaseDefinition;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Analysis Case Definition</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.AnalysisCaseDefinitionImpl#getResultExpression <em>Result
 * Expression</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AnalysisCaseDefinitionImpl extends CaseDefinitionImpl implements AnalysisCaseDefinition {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected AnalysisCaseDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getAnalysisCaseDefinition();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Expression getResultExpression() {
        Expression resultExpression = this.basicGetResultExpression();
        return resultExpression != null && resultExpression.eIsProxy() ? (Expression) this.eResolveProxy((InternalEObject) resultExpression) : resultExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Expression basicGetResultExpression() {
        // TODO: implement this method to return the 'Result Expression' reference
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
            case SysmlPackage.ANALYSIS_CASE_DEFINITION__RESULT_EXPRESSION:
                if (resolve)
                    return this.getResultExpression();
                return this.basicGetResultExpression();
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
            case SysmlPackage.ANALYSIS_CASE_DEFINITION__RESULT_EXPRESSION:
                return this.basicGetResultExpression() != null;
        }
        return super.eIsSet(featureID);
    }

} // AnalysisCaseDefinitionImpl
