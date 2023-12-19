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
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.FeatureValue#isIsDefault <em>Is Default</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.FeatureValue#isIsInitial <em>Is Initial</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.FeatureValue#getFeatureWithValue <em>Feature With Value</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.FeatureValue#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureValue()
 * @model
 * @generated
 */
public interface FeatureValue extends OwningMembership {
    /**
     * Returns the value of the '<em><b>Feature With Value</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature With Value</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureValue_FeatureWithValue()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Feature getFeatureWithValue();

    /**
     * Returns the value of the '<em><b>Is Default</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Default</em>' attribute.
     * @see #setIsDefault(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureValue_IsDefault()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsDefault();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.FeatureValue#isIsDefault <em>Is Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Default</em>' attribute.
     * @see #isIsDefault()
     * @generated
     */
    void setIsDefault(boolean value);

    /**
     * Returns the value of the '<em><b>Is Initial</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Initial</em>' attribute.
     * @see #setIsInitial(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureValue_IsInitial()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsInitial();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.FeatureValue#isIsInitial <em>Is Initial</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Initial</em>' attribute.
     * @see #isIsInitial()
     * @generated
     */
    void setIsInitial(boolean value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureValue_Value()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Expression getValue();

} // FeatureValue
