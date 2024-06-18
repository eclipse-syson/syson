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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Subclassification</b></em>'. <!-- end-user-doc
 * -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Subclassification#getOwningClassifier <em>Owning Classifier</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Subclassification#getSubclassifier <em>Subclassifier</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Subclassification#getSuperclassifier <em>Superclassifier</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getSubclassification()
 * @model
 * @generated
 */
public interface Subclassification extends Specialization {
    /**
     * Returns the value of the '<em><b>Owning Classifier</b></em>' reference. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Classifier#getOwnedSubclassification <em>Owned Subclassification</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owning Classifier</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getSubclassification_OwningClassifier()
     * @see org.eclipse.syson.sysml.Classifier#getOwnedSubclassification
     * @model opposite="ownedSubclassification" transient="true" changeable="false" volatile="true" derived="true"
     *        ordered="false"
     * @generated
     */
    Classifier getOwningClassifier();

    /**
     * Returns the value of the '<em><b>Subclassifier</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Subclassifier</em>' reference.
     * @see #setSubclassifier(Classifier)
     * @see org.eclipse.syson.sysml.SysmlPackage#getSubclassification_Subclassifier()
     * @model required="true" ordered="false" annotation="redefines"
     * @generated
     */
    Classifier getSubclassifier();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Subclassification#getSubclassifier <em>Subclassifier</em>}'
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Subclassifier</em>' reference.
     * @see #getSubclassifier()
     * @generated
     */
    void setSubclassifier(Classifier value);

    /**
     * Returns the value of the '<em><b>Superclassifier</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Superclassifier</em>' reference.
     * @see #setSuperclassifier(Classifier)
     * @see org.eclipse.syson.sysml.SysmlPackage#getSubclassification_Superclassifier()
     * @model required="true" ordered="false"
     * @generated
     */
    Classifier getSuperclassifier();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Subclassification#getSuperclassifier
     * <em>Superclassifier</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Superclassifier</em>' reference.
     * @see #getSuperclassifier()
     * @generated
     */
    void setSuperclassifier(Classifier value);

} // Subclassification
