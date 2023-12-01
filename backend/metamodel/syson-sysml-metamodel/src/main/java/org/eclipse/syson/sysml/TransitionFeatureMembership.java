/**
 * Copyright (c) 2023 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.syson.sysml;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transition Feature Membership</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.TransitionFeatureMembership#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.TransitionFeatureMembership#getTransitionFeature <em>Transition Feature</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getTransitionFeatureMembership()
 * @model
 * @generated
 */
public interface TransitionFeatureMembership extends FeatureMembership {
    /**
     * Returns the value of the '<em><b>Kind</b></em>' attribute.
     * The literals are from the enumeration {@link org.eclipse.syson.sysml.TransitionFeatureKind}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Kind</em>' attribute.
     * @see org.eclipse.syson.sysml.TransitionFeatureKind
     * @see #setKind(TransitionFeatureKind)
     * @see org.eclipse.syson.sysml.SysmlPackage#getTransitionFeatureMembership_Kind()
     * @model required="true" ordered="false"
     * @generated
     */
    TransitionFeatureKind getKind();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.TransitionFeatureMembership#getKind <em>Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Kind</em>' attribute.
     * @see org.eclipse.syson.sysml.TransitionFeatureKind
     * @see #getKind()
     * @generated
     */
    void setKind(TransitionFeatureKind value);

    /**
     * Returns the value of the '<em><b>Transition Feature</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Transition Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getTransitionFeatureMembership_TransitionFeature()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Step getTransitionFeature();

} // TransitionFeatureMembership
