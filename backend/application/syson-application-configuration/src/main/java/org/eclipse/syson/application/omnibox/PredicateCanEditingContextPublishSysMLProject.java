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
package org.eclipse.syson.application.omnibox;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.web.application.UUIDParser;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataSearchService;
import org.eclipse.syson.application.omnibox.api.IPredicateCanEditingContextPublishSysMLProject;
import org.eclipse.syson.sysml.SysmlPackage;
import org.springframework.stereotype.Service;

/**
 * {@link IPredicateCanEditingContextPublishSysMLProject} implementation that checks whether any of the resources in the
 * editing context is a SysML resource.
 *
 * @author flatombe
 */
@Service
public class PredicateCanEditingContextPublishSysMLProject implements IPredicateCanEditingContextPublishSysMLProject {

    private final ISemanticDataSearchService semanticDataSearchService;

    public PredicateCanEditingContextPublishSysMLProject(final ISemanticDataSearchService semanticDataSearchService) {
        this.semanticDataSearchService = Objects.requireNonNull(semanticDataSearchService);
    }

    @Override
    public boolean test(final String editingContextId) {
        return new UUIDParser().parse(editingContextId)
                .map(uuid -> this.semanticDataSearchService.isUsingDomains(uuid, List.of(SysmlPackage.eNS_URI)))
                .orElse(false);
    }
}
