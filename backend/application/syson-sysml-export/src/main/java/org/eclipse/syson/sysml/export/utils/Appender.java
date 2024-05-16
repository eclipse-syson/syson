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

import org.eclipse.syson.sysml.helper.NameHelper;

/**
 * Object that concatenate string using some custom convention.
 * 
 * @author Arthur Daussy
 */
public class Appender {

    private final StringBuilder builder = new StringBuilder();

    private final String indentation;

    private final String newLine;

    public Appender(String newLine, String indentation) {
        super();
        this.newLine = newLine;
        this.indentation = indentation;
    }
    
    public static String toPrintableName(String initialName) {
        return NameHelper.toPrintableName(initialName);
    }

    public Appender appendPrintableName(String name) {
        append(toPrintableName(name));
        return this;
    }

    public Appender appendSpaceIfNeeded() {
        if (!builder.isEmpty() && !endWithSpace()) {
            append(" ");
        }
        return this;
    }

    public Appender newLine() {
        return append(newLine);
    }

    public Appender indent() {
        append(indentation);
        return this;
    }

    public Appender appendIndentedContent(String content) {
        String indentedContent = indent(content);
        return append(indentedContent);
    }

    private String indent(String content) {
        return content.replaceAll(newLine, newLine + indentation);
    }

    private boolean endWithSpace() {
        char charAt = builder.charAt(builder.length() - 1);
        return charAt == ' ' || charAt == '\t';
    }

    public Appender append(String str) {
        if (str != null) {
            builder.append(str);
        }
        return this;
    }

    

    public String getNewLine() {
        return newLine;
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
