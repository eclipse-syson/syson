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
package org.eclipse.syson.sysml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Transition Usage</b></em>'. <!-- end-user-doc
 * -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.TransitionUsage#getEffectAction <em>Effect Action</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.TransitionUsage#getGuardExpression <em>Guard Expression</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.TransitionUsage#getSource <em>Source</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.TransitionUsage#getSuccession <em>Succession</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.TransitionUsage#getTarget <em>Target</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.TransitionUsage#getTriggerAction <em>Trigger Action</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getTransitionUsage()
 * @model
 * @generated
 */
public interface TransitionUsage extends ActionUsage {
    /**
     * Returns the value of the '<em><b>Effect Action</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ActionUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Effect Action</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getTransitionUsage_EffectAction()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    EList<ActionUsage> getEffectAction();

    /**
     * Returns the value of the '<em><b>Guard Expression</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Expression}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Guard Expression</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getTransitionUsage_GuardExpression()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    EList<Expression> getGuardExpression();

    /**
     * Returns the value of the '<em><b>Source</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Source</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getTransitionUsage_Source()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ActionUsage getSource();

    /**
     * Returns the value of the '<em><b>Succession</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Succession</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getTransitionUsage_Succession()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     *        annotation="subsets"
     * @generated
     */
    Succession getSuccession();

    /**
     * Returns the value of the '<em><b>Target</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Target</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getTransitionUsage_Target()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ActionUsage getTarget();

    /**
     * Returns the value of the '<em><b>Trigger Action</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.AcceptActionUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Trigger Action</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getTransitionUsage_TriggerAction()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    EList<AcceptActionUsage> getTriggerAction();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false"
     * @generated
     */
    Feature sourceFeature();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false"
     * @generated
     */
    ReferenceUsage triggerPayloadParameter();

} // TransitionUsage
