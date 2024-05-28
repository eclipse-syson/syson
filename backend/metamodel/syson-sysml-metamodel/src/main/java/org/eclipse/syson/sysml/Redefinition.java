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

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Redefinition</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Redefinition#getRedefinedFeature <em>Redefined Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Redefinition#getRedefiningFeature <em>Redefining Feature</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getRedefinition()
 * @model
 * @generated
 */
public interface Redefinition extends Subsetting {
    /**
     * Returns the value of the '<em><b>Redefined Feature</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Redefined Feature</em>' reference.
     * @see #setRedefinedFeature(Feature)
     * @see org.eclipse.syson.sysml.SysmlPackage#getRedefinition_RedefinedFeature()
     * @model required="true" ordered="false"
     * @generated
     */
    Feature getRedefinedFeature();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Redefinition#getRedefinedFeature <em>Redefined
     * Feature</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Redefined Feature</em>' reference.
     * @see #getRedefinedFeature()
     * @generated
     */
    void setRedefinedFeature(Feature value);

    /**
     * Returns the value of the '<em><b>Redefining Feature</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Redefining Feature</em>' reference.
     * @see #setRedefiningFeature(Feature)
     * @see org.eclipse.syson.sysml.SysmlPackage#getRedefinition_RedefiningFeature()
     * @model required="true" ordered="false"
     * @generated
     */
    Feature getRedefiningFeature();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Redefinition#getRedefiningFeature <em>Redefining
     * Feature</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Redefining Feature</em>' reference.
     * @see #getRedefiningFeature()
     * @generated
     */
    void setRedefiningFeature(Feature value);

} // Redefinition
