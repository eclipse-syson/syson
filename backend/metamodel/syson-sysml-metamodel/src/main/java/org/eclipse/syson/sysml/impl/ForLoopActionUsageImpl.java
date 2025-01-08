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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.ForLoopActionUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>For Loop Action Usage</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ForLoopActionUsageImpl#getLoopVariable <em>Loop Variable</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ForLoopActionUsageImpl#getSeqArgument <em>Seq Argument</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ForLoopActionUsageImpl extends LoopActionUsageImpl implements ForLoopActionUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ForLoopActionUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getForLoopActionUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ReferenceUsage getLoopVariable() {
        ReferenceUsage loopVariable = this.basicGetLoopVariable();
        return loopVariable != null && loopVariable.eIsProxy() ? (ReferenceUsage) this.eResolveProxy((InternalEObject) loopVariable) : loopVariable;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ReferenceUsage basicGetLoopVariable() {
        // TODO: implement this method to return the 'Loop Variable' reference
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
    public Expression getSeqArgument() {
        Expression seqArgument = this.basicGetSeqArgument();
        return seqArgument != null && seqArgument.eIsProxy() ? (Expression) this.eResolveProxy((InternalEObject) seqArgument) : seqArgument;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Expression basicGetSeqArgument() {
        // TODO: implement this method to return the 'Seq Argument' reference
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
            case SysmlPackage.FOR_LOOP_ACTION_USAGE__LOOP_VARIABLE:
                if (resolve) {
                    return this.getLoopVariable();
                }
                return this.basicGetLoopVariable();
            case SysmlPackage.FOR_LOOP_ACTION_USAGE__SEQ_ARGUMENT:
                if (resolve) {
                    return this.getSeqArgument();
                }
                return this.basicGetSeqArgument();
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
            case SysmlPackage.FOR_LOOP_ACTION_USAGE__LOOP_VARIABLE:
                return this.basicGetLoopVariable() != null;
            case SysmlPackage.FOR_LOOP_ACTION_USAGE__SEQ_ARGUMENT:
                return this.basicGetSeqArgument() != null;
        }
        return super.eIsSet(featureID);
    }

} // ForLoopActionUsageImpl
