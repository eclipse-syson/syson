/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.sysml.metamodel.helper;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.syson.sysml.metamodel.services.textual.SysMLv2Keywords;

/**
 * Set of methods to help to manipulate/escape name/qualified name of sysml elements.
 *
 * @author Guillaume Escande
 */
public final class NameHelper {

    private static final java.util.regex.Pattern VALID_CHARS = Pattern.compile("[^a-zA-Z0-9_]");

    /**
     * Parse a qualified name to extract list of sections.
     *
     * @param qualifiedName
     *            qualified name to split
     * @return
     */
    public List<String> parseQualifiedName(String qualifiedName) {
        return Arrays.asList(qualifiedName.split("::"));
    }

    /**
     * Escape a string.
     *
     * @param str
     *            string to escape
     * @return
     */
    public String escapeString(String str) {
        return StringEscapeUtils.escapeJava(str);
    }

    /**
     * Unescape a string.
     *
     * @param str
     *            string to unescape
     * @return
     */
    public String unescapeString(String str) {
        if (str != null && str.length() > 1 && str.startsWith("'") && str.endsWith("'")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    /**
     * Set a String printable for SysMLv2 name.
     *
     * @param initialName
     *            string to set printable
     * @return a String printable for SysMLv2 name
     */
    public String toPrintableName(String initialName) {
        return this.toPrintableName(initialName, false);
    }

    /**
     * Set a String printable for SysMLv2 name.
     *
     * @param initialName
     *            string to set printable
     * @param escapeSingleQuotes
     *            whether the single quotes should be escaped or not inside the printable value
     * @return a String printable for SysMLv2 name
     */
    public String toPrintableName(String initialName, boolean escapeSingleQuotes) {
        String name;
        if (initialName == null || initialName.isEmpty()) {
            name = "";
        } else if (VALID_CHARS.matcher(initialName).find() || !this.isLetterOrUnderscore(initialName.charAt(0)) || SysMLv2Keywords.KEYWORDS.contains(initialName.trim())) {
            if (escapeSingleQuotes) {
                name = '\'' + initialName.replaceAll("'", "\\\\'") + '\'';
            } else {
                name = '\'' + initialName + '\'';
            }
        } else {
            name = initialName;
        }
        return name;
    }

    private boolean isLetterOrUnderscore(char c) {
        return c == '_' || this.isLowerCaseLetter(c) || this.isUpperCaseLetter(c);
    }

    private boolean isUpperCaseLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

    private boolean isLowerCaseLetter(char c) {
        return c >= 'A' && c <= 'Z';
    }

}
