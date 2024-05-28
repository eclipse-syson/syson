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
import org.eclipse.syson.sysml.CalculationDefinition;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.CalculationDefinition} object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 *
 * @generated
 */
public class CalculationDefinitionItemProvider extends ActionDefinitionItemProvider {
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public CalculationDefinitionItemProvider(AdapterFactory adapterFactory) {
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

            this.addIsModelLevelEvaluablePropertyDescriptor(object);
            this.addExpressionPropertyDescriptor(object);
            this.addResultPropertyDescriptor(object);
            this.addCalculationPropertyDescriptor(object);
        }
        return this.itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Expression feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addExpressionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Function_expression_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Function_expression_feature", "_UI_Function_type"),
                SysmlPackage.eINSTANCE.getFunction_Expression(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Is Model Level Evaluable feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    protected void addIsModelLevelEvaluablePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Function_isModelLevelEvaluable_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Function_isModelLevelEvaluable_feature", "_UI_Function_type"),
                SysmlPackage.eINSTANCE.getFunction_IsModelLevelEvaluable(),
                true,
                false,
                false,
                ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Result feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addResultPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Function_result_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Function_result_feature", "_UI_Function_type"),
                SysmlPackage.eINSTANCE.getFunction_Result(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Calculation feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addCalculationPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_CalculationDefinition_calculation_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_CalculationDefinition_calculation_feature", "_UI_CalculationDefinition_type"),
                SysmlPackage.eINSTANCE.getCalculationDefinition_Calculation(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This returns CalculationDefinition.svg. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Object getImage(Object object) {
        return this.overlayImage(object, this.getResourceLocator().getImage("full/obj16/CalculationDefinition.svg"));
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((CalculationDefinition) object).getName();
        return label == null || label.length() == 0 ? this.getString("_UI_CalculationDefinition_type") : this.getString("_UI_CalculationDefinition_type") + " " + label;
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

        switch (notification.getFeatureID(CalculationDefinition.class)) {
            case SysmlPackage.CALCULATION_DEFINITION__IS_MODEL_LEVEL_EVALUABLE:
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
