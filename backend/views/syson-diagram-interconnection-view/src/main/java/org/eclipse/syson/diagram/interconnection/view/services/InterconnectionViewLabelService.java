/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
package org.eclipse.syson.diagram.interconnection.view.services;

import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.diagram.common.view.services.ShowDiagramsIconsService;
import org.eclipse.syson.diagram.common.view.services.ViewLabelService;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewDiagramDescriptionProvider;

/**
 * Label-related Java services used by the {@link InterconnectionViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class InterconnectionViewLabelService extends ViewLabelService {

    public InterconnectionViewLabelService(IFeedbackMessageService feedbackMessageService, ShowDiagramsIconsService showDiagramsIconsService) {
        super(feedbackMessageService, showDiagramsIconsService);
    }

}
