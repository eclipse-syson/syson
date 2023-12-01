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
 * A representation of the model object '<em><b>Control Node</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getControlNode()
 * @model abstract="true"
 * @generated
 */
public interface ControlNode extends ActionUsage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model required="true" ordered="false" multRequired="true" multOrdered="false" lowerRequired="true" lowerOrdered="false" upperRequired="true" upperOrdered="false"
     * @generated
     */
    boolean multiplicityHasBounds(Multiplicity mult, int lower, int upper);

} // ControlNode
