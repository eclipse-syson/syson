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
package org.eclipse.syson.textual.view.description;

import java.util.List;

import org.eclipse.sirius.components.collaborative.api.IRepresentationDescriptionsProvider;
import org.eclipse.sirius.components.collaborative.dto.RepresentationDescriptionMetadataDTO;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.representations.IRepresentationDescription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Provide the textual representation description for a given object.
 *
 * @author gcoutable
 */
@Service
public class TextualViewRepresentationDescriptionsProvider implements IRepresentationDescriptionsProvider {

    private final boolean enabled;

    public TextualViewRepresentationDescriptionsProvider(@Value("${org.eclipse.syson.experimental.textual.representation.enabled:false}") boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean canHandle(IRepresentationDescription representationDescription) {
        return TextualViewDescription.ID.equals(representationDescription.getId()) && this.enabled;
    }

    @Override
    public List<RepresentationDescriptionMetadataDTO> handle(IEditingContext editingContext, Object object, IRepresentationDescription representationDescription) {
        return List.of(new RepresentationDescriptionMetadataDTO(representationDescription.getId(), representationDescription.getLabel(), representationDescription.getLabel(), ""));
    }
}
