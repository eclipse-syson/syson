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
package org.eclipse.syson.standard.diagrams.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.collaborative.dto.RepresentationDescriptionMetadataDTO;
import org.eclipse.sirius.components.emf.services.api.IRepresentationDescriptionMetadataSorter;
import org.springframework.stereotype.Service;

/**
 * Sort representation descriptions in SysON. This sorter is used when creating a new representation in the frontend.
 *
 * @author arichard
 */
@Service
public class SysONRepresentationDescriptionMetadataSorter implements IRepresentationDescriptionMetadataSorter {

    @Override
    public List<RepresentationDescriptionMetadataDTO> sort(List<RepresentationDescriptionMetadataDTO> representationDescriptions) {
        List<RepresentationDescriptionMetadataDTO> sortedRepresentationDescriptions = new ArrayList<>();
        for (RepresentationDescriptionMetadataDTO representationDescriptionMetadata : representationDescriptions) {
            if (SDVDiagramDescriptionProvider.DESCRIPTION_NAME.equals(representationDescriptionMetadata.label())) {
                sortedRepresentationDescriptions.add(0, representationDescriptionMetadata);
            } else {
                sortedRepresentationDescriptions.add(representationDescriptionMetadata);
            }
        }
        return sortedRepresentationDescriptions;
    }
}
