/**
 * Copyright (c) 2023 Obeo.
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

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.OperatorExpression} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class OperatorExpressionItemProvider extends InvocationExpressionItemProvider {
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperatorExpressionItemProvider(AdapterFactory adapterFactory) {
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

            addOperatorPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Operator feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOperatorPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_OperatorExpression_operator_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_OperatorExpression_operator_feature", "_UI_OperatorExpression_type"),
                 SysmlPackage.eINSTANCE.getOperatorExpression_Operator(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
     * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
     * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
        if (childrenFeatures == null) {
            super.getChildrenFeatures(object);
            childrenFeatures.add(SysmlPackage.eINSTANCE.getOperatorExpression_Operand());
        }
        return childrenFeatures;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EStructuralFeature getChildFeature(Object object, Object child) {
        // Check the type of the specified child object and return the proper feature to use for
        // adding (see {@link AddCommand}) it as a child.

        return super.getChildFeature(object, child);
    }

    /**
     * This returns OperatorExpression.gif.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/OperatorExpression"));
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((OperatorExpression)object).getName();
        return label == null || label.length() == 0 ?
            getString("_UI_OperatorExpression_type") :
            getString("_UI_OperatorExpression_type") + " " + label;
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

        switch (notification.getFeatureID(OperatorExpression.class)) {
            case SysmlPackage.OPERATOR_EXPRESSION__OPERATOR:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case SysmlPackage.OPERATOR_EXPRESSION__OPERAND:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
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

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createCalculationUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createCaseUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createAnalysisCaseUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createConstraintUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createAssertConstraintUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createBooleanExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createInvocationExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createOperatorExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createCollectExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createRequirementUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createConcernUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createFeatureChainExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createFeatureReferenceExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createUseCaseUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createIncludeUseCaseUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createInvariant()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createLiteralExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createLiteralBoolean()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createLiteralInfinity()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createLiteralInteger()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createLiteralRational()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createLiteralString()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createMetadataAccessExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createNullExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createSatisfyRequirementUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createSelectExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createTriggerInvocationExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createVerificationCaseUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getOperatorExpression_Operand(),
                 SysmlFactory.eINSTANCE.createViewpointUsage()));
    }

}
