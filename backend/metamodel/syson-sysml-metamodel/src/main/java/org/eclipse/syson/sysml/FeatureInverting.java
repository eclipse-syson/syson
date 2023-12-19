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
 * A representation of the model object '<em><b>Feature Inverting</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.FeatureInverting#getFeatureInverted <em>Feature Inverted</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.FeatureInverting#getInvertingFeature <em>Inverting Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.FeatureInverting#getOwningFeature <em>Owning Feature</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureInverting()
 * @model
 * @generated
 */
public interface FeatureInverting extends Relationship {
    /**
     * Returns the value of the '<em><b>Feature Inverted</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Inverted</em>' reference.
     * @see #setFeatureInverted(Feature)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureInverting_FeatureInverted()
     * @model required="true" ordered="false"
     * @generated
     */
    Feature getFeatureInverted();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.FeatureInverting#getFeatureInverted <em>Feature Inverted</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Inverted</em>' reference.
     * @see #getFeatureInverted()
     * @generated
     */
    void setFeatureInverted(Feature value);

    /**
     * Returns the value of the '<em><b>Inverting Feature</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Inverting Feature</em>' reference.
     * @see #setInvertingFeature(Feature)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureInverting_InvertingFeature()
     * @model required="true" ordered="false"
     * @generated
     */
    Feature getInvertingFeature();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.FeatureInverting#getInvertingFeature <em>Inverting Feature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Inverting Feature</em>' reference.
     * @see #getInvertingFeature()
     * @generated
     */
    void setInvertingFeature(Feature value);

    /**
     * Returns the value of the '<em><b>Owning Feature</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Feature#getOwnedFeatureInverting <em>Owned Feature Inverting</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owning Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureInverting_OwningFeature()
     * @see org.eclipse.syson.sysml.Feature#getOwnedFeatureInverting
     * @model opposite="ownedFeatureInverting" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Feature getOwningFeature();

} // FeatureInverting
