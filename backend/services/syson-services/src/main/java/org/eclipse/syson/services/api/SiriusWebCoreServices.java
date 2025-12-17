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
package org.eclipse.syson.services.api;

import java.util.Objects;

import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.core.api.IURLParser;
import org.springframework.stereotype.Service;

/**
 * Service allowing to access to the main Sirius Web CoreServices.
 *
 * @author arichard
 */
@Service
public record SiriusWebCoreServices(IObjectSearchService objectSearchService, IIdentityService identityService, IFeedbackMessageService feedbackMessageService,
                                    IEditingContextSearchService editingContextSearchService, IRepresentationDescriptionSearchService representationDescriptionSearchService, IURLParser urlParser) {

    public SiriusWebCoreServices {
        Objects.requireNonNull(objectSearchService);
        Objects.requireNonNull(identityService);
        Objects.requireNonNull(feedbackMessageService);
        Objects.requireNonNull(editingContextSearchService);
        Objects.requireNonNull(representationDescriptionSearchService);
        Objects.requireNonNull(urlParser);
    }
}
