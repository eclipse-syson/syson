/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.application.controller.explorer.testers;

import com.jayway.jsonpath.JsonPath;

import java.util.List;
import java.util.Map;

import org.eclipse.sirius.web.tests.graphql.TreeItemContextMenuQueryRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Gets the context menu of a tree item in the explorer.
 *
 * @author gdaniel
 */
@Service
public class TreeItemContextMenuTester {

    @Autowired
    private TreeItemContextMenuQueryRunner treeItemContextMenuQueryRunner;

    public List<String> getContextMenuEntries(String editingContextId, String representationId, String treeItemId) {
        Map<String, Object> contextMenuVariables = Map.of(
                "editingContextId", editingContextId,
                "representationId", representationId,
                "treeItemId", treeItemId);
        var result = this.treeItemContextMenuQueryRunner.run(contextMenuVariables);
        List<String> contextMenuIds = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.contextMenu[*].id");
        return contextMenuIds;
    }

}
