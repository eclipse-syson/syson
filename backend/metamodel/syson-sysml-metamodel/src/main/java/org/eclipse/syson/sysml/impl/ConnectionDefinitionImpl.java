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
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Association;
import org.eclipse.syson.sysml.AssociationStructure;
import org.eclipse.syson.sysml.ConnectionDefinition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Connection Definition</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionDefinitionImpl#isIsImplied <em>Is Implied</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionDefinitionImpl#getOwnedRelatedElement <em>Owned Related
 * Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionDefinitionImpl#getOwningRelatedElement <em>Owning Related
 * Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionDefinitionImpl#getRelatedElement <em>Related Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionDefinitionImpl#getSource <em>Source</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionDefinitionImpl#getTarget <em>Target</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionDefinitionImpl#getAssociationEnd <em>Association End</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionDefinitionImpl#getRelatedType <em>Related Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionDefinitionImpl#getSourceType <em>Source Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionDefinitionImpl#getTargetType <em>Target Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionDefinitionImpl#getConnectionEnd <em>Connection End</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConnectionDefinitionImpl extends PartDefinitionImpl implements ConnectionDefinition {
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
    protected ConnectionDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getConnectionDefinition();
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
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.CONNECTION_DEFINITION__IS_IMPLIED, oldIsImplied, this.isImplied));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Element> getOwnedRelatedElement() {
        if (this.ownedRelatedElement == null) {
            this.ownedRelatedElement = new EObjectContainmentWithInverseEList<>(Element.class, this, SysmlPackage.CONNECTION_DEFINITION__OWNED_RELATED_ELEMENT,
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
        if (this.eContainerFeatureID() != SysmlPackage.CONNECTION_DEFINITION__OWNING_RELATED_ELEMENT)
            return null;
        return (Element) this.eInternalContainer();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetOwningRelatedElement(Element newOwningRelatedElement, NotificationChain msgs) {
        msgs = this.eBasicSetContainer((InternalEObject) newOwningRelatedElement, SysmlPackage.CONNECTION_DEFINITION__OWNING_RELATED_ELEMENT, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setOwningRelatedElement(Element newOwningRelatedElement) {
        if (newOwningRelatedElement != this.eInternalContainer() || (this.eContainerFeatureID() != SysmlPackage.CONNECTION_DEFINITION__OWNING_RELATED_ELEMENT && newOwningRelatedElement != null)) {
            if (EcoreUtil.isAncestor(this, newOwningRelatedElement))
                throw new IllegalArgumentException("Recursive containment not allowed for " + this.toString());
            NotificationChain msgs = null;
            if (this.eInternalContainer() != null)
                msgs = this.eBasicRemoveFromContainer(msgs);
            if (newOwningRelatedElement != null)
                msgs = ((InternalEObject) newOwningRelatedElement).eInverseAdd(this, SysmlPackage.ELEMENT__OWNED_RELATIONSHIP, Element.class, msgs);
            msgs = this.basicSetOwningRelatedElement(newOwningRelatedElement, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.CONNECTION_DEFINITION__OWNING_RELATED_ELEMENT, newOwningRelatedElement, newOwningRelatedElement));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getRelatedElement() {
        List<ActionUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRelationship_RelatedElement(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Element> getSource() {
        if (this.source == null) {
            this.source = new EObjectResolvingEList<>(Element.class, this, SysmlPackage.CONNECTION_DEFINITION__SOURCE);
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
            this.target = new EObjectResolvingEList<>(Element.class, this, SysmlPackage.CONNECTION_DEFINITION__TARGET);
        }
        return this.target;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getAssociationEnd() {
        return this.getEndFeature();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Type> getRelatedType() {
        List<ActionUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getAssociation_RelatedType(), data.size(), data.toArray());

    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getSourceType() {
        Type sourceType = this.basicGetSourceType();
        return sourceType != null && sourceType.eIsProxy() ? (Type) this.eResolveProxy((InternalEObject) sourceType) : sourceType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetSourceType() {
        // TODO: implement this method to return the 'Source Type' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Type> getTargetType() {
        List<ActionUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getAssociation_TargetType(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Usage> getConnectionEnd() {
        List<Usage> ends = this.getAssociationEnd().stream()
                .filter(Usage.class::isInstance)
                .map(Usage.class::cast)
                .toList();

        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getConnectionDefinition_ConnectionEnd(), ends.size(), ends.toArray());
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
            case SysmlPackage.CONNECTION_DEFINITION__OWNED_RELATED_ELEMENT:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) this.getOwnedRelatedElement()).basicAdd(otherEnd, msgs);
            case SysmlPackage.CONNECTION_DEFINITION__OWNING_RELATED_ELEMENT:
                if (this.eInternalContainer() != null)
                    msgs = this.eBasicRemoveFromContainer(msgs);
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
            case SysmlPackage.CONNECTION_DEFINITION__OWNED_RELATED_ELEMENT:
                return ((InternalEList<?>) this.getOwnedRelatedElement()).basicRemove(otherEnd, msgs);
            case SysmlPackage.CONNECTION_DEFINITION__OWNING_RELATED_ELEMENT:
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
            case SysmlPackage.CONNECTION_DEFINITION__OWNING_RELATED_ELEMENT:
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
            case SysmlPackage.CONNECTION_DEFINITION__IS_IMPLIED:
                return this.isIsImplied();
            case SysmlPackage.CONNECTION_DEFINITION__OWNED_RELATED_ELEMENT:
                return this.getOwnedRelatedElement();
            case SysmlPackage.CONNECTION_DEFINITION__OWNING_RELATED_ELEMENT:
                return this.getOwningRelatedElement();
            case SysmlPackage.CONNECTION_DEFINITION__RELATED_ELEMENT:
                return this.getRelatedElement();
            case SysmlPackage.CONNECTION_DEFINITION__SOURCE:
                return this.getSource();
            case SysmlPackage.CONNECTION_DEFINITION__TARGET:
                return this.getTarget();
            case SysmlPackage.CONNECTION_DEFINITION__ASSOCIATION_END:
                return this.getAssociationEnd();
            case SysmlPackage.CONNECTION_DEFINITION__RELATED_TYPE:
                return this.getRelatedType();
            case SysmlPackage.CONNECTION_DEFINITION__SOURCE_TYPE:
                if (resolve)
                    return this.getSourceType();
                return this.basicGetSourceType();
            case SysmlPackage.CONNECTION_DEFINITION__TARGET_TYPE:
                return this.getTargetType();
            case SysmlPackage.CONNECTION_DEFINITION__CONNECTION_END:
                return this.getConnectionEnd();
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
            case SysmlPackage.CONNECTION_DEFINITION__IS_IMPLIED:
                this.setIsImplied((Boolean) newValue);
                return;
            case SysmlPackage.CONNECTION_DEFINITION__OWNED_RELATED_ELEMENT:
                this.getOwnedRelatedElement().clear();
                this.getOwnedRelatedElement().addAll((Collection<? extends Element>) newValue);
                return;
            case SysmlPackage.CONNECTION_DEFINITION__OWNING_RELATED_ELEMENT:
                this.setOwningRelatedElement((Element) newValue);
                return;
            case SysmlPackage.CONNECTION_DEFINITION__SOURCE:
                this.getSource().clear();
                this.getSource().addAll((Collection<? extends Element>) newValue);
                return;
            case SysmlPackage.CONNECTION_DEFINITION__TARGET:
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
            case SysmlPackage.CONNECTION_DEFINITION__IS_IMPLIED:
                this.setIsImplied(IS_IMPLIED_EDEFAULT);
                return;
            case SysmlPackage.CONNECTION_DEFINITION__OWNED_RELATED_ELEMENT:
                this.getOwnedRelatedElement().clear();
                return;
            case SysmlPackage.CONNECTION_DEFINITION__OWNING_RELATED_ELEMENT:
                this.setOwningRelatedElement((Element) null);
                return;
            case SysmlPackage.CONNECTION_DEFINITION__SOURCE:
                this.getSource().clear();
                return;
            case SysmlPackage.CONNECTION_DEFINITION__TARGET:
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
            case SysmlPackage.CONNECTION_DEFINITION__IS_IMPLIED:
                return this.isImplied != IS_IMPLIED_EDEFAULT;
            case SysmlPackage.CONNECTION_DEFINITION__OWNED_RELATED_ELEMENT:
                return this.ownedRelatedElement != null && !this.ownedRelatedElement.isEmpty();
            case SysmlPackage.CONNECTION_DEFINITION__OWNING_RELATED_ELEMENT:
                return this.getOwningRelatedElement() != null;
            case SysmlPackage.CONNECTION_DEFINITION__RELATED_ELEMENT:
                return !this.getRelatedElement().isEmpty();
            case SysmlPackage.CONNECTION_DEFINITION__SOURCE:
                return this.source != null && !this.source.isEmpty();
            case SysmlPackage.CONNECTION_DEFINITION__TARGET:
                return this.target != null && !this.target.isEmpty();
            case SysmlPackage.CONNECTION_DEFINITION__ASSOCIATION_END:
                return !this.getAssociationEnd().isEmpty();
            case SysmlPackage.CONNECTION_DEFINITION__RELATED_TYPE:
                return !this.getRelatedType().isEmpty();
            case SysmlPackage.CONNECTION_DEFINITION__SOURCE_TYPE:
                return this.basicGetSourceType() != null;
            case SysmlPackage.CONNECTION_DEFINITION__TARGET_TYPE:
                return !this.getTargetType().isEmpty();
            case SysmlPackage.CONNECTION_DEFINITION__CONNECTION_END:
                return !this.getConnectionEnd().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == Relationship.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.CONNECTION_DEFINITION__IS_IMPLIED:
                    return SysmlPackage.RELATIONSHIP__IS_IMPLIED;
                case SysmlPackage.CONNECTION_DEFINITION__OWNED_RELATED_ELEMENT:
                    return SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT;
                case SysmlPackage.CONNECTION_DEFINITION__OWNING_RELATED_ELEMENT:
                    return SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT;
                case SysmlPackage.CONNECTION_DEFINITION__RELATED_ELEMENT:
                    return SysmlPackage.RELATIONSHIP__RELATED_ELEMENT;
                case SysmlPackage.CONNECTION_DEFINITION__SOURCE:
                    return SysmlPackage.RELATIONSHIP__SOURCE;
                case SysmlPackage.CONNECTION_DEFINITION__TARGET:
                    return SysmlPackage.RELATIONSHIP__TARGET;
                default:
                    return -1;
            }
        }
        if (baseClass == Association.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.CONNECTION_DEFINITION__ASSOCIATION_END:
                    return SysmlPackage.ASSOCIATION__ASSOCIATION_END;
                case SysmlPackage.CONNECTION_DEFINITION__RELATED_TYPE:
                    return SysmlPackage.ASSOCIATION__RELATED_TYPE;
                case SysmlPackage.CONNECTION_DEFINITION__SOURCE_TYPE:
                    return SysmlPackage.ASSOCIATION__SOURCE_TYPE;
                case SysmlPackage.CONNECTION_DEFINITION__TARGET_TYPE:
                    return SysmlPackage.ASSOCIATION__TARGET_TYPE;
                default:
                    return -1;
            }
        }
        if (baseClass == AssociationStructure.class) {
            switch (derivedFeatureID) {
                default:
                    return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == Relationship.class) {
            switch (baseFeatureID) {
                case SysmlPackage.RELATIONSHIP__IS_IMPLIED:
                    return SysmlPackage.CONNECTION_DEFINITION__IS_IMPLIED;
                case SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT:
                    return SysmlPackage.CONNECTION_DEFINITION__OWNED_RELATED_ELEMENT;
                case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                    return SysmlPackage.CONNECTION_DEFINITION__OWNING_RELATED_ELEMENT;
                case SysmlPackage.RELATIONSHIP__RELATED_ELEMENT:
                    return SysmlPackage.CONNECTION_DEFINITION__RELATED_ELEMENT;
                case SysmlPackage.RELATIONSHIP__SOURCE:
                    return SysmlPackage.CONNECTION_DEFINITION__SOURCE;
                case SysmlPackage.RELATIONSHIP__TARGET:
                    return SysmlPackage.CONNECTION_DEFINITION__TARGET;
                default:
                    return -1;
            }
        }
        if (baseClass == Association.class) {
            switch (baseFeatureID) {
                case SysmlPackage.ASSOCIATION__ASSOCIATION_END:
                    return SysmlPackage.CONNECTION_DEFINITION__ASSOCIATION_END;
                case SysmlPackage.ASSOCIATION__RELATED_TYPE:
                    return SysmlPackage.CONNECTION_DEFINITION__RELATED_TYPE;
                case SysmlPackage.ASSOCIATION__SOURCE_TYPE:
                    return SysmlPackage.CONNECTION_DEFINITION__SOURCE_TYPE;
                case SysmlPackage.ASSOCIATION__TARGET_TYPE:
                    return SysmlPackage.CONNECTION_DEFINITION__TARGET_TYPE;
                default:
                    return -1;
            }
        }
        if (baseClass == AssociationStructure.class) {
            switch (baseFeatureID) {
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (this.eIsProxy())
            return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (isImplied: ");
        result.append(this.isImplied);
        result.append(')');
        return result.toString();
    }

} // ConnectionDefinitionImpl
