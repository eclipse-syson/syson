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
import org.eclipse.syson.sysml.SuccessionFlowConnectionUsage;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.SuccessionFlowConnectionUsage} object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 *
 * @generated
 */
public class SuccessionFlowConnectionUsageItemProvider extends FlowConnectionUsageItemProvider {
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public SuccessionFlowConnectionUsageItemProvider(AdapterFactory adapterFactory) {
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

            this.addEffectStepPropertyDescriptor(object);
            this.addGuardExpressionPropertyDescriptor(object);
            this.addTransitionStepPropertyDescriptor(object);
            this.addTriggerStepPropertyDescriptor(object);
        }
        return this.itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Effect Step feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addEffectStepPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Succession_effectStep_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Succession_effectStep_feature", "_UI_Succession_type"),
                SysmlPackage.eINSTANCE.getSuccession_EffectStep(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Guard Expression feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addGuardExpressionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Succession_guardExpression_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Succession_guardExpression_feature", "_UI_Succession_type"),
                SysmlPackage.eINSTANCE.getSuccession_GuardExpression(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Transition Step feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addTransitionStepPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Succession_transitionStep_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Succession_transitionStep_feature", "_UI_Succession_type"),
                SysmlPackage.eINSTANCE.getSuccession_TransitionStep(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Trigger Step feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addTriggerStepPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Succession_triggerStep_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Succession_triggerStep_feature", "_UI_Succession_type"),
                SysmlPackage.eINSTANCE.getSuccession_TriggerStep(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This returns SuccessionFlowConnectionUsage.svg. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Object getImage(Object object) {
        return this.overlayImage(object, this.getResourceLocator().getImage("full/obj16/SuccessionFlowConnectionUsage.svg"));
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((SuccessionFlowConnectionUsage) object).getName();
        return label == null || label.length() == 0 ? this.getString("_UI_SuccessionFlowConnectionUsage_type") : this.getString("_UI_SuccessionFlowConnectionUsage_type") + " " + label;
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
