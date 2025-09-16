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

import java.util.Optional;

import org.eclipse.sirius.components.diagrams.INodeStyle;
import org.eclipse.sirius.components.diagrams.events.appearance.IAppearanceChange;
import org.eclipse.sirius.components.diagrams.events.appearance.NodeBackgroundAppearanceChange;
import org.eclipse.sirius.components.diagrams.events.appearance.NodeBorderColorAppearanceChange;
import org.eclipse.sirius.components.diagrams.events.appearance.NodeBorderSizeAppearanceChange;
import org.eclipse.sirius.components.diagrams.events.appearance.NodeBorderStyleAppearanceChange;
import org.eclipse.sirius.web.application.project.services.api.IDiagramImporterNodeStyleAppearanceChangeHandler;
import org.eclipse.syson.application.nodes.SysMLNoteNodeStyle;
import org.springframework.stereotype.Service;

/**
 * Implementation to handle SysMLNote node style.
 *
 * @author arichard
 */
@Service
public class DiagramImporterSysMLNoteNodeStyleAppearanceChangeHandler implements IDiagramImporterNodeStyleAppearanceChangeHandler {

    @Override
    public boolean canHandle(INodeStyle nodeStyle) {
        return nodeStyle instanceof SysMLNoteNodeStyle;
    }

    @Override
    public Optional<IAppearanceChange> handle(String nodeId, INodeStyle nodeStyle, String customizedStyleProperty) {
        if (nodeStyle instanceof SysMLNoteNodeStyle customNodeStyle) {
            return switch (customizedStyleProperty) {
                case SysMLNoteNodeAppearanceHandler.BACKGROUND -> Optional.of(new NodeBackgroundAppearanceChange(nodeId, customNodeStyle.getBackground()));
                case SysMLNoteNodeAppearanceHandler.BORDER_COLOR -> Optional.of(new NodeBorderColorAppearanceChange(nodeId, customNodeStyle.getBorderColor()));
                case SysMLNoteNodeAppearanceHandler.BORDER_SIZE -> Optional.of(new NodeBorderSizeAppearanceChange(nodeId, customNodeStyle.getBorderSize()));
                case SysMLNoteNodeAppearanceHandler.BORDER_STYLE -> Optional.of(new NodeBorderStyleAppearanceChange(nodeId, customNodeStyle.getBorderStyle()));
                default -> Optional.empty();
            };
        }
        return Optional.empty();
    }
}
