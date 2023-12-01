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
 * A representation of the model object '<em><b>Conjugated Port Typing</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.ConjugatedPortTyping#getConjugatedPortDefinition <em>Conjugated Port Definition</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.ConjugatedPortTyping#getPortDefinition <em>Port Definition</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getConjugatedPortTyping()
 * @model
 * @generated
 */
public interface ConjugatedPortTyping extends FeatureTyping {
    /**
     * Returns the value of the '<em><b>Conjugated Port Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Conjugated Port Definition</em>' reference.
     * @see #setConjugatedPortDefinition(ConjugatedPortDefinition)
     * @see org.eclipse.syson.sysml.SysmlPackage#getConjugatedPortTyping_ConjugatedPortDefinition()
     * @model required="true" ordered="false"
     * @generated
     */
    ConjugatedPortDefinition getConjugatedPortDefinition();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.ConjugatedPortTyping#getConjugatedPortDefinition <em>Conjugated Port Definition</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Conjugated Port Definition</em>' reference.
     * @see #getConjugatedPortDefinition()
     * @generated
     */
    void setConjugatedPortDefinition(ConjugatedPortDefinition value);

    /**
     * Returns the value of the '<em><b>Port Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Port Definition</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getConjugatedPortTyping_PortDefinition()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    PortDefinition getPortDefinition();

} // ConjugatedPortTyping
