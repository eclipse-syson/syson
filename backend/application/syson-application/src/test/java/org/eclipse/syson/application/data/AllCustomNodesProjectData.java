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

import org.eclipse.syson.sysml.helper.LabelConstants;

/**
 * Ids for project "AllCustomNodes".
 *
 * @author arichard
 */
public class AllCustomNodesProjectData {

    public static final String EDITING_CONTEXT_ID = "aeb3587e-29ae-425e-b4fb-c33745002aac";

    public static final String SCRIPT_PATH = "/scripts/database-content/AllCustomNodes.sql";

    /**
     * Ids of the graphical elements elements.
     *
     * @author Arthur Daussy
     */
    public static final class GraphicalIds {

        public static final String DIAGRAM_ID = "894983da-5371-41ef-bc80-9dc8a120a226";

    }

    /**
     * Ids of the semantic elements.
     *
     * @author arichard
     */
    public static final class SemanticIds {

        public static final String ROOT_PACKAGE_ELEMENT_ID = "dd708dd2-a885-4bad-9900-5d20b8cd59d1";
    }

    /**
     * Labels of the semantic elements.
     *
     * @author arichard
     */
    public static final class Labels {

        public static final String PACKAGE = "Package1";

        public static final String IMPORTED_PACKAGE = LabelConstants.OPEN_QUOTE + "private" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "Actions";

        public static final String NOTE = LabelConstants.OPEN_QUOTE + "comment" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + LabelConstants.CR + "add comment here";

        public static final String VIEW_FRAME = LabelConstants.OPEN_QUOTE + "view" + LabelConstants.CLOSE_QUOTE + " view2 : StandardViewDefinitions::GeneralView";

    }
}
