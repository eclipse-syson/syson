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
package org.eclipse.syson.sysml.textual;

import java.util.Objects;

import org.eclipse.syson.sysml.textual.utils.INameDeresolver;

/**
 * Option used in the SysmlSerializer.
 *
 * @param lineSeparator
 *         the string used to separate line
 * @param indentation
 *         the string used to indent the file
 * @param nameDeresolver
 *         a {@link INameDeresolver}
 * @param needEscapeCharacter
 *         (Optional) holds true if forbidden character in the textual format needs to be escaped
 */
public record SysMLSerializingOptions(String lineSeparator, String indentation, INameDeresolver nameDeresolver, boolean needEscapeCharacter) {

    public SysMLSerializingOptions {
        Objects.requireNonNull(lineSeparator, "property :lineSeparator is required");
        Objects.requireNonNull(indentation, "property :indentation is required");
        Objects.requireNonNull(nameDeresolver, "property :nameDeresolver is required");
    }

    /**
     * Builder for {@link SysMLSerializingOptions}.
     *
     * @author Arthur Daussy
     */
    public static final class Builder {

        private String lineSeparator;

        private String indentation;

        private INameDeresolver nameDeresolver;

        private boolean needEscapeCharacter;

        public Builder() {
        }

        public Builder lineSeparator(String aLineSeparator) {
            this.lineSeparator = Objects.requireNonNull(aLineSeparator, "Null aLineSeparator");
            return this;
        }

        public Builder indentation(String anIndentation) {
            this.indentation = Objects.requireNonNull(anIndentation, "Null anIndentation");
            return this;
        }

        public Builder nameDeresolver(INameDeresolver aNameDeresolver) {
            this.nameDeresolver = Objects.requireNonNull(aNameDeresolver, "Null aNameDeresolver");
            return this;
        }


        public Builder needEscapeCharacter(boolean isNeedEscapeCharacter) {
            this.needEscapeCharacter = isNeedEscapeCharacter;
            return this;
        }

        public SysMLSerializingOptions build() {
            return new SysMLSerializingOptions(this.lineSeparator, this.indentation, this.nameDeresolver, this.needEscapeCharacter);
        }
    }
}
