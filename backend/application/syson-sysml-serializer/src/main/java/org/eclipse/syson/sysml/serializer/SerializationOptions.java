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
package org.eclipse.syson.sysml.serializer;

import java.util.Objects;

import org.eclipse.syson.sysml.serializer.output.FormattingOptions;

/**
 * Immutable configuration for one serialization request.
 *
 * @param mode
 *            required syntax-preservation policy
 * @param formatting
 *            required output-formatting options
 * @param parserEntryPoint
 *            parser rule for standalone fragments; blank (or {@code null}, normalized to blank) for ordinary model
 *            serialization
 * @param implicitReferenceKeywordElision
 *            whether to omit reference keywords and anonymous reference syntax implied by their context; enabled by
 *            {@link #defaults()}
 */
public record SerializationOptions(SerializationMode mode, FormattingOptions formatting, String parserEntryPoint,
        boolean implicitReferenceKeywordElision) {

    /**
     * Validates the required components and normalizes a {@code null} entry point to blank, so renderers can compare
     * it without null checks.
     */
    public SerializationOptions {
        mode = Objects.requireNonNull(mode);
        formatting = Objects.requireNonNull(formatting);
        parserEntryPoint = Objects.requireNonNullElse(parserEntryPoint, "");
    }

    /**
     * Returns the default serializer options used by the public facade.
     *
     * @return normalized options using platform line separators, tab indentation, and omission of implicit
     *         reference keywords
     */
    public static SerializationOptions defaults() {
        return new SerializationOptions(SerializationMode.NORMALIZED, FormattingOptions.defaults(), "", true);
    }

    /**
     * Returns a copy using another serialization mode.
     *
     * @param newMode
     *            the serialization mode
     * @return a new option set
     */
    public SerializationOptions withMode(final SerializationMode newMode) {
        return new SerializationOptions(newMode, this.formatting, this.parserEntryPoint,
                this.implicitReferenceKeywordElision);
    }

    /**
     * Returns a copy carrying the parser entry point for a standalone parser-rule fragment.
     *
     * <p>
     * Some parser rules intentionally map several keywords to the same SysON metaclass. The entry point is therefore
     * required only when serializing such a fragment back to that same rule; complete models leave it blank.
     * </p>
     *
     * @param entryPoint
     *            the parser entry point, or blank for ordinary model serialization
     * @return a new option set
     */
    public SerializationOptions withParserEntryPoint(final String entryPoint) {
        return new SerializationOptions(this.mode, this.formatting, entryPoint, this.implicitReferenceKeywordElision);
    }

    /**
     * Returns a copy controlling omission of reference keywords that are implied by their context.
     *
     * @param elision
     *            whether implicit reference keywords should be omitted
     * @return a new option set
     */
    public SerializationOptions withImplicitReferenceKeywordElision(final boolean elision) {
        return new SerializationOptions(this.mode, this.formatting, this.parserEntryPoint, elision);
    }
}
