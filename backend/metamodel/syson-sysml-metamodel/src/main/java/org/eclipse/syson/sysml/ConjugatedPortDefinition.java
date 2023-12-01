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
 * A representation of the model object '<em><b>Conjugated Port Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.ConjugatedPortDefinition#getOriginalPortDefinition <em>Original Port Definition</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.ConjugatedPortDefinition#getOwnedPortConjugator <em>Owned Port Conjugator</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getConjugatedPortDefinition()
 * @model
 * @generated
 */
public interface ConjugatedPortDefinition extends PortDefinition {
    /**
     * Returns the value of the '<em><b>Original Port Definition</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.PortDefinition#getConjugatedPortDefinition <em>Conjugated Port Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Original Port Definition</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getConjugatedPortDefinition_OriginalPortDefinition()
     * @see org.eclipse.syson.sysml.PortDefinition#getConjugatedPortDefinition
     * @model opposite="conjugatedPortDefinition" required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    PortDefinition getOriginalPortDefinition();

    /**
     * Returns the value of the '<em><b>Owned Port Conjugator</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.PortConjugation#getConjugatedPortDefinition <em>Conjugated Port Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owned Port Conjugator</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getConjugatedPortDefinition_OwnedPortConjugator()
     * @see org.eclipse.syson.sysml.PortConjugation#getConjugatedPortDefinition
     * @model opposite="conjugatedPortDefinition" required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    PortConjugation getOwnedPortConjugator();

} // ConjugatedPortDefinition
