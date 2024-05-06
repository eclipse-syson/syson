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

import org.apache.commons.text.StringEscapeUtils;

/**
 * Set of static method to helm to manipulate / escape name / qualified name of sysml elements
 * 
 * @author Guillaume Escande
 */
public class NameHelper {

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
}
