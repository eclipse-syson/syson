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
 * A representation of the model object '<em><b>Feature Chaining</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.FeatureChaining#getChainingFeature <em>Chaining Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.FeatureChaining#getFeatureChained <em>Feature Chained</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureChaining()
 * @model
 * @generated
 */
public interface FeatureChaining extends Relationship {
    /**
     * Returns the value of the '<em><b>Chaining Feature</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Chaining Feature</em>' reference.
     * @see #setChainingFeature(Feature)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureChaining_ChainingFeature()
     * @model required="true" ordered="false"
     * @generated
     */
    Feature getChainingFeature();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.FeatureChaining#getChainingFeature <em>Chaining Feature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Chaining Feature</em>' reference.
     * @see #getChainingFeature()
     * @generated
     */
    void setChainingFeature(Feature value);

    /**
     * Returns the value of the '<em><b>Feature Chained</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Feature#getOwnedFeatureChaining <em>Owned Feature Chaining</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Chained</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureChaining_FeatureChained()
     * @see org.eclipse.syson.sysml.Feature#getOwnedFeatureChaining
     * @model opposite="ownedFeatureChaining" required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Feature getFeatureChained();

} // FeatureChaining
