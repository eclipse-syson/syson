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
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.ViewDefinition;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.ViewDefinition} object. <!-- begin-user-doc
 * --> <!-- end-user-doc -->
 *
 * @generated
 */
public class ViewDefinitionItemProvider extends PartDefinitionItemProvider {
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ViewDefinitionItemProvider(AdapterFactory adapterFactory) {
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

            this.addSatisfiedViewpointPropertyDescriptor(object);
            this.addViewPropertyDescriptor(object);
            this.addViewConditionPropertyDescriptor(object);
            this.addViewRenderingPropertyDescriptor(object);
        }
        return this.itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Satisfied Viewpoint feature. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    protected void addSatisfiedViewpointPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_ViewDefinition_satisfiedViewpoint_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_ViewDefinition_satisfiedViewpoint_feature", "_UI_ViewDefinition_type"),
                SysmlPackage.eINSTANCE.getViewDefinition_SatisfiedViewpoint(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the View feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addViewPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_ViewDefinition_view_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_ViewDefinition_view_feature", "_UI_ViewDefinition_type"),
                SysmlPackage.eINSTANCE.getViewDefinition_View(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the View Condition feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addViewConditionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_ViewDefinition_viewCondition_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_ViewDefinition_viewCondition_feature", "_UI_ViewDefinition_type"),
                SysmlPackage.eINSTANCE.getViewDefinition_ViewCondition(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the View Rendering feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addViewRenderingPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_ViewDefinition_viewRendering_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_ViewDefinition_viewRendering_feature", "_UI_ViewDefinition_type"),
                SysmlPackage.eINSTANCE.getViewDefinition_ViewRendering(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This returns ViewDefinition.svg. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Object getImage(Object object) {
        return this.overlayImage(object, this.getResourceLocator().getImage("full/obj16/ViewDefinition.svg"));
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((ViewDefinition) object).getName();
        return label == null || label.length() == 0 ? this.getString("_UI_ViewDefinition_type") : this.getString("_UI_ViewDefinition_type") + " " + label;
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

}
