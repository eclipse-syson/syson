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
package org.eclipse.syson.sysml.textual.utils;

import java.util.Collection;
import java.util.List;

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

    private final Collection<Character> symbols = List.of('[', '(', '.', '@');

    public Appender(String newLine, String indentation) {
        super();
        this.newLine = newLine;
        this.indentation = indentation;
    }

    public boolean isEmpty() {
        return this.builder.isEmpty() || this.builder.toString().isBlank();
    }

    public static String toPrintableName(String initialName) {
        return NameHelper.toPrintableName(initialName, true);
    }

    public Appender appendPrintableName(String name) {
        this.append(toPrintableName(name));
        return this;
    }

    public Appender appendWithSpaceIfNeeded(String content) {
        return this.appendSpaceIfNeeded().append(content);
    }

    public Appender appendSpaceIfNeeded() {
        if (!this.builder.isEmpty() && !this.endWithSpace() && !this.endWithSymbol()) {
            this.append(" ");
        }
        return this;
    }

    public Appender newLine() {
        return this.append(this.newLine);
    }

    public Appender indent() {
        this.append(this.indentation);
        return this;
    }

    public Appender appendIndentedContent(String content) {
        String indentedContent = this.indent(content);
        return this.append(indentedContent);
    }

    private String indent(String content) {
        if (content == null) {
            return "";
        }
        return content.replaceAll(this.newLine, this.newLine + this.indentation);
    }

    private boolean endWithSpace() {
        char charAt = this.builder.charAt(this.builder.length() - 1);
        return charAt == ' ' || charAt == '\t';
    }

    private boolean endWithSymbol() {
        char charAt = this.builder.charAt(this.builder.length() - 1);
        return this.symbols.contains(charAt);
    }

    public Appender append(String str) {
        if (str != null) {
            this.builder.append(str);
        }
        return this;
    }

    public String getNewLine() {
        return this.newLine;
    }

    @Override
    public String toString() {
        return this.builder.toString();
    }
}
