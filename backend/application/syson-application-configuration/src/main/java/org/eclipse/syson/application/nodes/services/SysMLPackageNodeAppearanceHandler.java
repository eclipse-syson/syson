/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.application.nodes.services;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.sirius.components.diagrams.INodeStyle;
import org.eclipse.sirius.components.diagrams.LineStyle;
import org.eclipse.sirius.components.diagrams.components.NodeAppearance;
import org.eclipse.sirius.components.diagrams.events.appearance.INodeAppearanceChange;
import org.eclipse.sirius.components.diagrams.events.appearance.NodeBackgroundAppearanceChange;
import org.eclipse.sirius.components.diagrams.events.appearance.NodeBorderColorAppearanceChange;
import org.eclipse.sirius.components.diagrams.events.appearance.NodeBorderSizeAppearanceChange;
import org.eclipse.sirius.components.diagrams.events.appearance.NodeBorderStyleAppearanceChange;
import org.eclipse.sirius.components.diagrams.events.appearance.ResetNodeAppearanceChange;
import org.eclipse.sirius.components.diagrams.renderer.INodeAppearanceHandler;
import org.eclipse.syson.application.nodes.SysMLPackageNodeStyle;
import org.springframework.stereotype.Service;

/**
 * Service used to handle the customization of a SysMLPackage node's appearance.
 *
 * @author arichard
 */
@Service
public class SysMLPackageNodeAppearanceHandler implements INodeAppearanceHandler {

    public static final String BACKGROUND = "BACKGROUND";

    public static final String BORDER_COLOR = "BORDER_COLOR";

    public static final String BORDER_SIZE = "BORDER_SIZE";

    public static final String BORDER_STYLE = "BORDER_STYLE";

    @Override
    public boolean canHandle(INodeStyle nodeStyle) {
        return nodeStyle instanceof SysMLPackageNodeStyle;
    }

    @Override
    public NodeAppearance handle(INodeStyle providedStyle, List<INodeAppearanceChange> changes, Optional<NodeAppearance> previousNodeAppearance) {
        Set<String> customizedStyleProperties = previousNodeAppearance.map(NodeAppearance::customizedStyleProperties).orElse(new LinkedHashSet<>());
        Optional<INodeStyle> optionalPreviousNodeStyle = previousNodeAppearance.map(NodeAppearance::style);
        if (providedStyle instanceof SysMLPackageNodeStyle providedSysMLPackageNodeStyle) {
            SysMLPackageNodeStyle.Builder styleBuilder = SysMLPackageNodeStyle.newSysMLPackageNodeStyle(providedSysMLPackageNodeStyle);
            this.handleBackground(styleBuilder, changes, optionalPreviousNodeStyle, customizedStyleProperties);
            this.handleBorderColor(styleBuilder, changes, optionalPreviousNodeStyle, customizedStyleProperties);
            this.handleBorderSize(styleBuilder, changes, optionalPreviousNodeStyle, customizedStyleProperties);
            this.handleBorderStyle(styleBuilder, changes, optionalPreviousNodeStyle, customizedStyleProperties);
            return new NodeAppearance(styleBuilder.build(), customizedStyleProperties);
        } else {
            return new NodeAppearance(providedStyle, customizedStyleProperties);
        }
    }

    private void handleBackground(SysMLPackageNodeStyle.Builder styleBuilder, List<INodeAppearanceChange> changes, Optional<INodeStyle> optionalPreviousNodeStyle,
            Set<String> customizedStyleProperties) {
        if (!this.handleResetChange(BACKGROUND, changes, customizedStyleProperties)) {
            Optional<NodeBackgroundAppearanceChange> optionalBackgroundChange = changes.stream()
                    .filter(NodeBackgroundAppearanceChange.class::isInstance)
                    .map(NodeBackgroundAppearanceChange.class::cast)
                    .findFirst();

            if (optionalBackgroundChange.isPresent()) {
                String newBackground = optionalBackgroundChange.get().background();
                styleBuilder.background(newBackground);
                customizedStyleProperties.add(BACKGROUND);
            } else if (customizedStyleProperties.contains(BACKGROUND) && optionalPreviousNodeStyle.isPresent() && optionalPreviousNodeStyle.get() instanceof SysMLPackageNodeStyle previousNodeStyle) {
                String previousBackground = previousNodeStyle.getBackground();
                styleBuilder.background(previousBackground);
            } else {
                customizedStyleProperties.remove(BACKGROUND);
            }
        }
    }

