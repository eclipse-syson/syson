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
package org.eclipse.syson.sysml.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.Feature} object. <!-- begin-user-doc --> <!--
 * end-user-doc -->
 *
 * @generated
 */
public class FeatureItemProvider extends TypeItemProvider {
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public FeatureItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
     * This returns the property descriptors for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
        if (this.itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);

            this.addDirectionPropertyDescriptor(object);
            this.addIsCompositePropertyDescriptor(object);
            this.addIsDerivedPropertyDescriptor(object);
            this.addIsEndPropertyDescriptor(object);
            this.addIsNonuniquePropertyDescriptor(object);
            this.addIsOrderedPropertyDescriptor(object);
            this.addIsPortionPropertyDescriptor(object);
            this.addIsReadOnlyPropertyDescriptor(object);
            this.addIsUniquePropertyDescriptor(object);
            this.addChainingFeaturePropertyDescriptor(object);
            this.addCrossFeaturePropertyDescriptor(object);
            this.addEndOwningTypePropertyDescriptor(object);
            this.addFeatureTargetPropertyDescriptor(object);
            this.addFeaturingTypePropertyDescriptor(object);
            this.addOwnedCrossSubsettingPropertyDescriptor(object);
            this.addOwnedFeatureChainingPropertyDescriptor(object);
            this.addOwnedFeatureInvertingPropertyDescriptor(object);
            this.addOwnedRedefinitionPropertyDescriptor(object);
            this.addOwnedReferenceSubsettingPropertyDescriptor(object);
            this.addOwnedSubsettingPropertyDescriptor(object);
            this.addOwnedTypeFeaturingPropertyDescriptor(object);
            this.addOwnedTypingPropertyDescriptor(object);
            this.addOwningFeatureMembershipPropertyDescriptor(object);
            this.addOwningTypePropertyDescriptor(object);
            this.addTypePropertyDescriptor(object);
            this.addValuationPropertyDescriptor(object);
        }
        return this.itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Chaining Feature feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addChainingFeaturePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_chainingFeature_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_chainingFeature_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_ChainingFeature(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Cross Feature feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addCrossFeaturePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_crossFeature_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_crossFeature_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_CrossFeature(),
                false,
                false,
                false,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Direction feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addDirectionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_direction_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_direction_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_Direction(),
                true,
                false,
                false,
                ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the End Owning Type feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addEndOwningTypePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_endOwningType_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_endOwningType_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_EndOwningType(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Feature Target feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addFeatureTargetPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_featureTarget_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_featureTarget_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_FeatureTarget(),
                false,
                false,
                false,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Featuring Type feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addFeaturingTypePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_featuringType_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_featuringType_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_FeaturingType(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Cross Subsetting feature. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    protected void addOwnedCrossSubsettingPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_ownedCrossSubsetting_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_ownedCrossSubsetting_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_OwnedCrossSubsetting(),
                false,
                false,
                false,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Is Composite feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addIsCompositePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_isComposite_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_isComposite_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_IsComposite(),
                true,
                false,
                false,
                ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Is Derived feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addIsDerivedPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_isDerived_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_isDerived_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_IsDerived(),
                true,
                false,
                false,
                ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Is End feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addIsEndPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_isEnd_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_isEnd_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_IsEnd(),
                true,
                false,
                false,
                ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Is Nonunique feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addIsNonuniquePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_isNonunique_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_isNonunique_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_IsNonunique(),
                true,
                false,
                false,
                ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Is Ordered feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addIsOrderedPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_isOrdered_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_isOrdered_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_IsOrdered(),
                true,
                false,
                false,
                ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Is Portion feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addIsPortionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_isPortion_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_isPortion_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_IsPortion(),
                true,
                false,
                false,
                ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Is Read Only feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addIsReadOnlyPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_isReadOnly_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_isReadOnly_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_IsReadOnly(),
                true,
                false,
                false,
                ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Is Unique feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addIsUniquePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_isUnique_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_isUnique_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_IsUnique(),
                true,
                false,
                false,
                ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Feature Chaining feature. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    protected void addOwnedFeatureChainingPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_ownedFeatureChaining_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_ownedFeatureChaining_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_OwnedFeatureChaining(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Feature Inverting feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedFeatureInvertingPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_ownedFeatureInverting_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_ownedFeatureInverting_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_OwnedFeatureInverting(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Redefinition feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedRedefinitionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_ownedRedefinition_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_ownedRedefinition_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_OwnedRedefinition(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Reference Subsetting feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedReferenceSubsettingPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_ownedReferenceSubsetting_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_ownedReferenceSubsetting_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_OwnedReferenceSubsetting(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Subsetting feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedSubsettingPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_ownedSubsetting_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_ownedSubsetting_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_OwnedSubsetting(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Type Featuring feature. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    protected void addOwnedTypeFeaturingPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_ownedTypeFeaturing_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_ownedTypeFeaturing_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_OwnedTypeFeaturing(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Typing feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedTypingPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_ownedTyping_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_ownedTyping_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_OwnedTyping(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owning Feature Membership feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    protected void addOwningFeatureMembershipPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_owningFeatureMembership_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_owningFeatureMembership_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_OwningFeatureMembership(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owning Type feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwningTypePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_owningType_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_owningType_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_OwningType(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Type feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addTypePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_type_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_type_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_Type(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Valuation feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addValuationPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Feature_valuation_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Feature_valuation_feature", "_UI_Feature_type"),
                SysmlPackage.eINSTANCE.getFeature_Valuation(),
                false,
                false,
                false,
                null,
                null,
                null));
    }

    /**
     * This returns Feature.svg. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Object getImage(Object object) {
        return this.overlayImage(object, this.getResourceLocator().getImage("full/obj16/Feature.svg"));
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((Feature) object).getName();
        return label == null || label.length() == 0 ? this.getString("_UI_Feature_type") : this.getString("_UI_Feature_type") + " " + label;
    }

    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached children and by creating
     * a viewer notification, which it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    @Override
    public void notifyChanged(Notification notification) {
        this.updateChildren(notification);

        switch (notification.getFeatureID(Feature.class)) {
            case SysmlPackage.FEATURE__DIRECTION:
            case SysmlPackage.FEATURE__IS_COMPOSITE:
            case SysmlPackage.FEATURE__IS_DERIVED:
            case SysmlPackage.FEATURE__IS_END:
            case SysmlPackage.FEATURE__IS_NONUNIQUE:
            case SysmlPackage.FEATURE__IS_ORDERED:
            case SysmlPackage.FEATURE__IS_PORTION:
            case SysmlPackage.FEATURE__IS_READ_ONLY:
            case SysmlPackage.FEATURE__IS_UNIQUE:
                this.fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
        }
        super.notifyChanged(notification);
    }

    /**
     * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children that can be created
     * under this object. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
        super.collectNewChildDescriptors(newChildDescriptors, object);
    }

}
