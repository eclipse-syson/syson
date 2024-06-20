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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Feature</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Feature#getDirection <em>Direction</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#isIsComposite <em>Is Composite</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#isIsDerived <em>Is Derived</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#isIsEnd <em>Is End</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#isIsNonunique <em>Is Nonunique</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#isIsOrdered <em>Is Ordered</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#isIsPortion <em>Is Portion</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#isIsReadOnly <em>Is Read Only</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#isIsUnique <em>Is Unique</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#getChainingFeature <em>Chaining Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#getEndOwningType <em>End Owning Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#getFeaturingType <em>Featuring Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#getOwnedFeatureChaining <em>Owned Feature Chaining</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#getOwnedFeatureInverting <em>Owned Feature Inverting</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#getOwnedRedefinition <em>Owned Redefinition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#getOwnedReferenceSubsetting <em>Owned Reference Subsetting</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#getOwnedSubsetting <em>Owned Subsetting</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#getOwnedTypeFeaturing <em>Owned Type Featuring</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#getOwnedTyping <em>Owned Typing</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#getOwningFeatureMembership <em>Owning Feature Membership</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#getOwningType <em>Owning Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#getType <em>Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Feature#getValuation <em>Valuation</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getFeature()
 * @model
 * @generated
 */
public interface Feature extends Type {
    /**
     * Returns the value of the '<em><b>Chaining Feature</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Feature}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Chaining Feature</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_ChainingFeature()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Feature> getChainingFeature();

    /**
     * Returns the value of the '<em><b>Direction</b></em>' attribute. The literals are from the enumeration
     * {@link org.eclipse.syson.sysml.FeatureDirectionKind}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Direction</em>' attribute.
     * @see org.eclipse.syson.sysml.FeatureDirectionKind
     * @see #isSetDirection()
     * @see #unsetDirection()
     * @see #setDirection(FeatureDirectionKind)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_Direction()
     * @model unsettable="true" ordered="false"
     * @generated
     */
    FeatureDirectionKind getDirection();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Feature#getDirection <em>Direction</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Direction</em>' attribute.
     * @see org.eclipse.syson.sysml.FeatureDirectionKind
     * @see #getDirection()
     * @generated
     */
    void setDirection(FeatureDirectionKind value);

    /**
     * Unsets the value of the '{@link org.eclipse.syson.sysml.Feature#getDirection <em>Direction</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #isSetDirection()
     * @see #getDirection()
     * @see #setDirection(FeatureDirectionKind)
     * @generated
     */
    void unsetDirection();

    /**
     * Returns whether the value of the '{@link org.eclipse.syson.sysml.Feature#getDirection <em>Direction</em>}'
     * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return whether the value of the '<em>Direction</em>' attribute is set.
     * @see #unsetDirection()
     * @see #getDirection()
     * @see #setDirection(FeatureDirectionKind)
     * @generated
     */
    boolean isSetDirection();

    /**
     * Returns the value of the '<em><b>End Owning Type</b></em>' reference. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Type#getOwnedEndFeature <em>Owned End Feature</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Owning Type</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_EndOwningType()
     * @see org.eclipse.syson.sysml.Type#getOwnedEndFeature
     * @model opposite="ownedEndFeature" transient="true" changeable="false" volatile="true" derived="true"
     *        ordered="false" annotation="subsets"
     * @generated
     */
    Type getEndOwningType();

    /**
     * Returns the value of the '<em><b>Featuring Type</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Type}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Featuring Type</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_FeaturingType()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Type> getFeaturingType();

    /**
     * Returns the value of the '<em><b>Is Composite</b></em>' attribute. The default value is <code>"false"</code>.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Composite</em>' attribute.
     * @see #setIsComposite(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_IsComposite()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsComposite();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Feature#isIsComposite <em>Is Composite</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is Composite</em>' attribute.
     * @see #isIsComposite()
     * @generated
     */
    void setIsComposite(boolean value);

