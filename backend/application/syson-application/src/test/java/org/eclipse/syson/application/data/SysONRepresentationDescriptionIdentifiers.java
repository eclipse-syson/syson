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
package org.eclipse.syson.application.data;

/**
 * Used to store the identifier of the description used in SysON.
 *
 * @author gdaniel
 */
public final class SysONRepresentationDescriptionIdentifiers {

    // All the general view diagrams have the same description
    public static final String GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID = "siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089";

    public static final String INTERCONNECTION_VIEW_DIAGRAM_DESCRIPTION_ID = "siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=e1bd3b6d-357b-3068-b2e9-e0c1e19d6856";

    private SysONRepresentationDescriptionIdentifiers() {
        // Prevent instantiation
    }
}
