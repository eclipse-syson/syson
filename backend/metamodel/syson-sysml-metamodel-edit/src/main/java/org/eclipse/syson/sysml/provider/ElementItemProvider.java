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
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.provider.EModelElementItemProvider;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.util.ElementUtil;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.Element} object. <!-- begin-user-doc --> <!--
 * end-user-doc -->
 *
 * @generated
 */
public class ElementItemProvider
        extends EModelElementItemProvider {
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ElementItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
     * Allow to hide KerML and SysML standard libraries elements from Reference Widget candidates. These candidates are
     * still available through Reference Widget's "..." action.
     *
     * @generated NOT
     */
    @Override
    protected ItemPropertyDescriptor createItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description, EStructuralFeature feature,
            boolean isSettable, boolean multiLine, boolean sortChoices, Object staticImage, String category, String[] filterFlags) {
        ItemPropertyDescriptor itemPropertyDescriptor = new ItemPropertyDescriptor(adapterFactory, resourceLocator, displayName, description, feature, isSettable, multiLine, sortChoices, staticImage,
                category, filterFlags) {
            @Override
            public Collection<?> getChoiceOfValues(Object object) {
                Collection<?> choiceOfValues = super.getChoiceOfValues(object);
                if (object instanceof Element element) {
                    return choiceOfValues.stream().filter(candidate -> {
                        if (candidate instanceof Element elt && elt.eResource() != null
                                && !ElementUtil.isStandardLibraryResource(elt.eResource())) {
                            return true;
                        }
                        return false;
                    }).toList();
                }
                return choiceOfValues;
            }
        };
        return itemPropertyDescriptor;
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

            this.addAliasIdsPropertyDescriptor(object);
            this.addDeclaredNamePropertyDescriptor(object);
            this.addDeclaredShortNamePropertyDescriptor(object);
            this.addElementIdPropertyDescriptor(object);
            this.addIsImpliedIncludedPropertyDescriptor(object);
            this.addIsLibraryElementPropertyDescriptor(object);
            this.addNamePropertyDescriptor(object);
            this.addQualifiedNamePropertyDescriptor(object);
            this.addShortNamePropertyDescriptor(object);
            this.addDocumentationPropertyDescriptor(object);
            this.addOwnedAnnotationPropertyDescriptor(object);
            this.addOwnedElementPropertyDescriptor(object);
            this.addOwnerPropertyDescriptor(object);
            this.addOwningMembershipPropertyDescriptor(object);
            this.addOwningNamespacePropertyDescriptor(object);
            this.addTextualRepresentationPropertyDescriptor(object);
        }
        return this.itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Alias Ids feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addAliasIdsPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_aliasIds_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_aliasIds_feature", "_UI_Element_type"),
                SysmlPackage.eINSTANCE.getElement_AliasIds(),
                true,
                false,
                false,
                ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Declared Name feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addDeclaredNamePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_declaredName_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_declaredName_feature", "_UI_Element_type"),
                SysmlPackage.eINSTANCE.getElement_DeclaredName(),
                true,
                false,
                false,
                ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Declared Short Name feature. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    protected void addDeclaredShortNamePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_declaredShortName_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_declaredShortName_feature", "_UI_Element_type"),
                SysmlPackage.eINSTANCE.getElement_DeclaredShortName(),
                true,
                false,
                false,
                ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Documentation feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addDocumentationPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_documentation_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_documentation_feature", "_UI_Element_type"),
                SysmlPackage.eINSTANCE.getElement_Documentation(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Element Id feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addElementIdPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_elementId_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_elementId_feature", "_UI_Element_type"),
                SysmlPackage.eINSTANCE.getElement_ElementId(),
                true,
                false,
                false,
                ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Is Implied Included feature. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    protected void addIsImpliedIncludedPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_isImpliedIncluded_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_isImpliedIncluded_feature", "_UI_Element_type"),
                SysmlPackage.eINSTANCE.getElement_IsImpliedIncluded(),
                true,
                false,
                false,
                ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Is Library Element feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addIsLibraryElementPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_isLibraryElement_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_isLibraryElement_feature", "_UI_Element_type"),
                SysmlPackage.eINSTANCE.getElement_IsLibraryElement(),
                true,
                false,
                false,
                ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Name feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addNamePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_name_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_name_feature", "_UI_Element_type"),
                SysmlPackage.eINSTANCE.getElement_Name(),
                true,
                false,
                false,
                ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Annotation feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedAnnotationPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_ownedAnnotation_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_ownedAnnotation_feature", "_UI_Element_type"),
                SysmlPackage.eINSTANCE.getElement_OwnedAnnotation(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Element feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedElementPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_ownedElement_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_ownedElement_feature", "_UI_Element_type"),
                SysmlPackage.eINSTANCE.getElement_OwnedElement(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owner feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwnerPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_owner_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_owner_feature", "_UI_Element_type"),
                SysmlPackage.eINSTANCE.getElement_Owner(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owning Membership feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwningMembershipPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_owningMembership_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_owningMembership_feature", "_UI_Element_type"),
                SysmlPackage.eINSTANCE.getElement_OwningMembership(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owning Namespace feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addOwningNamespacePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_owningNamespace_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_owningNamespace_feature", "_UI_Element_type"),
                SysmlPackage.eINSTANCE.getElement_OwningNamespace(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Qualified Name feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addQualifiedNamePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_qualifiedName_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_qualifiedName_feature", "_UI_Element_type"),
                SysmlPackage.eINSTANCE.getElement_QualifiedName(),
                true,
                false,
                false,
                ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Short Name feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addShortNamePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_shortName_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_shortName_feature", "_UI_Element_type"),
                SysmlPackage.eINSTANCE.getElement_ShortName(),
                true,
                false,
                false,
                ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Textual Representation feature. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    protected void addTextualRepresentationPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Element_textualRepresentation_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Element_textualRepresentation_feature", "_UI_Element_type"),
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
     * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    @Override
    public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
        if (this.childrenFeatures == null) {
            super.getChildrenFeatures(object);
            this.childrenFeatures.add(SysmlPackage.eINSTANCE.getElement_OwnedRelationship());
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
        String label = ((Element) object).getName();
        return label == null || label.length() == 0 ? this.getString("_UI_Element_type") : this.getString("_UI_Element_type") + " " + label;
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
                this.fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case SysmlPackage.ELEMENT__OWNED_RELATIONSHIP:
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

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createOwningMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createFeatureMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createParameterMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createActorMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createConnectionDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createAllocationDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createConnectionUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createAllocationUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createAnnotation()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createAssociation()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createAssociationStructure()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createConnector()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createBindingConnector()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createBindingConnectorAsUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createSpecialization()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createFeatureTyping()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createConjugatedPortTyping()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createConjugation()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createSubsetting()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createCrossSubsetting()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createDependency()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createDifferencing()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createDisjoining()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createElementFilterMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createEndFeatureMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createFeatureChaining()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createFeatureInverting()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createFeatureValue()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createFlowConnectionDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createFlowConnectionUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createRequirementConstraintMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createFramedConcernMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createInteraction()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createInterfaceDefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createInterfaceUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createIntersecting()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createItemFlow()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createMembershipImport()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createMembershipExpose()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createNamespaceImport()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createNamespaceExpose()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createObjectiveMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createPortConjugation()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createRedefinition()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createReferenceSubsetting()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createRequirementVerificationMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createResultExpressionMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createReturnParameterMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createStakeholderMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createStateSubactionMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createSubclassification()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createSubjectMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createSuccession()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createSuccessionAsUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createSuccessionFlowConnectionUsage()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createSuccessionItemFlow()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createTransitionFeatureMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createTypeFeaturing()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createUnioning()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createVariantMembership()));

        newChildDescriptors.add(this.createChildParameter(SysmlPackage.eINSTANCE.getElement_OwnedRelationship(),
                SysmlFactory.eINSTANCE.createViewRenderingMembership()));
    }

    /**
     * Return the resource locator for this item provider's resources. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator() {
        return ((IChildCreationExtender) this.adapterFactory).getResourceLocator();
    }

    /**
     * <!-- begin-user-doc --> See org.eclipse.syson.sysml.impl.ElementImpl.eContents(). <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Collection<?> getChildren(Object object) {
        if (object instanceof Element elt) {
            return elt.eContents();
        }
        return super.getChildren(object);
    }
}
