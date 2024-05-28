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
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.RequirementConstraintMembership} object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 *
 * @generated
 */
public class RequirementConstraintMembershipItemProvider extends FeatureMembershipItemProvider {
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public RequirementConstraintMembershipItemProvider(AdapterFactory adapterFactory) {
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

            this.addKindPropertyDescriptor(object);
            this.addOwnedConstraintPropertyDescriptor(object);
            this.addReferencedConstraintPropertyDescriptor(object);
        }
        return this.itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Kind feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addKindPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_RequirementConstraintMembership_kind_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_RequirementConstraintMembership_kind_feature", "_UI_RequirementConstraintMembership_type"),
                SysmlPackage.eINSTANCE.getRequirementConstraintMembership_Kind(),
                true,
                false,
                false,
                ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Constraint feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedConstraintPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_RequirementConstraintMembership_ownedConstraint_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_RequirementConstraintMembership_ownedConstraint_feature", "_UI_RequirementConstraintMembership_type"),
                SysmlPackage.eINSTANCE.getRequirementConstraintMembership_OwnedConstraint(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Referenced Constraint feature. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    protected void addReferencedConstraintPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_RequirementConstraintMembership_referencedConstraint_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_RequirementConstraintMembership_referencedConstraint_feature", "_UI_RequirementConstraintMembership_type"),
                SysmlPackage.eINSTANCE.getRequirementConstraintMembership_ReferencedConstraint(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This returns RequirementConstraintMembership.svg. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Object getImage(Object object) {
        return this.overlayImage(object, this.getResourceLocator().getImage("full/obj16/RequirementConstraintMembership.svg"));
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((RequirementConstraintMembership) object).getName();
        return label == null || label.length() == 0 ? this.getString("_UI_RequirementConstraintMembership_type") : this.getString("_UI_RequirementConstraintMembership_type") + " " + label;
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

        switch (notification.getFeatureID(RequirementConstraintMembership.class)) {
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP__KIND:
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

    /**
     * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
        Object childFeature = feature;
        Object childObject = child;

        boolean qualify = childFeature == SysmlPackage.eINSTANCE.getElement_OwnedRelationship() ||
                childFeature == SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement();

        if (qualify) {
            return this.getString("_UI_CreateChild_text2",
                    new Object[] { this.getTypeText(childObject), this.getFeatureText(childFeature), this.getTypeText(owner) });
        }
        return super.getCreateChildText(owner, feature, child, selection);
    }

}