    private void handleBorderColor(SysMLPackageNodeStyle.Builder styleBuilder, List<INodeAppearanceChange> changes, Optional<INodeStyle> optionalPreviousNodeStyle,
            Set<String> customizedStyleProperties) {
        if (!this.handleResetChange(BORDER_COLOR, changes, customizedStyleProperties)) {
            Optional<NodeBorderColorAppearanceChange> optionalBorderColorChange = changes.stream()
                    .filter(NodeBorderColorAppearanceChange.class::isInstance)
                    .map(NodeBorderColorAppearanceChange.class::cast)
                    .findFirst();

            if (optionalBorderColorChange.isPresent()) {
                String newBorderColor = optionalBorderColorChange.get().borderColor();
                styleBuilder.borderColor(newBorderColor);
                customizedStyleProperties.add(BORDER_COLOR);
            } else if (customizedStyleProperties.contains(BORDER_COLOR) && optionalPreviousNodeStyle.isPresent()
                    && optionalPreviousNodeStyle.get() instanceof SysMLPackageNodeStyle previousNodeStyle) {
                String previousBorderColor = previousNodeStyle.getBorderColor();
                styleBuilder.borderColor(previousBorderColor);
            } else {
                customizedStyleProperties.remove(BORDER_COLOR);
            }
        }
    }

    private void handleBorderSize(SysMLPackageNodeStyle.Builder styleBuilder, List<INodeAppearanceChange> changes, Optional<INodeStyle> optionalPreviousNodeStyle,
            Set<String> customizedStyleProperties) {
        if (!this.handleResetChange(BORDER_SIZE, changes, customizedStyleProperties)) {
            Optional<NodeBorderSizeAppearanceChange> optionalBorderSizeChange = changes.stream()
                    .filter(NodeBorderSizeAppearanceChange.class::isInstance)
                    .map(NodeBorderSizeAppearanceChange.class::cast)
                    .findFirst();

            if (optionalBorderSizeChange.isPresent()) {
                int newBorderSize = optionalBorderSizeChange.get().borderSize();
                styleBuilder.borderSize(newBorderSize);
                customizedStyleProperties.add(BORDER_SIZE);
            } else if (customizedStyleProperties.contains(BORDER_SIZE) && optionalPreviousNodeStyle.isPresent() && optionalPreviousNodeStyle.get() instanceof SysMLPackageNodeStyle previousNodeStyle) {
                int previousBorderSize = previousNodeStyle.getBorderSize();
                styleBuilder.borderSize(previousBorderSize);
            } else {
                customizedStyleProperties.remove(BORDER_SIZE);
            }
        }
    }

    private void handleBorderStyle(SysMLPackageNodeStyle.Builder styleBuilder, List<INodeAppearanceChange> changes, Optional<INodeStyle> optionalPreviousNodeStyle,
            Set<String> customizedStyleProperties) {
        if (!this.handleResetChange(BORDER_STYLE, changes, customizedStyleProperties)) {
            Optional<NodeBorderStyleAppearanceChange> optionalBorderStyleChange = changes.stream()
                    .filter(NodeBorderStyleAppearanceChange.class::isInstance)
                    .map(NodeBorderStyleAppearanceChange.class::cast)
                    .findFirst();

            if (optionalBorderStyleChange.isPresent()) {
                LineStyle newBorderStyle = optionalBorderStyleChange.get().borderStyle();
                styleBuilder.borderStyle(newBorderStyle);
                customizedStyleProperties.add(BORDER_STYLE);
            } else if (customizedStyleProperties.contains(BORDER_STYLE) && optionalPreviousNodeStyle.isPresent()
                    && optionalPreviousNodeStyle.get() instanceof SysMLPackageNodeStyle previousNodeStyle) {
                LineStyle previousBorderStyle = previousNodeStyle.getBorderStyle();
                styleBuilder.borderStyle(previousBorderStyle);
            } else {
                customizedStyleProperties.remove(BORDER_STYLE);
            }
        }
    }

    private boolean handleResetChange(String propertyName, List<INodeAppearanceChange> changes, Set<String> customizedStyleProperties) {
        boolean resetChange = changes.stream()
                .filter(ResetNodeAppearanceChange.class::isInstance)
                .map(ResetNodeAppearanceChange.class::cast)
                .anyMatch(reset -> Objects.equals(reset.propertyName(), propertyName));

        if (resetChange) {
            customizedStyleProperties.remove(propertyName);
        }
        return resetChange;
    }
}
