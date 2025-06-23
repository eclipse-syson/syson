/**
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
 */
package org.eclipse.syson.sysml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Flow</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Flow#getFlowEnd <em>Flow End</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Flow#getInteraction <em>Interaction</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Flow#getPayloadFeature <em>Payload Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Flow#getPayloadType <em>Payload Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Flow#getSourceOutputFeature <em>Source Output Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Flow#getTargetInputFeature <em>Target Input Feature</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getFlow()
 * @model
 * @generated
 */
public interface Flow extends Connector, Step {
    /**
     * Returns the value of the '<em><b>Flow End</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.FlowEnd}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Flow End</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFlow_FlowEnd()
     * @model upper="2" transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<FlowEnd> getFlowEnd();

    /**
     * Returns the value of the '<em><b>Interaction</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Interaction}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Interaction</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFlow_Interaction()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="redefines"
     * @generated
     */
    EList<Interaction> getInteraction();

    /**
     * Returns the value of the '<em><b>Payload Feature</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Payload Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFlow_PayloadFeature()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    PayloadFeature getPayloadFeature();

    /**
     * Returns the value of the '<em><b>Payload Type</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Classifier}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Payload Type</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFlow_PayloadType()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Classifier> getPayloadType();

    /**
     * Returns the value of the '<em><b>Source Output Feature</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Source Output Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFlow_SourceOutputFeature()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    Feature getSourceOutputFeature();

    /**
     * Returns the value of the '<em><b>Target Input Feature</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Target Input Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFlow_TargetInputFeature()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    Feature getTargetInputFeature();

} // Flow
