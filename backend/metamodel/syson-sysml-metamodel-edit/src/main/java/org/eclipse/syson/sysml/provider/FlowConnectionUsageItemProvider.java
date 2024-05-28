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
import org.eclipse.syson.sysml.FlowConnectionUsage;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.FlowConnectionUsage} object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 *
 * @generated
 */
public class FlowConnectionUsageItemProvider extends ConnectionUsageItemProvider {
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public FlowConnectionUsageItemProvider(AdapterFactory adapterFactory) {
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

            this.addBehaviorPropertyDescriptor(object);
            this.addParameterPropertyDescriptor(object);
            this.addActionDefinitionPropertyDescriptor(object);
            this.addInteractionPropertyDescriptor(object);
            this.addItemFeaturePropertyDescriptor(object);
            this.addItemFlowEndPropertyDescriptor(object);
            this.addItemTypePropertyDescriptor(object);
            this.addSourceOutputFeaturePropertyDescriptor(object);
            this.addTargetInputFeaturePropertyDescriptor(object);
            this.addFlowConnectionDefinitionPropertyDescriptor(object);
        }
        return this.itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Behavior feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addBehaviorPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Step_behavior_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Step_behavior_feature", "_UI_Step_type"),
                SysmlPackage.eINSTANCE.getStep_Behavior(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Parameter feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addParameterPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_Step_parameter_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_Step_parameter_feature", "_UI_Step_type"),
                SysmlPackage.eINSTANCE.getStep_Parameter(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Action Definition feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addActionDefinitionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_ActionUsage_actionDefinition_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_ActionUsage_actionDefinition_feature", "_UI_ActionUsage_type"),
                SysmlPackage.eINSTANCE.getActionUsage_ActionDefinition(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Interaction feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addInteractionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_ItemFlow_interaction_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_ItemFlow_interaction_feature", "_UI_ItemFlow_type"),
                SysmlPackage.eINSTANCE.getItemFlow_Interaction(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Item Feature feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addItemFeaturePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_ItemFlow_itemFeature_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_ItemFlow_itemFeature_feature", "_UI_ItemFlow_type"),
                SysmlPackage.eINSTANCE.getItemFlow_ItemFeature(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Item Flow End feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addItemFlowEndPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_ItemFlow_itemFlowEnd_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_ItemFlow_itemFlowEnd_feature", "_UI_ItemFlow_type"),
                SysmlPackage.eINSTANCE.getItemFlow_ItemFlowEnd(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Item Type feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected void addItemTypePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_ItemFlow_itemType_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_ItemFlow_itemType_feature", "_UI_ItemFlow_type"),
                SysmlPackage.eINSTANCE.getItemFlow_ItemType(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Source Output Feature feature. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    protected void addSourceOutputFeaturePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_ItemFlow_sourceOutputFeature_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_ItemFlow_sourceOutputFeature_feature", "_UI_ItemFlow_type"),
                SysmlPackage.eINSTANCE.getItemFlow_SourceOutputFeature(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Target Input Feature feature. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    protected void addTargetInputFeaturePropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_ItemFlow_targetInputFeature_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_ItemFlow_targetInputFeature_feature", "_UI_ItemFlow_type"),
                SysmlPackage.eINSTANCE.getItemFlow_TargetInputFeature(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This adds a property descriptor for the Flow Connection Definition feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    protected void addFlowConnectionDefinitionPropertyDescriptor(Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                this.getResourceLocator(),
                this.getString("_UI_FlowConnectionUsage_flowConnectionDefinition_feature"),
                this.getString("_UI_PropertyDescriptor_description", "_UI_FlowConnectionUsage_flowConnectionDefinition_feature", "_UI_FlowConnectionUsage_type"),
                SysmlPackage.eINSTANCE.getFlowConnectionUsage_FlowConnectionDefinition(),
                true,
                false,
                true,
                null,
                null,
                null));
    }

    /**
     * This returns FlowConnectionUsage.svg. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Object getImage(Object object) {
        return this.overlayImage(object, this.getResourceLocator().getImage("full/obj16/FlowConnectionUsage.svg"));
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((FlowConnectionUsage) object).getName();
        return label == null || label.length() == 0 ? this.getString("_UI_FlowConnectionUsage_type") : this.getString("_UI_FlowConnectionUsage_type") + " " + label;
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
