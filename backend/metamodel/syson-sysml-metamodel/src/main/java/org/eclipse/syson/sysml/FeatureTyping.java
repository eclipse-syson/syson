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
 * A representation of the model object '<em><b>Feature Typing</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.FeatureTyping#getOwningFeature <em>Owning Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.FeatureTyping#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.FeatureTyping#getTypedFeature <em>Typed Feature</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureTyping()
 * @model
 * @generated
 */
public interface FeatureTyping extends Specialization {
    /**
     * Returns the value of the '<em><b>Owning Feature</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Feature#getOwnedTyping <em>Owned Typing</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owning Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureTyping_OwningFeature()
     * @see org.eclipse.syson.sysml.Feature#getOwnedTyping
     * @model opposite="ownedTyping" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Feature getOwningFeature();

    /**
     * Returns the value of the '<em><b>Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' reference.
     * @see #setType(Type)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureTyping_Type()
     * @model required="true" ordered="false"
     * @generated
     */
    Type getType();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.FeatureTyping#getType <em>Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' reference.
     * @see #getType()
     * @generated
     */
    void setType(Type value);

    /**
     * Returns the value of the '<em><b>Typed Feature</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Typed Feature</em>' reference.
     * @see #setTypedFeature(Feature)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureTyping_TypedFeature()
     * @model required="true" ordered="false"
     * @generated
     */
    Feature getTypedFeature();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.FeatureTyping#getTypedFeature <em>Typed Feature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Typed Feature</em>' reference.
     * @see #getTypedFeature()
     * @generated
     */
    void setTypedFeature(Feature value);

} // FeatureTyping
