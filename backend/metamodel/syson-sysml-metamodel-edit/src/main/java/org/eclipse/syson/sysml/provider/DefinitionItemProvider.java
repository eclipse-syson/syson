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
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.Definition} object. <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class DefinitionItemProvider extends ClassifierItemProvider {
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public DefinitionItemProvider(AdapterFactory adapterFactory) {
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

            this.addIsVariationPropertyDescriptor(object);
            this.addDirectedUsagePropertyDescriptor(object);
            this.addOwnedActionPropertyDescriptor(object);
            this.addOwnedAllocationPropertyDescriptor(object);
            this.addOwnedAnalysisCasePropertyDescriptor(object);
            this.addOwnedAttributePropertyDescriptor(object);
            this.addOwnedCalculationPropertyDescriptor(object);
            this.addOwnedCasePropertyDescriptor(object);
            this.addOwnedConcernPropertyDescriptor(object);
            this.addOwnedConnectionPropertyDescriptor(object);
            this.addOwnedConstraintPropertyDescriptor(object);
            this.addOwnedEnumerationPropertyDescriptor(object);
            this.addOwnedFlowPropertyDescriptor(object);
            this.addOwnedInterfacePropertyDescriptor(object);
            this.addOwnedItemPropertyDescriptor(object);
            this.addOwnedMetadataPropertyDescriptor(object);
            this.addOwnedOccurrencePropertyDescriptor(object);
            this.addOwnedPartPropertyDescriptor(object);
            this.addOwnedPortPropertyDescriptor(object);
            this.addOwnedReferencePropertyDescriptor(object);
            this.addOwnedRenderingPropertyDescriptor(object);
            this.addOwnedRequirementPropertyDescriptor(object);
            this.addOwnedStatePropertyDescriptor(object);
            this.addOwnedTransitionPropertyDescriptor(object);
            this.addOwnedUsagePropertyDescriptor(object);
            this.addOwnedUseCasePropertyDescriptor(object);
            this.addOwnedVerificationCasePropertyDescriptor(object);
            this.addOwnedViewPropertyDescriptor(object);
            this.addOwnedViewpointPropertyDescriptor(object);
            this.addUsagePropertyDescriptor(object);
            this.addVariantPropertyDescriptor(object);
            this.addVariantMembershipPropertyDescriptor(object);
        }
        return this.itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Directed Usage feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addDirectedUsagePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_directedUsage_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_directedUsage_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_DirectedUsage(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Is Variation feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addIsVariationPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_isVariation_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_isVariation_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_IsVariation(),
                true,
                false,
                false,
                ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Action feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedActionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedAction_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedAction_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedAction(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Allocation feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedAllocationPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedAllocation_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedAllocation_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedAllocation(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Analysis Case feature. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    protected void addOwnedAnalysisCasePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedAnalysisCase_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedAnalysisCase_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedAnalysisCase(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedAttributePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedAttribute_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedAttribute_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Calculation feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedCalculationPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedCalculation_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedCalculation_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedCalculation(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Case feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedCasePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedCase_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedCase_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedCase(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Concern feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedConcernPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedConcern_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedConcern_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedConcern(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Connection feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedConnectionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedConnection_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedConnection_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedConnection(),
                true,
                false,
                true,
                null,
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
                this.getString("_UI_Definition_ownedConstraint_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedConstraint_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedConstraint(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Enumeration feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedEnumerationPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedEnumeration_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedEnumeration_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedEnumeration(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Flow feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedFlowPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedFlow_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedFlow_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedFlow(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Interface feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedInterfacePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedInterface_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedInterface_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedInterface(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Item feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedItemPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedItem_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedItem_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedItem(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Metadata feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedMetadataPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedMetadata_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedMetadata_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedMetadata(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Occurrence feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedOccurrencePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedOccurrence_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedOccurrence_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedOccurrence(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Part feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedPartPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedPart_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedPart_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedPart(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Port feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedPortPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedPort_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedPort_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedPort(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedReferencePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedReference_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedReference_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedReference(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Rendering feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedRenderingPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedRendering_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedRendering_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedRendering(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Requirement feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedRequirementPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedRequirement_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedRequirement_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned State feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedStatePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedState_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedState_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedState(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Transition feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedTransitionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedTransition_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedTransition_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedTransition(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Usage feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedUsagePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedUsage_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedUsage_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedUsage(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Use Case feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedUseCasePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedUseCase_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedUseCase_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedUseCase(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Verification Case feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedVerificationCasePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedVerificationCase_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedVerificationCase_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedVerificationCase(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned View feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedViewPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedView_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedView_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedView(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Viewpoint feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedViewpointPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_ownedViewpoint_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_ownedViewpoint_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_OwnedViewpoint(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Usage feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addUsagePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_usage_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_usage_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_Usage(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Variant feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addVariantPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_variant_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_variant_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_Variant(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Variant Membership feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addVariantMembershipPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Definition_variantMembership_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Definition_variantMembership_feature", "_UI_Definition_type"),
                SysmlPackage.eINSTANCE.getDefinition_VariantMembership(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This returns Definition.svg. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Object getImage(Object object) {
        return this.overlayImage(object, this.getResourceLocator().getImage("full/obj16/Definition.svg"));
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((Definition) object).getName();
        return label == null || label.length() == 0 ? this.getString("_UI_Definition_type") : this.getString("_UI_Definition_type") + " " + label;
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

        switch (notification.getFeatureID(Definition.class)) {
            case SysmlPackage.DEFINITION__IS_VARIATION:
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
