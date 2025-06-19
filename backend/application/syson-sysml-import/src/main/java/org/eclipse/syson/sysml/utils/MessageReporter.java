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
package org.eclipse.syson.sysml.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;

/**
 * Class that collects messages.
 *
 * @author Arthur Daussy
 */
public class MessageReporter {

    private final List<Message> reportedMessages = new ArrayList<>();

    public void error(String msg) {
        Message message = new Message(msg, MessageLevel.ERROR);
        this.reportedMessages.add(message);
    }

    public void info(String msg) {
        Message message = new Message(msg, MessageLevel.INFO);
        this.reportedMessages.add(message);
    }

    public void warning(String msg) {
        Message message = new Message(msg, MessageLevel.WARNING);
        this.reportedMessages.add(message);
    }

    public List<Message> getReportedMessages() {
        return this.reportedMessages;
    }
}
