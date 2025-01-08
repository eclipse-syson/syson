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
package org.eclipse.syson.sysml.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Relationship</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.RelationshipImpl#isIsImplied <em>Is Implied</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.RelationshipImpl#getOwnedRelatedElement <em>Owned Related Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.RelationshipImpl#getOwningRelatedElement <em>Owning Related
 * Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.RelationshipImpl#getRelatedElement <em>Related Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.RelationshipImpl#getSource <em>Source</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.RelationshipImpl#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class RelationshipImpl extends ElementImpl implements Relationship {
    /**
     * The default value of the '{@link #isIsImplied() <em>Is Implied</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsImplied()
     * @generated
     * @ordered
     */
    protected static final boolean IS_IMPLIED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsImplied() <em>Is Implied</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsImplied()
     * @generated
     * @ordered
     */
    protected boolean isImplied = IS_IMPLIED_EDEFAULT;

    /**
     * The cached value of the '{@link #getOwnedRelatedElement() <em>Owned Related Element</em>}' containment reference
     * list. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getOwnedRelatedElement()
     * @generated
     * @ordered
     */
    protected EList<Element> ownedRelatedElement;

    /**
     * The cached value of the '{@link #getSource() <em>Source</em>}' reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getSource()
     * @generated
     * @ordered
     */
    protected EList<Element> source;

    /**
     * The cached value of the '{@link #getTarget() <em>Target</em>}' reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getTarget()
     * @generated
     * @ordered
     */
    protected EList<Element> target;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected RelationshipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getRelationship();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsImplied() {
        return this.isImplied;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsImplied(boolean newIsImplied) {
        boolean oldIsImplied = this.isImplied;
        this.isImplied = newIsImplied;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.RELATIONSHIP__IS_IMPLIED, oldIsImplied, this.isImplied));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Element> getOwnedRelatedElement() {
        if (this.ownedRelatedElement == null) {
            this.ownedRelatedElement = new EObjectContainmentWithInverseEList<>(Element.class, this, SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT,
                    SysmlPackage.ELEMENT__OWNING_RELATIONSHIP);
        }
        return this.ownedRelatedElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Element getOwningRelatedElement() {
        if (this.eContainerFeatureID() != SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT) {
            return null;
        }
        return (Element) this.eInternalContainer();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetOwningRelatedElement(Element newOwningRelatedElement, NotificationChain msgs) {
        msgs = this.eBasicSetContainer((InternalEObject) newOwningRelatedElement, SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setOwningRelatedElement(Element newOwningRelatedElement) {
        if (newOwningRelatedElement != this.eInternalContainer() || (this.eContainerFeatureID() != SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT && newOwningRelatedElement != null)) {
            if (EcoreUtil.isAncestor(this, newOwningRelatedElement)) {
                throw new IllegalArgumentException("Recursive containment not allowed for " + this.toString());
            }
            NotificationChain msgs = null;
            if (this.eInternalContainer() != null) {
                msgs = this.eBasicRemoveFromContainer(msgs);
            }
            if (newOwningRelatedElement != null) {
                msgs = ((InternalEObject) newOwningRelatedElement).eInverseAdd(this, SysmlPackage.ELEMENT__OWNED_RELATIONSHIP, Element.class, msgs);
            }
            msgs = this.basicSetOwningRelatedElement(newOwningRelatedElement, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT, newOwningRelatedElement, newOwningRelatedElement));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getRelatedElement() {
        List<Element> relatedElements = new ArrayList<>();
        relatedElements.addAll(this.getSource());
        relatedElements.addAll(this.getTarget());
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRelationship_RelatedElement(), relatedElements.size(), relatedElements.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Element> getSource() {
        if (this.source == null) {
            this.source = new EObjectResolvingEList<>(Element.class, this, SysmlPackage.RELATIONSHIP__SOURCE);
        }
        return this.source;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Element> getTarget() {
        if (this.target == null) {
            this.target = new EObjectResolvingEList<>(Element.class, this, SysmlPackage.RELATIONSHIP__TARGET);
        }
        return this.target;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Namespace libraryNamespace() {
        Namespace libraryNamespace = null;
        if (this.getOwningRelatedElement() != null) {
            libraryNamespace = this.getOwningRelatedElement().libraryNamespace();
        } else if (this.getOwningRelationship() != null) {
            libraryNamespace = this.getOwningRelationship().libraryNamespace();
        }
        return libraryNamespace;
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
            case SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) this.getOwnedRelatedElement()).basicAdd(otherEnd, msgs);
            case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                if (this.eInternalContainer() != null) {
                    msgs = this.eBasicRemoveFromContainer(msgs);
                }
                return this.basicSetOwningRelatedElement((Element) otherEnd, msgs);
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
            case SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT:
                return ((InternalEList<?>) this.getOwnedRelatedElement()).basicRemove(otherEnd, msgs);
            case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                return this.basicSetOwningRelatedElement(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch (this.eContainerFeatureID()) {
            case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                return this.eInternalContainer().eInverseRemove(this, SysmlPackage.ELEMENT__OWNED_RELATIONSHIP, Element.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.RELATIONSHIP__IS_IMPLIED:
                return this.isIsImplied();
            case SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT:
                return this.getOwnedRelatedElement();
            case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                return this.getOwningRelatedElement();
            case SysmlPackage.RELATIONSHIP__RELATED_ELEMENT:
                return this.getRelatedElement();
            case SysmlPackage.RELATIONSHIP__SOURCE:
                return this.getSource();
            case SysmlPackage.RELATIONSHIP__TARGET:
                return this.getTarget();
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
            case SysmlPackage.RELATIONSHIP__IS_IMPLIED:
                this.setIsImplied((Boolean) newValue);
                return;
            case SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT:
                this.getOwnedRelatedElement().clear();
                this.getOwnedRelatedElement().addAll((Collection<? extends Element>) newValue);
                return;
            case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                this.setOwningRelatedElement((Element) newValue);
                return;
            case SysmlPackage.RELATIONSHIP__SOURCE:
                this.getSource().clear();
                this.getSource().addAll((Collection<? extends Element>) newValue);
                return;
            case SysmlPackage.RELATIONSHIP__TARGET:
                this.getTarget().clear();
                this.getTarget().addAll((Collection<? extends Element>) newValue);
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
            case SysmlPackage.RELATIONSHIP__IS_IMPLIED:
                this.setIsImplied(IS_IMPLIED_EDEFAULT);
                return;
            case SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT:
                this.getOwnedRelatedElement().clear();
                return;
            case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                this.setOwningRelatedElement((Element) null);
                return;
            case SysmlPackage.RELATIONSHIP__SOURCE:
                this.getSource().clear();
                return;
            case SysmlPackage.RELATIONSHIP__TARGET:
                this.getTarget().clear();
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
            case SysmlPackage.RELATIONSHIP__IS_IMPLIED:
                return this.isImplied != IS_IMPLIED_EDEFAULT;
            case SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT:
                return this.ownedRelatedElement != null && !this.ownedRelatedElement.isEmpty();
            case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                return this.getOwningRelatedElement() != null;
            case SysmlPackage.RELATIONSHIP__RELATED_ELEMENT:
                return !this.getRelatedElement().isEmpty();
            case SysmlPackage.RELATIONSHIP__SOURCE:
                return this.source != null && !this.source.isEmpty();
            case SysmlPackage.RELATIONSHIP__TARGET:
                return this.target != null && !this.target.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (this.eIsProxy()) {
            return super.toString();
        }

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (isImplied: ");
        result.append(this.isImplied);
        result.append(')');
        return result.toString();
    }

} // RelationshipImpl
