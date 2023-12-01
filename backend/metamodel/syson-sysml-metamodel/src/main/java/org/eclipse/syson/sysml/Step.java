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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Step</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.Step#getBehavior <em>Behavior</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Step#getParameter <em>Parameter</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getStep()
 * @model
 * @generated
 */
public interface Step extends Feature {
    /**
     * Returns the value of the '<em><b>Behavior</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Behavior}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Behavior</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getStep_Behavior()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Behavior> getBehavior();

    /**
     * Returns the value of the '<em><b>Parameter</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Feature}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getStep_Parameter()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Feature> getParameter();

} // Step
