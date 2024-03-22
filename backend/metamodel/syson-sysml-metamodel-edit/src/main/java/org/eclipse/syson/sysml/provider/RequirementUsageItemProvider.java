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

import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.RequirementUsage} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class RequirementUsageItemProvider extends ConstraintUsageItemProvider {
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RequirementUsageItemProvider(AdapterFactory adapterFactory) {
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

            addReqIdPropertyDescriptor(object);
            addTextPropertyDescriptor(object);
            addActorParameterPropertyDescriptor(object);
            addAssumedConstraintPropertyDescriptor(object);
            addFramedConcernPropertyDescriptor(object);
            addRequiredConstraintPropertyDescriptor(object);
            addRequirementDefinitionPropertyDescriptor(object);
            addStakeholderParameterPropertyDescriptor(object);
            addSubjectParameterPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Actor Parameter feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addActorParameterPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_RequirementUsage_actorParameter_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_RequirementUsage_actorParameter_feature", "_UI_RequirementUsage_type"),
                 SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Assumed Constraint feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addAssumedConstraintPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_RequirementUsage_assumedConstraint_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_RequirementUsage_assumedConstraint_feature", "_UI_RequirementUsage_type"),
                 SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Framed Concern feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addFramedConcernPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_RequirementUsage_framedConcern_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_RequirementUsage_framedConcern_feature", "_UI_RequirementUsage_type"),
                 SysmlPackage.eINSTANCE.getRequirementUsage_FramedConcern(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Req Id feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addReqIdPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_RequirementUsage_reqId_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_RequirementUsage_reqId_feature", "_UI_RequirementUsage_type"),
                 SysmlPackage.eINSTANCE.getRequirementUsage_ReqId(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Required Constraint feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addRequiredConstraintPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_RequirementUsage_requiredConstraint_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_RequirementUsage_requiredConstraint_feature", "_UI_RequirementUsage_type"),
                 SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Requirement Definition feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addRequirementDefinitionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_RequirementUsage_requirementDefinition_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_RequirementUsage_requirementDefinition_feature", "_UI_RequirementUsage_type"),
                 SysmlPackage.eINSTANCE.getRequirementUsage_RequirementDefinition(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Stakeholder Parameter feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addStakeholderParameterPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_RequirementUsage_stakeholderParameter_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_RequirementUsage_stakeholderParameter_feature", "_UI_RequirementUsage_type"),
                 SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Subject Parameter feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addSubjectParameterPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_RequirementUsage_subjectParameter_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_RequirementUsage_subjectParameter_feature", "_UI_RequirementUsage_type"),
                 SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Text feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addTextPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_RequirementUsage_text_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_RequirementUsage_text_feature", "_UI_RequirementUsage_type"),
                 SysmlPackage.eINSTANCE.getRequirementUsage_Text(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This returns RequirementUsage.svg.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/RequirementUsage.svg"));
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((RequirementUsage)object).getName();
        return label == null || label.length() == 0 ?
            getString("_UI_RequirementUsage_type") :
            getString("_UI_RequirementUsage_type") + " " + label;
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

        switch (notification.getFeatureID(RequirementUsage.class)) {
            case SysmlPackage.REQUIREMENT_USAGE__REQ_ID:
            case SysmlPackage.REQUIREMENT_USAGE__TEXT:
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
