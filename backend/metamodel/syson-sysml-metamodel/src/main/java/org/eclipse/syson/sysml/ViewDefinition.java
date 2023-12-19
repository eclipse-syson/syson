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
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>View Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.ViewDefinition#getSatisfiedViewpoint <em>Satisfied Viewpoint</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.ViewDefinition#getView <em>View</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.ViewDefinition#getViewCondition <em>View Condition</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.ViewDefinition#getViewRendering <em>View Rendering</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getViewDefinition()
 * @model
 * @generated
 */
public interface ViewDefinition extends PartDefinition {
    /**
     * Returns the value of the '<em><b>Satisfied Viewpoint</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.ViewpointUsage}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Satisfied Viewpoint</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getViewDefinition_SatisfiedViewpoint()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<ViewpointUsage> getSatisfiedViewpoint();

    /**
     * Returns the value of the '<em><b>View</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.ViewUsage}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>View</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getViewDefinition_View()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<ViewUsage> getView();

    /**
     * Returns the value of the '<em><b>View Condition</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>View Condition</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getViewDefinition_ViewCondition()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Expression> getViewCondition();

    /**
     * Returns the value of the '<em><b>View Rendering</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>View Rendering</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getViewDefinition_ViewRendering()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    RenderingUsage getViewRendering();

} // ViewDefinition
