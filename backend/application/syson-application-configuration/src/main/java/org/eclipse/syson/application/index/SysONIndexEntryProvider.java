/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.application.index;

import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.web.application.index.services.api.IIndexEntry;
import org.eclipse.sirius.web.application.index.services.api.IIndexEntryProviderDelegate;
import org.eclipse.sirius.web.application.studio.services.api.IStudioCapableEditingContextPredicate;
import org.eclipse.syson.sysml.Element;
import org.springframework.stereotype.Service;

/**
 * Provides index entries for SysON.
 *
 * <p>
 * This service converts SysML {@link Element} into {@link IIndexEntry} that can be persisted in the Elasticsearch indices.
 * </p>
 * <p>
 * Note that this service does not accept {@code editingContext} instances that contain Studio data. For these {@code editingContext}, the default implementation of
 * {@link org.eclipse.sirius.web.application.index.services.api.IDefaultIndexEntryProvider} is used instead.
 * </p>
 *
 * @author gdaniel
 */
@Service
public class SysONIndexEntryProvider implements IIndexEntryProviderDelegate {

    private final IStudioCapableEditingContextPredicate studioCapableEditingContextPredicate;

    private final IIdentityService identityService;

    private final ILabelService labelService;

    public SysONIndexEntryProvider(IStudioCapableEditingContextPredicate studioCapableEditingContextPredicate, IIdentityService identityService, ILabelService labelService) {
        this.studioCapableEditingContextPredicate = Objects.requireNonNull(studioCapableEditingContextPredicate);
        this.identityService = Objects.requireNonNull(identityService);
        this.labelService = Objects.requireNonNull(labelService);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, Object object) {
        // Only produce SysON index entries for non-studio projects.
        return !this.studioCapableEditingContextPredicate.test(editingContext.getId());
    }

    @Override
    public Optional<IIndexEntry> getIndexEntry(IEditingContext editingContext, Object object) {
        Optional<IIndexEntry> result = Optional.empty();
        if (object instanceof Element element) {
            result = new IndexEntrySwitch(this.identityService, this.labelService, editingContext)
                    .doSwitch(element);
        }
        return result;
    }
}
