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
 * A representation of the model object '<em><b>Perform Action Usage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.PerformActionUsage#getPerformedAction <em>Performed Action</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getPerformActionUsage()
 * @model
 * @generated
 */
public interface PerformActionUsage extends ActionUsage, EventOccurrenceUsage {
    /**
     * Returns the value of the '<em><b>Performed Action</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Performed Action</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getPerformActionUsage_PerformedAction()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ActionUsage getPerformedAction();

} // PerformActionUsage
