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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multiplicity Range</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.MultiplicityRangeImpl#getBound <em>Bound</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.MultiplicityRangeImpl#getLowerBound <em>Lower Bound</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.MultiplicityRangeImpl#getUpperBound <em>Upper Bound</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MultiplicityRangeImpl extends MultiplicityImpl implements MultiplicityRange {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MultiplicityRangeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getMultiplicityRange();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Expression> getBound() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getMultiplicityRange_Bound(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getLowerBound() {
        Expression lowerBound = basicGetLowerBound();
        return lowerBound != null && lowerBound.eIsProxy() ? (Expression)eResolveProxy((InternalEObject)lowerBound) : lowerBound;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Expression basicGetLowerBound() {
        // TODO: implement this method to return the 'Lower Bound' reference
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
    public Expression getUpperBound() {
        Expression upperBound = basicGetUpperBound();
        return upperBound != null && upperBound.eIsProxy() ? (Expression)eResolveProxy((InternalEObject)upperBound) : upperBound;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Expression basicGetUpperBound() {
        // TODO: implement this method to return the 'Upper Bound' reference
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
    public boolean hasBounds(int lower, int upper) {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public int valueOf(Expression bound) {
        return 0;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.MULTIPLICITY_RANGE__BOUND:
                return getBound();
            case SysmlPackage.MULTIPLICITY_RANGE__LOWER_BOUND:
                if (resolve) return getLowerBound();
                return basicGetLowerBound();
            case SysmlPackage.MULTIPLICITY_RANGE__UPPER_BOUND:
                if (resolve) return getUpperBound();
                return basicGetUpperBound();
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
            case SysmlPackage.MULTIPLICITY_RANGE__BOUND:
                return !getBound().isEmpty();
            case SysmlPackage.MULTIPLICITY_RANGE__LOWER_BOUND:
                return basicGetLowerBound() != null;
            case SysmlPackage.MULTIPLICITY_RANGE__UPPER_BOUND:
                return basicGetUpperBound() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.MULTIPLICITY_RANGE___HAS_BOUNDS__INT_INT:
                return hasBounds((Integer)arguments.get(0), (Integer)arguments.get(1));
            case SysmlPackage.MULTIPLICITY_RANGE___VALUE_OF__EXPRESSION:
                return valueOf((Expression)arguments.get(0));
        }
        return super.eInvoke(operationID, arguments);
    }

} //MultiplicityRangeImpl
