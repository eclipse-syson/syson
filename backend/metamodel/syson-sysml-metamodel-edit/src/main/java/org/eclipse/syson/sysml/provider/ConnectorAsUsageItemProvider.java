/**
 * Copyright (c) 2023, 2025 Obeo.
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
import org.eclipse.syson.sysml.ConnectorAsUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.ConnectorAsUsage} object. <!-- begin-user-doc
 * --> <!-- end-user-doc -->
 *
 * @generated
 */
public class ConnectorAsUsageItemProvider extends UsageItemProvider {
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ConnectorAsUsageItemProvider(AdapterFactory adapterFactory) {
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

            this.addIsImpliedPropertyDescriptor(object);
            this.addRelatedElementPropertyDescriptor(object);
            this.addSourcePropertyDescriptor(object);
            this.addTargetPropertyDescriptor(object);
            this.addAssociationPropertyDescriptor(object);
            this.addConnectorEndPropertyDescriptor(object);
            this.addDefaultFeaturingTypePropertyDescriptor(object);
            this.addRelatedFeaturePropertyDescriptor(object);
            this.addSourceFeaturePropertyDescriptor(object);
            this.addTargetFeaturePropertyDescriptor(object);
        }
        return this.itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Is Implied feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addIsImpliedPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Relationship_isImplied_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Relationship_isImplied_feature", "_UI_Relationship_type"),
                SysmlPackage.eINSTANCE.getRelationship_IsImplied(),
                true,
                false,
                false,
                ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Related Element feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addRelatedElementPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Relationship_relatedElement_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Relationship_relatedElement_feature", "_UI_Relationship_type"),
                SysmlPackage.eINSTANCE.getRelationship_RelatedElement(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Source feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addSourcePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Relationship_source_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Relationship_source_feature", "_UI_Relationship_type"),
                SysmlPackage.eINSTANCE.getRelationship_Source(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Target feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addTargetPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Relationship_target_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Relationship_target_feature", "_UI_Relationship_type"),
                SysmlPackage.eINSTANCE.getRelationship_Target(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Association feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addAssociationPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Connector_association_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Connector_association_feature", "_UI_Connector_type"),
                SysmlPackage.eINSTANCE.getConnector_Association(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Connector End feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addConnectorEndPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Connector_connectorEnd_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Connector_connectorEnd_feature", "_UI_Connector_type"),
                SysmlPackage.eINSTANCE.getConnector_ConnectorEnd(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Default Featuring Type feature. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    protected void addDefaultFeaturingTypePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Connector_defaultFeaturingType_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Connector_defaultFeaturingType_feature", "_UI_Connector_type"),
                SysmlPackage.eINSTANCE.getConnector_DefaultFeaturingType(),
                false,
                false,
                false,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Related Feature feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addRelatedFeaturePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Connector_relatedFeature_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Connector_relatedFeature_feature", "_UI_Connector_type"),
                SysmlPackage.eINSTANCE.getConnector_RelatedFeature(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Source Feature feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addSourceFeaturePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Connector_sourceFeature_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Connector_sourceFeature_feature", "_UI_Connector_type"),
                SysmlPackage.eINSTANCE.getConnector_SourceFeature(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Target Feature feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addTargetFeaturePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Connector_targetFeature_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Connector_targetFeature_feature", "_UI_Connector_type"),
                SysmlPackage.eINSTANCE.getConnector_TargetFeature(),
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
     * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    @Override
    public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
        if (this.childrenFeatures == null) {
            super.getChildrenFeatures(object);
            this.childrenFeatures.add(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement());
        }
        return this.childrenFeatures;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EStructuralFeature getChildFeature(Object object, Object child) {
        // Check the type of the specified child object and return the proper feature to use for
        // adding (see {@link AddCommand}) it as a child.

        return super.getChildFeature(object, child);
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((ConnectorAsUsage) object).getName();
        return label == null || label.length() == 0 ? this.getString("_UI_ConnectorAsUsage_type") : this.getString("_UI_ConnectorAsUsage_type") + " " + label;
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

        switch (notification.getFeatureID(ConnectorAsUsage.class)) {
            case SysmlPackage.CONNECTOR_AS_USAGE__IS_IMPLIED:
                this.fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case SysmlPackage.CONNECTOR_AS_USAGE__OWNED_RELATED_ELEMENT:
                this.fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
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

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createNamespace()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createType()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createFeature()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createOccurrenceUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createActionUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createAcceptActionUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createClassifier()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createOccurrenceDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createActionDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createOwningMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createFeatureMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createParameterMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createActorMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createItemDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createPartDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createConnectionDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createAllocationDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createConnectionUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createAllocationUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createCalculationDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createCaseDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createAnalysisCaseDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createCalculationUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createCaseUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createAnalysisCaseUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createAnnotatingElement()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createAnnotation()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createConstraintUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createAssertConstraintUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createAssignmentActionUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createAssociation()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createAssociationStructure()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createAttributeDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createAttributeUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createClass()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createBehavior()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createConnector()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createBindingConnector()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createBindingConnectorAsUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createStep()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createExpression()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createBooleanExpression()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createInvocationExpression()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createOperatorExpression()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createCollectExpression()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createComment()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createConstraintDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createRequirementDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createConcernDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createRequirementUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createConcernUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createPortDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createConjugatedPortDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createSpecialization()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createFeatureTyping()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createConjugatedPortTyping()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createConjugation()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createConstructorExpression()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createSubsetting()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createCrossSubsetting()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createDataType()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createDecisionNode()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createDependency()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createDifferencing()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createDisjoining()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createDocumentation()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createElementFilterMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createEndFeatureMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createEnumerationDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createEnumerationUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createEventOccurrenceUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createStateUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createExhibitStateUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createFeatureChainExpression()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createFeatureChaining()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createFeatureInverting()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createFeatureReferenceExpression()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createFeatureValue()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createFlow()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createFlowDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createFlowEnd()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createFlowUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createForkNode()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createForLoopActionUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createRequirementConstraintMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createFramedConcernMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createFunction()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createIfActionUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createUseCaseUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createIncludeUseCaseUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createIndexExpression()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createInteraction()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createInterfaceDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createInterfaceUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createIntersecting()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createInvariant()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createItemUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createJoinNode()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createPackage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createLibraryPackage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createLiteralExpression()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createLiteralBoolean()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createLiteralInfinity()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createLiteralInteger()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createLiteralRational()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createLiteralString()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createMembershipImport()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createMembershipExpose()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createMergeNode()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createStructure()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createMetaclass()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createMetadataAccessExpression()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createMetadataDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createMetadataFeature()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createMetadataUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createMultiplicity()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createMultiplicityRange()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createNamespaceImport()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createNamespaceExpose()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createNullExpression()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createObjectiveMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createPartUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createPayloadFeature()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createPerformActionUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createPortConjugation()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createPortUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createPredicate()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createRedefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createReferenceSubsetting()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createReferenceUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createRenderingDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createRenderingUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createRequirementVerificationMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createResultExpressionMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createReturnParameterMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createSatisfyRequirementUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createSelectExpression()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createSendActionUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createStakeholderMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createStateDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createStateSubactionMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createSubclassification()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createSubjectMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createSuccession()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createSuccessionAsUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createSuccessionFlow()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createSuccessionFlowUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createTerminateActionUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createTextualRepresentation()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createTransitionFeatureMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createTransitionUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createTriggerInvocationExpression()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createTypeFeaturing()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createUnioning()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createUseCaseDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createVariantMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createVerificationCaseDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createVerificationCaseUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createViewDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createViewpointDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createViewpointUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createViewRenderingMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createViewUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement(),
                SysmlFactory.eINSTANCE.createWhileLoopActionUsage()));
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
