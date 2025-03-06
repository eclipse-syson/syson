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
package org.eclipse.syson.sysml.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.AnnotatingElement;
import org.eclipse.syson.sysml.Annotation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Annotating Element</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.AnnotatingElementImpl#getAnnotatedElement <em>Annotated Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.AnnotatingElementImpl#getAnnotation <em>Annotation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AnnotatingElementImpl extends ElementImpl implements AnnotatingElement {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected AnnotatingElementImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getAnnotatingElement();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getAnnotatedElement() {
        final List<Element> annotatedElements;

        List<Annotation> annotations = this.getAnnotation();
        if (annotations.isEmpty()) {
            Element owningNamespace = this.getOwningNamespace();
            if (owningNamespace != null) {
                annotatedElements = Collections.singletonList(owningNamespace);
            } else {
                annotatedElements = Collections.emptyList();
            }
        } else {
            annotatedElements = annotations.stream()
                    .map(Annotation::getAnnotatedElement)
                    .filter(Objects::nonNull)
                    .toList();
        }

        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getAnnotatingElement_AnnotatedElement(), annotatedElements.size(), annotatedElements.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Annotation> getAnnotation() {
        List<Element> annotations = new ArrayList<>();
        Annotation owningAnnotatingRelationship = this.getOwningAnnotatingRelationship();
        if (owningAnnotatingRelationship != null) {
            annotations.add(owningAnnotatingRelationship);
        }
        annotations.addAll(this.getOwnedAnnotatingRelationship());
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getAnnotatingElement_Annotation(), annotations.size(), annotations.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Annotation> getOwnedAnnotatingRelationship() {
        List<Annotation> ownedAnnotatingRelationships = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(Annotation.class::isInstance)
                .map(Annotation.class::cast)
                .filter(a -> !this.equals(a.getAnnotatedElement()))
                .forEach(ownedAnnotatingRelationships::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getAnnotatingElement_OwnedAnnotatingRelationship(), ownedAnnotatingRelationships.size(),
                ownedAnnotatingRelationships.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Annotation getOwningAnnotatingRelationship() {
        Annotation owningAnnotatingRelationship = this.basicGetOwningAnnotatingRelationship();
        return owningAnnotatingRelationship != null && owningAnnotatingRelationship.eIsProxy() ? (Annotation) this.eResolveProxy((InternalEObject) owningAnnotatingRelationship)
                : owningAnnotatingRelationship;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Annotation basicGetOwningAnnotatingRelationship() {
        Relationship owningRelationship = this.getOwningRelationship();
        if (owningRelationship instanceof Annotation annotation) {
            return annotation;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.ANNOTATING_ELEMENT__ANNOTATED_ELEMENT:
                return this.getAnnotatedElement();
            case SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION:
                return this.getAnnotation();
            case SysmlPackage.ANNOTATING_ELEMENT__OWNED_ANNOTATING_RELATIONSHIP:
                return this.getOwnedAnnotatingRelationship();
            case SysmlPackage.ANNOTATING_ELEMENT__OWNING_ANNOTATING_RELATIONSHIP:
                if (resolve) {
                    return this.getOwningAnnotatingRelationship();
                }
                return this.basicGetOwningAnnotatingRelationship();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.ANNOTATING_ELEMENT__ANNOTATED_ELEMENT:
                return !this.getAnnotatedElement().isEmpty();
            case SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION:
                return !this.getAnnotation().isEmpty();
            case SysmlPackage.ANNOTATING_ELEMENT__OWNED_ANNOTATING_RELATIONSHIP:
                return !this.getOwnedAnnotatingRelationship().isEmpty();
            case SysmlPackage.ANNOTATING_ELEMENT__OWNING_ANNOTATING_RELATIONSHIP:
                return this.basicGetOwningAnnotatingRelationship() != null;
        }
        return super.eIsSet(featureID);
    }

} // AnnotatingElementImpl
