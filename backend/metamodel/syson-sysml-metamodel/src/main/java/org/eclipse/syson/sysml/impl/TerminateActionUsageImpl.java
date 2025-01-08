/**
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
 */
package org.eclipse.syson.sysml.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TerminateActionUsage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Terminate Action Usage</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.TerminateActionUsageImpl#getTerminatedOccurrenceArgument <em>Terminated
 * Occurrence Argument</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TerminateActionUsageImpl extends ActionUsageImpl implements TerminateActionUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected TerminateActionUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getTerminateActionUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Expression getTerminatedOccurrenceArgument() {
        Expression terminatedOccurrenceArgument = this.basicGetTerminatedOccurrenceArgument();
        return terminatedOccurrenceArgument != null && terminatedOccurrenceArgument.eIsProxy() ? (Expression) this.eResolveProxy((InternalEObject) terminatedOccurrenceArgument)
                : terminatedOccurrenceArgument;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Expression basicGetTerminatedOccurrenceArgument() {
        // TODO: implement this method to return the 'Terminated Occurrence Argument' reference
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
            case SysmlPackage.TERMINATE_ACTION_USAGE__TERMINATED_OCCURRENCE_ARGUMENT:
                if (resolve)
                    return this.getTerminatedOccurrenceArgument();
                return this.basicGetTerminatedOccurrenceArgument();
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
            case SysmlPackage.TERMINATE_ACTION_USAGE__TERMINATED_OCCURRENCE_ARGUMENT:
                return this.basicGetTerminatedOccurrenceArgument() != null;
        }
        return super.eIsSet(featureID);
    }

} // TerminateActionUsageImpl
