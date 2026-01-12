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

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.util.List;
import java.util.Map;

import org.eclipse.sirius.components.trees.tests.graphql.TreePathQueryRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.test.StepVerifier;

/**
 * Get the tree path of a tree item in the explorer.
 * <p>
 * This class should be used as part of a subscription verification (see
 * {@link StepVerifier#create(org.reactivestreams.Publisher)}).
 * </p>
 *
 * @author arichard
 */
@Service
public class TreePathTester {

    @Autowired
    private TreePathQueryRunner treePathQueryRunner;

    public List<String> getTreeItemIdsToExpand(String editingContextId, String treeId, List<String> selectionEntryIds) {
        Map<String, Object> getTreePathVariables = Map.of(
                "editingContextId", editingContextId,
                "treeId", treeId,
                "selectionEntryIds", selectionEntryIds);
        var result = this.treePathQueryRunner.run(getTreePathVariables);
        List<String> treeItemIdsToExpand = JsonPath.read(result.data(), "$.data.viewer.editingContext.treePath.treeItemIdsToExpand");
        assertThat(treeItemIdsToExpand).isNotEmpty();
        return treeItemIdsToExpand;
    }

}
