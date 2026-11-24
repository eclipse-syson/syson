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
package org.eclipse.syson.sysml.serializer.output;

import java.util.Objects;

import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;

/**
 * Immutable formatting policy for emitted SysML text.
 *
 * @param lineSeparator
 *            the text written for each line break
 * @param indentationUnit
 *            the text repeated once per block depth at the start of a line
 * @param tokenSeparator
 *            the text written between inline tokens when a space was requested
 */
public record FormattingOptions(String lineSeparator, String indentationUnit, String tokenSeparator) {

    /** Rejects missing separators: every formatting choice is required. */
    public FormattingOptions {
        lineSeparator = Objects.requireNonNull(lineSeparator);
        indentationUnit = Objects.requireNonNull(indentationUnit);
        tokenSeparator = Objects.requireNonNull(tokenSeparator);
    }

    /**
     * Creates options that separate inline tokens with a single space.
     *
     * @param lineSeparator
     *            the text written for each line break
     * @param indentationUnit
     *            the text repeated once per block depth
     */
    public FormattingOptions(final String lineSeparator, final String indentationUnit) {
        this(lineSeparator, indentationUnit, SysMLTokens.SPACE);
    }

    /**
     * Returns the platform line separator with tab indentation and single-space token separation.
     *
     * @return the default formatting options
     */
    public static FormattingOptions defaults() {
        return new FormattingOptions(System.lineSeparator(), SysMLTokens.TAB);
    }
}
