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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.omnibox.api.IOmniboxCommandProvider;
import org.eclipse.sirius.components.collaborative.omnibox.dto.OmniboxCommand;
import org.eclipse.syson.application.omnibox.api.IPredicateCanEditingContextPublishSysMLProject;
import org.springframework.stereotype.Service;

/**
 * The {@link IOmniboxCommandProvider} implementation for SysON.
 *
 * @author flatombe
 */
@Service
public class SysONOmniboxCommandProvider implements IOmniboxCommandProvider {

    public static final String PUBLISH_SYSML_PROJECT_COMMAND_ID = "publishProjectSysMLContentsAsLibrary";

    public static final String IMPORT_PUBLISHED_LIBRARY_COMMAND_ID = "importPublishedLibrary";

    private final IPredicateCanEditingContextPublishSysMLProject predicateCanEditingContextPublishSysMLProject;

    public SysONOmniboxCommandProvider(final IPredicateCanEditingContextPublishSysMLProject predicateCanEditingContextPublishSysMLProject) {
        this.predicateCanEditingContextPublishSysMLProject = Objects.requireNonNull(predicateCanEditingContextPublishSysMLProject);
    }

    @Override
    public List<OmniboxCommand> getCommands(String editingContextId, List<String> selectedObjectIds, String query) {
        final List<OmniboxCommand> result = new ArrayList<>();

        if (this.predicateCanEditingContextPublishSysMLProject.test(editingContextId)) {
            result.add(new OmniboxCommand(PUBLISH_SYSML_PROJECT_COMMAND_ID, "Publish SysML project contents as library", List.of("/images/omnibox/publish.svg"),
                    "Publishes the SysML contents of the project as a library that can be referenced by other projects."));
        }
        result.add(new OmniboxCommand(IMPORT_PUBLISHED_LIBRARY_COMMAND_ID, "Import published library", List.of("/images/omnibox/import.svg"),
                "Imports one of the published libraries."));

        return result;
    }
}
