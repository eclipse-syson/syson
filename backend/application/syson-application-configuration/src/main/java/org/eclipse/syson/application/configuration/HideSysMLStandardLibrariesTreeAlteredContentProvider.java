/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.application.configuration;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.web.services.explorer.api.IExplorerTreeAlteredContentProvider;
import org.springframework.stereotype.Service;

/**
 * An implementation of {@link IExplorerTreeAlteredContentProvider} allowing to hide SysML standard libraries documents
 * from Explorer tree.
 *
 * @author arichard
 */
@Service
public class HideSysMLStandardLibrariesTreeAlteredContentProvider implements IExplorerTreeAlteredContentProvider {

    @Override
    public boolean canHandle(IEditingContext editingContext, List<String> activeFilterIds) {
        return activeFilterIds.contains(SysONTreeFilterProvider.HIDE_SYSML_STANDARD_LIBRARIES_TREE_FILTER_ID);
    }

    @Override
    public List<Object> apply(List<Object> computedElements, VariableManager variableManager) {
        return computedElements.stream()
                .filter(element -> !(element instanceof Resource res && res.getURI() != null && res.getURI().toString().startsWith(SysMLStandardLibrariesConfiguration.SYSML_LIBRARY_SCHEME)))
                .toList();
    }
}
