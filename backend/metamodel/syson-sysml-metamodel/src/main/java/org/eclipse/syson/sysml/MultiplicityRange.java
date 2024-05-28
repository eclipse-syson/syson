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
package org.eclipse.syson.sysml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Multiplicity Range</b></em>'. <!-- end-user-doc
 * -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.MultiplicityRange#getBound <em>Bound</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.MultiplicityRange#getLowerBound <em>Lower Bound</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.MultiplicityRange#getUpperBound <em>Upper Bound</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getMultiplicityRange()
 * @model
 * @generated
 */
public interface MultiplicityRange extends Multiplicity {
    /**
     * Returns the value of the '<em><b>Bound</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Expression}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Bound</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getMultiplicityRange_Bound()
     * @model required="true" upper="2" transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Expression> getBound();

    /**
     * Returns the value of the '<em><b>Lower Bound</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Lower Bound</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getMultiplicityRange_LowerBound()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Expression getLowerBound();

    /**
     * Returns the value of the '<em><b>Upper Bound</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Upper Bound</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getMultiplicityRange_UpperBound()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Expression getUpperBound();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" ordered="false" lowerRequired="true" lowerOrdered="false" upperRequired="true"
     *        upperOrdered="false"
     * @generated
     */
    boolean hasBounds(int lower, int upper);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" boundOrdered="false"
     * @generated
     */
    int valueOf(Expression bound);

} // MultiplicityRange
