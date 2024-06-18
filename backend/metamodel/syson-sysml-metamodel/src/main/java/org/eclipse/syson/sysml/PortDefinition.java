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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Port Definition</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.PortDefinition#getConjugatedPortDefinition <em>Conjugated Port
 * Definition</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getPortDefinition()
 * @model
 * @generated
 */
public interface PortDefinition extends OccurrenceDefinition, Structure {
    /**
     * Returns the value of the '<em><b>Conjugated Port Definition</b></em>' reference. It is bidirectional and its
     * opposite is '{@link org.eclipse.syson.sysml.ConjugatedPortDefinition#getOriginalPortDefinition <em>Original Port
     * Definition</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Conjugated Port Definition</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getPortDefinition_ConjugatedPortDefinition()
     * @see org.eclipse.syson.sysml.ConjugatedPortDefinition#getOriginalPortDefinition
     * @model opposite="originalPortDefinition" transient="true" changeable="false" volatile="true" derived="true"
     *        ordered="false" annotation="subsets"
     * @generated
     */
    ConjugatedPortDefinition getConjugatedPortDefinition();

} // PortDefinition
