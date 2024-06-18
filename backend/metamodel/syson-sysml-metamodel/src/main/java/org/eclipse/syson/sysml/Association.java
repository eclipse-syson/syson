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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Association</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Association#getAssociationEnd <em>Association End</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Association#getRelatedType <em>Related Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Association#getSourceType <em>Source Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Association#getTargetType <em>Target Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getAssociation()
 * @model
 * @generated
 */
public interface Association extends Classifier, Relationship {
    /**
     * Returns the value of the '<em><b>Association End</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Feature}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Association End</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAssociation_AssociationEnd()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="redefines"
     * @generated
     */
    EList<Feature> getAssociationEnd();

    /**
     * Returns the value of the '<em><b>Related Type</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Type}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Related Type</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAssociation_RelatedType()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="redefines"
     * @generated
     */
    EList<Type> getRelatedType();

    /**
     * Returns the value of the '<em><b>Source Type</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Source Type</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAssociation_SourceType()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="redefines"
     *        annotation="subsets"
     * @generated
     */
    Type getSourceType();

    /**
     * Returns the value of the '<em><b>Target Type</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Type}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Target Type</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAssociation_TargetType()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="redefines"
     *        annotation="subsets"
     * @generated
     */
    EList<Type> getTargetType();

} // Association
