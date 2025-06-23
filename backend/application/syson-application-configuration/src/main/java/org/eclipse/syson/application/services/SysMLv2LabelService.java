/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.sirius.components.core.api.IDefaultLabelService;
import org.eclipse.sirius.components.core.api.ILabelServiceDelegate;
import org.eclipse.sirius.components.core.api.labels.StyledString;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.SysmlPackage;
import org.springframework.stereotype.Service;

/**
 * Specific {@link ILabelServiceDelegate} to handle labels of SysML elements.
 *
 * @author arichard
 */
@Service
public class SysMLv2LabelService implements ILabelServiceDelegate {

    private final IDefaultLabelService defaultLabelService;

    private final ComposedAdapterFactory composedAdapterFactory;

    public SysMLv2LabelService(IDefaultLabelService defaultObjectService, ComposedAdapterFactory composedAdapterFactory) {
        this.defaultLabelService = Objects.requireNonNull(defaultObjectService);
        this.composedAdapterFactory = Objects.requireNonNull(composedAdapterFactory);
    }

    @Override
    public boolean canHandle(Object object) {
        return object instanceof Element || (object instanceof EClass eClass && SysmlPackage.eINSTANCE.getElement().isSuperTypeOf(eClass));
    }

    @Override
    public StyledString getStyledLabel(Object object) {
        StyledString styledLabel = null;
        if (object instanceof Import imprt) {
            Adapter adapter = this.composedAdapterFactory.adapt(imprt, IItemLabelProvider.class);
            if (adapter instanceof IItemLabelProvider labelProvider) {
                styledLabel = StyledString.of(labelProvider.getText(object));
            }
        } else if (object instanceof EClass eClass && SysmlPackage.eINSTANCE.getElement().isSuperTypeOf(eClass)) {
            EObject dummyElement = EcoreUtil.create(eClass);
            Adapter adapter = this.composedAdapterFactory.adapt(dummyElement, IItemLabelProvider.class);
            if (adapter instanceof IItemLabelProvider labelProvider) {
                styledLabel = StyledString.of(labelProvider.getText(dummyElement));
            }
        } else {
            styledLabel = this.defaultLabelService.getStyledLabel(object);
        }
        return styledLabel;
    }

    @Override
    public List<String> getImagePaths(Object object) {
        return this.defaultLabelService.getImagePaths(object);
    }
}
