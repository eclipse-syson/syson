/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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

import java.util.List;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.web.application.views.explorer.services.api.IExplorerTreeAlteredContentProvider;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerFilterService;
import org.springframework.stereotype.Service;

/**
 * An implementation of {@link IExplorerTreeAlteredContentProvider} allowing to hide SysML standard libraries documents
 * from Explorer tree.
 *
 * @author arichard
 */
@Service
public class HideSysMLStandardLibrariesTreeAlteredContentProvider implements IExplorerTreeAlteredContentProvider {

    private final ISysONExplorerFilterService filterService;

    public HideSysMLStandardLibrariesTreeAlteredContentProvider(ISysONExplorerFilterService filterService) {
        this.filterService = filterService;
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, List<String> activeFilterIds) {
        return activeFilterIds.contains(SysONTreeFilterConstants.HIDE_SYSML_STANDARD_LIBRARIES_TREE_FILTER_ID);
    }

    @Override
    public List<Object> apply(List<Object> computedElements, VariableManager variableManager) {
        return this.filterService.hideSysMLStandardLibraries(computedElements);
    }
}
