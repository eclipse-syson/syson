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
package org.eclipse.syson.tree.explorer.view.duplicate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.browser.api.IModelBrowserTreeDescriptionIdProviderDelegate;
import org.eclipse.sirius.components.core.URLParser;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Relationship;
import org.springframework.stereotype.Service;

/**
 * {@link IModelBrowserTreeDescriptionIdProviderDelegate} that provides a custom tree to select the future owner of a duplication.
 *
 * @author Arthur Daussy
 */
@Service
public class SysONDuplicateTargetBrowserTreeDescriptionIdProviderDelegate implements IModelBrowserTreeDescriptionIdProviderDelegate {

    private final URLParser urlParser;

    private final IObjectSearchService objectSearchService;

    public SysONDuplicateTargetBrowserTreeDescriptionIdProviderDelegate(URLParser urlParser, IObjectSearchService objectSearchService) {
        this.urlParser = Objects.requireNonNull(urlParser);
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, String modelBrowserId) {
        Map<String, List<String>> parameters = this.urlParser.getParameterValues(modelBrowserId);
        List<String> descriptionIds = parameters.get("descriptionId");
        if (descriptionIds != null && descriptionIds.stream().anyMatch("duplicate-target-browser"::equals)) {
            List<String> ownerKind = parameters.get("ownerId");
            return ownerKind != null && ownerKind.stream().map(id -> this.objectSearchService.getObject(editingContext, id))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst()
                    .map(object -> object instanceof Element && !(object instanceof Relationship))
                    .orElse(false);
        }

        return false;
    }

    @Override
    public String getModelBrowserTreeDescriptionId(IEditingContext editingContext, String modelBrowserId) {
        return SysONDuplicateTargetBrowserTreeDescriptionProvider.SYSON_DUPLICATE_TARGET_BROWSER_TREE_REPRESENTATION_ID;
    }
}
