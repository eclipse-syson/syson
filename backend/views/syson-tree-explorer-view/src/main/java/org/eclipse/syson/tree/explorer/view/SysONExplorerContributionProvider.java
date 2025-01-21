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
package org.eclipse.syson.tree.explorer.view;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.trees.description.TreeDescription;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.view.emf.tree.ITreeIdProvider;
import org.eclipse.sirius.web.application.studio.services.api.IStudioCapableEditingContextPredicate;
import org.eclipse.sirius.web.application.views.explorer.services.api.IExplorerTreeDescriptionProvider;
import org.springframework.stereotype.Service;

/**
 * Provides the {@link TreeDescription}s to contribute to the Sirius Web explorer.
 *
 * @author gdaniel
 */
@Service
public class SysONExplorerContributionProvider implements IExplorerTreeDescriptionProvider {

    private final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    private final IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService;

    private final IStudioCapableEditingContextPredicate studioCapableEditingContextPredicate;

    public SysONExplorerContributionProvider(IRepresentationDescriptionSearchService representationDescriptionSearchService,
            IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService, IStudioCapableEditingContextPredicate studioCapableEditingContextPredicate) {
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
        this.viewRepresentationDescriptionSearchService = Objects.requireNonNull(viewRepresentationDescriptionSearchService);
        this.studioCapableEditingContextPredicate = Objects.requireNonNull(studioCapableEditingContextPredicate);
    }

    @Override
    public List<TreeDescription> getDescriptions(IEditingContext editingContext) {
        var optionalUMLExplorerDescription = this.getSysONExplorerTreeDescription(editingContext);
        if (optionalUMLExplorerDescription.isPresent()) {
            return List.of(optionalUMLExplorerDescription.get());
        }
        return List.of();
    }

    private Optional<TreeDescription> getSysONExplorerTreeDescription(IEditingContext editingContext) {
        Optional<TreeDescription> result = Optional.empty();
        if (!this.studioCapableEditingContextPredicate.test(editingContext.getId())) {
            // Do not use the custom SysON Explorer in studio project: they should be managed by the Sirius Web
            // explorer.
            result = this.representationDescriptionSearchService
                    .findAll(editingContext).values().stream()
                    .filter(TreeDescription.class::isInstance)
                    .map(TreeDescription.class::cast)
                    .filter(td -> this.isSysONExplorerView(td, editingContext))
                    .findFirst();
        }
        return result;
    }

    private boolean isSysONExplorerView(TreeDescription treeDescription, IEditingContext editingContext) {
        if (treeDescription.getId().startsWith(ITreeIdProvider.TREE_DESCRIPTION_KIND)) {
            // This tree description comes from a tree DSL
            var optionalViewTreeDescription = this.viewRepresentationDescriptionSearchService.findById(editingContext, treeDescription.getId());
            if (optionalViewTreeDescription.isPresent()) {
                return optionalViewTreeDescription.get().getName().equals(SysONExplorerTreeDescriptionProvider.SYSON_EXPLORER);
            }
        }
        return false;
    }

}
