/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.sysml.serializer.tokens;

import java.util.ArrayList;
import java.util.List;

/**
 * Locates reference separators without splitting unrestricted names or their escape sequences.
 * <p>
 * An unrestricted name such as {@code 'a::b'} may contain separator characters, so qualified references cannot be
 * split with plain string search.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ReferenceNameSyntax {

    private ReferenceNameSyntax() {
    }

    /**
     * Finds the next separator outside unrestricted names. {@code start} must be a segment boundary (not inside a
     * quoted name), since quoting state is tracked from there.
     *
     * @return the separator index, or {@code -1} when there is none
     */
    public static int separatorOutsideQuotes(final String name, final String separator, final int start) {
        boolean quoted = false;
        int index = start;
        while (index < name.length()) {
            final char character = name.charAt(index);
            if (quoted && character == '\\') {
                index++;
            } else if (character == '\'') {
                quoted = !quoted;
            } else if (!quoted && name.startsWith(separator, index)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Splits a reference at every separator outside unrestricted names, so {@code A::'B::C'} gives {@code A} and
     * {@code 'B::C'}. A name without separator gives a single segment; empty segments are kept.
     *
     * @return the segments, in text order
     */
    public static List<String> splitOutsideQuotes(final String name, final String separator) {
        final List<String> segments = new ArrayList<>();
        int start = 0;
        int next = separatorOutsideQuotes(name, separator, start);
        while (next >= 0) {
            segments.add(name.substring(start, next));
            start = next + separator.length();
            next = separatorOutsideQuotes(name, separator, start);
        }
        segments.add(name.substring(start));
        return segments;
    }

    /**
     * Finds the last separator outside unrestricted names, to split a qualified reference into its owner path and
     * simple name.
     *
     * @return the separator index, or {@code -1} for a simple name
     */
    public static int lastSeparatorOutsideQuotes(final String name, final String separator) {
        int last = -1;
        int next = separatorOutsideQuotes(name, separator, 0);
        while (next >= 0) {
            last = next;
            next = separatorOutsideQuotes(name, separator, next + separator.length());
        }
        return last;
    }

    /**
     * Removes the conjugation prefix of a conjugated reference ({@code conjugatedQualifiedName}), so that
     * <code>&#126;P::Pt</code> is compared as the name of the port definition it conjugates.
     *
     * @param name
     *            the reference spelling
     * @return the spelling without its leading <code>&#126;</code>
     */
    public static String withoutConjugation(final String name) {
        String withoutPrefix = name;
        if (name.startsWith(SysMLTokens.CONJUGATION_OPERATOR)) {
            withoutPrefix = name.substring(SysMLTokens.CONJUGATION_OPERATOR.length());
        }
        return withoutPrefix;
    }
}
