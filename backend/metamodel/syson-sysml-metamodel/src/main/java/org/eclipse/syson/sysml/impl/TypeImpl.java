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

import static java.util.stream.Collectors.toCollection;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Conjugation;
import org.eclipse.syson.sysml.Differencing;
import org.eclipse.syson.sysml.Disjoining;
import org.eclipse.syson.sysml.Element;
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
import org.eclipse.syson.sysml.helper.ImplicitSpecializationSwitch;
import org.eclipse.syson.sysml.helper.MembershipComputer;
import org.eclipse.syson.sysml.helper.NameConflictingFilter;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#isIsAbstract <em>Is Abstract</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#isIsConjugated <em>Is Conjugated</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#isIsSufficient <em>Is Sufficient</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getDifferencingType <em>Differencing Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getDirectedFeature <em>Directed Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getEndFeature <em>End Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getFeature <em>Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getFeatureMembership <em>Feature Membership</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getInheritedFeature <em>Inherited Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getInheritedMembership <em>Inherited Membership</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getInput <em>Input</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getIntersectingType <em>Intersecting Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getMultiplicity <em>Multiplicity</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOutput <em>Output</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedConjugator <em>Owned Conjugator</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedDifferencing <em>Owned Differencing</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedDisjoining <em>Owned Disjoining</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedEndFeature <em>Owned End Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedFeature <em>Owned Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedFeatureMembership <em>Owned Feature Membership</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedIntersecting <em>Owned Intersecting</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedSpecialization <em>Owned Specialization</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getOwnedUnioning <em>Owned Unioning</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeImpl#getUnioningType <em>Unioning Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TypeImpl extends NamespaceImpl implements Type {
    /**
     * The default value of the '{@link #isIsAbstract() <em>Is Abstract</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsAbstract()
     * @generated
     * @ordered
     */
    protected static final boolean IS_ABSTRACT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsAbstract() <em>Is Abstract</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsAbstract()
     * @generated
     * @ordered
     */
    protected boolean isAbstract = IS_ABSTRACT_EDEFAULT;

    /**
     * The default value of the '{@link #isIsConjugated() <em>Is Conjugated</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #isIsConjugated()
     * @generated
     * @ordered
     */
    protected static final boolean IS_CONJUGATED_EDEFAULT = false;

    /**
     * The default value of the '{@link #isIsSufficient() <em>Is Sufficient</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #isIsSufficient()
     * @generated
     * @ordered
     */
    protected static final boolean IS_SUFFICIENT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsSufficient() <em>Is Sufficient</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #isIsSufficient()
     * @generated
     * @ordered
     */
    protected boolean isSufficient = IS_SUFFICIENT_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected TypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Type> getDifferencingType() {
        List<Type> differencingTypes = new ArrayList<>();
        this.getOwnedDifferencing().stream()
                .map(Differencing::getDifferencingType)
                .forEach(differencingTypes::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_DifferencingType(), differencingTypes.size(), differencingTypes.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getDirectedFeature() {
        List<Feature> directedFeatures = new ArrayList<>();
        this.getFeature().stream()
                .filter(f -> this.directionOf(f) != null)
                .forEach(directedFeatures::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_DirectedFeature(), directedFeatures.size(), directedFeatures.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
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
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getFeature() {
        Feature[] features = this.getFeatureMembership().stream()
                .filter(fm -> fm.getFeature() != null)
                .map(FeatureMembership::getFeature)
                .toArray(Feature[]::new);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_Feature(), features.length, features);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<FeatureMembership> getFeatureMembership() {
        FeatureMembership[] featureMemberships = Stream.concat(this.getOwnedFeatureMembership().stream(), this.inheritedMemberships(new BasicEList<>(), new BasicEList<>(), false).stream())
                .filter(FeatureMembership.class::isInstance)
                .toArray(FeatureMembership[]::new);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_FeatureMembership(), featureMemberships.length, featureMemberships);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
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
     * <!-- begin-user-doc --> Partial implementation (see sub types, e.g. UsageImpl or DefinitionImpl). Should be: All
     * Memberships inherited by this Type via Specialization or Conjugation. These are included in the derived union for
     * the memberships of the Type. <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Membership> getInheritedMembership() {
        FeatureMembership[] data = this.inheritedMemberships(new BasicEList<>(), new BasicEList<>(), false).stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .toArray(FeatureMembership[]::new);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_InheritedMembership(), data.length, data);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getInput() {
        List<Feature> inputs = new ArrayList<>();
        this.getFeature().stream()
                .filter(f -> {
                    var direction = this.directionOf(f);
                    return direction == FeatureDirectionKind.IN || direction == FeatureDirectionKind.INOUT;
                })
                .forEach(inputs::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_Input(), inputs.size(), inputs.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Type> getIntersectingType() {
        List<Type> intersectingTypes = new ArrayList<>();
        this.getOwnedIntersecting().stream()
                .map(Intersecting::getIntersectingType)
                .forEach(intersectingTypes::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_IntersectingType(), intersectingTypes.size(), intersectingTypes.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsAbstract() {
        return this.isAbstract;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsAbstract(boolean newIsAbstract) {
        boolean oldIsAbstract = this.isAbstract;
        this.isAbstract = newIsAbstract;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.TYPE__IS_ABSTRACT, oldIsAbstract, this.isAbstract));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isIsConjugated() {
        return this.getOwnedConjugator() != null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsSufficient() {
        return this.isSufficient;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsSufficient(boolean newIsSufficient) {
        boolean oldIsSufficient = this.isSufficient;
        this.isSufficient = newIsSufficient;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.TYPE__IS_SUFFICIENT, oldIsSufficient, this.isSufficient));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Multiplicity getMultiplicity() {
        Multiplicity multiplicity = this.basicGetMultiplicity();
        return multiplicity != null && multiplicity.eIsProxy() ? (Multiplicity) this.eResolveProxy((InternalEObject) multiplicity) : multiplicity;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Multiplicity basicGetMultiplicity() {
        return this.getOwnedMember().stream()
                .filter(Multiplicity.class::isInstance)
                .map(Multiplicity.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getOutput() {
        List<Feature> outputs = new ArrayList<>();
        this.getFeature().stream()
                .filter(f -> {
                    var direction = this.directionOf(f);
                    return direction == FeatureDirectionKind.OUT || direction == FeatureDirectionKind.INOUT;
                })
                .forEach(outputs::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_Output(), outputs.size(), outputs.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Conjugation getOwnedConjugator() {
        Conjugation ownedConjugator = this.basicGetOwnedConjugator();
        return ownedConjugator != null && ownedConjugator.eIsProxy() ? (Conjugation) this.eResolveProxy((InternalEObject) ownedConjugator) : ownedConjugator;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Conjugation basicGetOwnedConjugator() {
        return this.getOwnedRelationship().stream()
                .filter(Conjugation.class::isInstance)
                .map(Conjugation.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Differencing> getOwnedDifferencing() {
        List<Differencing> ownedDifferencings = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(Differencing.class::isInstance)
                .map(Differencing.class::cast)
                .forEach(ownedDifferencings::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedDifferencing(), ownedDifferencings.size(), ownedDifferencings.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Disjoining> getOwnedDisjoining() {
        List<Disjoining> ownedDisjoinings = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(Disjoining.class::isInstance)
                .map(Disjoining.class::cast)
                .forEach(ownedDisjoinings::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedDisjoining(), ownedDisjoinings.size(), ownedDisjoinings.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getOwnedEndFeature() {
        List<Feature> ownedEndFeatures = new ArrayList<>();
        this.getOwnedFeature().stream()
                .filter(Feature::isIsEnd)
                .forEach(ownedEndFeatures::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedEndFeature(), ownedEndFeatures.size(), ownedEndFeatures.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getOwnedFeature() {
        List<Feature> ownedFeatures = new ArrayList<>();
        this.getOwnedFeatureMembership().stream()
                .map(FeatureMembership::getOwnedMemberFeature)
                .filter(Objects::nonNull)
                .forEach(ownedFeatures::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedFeature(), ownedFeatures.size(), ownedFeatures.toArray());
    }

    /**
     * <!-- begin-user-doc --> The ownedMemberships of this Type that are FeatureMemberships, for which the Type is the
     * owningType. Each such FeatureMembership identifies an ownedFeature of the Type. <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<FeatureMembership> getOwnedFeatureMembership() {
        List<FeatureMembership> ownedFeatureMemberships = new ArrayList<>();
        this.getOwnedMembership().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .forEach(ownedFeatureMemberships::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedFeatureMembership(), ownedFeatureMemberships.size(), ownedFeatureMemberships.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Intersecting> getOwnedIntersecting() {
        List<Intersecting> ownedIntersectings = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(Intersecting.class::isInstance)
                .map(Intersecting.class::cast)
                .forEach(ownedIntersectings::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedIntersecting(), ownedIntersectings.size(), ownedIntersectings.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Specialization> getOwnedSpecialization() {
        List<Specialization> ownedSpecializations = new ArrayList<>();
        // The ownedRelationships of this Type that are Specializations, and for which the Specialization's specific
        // Type is this Type.
        this.getOwnedRelationship().stream()
                .filter(Specialization.class::isInstance)
                .map(Specialization.class::cast)
                .filter(spec -> this.equals(spec.getSpecific()))
                .forEach(ownedSpecializations::add);
        var implicitSpecializations = new ImplicitSpecializationSwitch(ownedSpecializations).doSwitch(this);
        for (var specialization : implicitSpecializations) {
            ownedSpecializations.add(specialization);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedSpecialization(), ownedSpecializations.size(), ownedSpecializations.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Unioning> getOwnedUnioning() {
        List<Unioning> ownedUnionings = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(Unioning.class::isInstance)
                .map(Unioning.class::cast)
                .forEach(ownedUnionings::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_OwnedUnioning(), ownedUnionings.size(), ownedUnionings.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Type> getUnioningType() {
        List<Type> unioningTypes = this.getOwnedUnioning().stream()
                .map(Unioning::getUnioningType)
                .toList();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_UnioningType(), unioningTypes.size(), unioningTypes.toArray());
    }

    /**
     * <!-- begin-user-doc --> Return all Types related to this Type as supertypes directly or transitively by
     * Specialization Relationships. <br/>
     * body: ownedSpecialization -> closure(general.ownedSpecialization).general -> including(self) <br/>
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Type> allSupertypes() {
        return this.allSupertypes(this, new ArrayList<>());
    }

    /**
     * @genereted NOT
     */
    private EList<Type> allSupertypes(Type currentType, List<Type> visited) {
        EList<Type> superTypes = new BasicEList<>();
        if (currentType.isIsConjugated()) {
            Type originalType = currentType.getOwnedConjugator().getOriginalType();
            if (visited.contains(originalType)) {
                visited.add(originalType);
                EList<Type> allSupertypes = this.allSupertypes(originalType, visited);
                for (Type supertype : allSupertypes) {
                    if (!superTypes.contains(supertype)) {
                        superTypes.add(supertype);
                    }
                }
            }
        } else {
            EList<Specialization> ownedSpecialization = currentType.getOwnedSpecialization();
            for (Specialization specialization : ownedSpecialization) {
                Type general = specialization.getGeneral();
                if (general != null && !superTypes.contains(general) && !visited.contains(general)) {
                    superTypes.add(general);
                    visited.add(general);
                    EList<Type> generalAllSupertypes = this.allSupertypes(general, visited);
                    for (Type generalSupertype : generalAllSupertypes) {
                        if (!superTypes.contains(generalSupertype)) {
                            superTypes.add(generalSupertype);
                        }
                    }
                }
            }
        }
        if (!superTypes.contains(currentType)) {
            superTypes.add(currentType);
        }
        if (!visited.contains(currentType)) {
            visited.add(currentType);
        }
        return superTypes;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public FeatureDirectionKind directionOf(Feature feature) {
        return this.directionOfExcluding(feature, new BasicEList<>());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public FeatureDirectionKind directionOfExcluding(Feature feature, EList<Type> excluded) {
        FeatureDirectionKind directionOfExcluding = null;
        EList<Type> excludedSelf = new BasicEList<>();
        excludedSelf.addAll(excluded);
        excludedSelf.add(this);

        if (this.equals(feature.getOwningType())) {
            directionOfExcluding = feature.getDirection();
        } else {
            EList<Type> supertypes = this.supertypes(false);
            supertypes.removeAll(excludedSelf);
            var directions = supertypes.stream()
                    .map(s -> s.directionOfExcluding(feature, excludedSelf))
                    .filter(Objects::nonNull)
                    .toList();
            if (!directions.isEmpty()) {
                var direction = directions.get(0);
                if (!this.isIsConjugated()) {
                    directionOfExcluding = direction;
                } else if (direction == FeatureDirectionKind.IN) {
                    directionOfExcluding = FeatureDirectionKind.OUT;
                } else if (direction == FeatureDirectionKind.OUT) {
                    directionOfExcluding = FeatureDirectionKind.IN;
                } else {
                    directionOfExcluding = direction;
                }
            }
        }
        return directionOfExcluding;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Membership> inheritableMemberships(EList<Namespace> excludedNamespaces, EList<Type> excludedTypes, boolean excludeImplied) {
        EList<Membership> inheritableMemberships = new BasicEList<>();
        var excludingSelf = new HashSet<>();
        excludingSelf.addAll(excludedTypes);
        excludingSelf.add(this);

        EList<Type> supertypes = this.supertypes(excludeImplied);
        supertypes.removeIf(t -> excludingSelf.contains(t));
        var nonPrivateMemberships = supertypes.stream()
                .map(t -> t.nonPrivateMemberships(excludedNamespaces, excludedTypes, excludeImplied))
                .flatMap(Collection::stream)
                .toList();
        inheritableMemberships.addAll(nonPrivateMemberships);
        return inheritableMemberships;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Membership> inheritedMemberships(EList<Namespace> excludedNamespaces, EList<Type> excludedTypes, boolean excludeImplied) {
        // Implementation as specified by the SysMLv2 specification
        // return this.removeRedefinedFeatures(this.inheritableMemberships(excludedNamespaces, excludedTypes,
        // excludeImplied));
        // We should get rid of this custom implementation
        return new MembershipComputer(this, excludedTypes).inheritedMemberships();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Multiplicity> multiplicities() {
        EList<Multiplicity> multiplicities = new BasicEList<>();
        if (this.getMultiplicity() != null) {
            multiplicities.add(this.getMultiplicity());
        } else {
            List<Type> specializationsWithMultiplicities = new ArrayList<>();
            this.multiplicitiesClosure(this, specializationsWithMultiplicities);
            specializationsWithMultiplicities.stream()
                    .filter(t -> t.getMultiplicity() != null)
                    .map(Type::multiplicities)
                    .flatMap(Collection::stream)
                    .forEach(multiplicities::add);
        }
        return multiplicities;
    }

    /**
     * @generated NOT
     */
    private void multiplicitiesClosure(Type currentType, List<Type> accu) {
        var ownedSpecialization = currentType.getOwnedSpecialization();
        for (var specialization : ownedSpecialization) {
            var general = specialization.getGeneral();
            if (general != null && general.getMultiplicity() == null && !accu.contains(general)) {
                this.multiplicitiesClosure(general, accu);
            }
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Membership> nonPrivateMemberships(EList<Namespace> excludedNamespaces, EList<Type> excludedTypes, boolean excludeImplied) {
        EList<Membership> nonPrivateMemberships = new BasicEList<>();
        var publicMemberships = this.membershipsOfVisibility(VisibilityKind.PUBLIC, excludedNamespaces);
        var protectedMemberships = this.membershipsOfVisibility(VisibilityKind.PROTECTED, excludedNamespaces);
        var inheritedMemberships = this.inheritedMemberships(excludedNamespaces, excludedTypes, excludeImplied);
        nonPrivateMemberships.addAll(publicMemberships);
        nonPrivateMemberships.addAll(protectedMemberships);
        nonPrivateMemberships.addAll(inheritedMemberships);
        return nonPrivateMemberships;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Membership> removeRedefinedFeatures(EList<Membership> memberships) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * @generated NOT
     */
    @Override
    protected EList<Membership> getMembership(EList<Namespace> excluded) {
        List<Element> memberships = new ArrayList<>();
        NameConflictingFilter filter = new NameConflictingFilter();
        this.getOwnedMembership().stream()
                .filter(filter)
                .forEach(memberships::add);
        this.importedMemberships(excluded).stream()
                .filter(filter)
                .forEach(memberships::add);
        this.inheritedMemberships(excluded, excluded.stream()
                .filter(Type.class::isInstance)
                .map(Type.class::cast)
                .collect(toCollection(UniqueEList::new)), false)
                .stream()
                .filter(filter)
                .forEach(memberships::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getNamespace_Membership(), memberships.size(), memberships.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean specializes(Type supertype) {
        if (this.isIsConjugated()) {
            Type originalType = this.getOwnedConjugator().getOriginalType();
            return originalType != null && originalType.specializes(supertype);
        } else {
            return this.allSupertypes().contains(supertype);
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean specializesFromLibrary(String libraryTypeName) {
        Membership membership = this.resolve(libraryTypeName);
        if (membership != null) {
            Element memberElement = membership.getMemberElement();
            if (memberElement instanceof Type type) {
                return this.specializes(type);
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Type> supertypes(boolean excludeImplied) {
        EList<Type> supertypes = new BasicEList<>();
        if (this.isIsConjugated()) {
            Type originalType = this.getOwnedConjugator().getOriginalType();
            supertypes.add(originalType);
        } else if (!excludeImplied) {
            this.getOwnedSpecialization().stream()
                    .map(Specialization::getGeneral)
                    .filter(Objects::nonNull)
                    .forEach(supertypes::add);
        } else {
            this.getOwnedSpecialization().stream()
                    .filter(spe -> !spe.isIsImplied())
                    .map(Specialization::getGeneral)
                    .filter(Objects::nonNull)
                    .forEach(supertypes::add);
        }
        return supertypes;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.TYPE__IS_ABSTRACT:
                return this.isIsAbstract();
            case SysmlPackage.TYPE__IS_CONJUGATED:
                return this.isIsConjugated();
            case SysmlPackage.TYPE__IS_SUFFICIENT:
                return this.isIsSufficient();
            case SysmlPackage.TYPE__DIFFERENCING_TYPE:
                return this.getDifferencingType();
            case SysmlPackage.TYPE__DIRECTED_FEATURE:
                return this.getDirectedFeature();
            case SysmlPackage.TYPE__END_FEATURE:
                return this.getEndFeature();
            case SysmlPackage.TYPE__FEATURE:
                return this.getFeature();
            case SysmlPackage.TYPE__FEATURE_MEMBERSHIP:
                return this.getFeatureMembership();
            case SysmlPackage.TYPE__INHERITED_FEATURE:
                return this.getInheritedFeature();
            case SysmlPackage.TYPE__INHERITED_MEMBERSHIP:
                return this.getInheritedMembership();
            case SysmlPackage.TYPE__INPUT:
                return this.getInput();
            case SysmlPackage.TYPE__INTERSECTING_TYPE:
                return this.getIntersectingType();
            case SysmlPackage.TYPE__MULTIPLICITY:
                if (resolve) {
                    return this.getMultiplicity();
                }
                return this.basicGetMultiplicity();
            case SysmlPackage.TYPE__OUTPUT:
                return this.getOutput();
            case SysmlPackage.TYPE__OWNED_CONJUGATOR:
                if (resolve) {
                    return this.getOwnedConjugator();
                }
                return this.basicGetOwnedConjugator();
            case SysmlPackage.TYPE__OWNED_DIFFERENCING:
                return this.getOwnedDifferencing();
            case SysmlPackage.TYPE__OWNED_DISJOINING:
                return this.getOwnedDisjoining();
            case SysmlPackage.TYPE__OWNED_END_FEATURE:
                return this.getOwnedEndFeature();
            case SysmlPackage.TYPE__OWNED_FEATURE:
                return this.getOwnedFeature();
            case SysmlPackage.TYPE__OWNED_FEATURE_MEMBERSHIP:
                return this.getOwnedFeatureMembership();
            case SysmlPackage.TYPE__OWNED_INTERSECTING:
                return this.getOwnedIntersecting();
            case SysmlPackage.TYPE__OWNED_SPECIALIZATION:
                return this.getOwnedSpecialization();
            case SysmlPackage.TYPE__OWNED_UNIONING:
                return this.getOwnedUnioning();
            case SysmlPackage.TYPE__UNIONING_TYPE:
                return this.getUnioningType();
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
            case SysmlPackage.TYPE__IS_ABSTRACT:
                this.setIsAbstract((Boolean) newValue);
                return;
            case SysmlPackage.TYPE__IS_SUFFICIENT:
                this.setIsSufficient((Boolean) newValue);
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
            case SysmlPackage.TYPE__IS_ABSTRACT:
                this.setIsAbstract(IS_ABSTRACT_EDEFAULT);
                return;
            case SysmlPackage.TYPE__IS_SUFFICIENT:
                this.setIsSufficient(IS_SUFFICIENT_EDEFAULT);
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
            case SysmlPackage.TYPE__IS_ABSTRACT:
                return this.isAbstract != IS_ABSTRACT_EDEFAULT;
            case SysmlPackage.TYPE__IS_CONJUGATED:
                return this.isIsConjugated() != IS_CONJUGATED_EDEFAULT;
            case SysmlPackage.TYPE__IS_SUFFICIENT:
                return this.isSufficient != IS_SUFFICIENT_EDEFAULT;
            case SysmlPackage.TYPE__DIFFERENCING_TYPE:
                return !this.getDifferencingType().isEmpty();
            case SysmlPackage.TYPE__DIRECTED_FEATURE:
                return !this.getDirectedFeature().isEmpty();
            case SysmlPackage.TYPE__END_FEATURE:
                return !this.getEndFeature().isEmpty();
            case SysmlPackage.TYPE__FEATURE:
                return !this.getFeature().isEmpty();
            case SysmlPackage.TYPE__FEATURE_MEMBERSHIP:
                return !this.getFeatureMembership().isEmpty();
            case SysmlPackage.TYPE__INHERITED_FEATURE:
                return !this.getInheritedFeature().isEmpty();
            case SysmlPackage.TYPE__INHERITED_MEMBERSHIP:
                return !this.getInheritedMembership().isEmpty();
            case SysmlPackage.TYPE__INPUT:
                return !this.getInput().isEmpty();
            case SysmlPackage.TYPE__INTERSECTING_TYPE:
                return !this.getIntersectingType().isEmpty();
            case SysmlPackage.TYPE__MULTIPLICITY:
                return this.basicGetMultiplicity() != null;
            case SysmlPackage.TYPE__OUTPUT:
                return !this.getOutput().isEmpty();
            case SysmlPackage.TYPE__OWNED_CONJUGATOR:
                return this.basicGetOwnedConjugator() != null;
            case SysmlPackage.TYPE__OWNED_DIFFERENCING:
                return !this.getOwnedDifferencing().isEmpty();
            case SysmlPackage.TYPE__OWNED_DISJOINING:
                return !this.getOwnedDisjoining().isEmpty();
            case SysmlPackage.TYPE__OWNED_END_FEATURE:
                return !this.getOwnedEndFeature().isEmpty();
            case SysmlPackage.TYPE__OWNED_FEATURE:
                return !this.getOwnedFeature().isEmpty();
            case SysmlPackage.TYPE__OWNED_FEATURE_MEMBERSHIP:
                return !this.getOwnedFeatureMembership().isEmpty();
            case SysmlPackage.TYPE__OWNED_INTERSECTING:
                return !this.getOwnedIntersecting().isEmpty();
            case SysmlPackage.TYPE__OWNED_SPECIALIZATION:
                return !this.getOwnedSpecialization().isEmpty();
            case SysmlPackage.TYPE__OWNED_UNIONING:
                return !this.getOwnedUnioning().isEmpty();
            case SysmlPackage.TYPE__UNIONING_TYPE:
                return !this.getUnioningType().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.TYPE___ALL_SUPERTYPES:
                return this.allSupertypes();
            case SysmlPackage.TYPE___DIRECTION_OF__FEATURE:
                return this.directionOf((Feature) arguments.get(0));
            case SysmlPackage.TYPE___DIRECTION_OF_EXCLUDING__FEATURE_ELIST:
                return this.directionOfExcluding((Feature) arguments.get(0), (EList<Type>) arguments.get(1));
            case SysmlPackage.TYPE___INHERITABLE_MEMBERSHIPS__ELIST_ELIST_BOOLEAN:
                return this.inheritableMemberships((EList<Namespace>) arguments.get(0), (EList<Type>) arguments.get(1), (Boolean) arguments.get(2));
            case SysmlPackage.TYPE___INHERITED_MEMBERSHIPS__ELIST_ELIST_BOOLEAN:
                return this.inheritedMemberships((EList<Namespace>) arguments.get(0), (EList<Type>) arguments.get(1), (Boolean) arguments.get(2));
            case SysmlPackage.TYPE___MULTIPLICITIES:
                return this.multiplicities();
            case SysmlPackage.TYPE___NON_PRIVATE_MEMBERSHIPS__ELIST_ELIST_BOOLEAN:
                return this.nonPrivateMemberships((EList<Namespace>) arguments.get(0), (EList<Type>) arguments.get(1), (Boolean) arguments.get(2));
            case SysmlPackage.TYPE___REMOVE_REDEFINED_FEATURES__ELIST:
                return this.removeRedefinedFeatures((EList<Membership>) arguments.get(0));
            case SysmlPackage.TYPE___SPECIALIZES__TYPE:
                return this.specializes((Type) arguments.get(0));
            case SysmlPackage.TYPE___SPECIALIZES_FROM_LIBRARY__STRING:
                return this.specializesFromLibrary((String) arguments.get(0));
            case SysmlPackage.TYPE___SUPERTYPES__BOOLEAN:
                return this.supertypes((Boolean) arguments.get(0));
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
        result.append(" (isAbstract: ");
        result.append(this.isAbstract);
        result.append(", isSufficient: ");
        result.append(this.isSufficient);
        result.append(')');
        return result.toString();
    }

} // TypeImpl
