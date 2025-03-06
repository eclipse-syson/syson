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
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.syson.sysml.AnnotatingElement;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.AnnotatingElement} object. <!-- begin-user-doc
 * --> <!-- end-user-doc -->
 *
 * @generated
 */
public class AnnotatingElementItemProvider extends ElementItemProvider {
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public AnnotatingElementItemProvider(AdapterFactory adapterFactory) {
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

            this.addAnnotatedElementPropertyDescriptor(object);
            this.addAnnotationPropertyDescriptor(object);
            this.addOwnedAnnotatingRelationshipPropertyDescriptor(object);
            this.addOwningAnnotatingRelationshipPropertyDescriptor(object);
        }
        return this.itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Annotated Element feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addAnnotatedElementPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_AnnotatingElement_annotatedElement_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_AnnotatingElement_annotatedElement_feature", "_UI_AnnotatingElement_type"),
                SysmlPackage.eINSTANCE.getAnnotatingElement_AnnotatedElement(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Annotation feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addAnnotationPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_AnnotatingElement_annotation_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_AnnotatingElement_annotation_feature", "_UI_AnnotatingElement_type"),
                SysmlPackage.eINSTANCE.getAnnotatingElement_Annotation(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owned Annotating Relationship feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    protected void addOwnedAnnotatingRelationshipPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_AnnotatingElement_ownedAnnotatingRelationship_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_AnnotatingElement_ownedAnnotatingRelationship_feature", "_UI_AnnotatingElement_type"),
                SysmlPackage.eINSTANCE.getAnnotatingElement_OwnedAnnotatingRelationship(),
                false,
                false,
                false,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Owning Annotating Relationship feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    protected void addOwningAnnotatingRelationshipPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_AnnotatingElement_owningAnnotatingRelationship_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_AnnotatingElement_owningAnnotatingRelationship_feature", "_UI_AnnotatingElement_type"),
                SysmlPackage.eINSTANCE.getAnnotatingElement_OwningAnnotatingRelationship(),
                false,
                false,
                false,
                null,
                null,
                null));
    }

    /**
     * This returns AnnotatingElement.svg. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Object getImage(Object object) {
        return this.overlayImage(object, this.getResourceLocator().getImage("full/obj16/AnnotatingElement.svg"));
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((AnnotatingElement) object).getName();
        return label == null || label.length() == 0 ? this.getString("_UI_AnnotatingElement_type") : this.getString("_UI_AnnotatingElement_type") + " " + label;
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
