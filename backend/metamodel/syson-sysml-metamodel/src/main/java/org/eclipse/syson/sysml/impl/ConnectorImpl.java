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
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.syson.sysml.Association;
import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Connector</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectorImpl#isIsImplied <em>Is Implied</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectorImpl#getOwnedRelatedElement <em>Owned Related Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectorImpl#getOwningRelatedElement <em>Owning Related Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectorImpl#getRelatedElement <em>Related Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectorImpl#getSource <em>Source</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectorImpl#getTarget <em>Target</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectorImpl#getAssociation <em>Association</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectorImpl#getConnectorEnd <em>Connector End</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectorImpl#getRelatedFeature <em>Related Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectorImpl#getSourceFeature <em>Source Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectorImpl#getTargetFeature <em>Target Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConnectorImpl extends FeatureImpl implements Connector {
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
    protected ConnectorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getConnector();
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
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.CONNECTOR__IS_IMPLIED, oldIsImplied, this.isImplied));
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
            this.ownedRelatedElement = new EObjectContainmentWithInverseEList<>(Element.class, this, SysmlPackage.CONNECTOR__OWNED_RELATED_ELEMENT, SysmlPackage.ELEMENT__OWNING_RELATIONSHIP);
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
        if (this.eContainerFeatureID() != SysmlPackage.CONNECTOR__OWNING_RELATED_ELEMENT) {
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
        msgs = this.eBasicSetContainer((InternalEObject) newOwningRelatedElement, SysmlPackage.CONNECTOR__OWNING_RELATED_ELEMENT, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setOwningRelatedElement(Element newOwningRelatedElement) {
        if (newOwningRelatedElement != this.eInternalContainer() || (this.eContainerFeatureID() != SysmlPackage.CONNECTOR__OWNING_RELATED_ELEMENT && newOwningRelatedElement != null)) {
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
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.CONNECTOR__OWNING_RELATED_ELEMENT, newOwningRelatedElement, newOwningRelatedElement));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Association> getAssociation() {
        List<Association> associations = new ArrayList<>();
        super.getType().stream()
                .filter(Association.class::isInstance)
                .map(Association.class::cast)
                .forEach(associations::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getConnector_Association(), associations.size(), associations.toArray());
    }

    /**
     * <!-- begin-user-doc --> Value The endFeatures of a Connector, which redefine the endFeatures of the associations
     * of the Connector. The connectorEnds determine via ReferenceSubsetting Relationships which Features are related by
     * the Connector. <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getConnectorEnd() {
        List<Element> endFeatures = this.getOwnedRelationship().stream()
                .filter(EndFeatureMembership.class::isInstance)
                .map(EndFeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .toList();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getConnector_ConnectorEnd(), endFeatures.size(), endFeatures.toArray());
    }

    /**
     * <!-- begin-user-doc --> The Features that are related by this Connector considered as a Relationship and that
     * restrict the links it identifies, given by the referenced Features of the connectorEnds of the Connector.
     *
     * <pre>
     * {@code relatedFeature = connectorEnd.ownedReferenceSubsetting
     *  ->select(s | s <> null).subsettedFeature}
     * </pre>
     *
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getRelatedFeature() {
        List<Feature> data = this.getConnectorEnd().stream()
                .map(Feature::getOwnedReferenceSubsetting)
                .filter(Objects::nonNull)
                .map(ReferenceSubsetting::getSubsettedFeature)
                .toList();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getConnector_RelatedFeature(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> The source relatedFeature for this Connector. It is the first relatedFeature.
     *
     * <pre>
     * {@code sourceFeature = if relatedFeature->isEmpty() then null
     *  else relatedFeature->first() endif}
     * </pre>
     *
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getSourceFeature() {
        Feature sourceFeature = this.basicGetSourceFeature();
        return sourceFeature != null && sourceFeature.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) sourceFeature) : sourceFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Feature basicGetSourceFeature() {
        EList<Feature> feature = this.getRelatedFeature();
        if (!feature.isEmpty()) {
            return feature.get(0);
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getTargetFeature() {
        List<Feature> feature = this.getRelatedFeature();
        List<Feature> data = new ArrayList<>();
        if (feature.size() > 1) {
            data = feature.subList(1, feature.size());
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getConnector_TargetFeature(), data.size(), data.toArray());
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
            case SysmlPackage.CONNECTOR__OWNED_RELATED_ELEMENT:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) this.getOwnedRelatedElement()).basicAdd(otherEnd, msgs);
            case SysmlPackage.CONNECTOR__OWNING_RELATED_ELEMENT:
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
            case SysmlPackage.CONNECTOR__OWNED_RELATED_ELEMENT:
                return ((InternalEList<?>) this.getOwnedRelatedElement()).basicRemove(otherEnd, msgs);
            case SysmlPackage.CONNECTOR__OWNING_RELATED_ELEMENT:
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
            case SysmlPackage.CONNECTOR__OWNING_RELATED_ELEMENT:
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
            case SysmlPackage.CONNECTOR__IS_IMPLIED:
                return this.isIsImplied();
            case SysmlPackage.CONNECTOR__OWNED_RELATED_ELEMENT:
                return this.getOwnedRelatedElement();
            case SysmlPackage.CONNECTOR__OWNING_RELATED_ELEMENT:
                return this.getOwningRelatedElement();
            case SysmlPackage.CONNECTOR__RELATED_ELEMENT:
                return this.getRelatedElement();
            case SysmlPackage.CONNECTOR__SOURCE:
                return this.getSource();
            case SysmlPackage.CONNECTOR__TARGET:
                return this.getTarget();
            case SysmlPackage.CONNECTOR__ASSOCIATION:
                return this.getAssociation();
            case SysmlPackage.CONNECTOR__CONNECTOR_END:
                return this.getConnectorEnd();
            case SysmlPackage.CONNECTOR__RELATED_FEATURE:
                return this.getRelatedFeature();
            case SysmlPackage.CONNECTOR__SOURCE_FEATURE:
                if (resolve) {
                    return this.getSourceFeature();
                }
                return this.basicGetSourceFeature();
            case SysmlPackage.CONNECTOR__TARGET_FEATURE:
                return this.getTargetFeature();
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
            case SysmlPackage.CONNECTOR__IS_IMPLIED:
                this.setIsImplied((Boolean) newValue);
                return;
            case SysmlPackage.CONNECTOR__OWNED_RELATED_ELEMENT:
                this.getOwnedRelatedElement().clear();
                this.getOwnedRelatedElement().addAll((Collection<? extends Element>) newValue);
                return;
            case SysmlPackage.CONNECTOR__OWNING_RELATED_ELEMENT:
                this.setOwningRelatedElement((Element) newValue);
                return;
            case SysmlPackage.CONNECTOR__SOURCE:
                this.getSource().clear();
                this.getSource().addAll((Collection<? extends Element>) newValue);
                return;
            case SysmlPackage.CONNECTOR__TARGET:
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
            case SysmlPackage.CONNECTOR__IS_IMPLIED:
                this.setIsImplied(IS_IMPLIED_EDEFAULT);
                return;
            case SysmlPackage.CONNECTOR__OWNED_RELATED_ELEMENT:
                this.getOwnedRelatedElement().clear();
                return;
            case SysmlPackage.CONNECTOR__OWNING_RELATED_ELEMENT:
                this.setOwningRelatedElement((Element) null);
                return;
            case SysmlPackage.CONNECTOR__SOURCE:
                this.getSource().clear();
                return;
            case SysmlPackage.CONNECTOR__TARGET:
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
            case SysmlPackage.CONNECTOR__IS_IMPLIED:
                return this.isImplied != IS_IMPLIED_EDEFAULT;
            case SysmlPackage.CONNECTOR__OWNED_RELATED_ELEMENT:
                return this.ownedRelatedElement != null && !this.ownedRelatedElement.isEmpty();
            case SysmlPackage.CONNECTOR__OWNING_RELATED_ELEMENT:
                return this.getOwningRelatedElement() != null;
            case SysmlPackage.CONNECTOR__RELATED_ELEMENT:
                return !this.getRelatedElement().isEmpty();
            case SysmlPackage.CONNECTOR__SOURCE:
                return this.source != null && !this.source.isEmpty();
            case SysmlPackage.CONNECTOR__TARGET:
                return this.target != null && !this.target.isEmpty();
            case SysmlPackage.CONNECTOR__ASSOCIATION:
                return !this.getAssociation().isEmpty();
            case SysmlPackage.CONNECTOR__CONNECTOR_END:
                return !this.getConnectorEnd().isEmpty();
            case SysmlPackage.CONNECTOR__RELATED_FEATURE:
                return !this.getRelatedFeature().isEmpty();
            case SysmlPackage.CONNECTOR__SOURCE_FEATURE:
                return this.basicGetSourceFeature() != null;
            case SysmlPackage.CONNECTOR__TARGET_FEATURE:
                return !this.getTargetFeature().isEmpty();
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
                case SysmlPackage.CONNECTOR__IS_IMPLIED:
                    return SysmlPackage.RELATIONSHIP__IS_IMPLIED;
                case SysmlPackage.CONNECTOR__OWNED_RELATED_ELEMENT:
                    return SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT;
                case SysmlPackage.CONNECTOR__OWNING_RELATED_ELEMENT:
                    return SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT;
                case SysmlPackage.CONNECTOR__RELATED_ELEMENT:
                    return SysmlPackage.RELATIONSHIP__RELATED_ELEMENT;
                case SysmlPackage.CONNECTOR__SOURCE:
                    return SysmlPackage.RELATIONSHIP__SOURCE;
                case SysmlPackage.CONNECTOR__TARGET:
                    return SysmlPackage.RELATIONSHIP__TARGET;
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
                    return SysmlPackage.CONNECTOR__IS_IMPLIED;
                case SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT:
                    return SysmlPackage.CONNECTOR__OWNED_RELATED_ELEMENT;
                case SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT:
                    return SysmlPackage.CONNECTOR__OWNING_RELATED_ELEMENT;
                case SysmlPackage.RELATIONSHIP__RELATED_ELEMENT:
                    return SysmlPackage.CONNECTOR__RELATED_ELEMENT;
                case SysmlPackage.RELATIONSHIP__SOURCE:
                    return SysmlPackage.CONNECTOR__SOURCE;
                case SysmlPackage.RELATIONSHIP__TARGET:
                    return SysmlPackage.CONNECTOR__TARGET;
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
        if (this.eIsProxy()) {
            return super.toString();
        }

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (isImplied: ");
        result.append(this.isImplied);
        result.append(')');
        return result.toString();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Type> getType() {
        EList<Type> types = new BasicEList<>();
        EList<Association> association = this.getAssociation();
        if (association != null) {
            types.addAll(association);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_Type(), types.size(), types.toArray());
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getEndFeature() {
        EList<Feature> endFeatures = new BasicEList<>();
        EList<Feature> connectorEnd = this.getConnectorEnd();
        if (connectorEnd != null) {
            endFeatures.addAll(connectorEnd);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_EndFeature(), endFeatures.size(), endFeatures.toArray());
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getRelatedElement() {
        EList<Element> relatedElements = new BasicEList<>();
        EList<Feature> relatedFeature = this.getRelatedFeature();
        if (relatedFeature != null) {
            relatedElements.addAll(relatedFeature);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRelationship_RelatedElement(), relatedElements.size(), relatedElements.toArray());
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getSource() {
        EList<Element> sources = new BasicEList<>();
        Feature sourceFeature = this.getSourceFeature();
        if (sourceFeature != null) {
            sources.add(sourceFeature);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRelationship_Source(), sources.size(), sources.toArray());
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getTarget() {
        EList<Element> targets = new BasicEList<>();
        EList<Feature> targetFeature = this.getTargetFeature();
        if (targetFeature != null) {
            targets.addAll(targetFeature);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRelationship_Target(), targets.size(), targets.toArray());
    }

} // ConnectorImpl
