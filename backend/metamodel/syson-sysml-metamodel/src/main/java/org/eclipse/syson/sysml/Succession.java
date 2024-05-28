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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Succession</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Succession#getEffectStep <em>Effect Step</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Succession#getGuardExpression <em>Guard Expression</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Succession#getTransitionStep <em>Transition Step</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Succession#getTriggerStep <em>Trigger Step</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getSuccession()
 * @model
 * @generated
 */
public interface Succession extends Connector {
    /**
     * Returns the value of the '<em><b>Effect Step</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Step}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Effect Step</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getSuccession_EffectStep()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    EList<Step> getEffectStep();

    /**
     * Returns the value of the '<em><b>Guard Expression</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Expression}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Guard Expression</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getSuccession_GuardExpression()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    EList<Expression> getGuardExpression();

    /**
     * Returns the value of the '<em><b>Transition Step</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Transition Step</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getSuccession_TransitionStep()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Step getTransitionStep();

    /**
     * Returns the value of the '<em><b>Trigger Step</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Step}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Trigger Step</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getSuccession_TriggerStep()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    EList<Step> getTriggerStep();

} // Succession
