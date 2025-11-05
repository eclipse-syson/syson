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

import java.util.List;

import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.web.application.views.explorer.services.api.IExplorerTreeItemAlteredContentProvider;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerFilterService;
import org.springframework.stereotype.Service;

/**
 * An implementation of {@link IExplorerTreeItemAlteredContentProvider} allowing to hide root namespace tree items from
 * Explorer tree without hide their children.
 *
 * @author gdaniel
 */
@Service
public class HideRootNamespaceTreeItemAlteredContentProvider implements IExplorerTreeItemAlteredContentProvider {

    private final ISysONExplorerFilterService filterService;

    public HideRootNamespaceTreeItemAlteredContentProvider(ISysONExplorerFilterService filterService) {
        this.filterService = filterService;
    }

    @Override
    public boolean canHandle(Object object, List<String> activeFilterIds) {
        return activeFilterIds.contains(SysONTreeFilterProvider.HIDE_ROOT_NAMESPACES_ID);
    }

    @Override
    public List<Object> apply(List<Object> computedChildren, VariableManager variableManager) {
        return this.filterService.hideRootNamespace(computedChildren);
    }

}
