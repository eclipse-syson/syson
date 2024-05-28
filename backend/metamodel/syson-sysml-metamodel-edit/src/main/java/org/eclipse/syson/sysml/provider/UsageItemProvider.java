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
import org.eclipse.syson.sysml.Usage;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.Usage} object. <!-- begin-user-doc --> <!--
 * end-user-doc -->
 *
 * @generated
 */
public class UsageItemProvider extends FeatureItemProvider {
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public UsageItemProvider(AdapterFactory adapterFactory) {
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

            this.addIsReferencePropertyDescriptor(object);
            this.addIsVariationPropertyDescriptor(object);
            this.addDefinitionPropertyDescriptor(object);
            this.addDirectedUsagePropertyDescriptor(object);
            this.addNestedActionPropertyDescriptor(object);
            this.addNestedAllocationPropertyDescriptor(object);
            this.addNestedAnalysisCasePropertyDescriptor(object);
            this.addNestedAttributePropertyDescriptor(object);
            this.addNestedCalculationPropertyDescriptor(object);
            this.addNestedCasePropertyDescriptor(object);
            this.addNestedConcernPropertyDescriptor(object);
            this.addNestedConnectionPropertyDescriptor(object);
            this.addNestedConstraintPropertyDescriptor(object);
            this.addNestedEnumerationPropertyDescriptor(object);
            this.addNestedFlowPropertyDescriptor(object);
            this.addNestedInterfacePropertyDescriptor(object);
            this.addNestedItemPropertyDescriptor(object);
            this.addNestedMetadataPropertyDescriptor(object);
            this.addNestedOccurrencePropertyDescriptor(object);
            this.addNestedPartPropertyDescriptor(object);
            this.addNestedPortPropertyDescriptor(object);
            this.addNestedReferencePropertyDescriptor(object);
            this.addNestedRenderingPropertyDescriptor(object);
            this.addNestedRequirementPropertyDescriptor(object);
            this.addNestedStatePropertyDescriptor(object);
            this.addNestedTransitionPropertyDescriptor(object);
            this.addNestedUsagePropertyDescriptor(object);
            this.addNestedUseCasePropertyDescriptor(object);
            this.addNestedVerificationCasePropertyDescriptor(object);
            this.addNestedViewPropertyDescriptor(object);
            this.addNestedViewpointPropertyDescriptor(object);
            this.addOwningDefinitionPropertyDescriptor(object);
            this.addOwningUsagePropertyDescriptor(object);
            this.addUsagePropertyDescriptor(object);
            this.addVariantPropertyDescriptor(object);
            this.addVariantMembershipPropertyDescriptor(object);
        }
        return this.itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Definition feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addDefinitionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_definition_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_definition_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_Definition(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Directed Usage feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addDirectedUsagePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_directedUsage_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_directedUsage_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_DirectedUsage(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Is Reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addIsReferencePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_isReference_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_isReference_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_IsReference(),
                true,
                false,
                false,
                ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
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
                this.getString("_UI_Usage_isVariation_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_isVariation_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_IsVariation(),
                true,
                false,
                false,
                ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Action feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedActionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedAction_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedAction_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedAction(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Allocation feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedAllocationPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedAllocation_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedAllocation_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedAllocation(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Analysis Case feature. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    protected void addNestedAnalysisCasePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedAnalysisCase_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedAnalysisCase_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedAnalysisCase(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedAttributePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedAttribute_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedAttribute_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedAttribute(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Calculation feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedCalculationPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedCalculation_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedCalculation_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedCalculation(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Case feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedCasePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedCase_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedCase_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedCase(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Concern feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedConcernPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedConcern_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedConcern_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedConcern(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Connection feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedConnectionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedConnection_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedConnection_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedConnection(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Constraint feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedConstraintPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedConstraint_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedConstraint_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedConstraint(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Enumeration feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedEnumerationPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedEnumeration_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedEnumeration_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedEnumeration(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Flow feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedFlowPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedFlow_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedFlow_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedFlow(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Interface feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedInterfacePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedInterface_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedInterface_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedInterface(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Item feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedItemPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedItem_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedItem_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedItem(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Metadata feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedMetadataPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedMetadata_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedMetadata_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedMetadata(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Occurrence feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedOccurrencePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedOccurrence_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedOccurrence_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedOccurrence(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Part feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedPartPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedPart_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedPart_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedPart(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Port feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedPortPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedPort_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedPort_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedPort(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedReferencePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedReference_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedReference_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedReference(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Rendering feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedRenderingPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedRendering_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedRendering_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedRendering(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Requirement feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedRequirementPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedRequirement_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedRequirement_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedRequirement(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested State feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedStatePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedState_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedState_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedState(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Transition feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedTransitionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedTransition_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedTransition_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedTransition(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Usage feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedUsagePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedUsage_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedUsage_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedUsage(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Use Case feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedUseCasePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedUseCase_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedUseCase_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedUseCase(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Verification Case feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    protected void addNestedVerificationCasePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedVerificationCase_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedVerificationCase_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedVerificationCase(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested View feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedViewPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedView_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedView_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedView(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Nested Viewpoint feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNestedViewpointPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_nestedViewpoint_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedViewpoint_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_NestedViewpoint(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owning Definition feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwningDefinitionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_owningDefinition_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_owningDefinition_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_OwningDefinition(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owning Usage feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwningUsagePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Usage_owningUsage_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_owningUsage_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_OwningUsage(),
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
                this.getString("_UI_Usage_usage_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_usage_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_Usage(),
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
                this.getString("_UI_Usage_variant_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_variant_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_Variant(),
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
                this.getString("_UI_Usage_variantMembership_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Usage_variantMembership_feature", "_UI_Usage_type"),
                SysmlPackage.eINSTANCE.getUsage_VariantMembership(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This returns Usage.svg. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Object getImage(Object object) {
        return this.overlayImage(object, this.getResourceLocator().getImage("full/obj16/Usage.svg"));
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((Usage) object).getName();
        return label == null || label.length() == 0 ? this.getString("_UI_Usage_type") : this.getString("_UI_Usage_type") + " " + label;
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

        switch (notification.getFeatureID(Usage.class)) {
            case SysmlPackage.USAGE__IS_REFERENCE:
            case SysmlPackage.USAGE__IS_VARIATION:
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
