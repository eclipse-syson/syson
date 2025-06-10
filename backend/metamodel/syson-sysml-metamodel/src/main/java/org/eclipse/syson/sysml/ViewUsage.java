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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>View Usage</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.ViewUsage#getExposedElement <em>Exposed Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.ViewUsage#getSatisfiedViewpoint <em>Satisfied Viewpoint</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.ViewUsage#getViewCondition <em>View Condition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.ViewUsage#getViewDefinition <em>View Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.ViewUsage#getViewRendering <em>View Rendering</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getViewUsage()
 * @model
 * @generated
 */
public interface ViewUsage extends PartUsage {
    /**
     * Returns the value of the '<em><b>Exposed Element</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Element}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Exposed Element</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getViewUsage_ExposedElement()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<Element> getExposedElement();

    /**
     * Returns the value of the '<em><b>Satisfied Viewpoint</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ViewpointUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Satisfied Viewpoint</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getViewUsage_SatisfiedViewpoint()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<ViewpointUsage> getSatisfiedViewpoint();

    /**
     * Returns the value of the '<em><b>View Condition</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Expression}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>View Condition</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getViewUsage_ViewCondition()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<Expression> getViewCondition();

    /**
     * Returns the value of the '<em><b>View Definition</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>View Definition</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getViewUsage_ViewDefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ViewDefinition getViewDefinition();

    /**
     * Returns the value of the '<em><b>View Rendering</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>View Rendering</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getViewUsage_ViewRendering()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    RenderingUsage getViewRendering();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" ordered="false" elementRequired="true" elementOrdered="false"
     * @generated
     */
    boolean includeAsExposed(Element element);

} // ViewUsage
