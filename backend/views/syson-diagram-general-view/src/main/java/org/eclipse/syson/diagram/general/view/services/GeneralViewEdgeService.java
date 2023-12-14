/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.diagram.general.view.services;

import java.util.Objects;

import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;

/**
 * Edge-related Java services used by the {@link GeneralViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class GeneralViewEdgeService {

    private final IFeedbackMessageService feedbackMessageService;

    public GeneralViewEdgeService(IFeedbackMessageService feedbackMessageService) {
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
    }

    public boolean checkFeatureTypingEdgeReconnectionTarget(Element usage, Element newTarget) {
        boolean validTarget = false;
        if (usage instanceof AttributeUsage) {
            validTarget = newTarget instanceof AttributeDefinition;
        } else if (usage instanceof PortUsage) {
            validTarget = newTarget instanceof PortDefinition;
        } else if (usage instanceof InterfaceUsage) {
            validTarget = newTarget instanceof InterfaceDefinition;
        } else if (usage instanceof PartUsage) {
            validTarget = newTarget instanceof PartDefinition;
        } else if (usage instanceof ItemUsage) {
            validTarget = newTarget instanceof ItemDefinition;
        }
        if (!validTarget) {
            this.feedbackMessageService.addFeedbackMessage(new Message("The type of a " + usage.eClass().getName() + " cannot be a " + newTarget.eClass().getName(), MessageLevel.WARNING));
        }
        return validTarget;
    }
}
