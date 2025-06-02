/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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

import java.util.Optional;

import org.eclipse.sirius.components.diagrams.ILayoutStrategy;
import org.eclipse.sirius.components.diagrams.INodeStyle;
import org.eclipse.sirius.components.diagrams.LineStyle;
import org.eclipse.sirius.components.view.FixedColor;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.emf.diagram.INodeStyleProvider;
import org.eclipse.syson.sysmlcustomnodes.SysMLImportedPackageNodeStyleDescription;
import org.springframework.stereotype.Service;

/**
 * This class provides style information for the imported package custom node.
 * This code has been fully inspired from {@link SysMLPackageNodeStyleProvider}.
 *
 * @author Jerome Gout
 */
@Service
public class SysMLImportedPackageNodeStyleProvider implements INodeStyleProvider {

    public static final String NODE_SYSML_IMPORTED_PACKAGE = "customnode:sysmlimportedpackage";

    @Override
    public Optional<String> getNodeType(NodeStyleDescription nodeStyle) {
        if (nodeStyle instanceof SysMLImportedPackageNodeStyleDescription) {
            return Optional.of(NODE_SYSML_IMPORTED_PACKAGE);
        }
        return Optional.empty();
    }

    @Override
    public Optional<INodeStyle> createNodeStyle(NodeStyleDescription nodeStyle, Optional<String> optionalEditingContextId, ILayoutStrategy childrenLayoutStrategy) {
        Optional<INodeStyle> iNodeStyle = Optional.empty();
        Optional<String> nodeType = this.getNodeType(nodeStyle);
        if (nodeType.isPresent()) {
            return Optional.of(SysMLImportedPackageNodeStyle.newSysMLImportedPackageNodeStyle()
                    .background(Optional.ofNullable(((SysMLImportedPackageNodeStyleDescription) nodeStyle).getBackground())
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
                    .childrenLayoutStrategy(childrenLayoutStrategy)
                    .build());
        }

        return iNodeStyle;
    }
}
