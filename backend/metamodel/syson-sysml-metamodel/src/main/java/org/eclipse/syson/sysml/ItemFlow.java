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
 * A representation of the model object '<em><b>Item Flow</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.ItemFlow#getInteraction <em>Interaction</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.ItemFlow#getItemFeature <em>Item Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.ItemFlow#getItemFlowEnd <em>Item Flow End</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.ItemFlow#getItemType <em>Item Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.ItemFlow#getSourceOutputFeature <em>Source Output Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.ItemFlow#getTargetInputFeature <em>Target Input Feature</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getItemFlow()
 * @model
 * @generated
 */
public interface ItemFlow extends Connector, Step {
    /**
     * Returns the value of the '<em><b>Interaction</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Interaction}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Interaction</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getItemFlow_Interaction()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Interaction> getInteraction();

    /**
     * Returns the value of the '<em><b>Item Feature</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Item Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getItemFlow_ItemFeature()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ItemFeature getItemFeature();

    /**
     * Returns the value of the '<em><b>Item Flow End</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.ItemFlowEnd}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Item Flow End</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getItemFlow_ItemFlowEnd()
     * @model upper="2" transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<ItemFlowEnd> getItemFlowEnd();

    /**
     * Returns the value of the '<em><b>Item Type</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Classifier}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Item Type</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getItemFlow_ItemType()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Classifier> getItemType();

    /**
     * Returns the value of the '<em><b>Source Output Feature</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source Output Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getItemFlow_SourceOutputFeature()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    Feature getSourceOutputFeature();

    /**
     * Returns the value of the '<em><b>Target Input Feature</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Input Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getItemFlow_TargetInputFeature()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    Feature getTargetInputFeature();

} // ItemFlow
