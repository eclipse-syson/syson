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
package org.eclipse.syson.standard.diagrams.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.collaborative.api.RepresentationDescriptionMetadata;
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
    public List<RepresentationDescriptionMetadata> sort(List<RepresentationDescriptionMetadata> representationDescriptions) {
        List<RepresentationDescriptionMetadata> sortedRepresentationDescriptions = new ArrayList<>();
        for (RepresentationDescriptionMetadata representationDescriptionMetadata : representationDescriptions) {
            if (SDVDiagramDescriptionProvider.DESCRIPTION_NAME.equals(representationDescriptionMetadata.getLabel())) {
                sortedRepresentationDescriptions.add(0, representationDescriptionMetadata);
            } else {
                sortedRepresentationDescriptions.add(representationDescriptionMetadata);
            }
        }
        return sortedRepresentationDescriptions;
    }
}
