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
package org.eclipse.syson.services.explorer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.components.collaborative.trees.api.TreeFilter;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.graphql.tests.api.IExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.components.trees.description.TreeDescription;
import org.eclipse.syson.services.explorer.api.IExplorerDefaultFiltersSearchService;
import org.eclipse.syson.tree.explorer.view.SysONTreeFilterProvider;
import org.springframework.stereotype.Service;

/**
 * Finds the default filters of a given explorer.
 *
 * @author gdaniel
 */
@Service
public class ExplorerDefaultFiltersSearchService implements IExplorerDefaultFiltersSearchService {

    private final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    private final SysONTreeFilterProvider sysonTreeFilterProvider;

    private final IExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    public ExplorerDefaultFiltersSearchService(IRepresentationDescriptionSearchService representationDescriptionSearchService, SysONTreeFilterProvider sysonTreeFilterProvider,
            IExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner) {
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
        this.sysonTreeFilterProvider = Objects.requireNonNull(sysonTreeFilterProvider);
        this.executeEditingContextFunctionRunner = Objects.requireNonNull(executeEditingContextFunctionRunner);
    }

    @Override
    public List<String> findTreeDefaultFilterIds(String editingContextId, String treeDescriptionId) {
        ExecuteEditingContextFunctionInput executeEditingContextFunctionInput = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), editingContextId, (editingContext, input) -> {
            TreeDescription treeDescription = this.representationDescriptionSearchService.findById(editingContext, treeDescriptionId)
                    .filter(TreeDescription.class::isInstance)
                    .map(TreeDescription.class::cast)
                    .orElse(null);
            List<String> defaultFilterIds = this.sysonTreeFilterProvider.get(null, treeDescription).stream()
                    .filter(TreeFilter::defaultState)
                    .map(TreeFilter::id)
                    .toList();
            return new ExecuteEditingContextFunctionSuccessPayload(input.id(), defaultFilterIds);
        });

        IPayload payload = this.executeEditingContextFunctionRunner.execute(executeEditingContextFunctionInput).block();
        assertThat(payload).isInstanceOf(ExecuteEditingContextFunctionSuccessPayload.class);
        return (List<String>) ((ExecuteEditingContextFunctionSuccessPayload) payload).result();
    }
}
