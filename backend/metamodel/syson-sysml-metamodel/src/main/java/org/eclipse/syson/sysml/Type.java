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
package org.eclipse.syson.sysml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Type</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Type#isIsAbstract <em>Is Abstract</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#isIsConjugated <em>Is Conjugated</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#isIsSufficient <em>Is Sufficient</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getDifferencingType <em>Differencing Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getDirectedFeature <em>Directed Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getEndFeature <em>End Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getFeature <em>Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getFeatureMembership <em>Feature Membership</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getInheritedFeature <em>Inherited Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getInheritedMembership <em>Inherited Membership</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getInput <em>Input</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getIntersectingType <em>Intersecting Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getMultiplicity <em>Multiplicity</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getOutput <em>Output</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getOwnedConjugator <em>Owned Conjugator</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getOwnedDifferencing <em>Owned Differencing</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getOwnedDisjoining <em>Owned Disjoining</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getOwnedEndFeature <em>Owned End Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getOwnedFeature <em>Owned Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getOwnedFeatureMembership <em>Owned Feature Membership</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getOwnedIntersecting <em>Owned Intersecting</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getOwnedSpecialization <em>Owned Specialization</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getOwnedUnioning <em>Owned Unioning</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Type#getUnioningType <em>Unioning Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getType()
 * @model
 * @generated
 */
public interface Type extends Namespace {
    /**
     * Returns the value of the '<em><b>Differencing Type</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Type}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Differencing Type</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_DifferencingType()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Type> getDifferencingType();

    /**
     * Returns the value of the '<em><b>Directed Feature</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Feature}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Directed Feature</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_DirectedFeature()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<Feature> getDirectedFeature();

    /**
     * Returns the value of the '<em><b>End Feature</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Feature}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Feature</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_EndFeature()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<Feature> getEndFeature();

    /**
     * Returns the value of the '<em><b>Feature</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Feature}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Feature</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_Feature()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<Feature> getFeature();

    /**
     * Returns the value of the '<em><b>Feature Membership</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.FeatureMembership}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Feature Membership</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_FeatureMembership()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<FeatureMembership> getFeatureMembership();

    /**
     * Returns the value of the '<em><b>Inherited Feature</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Feature}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Inherited Feature</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_InheritedFeature()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<Feature> getInheritedFeature();

    /**
     * Returns the value of the '<em><b>Inherited Membership</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Membership}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Inherited Membership</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_InheritedMembership()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<Membership> getInheritedMembership();

    /**
     * Returns the value of the '<em><b>Input</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Feature}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Input</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_Input()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<Feature> getInput();

    /**
     * Returns the value of the '<em><b>Intersecting Type</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Type}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Intersecting Type</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_IntersectingType()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Type> getIntersectingType();

    /**
     * Returns the value of the '<em><b>Is Abstract</b></em>' attribute. The default value is <code>"false"</code>. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Abstract</em>' attribute.
     * @see #setIsAbstract(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_IsAbstract()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsAbstract();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Type#isIsAbstract <em>Is Abstract</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is Abstract</em>' attribute.
     * @see #isIsAbstract()
     * @generated
     */
    void setIsAbstract(boolean value);

    /**
     * Returns the value of the '<em><b>Is Conjugated</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Conjugated</em>' attribute.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_IsConjugated()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    boolean isIsConjugated();

    /**
     * Returns the value of the '<em><b>Is Sufficient</b></em>' attribute. The default value is <code>"false"</code>.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Sufficient</em>' attribute.
     * @see #setIsSufficient(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_IsSufficient()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsSufficient();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Type#isIsSufficient <em>Is Sufficient</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is Sufficient</em>' attribute.
     * @see #isIsSufficient()
     * @generated
     */
    void setIsSufficient(boolean value);

    /**
     * Returns the value of the '<em><b>Multiplicity</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Multiplicity</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_Multiplicity()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    Multiplicity getMultiplicity();

    /**
     * Returns the value of the '<em><b>Output</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Feature}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Output</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_Output()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<Feature> getOutput();

    /**
     * Returns the value of the '<em><b>Owned Conjugator</b></em>' reference. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Conjugation#getOwningType <em>Owning Type</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Owned Conjugator</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_OwnedConjugator()
     * @see org.eclipse.syson.sysml.Conjugation#getOwningType
     * @model opposite="owningType" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     *        annotation="subsets"
     * @generated
     */
    Conjugation getOwnedConjugator();

