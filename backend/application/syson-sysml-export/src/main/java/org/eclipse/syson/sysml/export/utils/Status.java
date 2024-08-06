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
package org.eclipse.syson.sysml.export.utils;

import java.text.MessageFormat;

import org.slf4j.Logger;

/**
 * Report status.
 *
 * @author Arthur Daussy
 */
public record Status(Severity severity, String message) {

    public static Status warning(String message, Object... arguments) {
        return new Status(Severity.WARNING, MessageFormat.format(message, arguments));
    }

    public static Status error(String message, Object... arguments) {
        return new Status(Severity.ERROR, MessageFormat.format(message, arguments));
    }

    public static Status info(String message, Object... arguments) {
        return new Status(Severity.INFO, MessageFormat.format(message, arguments));
    }

    public static Status debug(String message, Object... arguments) {
        return new Status(Severity.DEBUG, MessageFormat.format(message, arguments));
    }

    public void log(Logger logger) {
        switch (this.severity) {
            case DEBUG -> logger.debug(this.message);
            case INFO -> logger.info(this.message);
            case WARNING -> logger.warn(this.message);
            case ERROR -> logger.error(this.message);
            default -> logger.error(this.message);
        }
    }
}
