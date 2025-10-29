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
package org.eclipse.syson.diagram.common.view.services;

import java.text.MessageFormat;
import java.util.Objects;

import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.services.SimpleNameDeresolver;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.textual.SysMLElementSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Label-related Java services used in diagrams.
 *
 * @author arichard
 */
public class ViewLabelService {

    private final Logger logger = LoggerFactory.getLogger(ViewLabelService.class);

    private final ShowDiagramsIconsService showDiagramsIconsService;

    private final IFeedbackMessageService feedbackMessageService;

    public ViewLabelService(IFeedbackMessageService feedbackMessageService, ShowDiagramsIconsService showDiagramsIconsService) {
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.showDiagramsIconsService = Objects.requireNonNull(showDiagramsIconsService);
    }

    /**
     * Get the value of the showIcon property of the ShowDiagramsIconsService.
     *
     * @param object
     *            The current object.
     * @return the value of the showIcon property of the ShowDiagramsIconsService.
     */
    public boolean showIcon(Object object) {
        return this.showDiagramsIconsService.getShowIcons();
    }

    /**
     * Send a feedback message to the user with a textual representation of an element.
     *
     * @param msg
     *            using the {@link MessageFormat} convention to inject the textual representation of the given object
     *            (that is to say "{0}")
     * @param level
     *            the message level
     * @param element
     *            the element to convert to a textual format
     * @return the element itself
     */
    public Element sendMessageWithTextualRepresentation(String msg, String level, Element element) {
        var serializer = new SysMLElementSerializer("\n", " ", new SimpleNameDeresolver(), s -> {
            this.logger.info(s.message());
        });
        this.feedbackMessageService.addFeedbackMessage(new Message(MessageFormat.format(msg, serializer.doSwitch(element)), MessageLevel.valueOf(level)));
        return element;
    }
}
