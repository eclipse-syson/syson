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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Annotating Element</b></em>'. <!-- end-user-doc
 * -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.AnnotatingElement#getAnnotatedElement <em>Annotated Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.AnnotatingElement#getAnnotation <em>Annotation</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getAnnotatingElement()
 * @model
 * @generated
 */
public interface AnnotatingElement extends Element {
    /**
     * Returns the value of the '<em><b>Annotated Element</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Element}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Annotated Element</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAnnotatingElement_AnnotatedElement()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Element> getAnnotatedElement();

    /**
     * Returns the value of the '<em><b>Annotation</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Annotation}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Annotation#getAnnotatingElement <em>Annotating Element</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Annotation</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAnnotatingElement_Annotation()
     * @see org.eclipse.syson.sysml.Annotation#getAnnotatingElement
     * @model opposite="annotatingElement"
     * @generated
     */
    EList<Annotation> getAnnotation();

    /**
     * Returns the value of the '<em><b>Owned Annotating Relationship</b></em>' reference list. The list contents are of
     * type {@link org.eclipse.syson.sysml.Annotation}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Annotation#getOwningAnnotatingElement <em>Owning Annotating Element</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Annotating Relationship</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAnnotatingElement_OwnedAnnotatingRelationship()
     * @see org.eclipse.syson.sysml.Annotation#getOwningAnnotatingElement
     * @model opposite="owningAnnotatingElement" transient="true" changeable="false" volatile="true" derived="true"
     *        ordered="false" annotation="subsets"
     * @generated
     */
    EList<Annotation> getOwnedAnnotatingRelationship();

    /**
     * Returns the value of the '<em><b>Owning Annotating Relationship</b></em>' reference. It is bidirectional and its
     * opposite is '{@link org.eclipse.syson.sysml.Annotation#getOwnedAnnotatingElement <em>Owned Annotating
     * Element</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owning Annotating Relationship</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAnnotatingElement_OwningAnnotatingRelationship()
     * @see org.eclipse.syson.sysml.Annotation#getOwnedAnnotatingElement
     * @model opposite="ownedAnnotatingElement" transient="true" changeable="false" volatile="true" derived="true"
     *        ordered="false" annotation="subsets"
     * @generated
     */
    Annotation getOwningAnnotatingRelationship();

} // AnnotatingElement
