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
package org.eclipse.syson.tree.explorer.view.menu.context;

import java.util.Objects;

import org.eclipse.sirius.components.collaborative.trees.api.ITreeItemContextMenuEntryProvider;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.trees.Tree;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.components.trees.description.TreeDescription;
import org.eclipse.sirius.web.application.views.explorer.services.ExplorerDescriptionProvider;
import org.eclipse.sirius.web.application.views.explorer.services.ExplorerTreeItemContextMenuEntryProvider;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataSearchService;
import org.eclipse.syson.tree.explorer.view.SysONTreeViewDescriptionProvider;
import org.springframework.stereotype.Service;

/**
 * Customization of {@link ExplorerTreeItemContextMenuEntryProvider} for SysON to provide the contextual menu entries in
 * the 'Explorer' view.
 * 
 * @author flatombe
 */
@Service
public class SysONExplorerTreeItemContextMenuEntryProvider extends ExplorerTreeItemContextMenuEntryProvider implements ITreeItemContextMenuEntryProvider {

    private final SysONTreeViewDescriptionProvider sysONTreeViewDescriptionProvider;

    public SysONExplorerTreeItemContextMenuEntryProvider(final IObjectSearchService objectSearchService,
            final ILibrarySearchService librarySearchService,
            final ISemanticDataSearchService semanticDataSearchService,
            final SysONTreeViewDescriptionProvider sysONTreeViewDescriptionProvider) {
        super(objectSearchService, librarySearchService, semanticDataSearchService);
        this.sysONTreeViewDescriptionProvider = Objects.requireNonNull(sysONTreeViewDescriptionProvider);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, TreeDescription treeDescription, Tree tree, TreeItem treeItem) {
        return tree.getId().startsWith(ExplorerDescriptionProvider.PREFIX)
                && Objects.equals(tree.getDescriptionId(), sysONTreeViewDescriptionProvider.getDescriptionId());
    }

}
