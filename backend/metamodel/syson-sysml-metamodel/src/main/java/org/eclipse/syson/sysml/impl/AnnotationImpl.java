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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectEList;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.AnnotatingElement;
import org.eclipse.syson.sysml.Annotation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Annotation</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.AnnotationImpl#getAnnotatedElement <em>Annotated Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.AnnotationImpl#getAnnotatingElement <em>Annotating Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.AnnotationImpl#getOwnedAnnotatingElement <em>Owned Annotating
 * Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.AnnotationImpl#getOwningAnnotatedElement <em>Owning Annotated
 * Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.AnnotationImpl#getOwningAnnotatingElement <em>Owning Annotating
 * Element</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AnnotationImpl extends RelationshipImpl implements Annotation {
    /**
     * The cached value of the '{@link #getAnnotatedElement() <em>Annotated Element</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getAnnotatedElement()
     * @generated
     * @ordered
     */
    protected Element annotatedElement;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected AnnotationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getAnnotation();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Element getAnnotatedElement() {
        if (this.annotatedElement != null && this.annotatedElement.eIsProxy()) {
            InternalEObject oldAnnotatedElement = (InternalEObject) this.annotatedElement;
            this.annotatedElement = (Element) this.eResolveProxy(oldAnnotatedElement);
            if (this.annotatedElement != oldAnnotatedElement) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.ANNOTATION__ANNOTATED_ELEMENT, oldAnnotatedElement, this.annotatedElement));
                }
            }
        }
        return this.annotatedElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Element basicGetAnnotatedElement() {
        return this.annotatedElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setAnnotatedElement(Element newAnnotatedElement) {
        Element oldAnnotatedElement = this.annotatedElement;
        this.annotatedElement = newAnnotatedElement;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ANNOTATION__ANNOTATED_ELEMENT, oldAnnotatedElement, this.annotatedElement));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AnnotatingElement getAnnotatingElement() {
        AnnotatingElement annotatingElement = this.basicGetAnnotatingElement();
        return annotatingElement != null && annotatingElement.eIsProxy() ? (AnnotatingElement) this.eResolveProxy((InternalEObject) annotatingElement) : annotatingElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public AnnotatingElement basicGetAnnotatingElement() {
        AnnotatingElement ownedAnnotatingElement = this.getOwnedAnnotatingElement();
        if (ownedAnnotatingElement != null) {
            return ownedAnnotatingElement;
        }
        return this.getOwningAnnotatingElement();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AnnotatingElement getOwnedAnnotatingElement() {
        AnnotatingElement ownedAnnotatingElement = this.basicGetOwnedAnnotatingElement();
        return ownedAnnotatingElement != null && ownedAnnotatingElement.eIsProxy() ? (AnnotatingElement) this.eResolveProxy((InternalEObject) ownedAnnotatingElement) : ownedAnnotatingElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public AnnotatingElement basicGetOwnedAnnotatingElement() {
        List<AnnotatingElement> ownedAnnotatingElements = new ArrayList<>();
        this.getOwnedRelatedElement().stream()
                .filter(AnnotatingElement.class::isInstance)
                .map(AnnotatingElement.class::cast)
                .forEach(ownedAnnotatingElements::add);
        if (!ownedAnnotatingElements.isEmpty()) {
            return ownedAnnotatingElements.get(0);
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Element getOwningAnnotatedElement() {
        Element owningAnnotatedElement = this.basicGetOwningAnnotatedElement();
        return owningAnnotatedElement != null && owningAnnotatedElement.eIsProxy() ? (Element) this.eResolveProxy((InternalEObject) owningAnnotatedElement) : owningAnnotatedElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Element basicGetOwningAnnotatedElement() {
        Element annotatedElement = this.getAnnotatedElement();
        Element owningRelatedElement = this.getOwningRelatedElement();
        if (Objects.equals(annotatedElement, owningRelatedElement)) {
            return annotatedElement;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AnnotatingElement getOwningAnnotatingElement() {
        AnnotatingElement owningAnnotatingElement = this.basicGetOwningAnnotatingElement();
        return owningAnnotatingElement != null && owningAnnotatingElement.eIsProxy() ? (AnnotatingElement) this.eResolveProxy((InternalEObject) owningAnnotatingElement) : owningAnnotatingElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public AnnotatingElement basicGetOwningAnnotatingElement() {
        Element owningRelatedElement = this.getOwningRelatedElement();
        if (owningRelatedElement instanceof AnnotatingElement annotatingElement) {
            return annotatingElement;
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
            case SysmlPackage.ANNOTATION__ANNOTATED_ELEMENT:
                if (resolve) {
                    return this.getAnnotatedElement();
                }
                return this.basicGetAnnotatedElement();
            case SysmlPackage.ANNOTATION__ANNOTATING_ELEMENT:
                if (resolve) {
                    return this.getAnnotatingElement();
                }
                return this.basicGetAnnotatingElement();
            case SysmlPackage.ANNOTATION__OWNED_ANNOTATING_ELEMENT:
                if (resolve) {
                    return this.getOwnedAnnotatingElement();
                }
                return this.basicGetOwnedAnnotatingElement();
            case SysmlPackage.ANNOTATION__OWNING_ANNOTATED_ELEMENT:
                if (resolve) {
                    return this.getOwningAnnotatedElement();
                }
                return this.basicGetOwningAnnotatedElement();
            case SysmlPackage.ANNOTATION__OWNING_ANNOTATING_ELEMENT:
                if (resolve) {
                    return this.getOwningAnnotatingElement();
                }
                return this.basicGetOwningAnnotatingElement();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.ANNOTATION__ANNOTATED_ELEMENT:
                this.setAnnotatedElement((Element) newValue);
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
            case SysmlPackage.ANNOTATION__ANNOTATED_ELEMENT:
                this.setAnnotatedElement((Element) null);
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
            case SysmlPackage.ANNOTATION__ANNOTATED_ELEMENT:
                return this.annotatedElement != null;
            case SysmlPackage.ANNOTATION__ANNOTATING_ELEMENT:
                return this.basicGetAnnotatingElement() != null;
            case SysmlPackage.ANNOTATION__OWNED_ANNOTATING_ELEMENT:
                return this.basicGetOwnedAnnotatingElement() != null;
            case SysmlPackage.ANNOTATION__OWNING_ANNOTATED_ELEMENT:
                return this.basicGetOwningAnnotatedElement() != null;
            case SysmlPackage.ANNOTATION__OWNING_ANNOTATING_ELEMENT:
                return this.basicGetOwningAnnotatingElement() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getTarget() {
        EList<Element> targets = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.ANNOTATION__TARGET) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                if (collection != null) {
                    Iterator<? extends Element> iterator = collection.iterator();
                    if (iterator.hasNext()) {
                        Element next = iterator.next();
                        AnnotationImpl.this.setAnnotatedElement(next);
                        return true;
                    }
                }
                return false;
            }

            @Override
            protected void dispatchNotification(Notification notification) {
            }
        };
        Element annotatedElement = this.getAnnotatedElement();
        if (annotatedElement != null) {
            targets.add(annotatedElement);
        }
        return targets;
    }

    /**
     * <!-- begin-user-doc --> annotatingElement redefines source <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getSource() {
        EList<Element> sources = new BasicEList<>();
        AnnotatingElement annotatingElement = this.getAnnotatingElement();
        if (annotatingElement != null) {
            sources.add(annotatingElement);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRelationship_Source(), sources.size(), sources.toArray());
    }

} // AnnotationImpl