    /**
     * Returns the value of the '<em><b>Owned Differencing</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Differencing}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Differencing#getTypeDifferenced <em>Type Differenced</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Differencing</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_OwnedDifferencing()
     * @see org.eclipse.syson.sysml.Differencing#getTypeDifferenced
     * @model opposite="typeDifferenced" transient="true" changeable="false" volatile="true" derived="true"
     *        annotation="subsets"
     * @generated
     */
    EList<Differencing> getOwnedDifferencing();

    /**
     * Returns the value of the '<em><b>Owned Disjoining</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Disjoining}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Disjoining#getOwningType <em>Owning Type</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Owned Disjoining</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_OwnedDisjoining()
     * @see org.eclipse.syson.sysml.Disjoining#getOwningType
     * @model opposite="owningType" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     *        annotation="subsets"
     * @generated
     */
    EList<Disjoining> getOwnedDisjoining();

    /**
     * Returns the value of the '<em><b>Owned End Feature</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Feature}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Feature#getEndOwningType <em>End Owning Type</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Owned End Feature</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_OwnedEndFeature()
     * @see org.eclipse.syson.sysml.Feature#getEndOwningType
     * @model opposite="endOwningType" transient="true" changeable="false" volatile="true" derived="true"
     *        annotation="subsets"
     * @generated
     */
    EList<Feature> getOwnedEndFeature();

    /**
     * Returns the value of the '<em><b>Owned Feature</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Feature}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Feature#getOwningType <em>Owning Type</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Owned Feature</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_OwnedFeature()
     * @see org.eclipse.syson.sysml.Feature#getOwningType
     * @model opposite="owningType" transient="true" changeable="false" volatile="true" derived="true"
     *        annotation="subsets"
     * @generated
     */
    EList<Feature> getOwnedFeature();

    /**
     * Returns the value of the '<em><b>Owned Feature Membership</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.FeatureMembership}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.FeatureMembership#getOwningType <em>Owning Type</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Feature Membership</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_OwnedFeatureMembership()
     * @see org.eclipse.syson.sysml.FeatureMembership#getOwningType
     * @model opposite="owningType" transient="true" changeable="false" volatile="true" derived="true"
     *        annotation="subsets"
     * @generated
     */
    EList<FeatureMembership> getOwnedFeatureMembership();

    /**
     * Returns the value of the '<em><b>Owned Intersecting</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Intersecting}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Intersecting#getTypeIntersected <em>Type Intersected</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Intersecting</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_OwnedIntersecting()
     * @see org.eclipse.syson.sysml.Intersecting#getTypeIntersected
     * @model opposite="typeIntersected" transient="true" changeable="false" volatile="true" derived="true"
     *        annotation="subsets"
     * @generated
     */
    EList<Intersecting> getOwnedIntersecting();

    /**
     * Returns the value of the '<em><b>Owned Specialization</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Specialization}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Specialization#getOwningType <em>Owning Type</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Owned Specialization</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_OwnedSpecialization()
     * @see org.eclipse.syson.sysml.Specialization#getOwningType
     * @model opposite="owningType" transient="true" changeable="false" volatile="true" derived="true"
     *        annotation="subsets"
     * @generated
     */
    EList<Specialization> getOwnedSpecialization();

    /**
     * Returns the value of the '<em><b>Owned Unioning</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Unioning}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Unioning#getTypeUnioned <em>Type Unioned</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Owned Unioning</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_OwnedUnioning()
     * @see org.eclipse.syson.sysml.Unioning#getTypeUnioned
     * @model opposite="typeUnioned" transient="true" changeable="false" volatile="true" derived="true"
     *        annotation="subsets"
     * @generated
     */
    EList<Unioning> getOwnedUnioning();

    /**
     * Returns the value of the '<em><b>Unioning Type</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Type}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Unioning Type</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getType_UnioningType()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Type> getUnioningType();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false"
     * @generated
     */
    EList<Type> allSupertypes();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" featureRequired="true" featureOrdered="false"
     * @generated
     */
    FeatureDirectionKind directionOf(Feature feature);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" featureRequired="true" featureOrdered="false" excludedMany="true" excludedOrdered="false"
     * @generated
     */
    FeatureDirectionKind directionOfExcluding(Feature feature, EList<Type> excluded);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model excludedMany="true" excludedOrdered="false"
     * @generated
     */
    EList<Membership> inheritedMemberships(EList<Type> excluded);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" ordered="false" supertypeRequired="true" supertypeOrdered="false"
     * @generated
     */
    boolean specializes(Type supertype);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" ordered="false" libraryTypeNameRequired="true" libraryTypeNameOrdered="false"
     * @generated
     */
    boolean specializesFromLibrary(String libraryTypeName);

} // Type
