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
package org.eclipse.syson.util;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Tooling around AQL expressions.
 *
 * @author Jerome Gout
 */
public class AQLUtils {

    /**
     * Return an AQL string from the given string.
     *
     * @param string
     *            the string to transform to an AQL string
     * @return the AQL string built upon the given string.
     */
    public static String aqlString(String string) {
        return '\'' + string + '\'';
    }

    /**
     * Returns an AQL sequence from the given members.
     *
     * @param members
 *                The members to transform into a sequence
     * @return the AQL sequence made from the members
     */
    public static String aqlSequence(List<String> members) {
        return members.stream().collect(Collectors.joining(",", "Sequence{", "}"));
    }

}
