/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.util;

/**
 * Used to store the identifier of the description used in SysON.
 *
 * @author gdaniel
 */
public final class SysONRepresentationDescriptionIdentifiers {

    // All the general view diagrams have the same description
    public static final String GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID = "siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089";

    public static final String INTERCONNECTION_VIEW_DIAGRAM_DESCRIPTION_ID = "siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=e1bd3b6d-357b-3068-b2e9-e0c1e19d6856";

    public static final String ACTION_FLOW_VIEW_DIAGRAM_DESCRIPTION_ID = "siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=0d6e5145-02ea-336b-b4d4-9be6b0e63786&sourceElementId=ea3511e5-5ba5-3d2c-8e80-9182db336675";

    public static final String STATE_TRANSITION_VIEW_DIAGRAM_DESCRIPTION_ID = "siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=067eb84f-3fe2-3b1b-9d45-9ffb9b5bb65e&sourceElementId=be71dab0-25e1-3180-90d0-24b31f68df8f";

    private SysONRepresentationDescriptionIdentifiers() {
        // Prevent instantiation
    }
}
