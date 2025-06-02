/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.tree.explorer.view.services;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.representations.IRepresentationDescription;
import org.eclipse.sirius.components.representations.IRepresentationRenderVariableCustomizer;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.web.application.UUIDParser;
import org.eclipse.sirius.web.application.views.explorer.services.ExplorerDescriptionProvider;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.syson.tree.explorer.view.SysONExplorerTreeDescriptionProvider;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

/**
 * Provides additional render variables to be used by {@link SysONExplorerTreeDescriptionProvider} to reduce the amount of duplicate work (notably database access):
 *
 * <dl>
 * <dt>{@code existingRepresentations}<dt>
 * <dd>the list of all {@RepresentationMetadata representations} present in the current editing context.</dd>
 * </dl>
 *
 * @author arichard
 */
@Service
public class SysONExplorerRenderVariablesCustomizer implements IRepresentationRenderVariableCustomizer {

    private final IRepresentationMetadataSearchService representationMetadataSearchService;

    public SysONExplorerRenderVariablesCustomizer(IRepresentationMetadataSearchService representationMetadataSearchService) {
        this.representationMetadataSearchService = Objects.requireNonNull(representationMetadataSearchService);
    }

    @Override
    public VariableManager customize(IRepresentationDescription representationDescription, VariableManager variableManager) {
        if (SysONExplorerTreeDescriptionProvider.SYSON_EXPLORER.equals(representationDescription.getLabel())) {
            var optionalEditingContext = variableManager.get(IEditingContext.EDITING_CONTEXT, IEditingContext.class);
            if (optionalEditingContext.isPresent()) {
                VariableManager customizedVariableManager = variableManager.createChild();
                String editingContextId = optionalEditingContext.get().getId();
                var optionalSemanticDataId = new UUIDParser().parse(editingContextId);
                List<RepresentationMetadata> allRepresentationMetadata = this.representationMetadataSearchService.findAllRepresentationMetadataBySemanticData(AggregateReference.to(optionalSemanticDataId.get()));
                customizedVariableManager.put(ExplorerDescriptionProvider.EXISTING_REPRESENTATIONS, allRepresentationMetadata);
                return customizedVariableManager;
            }
        }
        return variableManager;
    }
}
