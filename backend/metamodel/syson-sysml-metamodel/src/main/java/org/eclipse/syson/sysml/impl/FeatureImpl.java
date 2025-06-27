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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.CrossSubsetting;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureInverting;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.MetadataFeature;
import org.eclipse.syson.sysml.Multiplicity;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.TypeFeaturing;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Feature</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getDirection <em>Direction</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsComposite <em>Is Composite</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsDerived <em>Is Derived</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsEnd <em>Is End</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsNonunique <em>Is Nonunique</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsOrdered <em>Is Ordered</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsPortion <em>Is Portion</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsReadOnly <em>Is Read Only</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsUnique <em>Is Unique</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getChainingFeature <em>Chaining Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getCrossFeature <em>Cross Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getEndOwningType <em>End Owning Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getFeatureTarget <em>Feature Target</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getFeaturingType <em>Featuring Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwnedCrossSubsetting <em>Owned Cross Subsetting</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwnedFeatureChaining <em>Owned Feature Chaining</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwnedFeatureInverting <em>Owned Feature Inverting</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwnedRedefinition <em>Owned Redefinition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwnedReferenceSubsetting <em>Owned Reference
 * Subsetting</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwnedSubsetting <em>Owned Subsetting</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwnedTypeFeaturing <em>Owned Type Featuring</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwnedTyping <em>Owned Typing</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwningFeatureMembership <em>Owning Feature
 * Membership</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwningType <em>Owning Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getType <em>Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getValuation <em>Valuation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureImpl extends TypeImpl implements Feature {
    /**
     * The default value of the '{@link #getDirection() <em>Direction</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getDirection()
     * @generated NOT
     * @ordered
     */
    protected static final FeatureDirectionKind DIRECTION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDirection() <em>Direction</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getDirection()
     * @generated
     * @ordered
     */
    protected FeatureDirectionKind direction = DIRECTION_EDEFAULT;

    /**
     * This is true if the Direction attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    protected boolean directionESet;

    /**
     * The default value of the '{@link #isIsComposite() <em>Is Composite</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsComposite()
     * @generated
     * @ordered
     */
    protected static final boolean IS_COMPOSITE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsComposite() <em>Is Composite</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsComposite()
     * @generated
     * @ordered
     */
    protected boolean isComposite = IS_COMPOSITE_EDEFAULT;

    /**
     * The default value of the '{@link #isIsDerived() <em>Is Derived</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsDerived()
     * @generated
     * @ordered
     */
    protected static final boolean IS_DERIVED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsDerived() <em>Is Derived</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsDerived()
     * @generated
     * @ordered
     */
    protected boolean isDerived = IS_DERIVED_EDEFAULT;

    /**
     * The default value of the '{@link #isIsEnd() <em>Is End</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsEnd()
     * @generated
     * @ordered
     */
    protected static final boolean IS_END_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsEnd() <em>Is End</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @see #isIsEnd()
     * @generated
     * @ordered
     */
    protected boolean isEnd = IS_END_EDEFAULT;

    /**
     * The default value of the '{@link #isIsNonunique() <em>Is Nonunique</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsNonunique()
     * @generated
     * @ordered
     */
    protected static final boolean IS_NONUNIQUE_EDEFAULT = false;

    /**
     * The default value of the '{@link #isIsOrdered() <em>Is Ordered</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsOrdered()
     * @generated
     * @ordered
     */
    protected static final boolean IS_ORDERED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsOrdered() <em>Is Ordered</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsOrdered()
     * @generated
     * @ordered
     */
    protected boolean isOrdered = IS_ORDERED_EDEFAULT;

    /**
     * The default value of the '{@link #isIsPortion() <em>Is Portion</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsPortion()
     * @generated
     * @ordered
     */
    protected static final boolean IS_PORTION_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsPortion() <em>Is Portion</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsPortion()
     * @generated
     * @ordered
     */
    protected boolean isPortion = IS_PORTION_EDEFAULT;

    /**
     * The default value of the '{@link #isIsReadOnly() <em>Is Read Only</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsReadOnly()
     * @generated
     * @ordered
     */
    protected static final boolean IS_READ_ONLY_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsReadOnly() <em>Is Read Only</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsReadOnly()
     * @generated
     * @ordered
     */
    protected boolean isReadOnly = IS_READ_ONLY_EDEFAULT;

    /**
     * The default value of the '{@link #isIsUnique() <em>Is Unique</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsUnique()
     * @generated
     * @ordered
     */
    protected static final boolean IS_UNIQUE_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isIsUnique() <em>Is Unique</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsUnique()
     * @generated
     * @ordered
     */
    protected boolean isUnique = IS_UNIQUE_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected FeatureImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFeature();
    }

    /**
     * @generated NOT
     */
    @Override
    public EList<Feature> getChainingFeature() {
        List<Feature> chainingFeatures = this.getOwnedFeatureChaining().stream()
                .map(FeatureChaining::getChainingFeature)
                .filter(Objects::nonNull)
                .toList();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_ChainingFeature(), chainingFeatures.size(), chainingFeatures.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getCrossFeature() {
        Feature crossFeature = this.basicGetCrossFeature();
        return crossFeature != null && crossFeature.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) crossFeature) : crossFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Feature basicGetCrossFeature() {
        var ownedCrossSubsetting = this.getOwnedCrossSubsetting();
        if (ownedCrossSubsetting != null) {
            var crossedFeature = ownedCrossSubsetting.getCrossedFeature();
            if (crossedFeature != null) {
                var chainingFeature = crossedFeature.getChainingFeature();
                if (chainingFeature.size() >= 2) {
                    return chainingFeature.get(1);
                }
            }
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureDirectionKind getDirection() {
        return this.direction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setDirection(FeatureDirectionKind newDirection) {
        FeatureDirectionKind oldDirection = this.direction;
        this.direction = newDirection == null ? DIRECTION_EDEFAULT : newDirection;
        boolean oldDirectionESet = this.directionESet;
        this.directionESet = true;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__DIRECTION, oldDirection, this.direction, !oldDirectionESet));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void unsetDirection() {
        FeatureDirectionKind oldDirection = this.direction;
        boolean oldDirectionESet = this.directionESet;
        this.direction = DIRECTION_EDEFAULT;
        this.directionESet = false;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.UNSET, SysmlPackage.FEATURE__DIRECTION, oldDirection, DIRECTION_EDEFAULT, oldDirectionESet));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isSetDirection() {
        return this.directionESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getEndOwningType() {
        Type endOwningType = this.basicGetEndOwningType();
        return endOwningType != null && endOwningType.eIsProxy() ? (Type) this.eResolveProxy((InternalEObject) endOwningType) : endOwningType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetEndOwningType() {
        // TODO: implement this method to return the 'End Owning Type' reference
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
    public Feature getFeatureTarget() {
        Feature featureTarget = this.basicGetFeatureTarget();
        return featureTarget != null && featureTarget.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) featureTarget) : featureTarget;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Feature basicGetFeatureTarget() {
        var chainingFeature = this.getChainingFeature();
        if (chainingFeature.isEmpty()) {
            return this;
        }
        return chainingFeature.get(chainingFeature.size() - 1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Type> getFeaturingType() {
        List<Type> featuringTypes = new LinkedList<>();
        this.getOwnedTypeFeaturing().stream()
                .map(TypeFeaturing::getFeaturingType)
                .forEach(featuringTypes::add);
        var chainingFeature = this.getChainingFeature();
        if (!chainingFeature.isEmpty()) {
            featuringTypes.addAll(chainingFeature.get(0).getFeaturingType());
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_FeaturingType(), featuringTypes.size(), featuringTypes.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CrossSubsetting getOwnedCrossSubsetting() {
        CrossSubsetting ownedCrossSubsetting = this.basicGetOwnedCrossSubsetting();
        return ownedCrossSubsetting != null && ownedCrossSubsetting.eIsProxy() ? (CrossSubsetting) this.eResolveProxy((InternalEObject) ownedCrossSubsetting) : ownedCrossSubsetting;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public CrossSubsetting basicGetOwnedCrossSubsetting() {
        return this.getOwnedSubsetting().stream()
                .filter(CrossSubsetting.class::isInstance)
                .map(CrossSubsetting.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsComposite() {
        return this.isComposite;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsComposite(boolean newIsComposite) {
        boolean oldIsComposite = this.isComposite;
        this.isComposite = newIsComposite;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__IS_COMPOSITE, oldIsComposite, this.isComposite));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsDerived() {
        return this.isDerived;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsDerived(boolean newIsDerived) {
        boolean oldIsDerived = this.isDerived;
        this.isDerived = newIsDerived;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__IS_DERIVED, oldIsDerived, this.isDerived));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsEnd() {
        return this.isEnd;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsEnd(boolean newIsEnd) {
        boolean oldIsEnd = this.isEnd;
        this.isEnd = newIsEnd;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__IS_END, oldIsEnd, this.isEnd));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isIsNonunique() {
        return !this.isIsUnique();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsOrdered() {
        return this.isOrdered;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsOrdered(boolean newIsOrdered) {
        boolean oldIsOrdered = this.isOrdered;
        this.isOrdered = newIsOrdered;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__IS_ORDERED, oldIsOrdered, this.isOrdered));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsPortion() {
        return this.isPortion;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsPortion(boolean newIsPortion) {
        boolean oldIsPortion = this.isPortion;
        this.isPortion = newIsPortion;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__IS_PORTION, oldIsPortion, this.isPortion));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsReadOnly() {
        return this.isReadOnly;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsReadOnly(boolean newIsReadOnly) {
        boolean oldIsReadOnly = this.isReadOnly;
        this.isReadOnly = newIsReadOnly;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__IS_READ_ONLY, oldIsReadOnly, this.isReadOnly));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsUnique() {
        return this.isUnique;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsUnique(boolean newIsUnique) {
        boolean oldIsUnique = this.isUnique;
        this.isUnique = newIsUnique;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__IS_UNIQUE, oldIsUnique, this.isUnique));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<FeatureChaining> getOwnedFeatureChaining() {
        List<FeatureChaining> ownedFeatureChainings = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureChaining.class::isInstance)
                .map(FeatureChaining.class::cast)
                .forEach(ownedFeatureChainings::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_OwnedFeatureChaining(), ownedFeatureChainings.size(), ownedFeatureChainings.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<FeatureInverting> getOwnedFeatureInverting() {
        List<FeatureInverting> ownedFeatureInvertings = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureInverting.class::isInstance)
                .map(FeatureInverting.class::cast)
                .filter(fi -> Objects.equals(this, fi.getFeatureInverted()))
                .forEach(ownedFeatureInvertings::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_OwnedFeatureInverting(), ownedFeatureInvertings.size(), ownedFeatureInvertings.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Redefinition> getOwnedRedefinition() {
        List<Redefinition> ownedRedefinitions = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(Redefinition.class::isInstance)
                .map(Redefinition.class::cast)
                .forEach(ownedRedefinitions::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_OwnedRedefinition(), ownedRedefinitions.size(), ownedRedefinitions.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ReferenceSubsetting getOwnedReferenceSubsetting() {
        ReferenceSubsetting ownedReferenceSubsetting = this.basicGetOwnedReferenceSubsetting();
        return ownedReferenceSubsetting != null && ownedReferenceSubsetting.eIsProxy() ? (ReferenceSubsetting) this.eResolveProxy((InternalEObject) ownedReferenceSubsetting)
                : ownedReferenceSubsetting;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public ReferenceSubsetting basicGetOwnedReferenceSubsetting() {
        return this.getOwnedSpecialization().stream()
                .filter(ReferenceSubsetting.class::isInstance)
                .map(ReferenceSubsetting.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Subsetting> getOwnedSubsetting() {
        Subsetting[] subSettings = this.getOwnedSpecialization().stream()
                .filter(Subsetting.class::isInstance)
                .map(Subsetting.class::cast)
                .toArray(Subsetting[]::new);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_OwnedSubsetting(), subSettings.length, subSettings);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<TypeFeaturing> getOwnedTypeFeaturing() {
        List<TypeFeaturing> ownedTypeFeaturings = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(TypeFeaturing.class::isInstance)
                .map(TypeFeaturing.class::cast)
                .filter(tf -> Objects.equals(this, tf.getFeatureOfType()))
                .forEach(ownedTypeFeaturings::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_OwnedTypeFeaturing(), ownedTypeFeaturings.size(), ownedTypeFeaturings.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<FeatureTyping> getOwnedTyping() {
        List<FeatureTyping> ownedTypings = new ArrayList<>();
        // The ownedSpecializations of this Feature that are FeatureTypings, for which the Feature is the typedFeature.
        this.getOwnedSpecialization().stream()
                .filter(FeatureTyping.class::isInstance)
                .map(FeatureTyping.class::cast)
                .filter(ft -> this.equals(ft.getTypedFeature()))
                .forEach(ownedTypings::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_OwnedTyping(), ownedTypings.size(), ownedTypings.toArray());
    }

    /**
     * <!-- begin-user-doc --> The FeatureMembership that owns this Feature as an ownedMemberFeature, determining its
     * owningType. <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMembership getOwningFeatureMembership() {
        FeatureMembership owningFeatureMembership = this.basicGetOwningFeatureMembership();
        return owningFeatureMembership != null && owningFeatureMembership.eIsProxy() ? (FeatureMembership) this.eResolveProxy((InternalEObject) owningFeatureMembership) : owningFeatureMembership;
    }

    /**
     * <!-- begin-user-doc --> The FeatureMembership that owns this Feature as an ownedMemberFeature, determining its
     * owningType. <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public FeatureMembership basicGetOwningFeatureMembership() {
        OwningMembership owningMembership = this.getOwningMembership();
        if (owningMembership instanceof FeatureMembership featureMembershipt) {
            return featureMembershipt;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> The Type that is the owningType of the owningFeatureMembership of this Feature. <!--
     * end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getOwningType() {
        Type owningType = this.basicGetOwningType();
        return owningType != null && owningType.eIsProxy() ? (Type) this.eResolveProxy((InternalEObject) owningType) : owningType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Type basicGetOwningType() {
        FeatureMembership owningFeatureMembership = this.getOwningFeatureMembership();
        if (owningFeatureMembership != null) {
            return owningFeatureMembership.getOwningType();
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> Types that restrict the values of this Feature, such that the values must be instances of
     * all the types. The types of a Feature are derived from its typings and the types of its subsettings. If the
     * Feature is chained, then the types of the last Feature in the chain are also types of the chained Feature.<!--
     * end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Type> getType() {
        List<Type> types = new ArrayList<>();
        this.getOwnedSpecialization().stream()
                .filter(FeatureTyping.class::isInstance)
                .map(FeatureTyping.class::cast)
                .map(typing -> typing.getType())
                .forEach(types::add);

        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_Type(), types.size(), types.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureValue getValuation() {
        FeatureValue valuation = this.basicGetValuation();
        return valuation != null && valuation.eIsProxy() ? (FeatureValue) this.eResolveProxy((InternalEObject) valuation) : valuation;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> allRedefinedFeatures() {
        EList<Feature> allRedefinedFeatures = new BasicEList<>();
        allRedefinedFeatures.add(this);
        List<Feature> accu = new LinkedList<>();
        this.allRedefinedFeatures(this, accu);
        allRedefinedFeatures.addAll(accu);
        return allRedefinedFeatures;
    }

    private void allRedefinedFeatures(Feature currentFeature, List<Feature> accu) {
        EList<Redefinition> ownedRedefinition = currentFeature.getOwnedRedefinition();
        for (Redefinition redefinition : ownedRedefinition) {
            Feature redefinedFeature = redefinition.getRedefinedFeature();
            if (redefinedFeature != null && !accu.contains(redefinedFeature)) {
                accu.add(redefinedFeature);
                this.allRedefinedFeatures(redefinedFeature, accu);
            }
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Type> asCartesianProduct() {
        EList<Type> cartesianProduct = new BasicEList<>();

        this.getFeaturingType().stream()
                .filter(t -> !this.equals(t.getOwner()))
                .forEach(cartesianProduct::add);

        this.getFeaturingType().stream()
                .filter(t -> this.equals(t.getOwner()))
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .map(f -> f.asCartesianProduct())
                .forEach(cartesianProduct::addAll);

        cartesianProduct.addAll(this.getType());

        cartesianProduct.removeIf(Objects::isNull);

        return cartesianProduct;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public FeatureValue basicGetValuation() {
        return this.getOwnedMembership().stream()
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public FeatureDirectionKind directionFor(Type type) {
        if (type != null) {
            return type.directionOf(this);
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isCartesianProduct() {
        boolean onlyOneType = this.getType().size() == 1;
        if (!onlyOneType) {
            return false;
        }
        boolean onlyOneFeaturingType = this.getFeaturingType().size() == 1;
        if (!onlyOneFeaturingType) {
            return false;
        }
        var firstFeaturingType = this.getFeaturingType().get(0);
        boolean firstFeaturingTypeOwnerEqualsSelf = Objects.equals(this, firstFeaturingType.getOwner());
        boolean firstFeaturingFeatureIsCartesionProduct = false;

        if (firstFeaturingType instanceof Feature firstFeaturingFeature) {
            firstFeaturingFeatureIsCartesionProduct = firstFeaturingFeature.isCartesianProduct();
        }

        return (!firstFeaturingTypeOwnerEqualsSelf) || firstFeaturingFeatureIsCartesionProduct;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isFeaturedWithin(Type type) {
        if (type == null) {
            return this.getFeaturingType().stream().allMatch(f -> Objects.equals(f, this.resolveGlobal("Base::Anything").getMemberElement()));
        }
        boolean isCompatibleWithAllTypes = this.getFeaturingType().stream().allMatch(f -> type.specializes(f));
        // isVariable does not exists so use false instead
        boolean isVariableAndCompatible = false && type.specializes(this.getOwningType());
        boolean isChainingFeatureVariable = false;
        if (!this.getChainingFeature().isEmpty()) {
            // isVariable does not exists so use false instead
            isChainingFeatureVariable = false && type.specializes(this.getChainingFeature().get(0).getOwningType());
        }
        return isCompatibleWithAllTypes || isVariableAndCompatible || isChainingFeatureVariable;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isOwnedCrossFeature() {
        if (this.getOwningNamespace() instanceof Feature f) {
            return Objects.equals(this, f.ownedCrossFeature());
        }
        return false;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Feature namingFeature() {
        return this.getOwnedRedefinition().stream()
                .findFirst()
                .map(Redefinition::getRedefinedFeature)
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Feature ownedCrossFeature() {
        if (!this.isIsEnd() || this.getOwningType() == null) {
            return null;
        }
        List<Feature> ownedMemberFeatures = new ArrayList<>();
        this.getOwnedMember().stream()
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .filter(f -> !(f instanceof Multiplicity) && !(f instanceof MetadataFeature) && !(f instanceof FeatureValue))
                .filter(f -> !(f.getOwningMembership() instanceof FeatureMembership))
                .forEach(ownedMemberFeatures::add);
        Feature ownedCrossFeature = null;
        if (!ownedMemberFeatures.isEmpty()) {
            ownedCrossFeature = ownedMemberFeatures.get(0);
        }
        return ownedCrossFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean redefines(Feature redefinedFeature) {
        return this.getOwnedRedefinition().stream()
                .map(Redefinition::getRedefinedFeature).toList()
                .contains(redefinedFeature);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean redefinesFromLibrary(String libraryFeatureName) {
        var mem = this.resolveGlobal(libraryFeatureName);
        if (mem != null && mem.getMemberElement() instanceof Feature f) {
            return this.redefines(f);
        }
        return false;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean subsetsChain(Feature first, Feature second) {
        return this.allSupertypes().stream()
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .anyMatch(f -> {
                    var chainingFeature = f.getChainingFeature();
                    int n = chainingFeature.size();
                    if (n >= 2) {
                        return Objects.equals(chainingFeature.get(n - 1), first) && Objects.equals(chainingFeature.get(n), second);
                    }
                    return false;
                });
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> typingFeatures() {
        EList<Feature> typingFeatures = new BasicEList<>();
        if (!this.isIsConjugated()) {
            var subsettedFeatures = this.getOwnedSubsetting().stream()
                    .filter(s -> !(s instanceof CrossSubsetting))
                    .map(Subsetting::getSubsettedFeature)
                    .toList();
            var chainingFeature = this.getChainingFeature();
            if (chainingFeature.isEmpty() || subsettedFeatures.contains(chainingFeature.get(chainingFeature.size() - 1))) {
                typingFeatures.addAll(subsettedFeatures);
            } else {
                subsettedFeatures.add(chainingFeature.get(chainingFeature.size() - 1));
                typingFeatures.addAll(subsettedFeatures);
            }
        } else {
            var conjugator = this.getOwnedConjugator();
            if (conjugator != null && conjugator.getOriginalType() instanceof Feature originalType) {
                typingFeatures.add(originalType);
            }
        }
        return typingFeatures;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.FEATURE__DIRECTION:
                return this.getDirection();
            case SysmlPackage.FEATURE__IS_COMPOSITE:
                return this.isIsComposite();
            case SysmlPackage.FEATURE__IS_DERIVED:
                return this.isIsDerived();
            case SysmlPackage.FEATURE__IS_END:
                return this.isIsEnd();
            case SysmlPackage.FEATURE__IS_NONUNIQUE:
                return this.isIsNonunique();
            case SysmlPackage.FEATURE__IS_ORDERED:
                return this.isIsOrdered();
            case SysmlPackage.FEATURE__IS_PORTION:
                return this.isIsPortion();
            case SysmlPackage.FEATURE__IS_READ_ONLY:
                return this.isIsReadOnly();
            case SysmlPackage.FEATURE__IS_UNIQUE:
                return this.isIsUnique();
            case SysmlPackage.FEATURE__CHAINING_FEATURE:
                return this.getChainingFeature();
            case SysmlPackage.FEATURE__CROSS_FEATURE:
                if (resolve) {
                    return this.getCrossFeature();
                }
                return this.basicGetCrossFeature();
            case SysmlPackage.FEATURE__END_OWNING_TYPE:
                if (resolve) {
                    return this.getEndOwningType();
                }
                return this.basicGetEndOwningType();
            case SysmlPackage.FEATURE__FEATURE_TARGET:
                if (resolve) {
                    return this.getFeatureTarget();
                }
                return this.basicGetFeatureTarget();
            case SysmlPackage.FEATURE__FEATURING_TYPE:
                return this.getFeaturingType();
            case SysmlPackage.FEATURE__OWNED_CROSS_SUBSETTING:
                if (resolve) {
                    return this.getOwnedCrossSubsetting();
                }
                return this.basicGetOwnedCrossSubsetting();
            case SysmlPackage.FEATURE__OWNED_FEATURE_CHAINING:
                return this.getOwnedFeatureChaining();
            case SysmlPackage.FEATURE__OWNED_FEATURE_INVERTING:
                return this.getOwnedFeatureInverting();
            case SysmlPackage.FEATURE__OWNED_REDEFINITION:
                return this.getOwnedRedefinition();
            case SysmlPackage.FEATURE__OWNED_REFERENCE_SUBSETTING:
                if (resolve) {
                    return this.getOwnedReferenceSubsetting();
                }
                return this.basicGetOwnedReferenceSubsetting();
            case SysmlPackage.FEATURE__OWNED_SUBSETTING:
                return this.getOwnedSubsetting();
            case SysmlPackage.FEATURE__OWNED_TYPE_FEATURING:
                return this.getOwnedTypeFeaturing();
            case SysmlPackage.FEATURE__OWNED_TYPING:
                return this.getOwnedTyping();
            case SysmlPackage.FEATURE__OWNING_FEATURE_MEMBERSHIP:
                if (resolve) {
                    return this.getOwningFeatureMembership();
                }
                return this.basicGetOwningFeatureMembership();
            case SysmlPackage.FEATURE__OWNING_TYPE:
                if (resolve) {
                    return this.getOwningType();
                }
                return this.basicGetOwningType();
            case SysmlPackage.FEATURE__TYPE:
                return this.getType();
            case SysmlPackage.FEATURE__VALUATION:
                if (resolve) {
                    return this.getValuation();
                }
                return this.basicGetValuation();
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
            case SysmlPackage.FEATURE__DIRECTION:
                this.setDirection((FeatureDirectionKind) newValue);
                return;
            case SysmlPackage.FEATURE__IS_COMPOSITE:
                this.setIsComposite((Boolean) newValue);
                return;
            case SysmlPackage.FEATURE__IS_DERIVED:
                this.setIsDerived((Boolean) newValue);
                return;
            case SysmlPackage.FEATURE__IS_END:
                this.setIsEnd((Boolean) newValue);
                return;
            case SysmlPackage.FEATURE__IS_ORDERED:
                this.setIsOrdered((Boolean) newValue);
                return;
            case SysmlPackage.FEATURE__IS_PORTION:
                this.setIsPortion((Boolean) newValue);
                return;
            case SysmlPackage.FEATURE__IS_READ_ONLY:
                this.setIsReadOnly((Boolean) newValue);
                return;
            case SysmlPackage.FEATURE__IS_UNIQUE:
                this.setIsUnique((Boolean) newValue);
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
            case SysmlPackage.FEATURE__DIRECTION:
                this.unsetDirection();
                return;
            case SysmlPackage.FEATURE__IS_COMPOSITE:
                this.setIsComposite(IS_COMPOSITE_EDEFAULT);
                return;
            case SysmlPackage.FEATURE__IS_DERIVED:
                this.setIsDerived(IS_DERIVED_EDEFAULT);
                return;
            case SysmlPackage.FEATURE__IS_END:
                this.setIsEnd(IS_END_EDEFAULT);
                return;
            case SysmlPackage.FEATURE__IS_ORDERED:
                this.setIsOrdered(IS_ORDERED_EDEFAULT);
                return;
            case SysmlPackage.FEATURE__IS_PORTION:
                this.setIsPortion(IS_PORTION_EDEFAULT);
                return;
            case SysmlPackage.FEATURE__IS_READ_ONLY:
                this.setIsReadOnly(IS_READ_ONLY_EDEFAULT);
                return;
            case SysmlPackage.FEATURE__IS_UNIQUE:
                this.setIsUnique(IS_UNIQUE_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * @generated NOT
     */
    @Override
    public String effectiveName() {
        String dName = this.getDeclaredName();
        String dShortName = this.getDeclaredShortName();
        final String effectiveName;
        if (dName != null || dShortName != null) {
            effectiveName = dName;
        } else {
            Feature namingFeature = this.namingFeature();
            if (namingFeature != null && namingFeature != this) {
                effectiveName = namingFeature.effectiveName();
            } else {
                effectiveName = null;
            }
        }
        return effectiveName;
    }

    /**
     * @generated NOT
     */
    @Override
    public String effectiveShortName() {
        String dName = this.getDeclaredName();
        String dShortName = this.getDeclaredShortName();
        final String effectiveName;
        if (dName != null || dShortName != null) {
            effectiveName = dShortName;
        } else {
            Feature namingFeature = this.namingFeature();
            if (namingFeature != null && namingFeature != this) {
                effectiveName = namingFeature.effectiveShortName();
            } else {
                effectiveName = null;
            }
        }
        return effectiveName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.FEATURE__DIRECTION:
                return this.isSetDirection();
            case SysmlPackage.FEATURE__IS_COMPOSITE:
                return this.isComposite != IS_COMPOSITE_EDEFAULT;
            case SysmlPackage.FEATURE__IS_DERIVED:
                return this.isDerived != IS_DERIVED_EDEFAULT;
            case SysmlPackage.FEATURE__IS_END:
                return this.isEnd != IS_END_EDEFAULT;
            case SysmlPackage.FEATURE__IS_NONUNIQUE:
                return this.isIsNonunique() != IS_NONUNIQUE_EDEFAULT;
            case SysmlPackage.FEATURE__IS_ORDERED:
                return this.isOrdered != IS_ORDERED_EDEFAULT;
            case SysmlPackage.FEATURE__IS_PORTION:
                return this.isPortion != IS_PORTION_EDEFAULT;
            case SysmlPackage.FEATURE__IS_READ_ONLY:
                return this.isReadOnly != IS_READ_ONLY_EDEFAULT;
            case SysmlPackage.FEATURE__IS_UNIQUE:
                return this.isUnique != IS_UNIQUE_EDEFAULT;
            case SysmlPackage.FEATURE__CHAINING_FEATURE:
                return !this.getChainingFeature().isEmpty();
            case SysmlPackage.FEATURE__CROSS_FEATURE:
                return this.basicGetCrossFeature() != null;
            case SysmlPackage.FEATURE__END_OWNING_TYPE:
                return this.basicGetEndOwningType() != null;
            case SysmlPackage.FEATURE__FEATURE_TARGET:
                return this.basicGetFeatureTarget() != null;
            case SysmlPackage.FEATURE__FEATURING_TYPE:
                return !this.getFeaturingType().isEmpty();
            case SysmlPackage.FEATURE__OWNED_CROSS_SUBSETTING:
                return this.basicGetOwnedCrossSubsetting() != null;
            case SysmlPackage.FEATURE__OWNED_FEATURE_CHAINING:
                return !this.getOwnedFeatureChaining().isEmpty();
            case SysmlPackage.FEATURE__OWNED_FEATURE_INVERTING:
                return !this.getOwnedFeatureInverting().isEmpty();
            case SysmlPackage.FEATURE__OWNED_REDEFINITION:
                return !this.getOwnedRedefinition().isEmpty();
            case SysmlPackage.FEATURE__OWNED_REFERENCE_SUBSETTING:
                return this.basicGetOwnedReferenceSubsetting() != null;
            case SysmlPackage.FEATURE__OWNED_SUBSETTING:
                return !this.getOwnedSubsetting().isEmpty();
            case SysmlPackage.FEATURE__OWNED_TYPE_FEATURING:
                return !this.getOwnedTypeFeaturing().isEmpty();
            case SysmlPackage.FEATURE__OWNED_TYPING:
                return !this.getOwnedTyping().isEmpty();
            case SysmlPackage.FEATURE__OWNING_FEATURE_MEMBERSHIP:
                return this.basicGetOwningFeatureMembership() != null;
            case SysmlPackage.FEATURE__OWNING_TYPE:
                return this.basicGetOwningType() != null;
            case SysmlPackage.FEATURE__TYPE:
                return !this.getType().isEmpty();
            case SysmlPackage.FEATURE__VALUATION:
                return this.basicGetValuation() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.FEATURE___ALL_REDEFINED_FEATURES:
                return this.allRedefinedFeatures();
            case SysmlPackage.FEATURE___AS_CARTESIAN_PRODUCT:
                return this.asCartesianProduct();
            case SysmlPackage.FEATURE___DIRECTION_FOR__TYPE:
                return this.directionFor((Type) arguments.get(0));
            case SysmlPackage.FEATURE___IS_CARTESIAN_PRODUCT:
                return this.isCartesianProduct();
            case SysmlPackage.FEATURE___IS_FEATURED_WITHIN__TYPE:
                return this.isFeaturedWithin((Type) arguments.get(0));
            case SysmlPackage.FEATURE___IS_OWNED_CROSS_FEATURE:
                return this.isOwnedCrossFeature();
            case SysmlPackage.FEATURE___NAMING_FEATURE:
                return this.namingFeature();
            case SysmlPackage.FEATURE___OWNED_CROSS_FEATURE:
                return this.ownedCrossFeature();
            case SysmlPackage.FEATURE___REDEFINES__FEATURE:
                return this.redefines((Feature) arguments.get(0));
            case SysmlPackage.FEATURE___REDEFINES_FROM_LIBRARY__STRING:
                return this.redefinesFromLibrary((String) arguments.get(0));
            case SysmlPackage.FEATURE___SUBSETS_CHAIN__FEATURE_FEATURE:
                return this.subsetsChain((Feature) arguments.get(0), (Feature) arguments.get(1));
            case SysmlPackage.FEATURE___TYPING_FEATURES:
                return this.typingFeatures();
        }
        return super.eInvoke(operationID, arguments);
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
        result.append(" (direction: ");
        if (this.directionESet) {
            result.append(this.direction);
        } else {
            result.append("<unset>");
        }
        result.append(", isComposite: ");
        result.append(this.isComposite);
        result.append(", isDerived: ");
        result.append(this.isDerived);
        result.append(", isEnd: ");
        result.append(this.isEnd);
        result.append(", isOrdered: ");
        result.append(this.isOrdered);
        result.append(", isPortion: ");
        result.append(this.isPortion);
        result.append(", isReadOnly: ");
        result.append(this.isReadOnly);
        result.append(", isUnique: ");
        result.append(this.isUnique);
        result.append(')');
        return result.toString();
    }

} // FeatureImpl
