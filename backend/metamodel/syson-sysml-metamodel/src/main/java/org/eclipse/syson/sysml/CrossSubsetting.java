/**
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
 */
package org.eclipse.syson.sysml;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Cross Subsetting</b></em>'. <!-- end-user-doc
 * -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.CrossSubsetting#getCrossedFeature <em>Crossed Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.CrossSubsetting#getCrossingFeature <em>Crossing Feature</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getCrossSubsetting()
 * @model
 * @generated
 */
public interface CrossSubsetting extends Subsetting {
    /**
     * Returns the value of the '<em><b>Crossed Feature</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Crossed Feature</em>' reference.
     * @see #setCrossedFeature(Feature)
     * @see org.eclipse.syson.sysml.SysmlPackage#getCrossSubsetting_CrossedFeature()
     * @model required="true" ordered="false" annotation="redefines"
     * @generated
     */
    Feature getCrossedFeature();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.CrossSubsetting#getCrossedFeature <em>Crossed
     * Feature</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Crossed Feature</em>' reference.
     * @see #getCrossedFeature()
     * @generated
     */
    void setCrossedFeature(Feature value);

    /**
     * Returns the value of the '<em><b>Crossing Feature</b></em>' reference. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Feature#getOwnedCrossSubsetting <em>Owned Cross Subsetting</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Crossing Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getCrossSubsetting_CrossingFeature()
     * @see org.eclipse.syson.sysml.Feature#getOwnedCrossSubsetting
     * @model opposite="ownedCrossSubsetting" required="true" transient="true" changeable="false" volatile="true"
     *        derived="true" ordered="false" annotation="redefines"
     * @generated
     */
    Feature getCrossingFeature();

} // CrossSubsetting
