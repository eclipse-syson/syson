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
package org.eclipse.syson.application.index;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.web.application.UUIDParser;
import org.eclipse.sirius.web.application.index.services.api.IIndexEntry;
import org.eclipse.sirius.web.application.index.services.api.IIndexEntryProviderDelegate;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataSearchService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author gdaniel
 */
@Service
public class ElementIndexEntryProvider implements IIndexEntryProviderDelegate {

    private final ISemanticDataSearchService semanticDataSearchService;

    private final IIdentityService identityService;

    private final ILabelService labelService;

    public ElementIndexEntryProvider(ISemanticDataSearchService semanticDataSearchService, IIdentityService identityService, ILabelService labelService) {
        this.semanticDataSearchService = Objects.requireNonNull(semanticDataSearchService);
        this.identityService = Objects.requireNonNull(identityService);
        this.labelService = Objects.requireNonNull(labelService);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, Object object) {
        // Only produce SysON index entries for editing context using the SysML domain.
        // Other editing context should still be indexed with Sirius Web's default indexing strategy.
        return new UUIDParser().parse(editingContext.getId())
                .map(id -> this.semanticDataSearchService.isUsingDomains(id, List.of(SysmlPackage.eNS_URI)))
                .orElse(false);
    }

    @Override
    public Optional<IIndexEntry> getIndexEntry(IEditingContext editingContext, Object object) {
        Optional<IIndexEntry> result = Optional.empty();
        if (object instanceof Element element) {
            result = new IndexEntrySwitch(this.identityService, this.labelService, editingContext)
                    .doSwitch(element);
        }
        return result;
    }
}
