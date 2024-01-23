/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
import java.util.Optional;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.sirius.components.core.api.IDefaultObjectService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectServiceDelegate;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.NamespaceImport;
import org.springframework.stereotype.Service;

/**
 * Specific {@link IObjectServiceDelegate} to handle edition of SysML elements.
 *
 * @author arichard
 */
@Service
public class SysMLv2ObjectService implements IObjectServiceDelegate {

    private final IDefaultObjectService defaultObjectService;

    private final ComposedAdapterFactory composedAdapterFactory;

    public SysMLv2ObjectService(IDefaultObjectService defaultObjectService, ComposedAdapterFactory composedAdapterFactory) {
        this.defaultObjectService = Objects.requireNonNull(defaultObjectService);
        this.composedAdapterFactory = Objects.requireNonNull(composedAdapterFactory);
    }

    @Override
    public boolean canHandle(Object object) {
        return object instanceof Element;
    }

    @Override
    public boolean canHandle(IEditingContext editingContext) {
        return false;
    }

    @Override
    public String getId(Object object) {
        return this.defaultObjectService.getId(object);
    }

    @Override
    public String getLabel(Object object) {
        String label = null;
        if (object instanceof NamespaceImport nsImport) {
            Adapter adapter = this.composedAdapterFactory.adapt(nsImport, IItemLabelProvider.class);
            if (adapter instanceof IItemLabelProvider labelProvider) {
                label = labelProvider.getText(object);
            }
        } else {
            label = this.defaultObjectService.getLabel(object);
        }
        return label;
    }

    @Override
    public String getKind(Object object) {
        return this.defaultObjectService.getKind(object);
    }

    @Override
    public String getFullLabel(Object object) {
        return this.defaultObjectService.getFullLabel(object);
    }

    @Override
    public List<String> getImagePath(Object object) {
        return this.defaultObjectService.getImagePath(object);
    }

    @Override
    public Optional<Object> getObject(IEditingContext editingContext, String objectId) {
        return this.defaultObjectService.getObject(editingContext, objectId);
    }

    @Override
    public List<Object> getContents(IEditingContext editingContext, String objectId) {
        return this.defaultObjectService.getContents(editingContext, objectId);
    }

    @Override
    public Optional<String> getLabelField(Object object) {
        return this.defaultObjectService.getLabelField(object);
    }

    @Override
    public boolean isLabelEditable(Object object) {
        return this.defaultObjectService.isLabelEditable(object);
    }
}
