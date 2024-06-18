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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Action Usage</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.ActionUsage#getActionDefinition <em>Action Definition</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getActionUsage()
 * @model
 * @generated
 */
public interface ActionUsage extends OccurrenceUsage, Step {
    /**
     * Returns the value of the '<em><b>Action Definition</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Behavior}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Action Definition</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getActionUsage_ActionDefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="redefines"
     * @generated
     */
    EList<Behavior> getActionDefinition();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" iRequired="true" iOrdered="false"
     * @generated
     */
    Expression argument(int i);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" iRequired="true" iOrdered="false"
     * @generated
     */
    Feature inputParameter(int i);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false"
     * @generated
     */
    EList<Feature> inputParameters();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model kind="operation" required="true" ordered="false"
     * @generated
     */
    boolean isSubactionUsage();

} // ActionUsage
