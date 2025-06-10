/*******************************************************************************
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
*******************************************************************************/
package org.eclipse.syson.sysml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Viewpoint Usage</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.ViewpointUsage#getViewpointDefinition <em>Viewpoint Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.ViewpointUsage#getViewpointStakeholder <em>Viewpoint Stakeholder</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getViewpointUsage()
 * @model
 * @generated
 */
public interface ViewpointUsage extends RequirementUsage {
    /**
     * Returns the value of the '<em><b>Viewpoint Definition</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Viewpoint Definition</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getViewpointUsage_ViewpointDefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="redefines"
     * @generated
     */
    ViewpointDefinition getViewpointDefinition();

    /**
     * Returns the value of the '<em><b>Viewpoint Stakeholder</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.PartUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Viewpoint Stakeholder</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getViewpointUsage_ViewpointStakeholder()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<PartUsage> getViewpointStakeholder();

} // ViewpointUsage
