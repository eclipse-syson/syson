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
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Relationship</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.RelationshipImpl#isIsImplied <em>Is Implied</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RelationshipImpl#getOwnedRelatedElement <em>Owned Related Element</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RelationshipImpl#getOwningRelatedElement <em>Owning Related Element</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RelationshipImpl#getRelatedElement <em>Related Element</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RelationshipImpl#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RelationshipImpl#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class RelationshipImpl extends ElementImpl implements Relationship {
    /**
     * The default value of the '{@link #isIsImplied() <em>Is Implied</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsImplied()
     * @generated
     * @ordered
     */
    protected static final boolean IS_IMPLIED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsImplied() <em>Is Implied</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsImplied()
     * @generated
     * @ordered
     */
    protected boolean isImplied = IS_IMPLIED_EDEFAULT;

    /**
     * The cached value of the '{@link #getOwnedRelatedElement() <em>Owned Related Element</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOwnedRelatedElement()
     * @generated
     * @ordered
     */
    protected EList<Element> ownedRelatedElement;

    /**
     * The cached value of the '{@link #getSource() <em>Source</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSource()
     * @generated
     * @ordered
     */
    protected EList<Element> source;

    /**
     * The cached value of the '{@link #getTarget() <em>Target</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTarget()
     * @generated
     * @ordered
     */
    protected EList<Element> target;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RelationshipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getRelationship();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isIsImplied() {
        return isImplied;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsImplied(boolean newIsImplied) {
        boolean oldIsImplied = isImplied;
        isImplied = newIsImplied;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.RELATIONSHIP__IS_IMPLIED, oldIsImplied, isImplied));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Element> getOwnedRelatedElement() {
        if (ownedRelatedElement == null) {
            ownedRelatedElement = new EObjectContainmentWithInverseEList<Element>(Element.class, this, SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT, SysmlPackage.ELEMENT__OWNING_RELATIONSHIP);
        }
        return ownedRelatedElement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Element getOwningRelatedElement() {
        if (eContainerFeatureID() != SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT) return null;
        return (Element)eInternalContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOwningRelatedElement(Element newOwningRelatedElement, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject)newOwningRelatedElement, SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setOwningRelatedElement(Element newOwningRelatedElement) {
        if (newOwningRelatedElement != eInternalContainer() || (eContainerFeatureID() != SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT && newOwningRelatedElement != null)) {
            if (EcoreUtil.isAncestor(this, newOwningRelatedElement))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newOwningRelatedElement != null)
                msgs = ((InternalEObject)newOwningRelatedElement).eInverseAdd(this, SysmlPackage.ELEMENT__OWNED_RELATIONSHIP, Element.class, msgs);
            msgs = basicSetOwningRelatedElement(newOwningRelatedElement, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT, newOwningRelatedElement, newOwningRelatedElement));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Element> getRelatedElement() {
        List<Element> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRelationship_RelatedElement(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Element> getSource() {
        if (source == null) {
            source = new EObjectResolvingEList<Element>(Element.class, this, SysmlPackage.RELATIONSHIP__SOURCE);
        }
        return source;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Element> getTarget() {
        if (target == null) {
            target = new EObjectResolvingEList<Element>(Element.class, this, SysmlPackage.RELATIONSHIP__TARGET);
        }
        return target;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT:
                return ((InternalEList<InternalEObject>)(InternalEList<?>)getOwnedRelatedElement()).basicAdd(otherEnd, msgs);
            case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                if (eInternalContainer() != null)
                    msgs = eBasicRemoveFromContainer(msgs);
                return basicSetOwningRelatedElement((Element)otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT:
                return ((InternalEList<?>)getOwnedRelatedElement()).basicRemove(otherEnd, msgs);
            case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                return basicSetOwningRelatedElement(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch (eContainerFeatureID()) {
            case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                return eInternalContainer().eInverseRemove(this, SysmlPackage.ELEMENT__OWNED_RELATIONSHIP, Element.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.RELATIONSHIP__IS_IMPLIED:
                return isIsImplied();
            case SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT:
                return getOwnedRelatedElement();
            case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                return getOwningRelatedElement();
            case SysmlPackage.RELATIONSHIP__RELATED_ELEMENT:
                return getRelatedElement();
            case SysmlPackage.RELATIONSHIP__SOURCE:
                return getSource();
            case SysmlPackage.RELATIONSHIP__TARGET:
                return getTarget();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.RELATIONSHIP__IS_IMPLIED:
                setIsImplied((Boolean)newValue);
                return;
            case SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT:
                getOwnedRelatedElement().clear();
                getOwnedRelatedElement().addAll((Collection<? extends Element>)newValue);
                return;
            case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                setOwningRelatedElement((Element)newValue);
                return;
            case SysmlPackage.RELATIONSHIP__SOURCE:
                getSource().clear();
                getSource().addAll((Collection<? extends Element>)newValue);
                return;
            case SysmlPackage.RELATIONSHIP__TARGET:
                getTarget().clear();
                getTarget().addAll((Collection<? extends Element>)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SysmlPackage.RELATIONSHIP__IS_IMPLIED:
                setIsImplied(IS_IMPLIED_EDEFAULT);
                return;
            case SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT:
                getOwnedRelatedElement().clear();
                return;
            case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                setOwningRelatedElement((Element)null);
                return;
            case SysmlPackage.RELATIONSHIP__SOURCE:
                getSource().clear();
                return;
            case SysmlPackage.RELATIONSHIP__TARGET:
                getTarget().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.RELATIONSHIP__IS_IMPLIED:
                return isImplied != IS_IMPLIED_EDEFAULT;
            case SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT:
                return ownedRelatedElement != null && !ownedRelatedElement.isEmpty();
            case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                return getOwningRelatedElement() != null;
            case SysmlPackage.RELATIONSHIP__RELATED_ELEMENT:
                return !getRelatedElement().isEmpty();
            case SysmlPackage.RELATIONSHIP__SOURCE:
                return source != null && !source.isEmpty();
            case SysmlPackage.RELATIONSHIP__TARGET:
                return target != null && !target.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (isImplied: ");
        result.append(isImplied);
        result.append(')');
        return result.toString();
    }

} //RelationshipImpl
