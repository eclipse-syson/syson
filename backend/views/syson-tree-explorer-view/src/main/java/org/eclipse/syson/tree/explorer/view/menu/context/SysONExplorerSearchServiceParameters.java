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
package org.eclipse.syson.tree.explorer.view.menu.context;

import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataSearchService;
import org.springframework.stereotype.Service;

/**
 * Bundles the various search-related services needed by {@link SysONExplorerTreeItemContextMenuEntryProvider}.
 *
 * @author pcdavid
 */
@Service
public record SysONExplorerSearchServiceParameters(
        IObjectSearchService objectSearchService,
        ILibrarySearchService librarySearchService,
        ISemanticDataSearchService semanticDataSearchService,
        IRepresentationMetadataSearchService representationMetadataSearchService) {
}
