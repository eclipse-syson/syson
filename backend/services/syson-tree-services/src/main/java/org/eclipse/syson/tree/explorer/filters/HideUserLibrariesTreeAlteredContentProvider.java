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
package org.eclipse.syson.tree.explorer.filters;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.web.application.views.explorer.services.api.IExplorerTreeAlteredContentProvider;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * An implementation of {@link IExplorerTreeAlteredContentProvider} allowing to hide user libraries documents from
 * Explorer tree.
 *
 * @author gdaniel
 */
@Service
public class HideUserLibrariesTreeAlteredContentProvider implements IExplorerTreeAlteredContentProvider {

    private final Logger logger = LoggerFactory.getLogger(HideUserLibrariesTreeAlteredContentProvider.class);

    private final ISysONExplorerFilterService filterService;

    public HideUserLibrariesTreeAlteredContentProvider(ISysONExplorerFilterService filterService) {
        this.filterService = filterService;
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, List<String> activeFilterIds) {
        return activeFilterIds.contains(SysONTreeFilterProvider.HIDE_USER_LIBRARIES_TREE_FILTER_ID);
    }

    @Override
    public List<Object> apply(List<Object> computedElements, VariableManager variableManager) {
        List<Object> result = new ArrayList<>();
        var optionalEditingContext = variableManager.get(IEditingContext.EDITING_CONTEXT, IEditingContext.class);
        if (optionalEditingContext.isPresent()) {
            result = this.filterService.hideUserLibraries(optionalEditingContext.get(), computedElements);
        } else {
            this.logger.warn("Cannot find variable editingContext");
        }
        return result;
    }
}
