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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Behavior</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Behavior#getParameter <em>Parameter</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Behavior#getStep <em>Step</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getBehavior()
 * @model
 * @generated
 */
public interface Behavior extends org.eclipse.syson.sysml.Class {
    /**
     * Returns the value of the '<em><b>Parameter</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Feature}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Parameter</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getBehavior_Parameter()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="redefines"
     * @generated
     */
    EList<Feature> getParameter();

    /**
     * Returns the value of the '<em><b>Step</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Step}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Step</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getBehavior_Step()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    EList<Step> getStep();

} // Behavior
