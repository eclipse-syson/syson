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
package org.eclipse.syson.sysml.helper;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;

/**
 * Set of static method to helm to manipulate / escape name / qualified name of sysml elements
 *
 * @author Guillaume Escande
 */
public class NameHelper {

    private static java.util.regex.Pattern VALID_CHARS = Pattern.compile("[^a-zA-Z0-9_]");

    /**
     * Constructor
     */
    private NameHelper() {
    }

    /**
     * Parse a qualified name to extract list of sections
     *
     * @param qualifiedName
     *            qualifed name to split
     * @return
     */
    public static List<String> parseQualifiedName(String qualifiedName) {
        return Arrays.asList(qualifiedName.split("::"));
    }

    /**
     * Escape a string
     *
     * @param str
     *            string to escape
     * @return
     */
    public static String escapeString(String str) {
        return StringEscapeUtils.escapeJava(str);
    }

    /**
     * Unescpae a string
     *
     * @param str
     *            string to unescape
     * @return
     */
    public static String unescapeString(String str) {
        return StringEscapeUtils.unescapeJava(str);
    }

    /**
     * Set a String printable for Sysml name
     *
     * @param initialName
     *            string to set printable
     * @return
     */
    public static String toPrintableName(String initialName) {
        String name;
        if (initialName == null || initialName.isEmpty()) {
            name = "";
        } else if (VALID_CHARS.matcher(initialName).find() || !isLetterOrUnderscore(initialName.charAt(0))) {
            name = '\'' + initialName + '\'';
        } else {
            name = initialName;
        }
        return name;
    }

    private static boolean isLetterOrUnderscore(char c) {
        return c == '_' || isLowerCaseLetter(c) || isUpperCaseLetter(c);
    }

    private static boolean isUpperCaseLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

    private static boolean isLowerCaseLetter(char c) {
        return c >= 'A' && c <= 'Z';
    }

}