    /**
     * Returns the value of the '<em><b>Is Derived</b></em>' attribute. The default value is <code>"false"</code>. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Derived</em>' attribute.
     * @see #setIsDerived(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_IsDerived()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsDerived();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Feature#isIsDerived <em>Is Derived</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is Derived</em>' attribute.
     * @see #isIsDerived()
     * @generated
     */
    void setIsDerived(boolean value);

    /**
     * Returns the value of the '<em><b>Is End</b></em>' attribute. The default value is <code>"false"</code>. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is End</em>' attribute.
     * @see #setIsEnd(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_IsEnd()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsEnd();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Feature#isIsEnd <em>Is End</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is End</em>' attribute.
     * @see #isIsEnd()
     * @generated
     */
    void setIsEnd(boolean value);

    /**
     * Returns the value of the '<em><b>Is Nonunique</b></em>' attribute. The default value is <code>"false"</code>.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Nonunique</em>' attribute.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_IsNonunique()
     * @model default="false" required="true" transient="true" changeable="false" volatile="true" derived="true"
     *        ordered="false"
     * @generated
     */
    boolean isIsNonunique();

    /**
     * Returns the value of the '<em><b>Is Ordered</b></em>' attribute. The default value is <code>"false"</code>. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Ordered</em>' attribute.
     * @see #setIsOrdered(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_IsOrdered()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsOrdered();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Feature#isIsOrdered <em>Is Ordered</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is Ordered</em>' attribute.
     * @see #isIsOrdered()
     * @generated
     */
    void setIsOrdered(boolean value);

    /**
     * Returns the value of the '<em><b>Is Portion</b></em>' attribute. The default value is <code>"false"</code>. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Portion</em>' attribute.
     * @see #setIsPortion(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_IsPortion()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsPortion();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Feature#isIsPortion <em>Is Portion</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is Portion</em>' attribute.
     * @see #isIsPortion()
     * @generated
     */
    void setIsPortion(boolean value);

    /**
     * Returns the value of the '<em><b>Is Read Only</b></em>' attribute. The default value is <code>"false"</code>.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Read Only</em>' attribute.
     * @see #setIsReadOnly(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_IsReadOnly()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsReadOnly();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Feature#isIsReadOnly <em>Is Read Only</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is Read Only</em>' attribute.
     * @see #isIsReadOnly()
     * @generated
     */
    void setIsReadOnly(boolean value);

    /**
     * Returns the value of the '<em><b>Is Unique</b></em>' attribute. The default value is <code>"true"</code>. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Unique</em>' attribute.
     * @see #setIsUnique(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_IsUnique()
     * @model default="true" required="true" ordered="false"
     * @generated
     */
    boolean isIsUnique();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Feature#isIsUnique <em>Is Unique</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is Unique</em>' attribute.
     * @see #isIsUnique()
     * @generated
     */
    void setIsUnique(boolean value);

    /**
     * Returns the value of the '<em><b>Owned Feature Chaining</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.FeatureChaining}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.FeatureChaining#getFeatureChained <em>Feature Chained</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Feature Chaining</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_OwnedFeatureChaining()
     * @see org.eclipse.syson.sysml.FeatureChaining#getFeatureChained
     * @model opposite="featureChained" transient="true" changeable="false" volatile="true" derived="true"
     *        annotation="subsets"
     * @generated
     */
    EList<FeatureChaining> getOwnedFeatureChaining();

    /**
     * Returns the value of the '<em><b>Owned Feature Inverting</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.FeatureInverting}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.FeatureInverting#getOwningFeature <em>Owning Feature</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Feature Inverting</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_OwnedFeatureInverting()
     * @see org.eclipse.syson.sysml.FeatureInverting#getOwningFeature
     * @model opposite="owningFeature" transient="true" changeable="false" volatile="true" derived="true"
     *        ordered="false" annotation="subsets"
     * @generated
     */
    EList<FeatureInverting> getOwnedFeatureInverting();

