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

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.Conjugation;
import org.eclipse.syson.sysml.Differencing;
import org.eclipse.syson.sysml.Disjoining;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Intersecting;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Multiplicity;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Unioning;
import org.eclipse.syson.sysml.VisibilityKind;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#isIsAbstract <em>Is Abstract</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#isIsConjugated <em>Is Conjugated</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#isIsSufficient <em>Is Sufficient</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getDifferencingType <em>Differencing Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getDirectedFeature <em>Directed Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getEndFeature <em>End Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getFeature <em>Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getFeatureMembership <em>Feature Membership</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getInheritedFeature <em>Inherited Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getInheritedMembership <em>Inherited Membership</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getInput <em>Input</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getIntersectingType <em>Intersecting Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getMultiplicity <em>Multiplicity</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOutput <em>Output</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedConjugator <em>Owned Conjugator</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedDifferencing <em>Owned Differencing</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedDisjoining <em>Owned Disjoining</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedEndFeature <em>Owned End Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedFeature <em>Owned Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedFeatureMembership <em>Owned Feature Membership</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedIntersecting <em>Owned Intersecting</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedSpecialization <em>Owned Specialization</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedUnioning <em>Owned Unioning</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getUnioningType <em>Unioning Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TypeImpl extends NamespaceImpl implements Type {
    /**
     * The default value of the '{@link #isIsAbstract() <em>Is Abstract</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsAbstract()
     * @generated
     * @ordered
     */
    protected static final boolean IS_ABSTRACT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsAbstract() <em>Is Abstract</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsAbstract()
     * @generated
     * @ordered
     */
    protected boolean isAbstract = IS_ABSTRACT_EDEFAULT;

    /**
     * The default value of the '{@link #isIsConjugated() <em>Is Conjugated</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsConjugated()
     * @generated
     * @ordered
     */
    protected static final boolean IS_CONJUGATED_EDEFAULT = false;

    /**
     * The default value of the '{@link #isIsSufficient() <em>Is Sufficient</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsSufficient()
     * @generated
     * @ordered
     */
    protected static final boolean IS_SUFFICIENT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsSufficient() <em>Is Sufficient</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsSufficient()
     * @generated
     * @ordered
     */
    protected boolean isSufficient = IS_SUFFICIENT_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Type> getDifferencingType() {
        List<Type> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_DifferencingType(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Feature> getDirectedFeature() {
        List<Feature> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_DirectedFeature(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Feature> getEndFeature() {
        List<Feature> ends = this.getOwnedFeature().stream()
                .filter(Feature::isIsEnd)
                .toList();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_EndFeature(), ends.size(), ends.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Feature> getFeature() {
        Feature[] features = getFeatureMembership().stream()
                .filter(fm -> fm.getFeature() != null)
                .map(FeatureMembership::getFeature)
                .toArray(Feature[]::new);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_Feature(), features.length, features);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<FeatureMembership> getFeatureMembership() {
        FeatureMembership[] featureMemberships = Stream.concat(getOwnedFeatureMembership().stream(), inheritedMemberships(new BasicEList<Type>()).stream()).filter(FeatureMembership.class::isInstance).toArray(FeatureMembership[]::new);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_FeatureMembership(), featureMemberships.length, featureMemberships);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Feature> getInheritedFeature() {
        List<Feature> inheritedFeatures = new ArrayList<>();
        this.getInheritedMembership().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .map(FeatureMembership::getOwnedMemberFeature)
                .filter(Objects::nonNull)
                .forEach(inheritedFeatures::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_InheritedFeature(), inheritedFeatures.size(), inheritedFeatures.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * Partial implementation (see sub types, e.g. UsageImpl or DefinitionImpl). Should be:
     * All Memberships inherited by this Type via Specialization or Conjugation.
     * These are included in the derived union for the memberships of the Type.
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Membership> getInheritedMembership() {
        FeatureMembership[] data = inheritedMemberships(new BasicEList<Type>()).stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .toArray(FeatureMembership[]::new);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_InheritedMembership(), data.length, data);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Feature> getInput() {
        List<Feature> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_Input(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Type> getIntersectingType() {
        List<Type> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_IntersectingType(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isIsAbstract() {
        return isAbstract;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsAbstract(boolean newIsAbstract) {
        boolean oldIsAbstract = isAbstract;
        isAbstract = newIsAbstract;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.TYPE__IS_ABSTRACT, oldIsAbstract, isAbstract));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean isIsConjugated() {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isIsSufficient() {
        return isSufficient;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsSufficient(boolean newIsSufficient) {
        boolean oldIsSufficient = isSufficient;
        isSufficient = newIsSufficient;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.TYPE__IS_SUFFICIENT, oldIsSufficient, isSufficient));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Multiplicity getMultiplicity() {
        Multiplicity multiplicity = basicGetMultiplicity();
        return multiplicity != null && multiplicity.eIsProxy() ? (Multiplicity)eResolveProxy((InternalEObject)multiplicity) : multiplicity;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Multiplicity basicGetMultiplicity() {
        // TODO: implement this method to return the 'Multiplicity' reference
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
    public EList<Feature> getOutput() {
        List<Feature> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_Output(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Conjugation getOwnedConjugator() {
        Conjugation ownedConjugator = basicGetOwnedConjugator();
        return ownedConjugator != null && ownedConjugator.eIsProxy() ? (Conjugation) eResolveProxy((InternalEObject) ownedConjugator) : ownedConjugator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Conjugation basicGetOwnedConjugator() {
        // TODO: implement this method to return the 'Owned Conjugator' reference
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
    public EList<Differencing> getOwnedDifferencing() {
        List<Differencing> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedDifferencing(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Disjoining> getOwnedDisjoining() {
        List<Disjoining> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedDisjoining(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Feature> getOwnedEndFeature() {
        List<Feature> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedEndFeature(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Feature> getOwnedFeature() {
        List<Feature> ownedFeatures = new ArrayList<>();
        this.getOwnedFeatureMembership().stream()
            .map(FeatureMembership::getOwnedMemberFeature)
            .forEach(ownedFeatures::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedFeature(), ownedFeatures.size(), ownedFeatures.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<FeatureMembership> getOwnedFeatureMembership() {
        List<FeatureMembership> ownedFeatureMemberships = new ArrayList<>();
        this.getOwnedRelationship().stream()
            .filter(FeatureMembership.class::isInstance)
            .map(FeatureMembership.class::cast)
            .forEach(ownedFeatureMemberships::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedFeatureMembership(), ownedFeatureMemberships.size(), ownedFeatureMemberships.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Intersecting> getOwnedIntersecting() {
        List<Intersecting> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedIntersecting(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Specialization> getOwnedSpecialization() {
        List<Specialization> ownedSpecializations = new ArrayList<>();
        // The ownedRelationships of this Type that are Specializations, and for which the Specialization's specific Type is this Type.
        this.getOwnedRelationship().stream()
            .filter(Specialization.class::isInstance)
            .map(Specialization.class::cast)
            .filter(spec -> this.equals(spec.getSpecific()))
            .forEach(ownedSpecializations::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedSpecialization(), ownedSpecializations.size(), ownedSpecializations.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Unioning> getOwnedUnioning() {
        List<Unioning> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedUnioning(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Type> getUnioningType() {
        List<Type> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_UnioningType(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Type> allSupertypes() {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FeatureDirectionKind directionOf(Feature feature) {
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
    public EList<Membership> inheritedMemberships(EList<Type> excluded) {
        excluded.add(this);
        Conjugation conjugator = getOwnedConjugator();
        List<Membership> conjugatedMemberships = List.of();
        if (conjugator != null) {
            Type type = conjugator.getOriginalType();
            if (type != null) {
                conjugatedMemberships = type.getMembership().stream().toList();
            }
        }

        List<Membership> generalMemberships = getOwnedSpecialization().stream()
                .map(spe -> spe.getGeneral())
                .filter(g -> g != null && !excluded.contains(g))
                .flatMap(gen -> gen.visibleMemberships(toNamespaceExclude(excluded), false, true).stream()).toList();

        Membership[] data = Stream.concat(conjugatedMemberships.stream(), generalMemberships.stream())
                // Also inherit protected memberships
                .filter(rel -> rel.getVisibility() != VisibilityKind.PRIVATE)
                .toArray(Membership[]::new);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_InheritedMembership(), data.length, data);
    }
    
    /**
     * @generated NOT
     */
    private EList<Namespace> toNamespaceExclude(EList<Type> excluded){
        return excluded.stream().filter(Namespace.class::isInstance)
                .map(Namespace.class::cast)
                .collect(toCollection(BasicEList<Namespace>::new));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean specializes(Type supertype) {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean specializesFromLibrary(String libraryTypeName) {
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
            case SysmlPackage.TYPE__IS_ABSTRACT:
                return isIsAbstract();
            case SysmlPackage.TYPE__IS_CONJUGATED:
                return isIsConjugated();
            case SysmlPackage.TYPE__IS_SUFFICIENT:
                return isIsSufficient();
            case SysmlPackage.TYPE__DIFFERENCING_TYPE:
                return getDifferencingType();
            case SysmlPackage.TYPE__DIRECTED_FEATURE:
                return getDirectedFeature();
            case SysmlPackage.TYPE__END_FEATURE:
                return getEndFeature();
            case SysmlPackage.TYPE__FEATURE:
                return getFeature();
            case SysmlPackage.TYPE__FEATURE_MEMBERSHIP:
                return getFeatureMembership();
            case SysmlPackage.TYPE__INHERITED_FEATURE:
                return getInheritedFeature();
            case SysmlPackage.TYPE__INHERITED_MEMBERSHIP:
                return getInheritedMembership();
            case SysmlPackage.TYPE__INPUT:
                return getInput();
            case SysmlPackage.TYPE__INTERSECTING_TYPE:
                return getIntersectingType();
            case SysmlPackage.TYPE__MULTIPLICITY:
                if (resolve) return getMultiplicity();
                return basicGetMultiplicity();
            case SysmlPackage.TYPE__OUTPUT:
                return getOutput();
            case SysmlPackage.TYPE__OWNED_CONJUGATOR:
                if (resolve) return getOwnedConjugator();
                return basicGetOwnedConjugator();
            case SysmlPackage.TYPE__OWNED_DIFFERENCING:
                return getOwnedDifferencing();
            case SysmlPackage.TYPE__OWNED_DISJOINING:
                return getOwnedDisjoining();
            case SysmlPackage.TYPE__OWNED_END_FEATURE:
                return getOwnedEndFeature();
            case SysmlPackage.TYPE__OWNED_FEATURE:
                return getOwnedFeature();
            case SysmlPackage.TYPE__OWNED_FEATURE_MEMBERSHIP:
                return getOwnedFeatureMembership();
            case SysmlPackage.TYPE__OWNED_INTERSECTING:
                return getOwnedIntersecting();
            case SysmlPackage.TYPE__OWNED_SPECIALIZATION:
                return getOwnedSpecialization();
            case SysmlPackage.TYPE__OWNED_UNIONING:
                return getOwnedUnioning();
            case SysmlPackage.TYPE__UNIONING_TYPE:
                return getUnioningType();
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
            case SysmlPackage.TYPE__IS_ABSTRACT:
                setIsAbstract((Boolean)newValue);
                return;
            case SysmlPackage.TYPE__IS_SUFFICIENT:
                setIsSufficient((Boolean)newValue);
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
            case SysmlPackage.TYPE__IS_ABSTRACT:
                setIsAbstract(IS_ABSTRACT_EDEFAULT);
                return;
            case SysmlPackage.TYPE__IS_SUFFICIENT:
                setIsSufficient(IS_SUFFICIENT_EDEFAULT);
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
            case SysmlPackage.TYPE__IS_ABSTRACT:
                return isAbstract != IS_ABSTRACT_EDEFAULT;
            case SysmlPackage.TYPE__IS_CONJUGATED:
                return isIsConjugated() != IS_CONJUGATED_EDEFAULT;
            case SysmlPackage.TYPE__IS_SUFFICIENT:
                return isSufficient != IS_SUFFICIENT_EDEFAULT;
            case SysmlPackage.TYPE__DIFFERENCING_TYPE:
                return !getDifferencingType().isEmpty();
            case SysmlPackage.TYPE__DIRECTED_FEATURE:
                return !getDirectedFeature().isEmpty();
            case SysmlPackage.TYPE__END_FEATURE:
                return !getEndFeature().isEmpty();
            case SysmlPackage.TYPE__FEATURE:
                return !getFeature().isEmpty();
            case SysmlPackage.TYPE__FEATURE_MEMBERSHIP:
                return !getFeatureMembership().isEmpty();
            case SysmlPackage.TYPE__INHERITED_FEATURE:
                return !getInheritedFeature().isEmpty();
            case SysmlPackage.TYPE__INHERITED_MEMBERSHIP:
                return !getInheritedMembership().isEmpty();
            case SysmlPackage.TYPE__INPUT:
                return !getInput().isEmpty();
            case SysmlPackage.TYPE__INTERSECTING_TYPE:
                return !getIntersectingType().isEmpty();
            case SysmlPackage.TYPE__MULTIPLICITY:
                return basicGetMultiplicity() != null;
            case SysmlPackage.TYPE__OUTPUT:
                return !getOutput().isEmpty();
            case SysmlPackage.TYPE__OWNED_CONJUGATOR:
                return basicGetOwnedConjugator() != null;
            case SysmlPackage.TYPE__OWNED_DIFFERENCING:
                return !getOwnedDifferencing().isEmpty();
            case SysmlPackage.TYPE__OWNED_DISJOINING:
                return !getOwnedDisjoining().isEmpty();
            case SysmlPackage.TYPE__OWNED_END_FEATURE:
                return !getOwnedEndFeature().isEmpty();
            case SysmlPackage.TYPE__OWNED_FEATURE:
                return !getOwnedFeature().isEmpty();
            case SysmlPackage.TYPE__OWNED_FEATURE_MEMBERSHIP:
                return !getOwnedFeatureMembership().isEmpty();
            case SysmlPackage.TYPE__OWNED_INTERSECTING:
                return !getOwnedIntersecting().isEmpty();
            case SysmlPackage.TYPE__OWNED_SPECIALIZATION:
                return !getOwnedSpecialization().isEmpty();
            case SysmlPackage.TYPE__OWNED_UNIONING:
                return !getOwnedUnioning().isEmpty();
            case SysmlPackage.TYPE__UNIONING_TYPE:
                return !getUnioningType().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * @generated NOT
     */
    @Override
    public EList<Membership> visibleMemberships(EList<Namespace> excluded, boolean isRecursive, boolean includeAll) {
        // Inherited members are visible members of a type
        BasicEList<Type> excludedTypes = excluded.stream()
                .filter(Type.class::isInstance)
                .map(Type.class::cast)
                .collect(toCollection(BasicEList<Type>::new));

        EList<Membership> superVisibleMemberships = super.visibleMemberships(excluded, isRecursive, includeAll);
        EList<Membership> inheritedMemberships = inheritedMemberships(excludedTypes);
        List<Membership> visibleMemberships = Stream.concat(superVisibleMemberships.stream(),
                inheritedMemberships.stream())
                .filter(rel -> includeAll || rel.getVisibility() == VisibilityKind.PUBLIC)
                .toList();
        return new BasicEList<>(visibleMemberships);
    }

    @Override
    public EList<Membership> getMembership() {
        Membership[] visibleMemberships = visibleMemberships(new BasicEList<Namespace>(), false, true).toArray(Membership[]::new);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getNamespace_Membership(), visibleMemberships.length, visibleMemberships);

    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.TYPE___ALL_SUPERTYPES:
                return allSupertypes();
            case SysmlPackage.TYPE___DIRECTION_OF__FEATURE:
                return directionOf((Feature)arguments.get(0));
            case SysmlPackage.TYPE___INHERITED_MEMBERSHIPS__ELIST:
                return inheritedMemberships((EList<Type>)arguments.get(0));
            case SysmlPackage.TYPE___SPECIALIZES__TYPE:
                return specializes((Type)arguments.get(0));
            case SysmlPackage.TYPE___SPECIALIZES_FROM_LIBRARY__STRING:
                return specializesFromLibrary((String)arguments.get(0));
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
        result.append(" (isAbstract: ");
        result.append(isAbstract);
        result.append(", isSufficient: ");
        result.append(isSufficient);
        result.append(')');
        return result.toString();
    }

} //TypeImpl
