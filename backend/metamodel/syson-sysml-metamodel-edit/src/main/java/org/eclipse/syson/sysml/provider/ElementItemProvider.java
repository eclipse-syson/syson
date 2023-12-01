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

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.Element} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ElementItemProvider 
    extends ItemProviderAdapter
    implements
        IEditingDomainItemProvider,
        IStructuredItemContentProvider,
        ITreeItemContentProvider,
        IItemLabelProvider,
        IItemPropertySource {
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ElementItemProvider(AdapterFactory adapterFactory) {
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

            addAliasIdsPropertyDescriptor(object);
            addDeclaredNamePropertyDescriptor(object);
            addDeclaredShortNamePropertyDescriptor(object);
            addElementIdPropertyDescriptor(object);
            addIsImpliedIncludedPropertyDescriptor(object);
            addIsLibraryElementPropertyDescriptor(object);
            addNamePropertyDescriptor(object);
            addQualifiedNamePropertyDescriptor(object);
            addShortNamePropertyDescriptor(object);
            addDocumentationPropertyDescriptor(object);
            addOwnedAnnotationPropertyDescriptor(object);
            addOwnedElementPropertyDescriptor(object);
            addOwnerPropertyDescriptor(object);
            addOwningMembershipPropertyDescriptor(object);
            addOwningNamespacePropertyDescriptor(object);
            addTextualRepresentationPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Alias Ids feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addAliasIdsPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_aliasIds_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_aliasIds_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_AliasIds(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Declared Name feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addDeclaredNamePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_declaredName_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_declaredName_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_DeclaredName(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Declared Short Name feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addDeclaredShortNamePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_declaredShortName_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_declaredShortName_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_DeclaredShortName(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Documentation feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addDocumentationPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_documentation_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_documentation_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_Documentation(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Element Id feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addElementIdPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_elementId_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_elementId_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_ElementId(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Is Implied Included feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addIsImpliedIncludedPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_isImpliedIncluded_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_isImpliedIncluded_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_IsImpliedIncluded(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Is Library Element feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addIsLibraryElementPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_isLibraryElement_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_isLibraryElement_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_IsLibraryElement(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Name feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addNamePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_name_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_name_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_Name(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owned Annotation feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwnedAnnotationPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_ownedAnnotation_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_ownedAnnotation_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_OwnedAnnotation(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owned Element feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwnedElementPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_ownedElement_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_ownedElement_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_OwnedElement(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owner feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwnerPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_owner_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_owner_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_Owner(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owning Membership feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwningMembershipPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_owningMembership_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_owningMembership_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_OwningMembership(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Owning Namespace feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addOwningNamespacePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_owningNamespace_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_owningNamespace_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_OwningNamespace(),
                 true,
                 false,
                 true,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Qualified Name feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addQualifiedNamePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_qualifiedName_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_qualifiedName_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_QualifiedName(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Short Name feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addShortNamePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_shortName_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_shortName_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_ShortName(),
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Textual Representation feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addTextualRepresentationPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_Element_textualRepresentation_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_Element_textualRepresentation_feature", "_UI_Element_type"),
                 SysmlPackage.eINSTANCE.getElement_TextualRepresentation(),
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
            childrenFeatures.add(SysmlPackage.eINSTANCE.getElement_OwnedRelationship());
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
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((Element)object).getName();
        return label == null || label.length() == 0 ?
            getString("_UI_Element_type") :
            getString("_UI_Element_type") + " " + label;
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

        switch (notification.getFeatureID(Element.class)) {
            case SysmlPackage.ELEMENT__ALIAS_IDS:
            case SysmlPackage.ELEMENT__DECLARED_NAME:
            case SysmlPackage.ELEMENT__DECLARED_SHORT_NAME:
            case SysmlPackage.ELEMENT__ELEMENT_ID:
            case SysmlPackage.ELEMENT__IS_IMPLIED_INCLUDED:
            case SysmlPackage.ELEMENT__IS_LIBRARY_ELEMENT:
            case SysmlPackage.ELEMENT__NAME:
            case SysmlPackage.ELEMENT__QUALIFIED_NAME:
            case SysmlPackage.ELEMENT__SHORT_NAME:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case SysmlPackage.ELEMENT__OWNED_RELATIONSHIP:
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
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createOwningMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createFeatureMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createParameterMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createActorMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createConnectionDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createAllocationDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createConnectionUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createAllocationUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createAnnotation()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createAssociation()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createAssociationStructure()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createConnector()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createBindingConnector()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createBindingConnectorAsUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createSpecialization()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createFeatureTyping()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createConjugatedPortTyping()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createConjugation()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createDependency()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createDifferencing()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createDisjoining()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createElementFilterMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createEndFeatureMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createFeatureChaining()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createFeatureInverting()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createFeatureValue()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createFlowConnectionDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createFlowConnectionUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createRequirementConstraintMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createFramedConcernMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createInteraction()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createInterfaceDefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createInterfaceUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createIntersecting()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createItemFlow()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createMembershipImport()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createMembershipExpose()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createNamespaceImport()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createNamespaceExpose()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createObjectiveMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createPortConjugation()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createSubsetting()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createRedefinition()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createReferenceSubsetting()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createRequirementVerificationMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createResultExpressionMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createReturnParameterMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createStakeholderMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createStateSubactionMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createSubclassification()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createSubjectMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createSuccession()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createSuccessionAsUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createSuccessionFlowConnectionUsage()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createSuccessionItemFlow()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createTransitionFeatureMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createTypeFeaturing()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createUnioning()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createVariantMembership()));

        newChildDescriptors.add
            (createChildParameter
                (SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                 SysmlFactory.eINSTANCE.createViewRenderingMembership()));
    }

    /**
     * Return the resource locator for this item provider's resources.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator() {
        return ((IChildCreationExtender)adapterFactory).getResourceLocator();
    }

}
