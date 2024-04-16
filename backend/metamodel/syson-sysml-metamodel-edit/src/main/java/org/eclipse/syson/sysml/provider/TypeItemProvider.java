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
package org.eclipse.syson.sysml.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.Type} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TypeItemProvider extends NamespaceItemProvider {
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TypeItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
     * This returns the property descriptors for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);

            addIsAbstractPropertyDescriptor(object);
            addIsConjugatedPropertyDescriptor(object);
            addIsSufficientPropertyDescriptor(object);
            addDifferencingTypePropertyDescriptor(object);
            addDirectedFeaturePropertyDescriptor(object);
            addEndFeaturePropertyDescriptor(object);
            addFeaturePropertyDescriptor(object);
            addFeatureMembershipPropertyDescriptor(object);
            addInheritedFeaturePropertyDescriptor(object);
            addInheritedMembershipPropertyDescriptor(object);
            addInputPropertyDescriptor(object);
            addIntersectingTypePropertyDescriptor(object);
            addMultiplicityPropertyDescriptor(object);
            addOutputPropertyDescriptor(object);
            addOwnedConjugatorPropertyDescriptor(object);
            addOwnedDifferencingPropertyDescriptor(object);
            addOwnedDisjoiningPropertyDescriptor(object);
            addOwnedEndFeaturePropertyDescriptor(object);
            addOwnedFeaturePropertyDescriptor(object);
            addOwnedFeatureMembershipPropertyDescriptor(object);
            addOwnedIntersectingPropertyDescriptor(object);
            addOwnedSpecializationPropertyDescriptor(object);
            addOwnedUnioningPropertyDescriptor(object);
            addUnioningTypePropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Differencing Type feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addDifferencingTypePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_differencingType_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_differencingType_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_DifferencingType(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Directed Feature feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addDirectedFeaturePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_directedFeature_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_directedFeature_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_DirectedFeature(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the End Feature feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addEndFeaturePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_endFeature_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_endFeature_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_EndFeature(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Feature feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addFeaturePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_feature_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_feature_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_Feature(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Feature Membership feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addFeatureMembershipPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_featureMembership_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_featureMembership_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_FeatureMembership(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Inherited Feature feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addInheritedFeaturePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_inheritedFeature_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_inheritedFeature_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_InheritedFeature(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Inherited Membership feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addInheritedMembershipPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_inheritedMembership_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_inheritedMembership_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_InheritedMembership(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Input feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addInputPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_input_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_input_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_Input(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Intersecting Type feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addIntersectingTypePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_intersectingType_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_intersectingType_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_IntersectingType(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Is Abstract feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addIsAbstractPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_isAbstract_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_isAbstract_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_IsAbstract(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Is Conjugated feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addIsConjugatedPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_isConjugated_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_isConjugated_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_IsConjugated(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Is Sufficient feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addIsSufficientPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_isSufficient_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_isSufficient_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_IsSufficient(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Multiplicity feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addMultiplicityPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_multiplicity_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_multiplicity_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_Multiplicity(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Output feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOutputPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_output_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_output_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_Output(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owned Conjugator feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwnedConjugatorPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_ownedConjugator_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_ownedConjugator_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_OwnedConjugator(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owned Differencing feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwnedDifferencingPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_ownedDifferencing_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_ownedDifferencing_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_OwnedDifferencing(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owned Disjoining feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwnedDisjoiningPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_ownedDisjoining_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_ownedDisjoining_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_OwnedDisjoining(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owned End Feature feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwnedEndFeaturePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_ownedEndFeature_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_ownedEndFeature_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_OwnedEndFeature(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owned Feature feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwnedFeaturePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_ownedFeature_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_ownedFeature_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_OwnedFeature(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owned Feature Membership feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwnedFeatureMembershipPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_ownedFeatureMembership_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_ownedFeatureMembership_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_OwnedFeatureMembership(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owned Intersecting feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwnedIntersectingPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_ownedIntersecting_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_ownedIntersecting_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_OwnedIntersecting(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owned Specialization feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwnedSpecializationPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_ownedSpecialization_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_ownedSpecialization_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_OwnedSpecialization(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owned Unioning feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwnedUnioningPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_ownedUnioning_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_ownedUnioning_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_OwnedUnioning(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Unioning Type feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addUnioningTypePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Type_unioningType_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Type_unioningType_feature", "_UI_Type_type"),
                 SysmlPackage.eINSTANCE.getType_UnioningType(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This returns Type.svg.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/Type.svg"));
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((Type)object).getName();
        return label == null || label.length() == 0 ?
            getString("_UI_Type_type") :
            getString("_UI_Type_type") + " " + label;
    }


    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached
     * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void notifyChanged(Notification notification) {
        updateChildren(notification);

        switch (notification.getFeatureID(Type.class)) {
            case SysmlPackage.TYPE__IS_ABSTRACT:
            case SysmlPackage.TYPE__IS_CONJUGATED:
            case SysmlPackage.TYPE__IS_SUFFICIENT:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
        }
        super.notifyChanged(notification);
    }

    /**
     * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
     * that can be created under this object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
        super.collectNewChildDescriptors(newChildDescriptors, object);
    }

}
