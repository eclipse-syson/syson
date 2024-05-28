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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Type Featuring</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.TypeFeaturing#getFeatureOfType <em>Feature Of Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.TypeFeaturing#getFeaturingType <em>Featuring Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.TypeFeaturing#getOwningFeatureOfType <em>Owning Feature Of Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getTypeFeaturing()
 * @model
 * @generated
 */
public interface TypeFeaturing extends Featuring {
    /**
     * Returns the value of the '<em><b>Feature Of Type</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Feature Of Type</em>' reference.
     * @see #setFeatureOfType(Feature)
     * @see org.eclipse.syson.sysml.SysmlPackage#getTypeFeaturing_FeatureOfType()
     * @model required="true" ordered="false"
     * @generated
     */
    Feature getFeatureOfType();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.TypeFeaturing#getFeatureOfType <em>Feature Of Type</em>}'
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Feature Of Type</em>' reference.
     * @see #getFeatureOfType()
     * @generated
     */
    void setFeatureOfType(Feature value);

    /**
     * Returns the value of the '<em><b>Featuring Type</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Featuring Type</em>' reference.
     * @see #setFeaturingType(Type)
     * @see org.eclipse.syson.sysml.SysmlPackage#getTypeFeaturing_FeaturingType()
     * @model required="true" ordered="false"
     * @generated
     */
    Type getFeaturingType();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.TypeFeaturing#getFeaturingType <em>Featuring Type</em>}'
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Featuring Type</em>' reference.
     * @see #getFeaturingType()
     * @generated
     */
    void setFeaturingType(Type value);

    /**
     * Returns the value of the '<em><b>Owning Feature Of Type</b></em>' reference. It is bidirectional and its opposite
     * is '{@link org.eclipse.syson.sysml.Feature#getOwnedTypeFeaturing <em>Owned Type Featuring</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owning Feature Of Type</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getTypeFeaturing_OwningFeatureOfType()
     * @see org.eclipse.syson.sysml.Feature#getOwnedTypeFeaturing
     * @model opposite="ownedTypeFeaturing" transient="true" changeable="false" volatile="true" derived="true"
     *        ordered="false"
     * @generated
     */
    Feature getOwningFeatureOfType();

} // TypeFeaturing
