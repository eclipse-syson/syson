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

package org.eclipse.syson.application.migration;

import org.eclipse.sirius.components.collaborative.representations.migration.IRepresentationMigrationParticipant;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.springframework.stereotype.Service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ObjectNode;

/**
 * Migration participant used to migrate start, done actions and control nodes elements previous to 2026.7.0.
 *
 * We need to update descriptionId, because it has been changed due to VSM description change.
 *
 * @author Jerome Gout
 */
@Service
public class StartDoneAndControlNodeMigrationParticipant implements IRepresentationMigrationParticipant {

    public static final String PARTICIPANT_VERSION = "2026.7.0-202606240000";
    public static final String DESCRIPTION_ID = "descriptionId";

    public static final String OLD_START_DESCRIPTION_ID = "siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=bf75f645-8a6d-30d9-9514-4e9738e212a3";
    public static final String NEW_START_DESCRIPTION_ID = "siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=596f7e8d-ba35-38d6-937f-011e05b74063";

    public static final String OLD_DONE_DESCRIPTION_ID = "siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=03789d5f-51d6-3f66-a496-146b1685930c";
    public static final String NEW_DONE_DESCRIPTION_ID = "siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=e7465fdc-fec8-38da-b7c0-26ba953b96de";

    public static final String OLD_DECISION_DESCRIPTION_ID = "siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=d6e7c035-98ae-3fcf-a5a8-a8e502cd447e";
    public static final String NEW_DECISION_DESCRIPTION_ID = "siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=b2f1248d-2a25-37a2-8822-9e21ed709c72";

    public static final String OLD_FORK_DESCRIPTION_ID = "siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=46dc7020-1788-3413-967d-e1e3053349ca";
    public static final String NEW_FORK_DESCRIPTION_ID = "siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=e940b643-cce5-3904-b785-fb3814493642";

    public static final String OLD_JOIN_DESCRIPTION_ID = "siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=ae252080-4a68-3129-970b-2d049bb3e5a3";
    public static final String NEW_JOIN_DESCRIPTION_ID = "siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=963e573a-d0ff-3134-9e96-9ce55e3384ec";

    public static final String OLD_MERGE_DESCRIPTION_ID = "siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=91f597c3-96b8-3a9e-8b30-99e5401952fa";
    public static final String NEW_MERGE_DESCRIPTION_ID = "siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=d47553da-9705-3eca-99ea-dc05ff972f55";

    @Override
    public String getVersion() {
        return PARTICIPANT_VERSION;
    }

    @Override
    public String getKind() {
        return "siriusComponents://representation?type=Diagram";
    }

    @Override
    public void replaceJsonNode(IEditingContext editingContext, ObjectNode root, String currentAttribute, JsonNode currentValue) {
        if (currentAttribute.equals(DESCRIPTION_ID) && currentValue.isString()) {
            if (OLD_START_DESCRIPTION_ID.equals(currentValue.asString())) {
                root.put(DESCRIPTION_ID, NEW_START_DESCRIPTION_ID);
            }
            if (OLD_DONE_DESCRIPTION_ID.equals(currentValue.asString())) {
                root.put(DESCRIPTION_ID, NEW_DONE_DESCRIPTION_ID);
            }
            if (OLD_DECISION_DESCRIPTION_ID.equals(currentValue.asString())) {
                root.put(DESCRIPTION_ID, NEW_DECISION_DESCRIPTION_ID);
            }
            if (OLD_FORK_DESCRIPTION_ID.equals(currentValue.asString())) {
                root.put(DESCRIPTION_ID, NEW_FORK_DESCRIPTION_ID);
            }
            if (OLD_JOIN_DESCRIPTION_ID.equals(currentValue.asString())) {
                root.put(DESCRIPTION_ID, NEW_JOIN_DESCRIPTION_ID);
            }
            if (OLD_MERGE_DESCRIPTION_ID.equals(currentValue.asString())) {
                root.put(DESCRIPTION_ID, NEW_MERGE_DESCRIPTION_ID);
            }
        }
    }
}
