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
package org.eclipse.syson.application.controller.explorer.testers;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.util.List;
import java.util.Map;

import org.eclipse.sirius.components.trees.tests.graphql.ExpandAllTreePathQueryRunner;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.test.StepVerifier;

/**
 * Expands a tree item in the explorer.
 * <p>
 * This class should be used as part of a subscription verification (see
 * {@link StepVerifier#create(org.reactivestreams.Publisher)}).
 * </p>
 *
 * @author gdaniel
 */
@Service
public class ExpandTreeItemTester {

    @Autowired
    private ExpandAllTreePathQueryRunner expandAllTreePathQueryRunner;

    public List<String> expandTreeItem(String projectId, String treeId, String treeItemId) {
        Map<String, Object> expandVariables = Map.of(
                "editingContextId", SysMLv2Identifiers.GENERAL_VIEW_EMPTY_PROJECT,
                "treeId", treeId,
                "treeItemId", treeItemId);
        String result = this.expandAllTreePathQueryRunner.run(expandVariables);
        List<String> treeItemIdsToExpand = JsonPath.read(result, "$.data.viewer.editingContext.expandAllTreePath.treeItemIdsToExpand");
        assertThat(treeItemIdsToExpand).isNotEmpty();
        return treeItemIdsToExpand;
    }

}
