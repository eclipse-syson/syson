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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>View Rendering Membership</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.ViewRenderingMembership#getOwnedRendering <em>Owned Rendering</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.ViewRenderingMembership#getReferencedRendering <em>Referenced Rendering</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getViewRenderingMembership()
 * @model
 * @generated
 */
public interface ViewRenderingMembership extends FeatureMembership {
    /**
     * Returns the value of the '<em><b>Owned Rendering</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Owned Rendering</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getViewRenderingMembership_OwnedRendering()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    RenderingUsage getOwnedRendering();

    /**
     * Returns the value of the '<em><b>Referenced Rendering</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Referenced Rendering</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getViewRenderingMembership_ReferencedRendering()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    RenderingUsage getReferencedRendering();

} // ViewRenderingMembership
