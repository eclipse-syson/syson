/*******************************************************************************
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
 *******************************************************************************/
package org.eclipse.syson.application.services;

import java.util.Objects;
import java.util.function.BiFunction;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.syson.sysml.Element;

/**
 * Utility class used to provide a label for a structural feature in the variable manager.
 *
 * @author arichard
 */
public class EStructuralFeatureLabelProvider implements BiFunction<Element, EStructuralFeature, String> {

    private AdapterFactory adapterFactory;

    public EStructuralFeatureLabelProvider(AdapterFactory adapterFactory) {
        this.adapterFactory = Objects.requireNonNull(adapterFactory);
    }

    @Override
    public String apply(Element element, EStructuralFeature feature) {
        EStructuralFeature eStructuralFeature = feature;

        Adapter adapter = this.adapterFactory.adapt(element, IItemPropertySource.class);
        if (adapter instanceof IItemPropertySource) {
            IItemPropertySource itemPropertySource = (IItemPropertySource) adapter;
            IItemPropertyDescriptor descriptor = itemPropertySource.getPropertyDescriptor(element, eStructuralFeature);
            if (descriptor != null) {
                String displayName = descriptor.getDisplayName(eStructuralFeature);
                return displayName;
            }
        }
        return "";
    }

}
