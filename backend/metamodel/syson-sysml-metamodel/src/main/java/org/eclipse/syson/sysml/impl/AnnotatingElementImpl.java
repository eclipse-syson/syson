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
package org.eclipse.syson.sysml.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.syson.sysml.AnnotatingElement;
import org.eclipse.syson.sysml.Annotation;
import org.eclipse.syson.sysml.Element;
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
     * The cached value of the '{@link #getAnnotation() <em>Annotation</em>}' reference list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getAnnotation()
     * @generated
     * @ordered
     */
    protected EList<Annotation> annotation;

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
        final List<Element> data;

        List<Annotation> annotations = this.getAnnotation();
        if (annotations.isEmpty()) {
            Element owningNamespace = this.getOwningNamespace();
            if (owningNamespace != null) {
                data = Collections.singletonList(owningNamespace);
            } else {
                data = Collections.emptyList();
            }
        } else {
            data = annotations.stream()
                    .map(Annotation::getAnnotatedElement)
                    .filter(e -> e != null)
                    .toList();
        }

        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getAnnotatingElement_AnnotatedElement(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Annotation> getAnnotation() {
        if (this.annotation == null) {
            this.annotation = new EObjectWithInverseResolvingEList<>(Annotation.class, this, SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION, SysmlPackage.ANNOTATION__ANNOTATING_ELEMENT);
        }
        return this.annotation;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Annotation> getOwnedAnnotatingRelationship() {
        List<Annotation> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getAnnotatingElement_OwnedAnnotatingRelationship(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) this.getAnnotation()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION:
                return ((InternalEList<?>) this.getAnnotation()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
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
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION:
                this.getAnnotation().clear();
                this.getAnnotation().addAll((Collection<? extends Annotation>) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION:
                this.getAnnotation().clear();
                return;
        }
        super.eUnset(featureID);
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
                return this.annotation != null && !this.annotation.isEmpty();
            case SysmlPackage.ANNOTATING_ELEMENT__OWNED_ANNOTATING_RELATIONSHIP:
                return !this.getOwnedAnnotatingRelationship().isEmpty();
        }
        return super.eIsSet(featureID);
    }

} // AnnotatingElementImpl
