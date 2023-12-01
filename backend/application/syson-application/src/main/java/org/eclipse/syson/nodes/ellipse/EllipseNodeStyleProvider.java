/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.nodes.ellipse;

import java.util.Optional;

import org.eclipse.sirius.components.diagrams.INodeStyle;
import org.eclipse.sirius.components.diagrams.LineStyle;
import org.eclipse.sirius.components.view.FixedColor;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.emf.diagram.INodeStyleProvider;
import org.eclipse.sirius.web.customnodes.EllipseNodeStyleDescription;
import org.springframework.stereotype.Service;

/**
 * This class provides style information for the custom node ellipse.
 *
 * @author frouene
 */
@Service
public class EllipseNodeStyleProvider implements INodeStyleProvider {

    public static final String NODE_ELLIPSE = "customnode:ellipse";

    @Override
    public Optional<String> getNodeType(NodeStyleDescription nodeStyle) {
        if (nodeStyle instanceof EllipseNodeStyleDescription) {
            return Optional.of(NODE_ELLIPSE);
        }
        return Optional.empty();
    }

    @Override
    public Optional<INodeStyle> createNodeStyle(NodeStyleDescription nodeStyle, Optional<String> optionalEditingContextId) {
        Optional<INodeStyle> iNodeStyle = Optional.empty();
        Optional<String> nodeType = this.getNodeType(nodeStyle);
        if (nodeType.isPresent()) {
            return Optional.of(EllipseNodeStyle.newEllipseNodeStyle()
                    .color(Optional.ofNullable(nodeStyle.getColor())
                    .filter(FixedColor.class::isInstance)
                    .map(FixedColor.class::cast)
                    .map(FixedColor::getValue)
                    .orElse("transparent"))
                    .borderColor(Optional.ofNullable(nodeStyle.getBorderColor())
                    .filter(FixedColor.class::isInstance)
                    .map(FixedColor.class::cast)
                    .map(FixedColor::getValue)
                    .orElse("black"))
                    .borderSize(nodeStyle.getBorderSize())
                    .borderStyle(LineStyle.valueOf(nodeStyle.getBorderLineStyle().getLiteral()))
                    .build());
        }

        return iNodeStyle;
    }
}