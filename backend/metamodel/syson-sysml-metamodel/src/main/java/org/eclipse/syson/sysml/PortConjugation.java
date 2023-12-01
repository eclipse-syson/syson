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
 * A representation of the model object '<em><b>Port Conjugation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.PortConjugation#getConjugatedPortDefinition <em>Conjugated Port Definition</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.PortConjugation#getOriginalPortDefinition <em>Original Port Definition</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getPortConjugation()
 * @model
 * @generated
 */
public interface PortConjugation extends Conjugation {
    /**
     * Returns the value of the '<em><b>Conjugated Port Definition</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.ConjugatedPortDefinition#getOwnedPortConjugator <em>Owned Port Conjugator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Conjugated Port Definition</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getPortConjugation_ConjugatedPortDefinition()
     * @see org.eclipse.syson.sysml.ConjugatedPortDefinition#getOwnedPortConjugator
     * @model opposite="ownedPortConjugator" required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ConjugatedPortDefinition getConjugatedPortDefinition();

    /**
     * Returns the value of the '<em><b>Original Port Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Original Port Definition</em>' reference.
     * @see #setOriginalPortDefinition(PortDefinition)
     * @see org.eclipse.syson.sysml.SysmlPackage#getPortConjugation_OriginalPortDefinition()
     * @model required="true" ordered="false"
     * @generated
     */
    PortDefinition getOriginalPortDefinition();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.PortConjugation#getOriginalPortDefinition <em>Original Port Definition</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Original Port Definition</em>' reference.
     * @see #getOriginalPortDefinition()
     * @generated
     */
    void setOriginalPortDefinition(PortDefinition value);

} // PortConjugation
