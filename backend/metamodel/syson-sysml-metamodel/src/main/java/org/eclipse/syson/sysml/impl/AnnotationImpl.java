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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
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
 * <li>{@link org.eclipse.syson.sysml.impl.AnnotationImpl#getOwningAnnotatedElement <em>Owning Annotated
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
     * The cached value of the '{@link #getAnnotatingElement() <em>Annotating Element</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getAnnotatingElement()
     * @generated
     * @ordered
     */
    protected AnnotatingElement annotatingElement;

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
        if (this.annotatingElement != null && this.annotatingElement.eIsProxy()) {
            InternalEObject oldAnnotatingElement = (InternalEObject) this.annotatingElement;
            this.annotatingElement = (AnnotatingElement) this.eResolveProxy(oldAnnotatingElement);
            if (this.annotatingElement != oldAnnotatingElement) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.ANNOTATION__ANNOTATING_ELEMENT, oldAnnotatingElement, this.annotatingElement));
                }
            }
        }
        return this.annotatingElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public AnnotatingElement basicGetAnnotatingElement() {
        return this.annotatingElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetAnnotatingElement(AnnotatingElement newAnnotatingElement, NotificationChain msgs) {
        AnnotatingElement oldAnnotatingElement = this.annotatingElement;
        this.annotatingElement = newAnnotatingElement;
        if (this.eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SysmlPackage.ANNOTATION__ANNOTATING_ELEMENT, oldAnnotatingElement, newAnnotatingElement);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setAnnotatingElement(AnnotatingElement newAnnotatingElement) {
        if (newAnnotatingElement != this.annotatingElement) {
            NotificationChain msgs = null;
            if (this.annotatingElement != null) {
                msgs = ((InternalEObject) this.annotatingElement).eInverseRemove(this, SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION, AnnotatingElement.class, msgs);
            }
            if (newAnnotatingElement != null) {
                msgs = ((InternalEObject) newAnnotatingElement).eInverseAdd(this, SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION, AnnotatingElement.class, msgs);
            }
            msgs = this.basicSetAnnotatingElement(newAnnotatingElement, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ANNOTATION__ANNOTATING_ELEMENT, newAnnotatingElement, newAnnotatingElement));
        }
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
     * @generated
     */
    public Element basicGetOwningAnnotatedElement() {
        // TODO: implement this method to return the 'Owning Annotated Element' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case SysmlPackage.ANNOTATION__ANNOTATING_ELEMENT:
                if (this.annotatingElement != null) {
                    msgs = ((InternalEObject) this.annotatingElement).eInverseRemove(this, SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION, AnnotatingElement.class, msgs);
                }
                return this.basicSetAnnotatingElement((AnnotatingElement) otherEnd, msgs);
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
            case SysmlPackage.ANNOTATION__ANNOTATING_ELEMENT:
                return this.basicSetAnnotatingElement(null, msgs);
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
            case SysmlPackage.ANNOTATION__OWNING_ANNOTATED_ELEMENT:
                if (resolve) {
                    return this.getOwningAnnotatedElement();
                }
                return this.basicGetOwningAnnotatedElement();
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
            case SysmlPackage.ANNOTATION__ANNOTATING_ELEMENT:
                this.setAnnotatingElement((AnnotatingElement) newValue);
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
            case SysmlPackage.ANNOTATION__ANNOTATING_ELEMENT:
                this.setAnnotatingElement((AnnotatingElement) null);
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
                return this.annotatingElement != null;
            case SysmlPackage.ANNOTATION__OWNING_ANNOTATED_ELEMENT:
                return this.basicGetOwningAnnotatedElement() != null;
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
        EList<Element> targets = new BasicEList<>();
        Element annotatedElement = this.getAnnotatedElement();
        if (annotatedElement != null) {
            targets.add(annotatedElement);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRelationship_Target(), targets.size(), targets.toArray());
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
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
