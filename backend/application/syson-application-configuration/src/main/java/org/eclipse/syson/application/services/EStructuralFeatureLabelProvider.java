/*******************************************************************************
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
 *******************************************************************************/
package org.eclipse.syson.application.services;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory.Descriptor;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.syson.sysml.Element;

/**
 * Utility class used to provide a label for a structural feature in the variable manager.
 *
 * @author arichard
 */
public class EStructuralFeatureLabelProvider implements BiFunction<Element, EStructuralFeature, String> {

    private final List<Descriptor> composedAdapterFactoryDescriptors;

    public EStructuralFeatureLabelProvider(List<Descriptor> composedAdapterFactoryDescriptors) {
        this.composedAdapterFactoryDescriptors = Objects.requireNonNull(composedAdapterFactoryDescriptors);
    }

    @Override
    public String apply(Element element, EStructuralFeature eStructuralFeature) {
        String displayName = "";
        List<AdapterFactory> adapterFactories = this.composedAdapterFactoryDescriptors.stream()
                .map(Descriptor::createAdapterFactory)
                .toList();
        var composedAdapterFactory = new ComposedAdapterFactory(adapterFactories);
        var adapter = composedAdapterFactory.adapt(element, IItemPropertySource.class);
        if (adapter instanceof IItemPropertySource itemPropertySource) {
            IItemPropertyDescriptor descriptor = itemPropertySource.getPropertyDescriptor(element, eStructuralFeature);
            if (descriptor != null) {
                displayName = descriptor.getDisplayName(eStructuralFeature);
            }
        }
        composedAdapterFactory.dispose();
        return displayName;
    }

}
