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

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.Usage} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class UsageItemProvider extends FeatureItemProvider {
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UsageItemProvider(AdapterFactory adapterFactory) {
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

            addIsReferencePropertyDescriptor(object);
            addIsVariationPropertyDescriptor(object);
            addDefinitionPropertyDescriptor(object);
            addDirectedUsagePropertyDescriptor(object);
            addNestedActionPropertyDescriptor(object);
            addNestedAllocationPropertyDescriptor(object);
            addNestedAnalysisCasePropertyDescriptor(object);
            addNestedAttributePropertyDescriptor(object);
            addNestedCalculationPropertyDescriptor(object);
            addNestedCasePropertyDescriptor(object);
            addNestedConcernPropertyDescriptor(object);
            addNestedConnectionPropertyDescriptor(object);
            addNestedConstraintPropertyDescriptor(object);
            addNestedEnumerationPropertyDescriptor(object);
            addNestedFlowPropertyDescriptor(object);
            addNestedInterfacePropertyDescriptor(object);
            addNestedItemPropertyDescriptor(object);
            addNestedMetadataPropertyDescriptor(object);
            addNestedOccurrencePropertyDescriptor(object);
            addNestedPartPropertyDescriptor(object);
            addNestedPortPropertyDescriptor(object);
            addNestedReferencePropertyDescriptor(object);
            addNestedRenderingPropertyDescriptor(object);
            addNestedRequirementPropertyDescriptor(object);
            addNestedStatePropertyDescriptor(object);
            addNestedTransitionPropertyDescriptor(object);
            addNestedUsagePropertyDescriptor(object);
            addNestedUseCasePropertyDescriptor(object);
            addNestedVerificationCasePropertyDescriptor(object);
            addNestedViewPropertyDescriptor(object);
            addNestedViewpointPropertyDescriptor(object);
            addOwningDefinitionPropertyDescriptor(object);
            addOwningUsagePropertyDescriptor(object);
            addUsagePropertyDescriptor(object);
            addVariantPropertyDescriptor(object);
            addVariantMembershipPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Definition feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addDefinitionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_definition_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_definition_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_Definition(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Directed Usage feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addDirectedUsagePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_directedUsage_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_directedUsage_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_DirectedUsage(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Is Reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addIsReferencePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_isReference_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_isReference_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_IsReference(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Is Variation feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addIsVariationPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_isVariation_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_isVariation_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_IsVariation(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Action feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedActionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedAction_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedAction_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedAction(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Allocation feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedAllocationPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedAllocation_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedAllocation_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedAllocation(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Analysis Case feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedAnalysisCasePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedAnalysisCase_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedAnalysisCase_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedAnalysisCase(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedAttributePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedAttribute_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedAttribute_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedAttribute(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Calculation feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedCalculationPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedCalculation_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedCalculation_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedCalculation(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Case feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedCasePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedCase_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedCase_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedCase(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Concern feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedConcernPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedConcern_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedConcern_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedConcern(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Connection feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedConnectionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedConnection_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedConnection_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedConnection(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Constraint feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedConstraintPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedConstraint_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedConstraint_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedConstraint(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Enumeration feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedEnumerationPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedEnumeration_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedEnumeration_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedEnumeration(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Flow feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedFlowPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedFlow_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedFlow_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedFlow(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Interface feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedInterfacePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedInterface_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedInterface_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedInterface(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Item feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedItemPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedItem_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedItem_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedItem(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Metadata feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedMetadataPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedMetadata_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedMetadata_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedMetadata(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Occurrence feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedOccurrencePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedOccurrence_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedOccurrence_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedOccurrence(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Part feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedPartPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedPart_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedPart_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedPart(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Port feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedPortPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedPort_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedPort_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedPort(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedReferencePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedReference_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedReference_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedReference(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Rendering feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedRenderingPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedRendering_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedRendering_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedRendering(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Requirement feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedRequirementPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedRequirement_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedRequirement_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedRequirement(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested State feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedStatePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedState_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedState_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedState(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Transition feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedTransitionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedTransition_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedTransition_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedTransition(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Usage feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedUsagePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedUsage_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedUsage_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedUsage(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Use Case feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedUseCasePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedUseCase_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedUseCase_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedUseCase(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Verification Case feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedVerificationCasePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedVerificationCase_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedVerificationCase_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedVerificationCase(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested View feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedViewPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedView_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedView_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedView(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Nested Viewpoint feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNestedViewpointPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_nestedViewpoint_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_nestedViewpoint_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_NestedViewpoint(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owning Definition feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwningDefinitionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_owningDefinition_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_owningDefinition_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_OwningDefinition(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owning Usage feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwningUsagePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_owningUsage_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_owningUsage_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_OwningUsage(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Usage feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addUsagePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_usage_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_usage_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_Usage(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Variant feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addVariantPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_variant_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_variant_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_Variant(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Variant Membership feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addVariantMembershipPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Usage_variantMembership_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Usage_variantMembership_feature", "_UI_Usage_type"),
                 SysmlPackage.eINSTANCE.getUsage_VariantMembership(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This returns Usage.gif.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/Usage"));
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((Usage)object).getName();
        return label == null || label.length() == 0 ?
            getString("_UI_Usage_type") :
            getString("_UI_Usage_type") + " " + label;
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

        switch (notification.getFeatureID(Usage.class)) {
            case SysmlPackage.USAGE__IS_REFERENCE:
            case SysmlPackage.USAGE__IS_VARIATION:
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
