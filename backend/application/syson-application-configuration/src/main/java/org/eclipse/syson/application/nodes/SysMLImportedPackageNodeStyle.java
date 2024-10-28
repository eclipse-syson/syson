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
package org.eclipse.syson.application.nodes;

import java.text.MessageFormat;
import java.util.Objects;

import org.eclipse.sirius.components.annotations.Immutable;
import org.eclipse.sirius.components.diagrams.INodeStyle;
import org.eclipse.sirius.components.diagrams.LineStyle;

/**
 * The SysML imported package node style. This code has been fully inspired from {@link SysMLPackageNodeStyle}.
 *
 * @author Jerome Gout
 */
@Immutable
public final class SysMLImportedPackageNodeStyle implements INodeStyle {

    private String background;

    private String borderColor;

    private int borderSize;

    private LineStyle borderStyle;

    private SysMLImportedPackageNodeStyle() {
        // Prevent instantiation
    }

    public static Builder newSysMLImportedPackageNodeStyle() {
        return new Builder();
    }

    public String getBackground() {
        return this.background;
    }

    public String getBorderColor() {
        return this.borderColor;
    }

    public int getBorderSize() {
        return this.borderSize;
    }

    public LineStyle getBorderStyle() {
        return this.borderStyle;
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'color: {1}, border: '{' background: {2}, size: {3}, style: {4} '}''}'";
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.background, this.borderColor, this.borderSize, this.borderStyle);
    }

    /**
     * The builder used to create the imported package node style.
     *
     * @author Jerome Gout
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {

        private String background;

        private String borderColor;

        private int borderSize;

        private LineStyle borderStyle;

        private Builder() {
            // Prevent instantiation
        }

        public Builder background(String background) {
            this.background = Objects.requireNonNull(background);
            return this;
        }

        public Builder borderColor(String borderColor) {
            this.borderColor = Objects.requireNonNull(borderColor);
            return this;
        }

        public Builder borderSize(int borderSize) {
            this.borderSize = borderSize;
            return this;
        }

        public Builder borderStyle(LineStyle borderStyle) {
            this.borderStyle = Objects.requireNonNull(borderStyle);
            return this;
        }

        public SysMLImportedPackageNodeStyle build() {
            SysMLImportedPackageNodeStyle nodeStyleDescription = new SysMLImportedPackageNodeStyle();
            nodeStyleDescription.background = Objects.requireNonNull(this.background);
            nodeStyleDescription.borderColor = Objects.requireNonNull(this.borderColor);
            nodeStyleDescription.borderSize = this.borderSize;
            nodeStyleDescription.borderStyle = Objects.requireNonNull(this.borderStyle);
            return nodeStyleDescription;
        }
    }
}
