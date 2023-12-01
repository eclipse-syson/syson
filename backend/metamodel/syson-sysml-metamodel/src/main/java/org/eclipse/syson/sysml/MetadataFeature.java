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
 * A representation of the model object '<em><b>Metadata Feature</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.MetadataFeature#getMetaclass <em>Metaclass</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getMetadataFeature()
 * @model
 * @generated
 */
public interface MetadataFeature extends Feature, AnnotatingElement {
    /**
     * Returns the value of the '<em><b>Metaclass</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Metaclass</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getMetadataFeature_Metaclass()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Metaclass getMetaclass();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model ordered="false" baseFeatureRequired="true" baseFeatureOrdered="false"
     * @generated
     */
    EList<Element> evaluateFeature(Feature baseFeature);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model kind="operation" required="true" ordered="false"
     * @generated
     */
    boolean isSemantic();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model kind="operation" required="true" ordered="false"
     * @generated
     */
    boolean isSyntactic();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model ordered="false"
     * @generated
     */
    Element syntaxElement();

} // MetadataFeature
