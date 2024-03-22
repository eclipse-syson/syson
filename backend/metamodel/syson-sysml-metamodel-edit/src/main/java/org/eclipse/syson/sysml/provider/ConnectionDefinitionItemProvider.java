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

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.syson.sysml.ConnectionDefinition;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.ConnectionDefinition} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ConnectionDefinitionItemProvider extends PartDefinitionItemProvider {
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConnectionDefinitionItemProvider(AdapterFactory adapterFactory) {
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

            addIsImpliedPropertyDescriptor(object);
            addRelatedElementPropertyDescriptor(object);
            addSourcePropertyDescriptor(object);
            addTargetPropertyDescriptor(object);
            addAssociationEndPropertyDescriptor(object);
            addRelatedTypePropertyDescriptor(object);
            addSourceTypePropertyDescriptor(object);
            addTargetTypePropertyDescriptor(object);
            addConnectionEndPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Is Implied feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addIsImpliedPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Relationship_isImplied_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Relationship_isImplied_feature", "_UI_Relationship_type"),
                 SysmlPackage.eINSTANCE.getRelationship_IsImplied(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Related Element feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addRelatedElementPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Relationship_relatedElement_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Relationship_relatedElement_feature", "_UI_Relationship_type"),
                 SysmlPackage.eINSTANCE.getRelationship_RelatedElement(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Source feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addSourcePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Relationship_source_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Relationship_source_feature", "_UI_Relationship_type"),
                 SysmlPackage.eINSTANCE.getRelationship_Source(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Target feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addTargetPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Relationship_target_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Relationship_target_feature", "_UI_Relationship_type"),
                 SysmlPackage.eINSTANCE.getRelationship_Target(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Association End feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addAssociationEndPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Association_associationEnd_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Association_associationEnd_feature", "_UI_Association_type"),
                 SysmlPackage.eINSTANCE.getAssociation_AssociationEnd(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Related Type feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addRelatedTypePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Association_relatedType_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Association_relatedType_feature", "_UI_Association_type"),
                 SysmlPackage.eINSTANCE.getAssociation_RelatedType(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Source Type feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addSourceTypePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Association_sourceType_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Association_sourceType_feature", "_UI_Association_type"),
                 SysmlPackage.eINSTANCE.getAssociation_SourceType(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Target Type feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addTargetTypePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Association_targetType_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Association_targetType_feature", "_UI_Association_type"),
                 SysmlPackage.eINSTANCE.getAssociation_TargetType(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Connection End feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addConnectionEndPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_ConnectionDefinition_connectionEnd_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_ConnectionDefinition_connectionEnd_feature", "_UI_ConnectionDefinition_type"),
                 SysmlPackage.eINSTANCE.getConnectionDefinition_ConnectionEnd(),
                 true,
                 false,
                 true,
                 null,
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
            childrenFeatures.add(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement());
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
     * This returns ConnectionDefinition.svg.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/ConnectionDefinition.svg"));
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((ConnectionDefinition)object).getName();
        return label == null || label.length() == 0 ?
            getString("_UI_ConnectionDefinition_type") :
            getString("_UI_ConnectionDefinition_type") + " " + label;
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

        switch (notification.getFeatureID(ConnectionDefinition.class)) {
            case SysmlPackage.CONNECTION_DEFINITION__IS_IMPLIED:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case SysmlPackage.CONNECTION_DEFINITION__OWNED_RELATED_ELEMENT:
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
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createNamespace()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createType()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createFeature()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createOccurrenceUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createActionUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createAcceptActionUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createClassifier()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createOccurrenceDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createActionDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createOwningMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createFeatureMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createParameterMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createActorMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createItemDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createPartDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createConnectionDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createAllocationDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createConnectionUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createAllocationUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createCalculationDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createCaseDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createAnalysisCaseDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createCalculationUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createCaseUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createAnalysisCaseUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createAnnotatingElement()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createAnnotation()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createConstraintUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createAssertConstraintUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createAssignmentActionUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createAssociation()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createAssociationStructure()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createAttributeDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createAttributeUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createClass()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createBehavior()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createConnector()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createBindingConnector()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createBindingConnectorAsUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createStep()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createBooleanExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createInvocationExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createOperatorExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createCollectExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createComment()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createConstraintDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createRequirementDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createConcernDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createRequirementUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createConcernUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createPortDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createConjugatedPortDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createSpecialization()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createFeatureTyping()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createConjugatedPortTyping()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createConjugation()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createDataType()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createDecisionNode()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createDependency()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createDifferencing()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createDisjoining()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createDocumentation()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createElementFilterMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createEndFeatureMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createEnumerationDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createEnumerationUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createEventOccurrenceUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createStateUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createExhibitStateUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createFeatureChainExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createFeatureChaining()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createFeatureInverting()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createFeatureReferenceExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createFeatureValue()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createFlowConnectionDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createFlowConnectionUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createForkNode()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createForLoopActionUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createRequirementConstraintMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createFramedConcernMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createFunction()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createIfActionUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createUseCaseUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createIncludeUseCaseUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createInteraction()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createInterfaceDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createInterfaceUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createIntersecting()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createInvariant()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createItemFeature()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createItemFlow()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createItemFlowEnd()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createItemUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createJoinNode()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createPackage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createLibraryPackage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createLifeClass()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createLiteralExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createLiteralBoolean()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createLiteralInfinity()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createLiteralInteger()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createLiteralRational()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createLiteralString()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createMembershipImport()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createMembershipExpose()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createMergeNode()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createStructure()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createMetaclass()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createMetadataAccessExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createMetadataDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createMetadataFeature()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createMetadataUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createMultiplicity()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createMultiplicityRange()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createNamespaceImport()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createNamespaceExpose()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createNullExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createObjectiveMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createPartUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createPerformActionUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createPortConjugation()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createPortUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createPredicate()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createSubsetting()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createRedefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createReferenceSubsetting()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createReferenceUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createRenderingDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createRenderingUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createRequirementVerificationMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createResultExpressionMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createReturnParameterMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createSatisfyRequirementUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createSelectExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createSendActionUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createStakeholderMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createStateDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createStateSubactionMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createSubclassification()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createSubjectMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createSuccession()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createSuccessionAsUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createSuccessionFlowConnectionUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createSuccessionItemFlow()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createTextualRepresentation()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createTransitionFeatureMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createTransitionUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createTriggerInvocationExpression()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createTypeFeaturing()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createUnioning()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createUseCaseDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createVariantMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createVerificationCaseDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createVerificationCaseUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createViewDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createViewpointDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createViewpointUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createViewRenderingMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createViewUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                 SysmlFactory.eINSTANCE.createWhileLoopActionUsage()));
    }

    /**
     * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
        Object childFeature = feature;
        Object childObject = child;

        boolean qualify =
            childFeature == SysmlPackage.eINSTANCE.getElement_OwnedRelationship() ||
            childFeature == SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement();

        if (qualify) {
            return getString
                ("_UI_CreateChild_text2",
                 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
        }
        return super.getCreateChildText(owner, feature, child, selection);
    }

}
