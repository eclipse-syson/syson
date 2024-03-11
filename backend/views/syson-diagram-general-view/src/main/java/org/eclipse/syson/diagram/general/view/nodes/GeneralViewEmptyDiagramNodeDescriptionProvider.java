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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.List;

import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.syson.diagram.common.view.nodes.AbstractEmptyDiagramNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;

/**
 * Fake Node Description allowing to store other Node Descriptions that will be reused by other Node Descriptions.
 * Typical use is for compartment Nodes.
 *
 * @author arichard
 */
public class GeneralViewEmptyDiagramNodeDescriptionProvider extends AbstractEmptyDiagramNodeDescriptionProvider {

    public static final String NAME = "GV Node EmptyDiagram";

    public GeneralViewEmptyDiagramNodeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider, new GVDescriptionNameGenerator());
    }

    @Override
    protected String getName() {
        return NAME;
    }

    @Override
    protected List<ToolSectionDescription> getToolSections() {
        return GeneralViewDiagramDescriptionProvider.TOOL_SECTIONS;
    }

}
