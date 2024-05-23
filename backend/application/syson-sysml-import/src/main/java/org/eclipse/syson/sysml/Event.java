/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.sysml;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import org.eclipse.emf.ecore.EObject;

/**
 * Represents an event for the writing of the import report with a message ID, associated EObject, and parameters for
 * logging and processing.
 *
 * @author wldblm
 */
public class Event {
    private final String messageId;

    private final EObject eObject;

    private final Object[] params;

    public Event(final String messageId, final EObject eObject, final Object... params) {
        this.messageId = messageId;
        this.eObject = eObject;
        this.params = params;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public Object[] getParams() {
        return this.params;
    }

    public EObject getEObject() {
        return this.eObject;
    }

    @Override
    public boolean equals(final Object o) {
        boolean isEqual = false;

        if (this == o) {
            isEqual = true;
        } else if (o != null && this.getClass() == o.getClass()) {
            final Event event = (Event) o;
            isEqual = Objects.equals(this.messageId, event.messageId) && Objects.equals(this.eObject, event.eObject) && Arrays.equals(this.params, event.params);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(this.messageId, this.eObject);
        result = 31 * result + Arrays.hashCode(this.params);
        return result;
    }

    public String toLogMessage(final ResourceBundle messages) {
        final List<Object> finalParams = new ArrayList<>(Arrays.asList(this.params));
        final String messageTemplate = messages.getString(this.messageId);
        if (this.eObject instanceof final Element elt && elt.getQualifiedName() != null) {
            String qualifiedName = elt.getQualifiedName();
            while (qualifiedName.contains("::null")) {
                qualifiedName = qualifiedName.replace("::null", "");
            }
            finalParams.add(qualifiedName);
        }
        return "\n" + MessageFormat.format(messageTemplate, finalParams.toArray());
    }

}
