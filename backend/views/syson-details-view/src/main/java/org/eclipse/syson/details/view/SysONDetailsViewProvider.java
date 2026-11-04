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
package org.eclipse.syson.details.view;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.forms.description.FormDescription;
import org.eclipse.sirius.components.forms.description.PageDescription;
import org.eclipse.sirius.web.application.views.details.services.api.IDetailsViewPageDescriptionProviderDelegate;
import org.springframework.stereotype.Service;

/**
 * Service to get SysMLv2 pages for the Details View from the editing context.
 *
 * @author arichard
 */
@Service
public class SysONDetailsViewProvider implements IDetailsViewPageDescriptionProviderDelegate {

    private final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    public SysONDetailsViewProvider(IRepresentationDescriptionSearchService representationDescriptionSearchService) {
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
    }

    @Override
    public List<PageDescription> getPageDescriptions(IEditingContext editingContext) {
        return this.representationDescriptionSearchService.findAll(editingContext)
                .values()
                .stream()
                .filter(FormDescription.class::isInstance)
                .map(FormDescription.class::cast)
                .filter(form -> SysONDetailsViewDescriptionProvider.SYSON_DETAILS_VIEW_NAME
                        .equals(form.getLabel()))
                .flatMap(form -> form.getPageDescriptions().stream())
                .toList();
    }

}
