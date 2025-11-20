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
package org.eclipse.syson.application.data;

/**
 * Project data for the "GV-SimpleNestedAction" project.
 *
 * @author arichard
 */
public class GVSimpleNestedActionTestProjectData {

    public static final String SCRIPT_PATH = "/scripts/database-content/GV-SimpleNestedAction.sql";

    public static final String EDITING_CONTEXT_ID = "f781efb8-51ca-4db0-87ad-e6373c59ffb5";

    /**
     *  Ids of graphical elements.
     */
    public static class GraphicalIds {

        public static final String DIAGRAM_ID = "4edeff5a-6309-49fc-ac11-dac21b7dfd43";

        public static final String ACTION_A_ID = "21bc7f4f-8b0b-3982-b3f5-87a6d1b5b7bc";

        public static final String ACTION_B_ID = "8099b56c-877c-3fd6-ab98-0b05c017ec33";

        public static final String A_B_EDGE_ID = "06c2c309-8ffa-3c72-85dd-de203d026ff2";

        public static final String ACTION_A_ACTION_FLOW_COMPARTMENT = "a69488c6-9ea4-3185-8699-2ac84baabdb1";

        public static final String ACTION_A_ACTIONS_COMPARTMENT = "b21fd6a5-a16b-3041-b2b0-335d88977ced";

        public static final String ACTION_A_ITEMS_COMPARTMENT = "bed5cc50-f3c2-3ac8-93fb-e255a8e70093";
    }

    /**
     * Ids for the semantic elements.
     */
    public static final class SemanticIds {

        public static final String ACTION_A_ELEMENT_ID = "21bc7f4f-8b0b-3982-b3f5-87a6d1b5b7bc";

        public static final String ACTION_B_ELEMENT_ID = "8099b56c-877c-3fd6-ab98-0b05c017ec33";
    }

}
