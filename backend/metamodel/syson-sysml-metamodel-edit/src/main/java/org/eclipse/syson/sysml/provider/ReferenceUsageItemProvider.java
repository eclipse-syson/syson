/*******************************************************************************
 * Copyright (c) 2023, 2026 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.syson.sysml.provider;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.syson.sysml.Behavior;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Step;

/**
 * This is the item provider adapter for a {@link org.eclipse.syson.sysml.ReferenceUsage} object. <!-- begin-user-doc
 * --> <!-- end-user-doc -->
 *
 * @generated
 */
public class ReferenceUsageItemProvider extends UsageItemProvider {
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ReferenceUsageItemProvider(AdapterFactory adapterFactory) {
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

        }
        return this.itemPropertyDescriptors;
    }

    /**
     * This returns ReferenceUsage.svg. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Object getImage(Object object) {
        StringBuilder iconPath = new StringBuilder();
        boolean isParameter = this.isParameter(object);
        if (isParameter) {
            iconPath.append("full/obj16/parameter");
        } else {
            iconPath.append("full/obj16/ReferenceUsage");
        }
        if (object instanceof ReferenceUsage referenceUsage) {
            FeatureDirectionKind direction = referenceUsage.getDirection();
            if (direction != null) {
                iconPath.append(StringUtils.capitalize(direction.getLiteral()));
            }
        }
        iconPath.append(".svg");
        return this.overlayImage(object, this.getResourceLocator().getImage(iconPath.toString()));
    }

    /**
     * @generated NOT
     */
    private boolean isParameter(Object object) {
        return object instanceof ReferenceUsage referenceUsage && (referenceUsage.getOwningType() instanceof Step || referenceUsage.getOwningType() instanceof Behavior)
                && referenceUsage.getDirection() != null;
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((ReferenceUsage) object).getName();
        return label == null || label.length() == 0 ? this.getString("_UI_ReferenceUsage_type") : this.getString("_UI_ReferenceUsage_type") + " " + label;
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
