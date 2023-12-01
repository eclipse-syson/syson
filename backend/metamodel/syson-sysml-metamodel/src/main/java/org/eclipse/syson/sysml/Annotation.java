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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annotation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.Annotation#getAnnotatedElement <em>Annotated Element</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Annotation#getAnnotatingElement <em>Annotating Element</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Annotation#getOwningAnnotatedElement <em>Owning Annotated Element</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getAnnotation()
 * @model
 * @generated
 */
public interface Annotation extends Relationship {
    /**
     * Returns the value of the '<em><b>Annotated Element</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Annotated Element</em>' reference.
     * @see #setAnnotatedElement(Element)
     * @see org.eclipse.syson.sysml.SysmlPackage#getAnnotation_AnnotatedElement()
     * @model required="true" ordered="false"
     * @generated
     */
    Element getAnnotatedElement();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Annotation#getAnnotatedElement <em>Annotated Element</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Annotated Element</em>' reference.
     * @see #getAnnotatedElement()
     * @generated
     */
    void setAnnotatedElement(Element value);

    /**
     * Returns the value of the '<em><b>Annotating Element</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.AnnotatingElement#getAnnotation <em>Annotation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Annotating Element</em>' reference.
     * @see #setAnnotatingElement(AnnotatingElement)
     * @see org.eclipse.syson.sysml.SysmlPackage#getAnnotation_AnnotatingElement()
     * @see org.eclipse.syson.sysml.AnnotatingElement#getAnnotation
     * @model opposite="annotation" required="true" ordered="false"
     * @generated
     */
    AnnotatingElement getAnnotatingElement();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Annotation#getAnnotatingElement <em>Annotating Element</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Annotating Element</em>' reference.
     * @see #getAnnotatingElement()
     * @generated
     */
    void setAnnotatingElement(AnnotatingElement value);

    /**
     * Returns the value of the '<em><b>Owning Annotated Element</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Element#getOwnedAnnotation <em>Owned Annotation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owning Annotated Element</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAnnotation_OwningAnnotatedElement()
     * @see org.eclipse.syson.sysml.Element#getOwnedAnnotation
     * @model opposite="ownedAnnotation" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Element getOwningAnnotatedElement();

} // Annotation
