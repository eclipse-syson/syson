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
 * A representation of the model object '<em><b>Connector</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.Connector#isIsDirected <em>Is Directed</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Connector#getAssociation <em>Association</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Connector#getConnectorEnd <em>Connector End</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Connector#getRelatedFeature <em>Related Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Connector#getSourceFeature <em>Source Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Connector#getTargetFeature <em>Target Feature</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getConnector()
 * @model
 * @generated
 */
public interface Connector extends Feature, Relationship {
    /**
     * Returns the value of the '<em><b>Association</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Association}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Association</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getConnector_Association()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Association> getAssociation();

    /**
     * Returns the value of the '<em><b>Connector End</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Feature}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Connector End</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getConnector_ConnectorEnd()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Feature> getConnectorEnd();

    /**
     * Returns the value of the '<em><b>Is Directed</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Directed</em>' attribute.
     * @see #setIsDirected(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getConnector_IsDirected()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsDirected();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Connector#isIsDirected <em>Is Directed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Directed</em>' attribute.
     * @see #isIsDirected()
     * @generated
     */
    void setIsDirected(boolean value);

    /**
     * Returns the value of the '<em><b>Related Feature</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Feature}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Related Feature</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getConnector_RelatedFeature()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Feature> getRelatedFeature();

    /**
     * Returns the value of the '<em><b>Source Feature</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getConnector_SourceFeature()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    Feature getSourceFeature();

    /**
     * Returns the value of the '<em><b>Target Feature</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Feature}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Feature</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getConnector_TargetFeature()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Feature> getTargetFeature();

} // Connector
