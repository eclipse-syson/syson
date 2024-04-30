/**
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
 */
package org.eclipse.syson.sysml.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureInverting;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.TypeFeaturing;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getDirection <em>Direction</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsComposite <em>Is Composite</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsDerived <em>Is Derived</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsEnd <em>Is End</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsNonunique <em>Is Nonunique</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsOrdered <em>Is Ordered</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsPortion <em>Is Portion</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsReadOnly <em>Is Read Only</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#isIsUnique <em>Is Unique</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getChainingFeature <em>Chaining Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getEndOwningType <em>End Owning Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getFeaturingType <em>Featuring Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwnedFeatureChaining <em>Owned Feature Chaining</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwnedFeatureInverting <em>Owned Feature Inverting</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwnedRedefinition <em>Owned Redefinition</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwnedReferenceSubsetting <em>Owned Reference Subsetting</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwnedSubsetting <em>Owned Subsetting</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwnedTypeFeaturing <em>Owned Type Featuring</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwnedTyping <em>Owned Typing</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwningFeatureMembership <em>Owning Feature Membership</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getOwningType <em>Owning Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureImpl#getType <em>Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureImpl extends TypeImpl implements Feature {
    /**
     * The default value of the '{@link #getDirection() <em>Direction</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDirection()
     * @generated
     * @ordered
     */
    protected static final FeatureDirectionKind DIRECTION_EDEFAULT = FeatureDirectionKind.IN;

    /**
     * The cached value of the '{@link #getDirection() <em>Direction</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDirection()
     * @generated
     * @ordered
     */
    protected FeatureDirectionKind direction = DIRECTION_EDEFAULT;

    /**
     * The default value of the '{@link #isIsComposite() <em>Is Composite</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsComposite()
     * @generated
     * @ordered
     */
    protected static final boolean IS_COMPOSITE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsComposite() <em>Is Composite</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsComposite()
     * @generated
     * @ordered
     */
    protected boolean isComposite = IS_COMPOSITE_EDEFAULT;

    /**
     * The default value of the '{@link #isIsDerived() <em>Is Derived</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsDerived()
     * @generated
     * @ordered
     */
    protected static final boolean IS_DERIVED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsDerived() <em>Is Derived</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsDerived()
     * @generated
     * @ordered
     */
    protected boolean isDerived = IS_DERIVED_EDEFAULT;

    /**
     * The default value of the '{@link #isIsEnd() <em>Is End</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsEnd()
     * @generated
     * @ordered
     */
    protected static final boolean IS_END_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsEnd() <em>Is End</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsEnd()
     * @generated
     * @ordered
     */
    protected boolean isEnd = IS_END_EDEFAULT;

    /**
     * The default value of the '{@link #isIsNonunique() <em>Is Nonunique</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsNonunique()
     * @generated
     * @ordered
     */
    protected static final boolean IS_NONUNIQUE_EDEFAULT = false;

    /**
     * The default value of the '{@link #isIsOrdered() <em>Is Ordered</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsOrdered()
     * @generated
     * @ordered
     */
    protected static final boolean IS_ORDERED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsOrdered() <em>Is Ordered</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsOrdered()
     * @generated
     * @ordered
     */
    protected boolean isOrdered = IS_ORDERED_EDEFAULT;

    /**
     * The default value of the '{@link #isIsPortion() <em>Is Portion</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsPortion()
     * @generated
     * @ordered
     */
    protected static final boolean IS_PORTION_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsPortion() <em>Is Portion</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsPortion()
     * @generated
     * @ordered
     */
    protected boolean isPortion = IS_PORTION_EDEFAULT;

    /**
     * The default value of the '{@link #isIsReadOnly() <em>Is Read Only</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsReadOnly()
     * @generated
     * @ordered
     */
    protected static final boolean IS_READ_ONLY_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsReadOnly() <em>Is Read Only</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsReadOnly()
     * @generated
     * @ordered
     */
    protected boolean isReadOnly = IS_READ_ONLY_EDEFAULT;

    /**
     * The default value of the '{@link #isIsUnique() <em>Is Unique</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsUnique()
     * @generated
     * @ordered
     */
    protected static final boolean IS_UNIQUE_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isIsUnique() <em>Is Unique</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsUnique()
     * @generated
     * @ordered
     */
    protected boolean isUnique = IS_UNIQUE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FeatureImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFeature();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Feature> getChainingFeature() {
        List<Feature> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_ChainingFeature(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FeatureDirectionKind getDirection() {
        return direction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setDirection(FeatureDirectionKind newDirection) {
        FeatureDirectionKind oldDirection = direction;
        direction = newDirection == null ? DIRECTION_EDEFAULT : newDirection;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__DIRECTION, oldDirection, direction));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getEndOwningType() {
        Type endOwningType = basicGetEndOwningType();
        return endOwningType != null && endOwningType.eIsProxy() ? (Type)eResolveProxy((InternalEObject)endOwningType) : endOwningType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Type basicGetEndOwningType() {
        // TODO: implement this method to return the 'End Owning Type' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Type> getFeaturingType() {
        List<Type> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_FeaturingType(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isIsComposite() {
        return isComposite;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsComposite(boolean newIsComposite) {
        boolean oldIsComposite = isComposite;
        isComposite = newIsComposite;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__IS_COMPOSITE, oldIsComposite, isComposite));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isIsDerived() {
        return isDerived;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsDerived(boolean newIsDerived) {
        boolean oldIsDerived = isDerived;
        isDerived = newIsDerived;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__IS_DERIVED, oldIsDerived, isDerived));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isIsEnd() {
        return isEnd;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsEnd(boolean newIsEnd) {
        boolean oldIsEnd = isEnd;
        isEnd = newIsEnd;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__IS_END, oldIsEnd, isEnd));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean isIsNonunique() {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isIsOrdered() {
        return isOrdered;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsOrdered(boolean newIsOrdered) {
        boolean oldIsOrdered = isOrdered;
        isOrdered = newIsOrdered;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__IS_ORDERED, oldIsOrdered, isOrdered));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isIsPortion() {
        return isPortion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsPortion(boolean newIsPortion) {
        boolean oldIsPortion = isPortion;
        isPortion = newIsPortion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__IS_PORTION, oldIsPortion, isPortion));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isIsReadOnly() {
        return isReadOnly;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsReadOnly(boolean newIsReadOnly) {
        boolean oldIsReadOnly = isReadOnly;
        isReadOnly = newIsReadOnly;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__IS_READ_ONLY, oldIsReadOnly, isReadOnly));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isIsUnique() {
        return isUnique;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsUnique(boolean newIsUnique) {
        boolean oldIsUnique = isUnique;
        isUnique = newIsUnique;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE__IS_UNIQUE, oldIsUnique, isUnique));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<FeatureInverting> getOwnedFeatureInverting() {
        List<FeatureInverting> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_OwnedFeatureInverting(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ReferenceSubsetting getOwnedReferenceSubsetting() {
        ReferenceSubsetting ownedReferenceSubsetting = basicGetOwnedReferenceSubsetting();
        return ownedReferenceSubsetting != null && ownedReferenceSubsetting.eIsProxy() ? (ReferenceSubsetting)eResolveProxy((InternalEObject)ownedReferenceSubsetting) : ownedReferenceSubsetting;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public ReferenceSubsetting basicGetOwnedReferenceSubsetting() {
        return this.getOwnedRelationship().stream()
            .filter(ReferenceSubsetting.class::isInstance)
            .map(ReferenceSubsetting.class::cast)
            .findFirst()
            .orElse(null);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Subsetting> getOwnedSubsetting() {
        Subsetting[] subSettings = this.getOwnedRelationship().stream()
            .filter(Subsetting.class::isInstance)
            .map(Subsetting.class::cast)
            .toArray(Subsetting[]::new);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_OwnedSubsetting(), subSettings.length, subSettings);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<TypeFeaturing> getOwnedTypeFeaturing() {
        List<TypeFeaturing> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_OwnedTypeFeaturing(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<FeatureTyping> getOwnedTyping() {
        List<FeatureTyping> ownedTypings = new ArrayList<>();
        //The ownedSpecializations of this Feature that are FeatureTypings, for which the Feature is the typedFeature.
        this.getOwnedSpecialization().stream()
            .filter(FeatureTyping.class::isInstance)
            .map(FeatureTyping.class::cast)
            .filter(ft -> this.equals(ft.getTypedFeature()))
            .forEach(ownedTypings::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_OwnedTyping(), ownedTypings.size(), ownedTypings.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * The FeatureMembership that owns this Feature as an ownedMemberFeature, determining its owningType.
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FeatureMembership getOwningFeatureMembership() {
        FeatureMembership owningFeatureMembership = basicGetOwningFeatureMembership();
        return owningFeatureMembership != null && owningFeatureMembership.eIsProxy() ? (FeatureMembership)eResolveProxy((InternalEObject)owningFeatureMembership) : owningFeatureMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * The FeatureMembership that owns this Feature as an ownedMemberFeature, determining its owningType.
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public FeatureMembership basicGetOwningFeatureMembership() {
        OwningMembership owningMembership = getOwningMembership();
        if (owningMembership instanceof FeatureMembership featureMembershipt) {
            return featureMembershipt;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * The Type that is the owningType of the owningFeatureMembership of this Feature.
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getOwningType() {
        Type owningType = basicGetOwningType();
        return owningType != null && owningType.eIsProxy() ? (Type)eResolveProxy((InternalEObject)owningType) : owningType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Type> getType() {
        List<Type> types = new ArrayList<>();
        this.getOwnedRelationship().stream()
            .filter(FeatureTyping.class::isInstance)
            .map(FeatureTyping.class::cast)
            .map(typing -> typing.getType())
            .forEach(types::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFeature_Type(), types.size(), types.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FeatureDirectionKind directionFor(Type type) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean isFeaturedWithin(Type type) {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature namingFeature() {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean redefines(Feature redefinedFeature) {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean redefinesFromLibrary(String libraryFeatureName) {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean subsetsChain(Feature first, Feature second) {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.FEATURE__DIRECTION:
                return getDirection();
            case SysmlPackage.FEATURE__IS_COMPOSITE:
                return isIsComposite();
            case SysmlPackage.FEATURE__IS_DERIVED:
                return isIsDerived();
            case SysmlPackage.FEATURE__IS_END:
                return isIsEnd();
            case SysmlPackage.FEATURE__IS_NONUNIQUE:
                return isIsNonunique();
            case SysmlPackage.FEATURE__IS_ORDERED:
                return isIsOrdered();
            case SysmlPackage.FEATURE__IS_PORTION:
                return isIsPortion();
            case SysmlPackage.FEATURE__IS_READ_ONLY:
                return isIsReadOnly();
            case SysmlPackage.FEATURE__IS_UNIQUE:
                return isIsUnique();
            case SysmlPackage.FEATURE__CHAINING_FEATURE:
                return getChainingFeature();
            case SysmlPackage.FEATURE__END_OWNING_TYPE:
                if (resolve) return getEndOwningType();
                return basicGetEndOwningType();
            case SysmlPackage.FEATURE__FEATURING_TYPE:
                return getFeaturingType();
            case SysmlPackage.FEATURE__OWNED_FEATURE_CHAINING:
                return getOwnedFeatureChaining();
            case SysmlPackage.FEATURE__OWNED_FEATURE_INVERTING:
                return getOwnedFeatureInverting();
            case SysmlPackage.FEATURE__OWNED_REDEFINITION:
                return getOwnedRedefinition();
            case SysmlPackage.FEATURE__OWNED_REFERENCE_SUBSETTING:
                if (resolve) return getOwnedReferenceSubsetting();
                return basicGetOwnedReferenceSubsetting();
            case SysmlPackage.FEATURE__OWNED_SUBSETTING:
                return getOwnedSubsetting();
            case SysmlPackage.FEATURE__OWNED_TYPE_FEATURING:
                return getOwnedTypeFeaturing();
            case SysmlPackage.FEATURE__OWNED_TYPING:
                return getOwnedTyping();
            case SysmlPackage.FEATURE__OWNING_FEATURE_MEMBERSHIP:
                if (resolve) return getOwningFeatureMembership();
                return basicGetOwningFeatureMembership();
            case SysmlPackage.FEATURE__OWNING_TYPE:
                if (resolve) return getOwningType();
                return basicGetOwningType();
            case SysmlPackage.FEATURE__TYPE:
                return getType();
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
            case SysmlPackage.FEATURE__DIRECTION:
                setDirection((FeatureDirectionKind)newValue);
                return;
            case SysmlPackage.FEATURE__IS_COMPOSITE:
                setIsComposite((Boolean)newValue);
                return;
            case SysmlPackage.FEATURE__IS_DERIVED:
                setIsDerived((Boolean)newValue);
                return;
            case SysmlPackage.FEATURE__IS_END:
                setIsEnd((Boolean)newValue);
                return;
            case SysmlPackage.FEATURE__IS_ORDERED:
                setIsOrdered((Boolean)newValue);
                return;
            case SysmlPackage.FEATURE__IS_PORTION:
                setIsPortion((Boolean)newValue);
                return;
            case SysmlPackage.FEATURE__IS_READ_ONLY:
                setIsReadOnly((Boolean)newValue);
                return;
            case SysmlPackage.FEATURE__IS_UNIQUE:
                setIsUnique((Boolean)newValue);
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
            case SysmlPackage.FEATURE__DIRECTION:
                setDirection(DIRECTION_EDEFAULT);
                return;
            case SysmlPackage.FEATURE__IS_COMPOSITE:
                setIsComposite(IS_COMPOSITE_EDEFAULT);
                return;
            case SysmlPackage.FEATURE__IS_DERIVED:
                setIsDerived(IS_DERIVED_EDEFAULT);
                return;
            case SysmlPackage.FEATURE__IS_END:
                setIsEnd(IS_END_EDEFAULT);
                return;
            case SysmlPackage.FEATURE__IS_ORDERED:
                setIsOrdered(IS_ORDERED_EDEFAULT);
                return;
            case SysmlPackage.FEATURE__IS_PORTION:
                setIsPortion(IS_PORTION_EDEFAULT);
                return;
            case SysmlPackage.FEATURE__IS_READ_ONLY:
                setIsReadOnly(IS_READ_ONLY_EDEFAULT);
                return;
            case SysmlPackage.FEATURE__IS_UNIQUE:
                setIsUnique(IS_UNIQUE_EDEFAULT);
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
            case SysmlPackage.FEATURE__DIRECTION:
                return direction != DIRECTION_EDEFAULT;
            case SysmlPackage.FEATURE__IS_COMPOSITE:
                return isComposite != IS_COMPOSITE_EDEFAULT;
            case SysmlPackage.FEATURE__IS_DERIVED:
                return isDerived != IS_DERIVED_EDEFAULT;
            case SysmlPackage.FEATURE__IS_END:
                return isEnd != IS_END_EDEFAULT;
            case SysmlPackage.FEATURE__IS_NONUNIQUE:
                return isIsNonunique() != IS_NONUNIQUE_EDEFAULT;
            case SysmlPackage.FEATURE__IS_ORDERED:
                return isOrdered != IS_ORDERED_EDEFAULT;
            case SysmlPackage.FEATURE__IS_PORTION:
                return isPortion != IS_PORTION_EDEFAULT;
            case SysmlPackage.FEATURE__IS_READ_ONLY:
                return isReadOnly != IS_READ_ONLY_EDEFAULT;
            case SysmlPackage.FEATURE__IS_UNIQUE:
                return isUnique != IS_UNIQUE_EDEFAULT;
            case SysmlPackage.FEATURE__CHAINING_FEATURE:
                return !getChainingFeature().isEmpty();
            case SysmlPackage.FEATURE__END_OWNING_TYPE:
                return basicGetEndOwningType() != null;
            case SysmlPackage.FEATURE__FEATURING_TYPE:
                return !getFeaturingType().isEmpty();
            case SysmlPackage.FEATURE__OWNED_FEATURE_CHAINING:
                return !getOwnedFeatureChaining().isEmpty();
            case SysmlPackage.FEATURE__OWNED_FEATURE_INVERTING:
                return !getOwnedFeatureInverting().isEmpty();
            case SysmlPackage.FEATURE__OWNED_REDEFINITION:
                return !getOwnedRedefinition().isEmpty();
            case SysmlPackage.FEATURE__OWNED_REFERENCE_SUBSETTING:
                return basicGetOwnedReferenceSubsetting() != null;
            case SysmlPackage.FEATURE__OWNED_SUBSETTING:
                return !getOwnedSubsetting().isEmpty();
            case SysmlPackage.FEATURE__OWNED_TYPE_FEATURING:
                return !getOwnedTypeFeaturing().isEmpty();
            case SysmlPackage.FEATURE__OWNED_TYPING:
                return !getOwnedTyping().isEmpty();
            case SysmlPackage.FEATURE__OWNING_FEATURE_MEMBERSHIP:
                return basicGetOwningFeatureMembership() != null;
            case SysmlPackage.FEATURE__OWNING_TYPE:
                return basicGetOwningType() != null;
            case SysmlPackage.FEATURE__TYPE:
                return !getType().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.FEATURE___DIRECTION_FOR__TYPE:
                return directionFor((Type)arguments.get(0));
            case SysmlPackage.FEATURE___IS_FEATURED_WITHIN__TYPE:
                return isFeaturedWithin((Type)arguments.get(0));
            case SysmlPackage.FEATURE___NAMING_FEATURE:
                return namingFeature();
            case SysmlPackage.FEATURE___REDEFINES__FEATURE:
                return redefines((Feature)arguments.get(0));
            case SysmlPackage.FEATURE___REDEFINES_FROM_LIBRARY__STRING:
                return redefinesFromLibrary((String)arguments.get(0));
            case SysmlPackage.FEATURE___SUBSETS_CHAIN__FEATURE_FEATURE:
                return subsetsChain((Feature)arguments.get(0), (Feature)arguments.get(1));
        }
        return super.eInvoke(operationID, arguments);
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
        result.append(" (direction: ");
        result.append(direction);
        result.append(", isComposite: ");
        result.append(isComposite);
        result.append(", isDerived: ");
        result.append(isDerived);
        result.append(", isEnd: ");
        result.append(isEnd);
        result.append(", isOrdered: ");
        result.append(isOrdered);
        result.append(", isPortion: ");
        result.append(isPortion);
        result.append(", isReadOnly: ");
        result.append(isReadOnly);
        result.append(", isUnique: ");
        result.append(isUnique);
        result.append(')');
        return result.toString();
    }

} //FeatureImpl
