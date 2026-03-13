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
package org.eclipse.syson.table.requirements.view;

import java.util.List;

import org.eclipse.sirius.components.collaborative.tables.api.ITableInput;
import org.eclipse.sirius.components.collaborative.tables.api.IToolMenuEntriesProvider;
import org.eclipse.sirius.components.collaborative.tables.dto.ToolMenuEntry;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.tables.Table;
import org.eclipse.sirius.components.tables.descriptions.TableDescription;
import org.springframework.stereotype.Service;

/**
 * Provide tools to Requirements Table Views.
 *
 * @author frouene
 */
@Service
public class RTVTableToolMenuEntriesProvider implements IToolMenuEntriesProvider {

    public static final String ADD_REQUIREMENT_TABLE_TOOL_ENTRY = "add-requirement-table-tool-entry";
    public static final String IMPORT_EXISTING_REQUIREMENTS_TABLE_TOOL_ENTRY = "import-existing-requirements-table-tool-entry";

    @Override
    public boolean canHandle(IEditingContext editingContext, Table table, TableDescription tableDescription, ITableInput tableInput) {
        return tableDescription.getLabel().equals(RTVTableDescriptionProvider.DESCRIPTION_NAME);
    }

    @Override
    public List<ToolMenuEntry> getToolMenuEntries(IEditingContext editingContext, Table table, TableDescription tableDescription, ITableInput tableInput) {
        return List.of(new ToolMenuEntry(ADD_REQUIREMENT_TABLE_TOOL_ENTRY, "Create requirement", List.of("/images/createRequirement.svg")),
                new ToolMenuEntry(IMPORT_EXISTING_REQUIREMENTS_TABLE_TOOL_ENTRY, "Import all existing requirements", List.of("/images/importRequirements.svg")));
    }
}