    /**
     * Returns the value of the '<em><b>Owned Redefinition</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Redefinition}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Redefinition</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_OwnedRedefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    EList<Redefinition> getOwnedRedefinition();

    /**
     * Returns the value of the '<em><b>Owned Reference Subsetting</b></em>' reference. It is bidirectional and its
     * opposite is '{@link org.eclipse.syson.sysml.ReferenceSubsetting#getReferencingFeature <em>Referencing
     * Feature</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Reference Subsetting</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_OwnedReferenceSubsetting()
     * @see org.eclipse.syson.sysml.ReferenceSubsetting#getReferencingFeature
     * @model opposite="referencingFeature" transient="true" changeable="false" volatile="true" derived="true"
     *        ordered="false" annotation="subsets"
     * @generated
     */
    ReferenceSubsetting getOwnedReferenceSubsetting();

    /**
     * Returns the value of the '<em><b>Owned Subsetting</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Subsetting}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Subsetting#getOwningFeature <em>Owning Feature</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Subsetting</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_OwnedSubsetting()
     * @see org.eclipse.syson.sysml.Subsetting#getOwningFeature
     * @model opposite="owningFeature" transient="true" changeable="false" volatile="true" derived="true"
     *        ordered="false" annotation="subsets"
     * @generated
     */
    EList<Subsetting> getOwnedSubsetting();

    /**
     * Returns the value of the '<em><b>Owned Type Featuring</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.TypeFeaturing}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.TypeFeaturing#getOwningFeatureOfType <em>Owning Feature Of Type</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Type Featuring</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_OwnedTypeFeaturing()
     * @see org.eclipse.syson.sysml.TypeFeaturing#getOwningFeatureOfType
     * @model opposite="owningFeatureOfType" transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<TypeFeaturing> getOwnedTypeFeaturing();

    /**
     * Returns the value of the '<em><b>Owned Typing</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.FeatureTyping}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.FeatureTyping#getOwningFeature <em>Owning Feature</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Typing</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_OwnedTyping()
     * @see org.eclipse.syson.sysml.FeatureTyping#getOwningFeature
     * @model opposite="owningFeature" transient="true" changeable="false" volatile="true" derived="true"
     *        annotation="subsets"
     * @generated
     */
    EList<FeatureTyping> getOwnedTyping();

    /**
     * Returns the value of the '<em><b>Owning Feature Membership</b></em>' reference. It is bidirectional and its
     * opposite is '{@link org.eclipse.syson.sysml.FeatureMembership#getOwnedMemberFeature <em>Owned Member
     * Feature</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owning Feature Membership</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_OwningFeatureMembership()
     * @see org.eclipse.syson.sysml.FeatureMembership#getOwnedMemberFeature
     * @model opposite="ownedMemberFeature" transient="true" changeable="false" volatile="true" derived="true"
     *        ordered="false" annotation="subsets"
     * @generated
     */
    FeatureMembership getOwningFeatureMembership();

    /**
     * Returns the value of the '<em><b>Owning Type</b></em>' reference. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Type#getOwnedFeature <em>Owned Feature</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Owning Type</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_OwningType()
     * @see org.eclipse.syson.sysml.Type#getOwnedFeature
     * @model opposite="ownedFeature" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     *        annotation="subsets"
     * @generated
     */
    Type getOwningType();

    /**
     * Returns the value of the '<em><b>Type</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Type}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Type</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_Type()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Type> getType();

    /**
     * Returns the value of the '<em><b>Valuation</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Valuation</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeature_Valuation()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    FeatureValue getValuation();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" typeRequired="true" typeOrdered="false"
     * @generated
     */
    FeatureDirectionKind directionFor(Type type);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" ordered="false" typeOrdered="false"
     * @generated
     */
    boolean isFeaturedWithin(Type type);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false"
     * @generated
     */
    Feature namingFeature();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" ordered="false" redefinedFeatureRequired="true" redefinedFeatureOrdered="false"
     * @generated
     */
    boolean redefines(Feature redefinedFeature);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" ordered="false" libraryFeatureNameRequired="true" libraryFeatureNameOrdered="false"
     * @generated
     */
    boolean redefinesFromLibrary(String libraryFeatureName);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" ordered="false" firstRequired="true" firstOrdered="false" secondRequired="true"
     *        secondOrdered="false"
     * @generated
     */
    boolean subsetsChain(Feature first, Feature second);

} // Feature
